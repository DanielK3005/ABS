package customerWindow.components.moneyaction;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MoneyActionController {

    @FXML
    private TextField AmountTF;


    public TextField getAmountTF() {
        return AmountTF;
    }

    public void setAmountTF(TextField amountTF) {
        AmountTF = amountTF;
    }
}
