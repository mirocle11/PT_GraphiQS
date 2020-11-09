package Controllers.Sheets.Shed;

import DataBase.DataBase;
import Model.data.shed.externalFraming.polesSec;
import Model.data.shed.externalFraming.columnsSec;
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

public class externalFramingController implements Initializable {

    //indicators
    public Label id_indicator = new Label(); //part id every selection

    //collections
    public ObservableList<String> parts = FXCollections.observableArrayList();
    public VBox PARTS_VBOX;
    public ArrayList<JFXButton> buttonList = new ArrayList<>();
    public ArrayList<GridPane> gridPaneList = new ArrayList<>();

    //components
    public ListView<Integer> PL_SECTIONS, CL_SECTIONS;

    //gridpanes (parts)
    public GridPane POLES, COLUMNS, GIRTS, DOORS, WINDOWS;

    public JFXComboBox<String> PL_SET, PL_SET_OVERRIDE, CL_SET, CL_SET_OVERRIDE;

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

    public static ObservableList<String> setsListPL = FXCollections.observableArrayList();
    public static ObservableList<String> setsListCL = FXCollections.observableArrayList();

    // section dimensions
    public static ObservableList<polesSec> polesSec = FXCollections.observableArrayList();
    public static ObservableList<columnsSec> columnsSec = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        loadPanes();

        DataBase db = DataBase.getInstance();
        parts.clear();
        db.displayShedParts(2, parts);
        db.getSets(4, setsListPL);
        db.getSets(5, setsListCL);

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
                PL_SET.setDisable(false);
                PL_SET_OVERRIDE.setDisable(false);
            }
            db.getSelectedSets(Integer.parseInt(id_indicator.getText()), PL_SECTIONS.getSelectionModel().getSelectedItem(),
                    PL_SET, PL_SET_OVERRIDE);
            db.showExternalFramingSD("Poles", PL_SECTIONS.getSelectionModel().getSelectedItem(), polesSec, columnsSec);
        });

        PL_SET.setOnAction(event -> {
            String set_override = "";
            if (!PL_SET_OVERRIDE.getSelectionModel().isEmpty()) {
                set_override = PL_SET_OVERRIDE.getSelectionModel().getSelectedItem();
            }
            db.setSelectedSets(Integer.parseInt(id_indicator.getText()), PL_SECTIONS.getSelectionModel()
                    .getSelectedItem(), PL_SET.getSelectionModel().getSelectedItem(), set_override);
//            setComponentContents(2, PL_SET.getSelectionModel().getSelectedItem());
        });

    }

    public void loadPanes() {
        gridPaneList.add(POLES);
        gridPaneList.add(COLUMNS);
        gridPaneList.add(GIRTS);
        gridPaneList.add(DOORS);
        gridPaneList.add(WINDOWS);
    }

}
