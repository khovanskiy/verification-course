package model;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import service.DiagramService;
import service.GraphvizService;

import java.io.File;
import java.io.IOException;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
@Slf4j
public class GraphvizTest {
    private DiagramService diagramService = new DiagramService();
    private GraphvizService graphvizService = new GraphvizService();

    private static final File dataDir = new File("data");
    private static final File tempDir = new File("temp");

    @Test
    public void generate() throws IOException {
        if (dataDir.mkdirs()) {
            log.info("The data directory \"{}\" is created", dataDir.getAbsoluteFile());
        }
        if (tempDir.mkdirs()) {
            log.info("The temporary directory \"{}\" is created", tempDir.getAbsoluteFile());
        }
        File[] xstdFiles = dataDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".xstd"));
        if (xstdFiles != null) {
            for (File xstdFile : xstdFiles) {
                String file = xstdFile.getName().substring(0, xstdFile.getName().length() - 4);
                log.info("File \"{}\" is processing...", xstdFile.getAbsoluteFile());
                Diagram diagram = diagramService.parseDiagram(xstdFile);
                File dotFile = new File(tempDir, file + ".dot");
                diagramService.convertDiagramToDot(diagram, dotFile);
                File pdfFile = new File(tempDir, file + ".pdf");
                graphvizService.dot(dotFile, pdfFile);
            }
        }
    }
}
