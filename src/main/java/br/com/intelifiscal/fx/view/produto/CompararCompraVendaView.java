package br.com.intelifiscal.fx.view.produto;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CompararCompraVendaView extends BorderPane {

    // ============================================================
    // CAMPOS DE PESQUISA
    // ============================================================

    private final TextField txtCodigoCompra =
            new TextField();

    private final TextField txtCodigoVenda =
            new TextField();

    private final Button btPesquisar =
            new Button("🔎 Pesquisar");

    private final Button btFechar =
            new Button("✖ Fechar");

    // ============================================================
    // INFORMAÇÕES DA COMPRA
    // ============================================================

    private final Label lblCompraCodigo =
            new Label("-");

    private final Label lblCompraDescricao =
            new Label("-");

    private final Label lblCompraUnidade =
            new Label("-");

    private final Label lblCompraQuantidade =
            new Label("-");

    private final Label lblCompraPreco =
            new Label("-");

    // ============================================================
    // INFORMAÇÕES DA VENDA
    // ============================================================

    private final Label lblVendaCodigo =
            new Label("-");

    private final Label lblVendaDescricao =
            new Label("-");

    private final Label lblVendaUnidade =
            new Label("-");

    private final Label lblVendaQuantidade =
            new Label("-");

    private final Label lblVendaPreco =
            new Label("-");

    // ============================================================
    // RESULTADO
    // ============================================================

    private final Label lblResultado =
            new Label("Informe os códigos para realizar a comparação.");

    // ============================================================
    // CONSTRUTOR
    // ============================================================

    public CompararCompraVendaView() {

        construirTela();
    }

    // ============================================================
    // CONSTRUÇÃO DA TELA
    // ============================================================

    private void construirTela() {

        setPadding(new Insets(20));

        // --------------------------------------------------------
        // TÍTULO
        // --------------------------------------------------------

        Label titulo =
                new Label("Comparar Compra × Venda");

        titulo.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #17365D;"
        );

        Label subtitulo =
                new Label(
                        "Compare o produto comprado com o produto vendido, " +
                                "mesmo quando os códigos e unidades forem diferentes."
                );

        subtitulo.setStyle(
                "-fx-font-size: 13px;"
        );

        VBox cabecalho =
                new VBox(
                        5,
                        titulo,
                        subtitulo
                );

        // --------------------------------------------------------
        // ÁREA DE PESQUISA
        // --------------------------------------------------------

        Label lblCodigoCompra =
                new Label("Código do produto na compra:");

        Label lblCodigoVenda =
                new Label("Código do produto na venda:");

        txtCodigoCompra.setPromptText(
                "Ex.: 0056"
        );

        txtCodigoVenda.setPromptText(
                "Ex.: 102326"
        );

        txtCodigoCompra.setPrefWidth(250);
        txtCodigoVenda.setPrefWidth(250);

        GridPane pesquisa =
                new GridPane();

        pesquisa.setHgap(15);
        pesquisa.setVgap(8);
        pesquisa.setPadding(
                new Insets(15, 0, 15, 0)
        );

        pesquisa.add(
                lblCodigoCompra,
                0,
                0
        );

        pesquisa.add(
                txtCodigoCompra,
                0,
                1
        );

        pesquisa.add(
                lblCodigoVenda,
                1,
                0
        );

        pesquisa.add(
                txtCodigoVenda,
                1,
                1
        );

        pesquisa.add(
                btPesquisar,
                2,
                1
        );

        // --------------------------------------------------------
        // PAINEL DE COMPARAÇÃO
        // --------------------------------------------------------

        VBox painelCompra =
                criarPainelCompra();

        VBox painelVenda =
                criarPainelVenda();

        HBox comparacao =
                new HBox(
                        20,
                        painelCompra,
                        painelVenda
                );

        comparacao.setAlignment(
                Pos.CENTER
        );

        // --------------------------------------------------------
        // RESULTADO
        // --------------------------------------------------------

        Label lblTituloResultado =
                new Label("Resultado da comparação");

        lblTituloResultado.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #17365D;"
        );

        lblResultado.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;"
        );

        VBox resultado =
                new VBox(
                        8,
                        lblTituloResultado,
                        lblResultado
                );

        resultado.setPadding(
                new Insets(20, 0, 20, 0)
        );

        // --------------------------------------------------------
        // BOTÃO FECHAR
        // --------------------------------------------------------

        HBox rodape =
                new HBox(
                        btFechar
                );

        rodape.setAlignment(
                Pos.CENTER_RIGHT
        );

        rodape.setPadding(
                new Insets(10, 0, 0, 0)
        );

        // --------------------------------------------------------
        // CONTEÚDO CENTRAL
        // --------------------------------------------------------

        VBox conteudo =
                new VBox(
                        10,
                        cabecalho,
                        new Separator(),
                        pesquisa,
                        comparacao,
                        resultado,
                        rodape
                );

        conteudo.setPadding(
                new Insets(10)
        );

        setCenter(conteudo);

        // --------------------------------------------------------
        // ESTILOS
        // --------------------------------------------------------

        btPesquisar.setPrefWidth(130);
        btPesquisar.setPrefHeight(32);

        btFechar.setPrefWidth(100);
        btFechar.setPrefHeight(32);

        painelCompra.setStyle(
                "-fx-border-color: #D9D9D9;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-background-color: white;"
        );

        painelVenda.setStyle(
                "-fx-border-color: #D9D9D9;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-background-color: white;"
        );
    }

    // ============================================================
    // PAINEL DA COMPRA
    // ============================================================

    private VBox criarPainelCompra() {

        Label titulo =
                new Label("🛒 Compra");

        titulo.setStyle(
                "-fx-font-size: 17px;" +
                        "-fx-font-weight: bold;"
        );

        GridPane dados =
                new GridPane();

        dados.setHgap(10);
        dados.setVgap(10);

        adicionarLinha(
                dados,
                "Código:",
                lblCompraCodigo,
                0
        );

        adicionarLinha(
                dados,
                "Descrição:",
                lblCompraDescricao,
                1
        );

        adicionarLinha(
                dados,
                "Unidade:",
                lblCompraUnidade,
                2
        );

        adicionarLinha(
                dados,
                "Quantidade:",
                lblCompraQuantidade,
                3
        );

        adicionarLinha(
                dados,
                "Preço unitário:",
                lblCompraPreco,
                4
        );

        VBox painel =
                new VBox(
                        15,
                        titulo,
                        dados
                );

        painel.setPadding(
                new Insets(15)
        );

        painel.setPrefWidth(400);

        return painel;
    }

    // ============================================================
    // PAINEL DA VENDA
    // ============================================================

    private VBox criarPainelVenda() {

        Label titulo =
                new Label("💰 Venda");

        titulo.setStyle(
                "-fx-font-size: 17px;" +
                        "-fx-font-weight: bold;"
        );

        GridPane dados =
                new GridPane();

        dados.setHgap(10);
        dados.setVgap(10);

        adicionarLinha(
                dados,
                "Código:",
                lblVendaCodigo,
                0
        );

        adicionarLinha(
                dados,
                "Descrição:",
                lblVendaDescricao,
                1
        );

        adicionarLinha(
                dados,
                "Unidade:",
                lblVendaUnidade,
                2
        );

        adicionarLinha(
                dados,
                "Quantidade:",
                lblVendaQuantidade,
                3
        );

        adicionarLinha(
                dados,
                "Preço unitário:",
                lblVendaPreco,
                4
        );

        VBox painel =
                new VBox(
                        15,
                        titulo,
                        dados
                );

        painel.setPadding(
                new Insets(15)
        );

        painel.setPrefWidth(400);

        return painel;
    }

    // ============================================================
    // AUXILIAR PARA LINHAS
    // ============================================================

    private void adicionarLinha(
            GridPane grid,
            String texto,
            Label valor,
            int linha) {

        Label label =
                new Label(texto);

        label.setStyle(
                "-fx-font-weight: bold;"
        );

        grid.add(
                label,
                0,
                linha
        );

        grid.add(
                valor,
                1,
                linha
        );
    }

    // ============================================================
    // GETTERS
    // ============================================================

    public TextField getTxtCodigoCompra() {
        return txtCodigoCompra;
    }

    public TextField getTxtCodigoVenda() {
        return txtCodigoVenda;
    }

    public Button getBtPesquisar() {
        return btPesquisar;
    }

    public Button getBtFechar() {
        return btFechar;
    }

    public Label getLblCompraCodigo() {
        return lblCompraCodigo;
    }

    public Label getLblCompraDescricao() {
        return lblCompraDescricao;
    }

    public Label getLblCompraUnidade() {
        return lblCompraUnidade;
    }

    public Label getLblCompraQuantidade() {
        return lblCompraQuantidade;
    }

    public Label getLblCompraPreco() {
        return lblCompraPreco;
    }

    public Label getLblVendaCodigo() {
        return lblVendaCodigo;
    }

    public Label getLblVendaDescricao() {
        return lblVendaDescricao;
    }

    public Label getLblVendaUnidade() {
        return lblVendaUnidade;
    }

    public Label getLblVendaQuantidade() {
        return lblVendaQuantidade;
    }

    public Label getLblVendaPreco() {
        return lblVendaPreco;
    }

    public Label getLblResultado() {
        return lblResultado;
    }
}