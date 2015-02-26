import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class XmlReader {

    public static void main(String[] args) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get("page.xml"));

        checkUnsignedByte128(bytes);

        String str = new String(bytes, Charset.forName("UTF-8"));

        checkChar128(str);

        //   System.setProperty("javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

        try {
            XMLStreamReader parser2 = xmlInputFactory.createXMLStreamReader(new StringReader(str));
            while (parser2.hasNext()) {
                parser2.next();
            }
            System.out.println("StringReader parsed successfully");
        } catch (XMLStreamException e) {
            System.err.println("StringReader parsing error");
            e.printStackTrace();
        }

        try {
            XMLStreamReader parser1 = xmlInputFactory.createXMLStreamReader(new FileInputStream("page.xml"), "UTF-8");
            while (parser1.hasNext()) {
                parser1.next();
            }
            System.out.println("FileInputStream parsed successfully");
        } catch (XMLStreamException e) {
            System.err.println("FileInputStream parsing error");
            e.printStackTrace();
        }
    }

    private static void checkUnsignedByte128(byte[] bytes) {
        boolean ub128found = false;
        for (byte aByte : bytes) {
            int unsigned = (int) aByte & 0xFF;
            if (unsigned == 128) {
                ub128found = true;
            }
        }

        System.out.println("Unsigned byte " + (ub128found ? "found" : "not found"));
    }

    private static void checkChar128(String str) {
        int pos = str.indexOf(128);
        boolean char128found = pos != -1;

        System.out.println("Char 128 " + (char128found ? "found at position: " + pos : "not found"));
    }
}
