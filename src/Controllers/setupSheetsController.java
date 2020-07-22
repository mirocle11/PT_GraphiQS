package Controllers;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class setupSheetsController implements Initializable {

    @FXML
    private Tab FOUNDATIONS, INT_FLOOR_LEV_1, EXT_OPENINGS;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Sheets/foundations.fxml"));
            AnchorPane foundations = loader.load();
            FOUNDATIONS.setContent(foundations);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Sheets/intFloorLev1.fxml"));
            AnchorPane intFloorLev1 = loader.load();
            INT_FLOOR_LEV_1.setContent(intFloorLev1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Sheets/extOpenings.fxml"));
            AnchorPane extOpenings = loader.load();
            EXT_OPENINGS.setContent(extOpenings);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}