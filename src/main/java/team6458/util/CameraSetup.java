package team6458.util;

import edu.wpi.first.wpilibj.CameraServer;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A utility class to start USB camera capture data.
 */
public final class CameraSetup {

    public static final String DEFAULT_CAMERA_NAME = "cam0";
    public static final int DEFAULT_CAMERA_ID = 0;
    private static final Logger logger = Logger.getLogger(CameraSetup.class.getName());

    /**
     * No instantiation.
     */
    private CameraSetup() {
    }

    /**
     * Sets up the default camera.
     *
     * @see #setupCamera
     * @see #DEFAULT_CAMERA_NAME
     * @see #DEFAULT_CAMERA_ID
     */
    public static synchronized boolean setupDefaultCamera() {
        return setupCamera(DEFAULT_CAMERA_NAME, DEFAULT_CAMERA_ID);
    }

    /**
     * Sets up automatic capture for a camera with a given name and ID.
     *
     * @param name The name for the camera
     * @param id   The integral ID for the camera
     * @return True if successful, false otherwise
     */
    public static synchronized boolean setupCamera(String name, int id) {
        try {
            CameraServer.getInstance().startAutomaticCapture(name, id);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
        }

        logger.log(Level.WARNING, "Failed to start camera capture (" + name + ", " + id + ")");

        return false;
    }

}
