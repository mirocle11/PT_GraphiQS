package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class createProjectController {

    public AnchorPane createBox, firstpane, secondpane;
    public JFXButton next, back, proceed;
    public Label page, second_label;
    public JFXCheckBox selectAllBox;

    public void cancel() {
        workspaceController.closeCreateProject();
    }

    public void next() {
        firstpane.setVisible(false);
        secondpane.setVisible(true);

        back.setDisable(false);
        next.setVisible(false);
        proceed.setVisible(true);
        selectAllBox.setVisible(true);
        second_label.setVisible(true);

        page.setText("2 out of 2");
    }

    public void back() {
        firstpane.setVisible(true);
        secondpane.setVisible(false);

        back.setDisable(true);
        next.setVisible(true);
        selectAllBox.setVisible(false);
        second_label.setVisible(false);
        proceed.setVisible(false);
        page.setText("1 out of 2");
    }

    public void proceed() {
        workspaceController.openPdfFile();
        cancel();
    }

    public void selectAll() {
        if (selectAllBox.isSelected()) {
            secondpane.getChildren().forEach(node -> {
                ((JFXCheckBox) node).setSelected(true);
            });
        } else {
            secondpane.getChildren().forEach(node -> ((JFXCheckBox) node).setSelected(false));
        }
    }

}
