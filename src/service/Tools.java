package service;

import Controllers.createProjectController;
import DataBase.DataBase;
import Controllers.workspaceController;
import Controllers.layoutsController;
import Model.data.layoutsData;
import Model.PageObject;
import Model.ShapeObject;
import Model.WindowObject;
import Model.data.windowData;
import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.control.ContextMenu;

import javax.swing.*;
import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class Tools {
    public PageObject page;
    Group group;
    String mode;
    Pane pane;
    Canvas canvas, stamp_canvas;
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
    layoutsController layouts;
    List<Shape> shapeList = new ArrayList<>();
    ArrayList<Point2D> pointList = new ArrayList<>();
    ArrayList<Shape> stampList = new ArrayList<>();
    ArrayList<PageObject> pageObjects = new ArrayList<>();
    ArrayList<double[][]> snapList = new ArrayList<>();
    public ArrayList<ShapeObject> shapeObjList = new ArrayList<>();

    double total;
    double stud_height;
    public Label rate = new Label();

    int window_no = 1;

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
        this.stamp_canvas = window.stamp_canvas;
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
        } catch(Exception ignored){

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
                    //do not remove
                    System.out.println(window.ws_indicator);
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
                            window_stamp.setStyle("-fx-border-color: red;");
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
                                                windowObject.getCladding(), windowObject.getType(), windowObject.getWidth(), windowObject.getHeight());

                                        Iterator itr =  layouts.windowData.iterator();
                                        while (itr.hasNext()) {
                                            windowData element = (windowData) itr.next();
                                            if (element.getWindow_no().equals(w.getWindow_no())) {
                                                System.out.print(element.getWindow_no());
                                                itr.remove();
                                                break;
                                            }
                                        }

                                        System.out.println("contains remove"+   layouts.windowData.remove(w));
                                    });
                                    stampMenu.getItems().add(removeStamp);
                                    stampMenu.show(window_stamp, event1.getScreenX(), event1.getScreenY());
                                }
                            });

                            layouts.windowData.addAll(new windowData(String.valueOf(window_no++), windowObject.getWindowNo(),
                                    windowObject.getCladding(), windowObject.getType(), windowObject.getWidth(), windowObject.getHeight()));
                        }
                    }
                });
                break;

//            case "WINDOW_STAMP":
//                this.canDraw = false;
//                pane.setOnMouseClicked(event -> {
//                    if (event.getButton() == MouseButton.PRIMARY) {
//                        if (!(window.ws_indicator == 0)) {
//                        Label window_stamp = new Label("       W" + window.WINDOW_NO.getText() + "\n        "
//                                    + window.CLADDING.getText() + "\n" + window.WIDTH.getText() + "  x  " +
//                                    window.HEIGHT.getText());
//                            window_stamp.setFont(new Font("Arial", 36));
//                            window_stamp.setTextFill(Color.RED);
//                            window_stamp.setLayoutX(event.getX() - 25);
//                            window_stamp.setLayoutY(event.getY() - 25);
//                            window_stamp.setAlignment(Pos.CENTER);
//
//                            pane.getChildren().add(window_stamp);
//
//                            window_stamp.setOnMouseClicked(event1 -> {
//                                if (event1.getButton() == MouseButton.SECONDARY) {
//                                    stampMenu = new ContextMenu();
//                                    stampMenu.hide();
//
//                                    MenuItem removeStamp = new MenuItem("Remove Stamp");
//                                    removeStamp.setOnAction(event2 -> {
//                                        window_stamp.setVisible(false);
//                                    });
//
//                                    stampMenu.getItems().add(removeStamp);
//                                    stampMenu.show(window_stamp, event1.getScreenX(), event1.getScreenY());
//                                }
//                            });
//
//                            layouts.windowData.addAll(new windowData(String.valueOf(window_no++),
//                                    "W" + window.WINDOW_NO.getText(), window.CLADDING.getText(),
//                                    window.WIDTH.getText(), window.HEIGHT.getText()));
//                        }
//                    }
//                });
//                break;
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
                    area += ((p1.getX() / page.getScale()) * (p2.getY() / page.getScale())) - ((p1.getY() / page.getScale()) * (p2.getX() / page.getScale()));
                }
                area = Math.abs(area / 2);
                area = area / 1000;
                sp0.setArea(Math.round(area * 100.0) / 100.0);
                Label lbl = new Label(sp0.getArea() + " m²");
                lbl.setFont(new Font("Arial", 36));
                lbl.setTextFill(sp0.getColor());
                double layX = sp0.getPolygon().getBoundsInParent().getMinX() + (sp0.getPolygon().getBoundsInParent().getWidth() - lbl.getWidth()) / 2;
                double layY = sp0.getPolygon().getBoundsInParent().getMinY() + (sp0.getPolygon().getBoundsInParent().getHeight() - lbl.getHeight()) / 2;
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
            sp1.setUnit(String.valueOf(bigDecimal));

            if (window.materialComboBox.getSelectionModel().getSelectedItem().equals("10mm Gib") ||
                    window.materialComboBox.getSelectionModel().getSelectedItem().equals("13mm Gib")) {
                DataBase db = DataBase.getInstance();
                db.setSubtrades(createProjectController.selectedClient, "Gib Stopper", rate);
                sp1.setLabour(String.valueOf(Math.round(sp1.getLength() * stud_height / 1000) * Integer.parseInt(rate.getText())));
            } else {
                sp1.setLabour("");
            }
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
                System.out.println("Page : "+ i + 1);
                System.out.println(String.valueOf(count++));

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

                layouts.data.addAll(new layoutsData(String.valueOf(count), String.valueOf(i + 1), sp2.getType(),
                        sp2.getStructure(), sp2.getWallType(), sp2.getWall(), sp2.getMaterial(), colorLabel,
                        NumberFormat.getNumberInstance(Locale.US).format(Double.valueOf(value)),
                        NumberFormat.getNumberInstance(Locale.US).format(Double.valueOf(sp2.getStud_height())),
                        NumberFormat.getNumberInstance(Locale.US).format(Double.valueOf(sp2.getUnit())),
                        NumberFormat.getNumberInstance(Locale.US).format(Double.valueOf(sp2.getLabour()))));
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
            stud_height = Float.parseFloat(JOptionPane.showInputDialog("Enter Stud height", 0.00 + " mm"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number.",
                    "Invalid Stud height value", JOptionPane.ERROR_MESSAGE);
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
        setStampCanvas();
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

    public void setStampCanvas() {
        stamp_canvas.setWidth(page.getImage().getWidth());
        stamp_canvas.setHeight(page.getImage().getHeight());
        GraphicsContext gc = stamp_canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, stamp_canvas.getWidth(), stamp_canvas.getHeight());
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
