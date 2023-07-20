import org.example.CurrencyRates;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CurrencyRatesTest {
    @Test
    public void testValidCurrencyCodeAndDate() throws ParserConfigurationException, IOException, SAXException {
        String expected = "USD (Доллар США): 61,2475";
        String actual = CurrencyRates.getCurrencyRate("USD", LocalDate.parse("2022-10-08"));
        assertEquals(expected, actual);
    }

    @Test
    public void testInvalidCurrencyCode() throws ParserConfigurationException, IOException, SAXException {
        String expected = "Currency with code XYZ not found";
        String actual = CurrencyRates.getCurrencyRate("XYZ", LocalDate.parse("2022-10-08"));
        assertEquals(expected, actual);
    }

    @Test
    public void testInvalidDate() {
        assertThrows(java.time.format.DateTimeParseException.class, () -> {
            CurrencyRates.getCurrencyRate("USD", LocalDate.parse("2022-13-01"));
        });
    }
}

