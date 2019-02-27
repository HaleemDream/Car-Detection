import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

public class CarController {

    private static final CascadeClassifier carCascade = new CascadeClassifier("./src/resources/haarcascade_cars.xml");
    private static int carSize = 0;

    private CarController(){}

    // detects cars from frame and draw rect on frame
    static void detectCars(Mat image){
        MatOfRect cars = new MatOfRect();
        Mat grayFrame = new Mat();

        Imgproc.cvtColor(image, grayFrame, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(grayFrame, grayFrame);

        if(carSize == 0){
            int height = grayFrame.rows();
            if (Math.round(height * 0.10f) > 0) {
                carSize = Math.round(height * 0.10f);
            }
        }

        carCascade.detectMultiScale(grayFrame, cars, 1.1, 3, Objdetect.CASCADE_SCALE_IMAGE,
                new Size(carSize, carSize), new Size());

        for (Rect aFacesArray : cars.toArray())
            Imgproc.rectangle(image, aFacesArray.tl(), aFacesArray.br(), new Scalar(0, 0, 255), 3);
    }
}
