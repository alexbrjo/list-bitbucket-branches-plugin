package alexbrjo.list_bitbucket_branches.api;

import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests class PublicApiThrottle
 */
public class PublicApiThrottleTest {

    @Test
    public void testNewPublicApiThrottle () {

        // test valid default constructor
        PublicApiThrottle<String> thr0 = new PublicApiThrottle<>();
        assertTrue(thr0.isCallAllowed());

        // test valid constructor
        PublicApiThrottle<String> thr1 = new PublicApiThrottle<>(49);
        assertTrue(thr1.isCallAllowed());

        // test valid constructor
        PublicApiThrottle<String> thr2 = new PublicApiThrottle<>(49, 123456);
        assertTrue(thr2.isCallAllowed());

        PublicApiThrottle<String> thr3 = null;
        try {
            thr3 = new PublicApiThrottle<>(-1);
        } catch (IllegalArgumentException e) {
            assertNull(thr3);
        }

        PublicApiThrottle<String> thr4 = null;
        try {
            thr4 = new PublicApiThrottle<>(-1);
        } catch (IllegalArgumentException e) {
            assertNull(thr4);
        }
    }

    /**
     * Tests that calls are stopped for small limits (including 1)
     */
    @Test
    public void testMaxThrottleSmallLimit () {
        fail("Not implemented");
    }

    /**
     * Tests that calls are stopped for large limits
     */
    @Test
    public void testMaxThrottleLargeLimit () {
        fail("Not implemented");
    }

    /**
     * Tests that after the given interval more calls are allowed
     */
    @Test
    public void testIntervalRollOver () {
        fail("Not implemented");
    }

}
