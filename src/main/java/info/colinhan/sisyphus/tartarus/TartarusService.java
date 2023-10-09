package info.colinhan.sisyphus.tartarus;

import info.colinhan.sisyphus.context.VariableValidationContext;
import info.colinhan.sisyphus.tartarus.model.Flow;
import info.colinhan.sisyphus.tartarus.parser.ErrorListener;
import info.colinhan.sisyphus.tartarus.parser.TartarusLexer;
import info.colinhan.sisyphus.tartarus.parser.TartarusParser;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.DiagramDescription;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TartarusService {
    public static Flow parseFlow(String code, VariableValidationContext context) {
        ErrorListener errors = new ErrorListener();
        TartarusLexer lexer = new TartarusLexer(CharStreams.fromString(code));
        lexer.removeErrorListeners();
        lexer.addErrorListener(errors);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        TartarusParser parser = new TartarusParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(errors);
        TartarusParser.DiagramContext diagram = parser.diagram();
        if (errors.hasError()) {
            errors.getErrors().forEach(System.err::println);
            throw new RuntimeException("Parse error!");
        }

        ScriptToModelTransformer modelTransformer = new ScriptToModelTransformer(context);
        return (Flow) modelTransformer.visit(diagram);
    }

    public static String generateSVG(String code, VariableValidationContext context) {
        Flow flow = parseFlow(code, context);
        StringBuilder builder = new StringBuilder();
        ModelToPlantUmlTransformer transformer = new ModelToPlantUmlTransformer(builder);
        transformer.visit(flow);

        String plantUml = builder.toString();
        SourceStringReader reader = new SourceStringReader(plantUml);
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            DiagramDescription desc = reader.outputImage(os, new FileFormatOption(FileFormat.SVG));
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new String(os.toByteArray(), StandardCharsets.UTF_8);
    }
}
