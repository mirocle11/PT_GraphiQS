package Model.Sheets;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class foundationsData extends RecursiveTreeObject<foundationsData> {

    private int section;
    private String set, set_override;
    private SimpleStringProperty no, qty, depth, width, length;

    public foundationsData(String no, String qty, String depth, String width, String length) {
        this.no = new SimpleStringProperty(no);
        this.qty = new SimpleStringProperty(qty);
        this.depth = new SimpleStringProperty(depth);
        this.width = new SimpleStringProperty(width);
        this.length = new SimpleStringProperty(length);
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public String getSet_override() {
        return set_override;
    }

    public void setSet_override(String set_override) {
        this.set_override = set_override;
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

}
