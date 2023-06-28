package info.colinhan.sisyphus.tartarus.utils;

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
            TartarusLexer lexer = new TartarusLexer(CharStreams.fromStream(stream));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            TartarusParser parser = new TartarusParser(tokens);
            parser.removeErrorListeners();
            ErrorListener errors = new ErrorListener();
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
}
