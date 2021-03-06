package Model.data;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class HistoryData extends RecursiveTreeObject<HistoryData> {

    public SimpleStringProperty type, measurement,color;


    public HistoryData(String  colors, String type, String measurement) {
        this.type = new SimpleStringProperty(type);
        this.measurement = new SimpleStringProperty(measurement);
        this.color = new SimpleStringProperty(colors);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getMeasurement() {
        return measurement.get();
    }

    public SimpleStringProperty measurementProperty() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement.set(measurement);
    }
}
