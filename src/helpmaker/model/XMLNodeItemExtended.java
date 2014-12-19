package helpmaker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XMLNodeItemExtended {
    private String caption;
    private List<XMLParameter> parameters;
    private String nodeName;

    public int isParameterExist(String key) {
        for (int i = 0; i < parameters.size(); i++) {
            if (parameters.get(i).getKey().equals(key))
                return i;
        }
        return -1;
    }

    public int updateParameter(String key, String value) {
        if (key.equals(XMLParameter.PARAMETER_CAPTION)) {
            this.caption = value;
            return 0;
        } else {
            int i = isParameterExist(key);
            if (i  >= 0) {
                parameters.get(i).setValue(value);
                return i;
            } else {
                parameters.add(new XMLParameter(key, value));
                return parameters.size() - 1;
            }
        }
    }

    public void addParameter(String key, String value) {
        updateParameter(key, value);
    }

    public void addParameter(XMLParameter xmlParameter) {
        updateParameter(xmlParameter.getKey(), xmlParameter.getValue());
    }

    public void deleteParameter(XMLParameter xmlParameter) {
        deleteParameter(xmlParameter.getKey());
    }

    public void deleteParameter(String key) {
        int i = isParameterExist(key);
        if (i >= 0) {
            parameters.remove(i);
        }
    }

    public void deleteParameter(int index) {
        if ((index >= 0) && (parameters.size() > index)) {
            parameters.remove(index);
        }
    }

    public XMLNodeItemExtended(String caption, ArrayList<XMLParameter> params) {
        this.caption = caption;
        parameters = params;
    }

    public XMLNodeItemExtended(String caption) {
        this.caption = caption;
        this.nodeName = "node";
        parameters = new ArrayList<>();
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
        updateParameter(XMLParameter.PARAMETER_CAPTION, caption);
    }

    public List<XMLParameter> getParameters() {
        return Collections.unmodifiableList(parameters);
    }



    public String getParameterValueByKey(String key) {
        int index = isParameterExist(key);
        if (index >= 0) {
            return parameters.get(index).getValue();
        } else {
            return null;
        }
    }

    public void setParameters(ArrayList<XMLParameter> parameters) {
        this.parameters = parameters;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    @Override
    public String toString() {
        return caption;
    }
}
