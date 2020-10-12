package Controllers.Sheets.Shed;

import DataBase.DataBase;
import Main.Main;
import Model.data.shed.foundations.postFootingsSec;
import Model.data.shed.foundationsData;
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
import javafx.scene.input.MouseButton;
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
    public int pf_section_no = 0;
    public int cb_section_no = 0;
    public int cf_section_no = 0;

    public int selectedSection = 0;

    //collections
    public ObservableList<String> parts = FXCollections.observableArrayList();
    public ArrayList<GridPane> gridPaneList = new ArrayList<>();
    public VBox PARTS_VBOX;
    public int pf_dimension_no = 0;
    public ToggleGroup toggleGroup = new ToggleGroup();
    public ArrayList<JFXButton> buttonList = new ArrayList<>();

    //gridpanes (parts)
    public GridPane POST_FOOTINGS, CONCRETE_BORES, CONCRETE_FLOOR;

    //components
    public ListView<Integer> PF_SECTIONS, CB_SECTIONS, CF_SECTIONS;
    public JFXButton PF_ADD_SECTION, PF_REMOVE_SECTION, CB_ADD_SECTION, CB_REMOVE_SECTION, CF_ADD_SECTION, CF_REMOVE_SECTION;
    public JFXComboBox<String> PF_SET, PF_SET_OVERRIDE;
    public JFXTextField PF_QTY;

    //component editor
    private double xOffset = 0;
    private double yOffset = 0;
    private static Stage editComponentStage;

    //sample data
    private ObservableList<String> PF_SET_DATA = FXCollections.observableArrayList("(None)", "17.5 Footing [1]",
            "17.5 Post [1]", "17.5 Slab", "17.5 Con Footings D12 CHANGE", "17.5 Con Post D12 CHANGE", "17.5 Con Slab D12 CHANGE");

    private ObservableList<String> PF_SET_OVERRIDE_DATA = FXCollections.observableArrayList("" , "20 Mpa",
            "25 Mpa", "30 Mpa");

    //tables
    public TreeTableView<foundationsData> TREE_TABLE_VIEW; //components tbl
    public TreeTableColumn<foundationsData, String> COMPONENT;
    public TreeTableColumn<foundationsData, String> CODE;
    public TreeTableColumn<foundationsData, String> DESCRIPTION;
    public TreeTableColumn<foundationsData, String> EXTRA1;
    public TreeTableColumn<foundationsData, String> EXTRA2;
    public TreeTableColumn<foundationsData, String> QUANTITY;
    public TreeTableColumn<foundationsData, String> USAGE;
    public TreeTableColumn<foundationsData, String> WASTE;
    public TreeTableColumn<foundationsData, String> SUBHEADING;
    public TreeTableColumn<foundationsData, String> USAGE2;

    //table right click menu
    public ContextMenu componentsMenu = new ContextMenu();
    public MenuItem editRow = new MenuItem("Edit Row");
    public MenuItem clearRow = new MenuItem("Clear Row");
    public MenuItem productSelect = new MenuItem("Product Select");

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
    public static ObservableList<foundationsData> foundationsData; //components
    public static ObservableList<postFootingsSec> postFootingsData; //section dimensions
    public static ObservableList<foundationsMaterials> foundationsMaterials;

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
                foundationsData.clear();
                db.getPartID(1, button.getId(), id_indicator);
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

        //component table
        foundationsData = FXCollections.observableArrayList();

        COMPONENT.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("component")
        );
        CODE.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("code")
        );
        DESCRIPTION.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("description")
        );
        EXTRA1.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("extra1")
        );
        EXTRA2.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("extra2")
        );
        QUANTITY.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("quantity")
        );
        USAGE.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("usage")
        );
        WASTE.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("waste")
        );
        SUBHEADING.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("subheading")
        );
        USAGE2.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("usage2")
        );

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

        TreeItem<foundationsData> component_root = new RecursiveTreeItem<>(foundationsData, RecursiveTreeObject::getChildren);
        TREE_TABLE_VIEW.setRoot(component_root);
        TREE_TABLE_VIEW.setShowRoot(false);

        TreeItem<postFootingsSec> post_footings_root = new RecursiveTreeItem<>(postFootingsData, RecursiveTreeObject::getChildren);
        PF_TABLE.setRoot(post_footings_root);
        PF_TABLE.setShowRoot(false);

        TreeItem<foundationsMaterials> foundationsMaterialsDataTreeItem = new RecursiveTreeItem<>
                (foundationsMaterials, RecursiveTreeObject::getChildren);
        MATERIALS_TBL.setRoot(foundationsMaterialsDataTreeItem);
        MATERIALS_TBL.setShowRoot(false);

        foundationsMaterials.addAll(new foundationsMaterials("STMESE620500SM",
                "STEEL REINFORCING MESH SE620-500SMALL 4.68X2.05 7.612M2", "SHT", "26"));
        foundationsMaterials.addAll(new foundationsMaterials("STBC5065E",
                "BAR CHAIR PLASTIC 50-65MM - PER EACH", "EACH", "300"));
        foundationsMaterials.addAll(new foundationsMaterials("RPPO250425",
                "POLYTHENE BLK 250MU X 4M X 25M 2010103", "ROLL", "2"));
        foundationsMaterials.addAll(new foundationsMaterials("RPJT2040116",
                "TAPE-IT HIGH ADHESIVE PVC JOINING TAPE 48MMX30M 2040116", "ROLL", "4"));
        foundationsMaterials.addAll(new foundationsMaterials("STW300GX1KG",
                "TIE WIRE GALV 300MM X 1KG", "EACH", "2"));

        //component table menu
        componentsMenu.getItems().add(editRow);
        componentsMenu.getItems().add(clearRow);
        componentsMenu.getItems().add(productSelect);

        TREE_TABLE_VIEW.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                componentsMenu.show(TREE_TABLE_VIEW, event.getScreenX(), event.getScreenY());
            } else {
                componentsMenu.hide();
            }
        });

        //menu items (components)
        editRow.setOnAction(event -> {
            loadComponentEditor();
        });

        clearRow.setOnAction(event -> {
            //clears details on selected row
        });

        productSelect.setOnAction(event -> {

        });

        //post footings
        PF_ADD_SECTION.setOnAction(event -> {
            pf_section_no++;
            postFootingsData.clear();
            clearForms();
            PF_SECTIONS.getItems().add(pf_section_no);
            db.setSections(Integer.parseInt(id_indicator.getText()), pf_section_no);
            db.setShedComponents(Integer.parseInt(id_indicator.getText()), pf_section_no);
            db.getPostFootingsSD(Integer.parseInt(id_indicator.getText()), selectedSection, postFootingsData);
        });

        PF_REMOVE_SECTION.setOnAction(event -> {
            pf_section_no--;
            PF_SECTIONS.getItems().remove(pf_section_no);
//            db.deleteSection(Integer.parseInt(id_indicator.getText()), PF_SECTIONS.getSelectionModel().getSelectedItem());
        });

