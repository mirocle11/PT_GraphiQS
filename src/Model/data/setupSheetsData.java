package Model.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class setupSheetsData {

    public static ObservableList<String> structures = FXCollections.observableArrayList("Foundations",
            "Prestressed Floors", "Block Openings", "Block Walls", "Floor Packing", "Subfloor", "Int Floor Lev 1",
            "Int Floor Lev 2", "Ext Openings", "Int Openings", "Brace Hardware", "Brace Sgl Lev", "Inter-Tenancy Section",
            "Wet Linings", "Wall Strapping", "Misc Manuf", "Post & Beam Hardware", "Walls Sgl Lev", "Walls Bsmnt",
            "Walls Gnd Lev", "Walls Lev 1", "Walls Lev 2", "Walls Lev 3", "Walls Lev 4", "BP Packers", "Wall Hardware",
            "Bolts Manual", "Chimney", "Trusses", "Roof", "Ext Lining", "Rain Screen", "Wet Ceilings", "Ceilings",
            "Cupboards", "Showers & Baths", "Decks", "Pergola", "Miscellanious", "Plumbing", "Bulk Heads", "Window Seats",
            "Landscaping", "Fencing", "Preliminary & General");

    public static ObservableList<String> foundations = FXCollections.observableArrayList("Post Footings",
            "Concrete Bores", "Footings", "Concrete Floor", "Raft Piles", "Raft Footings", "Raft Slab", "Patio Footings",
            "Patio Slab", "Carpot Footings", "Carpot Slab", "Deck Footing", "Deck Slab", "Wall Plates", "Water Proofing",
            "Columns", "Beams", "Concrete Wall", "Conc Steps", "Block Cnrs & Ends", "Site Works", "Profiles", "Boxing");

    public static ObservableList<String> extOpenings = FXCollections.observableArrayList("Wind Hardware",
            "Garage Door", "Doors", "Windows", "Window Heads Standard", "Window Heads Raking", "Cantilever Lintels",
            "Doors Measured Items");

    public static ObservableList<String> intOpenings = FXCollections.observableArrayList("Door Openings", "Windows",
            "Doors Units", "Doors Unit Sl-Bf", "Doors Unit Cav", "Door Unit Gib", "Doors Exp Std", "Doors Exp Sld",
            "Doors Exp BF Panel", "Doors Exp BF Plain", "Doors Cavity");

    public static ObservableList<String> postBeamHardware = FXCollections.observableArrayList("Posts",
            "Post Brackets", "Beam N/Plates", "Beam Connectors");

    public static ObservableList<String> wallsSglLev = FXCollections.observableArrayList("Misc", "Columns",
            "Gables&Jack Frames", "Handrails", "Steel Beams", "Flitched Lintels", "Measured Lintels", "Special Manufacturing",
            "Exterior", "Interior", "Lintels Non SLs", "Studs Nogs Deduction");

    public static ObservableList<String> roof = FXCollections.observableArrayList("Gables Above Brick", "Steel Beams",
            "Ceiling Beams", "Roof Beams", "Soffit Beams", "Fascia Beams", "Hip Beams", "Ridge Beams", "Valley Beams",
            "Trussed Roof Measured", "Skillion Measured", "Exposed Truss Member", "Roof Sundries", "Roof Ply/Substrate",
            "Substrate Measured", "Common Roof", "Gutters", "Hips", "Ridges", "Valleys", "Verge", "Standard Soffit",
            "Difficult Soffits", "Barge", "Fascia", "Spouting Measured", "Aprons", "Change of Roof Pitch", "Parapets",
            "Skylight Walls");

    public static ObservableList<String> extLining = FXCollections.observableArrayList("Gnd Level", "Gnd Ext Corners",
            "Gnd Int Corners", "Ext Enclosed Beams", "Upper Level", "Upper Ext Corners", "Upper Int Corners",
            "Gnd Measured Items", "Upper Measured Items");
}
