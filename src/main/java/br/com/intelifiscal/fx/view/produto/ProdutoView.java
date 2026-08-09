package br.com.intelifiscal.fx.view.produto;

import br.com.intelifiscal.dto.produto.ProdutoDTO;
import br.com.intelifiscal.dto.produto.ProdutoHistoricoDTO;
import br.com.intelifiscal.fx.components.common.Card;
import br.com.intelifiscal.fx.components.common.CrudButtonBar;
import br.com.intelifiscal.fx.components.common.SectionTitle;
import br.com.intelifiscal.fx.view.base.BaseView;

import javafx.scene.control.Label;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ProdutoView extends BaseView {

    //==================================================
    // PESQUISA
    //==================================================

    private final TextField txtPesquisa =
            new TextField();


    //==================================================
    // FORMULÁRIO DO PRODUTO
    //==================================================

    private final TextField txtCodigoProduto =
            new TextField();

    private final TextField txtCodigoBarras =
            new TextField();

    private final TextField txtDescricao =
            new TextField();

    private final TextField txtNcm =
            new TextField();

    private final TextField txtCest =
            new TextField();

    private final TextField txtUnidade =
            new TextField();

    private final CheckBox chkAtivo =
            new CheckBox("Produto ativo");


    //==================================================
    // TABELA
    //==================================================

    private final TableView<ProdutoDTO> tabelaProdutos =
            new TableView<>();

    private final TableView<ProdutoHistoricoDTO> tabelaHistorico =
            new TableView<>();


    //==================================================
    // BOTÕES
    //==================================================

    private final CrudButtonBar crudButtonBar =
            new CrudButtonBar();


    private ScrollPane scrollPane;


    //==================================================
    // CONSTRUTOR
    //==================================================

    public ProdutoView() {

        super(
                "Produtos",
                "Cadastro e consulta dos produtos"
        );

        initialize();

        configurarBotoesInicial();
    }


    //==================================================
    // INICIALIZAÇÃO
    //==================================================

    private void initialize() {

        //---------------------------------------------
        // PESQUISA
        //---------------------------------------------

        txtPesquisa.setPromptText(
                "Pesquisar por código, código de barras ou descrição..."
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
        // FORMULÁRIO
        //---------------------------------------------

        SectionTitle secDados =
                new SectionTitle(
                        "Dados do produto"
                );

        configurarFormulario();


        //---------------------------------------------
        // TÍTULO DA TABELA
        //---------------------------------------------

        SectionTitle secProdutos =
                new SectionTitle(
                        "Produtos cadastrados"
                );

        SectionTitle secHistorico =
                new SectionTitle(
                        "Histórico do produto"
                );


        //---------------------------------------------
        // TABELA
        //---------------------------------------------

        configurarTabela();

        configurarTabelaHistorico();


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

                secDados,

                criarFormulario(),

                secProdutos,

                tabelaProdutos,

                secHistorico,

                tabelaHistorico,

                crudButtonBar
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
    // CONFIGURA FORMULÁRIO
    //==================================================

    private void configurarFormulario() {

        txtCodigoProduto.setPromptText(
                "Código do produto"
        );

        txtCodigoBarras.setPromptText(
                "Código de barras"
        );

        txtDescricao.setPromptText(
                "Descrição"
        );

        txtNcm.setPromptText(
                "NCM"
        );

        txtCest.setPromptText(
                "CEST"
        );

        txtUnidade.setPromptText(
                "Unidade"
        );


        txtCodigoProduto.setPrefWidth(180);

        txtCodigoBarras.setPrefWidth(220);

        txtDescricao.setPrefWidth(400);

        txtNcm.setPrefWidth(150);

        txtCest.setPrefWidth(150);

        txtUnidade.setPrefWidth(100);


        chkAtivo.setSelected(true);
    }


    //==================================================
    // CRIA FORMULÁRIO
    //==================================================

    private GridPane criarFormulario() {

        GridPane grid =
                new GridPane();

        grid.setHgap(12);

        grid.setVgap(10);

        grid.setPadding(
                new Insets(5, 0, 10, 0)
        );


        //---------------------------------------------
        // LINHA 0
        //---------------------------------------------

        Label lblCodigo =
                new Label("Código:");

        Label lblCodigoBarras =
                new Label("Código de barras:");

        grid.add(
                lblCodigo,
                0,
                0
        );

        grid.add(
                txtCodigoProduto,
                1,
                0
        );

        grid.add(
                lblCodigoBarras,
                2,
                0
        );

        grid.add(
                txtCodigoBarras,
                3,
                0
        );


        //---------------------------------------------
        // LINHA 1
        //---------------------------------------------

        Label lblDescricao =
                new Label("Descrição:");

        grid.add(
                lblDescricao,
                0,
                1
        );

        grid.add(
                txtDescricao,
                1,
                1,
                3,
                1
        );


        //---------------------------------------------
        // LINHA 2
        //---------------------------------------------

        Label lblNcm =
                new Label("NCM:");

        Label lblCest =
                new Label("CEST:");

        Label lblUnidade =
                new Label("Unidade:");

        grid.add(
                lblNcm,
                0,
                2
        );

        grid.add(
                txtNcm,
                1,
                2
        );

        grid.add(
                lblCest,
                2,
                2
        );

        grid.add(
                txtCest,
                3,
                2
        );


        //---------------------------------------------
        // LINHA 3
        //---------------------------------------------

        grid.add(
                lblUnidade,
                0,
                3
        );

        grid.add(
                txtUnidade,
                1,
                3
        );

        grid.add(
                chkAtivo,
                3,
                3
        );


        return grid;
    }


    //==================================================
    // CONFIGURA TABELA
    //==================================================

    private void configurarTabela() {

        tabelaProdutos.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        tabelaProdutos.setPlaceholder(
                new Label(
                        "Nenhum produto cadastrado."
                )
        );


        //---------------------------------------------
        // COLUNAS
        //---------------------------------------------

        TableColumn<ProdutoDTO, String> colCodigo =
                new TableColumn<>(
                        "Código"
                );

        TableColumn<ProdutoDTO, String> colCodigoBarras =
                new TableColumn<>(
                        "Código de Barras"
                );

        TableColumn<ProdutoDTO, String> colDescricao =
                new TableColumn<>(
                        "Descrição"
                );

        TableColumn<ProdutoDTO, String> colNcm =
                new TableColumn<>(
                        "NCM"
                );

        TableColumn<ProdutoDTO, String> colCest =
                new TableColumn<>(
                        "CEST"
                );

        TableColumn<ProdutoDTO, String> colUnidade =
                new TableColumn<>(
                        "Unidade"
                );

        // ATIVO AGORA SERÁ TEXTO: SIM / NÃO
        TableColumn<ProdutoDTO, String> colAtivo =
                new TableColumn<>(
                        "Ativo"
                );


        //---------------------------------------------
        // ADICIONA COLUNAS
        //---------------------------------------------

        tabelaProdutos.getColumns().addAll(

                colCodigo,

                colCodigoBarras,

                colDescricao,

                colNcm,

                colCest,

                colUnidade,

                colAtivo
        );


        //---------------------------------------------
        // TAMANHO DA TABELA
        //---------------------------------------------

        tabelaProdutos.setPrefHeight(230);

        tabelaProdutos.setMinHeight(230);

        tabelaProdutos.setMaxHeight(230);
    }

    //==================================================
// CONFIGURA TABELA HISTÓRICO
//==================================================

    private void configurarTabelaHistorico() {

        tabelaHistorico.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        tabelaHistorico.setPlaceholder(
                new Label(
                        "Nenhum histórico encontrado."
                )
        );


        //---------------------------------------------
        // TIPO
        //---------------------------------------------

        TableColumn<ProdutoHistoricoDTO, String> colTipo =
                new TableColumn<>("Tipo");

        colTipo.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getTipo()
                        )
        );


        //---------------------------------------------
        // NF
        //---------------------------------------------

        TableColumn<ProdutoHistoricoDTO, String> colNf =
                new TableColumn<>("NF");

        colNf.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getNumeroNfe()
                        )
        );


        //---------------------------------------------
        // SÉRIE
        //---------------------------------------------

        TableColumn<ProdutoHistoricoDTO, String> colSerie =
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

        TableColumn<ProdutoHistoricoDTO, String> colData =
                new TableColumn<>("Emissão");

        colData.setCellValueFactory(
                data -> {

                    LocalDateTime dataEmissao =
                            data.getValue().getDataEmissao();

                    String texto =
                            dataEmissao == null
                                    ? ""
                                    : dataEmissao.format(
                                    DateTimeFormatter.ofPattern(
                                            "dd/MM/yyyy"
                                    )
                            );

                    return new SimpleStringProperty(
                            texto
                    );
                }
        );


        //---------------------------------------------
        // EMITENTE
        //---------------------------------------------

        TableColumn<ProdutoHistoricoDTO, String> colEmitente =
                new TableColumn<>("Emitente");

        colEmitente.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getEmitente()
                        )
        );


        //---------------------------------------------
        // DESTINATÁRIO
        //---------------------------------------------

        TableColumn<ProdutoHistoricoDTO, String> colDestinatario =
                new TableColumn<>("Destinatário");

        colDestinatario.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getDestinatario()
                        )
        );


        //---------------------------------------------
        // QUANTIDADE
        //---------------------------------------------

        TableColumn<ProdutoHistoricoDTO, Number> colQuantidade =
                new TableColumn<>("Quantidade");

        colQuantidade.setCellValueFactory(
                data ->
                        new SimpleObjectProperty<>(
                                data.getValue().getQuantidade()
                        )
        );


        //---------------------------------------------
        // VALOR UNITÁRIO
        //---------------------------------------------

        TableColumn<ProdutoHistoricoDTO, Number> colValorUnitario =
                new TableColumn<>("Valor Unitário");

        colValorUnitario.setCellValueFactory(
                data ->
                        new SimpleObjectProperty<>(
                                data.getValue().getValorUnitario()
                        )
        );


        //---------------------------------------------
        // VALOR TOTAL
        //---------------------------------------------

        TableColumn<ProdutoHistoricoDTO, Number> colValorTotal =
                new TableColumn<>("Valor Total");

        colValorTotal.setCellValueFactory(
                data ->
                        new SimpleObjectProperty<>(
                                data.getValue().getValorTotal()
                        )
        );


        //---------------------------------------------
        // ADICIONA COLUNAS
        //---------------------------------------------

        tabelaHistorico.getColumns().setAll(

                colTipo,

                colNf,

                colSerie,

                colData,

                colEmitente,

                colDestinatario,

                colQuantidade,

                colValorUnitario,

                colValorTotal
        );


        //---------------------------------------------
        // TAMANHO
        //---------------------------------------------

        tabelaHistorico.setPrefHeight(220);

        tabelaHistorico.setMinHeight(220);

        tabelaHistorico.setMaxHeight(220);
    }


    //==================================================
    // GETTERS
    //==================================================

    public TextField getTxtPesquisa() {

        return txtPesquisa;
    }


    public TextField getTxtCodigoProduto() {

        return txtCodigoProduto;
    }


    public TextField getTxtCodigoBarras() {

        return txtCodigoBarras;
    }


    public TextField getTxtDescricao() {

        return txtDescricao;
    }


    public TextField getTxtNcm() {

        return txtNcm;
    }


    public TextField getTxtCest() {

        return txtCest;
    }


    public TextField getTxtUnidade() {

        return txtUnidade;
    }


    public CheckBox getChkAtivo() {

        return chkAtivo;
    }


    public TableView<ProdutoDTO> getTabelaProdutos() {

        return tabelaProdutos;
    }


    public CrudButtonBar getCrudButtonBar() {

        return crudButtonBar;
    }

    //==================================================
    // CONTROLE DOS BOTÕES
    //==================================================

    public void configurarBotoesInicial() {

        crudButtonBar.getBtNovo().setDisable(true);

        crudButtonBar.getBtExcluir().setDisable(true);

        crudButtonBar.getBtSalvar().setDisable(false);

        crudButtonBar.getBtFechar().setDisable(false);

    }


    // Produto selecionado na tabela
    public void produtoSelecionado() {

        crudButtonBar.getBtNovo().setDisable(false);

        crudButtonBar.getBtExcluir().setDisable(false);

        crudButtonBar.getBtSalvar().setDisable(false);

        crudButtonBar.getBtFechar().setDisable(false);

    }


    // Entrou no modo de edição
    public void modoEdicao() {

        crudButtonBar.getBtNovo().setDisable(true);

        crudButtonBar.getBtExcluir().setDisable(false);

        crudButtonBar.getBtSalvar().setDisable(false);

        crudButtonBar.getBtFechar().setDisable(true);

    }


    public ScrollPane getScrollPane() {

        return scrollPane;
    }

    public TableView<ProdutoHistoricoDTO> getTabelaHistorico() {

        return tabelaHistorico;
    }

}