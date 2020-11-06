package Model;


import java.util.ArrayList;
/**
 * Created by User on 03/11/2020.
 */
public class ComponentData {
    public String structure;
    public String part;
    public String section;
    public ArrayList<String[]> components = new ArrayList<>();

    public ComponentData(String structure, String part, String section) {
        this.structure = structure;
        this.part = part;
        this.section = section;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public ArrayList<String[]> getComponents() {
        return components;
    }

    public void setComponents(ArrayList<String[]> components) {
        this.components = components;
    }

}
