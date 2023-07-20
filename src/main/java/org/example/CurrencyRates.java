package org.example;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class CurrencyRates {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter currency code (ISO 4217 format): ");
        String code = scanner.nextLine();

        System.out.print("Enter date (YYYY-MM-DD format): ");
        String dateString = scanner.nextLine();

        try {
            LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
            String result = getCurrencyRate(code, date);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static String getCurrencyRate(String code, LocalDate date) throws ParserConfigurationException, IOException, SAXException {
        String urlString = "https://www.cbr.ru/scripts/XML_daily.asp?date_req="
                + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(connection.getInputStream());

            NodeList currencyList = document.getElementsByTagName("Valute");
            for (int i = 0; i < currencyList.getLength(); i++) {
                Node currencyNode = currencyList.item(i);
                if (currencyNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element currencyElement = (Element) currencyNode;
                    String currencyCode = currencyElement.getElementsByTagName("CharCode").item(0).getTextContent();
                    if (currencyCode.equals(code)) {
                        String value = currencyElement.getElementsByTagName("Value").item(0).getTextContent();
                        String name = currencyElement.getElementsByTagName("Name").item(0).getTextContent();
                        return code + " (" + name + "): " + value;
                    }
                }
            }
        } else {
            throw new IOException("HTTP Error: " + responseCode);
        }

        return "Currency with code " + code + " not found";
    }
}