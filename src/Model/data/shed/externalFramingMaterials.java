package Model.data.shed;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class externalFramingMaterials extends RecursiveTreeObject<externalFramingMaterials> {

    private SimpleStringProperty component, sku_number, description, unit, quantity, usage;

    public externalFramingMaterials(String component, String sku_number, String description, String unit, String quantity,
                                String usage) {

        this.component = new SimpleStringProperty(component);
        this.sku_number = new SimpleStringProperty(sku_number);
        this.description = new SimpleStringProperty(description);
        this.unit = new SimpleStringProperty(unit);
        this.quantity = new SimpleStringProperty(quantity);
        this.usage = new SimpleStringProperty(usage);
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

    public String getQuantity() {
        return quantity.get();
    }

    public SimpleStringProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity.set(quantity);
    }

    public String getComponent() {
        return component.get();
    }

    public SimpleStringProperty componentProperty() {
        return component;
    }

    public void setComponent(String component) {
        this.component.set(component);
    }

    public String getUsage() {
        return usage.get();
    }

    public SimpleStringProperty usageProperty() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage.set(usage);
    }
}
