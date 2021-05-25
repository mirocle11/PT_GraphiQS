package Controllers.Sheets.Shed;

import Controllers.setupSheetsController;
import DataBase.DataBase;
import Model.ComponentData;
import Model.data.shed.externalFraming.columnsSec;
import Model.data.shed.externalFraming.polesSec;
import Model.data.shed.externalFramingMaterials;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static Controllers.layoutsController.*;

public class externalFramingController implements Initializable {

    //indicators
    public Label id_indicator = new Label(); //part id every selection
    public Label selectedMaterialPL = new Label();
    public Label selectedMaterialCL = new Label();
    public Label selectedMaterialGirts = new Label();

    //collections
    public ObservableList<String> parts = FXCollections.observableArrayList();
    public VBox PARTS_VBOX;
    public ArrayList<JFXButton> buttonList = new ArrayList<>();
    public ArrayList<GridPane> gridPaneList = new ArrayList<>();

    //components
    public ListView<Integer> PL_SECTIONS, CL_SECTIONS, GIRTS_SECTIONS;
    public JFXComboBox<String> PL_SET, PL_SET_OVERRIDE, CL_SET, CL_SET_OVERRIDE, GIRTS_SET, GIRTS_SET_OVERRIDE;
    public TextArea PL_NOTES, CL_NOTES, GIRTS_NOTES;

    //gridpanes (parts)
    public GridPane POLES, COLUMNS, GIRTS, DOORS, WINDOWS;

    //tables
    public JFXTreeTableView<externalFramingMaterials> MATERIALS_TBL;
    public TreeTableColumn<externalFramingMaterials, String> COMPONENT_COL;
    public TreeTableColumn<externalFramingMaterials, String> SKU_NUMBER_COL;
    public TreeTableColumn<externalFramingMaterials, String> DESCRIPTION_COL;
    public TreeTableColumn<externalFramingMaterials, String> UNIT_COL;
    public TreeTableColumn<externalFramingMaterials, String> QUANTITY_COL;
    public TreeTableColumn<externalFramingMaterials, String> USAGE_COL;

    public TreeTableView<polesSec> POLES_TABLE;
    public TreeTableColumn<polesSec, String> PL_COL_NO;
    public TreeTableColumn<polesSec, String> PL_COL_QTY;

    public TreeTableView<polesSec> COLUMNS_TABLE;
    public TreeTableColumn<polesSec, String> CL_COL_NO;
    public TreeTableColumn<polesSec, String> CL_COL_QTY;

    //data lists
    public static ObservableList<externalFramingMaterials> externalFramingMaterials;
    public static ObservableList<Integer> externalFramingSectionListPL = FXCollections.observableArrayList();
    public static ObservableList<Integer> externalFramingSectionListCL = FXCollections.observableArrayList();
    public static ObservableList<Integer> externalFramingSectionListGirts = FXCollections.observableArrayList();

    public static ObservableList<String> setsListPL = FXCollections.observableArrayList();
    public static ObservableList<String> setsListCL = FXCollections.observableArrayList();
    public static ObservableList<String> setsListGirts = FXCollections.observableArrayList();

    // section dimensions
    public static ObservableList<polesSec> polesSec = FXCollections.observableArrayList();
    public static ObservableList<columnsSec> columnsSec = FXCollections.observableArrayList();

    private ComponentData componentData;
    private String key;

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        loadPanes();

