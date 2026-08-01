package br.com.intelifiscal.util;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class XmlUtil {

    private XmlUtil() {
    }

    /**
     * Retorna o primeiro elemento encontrado.
     */
    public static Element getFirstElement(Element parent, String tag) {

        NodeList list = parent.getElementsByTagName(tag);

        if (list.getLength() == 0) {
            return null;
        }

        return (Element) list.item(0);
    }

    /**
     * Retorna o texto de uma tag filha.
     */
    public static String getText(Element parent, String tag) {

        Element element = getFirstElement(parent, tag);

        if (element == null) {
            return "";
        }

        return element.getTextContent().trim();
    }

    public static String formatarData(LocalDate data) {

        if (data == null) {
            return "";
        }

        return data.format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
        );
    }

    public static String formatarValor(BigDecimal valor) {

        if (valor == null) {
            return "";
        }

        NumberFormat formato =
                NumberFormat.getNumberInstance(
                        new Locale("pt", "BR")
                );

        formato.setMinimumFractionDigits(2);
        formato.setMaximumFractionDigits(2);

        return formato.format(valor);
    }

}