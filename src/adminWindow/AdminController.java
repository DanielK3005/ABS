package adminWindow;

import database.DataBase;
import message.MessageController;
import dto.LoanDTO;
import dto.LenderDTO;
import dto.PaymentDTO;
import dto.PersonDTO;
import engine.Engine;
import engine.EngineFunctions;
import exception.CategoryMismatchException;
import exception.DuplicatePersonException;
import exception.LoanPersonMismatchException;
import exception.NotDividedCorrectlyException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mainWindow.ABSController;
import time.Yaz;
import transaction.Loan;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminController {

    public enum MessageType{Error,Successfully,Information};

    @FXML
    private AnchorPane DynamicPane;

    @FXML
    private Button YazBtn;

    @FXML
    private Button FileBtn;

    @FXML
    private TabPane LoanTabs;

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
    private TableView<PersonDTO> CustomerTable;
    @FXML
    private TableColumn<PersonDTO, String> CustomerName;

    @FXML
    private TableColumn<PersonDTO, Double> CustomerBalance;

    @FXML
    private TableColumn<PersonDTO, Integer> NewTotalLoaners;

    @FXML
    private TableColumn<PersonDTO, Integer> PendingTotalLoaners;

    @FXML
    private TableColumn<PersonDTO, Integer> ActiveTotalLoaners;

    @FXML
    private TableColumn<PersonDTO, Integer> RiskTotalLoaners;

    @FXML
    private TableColumn<PersonDTO, Integer> FinishedTotalLoaners;

    @FXML
    private TableColumn<PersonDTO, Integer> NewTotalLenders;

    @FXML
    private TableColumn<PersonDTO, Integer> PendingTotalLenders;

    @FXML
    private TableColumn<PersonDTO, Integer> ActiveTotalLenders;

    @FXML
    private TableColumn<PersonDTO, Integer> RiskTotalLenders;

    @FXML
    private TableColumn<PersonDTO, Integer> FinishedTotalLenders;

    private EngineFunctions Model;
    private DataBase dataDTO;
    private Yaz time;
    private ABSController MainController;
    private Stage primaryStage;


    public void setFirstEntry(){
        YazBtn.setVisible(false);
        LoanTabs.setVisible(false);
        CustomerTable.setVisible(false);
    }
    @FXML
    void IncreazeYaz(ActionEvent event) {
        time.setValue(time.getValue()+1);
        this.Model.UpdateLoansStatus(time.getValue());
        dataDTO=this.Model.getDataBaseDTO();
        List<LoanDTO> newRisk=dataDTO.getLoansDTO().stream().filter(x->x.getStatus()== Loan.Status.RISK).collect(Collectors.toList());
        List<LoanDTO> newActive=dataDTO.getLoansDTO().stream().filter(x->x.getStatus()== Loan.Status.ACTIVE).collect(Collectors.toList());
        MainController.setModel(Model);
        MainController.setDataDTO(dataDTO);
        ObservableList<PersonDTO> updatePersonList=FXCollections.observableArrayList(MainController.getViewCB().getItems().get(0));
        updatePersonList.addAll(dataDTO.getCustomersDTO());
        MainController.getViewCB().getItems().setAll(updatePersonList);
        TableView<LoanDTO> newActiveTable=MainController.StatusTab("ACTIVE",newActive,ActiveTabPane);
        TableView<LoanDTO> newRiskTable=MainController.StatusTab("RISK",newRisk,RiskTabPane);
        TablePrefSettings(newActiveTable);
        TablePrefSettings(newRiskTable);
    }

    public ABSController getMainController() {
        return MainController;
    }

    public void setMainController(ABSController mainController) {
        MainController = mainController;
    }

    public void SetCustomerTable(){
        CustomerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        CustomerBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        MainController.DoubleNiceFormat(CustomerBalance);
        NewTotalLoaners.setCellValueFactory(new PropertyValueFactory<>("TotalNewLoans"));
        PendingTotalLoaners.setCellValueFactory(new PropertyValueFactory<>("TotalPendingLoans"));
        ActiveTotalLoaners.setCellValueFactory(new PropertyValueFactory<>("TotalActiveLoans"));
        RiskTotalLoaners.setCellValueFactory(new PropertyValueFactory<>("TotalRiskLoans"));
        FinishedTotalLoaners.setCellValueFactory(new PropertyValueFactory<>("TotalFinishedLoans"));
        NewTotalLenders.setCellValueFactory(new PropertyValueFactory<>("TotalNewLenders"));
        PendingTotalLenders.setCellValueFactory(new PropertyValueFactory<>("TotalPendingLenders"));
        ActiveTotalLenders.setCellValueFactory(new PropertyValueFactory<>("TotalActiveLenders"));
        RiskTotalLenders.setCellValueFactory(new PropertyValueFactory<>("TotalRiskLenders"));
        FinishedTotalLenders.setCellValueFactory(new PropertyValueFactory<>("TotalFinishedLenders"));
       // CustomerTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        CustomerTable.setItems(FXCollections.observableArrayList(dataDTO.getCustomersDTO()));
    }

    @FXML
    void LoadFile(ActionEvent event) throws IOException,JAXBException {
        FileChooser fileChooser = new FileChooser();
        Stage errorWindow;
        fileChooser.setTitle("Select Data file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }
        Model=new Engine();
        try{
            this.Model.loadFiletoDescriptor(selectedFile);
            this.Model.CheckValidDataFile();
            this.Model.LoadFileDataToEngine();
        } catch (NotDividedCorrectlyException e) {
            errorWindow=MainController.MessageStage(MessageType.Error,e.getMessage());
            errorWindow.initModality(Modality.APPLICATION_MODAL);
            errorWindow.show();
            return;
        } catch (CategoryMismatchException e) {
            errorWindow=MainController.MessageStage(MessageType.Error,e.getMessage());
            errorWindow.initModality(Modality.APPLICATION_MODAL);
            errorWindow.show();
            return;
        } catch (LoanPersonMismatchException e) {
            errorWindow=MainController.MessageStage(MessageType.Error,e.getMessage());
            errorWindow.initModality(Modality.APPLICATION_MODAL);
            errorWindow.show();
            return;
        } catch (DuplicatePersonException e) {
            errorWindow=MainController.MessageStage(MessageType.Error,e.getMessage());
            errorWindow.initModality(Modality.APPLICATION_MODAL);
            errorWindow.show();
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();
        //selectedFileProperty.set(absolutePath);
        MainController.setModel(this.Model);
        MainController.setText(absolutePath);
        MainController.getViewCB().setDisable(false);
        dataDTO=this.Model.getDataBaseDTO();
        MainController.setDataDTO(dataDTO);
        Integer size=MainController.getViewCB().getItems().size();
        if(size>1)
            MainController.getViewCB().getItems().remove(1,size);
        MainController.getViewCB().getItems().addAll(FXCollections.observableArrayList(dataDTO.getCustomersDTO()));
        Stage FileLoaded=MainController.MessageStage(MessageType.Successfully,"File loaded Successfully!");
        FileLoaded.initModality(Modality.APPLICATION_MODAL);
        YazBtn.setVisible(true);
        LoanTabs.setVisible(true);
        CustomerTable.setVisible(true);
        setTabs();
        SetCustomerTable();
        FileLoaded.show();
    }

    public void setTabs(){
        List<LoanDTO> data=dataDTO.getLoansDTO();
        List<LoanDTO> NewList=getStatusList("NEW",data);
        TableView<LoanDTO> NewTable=MainController.NewTab(NewList,NewTabPane);
        TablePrefSettings(NewTable);
        List<LoanDTO> PendingList=getStatusList("PENDING",data);
        TableView<LoanDTO> PendingTable=MainController.PendingTab(PendingList,PendingTabPane);
        TablePrefSettings(PendingTable);
        List<LoanDTO> ActiveList=getStatusList("ACTIVE",data);
        TableView<LoanDTO> ActiveTable=MainController.StatusTab("ACTIVE",ActiveList,ActiveTabPane);
        TablePrefSettings(ActiveTable);
        List<LoanDTO> RiskList=getStatusList("RISK",data);
        TableView<LoanDTO> RiskTable=MainController.StatusTab("RISK",RiskList,RiskTabPane);
        TablePrefSettings(RiskTable);
        List<LoanDTO> FinishedList=getStatusList("FINISHED",data);
        TableView<LoanDTO> FinishedTable=MainController.StatusTab("FINISHED",FinishedList,FinishedTabPane);
        TablePrefSettings(FinishedTable);
    }

    public List<LoanDTO> getStatusList(String status,List<LoanDTO> data){
        switch(status.toUpperCase()){
            case "NEW":
                return data.stream().filter(x -> x.getStatus() == Loan.Status.NEW).collect(Collectors.toList());
            case "PENDING":
                return data.stream().filter(x -> x.getStatus() == Loan.Status.PENDING).collect(Collectors.toList());
            case "ACTIVE":
                return data.stream().filter(x -> x.getStatus() == Loan.Status.ACTIVE).collect(Collectors.toList());
            case "RISK":
                return data.stream().filter(x -> x.getStatus() == Loan.Status.RISK).collect(Collectors.toList());
            case "FINISHED":
                return data.stream().filter(x -> x.getStatus() == Loan.Status.FINISHED).collect(Collectors.toList());
        }
        return  new ArrayList<>();
    }



    public void TablePrefSettings(TableView basic){
        basic.setPrefSize(1194,267);
        basic.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        AnchorPane.setBottomAnchor(basic,0.0);
        AnchorPane.setTopAnchor(basic,0.0);
        AnchorPane.setLeftAnchor(basic,0.0);
        AnchorPane.setRightAnchor(basic,0.0);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
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

    public void setTime(Yaz time) {
        this.time = time;
    }
}
