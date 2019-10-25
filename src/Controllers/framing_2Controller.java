package Controllers;

import Main.Main;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import Data.framing.beamsData;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class framing_2Controller implements Initializable {

    @FXML
    private static Stage beamAddRowStage;

    private double xOffset = 0;
    private double yOffset = 0;

    public JFXTreeTableView<beamsData> beamTableView;

    public TreeTableColumn<beamsData, String> COL_BEAM_NO;
    public TreeTableColumn<beamsData, String> COL_BEAM;
    public TreeTableColumn<beamsData, String> COL_BEAM_LENGTH;
    public TreeTableColumn<beamsData, String> COL_BEAM_TYPE;
    public TreeTableColumn<beamsData, String> COL_BEAM_SIZE;
    public TreeTableColumn COL_CLM_NO, COL_CLM_COLUMN, COL_CLM_LENGTH, COL_CLM_TYPE, COL_CLM_SIZE, COL_CLM_CLADDING;

    static ObservableList<beamsData> data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = FXCollections.observableArrayList();

        COL_BEAM_NO.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("beam_no")
        );
        COL_BEAM.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("beam")
        );
        COL_BEAM_LENGTH.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("length")
        );
        COL_BEAM_TYPE.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("type")
        );
        COL_BEAM_SIZE.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("size")
        );

        TreeItem<beamsData> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
        beamTableView.setRoot(root);
        beamTableView.setShowRoot(false);
    }

    public void addRow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("../Views/Framing/addBeamRow.fxml"));
            AnchorPane pane = loader.load();

            //draggable pop up
            pane.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            pane.setOnMouseDragged(event -> {
                beamAddRowStage.setX(event.getScreenX() - xOffset);
                beamAddRowStage.setY(event.getScreenY() - yOffset);
            });

            Scene scene = new Scene(pane);
            scene.getStylesheets().addAll(Main.class.getResource("../Views/CSS/style.css").toExternalForm());
            beamAddRowStage = new Stage();
            beamAddRowStage.setScene(scene);
            beamAddRowStage.initStyle(StageStyle.UNDECORATED);
            beamAddRowStage.initModality(Modality.NONE);
            beamAddRowStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void closeStage() {
        beamAddRowStage.close();
    }

}
