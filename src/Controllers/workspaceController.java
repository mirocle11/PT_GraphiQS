package Controllers;

import Controllers.Sheets.Shed.foundationsController;
import DataBase.DataBase;
import Model.PageObject;
import Model.ShapeObject;
import Model.data.WallData;
import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerNextArrowBasicTransition;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.Tools;

import javax.swing.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static Controllers.Sheets.Shed.externalFramingController.externalFramingSectionListGirts;

public class workspaceController implements Initializable {

    //buttons
    public JFXButton IMPORT_PDF, IMPORT_PDF_1, SAVE, SCALE, LENGTH, AREA, CLADDING_BTN, STAMP, ROTATE, structureToggle;
    public JFXHamburger hamburger;

    //combo box (for length selection)
    public JFXComboBox structureComboBox, wallTypeComboBox, wallComboBox, materialComboBox;
    public JFXColorPicker colorPicker;

    //checkbox
    public JFXCheckBox selectAllBox;

    //containers
    public GridPane gridPane, innerGridPane;
    public AnchorPane mainPane, frontPane, structurePane, loadingPane;
    public ScrollPane scroller, structureScrollPane;
    public Group scrollContent, group;
    public StackPane zoomPane;
    public Canvas canvas;
    public Pane pane;
    public JFXDrawer drawer;
    public VBox structureBox;
    public VBox toolsMenu;
    public Label toastLabel;
    public JFXButton PREVIOUS_PAGE, NEXT_PAGE;

    //collections
    List<Shape> shapeList = new ArrayList<>();
    ArrayList<Point2D> pointList = new ArrayList<>();
    ArrayList<Shape> stampList = new ArrayList<>();
    ArrayList<PageObject> pageObjects = new ArrayList<>();
    ArrayList<double[][]> snapList = new ArrayList<>();
    public ArrayList<ShapeObject> shapeObjList = new ArrayList<>();

    //scale
    double SCALE_DELTA = 1.1;
    double m_Scale = 0;

    //Snap Function
    public Image fxImage;
    Color color;
    double snapX = -1;
    double snapY = -1;

    //shapes
    Line line = new Line();
    Rectangle rect = new Rectangle();
    Circle circle = new Circle();
    Label noScaleLabel = new Label();

    //booleans
    private boolean isNew = true;
    private boolean canDraw = true;

    //indicator
    private int bool = 0;

    //static classes
    static Tools tools;
    static WallData wallData;

    //sheets list
    public static ObservableList<String> selectedBoxes = FXCollections.observableArrayList();
    public ObservableList<String> newItemsList = FXCollections.observableArrayList();

    //user_id
    public static int user_id = 0;
    public static int client_id = 0;

    //stamp (doors)
    public AnchorPane stampPicker;

    public ListView<ImageView> iconList;

    public int ds_indicator = 0;
    public Canvas stamp_canvas;

    public JFXButton DONE;
    public JFXComboBox<String> STAMP_TYPE, DOOR_TYPE, DOOR_HEIGHT, DOOR_WIDTH;

    public JFXButton UNDO, REDO;

    public Image[] iconImages;  // The little images that can be "stamped".

    //sample data
    private final ObservableList<String> stamp_type = FXCollections.observableArrayList("Foundations",
            "External Framing");
    private final ObservableList<String> door_type = FXCollections.observableArrayList( "Sgl Cav", "Dbl Cav",
            "Priv Cav", "Sgl B-fold to 900", "Sgl B-fold to 901-1200", "Dbl B-fold to 1200", "Dbl B-fold to 1201-1600",
            "Dbl B-fold to 1601-1800", "Dbl B-fold to 1801-2400");
    private final ObservableList<String> door_width = FXCollections.observableArrayList( "510", "560", "610",
            "660", "710", "760", "810", "860", "1200", "1600", "1800");
    private final ObservableList<String> door_height = FXCollections.observableArrayList("1980", "2000", "2100",
            "2200", "2400");

    public Pane DOOR_PANE, WINDOW_PANE, FOUNDATIONS_PANE, EXTERNAL_FRAMING_PANE;

    //stamp (windows)
    public int ws_indicator = 0;
    public JFXTextField WINDOW_NO, CLADDING, WIDTH, HEIGHT;
    public JFXComboBox<String> WINDOW_TYPE;
    private final ObservableList<String> window_type = FXCollections.observableArrayList( "Exterior", "Interior");

    //stamp (foundations)
    public int fs_indicator = 0;
    public JFXComboBox<String> FOUNDATIONS_PART, FOUNDATIONS_SET, FOUNDATIONS_MATERIAL;
    public JFXTextField FOUNDATIONS_DEPTH, FOUNDATIONS_WIDTH, FOUNDATIONS_LENGTH, FOUNDATIONS_DIAMETER,
            FOUNDATIONS_HEIGHT, FOUNDATIONS_VOLUME1, FOUNDATIONS_VOLUME2;
    public TextArea FOUNDATIONS_NOTES;

    private final ObservableList<String> foundations_parts = FXCollections.observableArrayList("Post Footings",
            "Pole Footings");
    public ObservableList<String> foundations_setLists = FXCollections.observableArrayList();

