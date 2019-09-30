package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class floorsController implements Initializable {

    @FXML
    private AnchorPane rootpane,subfloor;
    private AnchorPane ground_floor, first_floor, second_floor, third_floor, fourth_floor, fifth_floor, sixth_floor, seventh_floor;

    @FXML
    private Label floor_indicator;

    //floor indicator
    private int i = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/ground_floor.fxml"));
            ground_floor = loader.load();
            rootpane.getChildren().addAll(ground_floor);
            ground_floor.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/first_floor.fxml"));
            first_floor = loader.load();
            rootpane.getChildren().addAll(first_floor);
            first_floor.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/second_floor.fxml"));
            second_floor = loader.load();
            rootpane.getChildren().addAll(second_floor);
            second_floor.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/third_floor.fxml"));
            third_floor = loader.load();
            rootpane.getChildren().addAll(third_floor);
            third_floor.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/fourth_floor.fxml"));
            fourth_floor = loader.load();
            rootpane.getChildren().addAll(fourth_floor);
            fourth_floor.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/fifth_floor.fxml"));
            fifth_floor = loader.load();
            rootpane.getChildren().addAll(fifth_floor);
            fifth_floor.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/sixth_floor.fxml"));
            sixth_floor = loader.load();
            rootpane.getChildren().addAll(sixth_floor);
            sixth_floor.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/seventh_floor.fxml"));
            seventh_floor = loader.load();
            rootpane.getChildren().addAll(seventh_floor);
            seventh_floor.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void floorIndicator() {
        int ii = 0;
        if (ground_floor.isVisible()) {
            ii = 1;
        } else if (first_floor.isVisible()) {
            ii = 2;
        } else if (second_floor.isVisible()) {
            ii = 3;
        } else if (third_floor.isVisible()) {
            ii = 4;
        } else if (fourth_floor.isVisible()) {
            ii = 5;
        } else if (fifth_floor.isVisible()) {
            ii = 6;
        } else if (sixth_floor.isVisible()) {
            ii = 7;
        } else if (seventh_floor.isVisible()) {
            ii = 8;
        }
        floor_indicator.setText(ii + "/" + i);
    }

    public void addFloor(ActionEvent actionEvent) {
        if (i < 8) {
            i++;
        }
        floorIndicator();
    }

    public void removeFloor(ActionEvent actionEvent) {
        switch (i) {
            case 8:
                if (seventh_floor.isVisible()) {
                    seventh_floor.setVisible(false);
                    sixth_floor.setVisible(true);
                }
                break;
            case 7:
                if (sixth_floor.isVisible()) {
                    sixth_floor.setVisible(false);
                    fifth_floor.setVisible(true);
                }
                break;
            case 6:
                if (fifth_floor.isVisible()) {
                    fifth_floor.setVisible(false);
                    fourth_floor.setVisible(true);
                }
                break;
            case 5:
                if (fourth_floor.isVisible()) {
                    fourth_floor.setVisible(false);
                    third_floor.setVisible(true);
                }
                break;
            case 4:
                if (third_floor.isVisible()) {
                    third_floor.setVisible(false);
                    second_floor.setVisible(true);
                }
                break;
            case 3:
                if (second_floor.isVisible()) {
                    second_floor.setVisible(false);
                    first_floor.setVisible(true);
                }
                break;
            case 2:
                if (first_floor.isVisible()) {
                    first_floor.setVisible(false);
                    ground_floor.setVisible(true);
                }
                break;
            case 1:
                if (ground_floor.isVisible()) {
                    ground_floor.setVisible(false);
                    subfloor.setVisible(true);
                }
                break;
            default:
                break;
        }
        if (i > 0) {
            i--;
        }
        floorIndicator();
    }

    public void next(ActionEvent actionEvent) {
        if (i != 0) if (i <= 1) {
            if (subfloor.isVisible()) {
                subfloor.setVisible(false);
                ground_floor.setVisible(true);
            }
        } else if (i <= 2) {
            if (ground_floor.isVisible()) {
                ground_floor.setVisible(false);
                first_floor.setVisible(true);
            }
            if (subfloor.isVisible()) {
                subfloor.setVisible(false);
                ground_floor.setVisible(true);
            }
        } else if (i <= 3) {
            if (first_floor.isVisible()) {
                first_floor.setVisible(false);
                second_floor.setVisible(true);
            }
            if (ground_floor.isVisible()) {
                ground_floor.setVisible(false);
                first_floor.setVisible(true);
            }
            if (subfloor.isVisible()) {
                subfloor.setVisible(false);
                ground_floor.setVisible(true);
            }
        } else if (i <= 4) {
            if (second_floor.isVisible()) {
                second_floor.setVisible(false);
                third_floor.setVisible(true);
            }
            if (first_floor.isVisible()) {
                first_floor.setVisible(false);
                second_floor.setVisible(true);
            }
            if (ground_floor.isVisible()) {
                ground_floor.setVisible(false);
                first_floor.setVisible(true);
            }
            if (subfloor.isVisible()) {
                subfloor.setVisible(false);
                ground_floor.setVisible(true);
            }

        } else if (i <= 5) {
            if (third_floor.isVisible()) {
                third_floor.setVisible(false);
                fourth_floor.setVisible(true);
            }
            if (second_floor.isVisible()) {
                second_floor.setVisible(false);
                third_floor.setVisible(true);
            }
            if (first_floor.isVisible()) {
                first_floor.setVisible(false);
                second_floor.setVisible(true);
            }
            if (ground_floor.isVisible()) {
                ground_floor.setVisible(false);
                first_floor.setVisible(true);
            }
            if (subfloor.isVisible()) {
                subfloor.setVisible(false);
                ground_floor.setVisible(true);
            }
        } else if (i <= 6) {
            if (fourth_floor.isVisible()) {
                fourth_floor.setVisible(false);
                fifth_floor.setVisible(true);
            }
            if (third_floor.isVisible()) {
                third_floor.setVisible(false);
                fourth_floor.setVisible(true);
            }
            if (second_floor.isVisible()) {
                second_floor.setVisible(false);
                third_floor.setVisible(true);
            }
            if (first_floor.isVisible()) {
                first_floor.setVisible(false);
                second_floor.setVisible(true);
            }
            if (ground_floor.isVisible()) {
                ground_floor.setVisible(false);
                first_floor.setVisible(true);
            }
            if (subfloor.isVisible()) {
                subfloor.setVisible(false);
                ground_floor.setVisible(true);
            }
        } else if (i <= 7) {
            if (fifth_floor.isVisible()) {
                fifth_floor.setVisible(false);
                sixth_floor.setVisible(true);
            }
            if (fourth_floor.isVisible()) {
                fourth_floor.setVisible(false);
                fifth_floor.setVisible(true);
            }
            if (third_floor.isVisible()) {
                third_floor.setVisible(false);
                fourth_floor.setVisible(true);
            }
            if (second_floor.isVisible()) {
                second_floor.setVisible(false);
                third_floor.setVisible(true);
            }
            if (first_floor.isVisible()) {
                first_floor.setVisible(false);
                second_floor.setVisible(true);
            }
            if (ground_floor.isVisible()) {
                ground_floor.setVisible(false);
                first_floor.setVisible(true);
            }
            if (subfloor.isVisible()) {
                subfloor.setVisible(false);
                ground_floor.setVisible(true);
            }
        } else if (i <= 8) {
            if (sixth_floor.isVisible()) {
                sixth_floor.setVisible(false);
                seventh_floor.setVisible(true);
            }
            if (fifth_floor.isVisible()) {
                fifth_floor.setVisible(false);
                sixth_floor.setVisible(true);
            }
            if (fourth_floor.isVisible()) {
                fourth_floor.setVisible(false);
                fifth_floor.setVisible(true);
            }
            if (third_floor.isVisible()) {
                third_floor.setVisible(false);
                fourth_floor.setVisible(true);
            }
            if (second_floor.isVisible()) {
                second_floor.setVisible(false);
                third_floor.setVisible(true);
            }
            if (first_floor.isVisible()) {
                first_floor.setVisible(false);
                second_floor.setVisible(true);
            }
            if (ground_floor.isVisible()) {
                ground_floor.setVisible(false);
                first_floor.setVisible(true);
            }
            if (subfloor.isVisible()) {
                subfloor.setVisible(false);
                ground_floor.setVisible(true);
            }
        }
        floorIndicator();
    }

    public void back(ActionEvent actionEvent) {
        if (seventh_floor.isVisible()) {
            seventh_floor.setVisible((false));
            sixth_floor.setVisible(true);
        }else if (sixth_floor.isVisible()) {
            sixth_floor.setVisible(false);
            fifth_floor.setVisible(true);
        } else if (fifth_floor.isVisible()) {
            fifth_floor.setVisible(false);
            fourth_floor.setVisible(true);
        } else if (fourth_floor.isVisible()) {
            fourth_floor.setVisible(false);
            third_floor.setVisible(true);
        } else if (third_floor.isVisible()) {
            third_floor.setVisible(false);
            second_floor.setVisible(true);
        } else if (second_floor.isVisible()) {
            second_floor.setVisible(false);
            first_floor.setVisible(true);
        } else if (first_floor.isVisible()) {
            first_floor.setVisible(false);
            ground_floor.setVisible(true);
        } else if (ground_floor.isVisible()) {
            ground_floor.setVisible(false);
            subfloor.setVisible(true);
        }
        floorIndicator();
    }

}
