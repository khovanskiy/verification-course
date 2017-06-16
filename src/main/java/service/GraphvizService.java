package service;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
@Slf4j
public class GraphvizService {
    private boolean executeCommand(String command) {
        Process process;
        try {
            process = Runtime.getRuntime().exec(command);
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.error(e.getLocalizedMessage(), e);
            }
            return (process.exitValue() == 0);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    public boolean dot(File input, File output) {
        return executeCommand("dot -Gsplines=true -Goverlap=false -Tpdf " + input.getAbsolutePath() + " -o " + output.getAbsolutePath());
    }
}
