package service;

import lombok.extern.slf4j.Slf4j;
import model.buchi.Automaton;
import model.diagram.Diagram;
import model.graph.Edge;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
@Slf4j
public class GraphvizServiceTest {
    private static final File dataDir = new File("data");
    private static final File tempDiagramDir = new File("temp", "diagram");
    private static final File tempGraphDir = new File("temp", "graph");
    private DiagramService diagramService = new DiagramService();
    private AutomatonService automatonService = new AutomatonService();
    private GraphvizService graphvizService = new GraphvizService();

    @Test
    public void generate() throws IOException {
        File[] xstdFiles = getDataFiles();
        if (xstdFiles != null) {
            for (File xstdFile : xstdFiles) {
                String file = xstdFile.getName().substring(0, xstdFile.getName().length() - 5);
                log.info("File \"{}\" is processing...", xstdFile.getAbsoluteFile());
                Diagram diagram = diagramService.parseDiagram(xstdFile);
                File dotFile = new File(tempDiagramDir, file + ".dot");
                diagramService.convertDiagramToDot(diagram, dotFile);
                File pdfFile = new File(tempDiagramDir, file + ".pdf");
                graphvizService.dot(dotFile, pdfFile);
            }
        }
    }

    private File[] getDataFiles() {
        if (dataDir.mkdirs()) {
            log.info("The data directory \"{}\" is created", dataDir.getAbsoluteFile());
        }
        if (tempDiagramDir.mkdirs()) {
            log.info("The temporary directory \"{}\" is created", tempDiagramDir.getAbsoluteFile());
        }
        if (tempGraphDir.mkdirs()) {
            log.info("The temporary directory \"{}\" is created", tempGraphDir.getAbsoluteFile());
        }
        return dataDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".xstd"));
    }

    @Test
    public void generate2() throws IOException {
        File[] xstdFiles = getDataFiles();
        if (xstdFiles != null) {
            for (File xstdFile : xstdFiles) {
                String file = xstdFile.getName().substring(0, xstdFile.getName().length() - 5);
                log.info("File \"{}\" is processing...", xstdFile.getAbsoluteFile());
                Diagram diagram = diagramService.parseDiagram(xstdFile);
                Automaton<Edge> graph = automatonService.convertToGraph(diagram);
                File dotFile = new File(tempGraphDir, file + ".dot");
                automatonService.saveAsDot(graph, dotFile);
                File pdfFile = new File(tempGraphDir, file + ".pdf");
                graphvizService.dot(dotFile, pdfFile);
            }
        }
    }
}
