package info.colinhan.sisyphus.tartarus;

import info.colinhan.sisyphus.tartarus.model.*;
import info.colinhan.sisyphus.tartarus.utils.MockModelParseContext;
import info.colinhan.sisyphus.tartarus.utils.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScriptToModelTransformerTest {
    @Test
    public void testActions() {
        ScriptToModelTransformer transformer = new ScriptToModelTransformer(new MockModelParseContext());
        Flow flow = (Flow)transformer.visit(TestUtils.parseFile("actions.ss"));
        assertNotNull(flow);
        assertEquals(2, flow.getBlock().getStatements().size());
        assertInstanceOf(Action.class, flow.getBlock().getStatements().get(0));
        Action a1 = (Action) flow.getBlock().getStatements().get(0);
        assertNotNull(a1.getPositionedParameter());
        assertInstanceOf(Reference.class, a1.getParameter("by"));
        assertEquals(2, a1.getParameters().size());
        Action a2 = (Action) flow.getBlock().getStatements().get(1);
        assertInstanceOf(Literal.class, a2.getParameters().get("method"));
    }

    @Test
    public void testParallel() {
        ScriptToModelTransformer transformer = new ScriptToModelTransformer(new MockModelParseContext());
        Flow flow = (Flow)transformer.visit(TestUtils.parseFile("parallel.ss"));
        assertNotNull(flow);
        assertEquals(1, flow.getBlock().getStatements().size());
        assertInstanceOf(WhileStatement.class, flow.getBlock().getStatements().get(0));
        WhileStatement whileStatement = (WhileStatement) flow.getBlock().getStatements().get(0);
        assertTrue(whileStatement.isParallel());
    }
}