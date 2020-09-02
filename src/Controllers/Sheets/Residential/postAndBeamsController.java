package Controllers.Sheets.Residential;

import DataBase.DataBase;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class postAndBeamsController implements Initializable {

    public ObservableList<String> parts = FXCollections.observableArrayList();
    public VBox PARTS_VBOX;

    public GridPane POSTS, POST_BRACKETS, BEAM_N_PLATES, BEAM_CONNECTORS;

    public ArrayList<GridPane> gridPaneList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        loadPanes();

        DataBase db = DataBase.getInstance();
        parts.clear();
        db.displayParts(17, parts);

        parts.forEach(s -> {
            JFXButton button = new JFXButton(s);
            button.setPrefWidth(183);
            button.setPrefHeight(30);
            button.setFont(new Font("Segoe UI",15.0));
            button.setTextFill(Color.web("#bcbcbc"));
            PARTS_VBOX.getChildren().add(button);

            button.setOnAction(event -> {
                gridPaneList.forEach(gridPane -> {
                    gridPane.setVisible(false);
                    if (button.getText().equals(gridPane.getId())) {
                        gridPane.setVisible(true);
                    }
                });
            });
        });
    }

    public void loadPanes() {
        POSTS.setId("Posts");
        POST_BRACKETS.setId("Post Brackets");
        BEAM_N_PLATES.setId("Beam N/Plates");
        BEAM_CONNECTORS.setId("Beam Connectors");

        gridPaneList.add(POSTS);
        gridPaneList.add(POST_BRACKETS);
        gridPaneList.add(BEAM_N_PLATES);
        gridPaneList.add(BEAM_CONNECTORS);
    }

}
