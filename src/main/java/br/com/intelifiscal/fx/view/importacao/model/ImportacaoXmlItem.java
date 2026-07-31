package br.com.intelifiscal.fx.view.importacao.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ImportacaoXmlItem {

    private final StringProperty arquivo = new SimpleStringProperty();
    private final StringProperty tipo = new SimpleStringProperty();
    private final StringProperty numeroNota = new SimpleStringProperty();
    private final StringProperty serie = new SimpleStringProperty();
    private final StringProperty emitente = new SimpleStringProperty();
    private final StringProperty destinatario = new SimpleStringProperty();
    private final StringProperty emissao = new SimpleStringProperty();
    private final StringProperty valor = new SimpleStringProperty();
    private final StringProperty situacao = new SimpleStringProperty();

    public StringProperty arquivoProperty() {
        return arquivo;
    }

    public StringProperty tipoProperty() {
        return tipo;
    }

    public StringProperty numeroNotaProperty() {
        return numeroNota;
    }

    public StringProperty serieProperty() {
        return serie;
    }

    public StringProperty emitenteProperty() {
        return emitente;
    }

    public StringProperty destinatarioProperty() {
        return destinatario;
    }

    public StringProperty emissaoProperty() {
        return emissao;
    }

    public StringProperty valorProperty() {
        return valor;
    }

    public StringProperty situacaoProperty() {
        return situacao;
    }
}