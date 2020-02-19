package Views;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FxConcurrencyExample3  extends Application
{
    // Create the TextArea
    TextArea textArea = new TextArea();

    // Create the Label
    Label statusLabel = new Label("Not Started...");

    // Create the Buttons
    Button startButton = new Button("Start");
    Button exitButton = new Button("Exit");

    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(final Stage stage)
    {
        // Create the Event-Handlers for the Buttons
        startButton.setOnAction(new EventHandler <ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                startTask();
            }
        });

        exitButton.setOnAction(new EventHandler <ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                stage.close();
            }
        });

        // Create the ButtonBox
        HBox buttonBox = new HBox(5, startButton, exitButton);

        // Create the VBox
        VBox root = new VBox(10, statusLabel, buttonBox, textArea);

        // Set the Style-properties of the VBox
        root.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: blue;");

        // Create the Scene
        Scene scene = new Scene(root,400,300);
        // Add the scene to the Stage
        stage.setScene(scene);
        // Set the title of the Stage
        stage.setTitle("A simple Concurrency Example");
        // Display the Stage
        stage.show();
    }

    public void startTask()
    {
        // Create a Runnable
        Runnable task = new Runnable()
        {
            public void run()
            {
                runTask();
            }
        };

        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);
        // Start the thread
        backgroundThread.start();
    }

    public void runTask()
    {
        for(int i = 1; i <= 100; i++)
        {
            try
            {
                // Get the Status
                final String status = "Processing " + i + " of " + 10;

                // Update the Label on the JavaFx Application Thread
                Platform.runLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        statusLabel.setText(status);
                    }
                });

                textArea.appendText(status+"\n");

                Thread.sleep(500);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
//if (isNew) {
//        boolean isSnap = false;
//        if (snapX != -1 && snapY != -1) {
//        line.setStartX(snapX);
//        line.setEndX(snapX);
//        line.setStartY(snapY);
//        line.setEndY(snapY);
//        isSnap = true;
//        } else {
//        line.setStartX(clamp.getX());
//        line.setEndX(clamp.getX());
//        line.setStartY(clamp.getY());
//        line.setEndY(clamp.getY());
//        }
//        switch (mode) {
//        case "SCALE":
//        line.setStroke(Color.CHOCOLATE);
//        line.setStrokeWidth(8 / group.getScaleY());
//        pane.getChildren().add(createBox(line.getEndX(), line.getEndY()));
//        break;
//        case "LENGTH":
//        case "AREA":
//        line.setStroke(color);
//        if (isSnap) {
//        pointList.add(new Point2D(snapX, snapY));
//        } else {
//        pointList.add(clamp);
//        }
//        pane.getChildren().add(createBox(line.getEndX(), line.getEndY()));
//        break;
//        }
//        line.setVisible(true);
//        snapX = snapY = -1;
//        isNew = false;
//        } else {
//        if (snapX != -1 && snapY != -1) {
//        line.setEndX(snapX);
//        line.setEndY(snapY);
//        } else {
//        line.setEndX(clamp.getX());
//        line.setEndY(clamp.getY());
//        }
//        snapX = snapY = -1;
//        Point2D start = new Point2D(line.getStartX(), line.getStartY());
//        Point2D end = new Point2D(line.getEndX(), line.getEndY());
//
//        switch (mode) {
//        case "SCALE":
//        pane.getChildren().add(createBox(line.getEndX(), line.getEndY()));
//        try {
//        double m_input = Float.parseFloat(JOptionPane.showInputDialog("Enter Scale (mm)", 0.00 + " mm"));
//        if (m_input <= 0) {
//        throw new NumberFormatException();
//        }
//        double m_Length = start.distance(end);
//        m_Scale = m_Length / m_input;
//        } catch (Exception e) {
//        JOptionPane.showMessageDialog(null, "Please enter a valid number.", "Invalid Scale", JOptionPane.ERROR_MESSAGE);
//        }
//        redrawShapes();
//        isNew = true;
//        canDraw = false;
//        scroller.setPannable(true);
//        mode = "FREE_HAND";
//        break;
//        case "LENGTH":
//        pointList.add(end);
//        ShapeObject shapeObj = new ShapeObject();
//        shapeObj.setPane(pane);
//        shapeObj.setColor(color);
//        shapeObj.setPointList(pointList);
//        shapeObj.setController(this);
//        shapeObj.setType(mode);
//        shapeObjList.add(shapeObj);
//        isNew = true;
//        line.setVisible(false);
//        canDraw = false;
//        pointList.clear();
//        redrawShapes();
//        break;
//        case "AREA":
//        pointList.add(end);
//        pane.getChildren().add(createLine(line));
//        pane.getChildren().add(createBox(line.getEndX(), line.getEndY()));
//        line.setStartX(line.getEndX());
//        line.setStartY(line.getEndY());
//        line.setEndX(line.getEndX());
//        line.setEndY(line.getEndY());
//        }
//        }