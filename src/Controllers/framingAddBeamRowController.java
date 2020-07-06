package Controllers;

import Model.data.framing.beamsData;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

public class framingAddBeamRowController {

    @FXML
    JFXTextField BEAM, LENGTH, TYPE, SIZE;

    private int lastBeam_no;
    private int beam_no = 0;

    public void save() {
        framing_2Controller.data.addAll(new beamsData(beam_no, BEAM.getText(), LENGTH.getText(), TYPE.getText(), SIZE.getText()));
        framing_2Controller.closeStage();
    }

    public void cancel() {
        framing_2Controller.closeStage();
    }

}
