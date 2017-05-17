package alexbrjo.list_bitbucket_branches;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Controls the BitBucket API call, response parsing and Jelly UI generation.
 * @author Alex Johnson
 */
public class BitBucketApiHandler {

    /** The API url */
    private final static String QUERY = "https://api.bitbucket.org/2.0/repositories/{USER}/{REPO}/refs/branches";

    /** Static api throttler so the request limit is never exceeded */
    private PublicApiThrottle<Object> throttle;

    public BitBucketApiHandler (){
        throttle = new PublicApiThrottle<Object>(500);
    }

    /**
     * Gets the Branches of a specified git repo
     *
     * @param username the username of the repository's owner
     * @param repo the repository to list the branches of, must be Git
     * @return tmp string
     */
    public String getBranches (String username, String repo) {
        // Call public BitBucket API
        // Parse results using Jackson
        // Generate list of branches and display with jelly

        // TODO : check if user/repo are valid with regex

        if (!throttle.isCallAllowed())
            throw new CallLimitExceededException();

        try {
            return makeRequest(QUERY.replace("{USER}", username).replace("{REPO}", repo));
        } catch (IOException ex) {
            throw new IllegalArgumentException("Request Exception: " + ex.getMessage());
        }
    }

    /**
     * Makes a request for a url
     *
     * @param reqUrl the url to GET
     * @return the content of the response
     * @throws IOException if there is any problem with the protocol or request
     */
    public String makeRequest (String reqUrl) throws IOException {
        StringBuilder json = new StringBuilder();

        URL url = new URL(reqUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line = in.readLine();
        while (line != null) {
            json.append(line);
            line = in.readLine();
        }
        in.close();

        // do this better
        throttle.logCall(json.toString());

        return json.toString();
    }

}
