package Controllers;

import Main.Main;
import Model.ComponentData;
import Model.data.materialsData;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.*;

/**
 * Created by User on 30/10/2020.
 */
public class materialsContoller implements Initializable {

    public AnchorPane materials;
    public JFXTreeTableView<materialsData> MATERIALS_TABLE;
    public TreeTableColumn<materialsData, String> MATERIALS_COL_SKU;
    public TreeTableColumn<materialsData, String> MATERIALS_COL_DESCRIPTION;
    public TreeTableColumn<materialsData, String> MATERIALS_COL_UNIT;
    public TreeTableColumn<materialsData, String> MATERIALS_COL_QTY;
    public JFXButton MATERIALS_BTN_EXT;
    public JFXButton MATERIALS_BTN_FRAMING;
    public JFXButton MATERIALS_BTN_CLADDING;
    public JFXButton MATERIALS_BTN_FOUNDATION;

    public static ObservableList<materialsData> materials_extFraming;
    public static ObservableList<materialsData> materials_framingHardware;
    public static ObservableList<materialsData> materials_cladding;
    public static ObservableList<materialsData> materials_foundation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        materials_extFraming = FXCollections.observableArrayList();
        materials_framingHardware = FXCollections.observableArrayList();
        materials_cladding = FXCollections.observableArrayList();
        materials_foundation = FXCollections.observableArrayList();

