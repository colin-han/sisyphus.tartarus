package info.colinhan.sisyphus.tartarus;

import info.colinhan.sisyphus.tartarus.model.Flow;
import info.colinhan.sisyphus.tartarus.utils.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelToPlantUmlTransformerTest {
    @Test
    public void testExample() {
        Flow flow = TestUtils.readFlow("example.ss");
        StringBuilder sb = new StringBuilder();
        ModelToPlantUmlTransformer transformer = new ModelToPlantUmlTransformer(sb);
        transformer.visit(flow);
        assertEquals("", sb.toString());
    }

    @Test
    public void testActions() {
        Flow flow = TestUtils.readFlow("actions.ss");
        StringBuilder sb = new StringBuilder();
        ModelToPlantUmlTransformer transformer = new ModelToPlantUmlTransformer(sb);
        transformer.visit(flow);
        assertEquals("", sb.toString());
    }

    @Test
    public void testIfStatements() {
        Flow flow = TestUtils.readFlow("condition.ss");
        StringBuilder sb = new StringBuilder();
        ModelToPlantUmlTransformer transformer = new ModelToPlantUmlTransformer(sb);
        transformer.visit(flow);
        assertEquals("", sb.toString());
    }

}