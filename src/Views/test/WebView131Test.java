package Views.test;//import com.mohamnag.fxwebview_debugger.DevToolsDebuggerServer;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class WebView131Test extends Application {
    public static void main(String[] args) {
        System.setProperty("http.proxyHost", "www-proxy.idc.oracle.com");
        System.setProperty("http.proxyPort", "80");
        System.setProperty("https.proxyHost", "www-proxy.idc.oracle.com");
        System.setProperty("https.proxyPort", "80");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
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