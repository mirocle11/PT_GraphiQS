package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class setupSheetsController implements Initializable {

    public JFXTabPane TAB_PANE;
    public JFXButton REFRESH;
    public static ObservableList<String> selectedBoxes = FXCollections.observableArrayList();

    //sheets content (residential)
    public AnchorPane FOUNDATIONS, EXT_OPENINGS, INT_OPENINGS, POST_BEAM_HARDWARE, WALLS_SGL_LEV, ROOF, EXT_LINING;

    //sheets content (shed)
    public AnchorPane SHED_FOUNDATION;

    //contentList = residential
    public ArrayList<AnchorPane> contentList = new ArrayList();
    public ArrayList<AnchorPane> shedList = new ArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        loadTabs();

        REFRESH.setOnAction(event -> {
            TAB_PANE.getTabs().clear();
            if (jobInfoController.selectedProjectType.equals("Residential")) {
                selectedBoxes.forEach((String s) -> {
                    Tab tab = new Tab();
                    tab.setText(s);
                    contentList.forEach(anchorPane -> {
                        if (anchorPane.getId().equals(s)) {
                            tab.setContent(anchorPane);
                        }
                    });
                    TAB_PANE.getTabs().add(tab);
                });
            } else {
                selectedBoxes.forEach((String s) -> {
                    Tab tab = new Tab();
                    tab.setText(s);
                    shedList.forEach(anchorPane -> {
                        if (anchorPane.getId().equals(s)) {
                            tab.setContent(anchorPane);
                        }
                    });
                    TAB_PANE.getTabs().add(tab);
                });
            }
        });
    }

    public void loadTabs() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Sheets/Residential/foundations.fxml"));
            FOUNDATIONS = loader.load();
            FOUNDATIONS.setId("Foundations");
            contentList.add(FOUNDATIONS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Sheets/Residential/extOpenings.fxml"));
            EXT_OPENINGS = loader.load();
            EXT_OPENINGS.setId("Ext Openings");
            contentList.add(EXT_OPENINGS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Sheets/Residential/intOpenings.fxml"));
            INT_OPENINGS = loader.load();
            INT_OPENINGS.setId("Int Openings");
            contentList.add(INT_OPENINGS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Sheets/Residential/postAndBeams.fxml"));
            POST_BEAM_HARDWARE = loader.load();
            POST_BEAM_HARDWARE.setId("Post & Beam Hardware");
            contentList.add(POST_BEAM_HARDWARE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Sheets/Residential/wallsSglLev.fxml"));
            WALLS_SGL_LEV = loader.load();
            WALLS_SGL_LEV.setId("Walls Sgl Lev");
            contentList.add(WALLS_SGL_LEV);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Sheets/Residential/roof.fxml"));
            ROOF = loader.load();
            ROOF.setId("Roof");
            contentList.add(ROOF);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Sheets/Residential/extLining.fxml"));
            EXT_LINING = loader.load();
            EXT_LINING.setId("Ext Lining");
            contentList.add(EXT_LINING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //shed loaders
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Sheets/Shed/foundations.fxml"));
            SHED_FOUNDATION = loader.load();
            SHED_FOUNDATION.setId("Foundations");
            shedList.add(SHED_FOUNDATION);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Sheets/Shed/externalFraming.fxml"));
//            SHED_EXTERNAL_FRAMING = loader.load();
//            SHED_EXTERNAL_FRAMING.setId("Pole Shed External Framing");
//            shedList.add(SHED_EXTERNAL_FRAMING);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Sheets/Shed/framingHardware.fxml"));
//            SHED_FRAMING_HARDWARE = loader.load();
//            SHED_FRAMING_HARDWARE.setId("Poleshed Framing Hardware");
//            shedList.add(SHED_FRAMING_HARDWARE);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Sheets/Shed/cladding.fxml"));
//            SHED_CLADDING = loader.load();
//            SHED_CLADDING.setId("Poleshed Cladding");
//            shedList.add(SHED_CLADDING);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Sheets/Shed/claddingOpt.fxml"));
//            SHED_CLADDING_OPT = loader.load();
//            SHED_CLADDING_OPT.setId("Poleshed Cladding Opt");
//            shedList.add(SHED_CLADDING_OPT);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}