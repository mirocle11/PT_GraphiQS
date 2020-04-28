package Controllers;

import DataBase.DataBase;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class createProjectController implements Initializable {

    public AnchorPane createBox, firstpane, secondpane;
    public JFXButton next, back, proceed;
    public Label page, second_label;
    public JFXCheckBox selectAllBox;
    public JFXComboBox<String> CLIENT_COMBO_BOX;

    static ObservableList<String> list = FXCollections.observableArrayList();
    private ObservableList<String> CLIENTS = FXCollections.observableArrayList();

    public static String selectedClient;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DataBase db = DataBase.getInstance();
        db.clientComboBox(CLIENTS);

        CLIENT_COMBO_BOX.setItems(CLIENTS);
    }

    public void cancel() {
        workspaceController.closeCreateProject();
    }

    public void next() {
        secondpane.getChildren().forEach(node -> {
            ((JFXCheckBox)node).setSelected(false);
        });

        firstpane.setVisible(false);
        secondpane.setVisible(true);

        back.setDisable(false);
        next.setVisible(false);
        proceed.setVisible(true);
        selectAllBox.setVisible(true);
        second_label.setVisible(true);

        page.setText("2 out of 2");
    }

    public void back() {
        firstpane.setVisible(true);
        secondpane.setVisible(false);

        back.setDisable(true);
        next.setVisible(true);
        proceed.setVisible(false);
        selectAllBox.setVisible(false);
        second_label.setVisible(false);

        page.setText("1 out of 2");
    }

    public void proceed() {
        workspaceController.openPdfFile();
        cancel();
        selectStructures();
        selectedClient = CLIENT_COMBO_BOX.getSelectionModel().getSelectedItem();
    }

    public void selectAll() {
        if (selectAllBox.isSelected()) {
            secondpane.getChildren().forEach((Node node) -> {
                ((JFXCheckBox) node).setSelected(true);
            });
        } else {
            secondpane.getChildren().forEach(node -> {
                ((JFXCheckBox) node).setSelected(false);
            });
        }
    }

    private void selectStructures() {
        secondpane.getChildren().forEach(this::accept);
    }

    private void accept(Node node) {
        if (((JFXCheckBox) node).isSelected()) {
            String boxes = ((JFXCheckBox) node).getText();
            list.add(boxes);
            System.out.println(list);
            workspaceController.getSelectedStructures(list);
        }
    }

}
