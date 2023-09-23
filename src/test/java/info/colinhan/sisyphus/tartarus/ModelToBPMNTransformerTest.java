package info.colinhan.sisyphus.tartarus;

import info.colinhan.sisyphus.tartarus.model.Flow;
import info.colinhan.sisyphus.tartarus.utils.TestUtils;
import org.junit.jupiter.api.Test;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

class ModelToBPMNTransformerTest {
    @Test
    public void testExample() {
        Flow flow = TestUtils.readFlow("example.ss");
        StringWriter sb = new StringWriter();
        ModelToBPMNTransformer transformer = new ModelToBPMNTransformer(1, "flow", sb);
        transformer.visit(flow);
        assertEquals("", indent(sb));
    }

    private String indent(StringWriter sw) {
        TransformerFactory factory = TransformerFactory.newInstance();

        Transformer transformer = null;
        try {
            transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            StringWriter formattedStringWriter = new StringWriter();
            transformer.transform(new StreamSource(new StringReader(sw.toString())), new StreamResult(formattedStringWriter));
            return formattedStringWriter.toString();
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }
}