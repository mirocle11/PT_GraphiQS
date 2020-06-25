package Model.data;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class windowData extends RecursiveTreeObject<windowData> {

    private SimpleStringProperty no, window_no, cladding, type, width, height;

    public windowData(String no, String window_no, String cladding, String type, String width, String height) {

        this.no = new SimpleStringProperty(no);
        this.window_no = new SimpleStringProperty(window_no);
        this.cladding = new SimpleStringProperty(cladding);
        this.type = new SimpleStringProperty(type);
        this.width = new SimpleStringProperty(width);
        this.height = new SimpleStringProperty(height);
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

    public String getWindow_no() {
        return window_no.get();
    }

    public SimpleStringProperty window_noProperty() {
        return window_no;
    }

    public void setWindow_no(String window_no) {
        this.window_no.set(window_no);
    }

    public String getCladding() {
        return cladding.get();
    }

    public SimpleStringProperty claddingProperty() {
        return cladding;
    }

    public void setCladding(String cladding) {
        this.cladding.set(cladding);
    }

    public String getWidth() {
        return width.get();
    }

    public SimpleStringProperty widthProperty() {
        return width;
    }

    public void setWidth(String width) {
        this.width.set(width);
    }

    public String getHeight() {
        return height.get();
    }

    public SimpleStringProperty heightProperty() {
        return height;
    }

    public void setHeight(String height) {
        this.height.set(height);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }
}
