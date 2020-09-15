package Controllers.Sheets.Shed;

import DataBase.DataBase;
import Model.data.shed.foundations.postFootingsSec;
import Model.data.shed.foundationsData;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

    //dataholders
    public Label id_indicator = new Label();

    //collections
    public ObservableList<String> parts = FXCollections.observableArrayList();
    public ArrayList<GridPane> gridPaneList = new ArrayList<>();
    public VBox PARTS_VBOX;
    public int section_no = 0;
    public int dimension_no = 0;

    //gridpanes (parts)
    public GridPane POST_FOOTINGS, CONCRETE_BORES, CONCRETE_FLOOR;

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

    public TreeTableView<postFootingsSec> PF_TABLE; //section dimensions
    public TreeTableColumn<postFootingsSec, String> PF_COL_NO;
    public TreeTableColumn<postFootingsSec, String> PF_COL_DEPTH;
    public TreeTableColumn<postFootingsSec, String> PF_COL_WIDTH;
    public TreeTableColumn<postFootingsSec, String> PF_COL_LENGTH;
    public TreeTableColumn<postFootingsSec, String> PF_COL_QTY;

    //data lists
    public static ObservableList<foundationsData> foundationsData;
    public static ObservableList<postFootingsSec> postFootingsData; //section dimensions

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        loadPanes();

        DataBase db = DataBase.getInstance();
        parts.clear();
        db.displayShedParts(1, parts);

        parts.forEach(s -> {
            JFXButton button = new JFXButton(s);
            button.setPrefWidth(183);
            button.setPrefHeight(30);
            button.setFont(new Font("Segoe UI",15.0));
            button.setTextFill(Color.web("#bcbcbc"));
            PARTS_VBOX.getChildren().add(button);

            button.setOnAction(event -> {
                foundationsData.clear();
                db.getPartID(1, button.getText(), id_indicator);
                gridPaneList.forEach(gridPane -> {
                    gridPane.setVisible(false);
                    if (button.getText().equals(gridPane.getId())) {
                        gridPane.setVisible(true);
                    }
                });
            });
        });

        //component tbl
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

        //parts
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

        PF_ADD_SECTION.setOnAction(event -> {
            section_no++;
            PF_SECTIONS.getItems().add(section_no);
            db.setShedComponents(Integer.parseInt(id_indicator.getText()), section_no);
        });

        PF_REMOVE_SECTION.setOnAction(event -> {
            section_no--;
            PF_SECTIONS.getItems().remove(section_no);
        });

        PF_LENGTH.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                //adds a row to section dimensions
                //dimension_no++;
            }
        });

        PF_SET.setItems(PF_SET_DATA);
        PF_SET_OVERRIDE.setItems(PF_SET_OVERRIDE_DATA);

        PF_SECTIONS.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                //section list event
                foundationsData.clear();
                db.displayFoundationComponents(Integer.parseInt(id_indicator.getText()), Integer.parseInt(
                        PF_SECTIONS.getSelectionModel().getSelectedItem().toString()), foundationsData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void loadPanes() {
        gridPaneList.add(POST_FOOTINGS);
        gridPaneList.add(CONCRETE_BORES);
        gridPaneList.add(CONCRETE_FLOOR);
    }

}
