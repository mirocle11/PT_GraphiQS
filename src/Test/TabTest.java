package Test;

import com.jfoenix.controls.JFXTabPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TabTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private AnchorPane tabContent;

    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setMinWidth(800);
        anchorPane.setMinHeight(600);
        anchorPane.setStyle("-fx-background-color: #102027;");

        TabPane tabPane = new TabPane();
        anchorPane.getChildren().add(tabPane);

        AnchorPane.setLeftAnchor(tabPane,0.0);
        AnchorPane.setBottomAnchor(tabPane,0.0);
        AnchorPane.setTopAnchor(tabPane,0.0);
        AnchorPane.setRightAnchor(tabPane,50.0);

        Tab tab = new Tab();
        tab.setContent(createTabContent());
        tab.setText("Sample Tab");
        tabPane.getTabs().add(tab);

        Button button = new Button();
        button.setMinWidth(45);
        button.setMinHeight(30);
        button.setText("Add Tab");
        anchorPane.getChildren().add(button);

        AnchorPane.setRightAnchor(button, 10.0);

        button.setOnAction(event -> {
            Tab tab1 = new Tab();
            tab1.setText("Another Tab");
            tab1.setContent(createTabContent());
            tabPane.getTabs().add(tab1);
        });

        primaryStage.setScene(new Scene(anchorPane));
        primaryStage.getScene().getStylesheets().add("/Views/CSS/style.css");
        primaryStage.setResizable(false);
        primaryStage.setTitle("Sample TabPane");
        primaryStage.show();
    }

    public AnchorPane createTabContent() {
        tabContent = new AnchorPane();

        VBox vBox = new VBox();
        vBox.setStyle("-fx-background-color: DARKGRAY;");
        vBox.setMinWidth(150);
        vBox.setMinHeight(450);
        tabContent.getChildren().add(vBox);

        AnchorPane.setLeftAnchor(vBox,10.0);
        AnchorPane.setTopAnchor(vBox,30.0);

        return tabContent;
    }

}