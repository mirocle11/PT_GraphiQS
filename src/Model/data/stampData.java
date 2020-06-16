package Model.data;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class stampData extends RecursiveTreeObject<stampData> {

    private SimpleStringProperty no, stamp, type, width, height, image;

    public stampData(String no, String stamp, String type, String width, String height, String image) {

        this.no = new SimpleStringProperty(no);
        this.stamp = new SimpleStringProperty(stamp);
        this.type = new SimpleStringProperty(type);
        this.width = new SimpleStringProperty(width);
        this.height = new SimpleStringProperty(height);
        this.image = new SimpleStringProperty(image);
    }

    public String getNo() {
        return no.get();
    }

    public SimpleStringProperty noProperty() {
        return no;
    }

    public void setNo(String no) {
        this.no.set(no);
    }

    public String getStamp() {
        return stamp.get();
    }

    public SimpleStringProperty stampProperty() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp.set(stamp);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getWidth() {
        return width.get();
    }

    public SimpleStringProperty widthProperty() {
        return width;
    }

    public void setWidth(String width) {
        this.width.set(width);
    }

    public String getHeight() {
        return height.get();
    }

    public SimpleStringProperty heightProperty() {
        return height;
    }

    public void setHeight(String height) {
        this.height.set(height);
    }

    public String getImage() {
        return image.get();
    }

    public SimpleStringProperty imageProperty() {
        return image;
    }

    public void setImage(String image) {
        this.image.set(image);
    }
}
