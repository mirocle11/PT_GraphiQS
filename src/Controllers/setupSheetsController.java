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

    //sheets content
    public AnchorPane FOUNDATIONS, EXT_OPENINGS, INT_OPENINGS, POST_BEAM_HARDWARE, WALLS_SGL_LEV, ROOF, EXT_LINING;

    public ArrayList<AnchorPane> contentList = new ArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        loadTabs();

        REFRESH.setOnAction(event -> {
            TAB_PANE.getTabs().clear();
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
        });
    }

    public void loadTabs() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Sheets/foundations.fxml"));
            FOUNDATIONS = loader.load();
            FOUNDATIONS.setId("Foundations");
            contentList.add(FOUNDATIONS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Sheets/extOpenings.fxml"));
            EXT_OPENINGS = loader.load();
            EXT_OPENINGS.setId("Ext Openings");
            contentList.add(EXT_OPENINGS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Sheets/intOpenings.fxml"));
            INT_OPENINGS = loader.load();
            INT_OPENINGS.setId("Int Openings");
            contentList.add(INT_OPENINGS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Sheets/postAndBeams.fxml"));
            POST_BEAM_HARDWARE = loader.load();
            POST_BEAM_HARDWARE.setId("Post & Beam Hardware");
            contentList.add(POST_BEAM_HARDWARE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Sheets/wallsSglLev.fxml"));
            WALLS_SGL_LEV = loader.load();
            WALLS_SGL_LEV.setId("Walls Sgl Lev");
            contentList.add(WALLS_SGL_LEV);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Sheets/roof.fxml"));
            ROOF = loader.load();
            ROOF.setId("Roof");
            contentList.add(ROOF);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Sheets/extLining.fxml"));
            EXT_LINING = loader.load();
            EXT_LINING.setId("Ext Lining");
            contentList.add(EXT_LINING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}