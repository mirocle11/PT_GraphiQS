package Model.data.shed.externalFraming;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class columnsSec extends RecursiveTreeObject<columnsSec> {

    private SimpleStringProperty no, qty;

    public columnsSec(String no, String qty) {
        this.no = new SimpleStringProperty(no);
        this.qty = new SimpleStringProperty(qty);
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
}
