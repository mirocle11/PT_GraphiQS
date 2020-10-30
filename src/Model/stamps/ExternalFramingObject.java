package Model.stamps;

import javafx.scene.control.Label;

import javax.swing.text.html.ImageView;

public class ExternalFramingObject {

    private String no, part, quantity, material;
    private Label stamp;
    private ImageView img;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ImageView getImg() {
        return img;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Label getStamp() {
        return stamp;
    }

    public void setStamp(Label stamp) {
        this.stamp = stamp;
    }
}
