package Model.data.shed;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class claddingOptData extends RecursiveTreeObject<claddingOptData> {

    private SimpleStringProperty no, sku_number, description, unit;

    public claddingOptData(String no, String sku_number, String description, String unit) {
        this.no = new SimpleStringProperty(no);
        this.sku_number = new SimpleStringProperty(sku_number);
        this.description = new SimpleStringProperty(description);
        this.unit = new SimpleStringProperty(unit);
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

    public String getSku_number() {
        return sku_number.get();
    }

    public SimpleStringProperty sku_numberProperty() {
        return sku_number;
    }

    public void setSku_number(String sku_number) {
        this.sku_number.set(sku_number);
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getUnit() {
        return unit.get();
    }

    public SimpleStringProperty unitProperty() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit.set(unit);
    }
}
