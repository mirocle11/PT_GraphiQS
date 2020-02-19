package service;

import Main.Main;
import Model.PageObject;
import Model.ShapeObject;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.graphics.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point2D;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by User on 17/02/2020.
 */
public class FileService {

    static FileChooser chooser = new FileChooser();
    static File pdf, tempPdf;

    public FileService() {

    }

    public static File open() {
        chooser.setTitle("Select File");
        if (pdf != null) {
            tempPdf = pdf;
        }
        tempPdf = chooser.showOpenDialog(Main.dashboard_stage);
        if (tempPdf != null) {
            pdf = tempPdf;
            return pdf;
        } else {
            JOptionPane.showMessageDialog(null, "No File selected", "Error", 0, UIManager.getIcon("OptionPane.errorIcon"));
            return null;
        }

// else {
//
//            if (document != null) {
//                document.close();
//            }
//
//
//            document = new PdfDocument();
//            document.loadFromFile(pdfFile.getAbsolutePath());//load from selectedPdf
//            document.split(pdfDir.getAbsolutePath() + "/pdf_.pdf");
//            document.close();
//            pdfArr = pdfDir.listFiles();
//
//
//            try {
//
//                document = new PdfDocument();
//                document.loadFromFile(pdfArr[0].getAbsolutePath());
//                bufferedImage = document.saveAsImage(0, PdfImageType.Bitmap, 300, 300);
//                file = new File(imgDir.getAbsolutePath() + "/img_0.png");
//                ImageIO.write(bufferedImage, "PNG", file);
//                document.saveToFile(svgDir.getAbsolutePath() + "/svg_0.svg", FileFormat.SVG);
//                document.close();
//            } catch (IOException ex) {
////                System.out.println(ex.getMessage());
//            }
//
//            svgArr = svgDir.listFiles();
//            imgArr = imgDir.listFiles();
//            try {
//                load = new Loader(svgArr[0].getAbsolutePath());
//                load.getPaths();
//                String[] pntsA;
//                double[][] arr;
//                snapList = new ArrayList<>();
//                for (int i = 0; i < load.points.size(); i++) {
//                    try {
//                        if (!load.points.get(i).equals("") && !load.points.get(i).equals(null)) {
//                            pntsA = load.points.get(i).split(" ");
//
//                            double a = Double.parseDouble(pntsA[0]);
//                            double b = Double.parseDouble(pntsA[1]);
//                            arr = new double[1][2];
//                            arr[0][0] = (a / .23999);
//
//                            paneHegiht = pane.getHeight();
//                            arr[0][1] = 223 + (pane.getHeight() * 5) - (b / .23999) - 7.7;
//                            snapList.add(arr);
//                        }
//                    } catch (Exception e) {
////                    System.out.println(e.getMessage());
//                    }
//                }
//
//                pageObjects.add(new PageObject(0));
//                PageObject pageObject = pageObjects.get(0);
//                pageObject.setSnapList(snapList);
//                pageObject.setShapeObjList(shapeObjList);
//                pageObject.setStampList(stampList);
//
//                snapList.clear();
//                snapList.clear();
//                stampList.clear();
//
//            } catch (XMLStreamException e) {
////                System.out.println(e.getMessage());
//            }
//
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
////                    System.out.println("STARTING THREAD ");
//                    for (int i = 1; i < pdfArr.length; i++) {
//                        try {
//                            document = new PdfDocument();
//                            document.loadFromFile(pdfArr[i].getAbsolutePath());
//                            bufferedImage = document.saveAsImage(0, PdfImageType.Bitmap, 300, 300);
//                            file = new File(imgDir.getAbsolutePath() + "/img_" + i + ".png");
//                            ImageIO.write(bufferedImage, "PNG", file);
//                            document.saveToFile(svgDir.getAbsolutePath() + "/svg_" + i + ".svg", FileFormat.SVG);
//                            document.close();
//                        } catch (IOException e) {
////                            System.out.println(e.getMessage());
//                        }
//                    }
//                    imgArr = imgDir.listFiles();
//                    svgArr = svgDir.listFiles();
//                    for (int j = 1; j < svgArr.length; j++) {
//                        try {
//                            load = new Loader(svgArr[j].getAbsolutePath());
////                            System.out.println(svgArr[j].getAbsoluteFile() + " path " + j);
//                            load.getPaths();
//
//                            String[] pntsA;
//                            double[][] arr;
//                            snapList = new ArrayList<>();
//                            for (int k = 0; k < load.points.size(); k++) {
//                                try {
//                                    if (!load.points.get(k).equals("") && !load.points.get(k).equals(null)) {
//                                        pntsA = load.points.get(k).split(" ");
//
//                                        double a = Double.parseDouble(pntsA[0]);
//                                        double b = Double.parseDouble(pntsA[1]);
//                                        arr = new double[1][2];
//                                        arr[0][0] = (a / .23999);
//                                        arr[0][1] = 223 + (paneHegiht * 5) - (b / .23999) - 7.7;
//                                        snapList.add(arr);
//                                    }
//                                } catch (Exception e) {
//
//                                }
//                            }
//
//                            pageObjects.add(new PageObject(j));
//                            PageObject pageObject = pageObjects.get(j);
//                            pageObject.setSnapList(snapList);
//                            pageObject.setShapeObjList(shapeObjList);
//                            pageObject.setStampList(stampList);
//
//                        } catch (XMLStreamException e) {
////                            System.out.println(e.getMessage());
//                        }
//                    }
//                }
//            }).start();
//
//            SCALE.setDisable(false);
//            NEXT_PAGE.setDisable(false);
//            PREVIOUS_PAGE.setDisable(false);
//            SAVE.setDisable(false);
//            ROTATE.setDisable(false);
//            setPageElements();
//            scroller.setOpacity(1.0);
//            frontPane.setVisible(false);
//        }
    }

