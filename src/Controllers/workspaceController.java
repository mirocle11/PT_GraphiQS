package Controllers;

import Main.Main;
import Model.ShapeObject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerNextArrowBasicTransition;
import javafx.animation.TranslateTransition;
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
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class workspaceController implements Initializable {

    //buttons
    public JFXButton IMPORT, SAVE, SCALE, LENGTH, AREA, STAMP, structureToggle;
    public JFXHamburger hamburger;

    //checkbox
    public JFXCheckBox selectAllBox;

    //containers
    public AnchorPane frontPane, structurePane, shortListPane, preliminaryAndGeneralBox, foundationsBox, prestressedFloorsBox;
    public ScrollPane scroller, structureScrollPane;
    public Group scrollContent, group;
    public StackPane zoomPane;
    public Canvas canvas;
    public Pane pane;
    public JFXDrawer drawer;
    public VBox structureBox, shortListBox;

    public Image image;
    public ArrayList<ShapeObject> shapeObjList = new ArrayList<>();

    //temp shapes
    Line line = new Line();
    Rectangle rect = new Rectangle();

    //booleans
    boolean isNew = true;
    boolean canDraw = true;

    //collections
    Stack<Shape> undoHistory = new Stack();
    Stack<Shape> redoHistory = new Stack();
    List<Shape> shapeList = new ArrayList<>();
    ArrayList<Point2D> pointList = new ArrayList<>();

    //others
    double SCALE_DELTA = 1.1;
    double m_Scale = 0;
    double origScaleX;
    double origScaleY;
    String mode;
    ContextMenu contextMenu = new ContextMenu();

    //indicator
    private int i = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        origScaleX = group.getScaleX();
        origScaleY = group.getScaleY();
        pane.getChildren().add(line);
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
            hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
                burgerTask.setRate(burgerTask.getRate() * -1);
                burgerTask.play();

                if (drawer.isOpened()) {
                    drawer.close();
                } else {
                    drawer.toggle();
                }
            });
        } catch (Exception e) {
            Logger.getLogger(workspaceController.class.getName()).log(Level.SEVERE, null, e);
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

                //===========CHANGES=======================================================
                pane.getChildren().forEach(node -> {
                    if (node.getClass().getSuperclass() == Shape.class) {
                        Shape sp = (Shape) node;
                        System.out.println(10 / group.getScaleY());
                        sp.setStrokeWidth(10 / group.getScaleY());


                    }
                    if (node.getClass() == Label.class) {
                        Label lbl = (Label) node;
                        lbl.setFont(new Font("Arial", 18 / group.getScaleY()));
                    }
                });
            }
        }
    }

    public void updateLine(MouseEvent event) {
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
//===========CHANGES=======================================================
        if (mode == "DRAW_RECT") {
            if (!isNew) {
                rect.setWidth(cP.getX() - rect.getX());
                rect.setHeight(cP.getY() - rect.getY());
            }
        }
//===========CHANGES=======================================================
    }

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
                lbl.setFont(new Font("Arial", 36));
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
                        lbl.setFont(new Font("Arial", 36));
                        lbl.setLayoutY(mid.getY());
                        lbl.setLayoutX(mid.getX());
                        lbl.setOnMouseEntered(event -> {
                            lbl.setStyle("-fx-background-color: white");
                            lbl.setOpacity(1);
                        });
