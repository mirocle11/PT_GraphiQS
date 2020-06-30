package Model;

import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import service.Tools;

import java.util.ArrayList;
import java.util.List;

public class CladdingObject {

    Polygon polygon;
    private Pane pane;
    private Color color;
    private ArrayList<Point2D> pointList = new ArrayList<>();
    private Tools tools;
    private double strokeWidth;
    private double length = 0;
    private List<Line> lineList = new ArrayList<>();
    private List<Rectangle> boxList = new ArrayList<>();
    private String cladding_name;
    ContextMenu contextMenu;

    public void createLines(List<Point2D> point2DS) {
        lineList.clear();
        for (int x = 0; x < point2DS.size() - 1; x++) {
            lineList.add(new Line(point2DS.get(x).getX(), point2DS.get(x).getY(), point2DS.get(x + 1).getX(), point2DS.get(x + 1).getY()));
            Line l = lineList.get(lineList.size() - 1);
            l.setStrokeLineCap(StrokeLineCap.BUTT);
            l.setStroke(color);
            l.setStrokeWidth(strokeWidth);
            l.setOpacity(.8);
            l.setOnMouseEntered(event -> {
                l.setStroke(Color.RED);
            });
            l.setOnMouseExited(event -> {
                l.setStroke(color);
            });
            l.setOnMousePressed(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    contextMenu = new ContextMenu();
                    contextMenu.hide();
                        MenuItem removeLength = new MenuItem("Remove Cladding");
                        removeLength.setOnAction(event1 -> {
                            tools.page.claddingObjectList.remove(this);
                            tools.updateWindow();
                        });
                    contextMenu.getItems().add(removeLength);
                    contextMenu.show(l, event.getScreenX(), event.getScreenY());
                }
            });
        }
    }

    public void creatBoxes(List<Point2D> point2DS) {
        boxList.clear();
        double boxW = strokeWidth * 3;
        int ctr = point2DS.size();
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
            r.setOnMousePressed(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    contextMenu = new ContextMenu();
                    MenuItem removeLength = new MenuItem("Remove Length");
                    removeLength.setOnAction(event1 -> {
                        tools.page.claddingObjectList.remove(this);
                        tools.updateWindow();
                    });
                    MenuItem continueLength = new MenuItem("Continue Length");
                    continueLength.setOnAction(event1 -> {
                        tools.page.claddingObjectList.remove(this);
                        tools.updateWindow();
                    });

                    contextMenu.getItems().add(removeLength);
                    contextMenu.getItems().add(continueLength);

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

    public void setTools(Tools tools) {
        this.tools = tools;
    }

    public void setStrokeWidth(double strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public Pane getPane() {
        return pane;
    }

    public void setPane(Pane pane) {
        this.pane = pane;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public ArrayList<Point2D> getPointList() {
        return pointList;
    }

    public void setPointList(ArrayList<Point2D> pointLists) {
        this.pointList.clear();
        pointLists.forEach(point2D -> {
            this.pointList.add(point2D);
        });
        createLines(this.pointList);
        creatBoxes(this.pointList);
        createPolygon(this.pointList);
    }

    public Tools getTools() {
        return tools;
    }

    public double getStrokeWidth() {
        return strokeWidth;
    }

    public List<Line> getLineList() {
        return lineList;
    }

    public void setLineList(List<Line> lineList) {
        this.lineList = lineList;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public List<Rectangle> getBoxList() {
        return boxList;
    }

    public void setBoxList(List<Rectangle> boxList) {
        this.boxList = boxList;
    }

    public String getCladding_name() {
        return cladding_name;
    }

    public void setCladding_name(String cladding_name) {
        this.cladding_name = cladding_name;
        System.out.println("setting cladding name : "+cladding_name);
    }
}
