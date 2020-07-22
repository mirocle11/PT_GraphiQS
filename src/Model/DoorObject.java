package Model;

import javafx.scene.control.Label;

public class DoorObject {

    public String door_type, door_width, door_height;
    public Label door_stamp;

    public String getDoor_type() {
        return door_type;
    }

    public void setDoor_type(String door_type) {
        this.door_type = door_type;
    }

    public String getDoor_width() {
        return door_width;
    }

    public void setDoor_width(String door_width) {
        this.door_width = door_width;
    }

    public String getDoor_height() {
        return door_height;
    }

    public void setDoor_height(String door_height) {
        this.door_height = door_height;
    }

    public Label getDoor_stamp() {
        return door_stamp;
    }

    public void setDoor_stamp(Label door_stamp) {
        this.door_stamp = door_stamp;
    }
}
