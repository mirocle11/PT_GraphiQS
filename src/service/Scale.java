package service;

import Model.PageObject;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.util.*;


/**
 * Created by User on 17/02/2020.
 */
public class Scale implements IMeasurement {
    Tools tools;
    boolean isNew = true;
    Line line;
    Circle circle;
    Pane pane;
    Group group;
    PageObject page;
    ArrayList<double[][]> snapList;
    double snapX = -1, snapY = -1;

    public Scale(Tools tools) {
        this.tools = tools;
        this.line = tools.line;
        this.line.setStroke(Color.CHOCOLATE);
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
            line.setStartX(clamp.getX());
            line.setStartY(clamp.getY());
            line.setEndX(clamp.getX());
            line.setEndY(clamp.getY());
            if (snapX != -1 && snapY != -1) {
                line.setStartX(snapX);
                line.setStartY(snapY);
                line.setEndX(snapX);
                line.setEndY(snapY);
            }

            line.setVisible(true);
            DrawService.drawBox(clamp.getX(), clamp.getY(), pane, group.getScaleX());
            snapX = snapY = -1;
            isNew = false;
        } else {
            if (snapX != -1 && snapY != -1) {
                line.setEndX(snapX);
                line.setEndY(snapY);
            }
            Point2D start = new Point2D(line.getStartX(), line.getStartY());
            Point2D end = new Point2D(line.getEndX(), line.getEndY());
            try {
                DrawService.drawBox(line.getEndX(), line.getEndY(), pane, group.getScaleX());
                double m_input = Float.parseFloat(JOptionPane.showInputDialog("Enter Scale (mm)", 0.00 + " mm"));
                if (m_input <= 0) {
                    throw new NumberFormatException();
                }
                double m_Length = start.distance(end);
                page.setScale(m_Length / m_input);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number.", "Invalid Scale", JOptionPane.ERROR_MESSAGE);
            }
            snapX = snapY = -1;
            tools.updateWindow();
//            scroller.setPannable(true);
            tools.setMode("FREE");
        }
    }


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

    }

    @Override
    public void handleShowContextMenu(MouseEvent event) {

    }


}
