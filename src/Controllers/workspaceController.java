package Controllers;

import Data.historyData;
import Main.Main;
import Model.PageObject;
import Model.ShapeObject;
import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerNextArrowBasicTransition;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.graphics.*;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static Data.sets.setsData.*;

public class workspaceController implements Initializable {

    public static AnchorPane mainPane;
    //buttons
    public JFXButton IMPORT, SAVE, SCALE, LENGTH, AREA, STAMP, structureToggle;
    public JFXHamburger hamburger;
    //checkbox
    public JFXCheckBox selectAllBox;

    //custom popups
    public AnchorPane preliminaryAndGeneralPane, foundationsPane, prestressedFloorsPane, blockOpeningsPane, blockWallsPane,
            floorPackingPane, subfloorPane, intFloorLev1Pane, intFloorLev2Pane, extOpeningsPane, intOpeningsPane,
            braceHardwarePane, braceSglLevPane, interTenancySectionPane, wetLiningsPane, wallStrappingPane, miscManufPane,
            postAndBeamHardwarePane, wallsSglLevPane, wallsBasementPane, wallsGndLevPane, wallsLev1Pane, wallsLev2Pane,
            wallsLev3Pane, wallsLev4Pane, bp_packersPane, wallHardwarePane, boltsManualPane, chimneyPane, trussesPane,
            roofPane, extLiningPane, rainScreenPane, wetCeilingsPane, ceilingsPane, cupboardsPane, showersAndBathsPane,
            decksPane, pergolaPane, miscellaniousPane, plumbingPane, bulkHeadsPane, windowSeatsPane, landscapingPane, fencingPane;

    public VBox preliminaryAndGeneralBox, foundationsBox, prestressedFloorsBox, blockOpeningsBox, blockWallsBox,
            floorPackingBox, subfloorBox, intFloorLev1Box, intFloorLev2Box, extOpeningsBox, intOpeningsBox,
            braceHardwareBox, braceSglLevBox, interTenancySectionBox, wetLiningsBox, wallStrappingBox, miscManufBox,
            postAndBeamHardwareBox, wallsSglLevBox, wallsBasementBox, wallsGndLevBox, wallsLev1Box, wallsLev2Box,
            wallsLev3Box, wallsLev4Box, bp_packersBox, wallHardwareBox, boltsManualBox, chimneyBox, trussesBox,
            roofBox, extLiningBox, rainScreenBox, wetCeilingsBox, ceilingsBox, cupboardsBox, showersAndBathsBox,
            decksBox, pergolaBox, miscellaniousBox, plumbingBox, bulkHeadsBox, windowSeatsBox, landscapingBox, fencingBox;

    //containers
    public AnchorPane frontPane, structurePane, shortListPane;
    public ScrollPane scroller, structureScrollPane;
    public Group scrollContent, group;
    public StackPane zoomPane;
    public Canvas canvas;
    public Pane pane;
    public JFXDrawer drawer;
    public VBox structureBox, shortListBox;
    public AnchorPane sectionPane;
    public JFXColorPicker colorPicker;
    public JFXComboBox<String> setsComboBox;

    public Image image;
    public ArrayList<ShapeObject> shapeObjList = new ArrayList<>();

    //temp shapes
    Line line = new Line();
    Rectangle rect = new Rectangle();

    //booleans
    private boolean isNew = true;
    private boolean canDraw = true;

    //collections
    Stack<Shape> undoHistory = new Stack<>();
    Stack<Shape> redoHistory = new Stack<>();
    List<Shape> shapeList = new ArrayList<>();
    ArrayList<Point2D> pointList = new ArrayList<>();
    ArrayList<Shape> stampList = new ArrayList<>();
    ArrayList<String> shortList = new ArrayList<>();
    ArrayList<PageObject> pageList = new ArrayList<>();

    int pageNumber = 0;
    int numberOfPages = 0;
    File pdfFile;
    PdfDocument doc;

    //others
    double SCALE_DELTA = 1.1;
    double m_Scale = 0;
    double origScaleX;
    double origScaleY;
    String mode;
    ContextMenu contextMenu = new ContextMenu();
    Color color;

    //indicator
    private int i = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        origScaleX = group.getScaleX();
        origScaleY = group.getScaleY();

        line.setVisible(false);
        line.setOpacity(.5);
        line.setStrokeLineCap(StrokeLineCap.BUTT);

