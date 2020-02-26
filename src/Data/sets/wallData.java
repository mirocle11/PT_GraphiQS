package Data.sets;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Controllers.workspaceController;

import java.util.Objects;

public class wallData {

    private JFXComboBox wallsComboBox;
    private JFXComboBox typeComboBox;
    private JFXComboBox choicesComboBox;

    private ObservableList<String> WALLS = FXCollections.observableArrayList( "Exterior", "Interior");

    private ObservableList<String> EXTERIOR = FXCollections.observableArrayList("Cladding",
            "Wall Underlay", "Framing", "Insulation", "Internal Lining");

    private ObservableList<String> INTERIOR = FXCollections.observableArrayList("Framing",
            "Insulation", "Internal Lining");

    private ObservableList<String> CLADDING = FXCollections.observableArrayList("Brick veneer",
            "James Hardie Linea", "James Hardie Axon Panel", "Rockcote");

    private ObservableList<String> WALL_UNDERLAY = FXCollections.observableArrayList("Thermakraft Watergate Plus",
            "Flamestop", "Dristud", "HomeRAB", "RAB", "Ecoply");

    private ObservableList<String> FRAMING = FXCollections.observableArrayList("70x45", "90x45",
            "140x45", "190x45");

    private ObservableList<String> INSULATION = FXCollections.observableArrayList("R1.8", "R2.0",
            "R2.2", "R2.4", "R2.6");

    private ObservableList<String> INTERNAL_LINING = FXCollections.observableArrayList("10mm Gib",
            "10mm Aqualine", "13mm Gib", "Villaboard");

    public wallData (workspaceController workspace) {
        this.wallsComboBox = workspace.wallsComboBox;
        this.typeComboBox = workspace.typeComboBox;
        this.choicesComboBox = workspace.choicesComboBox;
    }

    public void wallsAction() {
        wallsComboBox.setItems(WALLS);

        wallsComboBox.setOnAction(event -> {
            if(wallsComboBox.getSelectionModel().isEmpty()) {
                typeComboBox.setDisable(true);
            } else {
                typeComboBox.setDisable(false);
                choicesComboBox.getSelectionModel().clearSelection();
                choicesComboBox.setDisable(true);
                showType();
            }
            typeComboBox.getSelectionModel().clearSelection();
        });
    }

    private void showType() {
        if (Objects.equals(wallsComboBox.getSelectionModel().getSelectedItem().toString(), "Exterior")) {
            typeComboBox.setItems(EXTERIOR);
        } else if (Objects.equals(wallsComboBox.getSelectionModel().getSelectedItem().toString(), "Interior")) {
            typeComboBox.setItems(INTERIOR);
        }
    }

    public void typeAction() {
        typeComboBox.setOnAction(event -> {
            if (typeComboBox.getSelectionModel().isEmpty()) {
                choicesComboBox.getSelectionModel().clearSelection();
                choicesComboBox.setDisable(true);
            } else {
                choicesComboBox.setDisable(false);
                wallData.this.showChoices();
            }
        });
    }

    private void showChoices() {
        if (Objects.equals(typeComboBox.getSelectionModel().getSelectedItem().toString(), "Cladding")) {
            choicesComboBox.setItems(CLADDING);
        } else if (Objects.equals(typeComboBox.getSelectionModel().getSelectedItem().toString(), "Wall Underlay")) {
            choicesComboBox.setItems(WALL_UNDERLAY);
        } else if (Objects.equals(typeComboBox.getSelectionModel().getSelectedItem().toString(), "Framing")) {
            choicesComboBox.setItems(FRAMING);
        } else if (Objects.equals(typeComboBox.getSelectionModel().getSelectedItem().toString(), "Insulation")) {
            choicesComboBox.setItems(INSULATION);
        } else if (Objects.equals(typeComboBox.getSelectionModel().getSelectedItem().toString(), "Internal Lining")) {
            choicesComboBox.setItems(INTERNAL_LINING);
        }
    }

}