package ru.dasha.MoexService.parser;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import ru.dasha.MoexService.dto.BondDto;
import ru.dasha.MoexService.exception.BondParsingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MoexXmlParserImpl implements Parser {

    private static final Logger logger = LoggerFactory.getLogger(MoexXmlParserImpl.class);

    @Override
    public List<BondDto> parse(String ratesAsString) {
        var bonds = new ArrayList<BondDto>();

        var dbf = DocumentBuilderFactory.newInstance();
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            var db = dbf.newDocumentBuilder();

            try (var reader = new StringReader(ratesAsString)) {
                Document doc = db.parse(new InputSource(reader));
                doc.getDocumentElement().normalize();

                NodeList list = doc.getElementsByTagName("row");

                for (var rowIdx = 0; rowIdx < list.getLength(); rowIdx++) {
                    var node = list.item(rowIdx);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        var element = (Element) node;
                        String ticker = element.getAttribute("SECID");
                        String price = element.getAttribute("PREVPRICE");
                        String name = element.getAttribute("SHORTNAME");
                        if (!ticker.isEmpty() && !price.isEmpty() && !name.isEmpty()) {
                            bonds.add(new BondDto(ticker, name, Double.parseDouble(price)));
                        }
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("xml parsing error, xml:{}", ratesAsString, ex);
            throw new BondParsingException(ex);
        }
        return bonds;
    }
}