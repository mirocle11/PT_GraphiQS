package Controllers;

import DataBase.DataBase;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class jobInfoController implements Initializable {

    public JFXButton NEXT, BACK, SUBMIT, RESIDENTIAL, SHED;
    public AnchorPane FIRST_PANE;
    public ScrollPane SECOND_PANE;
    public Pane RESIDENTIAL_PANE, SHED_PANE;

    public JFXCheckBox SELECT_ALL;
    public JFXComboBox<String> CLIENT_COMBO_BOX;

    public static ObservableList<String> list = FXCollections.observableArrayList();
    public ObservableList<String> CLIENTS = FXCollections.observableArrayList();

    public static String selectedClient;
    public static String selectedProjectType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DataBase db = DataBase.getInstance();
        db.clientComboBox(CLIENTS);
        CLIENT_COMBO_BOX.setItems(CLIENTS);

        NEXT.setOnAction(event -> {
            FIRST_PANE.setVisible(false);
            SECOND_PANE.setVisible(true);

            NEXT.setVisible(false);
            BACK.setDisable(false);
            SUBMIT.setVisible(true);

            RESIDENTIAL.setVisible(false);
            SHED.setVisible(false);
        });

        BACK.setOnAction(event -> {
            FIRST_PANE.setVisible(true);
            SECOND_PANE.setVisible(false);

            NEXT.setVisible(true);
            NEXT.setDisable(false);
            BACK.setDisable(true);
            SUBMIT.setVisible(false);

            RESIDENTIAL.setVisible(true);
            SHED.setVisible(true);
        });

        SELECT_ALL.setOnAction(event -> {
            if (SELECT_ALL.isSelected()) {
                RESIDENTIAL_PANE.getChildren().forEach((Node node) -> {
                    ((JFXCheckBox) node).setSelected(true);
                });
            } else {
                RESIDENTIAL_PANE.getChildren().forEach(node -> {
                    ((JFXCheckBox) node).setSelected(false);
                });
            }
        });

        SUBMIT.setOnAction(event -> {
            selectStructures();
            selectedClient = CLIENT_COMBO_BOX.getSelectionModel().getSelectedItem();
        });

        RESIDENTIAL.setOnAction(event -> {
            SHED_PANE.setVisible(false);
            RESIDENTIAL_PANE.setVisible(true);
            SELECT_ALL.setVisible(true);
            selectedProjectType = "Residential";
        });

        SHED.setOnAction(event -> {
            RESIDENTIAL_PANE.setVisible(false);
            SHED_PANE.setVisible(true);
            SELECT_ALL.setVisible(false);
            selectedProjectType = "Shed";
        });
    }

    private void selectStructures() {
        list.clear();
        if (selectedProjectType.equals("Residential")) {
            RESIDENTIAL_PANE.getChildren().forEach(node -> {
                if (((JFXCheckBox) node).isSelected()) {
                    String boxes = ((JFXCheckBox) node).getText();
                    list.add(boxes);
                    workspaceController.selectedBoxes = list;
                    setupSheetsController.selectedBoxes = list;
                }
            });
        } else {
            SHED_PANE.getChildren().forEach(node -> {
                if (((JFXCheckBox) node).isSelected()) {
                    String boxes = ((JFXCheckBox) node).getText();
                    list.add(boxes);
                    setupSheetsController.selectedBoxes = list;
                }
            });
        }

    }

}
