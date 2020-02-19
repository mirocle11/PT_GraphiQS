package Views;


import Controllers.workspaceController;
import Main.Main;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import java.io.File;


/**
 * Created by User on 20/01/2020.
 */
public class OpenFileService extends Service<File[]> {
    workspaceController controller;
    FileChooser openFile = new FileChooser();
    public OpenFileService(workspaceController controller) {
        this.controller = controller;
        setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                controller.SCALE.setDisable(false);
            }
        });
    }

    @Override
    protected Task<File[]> createTask() {
        return new Task<File[]>() {
            @Override
            protected File[] call() throws Exception {
                openFile.showOpenDialog(Main.dashboard_stage);
                return new File[0];
            }
        };
    }
//    public void open(){
//        FileChooser openFile = new FileChooser();
//        openFile.setTitle("Open PDF");
//        File temp = new File("");
//        if (pdfFile != null) {
//            temp = pdfFile;
//        }
//
//        pdfFile = openFile.showOpenDialog(Main.dashboard_stage);
//        if (pdfFile == null) {
//            pdfFile = temp;
//        } else {
//            if (document != null) {
//                document.close();
//            }
//
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
//            setupPageElements();
//            scroller.setOpacity(1.0);
//            frontPane.setVisible(false);
//        }
//    }
}
