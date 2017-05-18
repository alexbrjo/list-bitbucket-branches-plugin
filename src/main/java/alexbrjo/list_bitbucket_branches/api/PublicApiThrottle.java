package alexbrjo.list_bitbucket_branches.api;

import java.util.LinkedList;
import java.util.List;

/**
 * Prevents calls to a public API avoid being black-listed.
 *
 * @author Alex Johnson
 */
public class PublicApiThrottle<E> {
    /** Default limit of calls/hour */
    private static final int MAX_CALLS_PER_INTERVAL = 50;
    /** Default interval to track calls (1 hour) */
    private static final int INTERVAL_MILLIS = 3600000;

    /** The list of calls */
    private List<ApiCall> calls;
    /** The maximum number of calls per interval */
    private int callLimit;
    /** The interval to track calls */
    private int interval;

    /**
     * Constructions an Api Throttler with default max calls (50 calls) and interval (1 hour).
     */
    public PublicApiThrottle () {
        this(MAX_CALLS_PER_INTERVAL, INTERVAL_MILLIS);
    }

    /**
     * Constructions an Api Throttler for a given maximum number of calls for the default interval (1 hour).
     *
     * @param callLimit the maximum allowed number of calls per hour. Must be positive integer
     */
    public PublicApiThrottle (int callLimit) {
        this(callLimit, INTERVAL_MILLIS);
    }

    /**
     * Constructions an Api Throttler for a given number of calls and interval in millis.
     *
     * @param callLimit the maximum allowed number of calls per hour. Must be positive integer
     * @param interval the interval to track ApiCalls in
     */
    public PublicApiThrottle (int callLimit, int interval) {
        if (callLimit < 0) {
            throw new IllegalArgumentException("Call limit must be a positive integer");
        }

        if (interval < 0 ) {
            throw new IllegalArgumentException("Interval must be a positive integer");
        }


        calls = new LinkedList<ApiCall>();
        this.callLimit = callLimit;
        this.interval = interval;
    }

    /**
     * Determines if a call is allowed.
     * @return if a call is allowed
     */
    public boolean isCallAllowed(){
        prune();
        return calls.size() < callLimit;
    }

    /**
     * Adds an API call to the list
     * @param call the object that is being used to store the API response
     */
    public void logCall(E call){
        prune();
        calls.add(new ApiCall(call));
    }

    /**
     * Removes calls outside of interval
     */
    private void prune () {
        while (!calls.isEmpty() && calls.get(0).time + interval < System.currentTimeMillis())
            calls.remove(0);
    }

    /**
     * A single call to an Api
     */
    private class ApiCall {
        /** The approx. time that the request was sent */
        Long time;
        /** The repsonse of the */
        E response;

        /**
         * Creates a new ApiCall object for a reponse
         * @param res the response from the public API
         */
        public ApiCall (E res) {
            time = System.currentTimeMillis();
            response = res;
        }
    }
}
