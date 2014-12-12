package helpmaker.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class XMLNodeNewController {
    @FXML
    private TextField tfCaption;

    private String caption;
    private Stage dialogStage;

    public XMLNodeNewController() { }

    public String getCaption() {
        return caption;
    }

    public void onOKClick() {
        caption = tfCaption.getText();
        onCancelClick();
    }

    public void onCancelClick() {
        this.dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        this.dialogStage.getIcons().add(new Image("helpmaker/res/newdoc.png"));
    }
}
