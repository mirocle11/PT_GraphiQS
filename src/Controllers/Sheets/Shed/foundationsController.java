package Controllers.Sheets.Shed;

import Controllers.setupSheetsController;
import DataBase.DataBase;
import Main.Main;
import Model.ComponentData;
import Model.data.shed.foundations.concreteBoresSec;
import Model.data.shed.foundations.postFootingsSec;
import Model.data.shed.foundationsMaterials;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
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
import java.text.DecimalFormat;
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
    public GridPane POST_FOOTINGS, POLE_FOOTINGS, CONCRETE_FLOOR;

    //components
    public ListView<Integer> PF_SECTIONS, PLF_SECTIONS, CF_SECTIONS;
    public JFXButton PF_ADD_SECTION, PF_REMOVE_SECTION, CB_ADD_SECTION, CB_REMOVE_SECTION, CF_ADD_SECTION,
            CF_REMOVE_SECTION, REFRESH;
    public JFXComboBox<String> PF_SET, PF_SET_OVERRIDE, PLF_SET, PLF_SET_OVERRIDE, CF_SET, CF_SET_OVERRIDE;
    public TextArea PF_NOTES, PL_NOTES, CF_NOTES;

    //component editor
    private double xOffset = 0;
    private double yOffset = 0;
    private static Stage editComponentStage;

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
    public TreeTableColumn<postFootingsSec, String> PF_COL_VOLUME;

    public TreeTableView<concreteBoresSec> CB_TABLE; //section dimensions
    public TreeTableColumn<concreteBoresSec, String> CB_COL_NO;
    public TreeTableColumn<concreteBoresSec, String> CB_COL_QTY;
    public TreeTableColumn<concreteBoresSec, String> CB_COL_DIAMETER;
    public TreeTableColumn<concreteBoresSec, String> CB_COL_HEIGHT;
    public TreeTableColumn<concreteBoresSec, String> CB_COL_VOLUME;

    public JFXTreeTableView<foundationsMaterials> MATERIALS_TBL;
    public TreeTableColumn<foundationsMaterials, String> COMPONENT_COL;
    public TreeTableColumn<foundationsMaterials, String> SKU_NUMBER_COL;
    public TreeTableColumn<foundationsMaterials, String> DESCRIPTION_COL;
    public TreeTableColumn<foundationsMaterials, String> UNIT_COL;
    public TreeTableColumn<foundationsMaterials, String> QUANTITY_COL;
    public TreeTableColumn<foundationsMaterials, String> USAGE_COL;

    //data lists
    public static ObservableList<foundationsMaterials> foundationsMaterials;
    public static ObservableList<postFootingsSec> postFootingsData; //section dimensions
    public static ObservableList<concreteBoresSec> concreteBoresData; //section dimensions

    public Label foundations_pf_volume = new Label("");
    public Label foundations_cb_volume = new Label("");

    public static int cf_length = 0; //concrete floor
    public static double cf_area = 0.0;
    public static double cf_volume = 0.0;

    public static ObservableList<Integer> foundationsPFSectionList = FXCollections.observableArrayList();
    public static ObservableList<Integer> foundationsCBSectionList = FXCollections.observableArrayList();
    public static ObservableList<Integer> foundationsCFSectionList = FXCollections.observableArrayList();

    private final ObservableList<String> setsListPF = FXCollections.observableArrayList();
    private final ObservableList<String> setsListCB = FXCollections.observableArrayList();
    private final ObservableList<String> setsListCF = FXCollections.observableArrayList();

    private static String part;
    private static String section;
    private ComponentData componentData;
    private String key;

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        loadPanes();

        DataBase db = DataBase.getInstance();
        parts.clear();
        db.displayShedParts(1, parts);
        db.getSets(1, setsListPF);
        db.getSets(2, setsListCB);
        db.getSets(3, setsListCF);

        parts.forEach(s -> {
            JFXButton button = new JFXButton();
            button.setText(s);
            button.setId(s);
            button.setPrefWidth(183);
            button.setPrefHeight(30);
            button.setFont(new Font("Segoe UI", 15.0));
            button.setTextFill(Color.web("#bcbcbc"));
            PARTS_VBOX.getChildren().add(button);
            buttonList.add(button);

            button.setOnAction(event -> {
                foundationsMaterials.clear();
                db.getPartID(1, button.getId(), id_indicator);
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
        PF_COL_VOLUME.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("volume")
        );

        concreteBoresData = FXCollections.observableArrayList();

        CB_COL_NO.setCellValueFactory(new TreeItemPropertyValueFactory<>("no"));
        CB_COL_QTY.setCellValueFactory(new TreeItemPropertyValueFactory<>("qty"));
        CB_COL_DIAMETER.setCellValueFactory(new TreeItemPropertyValueFactory<>("diameter"));
        CB_COL_HEIGHT.setCellValueFactory(new TreeItemPropertyValueFactory<>("height"));
        CB_COL_VOLUME.setCellValueFactory(new TreeItemPropertyValueFactory<>("volume"));

        //foundations materials
        foundationsMaterials = FXCollections.observableArrayList();

        COMPONENT_COL.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("component")
        );
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
        USAGE_COL.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("usage")
        );

        TreeItem<postFootingsSec> post_footings_root = new RecursiveTreeItem<>(postFootingsData, RecursiveTreeObject::getChildren);
        PF_TABLE.setRoot(post_footings_root);
        PF_TABLE.setShowRoot(false);

        TreeItem<concreteBoresSec> concrete_bores_root = new RecursiveTreeItem<>(concreteBoresData, RecursiveTreeObject::getChildren);
        CB_TABLE.setRoot(concrete_bores_root);
        CB_TABLE.setShowRoot(false);

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
        PF_SET.setItems(setsListPF);

        PF_SET_OVERRIDE.getSelectionModel().select(0);

        PF_SET.setOnAction(event -> {
            String set_override = "";
            if (!PF_SET_OVERRIDE.getSelectionModel().isEmpty()) {
                set_override = PF_SET_OVERRIDE.getSelectionModel().getSelectedItem();
            }
            db.setSelectedSets(1, PF_SECTIONS.getSelectionModel().getSelectedItem(),
                    PF_SET.getSelectionModel().getSelectedItem(), set_override, "");
//            setComponentContents(1, PF_SET.getSelectionModel().getSelectedItem());
            updateComponentFromList(1, PF_SET.getSelectionModel().getSelectedItem());
        });

        PF_SET_OVERRIDE.setOnAction(event -> {

        });

        PF_SECTIONS.setItems(foundationsPFSectionList);
        PF_SECTIONS.setOnMouseReleased(event -> {
            try {
                key = "foundations_post_footings_" + PF_SECTIONS.getSelectionModel().getSelectedItem();

                componentData = new ComponentData("foundations", "post footings",
                        PF_SECTIONS.getSelectionModel().getSelectedItem().toString());

                if (setupSheetsController.componentList.containsKey(key)) {
                    componentData = setupSheetsController.componentList.get(key);
                } else {
                    setupSheetsController.componentList.put(key, componentData);
                }

                PF_SET.setDisable(false);
                PF_SET_OVERRIDE.setDisable(false);

                db.getSelectedSets(Integer.parseInt(id_indicator.getText()), PF_SECTIONS.getSelectionModel()
                        .getSelectedItem(), PF_SET, PF_SET_OVERRIDE);
                db.displayNotes(Integer.parseInt(id_indicator.getText()), PF_SECTIONS.getSelectionModel().getSelectedItem(),
                        PF_NOTES);

                System.out.println("clicked on post footings " + PF_SECTIONS.getSelectionModel().getSelectedItem());
                postFootingsData.clear();
                db.getPostFootingsSD(1, PF_SECTIONS.getSelectionModel().getSelectedItem(), postFootingsData);
                db.getFoundationsPFData(PF_SECTIONS.getSelectionModel().getSelectedItem(), foundations_pf_volume);

                setComponentsFromList();
                System.out.println(setupSheetsController.componentList.size());

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //pole footings
        PLF_SET.setItems(setsListCB);

        PLF_SET.setOnAction(event -> {
            String set_override = "";
            if (!PLF_SET_OVERRIDE.getSelectionModel().isEmpty()) {
                set_override = PLF_SET_OVERRIDE.getSelectionModel().getSelectedItem();
            }
            db.setSelectedSets(Integer.parseInt(id_indicator.getText()), PLF_SECTIONS.getSelectionModel()
                    .getSelectedItem(), PLF_SET.getSelectionModel().getSelectedItem(), set_override, "");
//            setComponentContents(2, PLF_SET.getSelectionModel().getSelectedItem());
            updateComponentFromList(2, PLF_SET.getSelectionModel().getSelectedItem());
        });

        PLF_SECTIONS.setItems(foundationsCBSectionList);
        PLF_SECTIONS.setOnMouseReleased(event -> {
            try {
                key = "foundations_pole_footings_" + PLF_SECTIONS.getSelectionModel().getSelectedItem();

                componentData = new ComponentData("foundations", "pole footings",
                        PLF_SECTIONS.getSelectionModel().getSelectedItem().toString());

                if (setupSheetsController.componentList.containsKey(key)) {
                    componentData = setupSheetsController.componentList.get(key);
                } else {
                    setupSheetsController.componentList.put(key, componentData);
                }


                PLF_SET.setDisable(false);
                PLF_SET_OVERRIDE.setDisable(false);
                db.getSelectedSets(Integer.parseInt(id_indicator.getText()), PLF_SECTIONS.getSelectionModel()
                        .getSelectedItem(), PLF_SET, PLF_SET_OVERRIDE);
                db.displayNotes(Integer.parseInt(id_indicator.getText()), PLF_SECTIONS.getSelectionModel().getSelectedItem(),
                        PL_NOTES);

                System.out.println("clicked on " + PLF_SECTIONS.getSelectionModel().getSelectedItem());
                concreteBoresData.clear();

                db.getConcreteBoresSD(1, PLF_SECTIONS.getSelectionModel().getSelectedItem(), concreteBoresData);
                db.getFoundationsCBData(PLF_SECTIONS.getSelectionModel().getSelectedItem(), foundations_cb_volume);

                setComponentsFromList();
                System.out.println(setupSheetsController.componentList.size());

//                if (!PLF_SET.getSelectionModel().isEmpty()) {
//                    setComponentContents(2, PLF_SET.getSelectionModel().getSelectedItem());
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //concrete floors
        CF_SET.setItems(setsListCF);

        CF_SECTIONS.setItems(foundationsCFSectionList);
        CF_SECTIONS.setOnMouseReleased(event -> {

            key = "foundations_concrete_floors_" + CF_SECTIONS.getSelectionModel().getSelectedItem();

            componentData = new ComponentData("foundations", "concrete floors", CF_SECTIONS.getSelectionModel().getSelectedItem().toString());

            if (setupSheetsController.componentList.containsKey(key)) {
                componentData = setupSheetsController.componentList.get(key);
            } else {
                setupSheetsController.componentList.put(key, componentData);
            }

            //section list event
            CF_SET.setDisable(false);
            CF_SET_OVERRIDE.setDisable(false);

            db.getSelectedSets(3, CF_SECTIONS.getSelectionModel().getSelectedItem(), CF_SET, CF_SET_OVERRIDE);
            db.displayNotes(3, CF_SECTIONS.getSelectionModel().getSelectedItem(), CF_NOTES);

            setComponentsFromList();
//            if (!CF_SET.getSelectionModel().isEmpty()) {
//                setComponentContents(3, CF_SET.getSelectionModel().getSelectedItem());
//            }
        });

        CF_SET.setOnAction(event -> {
            String set_override = "";
            if (!CF_SET_OVERRIDE.getSelectionModel().isEmpty()) {
                set_override = CF_SET_OVERRIDE.getSelectionModel().getSelectedItem();
            }
            db.setSelectedSets(3, CF_SECTIONS.getSelectionModel().getSelectedItem(),
                    CF_SET.getSelectionModel().getSelectedItem(), set_override, "");
//            setComponentContents(3, CF_SET.getSelectionModel().getSelectedItem());
            updateComponentFromList(3, CF_SET.getSelectionModel().getSelectedItem());
        });

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
        gridPaneList.add(POLE_FOOTINGS);
        gridPaneList.add(CONCRETE_FLOOR);
    }

    public void setComponentContents(int part_id, String set) { //add set override later on
        foundationsMaterials.clear();
        if (set != null && !set.isEmpty()) {
            switch (set) {
                case "17.5 Footing":
                    if (part_id == 1) {
                        foundationsMaterials.addAll(new foundationsMaterials("Concrete", "32100832",
                                "17.5MPA 19MM STRUCTURAL CONCRETE", "M3", String.valueOf(new DecimalFormat("0.00").
                                format(Double.parseDouble(foundations_pf_volume.getText()))), "FOOTINGS"));
                    }
                    if (part_id == 2) {
                        foundationsMaterials.addAll(new foundationsMaterials("Concrete", "32100832",
                                "17.5MPA 19MM STRUCTURAL CONCRETE", "M3", String.valueOf(new DecimalFormat("0.00").
                                format(Double.parseDouble(foundations_cb_volume.getText()))), "FOOTINGS"));
                    }
                    break;
                case "17.5 SE620-500":
                    if (part_id == 3) {
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
                        foundationsMaterials.addAll(new foundationsMaterials("Concrete", "32100832",
                                "17.5MPA 19MM STRUCTURAL CONCRETE", "M3",
                                String.valueOf(new DecimalFormat("0.00").format(cf_volume)), "FOOTINGS"));
                        foundationsMaterials.addAll(new foundationsMaterials("Steel Mesh", "STMESE620500SM",
                                "STEEL REINFORCING MESH SE620-500SMALL 4.68X2.05 7.612M2", "SHT",
                                String.valueOf(srm_quantity), "SLAB"));
                        foundationsMaterials.addAll(new foundationsMaterials("Spacers", "STBC5065E",
                                "BAR CHAIR PLASTIC 50-65MM - PER EACH", "EACH",
                                String.valueOf(srm_quantity * 12), "SLAB"));
                        foundationsMaterials.addAll(new foundationsMaterials("Underlay", "RPPO250425",
                                "POLYTHENE BLK 250MU X 4M X 25M 2010103", "ROLL",
                                String.valueOf(pb_quantity), "SLAB"));
                        foundationsMaterials.addAll(new foundationsMaterials("Underlay", "RPJT2040116",
                                "TAPE-IT HIGH ADHESIVE PVC JOINING TAPE 48MMX30M 2040116", "ROLL",
                                String.valueOf(tp_quantity), "SLAB"));
                        foundationsMaterials.addAll(new foundationsMaterials("Tie Wire", "STW300GX1KG",
                                "TIE WIRE GALV 300MM X 1KG", "EACH",
                                String.valueOf(tw_quantity), "SLAB"));
                    }
                    break;
            }
        }
    }

    public void setComponentsFromList() {
        foundationsMaterials.clear();
        for (int i = 0; i < componentData.getComponents().size(); i++) {
            foundationsMaterials.addAll(new foundationsMaterials(
                    componentData.getComponents().get(i)[0], componentData.getComponents().get(i)[1],
                    componentData.getComponents().get(i)[2], componentData.getComponents().get(i)[3],
                    componentData.getComponents().get(i)[4], componentData.getComponents().get(i)[5]));
        }
    }

    public void updateComponentFromList(int part_id, String set) {
        foundationsMaterials.clear();
        if (set != null && !set.isEmpty()) {
            switch (set) {
                case "17.5 Footing":
                    if (part_id == 1) {
                        foundationsMaterials.addAll(new foundationsMaterials("Concrete", "32100832",
                                "17.5MPA 19MM STRUCTURAL CONCRETE", "M3", String.valueOf(new DecimalFormat("0.00").
                                format(Double.parseDouble(foundations_pf_volume.getText()))), "FOOTINGS"));

                        componentData.getComponents().clear();
                        componentData.getComponents().add(new String[] {"Concrete", "32100832", "17.5MPA 19MM STRUCTURAL CONCRETE", "M3",
                                String.valueOf(new DecimalFormat("0.00").format(Double.parseDouble(foundations_pf_volume.getText()))), "FOOTINGS"});

                    }
                    if (part_id == 2) {
                        foundationsMaterials.addAll(new foundationsMaterials("Concrete", "32100832",
                                "17.5MPA 19MM STRUCTURAL CONCRETE", "M3", String.valueOf(new DecimalFormat("0.00").
                                format(Double.parseDouble(foundations_cb_volume.getText()))), "FOOTINGS"));

                        componentData.getComponents().clear();
                        componentData.getComponents().add(new String[] {
                                "Concrete", "32100832", "17.5MPA 19MM STRUCTURAL CONCRETE", "M3",
                                String.valueOf(new DecimalFormat("0.00").format(
                                        Double.parseDouble(foundations_cb_volume.getText()))), "FOOTINGS"
                        });

                    }
                    break;
                case "17.5 SE620-500":
                    if (part_id == 3) {
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
                        foundationsMaterials.addAll(new foundationsMaterials("Concrete", "32100832",
                                "17.5MPA 19MM STRUCTURAL CONCRETE", "M3",
                                String.valueOf(new DecimalFormat("0.00").format(cf_volume)), "FOOTINGS"));
                        foundationsMaterials.addAll(new foundationsMaterials("Steel Mesh", "STMESE620500SM",
                                "STEEL REINFORCING MESH SE620-500SMALL 4.68X2.05 7.612M2", "SHT",
                                String.valueOf(srm_quantity), "SLAB"));
                        foundationsMaterials.addAll(new foundationsMaterials("Spacers", "STBC5065E",
                                "BAR CHAIR PLASTIC 50-65MM - PER EACH", "EACH",
                                String.valueOf(srm_quantity * 12), "SLAB"));
                        foundationsMaterials.addAll(new foundationsMaterials("Underlay", "RPPO250425",
                                "POLYTHENE BLK 250MU X 4M X 25M 2010103", "ROLL",
                                String.valueOf(pb_quantity), "SLAB"));
                        foundationsMaterials.addAll(new foundationsMaterials("Underlay", "RPJT2040116",
                                "TAPE-IT HIGH ADHESIVE PVC JOINING TAPE 48MMX30M 2040116", "ROLL",
                                String.valueOf(tp_quantity), "SLAB"));
                        foundationsMaterials.addAll(new foundationsMaterials("Tie Wire", "STW300GX1KG",
                                "TIE WIRE GALV 300MM X 1KG", "EACH",
                                String.valueOf(tw_quantity), "SLAB"));

                        componentData.getComponents().clear();
                        for (foundationsMaterials fm : foundationsMaterials) {
                            componentData.getComponents().add(new String[] {
                                    fm.getComponent(), fm.getSku_number(), fm.getDescription(), fm.getUnit(),
                                    fm.getQuantity(), fm.getUsage()
                            });
                        }
                    }
                    break;
            }
        }
    }
}
