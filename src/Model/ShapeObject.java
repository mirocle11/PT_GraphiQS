package Model;

import Controllers.workspaceController;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class ShapeObject {
    Shape shape;
    Polygon polygon;
    Color color;
    double strokeWidth;
    List<Line> lineList = new ArrayList<>();
    private ArrayList<Point2D> pointList = new ArrayList<>();
    List<Rectangle> boxList = new ArrayList<>();
    String name;
    String type;
    Pane pane;
    workspaceController controller;
    static ContextMenu contextMenu;
    boolean remStart = true;
    boolean remEnd = true;
    boolean segmentRemoved = false;


    public ShapeObject() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setPane(Pane pane) {
        this.pane = pane;
    }

    public void setController(workspaceController controller) {
        this.controller = controller;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setStrokeWidth(double strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public List<Line> getLineList() {
        return lineList;
    }

    public List<Point2D> getPointList() {
        System.out.println("GETTING POINTLIST OF WITH TYPE " + type);
        return this.pointList;
    }

    public void setPointList(ArrayList<Point2D> pointList) {
        pointList.forEach(point2D -> {
            this.pointList.add(point2D);
        });
        createLines(this.pointList);
        creatBoxes(this.pointList);
        createPolygon(this.pointList);
    }

    public List<Rectangle> getBoxList() {
        return boxList;
    }

    public void createLines(List<Point2D> point2DS) {
        lineList.clear();
        for (int x = 0; x < point2DS.size() - 1; x++) {
            lineList.add(new Line(point2DS.get(x).getX(), point2DS.get(x).getY(), point2DS.get(x + 1).getX(), point2DS.get(x + 1).getY()));
            Line l = lineList.get(lineList.size() - 1);
            l.setStrokeLineCap(StrokeLineCap.BUTT);
            l.setStroke(color);
            l.setStrokeWidth(strokeWidth);
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
                    if (type == "LENGTH") {
                        MenuItem removeLength = new MenuItem("REMOVE LENGTH");
                        removeLength.setOnAction(event1 -> {
                            controller.shapeObjList.remove(this);
                            controller.redrawShapes();
                        });
                        contextMenu.getItems().add(removeLength);
                    } else {
                        MenuItem removeArea = new MenuItem("REMOVE AREA");
                        removeArea.setOnAction(event1 -> {
                            controller.shapeObjList.remove(this);
                            controller.redrawShapes();
                        });
                        contextMenu.getItems().add(removeArea);
                    }
                    contextMenu.show(l, event.getScreenX(), event.getScreenY());
                }
            });
        }
    }

    public void creatBoxes(List<Point2D> point2DS) {
        boxList.clear();
        double boxW = strokeWidth * 3;
        int ctr = point2DS.size();
        System.out.println("ASA ANG BOX SA LINE "+ctr);
        for (int x = 0; x < ctr; x++) {

            boxList.add(new Rectangle(point2DS.get(x).getX() - boxW / 2, point2DS.get(x).getY() - boxW / 2, boxW, boxW));
            Rectangle r = boxList.get(boxList.size() - 1);
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
            int finalX = x;
            r.setOnMousePressed(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    contextMenu = new ContextMenu();
                    if (type == "LENGTH") {
                        MenuItem removeLength = new MenuItem("REMOVE LENGTH");
                        removeLength.setOnAction(event1 -> {
                            controller.shapeObjList.remove(this);
                            controller.redrawShapes();
                        });
                        contextMenu.getItems().add(removeLength);
                    } else {
                        MenuItem removeArea = new MenuItem("REMOVE AREA");
                        removeArea.setOnAction(event1 -> {
                            controller.shapeObjList.remove(this);
                            controller.redrawShapes();
                        });
                        contextMenu.getItems().add(removeArea);
                    }
                    contextMenu.show(r, event.getScreenX(), event.getScreenY());
                }

            });

        }
    }

    public void createPolygon(List<Point2D> point2DS) {
        this.polygon = new Polygon();
        for (int x = 0; x < point2DS.size() - 1; x++) {
            double px = point2DS.get(x).getX();
            double py = point2DS.get(x).getY();
            polygon.getPoints().add(px);
            polygon.getPoints().add(py);
        }
        polygon.setFill(color);
        polygon.setOpacity(.3);
    }
}

