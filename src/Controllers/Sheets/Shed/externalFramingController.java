package Controllers.Sheets.Shed;

import DataBase.DataBase;
import Model.data.shed.externalFramingMaterials;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class externalFramingController implements Initializable {

    //collections
    public ObservableList<String> parts = FXCollections.observableArrayList();
    public VBox PARTS_VBOX;
    public ArrayList<JFXButton> buttonList = new ArrayList<>();

    //tables
    public JFXTreeTableView<externalFramingMaterials> MATERIALS_TBL;
    public TreeTableColumn<externalFramingMaterials, String> COMPONENT_COL;
    public TreeTableColumn<externalFramingMaterials, String> SKU_NUMBER_COL;
    public TreeTableColumn<externalFramingMaterials, String> DESCRIPTION_COL;
    public TreeTableColumn<externalFramingMaterials, String> UNIT_COL;
    public TreeTableColumn<externalFramingMaterials, String> QUANTITY_COL;
    public TreeTableColumn<externalFramingMaterials, String> USAGE_COL;

    //data lists
    public static ObservableList<externalFramingMaterials> externalFramingMaterials;

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        DataBase db = DataBase.getInstance();
        parts.clear();
        db.displayShedParts(2, parts);

        parts.forEach(s -> {
            JFXButton button = new JFXButton();
            button.setText(s);
            button.setId(s);
            button.setPrefWidth(183);
            button.setPrefHeight(30);
            button.setFont(new Font("Segoe UI",15.0));
            button.setTextFill(Color.web("#bcbcbc"));
            PARTS_VBOX.getChildren().add(button);
            buttonList.add(button);

            button.setOnAction(event -> {
                buttonList.forEach(button1 -> { //selection highlighter
                    if (button1.getId().equals(button.getId())) {
                        button1.setStyle("-fx-background-color: #394F5A");
                    } else {
                        button1.setStyle("-fx-background-color: TRANSPARENT");
                    }
                });
            });
        });

        //foundations materials
        externalFramingMaterials = FXCollections.observableArrayList();

        COMPONENT_COL.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("component")
        );
        SKU_NUMBER_COL.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("sku_number")
        );
        DESCRIPTION_COL.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("description")
        );
        UNIT_COL.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("unit")
        );
        QUANTITY_COL.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("quantity")
        );
        USAGE_COL.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("usage")
        );

        TreeItem<externalFramingMaterials> externalFramingControllerTreeItem = new RecursiveTreeItem<>
                (externalFramingMaterials, RecursiveTreeObject::getChildren);
        MATERIALS_TBL.setRoot(externalFramingControllerTreeItem);
        MATERIALS_TBL.setShowRoot(false);
    }

}
