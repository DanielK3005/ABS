package customerWindow;

import adminWindow.AdminController;
import customerWindow.components.information.InformationController;
import customerWindow.components.moneyaction.MoneyActionController;
import customerWindow.components.payment.PaymentController;
import customerWindow.components.scramble.ScrambleController;
import database.DataBase;
import dto.PersonDTO;
import engine.EngineFunctions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mainWindow.ABSController;
import time.Yaz;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;

public class CustomerController {

    public enum ActionType{SCRAMBLE,WITHDRAW,INCOME,PAYMENT}


    @FXML
    private TabPane CustomerTabs;

    @FXML
    private Tab ScrambleTab;

    @FXML
    private Tab informationTab;
    @FXML
    private Tab paymentTab;

    private String style;
    private EngineFunctions Model;
    private DataBase dataDTO;
    private Yaz time;
    private ScrambleController scrambleController;
    private InformationController informationController;
    private PaymentController paymentController;
    private MoneyActionController moneyActionController;
    private ABSController mainController;
    private PersonDTO PickedPerson;

    public void setScrambleControllerValues() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url;
        if(style.equals("BASIC"))
            url = getClass().getResource("components/scramble/resource/scramble-fxml.fxml");
        else
            url = getClass().getResource("components/scramble/resource/scramble"+style.toLowerCase()+"-fxml.fxml");
        fxmlLoader.setLocation(url);
        Parent ScramblePane=fxmlLoader.load(url.openStream());
        ScrambleTab.setContent(ScramblePane);
        scrambleController=fxmlLoader.getController();
        scrambleController.setModel(this.Model);
        scrambleController.setDataDTO(dataDTO);
        scrambleController.setTime(time);
        scrambleController.setCustomerController(this);
        scrambleController.setMainController(mainController);
        scrambleController.setPickedPerson(PickedPerson);
        scrambleController.setFirstEntry();
    }
    public void setInformationControllerValues() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url;
        if(style.equals("BASIC"))
            url = getClass().getResource("components/information/resource/information-fxml.fxml");
        else
            url=getClass().getResource("components/information/resource/information"+style.toLowerCase()+"-fxml.fxml");
        fxmlLoader.setLocation(url);
        informationTab.setContent(fxmlLoader.load(url.openStream()));
        informationController=fxmlLoader.getController();
        informationController.setModel(this.Model);
        informationController.setMainController(mainController);
        informationController.setCustomerController(this);
        informationController.setDataDTO(dataDTO);
        informationController.setTime(time);
        informationController.setPersonPicked(PickedPerson);
        informationController.setFirstEntry();
    }

    public void setPaymentControllerValues() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url;
        if(style.equals("BASIC"))
            url = getClass().getResource("components/payment/resource/payment-fxml.fxml");
        else
            url=getClass().getResource("components/payment/resource/payment"+style.toLowerCase()+"-fxml.fxml");
        fxmlLoader.setLocation(url);
        paymentTab.setContent(fxmlLoader.load(url.openStream()));
        paymentController=fxmlLoader.getController();
        paymentController.setModel(this.Model);
        paymentController.setMainController(mainController);
        paymentController.setCustomerController(this);
        paymentController.setDataDTO(dataDTO);
        paymentController.setTime(time);
        paymentController.setSelectedPerson(PickedPerson);
        paymentController.setFirstEntry();
    }

    public void UpdateChanges(EngineFunctions Model, DataBase dataDTO, ActionType type) throws IOException {
        mainController.setModel(Model);
        mainController.setDataDTO(dataDTO);
        ObservableList<PersonDTO> updatePersonList=FXCollections.observableArrayList(mainController.getViewCB().getItems().get(0));
        updatePersonList.addAll(dataDTO.getCustomersDTO());
        int selectind=mainController.getViewCB().getSelectionModel().getSelectedIndex();
        mainController.getViewCB().getItems().setAll(updatePersonList);
        mainController.getViewCB().getSelectionModel().select(selectind);
        Stage stage=mainController.MessageStage(AdminController.MessageType.Successfully,"The " + type.toString().toLowerCase(Locale.ROOT)+ " process finished successfully!");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public Optional<ButtonType> setMoneyAction(CustomerController.ActionType type,String name) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("components/moneyaction/resource/moneyaction-fxml.fxml");
        fxmlLoader.setLocation(url);
        DialogPane pane =fxmlLoader.load(url.openStream());
        moneyActionController =fxmlLoader.getController();
        informationController.setMoneyActionController(moneyActionController);
        paymentController.setMoneyActionController(moneyActionController);
        Dialog<ButtonType> dialog=new Dialog<>();
        dialog.setDialogPane(pane);
        if(type==ActionType.INCOME)
            dialog.setTitle("Insert Money To The Account");
        else if(type== ActionType.WITHDRAW)
            dialog.setTitle("Withdraw Money From The Account");
        else
            dialog.setTitle(name+ "Risk Loan");
        return dialog.showAndWait();
    }

    public int InputValidation(TextField textField, Label Error){
        int ret;
        try{
            ret=Integer.parseInt(textField.getText());
        } catch (NumberFormatException e) {
            Error.setText("please insert a number");
            Error.setVisible(true);
            return -1;
        }
        if(ret<0){
            Error.setText("please insert a positive number");
            Error.setVisible(true);
            return -1;
        }
        return ret;
    }

    public void setPickedPerson(PersonDTO pickedPerson) {
        PickedPerson = pickedPerson;
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

    public TabPane getCustomerTabs() {
        return CustomerTabs;
    }

    public void setTime(Yaz time) {
        this.time = time;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
