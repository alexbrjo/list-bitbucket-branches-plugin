package alexbrjo.list_bitbucket_branches;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadContext;
import hudson.Extension;
import hudson.Plugin;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import jenkins.model.Jenkins;
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
public class ListBitBucketBranchesPlugin extends Plugin implements Describable<ListBitBucketBranchesPlugin> {

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
        String branches = api.getBranches(formData.getString("username"),
                formData.getString("repo"));

        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(branches);

        /*while (false) {
            if (parser.nextToken().toString() == "type") {
                if (parser.nextToken().toString() == "name") {
                    LOG.info(parser.nextValue().toString());
                }
            }
        }*/
    }

    public Descriptor<ListBitBucketBranchesPlugin> getDescriptor() {
        return Jenkins.getActiveInstance().getDescriptorByType(ListBitBucketBranchesPlugin.DescriptorImpl.class);
    }

    @Extension(optional=true)
    public static final class DescriptorImpl extends Descriptor<ListBitBucketBranchesPlugin> {
        public String getDisplayName() {
            return "BitBucket Branches";
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
    }

}
