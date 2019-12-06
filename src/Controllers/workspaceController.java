package Controllers;

import Data.historyData;
import Main.Main;
import Model.ShapeObject;
import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerNextArrowBasicTransition;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.graphics.PdfImageType;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class workspaceController implements Initializable {

    public static AnchorPane mainPane;

    //buttons
    public JFXButton IMPORT, SAVE, SCALE, LENGTH, AREA, STAMP, structureToggle;
    public JFXHamburger hamburger;

    //checkbox
    public JFXCheckBox selectAllBox;

    //custom popups
    public AnchorPane preliminaryAndGeneralPane, foundationsPane, prestressedFloorsPane, blockOpeningsPane, blockWallsPane,
            floorPackingPane, subfloorPane, intFloorLev1Pane, intFloorLev2Pane, extOpeningsPane, intOpeningsPane,
            braceHardwarePane, braceSglLevPane, interTenancySectionPane, wetLiningsPane, wallStrappingPane, miscManufPane,
            postAndBeamHardwarePane, wallsSglLevPane, wallsBasementPane, wallsGndLevPane, wallsLev1Pane, wallsLev2Pane,
            wallsLev3Pane, wallsLev4Pane, bp_packersPane, wallHardwarePane, boltsManualPane, chimneyPane, trussesPane,
            roofPane, extLiningPane, rainScreenPane, wetCeilingsPane, ceilingsPane, cupboardsPane, showersAndBathsPane,
            decksPane, pergolaPane, miscellaniousPane, plumbingPane, bulkHeadsPane, windowSeatsPane, landscapingPane, fencingPane;

    public VBox preliminaryAndGeneralBox, foundationsBox, prestressedFloorsBox, blockOpeningsBox, blockWallsBox,
            floorPackingBox, subfloorBox, intFloorLev1Box, intFloorLev2Box, extOpeningsBox, intOpeningsBox,
            braceHardwareBox, braceSglLevBox, interTenancySectionBox, wetLiningsBox, wallStrappingBox, miscManufBox,
            postAndBeamHardwareBox, wallsSglLevBox, wallsBasementBox, wallsGndLevBox, wallsLev1Box, wallsLev2Box,
            wallsLev3Box, wallsLev4Box, bp_packersBox, wallHardwareBox, boltsManualBox, chimneyBox, trussesBox,
            roofBox, extLiningBox, rainScreenBox, wetCeilingsBox, ceilingsBox, cupboardsBox, showersAndBathsBox,
            decksBox, pergolaBox, miscellaniousBox, plumbingBox, bulkHeadsBox, windowSeatsBox, landscapingBox, fencingBox;

    //containers
    public AnchorPane frontPane, structurePane, shortListPane;
    public ScrollPane scroller, structureScrollPane;
    public Group scrollContent, group;
    public StackPane zoomPane;
    public Canvas canvas;
    public Pane pane;
    public JFXDrawer drawer;
    public VBox structureBox, shortListBox;
    public AnchorPane sectionPane;
    public JFXColorPicker colorPicker;
    public JFXComboBox<String> setsComboBox;

    public Image image;
    public ArrayList<ShapeObject> shapeObjList = new ArrayList<>();

    //temp shapes
    Line line = new Line();
    Rectangle rect = new Rectangle();

    //booleans
    private boolean isNew = true;
    private boolean canDraw = true;

    //collections
    Stack<Shape> undoHistory = new Stack<>();
    Stack<Shape> redoHistory = new Stack<>();
    List<Shape> shapeList = new ArrayList<>();
    ArrayList<Point2D> pointList = new ArrayList<>();

    //others
    double SCALE_DELTA = 1.1;
    double m_Scale = 0;
    double origScaleX;
    double origScaleY;
    String mode;
    ContextMenu contextMenu = new ContextMenu();
    Color color;

    //indicator
    private int i = 0;

    //sets
    private ObservableList<String> FOUNDATIONS_POST_FOOTINGS = FXCollections.observableArrayList("17.5 Footing",
            "17.5 Post", "17.5 Slab", "17.5 Con Footings D12 CHANGE", "17.5 Con Post D12 CHANGE","17.5 Con Slab D12 CHANGE");

    private ObservableList<String> FOUNDATIONS_CONCRETE_BORES = FXCollections.observableArrayList("17.5 Mpa D12 rod CHANGE",
            "20 Mpa D12 rod CHANGE");

    private ObservableList<String> FOUNDATIONS_FOOTINGS = FXCollections.observableArrayList("2-D12 17.5mpa D10-600",
            "2-D12 17.5mpa D12-600", "2-D12 17.5mpa R6-600", "2-D12 17.5mpa D10-600", "2-D16 17.5mpa D10-600",
            "3-D12 17.5mpa D10LS-600");

    private ObservableList<String> FOUNDATIONS_CONCRETE_FLOOR = FXCollections.observableArrayList("SE62 Mesh 17.5 Mpa & Polythene",
            "SE62 Mesh 20 Mpa & Polythene", "SE62 Mesh 25 Mpa & Polythene", "688 mesh 17.5 Mpa & Polythene", "688 mesh 20.0 Mpa & Polythene",
            "Harwood Homes", "Mt Masonry Fibermesh CHANGE", "No mesh 17.5 Mpa & Polythene", "Polythene Only", "SE625-500 Mesh 17.5 Mpa & Polythene",
            "SE625-500 Mesh 20 Mpa & Polythene", "SE625-500 Mesh 25 Mpa & Polythene");

    private ObservableList<String> FOUNDATIONS_RAFT_PILES = FXCollections.observableArrayList("Raft HD12 Rod");

    private ObservableList<String> FOUNDATIONS_RAFT_FOOTINGS = FXCollections.observableArrayList("Raft 3-HD12 20Mpa",
            "Raft Rod & Starters to Existing", "Raft Internal Beams");

    private ObservableList<String> FOUNDATIONS_RAFT_SLAB = FXCollections.observableArrayList("Raft 20 Mpa & Polythene",
            "Raft 20 Mpa [Whangarei]");

    private ObservableList<String> FOUNDATIONS_PATIO_FOOTINGS = FXCollections.observableArrayList("1-D12 17.5 Mpa D10-600",
            "2-D12 17.5 Mpa D10-600", "2-D12 17.5 Mpa D12-600", "2-D12 17.5 Mpa R6-600");

    private ObservableList<String> FOUNDATIONS_PATIO_SLAB = FXCollections.observableArrayList("SE62 Mesh 17.5 Mpa & Polythene",
            "668 mesh 17.5 Mpa & Polythene", "17.5 Mpa concrete only", "No mesh 17.5 Mpa & Polythene, SE625-500 Mesh 17.5 Mpa & Polythene");

    private ObservableList<String> FOUNDATIONS_CARPOT_FOOTINGS = FXCollections.observableArrayList("1-D12 17.5 Mpa D10-600",
            "2-D12 17.5 Mpa D10-600", "2-D12 17.5 Mpa D12-600", "2-D12 17.5 Mpa R6-600");

    private ObservableList<String> FOUNDATIONS_CARPOT_SLAB = FXCollections.observableArrayList("SE62 Mesh 17.5 Mpa & Polythene",
            "17.5 Mpa concrete only", "668 mesh 17.5 Mpa & Polythene", "No mesh 17.5 Mpa & Polythene", "SE615-500 m",
            "SE625-500 Mesh 17.5 Mpa & Polythene");

    private ObservableList<String> FOUNDATIONS_DECK_FOOTING = FXCollections.observableArrayList("1-D12 17.5 Mpa D10-600",
            "1-D12 17.5 Mpa D12-600", "2-D12 17.5 Mpa D10-600", "1-D12 17.5 Mpa D12-600");

    private ObservableList<String> FOUNDATIONS_DECK_SLAB = FXCollections.observableArrayList("SE62 Mesh 17.5 Mpa & Polythene",
            "688 Mesh 17.5 ", "17.5 Mpa Concrete Only", "No mesh 17.5 MPa & Polythene", "SE625-500 Mesh 17.5 Mpa & Polythene");

    private ObservableList<String> FOUNDATIONS_WALL_PLATES = FXCollections.observableArrayList("90x45 120mm Coach Screw Galv",
            "90x45 120mm Coach Screw SS", "90x45 120mm Trubolt Galv", "90x45 120mm Trubolt SS");

    private ObservableList<String> FOUNDATIONS_WATER_PROOFING = FXCollections.observableArrayList("3 Coats Flinkote & 25 Poly",
            "3 Coats Mulseal & 25 Poly", "Shelterbit Plain - Polystyrene - Bibim", "Shelterseal 3000 - Poly - Bidim");

    private ObservableList<String> FOUNDATIONS_COLUMNS = FXCollections.observableArrayList("20 Mpa 4/D16 R10 CHANGE",
            "20 Mpa Pump 4/D16 R10 CHANGE");

    private ObservableList<String> FOUNDATIONS_BEAM = FXCollections.observableArrayList("20 Mpa 4/D20 R10 CHANGE",
            "20 Mpa Pump 4/D20 R10 CHANGE");

    private ObservableList<String> FOUNDATIONS_CONCRETE_WALL = FXCollections.observableArrayList("17.5-D12 vert D16 hor CHANGE");

    private ObservableList<String> FOUNDATIONS_CONC_STEPS = FXCollections.observableArrayList("Std CHANGE");

    private ObservableList<String> FOUNDATIONS_BLOCK_CNRS_ENDS = FXCollections.observableArrayList("200 Series",
            "100 Series", "150 Series", "250 Series", "Half High 150 Series", "Half High 200 Series", "Header Block",
            "Hotbloc 200mm", "StackBond 100", "StackBond 100 Honed", "Stackbond 150", "Stackbond 200");

    private ObservableList<String> FOUNDATIONS_PROFILES = FXCollections.observableArrayList("Profiles Only",
            "Profiles & Electrical Pipe", "Electrical Pipe");

    private ObservableList<String> FOUNDATIONS_BOXING = FXCollections.observableArrayList("200 Boxing", "300 Boxing");

    private ObservableList<String> PRESTRESSED_FLOORS_CONCRETE_FLOOR = FXCollections.observableArrayList("25 Mpa - SE930 Mesh CHANGE",
            "Hi Bond - 25Mpa - SE930 Mesh CHANGE");

    private ObservableList<String> PRESTRESSED_FLOORS_CONCRETE_DECK = FXCollections.observableArrayList("25 Mpa - SE930 Mesh CHANGE",
            "Hi Bond - 25Mpa - SE930 Mesh CHANGE");

    private ObservableList<String> PRESTRESSED_FLOORS_SADDLES = FXCollections.observableArrayList("HD12@200 x 2000 CHANGE");

    private ObservableList<String> PRESTRESSED_FLOORS_BEAM = FXCollections.observableArrayList("20 Mpa 4/D20 R10 CHANGE",
            "20 Mpa Pump 4/D20 R10 CHANGE", "25.0 Mpa Pump Concrete & 1 D12 only", "25.0 Mpa Pump Concrete only TOPPING SLAB");

    private ObservableList<String> BLOCK_OPENINGS_LINTELS = FXCollections.observableArrayList("2-D12 & R6-200 ctrs",
            "4-D12 & R6-200 ctrs");

    private ObservableList<String> BLOCK_OPENINGS_DOORS = FXCollections.observableArrayList("150 Honed Ext Wall 1 End O/rides others",
            "150 Honed Int Wall 1 End O/rides others", "150 Series Exterior Wall", "150 Series Interior Wall", "150 Series Stackbond Exterior Wall",
            "200 GuardBloc Honed 1 End O/rides others", "200 Hotbloc Honed 1 End O/rides others", "200 Series Honed 1 Exterior Wall",
            "200 Series Honed 1 Exterior Wall - Garage", "200 Series Stackbond Exterior Wall", "200 Honed Ext-Gar 1 End O/rides others",
            "200 Honed Ext-Wall 1 End O/rides others", "200 Honed Int-Wall 1 End O/rides others", "200mm GuardBloc", "25 Series",
            "250 Honed 1 End O/rides others", "Hotbloc Exterior Wall 200mm", "Hotbloc Exterior 250mm", "Insulform 200mm Series",
            "Insulform 250mm Series", "Insulform 300mm Series");

    private ObservableList<String> BLOCK_OPENINGS_WINDOWS = FXCollections.observableArrayList("150 Honed Ext Wall 1 End O/rides others",
            "150 Honed Int Wall 1 End O/rides others", "150 Series Exterior Wall", "150 Series Interior Wall", "150 Series Stackbond Exterior Wall",
            "200 GuardBloc Honed 1 End O/rides others", "200 Hotbloc Honed 1 End O/rides others", "200 Series Honed 1 Exterior Wall",
            "200 Series Honed 1 Exterior Wall - Garage", "200 Series Stackbond Exterior Wall", "200 Honed Ext-Gar 1 End O/rides others",
            "200 Honed Ext-Wall 1 End O/rides others", "200 Honed Int-Wall 1 End O/rides others", "200mm GuardBloc", "25 Series",
            "250 Honed 1 End O/rides others", "Hotbloc Exterior Wall 200mm", "Hotbloc Exterior 250mm", "Insulform 200mm Series",
            "Insulform 250mm Series", "Insulform 300mm Series");

    private ObservableList<String> BLOCK_WALLS = FXCollections.observableArrayList("150 Honed 1 End O/ride others",
            "200 - D12@400 Vert - 4 D16 Hor CHANGE", "200 - D12@600 Vert - 4 D12 Hor CHANGE", "200 - D16@400 Vert - 4 D12 Hor CHANGE",
            "200 GardBloc Honed 1 End Overrides", "200 Honed 1 End O/rides others", "200 Hotbloc Honed 1 End O/rides", "200mm GuardBloc",
            "250 Honed 1 End O/rides others", "Grout to Superform 300 Series", "Hotbloc 200mm CHANGE", "Hotbloc 250mm CHANGE",
            "Insulform 200mm Series", "Insulform 250mm Series", "Insulform 300mm Series");

    private ObservableList<String> BLOCK_WALLS_CNRS_ENDS = FXCollections.observableArrayList("100 Series",
            "150 Honed 1 End Overides others", "150 Series", "200 Garbloc Honed 1 End O/rides others", "200 Honed 1 End Overrides Others",
            "200 Hotbloc Honed 1 End Overrides", "200 Series", "200mm GuardBloc", "250 Honed 1 End Overrides Others", "250 Series",
            "Half High 150 Series", "Half High 200 Series", "Header Block", "Hotbloc 200mm", "Hotbloc 250mm", "Insulform 200mm Series",
            "Insulform 250mm Series", "Insulform 300mm Series", "Stackbond 100", "Stackbond 100 Honed", "Stackbond 150", "Stackbond 200");

    private ObservableList<String> BLOCK_WALLS_WATER_PROOFING = FXCollections.observableArrayList("3 Coats Flintkote & 25 Poly",
            "3 Coats Mulseal & 25 Poly", "Shelterbit Plain - Polystyrene - Bibim", "Shelterseal 3000 - Poly - Bidim");

    private ObservableList<String> BLOCK_WALLS_COLS_PIERS = FXCollections.observableArrayList("390x390 Blk Column 4D16 Vert",
            "Honed 390x390 Blk Column 4D16 Vert", "Pier 2 D16 Vert", "Pier 2 D12 Vert", "Pilister 4 D12 Vert", "Pilister 4 D16 Vert");

    private ObservableList<String> BLOCK_WALLS_CP_MEASURED_ITEMS = FXCollections.observableArrayList("Bowmac B79");

    private ObservableList<String> BLOCK_WALLS_CONC_LINTELS = FXCollections.observableArrayList("Concrete Lintels/Beams");

    private ObservableList<String> BLOCK_WALLS_BEAMS = FXCollections.observableArrayList("Block Beams");

    private ObservableList<String> BLOCK_WALLS_PLATES = FXCollections.observableArrayList("90x45 120mm Coach Screw Galv",
            "90x45 120mm Coach Screw SS", "90x45 140mm Trubolt Galv", "90x45 140mm Trubolt SS", "Moore Living 250x50 W/Plate Co",
            "Moore Living 250x50 W/Plate ov", "Moore Living 250x50 W/Plate un");

    private ObservableList<String> FLOOR_PACKING_CONC_PACKING = FXCollections.observableArrayList("100x50 H3.2 Concrete Packing",
            "Hardi 18mm Compressed Sheet", "Hardi Tile & Slate U/Lay", "Plyfloor H3");

    private ObservableList<String> FLOOR_PACKING_FLOORING = FXCollections.observableArrayList("150x25 TG HT Rimu",
            "Hardi 18mm Compressed Sheet", "Hardi Tile & Slate U/Lay", "Plyfloor H3");

    private ObservableList<String> SUBFLOOR_PILE_CONCRETE_MEASURED = FXCollections.observableArrayList("All Items 17.5 Mpa",
            "All Items 20.0 Mpa");

    private ObservableList<String> SUBFLOOR_CONCRETE_ROUND = FXCollections.observableArrayList("17.5 Mpa - 250 Dia");

    private ObservableList<String> SUBFLOOR_CONCRETE_BLOCK_RING_WALL = FXCollections.observableArrayList("17.5 Mpa 2 D12 Hor D12 600 Vert - CHANGE");

    private ObservableList<String> SUBFLOOR_MEASURED = FXCollections.observableArrayList("Above 600 Chg bearer - Galv", "Below 600 Chg bearer - SS");

    private ObservableList<String> SUBFLOOR_BRACING = FXCollections.observableArrayList("100x75 H4 Brace <3m", "100x100 H4 Brace >3-5m",
            "100x100 H3.2 Brace >3-5m", "100x75 H3.2 Brace <3m");

    private ObservableList<String> SUBFLOOR_STEEL_BEAMS = FXCollections.observableArrayList("H1.2 All Sizes", "H3.2 Dry All Sizes",
            "Steel Beams H1.2", "D/Fir H1.2 All Sizes", "D/Fir UT All Sizes");

    private ObservableList<String> SUBFLOOR_FRAMING_MEASURED = FXCollections.observableArrayList("MSG8 H1.2 KD 90x45 Galv",
            "MSG8 H1.2 KD 90x45 SS", "MSG8 H1.2 KD 140x45 Galv", "MSG8 H1.2 KD 140x45 SS", "MSG8 H1.2 KD 190x45 Galv",
            "MSG8 H1.2 KD 190x45 SS", "MSG8 H1.2 KD 240x45 Galv", "MSG8 H1.2 KD 240x45 SS", "MSG8 H3.2 Dry 90x45 Galv",
            "MSG8 H3.2 Dry 90x45 SS", "MSG8 H3.2 Dry 140x45 Galv", "MSG8 H3.2 Dry 140x45 SS", "MSG8 H3.2 Dry 190x45 Galv",
            "MSG8 H3.2 Dry 190x45 SS", "MSG8 H3.2 Dry 240x45 Galv", "MSG8 H3.2 Dry 240x45 SS", "MSG8 H3.2 Dry 290x45 Galv",
            "MSG8 H3.2 Dry 290x45 SS", "MSG8 H3.2 Wet 290x45 Galv", "MSG8 H3.2 Wet 290x45 SS", "H1.2 Wet 100x50 Galv",
            "H1.2 Wet 100x50 SS", "H1.2 Wet 150x50 Galv", "H1.2 Wet 150x50 SS", "H1.2 Wet 200x50 Galv",
            "H1.2 Wet 200x50 SS", "H1.2 Wet 250x50 Galv", "H1.2 Wet 250x50 SS", "H3.2 Wet 100x50 Galv",
            "H3.2 Wet 100x50 SS", "H3.2 Wet 150x50 Galv", "H3.2 Wet 150x50 SS", "H3.2 Wet 200x50 Galv", "H3.2 Wet 200x50 SS");

    private ObservableList<String> SUBFLOOR_FLOORING = FXCollections.observableArrayList("Green/t 36x12 421 Ins",
            "Green/t 36x12 421 Ins Screwed", "Particle Bd 36x18 421 Ins", "Particle Bd 36x18 421 Ins Screwed",
            "Pynefloor Gold 3600x900 421 Ins", "150x25 T&G Ht Rimu", "18mm Compressed Sht", "Hardies Ezi Grid Tile U.Lay",
            "Hardies T/Slate U.Lay", "Kopine U/Lock 3600x1200x20mm", "Plyfloor 25x12x19mm", "Plyfloor 25x12x19mm Screwed",
            "Plyfloor 25x12x19mm H3", "Plyfloor 25x12x19mm H3 Screwed");

    private ObservableList<String> SUBFLOOR_JACK_FRAMING = FXCollections.observableArrayList("H1.2-10mm gib-R1.8-6mm H/F CHANGE",
            "H3.2-10mm gib-R1.8-6mm H/F CHANGE");

    private ObservableList<String> SUBFLOOR_BASE = FXCollections.observableArrayList("H3.2 100x25 H3 RS Merch",
            "H3.2 150x25 H3 RS Merch", "H3.2 75x25 H3 RS Merch", "H3.2 6mm H/Flex 2 Hor Vert@1.2");

    private ObservableList<String> SUBFLOOR_DIFFICULT_BASE = FXCollections.observableArrayList("H3.2 Wet 75x25 H3 R/S Merch",
            "H3.2 Wet 100x25 H3 R/S Merch", "H3.2 150x25 H3 RS Merch", "H3.2 Wet 6mm H/F Change");

    private ObservableList<String> SUBFLOOR_BASE_DOOR_ACCESSORIES = FXCollections.observableArrayList("Hardiflex 2.4x6mm - No Door",
            "Hardiflex 2.4x6mm & Door");

    private ObservableList<String> INT_FLOOR_LEV1_FRAMING_MEASURED = FXCollections.observableArrayList("H1.2 KD 140x45",
            "H1.2 KD 190x45", "H1.2 KD 240x45", "H1.2 KD 290x45", "HJ200-45 Hyjoist H3", "HJ240-63 Hyjoist H3", "HJ240-90 Hyjoist H3",
            "HJ300-63 Hyjoist H3", "HJ300-90 Hyjoist H3", "HJ360-63 Hyjoist H3", "HJ360-90 Hyjoist H3", "HJ400-90 Hyjoist H3",
            "HS 170x45 Hyspan H1", "HS 200x45 Hyspan H1", "HS 200x45 Hyspan H1", "HS 240x45 Hyspan H1", "HS 240x63 Hyspan H1",
            "HS 300x45 Hyspan H1", "HS 300x63 Hyspan H1", "HS 360x45 Hyspan H1", "HS 360x63 Hyspan H1", "HS 400x45 Hyspan H1",
            "HS 400x63 Hyspan H1", "HS 450x63 Hyspan H1", "HS 600x63 Hyspan H1", "Hyne I Beam 200x68 H3.1", "Hyne I Beam 245x68 H3.1",
            "Hyne I Beam 300x68 H3.1", "Hyne I Beam 360x68 H3.1", "Lumberworx I-Beam (Overrides for sizes)", "NelsonPine LV10 (Overrides for sizes)");

    private ObservableList<String> INT_FLOOR_LEV1_FLOORING = FXCollections.observableArrayList("Green/t 3.6x1.2 No Ins",
            "Green/t 3.6x1.2 No Ins Screwed", "Particle Bd 36x18 No Ins", "Particle Bd 36x18 No Ins Screwed", "18mm Hardies Compressed",
            "100x25 Rimu T&G", "Hardies Ezi Grid Tile U.Lay", "Hardies T/Slate U.Lay", "Kopine U/Lock 3600x1200", "Plyfloor 24x12x19mm",
            "Plyfloor 24x12x19mm H3", "Plyfloor 24x12x19mm H3 No Ins Screwed", "Plyfloor 24x12x19mm No Ins Screwed");

    private ObservableList<String> INT_FLOOR_LEV1_LANDING_MEASURED = FXCollections.observableArrayList("90X45 H1.2", "140x45 H1.2");

    private ObservableList<String> INT_FLOOR_LEV1_FLOOR_BEAMS = FXCollections.observableArrayList("All MSG8 H1.2",
            "All MSG8 H3.2 Dry", "All Douglas Fir Solid", "All Solid Macropa", "Ganglam H1.2", "Ganglam H3.2 Dry", "Pryda Flitch H1.2",
            "Pryda Flitch H3.2 Dry");

    private ObservableList<String> INT_FLOOR_LEV1_STEEL_BEAMS = FXCollections.observableArrayList("D/Fir H1.2 All Sizes",
            "D/Fir UT All Sizes", "H1.2 All Sizes", "H3.2 All Sizes");

    private ObservableList<String> INT_FLOOR_LEV1_FLITCH_BEAMS = FXCollections.observableArrayList("Flitch Beam H1.2 FB15L",
            "Flitch Beam H1.2 FB20M", "FLITCH BEAM H1.2 FB25L", "FLITCH BEAM H1.2 FB25M", "FLITCH BEAM H1.2 FB30M");

    private ObservableList<String> INT_FLOOR_LEV1_LVL_BEAMS = FXCollections.observableArrayList("HS 170x45 Hyspan H1",
            "HS 200x45 Hyspan H1", "HS 200x63 Hyspan H1", "HS 240x45 Hyspan H1", "HS 240x63 Hyspan H1", "HS 300x45 Hyspan H1",
            "HS 300x63 Hyspan H1", "HS 360x45 Hyspan H1", "HS 360x63 Hyspan H1", "HS 400x45 Hyspan H1", "HS 400x63 Hyspan H1",
            "HS 450x63 Hyspan H1", "HS 600x63 Hyspan H1", "Hyne Beam 17c 295x65 H3.1", "Hyne Beam 17c 295x85 H3.1",
            "Hyne Beam 17c 330x85 H3.1", "Hyne Beam 17c 360x85 H3.1", "Hyne Beam 17c 395x85 H3.1", "Hyne Beam 17c 425x85 H3.1",
            "Hyne Beam 17c 460x85 H3.1", "Hyne Beam 17c 495x85 H3.1", "Hyne Beam 17c 525x85 H3.1");

    private ObservableList<String> INT_FLOOR_LEV1_INT_ENCLOSED_BEAMS = FXCollections.observableArrayList("Gib Both Sides",
            "Gib One Side Only", "Scotia ONLY both sides");

    private ObservableList<String> INT_FLOOR_LEV1_ATTIC_INT_JOISTS = FXCollections.observableArrayList("140x45 H1.2 MSG8",
            "190x45 H1.2 MSG8", "240x45 H1.2 MSG8", "290x45 H1.2 MSG8");

    private ObservableList<String> INT_FLOOR_LEV1_I_JOISTS = FXCollections.observableArrayList("240-63 H3",
            "300-63 H3", "360-63 H3", "400-90 H1.2 MSG8");

    private ObservableList<String> INT_FLOOR_LEV2_FRAMING_MEASURED = FXCollections.observableArrayList("H1.2 KD 140x45",
            "H1.2 KD 190x45", "H1.2 KD 240x45", "H1.2 KD 290x45", "HJ200-45 Hyjoist H3", "HJ240-63 Hyjoist H3", "HJ240-90 Hyjoist H3",
            "HJ300-63 Hyjoist H3", "HJ300-90 Hyjoist H3", "HJ360-63 Hyjoist H3", "HJ360-90 Hyjoist H3", "HJ400-90 Hyjoist H3",
            "HS 170x45 Hyspan H1", "HS 200x45 Hyspan H1", "HS 200x45 Hyspan H1", "HS 240x45 Hyspan H1", "HS 240x63 Hyspan H1",
            "HS 300x45 Hyspan H1", "HS 300x63 Hyspan H1", "HS 360x45 Hyspan H1", "HS 360x63 Hyspan H1", "HS 400x45 Hyspan H1",
            "HS 400x63 Hyspan H1", "HS 450x63 Hyspan H1", "HS 600x63 Hyspan H1", "Hyne I Beam 200x68 H3.1", "Hyne I Beam 245x68 H3.1",
            "Hyne I Beam 300x68 H3.1", "Hyne I Beam 360x68 H3.1", "Lumberworx I-Beam (Overrides for sizes)", "NelsonPine LV10 (Overrides for sizes)");

    private ObservableList<String> INT_FLOOR_LEV2_FLOORING = FXCollections.observableArrayList("Green/t 3.6x1.2 No Ins",
            "Green/t 3.6x1.2 No Ins Screwed", "Particle Bd 36x18 No Ins", "Particle Bd 36x18 No Ins Screwed", "18mm Hardies Compressed",
            "100x25 Rimu T&G", "Hardies Ezi Grid Tile U.Lay", "Hardies T/Slate U.Lay", "Kopine U/Lock 3600x1200", "Plyfloor 24x12x19mm",
            "Plyfloor 24x12x19mm H3", "Plyfloor 24x12x19mm H3 No Ins Screwed", "Plyfloor 24x12x19mm No Ins Screwed");

    private ObservableList<String> INT_FLOOR_LEV2_LANDING_MEASURED = FXCollections.observableArrayList("90X45 H1.2", "140x45 H1.2");

    private ObservableList<String> INT_FLOOR_LEV2_FLOOR_BEAMS = FXCollections.observableArrayList("All MSG8 H1.2",
            "All MSG8 H3.2 Dry", "All Douglas Fir Solid", "All Solid Macropa", "Ganglam H1.2", "Ganglam H3.2 Dry", "Pryda Flitch H1.2",
            "Pryda Flitch H3.2 Dry");

    private ObservableList<String> INT_FLOOR_LEV2_STEEL_BEAMS = FXCollections.observableArrayList("D/Fir H1.2 All Sizes",
            "D/Fir UT All Sizes", "H1.2 All Sizes", "H3.2 All Sizes");

    private ObservableList<String> INT_FLOOR_LEV2_FLITCH_BEAMS = FXCollections.observableArrayList("Flitch Beam H1.2 FB15L",
            "Flitch Beam H1.2 FB20M", "FLITCH BEAM H1.2 FB25L", "FLITCH BEAM H1.2 FB25M", "FLITCH BEAM H1.2 FB30M");

    private ObservableList<String> INT_FLOOR_LEV2_LVL_BEAMS = FXCollections.observableArrayList("HS 170x45 Hyspan H1",
            "HS 200x45 Hyspan H1", "HS 200x63 Hyspan H1", "HS 240x45 Hyspan H1", "HS 240x63 Hyspan H1", "HS 300x45 Hyspan H1",
            "HS 300x63 Hyspan H1", "HS 360x45 Hyspan H1", "HS 360x63 Hyspan H1", "HS 400x45 Hyspan H1", "HS 400x63 Hyspan H1",
            "HS 450x63 Hyspan H1", "HS 600x63 Hyspan H1", "Hyne Beam 17c 295x65 H3.1", "Hyne Beam 17c 295x85 H3.1",
            "Hyne Beam 17c 330x85 H3.1", "Hyne Beam 17c 360x85 H3.1", "Hyne Beam 17c 395x85 H3.1", "Hyne Beam 17c 425x85 H3.1",
            "Hyne Beam 17c 460x85 H3.1", "Hyne Beam 17c 495x85 H3.1", "Hyne Beam 17c 525x85 H3.1");

    private ObservableList<String> INT_FLOOR_LEV2_INT_ENCLOSED_BEAMS = FXCollections.observableArrayList("Gib Both Sides",
            "Gib One Side Only", "Scotia ONLY both sides");

    private ObservableList<String> INT_FLOOR_LEV2_INT_LVL_JOISTS = FXCollections.observableArrayList("150x45 H3",
            "170x45 H3", "200x45 H3", "200x63 H3", "240x45 H3", "240x63 H3", "300x45 H3", "300x63 H3", "360x45 H3", "360x63 H3",
            "400x45 H3", "400x63 H3", "450x63 H3", "600x63 H3");

    private ObservableList<String> INT_FLOOR_LEV2_I_JOISTS = FXCollections.observableArrayList("240-63 H3",
            "300-63 H3", "360-63 H3", "400-90 H1.2 MSG8");

    private ObservableList<String> EXT_OPENINGS_WIND_HARDWARE = FXCollections.observableArrayList("Type B Conc & Timb",
            "Type C Conc", "Type C Timb", "Type D Conc", "Type D Timb", "Type E Conc & Timb", "Type F Conc & Timb", "Type G Conc",
            "Type G Timb", "Type H Conc", "Type H Timb", "Universal Homes");

    private ObservableList<String> EXT_OPENINGS_GARAGE_DOOR = FXCollections.observableArrayList("10 Series Masonry",
            "20mm Jamb & Brick", "40mm Jamb & Brick", "A-Lign 142mm Bev Back", "A-Lign 187mm Bev Back", "Axon Panel",
            "Cedar B&Batten 75x25 Facing DR", "Cedar Bev Bk", "Cedar Rustic", "Cedar Shiplap", "ColourSteel Cladding",
            "Eterpan Vent Clad Express Joints", "Eterside Cladding", "Exo Tec Top Hat Battens", "Garrison B/Back W/Bd",
            "H3 B&Batten 75x25 Facing DR", "H3 Bev Bk", "H3 Rustic", "H3 Shiplap", "Hardi Sheet", "Hardi Shingleside",
            "Hardibacker", "Hardiplank", "Hebel", "HP Cedar B&Batten", "HP Cedar Bev Bk", "HP Cedar Rustic", "HP Cedar Shiplap",
            "Insulclad", "Jamb Plates 200x25 Trim", "Linea & 100 Wide Facing", "Linea No Facing", "NuLine Plus", "Palliside Rust",
            "Palliside Trad", "Ply Cladding", "Polystyrene", "PP B&Batten 75x25 Facing DR", "PP Bev Bk", "PP Rustic", "PP Shiplap",
            "RK Cedar Bev Back", "Scyon Stria", "ShadowClad Natural H3.1", "ShadowClad Primed Ultra H3.1", "SmartClad 140mm",
            "SmarClad 185mm", "Triboard", "Triclad B&Batten 75x25 Facing BS", "Triclad Bev Bk 75x25 Facing BS",
            "Triclad Rustic 75x25 Facing BS", "Triclad Vertical 75x25 Facing BS", "Weathertex Panel", "Weathertex Weatherboard");

    private ObservableList<String> EXT_OPENINGS_DOORS = FXCollections.observableArrayList("10 Series Masonry",
            "A-Lign 142mm Bev Back", "A-Lign 187mm Bev Back", "Axon Panel", "Brick 70 series Monier", "Brick Midland 70 Series",
            "Brick Monier Presto Plastering Doors", "Brick PlastaBrick", "CedarB&Batten 75x25 Facing DR", "Cedar Bev Back",
            "Cedar Rusticated", "Cedar Shiplap", "ColourSteel Cladding", "Eterpan Vent Clad Express Joints", "Eterpan Vent Clad Plaster",
            "Eterpan Xpress Clad Express Joints", "Eterside Cladding", "Exo Tec Top Hat Battens", "Garrison B/Back W/Bd",
            "H3 B&Batten 75x25 FAcing DR", "H3 Bev Bk", "H3 Rustic", "H3 Shiplap", "Hardi Sheet", "Hardi Shingleside",
            "Hardibacker", "Hardiplank", "Hebel", "HP Cedar B&Batten", "HP Cedar Bev Back", "HP Cedar Rusticated",
            "HP Cedar Shiplap", "Insulclad", "Jamb Plate & Tape Only", "Linea & 100 Wide Facing", "Linea No Facing", "NuLine Plus",
            "Palliside Rustic", "Palliside Traditional", "Polystyrene", "PP B&Batten 75x25 Facing DR", "PP Bev Back", "PP Rustic",
            "PP Shiplap", "RK Cedar Bev Back", "Scyon Stria", "ShadowClad/Ply", "SmartClad 140mm", "SmartClad 185mm", "Triboard",
            "Triclad B&Batten 75x25 Facing BS", "TriClad Bev Back", "TriClad Rustic", "Triclad Vertical", "Weathertex Panels",
            "Weathertex Weatherboard");

    private ObservableList<String> EXT_OPENINGS_WINDOWS = FXCollections.observableArrayList("10 Series Masonry",
            "A-Lign 142mm Bev Back", "A-Lign 187mm Bev Back", "Axon Panel", "Brick 70 series Monier", "Brick Midland 70 Series",
            "Brick Monier Presto Plastering Doors", "Brick PlastaBrick", "CedarB&Batten 75x25 Facing DR", "Cedar Bev Back",
            "Cedar Rusticated", "Cedar Shiplap", "ColourSteel Cladding", "Eterpan Vent Clad Express Joints", "Eterpan Vent Clad Plaster",
            "Eterpan Xpress Clad Express Joints", "Eterside Cladding", "Exo Tec Top Hat Battens", "Garrison B/Back W/Bd",
            "H3 B&Batten 75x25 FAcing DR", "H3 Bev Bk", "H3 Rustic", "H3 Shiplap", "Hardi Sheet", "Hardi Shingleside",
            "Hardibacker", "Hardiplank", "Hebel", "HP Cedar B&Batten", "HP Cedar Bev Back", "HP Cedar Rusticated",
            "HP Cedar Shiplap", "Insulclad", "Jamb Plate & Tape Only", "Linea & 100 Wide Facing", "Linea No Facing", "NuLine Plus",
            "Palliside Rustic", "Palliside Traditional", "Polystyrene", "PP B&Batten 75x25 Facing DR", "PP Bev Back", "PP Rustic",
            "PP Shiplap", "RK Cedar Bev Back", "Scyon Stria", "ShadowClad/Ply", "SmartClad 140mm", "SmartClad 185mm", "Triboard",
            "Triclad B&Batten 75x25 Facing BS", "TriClad Bev Back", "TriClad Rustic", "Triclad Vertical", "Weathertex Panels",
            "Weathertex Weatherboard", "RK Cedar Bev Back");

    private ObservableList<String> EXT_OPENINGS_WINDOW_HEADS_STANDARD = FXCollections.observableArrayList("150X25 Cedar Bev Back BS",
            "150x25 Cedar Bev Back DR", "150x25 Cedar Rusticated BS", "150x25 Cedar Rusticated DR", "150x25 Cedar Shiplap BS",
            "150x25 Cedar Shiplap DR", "150x25 FJ PP Bev Back DR", "150x25 FJ PP RUSTICATED DR", "150x25 FJ PP SHIPLAP DR",
            "150x25 H3 Bev Back BS", "150x25 H3 Bev Back DR", "150x25 H3 Rusticated BS", "150x25 H3 Rusticated DR", "150x25 H3 Shiplap BS",
            "150x25 H3 Shiplap DR", "200x25 Cedar Bev Back BS", "200x25 Cedar Bev Back DR", "200x25 Cedar Rusticated BS",
            "200x25 Cedar Rusticated DR", "200x25 H3 Shiplap BS", "200x25 H3 Shiplap DR", "200x25 Cedar B&Batten BS",
            "200x25 CedarB&Batten DR");

    private ObservableList<String> EXT_OPENINGS_WINDOW_HEADS_RAKING = FXCollections.observableArrayList("150X25 Cedar Bev Back BS",
            "150x25 Cedar Bev Back DR", "150x25 Cedar Rusticated BS", "150x25 Cedar Rusticated DR", "150x25 Cedar Shiplap BS",
            "150x25 Cedar Shiplap DR", "150x25 FJ PP Bev Back", "150x25 FJ PP RUSTICATED", "150x25 FJ PP Shiplap",
            "150x25 H3 Bev Back BS", "150x25 H3 Bev Back DR", "150x25 H3 Rusticated BS", "150x25 H3 Rusticated DR", "150x25 H3 Shiplap BS",
            "150x25 H3 Shiplap DR", "200x25 Cedar Bev Back BS", "200x25 Cedar Bev Back DR", "200x25 Cedar Rusticated BS",
            "200x25 Cedar Rusticated DR", "200x25 Cedar Shiplap BS","200x25 Cedar Shiplap DR", "200x25 FJ PP Bev Back",
            "200x25 FJ PP RUSTICATED", "200x25 FJ PP Shiplap", "200x25 H3 Bev Back BS", "200x25 H3 Bev Back DR",
            "200x25 H3 Rusticated BS", "200x25 H3 Rusticated DR", "200x25 H3 Shiplap BS", "200x25 H3 Shiplap DR", "250x25 Cedar B&Batten BS",
            "250x25 Cedar B&Batten DR", "250x25 H3 B&Batten BS", "250x25 H3 B&Batten DR", "A-Lign 142mm Bev Back", "A-Lign 187mm Bev Back",
            "Eterside Cladding", "Featureboard", "Garrison 135x18mm B/Back", "Garrison 142x18mm B/Back", "Garisson 180x18mm B/Back",
            "Hardibacker", "Hardiflex 6.0", "Hardiflex 7.5", "Hardiplank Colonial", "Hardiplank Frontier 245", "Hardiplank Frontier 310",
            "Hardiplank Rustic", "Hardiplank Smooth 180", "Hardiplank Smooth 240", "Hardiplank 305", "Hardiplank Styleline",
            "Hardiplank Summit", "HP 150X25 Cedar B&Batten DR", "HP 150X25 Cedar B/Back DR", "HP 150X25 Cedar Rustic DR",
            "HP 150X25 Cedar Shiplap DR", "HP 200X25 Cedar B&Batten DR", "HP 200X25 Cedar B/Back DR", "HP 200X25 Cedar Rustic DR",
            "HP 200X25 Cedar Shiplap DR", "Linea 135", "Linea 150", "Linea 180", "Monotek", "Palliside Rustic Smooth",
            "Palliside Traditional", "Shadowclad Gvd Natural H3.1", "Shadowclad Gvd Primed Ultra H3.1", "Shadowclad Tex Natural H3.1",
            "Shadowclad Tex Primed Ultra H3.1", "SmartClad 140mm", "SmartClad 185mm", "Triclad BB 190mm", "Triclad BB 230mm",
            "Triclad Brd & Batten", "Triclad Rustic 190mm", "Triclad Vert 190mm", "Triclad Vert 230mm", "W/Tex Weatherboards (Overrides)",
            "Weathertex Pnls Smth 2400 (Overrides)");

    private ObservableList<String> EXT_OPENINGS_CANTILEVER_LINTELS = FXCollections.observableArrayList("Standard Conc Floor",
            "Standard Timber Floor");

    private ObservableList<String> INT_OPENINGS_DOOR_OPENINGS = FXCollections.observableArrayList("Jamb Plates & No19 Archs",
            "Jamb Plates Only", "FJ 19mm No18 Arc & Stops 100", "FJ 19mm No18 Arc & Stops 75", "FJ 19mm No18 Arc Sld-Bfld 100",
            "FJ 19mm No18 Arc Sld-Bfld 75", "FJ 25mm Gvd & Stops 100", "FJ 25mm Gvd & Stops 75", "FJ 25mm Gvd & Stops 75",
            "FJ 25mm Gvd Sld-Bfld 100", "FJ 25mm Gvd Sld-Bfld 75", "FJ 40mm Gvd & Stops 100", "FJ 40mm Gvd & Stops 75",
            "FJ 40mm Gvd Sld-Bfld 100", "FJ 40mm Gvd Sld-Bfld 75", "MUF - No18 Arc & Stops 100", "MUF - No18 Arc & Stops 75",
            "MUF - No18 Arc Sld-Bfld 100", "MUF - No18 Arc Sld-Bfld 75", "MUF Gvd & Stops 100", "MUF Gvd & Stops 75",
            "MUF Gvd Sld-Bfld 100", "MUF Gvd Sld-Bfld 75", "No18 Arc to Cavity Sliders");

    private ObservableList<String> INT_OPENINGS_WINDOWS = FXCollections.observableArrayList("100mm FJ Jambs",
            "Slimlines");

    private ObservableList<String> INT_OPENINGS_DOOR_UNITS = FXCollections.observableArrayList("MDF F/J Reb Jamb 75mm",
            "MDF F/J Reb Jamb 100mm", "MDF F/J Reb Jamb 100mm", "MDF F/J Reb Jamb 140mm", "MDF F/J Slimline Jamb 75mm", "MDF F/J Slimline Jamb 100mm",
            "MDF MUF Jamb & Stops 75mm", "MDF MUF Jamb & Stops 100mm", "Econ Rimu F/J Reb Jamb 75mm", "Econ Rimu F/J Reb Jamb 100mm",
            "Econ Rimu F/J Slimline Jamb 75mm", "Econ Rimu F/J Slimline Jamb 100mm", "Econ Rimu-MUF Jamb 75mm", "Econ Rimu-MUF Jamb 100mm",
            "Econ Rimu-Rimu Jamb 75mm", "Econ Rimu-Rimu Jamb 100mm", "Manhatten - JF Reb Jamb 75mm", "Manhatten - FJ Reb Jamb 100mm",
            "Manhatten - FJ Slimline Jamb 75mm", "Manhatten - FJ Slimeline Jamb 100mm", "Manhatten - MUF Jamb 75mm", "Mahatten - MUF Jamb 100mm",
            "N/Up 138x19 Pine Grvd - MDF UC", "Settler 4P F/J Jamb & Stops 100mm", "Settler 4P F/J Reb Jamb 75mm", "Settler 4P F/J Reb Jamb 100mm",
            "Settler 4P F/J Slimline Jamb 75mm", "Settler 4P F/J Slimline Jamb 100mm", "Settler 4P-MUF Jamb 75mm", "Settler 4P-MUF Jamb 100mm",
            "Solid Core F/J FL Jamb 75mm", "Solid Core F/J FL Jamb 100mm", "Solid Core F/J FL Jamb 140mm", "Solid Core MUF FL Jamb 75mm",
            "Solid Core MUF FL Jamb 100mm", "Solid MUF Reb Jamb 75mm", "Solid MUF Reb Jamb 100mm", "Solid Core Slimline Jamb 75mm",
            "Solid Core Slimline Jamb 100mm", "Windsor 4P - FJ Reb Jamb 75mm", "Windsor 4P - FJ Reb Jamb 100mm", "Windsor 4P - FJ Slimline Jamb 75mm",
            "Windsor 4P - FJ Slimline Jamb 100mm", "Windsor 4P - MUF Jamb 75mm", "Windsor 4P - MUF Jamb 100mm");


    //skip to ext walls
    private ObservableList<String> WALLS_LEV1_EXTERIOR = FXCollections.observableArrayList("90x45 H1.2 2.4L Timb R2.2",
            "90x45 H1.2 2.7L Timb R2.2", "90x45 H1.2 3.0L Timb R2.2", "90x45 H1.2 3.3L Timb R2.2", "90x45 H1.2 3.6L Timb R2.2",
            "90x45 H1.2 Rake Timb R2.2", "90x45 H1.2 Arch Timb R2.2", "90x45 H1.2 Curv Timb R2.2", "140x45 H1.2 <3.0m Timb R2.2",
            "140x45 H1.2 3.0m-5.9m Timb R2.2", "140x45 H1.2 Rake Timb R2.2", "140x45 H1.2 Arch Timb R2.2", "140x45 H1.2 Curv Timb R2.2",
            "190x45 H1.2 <3.0m Timb R2.2", "190x45 H1.2 3.0m-6.0 Timb R1", "190x45 H1.2 Rake Timb R2.2", "Gib to Existing Walls",
            "z90x35 H1.2 2.4L Timb R2.2", "z90x35 H1.2 2.7L Timb R2.2", "z90x45 Doug-Fir H1.2 Conc R2.2");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        origScaleX = group.getScaleX();
        origScaleY = group.getScaleY();
        pane.getChildren().add(line);
        scroller.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observable,
                                Bounds oldValue, Bounds newValue) {
                zoomPane.setMinSize(newValue.getWidth(), newValue.getHeight());
            }
        });

        try {
            VBox box = FXMLLoader.load(getClass().getResource("/Views/workspaceSideNavigation.fxml"));
            drawer.setSidePane(box);

            HamburgerNextArrowBasicTransition burgerTask = new HamburgerNextArrowBasicTransition(hamburger);
            burgerTask.setRate(-1);
            hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
                burgerTask.setRate(burgerTask.getRate() * -1);
                burgerTask.play();

                if (drawer.isOpened()) {
                    drawer.close();
                } else {
                    drawer.toggle();
                }
            });
        } catch (Exception e) {
            Logger.getLogger(workspaceController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    //=====GETTING SCROLL CHANGE WHE ZOOM=====//
    private Point2D figureScrollOffset(Node scrollContent, ScrollPane scroller) {
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        double hScrollProportion = (scroller.getHvalue() - scroller.getHmin()) / (scroller.getHmax() - scroller.getHmin());
        double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        double vScrollProportion = (scroller.getVvalue() - scroller.getVmin()) / (scroller.getVmax() - scroller.getVmin());
        double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);
        return new Point2D(scrollXOffset, scrollYOffset);
    }

    //=====FOR MOUSE POINT ZOOM=====//
    private void repositionScroller(Node scrollContent, ScrollPane scroller, double scaleFactor, Point2D scrollOffset) {
        double scrollXOffset = scrollOffset.getX();
        double scrollYOffset = scrollOffset.getY();
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        if (extraWidth > 0) {
            double halfWidth = scroller.getViewportBounds().getWidth() / 2;
            double newScrollXOffset = (scaleFactor - 1) * halfWidth + scaleFactor * scrollXOffset;
            scroller.setHvalue(scroller.getHmin() + newScrollXOffset * (scroller.getHmax() - scroller.getHmin()) / extraWidth);
        } else {
            scroller.setHvalue(scroller.getHmin());
        }
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        if (extraHeight > 0) {
            double halfHeight = scroller.getViewportBounds().getHeight() / 2;
            double newScrollYOffset = (scaleFactor - 1) * halfHeight + scaleFactor * scrollYOffset;
            scroller.setVvalue(scroller.getVmin() + newScrollYOffset * (scroller.getVmax() - scroller.getVmin()) / extraHeight);
        } else {
            scroller.setHvalue(scroller.getHmin());
        }
    }

    //=====STICKING POINTS TO IMAGE BOUNDARIES=====//
    public Point2D clamp(double x, double y) {
        double maxX = canvas.getBoundsInLocal().getMaxX();
        double maxY = canvas.getBoundsInLocal().getMaxY();
        double a = Math.max(0, x);
        double b = Math.max(0, y);
        double c = Math.min(a, maxX);
        double d = Math.min(b, maxY);
        Point2D p = new Point2D(c, d);
        return p;
    }

    public void zoom(ScrollEvent event) {
        if (event.getDeltaY() == 0) {
            return;
        }
        double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA : 1 / SCALE_DELTA;
        // amount of scrolling in each direction in scrollContent coordinate
        // units
        double newScale = group.getScaleX() * scaleFactor;
        if (event.isControlDown()) {
            if (newScale > .2 && newScale < 3.7) {
                group.setScaleX(newScale);
                group.setScaleY(newScale);
                Point2D scrollOffset = figureScrollOffset(scrollContent, scroller);
                repositionScroller(scrollContent, scroller, scaleFactor, scrollOffset);

                //===========CHANGES=======================================================
                pane.getChildren().forEach(node -> {
                    if (node.getClass().getSuperclass() == Shape.class) {
                        Shape sp = (Shape) node;
                        System.out.println(10 / group.getScaleY());
                        sp.setStrokeWidth(10 / group.getScaleY());

                    }
                    if (node.getClass() == Label.class) {
                        Label lbl = (Label) node;
                        lbl.setFont(new Font("Arial", 18 / group.getScaleY()));
                    }
                });
            }
        }
    }

    public void updateLine(MouseEvent event) {
        Point2D cP = clamp(event.getX(), event.getY());
        if (!isNew) {
            if (event.isShiftDown()) {
                if (cP.getX() < cP.getY()) {
                    line.setEndX(line.getStartX());
                    line.setEndY(cP.getY());
                } else {
                    line.setEndX(cP.getX());
                    line.setEndY(line.getStartY());
                }
                int xdiff = (int) Math.abs(line.getStartX() - cP.getX());
                int ydiff = (int) Math.abs(line.getStartY() - cP.getY());
                if (xdiff < ydiff) {
                    line.setEndX(line.getStartX());
                    line.setEndY(cP.getY());
                } else {
                    line.setEndX(cP.getX());
                    line.setEndY(line.getStartY());
                }
            } else {
                line.setEndX(cP.getX());
                line.setEndY(cP.getY());
            }
        }
//===========CHANGES=======================================================
        if (mode == "DRAW_RECT") {
            if (!isNew) {
                rect.setWidth(cP.getX() - rect.getX());
                rect.setHeight(cP.getY() - rect.getY());
            }
        }
//===========CHANGES=======================================================
    }

    public void redrawShapes() {
        pane.getChildren().clear();
        for (ShapeObject sp0 : shapeObjList) {
            if (sp0.getType() == "AREA") {
                pane.getChildren().add(sp0.getPolygon());
                double area = 0;

                for (int x = 0; x < sp0.getPointList().size() - 1; x++) {
                    Point2D p1 = sp0.getPointList().get(x);
                    Point2D p2 = sp0.getPointList().get(x + 1);
                    area += ((p1.getX() / m_Scale) * (p2.getY() / m_Scale)) - ((p1.getY() / m_Scale) * (p2.getX() / m_Scale));
                }
                area = Math.abs(area / 2);
                area = area / 1000;

                Label lbl = new Label(Math.round(area * 100.0) / 100.0 + " m²");
                lbl.setFont(new Font("Arial", 36));
                lbl.setTextFill(sp0.getColor());
                double layX = sp0.getPolygon().getBoundsInParent().getMinX() + (sp0.getPolygon().getBoundsInParent().getWidth() - lbl.getWidth()) / 2;
                double layY = sp0.getPolygon().getBoundsInParent().getMinY() + (sp0.getPolygon().getBoundsInParent().getHeight() - lbl.getHeight()) / 2;
                System.out.println(lbl.getWidth() + " LBL GET WIDTH");
                lbl.setOnMouseEntered(event -> {
                    lbl.setStyle("-fx-background-color: white");
                    lbl.setOpacity(1);
                });
                lbl.setOnMouseExited(event -> {
                    lbl.setStyle("-fx-background-color: transparent");
                    lbl.setOpacity(.5);
                });
                lbl.setLayoutX(layX);
                lbl.setLayoutY(layY);
                lbl.setOpacity(.5);
                pane.getChildren().add(lbl);

            }
        }

        for (ShapeObject sp1 : shapeObjList) {
            if (sp1.getType() == "LENGTH") {
                for (Line l : sp1.getLineList()) {
                    Point2D p1 = new Point2D(l.getStartX(), l.getStartY());
                    Point2D p2 = new Point2D(l.getEndX(), l.getEndY());
                    Point2D mid = p1.midpoint(p2);
                    line.getRotate();
                    System.out.println("ANGLE " + p1.angle(p2) + " ROTATE " + line.getRotate());
                    if (p1.distance(p2) != 0) {
                        double length = (p1.distance(p2) / m_Scale);
                        Label lbl = new Label(Math.round(length * 100.0) / 100.0 + " mm");
                        lbl.setFont(new Font("Arial", 36));
                        lbl.setLayoutY(mid.getY());
                        lbl.setLayoutX(mid.getX());
                        lbl.setOnMouseEntered(event -> {
                            lbl.setStyle("-fx-background-color: white");
                            lbl.setOpacity(1);
                        });
                        lbl.setOnMouseExited(event -> {
                            lbl.setStyle("-fx-background-color: transparent");
                            lbl.setOpacity(.5);
                        });
                        lbl.setTextFill(sp1.getColor());
                        lbl.setOpacity(.5);

                        pane.getChildren().add(lbl);
                    }
                }
            }
            pane.getChildren().addAll(sp1.getLineList());
            pane.getChildren().addAll(sp1.getBoxList());
        }
        pane.getChildren().add(line);
        line.setVisible(false);
        pane.getChildren().forEach(node -> {
            if (node.getClass().getSuperclass() == Shape.class) {
                Shape sp = (Shape) node;
                System.out.println(10 / group.getScaleY());
                sp.setStrokeWidth(10 / group.getScaleY());


            }
            if (node.getClass() == Label.class) {
                Label lbl = (Label) node;
                lbl.setFont(new Font("Arial", 18 / group.getScaleY()));
            }
        });
    }

    public Shape createBox(double x, double y) {

        double boxW = 10 * 3;
        shapeList.add(new Rectangle(x - (boxW / 2) / group.getScaleY(), y - (boxW / 2) / group.getScaleY(), boxW / group.getScaleY(), boxW / group.getScaleY()));
        Rectangle r = (Rectangle) shapeList.get(shapeList.size() - 1);
        r.setStroke(Color.GREEN);
        r.setOpacity(.5);
        r.setStrokeWidth((boxW / 5) / group.getScaleY());
        r.setFill(Color.TRANSPARENT);

        r.setOnMouseEntered(event -> {
            r.setStroke(Color.RED);
        });
        r.setOnMouseExited(event -> {
            r.setStroke(Color.GREEN);
        });

        r.setOnMouseReleased(event -> {
            if (!canDraw) {
                return;
            }

            ShapeObject shapeObj = new ShapeObject();
            shapeObj.setPane(pane);
            shapeObj.setStrokeWidth(10 / group.getScaleY());
            shapeObj.setColor(color);
            Point2D p2d = new Point2D(r.getX() + r.getHeight() / 2, r.getY() + r.getHeight() / 2);

            if (p2d != pointList.get(pointList.size() - 1)) {
                pointList.add(p2d);
            }

            shapeObj.setPointList(pointList);
            shapeObj.setController(this);
            shapeObj.setType(mode);
            shapeObjList.add(shapeObj);
            double area = 0;
            for (int xx = 0; xx < shapeObj.getPointList().size() - 1; xx++) {
                Point2D p1 = shapeObj.getPointList().get(xx);
                Point2D p2 = shapeObj.getPointList().get(xx + 1);
                area += ((p1.getX() / m_Scale) * (p2.getY() / m_Scale)) - ((p1.getY() / m_Scale) * (p2.getX() / m_Scale));
            }
            area = Math.abs(area / 2);
            area = area / 1000;

            Label lbl = new Label(Math.round(area * 100.0) / 100.0 + " m²");
            workspaceSideNavigatorController.historyList.addAll(new historyData(mode, lbl.getText()));
            redrawShapes();
            pointList.clear();
            LENGTH.setDisable(false);
            AREA.setDisable(false);
            scroller.setPannable(true);
            canDraw = false;
        });
        return shapeList.get(shapeList.size() - 1);
    }

    public Shape createLine(Line param) {
        shapeList.add(new Line(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY()));
        Line l = (Line) shapeList.get(shapeList.size() - 1);

        l.setStroke(color);
        l.setStrokeLineCap(StrokeLineCap.BUTT);
        l.setStrokeWidth(10 / group.getScaleY());
        l.setOpacity(.5);

        l.setOnMouseEntered(event -> {
            l.setStroke(Color.RED);
        });
        l.setOnMouseExited(event -> {
            l.setStroke(color);
        });
        l.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                contextMenu = new ContextMenu();
                MenuItem item = new MenuItem("REMOVE LENGTH");
                item.setOnAction(event1 -> {
                    System.out.println("SEGMENT REMOVED");
                    pane.getChildren().remove(l);
                    undoHistory.push(l);
                });
                contextMenu.getItems().add(item);
                contextMenu.show(l, event.getScreenX(), event.getScreenY());
            }
        });

        return shapeList.get(shapeList.size() - 1);
    }

    public void drawAction(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {
            return;
        }
        if (!canDraw) {
            return;
        }

        Point2D clamp = clamp(event.getX(), event.getY());
        line.setOpacity(.5);
        line.setStrokeLineCap(StrokeLineCap.BUTT);

        if (mode == "SCALE") {
            if (isNew) {
                line.setVisible(true);
                line.setStartX(clamp.getX());
                line.setStartY(clamp.getY());
                line.setEndX(clamp.getX());
                line.setEndY(clamp.getY());
                line.setStroke(Color.CHOCOLATE);
                line.setStrokeWidth(15 / group.getScaleY());
                line.setVisible(true);
                pane.getChildren().add(createBox(line.getEndX(), line.getEndY()));
                isNew = false;
            } else {
                pane.getChildren().add(createBox(line.getEndX(), line.getEndY()));
                Point2D start = new Point2D(line.getStartX(), line.getStartY());
                Point2D end = new Point2D(line.getEndX(), line.getEndY());
                try {
                    double m_input = Float.parseFloat(JOptionPane.showInputDialog("Enter Scale (mm)", 0.00 + " mm"));
                    if (m_input <= 0) {
                        throw new NumberFormatException();
                    }
                    double m_Length = start.distance(end);
                    m_Scale = m_Length / m_input;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number.", "Invalid Scale", JOptionPane.ERROR_MESSAGE);
                }
                redrawShapes();
                isNew = true;
                canDraw = false;
                scroller.setPannable(true);
                mode = "FREE_HAND";
            }

        } else if (mode == "LENGTH") {
            if (isNew) {
                line.setStartX(clamp.getX());
                line.setStartY(clamp.getY());
                line.setEndX(clamp.getX());
                line.setEndY(clamp.getY());
                pointList.add(clamp);
                line.setStrokeWidth(10 / group.getScaleY());
                line.setStroke(color);
                pane.getChildren().add(createBox(line.getEndX(), line.getEndY()));
                line.setVisible(true);
                isNew = false;
            } else {
                Point2D start = new Point2D(line.getStartX(), line.getStartY());
                Point2D end = new Point2D(line.getEndX(), line.getEndY());
                Point2D mid = start.midpoint(end);
                System.out.println(line.contains(mid) + " line contains");
                pointList.add(end);

                double length = (start.distance(end) / m_Scale);
                Label lbl = new Label(Math.round(length * 100.0) / 100.0 + " mm");
                lbl.setFont(new Font("Arial", 18 / group.getScaleY()));
                lbl.setLayoutY(mid.getY());
                lbl.setLayoutX(mid.getX());
                lbl.setTextFill(color);
                lbl.setOpacity(.5);
                pane.getChildren().add(lbl);
                pane.getChildren().add(createBox(line.getEndX(), line.getEndY()));
                pane.getChildren().add(createLine(line));
                ShapeObject shapeObj = new ShapeObject();
                shapeObj.setPane(pane);
                shapeObj.setStrokeWidth(10 / group.getScaleY());
                shapeObj.setColor(color);
                shapeObj.setPointList(pointList);
                shapeObj.setController(this);
                shapeObj.setType(mode);
                shapeObjList.add(shapeObj);
                isNew = true;
                line.setVisible(false);
                canDraw = false;
                pointList.clear();
                redrawShapes();
                workspaceSideNavigatorController.historyList.addAll(new historyData(mode, lbl.getText()));

            }
        } else if (mode == "AREA") {
            if (isNew) {
                line.setStartX(clamp.getX());
                line.setStartY(clamp.getY());
                line.setEndX(clamp.getX());
                line.setEndY(clamp.getY());
                pointList.add(clamp);
                line.setStrokeWidth(10 / group.getScaleY());
                line.setStroke(color);
                pane.getChildren().add(createBox(line.getEndX(), line.getEndY()));
                line.setVisible(true);
                isNew = false;
            } else {
                Point2D start = new Point2D(line.getStartX(), line.getStartY());
                Point2D end = new Point2D(line.getEndX(), line.getEndY());
                Point2D mid = start.midpoint(end);
                pointList.add(end);

                pane.getChildren().add(createLine(line));
                pane.getChildren().add(createBox(line.getEndX(), line.getEndY()));
                line.setStartX(line.getEndX());
                line.setStartY(line.getEndY());
                line.setEndX(clamp.getX());
                line.setEndY(clamp.getY());
            }
        } else if (mode == "DRAW_RECT") {
            isNew = true;
            canDraw = false;
            ShapeObject sp = new ShapeObject();
        }
    }

    public void openFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(Main.dashboard_stage);
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(null, "No File selected");
        } else {

            String inputFile = selectedFile.getAbsolutePath();
            PdfDocument doc = new PdfDocument();
            doc.loadFromFile(inputFile);
            BufferedImage bImage = doc.saveAsImage(3, PdfImageType.Bitmap, 300, 300);

            image = SwingFXUtils.toFXImage(bImage, null);
            doc.close();

            scroller.setOpacity(1.0);
            frontPane.setVisible(false);


            canvas.setWidth(image.getWidth());
            canvas.setHeight(image.getHeight());
            pane.setPrefSize(canvas.getWidth(), canvas.getHeight());
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.drawImage(image, 0, 0);
            origScaleX = group.getScaleX();
            origScaleY = group.getScaleY();
            System.out.println(group.getBoundsInParent());
            System.out.println(group.getBoundsInLocal());
            System.out.println(group.getScaleX() + " " + group.getScaleY());

            double newScale = group.getScaleX();
            for (int x = 0; x < 12; x++) {
                newScale *= (1 / 1.1);
            }
            group.setScaleX(newScale);
            group.setScaleY(newScale);
        }
    }

    public void saveFile(ActionEvent actionEvent) {
        double scaleX = group.getScaleX();
        double scaleY = group.getScaleY();
        FileChooser savefile = new FileChooser();
        savefile.setTitle("Save File");
        group.setScaleX(1);
        group.setScaleY(1);
        File file = savefile.showSaveDialog(Main.stage);
        if (file != null) {
            try {
                WritableImage writableImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());

                group.snapshot(new SnapshotParameters(), writableImage);
                group.setScaleX(scaleX);
                group.setScaleY(scaleY);

                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png", new File(file + ".jpg"));
            } catch (IOException ex) {
                System.out.println("Error!");
            }
        }
    }

    //=====SCALE ACTION====//
    public void scaleAction(ActionEvent actionEvent) {
        mode = "SCALE";
        canDraw = true;
        isNew = true;
        shortListPane.setVisible(false);
        preliminaryAndGeneralPane.setVisible(false);
        foundationsPane.setVisible(false);
        sectionPane.setVisible(false);
    }

    //=====AREA ACTION====//
    public void areaAction(ActionEvent actionEvent) {
        mode = "AREA";
        viewMeasurementList();
    }

    public void lengthAction(ActionEvent actionEvent) {
        mode = "LENGTH";
        viewMeasurementList();
    }

    public void rotateAction(ActionEvent actionEvent) {
        group.setRotate(group.getRotate() + 90);
    }

    public void drawRectangle(ActionEvent actionEvent) {
        mode = "DRAW_RECT";
        canDraw = true;
        isNew = true;
    }

    public void drawRect(MouseEvent event) {
        Point2D clamp = clamp(event.getX(), event.getY());
        if (mode == "DRAW_RECT") {
            if (!canDraw) {
                return;
            }
            if (event.getButton() == MouseButton.SECONDARY) {
                return;
            }
            line.setVisible(false);
            rect = new Rectangle();
            rect.setX(clamp.getX());
            rect.setY(clamp.getY());
            rect.setWidth(10);
            rect.setHeight(10);
            rect.setOpacity(.5);
            rect.setStroke(color);
            rect.setStrokeWidth(15);
            rect.setOnMouseEntered(event1 -> {
                rect.setStroke(Color.RED);
            });
            rect.setOnMouseExited(event1 -> {
                rect.setStroke(color);
            });
            rect.setOnMousePressed(event1 -> {
                if (event1.getButton() == MouseButton.SECONDARY) {
                    contextMenu = new ContextMenu();
                    MenuItem removeLength = new MenuItem("REMOVE FILL");
                    removeLength.setOnAction(event12 -> {
                        pane.getChildren().remove(rect);
                    });
                    contextMenu.getItems().add(removeLength);
                }
                contextMenu.show(rect, event1.getScreenX(), event1.getScreenY());
            });

            pane.getChildren().add(rect);
            isNew = false;
        } else if (mode == "STAMP") {
            Rectangle r = new Rectangle();
            r.setX(clamp.getX());
            r.setY(clamp.getY());
            r.setWidth(5 / group.getScaleY());
            r.setWidth(5 / group.getScaleY());
            pane.getChildren().add(r);
            System.out.println("ADD STAMP");
        }
    }

    public void handlePan(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            scroller.setPannable(false);
        } else {
            scroller.setPannable(true);
        }
    }

    public void stampAction() {
        mode = "STAMP";
        canDraw = true;
        System.out.println("STAMPING");
    }

    //measurements popup
    private void viewMeasurementList() {
        if (shortListPane.isVisible()) {
            shortListPane.setVisible(false);
            hideShortList();
            sectionPane.setVisible(false);
        } else {
            shortListPane.setVisible(true);
        }
    }

    public void structureListTransition() {
        if (i == 0) {
            structurePane.setVisible(true);
            structurePane.setDisable(true);
            structureToggle.setDisable(true);
            TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.2), structurePane);
            translateTransition1.setByX(+313);
            translateTransition1.play();
            translateTransition1.setOnFinished(event1 -> {
                structurePane.setDisable(false);
                structureToggle.setDisable(false);
            });
            i++;
        } else {
            structurePane.setDisable(true);
            structureToggle.setDisable(true);
            TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.2), structurePane);
            translateTransition1.setByX(-313);
            translateTransition1.play();
            translateTransition1.setOnFinished(event1 -> {
                structurePane.setDisable(false);
                structurePane.setVisible(false);
                structureToggle.setDisable(false);
            });
            i--;
        }
    }

    public void closeMeasurementList() {
        structurePane.setDisable(true);
        structureToggle.setDisable(true);
        TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.2), structurePane);
        translateTransition1.setByX(-313);
        translateTransition1.play();
        translateTransition1.setOnFinished(event1 -> {
            structurePane.setDisable(false);
            structurePane.setVisible(false);
            structureToggle.setDisable(false);
        });
        i--;
    }

    public void selectAll() {
        if (selectAllBox.isSelected()) {
            structureBox.getChildren().forEach(node -> {
                ((JFXCheckBox) node).setSelected(true);
            });
        } else {
            structureBox.getChildren().forEach(node -> ((JFXCheckBox) node).setSelected(false));
        }
    }

    public void addStructure() {
        shortListBox.getChildren().removeAll();
        shortListBox.getChildren().clear();
        structureBox.getChildren().forEach(this::accept);
        closeMeasurementList();
    }

    private void accept(Node node) {
        if (((JFXCheckBox) node).isSelected()) {
            String box = ((JFXCheckBox) node).getText();
            JFXButton button = new JFXButton(box);
            shortListBox.getChildren().add(button);

            button.setOnMouseClicked(event -> {
                switch (button.getText()) {
                    case "Preliminary & General":
                        if (!preliminaryAndGeneralPane.isVisible()) {
                            hideShortList();
                            preliminaryAndGeneralPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            preliminaryAndGeneralPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Foundations":
                        if (!foundationsPane.isVisible()) {
                            hideShortList();
                            foundationsPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            foundationsPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Prestressed Floors":
                        if (!prestressedFloorsPane.isVisible()) {
                            hideShortList();
                            prestressedFloorsPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            prestressedFloorsPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Block Openings":
                        if (!blockOpeningsPane.isVisible()) {
                            hideShortList();
                            blockOpeningsPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            blockOpeningsPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Block Walls":
                        if (!blockWallsPane.isVisible()) {
                            hideShortList();
                            blockWallsPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            blockWallsPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Floor Packing":
                        if (!floorPackingPane.isVisible()) {
                            hideShortList();
                            floorPackingPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            floorPackingPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Subfloor":
                        if (!subfloorPane.isVisible()) {
                            hideShortList();
                            subfloorPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            subfloorPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Int Floor Lev 1":
                        if (!intFloorLev1Pane.isVisible()) {
                            hideShortList();
                            intFloorLev1Pane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            intFloorLev1Pane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Int Floor Lev 2":
                        if (!intFloorLev2Pane.isVisible()) {
                            hideShortList();
                            intFloorLev2Pane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            intFloorLev2Pane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Ext Openings":
                        if (!extOpeningsPane.isVisible()) {
                            hideShortList();
                            extOpeningsPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            extOpeningsPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Int Openings":
                        if (!intOpeningsPane.isVisible()) {
                            hideShortList();
                            intOpeningsPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            intOpeningsPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Brace Hardware":
                        if (!braceHardwarePane.isVisible()) {
                            hideShortList();
                            braceHardwarePane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            braceHardwarePane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Brace Sgl Lev":
                        if (!braceSglLevPane.isVisible()) {
                            hideShortList();
                            braceSglLevPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            braceSglLevPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Inter-Tenancy Section":
                        if (!interTenancySectionPane.isVisible()) {
                            hideShortList();
                            interTenancySectionPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            interTenancySectionPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Wet Linings":
                        if (!wetLiningsPane.isVisible()) {
                            hideShortList();
                            wetLiningsPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wetLiningsPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Wall Strapping":
                        if (!wallStrappingPane.isVisible()) {
                            hideShortList();
                            wallStrappingPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wallStrappingPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Misc Manuf":
                        if (!miscManufPane.isVisible()) {
                            hideShortList();
                            miscManufPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            miscManufPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Post&Beam Hardware":
                        if (!postAndBeamHardwarePane.isVisible()) {
                            hideShortList();
                            postAndBeamHardwarePane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            postAndBeamHardwarePane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Walls Sgl Lev":
                        if (!wallsSglLevPane.isVisible()) {
                            hideShortList();
                            wallsSglLevPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wallsSglLevPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Walls Basement":
                        if (!wallsBasementPane.isVisible()) {
                            hideShortList();
                            wallsBasementPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wallsBasementPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Walls Gnd Lev":
                        if (!wallsGndLevPane.isVisible()) {
                            hideShortList();
                            wallsGndLevPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wallsGndLevPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Walls Lev 1":
                        if (!wallsLev1Pane.isVisible()) {
                            hideShortList();
                            wallsLev1Pane.setVisible(true);
                            sectionPane.setVisible(true);
                            wallsLev1Box.getChildren().forEach(node1 -> node.setOnMouseClicked(event1 -> {
                                showSets(((JFXButton) node).getText());
                            }));
                        } else {
                            wallsLev1Pane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Walls Lev 2":
                        if (!wallsLev2Pane.isVisible()) {
                            hideShortList();
                            wallsLev2Pane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wallsLev2Pane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Walls Lev 3":
                        if (!wallsLev3Pane.isVisible()) {
                            hideShortList();
                            wallsLev3Pane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wallsLev3Pane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Walls Lev 4":
                        if (!wallsLev4Pane.isVisible()) {
                            hideShortList();
                            wallsLev4Pane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wallsLev4Pane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "BP Packers":
                        if (!bp_packersPane.isVisible()) {
                            hideShortList();
                            bp_packersPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            bp_packersPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Wall Hardware":
                        if (!wallHardwarePane.isVisible()) {
                            hideShortList();
                            wallHardwarePane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wallHardwarePane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Bolts Manual":
                        if (!boltsManualPane.isVisible()) {
                            hideShortList();
                            boltsManualPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            boltsManualPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Chimney":
                        if (!chimneyPane.isVisible()) {
                            hideShortList();
                            chimneyPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            chimneyPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Trusses":
                        if (!trussesPane.isVisible()) {
                            hideShortList();
                            trussesPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            trussesPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Roof":
                        if (!roofPane.isVisible()) {
                            hideShortList();
                            roofPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            roofPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Ext Lining":
                        if (!extLiningPane.isVisible()) {
                            hideShortList();
                            extLiningPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            extLiningPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Rain Screen":
                        if (!rainScreenPane.isVisible()) {
                            hideShortList();
                            rainScreenPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            rainScreenPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Wet Ceilings":
                        if (!wetCeilingsPane.isVisible()) {
                            hideShortList();
                            wetCeilingsPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wetCeilingsPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Ceilings":
                        if (!ceilingsPane.isVisible()) {
                            hideShortList();
                            ceilingsPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            ceilingsPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Cupboards":
                        if (!cupboardsPane.isVisible()) {
                            hideShortList();
                            cupboardsPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            cupboardsPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Showers & Baths":
                        if (!showersAndBathsPane.isVisible()) {
                            hideShortList();
                            showersAndBathsPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            showersAndBathsPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Decks":
                        if (!decksPane.isVisible()) {
                            hideShortList();
                            decksPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            decksPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Pergola":
                        if (!pergolaPane.isVisible()) {
                            hideShortList();
                            pergolaPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            pergolaPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Miscellanious":
                        if (!miscellaniousPane.isVisible()) {
                            hideShortList();
                            miscellaniousPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            miscellaniousPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Plumbing":
                        if (!plumbingPane.isVisible()) {
                            hideShortList();
                            plumbingPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            plumbingPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Bulk Heads":
                        if (!bulkHeadsPane.isVisible()) {
                            hideShortList();
                            bulkHeadsPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            bulkHeadsPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Window Seats":
                        if (!windowSeatsPane.isVisible()) {
                            hideShortList();
                            windowSeatsPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            windowSeatsPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Landscaping":
                        if (!landscapingPane.isVisible()) {
                            hideShortList();
                            landscapingPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            landscapingPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Fencing":
                        if (!fencingPane.isVisible()) {
                            hideShortList();
                            fencingPane.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            fencingPane.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                }
                buttonsAction();
            });
        }
    }

    public void sectionDoneAction() {
        isNew = true;
        canDraw = true;
        color = colorPicker.getValue();

        hideShortList();
        shortListPane.setVisible(false);
        sectionPane.setVisible(false);
        setsComboBox.setItems(null);
    }

    private void hideShortList() {
        preliminaryAndGeneralPane.setVisible(false);
        foundationsPane.setVisible(false);
        prestressedFloorsPane.setVisible(false);
        blockOpeningsPane.setVisible(false);
        blockWallsPane.setVisible(false);
        floorPackingPane.setVisible(false);
        subfloorPane.setVisible(false);
        intFloorLev1Pane.setVisible(false);
        intFloorLev2Pane.setVisible(false);
        extOpeningsPane.setVisible(false);
        intOpeningsPane.setVisible(false);
        braceHardwarePane.setVisible(false);
        braceSglLevPane.setVisible(false);
        interTenancySectionPane.setVisible(false);
        wetLiningsPane.setVisible(false);
        wallStrappingPane.setVisible(false);
        miscManufPane.setVisible(false);
        postAndBeamHardwarePane.setVisible(false);
        wallsSglLevPane.setVisible(false);
        wallsBasementPane.setVisible(false);
        wallsGndLevPane.setVisible(false);
        wallsLev1Pane.setVisible(false);
        wallsLev2Pane.setVisible(false);
        wallsLev3Pane.setVisible(false);
        wallsLev4Pane.setVisible(false);
        bp_packersPane.setVisible(false);
        wallHardwarePane.setVisible(false);
        boltsManualPane.setVisible(false);
        chimneyPane.setVisible(false);
        trussesPane.setVisible(false);
        roofPane.setVisible(false);
        extLiningPane.setVisible(false);
        rainScreenPane.setVisible(false);
        wetCeilingsPane.setVisible(false);
        ceilingsPane.setVisible(false);
        cupboardsPane.setVisible(false);
        showersAndBathsPane.setVisible(false);
        decksPane.setVisible(false);
        pergolaPane.setVisible(false);
        miscellaniousPane.setVisible(false);
        plumbingPane.setVisible(false);
        bulkHeadsPane.setVisible(false);
        windowSeatsPane.setVisible(false);
        landscapingPane.setVisible(false);
        fencingPane.setVisible(false);
    }

    private void buttonsAction() {

//        prestressedFloorsBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
//            showSets(((JFXButton) node).getText());
//        }));
//        blockOpeningsBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
//            showSets(((JFXButton) node).getText());
//        }));
//        blockWallsBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
//            showSets(((JFXButton) node).getText());
//        }));
//        floorPackingBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
//            showSets(((JFXButton) node).getText());
//        }));
//        subfloorBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
//            showSets(((JFXButton) node).getText());
//        }));
//        intFloorLev1Box.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
//            showSets(((JFXButton) node).getText());
//        }));
//        intFloorLev2Box.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
//            showSets(((JFXButton) node).getText());
//        }));
//        extOpeningsBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
//            showSets(((JFXButton) node).getText());
//        }));
//        intOpeningsBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
//            showSets(((JFXButton) node).getText());
//        }));

        //skip to ext walls
        wallsLev1Box.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
    }

    private void showSets(String type) {
        //foundations
        if (type.equals("Post Footings")) {
            setsComboBox.setItems(FOUNDATIONS_POST_FOOTINGS);
        }
        else if (type.equals("Concrete Bores")) {
            setsComboBox.setItems(FOUNDATIONS_CONCRETE_BORES);
        }
        else if (type.equals("Footings")) {
            setsComboBox.setItems(FOUNDATIONS_FOOTINGS);
        }
        else if (type.equals("Concrete Floor") && foundationsPane.isVisible()) {
            setsComboBox.setItems(FOUNDATIONS_CONCRETE_FLOOR);
        }
        else if (type.equals("Raft Piles")) {
            setsComboBox.setItems(FOUNDATIONS_RAFT_PILES);
        }
        else if (type.equals("Raft Footings")) {
            setsComboBox.setItems(FOUNDATIONS_RAFT_FOOTINGS);
        }
        else if (type.equals("Raft Slab")) {
            setsComboBox.setItems(FOUNDATIONS_RAFT_SLAB);
        }
        else if (type.equals("Patio Footings")) {
            setsComboBox.setItems(FOUNDATIONS_PATIO_FOOTINGS);
        }
        else if (type.equals("Patio Slab")) {
            setsComboBox.setItems(FOUNDATIONS_PATIO_SLAB);
        }
        else if (type.equals("Carpot Footings")) {
            setsComboBox.setItems(FOUNDATIONS_CARPOT_FOOTINGS);
        }
        else if (type.equals("Carpot Slab")) {
            setsComboBox.setItems(FOUNDATIONS_CARPOT_SLAB);
        }
        else if (type.equals("Deck Footing")) {
            setsComboBox.setItems(FOUNDATIONS_DECK_FOOTING);
        }
        else if (type.equals("Deck Slab")) {
            setsComboBox.setItems(FOUNDATIONS_DECK_SLAB);
        }
        else if (type.equals("Wall Plates")) {
            setsComboBox.setItems(FOUNDATIONS_WALL_PLATES);
        }
        else if (type.equals("Water Proofing")) {
            setsComboBox.setItems(FOUNDATIONS_WATER_PROOFING);
        }
        else if (type.equals("Columns")) {
            setsComboBox.setItems(FOUNDATIONS_COLUMNS);
        }
        else if (type.equals("Beams")) {
            setsComboBox.setItems(FOUNDATIONS_BEAM);
        }
        else if (type.equals("Concrete Walls")) {
            setsComboBox.setItems(FOUNDATIONS_CONCRETE_WALL);
        }
        else if (type.equals("Conc Steps")) {
            setsComboBox.setItems(FOUNDATIONS_CONC_STEPS);
        }
        else if (type.equals("Block Cnrs & Ends")) {
            setsComboBox.setItems(FOUNDATIONS_BLOCK_CNRS_ENDS);
        }
        else if (type.equals("Profiles")) {
            setsComboBox.setItems(FOUNDATIONS_PROFILES);
        }
        else if (type.equals("Boxing")) {
            setsComboBox.setItems(FOUNDATIONS_BOXING);
        }
        //prestressed floors
        else if (type.equals("Concrete Floor") && prestressedFloorsPane.isVisible()) {
            setsComboBox.setItems(PRESTRESSED_FLOORS_CONCRETE_FLOOR);
        }
        else if (type.equals("Concrete Deck")) {
            setsComboBox.setItems(PRESTRESSED_FLOORS_CONCRETE_DECK);
        }
        else if (type.equals("Saddles")) {
            setsComboBox.setItems(PRESTRESSED_FLOORS_SADDLES);
        }
        else if (type.equals("Beams") && prestressedFloorsPane.isVisible()) {
            setsComboBox.setItems(PRESTRESSED_FLOORS_BEAM);
        }
        //block openings
        else if (type.equals("Lintels")) {
            setsComboBox.setItems(BLOCK_OPENINGS_LINTELS);
        }
        else if (type.equals("Doors")) {
            setsComboBox.setItems(BLOCK_OPENINGS_DOORS);
        }
        else if (type.equals("Windows")) {
            setsComboBox.setItems(BLOCK_OPENINGS_WINDOWS);
        }
        //block walls
        else if (type.equals("Walls")) {
            setsComboBox.setItems(BLOCK_WALLS);
        }
        else if (type.equals("Cnrs & Ends")) {
            setsComboBox.setItems(BLOCK_WALLS_CNRS_ENDS);
        }
        else if (type.equals("Water Proofing") && blockWallsPane.isVisible()) {
            setsComboBox.setItems(BLOCK_WALLS_WATER_PROOFING);
        }
        else if (type.equals("Cols & Piers")) {
            setsComboBox.setItems(BLOCK_WALLS_COLS_PIERS);
        }
        else if (type.equals("C & P Measured Items")) {
            setsComboBox.setItems(BLOCK_WALLS_CP_MEASURED_ITEMS);
        }
        else if (type.equals("Conc Lintels")) {
            setsComboBox.setItems(BLOCK_WALLS_CONC_LINTELS);
        }
        else if (type.equals("Beams") && blockWallsPane.isVisible()) {
            setsComboBox.setItems(BLOCK_WALLS_BEAMS);
        }
        else if (type.equals("Wall Plates") && blockWallsPane.isVisible()) {
            setsComboBox.setItems(BLOCK_WALLS_PLATES);
        }
        //floor packing
        else if (type.equals("Conc Packing")) {
            setsComboBox.setItems(FLOOR_PACKING_CONC_PACKING);
        }
        else if (type.equals("Flooring")) {
            setsComboBox.setItems(FLOOR_PACKING_FLOORING);
        }
        //subfloor
        else if (type.equals("Pile Concrete Measured")) {
            setsComboBox.setItems(SUBFLOOR_PILE_CONCRETE_MEASURED);
        }
        else if (type.equals("Pile Concrete Round")) {
            setsComboBox.setItems(SUBFLOOR_CONCRETE_ROUND);
        }
        else if (type.equals("Concrete Block/Ring Wall")) {
            setsComboBox.setItems(SUBFLOOR_CONCRETE_BLOCK_RING_WALL);
        }
        else if (type.equals("Subfloor Measured")) {
            setsComboBox.setItems(SUBFLOOR_MEASURED);
        }
        else if (type.equals("Bracing")) {
            setsComboBox.setItems(SUBFLOOR_BRACING);
        }
        else if (type.equals("Steel Beams")) {
            setsComboBox.setItems(SUBFLOOR_STEEL_BEAMS);
        }
        else if (type.equals("Framing Measured")) {
            setsComboBox.setItems(SUBFLOOR_FRAMING_MEASURED);
        }
        else if (type.equals("Flooring") && subfloorPane.isVisible()) {
            setsComboBox.setItems(SUBFLOOR_STEEL_BEAMS);
        }
        else if (type.equals("Jack Framing")) {
            setsComboBox.setItems(SUBFLOOR_JACK_FRAMING);
        }
        else if (type.equals("Base")) {
            setsComboBox.setItems(SUBFLOOR_BASE);
        }
        else if (type.equals("Difficult Base")) {
            setsComboBox.setItems(SUBFLOOR_DIFFICULT_BASE);
        }
        else if (type.equals("Base/Door Accessories")) {
            setsComboBox.setItems(SUBFLOOR_BASE_DOOR_ACCESSORIES);
        }
        //ext walls (skip)
        else if (type.equals("Exterior")) {
            setsComboBox.setItems(WALLS_LEV1_EXTERIOR);
        }
        else {
            setsComboBox.setItems(null);
        }
    }

}
