package Model;

import Model.stamps.FoundationsObject;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.shape.Shape;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PageObject {
    int pageNumber;
    public ArrayList<ShapeObject> shapeObjList = new ArrayList<>();
    public ArrayList<CladdingObject> claddingObjectList = new ArrayList<>();
    public ArrayList<FoundationsObject> foundationsObjectList = new ArrayList<>();
    ArrayList<Shape> stampList = new ArrayList<>();
    ArrayList<double[][]> snapList = new ArrayList<>();
    Image image;
    double scale = 0;

    public PageObject(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public PageObject(int pageNumber, Image image) {
        this.pageNumber = pageNumber;
        this.image = image;
    }

    public PageObject(int pageNumber, BufferedImage param) {
        this.pageNumber = pageNumber;
        this.image = SwingFXUtils.toFXImage(param,null);
    }

    public PageObject(int pageNumber, Image image, ArrayList<double[][]> snapList) {
        this.pageNumber = pageNumber;
        this.image = image;
        this.shapeObjList.clear();
        snapList.forEach(snap -> {
            this.snapList.add(snap);
        });
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public ArrayList<ShapeObject> getShapeList() {
        return shapeObjList;
    }

    public void setShapeObjList(ArrayList<ShapeObject> sp) {
        this.shapeObjList.clear();
        sp.forEach(shapeObject -> {
            this.shapeObjList.add(shapeObject);
        });
    }

    public ArrayList<CladdingObject> getCladdingObjectList() {
        return claddingObjectList;
    }

    public void setCladdingObjectList(ArrayList<CladdingObject> cl) {
        this.claddingObjectList.clear();
        cl.forEach(claddingObject -> {
            this.claddingObjectList.add(claddingObject);
        });
    }

    public ArrayList<Shape> getStampList() {
        return stampList;
    }

    public void setStampList(ArrayList<Shape> stampList) {
        this.stampList = stampList;
    }

    public ArrayList<double[][]> getSnapList() {
        return snapList;
    }

    public void setSnapList(ArrayList<double[][]> sn) {
        this.shapeObjList.clear();
        sn.forEach(snap -> {
//            System.out.println("snap "+ snap);
//            System.out.println("ADDING OBJECT");
            this.snapList.add(snap);

        });
//        System.out.println("Set Snap " + snapList.size());
//        this.snapList = snapList;
    }

    public Image getImage() {
        return image;
    }

    public ArrayList<FoundationsObject> getFoundationsObjectList() {
        return foundationsObjectList;
    }

    public void setFoundationsObjectList(ArrayList<FoundationsObject> f1) {
        this.foundationsObjectList.clear();
        f1.forEach(fo -> {
            this.foundationsObjectList.add(fo);
        });
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
