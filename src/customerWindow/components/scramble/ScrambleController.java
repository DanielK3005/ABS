package customerWindow.components.scramble;

import adminWindow.AdminController;
import customerWindow.CustomerController;
import customerWindow.components.information.InformationController;
import database.DataBase;
import dto.LoanDTO;
import dto.PersonDTO;
import engine.EngineFunctions;
import exception.LastLoanException;
import exception.MoreThanYouGotException;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.Notifications;
import time.Yaz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ScrambleController {
    @FXML
    private AnchorPane ResultPane;

    @FXML
    private TextField AmountTF;

    @FXML
    private Label AmountLable;

    @FXML
    private TextField minYazTF;

    @FXML
    private TextField openLoansTF;

    @FXML
    private CheckListView<String> CategoryList;

    @FXML
    private Label minYazLable;

    @FXML
    private Label MaxOpenLable;

    @FXML
    private ComboBox<Integer> ownershipCB;

    @FXML
    private TextField IntrestTF;

    @FXML
    private Label InterestLable;

    @FXML
    private CheckBox CategoryCB;

    @FXML
    private CheckBox IntrestCB;

    @FXML
    private CheckBox MinYazCB;

    @FXML
    private CheckBox MaxOpenCB;

    @FXML
    private CheckBox OwnerShipCB;

    @FXML
    private Button ScrambleBtn;

    @FXML
    private Button ClearAllBtn;

    @FXML
    private Button SearchBtn;

    @FXML
    private ProgressBar ProgressBar;

    @FXML
    private Label ProgressPercent;

    private EngineFunctions Model;
    private DataBase dataDTO;
    private Yaz time;
    private CustomerController customerController;
    private ABSController mainController;
    private PersonDTO PickedPerson;
    private Integer amountToInvest;
    private TableView<LoanDTO> LoansResultPick;

    @FXML
    public void initialize(){
        AmountLable.setVisible(false);
        minYazLable.setVisible(false);
        MaxOpenLable.setVisible(false);
        InterestLable.setVisible(false);
        ScrambleBtn.setDisable(true);

        CategoryList.disableProperty().bind(CategoryCB.selectedProperty().not());
        IntrestTF.disableProperty().bind(IntrestCB.selectedProperty().not());
        minYazTF.disableProperty().bind(MinYazCB.selectedProperty().not());
        openLoansTF.disableProperty().bind(MaxOpenCB.selectedProperty().not());
        ownershipCB.disableProperty().bind(OwnerShipCB.selectedProperty().not());
        List<Integer> numbers= IntStream.range(0, 100)
                .boxed()
                .collect(Collectors.toList());
        ownershipCB.setItems(FXCollections.observableArrayList(numbers));
    }

    public void setFirstEntry(){
        CategoryList.setItems(FXCollections.observableArrayList(this.Model.getCategories()));
    }

    @FXML
    void ClearAllAction(ActionEvent event) {
        AmountTF.setText("");
        CategoryList.getCheckModel().clearChecks();
        minYazTF.setText("");
        openLoansTF.setText("");
        IntrestTF.setText("");
    }

    @FXML
    void ScrambleListner(ActionEvent event) throws IOException {
        List<LoanDTO> checked=LoansResultPick.getItems().stream().filter(x->x.getPicked().isSelected()==true).collect(Collectors.toList());
        if(checked.size()==0){
            Notifications more=Notifications.create().title("Error").text("Please select loans to successfully finish the scramble").hideAfter(Duration.seconds(60)).position(Pos.TOP_LEFT);
            more.showError();
            return;
        }
        if(amountToInvest>PickedPerson.getBalance()){
            MoreThanYouGotException e=new MoreThanYouGotException(PickedPerson.getBalance(),amountToInvest);
            Notifications more=Notifications.create().title("Error").text(e.getMessage()).hideAfter(Duration.seconds(60)).position(Pos.TOP_LEFT);
            more.showError();
            return;
        }
        try {
            if(ownershipCB.getSelectionModel().getSelectedItem()!=null)
                this.Model.MakeAssignment(PickedPerson, amountToInvest, checked, ownershipCB.getSelectionModel().getSelectedItem(), time.getValue());
            else
                this.Model.MakeAssignment(PickedPerson, amountToInvest, checked, 100, time.getValue());
        }catch (LastLoanException e){
            Notifications more=Notifications.create().title("Notice:").text(e.getMessage()).hideAfter(Duration.seconds(60)).position(Pos.TOP_LEFT);
            more.showInformation();
        }
        dataDTO=this.Model.getDataBaseDTO();
        customerController.UpdateChanges(this.Model,this.dataDTO, CustomerController.ActionType.SCRAMBLE);
    }

    @FXML
    void SearchListner(ActionEvent event) {
        if(AmountTF.getText().equals("")){
            AmountLable.setText("*Required");
            AmountLable.setVisible(true);
            return;
        } else{
            AmountLable.setVisible(false);
            amountToInvest=customerController.InputValidation(AmountTF,AmountLable);
            if(amountToInvest==-1)
                return;
            if(amountToInvest>PickedPerson.getBalance()){
                MoreThanYouGotException e=new MoreThanYouGotException(PickedPerson.getBalance(),amountToInvest);
                Notifications more=Notifications.create().title("Error").text(e.getMessage()).hideAfter(Duration.seconds(60)).position(Pos.TOP_LEFT);
                more.showError();
                return;
            }
        }
        List<String> pickedCategories=new ArrayList<>(CategoryList.getCheckModel().getCheckedItems());
        Integer interestPick;
        Integer minYaz,maxOpen;
        if(!IntrestTF.getText().equals("")) {
            interestPick=customerController.InputValidation(IntrestTF,InterestLable);
            if(interestPick==-1)
                return;
        }
        else{
            InterestLable.setVisible(false);
            interestPick=null;
        }
        if(!minYazTF.getText().equals("")) {
         minYaz=customerController.InputValidation(minYazTF,minYazLable);
         if(minYaz==-1)
             return;
        }
            else{
            minYazLable.setVisible(false);
            minYaz=null;
        }
        if(!openLoansTF.getText().equals("")){
            maxOpen=customerController.InputValidation(openLoansTF,MaxOpenLable);
            if(maxOpen==-1)
                return;
        }else{
            MaxOpenLable.setVisible(false);
            maxOpen=null;
        }
        ScrambleTask SearchLoans=new ScrambleTask(Model,PickedPerson,dataDTO,interestPick,pickedCategories,minYaz,maxOpen);
        bindTaskToUIComponents(SearchLoans,()->{
            SearchBtn.setDisable(false);
            ResultPane.getChildren().addAll(LoansResultPick);
            ScrambleBtn.setDisable(false);
            ProgressBar.setVisible(false);
        });
        LoansResultPick=mainController.BasicLoanTable();
        TableColumn<LoanDTO,CheckBox> Picked=new TableColumn<>();
        Picked.setCellValueFactory(new PropertyValueFactory<>("picked"));
        LoansResultPick.getColumns().add(0,Picked);
        LoansResultPick.itemsProperty().bind(SearchLoans.valueProperty());
        LoansResultPick.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        LoansResultPick.setLayoutX(0);
        LoansResultPick.setLayoutY(58);
        LoansResultPick.setPrefHeight(465);
        new Thread(SearchLoans).start();
        AnchorPane.setTopAnchor(LoansResultPick,50.0);
        AnchorPane.setLeftAnchor(LoansResultPick,0.0);
        AnchorPane.setRightAnchor(LoansResultPick,0.0);
        AnchorPane.setBottomAnchor(LoansResultPick,80.0);
    }

    public void bindTaskToUIComponents(Task<ObservableList<LoanDTO>> SearchLoans, Runnable onFinish) {
        ProgressBar.progressProperty().bind(SearchLoans.progressProperty());
        BeforeTaskStarted();

        ProgressPercent.textProperty().bind(
                Bindings.concat(
                        Bindings.format(
                                "%.0f",
                                Bindings.multiply(
                                        SearchLoans.progressProperty(),
                                        100)),
                        " %"));

        // task cleanup upon finish
        SearchLoans.valueProperty().addListener((observable, oldValue, newValue) -> {
            onTaskFinished(Optional.ofNullable(onFinish));
        });
        }

        public void BeforeTaskStarted(){
            SearchBtn.setDisable(true);
            ProgressBar.setVisible(true);
            if(LoansResultPick!=null)
                LoansResultPick.setVisible(false);
        }

    public void onTaskFinished(Optional<Runnable> onFinish) {
        this.ProgressPercent.textProperty().unbind();
        this.ProgressBar.progressProperty().unbind();
        ProgressBar.setVisible(false);
        onFinish.ifPresent(Runnable::run);
    }

    public PersonDTO getPickedPerson() {
        return PickedPerson;
    }

    public void setPickedPerson(PersonDTO pickedPerson) {
        PickedPerson = pickedPerson;
    }

    public CustomerController getCustomerController() {
        return customerController;
    }

    public void setCustomerController(CustomerController customerController) {
        this.customerController = customerController;
    }

    public EngineFunctions getModel() {
        return Model;
    }

    public void setModel(EngineFunctions model) {
        Model = model;
    }

    public ABSController getMainController() {
        return mainController;
    }

    public void setMainController(ABSController mainController) {
        this.mainController = mainController;
    }

    public DataBase getDataDTO() {
        return dataDTO;
    }

    public void setDataDTO(DataBase dataDTO) {
        this.dataDTO = dataDTO;
    }

    public void setTime(Yaz time) {
        this.time = time;
    }
}
