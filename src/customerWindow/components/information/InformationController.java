package customerWindow.components.information;

import adminWindow.AdminController;
import customerWindow.CustomerController;
import customerWindow.components.moneyaction.MoneyActionController;
import database.DataBase;
import dto.LenderDTO;
import dto.LoanDTO;
import dto.PersonDTO;
import engine.EngineFunctions;
import exception.MoreThanYouGotException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mainWindow.ABSController;
import time.Yaz;
import transaction.Loan;
import transaction.Transaction;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InformationController {

    @FXML
    private TableView<Transaction> TransactionTable;

    @FXML
    private TableColumn<Transaction, Integer> YazTimeColumn;

    @FXML
    private TableColumn<Transaction, Transaction.TransactionType> TransactionTypeColumn;

    @FXML
    private TableColumn<Transaction, Double> amountColumn;

    @FXML
    private TableColumn<Transaction, Double> amountBeforeColumn;

    @FXML
    private TableColumn<Transaction, Double> amountAfterColumn;

    @FXML
    private Button WithdrawBtn;

    @FXML
    private Button ChargeBtn;

    @FXML
    private AnchorPane NewTabPane;

    @FXML
    private AnchorPane PendingTabPane;

    @FXML
    private AnchorPane ActiveTabPane;

    @FXML
    private AnchorPane RiskTabPane;

    @FXML
    private AnchorPane FinishedTabPane;

    @FXML
    private TableView<LenderDTO> LendersTable;

    @FXML
    private TableColumn<LenderDTO, PersonDTO> NameColumn;
    @FXML
    private TableColumn<LenderDTO, PersonDTO> LendToColumn;

    @FXML
    private TableColumn<LenderDTO, Double> AmountColumn;

    @FXML
    private TableColumn<LenderDTO, Double> PayYazColumn;

    @FXML
    private TableColumn<LenderDTO, Double> DebtColumn;

    @FXML
    private TableColumn<LenderDTO, Double> totalPaybackColumn;

    @FXML
    private TableColumn<LenderDTO, Double> leftToPayColumn;

    @FXML
    private TableColumn<LenderDTO, Double> totalIntresColumn;

    @FXML
    private TableColumn<LenderDTO, Double> OwnershipColumn;

    private ABSController mainController;
    private CustomerController customerController;
    private MoneyActionController moneyActionController;
    private EngineFunctions Model;
    private DataBase dataDTO;
    private Yaz time;
    private PersonDTO PersonPicked;


    @FXML
    void IncomeListner(ActionEvent event) throws IOException {
        Optional<ButtonType> clickedButton=customerController.setMoneyAction(CustomerController.ActionType.INCOME,"");
        if(clickedButton.get()==ButtonType.APPLY){
            Label ErrorMessage=new Label();
            Integer amount=customerController.InputValidation(moneyActionController.getAmountTF(),ErrorMessage);
            if(amount==-1){
                Stage stage=mainController.MessageStage(AdminController.MessageType.Error,ErrorMessage.getText());
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
                return;
            }
            this.Model.InsertIncome(PersonPicked,amount, time.getValue());
            this.dataDTO=this.Model.getDataBaseDTO();
            customerController.UpdateChanges(this.Model,this.dataDTO, CustomerController.ActionType.INCOME);
        }
    }

    @FXML
    void WithdrawListner(ActionEvent event) throws IOException {
        Optional<ButtonType> clickedButton=customerController.setMoneyAction(CustomerController.ActionType.WITHDRAW,"");
        if(clickedButton.get()==ButtonType.APPLY){
            Label ErrorMessage=new Label();
            Integer amount=customerController.InputValidation(moneyActionController.getAmountTF(),ErrorMessage);
            if(amount==-1){
                Stage stage=mainController.MessageStage(AdminController.MessageType.Error,ErrorMessage.getText());
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
                return;
            }
            try {
                this.Model.MakeAwithdraw(PersonPicked,amount, time.getValue());
                this.dataDTO = this.Model.getDataBaseDTO();
                customerController.UpdateChanges(this.Model, this.dataDTO, CustomerController.ActionType.WITHDRAW);
            } catch (MoreThanYouGotException e) {
                Stage stage=mainController.MessageStage(AdminController.MessageType.Error,e.getMessage());
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
                return;
            }
        }
    }

    public void setFirstEntry(){
        setCustomerLoansTabs();
        setLendersTable();
        setCustomerTransactions();
    }

    public void setLendersTable(){
        if(PersonPicked==null)
            return;
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("owner"));
        LendToColumn.setCellValueFactory(new PropertyValueFactory<>("lendTo"));
        AmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        mainController.DoubleNiceFormat(AmountColumn);
        PayYazColumn.setCellValueFactory(new PropertyValueFactory<>("payEveryYazPeriod"));
        mainController.DoubleNiceFormat(PayYazColumn);
        DebtColumn.setCellValueFactory(new PropertyValueFactory<>("debt"));
        mainController.DoubleNiceFormat(DebtColumn);
        totalPaybackColumn.setCellValueFactory(new PropertyValueFactory<>("totalPayBack"));
        mainController.DoubleNiceFormat(totalPaybackColumn);
        leftToPayColumn.setCellValueFactory(new PropertyValueFactory<>("leftToPay"));
        mainController.DoubleNiceFormat(leftToPayColumn);
        totalIntresColumn.setCellValueFactory(new PropertyValueFactory<>("totalInterest"));
        mainController.DoubleNiceFormat(totalIntresColumn);
        OwnershipColumn.setCellValueFactory(new PropertyValueFactory<>("OwnershipPercent"));
        mainController.DoubleNiceFormat(OwnershipColumn);
        LendersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        LendersTable.setItems(FXCollections.observableArrayList(PersonPicked.getOwnLenders()));
    }

    public void setCustomerLoansTabs(){
        if(PersonPicked==null)
            return;
        List<LoanDTO> data=dataDTO.getLoansDTO().stream().filter(x->x.getOwner().getName().equalsIgnoreCase(PersonPicked.getName())).collect(Collectors.toList());
        List<LoanDTO> NewData=data.stream().filter(x->x.getStatus()== Loan.Status.NEW).collect(Collectors.toList());
        TableView<LoanDTO> NewTable=mainController.NewTab(NewData,NewTabPane);
        TableSettings(NewTable);
        List<LoanDTO> PendingData=data.stream().filter(x->x.getStatus()== Loan.Status.PENDING).collect(Collectors.toList());
        TableView<LoanDTO> PendingTable=mainController.PendingTab(PendingData,PendingTabPane);
        TableSettings(PendingTable);
        List<LoanDTO> ActiveData=data.stream().filter(x->x.getStatus()== Loan.Status.ACTIVE).collect(Collectors.toList());
        TableView<LoanDTO> ActiveTable=mainController.StatusTab("ACTIVE",ActiveData,ActiveTabPane);
        TableSettings(ActiveTable);
        List<LoanDTO> RiskData=data.stream().filter(x->x.getStatus()== Loan.Status.RISK).collect(Collectors.toList());
        TableView<LoanDTO> RiskTable=mainController.StatusTab("RISK",RiskData,RiskTabPane);
        TableSettings(RiskTable);
        List<LoanDTO> FinishedData=data.stream().filter(x->x.getStatus()== Loan.Status.FINISHED).collect(Collectors.toList());
        TableView<LoanDTO> FinishedTable=mainController.StatusTab("FINISHED",FinishedData,FinishedTabPane);
        TableSettings(FinishedTable);
    }

    public void setCustomerTransactions(){
        YazTimeColumn.setCellValueFactory(new PropertyValueFactory<>("yaztime"));
        TransactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        mainController.DoubleNiceFormat(amountColumn);
        amountBeforeColumn.setCellValueFactory(new PropertyValueFactory<>("moneyBefore"));
        mainController.DoubleNiceFormat(amountBeforeColumn);
        amountAfterColumn.setCellValueFactory(new PropertyValueFactory<>("getMoneyAfter"));
        mainController.DoubleNiceFormat(amountAfterColumn);
        TransactionTable.setItems(FXCollections.observableArrayList(PersonPicked.getActions()));
    }

    private void TableSettings(TableView<LoanDTO> table){
        table.setPrefSize(854,300);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        AnchorPane.setBottomAnchor(table,0.0);
        AnchorPane.setTopAnchor(table,0.0);
        AnchorPane.setLeftAnchor(table,0.0);
        AnchorPane.setRightAnchor(table,0.0);
    }

    public EngineFunctions getModel() {
        return Model;
    }

    public void setModel(EngineFunctions model) {
        Model = model;
    }

    public PersonDTO getPersonPicked() {
        return PersonPicked;
    }

    public void setPersonPicked(PersonDTO personPicked) {
        PersonPicked = personPicked;
    }

    public void setMainController(ABSController adminController) {
        this.mainController = adminController;
    }

    public DataBase getDataDTO() {
        return dataDTO;
    }

    public void setDataDTO(DataBase dataDTO) {
        this.dataDTO = dataDTO;
    }

    public CustomerController getCustomerController() {
        return customerController;
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
