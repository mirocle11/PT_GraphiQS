package Controllers;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.BorderPane;
import Data.layoutsData;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class layoutsController implements Initializable {

    public BorderPane layoutsPane;
    public ScrollPane scrollPane;

    public JFXTextField SEARCH, ROW_NO, PAGE, MEASUREMENT, STRUCTURE, WALL_TYPE, WALL, MATERIAL, COLOR_TXT, VALUE,
            STUD_HEIGHT, VOLUME, LOBOUR;

    public JFXButton ALL, LENGTH, AREA, STAMPS;

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
    public TreeTableColumn<layoutsData, String> COL_LOBOUR;

    public static ObservableList<layoutsData> data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVmax(0.0);

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
                new TreeItemPropertyValueFactory<>("volume")
        );
        COL_LOBOUR.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("lobour")
        );

        TreeItem<layoutsData> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
        layoutsTableView.setRoot(root);
        layoutsTableView.setShowRoot(false);

        SEARCH.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                layoutsTableView.setPredicate(new Predicate<TreeItem<layoutsData>>() {
                    @Override
                    public boolean test(TreeItem<layoutsData> modelTreeItem) {
                        return modelTreeItem.getValue().getMeasurement().contains(newValue) |modelTreeItem.getValue()
                                .getMaterial().contains(newValue) ;
                    }
                });
            }
        });

        layoutsTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            showDetails(newValue);
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