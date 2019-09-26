package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class floorsController implements Initializable {

    @FXML
    private AnchorPane rootpane,subfloor;
    private AnchorPane ground_floor,first_floor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/ground_floor.fxml"));
            ground_floor = loader.load();
            rootpane.getChildren().addAll(ground_floor);
            ground_floor.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/first_floor.fxml"));
            first_floor = loader.load();
            rootpane.getChildren().addAll(first_floor);
            first_floor.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void next(ActionEvent actionEvent) {
        if(subfloor.isVisible()) {
            subfloor.setVisible(false);
            ground_floor.setVisible(true);
        }
        else if(ground_floor.isVisible()){
            ground_floor.setVisible(false);
            first_floor.setVisible(true);
        }
    }

    public void back(ActionEvent actionEvent) {
        if(first_floor.isVisible()) {
            first_floor.setVisible(false);
            ground_floor.setVisible(true);
        }
        else if(ground_floor.isVisible()) {
            ground_floor.setVisible(false);
            subfloor.setVisible(true);
        }
    }

}
