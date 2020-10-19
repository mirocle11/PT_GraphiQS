package Model.data.layouts;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;

public class foundationsPostFootingsData extends RecursiveTreeObject<foundationsPostFootingsData> {

    private final SimpleStringProperty no, quantity, depth, width, length, volume;
    private Label image;

    public foundationsPostFootingsData(String no, Label image, String quantity, String depth, String width,
                                       String length, String volume) {

        this.no = new SimpleStringProperty(no);
        this.image = image;
        this.quantity = new SimpleStringProperty(quantity);
        this.depth = new SimpleStringProperty(depth);
        this.width = new SimpleStringProperty(width);
        this.length = new SimpleStringProperty(length);
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

    public String getVolume() {
        return volume.get();
    }

    public SimpleStringProperty volumeProperty() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume.set(volume);
    }

    public Label getImage() {
        return image;
    }

    public void setImage(Label image) {
        this.image = image;
    }
}
