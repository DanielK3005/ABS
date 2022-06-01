package customerWindow.components.payment;

import adminWindow.AdminController;
import customerWindow.CustomerController;
import customerWindow.components.moneyaction.MoneyActionController;
import database.DataBase;
import dto.LoanDTO;
import dto.NotificationDTO;
import dto.PersonDTO;
import engine.EngineFunctions;
import exception.ExtraPaymentMoneyException;
import exception.NotEnoughMoneyException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import mainWindow.ABSController;
import org.controlsfx.control.Notifications;
import time.Yaz;
import transaction.Loan;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PaymentController {

    @FXML
    private TableView<NotificationDTO> NotificationTable;

    @FXML
    private TableColumn<NotificationDTO, String> NameColumn;

    @FXML
    private TableColumn<NotificationDTO, Integer> PayYazColumn;

    @FXML
    private TableColumn<NotificationDTO, Double> TotalPayColumn;

    @FXML
    private AnchorPane RootPane;

    @FXML
    private Button OnePaymentBtn;

    @FXML
    private Button PayAllBtn;

    @FXML
    private Label instractionLabel;

    TableView<LoanDTO> CustomerTable;

    private ABSController mainController;
    private CustomerController customerController;
    private MoneyActionController moneyActionController;
    private EngineFunctions Model;
    private DataBase dataDTO;
    private Yaz time;
    private PersonDTO SelectedPerson;

    public void setFirstEntry(){
        setLoanTable();
        setNotificationTable();
    }

    public void setLoanTable(){
        List<LoanDTO> CustomerList=this.dataDTO.getLoansDTO().stream()
                .filter(x->x.getOwner().getName().equalsIgnoreCase(SelectedPerson.getName()))
                .filter(x->x.getStatus()== Loan.Status.ACTIVE|| x.getStatus()== Loan.Status.RISK)
                .collect(Collectors.toList());
        instractionLabel.setVisible(true);
        CustomerTable=mainController.StatusTab("ACTIVE",CustomerList,RootPane);
        TablePrefSettings(CustomerTable);
    }

    public void setNotificationTable(){
        List<NotificationDTO> notifications=new ArrayList<>();
        List<LoanDTO> CustomerList=
                this.dataDTO.getLoansDTO().stream()
                        .filter(x->x.getStatus()==Loan.Status.ACTIVE || x.getStatus()==Loan.Status.RISK)
                        .filter(x->x.getOwner().getName().equalsIgnoreCase(SelectedPerson.getName()))
                        .collect(Collectors.toList());
        for(LoanDTO val: CustomerList)
            notifications.addAll(val.getNotifications());
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        PayYazColumn.setCellValueFactory(new PropertyValueFactory<>("paymentYaz"));
        TotalPayColumn.setCellValueFactory(new PropertyValueFactory<>("totalPay"));
        NotificationTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        NotificationTable.setItems(FXCollections.observableArrayList(notifications));
    }

    @FXML
    void OnePaymentListner(ActionEvent event) throws IOException {
        LoanDTO selected = CustomerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Notifications select = Notifications.create().title("Error").text("Please select loan from the table above that you wish to pay for").hideAfter(Duration.seconds(10)).position(Pos.TOP_LEFT);
            select.showError();
            return;
        }
        instractionLabel.setVisible(false);
        if (selected.getStatus() == Loan.Status.RISK) {
            Optional<ButtonType> clickedButton = customerController.setMoneyAction(CustomerController.ActionType.PAYMENT, selected.getId());
            if (clickedButton.get() == ButtonType.APPLY) {
                Label ErrorMessage = new Label();
                double amount = customerController.InputValidation(moneyActionController.getAmountTF(), ErrorMessage);
                if (amount == -1) {
                    Stage stage = mainController.MessageStage(AdminController.MessageType.Error, ErrorMessage.getText());
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.show();
                    return;
                }
                try {
                    this.Model.PaymentForRisk(selected, amount, time.getValue());
                } catch (ExtraPaymentMoneyException e) {
                    Notifications getBack = Notifications.create().title("Information").text(e.getMessage()).hideAfter(Duration.seconds(10)).position(Pos.TOP_LEFT);
                    getBack.showInformation();
                }
                dataDTO = this.Model.getDataBaseDTO();
                customerController.UpdateChanges(this.Model, this.dataDTO, CustomerController.ActionType.PAYMENT);
            }
        } else {
            try {
                this.Model.PaymentForActive(selected, time.getValue());
                dataDTO = this.Model.getDataBaseDTO();
                customerController.UpdateChanges(this.Model, this.dataDTO, CustomerController.ActionType.PAYMENT);
            } catch (NotEnoughMoneyException e) {
                Stage stage = mainController.MessageStage(AdminController.MessageType.Error, e.getMessage());
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            }
        }
    }

    @FXML
    void PayAllListner(ActionEvent event) throws IOException {
        LoanDTO payAll=CustomerTable.getSelectionModel().getSelectedItem();
        if (payAll == null) {
            Notifications select = Notifications.create().title("Error").text("Please select loan from the table above that you wish to pay for").hideAfter(Duration.seconds(10)).position(Pos.TOP_LEFT);
            select.showError();
            return;
        }
        instractionLabel.setVisible(false);
        try {
            this.Model.PayAll(payAll, time.getValue());
            this.dataDTO=this.Model.getDataBaseDTO();
            customerController.UpdateChanges(this.Model,this.dataDTO, CustomerController.ActionType.PAYMENT);
        } catch (NotEnoughMoneyException e) {
            Stage stage = mainController.MessageStage(AdminController.MessageType.Error, e.getMessage());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }
    }

    public void TablePrefSettings(TableView basic){
        basic.setPrefSize(960,450);
        basic.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        AnchorPane.setTopAnchor(basic,35.0);
        AnchorPane.setLeftAnchor(basic,0.0);
    }

    public void setMainController(ABSController mainController) {
        this.mainController = mainController;
    }

    public void setModel(EngineFunctions model) {
        Model = model;
    }

    public void setDataDTO(DataBase dataDTO) {
        this.dataDTO = dataDTO;
    }

    public void setSelectedPerson(PersonDTO selectedPerson) {
        SelectedPerson = selectedPerson;
    }

    public void setCustomerController(CustomerController customerController) {
        this.customerController = customerController;
    }

    public void setMoneyActionController(MoneyActionController moneyActionController) {
        this.moneyActionController = moneyActionController;
    }

    public void setTime(Yaz time) {
        this.time = time;
    }
}
