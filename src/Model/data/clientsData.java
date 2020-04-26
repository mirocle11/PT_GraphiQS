package Model.data;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class clientsData extends RecursiveTreeObject<clientsData> {

    private SimpleStringProperty id, full_name;

    public clientsData(String id, String full_name) {

        this.id = new SimpleStringProperty(id);
        this.full_name = new SimpleStringProperty(full_name);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getFull_name() {
        return full_name.get();
    }

    public SimpleStringProperty full_nameProperty() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name.set(full_name);
    }
}
