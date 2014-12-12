package helpmaker.model;

import helpmaker.util.XMLTreeSave;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;

public class HelpXMLTree {
    private String fileName;
    private TreeItem<XMLNodeItemExtended> rootItem;

    public HelpXMLTree(String fileName) {
        this.fileName = fileName;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); //создали фабрику строителей, сложный и грамосткий процесс (по реже выполняйте это действие)
//        dbf.setValidating(false); // не делать проверку валидации
        try {
            DocumentBuilder db = dbf.newDocumentBuilder(); // создали конкретного строителя документа
            Document doc = db.parse(new File(this.fileName)); // строитель построил документ
            rootItem = new TreeItem<>(new XMLNodeItemExtended(doc.getLocalName()));
            rootItem.setExpanded(true);
            parseCurrentNode(doc, rootItem);
            if (rootItem.getChildren().size() > 0)
                rootItem = rootItem.getChildren().get(0);
            //Document - тоже является нодом, и импленментирует методы
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException sax) {
            sax.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public TreeItem getRootTreeItem() {
        return rootItem;
    }

    private void parseCurrentNode(Node node, TreeItem<XMLNodeItemExtended> currXMLNode) {
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node childNode = list.item(i); // текущий нод
            TreeItem<XMLNodeItemExtended> item = new TreeItem<>(
                    new XMLNodeItemExtended(childNode.getAttributes().getNamedItem(XMLParameter.PARAMETER_CAPTION).getNodeValue()));
            item.setGraphic(getIcon(childNode.hasChildNodes()));
            currXMLNode.getChildren().add(item);
            item.getValue().setNodeName(childNode.getNodeName());
            readParameters(childNode, item);
            parseCurrentNode(childNode, item);
        }
    }
    private void readParameters(Node XMLnode, TreeItem<XMLNodeItemExtended> treeNode) {
        if (XMLnode instanceof Element){
            Element e = (Element) XMLnode;
            NamedNodeMap nodeMap =  e.getAttributes();
            for (int i = 0; i < nodeMap.getLength(); i++) {
                Node currNode = nodeMap.item(i);
                treeNode.getValue().addParameter(currNode.getNodeName(), currNode.getNodeValue());
            }
        }
    }

    public static javafx.scene.Node getIcon(boolean isDirectory) {
        String iconName;
        if (isDirectory)
            iconName = "Folder.png";
        else
            iconName = "document.png";
        return new ImageView(new Image("helpmaker/res/"+iconName));
    }

    public void saveTree() {
        XMLTreeSave.saveTreeToFile(rootItem, "help_out.xml");
    }

    public boolean deleteItem(TreeItem<XMLNodeItemExtended> item) {
        return delItem(rootItem, item);
    }

    private boolean delItem(TreeItem<XMLNodeItemExtended> tree, TreeItem<XMLNodeItemExtended> itemToDelete) {
        if (tree.equals(itemToDelete)) {
            tree.getParent().getChildren().remove(tree);
            return true;
        } else {
            for (int i = 0; i < tree.getChildren().size(); i++) {
                if (delItem(tree.getChildren().get(i), itemToDelete))
                    return true;
            }
            return false;
        }
    }

    public static boolean isTreeItemHasChildWithCaption(TreeItem<XMLNodeItemExtended> item, String caption) {
        for (int i = 0; i < item.getChildren().size(); i++) {
            if (item.getChildren().get(i).getValue().getCaption().equals(caption))
                return true;
        }
        return false;
    }

    public static boolean isTreeHasSameID(TreeItem<XMLNodeItemExtended> item, final String id) {
        if (item.getValue().getParameterValueByKey(XMLParameter.PARAMETER_ID).equals(id))
            return true;
        for (int i = 0; i < item.getChildren().size(); i++) {
            if (isTreeHasSameID(item.getChildren().get(i), id))
                return true;
        }
        return false;
    }
}