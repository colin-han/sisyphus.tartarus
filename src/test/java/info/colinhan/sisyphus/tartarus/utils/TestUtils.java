package info.colinhan.sisyphus.tartarus.utils;

import info.colinhan.sisyphus.tartarus.ScriptToModelTransformer;
import info.colinhan.sisyphus.tartarus.model.Flow;
import info.colinhan.sisyphus.tartarus.parser.ErrorListener;
import info.colinhan.sisyphus.tartarus.parser.TartarusLexer;
import info.colinhan.sisyphus.tartarus.parser.TartarusParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.io.InputStream;

public class TestUtils {
    public static TartarusParser.DiagramContext parseFile(String filename) {
        InputStream stream = TestUtils.class.getClassLoader().getResourceAsStream("syntax_test/" + filename);
        try {
            ErrorListener errors = new ErrorListener();
            TartarusLexer lexer = new TartarusLexer(CharStreams.fromStream(stream));
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
            return diagram;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Flow readFlow(String filename) {
        ScriptToModelTransformer transformer = new ScriptToModelTransformer(new MockModelParseContext());
        return (Flow)transformer.visit(parseFile(filename));
    }
}
