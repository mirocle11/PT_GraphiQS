package Controllers;

import DataBase.DataBase;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

import static Controllers.builderController.subtradesData;

public class registerSubtradeController implements Initializable {

    public JFXComboBox<String> SUBTRADE, BEST_WAY_TO_CONTACT, MATERIAL;
    public JFXTextField FIRST_NAME, LAST_NAME, ADDRESS, CONTACT_PERSON, EMAIL, MOBILE, RATE;
    public JFXButton CANCEL, SAVE;
    public Label UNIT;

    private ObservableList<String> subtrades = FXCollections.observableArrayList("Gib Stopper", "Brick Layer",
            "Electrician");
    private ObservableList<String> best_contact = FXCollections.observableArrayList("Mobile", "Email", "Landline");

    private ObservableList<String> GIB_STOPPER_MATERIAL = FXCollections.observableArrayList("Supply and Fit", "Fit");
    private ObservableList<String> BRICK_LAYER_MATERIAL = FXCollections.observableArrayList("Veneer Walls");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SUBTRADE.setItems(subtrades);
        BEST_WAY_TO_CONTACT.setItems(best_contact);

        SAVE.setOnAction(event -> {
            DataBase db = DataBase.getInstance();
            db.addSubtrade(builderController.client_id, SUBTRADE.getSelectionModel().getSelectedItem(),
                    FIRST_NAME.getText(), LAST_NAME.getText(), CONTACT_PERSON.getText(), ADDRESS.getText(),
                    EMAIL.getText(), MOBILE.getText(), BEST_WAY_TO_CONTACT.getSelectionModel().getSelectedItem());
            db.addPriceCard(MATERIAL.getSelectionModel().getSelectedItem(), UNIT.getText(), RATE.getText());
            db.displaySubtrades(builderController.client_id, subtradesData);
            JOptionPane.showMessageDialog(null, "Subtrade registered!");
            builderController.closeSubtradeStage();
        });

        CANCEL.setOnAction(event -> {
            builderController.closeSubtradeStage();
        });

        SUBTRADE.setOnAction(event -> {
            if (!SUBTRADE.getSelectionModel().isEmpty()) {
                setMaterialBox();
            }
        });
    }

    private void setMaterialBox() {
        if (SUBTRADE.getSelectionModel().getSelectedItem().equals("Gib Stopper")) {
            MATERIAL.setItems(GIB_STOPPER_MATERIAL);
            UNIT.setText("per m²");
        } else if (SUBTRADE.getSelectionModel().getSelectedItem().equals("Brick Layer")) {
            MATERIAL.setItems(BRICK_LAYER_MATERIAL);
            UNIT.setText("per 100 bricks");
        } else {
            MATERIAL.setItems(null);
            UNIT.setText("");
        }
    }

}
