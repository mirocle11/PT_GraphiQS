package Model;

import javafx.scene.image.Image;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

public class PageObject {
    int pageNumber;
    ArrayList<ShapeObject> shapeObjList = new ArrayList<>();
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

    public PageObject(int pageNumber, Image image, ArrayList<double[][]> snapList) {
        this.pageNumber = pageNumber;
        this.image = image;
        this.shapeObjList.clear();
        snapList.forEach(snap -> {
            System.out.println("ADDING OBJECT");
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
        System.out.println("PAGE " + pageNumber + " size " + shapeObjList.size());
        return shapeObjList;
    }

    public void setShapeObjList(ArrayList<ShapeObject> sp) {
        this.shapeObjList.clear();
        sp.forEach(shapeObject -> {
            this.shapeObjList.add(shapeObject);
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

    public void setImage(Image image) {
        this.image = image;
    }
}
