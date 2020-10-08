package Model.data.layouts;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class foundationsStampData extends RecursiveTreeObject<foundationsStampData> {

    private final SimpleStringProperty no, part, quantity, depth, width, length, diameter, height, volume;

    public foundationsStampData(String no, String part, String quantity, String depth, String width, String length,
                                String diameter, String height, String volume) {

        this.no = new SimpleStringProperty(no);
        this.part = new SimpleStringProperty(part);
        this.quantity = new SimpleStringProperty(quantity);
        this.depth = new SimpleStringProperty(depth);
        this.width = new SimpleStringProperty(width);
        this.length = new SimpleStringProperty(length);
        this.diameter = new SimpleStringProperty(diameter);
        this.height = new SimpleStringProperty(height);
        this.volume = new SimpleStringProperty(volume);
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

    public String getPart() {
        return part.get();
    }

    public SimpleStringProperty partProperty() {
        return part;
    }

    public void setPart(String part) {
        this.part.set(part);
    }

    public String getDepth() {
        return depth.get();
    }

    public SimpleStringProperty depthProperty() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth.set(depth);
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

    public String getLength() {
        return length.get();
    }

    public SimpleStringProperty lengthProperty() {
        return length;
    }

    public void setLength(String length) {
        this.length.set(length);
    }

    public String getQuantity() {
        return quantity.get();
    }

    public SimpleStringProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity.set(quantity);
    }

    public String getDiameter() {
        return diameter.get();
    }

    public SimpleStringProperty diameterProperty() {
        return diameter;
    }

    public void setDiameter(String diameter) {
        this.diameter.set(diameter);
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

    public String getVolume() {
        return volume.get();
    }

    public SimpleStringProperty volumeProperty() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume.set(volume);
    }
}
