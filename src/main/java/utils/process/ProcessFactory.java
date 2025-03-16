package utils.process;

import java.io.IOException;

public interface ProcessFactory {
    /**
     * This method creates new process in the specified directory and execute it with the specified parameters
     * @param path path of the directory
     * @param params parameters
     * @return {@link Process} object representing executed process
     */
    Process executeProcess(String path, String ... params) throws IOException;
}
