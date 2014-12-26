package helpmaker.view;

import helpmaker.model.Link;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class InsertLinkController {
    @FXML
    private TextField tfLink;
    @FXML
    private TextField tfCaption;
    @FXML
    private Button btnOK;
    @FXML
    private Button btnCancel;

    private Link link;

    private Stage dialogStage;

    public InsertLinkController() {  }

    public void onOKClick() {
        link.setCaption(tfCaption.getText());
        link.setLink(tfLink.getText());
        this.dialogStage.close();
    }

    public void onCancelClick() {
        this.dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        dialogStage.getIcons().add(new Image("helpmaker/res/link.png"));
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
        if (link != null) {
            tfCaption.setText(link.getCaption());
            tfLink.setText(link.getLink());
        }
    }
}
