package Controllers.Sheets.Shed;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class addNotesController implements Initializable {

    public JFXButton DONE;
    public Label STRUCTURE, PART;
    public TextArea NOTES;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DONE.setOnAction(event -> {

        });

    }

}
