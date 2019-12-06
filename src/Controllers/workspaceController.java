package Controllers;

import Data.historyData;
import Main.Main;
import Model.PageObject;
import Model.ShapeObject;
import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerNextArrowBasicTransition;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.graphics.*;
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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
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

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.List;
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
    public AnchorPane preliminaryAndGeneralBox, foundationsBox, prestressedFloorsBox, blockOpeningsBox, blockWallsBox,
            floorPackingBox, subfloorBox, intFloorLev1Box, intFloorLev2Box, extOpeningsBox, intOpeningsBox,
            braceHardwareBox, braceSglLevBox, interTenancySectionBox, wetLiningsBox, wallStrappingBox, miscManufBox,
            postAndBeamHardwareBox, wallsSglLevBox, wallsBasementBox, wallsGndLevBox, wallsLev1Box, wallsLev2Box,
            wallsLev3Box, wallsLev4Box, bp_packersBox, wallHardwareBox, boltsManualBox, chimneyBox, trussesBox,
            roofBox, extLiningBox, rainScreenBox, wetCeilingsBox, ceilingsBox, cupboardsBox, showersAndBathsBox,
            decksBox, pergolaBox, miscellaniousBox, plumbingBox, bulkHeadsBox, windowSeatsBox, landscapingBox, fencingBox;

    //containers
    public AnchorPane frontPane, structurePane, shortListPane;
    //    public AnchorPane frontPane, structurePane, shortListPane, preliminaryAndGeneralBox, foundationsBox,
//            prestressedFloorsBox, blockOpeningsBox, blockWallsBox, floorPackingBox;
    public ScrollPane scroller, structureScrollPane;
    public Group scrollContent, group;
    public StackPane zoomPane;
    public Canvas canvas;
    public Pane pane;
    public JFXDrawer drawer;
    public VBox structureBox, shortListBox, foundBox;
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
    ArrayList<String> shortList = new ArrayList<>();
    ArrayList<PageObject> pageList = new ArrayList<>();
    HashMap<String, ObservableList<String>> hashMap = new HashMap<>();

    int pageNumber = 0;
    int numberOfPages = 0;
    File pdfFile;
    PdfDocument doc;
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
            "17.5 Post", "17.5 Slab", "17.5 Con Footings D12 CHANGE", "17.5 Con Post D12 CHANGE", "17.5 Con Slab D12 CHANGE");

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
            "200x25 Cedar Rusticated DR", "200x25 Cedar Shiplap BS", "200x25 Cedar Shiplap DR", "200x25 FJ PP Bev Back",
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
            "Econ Rimu-Rimu Jamb 75mm", "Econ Rimu-Rimu Jamb 100mm");


    //skip to ext walls
    private ObservableList<String> WALLS_LEV1_EXTERIOR = FXCollections.observableArrayList("90x45 H1.2 2.4L Timb R2.2",
            "90x45 H1.2 2.7L Timb R2.2", "90x45 H1.2 3.0L Timb R2.2", "90x45 H1.2 3.3L Timb R2.2", "90x45 H1.2 3.6L Timb R2.2",
            "90x45 H1.2 Rake Timb R2.2", "90x45 H1.2 Arch Timb R2.2", "90x45 H1.2 Curv Timb R2.2", "140x45 H1.2 <3.0m Timb R2.2",
            "140x45 H1.2 3.0m-5.9m Timb R2.2", "140x45 H1.2 Rake Timb R2.2", "140x45 H1.2 Arch Timb R2.2", "140x45 H1.2 Curv Timb R2.2",
            "190x45 H1.2 <3.0m Timb R2.2", "190x45 H1.2 3.0m-6.0 Timb R1", "190x45 H1.2 Rake Timb R2.2", "Gib to Existing Walls",
            "z90x35 H1.2 2.4L Timb R2.2", "z90x35 H1.2 2.7L Timb R2.2", "z90x45 Doug-Fir H1.2 Conc R2.2");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hashMap.put("FOUNDATIONS_POST_FOOTINGS", FOUNDATIONS_POST_FOOTINGS);
        hashMap.put("FOUNDATIONS_CONCRETE_BORES", FOUNDATIONS_CONCRETE_BORES);
        hashMap.put("FOUNDATIONS_FOOTINGS", FOUNDATIONS_FOOTINGS);
        hashMap.put("FOUNDATIONS_RAFT_PILES", FOUNDATIONS_RAFT_PILES);
        HashMap<String,HashMap<String,HashMap<String,ObservableList<String>>>> foundation = new HashMap<>();
        HashMap<String,HashMap<String,HashMap<String,ObservableList<String>>>> walls = new HashMap<>();

