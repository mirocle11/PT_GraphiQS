package Controllers.Sheets.Residential;

import DataBase.DataBase;
import Model.Sheets.foundationsData;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class foundationsController implements Initializable {

    //foundations dataholder
    public foundationsData foundationsData;

    //collections
    public ObservableList<String> parts = FXCollections.observableArrayList();
    public ArrayList<GridPane> gridPaneList = new ArrayList<>();
    public VBox PARTS_VBOX;
    public int section_no = 0;
    public int dimension_no = 0;

    //gridpanes (parts)
    public GridPane POST_FOOTINGS, CONCRETE_BORES, FOOTINGS, CONCRETE_FLOOR, RAFT_PILES, RAFT_FOOTINGS, RAFT_SLAB,
            PATIO_FOOTINGS, PATIO_SLAB, CARPOT_FOOTINGS, CARPOT_SLAB, DECK_FOOTING, DECK_SLAB, WALL_PLATES,
            WATER_PROOFING, COLUMNS, BEAMS, CONCRETE_WALL, CONC_STEPS, BLOCK_CNRS_ENDS, SITE_WORKS, PROFILES, BOXING;

    //components
    public ListView<Integer> PF_SECTIONS;
    public JFXButton PF_ADD_SECTION, PF_REMOVE_SECTION;
    public JFXComboBox<String> PF_SET, PF_SET_OVERRIDE;
    public JFXTextField PF_QTY, PF_DEPTH, PF_WIDTH, PF_LENGTH, PF_TOTAL;

    //sample data
    private ObservableList<String> PF_SET_DATA = FXCollections.observableArrayList("(None)", "17.5 Footing [1]",
            "17.5 Post", "17.5 Slab", "17.5 Con Footings D12 CHANGE", "17.5 Con Post D12 CHANGE", "17.5 Con Slab D12 CHANGE");

    private ObservableList<String> PF_SET_OVERRIDE_DATA = FXCollections.observableArrayList("" , "20 Mpa",
            "25 Mpa", "30 Mpa");

    //tables
    public TreeTableView<foundationsData> PF_TABLE;
    public TreeTableColumn<foundationsData, String> PF_COL_NO;
    public TreeTableColumn<foundationsData, String> PF_COL_DEPTH;
    public TreeTableColumn<foundationsData, String> PF_COL_WIDTH;
    public TreeTableColumn<foundationsData, String> PF_COL_LENGTH;
    public TreeTableColumn<foundationsData, String> PF_COL_QTY;

    //data lists
    public static ObservableList<foundationsData> postFootingsData;

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

        postFootingsData = FXCollections.observableArrayList();

        PF_COL_NO.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("no")
        );
        PF_COL_DEPTH.setCellValueFactory( 
                new TreeItemPropertyValueFactory<>("depth")
        );
        PF_COL_WIDTH.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("width")
        );
        PF_COL_LENGTH.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("length")
        );
        PF_COL_QTY.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("qty")
        );

        TreeItem<foundationsData> root = new RecursiveTreeItem<>(postFootingsData, RecursiveTreeObject::getChildren);
        PF_TABLE.setRoot(root);
        PF_TABLE.setShowRoot(false);

        PF_ADD_SECTION.setOnAction(event -> {
            section_no++;
            PF_SECTIONS.getItems().add(section_no);
        });

        PF_REMOVE_SECTION.setOnAction(event -> {
            section_no--;
            PF_SECTIONS.getItems().remove(section_no);
        });

        PF_LENGTH.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                dimension_no++;
                postFootingsData.addAll(new foundationsData(String.valueOf(dimension_no), PF_QTY.getText(),
                        PF_DEPTH.getText(), PF_WIDTH.getText(), PF_LENGTH.getText()));
//                foundationsData.setNo(String.valueOf(dimension_no));
//                foundationsData.setSet(PF_SET.getSelectionModel().getSelectedItem());
//                foundationsData.setSet_override(PF_SET_OVERRIDE.getSelectionModel().getSelectedItem());
//                foundationsData.setQty(PF_QTY.getText());
//                foundationsData.setDepth(PF_DEPTH.getText());
//                foundationsData.setWidth(PF_WIDTH.getText());
//                foundationsData.setLength(PF_LENGTH.getText());
            }
        });

        PF_SET.setItems(PF_SET_DATA);
        PF_SET_OVERRIDE.setItems(PF_SET_OVERRIDE_DATA);

        PF_SECTIONS.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                //section list event
            } catch (Exception e) {
                e.printStackTrace();
            }
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
