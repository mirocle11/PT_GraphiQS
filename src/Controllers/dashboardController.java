package Controllers;

import Main.Main;
import Controllers.workspaceController;
import com.jfoenix.controls.JFXButton;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class dashboardController implements Initializable {

    @FXML
    private AnchorPane rootpane,dashboard_max,dashboard_min;
    private AnchorPane workspace,layouts;

    @FXML
    private Label time,date;

    private int i = 0; //dashboard indicator

    private Main main;
    private Stage stage;

    public void setMain(Stage stage, Main main) {
        this.main = main;
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/workspace.fxml"));
            workspace = loader.load();
            rootpane.getChildren().addAll(workspace);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/layouts.fxml"));
            layouts = loader.load();
            rootpane.getChildren().addAll(layouts);
            layouts.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setWorkspace();
        initClock();
    }

    private void toggleDashBoard() {
        if (i == 0) {
            TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.1), dashboard_max);
            translateTransition1.setByX(-265);
            dashboard_min.setVisible(true);
            translateTransition1.play();
            i++;
        } else {
            TranslateTransition translateTransition2 = new TranslateTransition(Duration.seconds(0.1), dashboard_max);
            dashboard_min.setVisible(false);
            translateTransition2.setByX(+265);
            translateTransition2.play();
            i--;
        }
    }

    private void setWorkspace() {
        if (i == 0) {
            workspace.setTranslateX(270);
        } else {
            workspace.setTranslateX(70);
        }
        AnchorPane.setLeftAnchor(workspace,0.0);
        AnchorPane.setRightAnchor(workspace,0.0);
        AnchorPane.setTopAnchor(workspace,0.0);
        AnchorPane.setBottomAnchor(workspace,0.0);
        workspace.setVisible(true);
        layouts.setVisible(false);
    }

    private void setLayouts() {
        if (i == 0) {
            layouts.setTranslateX(265);
        } else {
            layouts.setTranslateX(65);
        }
        AnchorPane.setLeftAnchor(layouts,0.0);
        AnchorPane.setRightAnchor(layouts,0.0);
        AnchorPane.setTopAnchor(layouts,0.0);
        AnchorPane.setBottomAnchor(layouts,0.0);
        workspace.setVisible(false);
        layouts.setVisible(true);
}

    private void initClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
        DateTimeFormatter timeformat = DateTimeFormatter.ofPattern("hh:mm:ss");
        DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        time.setText(LocalDateTime.now().format(timeformat));
        date.setText(LocalDateTime.now().format(dateformat));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public void workspaceAction(ActionEvent actionEvent) {
        setWorkspace();
    }

    public void layoutsAction(ActionEvent actionEvent) {
        setLayouts();
    }

    public void toggle(ActionEvent actionEvent) {
        toggleDashBoard();
        if (workspace.isVisible()) {
            setWorkspace();
        }
        if (layouts.isVisible()) {
            setLayouts();
        }
    }
}
