package utils;

import com.google.gson.Gson;
import exceptions.FetchingDataFromAPIException;
import lombok.AllArgsConstructor;
import utils.process.ProcessFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@AllArgsConstructor
public class DefaultUtils implements Utils{
    private Gson gson;
    private HttpClient httpClient;
    private ProcessFactory processFactory;

    public DefaultUtils(ProcessFactory processFactory) {
        this.processFactory = processFactory;
        this.gson = new Gson();
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    public List<String> executeCommand(String path, String ... params) throws IOException {
        BufferedReader reader = null;
        Process process = null;
        try {
            process = processFactory.executeProcess(path, params);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            List<String> result = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }

            return result;
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, "Error executing git command");
            return new ArrayList<>();
        } finally {
            if(process != null) {
                process.destroy();
            }
            if(reader != null) {
                reader.close();
            }
        }
    }

    @Override
    public <T> T fetchData(String route, Class<T> type, String accessToken) throws URISyntaxException, IOException,
            InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(route))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() != 200) {
            throw new FetchingDataFromAPIException();
        }

        return gson.fromJson(response.body(), type);
    }
}
