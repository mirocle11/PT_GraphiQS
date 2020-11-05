package service;

import Controllers.createProjectController;
import Controllers.layoutsController;
import Controllers.setupSheetsController;
import Controllers.workspaceController;
import DataBase.DataBase;
import Model.CladdingObject;
import Model.PageObject;
import Model.ShapeObject;
import Model.data.layouts.claddingData;
import Model.data.layouts.doorData;
import Model.data.layouts.layoutsData;
import Model.data.layouts.windowData;
import Model.stamps.DoorObject;
import Model.stamps.ExternalFramingObject;
import Model.stamps.FoundationsObject;
import Model.stamps.WindowObject;
import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;

import javax.swing.*;
import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static Controllers.Sheets.Shed.foundationsController.foundationsCBSectionList;
import static Controllers.Sheets.Shed.foundationsController.foundationsPFSectionList;

public class Tools {
    public PageObject page;
    Group group;
    String mode;
    Pane pane;
    Canvas canvas;
    Line line;
    Circle circle;
    Label noScale;
    VBox box;
    ScrollPane scroller;
    AnchorPane mainPane, frontPane, loadingPane;
    GridPane gridPane, innerGridPane;
    List shape = new ArrayList();
    Color color;
    ContextMenu contextMenu, stampMenu;
    int pageNumber = 0;
    static boolean canDraw = false;
    boolean issetCanvas = false;

    JFXButton LENGTH, AREA;

    public workspaceController window;
    public layoutsController layouts;
    List<Shape> shapeList = new ArrayList<>();
    ArrayList<Point2D> pointList = new ArrayList<>();
    ArrayList<Shape> stampList = new ArrayList<>();
    ArrayList<PageObject> pageObjects = new ArrayList<>();
    ArrayList<double[][]> snapList = new ArrayList<>();
    public ArrayList<ShapeObject> shapeObjList = new ArrayList<>();
    public ArrayList<CladdingObject> claddingObjectList = new ArrayList<>();
    public ArrayList<FoundationsObject> foundationsObjectList = new ArrayList<>();

    double total;
    double stud_height;
    public Label rate = new Label();

    int window_no = 1;
    int door_no = 1;

    //layouts foundations
    public Label foundations_pf_section = new Label();
    public Label foundations_pf_volume = new Label("");
    public Label foundations_pf_quantity = new Label("");
    public Label pfLastRowSection = new Label();

    public Label foundations_cb_section = new Label();
    public Label foundations_cb_volume = new Label("");
    public Label foundations_cb_quantity = new Label("");
    public Label cbLastRowSection = new Label();

    //layouts external framing
    public Label external_framing_section = new Label();
    public Label external_framing_quantity = new Label();
    public Label efLastRowSection = new Label();

    public Tools(PageObject page, Group group, String mode, Line line, Circle circle, VBox box) {
        this.page = page;
        this.group = group;
        this.mode = mode;
        this.line = line;
        this.circle = circle;
        group.getChildren().forEach(node -> {
            if (node.getClass().equals(Pane.class)) {
                this.pane = (Pane) node;
            } else if (node.getClass().equals(Canvas.class)) {
                this.canvas = (Canvas) node;
            }
        });
        this.noScale = new Label();
        this.noScale.setText("The page is not scaled. No measurements can be taken.");
        this.noScale.setTextFill(Color.RED);
        this.box = box;
        setMode(this.mode);
    }

    public Tools(Group group, String mode, Line line, Circle circle, VBox box) {
        this.group = group;
        this.mode = mode;
        this.line = line;
        this.circle = circle;
        group.getChildren().forEach(node -> {
            if (node.getClass().equals(Pane.class)) {
                this.pane = (Pane) node;
            } else if (node.getClass().equals(Canvas.class)) {
                this.canvas = (Canvas) node;
            }
        });
        this.noScale = new Label();
        this.noScale.setText("The page is not scaled. No measurements can be taken.");
        this.noScale.setTextFill(Color.RED);
        this.box = box;
        setMode(this.mode);
    }

    public Tools(workspaceController window) {
        this.window = window;
        this.canvas = window.canvas;
        this.pane = window.pane;
        this.scroller = window.scroller;
        this.mainPane = window.mainPane;
        this.frontPane = window.frontPane;
        this.loadingPane = window.loadingPane;
        this.gridPane = window.gridPane;
        this.innerGridPane = window.innerGridPane;
        this.box = window.toolsMenu;
        this.group = window.group;
        this.LENGTH = window.LENGTH;
        this.AREA = window.AREA;
        this.contextMenu = new ContextMenu();
        this.stampMenu = new ContextMenu();

        this.circle = new Circle();
        this.circle.setFill(Color.RED);
        this.circle.setRadius(5);

        this.line = new Line();
        this.line.setVisible(false);
        this.line.setOpacity(.7);
        this.line.setStrokeLineCap(StrokeLineCap.BUTT);

        this.noScale = new Label();
        this.noScale.setText("The page is not scaled. No measurements can be taken.");
        this.noScale.setTextFill(Color.RED);

        this.circle.setFill(Color.RED);
        this.circle.setRadius(5);
        this.pane.getChildren().add(circle);

        //stud
//        this.workspace_stud_height = window.stud_height;
    }

