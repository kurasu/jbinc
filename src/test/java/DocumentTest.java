import binc.Document;
import binc.Repository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

class DocumentTest {

    @Test
    void readFile() throws IOException {
        final var in = new DataInputStream(new FileInputStream("test-file.binc"));
        final var repository = Repository.read(in);
        Assertions.assertNotNull(repository);
        final var document = new Document(repository);
        Assertions.assertNotNull(document);
        Assertions.assertEquals(2, document.root().childCount());
        Assertions.assertEquals(0, document.root().child(0).childCount());
        Assertions.assertEquals(1, document.root().child(1).childCount());
    }

    @Test
    void create() {
        final var document = new Document();
        final var n1 = document.root().addChild();
        final var n2 = document.root().addChild();
        final var n3 = n2.addChild();

        Assertions.assertEquals(2, document.root().childCount());
        Assertions.assertEquals(0, document.root().child(0).childCount());
        Assertions.assertEquals(1, document.root().child(1).childCount());
    }

    @Test
    void write() throws IOException {
        final var document = new Document();
        final var n1 = document.root().addChild();
        final var n2 = document.root().addChild();
        final var n3 = n2.addChild();

        final var out = new ByteArrayOutputStream();
        document.write(new DataOutputStream(out));

        final var input = new DataInputStream(new ByteArrayInputStream(out.toByteArray()));
        final var repository = Repository.read(input);
        Assertions.assertNotNull(repository);
        Assertions.assertEquals(document.getRepository().getChanges().size(), repository.getChanges().size());
        final var readDocument = new Document(repository);
        Assertions.assertNotNull(readDocument);
        Assertions.assertEquals(document.root().childCount(), readDocument.root().childCount());
    }
}