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

        import java.awt.image.BufferedImage;
        import java.net.URL;
        import java.util.ResourceBundle;

public class workspaceSideNavigatorController implements Initializable {

    public static JFXSlider slider;

    @FXML
    public JFXTreeTableView<historyData> historyTable;
    public TreeTableColumn<historyData,String> COL_Color;
    public TreeTableColumn<historyData, String> COL_Type;
    public TreeTableColumn<historyData, String> COL_Measurements;
    static ObservableList<historyData> historyList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        COL_Color.setCellValueFactory(param -> param.getValue().getValue().color);
        COL_Type.setCellValueFactory(param -> param.getValue().getValue().type);
        COL_Measurements.setCellValueFactory(param -> param.getValue().getValue().measurement);
        historyList = FXCollections.observableArrayList();
        TreeItem<historyData> root = new RecursiveTreeItem<>(historyList, RecursiveTreeObject::getChildren);
        historyTable.setRoot(root);
        historyTable.setShowRoot(false);
    }
}
