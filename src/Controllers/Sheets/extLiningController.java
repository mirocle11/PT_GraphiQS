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

public class extLiningController implements Initializable {

    public ObservableList<String> parts = FXCollections.observableArrayList();
    public VBox PARTS_VBOX;

    public GridPane GND_LEVEL, GND_EXT_CORNERS, GND_INT_CORNERS, EXT_ENCLOSED_BEAMS, UPPER_LEVEL, UPPER_EXT_CORNERS,
            UPPER_INT_CORNERS, GND_MEASURED_ITEMS, UPPER_MEASURED_ITEMS;

    public ArrayList<GridPane> gridPaneList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        loadPanes();

        DataBase db = DataBase.getInstance();
        parts.clear();
        db.displayParts(31, parts);

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
        GND_LEVEL.setId("Gnd Level");
        GND_EXT_CORNERS.setId("Gnd Ext Corners");
        GND_INT_CORNERS.setId("Gnd Int Corners");
        EXT_ENCLOSED_BEAMS.setId("Ext Enclosed Beams");
        UPPER_LEVEL.setId("Upper Level");
        UPPER_EXT_CORNERS.setId("Upper Ext Corners");
        UPPER_INT_CORNERS.setId("Upper Int Corners");
        GND_MEASURED_ITEMS.setId("Gnd Measured Items");
        UPPER_MEASURED_ITEMS.setId("Upper Measured Items");

        gridPaneList.add(GND_LEVEL);
        gridPaneList.add(GND_EXT_CORNERS);
        gridPaneList.add(GND_INT_CORNERS);
        gridPaneList.add(EXT_ENCLOSED_BEAMS);
        gridPaneList.add(UPPER_LEVEL);
        gridPaneList.add(UPPER_EXT_CORNERS);
        gridPaneList.add(UPPER_INT_CORNERS);
        gridPaneList.add(GND_MEASURED_ITEMS);
        gridPaneList.add(UPPER_MEASURED_ITEMS);
    }

}
