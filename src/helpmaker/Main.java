package helpmaker;

import helpmaker.model.Link;
import helpmaker.model.XMLParameter;
import helpmaker.view.InsertLinkController;
import helpmaker.view.MainController;
import helpmaker.view.XMLNodeNewController;
import helpmaker.view.XMLParameterEditController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private SplitPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("HelpMaker");
        this.primaryStage.getIcons().add(new Image("helpmaker/res/favicon.png"));
        initRootLayout();
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/main.fxml"));
            rootLayout = loader.load();
            MainController controller = loader.getController();
            controller.setMainApp(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(handle -> {
                controller.btnSaveTreeClick();
                controller.saveHTMLFile();
            });
            primaryStage.show();
            controller.initializeAfterShow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showXMLParameterEditDialog(XMLParameter xmlParameter, String windowName, Image icon) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/XMLParameterEdit.fxml"));
            AnchorPane panel = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(windowName);
            dialogStage.getIcons().add(icon);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setResizable(false);
            Scene scene = new Scene(panel);
            dialogStage.setScene(scene);

            XMLParameterEditController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setXmlParameter(xmlParameter);
            dialogStage.showAndWait();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String showXMLNodeNewDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/XMLNodeNew.fxml"));
            AnchorPane panel = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add document");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setResizable(false);
            Scene scene = new Scene(panel);
            dialogStage.setScene(scene);
            XMLNodeNewController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
            return controller.getCaption();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public Link showInsertLinkDialog(String caption) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/InsertLink.fxml"));
            AnchorPane panel = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Insert Link");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setResizable(false);
            Scene scene = new Scene(panel);
            dialogStage.setScene(scene);
            Link link = new Link(caption);
            InsertLinkController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setLink(link);
            dialogStage.showAndWait();
            return controller.getLink();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}