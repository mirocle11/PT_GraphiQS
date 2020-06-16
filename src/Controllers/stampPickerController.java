package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import service.Tools;

import java.net.URL;
import java.util.ResourceBundle;

public class stampPickerController implements Initializable {

    public ListView<ImageView> iconList;
    public JFXButton DONE;
    public JFXComboBox<String> DOOR_TYPE;
    public JFXComboBox<String> DOOR_HEIGHT;
    public JFXComboBox<String> COLOR;

    public JFXButton UNDO, REDO;

    public static Image[] iconImages;  // The little images that can be "stamped".
    public static int iconNumber = 0;

    private ObservableList<String> door_type = FXCollections.observableArrayList( "Sgl Cav", "Dbl Cav",
            "Priv Cav", "Sgl B-fold to 900", "Sgl B-fold to 901-1200", "Dbl B-fold to 1200", "Dbl B-fold to 1201-1600",
            "Dbl B-fold to 1601-1800", "Dbl B-fold to 1801-2400");
    private ObservableList<String> door_height = FXCollections.observableArrayList( "510", "560", "610", "660",
            "710", "760", "810", "860", "1200", "1600", "1800", "1980", "2000", "2100", "2200", "2400");
    private ObservableList<String> colors = FXCollections.observableArrayList( "Red", "Green", "Blue");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DOOR_TYPE.setItems(door_type);
        DOOR_HEIGHT.setItems(door_height);
        COLOR.setItems(colors);

        COLOR.setOnAction(event -> {
            String[] iconNames = new String[] { // names of image resource file, in directory stamper_icons
                    "icon1.png", "icon2.png", "icon3.png", "icon4.png", "icon5.png", "icon6.png"
            };

            iconImages = new Image[iconNames.length];
            iconList.setPrefWidth(50);    // The default pref width is much too wide,
            iconList.setPrefHeight(246);  // The default pref height is 400, which is taller than the canvas,
            //    which would force the height of the BorderPane to be too big.

            String icon_url = null;
            if (!COLOR.getSelectionModel().isEmpty()) {
                String color = COLOR.getSelectionModel().getSelectedItem();
                switch (color) {
                    case "Green":
                        icon_url = "../Views/stamper_icons/green/";
                        break;
                    case "Red":
                        icon_url = "../Views/stamper_icons/red/";
                        break;
                    case "Blue":
                        icon_url = "../Views/stamper_icons/blue/";
                        break;
                }
            }

            iconList.getItems().clear();
            for (int i = 0; i < iconNames.length; i++) {
                Image icon = new Image(getClass().getResourceAsStream(icon_url + iconNames[i]));
                iconImages[i] = icon;
                iconList.getItems().add( new ImageView(icon));
            }
            iconList.getSelectionModel().select(0);  // The first item in the list is currently selected.
        });

        DONE.setOnAction(event -> {
//            workspaceController.stampPickerStage.close();
        });

        iconList.setOnMouseClicked(event -> {
            iconNumber = iconList.getSelectionModel().getSelectedIndex();
            System.out.println(iconNumber);
//            workspaceController.setStampMode(1);
        });

    }

}
