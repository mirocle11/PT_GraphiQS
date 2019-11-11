package Controllers;
import Main.Main;
import Model.ShapeObject;
import Controllers.workspaceSideNavigatorController.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerNextArrowBasicTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class workspaceController implements Initializable {

    //buttons
    public JFXButton IMPORT, SAVE, SCALE, LENGTH, AREA, STAMP;
    public JFXHamburger hamburger;
    public JFXDrawer drawer;

    //containers
    public AnchorPane frontPane;
    public ScrollPane scroller;
    public Group scrollContent, group;
    public StackPane zoomPane;
    public Canvas canvas;
    public Pane pane;
    public Image image;
    public ColorPicker cpLine;

    //temp shapes
    Line line = new Line();
    Rectangle rect = new Rectangle();
    Circle circ = new Circle();
    Ellipse elps = new Ellipse();
    Polyline poline = new Polyline();

    //booleans
    boolean isNew = true;
    boolean canDraw = true;

    //collections
    Stack<Shape> undoHistory = new Stack();
    Stack<Shape> redoHistory = new Stack();
    List<Shape> shapeList = new ArrayList<>();
    ArrayList<Point2D> pointList = new ArrayList<>();
    ArrayList<ShapeObject> shapeObjList = new ArrayList<>();

    //others
    double SCALE_DELTA = 1.1;
    double m_Scale = 0;
    double origScaleX;
    double origScaleY;
    String mode;
    ContextMenu contextMenu = new ContextMenu();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        scroller.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observable,
                                Bounds oldValue, Bounds newValue) {
                zoomPane.setMinSize(newValue.getWidth(), newValue.getHeight());
            }
        });

        try {
            VBox box = FXMLLoader.load(getClass().getResource("../Views/workspaceSideNavigation.fxml"));
            drawer.setSidePane(box);

            HamburgerNextArrowBasicTransition burgerTask = new HamburgerNextArrowBasicTransition(hamburger);
            burgerTask.setRate(-1);
            hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED,(e) ->{
                burgerTask.setRate(burgerTask.getRate()*-1);
                burgerTask.play();

                if (drawer.isOpened()) {
                    drawer.close();
                }
                else {
                    drawer.toggle();
                }
            });
        } catch(Exception e) {
            Logger.getLogger(workspaceController.class.getName()).log(Level.SEVERE,null, e);
        }
    }

    //=====GETTING SCROLL CHANGE WHE ZOOM=====//
    private Point2D figureScrollOffset(Node scrollContent, ScrollPane scroller) {
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        double hScrollProportion = (scroller.getHvalue() - scroller.getHmin()) / (scroller.getHmax() - scroller.getHmin());
        double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        double vScrollProportion = (scroller.getVvalue() - scroller.getVmin()) / (scroller.getVmax() - scroller.getVmin());
        double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);
        return new Point2D(scrollXOffset, scrollYOffset);
    }

    //=====FOR MOUSE POINT ZOOM=====//
    private void repositionScroller(Node scrollContent, ScrollPane scroller, double scaleFactor, Point2D scrollOffset) {
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

    //=====STICKING POINTS TO IMAGE BOUNDARIES=====//
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
            }
        }
    }

    public void updateLine(MouseEvent event) {
        if (!isNew) {
            Point2D cP = clamp(event.getX(), event.getY());
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

    //=====REPAINT ALL SHAPES====//
    public void redrawShapes() {
        pane.getChildren().clear();
        for (ShapeObject sp0 : shapeObjList) {
            if (sp0.getType() == "AREA") {
                pane.getChildren().add(sp0.getPolygon());
                double area = 0;

                for (int x = 0; x < sp0.getPointList().size() - 1; x++) {
                    Point2D p1 = sp0.getPointList().get(x);
                    Point2D p2 = sp0.getPointList().get(x + 1);
                    area += ((p1.getX() / m_Scale) * (p2.getY() / m_Scale)) - ((p1.getY() / m_Scale) * (p2.getX() / m_Scale));
                }
                area = Math.abs(area / 2);
                area = area / 1000;

                Label lbl = new Label(Math.round(area * 100.0) / 100.0 + " m²");
                lbl.setFont(new Font("Arial", 24));
                lbl.setTextFill(sp0.getColor());

                double layX = sp0.getPolygon().getBoundsInParent().getMinX() + (sp0.getPolygon().getBoundsInParent().getWidth() - lbl.getWidth()) / 2;
                double layY = sp0.getPolygon().getBoundsInParent().getMinY() + (sp0.getPolygon().getBoundsInParent().getHeight() - lbl.getHeight()) / 2;
                System.out.println(lbl.getWidth() + " LBL GET WIDTH");
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

        for (ShapeObject sp1 : shapeObjList) {
            if (sp1.getType() == "LENGTH") {
                for (Line l : sp1.getLineList()) {
                    Point2D p1 = new Point2D(l.getStartX(), l.getStartY());
                    Point2D p2 = new Point2D(l.getEndX(), l.getEndY());
                    Point2D mid = p1.midpoint(p2);
                    line.getRotate();
                    System.out.println("ANGLE " + p1.angle(p2) + " ROTATE " + line.getRotate());
                    if (p1.distance(p2) != 0) {
                        double length = (p1.distance(p2) / m_Scale);
                        Label lbl = new Label(Math.round(length * 100.0) / 100.0 + " mm");
                        lbl.setFont(new Font("Arial", 24));
                        lbl.setLayoutY(mid.getY());
                        lbl.setLayoutX(mid.getX());
                        lbl.setOnMouseEntered(event -> {
                            lbl.setStyle("-fx-background-color: white");
                            lbl.setOpacity(1);
                        });
                        lbl.setOnMouseExited(event -> {
                            lbl.setStyle("-fx-background-color: transparent");
                            lbl.setOpacity(.5);
                        });
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
    }

    //=====CREATING BOX OUTLINE=====//
    public Shape createBox(double x, double y) {

        double boxW = workspaceSideNavigatorController.slider.getValue() * 3;
        shapeList.add(new Rectangle(x - boxW / 2, y - boxW / 2, boxW, boxW));
        Rectangle r = (Rectangle) shapeList.get(shapeList.size() - 1);
        r.setStroke(Color.GREEN);
        r.setOpacity(.5);
        r.setStrokeWidth(boxW / 5);
        r.setFill(Color.TRANSPARENT);

        r.setOnMouseEntered(event -> {
            r.setStroke(Color.RED);
        });
        r.setOnMouseExited(event -> {
            r.setStroke(Color.GREEN);
        });

        r.setOnMouseReleased(event -> {
            if (!canDraw) {
                return;
            }

            canDraw = false;
            ShapeObject shapeObj = new ShapeObject();
            shapeObj.setPane(pane);
            shapeObj.setStrokeWidth(workspaceSideNavigatorController.slider.getValue());
            shapeObj.setColor(cpLine.getValue());
            Point2D p2d = new Point2D(r.getX() + r.getHeight() / 2, r.getY() + r.getHeight() / 2);

            if (p2d != pointList.get(pointList.size() - 1)) {
                pointList.add(p2d);
            }

            shapeObj.setPointList(pointList);
            shapeObj.setController(this);
            shapeObj.setType(mode);
            shapeObjList.add(shapeObj);
            redrawShapes();
            pointList.clear();
            LENGTH.setDisable(false);
            AREA.setDisable(false);
            scroller.setPannable(true);
        });
        return shapeList.get(shapeList.size() - 1);
    }

    //=====CREATING LINES=====//
    public Shape createLine(Line param) {
        Color color = cpLine.getValue();
        shapeList.add(new Line(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY()));
        Line l = (Line) shapeList.get(shapeList.size() - 1);

        l.setStroke(color);
        l.setStrokeLineCap(StrokeLineCap.BUTT);
        l.setStrokeWidth(workspaceSideNavigatorController.slider.getValue());
        l.setOpacity(.5);

        l.setOnMouseEntered(event -> {
            l.setStroke(Color.RED);
        });
        l.setOnMouseExited(event -> {
            l.setStroke(color);
        });
        l.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                contextMenu = new ContextMenu();
                MenuItem item = new MenuItem("REMOVE SEGMENT");
                item.setOnAction(event1 -> {
                    System.out.println("SEGMENT REMOVED");
                    pane.getChildren().remove(l);
                    undoHistory.push(l);
                });
                contextMenu.getItems().add(item);
                contextMenu.show(l, event.getScreenX(), event.getScreenY());
            }
        });

        return shapeList.get(shapeList.size() - 1);
    }

    //=====START DRAW (MOUSE RELEASED)=====//
    public void drawAction(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {
            return;
        }
        if (!canDraw) {
            return;
        }
        Point2D clamp = clamp(event.getX(), event.getY());
        line.setOpacity(.5);
        line.setStrokeLineCap(StrokeLineCap.BUTT);
        if (mode == "SCALE") {
            if (isNew) {
                line.setStartX(clamp.getX());
                line.setStartY(clamp.getY());
                line.setEndX(clamp.getX());
                line.setEndY(clamp.getY());
                line.setStroke(Color.CHOCOLATE);
                line.setStrokeWidth(5);
                line.setVisible(true);
                pane.getChildren().add(createBox(line.getEndX(), line.getEndY()));
                isNew = false;
            } else {
                pane.getChildren().add(createBox(line.getEndX(), line.getEndY()));
                Point2D start = new Point2D(line.getStartX(), line.getStartY());
                Point2D end = new Point2D(line.getEndX(), line.getEndY());
                try {
                    double m_input = Float.parseFloat(JOptionPane.showInputDialog("Enter Scale (mm)", 0.00 + " mm"));
                    double m_Length = start.distance(end);
                    m_Scale = m_Length / m_input;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number.", "Invalid Scale", JOptionPane.ERROR_MESSAGE);
                }
                redrawShapes();
                isNew = true;
                canDraw = false;
                scroller.setPannable(true);
                mode = "FREE_HAND";
            }
        } else if (mode != "FREE_HAND") {
            if (isNew) {
//                line.setStartX(clamp.getX());
//                line.setStartY(clamp.getY());
//                line.setEndX(clamp.getX());
//                line.setEndY(clamp.getY());
//                pointList.add(clamp);
//                line.setStrokeWidth(workspaceSideNavigatorController.slider.getValue());
//                line.setStroke(cpLine.getValue());
//                pane.getChildren().add(createBox(line.getEndX(), line.getEndY()));
//                line.setVisible(true);
//                isNew = false;
            } else {

                Point2D start = new Point2D(line.getStartX(), line.getStartY());
                Point2D end = new Point2D(line.getEndX(), line.getEndY());
                Point2D mid = start.midpoint(end);
                System.out.println(line.contains(mid) + " line contains");
                pointList.add(end);
                if (mode == "LENGTH") {

                    double length = (start.distance(end) / m_Scale);
                    Label lbl = new Label(Math.round(length * 100.0) / 100.0 + " mm");
                    lbl.setFont(new Font("Arial", 24));
                    lbl.setLayoutY(mid.getY());
                    lbl.setLayoutX(mid.getX());
                    lbl.setTextFill(cpLine.getValue());
                    lbl.setOpacity(.5);
                    pane.getChildren().add(lbl);
                }

                pane.getChildren().add(createLine(line));
                pane.getChildren().add(createBox(line.getEndX(), line.getEndY()));
                undoHistory.push(createLine(line));
                line.setStartX(line.getEndX());
                line.setStartY(line.getEndY());
                line.setEndX(clamp.getX());
                line.setEndY(clamp.getY());
            }
        }
    }

    public void openFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(Main.dashboard_stage);
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(null, "No Image selected");
        } else {
            scroller.setOpacity(1.0);
            frontPane.setVisible(false);

            image = new Image("file:" + selectedFile);
            canvas.setWidth(image.getWidth());
            canvas.setHeight(image.getHeight());
            pane.setPrefSize(canvas.getWidth(), canvas.getHeight());
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.drawImage(image, 0, 0);
            origScaleX = group.getScaleX();
            origScaleY = group.getScaleY();
            System.out.println(group.getBoundsInParent());
            System.out.println(group.getBoundsInLocal());
            System.out.println(group.getScaleX() + " " + group.getScaleY());

            double newScale = group.getScaleX();
            for (int x = 0; x < 12; x++) {
                newScale *= (1 / 1.1);
            }
            group.setScaleX(newScale);
            group.setScaleY(newScale);
        }
    }

    //=====SCALE ACTION====//
    public void scaleAction(ActionEvent actionEvent) {
        mode = "SCALE";
        canDraw = true;
    }

    //=====AREA ACTION====//
    public void areaAction(ActionEvent actionEvent) {
        cpLine.setValue(null);
//        cpLine.relocate(AREA.getWidth() + 20, (vbox.getHeight() / 2) + lengthbtn.getHeight());
        cpLine.show();
        cpLine.setOnHidden(event -> {
            if (cpLine.getValue() != null) {
                LENGTH.setDisable(true);
                canDraw = true;
                isNew = true;
            } else {
                LENGTH.setDisable(false);
            }
        });
        mode = "AREA";
    }

    public void handlePan(MouseEvent mouseEvent) {
    }
}
