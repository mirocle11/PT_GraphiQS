package Controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

public class framingAddRowController {

    @FXML
    JFXTextField SECTION, HEIGHT, LENGTH, TYPE, STUD, SPACING;

    public void save() {
        
    }

    public void cancel() {
        framingController.closeStage();
    }
}