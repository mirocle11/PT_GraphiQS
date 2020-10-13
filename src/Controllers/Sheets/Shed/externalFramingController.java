package Controllers.Sheets.Shed;

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
    public ObservableList<String> parts = FXCollections.observableArrayList("Building Poles", "Purlins");
    public VBox PARTS_VBOX;
    public ArrayList<JFXButton> buttonList = new ArrayList<>();

    //tables
    public JFXTreeTableView<externalFramingMaterials> MATERIALS_TBL;
    public TreeTableColumn<externalFramingMaterials, String> SKU_NUMBER_COL;
    public TreeTableColumn<externalFramingMaterials, String> DESCRIPTION_COL;
    public TreeTableColumn<externalFramingMaterials, String> UNIT_COL;
    public TreeTableColumn<externalFramingMaterials, String> QUANTITY_COL;

    //data lists
    public static ObservableList<externalFramingMaterials> externalFramingMaterials;

    @Override
    public void initialize(URL location, ResourceBundle resource) {
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

        TreeItem<externalFramingMaterials> externalFramingControllerTreeItem = new RecursiveTreeItem<>
                (externalFramingMaterials, RecursiveTreeObject::getChildren);
        MATERIALS_TBL.setRoot(externalFramingControllerTreeItem);
        MATERIALS_TBL.setShowRoot(false);

        externalFramingMaterials.addAll(new externalFramingMaterials("RWBP17542", //poles
                "4.2M BUILDING POLE H5 175-199", "EACH", "5"));
        externalFramingMaterials.addAll(new externalFramingMaterials("RWBP17548",
                "4.8M BUILDING POLE H5 175-199", "EACH", "10"));
        externalFramingMaterials.addAll(new externalFramingMaterials("RWBP17554",
                "5.4M BUILDING POLE H5 175-199", "EACH", "5"));

        externalFramingMaterials.addAll(new externalFramingMaterials("15050RVH32RS36", //purlins
                "150 X 50 RAD SG8 VERIFIED H3.2 WET RS 3.6M", "LGTH", "66"));
        externalFramingMaterials.addAll(new externalFramingMaterials("15050RVH32RS48",
                "150 X 50 RAD SG8 VERIFIED H3.2 WET RS 3.6M", "LGTH", "32"));
        externalFramingMaterials.addAll(new externalFramingMaterials("15050RVH32RS60",
                "150 X 50 RAD SG8 VERIFIED H3.2 WET RS 6.0M", "LGTH", "6"));
        externalFramingMaterials.addAll(new externalFramingMaterials("20050RVH32RS48",
                "200 X 50 RAD SG8 VERIFIED H3.2 WET RS 4.8M", "LGTH", "52"));
        externalFramingMaterials.addAll(new externalFramingMaterials("25050RVH32RS48",
                "250 X 50 RAD SG8 VERIFIED H3.2 WET RS 4.8M", "LGTH", "24"));
    }

}
