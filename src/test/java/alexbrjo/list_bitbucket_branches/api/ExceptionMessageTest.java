package alexbrjo.list_bitbucket_branches.api;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The exceptions don't do anything special
 */
public class ExceptionMessageTest {

    @Test
    public void testCallLimitExceededExcpetionMessage() {
        try {
            throw new CallLimitExceededException();
        } catch (Exception e) {
            assertTrue(e instanceof CallLimitExceededException);
            assertEquals("The API call limit has been exceeded", e.getMessage());
        }
    }

    @Test
    public void testUnsupportedRepositoryTypeMessage() {
        try {
            throw new UnsupportedRepositoryTypeException();
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedRepositoryTypeException);
            assertEquals("The specified repository SCM type is not supported by this Plugin", e.getMessage());
        }
    }
}
