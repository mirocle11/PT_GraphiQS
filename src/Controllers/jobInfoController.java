package Controllers;

import DataBase.DataBase;
import Model.StructureModel;
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

    public JFXButton NEXT, BACK, SUBMIT;
    public AnchorPane FIRST_PANE;
    public ScrollPane SECOND_PANE;
    public Pane LIST_PANE;

    public JFXCheckBox SELECT_ALL;
    public JFXComboBox<String> CLIENT_COMBO_BOX;

    static ObservableList<String> list = FXCollections.observableArrayList();
    private ObservableList<String> CLIENTS = FXCollections.observableArrayList();

    public static String selectedClient;
    static StructureModel structureModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        structureModel = new StructureModel(this);

        DataBase db = DataBase.getInstance();
        db.clientComboBox(CLIENTS);
        CLIENT_COMBO_BOX.setItems(CLIENTS);

        NEXT.setOnAction(event -> {
            FIRST_PANE.setVisible(false);
            SECOND_PANE.setVisible(true);

            NEXT.setVisible(false);
            BACK.setDisable(false);
            SUBMIT.setVisible(true);
        });

        BACK.setOnAction(event -> {
            FIRST_PANE.setVisible(true);
            SECOND_PANE.setVisible(false);

            NEXT.setVisible(true);
            NEXT.setDisable(false);
            BACK.setDisable(true);
            SUBMIT.setVisible(false);
        });

        SELECT_ALL.setOnAction(event -> {
            if (SELECT_ALL.isSelected()) {
                LIST_PANE.getChildren().forEach((Node node) -> {
                    ((JFXCheckBox) node).setSelected(true);
                });
            } else {
                LIST_PANE.getChildren().forEach(node -> {
                    ((JFXCheckBox) node).setSelected(false);
                });
            }
        });

        SUBMIT.setOnAction(event -> {
            selectStructures();
            selectedClient = CLIENT_COMBO_BOX.getSelectionModel().getSelectedItem();
        });

    }

    private void selectStructures() {
        list.clear();
        LIST_PANE.getChildren().forEach(node -> {
            if (((JFXCheckBox) node).isSelected()) {
                String boxes = ((JFXCheckBox) node).getText();
                list.add(boxes);
//                System.out.println(list);
                workspaceController.selectedBoxes = list;
                setupSheetsController.selectedBoxes = list;
            }
        });
    }

//    private void setTabs() {
//        LIST_PANE.getChildren().forEach(node -> {
//            if (((JFXCheckBox) node).isSelected()) {
//                String boxes = ((JFXCheckBox) node).getText();
//
//                //setup sheets tabs
//                String[] splitArray = boxes.split(",");
//                for (int i = 0; i < splitArray.length; i++) {
//                    setupSheets.createSelectedTabs(splitArray[i]);
//                    structureModel.setupSheets.createSelectedTabs(splitArray[i]);
//                }
//            }
//        });
//    }

//    private void accept(Node node) {
//        if (((JFXCheckBox) node).isSelected()) {
//            String boxes = ((JFXCheckBox) node).getText();
//            list.add(boxes);
//            System.out.println(list);
//            workspaceController.getSelectedStructures(list);
//        }
//    }

}
