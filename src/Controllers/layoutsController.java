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
    private Tab slabAndFootingTab, floorsTab, framingTab, wallsAndInsulationTab, claddingTab, doorsTab, roofTab, deckTab, miscTab;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/slabAndFooting.fxml"));
            AnchorPane slabAndFooting = loader.load();
            slabAndFootingTab.setContent(slabAndFooting);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/floors.fxml"));
            AnchorPane floors = loader.load();
            floorsTab.setContent(floors);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/framing.fxml"));
            AnchorPane framing = loader.load();
            framingTab.setContent(framing);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/wallsAndInsulation.fxml"));
            AnchorPane wallsAndInsulation = loader.load();
            wallsAndInsulationTab.setContent(wallsAndInsulation);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/cladding.fxml"));
            AnchorPane cladding = loader.load();
            claddingTab.setContent(cladding);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/doors.fxml"));
            AnchorPane doors = loader.load();
            doorsTab.setContent(doors);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/roof.fxml"));
            AnchorPane roof = loader.load();
            roofTab.setContent(roof);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/deck.fxml"));
            AnchorPane deck = loader.load();
            deckTab.setContent(deck);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/misc.fxml"));
            AnchorPane misc = loader.load();
            miscTab.setContent(misc);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}