package Controllers;

import Data.framing.framingData;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import Data.framing.beamsData;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class framing_2Controller implements Initializable {

    @FXML
    public JFXTreeTableView<beamsData> beamTableView;

    public TreeTableColumn COL_BEAM_NO, COL_BEAM, COL_BEAM_LENGTH, COL_BEAM_TYPE, COL_BEAM_SIZE;
    public TreeTableColumn COL_CLM_NO, COL_CLM_COLUMN, COL_CLM_LENGTH, COL_CLM_TYPE, COL_CLM_SIZE, COL_CLM_CLADDING;

    ObservableList<beamsData> data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        data = FXCollections.observableArrayList(
//                new beamsData("1","0.0","0.0","0.0","0.0"),
//                new beamsData("2","0.0","0.0","0.0","0.0"),
//                new beamsData("3","0.0","0.0","0.0","0.0")
//        );

        COL_BEAM_NO.setCellValueFactory(
                new TreeItemPropertyValueFactory<beamsData,String>("beam_no")
        );
        COL_BEAM.setCellValueFactory(
                new TreeItemPropertyValueFactory<beamsData,String>("beam")
        );
        COL_BEAM_LENGTH.setCellValueFactory(
                new TreeItemPropertyValueFactory<beamsData,String>("length")
        );
        COL_BEAM_TYPE.setCellValueFactory(
                new TreeItemPropertyValueFactory<beamsData,String>("type")
        );
        COL_BEAM_SIZE.setCellValueFactory(
                new TreeItemPropertyValueFactory<beamsData,String>("size")
        );

//        TreeItem<beamsData> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
//        beamTableView.setRoot(root);
//        beamTableView.setShowRoot(false);
    }

}
