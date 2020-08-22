package Controllers.Sheets;

import DataBase.DataBase;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class foundationsController implements Initializable {

    public ObservableList<String> parts = FXCollections.observableArrayList();
    public VBox PARTS_VBOX;

    public GridPane POST_FOOTINGS, CONCRETE_BORES, FOOTINGS, CONCRETE_FLOOR, RAFT_PILES, RAFT_FOOTINGS, RAFT_SLAB,
            PATIO_FOOTINGS, PATIO_SLAB, CARPOT_FOOTINGS, CARPOT_SLAB, DECK_FOOTING, DECK_SLAB, WALL_PLATES,
            WATER_PROOFING, COLUMNS, BEAMS, CONCRETE_WALL, CONC_STEPS, BLOCK_CNRS_ENDS, SITE_WORKS, PROFILES, BOXING;

    public ArrayList<GridPane> gridPaneList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        loadPanes();

        DataBase db = DataBase.getInstance();
        parts.clear();
        db.displayParts(1, parts);

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
        POST_FOOTINGS.setId("Post Footings");
        CONCRETE_BORES.setId("Concrete Bores");
        FOOTINGS.setId("Footings");
        CONCRETE_FLOOR.setId("Concrete Floor");
        RAFT_PILES.setId("Raft Piles");
        RAFT_FOOTINGS.setId("Raft Footings");
        RAFT_SLAB.setId("Raft Slab");
        PATIO_FOOTINGS.setId("Patio Footings");
        PATIO_SLAB.setId("Patio Slab");
        CARPOT_FOOTINGS.setId("Carpot Footings");
        CARPOT_SLAB.setId("Carpot Slab");
        DECK_FOOTING.setId("Deck Footing");
        DECK_SLAB.setId("Deck Slab");
        WALL_PLATES.setId("Wall Plates");
        WATER_PROOFING.setId("Water Proofing");
        COLUMNS.setId("Columns");
        BEAMS.setId("Beams");
        CONCRETE_WALL.setId("Concrete Wall");
        CONC_STEPS.setId("Conc Steps");
        BLOCK_CNRS_ENDS.setId("Block Cnrs & Ends");
        SITE_WORKS.setId("Site Works");
        PROFILES.setId("Profiles");
        BOXING.setId("Boxing");

        gridPaneList.add(POST_FOOTINGS);
        gridPaneList.add(CONCRETE_BORES);
        gridPaneList.add(FOOTINGS);
        gridPaneList.add(CONCRETE_FLOOR);
        gridPaneList.add(RAFT_PILES);
        gridPaneList.add(RAFT_FOOTINGS);
        gridPaneList.add(PATIO_SLAB);
        gridPaneList.add(CARPOT_FOOTINGS);
        gridPaneList.add(RAFT_SLAB);
        gridPaneList.add(PATIO_FOOTINGS);
        gridPaneList.add(CARPOT_SLAB);
        gridPaneList.add(DECK_FOOTING);
        gridPaneList.add(DECK_SLAB);
        gridPaneList.add(WALL_PLATES);
        gridPaneList.add(WATER_PROOFING);
        gridPaneList.add(COLUMNS);
        gridPaneList.add(BEAMS);
        gridPaneList.add(CONCRETE_WALL);
        gridPaneList.add(CONC_STEPS);
        gridPaneList.add(BLOCK_CNRS_ENDS);
        gridPaneList.add(SITE_WORKS);
        gridPaneList.add(PROFILES);
        gridPaneList.add(BOXING);
    }

}
