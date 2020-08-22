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

public class wallsSglLevController implements Initializable {

    public ObservableList<String> parts = FXCollections.observableArrayList();
    public VBox PARTS_VBOX;

    public GridPane MISC, COLUMNS, GABLES_JACK_FRAMES, HANDRAILS, STEEL_BEAMS, FLITCHED_LINTELS, MEASURED_LINTELS,
            SPECIAL_MANUFACTURING, EXTERIOR, INTERIOR, LINTELS_NON_SLS, STUDS_NOGS_DEDUCTION;

    public ArrayList<GridPane> gridPaneList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        loadPanes();

        DataBase db = DataBase.getInstance();
        parts.clear();
        db.displayParts(18, parts);

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
        MISC.setId("Misc");
        COLUMNS.setId("Columns");
        GABLES_JACK_FRAMES.setId("Gables&Jack Frames");
        HANDRAILS.setId("Handrails");
        STEEL_BEAMS.setId("Steel Beams");
        FLITCHED_LINTELS.setId("Flitched Lintels");
        MEASURED_LINTELS.setId("Measured Lintels");
        SPECIAL_MANUFACTURING.setId("Special Manufacturing");
        EXTERIOR.setId("Exterior");
        INTERIOR.setId("Interior");
        LINTELS_NON_SLS.setId("Lintels Non SLs");
        STUDS_NOGS_DEDUCTION.setId("Studs Nogs Deduction");

        gridPaneList.add(MISC);
        gridPaneList.add(COLUMNS);
        gridPaneList.add(GABLES_JACK_FRAMES);
        gridPaneList.add(HANDRAILS);
        gridPaneList.add(STEEL_BEAMS);
        gridPaneList.add(FLITCHED_LINTELS);
        gridPaneList.add(MEASURED_LINTELS);
        gridPaneList.add(SPECIAL_MANUFACTURING);
        gridPaneList.add(EXTERIOR);
        gridPaneList.add(INTERIOR);
        gridPaneList.add(LINTELS_NON_SLS);
        gridPaneList.add(STUDS_NOGS_DEDUCTION);
    }

}
