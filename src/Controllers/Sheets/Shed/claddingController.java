package Controllers.Sheets.Shed;

import Model.data.shed.claddingData;
import Model.data.shed.framingHardwareData;
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

public class claddingController implements Initializable {

    public JFXTreeTableView<claddingData> TREE_TABLE_VIEW;
    public TreeTableColumn<claddingData, String> NO;
    public TreeTableColumn<claddingData, String> SKU_NUMBER;
    public TreeTableColumn<claddingData, String> DESCRIPTION;
    public TreeTableColumn<claddingData, String> UNIT;

    public static ObservableList<claddingData> data;

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

        TreeItem<claddingData> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
        TREE_TABLE_VIEW.setRoot(root);
        TREE_TABLE_VIEW.setShowRoot(false);
    }

}
