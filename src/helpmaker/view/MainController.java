package helpmaker.view;

import helpmaker.Main;
import helpmaker.additional.XMLParameterEditDetails;
import helpmaker.components.CustomHTMLEditor;
import helpmaker.model.*;
import helpmaker.util.IDGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.io.*;

public class MainController {

    @FXML
    private TreeView XMLTreeView;
    @FXML
    private TableView XMLNodeParameters;
    @FXML
    private TableColumn<XMLParameter, String> keyColumn;
    @FXML
    private TableColumn<XMLParameter, String> valueColumn;
    @FXML
    private AnchorPane rightPane;
    private CustomHTMLEditor htmlEditor;

    private ObservableList<XMLParameter> parameters = FXCollections.observableArrayList();
    private XMLNodeItemExtended currentSelectedXMLNodeItemExtended;
    private HelpXMLTree XMLTreeModel;
    private Main mainApp;


    public MainController() { }

    public void initialize() {
        rightPane.getChildren().remove(0, rightPane.getChildren().size());

        htmlEditor = new CustomHTMLEditor();
        rightPane.getChildren().add(htmlEditor);
        htmlEditor.setBtnSaveSetOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                htmlEditor.setPageChanged(true);
                saveHTMLFile();
            }
        });

        htmlEditor.setBtnAddLinkSetOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Link link = mainApp.showInsertLinkDialog(htmlEditor.getSelectedText());
                if ((link != null) && (link.linkValid()))
                    htmlEditor.insertLink(link.getLink(), link.getCaption());
            }
        });

        XMLTreeModel = new HelpXMLTree("Help/help.xml");
        XMLTreeView.setRoot(XMLTreeModel.getRootTreeItem());
        XMLTreeView.setEditable(true);
        XMLTreeView.setCellFactory(new Callback<TreeView, TreeCell>() {
            @Override
            public TreeCell call(TreeView param) {
                return new TextFieldTreeCellImpl();
            }
        });
        keyColumn.setCellValueFactory(cellData -> cellData.getValue().getKeyProperty());
        valueColumn.setCellValueFactory(cellData -> cellData.getValue().getValueProperty());
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    public void btnSaveTreeClick() {
        XMLTreeModel.saveTree();
    }

    public void onTreeViewClick() {
        XMLNodeItemExtended justSelectedNode = ((TreeItem<XMLNodeItemExtended>) XMLTreeView.getFocusModel().getFocusedItem()).getValue();
        if ((justSelectedNode != null) && (currentSelectedXMLNodeItemExtended != justSelectedNode)) {
            saveHTMLFile();
            currentSelectedXMLNodeItemExtended = justSelectedNode;
            parameters.remove(0, parameters.size());
            parameters = FXCollections.observableArrayList(currentSelectedXMLNodeItemExtended.getParameters());
            XMLNodeParameters.setItems(parameters);
            readFileToHTMLEditor(currentSelectedXMLNodeItemExtended.getParameterValueByKey(XMLParameter.PARAMETER_ID));
        }
    }

    public void onAddXMLParameterClick() {
        int selectedXMLNodeIndex = XMLTreeView.getSelectionModel().getSelectedIndex();
        if (selectedXMLNodeIndex >= 0) {
            XMLParameter xmlParameter = new XMLParameter();
            mainApp.showXMLParameterEditDialog(xmlParameter,
                                               XMLParameterEditDetails.XMLPARAM_WINDOWNAME_ADD,
                                               XMLParameterEditDetails.getIconAdd());
            if (xmlParameter.isValid()) {
                currentSelectedXMLNodeItemExtended.addParameter(xmlParameter);
                XMLNodeParameters.getItems().add(XMLNodeParameters.getItems().size(), xmlParameter);
            }
        }
    }

    public  void onEditXMLParameterClick() {
        int selectedIndex = XMLNodeParameters.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            XMLParameter xmlParameter = (XMLParameter) XMLNodeParameters.getSelectionModel().getSelectedItem();
            mainApp.showXMLParameterEditDialog(xmlParameter,
                                               XMLParameterEditDetails.XMLPARAM_WINDOWNAME_EDIT,
                                               XMLParameterEditDetails.getIconEdit());
        }
    }

    public void onDeleteXMLParameterClick() {
        int selectedIndex = XMLNodeParameters.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            currentSelectedXMLNodeItemExtended.deleteParameter(selectedIndex);
            XMLNodeParameters.getItems().remove(selectedIndex);
        }
    }

    private void readFileToHTMLEditor(String fileName) {
        String text = "";
        if (!fileName.equals(""))
            try {
                FileInputStream inFile = new FileInputStream("Help/" + fileName + ".htm");
                byte[] str = new byte[inFile.available()];
                inFile.read(str);
                text = new String(str);
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
                e.printStackTrace();
            }
        htmlEditor.setHtmlText(text);
    }

    public void saveHTMLFile() {
        if (currentSelectedXMLNodeItemExtended != null)
            htmlEditor.saveHTMLPageToFile(
                    "Help/" +
                    currentSelectedXMLNodeItemExtended.getParameterValueByKey(XMLParameter.PARAMETER_ID) + ".htm");
    }

    public void onNewDocumentClick() {
        TreeItem<XMLNodeItemExtended> selectedTreeItem = (TreeItem<XMLNodeItemExtended>) XMLTreeView.getFocusModel().getFocusedItem();
        if (selectedTreeItem != null)  {
            String newTreeItemCaption = mainApp.showXMLNodeNewDialog();
            if ((!newTreeItemCaption.equals("")) &&
                (!HelpXMLTree.isTreeItemHasChildWithCaption(selectedTreeItem, newTreeItemCaption))) {
                TreeItem<XMLNodeItemExtended> newTreeItem =
                                new TreeItem<>(new XMLNodeItemExtended(newTreeItemCaption));
                newTreeItem.setGraphic(HelpXMLTree.getIcon(false));
                String idNumber;
                do {
                    idNumber = IDGenerator.generateID();
                } while (HelpXMLTree.isTreeHasSameID(XMLTreeModel.getRootTreeItem(), idNumber));
                newTreeItem.getValue().addParameter(XMLParameter.PARAMETER_ID, idNumber);
                selectedTreeItem.getChildren().add(newTreeItem);
                selectedTreeItem.setGraphic(HelpXMLTree.getIcon(true));
            }
        }
    }

    public void onDeleteDocumentClick() {
        TreeItem<XMLNodeItemExtended> selectedTreeItem = (TreeItem<XMLNodeItemExtended>) XMLTreeView.getFocusModel().getFocusedItem();
        if (selectedTreeItem != null) {
            if (selectedTreeItem.getParent().getChildren().size() == 1)
                selectedTreeItem.getParent().setGraphic(HelpXMLTree.getIcon(false));
            XMLTreeModel.deleteItem(selectedTreeItem);
        }
    }
}