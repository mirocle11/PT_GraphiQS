package service;

import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.graphics.PdfImageType;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by User on 20/01/2020.
 */
public class OpenFileService extends Service<File> {
    PdfDocument document;
    File[] pdfArr;
    BufferedImage bufferedImage;
    File file;
    public OpenFileService(File pdfFile) {
        setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {

            }
        });

    }


    @Override
    protected Task<File> createTask() {
        return new Task<File>() {
            @Override
            protected File call() throws Exception {
                document = new PdfDocument();
                document.loadFromFile(pdfArr[0].getAbsolutePath());
                bufferedImage = document.saveAsImage(0, PdfImageType.Bitmap, 300, 300);
//                file = new File(imgDir.getAbsolutePath() + "/img_0.png");
//                ImageIO.write(bufferedImage, "PNG", file);
//                document.saveToFile(svgDir.getAbsolutePath() + "/svg_0.svg", FileFormat.SVG);
//                document.close();
                return null;

            }
        };
    }
}
