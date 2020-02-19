package Views;

/**
 * Created by User on 22/01/2020.
 */

import javafx.application.Application;
        import javafx.scene.Scene;
        import javafx.scene.layout.VBox;
        import javafx.scene.web.WebView;
        import javafx.stage.Stage;

public class test extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX WebView Example");

        WebView webView = new WebView();

        webView.getEngine().load("file:///C:/Users/User/Documents/Projects/2559-PRV_SCHOLTZ%20RESIDENCE_vc1.0.2_17%20JAN%2020.pdf");

        VBox vBox = new VBox(webView);
        Scene scene = new Scene(vBox, 960, 600);

        primaryStage.setScene(scene);
        primaryStage.show();

    }
}