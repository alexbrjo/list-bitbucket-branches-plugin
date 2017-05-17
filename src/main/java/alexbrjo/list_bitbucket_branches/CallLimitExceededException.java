package alexbrjo.list_bitbucket_branches;

/**
 * Thrown when the Public Api call limit is exceeded
 *
 * @author Alex Johnson
 */
public class CallLimitExceededException extends RuntimeException {

    /**
     * Creates a CallLimitExceededException with default message
     */
    public CallLimitExceededException () {
        super("The API call limit has been exceeded");
    }

}
