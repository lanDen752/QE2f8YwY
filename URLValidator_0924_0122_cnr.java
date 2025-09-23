// 代码生成时间: 2025-09-24 01:22:53
import org.apache.http.client.utils.URIBuilder;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;

/**
 * A utility class to validate the URL link validity using JAVA and HIBERNATE framework.
 */
public class URLValidator {

    /**
     * Validates if the provided URL is a valid link and accessible.
     *
     * @param urlString the URL to be validated
     * @return true if the URL is valid and accessible, false otherwise.
     */
    public boolean validateURL(String urlString) {
        try {
            // Constructs a URI from the provided string
            URI uri = new URIBuilder(urlString).build();
            URL url = uri.toURL();

            // Opens a connection to the URL
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();

            // If the response code is between 200 and 399, the URL is valid and accessible
            int responseCode = connection.getResponseCode();
            return responseCode >= 200 && responseCode < 400;

        } catch (URISyntaxException e) {
            // If the URL is not well-formed, it's not valid
            System.err.println("Invalid URL format: " + urlString);
            return false;
        } catch (IOException e) {
            // If there is an IO exception, the URL is not accessible
            System.err.println("Error accessing URL: " + urlString);
            return false;
        }
    }

    /**
     * Main method for demonstration purposes.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        URLValidator validator = new URLValidator();
        String testURL = "https://www.example.com";
        boolean isValid = validator.validateURL(testURL);
        System.out.println("Is the URL valid and accessible? " + isValid);
    }
}
