package Controllers;

import Main.Main;
import javafx.animation.*;
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
            dashboard_max.setDisable(true);
            TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.2), dashboard_max);
            translateTransition1.setByX(-265);
            translateTransition1.play();
            translateTransition1.setOnFinished(event -> {
                TranslateTransition translateTransition2 = new TranslateTransition(Duration.seconds(0.2), dashboard_min);
                translateTransition2.setByX(+65);
                translateTransition2.play();
                translateTransition2.setOnFinished(event1 -> dashboard_min.setDisable(false));
            });
            i++;
        } else {
            dashboard_min.setDisable(true);
            TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.2), dashboard_min);
            translateTransition1.setByX(-65);
            translateTransition1.play();
            translateTransition1.setOnFinished(event -> {
                TranslateTransition translateTransition2 = new TranslateTransition(Duration.seconds(0.2), dashboard_max);
                translateTransition2.setByX(+265);
                translateTransition2.play();
                translateTransition2.setOnFinished(event1 -> dashboard_max.setDisable(false));
            });
            i--;
        }
    }

    private void setWorkspace() {
        AnchorPane.setRightAnchor(workspace,0.0);
        AnchorPane.setTopAnchor(workspace,0.0);
        AnchorPane.setBottomAnchor(workspace,0.0);

        if (i == 0) {
            AnchorPane.setLeftAnchor(workspace,270.0);
        } else {
            AnchorPane.setLeftAnchor(workspace,70.0);
        }
        workspace.setVisible(true);
        layouts.setVisible(false);
    }

    private void setLayouts() {
        AnchorPane.setRightAnchor(layouts,0.0);
        AnchorPane.setTopAnchor(layouts,0.0);
        AnchorPane.setBottomAnchor(layouts,0.0);

        if (i == 0) {
            AnchorPane.setLeftAnchor(layouts,265.0);
        } else {
            AnchorPane.setLeftAnchor(layouts,65.0);
        }
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

    public void workspaceAction() {
        setWorkspace();
    }

    public void layoutsAction() {
        setLayouts();
    }

    public void toggle() {
        toggleDashBoard();
        if (workspace.isVisible()) {
            setWorkspace();
        }
        if (layouts.isVisible()) {
            setLayouts();
        }
    }
}
