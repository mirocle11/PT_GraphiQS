package Model.data.shed.foundations;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by User on 19/10/2020.
 */
public class concreteBoresSec extends RecursiveTreeObject<concreteBoresSec> {

    private SimpleStringProperty no, qty, diameter, height, volume;

    public concreteBoresSec(String no,String qty, String diameter, String height, String volume) {
        this.no = new SimpleStringProperty(no);
        this.qty = new SimpleStringProperty(qty);
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

    public String getQty() {
        return qty.get();
    }

    public SimpleStringProperty qtyProperty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty.set(qty);
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
