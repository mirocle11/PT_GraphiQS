package Model.data.layouts;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;

public class externalFramingColumnsData extends RecursiveTreeObject<externalFramingColumnsData> {

    private SimpleStringProperty no, quantity, length;
    private Label image;

    public externalFramingColumnsData(String no, Label image, String quantity, String length) {
        this.no = new SimpleStringProperty(no);
        this.image = image;
        this.quantity = new SimpleStringProperty(quantity);
        this.length = new SimpleStringProperty(length);
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

    public String getQuantity() {
        return quantity.get();
    }

    public SimpleStringProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity.set(quantity);
    }

    public Label getImage() {
        return image;
    }

    public void setImage(Label image) {
        this.image = image;
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
}
