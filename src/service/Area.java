package service;

import Model.PageObject;
import Model.ShapeObject;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 18/02/2020.
 */
public class Area implements IMeasurement {

    Tools tools;
    boolean isNew = true;
    Line line;
    Circle circle;
    Pane pane;
    Group group;
    PageObject page;
    static List box = new ArrayList();
    ArrayList<double[][]> snapList;
    ArrayList<Point2D> pointList = new ArrayList<>();
    Rectangle rect;
    double snapX = -1, snapY = -1;

    public Area(Tools tools) {
        this.tools = tools;
        this.line = tools.line;
        this.line.setStroke(tools.color);
        this.line.setStrokeWidth(5 / tools.group.getScaleY());
        this.circle = tools.circle;
        this.pane = tools.pane;
        this.group = tools.group;
        this.page = tools.page;
        this.snapList = tools.page.getSnapList();

        this.tools.pane.setOnMouseClicked(event -> {
            handleClick(event);
        });

        this.tools.pane.setOnMouseMoved(event -> {
            handleMouseMove(event);
        });
    }


    @Override
    public void handleClick(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {
            return;
        }
        Point2D clamp = tools.clamp(event.getX(), event.getY());
        if (isNew) {
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
            line.setVisible(true);
            line.setStrokeWidth(5 / group.getScaleY());
            line.setStroke(tools.color);
            drawBox(line.getEndX(), line.getEndY(), pane, group.getScaleX());
            isNew = false;
            snapX = snapY = -1;
        } else {
            if (snapX != -1 && snapY != -1) {
                line.setEndX(snapX);
                line.setEndY(snapY);
            }
            Point2D start = new Point2D(line.getStartX(), line.getStartY());
            Point2D end = new Point2D(line.getEndX(), line.getEndY());
            Point2D mid = start.midpoint(end);
            pointList.add(end);
            snapX = snapY = -1;
            DrawService.drawLine(line, pane, group.getScaleX());
            drawBox(line.getEndX(), line.getEndY(), pane, group.getScaleX());
            line.setStartX(line.getEndX());
            line.setStartY(line.getEndY());
            line.setEndX(clamp.getX());
            line.setEndY(clamp.getY());
        }
    }

    @Override
    public void handleMouseMove(MouseEvent event) {
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
                        if (snapX == -1 && snapY == -1) {
                            snapX = circle.getCenterX();
                            snapY = circle.getCenterY();
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

        Point2D cP = tools.clamp(event.getX(), event.getY());
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

    @Override
    public void handleFinish() {
        ShapeObject shapeObj = new ShapeObject();
        shapeObj.setPane(pane);
        shapeObj.setStrokeWidth(6 / group.getScaleY());
        shapeObj.setColor(tools.color);

        Point2D p2d = new Point2D(rect.getX() + rect.getHeight() / 2, rect.getY() + rect.getHeight() / 2);
        if (p2d != pointList.get(pointList.size() - 1)) {
            pointList.add(p2d);
        }

        shapeObj.setPointList(pointList);
        shapeObj.setType("AREA");
        page.getShapeList().add(shapeObj);

        tools.setMode("FREE");
        tools.updateWindow();
        pointList.clear();
    }

    @Override
    public void handleShowContextMenu(MouseEvent event) {

    }

    public void drawBox(double x, double y, Pane pane, double scale) {
        box.add(new Rectangle());
        Rectangle r = (Rectangle) box.get(0);
        r.setX(x - 5 / scale);
        r.setY(y - 5 / scale);
        r.setWidth(10 / scale);
        r.setHeight(10 / scale);
        r.setStroke(Color.GREEN);
        r.setOpacity(.5);
        r.setStrokeWidth(4 / scale);
        r.setFill(Color.TRANSPARENT);
        r.setOnMouseEntered(event -> {
            r.setStroke(Color.RED);
        });
        r.setOnMouseExited(event -> {
            r.setStroke(Color.GREEN);
        });
        r.setOnMouseReleased(event -> {
            if (!Tools.canDraw || event.getButton() == MouseButton.SECONDARY) {
                return;
            }
            this.rect = r;
            handleFinish();
        });
        pane.getChildren().add(r);
        box.clear();
    }
}
