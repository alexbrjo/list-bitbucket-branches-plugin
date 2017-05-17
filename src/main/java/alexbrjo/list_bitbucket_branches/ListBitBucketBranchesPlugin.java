package alexbrjo.list_bitbucket_branches;

import hudson.Plugin;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.QueryParameter;
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
    /** Handles all the API requests */
    private BitBucketApiHandler api;

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
     * Gets the Branches of a specified git repo
     *
     * @param username the username of the repository's owner
     * @param repo the repository to list the branches of, must be Git
     * @return tmp string
     */
    public FormValidation doListBranches (@QueryParameter("username") String username,
                                          @QueryParameter("repo") String repo) {
        return FormValidation.ok(api.getBranches(username, repo));
    }

    /**
     * Applies the configuration. Does nothing for now.
     * @param req the stapler request
     * @param formData the data from the configuration form
     * @throws IOException
     * @throws ServletException
     * @throws Descriptor.FormException
     */
    @Override
    public void configure(StaplerRequest req, JSONObject formData) throws IOException, ServletException,
            Descriptor.FormException {
        LOG.info("LIST-BITBUCKET-BRANCHES: configuration applied, did nothing");
        LOG.info(api.getBranches(formData.getString("username"),
                formData.getString("repo")));
    }
}
