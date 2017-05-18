package alexbrjo.list_bitbucket_branches.api;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Controls the BitBucket API call, response parsing and Jelly UI generation.
 * @author Alex Johnson
 */
public class BitBucketApiHandler {

    /** The API url */
    private final static String QUERY = "https://api.bitbucket.org/2.0/repositories/{USER}/{REPO}/refs/branches";

    /** Static api throttler so the request limit is never exceeded */
    private PublicApiThrottle<Object> throttle;

    /**
     * Constructs a new BitBucketApiHandler
     */
    public BitBucketApiHandler (){
        throttle = new PublicApiThrottle<Object>(500);
    }

    /**
     * Gets the Branches of a specified git repo
     *
     * @param username the username of the repository's owner
     * @param repo the repository to list the branches of, must be Git
     * @return string for branches from a BitBucket repository
     */
    public List<String> getBranches (String username, String repo) {
        // Call public BitBucket API
        // Parse results using Jackson
        // Generate list of branches and display with jelly

        // TODO : check if user/repo are valid with regex

        if (!throttle.isCallAllowed())
            throw new CallLimitExceededException();

        String response;
        try {
            response = makeRequest(QUERY.replace("{USER}", username).replace("{REPO}", repo));
        } catch (IOException ex) {
            throw new IllegalArgumentException("Request Exception: " + ex.getMessage());
        }

        LinkedList<String> branches = new LinkedList<>(); // the list of branches
        JsonParser p;
        boolean waitForName = false;
        try {
            p = (new JsonFactory()).createParser(response);

            // Iterate through all the JSON tokens
            while (!p.isClosed()) {
                JsonToken field = p.nextToken();
                if (JsonToken.FIELD_NAME.equals(field)) { // if the token is a field name
                    String fieldName = p.getCurrentName();
                    p.nextToken(); // skip next token

                    if ("type".equals(fieldName) && (p.getValueAsString()).contains("branch")) {
                        waitForName = true;
                    } else if (waitForName && "name".equals(fieldName)) {
                        branches.add(p.getValueAsString() + " ");
                        waitForName = false;
                    }
                }
            }

            return branches;
        } catch (IOException ex) {
            throw new IllegalArgumentException("JSON parsing exception: " + ex.getMessage());
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

        // TODO: log more than string
        throttle.logCall(json.toString());

        return json.toString();
    }
}
