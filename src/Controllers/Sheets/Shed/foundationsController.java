package Controllers.Sheets.Shed;

import DataBase.DataBase;
import Main.Main;
import Model.data.shed.foundations.postFootingsSec;
import Model.data.shed.foundationsMaterials;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class foundationsController implements Initializable {

    //indicators
    public Label id_indicator = new Label(); //part id every selection

    public int selectedSection = 0;

    //collections
    public ObservableList<String> parts = FXCollections.observableArrayList();
    public ArrayList<GridPane> gridPaneList = new ArrayList<>();
    public VBox PARTS_VBOX;
    public ArrayList<JFXButton> buttonList = new ArrayList<>();

    //gridpanes (parts)
    public GridPane POST_FOOTINGS, CONCRETE_BORES, CONCRETE_FLOOR;

    //components
    public ListView<Integer> PF_SECTIONS, CB_SECTIONS, CF_SECTIONS;
    public JFXButton PF_ADD_SECTION, PF_REMOVE_SECTION, CB_ADD_SECTION, CB_REMOVE_SECTION, CF_ADD_SECTION,
            CF_REMOVE_SECTION, REFRESH;
    public JFXComboBox<String> PF_SET, PF_SET_OVERRIDE, CB_SET, CB_SET_OVERRIDE, CF_SET, CF_SET_OVERRIDE;
//    public JFXTextField PF_QTY;

    //component editor
    private double xOffset = 0;
    private double yOffset = 0;
    private static Stage editComponentStage;

    //sample data
    private ObservableList<String> CF_SET_DATA = FXCollections.observableArrayList("(None)", "17.5 SE620-500");

    //table right click menu
    public ContextMenu componentsMenu = new ContextMenu();
    public MenuItem editRow = new MenuItem("Edit Row");
    public MenuItem clearRow = new MenuItem("Clear Row");

    public TreeTableView<postFootingsSec> PF_TABLE; //section dimensions
    public TreeTableColumn<postFootingsSec, String> PF_COL_NO;
    public TreeTableColumn<postFootingsSec, String> PF_COL_DEPTH;
    public TreeTableColumn<postFootingsSec, String> PF_COL_WIDTH;
    public TreeTableColumn<postFootingsSec, String> PF_COL_LENGTH;
    public TreeTableColumn<postFootingsSec, String> PF_COL_QTY;

    public JFXTreeTableView<foundationsMaterials> MATERIALS_TBL;
    public TreeTableColumn<foundationsMaterials, String> SKU_NUMBER_COL;
    public TreeTableColumn<foundationsMaterials, String> DESCRIPTION_COL;
    public TreeTableColumn<foundationsMaterials, String> UNIT_COL;
    public TreeTableColumn<foundationsMaterials, String> QUANTITY_COL;

    //data lists
    public static ObservableList<foundationsMaterials> foundationsMaterials;
    public static ObservableList<postFootingsSec> postFootingsData; //section dimensions

    public static int cf_length = 0;
    public static double cf_area = 0.0;
    public static double cf_volume = 0.0;

    public static ObservableList<Integer> foundationsPFSectionList = FXCollections.observableArrayList();
    public static ObservableList<Integer> foundationsCBSectionList = FXCollections.observableArrayList();

    private final ObservableList<String> setsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        loadPanes();

        DataBase db = DataBase.getInstance();
        parts.clear();
        db.displayShedParts(1, parts);

        parts.forEach(s -> {
            JFXButton button = new JFXButton();
            button.setText(s);
            button.setId(s);
            button.setPrefWidth(183);
            button.setPrefHeight(30);
            button.setFont(new Font("Segoe UI",15.0));
            button.setTextFill(Color.web("#bcbcbc"));
            PARTS_VBOX.getChildren().add(button);
            buttonList.add(button);

            button.setOnAction(event -> {
//                foundationsData.clear();
                db.getPartID(1, button.getId(), id_indicator);
                db.getSets(Integer.parseInt(id_indicator.getText()), setsList); //apply sets to selected part
                //selection highlighter
                buttonList.forEach(button1 -> {
                    if (button1.getId().equals(button.getId())) {
                        button1.setStyle("-fx-background-color: #394F5A");
                    } else {
                        button1.setStyle("-fx-background-color: TRANSPARENT");
                    }
                });
                gridPaneList.forEach(gridPane -> {
                    gridPane.setVisible(false);
                    if (button.getId().equals(gridPane.getId())) {
                        gridPane.setVisible(true);
                    }
                });
            });
        });

        //parts - section dimensions
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

        //foundations materials
        foundationsMaterials = FXCollections.observableArrayList();

        SKU_NUMBER_COL.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("sku_number")
        );
        DESCRIPTION_COL.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("description")
        );
        UNIT_COL.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("unit")
        );
        QUANTITY_COL.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("quantity")
        );

        TreeItem<postFootingsSec> post_footings_root = new RecursiveTreeItem<>(postFootingsData, RecursiveTreeObject::getChildren);
        PF_TABLE.setRoot(post_footings_root);
        PF_TABLE.setShowRoot(false);

        TreeItem<foundationsMaterials> foundationsMaterialsDataTreeItem = new RecursiveTreeItem<>
                (foundationsMaterials, RecursiveTreeObject::getChildren);
        MATERIALS_TBL.setRoot(foundationsMaterialsDataTreeItem);
        MATERIALS_TBL.setShowRoot(false);

        //component table menu
        componentsMenu.getItems().add(editRow);
        componentsMenu.getItems().add(clearRow);

        //menu items (components)
        editRow.setOnAction(event -> {
            loadComponentEditor();
        });

        clearRow.setOnAction(event -> {
            //clears details on selected row
        });

        //post footings
        PF_SECTIONS.setItems(foundationsPFSectionList);

        PF_SET.setItems(setsList);