//        PF_LENGTH.setOnKeyPressed(event -> {
//            if (event.getCode().equals(KeyCode.ENTER)) {
//                //adds a row to section dimensions
//                pf_dimension_no++;
//                postFootingsData.clear();
//                //formula
//                PF_TOTAL.setText("" + Double.valueOf(PF_DEPTH.getText()) * Double.valueOf(PF_LENGTH.getText()) *
//                        Double.valueOf(PF_WIDTH.getText()));
//
//                db.setSectionDimensions(Integer.parseInt(id_indicator.getText()), selectedSection, pf_dimension_no,
//                        PF_DEPTH.getText(), PF_WIDTH.getText(), PF_LENGTH.getText(), PF_TOTAL.getText(), "");
//                db.getPostFootingsSD(Integer.parseInt(id_indicator.getText()), selectedSection, postFootingsData);
//            }
//        });

        PF_SET.setItems(PF_SET_DATA);
        PF_SET.getSelectionModel().select(0);
        PF_SET_OVERRIDE.setItems(PF_SET_OVERRIDE_DATA);
        PF_SET_OVERRIDE.getSelectionModel().select(0);

        PF_SET.setOnAction(event -> {
            foundationsData.clear();
            db.setSelectedSets(Integer.parseInt(id_indicator.getText()), selectedSection, PF_SET.getSelectionModel().
                    getSelectedItem(), PF_SET_OVERRIDE.getSelectionModel().getSelectedItem());
            if (!PF_SET.getSelectionModel().isEmpty()) {
                db.getFoundationsSetComponents(Integer.parseInt(id_indicator.getText()), PF_SET.getSelectionModel().
                        getSelectedItem(), foundationsData);
            }
            selectedSection = PF_SECTIONS.getSelectionModel().getSelectedItem();
            db.getFoundationsSetOverrideComponents(Integer.parseInt(id_indicator.getText()), selectedSection,
                    PF_SET.getSelectionModel().getSelectedItem(), PF_SET_OVERRIDE.getSelectionModel()
                            .getSelectedItem(), foundationsData);
        });

        PF_SET_OVERRIDE.setOnAction(event -> {
            selectedSection = PF_SECTIONS.getSelectionModel().getSelectedItem();
            db.setSelectedSets(Integer.parseInt(id_indicator.getText()), selectedSection, PF_SET.getSelectionModel().
                    getSelectedItem(), PF_SET_OVERRIDE.getSelectionModel().getSelectedItem());
            db.getFoundationsSetOverrideComponents(Integer.parseInt(id_indicator.getText()), selectedSection,
                    PF_SET.getSelectionModel().getSelectedItem(), PF_SET_OVERRIDE.getSelectionModel()
                            .getSelectedItem(), foundationsData);
        });

        PF_SECTIONS.setOnMouseReleased(event -> {
            try {
                //section list event
                clearForms();
                foundationsData.clear();
                postFootingsData.clear();
                selectedSection = PF_SECTIONS.getSelectionModel().getSelectedItem();
                db.displayFoundationComponents(Integer.parseInt(id_indicator.getText()), selectedSection, foundationsData);
                db.getSelectedSets(Integer.parseInt(id_indicator.getText()), selectedSection, PF_SET, PF_SET_OVERRIDE); // combo boxes updater
                db.getPostFootingsSD(Integer.parseInt(id_indicator.getText()), selectedSection, postFootingsData);// section dimensions updater
                db.getFoundationsSetOverrideComponents(Integer.parseInt(id_indicator.getText()), selectedSection,
                        PF_SET.getSelectionModel().getSelectedItem(), PF_SET_OVERRIDE.getSelectionModel()
                                .getSelectedItem(), foundationsData);
                System.out.print("foundation params: " + id_indicator.getText() + " " +selectedSection + " " +
                        PF_SET.getSelectionModel().getSelectedItem() + " " + PF_SET_OVERRIDE.getSelectionModel()
                        .getSelectedItem());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //concrete bores
        CB_ADD_SECTION.setOnAction(event -> {
            cb_section_no++;
            CB_SECTIONS.getItems().add(cb_section_no);
            db.setShedComponents(Integer.parseInt(id_indicator.getText()), cb_section_no);
        });

        CB_REMOVE_SECTION.setOnAction(event -> {
            cb_section_no--;
            CB_SECTIONS.getItems().remove(cb_section_no);
        });

        CB_SECTIONS.setOnMouseReleased(event -> {
            //section list event
            foundationsData.clear();
            db.displayFoundationComponents(Integer.parseInt(id_indicator.getText()), Integer.parseInt(
                    CB_SECTIONS.getSelectionModel().getSelectedItem().toString()), foundationsData);
        });

        //concrete floors
        CF_ADD_SECTION.setOnAction(event -> {
            cf_section_no++;
            CF_SECTIONS.getItems().add(cf_section_no);
            db.setShedComponents(Integer.parseInt(id_indicator.getText()), cf_section_no);
        });

        CF_REMOVE_SECTION.setOnAction(event -> {
            cf_section_no--;
            CF_SECTIONS.getItems().remove(cf_section_no);
        });

        CF_SECTIONS.setOnMouseReleased(event -> {
            //section list event
            foundationsData.clear();
            db.displayFoundationComponents(Integer.parseInt(id_indicator.getText()), Integer.parseInt(
                    CF_SECTIONS.getSelectionModel().getSelectedItem().toString()), foundationsData);
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
        gridPaneList.add(CONCRETE_BORES);
        gridPaneList.add(CONCRETE_FLOOR);
    }

    public void clearForms() {
        //post footings
        PF_SET.getSelectionModel().select(0);
        PF_SET_OVERRIDE.getSelectionModel().select(0);
        PF_QTY.setText("");
    }

}
