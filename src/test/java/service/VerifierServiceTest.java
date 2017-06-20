package service;

import lombok.extern.slf4j.Slf4j;
import model.buchi.Automaton;
import model.diagram.Diagram;
import model.ltl.Formula;
import model.ltl.LTL;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
@Slf4j
public class VerifierServiceTest {
    private static final File dataDir = new File("data");
    private static final File tempLogs = new File("temp", "logs");

    private AutomatonService automatonService;
    private LtlService ltlService;
    private DiagramService diagramService;
    private VerifierService verifierService;

    @Before
    public void setUp() {
        ltlService = new LtlService();
        SystemService systemService = new SystemService();
        automatonService = new AutomatonService(systemService);
        diagramService = new DiagramService();
        verifierService = new VerifierService(automatonService);
    }

    @Test
    public void verify() throws IOException {
        if (tempLogs.mkdirs()) {
            log.info("The temporary directory \"{}\" is created", tempLogs.getAbsoluteFile());
        }
        for(String model : getModelNames()) {
            log.info("Testing model " + model);
            File modelFile = resolve(model, ".xstd");
            Diagram diagram = diagramService.parseDiagram(modelFile);
            Automaton<Formula<String>> automaton = automatonService.createFromDiagram(diagram);
            File correct = resolve(model, ".ltl.correct");
            File incorrect = resolve(model, ".ltl.incorrect");
            check(automaton, correct, true);
            check(automaton, incorrect, false);
        }
    }

    private void check(Automaton<Formula<String>> a, File ltl, boolean iscorrect) {
        try(Scanner scanner = new Scanner(ltl)){
            while (scanner.hasNextLine()){
                String formula = scanner.nextLine();
                log.info("Checking formula: " + formula);
                Formula<String> f = ltlService.parse(formula);
                Iterable<Formula<String>> ex = verifierService.verify(a, f);
                if(iscorrect){
                    Assert.assertNull(ex);
                } else {
                    Assert.assertNotNull(ex);
                }
            }
        } catch (FileNotFoundException ignored) {}
    }

    private List<String> getModelNames() {
        File[] files = dataDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".xstd"));
        return Arrays.stream(files).map(x -> x.getName().replace(".xstd", "")).collect(Collectors.toList());
    }

    private File resolve(String model, String extension){
        return dataDir.toPath().resolve(model + extension).toFile();
    }

    @Test
    public void verifyExample() throws IOException {
        Formula<String> ltl = LTL.future(LTL.var("S(Dot)"));
        Diagram diagram = diagramService.parseDiagram(new File("data/VarParser.xstd"));
        Automaton<Formula<String>> automaton = automatonService.createFromDiagram(diagram);
        Iterable<Formula<String>> iterable = verifierService.verify(automaton, ltl);
        if (iterable != null) {
            for (Formula<String> f : iterable) {
                log.info(f.toString());
            }
        }
    }
}
