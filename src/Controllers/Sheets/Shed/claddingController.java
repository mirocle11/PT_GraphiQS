package Controllers.Sheets.Shed;

import Model.data.shed.claddingMaterials;
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

public class claddingController implements Initializable {

    //collections
    public ObservableList<String> parts = FXCollections.observableArrayList();
    public VBox PARTS_VBOX;
    public ArrayList<JFXButton> buttonList = new ArrayList<>();

    //tables
    public JFXTreeTableView<claddingMaterials> MATERIALS_TBL;
    public TreeTableColumn<claddingMaterials, String> SKU_NUMBER_COL;
    public TreeTableColumn<claddingMaterials, String> DESCRIPTION_COL;
    public TreeTableColumn<claddingMaterials, String> UNIT_COL;
    public TreeTableColumn<claddingMaterials, String> QUANTITY_COL;

    //data lists
    public static ObservableList<claddingMaterials> claddingMaterials;

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
        claddingMaterials = FXCollections.observableArrayList();

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

        TreeItem<claddingMaterials> claddingMaterialsTreeItem = new RecursiveTreeItem<>
                (claddingMaterials, RecursiveTreeObject::getChildren);
        MATERIALS_TBL.setRoot(claddingMaterialsTreeItem);
        MATERIALS_TBL.setShowRoot(false);

        claddingMaterials.addAll(new claddingMaterials("Buy-in",
                "LR CORRUGATE ENDURA .40", "EACH", "0"));
        claddingMaterials.addAll(new claddingMaterials("Buy-in",
                "RIDGE - 200X200 WESTCOAST FL/G ENDURA CLR", "EACH", "0"));
        claddingMaterials.addAll(new claddingMaterials("Buy-in",
                "VERMIN STRIP", "EACH", "0"));
        claddingMaterials.addAll(new claddingMaterials("Buy-in",
                "HP TIMBERTITE SCREWS 12X65 COLOUR (100)", "EACH", "0"));
        claddingMaterials.addAll(new claddingMaterials("BUNE18315250",
                "SAFETY MESH 1800X300X150X2 50M 90M2 AUSMESH", "ROLL", "0"));
        claddingMaterials.addAll(new claddingMaterials("RPTKCTP135055",
                "T/KRAFT COVERTEK 403 PLUS ROOF UNDERLAY 1350 X 55.6M 75M2", "ROLL", "0"));
        claddingMaterials.addAll(new claddingMaterials("RPTKWGJH274036",
                "T/KRAFT *JENNIAN HOMES* WATERGATE PLUS 295 2740X36.5M 100M2", "ROLL", "0"));
        claddingMaterials.addAll(new claddingMaterials("Buy-in",
                "ENDURA P A DOOR 2MX800MM", "EACH", "0"));
        claddingMaterials.addAll(new claddingMaterials("Buy-in",
                "COLOURSTEEL ROLLER DOOR 3MX3M", "EACH", "0"));
        claddingMaterials.addAll(new claddingMaterials("Buy-in",
                "COLOURSTEEL ROLLER DOOR 3.6(W)X3350MM(H)", "EACH", "0"));
        claddingMaterials.addAll(new claddingMaterials("PSAMPSSC8C30",
                "SOLASAFE CORRUGATED 850MM X 3.0M CLEAR 760 COVER", "SHT", "0"));
        claddingMaterials.addAll(new claddingMaterials("PSAMPSSC8C42",
                "SOLASAFE CORRUGATED 850MM X 4.2M CLEAR 760 COVER", "SHT", "0"));
    }

}
