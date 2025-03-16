package utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface Utils {
    /**
     * This method executes command in the specified directory with specified parameters
     * @param path path of the directory
     * @param params parameters
     * @return List of {@link String} objects representing result of executed command
     * @throws IOException
     */
    List<String> executeCommand(String path, String ... params) throws IOException;

    /**
     * This method fetches data from API endpoint and deserializes response into provided
     * class type
     * @param route URL of API endpoint
     * @param type class type to deserialize the response body into
     * @param accessToken token used for authorization
     * @return Instance of <T> representing deserialized response
     * @param <T> type of the object to return after deserialization
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    <T> T fetchData(String route, Class<T> type, String accessToken) throws URISyntaxException, IOException,
            InterruptedException;
}
