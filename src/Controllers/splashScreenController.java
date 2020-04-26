package Controllers;

import Main.Main;
import com.jfoenix.controls.JFXSpinner;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;

public class splashScreenController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label loadingLabel;

    @FXML
    private Label waitLabel;

    @FXML
    private Pane titlePane;

    @FXML
    private JFXSpinner spinner;

    Main main;
    Stage stage;

    public void main(Main main) {
        this.main = main;
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TranslateTransition loadingLabelTransition = new TranslateTransition(Duration.seconds(0.5), loadingLabel);
        loadingLabelTransition.setByY(700);
        loadingLabelTransition.play();

        TranslateTransition titlePaneTransition = new TranslateTransition(Duration.seconds(0.5), titlePane);
        titlePaneTransition.setByY(700);
        titlePaneTransition.play    ();

        TranslateTransition waitLabelTransition = new TranslateTransition(Duration.seconds(0.5), waitLabel);
        waitLabelTransition.setByY(700);
        waitLabelTransition.play();

        loadingLabelTransition.setOnFinished(event -> {
            loadingLabel.setVisible(true);

            TranslateTransition loadingLabelTransition1 = new TranslateTransition(Duration.seconds(1), loadingLabel);
            loadingLabelTransition1.setByY(-700);
            loadingLabelTransition1.play();

            loadingLabelTransition1.setOnFinished(event1 -> {

                titlePane.setVisible(true);

                TranslateTransition titlePaneTransition1 = new TranslateTransition(Duration.seconds(0.5), titlePane);
                titlePaneTransition1.setByY(-700);
                titlePaneTransition1.play();

                titlePaneTransition1.setOnFinished(event2 -> {
                    waitLabel.setVisible(true);

                    TranslateTransition waitLabelTransition1 = new TranslateTransition(Duration.seconds(0.5), waitLabel);
                    waitLabelTransition1.setByY(-700);
                    waitLabelTransition1.play();

                    waitLabelTransition1.setOnFinished(event3 -> {
                        spinner.setVisible(true);

                        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3), spinner);
                        fadeTransition.setFromValue(0);
                        fadeTransition.setToValue(3);
                        fadeTransition.play();

                        fadeTransition.setOnFinished(event4 -> {

                            FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(2), rootPane);
                            fadeTransition1.setFromValue(1);
                            fadeTransition1.setToValue(0.1);
                            fadeTransition1.play();

                            fadeTransition1.setOnFinished(event5 -> {
                                main.closeStage();
                                main.loginWindow();
                            });
                        });
                    });
                });
            });
        });
    }
}
