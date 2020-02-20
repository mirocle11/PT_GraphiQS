package Controllers;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import service.Tools;

public class createProjectController {

    public AnchorPane createBox, firstpane, secondpane;
    public JFXButton next, back, proceed;
    public Label page;

    public void cancel() {
        workspaceController.closeCreateProject();
    }

    public void next() {
        firstpane.setVisible(false);
        secondpane.setVisible(true);

        back.setDisable(false);
        next.setVisible(false);
        proceed.setVisible(true);
        page.setText("2 out of 2");
    }

    public void back() {
       firstpane.setVisible(true);
        secondpane.setVisible(false);

        back.setDisable(true);
        next.setVisible(true);
        proceed.setVisible(false);
        page.setText("1 out of 2");
    }

    public void proceed() {
        workspaceController.openPdfFile();
        cancel();
    }

}
