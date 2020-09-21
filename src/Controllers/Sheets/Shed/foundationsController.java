package Controllers.Sheets.Shed;

import DataBase.DataBase;
import Main.Main;
import Model.data.shed.foundations.postFootingsSec;
import Model.data.shed.foundationsData;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.KeyCode;
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

    public int pf_selected_section = 0;

    //collections
    public ObservableList<String> parts = FXCollections.observableArrayList();
    public ArrayList<GridPane> gridPaneList = new ArrayList<>();
    public VBox PARTS_VBOX;
    public int dimension_no = 0;
    public ToggleGroup toggleGroup = new ToggleGroup();

    //gridpanes (parts)
    public GridPane POST_FOOTINGS, CONCRETE_BORES, CONCRETE_FLOOR;

    //components
    public ListView<Integer> PF_SECTIONS, CB_SECTIONS, CF_SECTIONS;
    public JFXButton PF_ADD_SECTION, PF_REMOVE_SECTION, CB_ADD_SECTION, CB_REMOVE_SECTION, CF_ADD_SECTION, CF_REMOVE_SECTION;
    public JFXComboBox<String> PF_SET, PF_SET_OVERRIDE;
    public JFXTextField PF_QTY, PF_DEPTH, PF_WIDTH, PF_LENGTH, PF_TOTAL;

    //component editor
    private double xOffset = 0;
    private double yOffset = 0;
    private static Stage editComponentStage;

    //sample data
    private ObservableList<String> PF_SET_DATA = FXCollections.observableArrayList("(None)", "17.5 Footing [1]",
            "17.5 Post", "17.5 Slab", "17.5 Con Footings D12 CHANGE", "17.5 Con Post D12 CHANGE", "17.5 Con Slab D12 CHANGE");

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

    //data lists
    public static ObservableList<foundationsData> foundationsData; //components
    public static ObservableList<postFootingsSec> postFootingsData; //section dimensions

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        loadPanes();

        DataBase db = DataBase.getInstance();
        parts.clear();
        db.displayShedParts(1, parts);

        parts.forEach(s -> {
            JFXToggleButton button = new JFXToggleButton();
            button.setText(s);
            button.setId(s);
            button.setPrefWidth(183);
            button.setPrefHeight(30);
            button.setFont(new Font("Segoe UI",15.0));
            button.setTextFill(Color.web("#bcbcbc"));
            PARTS_VBOX.getChildren().add(button);
            toggleGroup.getToggles().add(button);

            button.setOnAction(event -> {
                foundationsData.clear();
                db.getPartID(1, button.getId(), id_indicator);
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

        TreeItem<foundationsData> component_root = new RecursiveTreeItem<>(foundationsData, RecursiveTreeObject::getChildren);
        TREE_TABLE_VIEW.setRoot(component_root);
        TREE_TABLE_VIEW.setShowRoot(false);

        TreeItem<postFootingsSec> post_footings_root = new RecursiveTreeItem<>(postFootingsData, RecursiveTreeObject::getChildren);
        PF_TABLE.setRoot(post_footings_root);
        PF_TABLE.setShowRoot(false);

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

        editRow.setOnAction(event -> {
            loadComponentEditor();
        });

        clearRow.setOnAction(event -> {

        });

        productSelect.setOnAction(event -> {

        });

        //post footings
        PF_ADD_SECTION.setOnAction(event -> {
            pf_section_no++;
            PF_SECTIONS.getItems().add(pf_section_no);
            db.setSections(Integer.parseInt(id_indicator.getText()), pf_section_no);
            db.setShedComponents(Integer.parseInt(id_indicator.getText()), pf_section_no);
        });

        PF_REMOVE_SECTION.setOnAction(event -> {
            pf_section_no--;
            PF_SECTIONS.getItems().remove(pf_section_no);
        });

        PF_LENGTH.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                //adds a row to section dimensions
                dimension_no++;
                postFootingsData.add(new postFootingsSec(String.valueOf(dimension_no), PF_DEPTH.getText(),
                        PF_WIDTH.getText(), PF_LENGTH.getText(),PF_QTY.getText())); //temporary data addition
            }
        });

        PF_SET.setItems(PF_SET_DATA);
        PF_SET_OVERRIDE.setItems(PF_SET_OVERRIDE_DATA);

        PF_SET.setOnAction(event -> {
            db.setSelectedSets(Integer.parseInt(id_indicator.getText()), pf_selected_section, PF_SET.getSelectionModel().
                    getSelectedItem(), PF_SET_OVERRIDE.getSelectionModel().getSelectedItem());
        });

        PF_SET_OVERRIDE.setOnAction(event -> {
            db.setSelectedSets(Integer.parseInt(id_indicator.getText()), pf_selected_section, PF_SET.getSelectionModel().
                    getSelectedItem(), PF_SET_OVERRIDE.getSelectionModel().getSelectedItem());
        });

        PF_SECTIONS.setOnMouseReleased(event -> {
            try {
                //section list event
                foundationsData.clear();
                pf_selected_section = Integer.parseInt(PF_SECTIONS.getSelectionModel().getSelectedItem().toString());
                db.displayFoundationComponents(Integer.parseInt(id_indicator.getText()), pf_selected_section, foundationsData);
                db.getSelectedSets(Integer.parseInt(id_indicator.getText()), pf_selected_section, PF_SET, PF_SET_OVERRIDE);
            } catch (Exception e) {
                e.getSuppressed();
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
        PF_SET.getSelectionModel().clearSelection();
        PF_SET_OVERRIDE.getSelectionModel().clearSelection();
        PF_QTY.setText("");
        PF_DEPTH.setText("");
        PF_WIDTH.setText("");
        PF_LENGTH.setText("");
        PF_TOTAL.setText("");
    }

}
