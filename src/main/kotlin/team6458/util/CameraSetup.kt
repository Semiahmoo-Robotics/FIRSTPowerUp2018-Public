package team6458.util

import edu.wpi.first.wpilibj.CameraServer
import team6458.util.CameraSetup.setupDefaultCamera
import java.util.logging.Level
import java.util.logging.Logger


/**
 * Sets up cameras for the Driver Station. Use [setupDefaultCamera] for a camera with name `cam0` and ID `0`.
 */
object CameraSetup {

    private val logger = Logger.getLogger(this::class.java.name)

    private const val DEFAULT_CAMERA_NAME: String = "cam0"
    private const val DEFAULT_CAMERA_ID: Int = 0

    /**
     * Sets up a camera with the name [DEFAULT_CAMERA_NAME] and ID [DEFAULT_CAMERA_ID].
     *
     * @see setupCamera
     */
    fun setupDefaultCamera() = setupCamera(DEFAULT_CAMERA_NAME, DEFAULT_CAMERA_ID)

    /**
     * Sets up a camera with the [name] and [id].
     *
     * @return True if successful, false otherwise
     */
    fun setupCamera(name: String, id: Int): Boolean {
        try {
            CameraServer.getInstance()?.startAutomaticCapture(name, id) ?: error("CameraServer instance is null")
            return true
        } catch (t: Throwable) {
            t.printStackTrace()
        }

        logger.log(Level.WARNING, "Failed to initialize camera set-up!")

        return false
    }

}