    //stamp (external framing)
    public int ef_indicator = 0;
    public JFXComboBox<String> EXTERNAL_FRAMING_PART, EXTERNAL_FRAMING_SET, EXTERNAL_FRAMING_MATERIAL;
    public JFXTextField EXTERNAL_FRAMING_LENGTH;
    public TextArea EXTERNAL_FRAMING_NOTES;

    private final ObservableList<String> external_framing_parts = FXCollections.observableArrayList("Poles",
            "Columns");
    public ObservableList<String> external_framing_setLists = FXCollections.observableArrayList();
    public ObservableList<String> external_framing_materialLists = FXCollections.observableArrayList();

    public static String selectedMaterial = "";

    //set stud height
    public double stud_height;
    public int sh_indicator = 0;

    //cladding
    public VBox CLADDING_BOX;
    public JFXComboBox<String> CLADDING_METHOD;
    private final ObservableList<String> cladding_method = FXCollections.observableArrayList("Measure", "Input");

    public JFXButton CLD_PROCEED, CLD_CANCEL;
    public AnchorPane CLADDING_LIST;
    public Pane CLD_INPUT_PANE;
    public JFXColorPicker CLD_COLOR_PICKER;
    public String selectedCladding;
    int cld_indicator = 0;

    //misc --
    public AnchorPane MISCELLANEOUS_PICKER, MISC_CONCRETE_FLOOR, MISC_GIRTS;
    public JFXButton MISCELLANEOUS_DONE, MISCELLANEOUS_CLOSE;
    public JFXComboBox<String> MISC_PART, MISC_SHED_TYPE, MISC_CF_SET, MISC_GIRTS_SET, MISC_GIRTS_MATERIAL;
    public ObservableList<String> misc_parts = FXCollections.observableArrayList("Concrete Floor", "Girts");
    public ObservableList<String> shed_type_parts = FXCollections.observableArrayList("Enclosed", "Open");

    //foundations
    public JFXTextField CONCRETE_FLOOR_LENGTH, CONCRETE_FLOOR_WIDTH, CONCRETE_FLOOR_THICKNESS;
    public Label CONCRETE_FLOOR_LENGTH_ERROR, CONCRETE_FLOOR_WIDTH_ERROR, CONCRETE_FLOOR_THICKNESS_ERROR;
    //girts
    public JFXTextField GIRTS_LENGTH, GIRTS_DEPTH, GIRTS_REAR_HEIGHT, GIRTS_NO, GIRTS_TOTAL, GIRTS_NO_OF_BAYS,
            GIRTS_BAY_SPACING;
    public Label GIRTS_LENGTH_ERROR, GIRTS_WIDTH_ERROR, GIRTS_REAR_HEIGHT_ERROR;

    public ObservableList<String> concrete_floor_setList = FXCollections.observableArrayList();

    public ObservableList<String> girts_setList = FXCollections.observableArrayList();
    public ObservableList<String> girts_materialList = FXCollections.observableArrayList();

    //notes
    public static String selectedNoteStructure;
    public static String selectedNotePart;

    private static Stage addNotesStage;
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //stamping combo boxes
        STAMP_TYPE.setItems(stamp_type);
        DOOR_TYPE.setItems(door_type);
        DOOR_WIDTH.setItems(door_width);
        DOOR_HEIGHT.setItems(door_height);
        WINDOW_TYPE.setItems(window_type);
        FOUNDATIONS_PART.setItems(foundations_parts);
        EXTERNAL_FRAMING_PART.setItems(external_framing_parts);
        CLADDING_METHOD.setItems(cladding_method);
        FOUNDATIONS_SET.setItems(foundations_setLists);
        EXTERNAL_FRAMING_SET.setItems(external_framing_setLists);
        EXTERNAL_FRAMING_MATERIAL.setItems(external_framing_materialLists);
        MISC_CF_SET.setItems(concrete_floor_setList);
        MISC_GIRTS_SET.setItems(girts_setList);
        MISC_GIRTS_MATERIAL.setItems(girts_materialList);

        //misc
        MISC_PART.setItems(misc_parts);
        MISC_SHED_TYPE.setItems(shed_type_parts);

        FOUNDATIONS_PART.setOnAction(event -> {
            if (FOUNDATIONS_PART.getSelectionModel().getSelectedItem().equals("Post Footings")) {
                FOUNDATIONS_DEPTH.setVisible(true);
                FOUNDATIONS_WIDTH.setVisible(true);
                FOUNDATIONS_LENGTH.setVisible(true);
                FOUNDATIONS_VOLUME1.setVisible(true);

                FOUNDATIONS_DIAMETER.setVisible(false);
                FOUNDATIONS_HEIGHT.setVisible(false);
                FOUNDATIONS_VOLUME2.setVisible(false);

                FOUNDATIONS_DIAMETER.setText("");
                FOUNDATIONS_HEIGHT.setText("");

                DataBase db = DataBase.getInstance();
                db.getSets(1, foundations_setLists);
            } else if (FOUNDATIONS_PART.getSelectionModel().getSelectedItem().equals("Pole Footings")) {
                FOUNDATIONS_DEPTH.setVisible(false);
                FOUNDATIONS_WIDTH.setVisible(false);
                FOUNDATIONS_LENGTH.setVisible(false);
                FOUNDATIONS_VOLUME1.setVisible(false);

                FOUNDATIONS_DEPTH.setText("");
                FOUNDATIONS_WIDTH.setText("");
                FOUNDATIONS_LENGTH.setText("");

                FOUNDATIONS_DIAMETER.setVisible(true);
                FOUNDATIONS_HEIGHT.setVisible(true);
                FOUNDATIONS_VOLUME2.setVisible(true);

                DataBase db = DataBase.getInstance();
                db.getSets(2, foundations_setLists);
            }
        });

