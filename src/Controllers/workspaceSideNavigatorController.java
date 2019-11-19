package Controllers;

        import Data.framing.framingData;
        import Data.historyData;
        import com.jfoenix.controls.JFXSlider;
        import com.jfoenix.controls.JFXTreeTableView;
        import com.jfoenix.controls.RecursiveTreeItem;
        import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
        import javafx.beans.value.ObservableValue;
        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.fxml.FXML;
        import javafx.fxml.Initializable;
        import javafx.scene.control.TreeItem;
        import javafx.scene.control.TreeTableColumn;
        import javafx.util.Callback;

        import java.net.URL;
        import java.util.ResourceBundle;

public class workspaceSideNavigatorController implements Initializable {

    public static JFXSlider slider;

    @FXML
    public JFXTreeTableView<historyData> historyTable;
    public TreeTableColumn<historyData, String> COL_Type;
    public TreeTableColumn<historyData, String> COL_Measurements;
    static ObservableList<historyData> historyList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        COL_Type.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<historyData, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<historyData, String> param) {
                return param.getValue().getValue().type;
            }
        });
        COL_Measurements.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<historyData, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<historyData, String> param) {
                return param.getValue().getValue().measurement;
            }
        });
        historyList = FXCollections.observableArrayList();
        TreeItem<historyData> root = new RecursiveTreeItem<historyData>(historyList, RecursiveTreeObject::getChildren);
        historyTable.setRoot(root);
        historyTable.setShowRoot(false);
    }
}
