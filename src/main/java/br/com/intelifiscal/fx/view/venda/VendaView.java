package br.com.intelifiscal.fx.view.venda;

import br.com.intelifiscal.dto.venda.VendaDTO;
import br.com.intelifiscal.dto.venda.VendaItemDTO;
import br.com.intelifiscal.fx.components.common.Card;
import br.com.intelifiscal.fx.components.common.SectionTitle;
import br.com.intelifiscal.fx.view.base.BaseView;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import br.com.intelifiscal.util.FormatadorNumero;

import javafx.scene.control.TableColumn;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class VendaView extends BaseView {

    //==================================================
    // PESQUISA
    //==================================================

    private final TextField txtPesquisa =
            new TextField();


    //==================================================
    // TABELAS
    //==================================================

    private final TableView<VendaDTO> tabelaVendas =
            new TableView<>();

    private final TableView<VendaItemDTO> tabelaItens =
            new TableView<>();


    //==================================================
    // SCROLL
    //==================================================

    private ScrollPane scrollPane;


    //==================================================
    // CONSTRUTOR
    //==================================================

    public VendaView() {

        super(
                "Vendas",
                "Consulta das Notas Fiscais de saída"
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
                "Pesquisar por NF, cliente ou CNPJ..."
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

        SectionTitle secVendas =
                new SectionTitle(
                        "Vendas"
                );

        SectionTitle secItens =
                new SectionTitle(
                        "Itens da venda"
                );


        //---------------------------------------------
        // TABELAS
        //---------------------------------------------

        configurarTabelaVendas();

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

                secVendas,

                tabelaVendas,

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
    // TABELA DE VENDAS
    //==================================================

    private void configurarTabelaVendas() {

        tabelaVendas.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        tabelaVendas.setPlaceholder(
                new Label(
                        "Nenhuma venda encontrada."
                )
        );


        //---------------------------------------------
        // NF
        //---------------------------------------------

        TableColumn<VendaDTO, String> colNumero =
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

        TableColumn<VendaDTO, String> colSerie =
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

        TableColumn<VendaDTO, String> colData =
                new TableColumn<>("Emissão");

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        colData.setCellValueFactory(
                data -> {

                    LocalDate dataEmissao =
                            data.getValue().getDataEmissao();

                    String texto =
                            dataEmissao == null
                                    ? ""
                                    : dataEmissao.format(formatter);

                    return new SimpleStringProperty(
                            texto
                    );
                }
        );

        colData.setComparator((a, b) -> {

            if (a == null || a.isBlank()) {
                return (b == null || b.isBlank()) ? 0 : -1;
            }

            if (b == null || b.isBlank()) {
                return 1;
            }

            return LocalDate.parse(a, formatter)
                    .compareTo(
                            LocalDate.parse(b, formatter)
                    );
        });

        //---------------------------------------------
        // CLIENTE
        //---------------------------------------------

        TableColumn<VendaDTO, String> colCliente =
                new TableColumn<>("Cliente");

        colCliente.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getDestinatario()
                        )
        );

        colCliente.setComparator(
                String.CASE_INSENSITIVE_ORDER
        );

        //---------------------------------------------
        // CNPJ
        //---------------------------------------------

        TableColumn<VendaDTO, String> colCnpj =
                new TableColumn<>("CNPJ");

        colCnpj.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getCnpjDestinatario()
                        )
        );


        //---------------------------------------------
        // VALOR
        //---------------------------------------------

        TableColumn<VendaDTO, Number> colValor =
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

        TableColumn<VendaDTO, String> colSituacao =
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

        tabelaVendas.getColumns().setAll(

                colNumero,

                colSerie,

                colData,

                colCliente,

                colCnpj,

                colValor,

                colSituacao
        );

        //=============================================
        // LARGURAS PREFERENCIAIS
        //=============================================

        colNumero.setPrefWidth(70);
        colSerie.setPrefWidth(60);
        colData.setPrefWidth(100);
        colCliente.setPrefWidth(280);
        colCnpj.setPrefWidth(150);
        colValor.setPrefWidth(120);
        colSituacao.setPrefWidth(110);

        //=============================================
        // TAMANHO
        //=============================================

        tabelaVendas.setPrefHeight(300);

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
                        "Selecione uma venda para visualizar os itens."
                )
        );


        //---------------------------------------------
        // ITEM
        //---------------------------------------------

        TableColumn<VendaItemDTO, Number> colItem =
                new TableColumn<>("Item");

        colItem.setCellValueFactory(
                data ->
                        new SimpleObjectProperty<>(
                                data.getValue().getNumeroItem()
                        )
        );


        //---------------------------------------------
        // CÓDIGO
        //---------------------------------------------

        TableColumn<VendaItemDTO, String> colCodigo =
                new TableColumn<>("Código");

        colCodigo.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getCodigoProduto()
                        )
        );


        //---------------------------------------------
        // DESCRIÇÃO
        //---------------------------------------------

        TableColumn<VendaItemDTO, String> colDescricao =
                new TableColumn<>("Descrição");

        colDescricao.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getDescricao()
                        )
        );


        //---------------------------------------------
        // UNIDADE
        //---------------------------------------------

        TableColumn<VendaItemDTO, String> colUnidade =
                new TableColumn<>("Unidade");

        colUnidade.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getUnidade()
                        )
        );


        //---------------------------------------------
        // QUANTIDADE
        //---------------------------------------------

        TableColumn<VendaItemDTO, Number> colQuantidade =
                new TableColumn<>("Quantidade");

        colQuantidade.setCellValueFactory(
                data ->
                        new SimpleObjectProperty<>(
                                data.getValue().getQuantidade()
                        )
        );

        FormatadorNumero.aplicarQuantidade(
                colQuantidade
        );

        //---------------------------------------------
        // VALOR UNITÁRIO
        //---------------------------------------------

        TableColumn<VendaItemDTO, Number> colValorUnitario =
                new TableColumn<>("Valor Unitário");

        colValorUnitario.setCellValueFactory(
                data ->
                        new SimpleObjectProperty<>(
                                data.getValue().getValorUnitario()
                        )
        );

        FormatadorNumero.aplicarValorUnitario(
                colValorUnitario
        );


        //---------------------------------------------
        // DESCONTO
        //---------------------------------------------

        TableColumn<VendaItemDTO, Number> colDesconto =
                new TableColumn<>("Desconto");

        colDesconto.setCellValueFactory(
                data ->
                        new SimpleObjectProperty<>(
                                data.getValue().getDesconto()
                        )
        );

        FormatadorNumero.aplicar(
                colDesconto
        );

        //---------------------------------------------
        // VALOR TOTAL
        //---------------------------------------------

        TableColumn<VendaItemDTO, Number> colValorTotal =
                new TableColumn<>("Valor Total");

        colValorTotal.setCellValueFactory(
                data ->
                        new SimpleObjectProperty<>(
                                data.getValue().getValorTotal()
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


        //=============================================
        // LARGURAS PREFERENCIAIS
        //=============================================

        colItem.setPrefWidth(55);
        colCodigo.setPrefWidth(100);
        colDescricao.setPrefWidth(300);
        colUnidade.setPrefWidth(75);
        colQuantidade.setPrefWidth(100);
        colValorUnitario.setPrefWidth(120);
        colDesconto.setPrefWidth(100);
        colValorTotal.setPrefWidth(120);

    }

    //==================================================
    // GETTERS
    //==================================================

    public TextField getTxtPesquisa() {

        return txtPesquisa;
    }


    public TableView<VendaDTO> getTabelaVendas() {

        return tabelaVendas;
    }


    public TableView<VendaItemDTO> getTabelaItens() {

        return tabelaItens;
    }


    public ScrollPane getScrollPane() {

        return scrollPane;
    }
}