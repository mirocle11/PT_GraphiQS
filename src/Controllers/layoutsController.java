package Controllers;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class layoutsController implements Initializable {

    public BorderPane layoutsPane;
    public JFXTreeTableView layoutsTable;
    public ScrollPane scrollPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVmax(0.0);
    }



}