//                        lbl.setL
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
        pane.getChildren().forEach(node -> {
            if (node.getClass().getSuperclass() == Shape.class) {
                Shape sp = (Shape) node;
                System.out.println(10 / group.getScaleY());
                sp.setStrokeWidth(10 / group.getScaleY());


            }
            if (node.getClass() == Label.class) {
                Label lbl = (Label) node;
                lbl.setFont(new Font("Arial", 18 / group.getScaleY()));
            }
        });
    }

    public Shape createBox(double x, double y) {

        double boxW = 10 * 3;
        shapeList.add(new Rectangle(x - (boxW / 2) / group.getScaleY(), y - (boxW / 2) / group.getScaleY(), boxW / group.getScaleY(), boxW / group.getScaleY()));
        Rectangle r = (Rectangle) shapeList.get(shapeList.size() - 1);
        r.setStroke(Color.GREEN);
        r.setOpacity(.5);
        r.setStrokeWidth((boxW / 5) / group.getScaleY());
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

            ShapeObject shapeObj = new ShapeObject();
            shapeObj.setPane(pane);
            shapeObj.setStrokeWidth(10 / group.getScaleY());
            shapeObj.setColor(Color.BLUE);
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
            canDraw = false;
        });
        return shapeList.get(shapeList.size() - 1);
    }

    public Shape createLine(Line param) {
        Color color = Color.BLUE;
        shapeList.add(new Line(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY()));
        Line l = (Line) shapeList.get(shapeList.size() - 1);

        l.setStroke(color);
        l.setStrokeLineCap(StrokeLineCap.BUTT);
        l.setStrokeWidth(10 / group.getScaleY());
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
                MenuItem item = new MenuItem("REMOVE LENGTH");
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
                line.setVisible(true);
                line.setStartX(clamp.getX());
                line.setStartY(clamp.getY());
                line.setEndX(clamp.getX());
                line.setEndY(clamp.getY());
                line.setStroke(Color.CHOCOLATE);
                line.setStrokeWidth(15 / group.getScaleY());
                line.setVisible(true);
                pane.getChildren().add(createBox(line.getEndX(), line.getEndY()));
                isNew = false;
            } else {
                pane.getChildren().add(createBox(line.getEndX(), line.getEndY()));
                Point2D start = new Point2D(line.getStartX(), line.getStartY());
                Point2D end = new Point2D(line.getEndX(), line.getEndY());
                try {
                    double m_input = Float.parseFloat(JOptionPane.showInputDialog("Enter Scale (mm)", 0.00 + " mm"));
                    if (m_input <= 0) {
                        throw new NumberFormatException();
                    }
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

        } else if (mode == "LENGTH") {
            if (isNew) {
                line.setStartX(clamp.getX());
                line.setStartY(clamp.getY());
                line.setEndX(clamp.getX());
                line.setEndY(clamp.getY());
                pointList.add(clamp);
                line.setStrokeWidth(10 / group.getScaleY());
                line.setStroke(Color.BLUE);
                pane.getChildren().add(createBox(line.getEndX(), line.getEndY()));
                line.setVisible(true);
                isNew = false;
            } else {
                Point2D start = new Point2D(line.getStartX(), line.getStartY());
                Point2D end = new Point2D(line.getEndX(), line.getEndY());
                Point2D mid = start.midpoint(end);
                System.out.println(line.contains(mid) + " line contains");
                pointList.add(end);

                double length = (start.distance(end) / m_Scale);
                Label lbl = new Label(Math.round(length * 100.0) / 100.0 + " mm");
                lbl.setFont(new Font("Arial", 18 / group.getScaleY()));
                lbl.setLayoutY(mid.getY());
                lbl.setLayoutX(mid.getX());
                lbl.setTextFill(Color.BLUE);
                lbl.setOpacity(.5);
                pane.getChildren().add(lbl);
                pane.getChildren().add(createBox(line.getEndX(), line.getEndY()));
                pane.getChildren().add(createLine(line));
                ShapeObject shapeObj = new ShapeObject();
                shapeObj.setPane(pane);
                shapeObj.setStrokeWidth(10 / group.getScaleY());
                shapeObj.setColor(Color.BLUE);
                shapeObj.setPointList(pointList);
                shapeObj.setController(this);
                shapeObj.setType(mode);
                shapeObjList.add(shapeObj);
                isNew = true;
                line.setVisible(false);
                canDraw = false;
                pointList.clear();
                redrawShapes();

            }
        } else if (mode == "AREA") {
            if (isNew) {
                line.setStartX(clamp.getX());
                line.setStartY(clamp.getY());
                line.setEndX(clamp.getX());
                line.setEndY(clamp.getY());
                pointList.add(clamp);
                line.setStrokeWidth(10 / group.getScaleY());
                line.setStroke(Color.BLUE);
                pane.getChildren().add(createBox(line.getEndX(), line.getEndY()));
                line.setVisible(true);
                isNew = false;
            } else {
                Point2D start = new Point2D(line.getStartX(), line.getStartY());
                Point2D end = new Point2D(line.getEndX(), line.getEndY());
                Point2D mid = start.midpoint(end);
                pointList.add(end);

                pane.getChildren().add(createLine(line));
                pane.getChildren().add(createBox(line.getEndX(), line.getEndY()));
                line.setStartX(line.getEndX());
                line.setStartY(line.getEndY());
                line.setEndX(clamp.getX());
                line.setEndY(clamp.getY());
            }
        } else if (mode == "DRAW_RECT") {
            isNew = true;
            canDraw = false;
            ShapeObject sp = new ShapeObject();
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
        isNew = true;
    }

    //=====AREA ACTION====//
    public void areaAction(ActionEvent actionEvent) {
        mode = "AREA";
        isNew = true;
        canDraw = true;
    }

    public void rotateAction(ActionEvent actionEvent) {
        group.setRotate(group.getRotate() + 90);
    }

    public void drawRectangle(ActionEvent actionEvent) {
        mode = "DRAW_RECT";
        canDraw = true;
        isNew = true;
    }

    public void drawRect(MouseEvent event) {
        Point2D clamp = clamp(event.getX(), event.getY());
        if (mode == "DRAW_RECT") {
            if (!canDraw) {
                return;
            }
            if (event.getButton() == MouseButton.SECONDARY) {
                return;
            }
            line.setVisible(false);
            rect = new Rectangle();
            rect.setX(clamp.getX());
            rect.setY(clamp.getY());
            rect.setWidth(10);
            rect.setHeight(10);
            rect.setOpacity(.5);
            rect.setStroke(Color.BLUE);
            rect.setStrokeWidth(15);
            rect.setOnMouseEntered(event1 -> {
                rect.setStroke(Color.RED);
            });
            rect.setOnMouseExited(event1 -> {
                rect.setStroke(Color.BLUE);
            });
            rect.setOnMousePressed(event1 -> {
                if (event1.getButton() == MouseButton.SECONDARY) {
                    contextMenu = new ContextMenu();
                    MenuItem removeLength = new MenuItem("REMOVE FILL");
                    removeLength.setOnAction(event12 -> {
                        pane.getChildren().remove(rect);
                    });
                    contextMenu.getItems().add(removeLength);
                }
                contextMenu.show(rect, event1.getScreenX(), event1.getScreenY());
            });

            pane.getChildren().add(rect);
            isNew = false;
        } else if (mode == "STAMP") {
            Rectangle r = new Rectangle();
            r.setX(clamp.getX());
            r.setY(clamp.getY());
            r.setWidth(5 / group.getScaleY());
            r.setWidth(5 / group.getScaleY());
            pane.getChildren().add(r);
            System.out.println("ADD STAMP");
        }
    }

    public void handlePan(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            scroller.setPannable(false);
        } else {
            scroller.setPannable(true);
        }
    }

    public void stampAction() {
        mode = "STAMP";
        canDraw = true;
        System.out.println("STAMPING");
    }

    //measurements popup
    public void viewMeasurementList() {
        if (shortListPane.isVisible()) {
            shortListPane.setVisible(false);

            preliminaryAndGeneralBox.setVisible(false);
            foundationsBox.setVisible(false);

            isNew = true;
            canDraw = true;
            mode = "LENGTH";
        } else {
            shortListPane.setVisible(true);
        }
    }

    public void structureListTransition() {
        if (i == 0) {
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
            i++;
        } else {
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
            i--;
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
        i--;
    }

    public void selectAll() {
        if (selectAllBox.isSelected()) {
            structureBox.getChildren().forEach(node -> ((JFXCheckBox) node).setSelected(true));
        } else {
            structureBox.getChildren().forEach(node -> ((JFXCheckBox) node).setSelected(false));
        }
    }

    public void addStructure() {
        structureBox.getChildren().forEach(this::accept);
        closeMeasurementList();
        }

        private void accept(Node node) {
            if (((JFXCheckBox) node).isSelected()) {
                String box = ((JFXCheckBox) node).getText();
                JFXButton button = new JFXButton(box);
                shortListBox.getChildren().add(button);

                button.setOnMouseClicked(event -> {
                    switch (button.getText()) {
                        case "Preliminary & General":
                            if (!preliminaryAndGeneralBox.isVisible()) {
                                preliminaryAndGeneralBox.setVisible(true);
                                foundationsBox.setVisible(false);
                                prestressedFloorsBox.setVisible(false);
                            } else {
                                preliminaryAndGeneralBox.setVisible(false);
                            }
                            break;
                        case "Foundations":
                            if (!foundationsBox.isVisible()) {
                                foundationsBox.setVisible(true);
                                preliminaryAndGeneralBox.setVisible(false);
                                prestressedFloorsBox.setVisible(false);
                            } else {
                                foundationsBox.setVisible(false);
                            }
                            break;
                        case "Prestressed Floors":
                            if (!prestressedFloorsBox.isVisible()) {
                                prestressedFloorsBox.setVisible(true);
                                preliminaryAndGeneralBox.setVisible(false);
                                foundationsBox.setVisible(false);
                            } else {
                                prestressedFloorsBox.setVisible(false);
                            }
                            break;
                    }
            });
        }
    }
}
