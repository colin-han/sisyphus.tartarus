package info.colinhan.sisyphus.tartarus;

import java.io.FileWriter;

public class IndentingWriter {
    private int indentLevel = 0;
    private final String indentString = "  ";

    private interface Writer {
        void write(String s);
    }
    private static class StringBuilderWriter implements Writer {
        private final StringBuilder builder;

        public StringBuilderWriter(StringBuilder builder) {
            this.builder = builder;
        }

        @Override
        public void write(String s) {
            builder.append(s);
        }
    }

    private static class StringBufferWriter implements Writer {
        private final StringBuffer buffer;

        public StringBufferWriter(StringBuffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public void write(String s) {
            buffer.append(s);
        }
    }

    private static class FileWriter implements Writer {
        private final java.io.FileWriter writer;

        public FileWriter(java.io.FileWriter writer) {
            this.writer = writer;
        }

        @Override
        public void write(String s) {
            try {
                writer.write(s);
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private final Writer writer;

    public IndentingWriter(StringBuilder stringBuilder) {
        this.writer = new StringBuilderWriter(stringBuilder);
    }
    public IndentingWriter(StringBuffer stringBuffer) {
        this.writer = new StringBufferWriter(stringBuffer);
    }
    public IndentingWriter(java.io.FileWriter writer) {
        this.writer = new FileWriter(writer);
    }

    public void indent() {
        indentLevel++;
    }

    public void unindent() {
        indentLevel--;
    }

    public void bookmark() {
        writeLine("===");
    }

    public void write(String s) {
        writer.write(s);
    }

    public void writeLine(String format, Object... params) {
        writeLine(String.format(format, params));
    }
    public void writeLine(String s) {
        writeIndent();
        write(s);
        write("\n");
    }

    public void writeIndent() {
        for (int i = 0; i < indentLevel; i++) {
            write(indentString);
        }
    }
}