        FOUNDATIONS_VOLUME1.textProperty().bind(Bindings.createStringBinding(()-> {
            //Do your calculation
            double volume = 0;
            if (!FOUNDATIONS_WIDTH.getText().isEmpty() && !FOUNDATIONS_DEPTH.getText().isEmpty() &&
                    !FOUNDATIONS_LENGTH.getText().isEmpty()) {
                double width = Double.parseDouble(FOUNDATIONS_WIDTH.getText()) / 1000;
                double length = Double.parseDouble(FOUNDATIONS_LENGTH.getText()) / 1000;
                double depth = Double.parseDouble(FOUNDATIONS_DEPTH.getText()) / 1000;

                volume = length * width * depth;
            }
            //Return result as String
            return String.valueOf(volume);
        }, FOUNDATIONS_WIDTH.textProperty(), FOUNDATIONS_DEPTH.textProperty(), FOUNDATIONS_LENGTH.textProperty()));

        FOUNDATIONS_VOLUME2.textProperty().bind(Bindings.createStringBinding(()->{
            //Do your calculations
            double volume = 0;
            DecimalFormat df = new DecimalFormat("0.00");
            if (!FOUNDATIONS_DIAMETER.getText().isEmpty() && !FOUNDATIONS_HEIGHT.getText().isEmpty()) {
                double radius = Double.parseDouble(FOUNDATIONS_DIAMETER.getText()) / 1000 / 2;
                volume = radius * radius * 3.14 * (Double.parseDouble(FOUNDATIONS_HEIGHT.getText()) / 1000);
            }
            //Return result as String
            return String.valueOf(df.format(volume));
        }, FOUNDATIONS_DIAMETER.textProperty(), FOUNDATIONS_HEIGHT.textProperty()));

        DONE.setOnAction(event -> { //stamping
            stampPicker.setVisible(false);
            DOOR_PANE.setVisible(false);
            WINDOW_PANE.setVisible(false);

            ws_indicator = 0;
            ds_indicator = 0;
            fs_indicator = 0;

            if (!STAMP_TYPE.getSelectionModel().isEmpty()) {
                STAMP_TYPE.getSelectionModel().clearSelection();
            }
        });

        EXTERNAL_FRAMING_PART.setOnAction(event -> {
            if (!EXTERNAL_FRAMING_PART.getSelectionModel().isEmpty())
                if (EXTERNAL_FRAMING_PART.getSelectionModel().getSelectedItem().equals("Poles")) {
                    DataBase db = DataBase.getInstance();
                    db.getSets(4, external_framing_setLists);
                } else if (EXTERNAL_FRAMING_PART.getSelectionModel().getSelectedItem().equals("Columns")) {
                    DataBase db = DataBase.getInstance();
                    db.getSets(5, external_framing_setLists);
                }
        });

        EXTERNAL_FRAMING_SET.setOnAction(event -> {
            if (!EXTERNAL_FRAMING_SET.getSelectionModel().isEmpty()) {
                DataBase db = DataBase.getInstance();
                db.getSetMaterials(EXTERNAL_FRAMING_SET.getSelectionModel().getSelectedItem(),
                        external_framing_materialLists);
            }
        });

        MISC_PART.setOnAction(event -> {
            if (!MISC_PART.getSelectionModel().isEmpty())
                if (MISC_PART.getSelectionModel().getSelectedItem().equals("Concrete Floor")) {
                    MISC_CONCRETE_FLOOR.setVisible(true);
                    MISC_GIRTS.setVisible(false);
                    DataBase db = DataBase.getInstance();
                    db.getSets(3, concrete_floor_setList);
                } else if (MISC_PART.getSelectionModel().getSelectedItem().equals("Girts")) {
                    MISC_CONCRETE_FLOOR.setVisible(false);
                    MISC_GIRTS.setVisible(true);
                    DataBase db = DataBase.getInstance();
                    db.getSets(6, girts_setList);
                }
        });

        MISC_GIRTS_SET.setOnAction(event -> {
            if (!MISC_GIRTS_SET.getSelectionModel().getSelectedItem().isEmpty()) {
                DataBase db = DataBase.getInstance();
                db.getSetMaterials(MISC_GIRTS_SET.getSelectionModel().getSelectedItem(), girts_materialList);
            }
        });