//        foundation.put("SETS",hashMap);
        ArrayList<HashMap> structureList = new ArrayList<>();
        structureList.add(foundation);
        structureList.get(0).get("SETS");


        hashMap.get("FOUNDATIONS_RAFT_PILES").forEach(s -> {
            System.out.println("FROM MAP " + s);
        });

        origScaleX = group.getScaleX();
        origScaleY = group.getScaleY();

        line.setVisible(false);
        line.setOpacity(.5);
        line.setStrokeLineCap(StrokeLineCap.BUTT);

        pane.getChildren().add(line);
        foundBox.getChildren().forEach(nodes -> {
            nodes.setOnMouseReleased(event -> {
                sectionPane.setVisible(true);
            });
        });
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

    //==FILE FUNCTIONS
    public void openFile(ActionEvent actionEvent) {
        FileChooser openFile = new FileChooser();
        openFile.setTitle("Open PDF");

        File temp = new File("");
        if (pdfFile != null) {
            temp = pdfFile;
        }
        pdfFile = openFile.showOpenDialog(Main.dashboard_stage);
        if (pdfFile == null) {
            pdfFile = temp;
            JOptionPane.showMessageDialog(null, "No File selected");
        } else {
            if (doc != null) {
                doc.close();
            }
            shapeObjList.clear();
            shapeObjList.clear();
            pageList.clear();
            shapeList.clear();
            m_Scale = 0;

            String inputFile = pdfFile.getAbsolutePath();
            doc = new PdfDocument();
            doc.loadFromFile(inputFile);

            BufferedImage bImage = doc.saveAsImage(0, PdfImageType.Bitmap, 300, 300);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    BufferedImage imgs;
                    for (int x = 1; x < doc.getPages().getCount(); x++) {
                        imgs = doc.saveAsImage(x, PdfImageType.Bitmap, 300, 300);
                        Image img = SwingFXUtils.toFXImage(imgs, null);
                        pageList.add(new PageObject(x, img));
                    }
                }
            }).start();
            image = SwingFXUtils.toFXImage(bImage, null);
            pageList.add(new PageObject(0, image));
            pageList.get(0).setShapeObjList(shapeObjList);
            numberOfPages = doc.getPages().getCount();
            setupCanvas();
            scroller.setOpacity(1.0);
            frontPane.setVisible(false);
        }
    }

    public void saveFile(ActionEvent actionEvent) {
        pageList.get(pageNumber).setShapeObjList(shapeObjList);
        group.setScaleX(1);
        group.setScaleY(1);

        FileChooser savefile = new FileChooser();
        savefile.setTitle("Save PDF");
        String pdfName = pdfFile.getName().replaceAll(".pdf", "");
//        String path = pdfFile.getAbsolutePath().replaceAll(pdfFile.getName(), "");
        savefile.setInitialFileName(pdfName + "[EXPORTED].pdf");

//        savefile.setInitialDirectory(new File(path));

        File file = savefile.showSaveDialog(Main.stage);
        if (file != null) {
            for (int ctr = 0; ctr < doc.getPages().getCount(); ctr++) {
                PdfPageBase page = doc.getPages().get(ctr);
                page.getCanvas().setTransparency(0.5f, 0.5f, PdfBlendMode.Normal);
                PdfGraphicsState state = page.getCanvas().save();
                page.getCanvas().translateTransform(0, 0);

                double subX = pageList.get(ctr).getImage().getWidth() / page.getSize().getWidth();
                double subY = pageList.get(ctr).getImage().getHeight() / page.getSize().getHeight();

                shapeObjList = pageList.get(ctr).getShapeList();
                shapeObjList.forEach(shapeObject -> {
                    PdfRGBColor pdfRGBColor = new PdfRGBColor(new java.awt.Color((float) shapeObject.getColor().getRed(), (float) shapeObject.getColor().getGreen(), (float) shapeObject.getColor().getBlue()));
                    PdfFont font = new PdfFont(PdfFontFamily.Helvetica, 8);
                    if (shapeObject.getType().equals("AREA")) {
                        PdfPen pen = new PdfPen(pdfRGBColor, 3);
                        int ndx = 0;
                        PdfBrush brush = new PdfSolidBrush(new PdfRGBColor(pdfRGBColor));
                        java.awt.geom.Point2D[] points = new java.awt.geom.Point2D[shapeObject.getPointList().size()];
                        for (Point2D p2d : shapeObject.getPointList()) {
                            System.out.println(p2d);
                            points[ndx] = new java.awt.geom.Point2D.Double(p2d.getX() / subX, (p2d.getY() / subY) - 1.8);
                            ndx++;
                        }
                        page.getCanvas().drawPolygon(brush, points);

                        PdfPen pens = new PdfPen(pdfRGBColor, 1);
                        double layX = shapeObject.getPolygon().getBoundsInParent().getMinX() + (shapeObject.getPolygon().getBoundsInParent().getWidth()) / 2;
                        double layY = shapeObject.getPolygon().getBoundsInParent().getMinY() + (shapeObject.getPolygon().getBoundsInParent().getHeight()) / 2;
                        page.getCanvas().drawString(shapeObject.getArea() + " m²", font, pens, (float) layX / subX, (float) layY / subY);

                    } else if (shapeObject.getType().equals("LENGTH")) {
                        PdfPen pens = new PdfPen(pdfRGBColor, 1);
                        shapeObject.getLineList().forEach(line1 -> {
                            Point2D p1 = new Point2D(line1.getStartX(), line1.getStartY());
                            Point2D p2 = new Point2D(line1.getEndX(), line1.getEndY());
                            Point2D mid = p1.midpoint(p2);
                            page.getCanvas().drawString(shapeObject.getLength() + " mm", font, pens, (float) mid.getX() / subX, (float) mid.getY() / subY);
                        });

                    }
                    shapeObject.getLineList().forEach(line1 -> { 
                        PdfPen pen = new PdfPen(pdfRGBColor, 2);
                        page.getCanvas().drawLine(pen, line1.getStartX() / subX, (line1.getStartY() / subY) - 1.8, line1.getEndX() / subX, (line1.getEndY() / subY) - 1.8);
                    });
                    shapeObject.getPointList().forEach(point2D -> {
                        PdfPen pen = new PdfPen(PdfPens.getLightGreen().getColor(), 2);
                        page.getCanvas().drawRectangle(pen, (point2D.getX() / subX) - 5, (point2D.getY() / subY) - 6.8, 10, 10);
                    });
                });
                page.getCanvas().restore(state);
            }
            if (!file.getName().contains(".")) {
                file = new File(file.getAbsolutePath() + ".pdf");
                doc.saveToFile(file.getAbsolutePath());
            }
        } else {
            JOptionPane.showMessageDialog(null, "No File selected");
        }
    }

    //==SIDE BUTTON ACTIONS
    public void scaleAction(ActionEvent actionEvent) {
        mode = "SCALE";
        canDraw = true;
        isNew = true;
    }

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

    public void nextPageAction(ActionEvent actionEvent) {
        pageList.get(pageNumber).setShapeObjList(shapeObjList);
        if (pageNumber < numberOfPages - 1) {
            pageNumber++;
            setupPageElements();
        }
    }

    public void previousPageAction(ActionEvent actionEvent) {
        pageList.get(pageNumber).setShapeObjList(shapeObjList);
        if (pageNumber > 0) {
            pageNumber--;
            setupPageElements();
        }
    }

    public void drawRectAction(ActionEvent actionEvent) {
        mode = "DRAW_RECT";
        canDraw = true;
        isNew = true;
    }

    public void stampAction() {
        mode = "STAMP";
        canDraw = true;
    }


    //==DRAW FUNCTIONS
    public void drawAction(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {
            return;
        }
        if (!canDraw) {
            return;
        }
        Point2D clamp = clamp(event.getX(), event.getY());

        if (mode == "SCALE") {
            if (isNew) {
                line.setVisible(true);
                line.setStartX(clamp.getX());
                line.setStartY(clamp.getY());
                line.setEndX(clamp.getX());
                line.setEndY(clamp.getY());
                line.setStroke(Color.CHOCOLATE);
                line.setStrokeWidth(8 / group.getScaleY());
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
                line.setStrokeWidth(8 / group.getScaleY());
                line.setStroke(color);
                pane.getChildren().add(createBox(line.getEndX(), line.getEndY()));
                line.setVisible(true);
                isNew = false;
            } else {
                Point2D end = new Point2D(line.getEndX(), line.getEndY());
                pointList.add(end);

                ShapeObject shapeObj = new ShapeObject();
                shapeObj.setPane(pane);
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

                //==TODO add in History
                //workspaceSideNavigatorController.historyList.addAll(new historyData(mode, lbl.getText()));
            }
        } else if (mode == "AREA") {
            if (isNew) {
                line.setStartX(clamp.getX());
                line.setStartY(clamp.getY());
                line.setEndX(clamp.getX());
                line.setEndY(clamp.getY());
                pointList.add(clamp);
                line.setStrokeWidth(8 / group.getScaleY());
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

    public void updateRect(MouseEvent event) {
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
        }
    }

    public Shape createBox(double x, double y) {

        double boxW = 20;
        shapeList.add(new Rectangle(x - (10) / group.getScaleY(), y - (10) / group.getScaleY(), boxW / group.getScaleY(), boxW / group.getScaleY()));
        Rectangle r = (Rectangle) shapeList.get(shapeList.size() - 1);
        r.setStroke(Color.GREEN);
        r.setOpacity(.5);
        r.setStrokeWidth(6 / group.getScaleY());
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
            shapeObj.setStrokeWidth(6 / group.getScaleY());
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
        l.setStrokeWidth(8 / group.getScaleY());
        l.setOpacity(.5);

        l.setOnMouseEntered(event -> {
            l.setStroke(Color.RED);
        });
        l.setOnMouseExited(event -> {
            l.setStroke(color);
        });
        return shapeList.get(shapeList.size() - 1);
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
                sp0.setArea(Math.round(area * 100.0) / 100.0);
                Label lbl = new Label(sp0.getArea() + " m²");
                lbl.setFont(new Font("Arial", 36));
                lbl.setTextFill(sp0.getColor());
                double layX = sp0.getPolygon().getBoundsInParent().getMinX() + (sp0.getPolygon().getBoundsInParent().getWidth() - lbl.getWidth()) / 2;
                double layY = sp0.getPolygon().getBoundsInParent().getMinY() + (sp0.getPolygon().getBoundsInParent().getHeight() - lbl.getHeight()) / 2;
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
                    if (p1.distance(p2) != 0) {
                        double length = (p1.distance(p2) / m_Scale);
                        sp1.setLength(Math.round(length * 100.0) / 100.0);
                        Label lbl = new Label(sp1.getLength() + " mm");
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
                if (sp.getClass() == Rectangle.class) {
                    Rectangle rect = (Rectangle) sp;
                    double centerX = rect.getX() + rect.getWidth() / 2;
                    double centerY = rect.getY() + rect.getHeight() / 2;
                    rect.setStrokeWidth(6 / group.getScaleY());
                    rect.setX(centerX - 10 / group.getScaleY());
                    rect.setY(centerY - 10 / group.getScaleY());
                    rect.setWidth(20 / group.getScaleY());
                    rect.setHeight(20 / group.getScaleY());
                } else {
                    sp.setStrokeWidth(8 / group.getScaleY());
                }
            }
            if (node.getClass() == Label.class) {
                Label lbl = (Label) node;
                lbl.setFont(new Font("Arial", 18 / group.getScaleY()));
            }
        });
    }


    //==PAGE FUNCTIONS
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
                        if (sp.getClass() == Rectangle.class) {
                            Rectangle rect = (Rectangle) sp;
                            double centerX = rect.getX() + rect.getWidth() / 2;
                            double centerY = rect.getY() + rect.getHeight() / 2;
                            rect.setStrokeWidth(6 / group.getScaleY());
                            rect.setX(centerX - 10 / group.getScaleY());
                            rect.setY(centerY - 10 / group.getScaleY());
                            rect.setWidth(20 / group.getScaleY());
                            rect.setHeight(20 / group.getScaleY());
                        } else {
                            sp.setStrokeWidth(8 / group.getScaleY());
                        }
                    }
                    if (node.getClass() == Label.class) {
                        Label lbl = (Label) node;
                        lbl.setFont(new Font("Arial", 18 / group.getScaleY()));
                    }
                });
            }
            System.out.println("Zoom " + group.getScaleY());
        }
    }

    private Point2D figureScrollOffset(Node scrollContent, ScrollPane scroller) {
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        double hScrollProportion = (scroller.getHvalue() - scroller.getHmin()) / (scroller.getHmax() - scroller.getHmin());
        double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        double vScrollProportion = (scroller.getVvalue() - scroller.getVmin()) / (scroller.getVmax() - scroller.getVmin());
        double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);
        return new Point2D(scrollXOffset, scrollYOffset);
    }

    private void repositionScroller(Node scrollContent, ScrollPane scroller, double scaleFactor, Point2D
            scrollOffset) {
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

    public void handlePan(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            scroller.setPannable(false);
        } else {
            scroller.setPannable(true);
        }
    }


    //==PAGE SETUPS
    public void setupCanvas() {
        group.setScaleY(1);
        group.setScaleX(1);
        canvas.setWidth(image.getWidth());
        canvas.setHeight(image.getHeight());
        pane.setPrefSize(canvas.getWidth(), canvas.getHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(image, 0, 0);
        origScaleX = group.getScaleX();
        origScaleY = group.getScaleY();
        double newScale = group.getScaleX();
        for (int x = 0; x < 12; x++) {
            newScale *= (1 / 1.1);
        }
        group.setScaleX(newScale);
        group.setScaleY(newScale);
        redrawShapes();
    }

    public void setupPageElements() {
        shapeObjList = new ArrayList<>();
        try {
            shapeObjList.addAll(pageList.get(pageNumber).getShapeList());
            image = pageList.get(pageNumber).getImage();
        } catch (Exception e) {
            BufferedImage bImage = doc.saveAsImage(pageNumber, PdfImageType.Bitmap, 300, 300);
            image = SwingFXUtils.toFXImage(bImage, null);
            pageList.add(new PageObject(pageNumber, image));
        }
        setupCanvas();
        redrawShapes();
    }

    //==MEASUREMENT POPUPS
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
                        if (!preliminaryAndGeneralBox.isVisible()) {
                            hideShortList();
                            preliminaryAndGeneralBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            preliminaryAndGeneralBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Foundations":
                        if (!foundationsBox.isVisible()) {
                            hideShortList();
                            foundationsBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            foundationsBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Prestressed Floors":
                        if (!prestressedFloorsBox.isVisible()) {
                            hideShortList();
                            prestressedFloorsBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            prestressedFloorsBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Block Openings":
                        if (!blockOpeningsBox.isVisible()) {
                            hideShortList();
                            blockOpeningsBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            blockOpeningsBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Block Walls":
                        if (!blockWallsBox.isVisible()) {
                            hideShortList();
                            blockWallsBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            blockWallsBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Floor Packing":
                        if (!floorPackingBox.isVisible()) {
                            hideShortList();
                            floorPackingBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            floorPackingBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Subfloor":
                        if (!subfloorBox.isVisible()) {
                            hideShortList();
                            subfloorBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            subfloorBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Int Floor Lev 1":
                        if (!intFloorLev1Box.isVisible()) {
                            hideShortList();
                            intFloorLev1Box.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            intFloorLev1Box.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Int Floor Lev 2":
                        if (!intFloorLev2Box.isVisible()) {
                            hideShortList();
                            intFloorLev2Box.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            intFloorLev2Box.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Ext Openings":
                        if (!extOpeningsBox.isVisible()) {
                            hideShortList();
                            extOpeningsBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            extOpeningsBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Int Openings":
                        if (!intOpeningsBox.isVisible()) {
                            hideShortList();
                            intOpeningsBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            intOpeningsBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Brace Hardware":
                        if (!braceHardwareBox.isVisible()) {
                            hideShortList();
                            braceHardwareBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            braceHardwareBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Brace Sgl Lev":
                        if (!braceSglLevBox.isVisible()) {
                            hideShortList();
                            braceSglLevBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            braceSglLevBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Inter-Tenancy Section":
                        if (!interTenancySectionBox.isVisible()) {
                            hideShortList();
                            interTenancySectionBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            interTenancySectionBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Wet Linings":
                        if (!wetLiningsBox.isVisible()) {
                            hideShortList();
                            wetLiningsBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wetLiningsBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Wall Strapping":
                        if (!wallStrappingBox.isVisible()) {
                            hideShortList();
                            wallStrappingBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wallStrappingBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Misc Manuf":
                        if (!miscManufBox.isVisible()) {
                            hideShortList();
                            miscManufBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            miscManufBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Post&Beam Hardware":
                        if (!postAndBeamHardwareBox.isVisible()) {
                            hideShortList();
                            postAndBeamHardwareBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            postAndBeamHardwareBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Walls Sgl Lev":
                        if (!wallsSglLevBox.isVisible()) {
                            hideShortList();
                            wallsSglLevBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wallsSglLevBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Walls Basement":
                        if (!wallsBasementBox.isVisible()) {
                            hideShortList();
                            wallsBasementBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wallsBasementBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Walls Gnd Lev":
                        if (!wallsGndLevBox.isVisible()) {
                            hideShortList();
                            wallsGndLevBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wallsGndLevBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Walls Lev 1":
                        if (!wallsLev1Box.isVisible()) {
                            hideShortList();
                            wallsLev1Box.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wallsLev1Box.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Walls Lev 2":
                        if (!wallsLev2Box.isVisible()) {
                            hideShortList();
                            wallsLev2Box.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wallsLev2Box.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Walls Lev 3":
                        if (!wallsLev3Box.isVisible()) {
                            hideShortList();
                            wallsLev3Box.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wallsLev3Box.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Walls Lev 4":
                        if (!wallsLev4Box.isVisible()) {
                            hideShortList();
                            wallsLev4Box.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wallsLev4Box.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "BP Packers":
                        if (!bp_packersBox.isVisible()) {
                            hideShortList();
                            bp_packersBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            bp_packersBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Wall Hardware":
                        if (!wallHardwareBox.isVisible()) {
                            hideShortList();
                            wallHardwareBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wallHardwareBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Bolts Manual":
                        if (!boltsManualBox.isVisible()) {
                            hideShortList();
                            boltsManualBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            boltsManualBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Chimney":
                        if (!chimneyBox.isVisible()) {
                            hideShortList();
                            chimneyBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            chimneyBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Trusses":
                        if (!trussesBox.isVisible()) {
                            hideShortList();
                            trussesBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            trussesBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Roof":
                        if (!roofBox.isVisible()) {
                            hideShortList();
                            roofBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            roofBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Ext Lining":
                        if (!extLiningBox.isVisible()) {
                            hideShortList();
                            extLiningBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            extLiningBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Rain Screen":
                        if (!rainScreenBox.isVisible()) {
                            hideShortList();
                            rainScreenBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            rainScreenBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Wet Ceilings":
                        if (!wetCeilingsBox.isVisible()) {
                            hideShortList();
                            wetCeilingsBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            wetCeilingsBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Ceilings":
                        if (!ceilingsBox.isVisible()) {
                            hideShortList();
                            ceilingsBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            ceilingsBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Cupboards":
                        if (!cupboardsBox.isVisible()) {
                            hideShortList();
                            cupboardsBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            cupboardsBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Showers & Baths":
                        if (!showersAndBathsBox.isVisible()) {
                            hideShortList();
                            showersAndBathsBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            showersAndBathsBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Decks":
                        if (!decksBox.isVisible()) {
                            hideShortList();
                            decksBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            decksBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Pergola":
                        if (!pergolaBox.isVisible()) {
                            hideShortList();
                            pergolaBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            pergolaBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Miscellanious":
                        if (!miscellaniousBox.isVisible()) {
                            hideShortList();
                            miscellaniousBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            miscellaniousBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Plumbing":
                        if (!plumbingBox.isVisible()) {
                            hideShortList();
                            plumbingBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            plumbingBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Bulk Heads":
                        if (!bulkHeadsBox.isVisible()) {
                            hideShortList();
                            bulkHeadsBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            bulkHeadsBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Window Seats":
                        if (!windowSeatsBox.isVisible()) {
                            hideShortList();
                            windowSeatsBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            windowSeatsBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Landscaping":
                        if (!landscapingBox.isVisible()) {
                            hideShortList();
                            landscapingBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            landscapingBox.setVisible(false);
                            sectionPane.setVisible(false);
                        }
                        break;
                    case "Fencing":
                        if (!fencingBox.isVisible()) {
                            hideShortList();
                            fencingBox.setVisible(true);
                            sectionPane.setVisible(true);
                        } else {
                            fencingBox.setVisible(false);
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
        preliminaryAndGeneralBox.setVisible(false);
        foundationsBox.setVisible(false);
        prestressedFloorsBox.setVisible(false);
        blockOpeningsBox.setVisible(false);
        blockWallsBox.setVisible(false);
        floorPackingBox.setVisible(false);
        subfloorBox.setVisible(false);
        intFloorLev1Box.setVisible(false);
        intFloorLev2Box.setVisible(false);
        extOpeningsBox.setVisible(false);
        intOpeningsBox.setVisible(false);
        braceHardwareBox.setVisible(false);
        braceSglLevBox.setVisible(false);
        interTenancySectionBox.setVisible(false);
        wetLiningsBox.setVisible(false);
        wallStrappingBox.setVisible(false);
        miscManufBox.setVisible(false);
        postAndBeamHardwareBox.setVisible(false);
        wallsSglLevBox.setVisible(false);
        wallsBasementBox.setVisible(false);
        wallsGndLevBox.setVisible(false);
        wallsLev1Box.setVisible(false);
        wallsLev2Box.setVisible(false);
        wallsLev3Box.setVisible(false);
        wallsLev4Box.setVisible(false);
        bp_packersBox.setVisible(false);
        wallHardwareBox.setVisible(false);
        boltsManualBox.setVisible(false);
        chimneyBox.setVisible(false);
        trussesBox.setVisible(false);
        roofBox.setVisible(false);
        extLiningBox.setVisible(false);
        rainScreenBox.setVisible(false);
        wetCeilingsBox.setVisible(false);
        ceilingsBox.setVisible(false);
        cupboardsBox.setVisible(false);
        showersAndBathsBox.setVisible(false);
        decksBox.setVisible(false);
        pergolaBox.setVisible(false);
        miscellaniousBox.setVisible(false);
        plumbingBox.setVisible(false);
        bulkHeadsBox.setVisible(false);
        windowSeatsBox.setVisible(false);
        landscapingBox.setVisible(false);
        fencingBox.setVisible(false);
    }

    private void buttonsAction() {
        foundBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            showSets(((JFXButton) node).getText());
        }));
    }

    private void showSets(String type) {
        //foundations
        if (type.equals("Post Footings")) {
            setsComboBox.setItems(FOUNDATIONS_POST_FOOTINGS);
        } else if (type.equals("Concrete Bores")) {
            setsComboBox.setItems(FOUNDATIONS_CONCRETE_BORES);
        } else if (type.equals("Footings")) {
            setsComboBox.setItems(FOUNDATIONS_FOOTINGS);
        } else if (type.equals("Concrete Floor") && foundationsBox.isVisible()) {
            setsComboBox.setItems(FOUNDATIONS_CONCRETE_FLOOR);
        } else if (type.equals("Raft Piles")) {
            setsComboBox.setItems(FOUNDATIONS_RAFT_PILES);
        } else if (type.equals("Raft Footings")) {
            setsComboBox.setItems(FOUNDATIONS_RAFT_FOOTINGS);
        } else if (type.equals("Raft Slab")) {
            setsComboBox.setItems(FOUNDATIONS_RAFT_SLAB);
        } else if (type.equals("Patio Footings")) {
            setsComboBox.setItems(FOUNDATIONS_PATIO_FOOTINGS);
        } else if (type.equals("Patio Slab")) {
            setsComboBox.setItems(FOUNDATIONS_PATIO_SLAB);
        } else if (type.equals("Carpot Footings")) {
            setsComboBox.setItems(FOUNDATIONS_CARPOT_FOOTINGS);
        } else if (type.equals("Carpot Slab")) {
            setsComboBox.setItems(FOUNDATIONS_CARPOT_SLAB);
        } else if (type.equals("Deck Footing")) {
            setsComboBox.setItems(FOUNDATIONS_DECK_FOOTING);
        } else if (type.equals("Deck Slab")) {
            setsComboBox.setItems(FOUNDATIONS_DECK_SLAB);
        } else if (type.equals("Wall Plates")) {
            setsComboBox.setItems(FOUNDATIONS_WALL_PLATES);
        } else if (type.equals("Water Proofing")) {
            setsComboBox.setItems(FOUNDATIONS_WATER_PROOFING);
        } else if (type.equals("Columns")) {
            setsComboBox.setItems(FOUNDATIONS_COLUMNS);
        } else if (type.equals("Beams")) {
            setsComboBox.setItems(FOUNDATIONS_BEAM);
        } else if (type.equals("Concrete Walls")) {
            setsComboBox.setItems(FOUNDATIONS_CONCRETE_WALL);
        } else if (type.equals("Conc Steps")) {
            setsComboBox.setItems(FOUNDATIONS_CONC_STEPS);
        } else if (type.equals("Block Cnrs & Ends")) {
            setsComboBox.setItems(FOUNDATIONS_BLOCK_CNRS_ENDS);
        } else if (type.equals("Profiles")) {
            setsComboBox.setItems(FOUNDATIONS_PROFILES);
        } else if (type.equals("Boxing")) {
            setsComboBox.setItems(FOUNDATIONS_BOXING);
        }
        //prestressed floors
        else if (type.equals("Concrete Floor") && prestressedFloorsBox.isVisible()) {
            setsComboBox.setItems(PRESTRESSED_FLOORS_CONCRETE_FLOOR);
        } else if (type.equals("Concrete Deck")) {
            setsComboBox.setItems(PRESTRESSED_FLOORS_CONCRETE_DECK);
        } else if (type.equals("Saddles")) {
            setsComboBox.setItems(PRESTRESSED_FLOORS_SADDLES);
        } else if (type.equals("Beams") && prestressedFloorsBox.isVisible()) {
            setsComboBox.setItems(PRESTRESSED_FLOORS_BEAM);
        }
        //block openings
        else if (type.equals("Lintels")) {
            setsComboBox.setItems(BLOCK_OPENINGS_LINTELS);
        } else if (type.equals("Doors")) {
            setsComboBox.setItems(BLOCK_OPENINGS_DOORS);
        } else if (type.equals("Windows")) {
            setsComboBox.setItems(BLOCK_OPENINGS_WINDOWS);
        }
        //block walls
        else if (type.equals("Walls")) {
            setsComboBox.setItems(BLOCK_WALLS);
        } else if (type.equals("Cnrs & Ends")) {
            setsComboBox.setItems(BLOCK_WALLS_CNRS_ENDS);
        } else if (type.equals("Water Proofing") && blockWallsBox.isVisible()) {
            setsComboBox.setItems(BLOCK_WALLS_WATER_PROOFING);
        } else if (type.equals("Cols & Piers")) {
            setsComboBox.setItems(BLOCK_WALLS_COLS_PIERS);
        } else if (type.equals("C & P Measured Items")) {
            setsComboBox.setItems(BLOCK_WALLS_CP_MEASURED_ITEMS);
        } else if (type.equals("Conc Lintels")) {
            setsComboBox.setItems(BLOCK_WALLS_CONC_LINTELS);
        } else if (type.equals("Beams") && blockWallsBox.isVisible()) {
            setsComboBox.setItems(BLOCK_WALLS_BEAMS);
        } else if (type.equals("Wall Plates") && blockWallsBox.isVisible()) {
            setsComboBox.setItems(BLOCK_WALLS_PLATES);
        }
        //floor packing
        else if (type.equals("Conc Packing")) {
            setsComboBox.setItems(FLOOR_PACKING_CONC_PACKING);
        } else if (type.equals("Flooring")) {
            setsComboBox.setItems(FLOOR_PACKING_FLOORING);
        }
        //subfloor
        else if (type.equals("Pile Concrete Measured")) {
            setsComboBox.setItems(SUBFLOOR_PILE_CONCRETE_MEASURED);
        } else if (type.equals("Pile Concrete Round")) {
            setsComboBox.setItems(SUBFLOOR_CONCRETE_ROUND);
        } else if (type.equals("Concrete Block/Ring Wall")) {
            setsComboBox.setItems(SUBFLOOR_CONCRETE_BLOCK_RING_WALL);
        } else if (type.equals("Subfloor Measured")) {
            setsComboBox.setItems(SUBFLOOR_MEASURED);
        } else if (type.equals("Bracing")) {
            setsComboBox.setItems(SUBFLOOR_BRACING);
        } else if (type.equals("Steel Beams")) {
            setsComboBox.setItems(SUBFLOOR_STEEL_BEAMS);
        } else if (type.equals("Framing Measured")) {
            setsComboBox.setItems(SUBFLOOR_FRAMING_MEASURED);
        } else if (type.equals("Flooring") && subfloorBox.isVisible()) {
            setsComboBox.setItems(SUBFLOOR_STEEL_BEAMS);
        } else if (type.equals("Jack Framing")) {
            setsComboBox.setItems(SUBFLOOR_JACK_FRAMING);
        } else if (type.equals("Base")) {
            setsComboBox.setItems(SUBFLOOR_BASE);
        } else if (type.equals("Difficult Base")) {
            setsComboBox.setItems(SUBFLOOR_DIFFICULT_BASE);
        } else if (type.equals("Base/Door Accessories")) {
            setsComboBox.setItems(SUBFLOOR_BASE_DOOR_ACCESSORIES);
        }
        //ext walls (skip)
        else if (type.equals("Exterior") && wallsLev1Box.isVisible()) {
            setsComboBox.setItems(WALLS_LEV1_EXTERIOR);
        } else {
            setsComboBox.setItems(null);
        }
    }

}
