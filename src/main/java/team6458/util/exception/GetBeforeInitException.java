package team6458.util.exception;

/**
 * A type of RuntimeException that outputs one message.
 * @see #GetBeforeInitException(String)
 */
public class GetBeforeInitException extends RuntimeException {

    /**
     * Creates an exception with the message "Attempt to get $system instance before initialization!"
     * @param system The name of the system that was attempted to be gotten.
     */
    public GetBeforeInitException(String system) {
        super("Attempt to get " + system + " instance before initialization!");
    }
}