        GIRTS_LENGTH.textProperty().bind(Bindings.createStringBinding(()-> {
            //Do your calculation
            double length = 0;
            DecimalFormat df = new DecimalFormat("0.00");
            if (!GIRTS_NO_OF_BAYS.getText().isEmpty() && !GIRTS_BAY_SPACING.getText().isEmpty()) {
                double bay_spacing = Double.parseDouble(GIRTS_BAY_SPACING.getText());
                length = Integer.parseInt(GIRTS_NO_OF_BAYS.getText()) * bay_spacing;
            }
            //Return result as String
            return String.valueOf(df.format(length));
        }, GIRTS_NO_OF_BAYS.textProperty(), GIRTS_BAY_SPACING.textProperty()));

        GIRTS_NO.textProperty().bind(Bindings.createStringBinding(()-> {
            //Do your calculation
            int qty = 0;

            if (!GIRTS_REAR_HEIGHT.getText().isEmpty()) {
                double rear_height = Double.parseDouble(GIRTS_REAR_HEIGHT.getText());
                qty = (int) (rear_height - 300) / 1000;
            }
            //Return result as String
            return String.valueOf(qty);
        }, GIRTS_REAR_HEIGHT.textProperty()));

        GIRTS_TOTAL.textProperty().bind(Bindings.createStringBinding(()-> {
            //Do your calculation
            double total = 0;
            DecimalFormat df = new DecimalFormat("0.00");
            if (!GIRTS_LENGTH.getText().isEmpty() && !GIRTS_DEPTH.getText().isEmpty() &&
                    !MISC_SHED_TYPE.getSelectionModel().isEmpty()) {
                if (MISC_SHED_TYPE.getSelectionModel().getSelectedItem().equals("Enclosed")) {
                    double length = Double.parseDouble(GIRTS_LENGTH.getText());
                    double width = Double.parseDouble(GIRTS_DEPTH.getText());

                    total = (length + width) * 2;
                } else if (MISC_SHED_TYPE.getSelectionModel().getSelectedItem().equals("Open")) {
                    double length = Double.parseDouble(GIRTS_LENGTH.getText());
                    double width = Double.parseDouble(GIRTS_DEPTH.getText());

                    total = length + (width * 2);
                }
            }
            //Return result as String
            return String.valueOf(df.format(total));
        }, GIRTS_LENGTH.textProperty(), GIRTS_DEPTH.textProperty()));

