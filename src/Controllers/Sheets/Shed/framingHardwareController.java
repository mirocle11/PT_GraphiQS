package Controllers.Sheets.Shed;

import Model.data.shed.framingHardwareMaterials;
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

public class framingHardwareController implements Initializable {

    //collections
    public ObservableList<String> parts = FXCollections.observableArrayList();
    public VBox PARTS_VBOX;
    public ArrayList<JFXButton> buttonList = new ArrayList<>();

    //tables
    public JFXTreeTableView<framingHardwareMaterials> MATERIALS_TBL;
    public TreeTableColumn<framingHardwareMaterials, String> SKU_NUMBER_COL;
    public TreeTableColumn<framingHardwareMaterials, String> DESCRIPTION_COL;
    public TreeTableColumn<framingHardwareMaterials, String> UNIT_COL;
    public TreeTableColumn<framingHardwareMaterials, String> QUANTITY_COL;

    //data lists
    public static ObservableList<framingHardwareMaterials> framingHardwareMaterials;

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
        framingHardwareMaterials = FXCollections.observableArrayList();

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

        TreeItem<framingHardwareMaterials> framingHardwareMaterialsTreeItem = new RecursiveTreeItem<>
                (framingHardwareMaterials, RecursiveTreeObject::getChildren);
        MATERIALS_TBL.setRoot(framingHardwareMaterialsTreeItem);
        MATERIALS_TBL.setShowRoot(false);

        framingHardwareMaterials.addAll(new framingHardwareMaterials("FXLLSP400802",
                "MITEK SPLICE PLATE 400 X 80 X 2 MM RS40080", "EACH", "8"));
        framingHardwareMaterials.addAll(new framingHardwareMaterials("FXLL4T10",
                "L/LOK 4T10 TYLOK NAIL PLATE 120X68MM", "EACH", "16"));
        framingHardwareMaterials.addAll(new framingHardwareMaterials("FXLL4T10",
                "WASHER SQ M16 X 50 X 50 X 6 GALV P/ EA", "EACH", "100"));
        framingHardwareMaterials.addAll(new framingHardwareMaterials("BHBMB75",
                "BOWMAC B75 STRAP", "EACH", "28"));
        framingHardwareMaterials.addAll(new framingHardwareMaterials("FXLLCPC80",
                "L/LOK CPC80 CONCEALED PURLIN CLEAT 2.0MM X 80MM", "EACH", "12"));
        framingHardwareMaterials.addAll(new framingHardwareMaterials("BULLGIRT",
                "LUMBERLOK GIRT TO POLE FIXING PLATE", "EACH", "278"));
        framingHardwareMaterials.addAll(new framingHardwareMaterials("FXLLJH1252",
                "L/LOK 120 X 52MM JOIST HANGER", "EACH", "72"));
        framingHardwareMaterials.addAll(new framingHardwareMaterials("BOEN12130",
                "M12 X 130 ENG BOLT/NUT GALV EACH", "EACH", "56"));
        framingHardwareMaterials.addAll(new framingHardwareMaterials("BOEN16240",
                "M16 X 240 ENG BOLT/NUT GALV EACH", "EACH", "50"));
        framingHardwareMaterials.addAll(new framingHardwareMaterials("BULLMB30",
                "L/LOK 30M COIL MULTIBRACE 1MMX53MM WIDE PUNCHED", "COIL", "3"));
        framingHardwareMaterials.addAll(new framingHardwareMaterials("FXLLMBT",
                "L/LOK MULTIBRACE TENSIONER ONLY EACH", "EACH", "12"));
        framingHardwareMaterials.addAll(new framingHardwareMaterials("FXLLNP110124",
                "L/LOK NAILON PLATE 110MM WIDE X 1.0MM X 240MM", "EACH", "8"));
        framingHardwareMaterials.addAll(new framingHardwareMaterials("NALLPN30315P",
                "L/LOK 30X3.15MM PRODUCT NAILS 500G PKT", "PKT", "3"));
        framingHardwareMaterials.addAll(new framingHardwareMaterials("NAGFH100X2KG",
                "NAIL FLAT HEAD GALV 100 X 4.00MM 2KG", "BOX", "1"));
        framingHardwareMaterials.addAll(new framingHardwareMaterials("SCMKSC3512DG",
                "MITEK 12G 35MM GALV SCREWS 100/BAG SC3512DG", "BAG", "23"));
        framingHardwareMaterials.addAll(new framingHardwareMaterials("FXLLSC3514EG",
                "LUMBERLOK SCREW FIXINGS 14G 35MM ELECTRO GALV BAG OF 100", "BAG", "1"));
        framingHardwareMaterials.addAll(new framingHardwareMaterials("FXLLSC3514EG",
                "LUMBERLOK SCREW FIXINGS 14G 75MM ELECTRO GALV BAG OF 100", "BAG", "1"));
        framingHardwareMaterials.addAll(new framingHardwareMaterials("NAIMPZB20571",
                "PASLODE IMPULSE 90X3.15MM HD GALV D-HEAD NAIL 1000 B20571", "BOX", "1"));

    }

}
