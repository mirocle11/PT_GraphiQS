package Model.stamps;

import javafx.scene.control.Label;

public class WindowObject {

    public String windowNo, cladding, width, height, type;
    public Label windowStamp;

    public String getWindowNo() {
        return windowNo;
    }

    public void setWindowNo(String windowNo) {
        this.windowNo = windowNo;
    }

    public String getCladding() {
        return cladding;
    }

    public void setCladding(String cladding) {
        this.cladding = cladding;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public Label getWindowStamp() {
        return windowStamp;
    }

    public void setWindowStamp(Label windowStamp) {
        this.windowStamp = windowStamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
