package br.com.intelifiscal.fx.components.common;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class ImportacaoResumo extends GridPane {

    private final Label lblXml = new Label("0");
    private final Label lblCompras = new Label("0");
    private final Label lblVendas = new Label("0");
    private final Label lblDuplicados = new Label("0");
    private final Label lblErros = new Label("0");

    public ImportacaoResumo() {

        setHgap(15);
        setVgap(8);
        setPadding(new Insets(10));

        add(new Label("XML carregados:"), 0, 0);
        add(lblXml, 1, 0);

        add(new Label("Compras:"), 0, 1);
        add(lblCompras, 1, 1);

        add(new Label("Vendas:"), 2, 1);
        add(lblVendas, 3, 1);

        add(new Label("Duplicados:"), 0, 2);
        add(lblDuplicados, 1, 2);

        add(new Label("Erros:"), 2, 2);
        add(lblErros, 3, 2);
    }

    public void setXml(int valor) {
        lblXml.setText(String.valueOf(valor));
    }

    public void setCompras(int valor) {
        lblCompras.setText(String.valueOf(valor));
    }

    public void setVendas(int valor) {
        lblVendas.setText(String.valueOf(valor));
    }

    public void setDuplicados(int valor) {
        lblDuplicados.setText(String.valueOf(valor));
    }

    public void setErros(int valor) {
        lblErros.setText(String.valueOf(valor));
    }
}