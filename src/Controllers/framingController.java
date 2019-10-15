package Controllers;
import Data.framing.framingData;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class framingController implements Initializable {

    @FXML
    private AnchorPane rootpane,firstpane;
    private AnchorPane secondpane;

    @FXML
    private ImageView first,second;

    @FXML
    public JFXTreeTableView<framingData> tableView;
    public TreeTableColumn COL_SECTION, COL_HEIGHT, COL_LENGTH, COL_TYPE, COL_STUD, COL_SPACING;

    ObservableList<framingData> data;

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
                new framingData("Exterior","0.0","0.0","0.0","0.0","0.0"),
                new framingData("Interior","0.0","0.0","0.0","0.0","0.0"),
                new framingData("Wet Areas","0.0","0.0","0.0","0.0","0.0")
        );

        COL_SECTION.setCellValueFactory(
                new TreeItemPropertyValueFactory<framingData,String>("section")
        );
        COL_HEIGHT.setCellValueFactory(
                new TreeItemPropertyValueFactory<framingData,String>("height")
        );
        COL_LENGTH.setCellValueFactory(
                new TreeItemPropertyValueFactory<framingData,String>("length")
        );
        COL_TYPE.setCellValueFactory(
                new TreeItemPropertyValueFactory<framingData,String>("type")
        );
        COL_STUD.setCellValueFactory(
                new TreeItemPropertyValueFactory<framingData,String>("stud")
        );
        COL_SPACING.setCellValueFactory(
                new TreeItemPropertyValueFactory<framingData,String>("spacing")
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

}