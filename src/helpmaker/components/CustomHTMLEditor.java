package helpmaker.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.HTMLEditor;

public final class CustomHTMLEditor extends HTMLEditor {
    private Button btnAddLink;
    private Button btnSave;
    private boolean pageChanged;

    public CustomHTMLEditor() {
        addSaveButton();
        addLinkButton();
        pageChanged = false;
        this.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                pageChanged = true;
            }
        });
    }

    private void addLinkButton() {
        ImageView graphic = new ImageView(new Image("helpmaker/res/link.png", 16, 16, true, true));
        btnAddLink = new Button("", graphic);
        addNodeToTopToolbar(btnAddLink);
    }

    private void addSaveButton() {
        ImageView graphic = new ImageView(new Image("helpmaker/res/save.png", 16, 16, true, true));
        btnSave = new Button("", graphic);
        addNodeToTopToolbar(btnSave);
    }

    private void addNodeToTopToolbar(Node newNode) {
        Node node = this.lookup(".top-toolbar");
        if (node instanceof ToolBar) {
            ToolBar bar = (ToolBar) node;
            bar.getItems().add(newNode);
        }
    }

    public void setBtnSaveSetOnAction(EventHandler<ActionEvent> eventHandler) {
        btnSave.setOnAction(eventHandler);
    }

    public void setBtnAddLinkSetOnAction(EventHandler<ActionEvent> eventHandler) {
        btnAddLink.setOnAction(eventHandler);
    }



    public boolean pageChanged() {
        return pageChanged;
    }

    public void setPageChanged(boolean state) {
        pageChanged = state;
    }
}
