package alexbrjo.list_bitbucket_branches;

import alexbrjo.list_bitbucket_branches.api.BitBucketApiHandler;
import hudson.Plugin;

import java.util.logging.Logger;

/**
 * Logs a message when the plugin is loaded
 * @author Alex Johnson
 */
public class ListBitBucketBranchesPlugin extends Plugin {

    /** The logger of the LbbB-plugin class */
    private final static Logger LOG = Logger.getLogger(ListBitBucketBranchesPlugin.class.getName());
    /** Handles all the API requests */
    private static BitBucketApiHandler api;

    /**
     * Logs that the Plugin actually was loaded
     */
    public void start() throws Exception {
        /* Unauthenticated calls are tracked by ip in one hour blocks. Unsure if limited to 1K or
         * 60K calls per hour but 500 should be plenty.
         * see https://confluence.atlassian.com/bitbucket/rate-limits-668173227.html
         */
        api = new BitBucketApiHandler();
        load();
        LOG.info("LIST-BITBUCKET-BRANCHES: successfully loaded");
    }

    /**
     * Gets the api handler
     *
     * @return the api handler for the plugin
     */
    public static BitBucketApiHandler getAPI () {
        if (api == null) {
            throw new RuntimeException("Plugin never started, but being used");
        }
        return api;
    }
}
