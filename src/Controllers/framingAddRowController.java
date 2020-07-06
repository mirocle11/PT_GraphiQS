package Controllers;

import Model.data.framing.framingData;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

public class framingAddRowController {

    @FXML
    public JFXTextField SECTION, HEIGHT, LENGTH, TYPE, STUD, SPACING;

    public void save() {
        framingController.data.addAll(new framingData(SECTION.getText(), HEIGHT.getText(), LENGTH.getText(),
                TYPE.getText(), STUD.getText(), SPACING.getText()));
        framingController.closeStage();
    }

    public void cancel() {
        framingController.closeStage();
    }
}