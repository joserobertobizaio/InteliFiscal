package br.com.intelifiscal.fx.components.common;

import br.com.intelifiscal.fx.view.importacao.model.ImportacaoXmlItem;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ImportacaoTable extends TableView<ImportacaoXmlItem> {

    public ImportacaoTable() {

        TableColumn<ImportacaoXmlItem, String> colArquivo =
                new TableColumn<>("Arquivo");
        colArquivo.setCellValueFactory(c -> c.getValue().arquivoProperty());

        TableColumn<ImportacaoXmlItem, String> colTipo =
                new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(c -> c.getValue().tipoProperty());

        TableColumn<ImportacaoXmlItem, String> colNumero =
                new TableColumn<>("NF");
        colNumero.setCellValueFactory(c -> c.getValue().numeroNotaProperty());

        TableColumn<ImportacaoXmlItem, String> colSerie =
                new TableColumn<>("Série");
        colSerie.setCellValueFactory(c -> c.getValue().serieProperty());

        TableColumn<ImportacaoXmlItem, String> colEmitente =
                new TableColumn<>("Emitente");
        colEmitente.setCellValueFactory(c -> c.getValue().emitenteProperty());

        TableColumn<ImportacaoXmlItem, String> colDestinatario =
                new TableColumn<>("Destinatário");
        colDestinatario.setCellValueFactory(c -> c.getValue().destinatarioProperty());

        TableColumn<ImportacaoXmlItem, String> colEmissao =
                new TableColumn<>("Emissão");
        colEmissao.setCellValueFactory(c -> c.getValue().emissaoProperty());

        TableColumn<ImportacaoXmlItem, String> colValor =
                new TableColumn<>("Valor");
        colValor.setCellValueFactory(c -> c.getValue().valorProperty());

        TableColumn<ImportacaoXmlItem, String> colSituacao =
                new TableColumn<>("Situação");
        colSituacao.setCellValueFactory(c -> c.getValue().situacaoProperty());

        getColumns().addAll(
                colArquivo,
                colTipo,
                colNumero,
                colSerie,
                colEmitente,
                colDestinatario,
                colEmissao,
                colValor,
                colSituacao
        );

        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        setPlaceholder(new Label("Nenhum XML selecionado."));
    }

}