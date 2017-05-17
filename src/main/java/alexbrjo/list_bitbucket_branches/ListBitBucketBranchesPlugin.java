package alexbrjo.list_bitbucket_branches;

import hudson.Plugin;
import hudson.model.Descriptor;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Logs a message when the plugin is loaded
 * @author Alex Johnson
 */
public class ListBitBucketBranchesPlugin extends Plugin {

    /** The logger of the LbbB-plugin class */
    private final static Logger LOG = Logger.getLogger(ListBitBucketBranchesPlugin.class.getName());
    /** Static api throttler so the request limit is never exceeded */
    protected static PublicApiThrottle<Object> api;

    /**
     * Logs that the Plugin actually was loaded
     */
    public void start() throws Exception {
        /* Unauthenticated calls are tracked by ip in one hour blocks. Unsure if limited to 1K or
         * 60K calls per hour but 500 should be plenty.
         * see https://confluence.atlassian.com/bitbucket/rate-limits-668173227.html
         */
        load();
        api = new PublicApiThrottle<>(500);
        LOG.info("LIST-BITBUCKET-BRANCHES: successfully loaded");
    }

    @Override
    public void configure(StaplerRequest req, JSONObject formData) throws IOException, ServletException,
            Descriptor.FormException {
        LOG.info("CONFIGURE CALLED FROM LISTBITBUCKETBRANCHES PLUGIN CLASS");
    }

}
