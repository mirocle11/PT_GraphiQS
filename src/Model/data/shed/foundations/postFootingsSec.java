package Model.data.shed.foundations;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class postFootingsSec extends RecursiveTreeObject<postFootingsSec> {

    private SimpleStringProperty no, depth, width, length, qty;

    public postFootingsSec(String no, String depth, String width, String length, String qty) {
        this.no = new SimpleStringProperty(no);
        this.depth = new SimpleStringProperty(depth);
        this.width = new SimpleStringProperty(width);
        this.length = new SimpleStringProperty(length);
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
