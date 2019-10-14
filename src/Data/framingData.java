package Data;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class framingData extends RecursiveTreeObject<framingData> {

    private final SimpleStringProperty section, height, length, type, stud, spacing;

    public framingData(String section, String height, String length, String type, String stud, String spacing) {
        this.section = new SimpleStringProperty(section);
        this.height = new SimpleStringProperty(height);
        this.length = new SimpleStringProperty(length);
        this.type = new SimpleStringProperty(type);
        this.stud = new SimpleStringProperty(stud);
        this.spacing = new SimpleStringProperty(spacing);
    }

    public String getSection() {
        return section.get();
    }

    public void setSection(String section) {
        this.section.set(section);
    }

    public String getHeight() {
        return height.get();
    }

    public void setHeight(String height) {
        this.height.set(height);
    }

    public String getLength() {
        return length.get();
    }

    public void setLength(String length) {
        this.length.set(length);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getStud() {
        return stud.get();
    }

    public void setStud(String stud) {
        this.stud.set(stud);
    }

    public String getSpacing() {
        return spacing.get();
    }

    public void setSpacing(String spacing) {
        this.spacing.set(spacing);
    }
}
