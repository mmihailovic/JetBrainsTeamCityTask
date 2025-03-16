package utils;

import com.google.gson.Gson;
import exceptions.FetchingDataFromAPIException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.process.ProcessFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultUtilsTest {
    private static final String URL = "https://api.github.com/";
    private static final String ACCESS_TOKEN = "123";
    private static final String BODY = "BODY";
    @InjectMocks
    private DefaultUtils utils;

    @Mock
    private Gson gson;

    @Mock
    private HttpClient httpClient;

    @Mock
    private ProcessFactory processFactory;

    @Mock
    private Process process;

    @Mock
    private HttpResponse<String> httpResponse;

    @Test
    void testExecuteGitCommand() throws IOException {
        when(process.getInputStream()).thenReturn(new ByteArrayInputStream("result".getBytes()));
        when(processFactory.executeProcess(anyString(), anyString())).thenReturn(process);

        List<String> result = utils.executeCommand(anyString(), anyString());
        assertEquals(1, result.size());
        verify(process).destroy();
    }

    @Test
    void testExecuteGitCommand_Exception() throws IOException {
        when(processFactory.executeProcess(anyString(), anyString())).thenThrow(IOException.class);

        List<String> result = utils.executeCommand(anyString(), anyString());
        assertEquals(0, result.size());
    }

    @Test
    void testFetchData_FetchingDataFromAPIException() throws IOException, InterruptedException {
        when(httpResponse.statusCode()).thenReturn(400);
        when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(httpResponse);

        assertThrows(FetchingDataFromAPIException.class, () -> utils.fetchData(URL, Class.class, ACCESS_TOKEN));
    }

    @Test
    void testFetchData_Successful() throws IOException, InterruptedException {
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(httpResponse);
        when(httpResponse.body()).thenReturn(BODY);

        assertDoesNotThrow(() -> utils.fetchData(URL, Class.class, ACCESS_TOKEN));
    }

}