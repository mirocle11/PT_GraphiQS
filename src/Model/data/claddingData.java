package Model.data;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class claddingData extends RecursiveTreeObject<claddingData> {

    private SimpleStringProperty no, cladding_name, length;

    public claddingData(String no, String cladding_name, String length) {

        this.no = new SimpleStringProperty(no);
        this.cladding_name = new SimpleStringProperty(cladding_name);
        this.length = new SimpleStringProperty(length);
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

    public String getCladding_name() {
        return cladding_name.get();
    }

    public SimpleStringProperty cladding_nameProperty() {
        return cladding_name;
    }

    public void setCladding_name(String cladding_name) {
        this.cladding_name.set(cladding_name);
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
