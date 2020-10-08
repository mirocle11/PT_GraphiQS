package Controllers;

import Model.data.layouts.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class layoutsController implements Initializable {

    public BorderPane layoutsPane;
    public ScrollPane lengthScrollPane;

    public JFXTextField SEARCH, ROW_NO, PAGE, MEASUREMENT, STRUCTURE, WALL_TYPE, WALL, MATERIAL, COLOR_TXT, VALUE,
            STUD_HEIGHT, VOLUME, LOBOUR;

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
    public JFXTreeTableView<foundationsStampData> FOUNDATIONS_TBL;
    public TreeTableColumn<foundationsStampData, String> FOUNDATIONS_NO;
    public TreeTableColumn<foundationsStampData, String> FOUNDATIONS_PART;
    public TreeTableColumn<foundationsStampData, String> FOUNDATIONS_QTY;
    public TreeTableColumn<foundationsStampData, String> FOUNDATIONS_DEPTH;
    public TreeTableColumn<foundationsStampData, String> FOUNDATIONS_WIDTH;
    public TreeTableColumn<foundationsStampData, String> FOUNDATIONS_LENGTH;
    public TreeTableColumn<foundationsStampData, String> FOUNDATIONS_DIAMETER;
    public TreeTableColumn<foundationsStampData, String> FOUNDATIONS_HEIGHT;
    public TreeTableColumn<foundationsStampData, String> FOUNDATIONS_VOLUME;


    //data lists
    public static ObservableList<layoutsData> data;
    public static ObservableList<doorData> doorData;
    public static ObservableList<windowData> windowData;
    public static ObservableList<claddingData> claddingData;
    public static ObservableList<foundationsStampData> foundationsStampData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        foundationsStampData = FXCollections.observableArrayList();

        FOUNDATIONS_NO.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("no")
        );
        FOUNDATIONS_PART.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("part")
        );
        FOUNDATIONS_QTY.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("quantity")
        );
        FOUNDATIONS_DEPTH.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("depth")
        );
        FOUNDATIONS_WIDTH.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("width")
        );
        FOUNDATIONS_LENGTH.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("length")
        );
        FOUNDATIONS_DIAMETER.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("diameter")
        );
        FOUNDATIONS_HEIGHT.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("height")
        );
        FOUNDATIONS_VOLUME.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("volume")
        );

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

        TreeItem<foundationsStampData> foundations_root = new RecursiveTreeItem<>(foundationsStampData, RecursiveTreeObject::getChildren);
        FOUNDATIONS_TBL.setRoot(foundations_root);
        FOUNDATIONS_TBL.setShowRoot(false);

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
            FOUNDATIONS_TBL.setVisible(false);
        });

        LENGTH.setOnAction(event -> {
            lengthScrollPane.setVisible(true);
            STAMP_TBL.setVisible(false);
            WINDOW_TBL.setVisible(false);
            CLADDING_TBL.setVisible(false);
            FOUNDATIONS_TBL.setVisible(false);
        });

        WINDOWS.setOnAction(event -> {
            lengthScrollPane.setVisible(false);
            STAMP_TBL.setVisible(false);
            CLADDING_TBL.setVisible(false);
            WINDOW_TBL.setVisible(true);
            FOUNDATIONS_TBL.setVisible(false);
        });

        CLADDING.setOnAction(event -> {
            lengthScrollPane.setVisible(false);
            STAMP_TBL.setVisible(false);
            WINDOW_TBL.setVisible(false);
            CLADDING_TBL.setVisible(true);
            FOUNDATIONS_TBL.setVisible(false);
        });

        FOUNDATIONS.setOnAction(event -> {
            lengthScrollPane.setVisible(false);
            STAMP_TBL.setVisible(false);
            WINDOW_TBL.setVisible(false);
            CLADDING_TBL.setVisible(false);
            FOUNDATIONS_TBL.setVisible(true);
        });
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