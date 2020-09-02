package Controllers.Sheets.Residential;

import DataBase.DataBase;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class intOpeningsController implements Initializable {

    public ObservableList<String> parts = FXCollections.observableArrayList();
    public VBox PARTS_VBOX;

    public GridPane DOOR_OPENINGS, WINDOWS, DOORS_UNITS, DOORS_UNIT_SL_BF, DOORS_UNIT_CAV, DOOR_UNIT_GIB,
            DOORS_EXP_STD, DOORS_EXP_SLD, DOORS_EXP_BF_PANEL, DOORS_EXP_BF_PLAIN, DOORS_CAVITY;

    public ArrayList<GridPane> gridPaneList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        loadPanes();

        DataBase db = DataBase.getInstance();
        parts.clear();
        db.displayParts(10, parts);

        parts.forEach(s -> {
            JFXButton button = new JFXButton(s);
            button.setPrefWidth(183);
            button.setPrefHeight(30); 
            button.setFont(new Font("Segoe UI",15.0));
            button.setTextFill(Color.web("#bcbcbc"));
            PARTS_VBOX.getChildren().add(button);

            button.setOnAction(event -> {
                gridPaneList.forEach(gridPane -> {
                    gridPane.setVisible(false);
                    if (button.getText().equals(gridPane.getId())) {
                        gridPane.setVisible(true);
                    }
                });
            });
        });
    }

    public void loadPanes() {
        DOOR_OPENINGS.setId("Door Openings");
        WINDOWS.setId("Windows");
        DOORS_UNITS.setId("Doors Units");
        DOORS_UNIT_SL_BF.setId("Doors Unit Sl-Bf");
        DOORS_UNIT_CAV.setId("Doors Unit Cav");
        DOOR_UNIT_GIB.setId("Door Unit Gib");
        DOORS_EXP_STD.setId("Doors Exp Std");
        DOORS_EXP_SLD.setId("Doors Exp Sld");
        DOORS_EXP_BF_PANEL.setId("Doors Exp BF Panel");
        DOORS_EXP_BF_PLAIN.setId("Doors Exp BF Plain");
        DOORS_CAVITY.setId("Doors Cavity");

        gridPaneList.add(DOOR_OPENINGS);
        gridPaneList.add(WINDOWS);
        gridPaneList.add(DOORS_UNITS);
        gridPaneList.add(DOORS_UNIT_SL_BF);
        gridPaneList.add(DOORS_UNIT_CAV);
        gridPaneList.add(DOOR_UNIT_GIB);
        gridPaneList.add(DOORS_EXP_STD);
        gridPaneList.add(DOORS_EXP_SLD);
        gridPaneList.add(DOORS_EXP_BF_PANEL);
        gridPaneList.add(DOORS_EXP_BF_PLAIN);
        gridPaneList.add(DOORS_CAVITY);
    }

}
