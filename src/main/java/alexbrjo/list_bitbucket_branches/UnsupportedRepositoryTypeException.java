package alexbrjo.list_bitbucket_branches;

/**
 * If a user tries to load a repository from an SCM type other than Git
 *
 * @author Alex Johnson
 */
public class UnsupportedRepositoryTypeException extends RuntimeException {

    public UnsupportedRepositoryTypeException () {
        super("The specified repository SCM type is not supported by this Plugin ");
    }

}
