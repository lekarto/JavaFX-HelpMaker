package helpmaker.view;

import helpmaker.model.XMLParameter;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class XMLParameterEditController {
    private Stage dialogStage;
    @FXML
    private TextField keyField;
    @FXML
    private TextField valueField;

    private XMLParameter xmlParameter;

    public XMLParameterEditController() { }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setXmlParameter(XMLParameter xmlParameter) {
        this.xmlParameter = xmlParameter;
        keyField.setText(xmlParameter.getKey());
        valueField.setText(xmlParameter.getValue());
    }

    public void btnOKClick() {
        xmlParameter.setKey(keyField.getText());
        xmlParameter.setValue(valueField.getText());
        btnCancelClick();
    }

    public void btnCancelClick() {
        this.dialogStage.close();
    }
}
