package info.colinhan.sisyphus.tartarus;

import info.colinhan.sisyphus.tartarus.model.Action;
import info.colinhan.sisyphus.tartarus.model.Flow;
import info.colinhan.sisyphus.tartarus.model.TemplateString;
import info.colinhan.sisyphus.tartarus.utils.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScriptToModelTransformerTest {
    @Test
    public void testActions() {
        ScriptToModelTransformer transformer = new ScriptToModelTransformer();
        Flow flow = (Flow)transformer.visit(TestUtils.parseFile("actions.ss"));
        assertNotNull(flow);
        assertEquals(2, flow.getBlock().getStatements().size());
        assertInstanceOf(Action.class, flow.getBlock().getStatements().get(0));
        Action a1 = (Action) flow.getBlock().getStatements().get(0);
        assertNotNull(a1.getPositionedParameter());
        assertEquals(1, a1.getNamedParameters().size());
        Action a2 = (Action) flow.getBlock().getStatements().get(1);
        assertInstanceOf(TemplateString.class, a2.getNamedParameters().get("method"));
    }
}