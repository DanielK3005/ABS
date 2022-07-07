package mainWindow;

import adminWindow.AdminController;
import database.DataBase;
import dto.LenderDTO;
import dto.LoanDTO;
import dto.PaymentDTO;
import dto.PersonDTO;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import message.MessageController;
import customerWindow.CustomerController;
import engine.EngineFunctions;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;
import time.Yaz;
import transaction.Loan;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ABSController {

    @FXML
    private ComboBox<PersonDTO> ViewCB;

    @FXML
    private TextField PathTextFiled;

    @FXML
    private Label YazLabel;

    @FXML
    private ScrollPane DynamicPane;

    @FXML
    private BorderPane Root;
    @FXML
    private Label BalanceLable;

    @FXML
    private ComboBox<String> StyleCB;

    private String style;
    private EngineFunctions Model;
    private DataBase dataDTO;
    private Yaz time;
    private CustomerController customerController;
    private AdminController adminController;
    private Stage primaryStage;
    private MessageController messageController;


    @FXML
    public void initialize() throws  IOException {
        PersonDTO admin=new PersonDTO("Admin",0,new ArrayList<>());
        ViewCB.setItems(FXCollections.observableArrayList(admin));
        ViewCB.getSelectionModel().select(0);
        ViewCB.setDisable(true);
        BalanceLable.setVisible(false);
        time=new Yaz();
        time.valueProperty().addListener((observable, oldValue, newValue) -> {
            YazLabel.setText("Current Yaz: " + newValue.intValue());
        });
        time.setValue(1);
        StyleCB.setItems(FXCollections.observableArrayList("BASIC","DARK","MONEY"));
        StyleCB.getSelectionModel().select(0);
        style=StyleCB.getSelectionModel().getSelectedItem();
        setAdmin(style);
        adminController.setFirstEntry();
    }

    @FXML
    void ViewListner(ActionEvent event) throws IOException {
        if(ViewCB.getSelectionModel().getSelectedItem().getName().equalsIgnoreCase("Admin")){
            setAdmin(style);
            adminController.setTabs();
            adminController.SetCustomerTable();
        } else
        {
            setCustomer(style);
        }
    }

//    public void setLogin(String style) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader();
//        URL url;
//        //if(!style.equals("BASIC"))
//           // url = getClass().getResource("/adminWindow/resource/admin"+style.toLowerCase()+"-fxml.fxml");
//        //else
//        url = getClass().getResource("/loginWindow/login-fxml.fxml");
//        fxmlLoader.setLocation(url);
//        DynamicPane =fxmlLoader.load(url.openStream());
//        loginController =fxmlLoader.getController();
//        loginController.setMainController(this);
//        BalanceLable.setVisible(false);
//        Root.setCenter(DynamicPane);
//    }

    public void setAdmin(String style) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url;
        if(!style.equals("BASIC"))
            url = getClass().getResource("/adminWindow/resource/admin"+style.toLowerCase()+"-fxml.fxml");
        else
            url = getClass().getResource("/adminWindow/resource/admin-fxml.fxml");
        fxmlLoader.setLocation(url);
        DynamicPane =fxmlLoader.load(url.openStream());
        adminController =fxmlLoader.getController();
        adminController.setModel(this.Model);
        adminController.setMainController(this);
        BalanceLable.setVisible(false);
        adminController.setDataDTO(dataDTO);
        adminController.setTime(time);
        Root.setCenter(DynamicPane);
    }

    public void setCustomer(String style) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url;
        if(!style.equals("BASIC"))
            url = getClass().getResource("/customerWindow/resource/customer"+style.toLowerCase()+"-fxml.fxml");
        else
            url = getClass().getResource("/customerWindow/resource/customer-fxml.fxml");
        fxmlLoader.setLocation(url);
        DynamicPane =fxmlLoader.load(url.openStream());
        CustomerController ReloadData=customerController;
        customerController =fxmlLoader.getController();
        if(ReloadData!=null){
            Integer currentTabInd=ReloadData.getCustomerTabs().getSelectionModel().getSelectedIndex();
            customerController.getCustomerTabs().getSelectionModel().select(currentTabInd);
        }
        customerController.setModel(this.Model);
        customerController.setMainController(this);
        customerController.setDataDTO(dataDTO);
        customerController.setTime(time);
        customerController.setStyle(style);
        customerController.setPickedPerson(ViewCB.getSelectionModel().getSelectedItem());
        BalanceLable.setVisible(true);
        BalanceLable.setText(String.format("Balance: %,.1f",ViewCB.getSelectionModel().getSelectedItem().getBalance()));
        customerController.setScrambleControllerValues();
        customerController.setInformationControllerValues();
        customerController.setPaymentControllerValues();
        Root.setCenter(DynamicPane);
    }


    @FXML
    void StyleListner(ActionEvent event) throws IOException {
        style=StyleCB.getSelectionModel().getSelectedItem();
        primaryStage.getScene().getStylesheets().clear();
        if(!style.equals("BASIC"))
            primaryStage.getScene().getStylesheets().add("/mainWindow/resource/main"+ style.toLowerCase()+".css");
        else
            primaryStage.getScene().getStylesheets().add("/mainWindow/resource/main.css");
        if(ViewCB.getSelectionModel().getSelectedItem().getName().equalsIgnoreCase("Admin")){
            setAdmin(style);
            if(this.Model!=null && this.dataDTO!=null) {
                adminController.setTabs();
                adminController.SetCustomerTable();
            }else{
                adminController.setFirstEntry();
            }
        } else
        {
            setCustomer(style);
        }

    }

    public TableView<LoanDTO> BasicLoanTable(){
        TableView<LoanDTO> basic=new TableView<>();
        TableColumn<LoanDTO,String> id=new TableColumn<>("id");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<LoanDTO,PersonDTO> name=new TableColumn<>("name");
        name.setCellValueFactory(new PropertyValueFactory<>("owner"));
        TableColumn<LoanDTO, Loan.Status> status=new TableColumn<>("status");
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        TableColumn<LoanDTO,String> category=new TableColumn<>("category");
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        TableColumn<LoanDTO,Integer> amount=new TableColumn<>("amount");
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableColumn<LoanDTO,Integer> totalYaz=new TableColumn<>("Total\nYaz");
        totalYaz.setCellValueFactory(new PropertyValueFactory<>("totalYaz"));
        TableColumn<LoanDTO,Integer> timeToPay=new TableColumn<>("Pay\nEvery\nYaz");
        timeToPay.setCellValueFactory(new PropertyValueFactory<>("timeToPay"));
        TableColumn<LoanDTO,Integer> interest=new TableColumn<>("interest");
        interest.setCellValueFactory(new PropertyValueFactory<>("interest"));
        basic.getColumns().addAll(id,name,status,category,amount,totalYaz,timeToPay,interest);
        return basic;
    }
    public TableView<LoanDTO> NewTab(List<LoanDTO> StatusNew, AnchorPane TabPane){
        ObservableList<LoanDTO> data= FXCollections.observableArrayList(StatusNew);
        TableView<LoanDTO> NewTable=BasicLoanTable();
        NewTable.setItems(data);
        TabPane.getChildren().addAll(NewTable);
        return NewTable;
    }
    public TableView<LoanDTO> PendingTab(List<LoanDTO> StatusPending,AnchorPane TabPane){

        TableView<LoanDTO> PendingTable=BasicLoanTable();
        TableColumn<LoanDTO, Button> loaners=new TableColumn<>("Lenders&\nPayments");
        loaners.setCellValueFactory(new PropertyValueFactory<>("showLoaner"));
        TableColumn<LoanDTO, Double> totalLoanerMoney=new TableColumn<>("Total Raised\nSo Far");
        totalLoanerMoney.setCellValueFactory(new PropertyValueFactory<>("totalLoanerMoney"));
        DoubleNiceFormat(totalLoanerMoney);
        TableColumn<LoanDTO, Double> leftToMakeActive=new TableColumn<>("Left To\nMake Active");
        leftToMakeActive.setCellValueFactory(new PropertyValueFactory<>("amountOfMoneyToMakeActive"));
        DoubleNiceFormat(leftToMakeActive);
        PendingTable.getColumns().addAll(loaners,totalLoanerMoney,leftToMakeActive);
        for(LoanDTO val: StatusPending){
            val.getShowLoaner().setOnAction(e->PendingActionHandle(val.getId(),val.getLoanerList()));
        }
        //TablePrefSettings(PendingTable);
        ObservableList<LoanDTO> data=FXCollections.observableArrayList(StatusPending);
        PendingTable.setItems(data);
        TabPane.getChildren().addAll(PendingTable);
        return PendingTable;
    }

    public void PendingActionHandle(String id,List<LenderDTO> loaners){
        BorderPane PendingLoaners=new BorderPane();
        PendingLoaners.setPrefSize(400,400);
        TableView<LenderDTO> PendingTable=new TableView<>();
        TableColumn<LenderDTO,PersonDTO> name=new TableColumn<>("name");
        name.setCellValueFactory(new PropertyValueFactory<>("owner"));
        TableColumn<LenderDTO,Double> amount=new TableColumn<>("amount");
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        DoubleNiceFormat(amount);
        PendingTable.getColumns().addAll(name,amount);
        PendingTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        PendingTable.setItems(FXCollections.observableArrayList(loaners));
        PendingLoaners.setCenter(PendingTable);
        Scene scene=new Scene(PendingLoaners,400,400);
        Stage stage=new Stage();
        stage.setTitle(id+" Lenders List");
        stage.setScene(scene);
        stage.show();
    }
    public TableView<LoanDTO> StatusTab(String status,List<LoanDTO> StatusList,AnchorPane TabPane) {
        TableView<LoanDTO> StatusTable = BasicLoanTable();
        for(LoanDTO val: StatusList){
            val.getShowLoaner().setOnAction(e->ActiveActionHandle(val.getId(),val.getLoanerList()));
        }
        TableColumn<LoanDTO, Button> loaners = new TableColumn<>("Lenders&\nPayments");
        loaners.setCellValueFactory(new PropertyValueFactory<>("showLoaner"));
        TableColumn<LoanDTO,Integer> activeStatusYaz=new TableColumn<>("active\nstatus yaz");
        activeStatusYaz.setCellValueFactory(new PropertyValueFactory<>("activeYaz"));
        TableColumn<LoanDTO,Integer> nextPaymentYaz=new TableColumn<>("next\npayment yaz");
        nextPaymentYaz.setCellValueFactory(new PropertyValueFactory<>("nextPaymentYaz"));
        TableColumn<LoanDTO,Integer> finishedStatusYaz=new TableColumn<>("finished\nstatus yaz");
        finishedStatusYaz.setCellValueFactory(new PropertyValueFactory<>("finishedYaz"));
        if(status.equalsIgnoreCase("FINISHED"))
            StatusTable.getColumns().addAll(loaners,activeStatusYaz,nextPaymentYaz,finishedStatusYaz);
        else
            StatusTable.getColumns().addAll(loaners,activeStatusYaz,nextPaymentYaz);
        //TablePrefSettings(StatusTable);
        ObservableList<LoanDTO> data = FXCollections.observableArrayList(StatusList);
        StatusTable.setItems(data);
        TabPane.getChildren().addAll(StatusTable);
        return StatusTable;
    }
    public void ActiveActionHandle(String id, List<LenderDTO> loaners){
        BorderPane ActiveLoaners=new BorderPane();
        ActiveLoaners.setPrefSize(800,600);
        TableView<LenderDTO> ActiveTable=new TableView<>();
        ActiveTable.setPrefSize(800,300);
        TableColumn<LenderDTO,PersonDTO> name=new TableColumn<>("name");
        name.setCellValueFactory(new PropertyValueFactory<>("owner"));
        TableColumn<LenderDTO,Double> amount=new TableColumn<>("amount");
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        DoubleNiceFormat(amount);
        TableColumn<LenderDTO, Hyperlink> payment=new TableColumn<>("Payments");
        payment.setCellValueFactory(new PropertyValueFactory<>("showPayment"));
        TableColumn<LenderDTO,Double> totalPayBack=new TableColumn<>("total pay\nback");
        totalPayBack.setCellValueFactory(new PropertyValueFactory<>("totalPayBack"));
        DoubleNiceFormat(totalPayBack);
        TableColumn<LenderDTO,Double> leftToPay=new TableColumn<>("left to\npay");
        leftToPay.setCellValueFactory(new PropertyValueFactory<>("leftToPay"));
        DoubleNiceFormat(leftToPay);
        TableColumn<LenderDTO,Double> totalInterestPaySoFar=new TableColumn<>("interest paid\nso far");
        totalInterestPaySoFar.setCellValueFactory(new PropertyValueFactory<>("totalInterestPaidSoFar"));
        DoubleNiceFormat(totalInterestPaySoFar);
        TableColumn<LenderDTO,Double> totalInterest=new TableColumn<>("total interest");
        totalInterest.setCellValueFactory(new PropertyValueFactory<>("totalInterest"));
        DoubleNiceFormat(totalInterest);
        if(loaners.size()>0 && loaners.get(0).getDebt()!=0){
            TableColumn<LenderDTO,Double> debt=new TableColumn<>("debt");
            debt.setCellValueFactory(new PropertyValueFactory<>("debt"));
            DoubleNiceFormat(debt);
            ActiveTable.getColumns().addAll(name,amount,payment,totalPayBack,debt,leftToPay,totalInterestPaySoFar,totalInterest);
        } else
            ActiveTable.getColumns().addAll(name,amount,payment,totalPayBack,leftToPay,totalInterestPaySoFar,totalInterest);
        TableView<PaymentDTO> paynemtTable=new TableView<>();
        paynemtTable.setPrefSize(800,300);
        paynemtTable.setVisible(false);
        for(LenderDTO val: loaners){
            val.getShowPayment().setOnAction(e-> {PaymentActionHandle(val.getPaymentlog(),paynemtTable);} );
        }
        paynemtTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        ActiveTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        ActiveTable.setItems(FXCollections.observableArrayList(loaners));
        ActiveLoaners.setTop(ActiveTable);
        ActiveLoaners.setBottom(paynemtTable);
        Scene scene=new Scene(ActiveLoaners,800,600);
        Stage stage=new Stage();
        stage.setTitle(id+" Lenders & Payment List");
        stage.resizableProperty().set(false);
        stage.setScene(scene);
        stage.show();
    }

    public void PaymentActionHandle(List<PaymentDTO> payments,TableView<PaymentDTO> paynemtTable){
    TableColumn<PaymentDTO,Integer> payYaz=new TableColumn<>("Payment\nYaz");
    payYaz.setCellValueFactory(new PropertyValueFactory<>("currentyaz"));
    TableColumn<PaymentDTO,Double> onePayment=new TableColumn<>("Payment\namount");
    onePayment.setCellValueFactory(new PropertyValueFactory<>("onePayment"));
    DoubleNiceFormat(onePayment);
    TableColumn<PaymentDTO,Double> interest=new TableColumn<>("Interest");
    interest.setCellValueFactory(new PropertyValueFactory<>("interestOnePay"));
    DoubleNiceFormat(interest);
    TableColumn<PaymentDTO,Double> total=new TableColumn<>("Total\nPayment");
    total.setCellValueFactory(new PropertyValueFactory<>("totalOnePayment"));
    DoubleNiceFormat(total);
    TableColumn<PaymentDTO,Boolean> gotPaid=new TableColumn<>("Got Paid");
    gotPaid.setCellValueFactory(new PropertyValueFactory<>("paidSuccesfully"));
    gotPaid.setCellFactory(column -> {
        return new TableCell<PaymentDTO, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);

                setText(empty ? "" : getItem().toString());
                setGraphic(null);

                TableRow<PaymentDTO> currentRow = getTableRow();

                if (!isEmpty()) {
                    if (item)
                        currentRow.setStyle("-fx-background-color: #7cfc00");
                    else
                        currentRow.setStyle("-fx-background-color:#fa2222");
                }
            }
        };
    });
    if(paynemtTable.getColumns().size()>0 || paynemtTable.getItems().size()>0){
        paynemtTable.getColumns().clear();
        paynemtTable.getItems().clear();
    }
    paynemtTable.getColumns().addAll(payYaz,onePayment,interest,total,gotPaid);
    paynemtTable.setItems(FXCollections.observableArrayList(payments));
    paynemtTable.setVisible(true);
    }

    public void DoubleNiceFormat(TableColumn priceCol){
        priceCol.setCellFactory(c -> new TableCell<Object,Double>() {
            @Override
            protected void updateItem(Double balance, boolean empty) {
                super.updateItem(balance, empty);
                if (balance == null || empty) {
                    setText(null);
                } else {
                    setText(String.format("%,.2f", balance));
                }
            }
        });
    }

    public Stage MessageStage(AdminController.MessageType type , String message) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Stage stage=new Stage();
        stage.setTitle("Message");
        URL url = getClass().getResource("/message/message-fxml.fxml");
        fxmlLoader.setLocation(url);
        ScrollPane MessagePane =fxmlLoader.load(url.openStream());
        messageController =fxmlLoader.getController();
        messageController.setMessageText(message);
        switch (type){
            case Error:
                Image icon=new Image(getClass().getResourceAsStream("/message/resources/error-icon-png-23.jpg"));
                messageController.setMessageImage(icon);
                break;
            case Successfully:
                icon=new Image(getClass().getResourceAsStream("/message/resources/successfully-icon-png-24.jpg"));
                messageController.setMessageImage(icon);
                break;
        }

        Scene scene=new Scene(MessagePane,750,170);
        stage.setScene(scene);
        return stage;
    }

    public Label getBalanceLable() {
        return BalanceLable;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        //adminController.setPrimaryStage(primaryStage);
    }

    public void setText(String path){
        this.PathTextFiled.setText(path);
    }

    public ComboBox<PersonDTO> getViewCB() {
        return ViewCB;
    }

    public EngineFunctions getModel() {
        return Model;
    }

    public void setModel(EngineFunctions model) {
        Model = model;
    }

    public DataBase getDataDTO() {
        return dataDTO;
    }

    public void setDataDTO(DataBase dataDTO) {
        this.dataDTO = dataDTO;
    }
}
