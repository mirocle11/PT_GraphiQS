package Controllers;

import Data.HistoryData;
import Data.WallData;
import Main.Main;
import Model.PageObject;
import Model.ShapeObject;
import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerNextArrowBasicTransition;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import service.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class workspaceController implements Initializable {

    //buttons
    public JFXButton IMPORT, SAVE, SCALE, LENGTH, AREA, STAMP, RECTANGLE, ROTATE, structureToggle;
//    public JFXButton NEXT_PAGE, PREVIOUS_PAGE;
    public JFXHamburger hamburger;

    //combo box (for selection)
    public JFXComboBox wallsComboBox, typeComboBox, choicesComboBox, structureComboBox;
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

    //others 
    String mode;
    ContextMenu contextMenu = new ContextMenu();
    int pageNumber = 0;

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

    //create form
    private static Stage createProjectStage;
    private double xOffset = 0;
    private double yOffset = 0;

    private static ObservableList<String> selectedBoxes = FXCollections.observableArrayList();
    private ObservableList<String> newItemsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        wallData = new WallData(this);
        wallData.structureAction();
        wallData.wallsAction();
        wallData.typeAction();
        wallData.choicesAction();

        unselectAllAction();

        tools = new Tools(this);
        tools.setMode("FREE");

        Tools.enableButtons(new String[]{"IMPORT"}, toolsMenu);

        line.setVisible(false);
        line.setOpacity(.5);
        line.setStrokeLineCap(StrokeLineCap.BUTT);
        pane.getChildren().add(circle);
        circle.setFill(Color.RED);
        circle.setRadius(5);
        pane.getChildren().add(line);

        noScaleLabel.setText("The page is not scaled. No measurements can be taken.");
        noScaleLabel.setTextFill(Color.RED);

        scroller.viewportBoundsProperty().addListener((observable, oldValue, newValue) -> zoomPane.setMinSize(
                newValue.getWidth(), newValue.getHeight()));

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
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/Views/createProject.fxml"));
            AnchorPane pane = loader.load();

            //draggable pop up
            pane.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            pane.setOnMouseDragged(event -> {
                createProjectStage.setX(event.getScreenX() - xOffset);
                createProjectStage.setY(event.getScreenY() - yOffset);
            });

            Scene scene = new Scene(pane);
            scene.setFill(Color.TRANSPARENT);
            scene.getStylesheets().addAll(Main.class.getResource("/Views/CSS/style.css").toExternalForm());
            createProjectStage = new Stage();
            createProjectStage.setScene(scene);
            createProjectStage.initStyle(StageStyle.UNDECORATED);
            createProjectStage.initModality(Modality.APPLICATION_MODAL);
            createProjectStage.initStyle(StageStyle.TRANSPARENT);
            createProjectStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void openPdfFile() {
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

    public static void closeCreateProject() {
        createProjectStage.close();
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

    public void rotateAction() {
        group.setRotate(group.getRotate() + 90);
    }

    public void nextPageAction() {
        tools.nextPage();
    }

    public void previousPageAction() {
        tools.previousPage();
    }

    public void drawRectAction() {
        mode = "DRAW_RECT";
        canDraw = true;
        isNew = true;
    }

    public void stampAction() {
        mode = "STAMP";
        canDraw = true;
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
        if (mode == "DRAW_RECT") {
            if (!isNew) {
                rect.setWidth(cP.getX() - rect.getX());
                rect.setHeight(cP.getY() - rect.getY());
            }
        }
    }

    public void updateRect(MouseEvent event) {
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
            rect.setStroke(color);
            rect.setStrokeWidth(15);
            rect.setOnMouseEntered(event1 -> {
                rect.setStroke(Color.RED);
            });
            rect.setOnMouseExited(event1 -> {
                rect.setStroke(color);
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
        }
    }

    public Shape createBox(double x, double y) {

        double boxW = 10;
        shapeList.add(new Rectangle(x - (5) / group.getScaleY(), y - (5) / group.getScaleY(),
                boxW / group.getScaleY(), boxW / group.getScaleY()));
        Rectangle r = (Rectangle) shapeList.get(shapeList.size() - 1);
        r.setStroke(Color.GREEN);
        r.setOpacity(.5);
        r.setStrokeWidth(4 / group.getScaleY());
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
            shapeObj.setStrokeWidth(6 / group.getScaleY());
            shapeObj.setColor(color);
            Point2D p2d = new Point2D(r.getX() + r.getHeight() / 2, r.getY() + r.getHeight() / 2);

            if (p2d != pointList.get(pointList.size() - 1)) {
                pointList.add(p2d);
            }

            shapeObj.setPointList(pointList);
            shapeObj.setType(mode);

            shapeObjList.add(shapeObj);
            double area = 0;
            for (int xx = 0; xx < shapeObj.getPointList().size() - 1; xx++) {
                Point2D p1 = shapeObj.getPointList().get(xx);
                Point2D p2 = shapeObj.getPointList().get(xx + 1);
                area += ((p1.getX() / m_Scale) * (p2.getY() / m_Scale)) - ((p1.getY() / m_Scale) * (p2.getX() / m_Scale));
            }
            area = Math.abs(area / 2);
            area = area / 1000;

            Label lbl = new Label(Math.round(area * 100.0) / 100.0 + " m²");

            workspaceSideNavigatorController.historyList.addAll(new HistoryData(color.toString(), mode, lbl.getText()));
            redrawShapes();
            pointList.clear();
            LENGTH.setDisable(false);
            AREA.setDisable(false);
            scroller.setPannable(true);
            canDraw = false;
        });
        return shapeList.get(shapeList.size() - 1);
    }

    public void setLine(Point2D clamp) {
        if (snapX != -1 && snapY != -1) {
            line.setStartX(snapX);
            line.setStartY(snapY);
            line.setEndX(snapX);
            line.setEndY(snapY);
            Point2D snap = new Point2D(snapX, snapY);
            pointList.add(snap);
        } else {
            line.setStartX(clamp.getX());
            line.setStartY(clamp.getY());
            line.setEndX(clamp.getX());
            line.setEndY(clamp.getY());
            pointList.add(clamp);
        }
        line.setStrokeWidth(5 / group.getScaleY());
        line.setStroke(color);
        pane.getChildren().add(createBox(line.getEndX(), line.getEndY()));
        line.setVisible(true);
        isNew = false;
        snapX = snapY = -1;
    }

    public void redrawShapes() {
        pane.getChildren().clear();
        pane.getChildren().add(circle);
        circle.setVisible(false);

        if (pageObjects.get(pageNumber).getScale() == 0) {
            if (!pane.getChildren().contains(noScaleLabel)) {
                noScaleLabel.setLayoutX(15);
                noScaleLabel.setLayoutY(15);
                pane.getChildren().add(noScaleLabel);

                AREA.setDisable(true);
                STAMP.setDisable(true);
            }
        } else {
            pane.getChildren().remove(noScaleLabel);
            LENGTH.setDisable(false);
            AREA.setDisable(false);
            STAMP.setDisable(false);
            RECTANGLE.setDisable(false);
        }

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
                    if (p1.distance(p2) != 0) {
                        double length = (p1.distance(p2) / m_Scale);
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
                        lbl.setTextFill(sp1.getColor());
                        lbl.setOpacity(.5);
                        pane.getChildren().add(lbl);
                    }
                }
            }
            pane.getChildren().addAll(sp1.getLineList());
            pane.getChildren().addAll(sp1.getBoxList());
        }
        pane.getChildren().addAll(stampList);
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

}