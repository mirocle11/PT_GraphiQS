package Model.data.shed;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class foundationsData extends RecursiveTreeObject<foundationsData> {

    private final SimpleStringProperty component, code, description, extra1, extra2, quantity, usage, waste, subheading, usage2;

    public foundationsData(String component, String code, String description, String extra1, String extra2,
                           String quantity, String usage, String waste, String subheading, String usage2) {

        this.component = new SimpleStringProperty(component);
        this.code = new SimpleStringProperty(code);
        this.description = new SimpleStringProperty(description);
        this.extra1 = new SimpleStringProperty(extra1);
        this.extra2 = new SimpleStringProperty(extra2);
        this.quantity = new SimpleStringProperty(quantity);
        this.usage = new SimpleStringProperty(usage);
        this.waste = new SimpleStringProperty(waste);
        this.subheading = new SimpleStringProperty(subheading);
        this.usage2 = new SimpleStringProperty(usage2);
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

    public String getCode() {
        return code.get();
    }

    public SimpleStringProperty codeProperty() {
        return code;
    }

    public void setCode(String code) {
        this.code.set(code);
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

    public String getExtra1() {
        return extra1.get();
    }

    public SimpleStringProperty extra1Property() {
        return extra1;
    }

    public void setExtra1(String extra1) {
        this.extra1.set(extra1);
    }

    public String getExtra2() {
        return extra2.get();
    }

    public SimpleStringProperty extra2Property() {
        return extra2;
    }

    public void setExtra2(String extra2) {
        this.extra2.set(extra2);
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

    public String getUsage() {
        return usage.get();
    }

    public SimpleStringProperty usageProperty() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage.set(usage);
    }

    public String getWaste() {
        return waste.get();
    }

    public SimpleStringProperty wasteProperty() {
        return waste;
    }

    public void setWaste(String waste) {
        this.waste.set(waste);
    }

    public String getSubheading() {
        return subheading.get();
    }

    public SimpleStringProperty subheadingProperty() {
        return subheading;
    }

    public void setSubheading(String subheading) {
        this.subheading.set(subheading);
    }

    public String getUsage2() {
        return usage2.get();
    }

    public SimpleStringProperty usage2Property() {
        return usage2;
    }

    public void setUsage2(String usage2) {
        this.usage2.set(usage2);
    }
}
