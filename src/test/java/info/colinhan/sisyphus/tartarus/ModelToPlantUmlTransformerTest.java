package info.colinhan.sisyphus.tartarus;

import info.colinhan.sisyphus.tartarus.model.Flow;
import info.colinhan.sisyphus.tartarus.utils.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelToPlantUmlTransformerTest {
    @Test
    public void testExample() {
        Flow flow = readFlow("example.ss");
        StringBuilder sb = new StringBuilder();
        ModelToPlantUmlTransformer transformer = new ModelToPlantUmlTransformer(sb);
        transformer.visit(flow);
        assertEquals("", sb.toString());
    }

    @Test
    public void testActions() {
        Flow flow = readFlow("actions.ss");
        StringBuilder sb = new StringBuilder();
        ModelToPlantUmlTransformer transformer = new ModelToPlantUmlTransformer(sb);
        transformer.visit(flow);
        assertEquals("", sb.toString());
    }

    @Test
    public void testIfStatements() {
        Flow flow = readFlow("condition.ss");
        StringBuilder sb = new StringBuilder();
        ModelToPlantUmlTransformer transformer = new ModelToPlantUmlTransformer(sb);
        transformer.visit(flow);
        assertEquals("", sb.toString());
    }

    private static Flow readFlow(String filename) {
        ScriptToModelTransformer transformer = new ScriptToModelTransformer();
        return (Flow)transformer.visit(TestUtils.parseFile(filename));
    }
}