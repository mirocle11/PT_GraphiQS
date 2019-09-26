package Controllers;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class layoutsController implements Initializable {

    @FXML
    private Tab slabAndFootingTab, floorsTab;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/slabAndFooting.fxml"));
            AnchorPane slabAndFooting = loader.load();
            slabAndFootingTab.setContent(slabAndFooting);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/floors.fxml"));
            AnchorPane floors = loader.load();
            floorsTab.setContent(floors);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}