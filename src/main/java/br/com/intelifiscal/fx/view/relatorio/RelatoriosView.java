package br.com.intelifiscal.fx.view.relatorio;

import br.com.intelifiscal.fx.view.base.BaseView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class RelatoriosView extends BaseView {

    private final Button btResumoPeriodo =
            new Button("📊 Resumo dos últimos 12 meses");

    private final Button btResumoCompras =
            new Button("🛒 Resumo de Compras");

    private final Button btResumoVendas =
            new Button("💰 Resumo de Vendas");

    private final Button btHistoricoProduto =
            new Button("📦 Histórico de Produto");

    private final Button btCompararCompraVenda =
            new Button("🔄 Comparar Compra × Venda");

    public RelatoriosView() {

        super(
                "Relatórios Gerenciais",
                "Consulte os principais relatórios gerenciais do sistema."
        );

        initialize();
    }

    private void initialize() {

        Label titulo =
                new Label("Relatórios disponíveis");

        titulo.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;"
        );

        Label descricao =
                new Label(
                        "Selecione uma opção para consultar as informações."
                );

        descricao.setStyle(
                "-fx-font-size: 13px;"
        );

        VBox cabecalho =
                new VBox(
                        5,
                        titulo,
                        descricao
                );

        cabecalho.setAlignment(
                Pos.CENTER_LEFT
        );

        configurarBotao(btResumoPeriodo);
        configurarBotao(btResumoCompras);
        configurarBotao(btResumoVendas);
        configurarBotao(btHistoricoProduto);
        // Comparar Compra × Venda ficará oculto por enquanto
        btCompararCompraVenda.setVisible(false);
        btCompararCompraVenda.setManaged(false);

        GridPane grid =
                new GridPane();

        grid.setHgap(20);
        grid.setVgap(20);

        grid.add(btResumoPeriodo, 0, 0);
        grid.add(btResumoCompras, 1, 0);

        grid.add(btResumoVendas, 0, 1);
        grid.add(btHistoricoProduto, 1, 1);

        grid.add(
                btCompararCompraVenda,
                0,
                2,
                2,
                1
        );

        VBox conteudo =
                new VBox(
                        25,
                        cabecalho,
                        grid
                );

        conteudo.setPadding(
                new Insets(25)
        );

        conteudo.setAlignment(
                Pos.TOP_CENTER
        );

        BorderPane painel =
                new BorderPane();

        painel.setCenter(
                conteudo
        );

        setContent(painel);
    }

    private void configurarBotao(Button botao) {

        botao.setPrefWidth(300);
        botao.setPrefHeight(55);

        botao.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;"
        );
    }

    public Button getBtResumoPeriodo() {
        return btResumoPeriodo;
    }

    public Button getBtResumoCompras() {
        return btResumoCompras;
    }

    public Button getBtResumoVendas() {
        return btResumoVendas;
    }

    public Button getBtHistoricoProduto() {
        return btHistoricoProduto;
    }

    public Button getBtCompararCompraVenda() {
        return btCompararCompraVenda;
    }
}