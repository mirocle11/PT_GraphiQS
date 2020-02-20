package Views.test;

/**
 * Created by User on 22/01/2020.
 */

import javafx.application.Application;
        import javafx.scene.Scene;
        import javafx.scene.web.WebEngine;
        import javafx.scene.web.WebView;
        import javafx.stage.Stage;

public class hala extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.setOnError(event -> System.err.println(event.getMessage()));
        webEngine.setOnAlert(event -> System.err.println(event.getData()));

        webEngine.setJavaScriptEnabled(true);
        webEngine.load("https://mozilla.github.io/pdf.js/web/viewer.html");

        Scene scene = new Scene(browser);
        primaryStage.setScene(scene);

        primaryStage.show();



    }

}
