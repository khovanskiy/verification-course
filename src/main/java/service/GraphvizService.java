package service;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
@Slf4j
public class GraphvizService {
    private final SystemService systemService;

    public GraphvizService(SystemService systemService) {
        this.systemService = systemService;
    }

    public boolean dot(File input, File output) {
        return systemService.execute("dot -Gsplines=true -Goverlap=false -Tpdf " + input.getAbsolutePath() + " -o " + output.getAbsolutePath());
    }
}
