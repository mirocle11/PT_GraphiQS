package Model.data;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Controllers.workspaceController;
import javafx.scene.paint.Color;

import java.util.Objects;

public class WallData {

    private JFXComboBox structureComboBox, wallTypeComboBox, wallComboBox, materialComboBox;
    private JFXButton LENGTH, AREA;
    private JFXColorPicker colorPicker;

    private ObservableList<String> WALLS = FXCollections.observableArrayList( "Exterior", "Interior");

    private ObservableList<String> EXTERIOR = FXCollections.observableArrayList("Cladding", "Wall Underlay",
            "Framing", "Insulation", "Internal Lining");

    private ObservableList<String> INTERIOR = FXCollections.observableArrayList("Framing", "Insulation",
            "Internal Lining");

    private ObservableList<String> CLADDING = FXCollections.observableArrayList("Brick veneer", "James Hardie Linea",
            "James Hardie Axon Panel", "Rockcote");

    private ObservableList<String> WALL_UNDERLAY = FXCollections.observableArrayList("Thermakraft Watergate Plus",
            "Flamestop", "Dristud", "HomeRAB", "RAB", "Ecoply");

    private ObservableList<String> FRAMING = FXCollections.observableArrayList("70x45", "90x45", "140x45",
            "190x45");

    private ObservableList<String> INSULATION = FXCollections.observableArrayList("R1.8", "R2.0", "R2.2",
            "R2.4", "R2.6");

    private ObservableList<String> INTERNAL_LINING = FXCollections.observableArrayList("10mm Gib", "10mm Aqualine",
            "13mm Gib", "Villaboard");

    public WallData(workspaceController workspace) {
        this.wallTypeComboBox = workspace.wallTypeComboBox;
        this.wallComboBox = workspace.wallComboBox;
        this.materialComboBox = workspace.materialComboBox;
        this.structureComboBox = workspace.structureComboBox;
        this.colorPicker = workspace.colorPicker;
        this.LENGTH = workspace.LENGTH;
        this.AREA = workspace.AREA;
    }

    public void structureAction() {
        structureComboBox.setOnAction(event -> {
            if (!structureComboBox.getSelectionModel().isEmpty()) {
                wallTypeComboBox.setDisable(false);
                wallComboBox.getSelectionModel().clearSelection();
                wallComboBox.setDisable(true);
            } else {
                wallTypeComboBox.setDisable(true);
            }
            wallTypeComboBox.getSelectionModel().clearSelection();
            colorPickerAction();
        });
    }

    public void wallsAction() {
        wallTypeComboBox.setItems(WALLS);

        wallTypeComboBox.setOnAction(event -> {
            if (!wallTypeComboBox.getSelectionModel().isEmpty()) {
                wallComboBox.setDisable(false);
                materialComboBox.getSelectionModel().clearSelection();
                materialComboBox.setDisable(true);
                showType();
            } else {
                wallComboBox.setDisable(true);
            }
            wallComboBox.getSelectionModel().clearSelection();
            colorPickerAction();
        });
    }

    private void showType() {
        if (Objects.equals(wallTypeComboBox.getSelectionModel().getSelectedItem().toString(), "Exterior")) {
            wallComboBox.setItems(EXTERIOR);
        } else if (Objects.equals(wallTypeComboBox.getSelectionModel().getSelectedItem().toString(), "Interior")) {
            wallComboBox.setItems(INTERIOR);
        }
    }

    public void typeAction() {
        wallComboBox.setOnAction(event -> {
            if (wallComboBox.getSelectionModel().isEmpty()) {
                materialComboBox.getSelectionModel().clearSelection();
                materialComboBox.setDisable(true);
            } else {
                materialComboBox.setDisable(false);
                showChoices();
                colorPickerAction();
            }
        });
    }

    private void showChoices() {
        if (Objects.equals(wallComboBox.getSelectionModel().getSelectedItem().toString(), "Cladding")) {
            materialComboBox.setItems(CLADDING);
        } else if (Objects.equals(wallComboBox.getSelectionModel().getSelectedItem().toString(), "Wall Underlay")) {
            materialComboBox.setItems(WALL_UNDERLAY);
        } else if (Objects.equals(wallComboBox.getSelectionModel().getSelectedItem().toString(), "Framing")) {
            materialComboBox.setItems(FRAMING);
        } else if (Objects.equals(wallComboBox.getSelectionModel().getSelectedItem().toString(), "Insulation")) {
            materialComboBox.setItems(INSULATION);
        } else if (Objects.equals(wallComboBox.getSelectionModel().getSelectedItem().toString(), "Internal Lining")) {
            materialComboBox.setItems(INTERNAL_LINING);
        }
    }

    public void choicesAction() {
        materialComboBox.setOnAction(event -> {
            if (materialComboBox.getSelectionModel().isEmpty()) {
                colorPicker.setValue(Color.WHITE);
                colorPicker.setDisable(true);
            } else {
                colorPicker.setDisable(false);
            }
            colorPickerAction();
        });
    }

    public void colorPickerAction() {
        if (colorPicker.isDisabled()) {
            LENGTH.setDisable(true);
            AREA.setDisable(true);
        } else {
            LENGTH.setDisable(false);
            AREA.setDisable(false);
        }
    }


}