package alexbrjo.list_bitbucket_branches;

import hudson.Extension;
import hudson.model.RootAction;
import jenkins.model.ModelObjectWithContextMenu;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 * The Display of the BitBucket Branches
 * @author Alex Johnson
 */
@Extension
public class Display implements RootAction {
    public String getIconFileName() {
        return "gear.png";
    }

    public String getDisplayName() {
        return "List BitBucket Branches";
    }

    public String getUrlName() {
        return "list-bitbucket-branches";
    }
}
