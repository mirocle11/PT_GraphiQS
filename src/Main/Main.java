package Main;

import Controllers.builderController;
import Controllers.dashboardController;
import Controllers.loginController;
import Controllers.splashScreenController;
import DataBase.DataBase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;

public class Main extends Application {

    public static Stage stage, dashboard_stage, loginStage;
    public Scene dashboardScene, loginScene;

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) {
        DataBase db = DataBase.getInstance();
        db.createDatabase();
        stage = primaryStage;
        splashScreenWindow();
    }

    private void splashScreenWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/splashScreen.fxml"));
            AnchorPane pane = loader.load();
            splashScreenController splashScreenController = loader.getController();
            splashScreenController.main(this);

            Scene scene = new Scene(pane);
            scene.setFill(Color.TRANSPARENT);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setTitle("Please wait..");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loginWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/Views/loginForm.fxml"));
            AnchorPane pane = loader.load();

            loginScene = new Scene(pane);
            loginController controller = loader.getController();
            controller.setMain(loginStage,this);
            loginScene.getStylesheets().addAll(Main.class.getResource("/Views/CSS/style.css").toExternalForm());
            loginStage = new Stage();
            loginStage.setScene(loginScene);

            loginScene.setFill(Color.TRANSPARENT);
            loginStage.initStyle(StageStyle.UNDECORATED);
            loginStage.initStyle(StageStyle.TRANSPARENT);

            //draggable pop up
            pane.setOnMousePressed(event1 -> {
                xOffset = event1.getSceneX();
                yOffset = event1.getSceneY();
            });

            pane.setOnMouseDragged(event1 -> {
                loginStage.setX(event1.getScreenX() - xOffset);
                loginStage.setY(event1.getScreenY() - yOffset);
            });

            loginStage.show();
            loginStage.setTitle("GraphiQS");
            loginStage.setMinWidth(726);
            loginStage.setMinHeight(519);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/Views/dashboard.fxml"));
            AnchorPane pane = loader.load();

            dashboardScene = new Scene(pane);
            dashboardController controller = loader.getController();
            controller.setMain(dashboard_stage,this);
            dashboardScene.getStylesheets().addAll(Main.class.getResource("/Views/CSS/style.css").toExternalForm());
            dashboard_stage = new Stage();
            dashboard_stage.setScene(dashboardScene);
            dashboard_stage.show();
            dashboard_stage.setTitle("GraphiQS");
            dashboard_stage.setMinWidth(1275);
            dashboard_stage.setMinHeight(700);
            dashboard_stage.setMaximized(true);
            dashboard_stage.setOnCloseRequest(event -> {
                int n = JOptionPane.showConfirmDialog(null, "You have unsaved progress, " +
                        "do you want to exit?", "Info", JOptionPane.YES_NO_OPTION);
                if (n == 0) {
                    DataBase db = DataBase.getInstance();
                    db.clearComponents();
//                    db.clearSectionDimensions();
                    db.clearLayoutsFoundations();
                    dashboard_stage.close();
                }
                else {
                    event.consume();
                }
                System.out.println(n);
            });

            DataBase db = DataBase.getInstance();
            db.displayClients(builderController.clientsData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeStage() {
        stage.close();
    }

    public void closeLoginStage() {
        loginStage.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