        DataBase db = DataBase.getInstance();
        parts.clear();
        db.displayShedParts(2, parts);
        db.getSets(4, setsListPL);
        db.getSets(5, setsListCL);
        db.getSets(6, setsListGirts);

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
                externalFramingMaterials.clear();
                db.getPartID(2, button.getId(), id_indicator);
                buttonList.forEach(button1 -> { //selection highlighter
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

        //externalframing materials
        externalFramingMaterials = FXCollections.observableArrayList();

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

        TreeItem<externalFramingMaterials> externalFramingControllerTreeItem = new RecursiveTreeItem<>
                (externalFramingMaterials, RecursiveTreeObject::getChildren);
        MATERIALS_TBL.setRoot(externalFramingControllerTreeItem);
        MATERIALS_TBL.setShowRoot(false);

        PL_SET.setItems(setsListPL);
        PL_SECTIONS.setItems(externalFramingSectionListPL);

        PL_SECTIONS.setOnMouseReleased(event -> {
            //section list event
            if (!PL_SECTIONS.getSelectionModel().isEmpty()) {

                try {
                    key = "external_framing_poles_" + PL_SECTIONS.getSelectionModel().getSelectedItem();

                    componentData = new ComponentData("external framing", "poles",
                            PL_SECTIONS.getSelectionModel().getSelectedItem().toString());

                    if (setupSheetsController.componentList.containsKey(key)) {
                        componentData = setupSheetsController.componentList.get(key);
                    } else {
                        setupSheetsController.componentList.put(key, componentData);
                    }

                    PL_SET.setDisable(false);
                    PL_SET_OVERRIDE.setDisable(false);
                    db.getSelectedSets(Integer.parseInt(id_indicator.getText()), PL_SECTIONS.getSelectionModel().getSelectedItem(),
                            PL_SET, PL_SET_OVERRIDE);
                    db.displayNotes(Integer.parseInt(id_indicator.getText()), PL_SECTIONS.getSelectionModel().getSelectedItem(),
                            PL_NOTES);
                    db.showExternalFramingSD("Poles", PL_SECTIONS.getSelectionModel().getSelectedItem(), polesSec, columnsSec);

//                    setComponentContents(1, PL_SET.getSelectionModel().getSelectedItem());
                    setComponentsFromList();
                    //setComopnents from list
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        PL_SET.setOnAction(event -> {
            String set_override = "";
            if (!PL_SET_OVERRIDE.getSelectionModel().isEmpty()) {
                set_override = PL_SET_OVERRIDE.getSelectionModel().getSelectedItem();
            }
            db.setSelectedSets(Integer.parseInt(id_indicator.getText()), PL_SECTIONS.getSelectionModel()
                    .getSelectedItem(), PL_SET.getSelectionModel().getSelectedItem(), set_override, "");

//            setComponentContents(1, PL_SET.getSelectionModel().getSelectedItem());
            updateComponentFromList(1, PL_SET.getSelectionModel().getSelectedItem());
        });

        CL_SET.setItems(setsListCL);
        CL_SECTIONS.setItems(externalFramingSectionListCL);

        CL_SECTIONS.setOnMouseReleased(event -> {
            //section list event
            if (!CL_SECTIONS.getSelectionModel().isEmpty()) {
                try {
                    key = "external_framing_columns_" + CL_SECTIONS.getSelectionModel().getSelectedItem();

                    componentData = new ComponentData("external framing", "poles",
                            CL_SECTIONS.getSelectionModel().getSelectedItem().toString());

                    if (setupSheetsController.componentList.containsKey(key)) {
                        componentData = setupSheetsController.componentList.get(key);
                    } else {
                        setupSheetsController.componentList.put(key, componentData);
                    }

                    CL_SET.setDisable(false);
                    CL_SET_OVERRIDE.setDisable(false);

                    db.getSelectedSets(Integer.parseInt(id_indicator.getText()), CL_SECTIONS.getSelectionModel().getSelectedItem(),
                            CL_SET, CL_SET_OVERRIDE);
                    db.displayNotes(Integer.parseInt(id_indicator.getText()), CL_SECTIONS.getSelectionModel().getSelectedItem(),
                            CL_NOTES);
                    db.showExternalFramingSD("Columns", CL_SECTIONS.getSelectionModel().getSelectedItem(), polesSec, columnsSec);
//                    setComponentContents(2, CL_SET.getSelectionModel().getSelectedItem());
                    setComponentsFromList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        CL_SET.setOnAction(event -> {
            String set_override = "";
            if (!CL_SET_OVERRIDE.getSelectionModel().isEmpty()) {
                set_override = CL_SET_OVERRIDE.getSelectionModel().getSelectedItem();
            }
            db.setSelectedSets(Integer.parseInt(id_indicator.getText()), CL_SECTIONS.getSelectionModel()
                    .getSelectedItem(), CL_SET.getSelectionModel().getSelectedItem(), set_override, "");
//            setComponentContents(2, CL_SET.getSelectionModel().getSelectedItem());
            updateComponentFromList(2, CL_SET.getSelectionModel().getSelectedItem());
        });

        GIRTS_SET.setItems(setsListGirts);
        GIRTS_SECTIONS.setItems(externalFramingSectionListGirts);

        GIRTS_SECTIONS.setOnMouseReleased(event -> {
            //section list event
            if (!GIRTS_SECTIONS.getSelectionModel().isEmpty()) {
                try {
                    key = "external_framing_girt_" + PL_SECTIONS.getSelectionModel().getSelectedItem();

                    componentData = new ComponentData("external framing", "girt",
                            PL_SECTIONS.getSelectionModel().getSelectedItem().toString());

                    if (setupSheetsController.componentList.containsKey(key)) {
                        componentData = setupSheetsController.componentList.get(key);
                    } else {
                        setupSheetsController.componentList.put(key, componentData);
                    }

                    GIRTS_SET.setDisable(false);
                    GIRTS_SET_OVERRIDE.setDisable(false);

                    db.getSelectedSets(Integer.parseInt(id_indicator.getText()), GIRTS_SECTIONS.getSelectionModel().getSelectedItem(),
                            GIRTS_SET, GIRTS_SET_OVERRIDE);
                    db.displayNotes(Integer.parseInt(id_indicator.getText()), GIRTS_SECTIONS.getSelectionModel().getSelectedItem(),
                            GIRTS_NOTES);
                    db.showExternalFramingSD("Girts", GIRTS_SECTIONS.getSelectionModel().getSelectedItem(), polesSec, columnsSec);
//                    setComponentContents(3, GIRTS_SET.getSelectionModel().getSelectedItem());
                    setComponentsFromList();
                } catch (Exception e) {

                }
            }
        });

        GIRTS_SET.setOnAction(event -> {
            String set_override = "";
            if (!GIRTS_SET_OVERRIDE.getSelectionModel().isEmpty()) {
                set_override = GIRTS_SET_OVERRIDE.getSelectionModel().getSelectedItem();
            }
            db.setSelectedSets(Integer.parseInt(id_indicator.getText()), GIRTS_SECTIONS.getSelectionModel()
                    .getSelectedItem(), GIRTS_SET.getSelectionModel().getSelectedItem(), set_override, "");
//            setComponentContents(3, GIRTS_SET.getSelectionModel().getSelectedItem());
            updateComponentFromList(3, GIRTS_SET.getSelectionModel().getSelectedItem());
        });

    }

    public void setComponentContents(int part_id, String set) { // add set override later on
        externalFramingMaterials.clear();
        DataBase db = DataBase.getInstance();
        if (set != null && !set.isEmpty()) {
            switch (set) {
                case "200mm SED Pole":
                    externalFramingMaterials.addAll(new externalFramingMaterials("Poles", "",
                            "200 SED Pole", "EACH", externalFramingPolesData.get(
                            PL_SECTIONS.getSelectionModel().getSelectedIndex()).getQuantity(), "Poles"));
                    break;
                case "Building Pole H5 175-199":
                    db.getSelectedMaterial(4, PL_SECTIONS.getSelectionModel().getSelectedItem(), selectedMaterialPL);

                    if (selectedMaterialPL.getText().equals("4.2M BUILDING POLE H5 175-199")) {
                        externalFramingMaterials.addAll(new externalFramingMaterials("Poles", "RWBP17542",
                                "4.2M BUILDING POLE H5 175-199", "EACH", externalFramingPolesData.get(
                                PL_SECTIONS.getSelectionModel().getSelectedIndex()).getQuantity(), "Poles"));
                    } else if (selectedMaterialPL.getText().equals("4.8M Building Pole H5 175-199")) {
                        externalFramingMaterials.addAll(new externalFramingMaterials("Poles", "RWBP17548",
                                "4.8M BUILDING POLE H5 175-199", "EACH", externalFramingPolesData.get(
                                PL_SECTIONS.getSelectionModel().getSelectedIndex()).getQuantity(), "Poles"));
                    } else if (selectedMaterialPL.getText().equals("5.4M Building Pole H5 175-199")) {
                        externalFramingMaterials.addAll(new externalFramingMaterials("Poles", "RWBP17554",
                                "5.4M BUILDING POLE H5 175-199", "EACH", externalFramingPolesData.get(
                                PL_SECTIONS.getSelectionModel().getSelectedIndex()).getQuantity(), "Poles"));
                    }
                    break;
                case "2/150 x 50 H3.2 Column":
                    // insert declaration of material selected -> condition material for calculations
                    db.getSelectedMaterial(5, CL_SECTIONS.getSelectionModel().getSelectedItem(), selectedMaterialCL);

                    if (selectedMaterialCL.getText().equals("150 X 50 RAD SG8 VERIFIED H3.2 WET RS 3.6M")) {
                        externalFramingMaterials.addAll(new externalFramingMaterials("Columns", "15050RVH32RS36",
                                "150 X 50 RAD SG8 VERIFIED H3.2 WET RS 3.6M", "LGTH", String.valueOf(
                                Integer.parseInt(externalFramingColumnsData.get(CL_SECTIONS.getSelectionModel()
                                        .getSelectedIndex()).getQuantity()) * 2), "Columns"));
                    } else if (selectedMaterialCL.getText().equals("150 X 50 RAD SG8 VERIFIED H3.2 WET RS 4.8M")) {
                        externalFramingMaterials.addAll(new externalFramingMaterials("Columns", "15050RVH32RS48",
                                "150 X 50 RAD SG8 VERIFIED H3.2 WET RS 4.8M", "LGTH", String.valueOf(
                                Integer.parseInt(externalFramingColumnsData.get(CL_SECTIONS.getSelectionModel()
                                        .getSelectedIndex()).getQuantity()) * 2), "Columns"));
                    } else if (selectedMaterialCL.getText().equals("150 X 50 RAD SG8 VERIFIED H3.2 WET RS 6.0M")) {
                        externalFramingMaterials.addAll(new externalFramingMaterials("Columns", "15050RVH32RS60",
                                "150 X 50 RAD SG8 VERIFIED H3.2 WET RS 6.0M", "LGTH", String.valueOf(
                                Integer.parseInt(externalFramingColumnsData.get(CL_SECTIONS.getSelectionModel()
                                        .getSelectedIndex()).getQuantity()) * 2), "Columns"));
                    }
                    break;
                case "2/200 x 50 H3.2 Column":
                    db.getSelectedMaterial(5, CL_SECTIONS.getSelectionModel().getSelectedItem(), selectedMaterialCL);

                    if (selectedMaterialCL.getText().equals("200 X 50 RAD SG8 VERIFIED H3.2 WET RS 4.8M")) {
                        externalFramingMaterials.addAll(new externalFramingMaterials("Columns", "20050RVH32RS48",
                                "200 X 50 RAD SG8 VERIFIED H3.2 WET RS 4.8M", "LGTH", String.valueOf(
                                Integer.parseInt(externalFramingColumnsData.get(CL_SECTIONS.getSelectionModel()
                                        .getSelectedIndex()).getQuantity()) * 2), "Columns"));
                    }
                    break;
                case "2/250 x 50 H3.2 Column":
                    db.getSelectedMaterial(5, CL_SECTIONS.getSelectionModel().getSelectedItem(), selectedMaterialCL);

                    if (selectedMaterialCL.getText().equals("250 X 50 RAD SG8 VERIFIED H3.2 WET RS 4.8M")) {
                        externalFramingMaterials.addAll(new externalFramingMaterials("Columns", "25050RVH32RS48",
                                "250 X 50 RAD SG8 VERIFIED H3.2 WET RS 4.8M", "LGTH", String.valueOf(
                                Integer.parseInt(externalFramingColumnsData.get(CL_SECTIONS.getSelectionModel()
                                        .getSelectedIndex()).getQuantity()) * 2), "Columns"));
                    }
                    break;
                case "150 x 50 H3.2 Girts":
                    //calculations
                    int total_150 = (int) Double.parseDouble(girtsData.get(6));

                    int remainder_6_150 = total_150 % 6000;
                    int remainder_48_150 = total_150 % 4800;
                    int remainder_36_150 = total_150 % 3600;

                    int girts_6_150 = total_150 / 6000;
                    if (remainder_6_150 > 0) {
                        girts_6_150++;
                    }
                    int girts_48_150 = total_150 / 4800;
                    if (remainder_48_150 > 0) {
                        girts_48_150++;
                    }
                    int girts_36_150 = total_150 / 3600;
                    if (remainder_36_150 > 0) {
                        girts_36_150++;
                    }

                    db.getSelectedMaterial(6, GIRTS_SECTIONS.getSelectionModel().getSelectedItem(), selectedMaterialGirts);

                    if (selectedMaterialGirts.getText().equals("150 X 50 RAD SG8 VERIFIED H3.2 WET RS 3.6M")) {
                        externalFramingMaterials.addAll(new externalFramingMaterials("Girts", "15050RVH32RS36",
                                "150 X 50 RAD SG8 VERIFIED H3.2 WET RS 3.6M", "LGTH", String.valueOf(girts_6_150), "Girts"));
                    } else if (selectedMaterialGirts.getText().equals("150 X 50 RAD SG8 VERIFIED H3.2 WET RS 4.8M")) {
                        externalFramingMaterials.addAll(new externalFramingMaterials("Girts", "15050RVH32RS36",
                                "150 X 50 RAD SG8 VERIFIED H3.2 WET RS 4.8M", "LGTH", String.valueOf(girts_48_150), "Girts"));
                    } else if (selectedMaterialGirts.getText().equals("150 X 50 RAD SG8 VERIFIED H3.2 WET RS 6.0M")) {
                        externalFramingMaterials.addAll(new externalFramingMaterials("Girts", "15050RVH32RS36",
                                "150 X 50 RAD SG8 VERIFIED H3.2 WET RS 6.0M", "LGTH", String.valueOf(girts_36_150), "Girts"));
                    }
                    break;
                case "200 x 50 H3.2 Girts":
                    //calculations
                    int total_200 = (int) Double.parseDouble(girtsData.get(6));

                    int remainder_48_200 = total_200 % 4800;

                    int girts_48_200 = total_200 / 4800;
                    if (remainder_48_200 > 0) {
                        girts_48_200++;
                    }

                    if (selectedMaterialGirts.getText().equals("200 X 50 RAD SG8 VERIFIED H3.2 WET RS 4.8M")) {
                        externalFramingMaterials.addAll(new externalFramingMaterials("Girts", "15050RVH32RS36",
                                "200 X 50 RAD SG8 VERIFIED H3.2 WET RS 4.8M", "LGTH", String.valueOf(girts_48_200), "Girts"));
                    }
                    break;
                case "250 x 50 H3.2 Girts":
                    //calculations
                    int total_250 = (int) Double.parseDouble(girtsData.get(6));

                    int remainder_250 = total_250 % 4800;
                    int girts_48_250 = total_250 / 4800;
                    if (remainder_250 > 0) {
                        girts_48_250++;
                    }

                    if (selectedMaterialGirts.getText().equals("250 X 50 RAD SG8 VERIFIED H3.2 WET RS 4.8M")) {
                        externalFramingMaterials.addAll(new externalFramingMaterials("Girts", "15050RVH32RS36",
                                "250 X 50 RAD SG8 VERIFIED H3.2 WET RS 4.8M", "LGTH", String.valueOf(girts_48_250), "Girts"));
                    }
                    break;
            }
        }
    }

    public void setComponentsFromList() {
        externalFramingMaterials.clear();
        for (int i = 0; i < componentData.getComponents().size(); i++) {
            externalFramingMaterials.addAll(new externalFramingMaterials(
                    componentData.getComponents().get(i)[0], componentData.getComponents().get(i)[1],
                    componentData.getComponents().get(i)[2], componentData.getComponents().get(i)[3],
                    componentData.getComponents().get(i)[4], componentData.getComponents().get(i)[5]));
        }
    }

    public void updateComponentFromList(int part_id, String set) { // add set override later on
        externalFramingMaterials.clear();
        DataBase db = DataBase.getInstance();
        if (set != null && !set.isEmpty()) {
            switch (set) {
                case "200mm SED Pole":
                    externalFramingMaterials.addAll(new externalFramingMaterials("Poles", "",
                            "200 SED Pole", "EACH", externalFramingPolesData.get(
                            PL_SECTIONS.getSelectionModel().getSelectedIndex()).getQuantity(), "Poles"));


                    break;
                case "Building Pole H5 175-199":
                    db.getSelectedMaterial(4, PL_SECTIONS.getSelectionModel().getSelectedItem(), selectedMaterialPL);

                    if (selectedMaterialPL.getText().equals("4.2M BUILDING POLE H5 175-199")) {
                        externalFramingMaterials.addAll(new externalFramingMaterials("Poles", "RWBP17542",
                                "4.2M BUILDING POLE H5 175-199", "EACH", externalFramingPolesData.get(
                                PL_SECTIONS.getSelectionModel().getSelectedIndex()).getQuantity(), "Poles"));
                    } else if (selectedMaterialPL.getText().equals("4.8M Building Pole H5 175-199")) {
                        externalFramingMaterials.addAll(new externalFramingMaterials("Poles", "RWBP17548",
                                "4.8M BUILDING POLE H5 175-199", "EACH", externalFramingPolesData.get(
                                PL_SECTIONS.getSelectionModel().getSelectedIndex()).getQuantity(), "Poles"));
                    } else if (selectedMaterialPL.getText().equals("5.4M Building Pole H5 175-199")) {
                        externalFramingMaterials.addAll(new externalFramingMaterials("Poles", "RWBP17554",
                                "5.4M BUILDING POLE H5 175-199", "EACH", externalFramingPolesData.get(
                                PL_SECTIONS.getSelectionModel().getSelectedIndex()).getQuantity(), "Poles"));
                    }
                    break;
                case "2/150 x 50 H3.2 Column":
                    // insert declaration of material selected -> condition material for calculations
                    db.getSelectedMaterial(5, CL_SECTIONS.getSelectionModel().getSelectedItem(), selectedMaterialCL);

                    if (selectedMaterialCL.getText().equals("150 X 50 RAD SG8 VERIFIED H3.2 WET RS 3.6M")) {
                        externalFramingMaterials.addAll(new externalFramingMaterials("Columns", "15050RVH32RS36",
                                "150 X 50 RAD SG8 VERIFIED H3.2 WET RS 3.6M", "LGTH", String.valueOf(
                                Integer.parseInt(externalFramingColumnsData.get(CL_SECTIONS.getSelectionModel()
                                        .getSelectedIndex()).getQuantity()) * 2), "Columns"));
                    } else if (selectedMaterialCL.getText().equals("150 X 50 RAD SG8 VERIFIED H3.2 WET RS 4.8M")) {
                        externalFramingMaterials.addAll(new externalFramingMaterials("Columns", "15050RVH32RS48",
                                "150 X 50 RAD SG8 VERIFIED H3.2 WET RS 4.8M", "LGTH", String.valueOf(
                                Integer.parseInt(externalFramingColumnsData.get(CL_SECTIONS.getSelectionModel()
                                        .getSelectedIndex()).getQuantity()) * 2), "Columns"));
                    } else if (selectedMaterialCL.getText().equals("150 X 50 RAD SG8 VERIFIED H3.2 WET RS 6.0M")) {
                        externalFramingMaterials.addAll(new externalFramingMaterials("Columns", "15050RVH32RS60",
                                "150 X 50 RAD SG8 VERIFIED H3.2 WET RS 6.0M", "LGTH", String.valueOf(
                                Integer.parseInt(externalFramingColumnsData.get(CL_SECTIONS.getSelectionModel()
                                        .getSelectedIndex()).getQuantity()) * 2), "Columns"));
                    }
                    break;
                case "2/200 x 50 H3.2 Column":
                    db.getSelectedMaterial(5, CL_SECTIONS.getSelectionModel().getSelectedItem(), selectedMaterialCL);

                    if (selectedMaterialCL.getText().equals("200 X 50 RAD SG8 VERIFIED H3.2 WET RS 4.8M")) {
                        externalFramingMaterials.addAll(new externalFramingMaterials("Columns", "20050RVH32RS48",
                                "200 X 50 RAD SG8 VERIFIED H3.2 WET RS 4.8M", "LGTH", String.valueOf(
                                Integer.parseInt(externalFramingColumnsData.get(CL_SECTIONS.getSelectionModel()
                                        .getSelectedIndex()).getQuantity()) * 2), "Columns"));
                    }
                    break;
                case "2/250 x 50 H3.2 Column":
                    db.getSelectedMaterial(5, CL_SECTIONS.getSelectionModel().getSelectedItem(), selectedMaterialCL);

                    if (selectedMaterialCL.getText().equals("250 X 50 RAD SG8 VERIFIED H3.2 WET RS 4.8M")) {
                        externalFramingMaterials.addAll(new externalFramingMaterials("Columns", "25050RVH32RS48",
                                "250 X 50 RAD SG8 VERIFIED H3.2 WET RS 4.8M", "LGTH", String.valueOf(
                                Integer.parseInt(externalFramingColumnsData.get(CL_SECTIONS.getSelectionModel()
                                        .getSelectedIndex()).getQuantity()) * 2), "Columns"));
                    }
                    break;
                case "150 x 50 H3.2 Girts":
                    //calculations
                    int total_150 = (int) Double.parseDouble(girtsData.get(6));

                    int remainder_6_150 = total_150 % 6000;
                    int remainder_48_150 = total_150 % 4800;
                    int remainder_36_150 = total_150 % 3600;

                    int girts_6_150 = total_150 / 6000;
                    if (remainder_6_150 > 0) {
                        girts_6_150++;
                    }
                    int girts_48_150 = total_150 / 4800;
                    if (remainder_48_150 > 0) {
                        girts_48_150++;
                    }
                    int girts_36_150 = total_150 / 3600;
                    if (remainder_36_150 > 0) {
                        girts_36_150++;
                    }

                    db.getSelectedMaterial(6, GIRTS_SECTIONS.getSelectionModel().getSelectedItem(), selectedMaterialGirts);

                    if (selectedMaterialGirts.getText().equals("150 X 50 RAD SG8 VERIFIED H3.2 WET RS 3.6M")) {
                        externalFramingMaterials.addAll(new externalFramingMaterials("Girts", "15050RVH32RS36",
                                "150 X 50 RAD SG8 VERIFIED H3.2 WET RS 3.6M", "LGTH", String.valueOf(girts_6_150), "Girts"));
                    } else if (selectedMaterialGirts.getText().equals("150 X 50 RAD SG8 VERIFIED H3.2 WET RS 4.8M")) {
                        externalFramingMaterials.addAll(new externalFramingMaterials("Girts", "15050RVH32RS36",
                                "150 X 50 RAD SG8 VERIFIED H3.2 WET RS 4.8M", "LGTH", String.valueOf(girts_48_150), "Girts"));
                    } else if (selectedMaterialGirts.getText().equals("150 X 50 RAD SG8 VERIFIED H3.2 WET RS 6.0M")) {
                        externalFramingMaterials.addAll(new externalFramingMaterials("Girts", "15050RVH32RS36",
                                "150 X 50 RAD SG8 VERIFIED H3.2 WET RS 6.0M", "LGTH", String.valueOf(girts_36_150), "Girts"));
                    }
                    break;
                case "200 x 50 H3.2 Girts":
                    //calculations
                    int total_200 = (int) Double.parseDouble(girtsData.get(6));

                    int remainder_48_200 = total_200 % 4800;

                    int girts_48_200 = total_200 / 4800;
                    if (remainder_48_200 > 0) {
                        girts_48_200++;
                    }

                    if (selectedMaterialGirts.getText().equals("200 X 50 RAD SG8 VERIFIED H3.2 WET RS 4.8M")) {
                        externalFramingMaterials.addAll(new externalFramingMaterials("Girts", "15050RVH32RS36",
                                "200 X 50 RAD SG8 VERIFIED H3.2 WET RS 4.8M", "LGTH", String.valueOf(girts_48_200), "Girts"));
                    }
                    break;
                case "250 x 50 H3.2 Girts":
                    //calculations
                    int total_250 = (int) Double.parseDouble(girtsData.get(6));

                    int remainder_250 = total_250 % 4800;
                    int girts_48_250 = total_250 / 4800;
                    if (remainder_250 > 0) {
                        girts_48_250++;
                    }

                    if (selectedMaterialGirts.getText().equals("250 X 50 RAD SG8 VERIFIED H3.2 WET RS 4.8M")) {
                        externalFramingMaterials.addAll(new externalFramingMaterials("Girts", "15050RVH32RS36",
                                "250 X 50 RAD SG8 VERIFIED H3.2 WET RS 4.8M", "LGTH", String.valueOf(girts_48_250), "Girts"));
                    }
                    break;
            }
        }
        componentData.getComponents().clear();
        for (externalFramingMaterials fm : externalFramingMaterials) {
            componentData.getComponents().add(new String[]{
                    fm.getComponent(), fm.getSku_number(), fm.getDescription(), fm.getUnit(),
                    fm.getQuantity(), fm.getUsage()
            });
        }
    }

    public void loadPanes() {
        gridPaneList.add(POLES);
        gridPaneList.add(COLUMNS);
        gridPaneList.add(GIRTS);
        gridPaneList.add(DOORS);
        gridPaneList.add(WINDOWS);
    }

}
