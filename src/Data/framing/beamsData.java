package Data.framing;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class beamsData extends RecursiveTreeObject<beamsData> {

    private final SimpleStringProperty beam_no, beam, length, type, size;

    public beamsData(String beam_no, String beam, String length, String type, String size) {
        this.beam_no = new SimpleStringProperty(beam_no);
        this.beam = new SimpleStringProperty(beam);
        this.length = new SimpleStringProperty(length);
        this.type = new SimpleStringProperty(type);
        this.size = new SimpleStringProperty(size);
    }

    public String getBeam_no() {
        return beam_no.get();
    }

    public SimpleStringProperty beam_noProperty() {
        return beam_no;
    }

    public void setBeam_no(String beam_no) {
        this.beam_no.set(beam_no);
    }

    public String getBeam() {
        return beam.get();
    }

    public SimpleStringProperty beamProperty() {
        return beam;
    }

    public void setBeam(String beam) {
        this.beam.set(beam);
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

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getSize() {
        return size.get();
    }

    public SimpleStringProperty sizeProperty() {
        return size;
    }

    public void setSize(String size) {
        this.size.set(size);
    }
}
