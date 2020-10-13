package Controllers;

import Main.Main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class loginController implements Initializable {

    public JFXTextField USERNAME;
    public JFXPasswordField PASSWORD;
    public JFXButton FORGOT_PASSWORD, LOGIN, CLOSE;

    private Main main;
    private Stage stage;

    public void setMain(Stage stage, Main main) {
        this.main = main;
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGIN.setDefaultButton(true);
        LOGIN.setOnAction((ActionEvent event) -> {
//            DataBase db = DataBase.getInstance();
//            if (db.setAccountIndex(workspaceController.user_id, USERNAME.getText(), PASSWORD.getText())) {
                main.closeLoginStage();
                main.mainWindow();
//            }
        });

        CLOSE.setOnAction(event -> main.closeLoginStage());
    }

}
