package service;

import Controllers.workspaceController;
import Model.PageObject;
import Model.ShapeObject;
import com.jfoenix.controls.JFXButton;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by User on 17/02/2020.
 */
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
    AnchorPane frontPane;
    List shape = new ArrayList();
    Color color;
    ContextMenu contextMenu;
    int pageNumber = 0;
    static boolean canDraw = false;
    boolean issetCanvas = false;

    workspaceController window;
    List<Shape> shapeList = new ArrayList<>();
    ArrayList<Point2D> pointList = new ArrayList<>();
    ArrayList<Shape> stampList = new ArrayList<>();
    ArrayList<PageObject> pageObjects = new ArrayList<>();
    ArrayList<double[][]> snapList = new ArrayList<>();
    public ArrayList<ShapeObject> shapeObjList = new ArrayList<>();


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
        this.frontPane = window.frontPane;
        this.box = window.toolsMenu;
        this.group = window.group;
        this.contextMenu = new ContextMenu();

        this.circle = new Circle();
        this.circle.setFill(Color.RED);
        this.circle.setRadius(5);

        this.line = new Line();
        this.line.setVisible(false);
        this.line.setOpacity(.5);
        this.line.setStrokeLineCap(StrokeLineCap.BUTT);

        this.noScale = new Label();
        this.noScale.setText("The page is not scaled. No measurements can be taken.");
        this.noScale.setTextFill(Color.RED);

        this.circle.setFill(Color.RED);
        this.circle.setRadius(5);
        this.pane.getChildren().add(circle);

    }

    //File Controlls
    public void open() {
        File pdf = FileService.open();
        PdfToImageService service = new PdfToImageService(pdf);
        service.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                pageObjects.add(newValue);
                if (!issetCanvas) {
                    page = newValue;
                    setPageElements();
                    Tools.disableButtons(new String[]{"LENGTH", "AREA", "STAMP", "RECTANGLE"}, box);
                    scroller.setOpacity(1.0);
                    frontPane.setVisible(false);
                    issetCanvas = true;
                }
            }
        });
        service.start();
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
                                circle.setRadius(3 / group.getScaleY());
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
                disableButtons(new String[]{"LENGTH", "AREA", "STAMP", "RECTANGLE"}, box);
            }
        } else {
            pane.getChildren().remove(noScale);
            disableButtons(new String[]{}, box);
        }

        for (ShapeObject sp0 : page.getShapeList()) {
            if (sp0.getType() == "AREA") {
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
                    lbl.setOpacity(.5);
                });
                lbl.setLayoutX(layX);
                lbl.setLayoutY(layY);
                lbl.setOpacity(.5);
                pane.getChildren().add(lbl);
            }
        }
        for (ShapeObject sp1 : page.getShapeList()) {
            if (sp1.getType() == "LENGTH") {
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
                            lbl.setOpacity(.5);
                        });
//                        lbl.setOnMousePressed(event -> {
//                            if (event.getButton() == MouseButton.SECONDARY) {
//                                contextMenu = new ContextMenu();
//                                contextMenu.hide();
//                                if (type == "LENGTH") {
//                                    MenuItem continueLength = new MenuItem("CONTINUE LENGTH");
//                                    continueLength.setOnAction(event1 -> {
//                                        controller.shapeObjList.remove(this);
//                                        controller.redrawShapes();
//                                    });
//                                    MenuItem removeLength = new MenuItem("REMOVE LENGTH");
//                                    removeLength.setOnAction(event1 -> {
//                                        controller.shapeObjList.remove(this);
//                                        controller.redrawShapes();
//                                    });
//                                    MenuItem stop = new MenuItem("STOP LENGTH");
//                                    removeLength.setOnAction(event1 -> {
//                                        controller.shapeObjList.remove(this);
//                                        controller.redrawShapes();
//                                    });
//                                    contextMenu.getItems().add(continueLength);
//                                    contextMenu.getItems().add(stop);
//                                    contextMenu.getItems().add(removeLength);
//                                } else {
//                                    MenuItem removeArea = new MenuItem("REMOVE AREA");
//                                    removeArea.setOnAction(event1 -> {
//                                        controller.shapeObjList.remove(this);
//                                        controller.redrawShapes();
//                                    });
//
//                                    contextMenu.getItems().add(removeArea);
//
//                                }
//                                contextMenu.show(l, event.getScreenX(), event.getScreenY());
//                            }
//                        });
                        lbl.setTextFill(sp1.getColor());
                        lbl.setOpacity(.5);
                        pane.getChildren().add(lbl);
                    }
                }
            }
            pane.getChildren().addAll(sp1.getLineList());
            pane.getChildren().addAll(sp1.getBoxList());
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
    }

    public void previousPage() {
        if (pageNumber > 0) {
            pageNumber--;
            setPageElements();
            setMode("FREE");
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

    //button controlls
    public static void disableButtons(String[] btns, VBox param) {
        param.getChildren().forEach(node -> {
            JFXButton btn = (JFXButton) node;
            btn.setDisable(false);
            for (String s : btns) {
                if (btn.getId().equals(s)) {
                    btn.setDisable(true);
                }
            }
        });
    }

    public static void enableButtons(String[] btns, VBox param) {
        param.getChildren().forEach(node -> {
            JFXButton btn = (JFXButton) node;
            btn.setDisable(true);
            for (String s : btns) {
                if (btn.getId().equals(s)) {
                    btn.setDisable(false);
                }
            }
        });
    }
}
