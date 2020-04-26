package Controllers;

import DataBase.DataBase;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class viewClientController implements Initializable {

    public JFXButton CANCEL, EDIT, SAVE, BACK;
    public Label FIRST_NAME_LBL, LAST_NAME_LBL, CONTACT_PERSON_LBL, EMAIL_LBL, MOBILE_LBL, LANDLINE_LBL;
    public JFXTextField FIRST_NAME_FLD, LAST_NAME_FLD, CONTACT_PERSON_FLD, EMAIL_FLD, MOBILE_FLD, LANDLINE_FLD;
    public VBox LABEL_BOX, FIELD_BOX;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DataBase db = DataBase.getInstance();
        db.viewClientDetails(builderController.client_id, FIRST_NAME_LBL, LAST_NAME_LBL, CONTACT_PERSON_LBL,
                EMAIL_LBL, MOBILE_LBL, LANDLINE_LBL);

        FIRST_NAME_FLD.setText(FIRST_NAME_LBL.getText());
        LAST_NAME_FLD.setText(LAST_NAME_LBL.getText());
        CONTACT_PERSON_FLD.setText(CONTACT_PERSON_LBL.getText());
        EMAIL_FLD.setText(EMAIL_LBL.getText());
        MOBILE_FLD.setText(MOBILE_LBL.getText());
        LANDLINE_FLD.setText(LANDLINE_LBL.getText());

        CANCEL.setOnAction(event -> {
            builderController.closeViewClientStage();
        });

        EDIT.setOnAction(event -> {
            LABEL_BOX.setVisible(false);
            FIELD_BOX.setVisible(true);
            BACK.setVisible(true);
            EDIT.setVisible(false);
            SAVE.setVisible(true);
        });

        BACK.setOnAction(event -> {
            LABEL_BOX.setVisible(true);
            FIELD_BOX.setVisible(false);
            BACK.setVisible(false);
            EDIT.setVisible(true);
            SAVE.setVisible(false);
        });

        SAVE.setOnAction(event -> {
            db.updateClient(builderController.client_id, FIRST_NAME_FLD.getText(), LAST_NAME_FLD.getText(),
                    CONTACT_PERSON_FLD.getText(), EMAIL_FLD.getText(), MOBILE_FLD.getText(), LANDLINE_FLD.getText());
            builderController.closeViewClientStage();
            db.displayClients(builderController.clientsData);
            JOptionPane.showMessageDialog(null, "Client updated!");
        });
    }

}
