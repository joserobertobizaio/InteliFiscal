package br.com.intelifiscal.fx.view.compra;

import javafx.scene.control.TableCell;
import br.com.intelifiscal.dto.compra.CompraDTO;
import br.com.intelifiscal.dto.compra.CompraItemDTO;
import br.com.intelifiscal.fx.components.common.Card;
import br.com.intelifiscal.fx.components.common.SectionTitle;
import br.com.intelifiscal.fx.view.base.BaseView;
import br.com.intelifiscal.util.FormatadorNumero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CompraView extends BaseView {

    //==================================================
    // PESQUISA
    //==================================================

    private final TextField txtPesquisa =
            new TextField();


    //==================================================
    // TABELAS
    //==================================================

    private final TableView<CompraDTO> tabelaCompras =
            new TableView<>();

    private final TableView<CompraItemDTO> tabelaItens =
            new TableView<>();


    //==================================================
    // SCROLL
    //==================================================

    private ScrollPane scrollPane;


    //==================================================
    // CONSTRUTOR
    //==================================================

    public CompraView() {

        super(
                "Compras",
                "Consulta das Notas Fiscais de entrada"
        );

        initialize();
    }


    //==================================================
    // INICIALIZAÇÃO
    //==================================================

    private void initialize() {

        //---------------------------------------------
        // PESQUISA
        //---------------------------------------------

        txtPesquisa.setPromptText(
                "Pesquisar por NF, fornecedor ou CNPJ..."
        );

        txtPesquisa.setPrefWidth(500);

        HBox barraPesquisa =
                new HBox(
                        12,
                        txtPesquisa
                );

        barraPesquisa.setAlignment(
                Pos.CENTER_LEFT
        );


        //---------------------------------------------
        // TÍTULOS
        //---------------------------------------------

        SectionTitle secCompras =
                new SectionTitle(
                        "Compras"
                );

        SectionTitle secItens =
                new SectionTitle(
                        "Itens da compra"
                );


        //---------------------------------------------
        // TABELAS
        //---------------------------------------------

        configurarTabelaCompras();

        configurarTabelaItens();


        //---------------------------------------------
        // CONTEÚDO
        //---------------------------------------------

        VBox conteudo =
                new VBox(18);

        conteudo.setPadding(
                new Insets(20)
        );

        conteudo.getChildren().addAll(

                barraPesquisa,

                secCompras,

                tabelaCompras,

                secItens,

                tabelaItens
        );


        //---------------------------------------------
        // CARD
        //---------------------------------------------

        Card card =
                new Card(conteudo);

        card.setWidthPercentage(0.92);

        card.setMaxContentWidth(1100);


        //---------------------------------------------
        // PAINEL
        //---------------------------------------------

        VBox painel =
                new VBox(card);

        painel.setPadding(
                new Insets(10)
        );


        //---------------------------------------------
        // SCROLL
        //---------------------------------------------

        scrollPane =
                new ScrollPane(painel);

        scrollPane.setFitToWidth(true);

        scrollPane.setFitToHeight(false);

        scrollPane.setPannable(true);


        //---------------------------------------------
        // CONTENT DA BASE
        //---------------------------------------------

        setContent(scrollPane);
    }


    //==================================================
    // TABELA DE COMPRAS
    //==================================================

    private void configurarTabelaCompras() {

        tabelaCompras.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        tabelaCompras.setPlaceholder(
                new Label(
                        "Nenhuma compra encontrada."
                )
        );


        //---------------------------------------------
        // NF
        //---------------------------------------------

        TableColumn<CompraDTO, String> colNumero =
                new TableColumn<>("NF");

        colNumero.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getNumero()
                        )
        );

        colNumero.setComparator((a, b) -> {

            try {

                return Integer.compare(
                        Integer.parseInt(a),
                        Integer.parseInt(b)
                );

            } catch (NumberFormatException e) {

                return a.compareToIgnoreCase(b);
            }
        });


        //---------------------------------------------
        // SÉRIE
        //---------------------------------------------

        TableColumn<CompraDTO, String> colSerie =
                new TableColumn<>("Série");

        colSerie.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getSerie()
                        )
        );


        //---------------------------------------------
        // EMISSÃO
        //---------------------------------------------

        TableColumn<CompraDTO, String> colData =
                new TableColumn<>("Emissão");

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy"
                );

        colData.setCellValueFactory(
                data -> {

                    LocalDate dataEmissao =
                            data.getValue()
                                    .getDataEmissao();

                    String texto =
                            dataEmissao == null
                                    ? ""
                                    : dataEmissao.format(
                                    formatter
                            );

                    return new SimpleStringProperty(
                            texto
                    );
                }
        );

        colData.setComparator((a, b) -> {

            if (a == null || a.isBlank()) {

                return (b == null || b.isBlank())
                        ? 0
                        : -1;
            }

            if (b == null || b.isBlank()) {
                return 1;
            }

            return LocalDate.parse(
                    a,
                    formatter
            ).compareTo(
                    LocalDate.parse(
                            b,
                            formatter
                    )
            );
        });


        //---------------------------------------------
        // FORNECEDOR
        //---------------------------------------------

        TableColumn<CompraDTO, String> colFornecedor =
                new TableColumn<>("Fornecedor");

        colFornecedor.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getEmitente()
                        )
        );

        colFornecedor.setComparator(
                String.CASE_INSENSITIVE_ORDER
        );


        //---------------------------------------------
        // CNPJ
        //---------------------------------------------

        TableColumn<CompraDTO, String> colCnpj =
                new TableColumn<>("CNPJ");

        colCnpj.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue()
                                        .getCnpjEmitente()
                        )
        );


        //---------------------------------------------
        // VALOR
        //---------------------------------------------

        TableColumn<CompraDTO, Number> colValor =
                new TableColumn<>("Valor");

        colValor.setCellValueFactory(
                data ->
                        new SimpleObjectProperty<>(
                                data.getValue().getValorTotal()
                        )
        );

        FormatadorNumero.aplicar(
                colValor
        );


        //---------------------------------------------
        // SITUAÇÃO
        //---------------------------------------------

        TableColumn<CompraDTO, String> colSituacao =
                new TableColumn<>("Situação");

        colSituacao.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getSituacao()
                        )
        );


        //---------------------------------------------
        // ADICIONA COLUNAS
        //---------------------------------------------

        tabelaCompras.getColumns().setAll(

                colNumero,

                colSerie,

                colData,

                colFornecedor,

                colCnpj,

                colValor,

                colSituacao
        );


        //---------------------------------------------
        // TAMANHO
        //---------------------------------------------

        tabelaCompras.setPrefHeight(300);

        tabelaCompras.setMinHeight(300);

        tabelaCompras.setMaxHeight(300);
    }


    //==================================================
    // TABELA DE ITENS
    //==================================================

    private void configurarTabelaItens() {

        tabelaItens.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        tabelaItens.setPlaceholder(
                new Label(
                        "Selecione uma compra para visualizar os itens."
                )
        );


        //---------------------------------------------
        // ITEM
        //---------------------------------------------

        TableColumn<CompraItemDTO, Number> colItem =
                new TableColumn<>("Item");

        colItem.setCellValueFactory(
                data ->
                        new SimpleObjectProperty<>(
                                data.getValue()
                                        .getNumeroItem()
                        )
        );


        //---------------------------------------------
        // CÓDIGO
        //---------------------------------------------

        TableColumn<CompraItemDTO, String> colCodigo =
                new TableColumn<>("Código");

        colCodigo.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue()
                                        .getCodigoProduto()
                        )
        );


        //---------------------------------------------
        // DESCRIÇÃO
        //---------------------------------------------

        TableColumn<CompraItemDTO, String> colDescricao =
                new TableColumn<>("Descrição");

        colDescricao.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue()
                                        .getDescricao()
                        )
        );

        colDescricao.setComparator(
                String.CASE_INSENSITIVE_ORDER
        );


        //---------------------------------------------
        // UNIDADE
        //---------------------------------------------

        TableColumn<CompraItemDTO, String> colUnidade =
                new TableColumn<>("Unidade");

        colUnidade.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue()
                                        .getUnidade()
                        )
        );


        //---------------------------------------------
        // QUANTIDADE
        //---------------------------------------------

        TableColumn<CompraItemDTO, Number> colQuantidade =
                new TableColumn<>("Quantidade");

        colQuantidade.setCellValueFactory(
                data ->
                        new SimpleObjectProperty<>(
                                data.getValue()
                                        .getQuantidade()
                        )
        );

        FormatadorNumero.aplicarQuantidade(
                colQuantidade
        );

        //---------------------------------------------
        // VALOR UNITÁRIO
        //---------------------------------------------

        TableColumn<CompraItemDTO, Number> colValorUnitario =
                new TableColumn<>("Valor Unitário");

        colValorUnitario.setCellValueFactory(
                data ->
                        new SimpleObjectProperty<>(
                                data.getValue()
                                        .getValorUnitario()
                        )
        );

        FormatadorNumero.aplicarValorUnitario(
                colValorUnitario
        );

        //---------------------------------------------
        // DESCONTO
        //---------------------------------------------

        TableColumn<CompraItemDTO, Number> colDesconto =
                new TableColumn<>("Desconto");

        colValorUnitario.setCellValueFactory(
                data ->
                        new SimpleObjectProperty<>(
                                data.getValue()
                                        .getValorUnitario()
                        )
        );

        FormatadorNumero.aplicarValorUnitario(
                colValorUnitario
        );

        //---------------------------------------------
        // VALOR TOTAL
        //---------------------------------------------

        TableColumn<CompraItemDTO, Number> colValorTotal =
                new TableColumn<>("Valor Total");

        colValorTotal.setCellValueFactory(
                data ->
                        new SimpleObjectProperty<>(
                                data.getValue()
                                        .getValorTotal()
                        )
        );

        FormatadorNumero.aplicar(
                colValorTotal
        );

        //---------------------------------------------
        // ADICIONA COLUNAS
        //---------------------------------------------

        tabelaItens.getColumns().setAll(

                colItem,

                colCodigo,

                colDescricao,

                colUnidade,

                colQuantidade,

                colValorUnitario,

                colDesconto,

                colValorTotal
        );


        //---------------------------------------------
        // TAMANHO
        //---------------------------------------------

        tabelaItens.setPrefHeight(220);

        tabelaItens.setMinHeight(220);

        tabelaItens.setMaxHeight(220);
    }


    //==================================================
    // GETTERS
    //==================================================

    public TextField getTxtPesquisa() {

        return txtPesquisa;
    }


    public TableView<CompraDTO> getTabelaCompras() {

        return tabelaCompras;
    }


    public TableView<CompraItemDTO> getTabelaItens() {

        return tabelaItens;
    }


    public ScrollPane getScrollPane() {

        return scrollPane;
    }
}