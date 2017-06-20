import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import lombok.extern.slf4j.Slf4j;
import model.buchi.Automaton;
import model.diagram.Diagram;
import model.ltl.Formula;
import service.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static java.util.Arrays.asList;

/**
 * @author Victor Khovanskiy
 * @since 1.0.0
 */
@Slf4j
public class MainApplication {
    public static final String ENCODING = "UTF8";
    private static File modelFile;
    private static Scanner ltlScanner;

    public static void main(String[] args) {
        try {
            parseArgs(args);
            DiagramService diagramService = new DiagramService();
            SystemService systemService = new SystemService();
            AutomatonService automatonService = new AutomatonService(systemService);
            LtlService ltlService = new LtlService();
            VerifierService verifierService = new VerifierService(automatonService);
            Diagram diagram = diagramService.parseDiagram(modelFile);
            Automaton<Formula<String>> automaton = automatonService.createFromDiagram(diagram);
            while(ltlScanner.hasNextLine()){
                String formula = ltlScanner.nextLine();
                System.out.println("Considering the formula " + formula);
                Formula<String> ltl = ltlService.parse(formula);
                if(ltl == null){
                    System.err.println("Errors occurred while parsing ltl formula");
                } else {
                    Iterable<Formula<String>> counterexample = verifierService.verify(automaton, ltl);
                    if (counterexample != null) {
                        System.out.println("Counterexample for the formula: ");
                        System.out.println(verifierService.exampleToString(counterexample));
                    } else {
                        System.out.println("The ltl formula is correct for the automaton.");
                    }
                }
                System.out.println();
            }
        } catch (IOException e) {
            // Option parser exception. Scanner never throws IOException
            System.exit(1);
        }
    }

    /**
     * CLI args parser. It requires model file and one of ltl formula sources to be specified.
     *
     * @throws IOException if it can't print help message
     */
    private static void parseArgs(String[] args) throws IOException {
        OptionParser optionParser = new OptionParser();
        try {
            optionParser.acceptsAll(asList("h", "help"), "Print a short help message");
            optionParser.acceptsAll(asList("ltl", "file"));
            OptionSpec<String> model = optionParser.acceptsAll(asList("m", "model"),
                    "Filename of a model file(Harel automaton in XSTD format)")
                    .requiredUnless("h").withRequiredArg().ofType(String.class);
            optionParser.acceptsAll(asList("i", "interactive"),
                    "Interactive mode(type one LTL formula at line)").availableUnless("ltl", "file");
            OptionSpec<String> formula = optionParser.acceptsAll(asList("l", "ltl"),
                    "LTL formula as command line argument")
                    .availableUnless("i", "file").withRequiredArg().ofType(String.class);
            OptionSpec<String> ltlFile = optionParser.acceptsAll(asList("f", "file"),
                    "File with LTL formulae to check(one at line)")
                    .availableUnless("i", "ltl").withRequiredArg().ofType(String.class);

            OptionSet options = optionParser.parse(args);
            if(options.has("h")){
                optionParser.printHelpOn(System.out);
                System.exit(0);
            }
            if (!(options.has("i") || options.has("f") || options.has("l"))) {
                System.err.println("You should specify at least one LTL formula. See -h for help");
                System.exit(1);
            }
            modelFile = new File(options.valueOf(model));
            if (options.has("interactive")) {
                ltlScanner = new Scanner(System.in);
            }
            if (options.has(formula)) {
                ltlScanner = new Scanner(options.valueOf(formula));
            }
            if (options.has(ltlFile)) {
                try {
                    ltlScanner = new Scanner(new File(options.valueOf(ltlFile)));
                } catch (FileNotFoundException e) {
                    System.err.println("File " + options.valueOf(ltlFile) + " not found.");
                }
            }
        } catch (OptionException e) {
            System.err.println(e.getMessage() + "\n");
            optionParser.printHelpOn(System.err);
            System.exit(1);
        }
    }
}
