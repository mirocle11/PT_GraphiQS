package Controllers;
import Data.framing.framingData;
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
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class framingController implements Initializable {

    private static Stage framingAddRowStage;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private AnchorPane rootpane, firstpane;
    private AnchorPane secondpane;

    @FXML
    private ImageView first, second;

    @FXML
    public JFXTreeTableView<framingData> tableView;
    public TreeTableColumn<framingData, String> COL_SECTION;
    public TreeTableColumn<framingData, String> COL_HEIGHT;
    public TreeTableColumn<framingData, String> COL_LENGTH;
    public TreeTableColumn<framingData, String> COL_TYPE;
    public TreeTableColumn<framingData, String> COL_STUD;
    public TreeTableColumn<framingData, String> COL_SPACING;

    static ObservableList<framingData> data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/framing_2.fxml"));
            secondpane = loader.load();
            rootpane.getChildren().addAll(secondpane);
            secondpane.setLayoutY(120);
            AnchorPane.setBottomAnchor(secondpane,0.0);
            AnchorPane.setTopAnchor(secondpane,120.0);
            secondpane.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

         data = FXCollections.observableArrayList(
                new framingData("Exterior", "0.0", "0.0", "0.0", "0.0", "0.0"),
                new framingData("Interior", "0.0", "0.0", "0.0", "0.0", "0.0"),
                new framingData("Wet Areas", "0.0", "0.0", "0.0", "0.0", "0.0")
        );

        COL_SECTION.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("section")
        );
        COL_HEIGHT.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("height")
        );
        COL_LENGTH.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("length")
        );
        COL_TYPE.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("type")
        );
        COL_STUD.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("stud")
        );
        COL_SPACING.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("spacing")
        );

        TreeItem<framingData> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
        tableView.setRoot(root);
        tableView.setShowRoot(false);
    }

    public void next() {
        firstpane.setVisible(false);
        secondpane.setVisible(true);

        first.setImage(new Image("Views/images/1_circle_25px.png"));
        second.setImage(new Image("Views/images/2_circle_c_25px.png"));
    }

    public void back() {
        firstpane.setVisible(true);
        secondpane.setVisible(false);

        first.setImage(new Image("Views/images/1_circle_c_25px.png"));
        second.setImage(new Image("Views/images/2_circle_25px.png"));
    }

    public void addRow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("../Views/framingAddRow.fxml"));
            AnchorPane pane = loader.load();

            //draggable pop up
            pane.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            pane.setOnMouseDragged(event -> {
                framingAddRowStage.setX(event.getScreenX() - xOffset);
                framingAddRowStage.setY(event.getScreenY() - yOffset);
            });

            Scene scene = new Scene(pane);
            scene.getStylesheets().addAll(Main.class.getResource("../Views/CSS/style.css").toExternalForm());
            framingAddRowStage = new Stage();
            framingAddRowStage.setScene(scene);
            framingAddRowStage.initStyle(StageStyle.UNDECORATED);
            framingAddRowStage.initModality(Modality.APPLICATION_MODAL);
            framingAddRowStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteRow() {
        int index = tableView.getSelectionModel().getSelectedIndex();
        if (index > 2) {
            data.remove(index);
        }
    }

    static void closeStage() {
        framingAddRowStage.close();
    }

}