    //File Controls
    public void open() {
        try {
            File pdf = FileService.open();
            frontPane.setVisible(false);
            innerGridPane.setVisible(false);
            loadingPane.setVisible(true);
            PdfToImageService service = new PdfToImageService(pdf);
            service.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    pageObjects.add(newValue);
                    if (!issetCanvas) {
                        page = newValue;
                        setPageElements();
//                        Tools.disableButtons(new String[]{"LENGTH", "AREA", "STAMP", "RECTANGLE"}, box);
                        scroller.setOpacity(1.0);
                        issetCanvas = true;
                        loadingPane.setVisible(false);
                        gridPane.setVisible(false);
                        window.setStructures();
                        window.connectStructures();
                        window.structureToggle.setDisable(false);
                        window.NEXT_PAGE.setDisable(false);
                        window.PREVIOUS_PAGE.setDisable(false);
                        window.showToast("PDF Successfully Loaded!");
                    }
                }
            });
            service.start();
        } catch (Exception ignored) {

        }
    }

    public void save() {
        FileService.save(pageObjects);
    }

    //Switching to different controllers
    public void setMode(String mode) {
        switch (mode) {
            case "SCALE":
                Scale scale = new Scale(this);
                break;
            case "LENGTH":
                this.canDraw = true;
                Length length = new Length(this);
                break;
            case "AREA":
                this.canDraw = true;
                Area area = new Area(this);
                break;
            case "CLADDING":
                this.canDraw = true;
                Cladding cladding = new Cladding(this);
                break;
            case "FREE":
                this.canDraw = false;
                pane.setOnMouseMoved(event -> {
                    try {
                        for (double[][] arr : snapList) {
                            int a = (int) arr[0][0] - (int) event.getX();
                            int b = (int) arr[0][1] - (int) event.getY();
                            if ((a >= -3 && a <= 3) && (b >= -3 && b <= 3)) {
                                circle.setCenterX(arr[0][0]);
                                circle.setCenterY(arr[0][1]);
                                circle.setVisible(true);
                                circle.setRadius(5);
                                circle.setOnMouseExited(event1 -> {
                                    circle.setVisible(false);
                                });
                                pane.getChildren().add(circle);
                                break;
                            } else {
                                circle.setVisible(false);
                            }
                        }
                    } catch (Exception ex) {

                    }
                });
                pane.setOnMouseClicked(event -> {
                    //stamping
                    if (event.getButton() == MouseButton.PRIMARY) {
                        if (!(window.ws_indicator == 0)) {
                            Label window_stamp = new Label();
                            window_stamp.setText("       W" + window.WINDOW_NO.getText() + "\n        " +
                                    window.CLADDING.getText() + "\n" + window.WIDTH.getText() + "  x  " +
                                    window.HEIGHT.getText());
                            window_stamp.setFont(new Font("Arial", 36));
                            window_stamp.setTextFill(Color.RED);
                            window_stamp.setLayoutX(event.getX() - 50);
                            window_stamp.setLayoutY(event.getY() - 40);
                            window_stamp.setStyle("-fx-border-color: #ff0000;");
                            window_stamp.setAlignment(Pos.CENTER);

                            pane.getChildren().add(window_stamp);

                            WindowObject windowObject = new WindowObject();
                            windowObject.setWindowStamp(window_stamp);
                            windowObject.setWindowNo("W" + window.WINDOW_NO.getText());
                            windowObject.setCladding(window.CLADDING.getText());
                            windowObject.setType(window.WINDOW_TYPE.getSelectionModel().getSelectedItem());
                            windowObject.setWidth(window.WIDTH.getText());
                            windowObject.setHeight(window.HEIGHT.getText());

                            window_stamp.setOnMouseClicked(event1 -> {
                                if (event1.getButton() == MouseButton.SECONDARY) {
                                    stampMenu = new ContextMenu();
                                    stampMenu.hide();

                                    MenuItem removeStamp = new MenuItem("Remove Stamp");
                                    removeStamp.setOnAction(event2 -> {
                                        window_stamp.setVisible(false);

                                        windowData w = new windowData(String.valueOf(window_no++), windowObject.getWindowNo(),
                                                windowObject.getCladding(), windowObject.getType(), windowObject.getWidth(),
                                                windowObject.getHeight());

                                        Iterator itr = layouts.windowData.iterator();
                                        while (itr.hasNext()) {
                                            windowData element = (windowData) itr.next();
                                            if (element.getWindow_no().equals(w.getWindow_no())) {
                                                System.out.print(element.getWindow_no());
                                                itr.remove();
                                                break;
                                            }
                                        }

                                        System.out.println("contains remove" + layouts.windowData.remove(w));
                                    });
                                    stampMenu.getItems().add(removeStamp);
                                    stampMenu.show(window_stamp, event1.getScreenX(), event1.getScreenY());
                                }
                            });

                            layouts.windowData.addAll(new windowData(String.valueOf(window_no++), windowObject.getWindowNo(),
                                    windowObject.getCladding(), windowObject.getType(), windowObject.getWidth(),
                                    windowObject.getHeight()));
                        }
                        //doors
                        if (!(window.ds_indicator == 0)) {
                            ImageView door_img = new ImageView();
                            Label door_stamp = new Label();

                            int selected_icon = window.iconList.getSelectionModel().getSelectedIndex();

                            switch (selected_icon) {
                                case 0:
                                    door_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/blue-icon1.png")));
                                    break;
                                case 1:
                                    door_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/blue-icon2.png")));
                                    break;
                                case 2:
                                    door_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/blue-icon3.png")));
                                    break;
                                case 3:
                                    door_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/blue-icon4.png")));
                                    break;
                                case 4:
                                    door_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/blue-icon5.png")));
                                    break;
                                case 5:
                                    door_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/blue-icon6.png")));
                                    break;
                                case 6:
                                    door_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/green-icon1.png")));
                                    break;
                                case 7:
                                    door_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/green-icon2.png")));
                                    break;
                                case 8:
                                    door_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/green-icon3.png")));
                                    break;
                                case 9:
                                    door_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/green-icon4.png")));
                                    break;
                                case 10:
                                    door_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/green-icon5.png")));
                                    break;
                                case 11:
                                    door_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/green-icon6.png")));
                                    break;
                                case 12:
                                    door_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/red-icon1.png")));
                                    break;
                                case 13:
                                    door_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/red-icon2.png")));
                                    break;
                                case 14:
                                    door_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/red-icon3.png")));
                                    break;
                                case 15:
                                    door_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/red-icon4.png")));
                                    break;
                                case 16:
                                    door_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/red-icon5.png")));
                                    break;
                                case 17:
                                    door_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/red-icon6.png")));
                                    break;
                            }

                            door_stamp.setGraphic(door_img);
                            door_stamp.setLayoutX(event.getX());
                            door_stamp.setLayoutY(event.getY());
                            door_stamp.setAlignment(Pos.CENTER);

                            pane.getChildren().add(door_stamp);

                            DoorObject doorObject = new DoorObject();
                            doorObject.setNo(String.valueOf(door_no++));
                            doorObject.setDoor_type(window.DOOR_TYPE.getSelectionModel().getSelectedItem());
                            doorObject.setDoor_width(window.DOOR_WIDTH.getSelectionModel().getSelectedItem());
                            doorObject.setDoor_height(window.DOOR_HEIGHT.getSelectionModel().getSelectedItem());
                            doorObject.setDoor_stamp(door_stamp);

                            door_stamp.setOnMouseClicked(event1 -> {
                                if (event1.getButton() == MouseButton.SECONDARY) {
                                    stampMenu = new ContextMenu();
                                    stampMenu.hide();

                                    MenuItem removeStamp = new MenuItem("Remove Stamp");
                                    removeStamp.setOnAction(event2 -> {
                                        door_stamp.setVisible(false);

                                        doorData d = new doorData(doorObject.getNo(), doorObject.getDoor_type(),
                                                doorObject.getDoor_width(), doorObject.getDoor_height());

                                        Iterator itr = layouts.doorData.iterator();
                                        while (itr.hasNext()) {
                                            doorData element = (doorData) itr.next();
                                            if (element.getNo().equals(d.getNo())) {
                                                System.out.print(element.getNo());
                                                itr.remove();
                                                break;
                                            }
                                        }

                                        System.out.println("contains remove" + layouts.doorData.remove(d));
                                    });
                                    stampMenu.getItems().add(removeStamp);
                                    stampMenu.show(door_stamp, event1.getScreenX(), event1.getScreenY());
                                }
                            });

                            layouts.doorData.addAll(new doorData(doorObject.getNo(), doorObject.getDoor_type(),
                                    doorObject.getDoor_width(), doorObject.getDoor_height()));
                        }
                        if (!(window.fs_indicator == 0)) {

                            ImageView foundations_img = new ImageView();
                            Label foundations_stamp = new Label();

                            String path = "";

                            int selected_icon = window.iconList.getSelectionModel().getSelectedIndex();

                            switch (selected_icon) {
                                case 0:
                                    foundations_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/blue-icon1.png")));
                                    path = "../Views/stamper_icons/blue-icon1.png";
                                    break;
                                case 1:
                                    foundations_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/blue-icon2.png")));
                                    path = "../Views/stamper_icons/blue-icon2.png";
                                    break;
                                case 2:
                                    foundations_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/blue-icon3.png")));
                                    path = "../Views/stamper_icons/blue-icon3.png";
                                    break;
                                case 3:
                                    foundations_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/blue-icon4.png")));
                                    path = "../Views/stamper_icons/blue-icon4.png";
                                    break;
                                case 4:
                                    foundations_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/blue-icon5.png")));
                                    path = "../Views/stamper_icons/blue-icon5.png";
                                    break;
                                case 5:
                                    foundations_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/blue-icon6.png")));
                                    path = "../Views/stamper_icons/blue-icon6.png";
                                    break;
                                case 6:
                                    foundations_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/green-icon1.png")));
                                    path = "../Views/stamper_icons/green-icon1.png";
                                    break;
                                case 7:
                                    foundations_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/green-icon2.png")));
                                    path = "../Views/stamper_icons/green-icon2.png";
                                    break;
                                case 8:
                                    foundations_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/green-icon3.png")));
                                    path = "../Views/stamper_icons/green-icon3.png";
                                    break;
                                case 9:
                                    foundations_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/green-icon4.png")));
                                    path = "../Views/stamper_icons/green-icon4.png";
                                    break;
                                case 10:
                                    foundations_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/green-icon5.png")));
                                    path = "../Views/stamper_icons/green-icon5.png";
                                    break;
                                case 11:
                                    foundations_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/green-icon6.png")));
                                    path = "../Views/stamper_icons/green-icon6.png";
                                    break;
                                case 12:
                                    foundations_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/red-icon1.png")));
                                    path = "../Views/stamper_icons/red-icon1.png";
                                    break;
                                case 13:
                                    foundations_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/red-icon2.png")));
                                    path = "../Views/stamper_icons/red-icon2.png";
                                    break;
                                case 14:
                                    foundations_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/red-icon3.png")));
                                    path = "../Views/stamper_icons/red-icon3.png";
                                    break;
                                case 15:
                                    foundations_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/red-icon4.png")));
                                    path = "../Views/stamper_icons/red-icon4.png";
                                    break;
                                case 16:
                                    foundations_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/red-icon5.png")));
                                    path = "../Views/stamper_icons/red-icon5.png";
                                    break;
                                case 17:
                                    foundations_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/red-icon6.png")));
                                    path = "../Views/stamper_icons/red-icon6.png";
                                    break;
                            }
                            foundations_stamp.setGraphic(foundations_img);
                            foundations_stamp.setLayoutX(event.getX());
                            foundations_stamp.setLayoutY(event.getY());
                            foundations_stamp.setAlignment(Pos.CENTER);

                            FoundationsObject foundations_stamp_object = new FoundationsObject();
                            foundations_stamp_object.setStamp(foundations_stamp);

                            pane.getChildren().add(foundations_stamp_object.getStamp());

                            //db for managing table sections w/ identifier
                            DataBase db = DataBase.getInstance();
                            //identifies whether to update or insert row by section
                            db.identifyFoundationsPFValues(window.FOUNDATIONS_DEPTH.getText(),
                                    window.FOUNDATIONS_WIDTH.getText(), window.FOUNDATIONS_LENGTH.getText(),
                                    foundations_pf_section, foundations_pf_volume, foundations_pf_quantity);

                            db.identifyFoundationsCBValues(window.FOUNDATIONS_DIAMETER.getText(),
                                    window.FOUNDATIONS_HEIGHT.getText(), foundations_cb_section, foundations_cb_volume,
                                    foundations_cb_quantity);

                            foundations_stamp_object.setPart(window.FOUNDATIONS_PART.getSelectionModel().getSelectedItem());
                            if (window.FOUNDATIONS_PART.getSelectionModel().getSelectedItem().equals("Post Footings")) {
                                if (foundations_pf_section.getText().equals("")) {
                                    db.getFoundationsPFLastRow(pfLastRowSection);
                                    if (pfLastRowSection.getText().equals("")) {
                                        pfLastRowSection.setText("1");
                                    }

                                    db.insertFoundationsPF(Integer.parseInt(pfLastRowSection.getText()), path, 1,
                                            window.FOUNDATIONS_DEPTH.getText(), window.FOUNDATIONS_WIDTH.getText(),
                                            window.FOUNDATIONS_LENGTH.getText(), window.FOUNDATIONS_VOLUME1.getText());

                                    //new insert api here


                                } else {
                                    int pf_quantity = Integer.parseInt(foundations_pf_quantity.getText()) + 1;
                                    double pf_volume = pf_quantity * (Double.parseDouble(foundations_pf_volume.getText())
                                            / Double.parseDouble(foundations_pf_quantity.getText()));
                                    foundations_stamp_object.setNo(pfLastRowSection.getText());
                                    db.updateFoundationsPFCount(pf_quantity, String.valueOf(new DecimalFormat("0.00")
                                            .format(pf_volume)), Integer.parseInt(foundations_pf_section.getText()));
                                }

                                foundations_stamp_object.setNo(pfLastRowSection.getText());

//                                db.addSectionDimension("Foundations",window.FOUNDATIONS_PART.getSelectionModel().getSelectedItem(),
//                                        path, "", "", window.FOUNDATIONS_DEPTH.getText(),
//                                        window.FOUNDATIONS_WIDTH.getText(), window.FOUNDATIONS_LENGTH.getText(),
//                                        window.FOUNDATIONS_VOLUME1.getText());
                            }

                            if (window.FOUNDATIONS_PART.getSelectionModel().getSelectedItem().equals("Pole Footings")) {
                                if (foundations_cb_section.getText().equals("")) {
                                    db.getFoundationsCBLastRow(cbLastRowSection);
                                    if (cbLastRowSection.getText().equals("")) {
                                        cbLastRowSection.setText("1");
                                    }

                                    db.insertFoundationsCB(Integer.parseInt(cbLastRowSection.getText()), path, 1,
                                            window.FOUNDATIONS_DIAMETER.getText(), window.FOUNDATIONS_HEIGHT.getText(),
                                            window.FOUNDATIONS_VOLUME2.getText());

                                    //new insert api here


                                } else {
                                    int cb_quantity = Integer.parseInt(foundations_cb_quantity.getText()) + 1;
                                    double cb_volume = cb_quantity * (Double.parseDouble(foundations_cb_volume.getText())
                                            / Double.parseDouble(foundations_cb_quantity.getText()));


                                    db.updateFoundationsCBCount(cb_quantity, String.valueOf(
                                            new DecimalFormat("0.00").format(cb_volume)),
                                            Integer.parseInt(foundations_cb_section.getText()));
                                }

                                foundations_stamp_object.setNo(cbLastRowSection.getText());

//                                db.addSectionDimension("Foundations",window.FOUNDATIONS_PART.getSelectionModel().getSelectedItem(),
//                                        path, window.FOUNDATIONS_DIAMETER.getText(), window.FOUNDATIONS_HEIGHT.getText(), "",
//                                        "", "", window.FOUNDATIONS_VOLUME2.getText());

                            }
                            foundationsObjectList.add(foundations_stamp_object);
                            foundations_stamp_object.setId(foundationsObjectList.size());

                            foundations_stamp_object.getStamp().setOnMouseClicked(event1 -> {
                                if (event1.getButton() == MouseButton.SECONDARY) {
                                    stampMenu = new ContextMenu();
                                    stampMenu.hide();

                                    MenuItem removeStamp = new MenuItem("Remove Stamp");
                                    removeStamp.setOnAction(event2 -> {
                                        foundations_stamp_object.getStamp().setVisible(false);
                                        int part = 1;
                                        String key = "";
                                        Iterator itr = foundationsObjectList.iterator();
                                        while (itr.hasNext()) {
                                            FoundationsObject element = (FoundationsObject) itr.next();
                                            if (element.getId() == (foundations_stamp_object.getId())) {
                                                String table = "shed_foundations_";
                                                if (element.getPart() == "Post Footings") {
                                                    table += "post_footings";
                                                    part = 1;
                                                    key="pf_";
                                                } else {
                                                    table += "concrete_bores";
                                                    part = 2;
                                                    key="cb_";
                                                }
                                                String[] result = db.getSectionData(table, Integer.parseInt(element.getNo()));
                                                if (result != null) {
                                                    double vol = Double.parseDouble(result[0]);
                                                    int qty = Integer.parseInt(result[1]);
                                                    if (qty == 1) {
                                                        db.deleteSectionData(table, Integer.parseInt(element.getNo()));
                                                        db.deleteFoundationsPFSections(part,Integer.parseInt(element.getNo()));
                                                        setupSheetsController.componentList.remove("foundations_"+key+element.getNo());
                                                    } else {
                                                        if (element.getPart() == "Post Footings") {
                                                            vol -= (vol / qty);
                                                            db.updateFoundationsPFCount(qty - 1, String.valueOf(
                                                                    new DecimalFormat("0.00").format(vol)),
                                                                    Integer.parseInt(element.getNo()));
                                                        } else {
                                                            vol -= (vol / qty);
                                                            db.updateFoundationsCBCount(qty - 1, String.valueOf(
                                                                    new DecimalFormat("0.00").format(vol)),
                                                                    Integer.parseInt(element.getNo()));
                                                        }
                                                    }
                                                }

                                                layoutsController.foundationsPostFootingsData.clear();
                                                foundations_pf_section.setText("");
                                                db.showFoundationsPF(layoutsController.foundationsPostFootingsData);
//                                                db.updatePFLayoutsTable("Foundations","Post Footings",layoutsController.foundationsPostFootingsData);


                                                layoutsController.foundationsConcreteBoresData.clear();
                                                foundations_cb_section.setText("");
                                                db.showFoundationsCB(layoutsController.foundationsConcreteBoresData);
//                                                db.updateCBLayoutsTable("Foundations","Pole Footings",layoutsController.foundationsConcreteBoresData);



                                                layoutsController.totalData.clear();

                                                db.getFoundationsPFSections(foundationsPFSectionList);
                                                db.getFoundationsCBSections(foundationsCBSectionList);


                                                double pf = Double.parseDouble(db.getTotalVolume("shed_foundations_post_footings"));
                                                double cb = Double.parseDouble(db.getTotalVolume("shed_foundations_concrete_bores"));
                                                double cf = 0;
                                                try {
                                                    cf = Double.parseDouble(layoutsController.concreteData.get(3));
                                                } catch (Exception e) {
                                                    cf = 0;
                                                }
                                                double total = pf + cb + cf;
                                                new DecimalFormat("0.00").format(total);
                                                layoutsController.totalData.add(0, Double.toString(pf));
                                                layoutsController.totalData.add(1, Double.toString(cb));
                                                layoutsController.totalData.add(2, String.valueOf(
                                                        new DecimalFormat("0.00").format(total)));
                                                itr.remove();
                                                break;
                                            }
                                        }
                                    });
                                    stampMenu.getItems().add(removeStamp);
                                    stampMenu.show(foundations_stamp_object.getStamp(), event1.getScreenX(), event1.getScreenY());


                                }
                            });

                            //pass section to setup sheet
                            System.out.println("pass section to setup sheet");
                            db.getFoundationsPFSections(foundationsPFSectionList);
                            db.getFoundationsCBSections(foundationsCBSectionList);
//                            db.updatePFLayoutsTable("Foundations","Post Footings",layoutsController.foundationsPostFootingsData);
//                            db.updateCBLayoutsTable("Foundations","Pole Footings",layoutsController.foundationsConcreteBoresData);

                            //show foundations table
                            layoutsController.foundationsPostFootingsData.clear();
                            foundations_pf_section.setText("");
                            db.showFoundationsPF(layoutsController.foundationsPostFootingsData);

                            layoutsController.foundationsConcreteBoresData.clear();
                            foundations_cb_section.setText("");
                            db.showFoundationsCB(layoutsController.foundationsConcreteBoresData);

                            layoutsController.totalData.clear();

                            double pf = Double.parseDouble(db.getTotalVolume("shed_foundations_post_footings"));
                            double cb = Double.parseDouble(db.getTotalVolume("shed_foundations_concrete_bores"));
                            double cf = 0;
                            try {
                                cf = Double.parseDouble(layoutsController.concreteData.get(3));
                            } catch (Exception e) {
                                cf = 0;
                            }
                            double total = pf + cb + cf;
                            new DecimalFormat("0.00").format(total);
                            layoutsController.totalData.add(0, Double.toString(pf));
                            layoutsController.totalData.add(1, Double.toString(cb));
                            layoutsController.totalData.add(2, String.valueOf(
                                    new DecimalFormat("0.00").format(total)));

                        }
                        if (!(window.ef_indicator == 0)) {
                            ImageView external_framing_img = new ImageView();
                            Label externalFramingStamp = new Label();

                            String path = "";

                            int selected_icon = window.iconList.getSelectionModel().getSelectedIndex();

                            switch (selected_icon) {
                                case 0:
                                    external_framing_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/blue-icon1.png")));
//                                    path = "C:\\Users\\User\\Documents\\IdeaProjects\\PT_GraphiQS\\src\\Views\\stamper_icons\\blue-icon1.png";
                                    path = "../Views/stamper_icons/blue-icon1.png";
                                    break;
                                case 1:
                                    external_framing_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/blue-icon2.png")));
                                    path = "../Views/stamper_icons/blue-icon2.png";
                                    break;
                                case 2:
                                    external_framing_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/blue-icon3.png")));
                                    path = "../Views/stamper_icons/blue-icon3.png";
                                    break;
                                case 3:
                                    external_framing_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/blue-icon4.png")));
                                    path = "../Views/stamper_icons/blue-icon4.png";
                                    break;
                                case 4:
                                    external_framing_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/blue-icon5.png")));
                                    path = "../Views/stamper_icons/blue-icon5.png";
                                    break;
                                case 5:
                                    external_framing_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/blue-icon6.png")));
                                    path = "../Views/stamper_icons/blue-icon6.png";
                                    break;
                                case 6:
                                    external_framing_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/green-icon1.png")));
                                    path = "../Views/stamper_icons/green-icon1.png";
                                    break;
                                case 7:
                                    external_framing_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/green-icon2.png")));
                                    path = "../Views/stamper_icons/green-icon2.png";
                                    break;
                                case 8:
                                    external_framing_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/green-icon3.png")));
                                    path = "../Views/stamper_icons/green-icon3.png";
                                    break;
                                case 9:
                                    external_framing_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/green-icon4.png")));
                                    path = "../Views/stamper_icons/green-icon4.png";
                                    break;
                                case 10:
                                    external_framing_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/green-icon5.png")));
                                    path = "../Views/stamper_icons/green-icon5.png";
                                    break;
                                case 11:
                                    external_framing_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/green-icon6.png")));
                                    path = "../Views/stamper_icons/green-icon6.png";
                                    break;
                                case 12:
                                    external_framing_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/red-icon1.png")));
                                    path = "../Views/stamper_icons/red-icon1.png";
                                    break;
                                case 13:
                                    external_framing_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/red-icon2.png")));
                                    path = "../Views/stamper_icons/red-icon2.png";
                                    break;
                                case 14:
                                    external_framing_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/red-icon3.png")));
                                    path = "../Views/stamper_icons/red-icon3.png";
                                    break;
                                case 15:
                                    external_framing_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/red-icon4.png")));
                                    path = "../Views/stamper_icons/red-icon4.png";
                                    break;
                                case 16:
                                    external_framing_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/red-icon5.png")));
                                    path = "../Views/stamper_icons/red-icon5.png";
                                    break;
                                case 17:
                                    external_framing_img.setImage(new Image(getClass().getResourceAsStream("../Views/stamper_icons/red-icon6.png")));
                                    path = "../Views/stamper_icons/red-icon6.png";
                                    break;
                            }
                            externalFramingStamp.setGraphic(external_framing_img);
                            externalFramingStamp.setLayoutX(event.getX());
                            externalFramingStamp.setLayoutY(event.getY());
                            externalFramingStamp.setAlignment(Pos.CENTER);

                            ExternalFramingObject externalFramingObject = new ExternalFramingObject();
                            externalFramingObject.setStamp(externalFramingStamp);

                            pane.getChildren().add(externalFramingObject.getStamp());

                            DataBase db = DataBase.getInstance();
                            //identify same values
                            db.identifyExternalFramingValues(window.EXTERNAL_FRAMING_PART.getSelectionModel().getSelectedItem(),
                                    window.EXTERNAL_FRAMING_MATERIAL.getSelectionModel().getSelectedItem(),
                                    external_framing_section, external_framing_quantity);

                            externalFramingObject.setPart(window.FOUNDATIONS_PART.getSelectionModel().getSelectedItem());
                            if (external_framing_section.getText().equals("")) {
                                db.getExternalFramingLastRow(efLastRowSection,
                                        window.EXTERNAL_FRAMING_PART.getSelectionModel().getSelectedItem());
                                if (efLastRowSection.getText().equals("")) {
                                    efLastRowSection.setText("1");
                                }
                                db.insertExternalFraming(Integer.parseInt(efLastRowSection.getText()), path,
                                        window.EXTERNAL_FRAMING_PART.getSelectionModel().getSelectedItem(), 1,
                                        window.EXTERNAL_FRAMING_MATERIAL.getSelectionModel().getSelectedItem());
                            } else {
                                int ef_quantity = Integer.parseInt(external_framing_quantity.getText()) + 1;

                                externalFramingObject.setNo(efLastRowSection.getText());
                                db.updateExternalFramingCount(ef_quantity, window.EXTERNAL_FRAMING_PART.getSelectionModel().getSelectedItem(),
                                        Integer.parseInt(external_framing_section.getText()));
                            }

                            externalFramingObject.setNo(efLastRowSection.getText());
                            //clear values
                            external_framing_section.setText("");
                            external_framing_quantity.setText("");
                            efLastRowSection.setText("");

                            //pass to layouts
                            layoutsController.externalFramingPolesData.clear();
                            layoutsController.externalFramingColumnsData.clear();
                            db.showExternalFramingLayouts(layoutsController.externalFramingPolesData,
                                    layoutsController.externalFramingColumnsData);
                        }
                    }
                });
                break;
        }
    }

    public void setColor(Color color) {
        this.color = color;
    }

    //Refresh Window
    public void updateWindow() {
        pane.getChildren().clear();
        pane.getChildren().add(circle);
        circle.setVisible(false);

        if (page.getScale() == 0) {
            if (!pane.getChildren().contains(noScale)) {
                noScale.setLayoutX(15);
                noScale.setLayoutY(15);
                pane.getChildren().add(noScale);
//                disableButtons(new String[]{"LENGTH", "AREA", "STAMP", "RECTANGLE"}, box);
                window.scaledIndicator(0);
            }
        } else {
            pane.getChildren().remove(noScale);
            if (window.colorPicker.isDisabled()) {
//                disableButtons(new String[]{"LENGTH", "AREA"}, box);
            }
            window.scaledIndicator(1);
        }

        for (ShapeObject sp0 : page.getShapeList()) {
            if (sp0.getType().equals("AREA")) {
                pane.getChildren().add(sp0.getPolygon());
                double area = 0;
                for (int x = 0; x < sp0.getPointList().size() - 1; x++) {
                    Point2D p1 = sp0.getPointList().get(x);
                    Point2D p2 = sp0.getPointList().get(x + 1);
                    area += ((p1.getX() / page.getScale()) * (p2.getY() / page.getScale())) - ((p1.getY() /
                            page.getScale()) * (p2.getX() / page.getScale()));
                }
                area = Math.abs(area / 2);
                area = area / 1000;
                sp0.setArea(Math.round(area * 100.0) / 100.0);
                Label lbl = new Label(sp0.getArea() + " m²");
                lbl.setFont(new Font("Arial", 36));
                lbl.setTextFill(sp0.getColor());
                double layX = sp0.getPolygon().getBoundsInParent().getMinX() + (sp0.getPolygon().getBoundsInParent()
                        .getWidth() - lbl.getWidth()) / 2;
                double layY = sp0.getPolygon().getBoundsInParent().getMinY() + (sp0.getPolygon().getBoundsInParent()
                        .getHeight() - lbl.getHeight()) / 2;
                lbl.setOnMouseEntered(event -> {
                    lbl.setStyle("-fx-background-color: white");
                    lbl.setOpacity(1);
                });
                lbl.setOnMouseExited(event -> {
                    lbl.setStyle("-fx-background-color: transparent");
                    lbl.setOpacity(.7);
                });
                lbl.setLayoutX(layX);
                lbl.setLayoutY(layY);
                lbl.setOpacity(.7);
                pane.getChildren().add(lbl);
            }
        }
        for (ShapeObject sp1 : page.getShapeList()) {
            if (sp1.getType().equals("LENGTH")) {
                for (Line l : sp1.getLineList()) {
                    Point2D p1 = new Point2D(l.getStartX(), l.getStartY());
                    Point2D p2 = new Point2D(l.getEndX(), l.getEndY());
                    Point2D mid = p1.midpoint(p2);
                    line.getRotate();
                    if (p1.distance(p2) != 0) {
                        double length = (p1.distance(p2) / page.getScale());
                        sp1.setLength(Math.round(length * 100.0) / 100.0);
                        Label lbl = new Label(sp1.getLength() + " mm");
                        double theta = Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX());
                        lbl.setFont(new Font("Arial", 36));
                        lbl.setRotate(theta * 180 / Math.PI);
                        lbl.setLayoutY(mid.getY());
                        lbl.setLayoutX(mid.getX() - (55 / group.getScaleY()));
                        lbl.setOnMouseEntered(event -> {
                            lbl.setStyle("-fx-background-color: white");
                            lbl.setOpacity(1);
                        });
                        lbl.setOnMouseExited(event -> {
                            lbl.setStyle("-fx-background-color: transparent");
                            lbl.setOpacity(.7);
                        });
                        lbl.setTextFill(sp1.getColor());
                        lbl.setOpacity(.7);
                        pane.getChildren().add(lbl);
                        total = total + sp1.getLength();
                    }
                }
                sp1.setLength(Math.round(total * 100.0) / 100.0);
                total = 0.0;
            }
            pane.getChildren().addAll(sp1.getLineList());
            pane.getChildren().addAll(sp1.getBoxList());

            //set shapeObject data
            sp1.setStructure(window.structureComboBox.getSelectionModel().getSelectedItem().toString());
            sp1.setWallType(window.wallTypeComboBox.getSelectionModel().getSelectedItem().toString());
            sp1.setWall(window.wallComboBox.getSelectionModel().getSelectedItem().toString());
            sp1.setMaterial(window.materialComboBox.getSelectionModel().getSelectedItem().toString());
            sp1.setStud_height(String.valueOf(stud_height));
            BigDecimal bigDecimal = new BigDecimal(Math.round(sp1.getLength() * stud_height / 1000));
            sp1.setUnit(String.valueOf(new BigDecimal(Math.round(sp1.getLength() * stud_height / 1000))));

            if (window.materialComboBox.getSelectionModel().getSelectedItem().equals("10mm Gib") ||
                    window.materialComboBox.getSelectionModel().getSelectedItem().equals("13mm Gib")) {
                DataBase db = DataBase.getInstance();
                db.setSubtrades(createProjectController.selectedClient, "Gib Stopper", rate);
                sp1.setLabour(String.valueOf(Math.round(sp1.getLength() * stud_height / 1000) *
                        Integer.parseInt(rate.getText())));
            } else {
                sp1.setLabour("");
            }
        }

        for (CladdingObject cl0 : page.getCladdingObjectList()) {
            for (Line l : cl0.getLineList()) {
                Point2D p1 = new Point2D(l.getStartX(), l.getStartY());
                Point2D p2 = new Point2D(l.getEndX(), l.getEndY());
                Point2D mid = p1.midpoint(p2);
                line.getRotate();
                if (p1.distance(p2) != 0) {
                    double length = (p1.distance(p2) / page.getScale());
                    cl0.setLength(Math.round(length * 100.0) / 100.0);
                    Label lbl = new Label(cl0.getLength() + " mm");
                    double theta = Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX());
                    lbl.setFont(new Font("Arial", 36));
                    lbl.setRotate(theta * 180 / Math.PI);
                    lbl.setLayoutY(mid.getY());
                    lbl.setLayoutX(mid.getX() - (55 / group.getScaleY()));
                    lbl.setOnMouseEntered(event -> {
                        lbl.setStyle("-fx-background-color: white");
                        lbl.setOpacity(1);
                    });
                    lbl.setOnMouseExited(event -> {
                        lbl.setStyle("-fx-background-color: transparent");
                        lbl.setOpacity(.7);
                    });
                    lbl.setTextFill(cl0.getColor());
                    lbl.setOpacity(.7);
                    pane.getChildren().add(lbl);
                    total = total + cl0.getLength();
                }
            }
            cl0.setLength(Math.round(total * 100.0) / 100.0);
            total = 0.0;

            pane.getChildren().addAll(cl0.getLineList());
            pane.getChildren().addAll(cl0.getBoxList());
        }

        //get shapeObject data to layout table
        int count = 0;
        layouts.data.clear();
        for (int i = 0; i < pageObjects.size(); i++) {
            PageObject p = pageObjects.get(i);
            for (ShapeObject sp2 : p.getShapeList()) {
                System.out.println("Elements");
                System.out.println(sp2.getStructure());
                System.out.println(sp2.getWallType());
                System.out.println(sp2.getWall());
                System.out.println(sp2.getMaterial());
                System.out.println("Page : " + i + 1);
                System.out.println(count++);

                double value;
                if (sp2.getType().equals("LENGTH")) {
                    value = sp2.getLength();
                } else {
                    value = sp2.getArea();
                }

                Label colorLabel = new Label();
                colorLabel.setMinWidth(60);
                colorLabel.setMinHeight(20);
                colorLabel.setBackground(new Background(new BackgroundFill(sp2.getColor(), CornerRadii.EMPTY, Insets.EMPTY)));

                if (!sp2.getLabour().equals("")) {
                    layouts.data.addAll(new layoutsData(String.valueOf(count), String.valueOf(i + 1), sp2.getType(),
                            sp2.getStructure(), sp2.getWallType(), sp2.getWall(), sp2.getMaterial(), colorLabel,
                            NumberFormat.getNumberInstance(Locale.US).format(Double.valueOf(value)),
                            NumberFormat.getNumberInstance(Locale.US).format(Double.valueOf(sp2.getStud_height())),
                            NumberFormat.getNumberInstance(Locale.US).format(Double.valueOf(sp2.getUnit())),
                            NumberFormat.getNumberInstance(Locale.US).format(Double.valueOf(sp2.getLabour()))));
                } else {
                    layouts.data.addAll(new layoutsData(String.valueOf(count), String.valueOf(i + 1), sp2.getType(),
                            sp2.getStructure(), sp2.getWallType(), sp2.getWall(), sp2.getMaterial(), colorLabel,
                            NumberFormat.getNumberInstance(Locale.US).format(Double.valueOf(value)),
                            NumberFormat.getNumberInstance(Locale.US).format(Double.valueOf(sp2.getStud_height())),
                            NumberFormat.getNumberInstance(Locale.US).format(Double.valueOf(sp2.getUnit())),
                            sp2.getLabour()));
                }
            }
        }

        //cladding layout
        int cld_count = 0;
        layouts.claddingData.clear();
        for (int j = 0; j < pageObjects.size(); j++) {
            PageObject p1 = pageObjects.get(j);
            for (CladdingObject cl1 : p1.getCladdingObjectList()) {
                cld_count++;
                layouts.claddingData.addAll(new claddingData("C" + cld_count, cl1.getCladding_name(),
                        String.valueOf(cl1.getLength()), String.valueOf(cl1.getCladding_height())));
                System.out.println(cl1.getCladding_name());
            }
        }

        pane.getChildren().add(line);
        line.setVisible(false);
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
                    sp.setStrokeWidth(5 / group.getScaleY());
                }
            }
            if (node.getClass() == Label.class) {
                Label lbl = (Label) node;
                lbl.setFont(new Font("Arial", 18 / group.getScaleY()));
            }
        });
    }

    public void studPopup() {
        try {
            stud_height = Float.parseFloat(JOptionPane.showInputDialog("Enter Stud height",
                    0.00 + " mm"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number.",
                    "Invalid Stu    `d height value", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setPageElements() {
        shapeObjList = new ArrayList<>();
        stampList = new ArrayList<>();
        snapList = new ArrayList<>();
        page = pageObjects.get(pageNumber);
        shapeObjList.addAll(page.getShapeList());
        stampList.addAll(page.getStampList());
        snapList.addAll(page.getSnapList());
        setCanvas();
    }

    public void setCanvas() {
        canvas.setWidth(page.getImage().getWidth());
        canvas.setHeight(page.getImage().getHeight());
        pane.setPrefSize(canvas.getWidth(), canvas.getHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(page.getImage(), 0, 0);
        updateWindow();
    }

    //Pagination
    public void nextPage() {
        if (pageNumber < pageObjects.size() - 1) {
            pageNumber++;
            setPageElements();
            setMode("FREE");
        }
        //scale indicator
        if (pane.getChildren().contains(noScale)) {
            window.scaledIndicator(0);
        } else {
            window.scaledIndicator(1);
        }
    }

    public void previousPage() {
        if (pageNumber > 0) {
            pageNumber--;
            setPageElements();
            setMode("FREE");
        }
        //scale indicator
        if (pane.getChildren().contains(noScale)) {
            window.scaledIndicator(0);
        } else {
            window.scaledIndicator(1);
        }
    }

    //Utils
    public Point2D clamp(double x, double y) {
        double maxX = canvas.getWidth();
        double maxY = canvas.getHeight();
        double a = Math.max(0, x);
        double b = Math.max(0, y);
        double c = Math.min(a, maxX);
        double d = Math.min(b, maxY);
        Point2D p = new Point2D(c, d);
        return p;
    }

    //button controls
//    public static void disableButtons(String[] btns, VBox param) {
//        param.getChildren().forEach((Node node) -> {
//            JFXButton btn = (JFXButton) node;
//            btn.setDisable(false);
//            for (String s : btns) {
//                if (btn.getId().equals(s)) {
//                    btn.setDisable(true);
//                }
//            }
//        });
//    }
//
//    public static void enableButtons(String[] btns, VBox param) {
//        param.getChildren().forEach(node -> {
//            JFXButton btn = (JFXButton) node;
//            btn.setDisable(true);
//            for (String s : btns) {
//                if (btn.getId().equals(s)) {
//                    btn.setDisable(false);
//                }
//            }
//        });
//    }
}
