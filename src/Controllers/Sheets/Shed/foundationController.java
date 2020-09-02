package Controllers.Sheets.Shed;

import Model.data.shed.foundationData;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class foundationController implements Initializable {

    public JFXTreeTableView<foundationData> TREE_TABLE_VIEW;
    public TreeTableColumn<foundationData, String> NO;
    public TreeTableColumn<foundationData, String> SKU_NUMBER;
    public TreeTableColumn<foundationData, String> DESCRIPTION;
    public TreeTableColumn<foundationData, String> UNIT;

    public static ObservableList<foundationData> data;

    @Override
    public void initialize(URL location, ResourceBundle resource) {


        data = FXCollections.observableArrayList();

        NO.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("no")
        );
        SKU_NUMBER.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("sku_number")
        );
        DESCRIPTION.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("description")
        );
        UNIT.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("unit")
        );

        TreeItem<foundationData> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
        TREE_TABLE_VIEW.setRoot(root);
        TREE_TABLE_VIEW.setShowRoot(false);
    }

}
