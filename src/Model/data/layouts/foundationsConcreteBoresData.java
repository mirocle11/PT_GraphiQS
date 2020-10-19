package Model.data.layouts;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;

public class foundationsConcreteBoresData extends RecursiveTreeObject<foundationsConcreteBoresData> {

    private final SimpleStringProperty no, quantity, diameter, height, volume;
    private Label image;

    public foundationsConcreteBoresData(String no, Label image, String quantity, String diameter,
                                        String height, String volume) {

        this.no = new SimpleStringProperty(no);
        this.image = image;
        this.quantity = new SimpleStringProperty(quantity);
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
}
