import javafx.application.Application;
import javafx.application.Platform;

import javafx.embed.swing.SwingFXUtils;

import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.SnapshotResult;
import javafx.scene.control.SplitPane;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.util.concurrent.*;

public class Main extends Application {

    private final int WIDTH = 1024;
    private final int HEIGHT = 768;

    private ScheduledExecutorService executor;

    private ImageView frames = new ImageView();

    @Override
    public void start(Stage primaryStage){
        // load native opencv lib
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // youtube embedded iframe
        String iFrame = "<iframe width=\"1024\" height=\"357\" src=\"https://www.youtube.com/embed/byUXOWzyiyo?autoplay=1\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>";

        // load iframe into javafx
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.loadContent(iFrame);

        // javafx construction
        Pane pane = new Pane();
        pane.setMaxHeight(HEIGHT);
        pane.setMaxWidth(WIDTH);

        SplitPane splitPane = new SplitPane(webView, frames);
        splitPane.setOrientation(Orientation.VERTICAL);
        splitPane.setPrefHeight(HEIGHT);

        pane.getChildren().add(splitPane);

        Scene scene = new Scene(pane, WIDTH, HEIGHT);
        // end javafx construction

        // callback used to capture frames
        Runnable screenCapture = () ->
                Platform.runLater(() -> scene.snapshot(new ScreenCapture(), new WritableImage(WIDTH, HEIGHT/2)));

        // service used to capture frames
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(screenCapture, 1000, 50, TimeUnit.MILLISECONDS);

        // start javafx
        primaryStage.setTitle("Window Capture");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop(){
        executor.shutdown();
    }

    class ScreenCapture extends SnapshotParameters implements javafx.util.Callback<SnapshotResult, Void> {
        @Override
        public Void call(SnapshotResult param) {
            // converts frame to mat
            Mat mat = Utils.imageToMat(param.getImage());
            // detects cars
            CarController.detectCars(mat);

            // must convert colors on mat since the 'imageToMat' call does some extra stuff...
            // this fixes it
            Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGBA2RGB);

            // compress and resize mat
            MatOfByte buffer = new MatOfByte();
            Imgcodecs.imencode(".png", mat, buffer);

            // set back to image and then saved as a frame on javafx
            BufferedImage img = Utils.matToBufferedImage(mat);
            frames.setImage(SwingFXUtils.toFXImage(img, null));
            return null;
        }
    }
}
