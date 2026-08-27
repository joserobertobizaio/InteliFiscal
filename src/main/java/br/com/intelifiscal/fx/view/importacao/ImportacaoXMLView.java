package br.com.intelifiscal.fx.view.importacao;

import br.com.intelifiscal.fx.components.common.Card;
import br.com.intelifiscal.fx.components.common.ImportButtonBar;
import br.com.intelifiscal.fx.components.common.SectionTitle;
import br.com.intelifiscal.fx.view.base.BaseView;
import br.com.intelifiscal.fx.components.common.ImportacaoTable;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ScrollPane;
import br.com.intelifiscal.fx.components.common.ImportacaoResumo;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;


public class ImportacaoXMLView extends BaseView {

    private final ImportButtonBar buttonBar = new ImportButtonBar();

    private final ImportacaoTable tabela = new ImportacaoTable();

    private final ImportacaoResumo resumo = new ImportacaoResumo();

    private final ProgressBar progressBar = new ProgressBar(0);

    private final TextArea txtLog = new TextArea();

    public ImportacaoXMLView() {

        super(  "Importação XML",
                "Importação de Notas Fiscais Eletrônicas de Compra e Venda"
        );

        initialize();
    }

    private void initialize() {

        progressBar.setPrefWidth(Double.MAX_VALUE);

        txtLog.setEditable(false);
        txtLog.setPrefRowCount(4);

        BorderPane layout = new BorderPane();

        layout.setPadding(new Insets(2,20,20,20));

        layout.setTop(buttonBar);

        VBox centro = new VBox(12);

        centro.getChildren().addAll(

                new SectionTitle("Arquivos Selecionados"),
                tabela,

                new SectionTitle("Resumo da Importação"),
               //resumo,

                //new SectionTitle("Progresso"),  // se a barra de progresso der problema visual
                //progressBar,                        // é só comentar essas duas linhas

                new SectionTitle("Log da Importação"),
                txtLog

        );

        tabela.setPrefHeight(220);

        layout.setCenter(centro);

        Card card = new Card(layout);

        card.setWidthPercentage(0.92);
        card.setMaxContentWidth(1200);

        ScrollPane scroll = new ScrollPane(card);

        scroll.setFitToWidth(true);
        scroll.setFitToHeight(false);

        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        scroll.setStyle(
                "-fx-background-color:transparent;" +
                        "-fx-background:transparent;"
        );

        setContent(scroll);
    }

    public ImportButtonBar getButtonBar() {
        return buttonBar;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public TextArea getTxtLog() {
        return txtLog;
    }

    public ImportacaoTable getTabela() {
        return tabela;
    }

    public ImportacaoResumo getResumo() {return resumo;}
}