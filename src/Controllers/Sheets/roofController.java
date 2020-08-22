package Controllers.Sheets;

import DataBase.DataBase;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class roofController implements Initializable {

    public ObservableList<String> parts = FXCollections.observableArrayList();
    public VBox PARTS_VBOX;

    public GridPane GABLES_ABOVE_BRICK, STEEL_BEAMS, CEILING_BEAMS, ROOF_BEAMS, SOFFIT_BEAMS, FASCIA_BEAMS, HIP_BEAMS,
            RIDGE_BEAMS, VALLEY_BEAMS, TRUSSED_ROOF_MEASURED, SKILLION_MEASURED, EXPOSED_TRUSS_MEMBER, ROOF_SUNDRIES,
            ROOF_PLY_SUBSTRATE, SUBSTRATE_MEASURED, COMMON_ROOF, GUTTERS, HIPS, RIDGES, VALLEYS, VERGE, STANDARD_SOFFIT,
            DIFFICULT_SOFFITS, BARGE, FASCIA, SPOUTING_MEASURED, APRONS, CHANGE_OF_ROOF_PITCH, PARAPETS, SKYLIGHT_WALLS;

    public ArrayList<GridPane> gridPaneList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        loadPanes();

        DataBase db = DataBase.getInstance();
        parts.clear();
        db.displayParts(30, parts);

        parts.forEach(s -> {
            JFXButton button = new JFXButton(s);
            button.setPrefWidth(183);
            button.setPrefHeight(30);
            button.setFont(new Font("Segoe UI",15.0));
            button.setTextFill(Color.web("#bcbcbc"));
            PARTS_VBOX.getChildren().add(button);

            button.setOnAction(event -> {
                gridPaneList.forEach(gridPane -> {
                    gridPane.setVisible(false);
                    if (button.getText().equals(gridPane.getId())) {
                        gridPane.setVisible(true);
                    }
                });
            });
        });
    }

    public void loadPanes() {
        GABLES_ABOVE_BRICK.setId("Gables Above Brick");
        STEEL_BEAMS.setId("Steel Beams");
        CEILING_BEAMS.setId("Ceiling Beams");
        ROOF_BEAMS.setId("Roof Beams");
        SOFFIT_BEAMS.setId("Soffit Beams");
        FASCIA_BEAMS.setId("Fascia Beams");
        HIP_BEAMS.setId("Hip Beams");
        RIDGE_BEAMS.setId("Ridge Beams");
        VALLEY_BEAMS.setId("Valley Beams");
        TRUSSED_ROOF_MEASURED.setId("Trussed Roof Measured");
        SKILLION_MEASURED.setId("Skillion Measured");
        EXPOSED_TRUSS_MEMBER.setId("Exposed Truss Member");
        ROOF_SUNDRIES.setId("Roof Sundries");
        ROOF_PLY_SUBSTRATE.setId("Roof Ply/Substrate");
        SUBSTRATE_MEASURED.setId("Substrate Measured");
        COMMON_ROOF.setId("Common Roof");
        GUTTERS.setId("Gutters");
        HIPS.setId("Hips");
        RIDGES.setId("Ridges");
        VALLEYS.setId("Valleys");
        VERGE.setId("Verge");
        STANDARD_SOFFIT.setId("Standard Soffit");
        DIFFICULT_SOFFITS.setId("Difficult Soffits");
        BARGE.setId("Barge");
        FASCIA.setId("Fascia");
        SPOUTING_MEASURED.setId("Spouting Measured");
        APRONS.setId("Aprons");
        CHANGE_OF_ROOF_PITCH.setId("Change of Roof Pitch");
        PARAPETS.setId("Parapets");
        SKYLIGHT_WALLS.setId("Skylight Walls");

        gridPaneList.add(GABLES_ABOVE_BRICK);
        gridPaneList.add(STEEL_BEAMS);
        gridPaneList.add(CEILING_BEAMS);
        gridPaneList.add(ROOF_BEAMS);
        gridPaneList.add(SOFFIT_BEAMS);
        gridPaneList.add(FASCIA_BEAMS);
        gridPaneList.add(HIP_BEAMS);
        gridPaneList.add(RIDGE_BEAMS);
        gridPaneList.add(VALLEY_BEAMS);
        gridPaneList.add(TRUSSED_ROOF_MEASURED);
        gridPaneList.add(SKILLION_MEASURED);
        gridPaneList.add(EXPOSED_TRUSS_MEMBER);
        gridPaneList.add(ROOF_SUNDRIES);
        gridPaneList.add(ROOF_PLY_SUBSTRATE);
        gridPaneList.add(SUBSTRATE_MEASURED);
        gridPaneList.add(COMMON_ROOF);
        gridPaneList.add(GUTTERS);
        gridPaneList.add(HIPS);
        gridPaneList.add(RIDGES);
        gridPaneList.add(VALLEYS);
        gridPaneList.add(VERGE);
        gridPaneList.add(STANDARD_SOFFIT);
        gridPaneList.add(DIFFICULT_SOFFITS);
        gridPaneList.add(BARGE);
        gridPaneList.add(FASCIA);
        gridPaneList.add(SPOUTING_MEASURED);
        gridPaneList.add(APRONS);
        gridPaneList.add(CHANGE_OF_ROOF_PITCH);
        gridPaneList.add(PARAPETS);
        gridPaneList.add(SKYLIGHT_WALLS);
    }

}
