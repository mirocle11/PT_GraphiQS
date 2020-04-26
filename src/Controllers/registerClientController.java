package Controllers;

import DataBase.DataBase;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class registerClientController implements Initializable {

    public JFXButton CANCEL, ADD, MIN, NEXT, BACK, SAVE;
    public VBox CLIENT_DETAILS_BOX, CLIENT_CREDENTIALS_BOX;
    public Label CLIENT_DETAILS_LBL, CLIENT_CREDENTIALS_LBL;
    public HBox hBox;

    public JFXTextField FIRST_NAME, LAST_NAME, CONTACT_PERSON, EMAIL, MOBILE, LANDLINE, USERNAME, SECURITY_ANSWER;
    public JFXPasswordField PASSWORD, RETYPE_PASSWORD;
    public JFXComboBox<String> SECURITY_QUESTION;

    private ObservableList<String> security_questions = FXCollections.observableArrayList("What is your Mother's name?",
            "What is your Partner's name?", "What is your Sibling's name?", "What is your dog's name?", "What primary school did you attend?");

    private ObservableList<String> subtrades = FXCollections.observableArrayList( "Gib Stopper", "Brick Layer",
            "Electrician");

    private int indicator = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createNewBox();

        SECURITY_QUESTION.setItems(security_questions);

        CANCEL.setOnAction(event -> {
            builderController.closeClientStage();
        });

        ADD.setOnAction(event -> {
            createNewBox();
            indicator++;
        });

        MIN.setOnAction(event -> {
            deleteLastBox();
        });

        NEXT.setOnAction(event -> {
            CLIENT_DETAILS_LBL.setVisible(false);
            CLIENT_DETAILS_BOX.setVisible(false);

            CLIENT_CREDENTIALS_LBL.setVisible(true);
            CLIENT_CREDENTIALS_BOX.setVisible(true);

            NEXT.setVisible(false);
            BACK.setVisible(true);
        });

        BACK.setOnAction(event -> {
            CLIENT_DETAILS_LBL.setVisible(true);
            CLIENT_DETAILS_BOX.setVisible(true);

            CLIENT_CREDENTIALS_LBL.setVisible(false);
            CLIENT_CREDENTIALS_BOX.setVisible(false);

            NEXT.setVisible(true);
            BACK.setVisible(false);
        });

        SAVE.setOnAction(event -> {
            DataBase db = DataBase.getInstance();
            db.addUser(USERNAME.getText(), PASSWORD.getText(), SECURITY_QUESTION.getSelectionModel().getSelectedItem(),
                    SECURITY_ANSWER.getText());
            db.addClient(FIRST_NAME.getText(), LAST_NAME.getText(), CONTACT_PERSON.getText(), EMAIL.getText(),
                    MOBILE.getText(), LANDLINE.getText());
            JOptionPane.showMessageDialog(null, "Client registered!");
            db.displayClients(builderController.clientsData);
            builderController.closeClientStage();
        });
    }

    private void createNewBox() {
        VBox vbox = new VBox();
        vbox.setSpacing(25);
        vbox.setMinHeight(Region.USE_COMPUTED_SIZE);
        vbox.setPrefWidth(225);
        hBox.getChildren().add(vbox);

        JFXComboBox<String> subtrade = new JFXComboBox<String>();
        subtrade.setPromptText("Subtrade");
        subtrade.setMinWidth(250);
        subtrade.setFocusTraversable(false);
        subtrade.setItems(subtrades);
        vbox.getChildren().add(subtrade);

        JFXTextField first_name = new JFXTextField();
        first_name.setPromptText("First Name");
        first_name.setFont(new Font("Segoe UI", 14));
        first_name.setFocusTraversable(false);
        vbox.getChildren().add(first_name);

        JFXTextField last_name = new JFXTextField();
        last_name.setPromptText("Last Name");
        last_name.setFont(new Font("Segoe UI", 14));
        last_name.setFocusTraversable(false);
        vbox.getChildren().add(last_name);

        JFXTextField address = new JFXTextField();
        address.setPromptText("Address");
        address.setFont(new Font("Segoe UI", 14));
        address.setFocusTraversable(false);
        vbox.getChildren().add(address);

        JFXTextField contact = new JFXTextField();
        contact.setPromptText("Contact");
        contact.setFont(new Font("Segoe UI", 14));
        contact.setFocusTraversable(false);
        vbox.getChildren().add(contact);

        JFXTextField email = new JFXTextField();
        email.setPromptText("Email");
        email.setFont(new Font("Segoe UI", 14));
        email.setFocusTraversable(false);
        vbox.getChildren().add(email);

        JFXTextField mob = new JFXTextField();
        mob.setPromptText("MOB #");
        mob.setFont(new Font("Segoe UI", 14));
        mob.setFocusTraversable(false);
        vbox.getChildren().add(mob);

        JFXTextField bwtc = new JFXTextField();
        bwtc.setPromptText("Best way to contact");
        bwtc.setFont(new Font("Segoe UI", 14));
        bwtc.setFocusTraversable(false);
        vbox.getChildren().add(bwtc);

        JFXButton rate = new JFXButton();
        rate.setMinHeight(27);
        rate.setMinWidth(250);
        rate.setText("Proceed to Rate");
        rate.setGraphic(new javafx.scene.image.ImageView(new Image(getClass()
                .getResourceAsStream("../Views/images/pricing_25px.png"))));
        rate.setFocusTraversable(false);
        rate.setFont(new Font("Segoe UI", 14));
        vbox.getChildren().add(rate);
    }

    private void deleteLastBox() {
        if (!(indicator == 0)) {
            hBox.getChildren().remove(indicator);
            indicator--;
        }
    }

//    private void saveData() {
//        subtradeList.clear();
//        hBox.getChildren().forEach(node -> {
//            for (Node node1 : ((VBox)node).getChildren()) {
//                if (node1 instanceof JFXComboBox) {
//                    String data = ((JFXComboBox)node1).getSelectionModel().getSelectedItem().toString();
//                    subtradeList.add(data);
//                }
//                if (node1 instanceof JFXTextField) {
//                    String data = ((JFXTextField)node1).getText();
//                    subtradeList.add(data);
//                }
//            }
//        });
//        System.out.println(subtradeList);
//        System.out.println(subtradeList.size());

//        if (subtradeList.size() == 8) {
//            DataBase db = DataBase.getInstance();
//            db.addSubtrade(subtradeList.get(0),subtradeList.get(1),subtradeList.get(2),subtradeList.get(3),
//                    subtradeList.get(4),subtradeList.get(5),subtradeList.get(6),subtradeList.get(7),subtradeList.get(8));
//            subtradeList.clear();

//            System.out.println(subtradeList);
//
//        }
//
//    }

}