//        PF_SET_OVERRIDE.setItems(PF_SET_OVERRIDE_DATA);
        PF_SET_OVERRIDE.getSelectionModel().select(0);

        PF_SET.setOnAction(event -> {
            String set_override;
            try {
                set_override = PF_SET_OVERRIDE.getSelectionModel().getSelectedItem();
            } catch (Exception e) {
                set_override = "";
            }
            db.setSelectedSets(1, PF_SECTIONS.getSelectionModel().getSelectedItem(),
                    PF_SET.getSelectionModel().getSelectedItem(), set_override);
        });

        PF_SET_OVERRIDE.setOnAction(event -> {

        });

        PF_SECTIONS.setOnMouseReleased(event -> {
            try {
//                clearForms();
                //section list event
                PF_SET.setDisable(false);
                PF_SET_OVERRIDE.setDisable(false);
                db.getSelectedSets(Integer.parseInt(id_indicator.getText()), PF_SECTIONS.getSelectionModel()
                        .getSelectedItem(), PF_SET, PF_SET_OVERRIDE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //concrete bores
        CB_SET.setItems(setsList);

        CB_SET.setOnAction(event -> {
            String set_override;
            try {
                set_override = CB_SET_OVERRIDE.getSelectionModel().getSelectedItem();
            } catch (Exception e) {
                set_override = "";
            }
            db.setSelectedSets(Integer.parseInt(id_indicator.getText()), CB_SECTIONS.getSelectionModel().getSelectedItem(),
                    CB_SET.getSelectionModel().getSelectedItem(), set_override);
        });

        CB_SET_OVERRIDE.setOnAction(event -> {

        });

        CB_SECTIONS.setItems(foundationsCBSectionList);
        CB_SECTIONS.setOnMouseReleased(event -> {
            try {
//                clearForms();
                //section list event
                CB_SET.setDisable(false);
                CB_SET_OVERRIDE.setDisable(false);
                db.getSelectedSets(Integer.parseInt(id_indicator.getText()), CB_SECTIONS.getSelectionModel()
                        .getSelectedItem(), CB_SET, CB_SET_OVERRIDE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //concrete floors
        CF_SET.setItems(setsList);
        CF_SET.getSelectionModel().select(0);

        CF_SECTIONS.setOnMouseReleased(event -> {
            //section list event
        });

        CF_SET.setOnAction(event -> {
            if (CF_SET.getSelectionModel().getSelectedIndex() == 1) {
                refreshConcreteFloorMaterials();
            }
        });

        REFRESH.setOnAction(event -> {
            //refresh all tables
            refreshConcreteFloorMaterials();
        });
    }

    public void refreshConcreteFloorMaterials() {
        foundationsMaterials.clear();
        int srm_quantity = (int) ((int) cf_area / 7.612);
        int pb_quantity;
        if (cf_area < 100 && srm_quantity > 0.00) {
            pb_quantity = 1;
        } else {
            int remainder = (int) (cf_area % 100);
            if (remainder > 0) {
                pb_quantity = (int) (cf_area / 100) + 1;
            } else {
                pb_quantity = (int) (cf_area / 100);
            }
        }
        int tp_quantity;
        if (cf_length < 30 && cf_length > 0) {
            tp_quantity = 1;
        } else {
            int remainder = cf_length % 30;
            if (remainder > 0) {
                tp_quantity = cf_length / 30 + 1;
            } else {
                tp_quantity = cf_length / 30;
            }
        }
        int tw_quantity;
        if (cf_area < 20 && cf_area > 0) {
            tw_quantity = 1;
        } else {
            int remainder = (int) (cf_area % 20);
            if (remainder > 0) {
                tw_quantity = (int) (cf_area / 20 + 1);
            } else {
                tw_quantity = (int) (cf_area / 20);
            }
        }
//        foundationsMaterials.addAll(new foundationsMaterials("32100832", "Structural Concrete 17.5Mpa 19mm",
//                "M3", String.valueOf(new DecimalFormat("0.00").format(cf_volume))));
//        foundationsMaterials.addAll(new foundationsMaterials("STMESE620500SM",
//                "STEEL REINFORCING MESH SE620-500SMALL 4.68X2.05 7.612M2", "SHT", String.valueOf(srm_quantity)));
//        foundationsMaterials.addAll(new foundationsMaterials("STBC5065E",
//                "BAR CHAIR PLASTIC 50-65MM - PER EACH", "EACH", String.valueOf(srm_quantity * 12)));
//        foundationsMaterials.addAll(new foundationsMaterials("RPPO250425",
//                "POLYTHENE BLK 250MU X 4M X 25M 2010103", "ROLL", String.valueOf(pb_quantity)));
//        foundationsMaterials.addAll(new foundationsMaterials("RPJT2040116",
//                "TAPE-IT HIGH ADHESIVE PVC JOINING TAPE 48MMX30M 2040116", "ROLL", String.valueOf(tp_quantity)));
//        foundationsMaterials.addAll(new foundationsMaterials("STW300GX1KG", "TIE WIRE GALV 300MM X 1KG",
//                "EACH", String.valueOf(tw_quantity)));
    }

    public void loadComponentEditor() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/Views/Sheets/Shed/editComponent.fxml"));
            AnchorPane pane = loader.load();

            //draggable pop up
            pane.setOnMousePressed(event1 -> {
                xOffset = event1.getSceneX();
                yOffset = event1.getSceneY();
            });

            pane.setOnMouseDragged(event1 -> {
                editComponentStage.setX(event1.getScreenX() - xOffset);
                editComponentStage.setY(event1.getScreenY() - yOffset);
            });

            Scene scene = new Scene(pane);
            scene.setFill(Color.TRANSPARENT);
            scene.getStylesheets().addAll(Main.class.getResource("/Views/CSS/style.css").toExternalForm());
            editComponentStage = new Stage();
            editComponentStage.setScene(scene);
            editComponentStage.initStyle(StageStyle.UNDECORATED);
            editComponentStage.initModality(Modality.APPLICATION_MODAL);
            editComponentStage.initStyle(StageStyle.TRANSPARENT);
            editComponentStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadPanes() {
        gridPaneList.add(POST_FOOTINGS);
        gridPaneList.add(CONCRETE_BORES);
        gridPaneList.add(CONCRETE_FLOOR);
    }

    public void clearForms() {
        try {
            //post footings
            if (PF_SET.getSelectionModel().isEmpty()) {
                PF_SET.getSelectionModel().clearSelection();
                PF_SET_OVERRIDE.getSelectionModel().clearSelection();
            }

            //concrete bores
            CB_SET.getSelectionModel().clearSelection();
            CB_SET_OVERRIDE.getSelectionModel().clearSelection();
            //concrete bores
            CF_SET.getSelectionModel().clearSelection();
            CF_SET_OVERRIDE.getSelectionModel().clearSelection();
        } catch (Exception e) {

        }
    }

}
