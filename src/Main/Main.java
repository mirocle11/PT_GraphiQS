package Main;

import Controllers.dashboardController;
import Controllers.splashScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public static Stage stage, dashboard_stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        splashScreenWindow();
    }

    private void splashScreenWindow() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/splashScreen.fxml"));
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

    public void mainWindow() {

        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("../Views/dashboard.fxml"));
            AnchorPane pane = loader.load();

            Scene scene = new Scene(pane);
            dashboardController controller = loader.getController();
            controller.setMain(dashboard_stage,this);
            scene.getStylesheets().addAll(Main.class.getResource("../Views/CSS/style.css").toExternalForm());
            dashboard_stage = new Stage();
            dashboard_stage.setScene(scene);
            dashboard_stage.show();
            dashboard_stage.setTitle("GraphiQS");
            dashboard_stage.setMinWidth(900);
            dashboard_stage.setMinHeight(600);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeStage() {
        stage.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