        MISCELLANEOUS_DONE.setOnAction(event -> {
            if (!MISC_PART.getSelectionModel().isEmpty()) {
                if (MISC_PART.getSelectionModel().getSelectedItem().equals("Concrete Floor")) {
                    try {
                        double length = Double.parseDouble(CONCRETE_FLOOR_LENGTH.getText()) / 1000;
                        double width = Double.parseDouble(CONCRETE_FLOOR_WIDTH.getText()) / 1000;
                        double thickness = Double.parseDouble(CONCRETE_FLOOR_THICKNESS.getText()) / 1000;

                        layoutsController.concreteData.add(0, Double.toString(length * 1000));
                        layoutsController.concreteData.add(1, Double.toString(width * 1000));
                        layoutsController.concreteData.add(2, Double.toString(thickness * 1000));

                        double volume = length * width * thickness;
                        double area = length * width;

                        layoutsController.concreteData.add(3, Double.toString(Double.parseDouble(new DecimalFormat
                                ("0.00").format(volume))));
                        layoutsController.concreteData.add(4, Double.toString(Double.parseDouble(new DecimalFormat
                                ("0.00").format(area))));

                        foundationsController.cf_area = area;
                        foundationsController.cf_length = (int) length;
                        foundationsController.cf_volume = volume;

                        //if there are values
                        if (length > 0 && width > 0 && thickness > 0) {
                            DataBase db = DataBase.getInstance();
                            db.setSections(3, 1);
                            db.setSelectedSets(3, 1, MISC_CF_SET.getSelectionModel().getSelectedItem(),
                                    "", "");
                            foundationsController.foundationsCFSectionList.clear();
                            foundationsController.foundationsCFSectionList.add(1);
                        }

                        CONCRETE_FLOOR_LENGTH_ERROR.setVisible(false);
                        CONCRETE_FLOOR_WIDTH_ERROR.setVisible(false);
                        CONCRETE_FLOOR_THICKNESS_ERROR.setVisible(false);
                        CONCRETE_FLOOR_LENGTH.setUnFocusColor(Paint.valueOf("#b9b9b9"));
                        CONCRETE_FLOOR_WIDTH.setUnFocusColor(Paint.valueOf("#b9b9b9"));
                        CONCRETE_FLOOR_THICKNESS.setUnFocusColor(Paint.valueOf("#b9b9b9"));
                        MISCELLANEOUS_PICKER.setVisible(false);

                    } catch (Exception e) {
                        try {
                            double lenght = Double.parseDouble(CONCRETE_FLOOR_LENGTH.getText());
                            CONCRETE_FLOOR_LENGTH.setUnFocusColor(Paint.valueOf("#b9b9b9"));
                            CONCRETE_FLOOR_LENGTH_ERROR.setVisible(false);
                        } catch (Exception a) {
                            CONCRETE_FLOOR_LENGTH.setUnFocusColor(Paint.valueOf("#ff5148"));
                            CONCRETE_FLOOR_LENGTH_ERROR.setVisible(true);
                        }
                        try {
                            double width = Double.parseDouble(CONCRETE_FLOOR_WIDTH.getText());
                            CONCRETE_FLOOR_WIDTH.setUnFocusColor(Paint.valueOf("#b9b9b9"));
                            CONCRETE_FLOOR_WIDTH_ERROR.setVisible(false);
                        } catch (Exception a) {
                            CONCRETE_FLOOR_WIDTH.setUnFocusColor(Paint.valueOf("#ff5148"));
                            CONCRETE_FLOOR_WIDTH_ERROR.setVisible(true);
                        }
                        try {
                            double thickness = Double.parseDouble(CONCRETE_FLOOR_THICKNESS.getText());
                            CONCRETE_FLOOR_THICKNESS.setUnFocusColor(Paint.valueOf("#b9b9b9"));
                            CONCRETE_FLOOR_THICKNESS_ERROR.setVisible(false);
                        } catch (Exception a) {
                            CONCRETE_FLOOR_THICKNESS.setUnFocusColor(Paint.valueOf("#ff5148"));
                            CONCRETE_FLOOR_THICKNESS_ERROR.setVisible(true);
                        }
                    }
                } else if (MISC_PART.getSelectionModel().getSelectedItem().equals("Girts")) {
                    try {
                        // layouts and db function
                        layoutsController.girtsData.add(0, GIRTS_NO_OF_BAYS.getText());
                        layoutsController.girtsData.add(1, GIRTS_BAY_SPACING.getText());
                        layoutsController.girtsData.add(2, GIRTS_LENGTH.getText());
                        layoutsController.girtsData.add(3, GIRTS_DEPTH.getText());
                        layoutsController.girtsData.add(4, GIRTS_REAR_HEIGHT.getText());
                        layoutsController.girtsData.add(5, GIRTS_NO.getText());
                        layoutsController.girtsData.add(6, GIRTS_TOTAL.getText());

                        DataBase db = DataBase.getInstance();
                        db.setSections(6, 1);
                        db.setSelectedSets(6, 1, MISC_GIRTS_SET.getSelectionModel().getSelectedItem(),
                                "", MISC_GIRTS_MATERIAL.getSelectionModel().getSelectedItem());
                        externalFramingSectionListGirts.clear();
                        externalFramingSectionListGirts.add(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        MISCELLANEOUS_CLOSE.setOnAction(event -> {
            CONCRETE_FLOOR_LENGTH.setText("");
            CONCRETE_FLOOR_WIDTH.setText("");
            CONCRETE_FLOOR_THICKNESS.setText("");
            MISCELLANEOUS_PICKER.setVisible(false);
        });

        wallData = new WallData(this);
        wallData.structureAction();
        wallData.wallsAction();
        wallData.typeAction();
        wallData.choicesAction();

        STAMP_TYPE.setOnAction(event -> {
            if (!STAMP_TYPE.getSelectionModel().isEmpty()) {
                switch (STAMP_TYPE.getSelectionModel().getSelectedItem()) {
                    case "Doors":
                        DOOR_PANE.setVisible(true);
                        WINDOW_PANE.setVisible(false);

                        ds_indicator = 1;
                        ws_indicator = 0;
                        fs_indicator = 0;

                        iconList = createIconList();
                        stampPicker.getChildren().add(iconList);
                        break;
                    case "Windows":
                        DOOR_PANE.setVisible(false);
                        WINDOW_PANE.setVisible(true);
                        iconList.setVisible(false);

                        ds_indicator = 0;
                        ws_indicator = 1;
                        fs_indicator = 0;

                        UNDO.setVisible(false);
                        REDO.setVisible(false);
                        break;
                    case "Foundations":
                        DOOR_PANE.setVisible(false);
                        WINDOW_PANE.setVisible(false);
                        FOUNDATIONS_PANE.setVisible(true);
                        EXTERNAL_FRAMING_PANE.setVisible(false);

                        ds_indicator = 0;
                        ws_indicator = 0;
                        fs_indicator = 1;
                        ef_indicator = 0;

                        iconList = createIconList();
                        stampPicker.getChildren().add(iconList);
                        break;
                    case "External Framing":
                        DOOR_PANE.setVisible(false);
                        WINDOW_PANE.setVisible(false);
                        FOUNDATIONS_PANE.setVisible(false);
                        EXTERNAL_FRAMING_PANE.setVisible(true);

                        ds_indicator = 0;
                        ws_indicator = 0;
                        fs_indicator = 0;
                        ef_indicator = 1;

                        iconList = createIconList();
                        stampPicker.getChildren().add(iconList);
                        break;
                }
            }
        });

        //cladding
        CLADDING_BTN.setOnAction(event -> {
            CLADDING_LIST.setVisible(true);
        });

        CLADDING_METHOD.setOnAction(event -> {
            if (!CLADDING_METHOD.getSelectionModel().isEmpty())
                if (CLADDING_METHOD.getSelectionModel().getSelectedItem().equals("Measure")) {
                    CLADDING_BOX.setVisible(true);
                    CLD_COLOR_PICKER.setVisible(true);
                    CLD_INPUT_PANE.setVisible(false);
                } else if (CLADDING_METHOD.getSelectionModel().getSelectedItem().equals("Input")) {
                    CLADDING_BOX.setVisible(false);
                    CLD_COLOR_PICKER.setVisible(false);
                    CLD_INPUT_PANE.setVisible(true);
                }
        });

        CLD_PROCEED.setOnAction(event -> {
                setSelectedCladding();
                claddingAction();
                CLADDING_LIST.setVisible(false);
        });

        CLD_CANCEL.setOnAction(event -> {
            CLADDING_LIST.setVisible(false);
        });

        unselectAllAction();
        setCladdingIndicator();

        tools = new Tools(this);
        tools.setMode("FREE");

        line.setVisible(false);
        line.setOpacity(.7);
        line.setStrokeLineCap(StrokeLineCap.BUTT);
        pane.getChildren().add(circle);
        circle.setFill(Color.RED);
        circle.setRadius(5);
        pane.getChildren().add(line);

        noScaleLabel.setText("The page is not scaled. No measurements can be taken.");
        noScaleLabel.setTextFill(Color.RED);

        scroller.viewportBoundsProperty().addListener((observable, oldValue, newValue) ->
                zoomPane.setMinSize(newValue.getWidth(), newValue.getHeight()));

        try {
            VBox box = FXMLLoader.load(getClass().getResource("/Views/workspaceSideNavigation.fxml"));
            drawer.setSidePane(box);

            HamburgerNextArrowBasicTransition burgerTask = new HamburgerNextArrowBasicTransition(hamburger);
            burgerTask.setRate(-1);
            hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
                burgerTask.setRate(burgerTask.getRate() * -1);
                burgerTask.play();

                if (drawer.isOpened()) {
                    drawer.close();
                    drawer.toBack();
                } else {
                    drawer.toggle();
                    drawer.toFront();
                }
            });
        } catch (Exception e) {
            Logger.getLogger(workspaceController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void showToast(String message) {
        toastLabel.setVisible(true);
        toastLabel.setText(message);

        FadeTransition toastTransition = new FadeTransition(Duration.seconds(2), toastLabel);
        toastTransition.setToValue(2);
        toastTransition.setFromValue(0);
        toastTransition.play();

        toastTransition.setOnFinished(event -> {
            FadeTransition toastTransition1 = new FadeTransition(Duration.seconds(4), toastLabel);
            toastTransition1.setToValue(0);
            toastTransition1.setFromValue(1);
            toastTransition1.play();
        });
    }

    public void openFile() {
        tools.open();
    }

    static void getSelectedStructures(ObservableList<String> selectedStructure) {
        selectedBoxes = selectedStructure;
    }

    public void setStructures() {
        structureComboBox.getItems().clear();
        structureComboBox.setItems(selectedBoxes);
        System.out.println(selectedBoxes);
    }

    public void connectStructures() {
        structureBox.getChildren().forEach((Node node) -> {
            createProjectController.list.forEach((String s) -> {
                if (((JFXCheckBox) node).getText().equals(s)) {
                    ((JFXCheckBox) node).setSelected(true);
                }
            });
        });
    }

    public void saveFile() {
        tools.save();
    }

    //==SIDE BUTTON ACTIONS
    public void scaleAction() {
        tools.setMode("SCALE");
    }

    public void scaledIndicator(int indicator) {
        Image i = new Image(getClass().getResourceAsStream("../Views/images/ruler_40px.png"));
        Image ii = new Image(getClass().getResourceAsStream("../Views/images/scaled_40px.png"));

        if (indicator == 0) {
            SCALE.setGraphic(new ImageView(i));
        } else {
            SCALE.setGraphic(new ImageView(ii));
        }

    }

    public void areaAction() {
        isNew = true;
        canDraw = true;

        tools.setColor(colorPicker.getValue());
        tools.setMode("AREA");
    }

    public void lengthAction() {
        isNew = true;
        canDraw = true;

        tools.setColor(colorPicker.getValue());
        tools.setMode("LENGTH");
    }

    public void claddingAction() {
        isNew = true;
        canDraw = true;

        tools.setColor(colorPicker.getValue());
        tools.setMode("CLADDING");
        stamp_canvas.setVisible(false);
    }

    public void rotateAction() {
        group.setRotate(group.getRotate() + 90);
    }

    public void nextPageAction() {
        tools.nextPage();
    }

    public void previousPageAction() {
        tools.previousPage();
    }

    public void stampAction() {
        stampPicker.setVisible(true);
    }

    private ListView<ImageView> createIconList() {
        String[] iconNames = new String[] { // names of image resource file, in directory stamper_icons
                "blue-icon1.png", "blue-icon2.png", "blue-icon3.png", "blue-icon4.png", "blue-icon5.png", "blue-icon6.png",
                "green-icon1.png", "green-icon2.png", "green-icon3.png", "green-icon4.png", "green-icon5.png", "green-icon6.png",
                "red-icon1.png", "red-icon2.png", "red-icon3.png", "red-icon4.png", "red-icon5.png", "red-icon6.png"
        };

        iconImages = new Image[iconNames.length];

        ListView<ImageView> list = new ListView<>();

        list.setPrefWidth(57);
        list.setPrefHeight(246);
        list.setLayoutX(240);
        list.setLayoutY(65);

        String icon_url = "../Views/stamper_icons/";

        for (int i = 0; i < iconNames.length; i++) {
            Image icon = new Image(getClass().getResourceAsStream(icon_url + iconNames[i]));
            iconImages[i] = icon;
            list.getItems().add(new ImageView(icon));
        }

        list.getSelectionModel().select(0);  //The first item in the list is currently selected.
        return list;
    }

    //stud_height
    public void setStudHeight() {
        try {
            stud_height = Float.parseFloat(JOptionPane.showInputDialog("Enter whole Stud Height",
                    0.00 + " mm"));
            System.out.println("whole stud height is: " + stud_height);
            sh_indicator++;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number.",
                    "Invalid Stud height value", JOptionPane.ERROR_MESSAGE);
            sh_indicator--;
        }
    }

    //Todo ->  Should be a service or in a thread safe environment
    public void updateLine(MouseEvent event) {
        try {
            for (double[][] arr : snapList) {

                int a = (int) arr[0][0] - (int) event.getX();
                int b = (int) arr[0][1] - (int) event.getY();

                if ((a >= -3 && a <= 3) && (b >= -3 && b <= 3)) {
                    circle.setCenterX(arr[0][0]);
                    circle.setCenterY(arr[0][1]);
                    circle.setVisible(true);
                    circle.setRadius(3 / group.getScaleY());
                    circle.setOnMouseExited(event1 -> {
                        circle.setVisible(false);
                    });
                    circle.setOnMouseReleased(event1 -> {
                        if (canDraw) {
                            if (snapX == -1 && snapY == -1) {
                                snapX = circle.getCenterX();
                                snapY = circle.getCenterY();
                            }
                        }
                    });
                    pane.getChildren().add(circle);
                    break;
                } else {
                    circle.setVisible(false);
                }
            }
        } catch (Exception ex) {
        }

        Point2D cP = clamp(event.getX(), event.getY());
        if (!isNew) {
            if (event.isShiftDown()) {
                if (cP.getX() < cP.getY()) {
                    line.setEndX(line.getStartX());
                    line.setEndY(cP.getY());
                } else {
                    line.setEndX(cP.getX());
                    line.setEndY(line.getStartY());
                }
                int xdiff = (int) Math.abs(line.getStartX() - cP.getX());
                int ydiff = (int) Math.abs(line.getStartY() - cP.getY());
                if (xdiff < ydiff) {
                    line.setEndX(line.getStartX());
                    line.setEndY(cP.getY());
                } else {
                    line.setEndX(cP.getX());
                    line.setEndY(line.getStartY());
                }
            } else {
                line.setEndX(cP.getX());
                line.setEndY(cP.getY());
            }
        }
    }

    public void zoom(ScrollEvent event) {
        if (event.getDeltaY() == 0) {
            return;
        }
        double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA : 1 / SCALE_DELTA;
        // amount of scrolling in each direction in scrollContent coordinate
        // units
        double newScale = group.getScaleX() * scaleFactor;
        if (event.isControlDown()) {
            if (newScale > .2 && newScale < 3.7) {
                group.setScaleX(newScale);
                group.setScaleY(newScale);
                Point2D scrollOffset = figureScrollOffset(scrollContent, scroller);
                repositionScroller(scrollContent, scroller, scaleFactor, scrollOffset);

                pane.getChildren().forEach(node -> {
                    if (node.getClass().getSuperclass() == Shape.class) {
                        Shape sp = (Shape) node;
                        if (sp.getClass() == Rectangle.class) {
                            Rectangle rect = (Rectangle) sp;
                            double centerX = rect.getX() + rect.getWidth() / 2;
                            double centerY = rect.getY() + rect.getHeight() / 2;
                            rect.setStrokeWidth(4 / group.getScaleY());
                            rect.setX(centerX - 5 / group.getScaleY());
                            rect.setY(centerY - 5 / group.getScaleY());
                            rect.setWidth(10 / group.getScaleY());
                            rect.setHeight(10 / group.getScaleY());
                        } else {
                            sp.setStrokeWidth(8 / group.getScaleY());
                        }
                    }
                    if (node.getClass() == Label.class) {
                        Label lbl = (Label) node;
                        lbl.setFont(new Font("Arial", 18 / group.getScaleY()));
                    }
                });
            }
        }
    }

    private Point2D figureScrollOffset (Node scrollContent, ScrollPane scroller) {
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        double hScrollProportion = (scroller.getHvalue() - scroller.getHmin()) / (scroller.getHmax() - scroller.getHmin());
        double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        double vScrollProportion = (scroller.getVvalue() - scroller.getVmin()) / (scroller.getVmax() - scroller.getVmin());
        double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);
        return new Point2D(scrollXOffset, scrollYOffset);
    }

    private void repositionScroller (Node scrollContent, ScrollPane scroller, double scaleFactor, Point2D scrollOffset) {
        double scrollXOffset = scrollOffset.getX();
        double scrollYOffset = scrollOffset.getY();
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        if (extraWidth > 0) {
            double halfWidth = scroller.getViewportBounds().getWidth() / 2;
            double newScrollXOffset = (scaleFactor - 1) * halfWidth + scaleFactor * scrollXOffset;
            scroller.setHvalue(scroller.getHmin() + newScrollXOffset * (scroller.getHmax() - scroller.getHmin()) / extraWidth);
        } else {
            scroller.setHvalue(scroller.getHmin());
        }
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        if (extraHeight > 0) {
            double halfHeight = scroller.getViewportBounds().getHeight() / 2;
            double newScrollYOffset = (scaleFactor - 1) * halfHeight + scaleFactor * scrollYOffset;
            scroller.setVvalue(scroller.getVmin() + newScrollYOffset * (scroller.getVmax() - scroller.getVmin()) / extraHeight);
        } else {
            scroller.setHvalue(scroller.getHmin());
        }
    }

    public Point2D clamp(double x, double y) {
        double maxX = canvas.getBoundsInLocal().getMaxX();
        double maxY = canvas.getBoundsInLocal().getMaxY();
        double a = Math.max(0, x);
        double b = Math.max(0, y);
        double c = Math.min(a, maxX);
        double d = Math.min(b, maxY);
        Point2D p = new Point2D(c, d);
        return p;
    }

    public void handlePan(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            scroller.setPannable(false);
        } else {
            scroller.setPannable(true);
        }
    }

    //Measurement popups
    public void structureListTransition() {
        structureBox.getChildren().forEach(node -> {
            if (!((JFXCheckBox)node).isSelected()) {
                selectAllBox.setSelected(false);
            }
        });

        if (bool == 0) {
            structurePane.setVisible(true);
            structurePane.setDisable(true);
            structureToggle.setDisable(true);
            TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.2), structurePane);
            translateTransition1.setByX(+313);
            translateTransition1.play();
            translateTransition1.setOnFinished(event1 -> {
                structurePane.setDisable(false);
                structureToggle.setDisable(false);
            });
            bool++;
        } else {
            closeMeasurementList();
        }
    }

    public void closeMeasurementList() {
        structurePane.setDisable(true);
        structureToggle.setDisable(true);
        TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.2), structurePane);
        translateTransition1.setByX(-313);
        translateTransition1.play();
        translateTransition1.setOnFinished(event1 -> {
            structurePane.setDisable(false);
            structurePane.setVisible(false);
            structureToggle.setDisable(false);
        });
        bool--;