        pane.getChildren().add(line);
        foundationsBox.getChildren().forEach(nodes -> {
            nodes.setOnMouseReleased(event -> {
                sectionPane.setVisible(true);
            });
        });
        scroller.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observable,
                                Bounds oldValue, Bounds newValue) {
                zoomPane.setMinSize(newValue.getWidth(), newValue.getHeight());
            }
        });

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
                } else {
                    drawer.toggle();
                }
            });
        } catch (Exception e) {
            Logger.getLogger(workspaceController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    //==FILE FUNCTIONS
    public void openFile(ActionEvent actionEvent) {
        FileChooser openFile = new FileChooser();
        openFile.setTitle("Open PDF");

        File temp = new File("");
        if (pdfFile != null) {
            temp = pdfFile;
        }
        pdfFile = openFile.showOpenDialog(Main.dashboard_stage);
        if (pdfFile == null) {
            pdfFile = temp;
            JOptionPane.showMessageDialog(null, "No File selected");
        } else {
            if (doc != null) {
                doc.close();
            }
            shapeObjList.clear();
            pageList.clear();
            stampList.clear();
            shapeList.clear();
            m_Scale = 0;

            String inputFile = pdfFile.getAbsolutePath();
            doc = new PdfDocument();
            doc.loadFromFile(inputFile);

            BufferedImage bImage = doc.saveAsImage(0, PdfImageType.Bitmap, 300, 300);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    BufferedImage imgs;
                    for (int x = 1; x < doc.getPages().getCount(); x++) {
                        imgs = doc.saveAsImage(x, PdfImageType.Bitmap, 300, 300);
                        Image img = SwingFXUtils.toFXImage(imgs, null);
                        pageList.add(new PageObject(x, img));
                    }
                }
            }).start();
            image = SwingFXUtils.toFXImage(bImage, null);
            pageList.add(new PageObject(0, image));
            pageList.get(0).setShapeObjList(shapeObjList);
            pageList.get(0).setStampList(stampList);
            numberOfPages = doc.getPages().getCount();
            setupCanvas();
            scroller.setOpacity(1.0);
            frontPane.setVisible(false);
        }
    }

    public void saveFile(ActionEvent actionEvent) {
        pageList.get(pageNumber).setShapeObjList(shapeObjList);
        group.setScaleX(1);
        group.setScaleY(1);

        FileChooser savefile = new FileChooser();
        savefile.setTitle("Save PDF");
        String pdfName = pdfFile.getName().replaceAll(".pdf", "");
        File file = savefile.showSaveDialog(Main.stage);
        if (file != null) {
            for (int ctr = 0; ctr < doc.getPages().getCount(); ctr++) {
                PdfPageBase page = doc.getPages().get(ctr);
                page.getCanvas().setTransparency(0.5f, 0.5f, PdfBlendMode.Normal);
                PdfGraphicsState state = page.getCanvas().save();
                page.getCanvas().translateTransform(0, 0);

                double subX = pageList.get(ctr).getImage().getWidth() / page.getSize().getWidth();
                double subY = pageList.get(ctr).getImage().getHeight() / page.getSize().getHeight();

                shapeObjList = pageList.get(ctr).getShapeList();
                shapeObjList.forEach(shapeObject -> {
                    PdfRGBColor pdfRGBColor = new PdfRGBColor(new java.awt.Color((float) shapeObject.getColor().getRed(), (float) shapeObject.getColor().getGreen(), (float) shapeObject.getColor().getBlue()));
                    PdfFont font = new PdfFont(PdfFontFamily.Helvetica, 8);
                    if (shapeObject.getType().equals("AREA")) {
                        PdfPen pen = new PdfPen(pdfRGBColor, 3);
                        int ndx = 0;
                        PdfBrush brush = new PdfSolidBrush(new PdfRGBColor(pdfRGBColor));
                        java.awt.geom.Point2D[] points = new java.awt.geom.Point2D[shapeObject.getPointList().size()];
                        for (Point2D p2d : shapeObject.getPointList()) {
                            System.out.println(p2d);
                            points[ndx] = new java.awt.geom.Point2D.Double(p2d.getX() / subX, (p2d.getY() / subY) - 1.8);
                            ndx++;
                        }
                        page.getCanvas().drawPolygon(brush, points);

                        PdfPen pens = new PdfPen(pdfRGBColor, 1);
                        double layX = shapeObject.getPolygon().getBoundsInParent().getMinX() + (shapeObject.getPolygon().getBoundsInParent().getWidth()) / 2;
                        double layY = shapeObject.getPolygon().getBoundsInParent().getMinY() + (shapeObject.getPolygon().getBoundsInParent().getHeight()) / 2;
                        page.getCanvas().drawString(shapeObject.getArea() + " m²", font, pens, (float) layX / subX, (float) layY / subY);

                    } else if (shapeObject.getType().equals("LENGTH")) {
                        PdfPen pens = new PdfPen(pdfRGBColor, 1);
                        shapeObject.getLineList().forEach(line1 -> {
                            Point2D p1 = new Point2D(line1.getStartX(), line1.getStartY());
                            Point2D p2 = new Point2D(line1.getEndX(), line1.getEndY());
                            Point2D mid = p1.midpoint(p2);
                            page.getCanvas().drawString(shapeObject.getLength() + " mm", font, pens, (float) mid.getX() / subX, (float) mid.getY() / subY);
                        });

                    }
                    shapeObject.getLineList().forEach(line1 -> {
                        PdfPen pen = new PdfPen(pdfRGBColor, 2);
                        page.getCanvas().drawLine(pen, line1.getStartX() / subX, (line1.getStartY() / subY) - 1.8, line1.getEndX() / subX, (line1.getEndY() / subY) - 1.8);
                    });
                    shapeObject.getPointList().forEach(point2D -> {
                        PdfPen pen = new PdfPen(PdfPens.getLightGreen().getColor(), 2);
                        page.getCanvas().drawRectangle(pen, (point2D.getX() / subX) - 5, (point2D.getY() / subY) - 6.8, 10, 10);
                    });
                });
                page.getCanvas().restore(state);
            }
            if (!file.getName().contains(".")) {
                file = new File(file.getAbsolutePath() + ".pdf");
                doc.saveToFile(file.getAbsolutePath());
            }
        } else {
            JOptionPane.showMessageDialog(null, "No File selected");
        }
    }

    //==SIDE BUTTON ACTIONS
    public void scaleAction(ActionEvent actionEvent) {
        mode = "SCALE";
        canDraw = true;
        isNew = true;
    }

    public void areaAction(ActionEvent actionEvent) {
        mode = "AREA";
        viewMeasurementList();
    }

    public void lengthAction(ActionEvent actionEvent) {
        mode = "LENGTH";
        viewMeasurementList();
    }

    public void rotateAction(ActionEvent actionEvent) {
        group.setRotate(group.getRotate() + 90);
    }

    public void nextPageAction(ActionEvent actionEvent) {
        pageList.get(pageNumber).setShapeObjList(shapeObjList);
        pageList.get(pageNumber).setStampList(stampList);
        if (pageNumber < numberOfPages - 1) {
            pageNumber++;
            setupPageElements();
        }
    }

    public void previousPageAction(ActionEvent actionEvent) {
        pageList.get(pageNumber).setShapeObjList(shapeObjList);
        pageList.get(pageNumber).setStampList(stampList);
        if (pageNumber > 0) {
            pageNumber--;
            setupPageElements();
        }
    }

    public void drawRectAction(ActionEvent actionEvent) {
        mode = "DRAW_RECT";
        canDraw = true;
        isNew = true;
    }

    public void stampAction() {
        mode = "STAMP";
        canDraw = true;
    }


    //==DRAW FUNCTIONS
    public void drawAction(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {
            return;
        }
        if (!canDraw) {
            return;
        }
        Point2D clamp = clamp(event.getX(), event.getY());

        if (mode == "SCALE") {
            if (isNew) {
                line.setVisible(true);
                line.setStartX(clamp.getX());
                line.setStartY(clamp.getY());
                line.setEndX(clamp.getX());
                line.setEndY(clamp.getY());
                line.setStroke(Color.CHOCOLATE);
                line.setStrokeWidth(8 / group.getScaleY());
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
                line.setStrokeWidth(8 / group.getScaleY());
                line.setStroke(color);
                pane.getChildren().add(createBox(line.getEndX(), line.getEndY()));
                line.setVisible(true);
                isNew = false;
            } else {
                Point2D end = new Point2D(line.getEndX(), line.getEndY());
                pointList.add(end);

                ShapeObject shapeObj = new ShapeObject();
                shapeObj.setPane(pane);
                shapeObj.setColor(color);
                shapeObj.setPointList(pointList);
                shapeObj.setController(this);
                shapeObj.setType(mode);

                shapeObjList.add(shapeObj);
                isNew = true;
                line.setVisible(false);
                canDraw = false;
                pointList.clear();
                redrawShapes();

                //==TODO add in History
                //workspaceSideNavigatorController.historyList.addAll(new historyData(mode, lbl.getText()));
            }
        } else if (mode == "AREA") {
            if (isNew) {
                line.setStartX(clamp.getX());
                line.setStartY(clamp.getY());
                line.setEndX(clamp.getX());
                line.setEndY(clamp.getY());
                pointList.add(clamp);
                line.setStrokeWidth(8 / group.getScaleY());
                line.setStroke(color);
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
        } else if (mode == "STAMP") {
            isNew = true;
            canDraw = false;
            Rectangle r = new Rectangle(clamp.getX(), clamp.getY(), 20 / group.getScaleY(), 20 / group.getScaleY());
            r.setFill(color);
            r.setOpacity(.5);
            stampList.add(r);
            pane.getChildren().addAll(r);
            redrawShapes();
            mode = "FREE_HAND";
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

        double boxW = 20;
        shapeList.add(new Rectangle(x - (10) / group.getScaleY(), y - (10) / group.getScaleY(), boxW / group.getScaleY(), boxW / group.getScaleY()));
        Rectangle r = (Rectangle) shapeList.get(shapeList.size() - 1);
        r.setStroke(Color.GREEN);
        r.setOpacity(.5);
        r.setStrokeWidth(6 / group.getScaleY());
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
            shapeObj.setController(this);
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
            workspaceSideNavigatorController.historyList.addAll(new historyData(mode, lbl.getText()));
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
        shapeList.add(new Line(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY()));
        Line l = (Line) shapeList.get(shapeList.size() - 1);

        l.setStroke(color);
        l.setStrokeLineCap(StrokeLineCap.BUTT);
        l.setStrokeWidth(8 / group.getScaleY());
        l.setOpacity(.5);

        l.setOnMouseEntered(event -> {
            l.setStroke(Color.RED);
        });
        l.setOnMouseExited(event -> {
            l.setStroke(color);
        });
        return shapeList.get(shapeList.size() - 1);
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
                        lbl.setFont(new Font("Arial", 36));
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
                    rect.setStrokeWidth(6 / group.getScaleY());
                    rect.setX(centerX - 10 / group.getScaleY());
                    rect.setY(centerY - 10 / group.getScaleY());
                    rect.setWidth(20 / group.getScaleY());
                    rect.setHeight(20 / group.getScaleY());
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

    //==PAGE FUNCTIONS
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
                            rect.setStrokeWidth(6 / group.getScaleY());
                            rect.setX(centerX - 10 / group.getScaleY());
                            rect.setY(centerY - 10 / group.getScaleY());
                            rect.setWidth(20 / group.getScaleY());
                            rect.setHeight(20 / group.getScaleY());
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
            System.out.println("Zoom " + group.getScaleY());
        }
    }

    private Point2D figureScrollOffset(Node scrollContent, ScrollPane scroller) {
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        double hScrollProportion = (scroller.getHvalue() - scroller.getHmin()) / (scroller.getHmax() - scroller.getHmin());
        double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        double vScrollProportion = (scroller.getVvalue() - scroller.getVmin()) / (scroller.getVmax() - scroller.getVmin());
        double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);
        return new Point2D(scrollXOffset, scrollYOffset);
    }

    private void repositionScroller(Node scrollContent, ScrollPane scroller, double scaleFactor, Point2D
            scrollOffset) {
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

    //==PAGE SETUPS
    public void setupCanvas() {
        group.setScaleY(1);
        group.setScaleX(1);
        canvas.setWidth(image.getWidth());
        canvas.setHeight(image.getHeight());
        pane.setPrefSize(canvas.getWidth(), canvas.getHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(image, 0, 0);
        origScaleX = group.getScaleX();
        origScaleY = group.getScaleY();
        double newScale = group.getScaleX();
        for (int x = 0; x < 12; x++) {
            newScale *= (1 / 1.1);
        }
        group.setScaleX(newScale);
        group.setScaleY(newScale);
        redrawShapes();
    }

    public void setupPageElements() {
        shapeObjList = new ArrayList<>();
        stampList = new ArrayList<>();
        try {
            shapeObjList.addAll(pageList.get(pageNumber).getShapeList());
            stampList.addAll(pageList.get(pageNumber).getStampList());
            image = pageList.get(pageNumber).getImage();
        } catch (Exception e) {
            BufferedImage bImage = doc.saveAsImage(pageNumber, PdfImageType.Bitmap, 300, 300);
            image = SwingFXUtils.toFXImage(bImage, null);
            pageList.add(new PageObject(pageNumber, image));
        }
        setupCanvas();
        redrawShapes();
    }

    //==MEASUREMENT POPUPS
    private void viewMeasurementList() {
        if (shortListPane.isVisible()) {
            shortListPane.setVisible(false);
            hideShortList();
            sectionPane.setVisible(false);
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
            structureBox.getChildren().forEach(node -> {
                ((JFXCheckBox) node).setSelected(true);
            });
        } else {
            structureBox.getChildren().forEach(node -> ((JFXCheckBox) node).setSelected(false));
        }
    }

    public void addStructure() {
        shortListBox.getChildren().removeAll();
        shortListBox.getChildren().clear();
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
                        if (!preliminaryAndGeneralPane.isVisible()) {
                            hideShortList();
                            preliminaryAndGeneralPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            preliminaryAndGeneralPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Foundations":
                        if (!foundationsPane.isVisible()) {
                            hideShortList();
                            foundationsPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            foundationsPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Prestressed Floors":
                        if (!prestressedFloorsPane.isVisible()) {
                            hideShortList();
                            prestressedFloorsPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            prestressedFloorsPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Block Openings":
                        if (!blockOpeningsPane.isVisible()) {
                            hideShortList();
                            blockOpeningsPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            blockOpeningsPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Block Walls":
                        if (!blockWallsPane.isVisible()) {
                            hideShortList();
                            blockWallsPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            blockWallsPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Floor Packing":
                        if (!floorPackingPane.isVisible()) {
                            hideShortList();
                            floorPackingPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            floorPackingPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Subfloor":
                        if (!subfloorPane.isVisible()) {
                            hideShortList();
                            subfloorPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            subfloorPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Int Floor Lev 1":
                        if (!intFloorLev1Pane.isVisible()) {
                            hideShortList();
                            intFloorLev1Pane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            intFloorLev1Pane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Int Floor Lev 2":
                        if (!intFloorLev2Pane.isVisible()) {
                            hideShortList();
                            intFloorLev2Pane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            intFloorLev2Pane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Ext Openings":
                        if (!extOpeningsPane.isVisible()) {
                            hideShortList();
                            extOpeningsPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            extOpeningsPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Int Openings":
                        if (!intOpeningsPane.isVisible()) {
                            hideShortList();
                            intOpeningsPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            intOpeningsPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Brace Hardware":
                        if (!braceHardwarePane.isVisible()) {
                            hideShortList();
                            braceHardwarePane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            braceHardwarePane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Brace Sgl Lev":
                        if (!braceSglLevPane.isVisible()) {
                            hideShortList();
                            braceSglLevPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            braceSglLevPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Inter-Tenancy Section":
                        if (!interTenancySectionPane.isVisible()) {
                            hideShortList();
                            interTenancySectionPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            interTenancySectionPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Wet Linings":
                        if (!wetLiningsPane.isVisible()) {
                            hideShortList();
                            wetLiningsPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wetLiningsPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Wall Strapping":
                        if (!wallStrappingPane.isVisible()) {
                            hideShortList();
                            wallStrappingPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wallStrappingPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Misc Manuf":
                        if (!miscManufPane.isVisible()) {
                            hideShortList();
                            miscManufPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            miscManufPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Post&Beam Hardware":
                        if (!postAndBeamHardwarePane.isVisible()) {
                            hideShortList();
                            postAndBeamHardwarePane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            postAndBeamHardwarePane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Walls Sgl Lev":
                        if (!wallsSglLevPane.isVisible()) {
                            hideShortList();
                            wallsSglLevPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wallsSglLevPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Walls Basement":
                        if (!wallsBasementPane.isVisible()) {
                            hideShortList();
                            wallsBasementPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wallsBasementPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Walls Gnd Lev":
                        if (!wallsGndLevPane.isVisible()) {
                            hideShortList();
                            wallsGndLevPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wallsGndLevPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Walls Lev 1":
                        if (!wallsLev1Pane.isVisible()) {
                            hideShortList();
                            wallsLev1Pane.setVisible(true);
                            sectionPane.setVisible(true);
                            wallsLev1Box.getChildren().forEach(node1 -> node.setOnMouseClicked(event1 -> {
                                showSets(((JFXButton) node).getText());
                            }));
                        } else {
                            wallsLev1Pane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Walls Lev 2":
                        if (!wallsLev2Pane.isVisible()) {
                            hideShortList();
                            wallsLev2Pane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wallsLev2Pane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Walls Lev 3":
                        if (!wallsLev3Pane.isVisible()) {
                            hideShortList();
                            wallsLev3Pane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wallsLev3Pane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Walls Lev 4":
                        if (!wallsLev4Pane.isVisible()) {
                            hideShortList();
                            wallsLev4Pane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wallsLev4Pane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "BP Packers":
                        if (!bp_packersPane.isVisible()) {
                            hideShortList();
                            bp_packersPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            bp_packersPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Wall Hardware":
                        if (!wallHardwarePane.isVisible()) {
                            hideShortList();
                            wallHardwarePane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wallHardwarePane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Bolts Manual":
                        if (!boltsManualPane.isVisible()) {
                            hideShortList();
                            boltsManualPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            boltsManualPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Chimney":
                        if (!chimneyPane.isVisible()) {
                            hideShortList();
                            chimneyPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            chimneyPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Trusses":
                        if (!trussesPane.isVisible()) {
                            hideShortList();
                            trussesPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            trussesPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Roof":
                        if (!roofPane.isVisible()) {
                            hideShortList();
                            roofPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            roofPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Ext Lining":
                        if (!extLiningPane.isVisible()) {
                            hideShortList();
                            extLiningPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            extLiningPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Rain Screen":
                        if (!rainScreenPane.isVisible()) {
                            hideShortList();
                            rainScreenPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            rainScreenPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Wet Ceilings":
                        if (!wetCeilingsPane.isVisible()) {
                            hideShortList();
                            wetCeilingsPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wetCeilingsPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Ceilings":
                        if (!ceilingsPane.isVisible()) {
                            hideShortList();
                            ceilingsPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            ceilingsPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Cupboards":
                        if (!cupboardsPane.isVisible()) {
                            hideShortList();
                            cupboardsPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            cupboardsPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Showers & Baths":
                        if (!showersAndBathsPane.isVisible()) {
                            hideShortList();
                            showersAndBathsPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            showersAndBathsPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Decks":
                        if (!decksPane.isVisible()) {
                            hideShortList();
                            decksPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            decksPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Pergola":
                        if (!pergolaPane.isVisible()) {
                            hideShortList();
                            pergolaPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            pergolaPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Miscellanious":
                        if (!miscellaniousPane.isVisible()) {
                            hideShortList();
                            miscellaniousPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            miscellaniousPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Plumbing":
                        if (!plumbingBox.isVisible()) {
                            hideShortList();
                            plumbingBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            plumbingBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Bulk Heads":
                        if (!bulkHeadsBox.isVisible()) {
                            hideShortList();
                            bulkHeadsBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            bulkHeadsBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Window Seats":
                        if (!windowSeatsBox.isVisible()) {
                            hideShortList();
                            windowSeatsBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            windowSeatsBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Landscaping":
                        if (!landscapingBox.isVisible()) {
                            hideShortList();
                            landscapingBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            landscapingBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Fencing":
                        if (!fencingBox.isVisible()) {
                            hideShortList();
                            fencingBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            fencingBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                }
                buttonsAction();
            });
        }
    }

    public void sectionDoneAction() {
        isNew = true;
        canDraw = true;
        color = colorPicker.getValue();

        hideShortList();
        shortListPane.setVisible(false);
        sectionPane.setVisible(false);
        setsComboBox.setItems(null);
    }

    private void hideShortList() {
        preliminaryAndGeneralPane.setVisible(false);
        foundationsPane.setVisible(false);
        prestressedFloorsPane.setVisible(false);
        blockOpeningsPane.setVisible(false);
        blockWallsPane.setVisible(false);
        floorPackingPane.setVisible(false);
        subfloorPane.setVisible(false);
        intFloorLev1Pane.setVisible(false);
        intFloorLev2Pane.setVisible(false);
        extOpeningsPane.setVisible(false);
        intOpeningsPane.setVisible(false);
        braceHardwarePane.setVisible(false);
        braceSglLevPane.setVisible(false);
        interTenancySectionPane.setVisible(false);
        wetLiningsPane.setVisible(false);
        wallStrappingPane.setVisible(false);
        miscManufPane.setVisible(false);
        postAndBeamHardwarePane.setVisible(false);
        wallsSglLevPane.setVisible(false);
        wallsBasementPane.setVisible(false);
        wallsGndLevPane.setVisible(false);
        wallsLev1Pane.setVisible(false);
        wallsLev2Pane.setVisible(false);
        wallsLev3Pane.setVisible(false);
        wallsLev4Pane.setVisible(false);
        bp_packersPane.setVisible(false);
        wallHardwarePane.setVisible(false);
        boltsManualPane.setVisible(false);
        chimneyPane.setVisible(false);
        trussesPane.setVisible(false);
        roofPane.setVisible(false);
        extLiningPane.setVisible(false);
        rainScreenPane.setVisible(false);
        wetCeilingsPane.setVisible(false);
        ceilingsPane.setVisible(false);
        cupboardsPane.setVisible(false);
        showersAndBathsPane.setVisible(false);
        decksPane.setVisible(false);
        pergolaPane.setVisible(false);
        miscellaniousPane.setVisible(false);
        plumbingPane.setVisible(false);
        bulkHeadsPane.setVisible(false);
        windowSeatsPane.setVisible(false);
        landscapingPane.setVisible(false);
        fencingPane.setVisible(false);
    }

    private void buttonsAction() {
        foundationsBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        prestressedFloorsBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        blockOpeningsBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        blockWallsBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        floorPackingBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        subfloorBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        intFloorLev1Box.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        intFloorLev2Box.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        extOpeningsBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        intOpeningsBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        braceHardwareBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        braceSglLevBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        interTenancySectionBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        wetLiningsBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        wallStrappingBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        miscManufBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        postAndBeamHardwareBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        wallsSglLevBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        wallsBasementBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        wallsLev1Box.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        wallsLev2Box.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        wallsLev3Box.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        wallsLev4Box.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        bp_packersBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        wallHardwareBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        boltsManualBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        chimneyBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        trussesBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        roofBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        extLiningBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        rainScreenBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        wetCeilingsBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        ceilingsBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        cupboardsBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        showersAndBathsBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        decksBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        pergolaBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        miscellaniousBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        plumbingBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        bulkHeadsBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        windowSeatsBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        landscapingBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
        fencingBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
    }

    private void showSets(String type) {
        //foundations
        if (type.equals("Post Footings") && foundationsBox.isVisible()) {
            setsComboBox.setItems(FOUNDATIONS_POST_FOOTINGS);
        } else if (type.equals("Concrete Bores") && foundationsBox.isVisible()) {
            setsComboBox.setItems(FOUNDATIONS_CONCRETE_BORES);
        } else if (type.equals("Footings") && foundationsBox.isVisible()) {
            setsComboBox.setItems(FOUNDATIONS_FOOTINGS);
        } else if (type.equals("Concrete Floor") && foundationsBox.isVisible()) {
            setsComboBox.setItems(FOUNDATIONS_CONCRETE_FLOOR);
        } else if (type.equals("Raft Piles") && foundationsBox.isVisible()) {
            setsComboBox.setItems(FOUNDATIONS_RAFT_PILES);
        } else if (type.equals("Raft Footings") && foundationsBox.isVisible()) {
            setsComboBox.setItems(FOUNDATIONS_RAFT_FOOTINGS);
        } else if (type.equals("Raft Slab") && foundationsBox.isVisible()) {
            setsComboBox.setItems(FOUNDATIONS_RAFT_SLAB);
        } else if (type.equals("Patio Footings") && foundationsBox.isVisible()) {
            setsComboBox.setItems(FOUNDATIONS_PATIO_FOOTINGS);
        } else if (type.equals("Patio Slab") && foundationsBox.isVisible()) {
            setsComboBox.setItems(FOUNDATIONS_PATIO_SLAB);
        } else if (type.equals("Carpot Footings") && foundationsBox.isVisible()) {
            setsComboBox.setItems(FOUNDATIONS_CARPOT_FOOTINGS);
        } else if (type.equals("Carpot Slab") && foundationsBox.isVisible()) {
            setsComboBox.setItems(FOUNDATIONS_CARPOT_SLAB);
        } else if (type.equals("Deck Footing") && foundationsBox.isVisible()) {
            setsComboBox.setItems(FOUNDATIONS_DECK_FOOTING);
        } else if (type.equals("Deck Slab") && foundationsBox.isVisible()) {
            setsComboBox.setItems(FOUNDATIONS_DECK_SLAB);
        } else if (type.equals("Wall Plates") && foundationsBox.isVisible()) {
            setsComboBox.setItems(FOUNDATIONS_WALL_PLATES);
        } else if (type.equals("Water Proofing") && foundationsBox.isVisible()) {
            setsComboBox.setItems(FOUNDATIONS_WATER_PROOFING);
        } else if (type.equals("Columns") && foundationsBox.isVisible()) {
            setsComboBox.setItems(FOUNDATIONS_COLUMNS);
        } else if (type.equals("Beams") && foundationsBox.isVisible()) {
            setsComboBox.setItems(FOUNDATIONS_BEAM);
        } else if (type.equals("Concrete Walls") && foundationsBox.isVisible()) {
            setsComboBox.setItems(FOUNDATIONS_CONCRETE_WALL);
        } else if (type.equals("Conc Steps") && foundationsBox.isVisible()) {
            setsComboBox.setItems(FOUNDATIONS_CONC_STEPS);
        } else if (type.equals("Block Cnrs & Ends") && foundationsBox.isVisible()) {
            setsComboBox.setItems(FOUNDATIONS_BLOCK_CNRS_ENDS);
        } else if (type.equals("Profiles") && foundationsBox.isVisible()) {
            setsComboBox.setItems(FOUNDATIONS_PROFILES);
        } else if (type.equals("Boxing") && foundationsBox.isVisible()) {
            setsComboBox.setItems(FOUNDATIONS_BOXING);
        }
        //prestressed floors
        else if (type.equals("Concrete Floor") && prestressedFloorsBox.isVisible()) {
            setsComboBox.setItems(PRESTRESSED_FLOORS_CONCRETE_FLOOR);
        } else if (type.equals("Concrete Deck") && prestressedFloorsBox.isVisible()) {
            setsComboBox.setItems(PRESTRESSED_FLOORS_CONCRETE_DECK);
        } else if (type.equals("Saddles") && prestressedFloorsBox.isVisible()) {
            setsComboBox.setItems(PRESTRESSED_FLOORS_SADDLES);
        } else if (type.equals("Beams") && prestressedFloorsBox.isVisible()) {
            setsComboBox.setItems(PRESTRESSED_FLOORS_BEAM);
        }
        //block openings
        else if (type.equals("Lintels") && blockOpeningsBox.isVisible()) {
            setsComboBox.setItems(BLOCK_OPENINGS_LINTELS);
        } else if (type.equals("Doors") && blockOpeningsBox.isVisible()) {
            setsComboBox.setItems(BLOCK_OPENINGS_DOORS);
        } else if (type.equals("Windows") && blockOpeningsBox.isVisible()) {
            setsComboBox.setItems(BLOCK_OPENINGS_WINDOWS);
        }
        //block walls
        else if (type.equals("Walls") && blockWallsBox.isVisible()) {
            setsComboBox.setItems(BLOCK_WALLS);
        } else if (type.equals("Cnrs & Ends") && blockWallsBox.isVisible()) {
            setsComboBox.setItems(BLOCK_WALLS_CNRS_ENDS);
        } else if (type.equals("Water Proofing") && blockWallsBox.isVisible()) {
            setsComboBox.setItems(BLOCK_WALLS_WATER_PROOFING);
        } else if (type.equals("Cols & Piers") && blockWallsBox.isVisible()) {
            setsComboBox.setItems(BLOCK_WALLS_COLS_PIERS);
        } else if (type.equals("C & P Measured Items") && blockWallsBox.isVisible()) {
            setsComboBox.setItems(BLOCK_WALLS_CP_MEASURED_ITEMS);
        } else if (type.equals("Conc Lintels") && blockWallsBox.isVisible()) {
            setsComboBox.setItems(BLOCK_WALLS_CONC_LINTELS);
        } else if (type.equals("Beams") && blockWallsBox.isVisible()) {
            setsComboBox.setItems(BLOCK_WALLS_BEAMS);
        } else if (type.equals("Wall Plates") && blockWallsBox.isVisible()) {
            setsComboBox.setItems(BLOCK_WALLS_PLATES);
        }
        //floor packing
        else if (type.equals("Conc Packing") && floorPackingBox.isVisible()) {
            setsComboBox.setItems(FLOOR_PACKING_CONC_PACKING);
        } else if (type.equals("Flooring") && floorPackingBox.isVisible()) {
            setsComboBox.setItems(FLOOR_PACKING_FLOORING);
        }
        //subfloor
        else if (type.equals("Pile Concrete Measured") && subfloorBox.isVisible()) {
            setsComboBox.setItems(SUBFLOOR_PILE_CONCRETE_MEASURED);
        } else if (type.equals("Pile Concrete Round") && subfloorBox.isVisible()) {
            setsComboBox.setItems(SUBFLOOR_CONCRETE_ROUND);
        } else if (type.equals("Concrete Block/Ring Wall") && subfloorBox.isVisible()) {
            setsComboBox.setItems(SUBFLOOR_CONCRETE_BLOCK_RING_WALL);
        } else if (type.equals("Subfloor Measured") && subfloorBox.isVisible()) {
            setsComboBox.setItems(SUBFLOOR_MEASURED);
        } else if (type.equals("Bracing") && subfloorBox.isVisible()) {
            setsComboBox.setItems(SUBFLOOR_BRACING);
        } else if (type.equals("Steel Beams") && subfloorBox.isVisible()) {
            setsComboBox.setItems(SUBFLOOR_STEEL_BEAMS);
        } else if (type.equals("Framing Measured") && subfloorBox.isVisible()) {
            setsComboBox.setItems(SUBFLOOR_FRAMING_MEASURED);
        } else if (type.equals("Flooring") && subfloorBox.isVisible()) {
            setsComboBox.setItems(SUBFLOOR_FLOORING);
        } else if (type.equals("Jack Framing") && subfloorBox.isVisible()) {
            setsComboBox.setItems(SUBFLOOR_JACK_FRAMING);
        } else if (type.equals("Base") && subfloorBox.isVisible()) {
            setsComboBox.setItems(SUBFLOOR_BASE);
        } else if (type.equals("Difficult Base") && subfloorBox.isVisible()) {
            setsComboBox.setItems(SUBFLOOR_DIFFICULT_BASE);
        } else if (type.equals("Base/Door Accessories") && subfloorBox.isVisible()) {
            setsComboBox.setItems(SUBFLOOR_BASE_DOOR_ACCESSORIES);
        }
        //int floor lev 1
        else if (type.equals("Framing Measured") && intFloorLev1Box.isVisible()) {
            setsComboBox.setItems(INT_FLOOR_LEV1_FRAMING_MEASURED);
        } else if (type.equals("Flooring") && intFloorLev1Box.isVisible()) {
            setsComboBox.setItems(INT_FLOOR_LEV1_FLOORING);
        } else if (type.equals("Landing Measured") && intFloorLev1Box.isVisible()) {
            setsComboBox.setItems(INT_FLOOR_LEV1_LANDING_MEASURED);
        } else if (type.equals("Floor Beams") && intFloorLev1Box.isVisible()) {
            setsComboBox.setItems(INT_FLOOR_LEV1_FLOOR_BEAMS);
        } else if (type.equals("Steel Beams") && intFloorLev1Box.isVisible()) {
            setsComboBox.setItems(INT_FLOOR_LEV1_STEEL_BEAMS);
        } else if (type.equals("Flitch Beams") && intFloorLev1Box.isVisible()) {
            setsComboBox.setItems(INT_FLOOR_LEV1_FLITCH_BEAMS);
        } else if (type.equals("LVL Beams") && intFloorLev1Box.isVisible()) {
            setsComboBox.setItems(INT_FLOOR_LEV1_LVL_BEAMS);
        } else if (type.equals("Int Enclosed Beams") && intFloorLev1Box.isVisible()) {
            setsComboBox.setItems(INT_FLOOR_LEV1_INT_ENCLOSED_BEAMS);
        } else if (type.equals("Attic Int Joists") && intFloorLev1Box.isVisible()) {
            setsComboBox.setItems(INT_FLOOR_LEV1_ATTIC_INT_JOISTS);
        } else if (type.equals("I Joists") && intFloorLev1Box.isVisible()) {
            setsComboBox.setItems(INT_FLOOR_LEV1_I_JOISTS);
        }
        //int floor lev 2
        else if (type.equals("Framing Measured") && intFloorLev2Box.isVisible()) {
            setsComboBox.setItems(INT_FLOOR_LEV2_FRAMING_MEASURED);
        } else if (type.equals("Flooring") && intFloorLev2Box.isVisible()) {
            setsComboBox.setItems(INT_FLOOR_LEV2_FLOORING);
        } else if (type.equals("Landing Measured") && intFloorLev2Box.isVisible()) {
            setsComboBox.setItems(INT_FLOOR_LEV2_LANDING_MEASURED);
        } else if (type.equals("Floor Beams") && intFloorLev2Box.isVisible()) {
            setsComboBox.setItems(INT_FLOOR_LEV2_FLOOR_BEAMS);
        } else if (type.equals("Steel Beams") && intFloorLev2Box.isVisible()) {
            setsComboBox.setItems(INT_FLOOR_LEV2_STEEL_BEAMS);
        } else if (type.equals("Flitch Beams") && intFloorLev2Box.isVisible()) {
            setsComboBox.setItems(INT_FLOOR_LEV2_FLITCH_BEAMS);
        } else if (type.equals("LVL Beams") && intFloorLev2Box.isVisible()) {
            setsComboBox.setItems(INT_FLOOR_LEV2_LVL_BEAMS);
        } else if (type.equals("Int Enclosed Beams") && intFloorLev2Box.isVisible()) {
            setsComboBox.setItems(INT_FLOOR_LEV2_INT_ENCLOSED_BEAMS);
        } else if (type.equals("LVL Joists") && intFloorLev2Box.isVisible()) {
            setsComboBox.setItems(INT_FLOOR_LEV2_INT_LVL_JOISTS);
        } else if (type.equals("I Joists") && intFloorLev2Box.isVisible()) {
            setsComboBox.setItems(INT_FLOOR_LEV2_I_JOISTS);
        }
        //ext openings
        else if (type.equals("Wind Hardware") && extOpeningsBox.isVisible()) {
            setsComboBox.setItems(EXT_OPENINGS_WIND_HARDWARE);
        } else if (type.equals("Garage Door") && extOpeningsBox.isVisible()) {
            setsComboBox.setItems(EXT_OPENINGS_GARAGE_DOOR);
        } else if (type.equals("Doors") && extOpeningsBox.isVisible()) {
            setsComboBox.setItems(EXT_OPENINGS_DOORS);
        } else if (type.equals("Windows") && extOpeningsBox.isVisible()) {
            setsComboBox.setItems(EXT_OPENINGS_WINDOWS);
        } else if (type.equals("Window Heads Standard") && extOpeningsBox.isVisible()) {
            setsComboBox.setItems(EXT_OPENINGS_WINDOW_HEADS_STANDARD);
        } else if (type.equals("Window Heads Raking") && extOpeningsBox.isVisible()) {
            setsComboBox.setItems(EXT_OPENINGS_WINDOW_HEADS_RAKING);
        } else if (type.equals("Catilever Lintels") && extOpeningsBox.isVisible()) {
            setsComboBox.setItems(EXT_OPENINGS_CANTILEVER_LINTELS);
        }
        //int openings
        else if (type.equals("Door Openings") && intOpeningsBox.isVisible()) {
            setsComboBox.setItems(INT_OPENINGS_DOOR_OPENINGS);
        } else if (type.equals("Windows") && intOpeningsBox.isVisible()) {
            setsComboBox.setItems(INT_OPENINGS_WINDOWS);
        } else if (type.equals("Doors Units") && intOpeningsBox.isVisible()) {
            setsComboBox.setItems(INT_OPENINGS_DOOR_UNITS);
        } else if (type.equals("Door Unit-SI-Bf") && intOpeningsBox.isVisible()) {
            setsComboBox.setItems(INT_OPENINGS_DOOR_UNIT_SL_BF);
        } else if (type.equals("Doors Unit Cav") && intOpeningsBox.isVisible()) {
            setsComboBox.setItems(INT_OPENINGS_DOORS_UNIT_CAV);
        } else if (type.equals("Doors Unit Gib") && intOpeningsBox.isVisible()) {
            setsComboBox.setItems(INT_OPENINGS_DOORS_UNIT_GIB);
        } else if (type.equals("Doors Exp STD") && intOpeningsBox.isVisible()) {
            setsComboBox.setItems(INT_OPENINGS_DOORS_EXP_STD);
        } else if (type.equals("Doors Exp BF Panel") && intOpeningsBox.isVisible()) {
            setsComboBox.setItems(INT_OPENINGS_DOORS_BF_PANEL);
        } else if (type.equals("Doors Exp BF Plain") && intOpeningsBox.isVisible()) {
            setsComboBox.setItems(INT_OPENINGS_DOORS_BF_PLAIN);
        } else if (type.equals("Doors Cavity") && intOpeningsBox.isVisible()) {
            setsComboBox.setItems(INT_OPENINGS_DOORS_CAVITY);
        }
        //brace hardware
        else if (type.equals("Hardware") && braceHardwareBox.isVisible()) {
            setsComboBox.setItems(BRACE_HARDWARE);
        }
        //brace hardware
        else if (type.equals("Hardware") && braceHardwareBox.isVisible()) {
            setsComboBox.setItems(BRACE_HARDWARE);
        }
        //brace sgl lev
        else if (type.equals("Walls") && braceSglLevBox.isVisible()) {
            setsComboBox.setItems(BRACE_SGL_LEV_WALLS);
        }
        //inter-tenancy section
        else if (type.equals("Walls") && interTenancySectionBox.isVisible()) {
            setsComboBox.setItems(INTER_TENANCY_SECTION_WALLS);
        }
        //wet linings
        else if (type.equals("Walls") && wetLiningsBox.isVisible()) {
            setsComboBox.setItems(WET_LININGS_WALLS);
        } else if (type.equals("Measured Items") && wetLiningsBox.isVisible()) {
            setsComboBox.setItems(WET_LININGS_MEASURED_ITEMS);
        }
        //wall strapping
        else if (type.equals("Linings") && wallStrappingBox.isVisible()) {
            setsComboBox.setItems(WALL_STRAPPING_LININGS);
        }
        //misc manuf
        else if (type.equals("Manual") && miscManufBox.isVisible()) {
            setsComboBox.setItems(MISC_MANUF_MANUAL);
        }
        //post & beam hardware/posts
        else if (type.equals("Posts") && postAndBeamHardwareBox.isVisible()) {
            setsComboBox.setItems(POST_BEAM_HARDWARE_POSTS);
        } else if (type.equals("Post Brackets") && postAndBeamHardwareBox.isVisible()) {
            setsComboBox.setItems(POST_BEAM_HARDWARE_POST_BRACKETS);
        } else if (type.equals("Beam N/Plates") && postAndBeamHardwareBox.isVisible()) {
            setsComboBox.setItems(POST_BEAM_HARDWARE_BEAM_N_PLATES);
        } else if (type.equals("Beam Connectors") && postAndBeamHardwareBox.isVisible()) {
            setsComboBox.setItems(POST_BEAM_HARDWARE_BEAM_CONNECTORS);
        }
        //walls sgl lev
        else if (type.equals("Misc") && wallsSglLevBox.isVisible()) {
            setsComboBox.setItems(WALLS_SGL_LEV_MISC);
        } else if (type.equals("Columns") && wallsSglLevBox.isVisible()) {
            setsComboBox.setItems(WALLS_SGL_LEV_COLUMNS);
        } else if (type.equals("Gables & Jack Frames") && wallsSglLevBox.isVisible()) {
            setsComboBox.setItems(WALLS_SGL_LEV_GABLES_JACK_FRAMES);
        } else if (type.equals("Handrails") && wallsSglLevBox.isVisible()) {
            setsComboBox.setItems(WALLS_SGL_LEV_HANDRAILS);
        } else if (type.equals("Steel Beams") && wallsSglLevBox.isVisible()) {
            setsComboBox.setItems(WALLS_SGL_LEV_STEEL_BEAMS);
        } else if (type.equals("Flitched Lintels") && wallsSglLevBox.isVisible()) {
            setsComboBox.setItems(WALLS_SGL_LEV_FLITCHED_LINTELS);
        } else if (type.equals("Measured Lintels") && wallsSglLevBox.isVisible()) {
            setsComboBox.setItems(WALLS_SGL_LEV_MEASURED_LINTELS);
        } else if (type.equals("Special Manufacturing") && wallsSglLevBox.isVisible()) {
            setsComboBox.setItems(WALLS_SGL_LEV_SPECIAL_MANUFACTURING);
        } else if (type.equals("Exterior") && wallsSglLevBox.isVisible()) {
            setsComboBox.setItems(WALLS_SGL_LEV_EXTERIOR);
        } else if (type.equals("Interior") && wallsSglLevBox.isVisible()) {
            setsComboBox.setItems(WALLS_SGL_LEV_INTERIOR);
        } else if (type.equals("Lintels Non SLs") && wallsSglLevBox.isVisible()) {
            setsComboBox.setItems(WALLS_SGL_LEV_LINTELS_NON_SLS);
        }  else if (type.equals("Studs Nogs Deduction") && wallsSglLevBox.isVisible()) {
            setsComboBox.setItems(WALLS_SGL_LEV_STUDS_NOGS_DEDUCTION);
        }
        //walls basement
        else if (type.equals("Misc") && wallsBasementBox.isVisible()) {
            setsComboBox.setItems(WALLS_BASEMENT_MISC);
        } else if (type.equals("Columns") && wallsBasementBox.isVisible()) {
            setsComboBox.setItems(WALLS_BASEMENT_COLUMNS);
        } else if (type.equals("Gables & Jack Frames") && wallsBasementBox.isVisible()) {
            setsComboBox.setItems(WALLS_BASEMENT_GABLES_JACK_FRAMES);
        } else if (type.equals("Handrails") && wallsBasementBox.isVisible()) {
            setsComboBox.setItems(WALLS_BASEMENT_HANDRAILS);
        } else if (type.equals("Steel Beams") && wallsBasementBox.isVisible()) {
            setsComboBox.setItems(WALLS_BASEMENT_STEEL_BEAMS);
        } else if (type.equals("Flitched Lintels") && wallsBasementBox.isVisible()) {
            setsComboBox.setItems(WALLS_BASEMENT_FLITCHED_LINTELS);
        } else if (type.equals("Measured Lintels") && wallsBasementBox.isVisible()) {
            setsComboBox.setItems(WALLS_BASEMENT_MEASURED_LINTELS);
        } else if (type.equals("Special Manufacturing") && wallsBasementBox.isVisible()) {
            setsComboBox.setItems(WALLS_BASEMENT_SPECIAL_MANUFACTURING);
        } else if (type.equals("Exterior") && wallsBasementBox.isVisible()) {
            setsComboBox.setItems(WALLS_BASEMENT_EXTERIOR);
        } else if (type.equals("Interior") && wallsBasementBox.isVisible()) {
            setsComboBox.setItems(WALLS_BASEMENT_INTERIOR);
        } else if (type.equals("Lintels Non SLs") && wallsBasementBox.isVisible()) {
            setsComboBox.setItems(WALLS_BASEMENT_LINTELS_NON_SLS);
        }  else if (type.equals("Studs Nogs Deduction") && wallsBasementBox.isVisible()) {
            setsComboBox.setItems(WALLS_BASEMENT_STUDS_NOGS_DEDUCTION);
        }
        //walls gnd lev
        else if (type.equals("Misc") && wallsGndLevBox.isVisible()) {
            setsComboBox.setItems(WALLS_GND_LEV_MISC);
        } else if (type.equals("Columns") && wallsGndLevBox.isVisible()) {
            setsComboBox.setItems(WALLS_GND_LEV_COLUMNS);
        } else if (type.equals("Gables & Jack Frames") && wallsGndLevBox.isVisible()) {
            setsComboBox.setItems(WALLS_GND_LEV_GABLES_JACK_FRAMES);
        } else if (type.equals("Handrails") && wallsGndLevBox.isVisible()) {
            setsComboBox.setItems(WALLS_GND_LEV_HANDRAILS);
        } else if (type.equals("Steel Beams") && wallsGndLevBox.isVisible()) {
            setsComboBox.setItems(WALLS_GND_LEV_STEEL_BEAMS);
        } else if (type.equals("Flitched Lintels") && wallsGndLevBox.isVisible()) {
            setsComboBox.setItems(WALLS_GND_LEV_FLITCHED_LINTELS);
        } else if (type.equals("Measured Lintels") && wallsGndLevBox.isVisible()) {
            setsComboBox.setItems(WALLS_GND_LEV_MEASURED_LINTELS);
        } else if (type.equals("Special Manufacturing") && wallsGndLevBox.isVisible()) {
            setsComboBox.setItems(WALLS_GND_LEV_SPECIAL_MANUFACTURING);
        } else if (type.equals("Exterior") && wallsGndLevBox.isVisible()) {
            setsComboBox.setItems(WALLS_GND_LEV_EXTERIOR);
        } else if (type.equals("Interior") && wallsGndLevBox.isVisible()) {
            setsComboBox.setItems(WALLS_GND_LEV_INTERIOR);
        } else if (type.equals("Lintels Non SLs") && wallsGndLevBox.isVisible()) {
            setsComboBox.setItems(WALLS_GND_LEV_LINTELS_NON_SLS);
        }  else if (type.equals("Studs Nogs Deduction") && wallsGndLevBox.isVisible()) {
            setsComboBox.setItems(WALLS_GND_LEV_STUDS_NOGS_DEDUCTION);
        }
        //walls lev 1
        else if (type.equals("Misc") && wallsLev1Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV1_MISC);
        } else if (type.equals("Columns") && wallsLev1Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV1_COLUMNS);
        } else if (type.equals("Gables & Jack Frames") && wallsLev1Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV1_GABLES_JACK_FRAMES);
        } else if (type.equals("Handrails") && wallsLev1Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV1_HANDRAILS);
        } else if (type.equals("Steel Beams") && wallsLev1Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV1_STEEL_BEAMS);
        } else if (type.equals("Flitched Lintels") && wallsLev1Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV1_FLITCHED_LINTELS);
        } else if (type.equals("Measured Lintels") && wallsLev1Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV1_MEASURED_LINTELS);
        } else if (type.equals("Special Manufacturing") && wallsLev1Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV1_SPECIAL_MANUFACTURING);
        } else if (type.equals("Exterior") && wallsLev1Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV1_EXTERIOR);
        } else if (type.equals("Interior") && wallsLev1Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV1_INTERIOR);
        } else if (type.equals("Lintels Non SLs") && wallsLev1Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV1_LINTELS_NON_SLS);
        } else if (type.equals("Studs Nogs Deduction") && wallsLev1Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV1_STUDS_NOGS_DEDUCTION);
        }
        //walls lev 2
        else if (type.equals("Misc") && wallsLev2Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV2_MISC);
        } else if (type.equals("Columns") && wallsLev2Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV2_COLUMNS);
        } else if (type.equals("Gables & Jack Frames") && wallsLev2Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV2_GABLES_JACK_FRAMES);
        } else if (type.equals("Handrails") && wallsLev2Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV2_HANDRAILS);
        } else if (type.equals("Steel Beams") && wallsLev2Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV2_STEEL_BEAMS);
        } else if (type.equals("Flitched Lintels") && wallsLev2Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV2_FLITCHED_LINTELS);
        } else if (type.equals("Measured Lintels") && wallsLev2Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV2_MEASURED_LINTELS);
        } else if (type.equals("Special Manufacturing") && wallsLev2Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV2_SPECIAL_MANUFACTURING);
        } else if (type.equals("Exterior") && wallsLev2Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV2_EXTERIOR);
        } else if (type.equals("Interior") && wallsLev2Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV2_INTERIOR);
        } else if (type.equals("Lintels Non SLs") && wallsLev2Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV2_LINTELS_NON_SLS);
        } else if (type.equals("Studs Nogs Deduction") && wallsLev2Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV2_STUDS_NOGS_DEDUCTION);
        }
        //walls lev 3
        else if (type.equals("Misc") && wallsLev3Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV3_MISC);
        } else if (type.equals("Columns") && wallsLev3Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV3_COLUMNS);
        } else if (type.equals("Gables & Jack Frames") && wallsLev3Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV3_GABLES_JACK_FRAMES);
        } else if (type.equals("Handrails") && wallsLev3Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV3_HANDRAILS);
        } else if (type.equals("Steel Beams") && wallsLev3Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV3_STEEL_BEAMS);
        } else if (type.equals("Flitched Lintels") && wallsLev3Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV3_FLITCHED_LINTELS);
        } else if (type.equals("Measured Lintels") && wallsLev3Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV3_MEASURED_LINTELS);
        } else if (type.equals("Special Manufacturing") && wallsLev3Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV3_SPECIAL_MANUFACTURING);
        } else if (type.equals("Exterior") && wallsLev3Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV3_EXTERIOR);
        } else if (type.equals("Interior") && wallsLev3Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV3_INTERIOR);
        } else if (type.equals("Lintels Non SLs") && wallsLev3Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV3_LINTELS_NON_SLS);
        } else if (type.equals("Studs Nogs Deduction") && wallsLev3Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV3_STUDS_NOGS_DEDUCTION);
        }
        //walls lev 4
        else if (type.equals("Misc") && wallsLev4Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV4_MISC);
        } else if (type.equals("Columns") && wallsLev4Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV4_COLUMNS);
        } else if (type.equals("Gables & Jack Frames") && wallsLev4Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV4_GABLES_JACK_FRAMES);
        } else if (type.equals("Handrails") && wallsLev4Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV4_HANDRAILS);
        } else if (type.equals("Steel Beams") && wallsLev4Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV4_STEEL_BEAMS);
        } else if (type.equals("Flitched Lintels") && wallsLev4Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV4_FLITCHED_LINTELS);
        } else if (type.equals("Measured Lintels") && wallsLev4Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV4_MEASURED_LINTELS);
        } else if (type.equals("Special Manufacturing") && wallsLev4Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV4_SPECIAL_MANUFACTURING);
        } else if (type.equals("Exterior") && wallsLev4Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV4_EXTERIOR);
        } else if (type.equals("Interior") && wallsLev4Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV4_INTERIOR);
        } else if (type.equals("Lintels Non SLs") && wallsLev4Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV4_LINTELS_NON_SLS);
        } else if (type.equals("Studs Nogs Deduction") && wallsLev4Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV4_STUDS_NOGS_DEDUCTION);
        }
        //bp packers
        else if (type.equals("Packers Concrete") && bp_packersBox.isVisible()) {
            setsComboBox.setItems(BP_PACKERS_CONCRETE);
        } else if (type.equals("Columns") && bp_packersBox.isVisible()) {
            setsComboBox.setItems(BP_PACKERS_CONCRETE_140_190);
        } else if (type.equals("Gables & Jack Frames") && bp_packersBox.isVisible()) {
            setsComboBox.setItems(BP_PACKERS_TIMBER);
        } else if (type.equals("Handrails") && bp_packersBox.isVisible()) {
            setsComboBox.setItems(BP_PACKERS_TIMBER_140_190);
        }
        //wall hardware
        else if (type.equals("Wall Hardware") && wallHardwareBox.isVisible()) {
            setsComboBox.setItems(WALL_HARDWARE);
        }
        //bolts manual
        else if (type.equals("Subfloor") && boltsManualBox.isVisible()) {
            setsComboBox.setItems(BOLTS_MANUAL_SUBFLOOR);
        } else if (type.equals("Framing") && boltsManualBox.isVisible()) {
            setsComboBox.setItems(BOLTS_MANUAL_FRAMING);
        }
        //chimney
        else if (type.equals("Chimney") && chimneyBox.isVisible()) {
            setsComboBox.setItems(CHIMNEY_FRAME);
        }
        //trusses
        else if (type.equals("Timber") && trussesBox.isVisible()) {
            setsComboBox.setItems(TRUSSES_TIMBER);
        } else if (type.equals("Hardware") && trussesBox.isVisible()) {
            setsComboBox.setItems(TRUSSES_HARDWARE);
        } else if (type.equals("Nail Plates") && trussesBox.isVisible()) {
            setsComboBox.setItems(TRUSSES_NAIL_PLATES);
        }
        //roof
        else if (type.equals("Gables Above Brick") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_GABLES_ABOVE_BRICK);
        } else if (type.equals("Steel Beams") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_STEEL_BEAMS);
        } else if (type.equals("Ceilings") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_CEILING_BEAMS);
        } else if (type.equals("Hardware") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_ROOF_BEAMS);
        } else if (type.equals("Soffit Beams") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_SOFFIT_BEAMS);
        } else if (type.equals("Fascia Beams") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_FASCIA_BEAMS);
        } else if (type.equals("Hip Beams") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_HIP_BEAMS);
        } else if (type.equals("Ridge Beams") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_RIDGE_BEAMS);
        } else if (type.equals("Valley Beams") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_VALLEY_BEAMS);
        } else if (type.equals("Nail Plates") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_TRUSSED_ROOF_MEASURED);
        } else if (type.equals("Skillion Measured") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_SKILLION_MEASURED);
        } else if (type.equals("Exposed Truss Member") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_EXPOSED_TRUSS_MEMBER);
        } else if (type.equals("Roof Ply/Substrate") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_PLY_SUBSTRATE);
        } else if (type.equals("Substrate Measured") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_SUBSTRATE_MEASURED);
        } else if (type.equals("Common Roof") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_COMMON_ROOF);
        } else if (type.equals("Gutters") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_GUTTERS);
        } else if (type.equals("Hips") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_HIPS);
        } else if (type.equals("Ridges") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_RIDGES);
        } else if (type.equals("Valleys") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_VALLEYS);
        } else if (type.equals("Verge") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_VERGE);
        } else if (type.equals("Standard Soffit") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_STANDARD_SOFFIT);
        } else if (type.equals("Difficult Soffits") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_DIFFICULT_SOFFITS);
        } else if (type.equals("Barge") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_BARGE);
        } else if (type.equals("Fascia") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_FASCIA);
        } else if (type.equals("Spouting Measured") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_SPOUTING_MEASURED);
        } else if (type.equals("Aprons") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_APRONS);
        } else if (type.equals("Change of Roof Pitch") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_CHANGE_OF_ROOF_PITCH);
        } else if (type.equals("Parapets") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_PARAPETS);
        } else if (type.equals("Skylight Walls") && roofBox.isVisible()) {
            setsComboBox.setItems(ROOF_SKYLIGHT_WALLS);
        }
        //ext lining
        else if (type.equals("Gnd Level") && extLiningBox.isVisible()) {
            setsComboBox.setItems(EXT_LINING_GND_LEVEL);
        } else if (type.equals("Gnd Ext Corners") && extLiningBox.isVisible()) {
            setsComboBox.setItems(EXT_LINING_EXT_CORNERS);
        } else if (type.equals("Gnd Enclosed Beams") && extLiningBox.isVisible()) {
            setsComboBox.setItems(EXT_ENCLOSED_BEAMS);
        } else if (type.equals("Upper Level") && extLiningBox.isVisible()) {
            setsComboBox.setItems(EXT_LINING_UPPER_LEVEL);
        } else if (type.equals("Upper Ext Corners") && extLiningBox.isVisible()) {
            setsComboBox.setItems(EXT_LINING_UPPER_EXT_CORNERS);
        } else if (type.equals("Upper Int Corners") && extLiningBox.isVisible()) {
            setsComboBox.setItems(EXT_LINING_UPPER_INT_CORNERS);
        } else if (type.equals("Gnd Measured Items") && extLiningBox.isVisible()) {
            setsComboBox.setItems(EXT_LINING_GND_MEASURED_ITEMS);
        } else if (type.equals("Upper Measured Items") && extLiningBox.isVisible()) {
            setsComboBox.setItems(EXT_LINING_UPPER_MEASURED_ITEMS);
        }
        //rain screen
        else if (type.equals("Bottom Starter") && rainScreenBox.isVisible()) {
            setsComboBox.setItems(RAIN_SCREEN_BOTTOM_STARTER);
        } else if (type.equals("Cladding Area") && rainScreenBox.isVisible()) {
            setsComboBox.setItems(RAIN_SCREEN_CLADDING_AREA);
        } else if (type.equals("Vertical Joints") && rainScreenBox.isVisible()) {
            setsComboBox.setItems(RAIN_SCREEN_VERTICAL_JOINTS);
        } else if (type.equals("Horizontal Joints") && rainScreenBox.isVisible()) {
            setsComboBox.setItems(RAIN_SCREEN_HORIZONTAL_JOINTS);
        } else if (type.equals("Corners") && rainScreenBox.isVisible()) {
            setsComboBox.setItems(RAIN_SCREEN_CORNERS);
        }
        //wet ceilings
        else if (type.equals("Ceiling") && wetCeilingsBox.isVisible()) {
            setsComboBox.setItems(WET_CEILINGS);
        }
        //ceilings
        else if (type.equals("Ceiling") && ceilingsBox.isVisible()) {
            setsComboBox.setItems(CEILINGS_CEILING);
        } else if (type.equals("Ceiling Joists") && ceilingsBox.isVisible()) {
            setsComboBox.setItems(CEILINGS_CEILING_JOISTS);
        } else if (type.equals("Int Enclosed Beams") && ceilingsBox.isVisible()) {
            setsComboBox.setItems(CEILINGS_INT_ENCLOSED_BEAMS);
        } else if (type.equals("Int Measured Sheets") && ceilingsBox.isVisible()) {
            setsComboBox.setItems(CEILINGS_INT_MEASURED_SHEETS);
        }
        //cupboards
        else if (type.equals("Shelving Measured") && cupboardsBox.isVisible()) {
            setsComboBox.setItems(CUPBOARDS_SHELVING_MEASURED);
        }
        //showers
        else if (type.equals("Showers & Bath Measured") && showersAndBathsBox.isVisible()) {
            setsComboBox.setItems(SHOWERS_BATH_MEASURED);
        }
        //decks
        else if (type.equals("Post Pile Footings") && decksBox.isVisible()) {
            setsComboBox.setItems(DECKS_POST_PILE_FOOTINGS);
        } else if (type.equals("Post Concrete Round") && decksBox.isVisible()) {
            setsComboBox.setItems(DECKS_POST_PILE_CONCRETE_ROUND);
        } else if (type.equals("Posts") && decksBox.isVisible()) {
            setsComboBox.setItems(DECKS_POSTS);
        } else if (type.equals("Post Hardware") && decksBox.isVisible()) {
            setsComboBox.setItems(DECKS_POST_HARDWARE);
        } else if (type.equals("Steel Beams") && decksBox.isVisible()) {
            setsComboBox.setItems(DECKS_STEEL_BEAMS);
        } else if (type.equals("Deck Beams") && decksBox.isVisible()) {
            setsComboBox.setItems(DECKS_DECK_BEAMS);
        } else if (type.equals("Subfloor Measured Items") && decksBox.isVisible()) {
            setsComboBox.setItems(DECKS_SUBFLOOR_MEASURED_ITEMS);
        } else if (type.equals("Bracings") && decksBox.isVisible()) {
            setsComboBox.setItems(DECKS_BRACING);
        } else if (type.equals("Framings Measured Items") && decksBox.isVisible()) {
            setsComboBox.setItems(DECKS_FRAMING_MEASURED_ITEMS);
        } else if (type.equals("Decking") && decksBox.isVisible()) {
            setsComboBox.setItems(DECKS_DECKING);
        } else if (type.equals("Layered Steps") && decksBox.isVisible()) {
            setsComboBox.setItems(DECKS_LAYERED_STEPS);
        } else if (type.equals("Deck Stairs & Gutter Frame") && decksBox.isVisible()) {
            setsComboBox.setItems(DECKS_DECK_STAIRS_GUTTER_FRAME);
        } else if (type.equals("Base") && decksBox.isVisible()) {
            setsComboBox.setItems(DECKS_BASE);
        } else if (type.equals("Base Measured Items") && decksBox.isVisible()) {
            setsComboBox.setItems(DECKS_BASE_MEASURED_ITEMS);
        } else if (type.equals("Handrail Measured Items") && decksBox.isVisible()) {
            setsComboBox.setItems(DECKS_HANDRAIL_MEASURED_ITEMS);
        } else if (type.equals("Handrails") && decksBox.isVisible()) {
            setsComboBox.setItems(DECKS_HANDRAILS);
        } else if (type.equals("Seat Measured Items") && decksBox.isVisible()) {
            setsComboBox.setItems(DECKS_SEAT_MEASURED_ITEMS);
        } else if (type.equals("Seats") && decksBox.isVisible()) {
            setsComboBox.setItems(DECKS_SEATS);
        }
        //pergola
        else if (type.equals("Post Pile Footings") && pergolaBox.isVisible()) {
            setsComboBox.setItems(PERGOLA_POST_FOOTINGS);
        } else if (type.equals("Post Concrete Round") && pergolaBox.isVisible()) {
            setsComboBox.setItems(PERGOLA_POST);
        } else if (type.equals("Posts") && pergolaBox.isVisible()) {
            setsComboBox.setItems(PERGOLA_MEASURED_ITEMS);
        }
        //misc
        else if (type.equals("Post Pile Footings") && miscellaniousBox.isVisible()) {
            setsComboBox.setItems(MISC_SUNDRIES);
        } else if (type.equals("Post Concrete Round") && miscellaniousBox.isVisible()) {
            setsComboBox.setItems(MISC_ELECTRICAL_APPLIANCES);
        } else if (type.equals("Posts") && miscellaniousBox.isVisible()) {
            setsComboBox.setItems(MISC_GARAGE_DOOR);
        } else if (type.equals("Posts") && miscellaniousBox.isVisible()) {
            setsComboBox.setItems(MISC_ENTRY_DOOR);
        }
        //plumbing
        else if (type.equals("Plumbingware") && plumbingBox.isVisible()) {
            setsComboBox.setItems(PLUMBING_PLUMBINGWARE);
        }
        //bulk heads
        else if (type.equals("Sides") && bulkHeadsBox.isVisible()) {
            setsComboBox.setItems(BULK_HEADS_SIDES);
        } else if (type.equals("Bottom") && bulkHeadsBox.isVisible()) {
            setsComboBox.setItems(BULK_BOTTOM);
        }
        //window seats
        else if (type.equals("Side") && windowSeatsBox.isVisible()) {
            setsComboBox.setItems(WINDOW_SEATS_SIDE);
        } else if (type.equals("Top") && windowSeatsBox.isVisible()) {
            setsComboBox.setItems(WINDOW_SEATS_TOP);
        }
        //landscaping
        else if (type.equals("Concrete to Posts") && landscapingBox.isVisible()) {
            setsComboBox.setItems(LANDSCAPING_CONCRETE_TO_POSTS);
        } else if (type.equals("Concrete Bores") && landscapingBox.isVisible()) {
            setsComboBox.setItems(LANDSCAPING_CONCRETE_BORES);
        } else if (type.equals("Water Proofing") && landscapingBox.isVisible()) {
            setsComboBox.setItems(LANDSCAPING_WATER_PROOFING);
        } else if (type.equals("Block walls/Footings") && landscapingBox.isVisible()) {
            setsComboBox.setItems(LANDSCAPING_BLOCK_RETAINING_WALLS_FOOTINGS);
        } else if (type.equals("Timber Retaining Wall") && landscapingBox.isVisible()) {
            setsComboBox.setItems(LANDSCAPING_RETAINING_WALL);
        }
        //fencing
        else if (type.equals("Fence Post Footings") && fencingBox.isVisible()) {
            setsComboBox.setItems(FENCING_FENCE_POST_FOOTINGS);
        } else if (type.equals("Fence Posts") && fencingBox.isVisible()) {
            setsComboBox.setItems(FENCING_FENCE_POSTS);
        } else if (type.equals("Fence Rails Pailings") && fencingBox.isVisible()) {
            setsComboBox.setItems(FENCING_RAILS_PAILINGS);
        } else if (type.equals("Fence Footing") && fencingBox.isVisible()) {
            setsComboBox.setItems(FENCING_FENCE_FOOTING);
        } else if (type.equals("Fence Block Walls") && fencingBox.isVisible()) {
            setsComboBox.setItems(FENCING_FENCE_BLOCK_WALLS);
        } else if (type.equals("Fence Block Cnrs & Ends") && fencingBox.isVisible()) {
            setsComboBox.setItems(FENCING_BLOCK_CNRS_ENDS);
        } else if (type.equals("Fence Cols & Piers") && fencingBox.isVisible()) {
            setsComboBox.setItems(FENCING_COLS_PIERS);
        } else if (type.equals("Fence C&P Measured Items") && fencingBox.isVisible()) {
            setsComboBox.setItems(FENCING_FENCE_CP_MEASURED_ITEMS);
        }

        else {
            setsComboBox.setItems(null);
        }
    }

}