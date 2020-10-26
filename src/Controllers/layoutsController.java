package Controllers;

import DataBase.DataBase;
import Model.data.layouts.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class layoutsController implements Initializable {

    public BorderPane layoutsPane;
    public ScrollPane lengthScrollPane;

    public JFXTextField SEARCH, ROW_NO, PAGE, MEASUREMENT, STRUCTURE, WALL_TYPE, WALL, MATERIAL, COLOR_TXT, VALUE,
            STUD_HEIGHT, VOLUME, LOBOUR, FOUNDATIONS_PF_TOTAL, FOUNDATIONS_CB_TOTAL;

    public JFXButton LENGTH, AREA, STAMPS, WINDOWS, CLADDING, FOUNDATIONS;

    //length table
    public JFXTreeTableView<layoutsData> layoutsTableView;
    public TreeTableColumn<layoutsData, String> COL_NO;
    public TreeTableColumn<layoutsData, String> COL_PAGE;
    public TreeTableColumn<layoutsData, String> COL_MEASUREMENT;
    public TreeTableColumn<layoutsData, String> COL_STRUCTURE;
    public TreeTableColumn<layoutsData, String> COL_WALL_TYPE;
    public TreeTableColumn<layoutsData, String> COL_WALL;
    public TreeTableColumn<layoutsData, String> COL_MATERIAL;
    public TreeTableColumn<layoutsData, Label> COL_COLOR;
    public TreeTableColumn<layoutsData, String> COL_VALUE;
    public TreeTableColumn<layoutsData, String> COL_STUD_HEIGHT;
    public TreeTableColumn<layoutsData, String> COL_VOLUME;
    public TreeTableColumn<layoutsData, String> COL_LABOUR;

    //stamp table
    public JFXTreeTableView<doorData> STAMP_TBL;
    public TreeTableColumn<doorData, String> STAMP_NO;
    public TreeTableColumn<doorData, String> STAMP;
    public TreeTableColumn<doorData, String> TYPE;
    public TreeTableColumn<doorData, String> WIDTH;
    public TreeTableColumn<doorData, String> HEIGHT;
    public TreeTableColumn<doorData, String> IMAGE;

    //window table
    public JFXTreeTableView<windowData> WINDOW_TBL;
    public TreeTableColumn<windowData, String> WINDOW_COL_NO;
    public TreeTableColumn<windowData, String> WINDOW_NO;
    public TreeTableColumn<windowData, String> WINDOW_CLADDING;
    public TreeTableColumn<windowData, String> WINDOW_TYPE;
    public TreeTableColumn<windowData, String> WINDOW_WIDTH;
    public TreeTableColumn<windowData, String> WINDOW_HEIGHT;

    //cladding table
    public JFXTreeTableView<claddingData> CLADDING_TBL;
    public TreeTableColumn<claddingData, String> CLADDING_NO;
    public TreeTableColumn<claddingData, String> CLADDING_NAME;
    public TreeTableColumn<claddingData, String> CLADDING_LENGTH;
    public TreeTableColumn<claddingData, String> CLADDING_HEIGHT;

    //foundations table (setup sheets)
    public JFXTreeTableView<foundationsPostFootingsData> FOUNDATIONS_POST_FOOTINGS_TBL; // post footings
    public TreeTableColumn<foundationsPostFootingsData, String> FOUNDATIONS_PF_NO;
    public TreeTableColumn<foundationsPostFootingsData, String> FOUNDATIONS_PF_PART;
    public TreeTableColumn<foundationsPostFootingsData, String> FOUNDATIONS_PF_IMAGE;
    public TreeTableColumn<foundationsPostFootingsData, String> FOUNDATIONS_PF_QTY;
    public TreeTableColumn<foundationsPostFootingsData, String> FOUNDATIONS_PF_DEPTH;
    public TreeTableColumn<foundationsPostFootingsData, String> FOUNDATIONS_PF_WIDTH;
    public TreeTableColumn<foundationsPostFootingsData, String> FOUNDATIONS_PF_LENGTH;
    public TreeTableColumn<foundationsPostFootingsData, String> FOUNDATIONS_PF_VOLUME;

    public JFXTreeTableView<foundationsConcreteBoresData> FOUNDATIONS_CONCRETE_BORES_TBL; // concrete bores
    public TreeTableColumn<foundationsConcreteBoresData, String> FOUNDATIONS_CB_NO;
    public TreeTableColumn<foundationsConcreteBoresData, String> FOUNDATIONS_CB_PART;
    public TreeTableColumn<foundationsConcreteBoresData, String> FOUNDATIONS_CB_IMAGE;
    public TreeTableColumn<foundationsConcreteBoresData, String> FOUNDATIONS_CB_QTY;
    public TreeTableColumn<foundationsConcreteBoresData, String> FOUNDATIONS_CB_DIAMETER;
    public TreeTableColumn<foundationsConcreteBoresData, String> FOUNDATIONS_CB_HEIGHT;
    public TreeTableColumn<foundationsConcreteBoresData, String> FOUNDATIONS_CB_VOLUME;

    //data lists
    public static ObservableList<layoutsData> data;
    public static ObservableList<doorData> doorData;
    public static ObservableList<windowData> windowData;
    public static ObservableList<claddingData> claddingData;

    public static ObservableList<foundationsPostFootingsData> foundationsPostFootingsData;
    public static ObservableList<foundationsConcreteBoresData> foundationsConcreteBoresData;
    public static ObservableList<String> concreteData;
    public static ObservableList<String> totalData;

    public Pane CONCRETE_FLOOR_TBL;
    public JFXTextField CONCRETE_FLOOR_LENGTH;
    public JFXTextField CONCRETE_FLOOR_WIDTH;
    public JFXTextField CONCRETE_FLOOR_THICKNESS;
    public JFXTextField CONCRETE_FLOOR_VOLUME;
    public JFXTextField CONCRETE_FLOOR_AREA;
    public JFXButton POST_FOOTING_BTN;
    public JFXButton CONCRETE_BORES_BTN;
    public JFXButton CONCRETE_FLOORS_BTN;
    public AnchorPane FOUNDATIONS_TAB;
    public JFXTextField POST_FOOTING_TOTAL;
    public JFXTextField POLE_FOOTING_TOTAL;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FOUNDATIONS_PF_TOTAL.setEditable(false);
        FOUNDATIONS_CB_TOTAL.setEditable(false);

        lengthScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        lengthScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        lengthScrollPane.setVmax(0.0);

        data = FXCollections.observableArrayList();

        COL_NO.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("no")
        );
        COL_PAGE.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("page")
        );
        COL_MEASUREMENT.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("measurement")
        );
        COL_STRUCTURE.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("structure")
        );
        COL_WALL_TYPE.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("wall_type")
        );
        COL_WALL.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("wall")
        );
        COL_MATERIAL.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("material")
        );
        COL_COLOR.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("color")
        );
        COL_VALUE.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("value")
        );
        COL_STUD_HEIGHT.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("stud_height")
        );
        COL_VOLUME.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("unit")
        );
        COL_LABOUR.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("labour")
        );

        //stamp doors
        doorData = FXCollections.observableArrayList();

        STAMP_NO.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("no")
        );
        STAMP.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("stamp")
        );
        TYPE.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("type")
        );
        WIDTH.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("width")
        );
        HEIGHT.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("height")
        );
        IMAGE.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("wall")
        );

        //stamp windows
        windowData = FXCollections.observableArrayList();

        WINDOW_COL_NO.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("no")
        );
        WINDOW_NO.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("window_no")
        );
        WINDOW_CLADDING.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("cladding")
        );
        WINDOW_TYPE.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("type")
        );
        WINDOW_WIDTH.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("width")
        );
        WINDOW_HEIGHT.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("height")
        );

        //cladding
        claddingData = FXCollections.observableArrayList();

        CLADDING_NO.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("no")
        );
        CLADDING_NAME.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("cladding_name")
        );
        CLADDING_LENGTH.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("length")
        );
        CLADDING_HEIGHT.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("height")
        );

        //foundations
        foundationsPostFootingsData = FXCollections.observableArrayList();

        FOUNDATIONS_PF_NO.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("no")
        );
        FOUNDATIONS_PF_IMAGE.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("image")
        );
        FOUNDATIONS_PF_QTY.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("quantity")
        );
        FOUNDATIONS_PF_DEPTH.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("depth")
        );
        FOUNDATIONS_PF_WIDTH.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("width")
        );
        FOUNDATIONS_PF_LENGTH.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("length")
        );
        FOUNDATIONS_PF_VOLUME.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("volume")
        );

        foundationsConcreteBoresData = FXCollections.observableArrayList();

        FOUNDATIONS_CB_NO.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("no")
        );
        FOUNDATIONS_CB_IMAGE.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("image")
        );
        FOUNDATIONS_CB_QTY.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("quantity")
        );
        FOUNDATIONS_CB_DIAMETER.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("diameter")
        );
        FOUNDATIONS_CB_HEIGHT.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("height")
        );
        FOUNDATIONS_CB_VOLUME.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("volume")
        );



        DataBase db = DataBase.getInstance();
        POST_FOOTING_TOTAL.setText(db.getTotalVolume("shed_foundations_post_footings"));

        TreeItem<layoutsData> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
        layoutsTableView.setRoot(root);
        layoutsTableView.setShowRoot(false);

        TreeItem<doorData> stamp_root = new RecursiveTreeItem<>(doorData, RecursiveTreeObject::getChildren);
        STAMP_TBL.setRoot(stamp_root);
        STAMP_TBL.setShowRoot(false);

        TreeItem<windowData> window_root = new RecursiveTreeItem<>(windowData, RecursiveTreeObject::getChildren);
        WINDOW_TBL.setRoot(window_root);
        WINDOW_TBL.setShowRoot(false);

        TreeItem<claddingData> cladding_root = new RecursiveTreeItem<>(claddingData, RecursiveTreeObject::getChildren);
        CLADDING_TBL.setRoot(cladding_root);
        CLADDING_TBL.setShowRoot(false);

        TreeItem<foundationsPostFootingsData> foundationsPostFootingsRoot = new RecursiveTreeItem<>(foundationsPostFootingsData,
                RecursiveTreeObject::getChildren);
        FOUNDATIONS_POST_FOOTINGS_TBL.setRoot(foundationsPostFootingsRoot);
        FOUNDATIONS_POST_FOOTINGS_TBL.setShowRoot(false);

        TreeItem<foundationsConcreteBoresData> foundationsConcreteBoresRoot = new RecursiveTreeItem<>(foundationsConcreteBoresData,
                RecursiveTreeObject::getChildren);
        FOUNDATIONS_CONCRETE_BORES_TBL.setRoot(foundationsConcreteBoresRoot);
        FOUNDATIONS_CONCRETE_BORES_TBL.setShowRoot(false);

        SEARCH.textProperty().addListener((observable, oldValue, newValue) -> layoutsTableView.setPredicate(
                modelTreeItem -> modelTreeItem.getValue().getMeasurement().contains(newValue) |
                        modelTreeItem.getValue().getMaterial().contains(newValue)));

        layoutsTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            showDetails(newValue);
        });

        STAMPS.setOnAction(event -> {
            lengthScrollPane.setVisible(false);
            STAMP_TBL.setVisible(true);
            WINDOW_TBL.setVisible(false);
            CLADDING_TBL.setVisible(false);
            FOUNDATIONS_TAB.setVisible(false);
        });

        LENGTH.setOnAction(event -> {
            lengthScrollPane.setVisible(true);
            STAMP_TBL.setVisible(false);
            WINDOW_TBL.setVisible(false);
            CLADDING_TBL.setVisible(false);
            FOUNDATIONS_TAB.setVisible(false);
        });

        WINDOWS.setOnAction(event -> {
            lengthScrollPane.setVisible(false);
            STAMP_TBL.setVisible(false);
            CLADDING_TBL.setVisible(false);
            WINDOW_TBL.setVisible(true);
            FOUNDATIONS_TAB.setVisible(false);
        });

        CLADDING.setOnAction(event -> {
            lengthScrollPane.setVisible(false);
            STAMP_TBL.setVisible(false);
            WINDOW_TBL.setVisible(false);
            CLADDING_TBL.setVisible(true);
            FOUNDATIONS_TAB.setVisible(false);
        });

        FOUNDATIONS.setOnAction(event -> {
            lengthScrollPane.setVisible(false);
            STAMP_TBL.setVisible(false);
            WINDOW_TBL.setVisible(false);
            CLADDING_TBL.setVisible(false);
            FOUNDATIONS_TAB.setVisible(true);


            db.getFoundationsTotal(FOUNDATIONS_PF_TOTAL, FOUNDATIONS_CB_TOTAL);
        });

        POST_FOOTING_BTN.setOnAction(event -> {
            FOUNDATIONS_POST_FOOTINGS_TBL.setVisible(true);
            FOUNDATIONS_CONCRETE_BORES_TBL.setVisible(false);
            CONCRETE_FLOOR_TBL.setVisible(false);
            POST_FOOTING_TOTAL.setVisible(true);
            POLE_FOOTING_TOTAL.setVisible(false);

//            POST_FOOTING_TOTAL.setText(db.getTotalVolume("shed_foundations_post_footings"));
        });

        CONCRETE_BORES_BTN.setOnAction(event -> {
            FOUNDATIONS_POST_FOOTINGS_TBL.setVisible(false);
            FOUNDATIONS_CONCRETE_BORES_TBL.setVisible(true);
            CONCRETE_FLOOR_TBL.setVisible(false);
            POLE_FOOTING_TOTAL.setVisible(true);
            POST_FOOTING_TOTAL.setVisible(false);

//            POLE_FOOTING_TOTAL.setText(db.getTotalVolume("shed_foundations_concrete_bores"));
        });

        CONCRETE_FLOORS_BTN.setOnAction(event -> {
            FOUNDATIONS_POST_FOOTINGS_TBL.setVisible(false);
            FOUNDATIONS_CONCRETE_BORES_TBL.setVisible(false);
            CONCRETE_FLOOR_TBL.setVisible(true);
        });

        concreteData = FXCollections.observableArrayList();
        CONCRETE_FLOOR_LENGTH.textProperty().bind(Bindings.stringValueAt(concreteData, 0));
        CONCRETE_FLOOR_WIDTH.textProperty().bind(Bindings.stringValueAt(concreteData, 1));
        CONCRETE_FLOOR_THICKNESS.textProperty().bind(Bindings.stringValueAt(concreteData, 2));
        CONCRETE_FLOOR_VOLUME.textProperty().bind(Bindings.stringValueAt(concreteData, 3));
        CONCRETE_FLOOR_AREA.textProperty().bind(Bindings.stringValueAt(concreteData, 4));

        totalData= FXCollections.observableArrayList();
        POST_FOOTING_TOTAL.textProperty().bind(Bindings.stringValueAt(totalData, 0));
        POLE_FOOTING_TOTAL.textProperty().bind(Bindings.stringValueAt(totalData, 1));
        FOUNDATIONS_PF_TOTAL.textProperty().bind(Bindings.stringValueAt(totalData, 2));

    }

    private void showDetails(TreeItem<layoutsData> treeItem) {
        try {
            ROW_NO.setText(treeItem.getValue().getNo());
            PAGE.setText(treeItem.getValue().getPage());

            MEASUREMENT.setText(treeItem.getValue().getMeasurement());
            STRUCTURE.setText(treeItem.getValue().getStructure());
            WALL_TYPE.setText(treeItem.getValue().getWall_type());
            WALL.setText(treeItem.getValue().getWall());
            MATERIAL.setText(treeItem.getValue().getMaterial());

            COLOR_TXT.setText(treeItem.getValue().getColor().getBackground().getFills().get(0).getFill().toString());
            VALUE.setText(treeItem.getValue().getValue());

            STUD_HEIGHT.setText(treeItem.getValue().getColor().getBackground().getFills().get(0).getFill().toString());
            VOLUME.setText(treeItem.getValue().getColor().getBackground().getFills().get(0).getFill().toString());
        } catch (Exception e) {
            e.getSuppressed();
        }
    }

}