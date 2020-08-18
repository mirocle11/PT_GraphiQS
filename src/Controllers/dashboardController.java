package Controllers;

import Main.Main;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class dashboardController implements Initializable {

    @FXML
    public AnchorPane rootpane,dashboard_max,dashboard_min;
    private AnchorPane jobInfo, workspace;
    private BorderPane layouts;
    private AnchorPane sheets;
    private BorderPane builder;

    @FXML
    private Label time,date;

    //dashboard indicator
    private int i = 0;

    private Main main;
    private Stage stage;

    public void setMain(Stage stage, Main main) {
        this.main = main;
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/jobInfo.fxml"));
            jobInfo = loader.load();
            rootpane.getChildren().addAll(jobInfo);
            jobInfo.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/workspace.fxml"));
            workspace = loader.load();
            rootpane.getChildren().addAll(workspace);
            workspace.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/layouts.fxml"));
            layouts = loader.load();
            rootpane.getChildren().addAll(layouts);
            layouts.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/setupSheets.fxml"));
            sheets = loader.load();
            rootpane.getChildren().addAll(sheets);
            sheets.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/builder.fxml"));
            builder = loader.load();
            rootpane.getChildren().addAll(builder);
            builder.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    private void setJobInfo() {
        AnchorPane.setRightAnchor(jobInfo,0.0);
        AnchorPane.setTopAnchor(jobInfo,0.0);
        AnchorPane.setBottomAnchor(jobInfo,0.0);

        if (i == 0) {
            AnchorPane.setLeftAnchor(jobInfo,265.0);
        } else {
            AnchorPane.setLeftAnchor(jobInfo,65.0);
        }
        jobInfo.setVisible(true);
        workspace.setVisible(false);
        layouts.setVisible(false);
        sheets.setVisible(false);
        builder.setVisible(false);
    }

    private void setWorkspace() {
        AnchorPane.setRightAnchor(workspace,0.0);
        AnchorPane.setTopAnchor(workspace,0.0);
        AnchorPane.setBottomAnchor(workspace,0.0);

        if (i == 0) {
            AnchorPane.setLeftAnchor(workspace,265.0);
        } else {
            AnchorPane.setLeftAnchor(workspace,65.0);
        }
        jobInfo.setVisible(false);
        workspace.setVisible(true);
        layouts.setVisible(false);
        sheets.setVisible(false);
        builder.setVisible(false);
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
        jobInfo.setVisible(false);
        workspace.setVisible(false);
        layouts.setVisible(true);
        sheets.setVisible(false);
        builder.setVisible(false);
    }

    private void setSetupSheets() {
        AnchorPane.setRightAnchor(sheets,0.0);
        AnchorPane.setTopAnchor(sheets,0.0);
        AnchorPane.setBottomAnchor(sheets,0.0);

        if (i == 0) {
            AnchorPane.setLeftAnchor(sheets,265.0);
        } else {
            AnchorPane.setLeftAnchor(sheets,65.0);
        }
        jobInfo.setVisible(false);
        workspace.setVisible(false);
        layouts.setVisible(false);
        sheets.setVisible(true);
        builder.setVisible(false);
    }

    private void setBuilder() {
        AnchorPane.setRightAnchor(builder,0.0);
        AnchorPane.setTopAnchor(builder,0.0);
        AnchorPane.setBottomAnchor(builder,0.0);

        if (i == 0) {
            AnchorPane.setLeftAnchor(builder,265.0);
        } else {
            AnchorPane.setLeftAnchor(builder,65.0);
        }
        jobInfo.setVisible(false);
        workspace.setVisible(false);
        layouts.setVisible(false);
        sheets.setVisible(false);
        builder.setVisible(true);
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

    public void jobInfoAction() {
        setJobInfo();
    }

    public void workspaceAction() {
        setWorkspace();
    }

    public void layoutsAction() {
        setLayouts();
    }

    public void sheetsAction() {
        setSetupSheets();
    }

    public void builderAction() {
        setBuilder();
    }

    public void toggle() {
        toggleDashBoard();
        if (jobInfo.isVisible()) {
            setJobInfo();
        }
        if (workspace.isVisible()) {
            setWorkspace();
        }
        if (layouts.isVisible()) {
            setLayouts();
        }
        if (sheets.isVisible()) {
            setSetupSheets();
        }
        if (builder.isVisible()) {
            setBuilder();
        }
    }

}
