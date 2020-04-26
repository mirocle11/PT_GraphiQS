package Controllers;

import DataBase.DataBase;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class viewSubtradeController implements Initializable {

    public Label SUBTRADE_LBL, FIRST_NAME_LBL, LAST_NAME_LBL, ADDRESS_LBL, CONTACT_PERSON_LBL, EMAIL_LBL, MOBILE_LBL,
            BEST_CONTACT_LBL, MATERIAL_LBL, RATE_LBL, UNIT_LBL, material_lbl, rate_lbl, unit_lbl;

    public JFXTextField FIRST_NAME_FLD, LAST_NAME_FLD, ADDRESS_FLD, CONTACT_PERSON_FLD, EMAIL_FLD, MOBILE_FLD, RATE_FLD;
    public JFXComboBox<String> SUBTRADE_CMB, BEST_CONTACT_CMB, MATERIAL_CMB;
    public JFXButton SAVE, CANCEL, EDIT, BACK;

    public VBox DETAIL_BOX_LBL, DETAIL_BOX_FLDS;

    private ObservableList<String> subtrades = FXCollections.observableArrayList("Gib Stopper", "Brick Layer",
            "Electrician");
    private ObservableList<String> best_contact = FXCollections.observableArrayList("Mobile", "Email", "Landline");

    private ObservableList<String> GIB_STOPPER_MATERIAL = FXCollections.observableArrayList("Supply and Fit", "Fit");
    private ObservableList<String> BRICK_LAYER_MATERIAL = FXCollections.observableArrayList("Veneer Walls");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DataBase db = DataBase.getInstance();
        db.viewSubtradeDetails(builderController.subtrade_id, SUBTRADE_LBL, FIRST_NAME_LBL, LAST_NAME_LBL, ADDRESS_LBL,
                CONTACT_PERSON_LBL, EMAIL_LBL, MOBILE_LBL, BEST_CONTACT_LBL, MATERIAL_LBL, RATE_LBL, UNIT_LBL);

        SUBTRADE_CMB.setItems(subtrades);
        SUBTRADE_CMB.setValue(SUBTRADE_LBL.getText());
        FIRST_NAME_FLD.setText(FIRST_NAME_LBL.getText());
        LAST_NAME_FLD.setText(LAST_NAME_LBL.getText());
        ADDRESS_FLD.setText(ADDRESS_LBL.getText());
        CONTACT_PERSON_FLD.setText(CONTACT_PERSON_LBL.getText());
        EMAIL_FLD.setText(EMAIL_LBL.getText());
        MOBILE_FLD.setText(MOBILE_LBL.getText());
        BEST_CONTACT_CMB.setItems(best_contact);
        BEST_CONTACT_CMB.setValue(BEST_CONTACT_LBL.getText());

        setMaterial();
        MATERIAL_CMB.setValue(MATERIAL_LBL.getText());
        RATE_FLD.setText(RATE_LBL.getText());

        SUBTRADE_CMB.setOnAction(event -> {
            if (!SUBTRADE_CMB.getSelectionModel().isEmpty()) {
                setMaterial();
            }
        });

        EDIT.setOnAction(event -> {
            EDIT.setVisible(false);
            SAVE.setVisible(true);
            BACK.setVisible(true);

            DETAIL_BOX_LBL.setVisible(false);
            DETAIL_BOX_FLDS.setVisible(true);

            material_lbl.setVisible(false);
            rate_lbl.setVisible(false);
            MATERIAL_LBL.setVisible(false);
            RATE_LBL.setVisible(false);

            MATERIAL_CMB.setVisible(true);
            RATE_FLD.setVisible(true);
        });

        BACK.setOnAction(event -> {
            EDIT.setVisible(true);
            SAVE.setVisible(false);
            BACK.setVisible(false);

            DETAIL_BOX_LBL.setVisible(true);
            DETAIL_BOX_FLDS.setVisible(false);

            material_lbl.setVisible(true);
            rate_lbl.setVisible(true);
            MATERIAL_LBL.setVisible(true);
            RATE_LBL.setVisible(true);

            MATERIAL_CMB.setVisible(false);
            RATE_FLD.setVisible(false);
        });

        SAVE.setOnAction(event -> {
            db.updateSubtrade(builderController.subtrade_id, SUBTRADE_CMB.getSelectionModel().getSelectedItem(),
                    FIRST_NAME_FLD.getText(), LAST_NAME_FLD.getText(), ADDRESS_FLD.getText(), CONTACT_PERSON_FLD.getText(),
                    EMAIL_FLD.getText(), MOBILE_FLD.getText(), BEST_CONTACT_CMB.getSelectionModel().getSelectedItem());

            db.updatePriceCard(builderController.subtrade_id, MATERIAL_CMB.getSelectionModel().getSelectedItem(), RATE_FLD.getText());
            db.displaySubtrades(builderController.client_id, builderController.subtradesData);

            JOptionPane.showMessageDialog(null, "Subtrade updated!");
            builderController.closeViewSubtradeStage();
        });

        CANCEL.setOnAction(event -> {
            builderController.closeViewSubtradeStage();
        });

    }

    private void setMaterial() {
        if (SUBTRADE_CMB.getSelectionModel().getSelectedItem().equals("Gib Stopper")) {
            MATERIAL_CMB.setItems(GIB_STOPPER_MATERIAL);
            UNIT_LBL.setText("per m²");
        } else if (SUBTRADE_CMB.getSelectionModel().getSelectedItem().equals("Brick Layer")) {
            MATERIAL_CMB.setItems(BRICK_LAYER_MATERIAL);
            UNIT_LBL.setText("per 100 bricks");
        } else {
            MATERIAL_CMB.setItems(null);
            UNIT_LBL.setText("");
        }
    }

}
