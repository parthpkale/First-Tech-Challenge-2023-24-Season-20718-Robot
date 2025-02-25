package org.firstinspires.ftc.teamcode.AutoCore;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

/** Manages AprilTag detection. 
 * Based off of ConceptAprilTag.java from external samples. */
public class AprilTagCore {
    private AprilTagProcessor aprilTag;

    /** Adds the TensorFlow build method to VisionPortalCore.
     * Initialize this before running "visionPortalCoreName".build()
     * @param builder Use your VisionPortalCore variable. Input "visionPortalCoreName".builder. */
    public AprilTagCore(HardwareMap hardwareMap, VisionPortal.Builder builder, int decimation) {
        // Create the AprilTag processor.
        aprilTag = new AprilTagProcessor.Builder()

                // The following default settings are available to un-comment and edit as needed.
                //.setDrawAxes(false)
                //.setDrawCubeProjection(false)
                //.setDrawTagOutline(true)
                //.setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                //.setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
                //.setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)

                // == CAMERA CALIBRATION ==
                // If you do not manually specify calibration parameters, the SDK will attempt
                // to load a predefined calibration for your camera.
                //.setLensIntrinsics(578.272, 578.272, 402.145, 221.506)
                // ... these parameters are fx, fy, cx, cy.

                .build();

        // Adjust Image Decimation to trade-off detection-range for detection-rate.
        // eg: Some typical detection data using a Logitech C920 WebCam
        // Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        // Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        // Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second (default)
        // Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second (default)
        // Note: Decimation can be changed on-the-fly to adapt during a match.
        aprilTag.setDecimation(decimation);

        // Choose a camera resolution. Not all cameras support all resolutions.
        //builder.setCameraResolution(new Size(640, 480));

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        //builder.enableLiveView(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        //builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(aprilTag);

        // Disable or re-enable the aprilTag processor at any time.
        //visionPortal.setProcessorEnabled(aprilTag, true);

    }

    /** Returns a list of all currently detected AprilTags.
     * See .telemetry() to see what information can be found from an AprilTag. */
    public List<AprilTagDetection> getDetections() {
        return aprilTag.getDetections();
    }

    public void telemetry(Telemetry telemetry) {
        telemetry.addData("\nCURRENT CLASS:", "AprilTagCore.java");
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        telemetry.addData("# AprilTags Detected", currentDetections.size());

        // Step through the list of detections and display info for each one.
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                telemetry.addData("\n====", "(ID %d) %s", detection.id, detection.metadata.name);
                telemetry.addData("XYZ", "%6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z);
                telemetry.addData("PRY", "%6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw);
                telemetry.addData("RBE", "%6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation);
            } else {
                telemetry.addData("\n====", "(ID %d) Unknown", detection.id);
                telemetry.addData("Center", "%6.0f %6.0f    (pixels)", detection.center.x, detection.center.y);
            }
        }   // end for() loop

        // Add "key" information to telemetry
        telemetry.addData("", "\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
        telemetry.addData("", "PRY = Pitch, Roll & Yaw (XYZ Rotation)");
        telemetry.addData("", "RBE = Range, Bearing & Elevation");
    }
}
