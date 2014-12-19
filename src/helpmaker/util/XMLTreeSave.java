package helpmaker.util;

import helpmaker.model.XMLNodeItemExtended;
import helpmaker.model.XMLParameter;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class XMLTreeSave {
    public static void saveTreeToFile(TreeItem<XMLNodeItemExtended> rootItem, String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fileName))));
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
            writer.newLine();
            writeNode(writer, rootItem);
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeNode(BufferedWriter writer, TreeItem<XMLNodeItemExtended> node) throws IOException {
        writer.write("<"+node.getValue().getNodeName());
        writeCurrentParameters(writer, node);
        writer.write(">");
        ObservableList<TreeItem<XMLNodeItemExtended>> childList = node.getChildren();
        for (TreeItem<XMLNodeItemExtended> currentChild : childList) {
            writeNode(writer, currentChild);
        }
        writer.write("</"+node.getValue().getNodeName()+">");
    }

    private static void writeCurrentParameters(BufferedWriter writer, TreeItem<XMLNodeItemExtended> node) throws IOException {
        List<XMLParameter> parameters = node.getValue().getParameters();
        writer.write(" "+XMLParameter.PARAMETER_CAPTION+"=\""+node.getValue().getCaption()+"\"");
        for (XMLParameter param : parameters) {
            writer.write(" "+param.getKey()+"=\""+param.getValue()+"\"");
        }
    }
}