        MATERIALS_COL_SKU.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("sku_number")
        );
        MATERIALS_COL_DESCRIPTION.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("description")
        );
        MATERIALS_COL_UNIT.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("unit")
        );
        MATERIALS_COL_QTY.setCellValueFactory(
                new TreeItemPropertyValueFactory<>("qty")
        );

        TreeItem<materialsData> roota = new RecursiveTreeItem<>(materials_foundation, RecursiveTreeObject::getChildren);
        MATERIALS_TABLE.setRoot(roota);
        MATERIALS_TABLE.setShowRoot(false);

        MATERIALS_BTN_EXT.setOnAction(event -> {
            TreeItem<materialsData> root = new RecursiveTreeItem<>(materials_extFraming, RecursiveTreeObject::getChildren);
            MATERIALS_TABLE.setRoot(root);
            MATERIALS_TABLE.setShowRoot(false);
        });

        MATERIALS_BTN_FRAMING.setOnAction(event -> {
            TreeItem<materialsData> root = new RecursiveTreeItem<>(materials_framingHardware, RecursiveTreeObject::getChildren);
            MATERIALS_TABLE.setRoot(root);
            MATERIALS_TABLE.setShowRoot(false);
        });

        MATERIALS_BTN_CLADDING.setOnAction(event -> {
            TreeItem<materialsData> root = new RecursiveTreeItem<>(materials_cladding, RecursiveTreeObject::getChildren);
            MATERIALS_TABLE.setRoot(root);
            MATERIALS_TABLE.setShowRoot(false);
        });

        MATERIALS_BTN_FOUNDATION.setOnAction(event -> {
            TreeItem<materialsData> root = new RecursiveTreeItem<>(materials_foundation, RecursiveTreeObject::getChildren);
            MATERIALS_TABLE.setRoot(root);
            MATERIALS_TABLE.setShowRoot(false);
//            exportExcel();
        });

    }

    public static void updateTables() {
        ArrayList<ComponentData> ext_framing = new ArrayList<>();
        ArrayList<ComponentData> framing_hardware = new ArrayList<>();
        ArrayList<ComponentData> cladding = new ArrayList<>();
        ArrayList<ComponentData> foundation = new ArrayList<>();
        System.out.println(setupSheetsController.componentList.size() + " MATERIALS COMPONENT SIZE");

        for (String key : setupSheetsController.componentList.keySet()) {
            System.out.println("KEY " + key);
            if (key.contains("foundation")) {
                foundation.add(setupSheetsController.componentList.get(key));
            } else if (key.contains("external_framing")) {
                ext_framing.add(setupSheetsController.componentList.get(key));
            }
        }


        materials_foundation.clear();
        materials_extFraming.clear();

        for (ComponentData cd : foundation) {
            System.out.println(cd.getComponents().size());
            for (String[] arr : cd.getComponents()) {
                materialsData materialsData = null;
                for (materialsData mf : materials_foundation) {
                    if (mf.getSku_number() == arr[1]) {
                        materialsData = mf;
                    }
                }
                if (materialsData != null) {
                    double qty = Double.parseDouble(materialsData.getQty()) + Double.parseDouble(arr[4]);
                    materialsData.setQty(String.valueOf(qty));
                } else {
                    materials_foundation.add(new materialsData(arr[1], arr[2], arr[3], arr[4]));
                }
            }
        }

        for (ComponentData cd : ext_framing) {
            System.out.println(cd.getComponents().size());
            for (String[] arr : cd.getComponents()) {
                materialsData materialsData = null;
                for (materialsData mf : materials_extFraming) {
                    if (mf.getSku_number() == arr[1]) {
                        materialsData = mf;
                    }
                }
                if (materialsData != null) {
                    double qty = Double.parseDouble(materialsData.getQty()) + Double.parseDouble(arr[4]);
                    materialsData.setQty(String.valueOf(qty));
                } else {
                    materials_extFraming.add(new materialsData(arr[1], arr[2], arr[3], arr[4]));
                }
            }
        }
    }

    public void exportExcel() {
        //Create blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        //Create a blank sheet
        XSSFSheet spreadsheet = workbook.createSheet("Material Listing");
        //Create row object
        XSSFRow row;


        Map<Integer, Object[]> foundations = new TreeMap<Integer, Object[]>();
        Map<Integer, Object[]> ext_framing = new TreeMap<Integer, Object[]>();
        //This data needs to be written (Object[])
        int foundations_id = 0;
        if (materials_foundation.size() > 0) {
            foundations.put(foundations_id++, new Object[]{"PoleShed Foundation"});
            foundations.put(foundations_id++, new Object[]{"SKU Number", "Description", "Unit", "Quantity"});
            for (materialsData md : materials_foundation) {
                foundations.put(foundations_id++, new Object[]{
                        md.getSku_number(), md.getDescription(), md.getUnit(), md.getQty()
                });
            }
        }

        int ext_framing_id = 0;
        if (materials_extFraming.size() > 0) {
            ext_framing.put(ext_framing_id++, new Object[]{"PoleShed External Framing"});
            ext_framing.put(ext_framing_id++, new Object[]{"SKU Number", "Description", "Unit", "Quantity"});
            for (materialsData md : materials_extFraming) {
                ext_framing.put(ext_framing_id++, new Object[]{
                        md.getSku_number(), md.getDescription(), md.getUnit(), md.getQty()
                });
            }
        }

        int rowid = 0;
        spreadsheet.addMergedRegion(new CellRangeAddress(rowid, rowid, 0, 3));
        Set<Integer> foundation_keys = foundations.keySet();
        for (Integer key : foundation_keys) {
            row = spreadsheet.createRow(rowid++);
            Object[] objectArr = foundations.get(key);
            int cellid = 0;
            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellid++);


                CellStyle style = workbook.createCellStyle();

                if (key != 0) {
                    style.setBorderTop(BorderStyle.THIN);
                    style.setTopBorderColor(IndexedColors.BLACK.getIndex());

                    style.setBorderBottom(BorderStyle.THIN);
                    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());

                    style.setBorderRight(BorderStyle.THIN);
                    style.setRightBorderColor(IndexedColors.BLACK.getIndex());

                    style.setBorderLeft(BorderStyle.THIN);
                    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
                }

                if (key == 0 || key == 1) {
                    Font font = workbook.createFont();
                    font.setBold(true);
                    style.setFont(font);
                }

                cell.setCellStyle(style);
                cell.setCellValue((String) obj);
            }
        }

        row = spreadsheet.createRow(rowid++);//add space for each label
        spreadsheet.addMergedRegion(new CellRangeAddress(rowid, rowid, 0, 3));//merge for titles
        Set<Integer> ext_framing_keys = ext_framing.keySet();
        for (Integer key : ext_framing_keys) {
            row = spreadsheet.createRow(rowid++);
            Object[] objectArr = ext_framing.get(key);
            int cellid = 0;
            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellid++);

                CellStyle style = workbook.createCellStyle();

                if (key != 0) {
                    style.setBorderTop(BorderStyle.THIN);
                    style.setTopBorderColor(IndexedColors.BLACK.getIndex());

                    style.setBorderBottom(BorderStyle.THIN);
                    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());

                    style.setBorderRight(BorderStyle.THIN);
                    style.setRightBorderColor(IndexedColors.BLACK.getIndex());

                    style.setBorderLeft(BorderStyle.THIN);
                    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
                }

                if (key == 0 || key == 1) {
                    Font font = workbook.createFont();
                    font.setBold(true);
                    style.setFont(font);
                }
                cell.setCellStyle(style);
                cell.setCellValue((String) obj);
            }
        }

        spreadsheet.autoSizeColumn(0);
        spreadsheet.autoSizeColumn(1);
        spreadsheet.autoSizeColumn(2);
        spreadsheet.autoSizeColumn(3);

        //Write the workbook in file system
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Material List");

            File file = fileChooser.showSaveDialog(Main.dashboard_stage);

            if (file != null) {
                if (!file.getName().contains(".xlsx")) {
                    file = new File(file.getAbsolutePath() + ".xlsx");
                }
                FileOutputStream out = new FileOutputStream(file);
                workbook.write(out);
                out.close();
                JOptionPane.showMessageDialog(null, "Material Listing exported.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Export error. Worksheet is curently open.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }


}
