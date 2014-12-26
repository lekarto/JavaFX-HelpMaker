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
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class CustomHTMLEditor extends HTMLEditor {
    private Button btnAddLink;
    private Button btnSave;
    private boolean pageChanged;
    private WebView webView;

    private static final String JS_GET_SELECTED_TEXT =
            "(function getSelectionText() {\n" +
                    "    var text = \"\";\n" +
                    "    if (window.getSelection) {\n" +
                    "        text = window.getSelection().toString();\n" +
                    "    } else if (document.selection && document.selection.type != \"Control\") {\n" +
                    "        text = document.selection.createRange().text;\n" +
                    "    }\n" +
                    "    return text;\n" +
                    "})()";
    private static final String JS_CREATE_LINK =
                    "(function createLink(link, caption) {\n" +
                    "        var selRange = window.getSelection().getRangeAt(0);\n" +
                    "        var span = document.createElement('span');\n" +
                    "        span.innerHTML = '<a href=\\'' + link + '\\'>' + caption + '</a>';\n" +
                    "        selRange.deleteContents();\n" +
                    "        selRange.insertNode(span);\n" +

                    "})";

    private static final String HTMLStyle = "<style>p{margin-top:3px;margin-bottom:3px;}</style>";

    public CustomHTMLEditor() {
        addSaveButton();
        addLinkButton();
        pageChanged = false;
        webView = (WebView) this.lookup("WebView");
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

    public void saveHTMLPageToFile(String fileName) {
        if ((pageChanged()) && (!fileName.equals(""))) {
            String resultHTML;
            if (getHtmlText().contains(HTMLStyle))
                resultHTML = this.getHtmlText();
            else
                resultHTML = getHtmlText().replace("<head>", "<head>"+ HTMLStyle);
            resultHTML = resultHTML.replace("contenteditable=\"true\"", "");
            try {
                BufferedWriter outBuffer = new BufferedWriter(new FileWriter(new File(fileName)));
                outBuffer.write(resultHTML);
                outBuffer.flush();
                outBuffer.close();
                setPageChanged(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setPageChanged(false);
    }

    public String getSelectedText() {
        if (webView == null) return "";
        return (String)webView.getEngine().executeScript(JS_GET_SELECTED_TEXT);
    }

    public void insertLink(String link, String caption) {
        if (webView == null) return;
        WebEngine engine = webView.getEngine();
        engine.executeScript(JS_CREATE_LINK+"('"+link+"','"+caption+"')");
    }

}
