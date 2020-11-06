package Model.data.shed.externalFraming;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class columnsSec extends RecursiveTreeObject<columnsSec> {

    private SimpleStringProperty no, qty, length;

    public columnsSec(String no, String qty, String length) {
        this.no = new SimpleStringProperty(no);
        this.qty = new SimpleStringProperty(qty);
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

    public String getQty() {
        return qty.get();
    }

    public SimpleStringProperty qtyProperty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty.set(qty);
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
