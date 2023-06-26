package info.colinhan.sisyphus.utils;

import info.colinhan.sisyphus.parser.ErrorListener;
import info.colinhan.sisyphus.parser.SisyphusLexer;
import info.colinhan.sisyphus.parser.SisyphusParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.io.InputStream;

public class TestUtils {
    public static SisyphusParser.DiagramContext parseFile(String filename) {
        InputStream stream = TestUtils.class.getClassLoader().getResourceAsStream("syntax_test/" + filename);
        try {
            SisyphusLexer lexer = new SisyphusLexer(CharStreams.fromStream(stream));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            SisyphusParser parser = new SisyphusParser(tokens);
            parser.removeErrorListeners();
            ErrorListener errors = new ErrorListener();
            parser.addErrorListener(errors);
            SisyphusParser.DiagramContext diagram = parser.diagram();
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
