package Controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class sectionPopupController implements Initializable {
    public ColorPicker colorPicker;
    public AnchorPane secondpane;
    AnchorPane ap = workspaceController.mainPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/sectionPopup.fxml"));
            secondpane = loader.load();
            ap.getChildren().addAll(secondpane);
            secondpane.setLayoutY(120);
            AnchorPane.setBottomAnchor(secondpane, 0.0);
            AnchorPane.setTopAnchor(secondpane, 120.0);
            secondpane.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
        secondpane.setVisible(true);
    }

    public void hide() {
        secondpane.setVisible(false);
    }

}
