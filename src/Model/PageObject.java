package Model;

import javafx.scene.image.Image;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

public class PageObject {
    int pageNumber;
    ArrayList<ShapeObject> shapeObjList = new ArrayList<>();
    ArrayList<Shape> stampList = new ArrayList<>();
    Image image;

    public PageObject(int pageNumber, Image image) {
        this.pageNumber = pageNumber;
        this.image = image;
    }


    public ArrayList<ShapeObject> getShapeList() {
        System.out.println("PAGE " + pageNumber + " size " + shapeObjList.size());
        return shapeObjList;
    }

    public void setShapeObjList(ArrayList<ShapeObject> sp) {
        System.out.println("SETTING SPO " + sp.size());
        this.shapeObjList.clear();
        sp.forEach(shapeObject -> {
            System.out.println("ADDING OBJECT");
            this.shapeObjList.add(shapeObject);
        });
    }

    public ArrayList<Shape> getStampList() {
        return stampList;
    }

    public void setStampList(ArrayList<Shape> stampList) {
        this.stampList = stampList;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
