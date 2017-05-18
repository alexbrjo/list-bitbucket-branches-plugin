package alexbrjo.list_bitbucket_branches;

import hudson.Extension;
import hudson.ExtensionPoint;
import hudson.model.*;
import hudson.util.ComboBoxModel;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.QueryParameter;

import java.util.List;

/**
 * The Display of the BitBucket Branches
 * @author Alex Johnson
 */
@Extension
public class Display implements RootAction, ExtensionPoint, Action, Describable<Display> {

    public String getIconFileName() {
        return "notepad.png";
    }

    public String getDisplayName() {
        return "List BitBucket Branches";
    }

    public String getUrlName() {
        return "list-bitbucket-branches";
    }

    public String getListBranches () {
        String ret = "";
        for (String b : ListBitBucketBranchesPlugin.getAPI().getBranches("atlassian", "connector-eclipse")) {
            ret += b + ", ";
        }
        return ret;
    }

    /**
     * Gets the Branches of a specified git repo
     *
     * @param username the username of the repository's owner
     * @param repo the repository to list the branches of, must be Git
     * @return tmp string
     */
    public List<String> getListBranches (@QueryParameter("username") String username,
                                         @QueryParameter("repo") String repo) {
        return ListBitBucketBranchesPlugin.getAPI().getBranches(username, repo);
    }

    public Descriptor<Display> getDescriptor() {
        return (Descriptor<Display>) Jenkins.getInstance().getDescriptorOrDie(getClass());
    }

    @Extension
    public static final class DescriptorImpl extends Descriptor<Display> {

        public FormValidation doCheckUsername(@QueryParameter String value) {
            return FormValidation.error("checked username");
        }

        public FormValidation doCheckRepo(@QueryParameter String value) {
            return FormValidation.ok("checked repo");
        }

        @Override
        public String getDisplayName() {
            return "LBBB";
        }
    }
}