        //clears the structure list
        structureBox.getChildren().forEach( node -> {
            ((JFXCheckBox) node).setSelected(false);
        });

        //updates the selected structures on top list
        structureBox.getChildren().forEach((Node node) -> {
            structureComboBox.getItems().forEach(o -> {
                if (((JFXCheckBox) node).getText().equals(o)) {
                    ((JFXCheckBox) node).setSelected(true);
                }
            });
        });
    }

    public void selectAll() {
        if (selectAllBox.isSelected()) {
            structureBox.getChildren().forEach(node -> {
                ((JFXCheckBox) node).setSelected(true);
            });
        } else {
            structureBox.getChildren().forEach(node -> ((JFXCheckBox) node).setSelected(false));
        }
    }

    private void unselectAllAction() {
        structureBox.getChildren().forEach(node -> {
            ((JFXCheckBox) node).setOnAction(event -> {
                if (!((JFXCheckBox) node).isSelected()) {
                    selectAllBox.setSelected(false);
                }
            });
        });
    }

    public void saveStructures() {
        //updates the selected structures on top list
        newItemsList.clear();
        structureBox.getChildren().forEach(node -> {
            if (((JFXCheckBox) node).isSelected()) {
                String newItems = ((JFXCheckBox) node).getText();
                newItemsList.add(newItems);
                structureComboBox.setItems(newItemsList);
            }
        });

        structurePane.setDisable(true);
        structureToggle.setDisable(true);
        TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.2), structurePane);
        translateTransition1.setByX(-313);
        translateTransition1.play();
        translateTransition1.setOnFinished(event1 -> {
            structurePane.setDisable(false);
            structurePane.setVisible(false);
            structureToggle.setDisable(false);
        });
        bool--;
    }

    public void colorPickerAction() {
        wallData.colorPickerAction();
    }

    //cladding list picker
    public void setSelectedCladding() {
        CLADDING_BOX.getChildren().forEach(node -> {
            if (((JFXCheckBox) node).isSelected()) {
                selectedCladding = ((JFXCheckBox) node).getText();
            }
        });
    }

    public void setCladdingIndicator() {
        CLADDING_BOX.getChildren().forEach(node -> {
            ((JFXCheckBox) node).setOnAction(event -> {
                if (((JFXCheckBox) node).isSelected()) {
                    cld_indicator = 1;
                } else {
                    cld_indicator = 0;
                }
            });
        });
    }

    //Miscellaneous
    public void miscellaneousAction() {
        MISCELLANEOUS_PICKER.setVisible(true);
    }

    public void clearMisc() {
        MISC_PART.getSelectionModel().clearSelection();
        CONCRETE_FLOOR_LENGTH.setText("");
        CONCRETE_FLOOR_THICKNESS.setText("");
        CONCRETE_FLOOR_WIDTH.setText("");

        GIRTS_LENGTH.setText("");
        GIRTS_DEPTH.setText("");
        GIRTS_REAR_HEIGHT.setText("");
    }
}