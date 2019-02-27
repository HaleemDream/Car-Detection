# Car Detection

Detects cars on youtube videos.

## How to Use

Find any video containing cars. Get the embedded link and change the src code to use it. 

The training data set was found online. 

![](https://media.giphy.com/media/8F3y2OZeYIZOqJupMl/source.gif)

**Credits to the java opencv tutorial**

[Tutorial](https://opencv-java-tutorials.readthedocs.io/en/latest/01-installing-opencv-for-java.html)

The tutorial should be helpful if you had any questions. I did not use the `VideoCapture` object which is a big part of the OpenCV project. I captured frames my own way and converted them. 

### Notes

- This is a very basic approach

- Project makes use of JavaFX 8

- **Must have opencv jar and native lib (in resource folder, add them as dependencies)**

- It's very easy to switch between different types of object detection.