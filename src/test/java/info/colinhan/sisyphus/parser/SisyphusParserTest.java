package info.colinhan.sisyphus.parser;

import info.colinhan.sisyphus.utils.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SisyphusParserTest {
    @Test
    public void test() {
        TestUtils.parseFile("array.ss");
        TestUtils.parseFile("condition.ss");
        TestUtils.parseFile("simple.ss");
    }

    @Test
    public void testActions() {
        SisyphusParser.DiagramContext diagram = TestUtils.parseFile("actions.ss");
        assertNotNull(diagram.block().statement(0).action());
        SisyphusParser.ActionContext action1 = diagram.block().statement(0).action();
        assertEquals("fillForm", action1.actionDefinition().ActionName().getText());
        assertNotNull(diagram.block().statement(1).action());
        SisyphusParser.ActionContext action2 = diagram.block().statement(1).action();
        assertEquals("callApi", action2.actionDefinition().ActionName().getText());
        assertEquals("http://www.google.com", action2.actionDefinition().positionedParameter().getText().trim());
        assertEquals("method", action2.actionDefinition().namedParameter(0).ParameterName().getText());
        assertEquals("post", action2.actionDefinition().namedParameter(0).literal().getText().trim());
    }
}
