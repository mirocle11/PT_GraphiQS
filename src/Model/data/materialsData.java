package Model.data;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class materialsData extends RecursiveTreeObject<materialsData> {

    private SimpleStringProperty qty, sku_number, description, unit;

    public materialsData( String sku_number, String description, String unit,String qty) {
        this.qty = new SimpleStringProperty(qty);
        this.sku_number = new SimpleStringProperty(sku_number);
        this.description = new SimpleStringProperty(description);
        this.unit = new SimpleStringProperty(unit);
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
