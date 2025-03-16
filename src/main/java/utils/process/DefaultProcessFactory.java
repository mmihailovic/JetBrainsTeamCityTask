package utils.process;

import java.io.File;
import java.io.IOException;

public class DefaultProcessFactory implements ProcessFactory{
    @Override
    public Process executeProcess(String path, String... params) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(params);
        processBuilder.directory(new File(path));
        return processBuilder.start();
    }
}
