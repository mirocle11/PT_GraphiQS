package Controllers;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class framingController implements Initializable {

    @FXML
    private AnchorPane rootpane,firstpane;
    private AnchorPane secondpane;

    @FXML
    private ImageView first,second;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/framing_2.fxml"));
            secondpane = loader.load();
            rootpane.getChildren().addAll(secondpane);
            secondpane.setLayoutY(120);
            AnchorPane.setBottomAnchor(secondpane,0.0);
            AnchorPane.setTopAnchor(secondpane,120.0);
            secondpane.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void next() {
        firstpane.setVisible(false);
        secondpane.setVisible(true);

        first.setImage(new Image("Views/images/1_circle_25px.png"));
        second.setImage(new Image("Views/images/2_circle_c_25px.png"));
    }

    public void back() {
        firstpane.setVisible(true);
        secondpane.setVisible(false);

        first.setImage(new Image("Views/images/1_circle_c_25px.png"));
        second.setImage(new Image("Views/images/2_circle_25px.png"));
    }

}