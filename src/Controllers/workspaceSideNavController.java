package Controllers;

    import DataBase.DataBase;
    import Model.data.HistoryData;
    import com.jfoenix.controls.*;
    import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.fxml.Initializable;
    import javafx.scene.control.Label;
    import javafx.scene.control.TreeItem;
    import javafx.scene.control.TreeTableColumn;

    import java.net.URL;
    import java.util.ResourceBundle;

public class workspaceSideNavController implements Initializable {

    public Label USERNAME, POSITION;
    public JFXSlider slider;

    public JFXTreeTableView<HistoryData> historyTable;
    public TreeTableColumn<HistoryData, String> COL_Color;
    public TreeTableColumn<HistoryData, String> COL_Type;
    public TreeTableColumn<HistoryData, String> COL_Measurements;
    static ObservableList<HistoryData> historyList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        COL_Color.setCellValueFactory(param -> param.getValue().getValue().color);
        COL_Type.setCellValueFactory(param -> param.getValue().getValue().type);
        COL_Measurements.setCellValueFactory(param -> param.getValue().getValue().measurement);
        historyList = FXCollections.observableArrayList();
        TreeItem<HistoryData> root = new RecursiveTreeItem<>(historyList, RecursiveTreeObject::getChildren);
        historyTable.setRoot(root);
        historyTable.setShowRoot(false);

        DataBase db = DataBase.getInstance();
        db.displayOperator(USERNAME, POSITION);
    }

}
