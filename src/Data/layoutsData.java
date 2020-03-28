package Data;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;

public class layoutsData extends RecursiveTreeObject<layoutsData> {

    private SimpleStringProperty no, page, measurement, structure, wall_type, wall, material, value, stud_height,
            volume, lobour;

    private Label color;

    public layoutsData(String no, String page, String measurement, String structure, String wall_type, String wall,
                       String material, Label color, String value, String stud_height, String volume, String lobour) {

        this.no = new SimpleStringProperty(no);
        this.page = new SimpleStringProperty(page);
        this.measurement = new SimpleStringProperty(measurement);
        this.structure = new SimpleStringProperty(structure);
        this.wall_type = new SimpleStringProperty(wall_type);
        this.wall = new SimpleStringProperty(wall);
        this.material = new SimpleStringProperty(material);
        this.color = color;
        this.value = new SimpleStringProperty(value);
        this.stud_height = new SimpleStringProperty(stud_height);
        this.volume = new SimpleStringProperty(volume);
        this.lobour = new SimpleStringProperty(lobour);
    }

    public String getNo() {
        return no.get();
    }

    public SimpleStringProperty noProperty() {
        return no;
    }

    public void setNo(String no) {
        this.no.set(no);
    }

    public String getPage() {
        return page.get();
    }

    public SimpleStringProperty pageProperty() {
        return page;
    }

    public void setPage(String page) {
        this.page.set(page);
    }

    public String getMeasurement() {
        return measurement.get();
    }

    public SimpleStringProperty measurementProperty() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement.set(measurement);
    }

    public String getStructure() {
        return structure.get();
    }

    public SimpleStringProperty structureProperty() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure.set(structure);
    }

    public String getWall_type() {
        return wall_type.get();
    }

    public SimpleStringProperty wall_typeProperty() {
        return wall_type;
    }

    public void setWall_type(String wall_type) {
        this.wall_type.set(wall_type);
    }

    public String getWall() {
        return wall.get();
    }

    public SimpleStringProperty wallProperty() {
        return wall;
    }

    public void setWall(String wall) {
        this.wall.set(wall);
    }

    public String getMaterial() {
        return material.get();
    }

    public SimpleStringProperty materialProperty() {
        return material;
    }

    public void setMaterial(String material) {
        this.material.set(material);
    }

    public Label getColor() {
        return color;
}

    public void setColor(Label color) {
        this.color = color;
    }

    public String getValue() {
        return value.get();
    }

    public SimpleStringProperty valueProperty() {
        return value;
    }

    public void setValue(String value) {
        this.value.set(value);
    }


    public String getStud_height() {
        return stud_height.get();
    }

    public SimpleStringProperty stud_heightProperty() {
        return stud_height;
    }

    public void setStud_height(String stud_height) {
        this.stud_height.set(stud_height);
    }

    public String getVolume() {
        return volume.get();
    }

    public SimpleStringProperty volumeProperty() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume.set(volume);
    }

    public String getLobour() {
        return lobour.get();
    }

    public SimpleStringProperty lobourProperty() {
        return lobour;
    }

    public void setLobour(String lobour) {
        this.lobour.set(lobour);
    }
}
