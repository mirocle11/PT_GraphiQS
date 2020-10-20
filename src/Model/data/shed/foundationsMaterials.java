package Model.data.shed;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class foundationsMaterials extends RecursiveTreeObject<foundationsMaterials> {

    private SimpleStringProperty components, sku_number, description, unit, quantity, usage;

    public foundationsMaterials(String components, String sku_number, String description, String unit, String quantity,
                                String usage) {

        this.components = new SimpleStringProperty(components);
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

    public String getComponents() {
        return components.get();
    }

    public SimpleStringProperty componentsProperty() {
        return components;
    }

    public void setComponents(String components) {
        this.components.set(components);
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
