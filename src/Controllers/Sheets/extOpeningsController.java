package Controllers.Sheets;

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

public class extOpeningsController implements Initializable {

    public ObservableList<String> parts = FXCollections.observableArrayList();
    public VBox PARTS_VBOX;

    public GridPane WIND_HARDWARE, GARAGE_DOOR, DOORS, WINDOWS, WINDOW_HEADS_STANDARD, WINDOW_HEADS_RAKING,
            CANTILEVER_LINTELS, DOORS_MEASURED_ITEMS;

    public ArrayList<GridPane> gridPaneList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        loadPanes();

        DataBase db = DataBase.getInstance();
        parts.clear();
        db.displayParts(9, parts);

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
        WIND_HARDWARE.setId("Wind Hardware");
        GARAGE_DOOR.setId("Garage Door");
        DOORS.setId("Doors");
        WINDOWS.setId("Windows");
        WINDOW_HEADS_STANDARD.setId("Window Heads Standard");
        WINDOW_HEADS_RAKING.setId("Window Heads Raking");
        CANTILEVER_LINTELS.setId("Cantilever Lintels");
        DOORS_MEASURED_ITEMS.setId("Doors Measured Items");

        gridPaneList.add(WIND_HARDWARE);
        gridPaneList.add(GARAGE_DOOR);
        gridPaneList.add(DOORS);
        gridPaneList.add(WINDOWS);
        gridPaneList.add(WINDOW_HEADS_STANDARD);
        gridPaneList.add(WINDOW_HEADS_RAKING);
        gridPaneList.add(CANTILEVER_LINTELS);
        gridPaneList.add(DOORS_MEASURED_ITEMS);
    }

}