    public static void save(ArrayList<PageObject> pageObjects) {
        PdfDocument document;

        chooser.setTitle("Save PDF");

        document = new PdfDocument();
        document.loadFromFile(pdf.getAbsolutePath());

        BufferedImage bufferedImage;
        ArrayList<ShapeObject> shapeObjList;

        File file = chooser.showSaveDialog(Main.stage);
        if (file != null) {
            for (int i = 0; i < pageObjects.size(); i++) {

                PageObject pageObject = pageObjects.get(i);
                PdfPageBase page = document.getPages().get(i);
                page.getCanvas().setTransparency(0.5f, 0.5f, PdfBlendMode.Normal);
                PdfGraphicsState state = page.getCanvas().save();
                page.getCanvas().translateTransform(0, 0);

                bufferedImage = SwingFXUtils.fromFXImage(pageObject.getImage(), null);

                double subX = bufferedImage.getWidth() / page.getSize().getWidth();
                double subY = bufferedImage.getHeight() / page.getSize().getHeight();

                shapeObjList = pageObjects.get(i).getShapeList();
                shapeObjList.forEach(shapeObject -> {
                    PdfRGBColor pdfRGBColor = new PdfRGBColor(new java.awt.Color((float) shapeObject.getColor().getRed(), (float) shapeObject.getColor().getGreen(), (float) shapeObject.getColor().getBlue()));
                    PdfFont font = new PdfFont(PdfFontFamily.Helvetica, 8);
                    if (shapeObject.getType().equals("AREA")) {
                        int ndx = 0;
                        PdfBrush brush = new PdfSolidBrush(new PdfRGBColor(pdfRGBColor));
                        java.awt.geom.Point2D[] points = new java.awt.geom.Point2D[shapeObject.getPointList().size()];
                        for (Point2D p2d : shapeObject.getPointList()) {
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
                            double length = (p1.distance(p2) / pageObject.getScale());
                            page.getCanvas().drawString(Math.round(length * 100.0) / 100.0 + " mm", font, pens, (float) mid.getX() / subX, (float) mid.getY() / subY);
                        });
                    }
                    shapeObject.getLineList().forEach(line1 -> {
                        PdfPen pen = new PdfPen(pdfRGBColor, 2);
                        page.getCanvas().drawLine(pen, line1.getStartX() / subX, (line1.getStartY() / subY) - 1.8, line1.getEndX() / subX, (line1.getEndY() / subY) - 1.8);
                    });

                });
                if (!file.getName().contains(".")) {
                    file = new File(file.getAbsolutePath() + ".pdf");
                    document.saveToFile(file.getAbsolutePath());
                }
                page.getCanvas().restore(state);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No File selected");
        }
        document.close();
    }
}
