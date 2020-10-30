package Model.data.layouts;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;

public class externalFramingColumnsData extends RecursiveTreeObject<externalFramingColumnsData> {

    private SimpleStringProperty no, quantity, material;
    private Label image;

    public externalFramingColumnsData(String no, Label image, String quantity, String material) {
        this.no = new SimpleStringProperty(no);
        this.image = image;
        this.quantity = new SimpleStringProperty(quantity);
        this.material = new SimpleStringProperty(material);
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

    public String getMaterial() {
        return material.get();
    }

    public SimpleStringProperty materialProperty() {
        return material;
    }

    public void setMaterial(String material) {
        this.material.set(material);
    }
}
