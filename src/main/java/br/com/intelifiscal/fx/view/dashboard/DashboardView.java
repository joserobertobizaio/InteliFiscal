package br.com.intelifiscal.fx.view.dashboard;

import br.com.intelifiscal.fx.view.base.BaseView;
import br.com.intelifiscal.fx.components.common.Card;
import br.com.intelifiscal.fx.components.dashboard.MetricCard;
import br.com.intelifiscal.fx.components.common.icons.IconType;
import br.com.intelifiscal.fx.controller.dashboard.DashboardController;
import javafx.geometry.HPos;
import javafx.scene.Cursor;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class DashboardView extends BaseView {

    // ============================================================
    // LINKS - CONSULTAS RÁPIDAS
    // ============================================================

    private final Label comprasLink =
            new Label("Consultar →");

    private final Label vendasLink =
            new Label("Consultar →");

    private final Label produtosLink =
            new Label("Consultar →");

    private final Label periodoLink =
            new Label("Consultar →");

    private MetricCard produtosCard;

    private MetricCard clientesCard;

    private MetricCard comprasCard;

    private MetricCard vendasCard;

    public DashboardView() {

        super(
                "Dashboard",
                "Bem-vindo ao InteliFiscal"
        );

        initialize();
    }

    private void initialize() {

        // ============================================================
        // TEXTO CENTRAL ATUAL
        // ============================================================

        Label lblIcon = new Label("🚧");
        lblIcon.getStyleClass().add("dashboard-icon");

        Label lblTitulo = new Label("Dashboard em construção");
        lblTitulo.getStyleClass().add("dashboard-title");

        Label lblDescricao = new Label(
                "Os indicadores, gráficos e análises serão exibidos\n"
                        + "após a importação das Notas Fiscais Eletrônicas."
        );

        lblDescricao.getStyleClass().add("dashboard-description");

        VBox conteudo = new VBox(18);

        conteudo.setAlignment(Pos.CENTER);

        conteudo.getChildren().addAll(
                lblIcon,
                lblTitulo,
                lblDescricao
        );

        // ============================================================
        // CARDS DOS INDICADORES
        // ============================================================

        GridPane indicadores = new GridPane();

        indicadores.setAlignment(Pos.CENTER);

        indicadores.setHgap(30);
        indicadores.setVgap(20);

        clientesCard =
                new MetricCard(
                        IconType.IMPORTACAO_XML,
                        "Clientes -> Consulte aqui",
                        "os maiores clientes."
                );

        clientesCard.setCursor(Cursor.HAND);

        comprasCard =
                new MetricCard(
                        IconType.COMPRAS,
                        "Compras -> Consulte aqui",
                        "a análise detalhada."
                );

        comprasCard.setCursor(Cursor.HAND);


        vendasCard =
                new MetricCard(
                        IconType.VENDAS,
                        "Vendas -> Consulte aqui",
                        "a análise detalhada."
                );

        vendasCard.setCursor(Cursor.HAND);

        produtosCard =
                new MetricCard(
                        IconType.PRODUTOS,
                        "Produtos -> Consulte aqui",
                        "os produtos mais vendidos."
                );

        produtosCard.setCursor(Cursor.HAND);

        indicadores.add(clientesCard, 0, 0);
        indicadores.add(comprasCard, 1, 0);
        indicadores.add(vendasCard, 2, 0);
        indicadores.add(produtosCard, 3, 0);

        GridPane.setHalignment(clientesCard, HPos.CENTER);
        GridPane.setHalignment(comprasCard, HPos.CENTER);
        GridPane.setHalignment(vendasCard, HPos.CENTER);
        GridPane.setHalignment(produtosCard, HPos.CENTER);

        StackPane faixaIndicadores = new StackPane(indicadores);

        faixaIndicadores.setAlignment(Pos.CENTER);

        faixaIndicadores.setPadding(
                new Insets(10, 0, 10, 0)
        );

        // ============================================================
        // CONSULTAS RÁPIDAS
        // ============================================================

        Label lblConsultas = new Label("Consultas rápidas");

        lblConsultas.setStyle(
                "-fx-font-size: 20px;"
                        + "-fx-font-weight: bold;"
        );

        Label lblSubtituloConsultas = new Label(
                "Acesse rapidamente as principais informações do sistema."
        );

        lblSubtituloConsultas.setStyle(
                "-fx-font-size: 13px;"
        );

        VBox tituloConsultas = new VBox(5);

        tituloConsultas.setAlignment(Pos.CENTER_LEFT);

        tituloConsultas.getChildren().addAll(
                lblConsultas,
                lblSubtituloConsultas
        );

        // ------------------------------------------------------------
        // CONSULTA 1 - COMPRAS
        // ------------------------------------------------------------

        Label comprasTitulo = new Label("📊  Resumo de Compras");

        comprasTitulo.setStyle(
                "-fx-font-size: 15px;"
                        + "-fx-font-weight: bold;"
        );

        comprasLink.setStyle(
                "-fx-font-size: 13px;"
                        + "-fx-cursor: hand;"
        );

        VBox consultaCompras = new VBox(8);

        consultaCompras.setPadding(new Insets(15));

        consultaCompras.setPrefWidth(300);

        consultaCompras.getChildren().addAll(
                comprasTitulo,
                comprasLink
        );

        consultaCompras.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 10;"
                        + "-fx-border-color: #e0e0e0;"
                        + "-fx-border-radius: 10;"
                        + "-fx-border-width: 1;"
        );

        // ------------------------------------------------------------
        // CONSULTA 2 - VENDAS
        // ------------------------------------------------------------

        Label vendasTitulo = new Label("📈  Resumo de Vendas");

        vendasTitulo.setStyle(
                "-fx-font-size: 15px;"
                        + "-fx-font-weight: bold;"
        );

        vendasLink.setStyle(
                "-fx-font-size: 13px;"
                        + "-fx-cursor: hand;"
        );

        VBox consultaVendas = new VBox(8);

        consultaVendas.setPadding(new Insets(15));

        consultaVendas.setPrefWidth(300);

        consultaVendas.getChildren().addAll(
                vendasTitulo,
                vendasLink
        );

        consultaVendas.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 10;"
                        + "-fx-border-color: #e0e0e0;"
                        + "-fx-border-radius: 10;"
                        + "-fx-border-width: 1;"
        );

        // ------------------------------------------------------------
        // CONSULTA 3 - PRODUTOS
        // ------------------------------------------------------------

        Label produtosTitulo = new Label("📦  Histórico de Produto");

        produtosTitulo.setStyle(
                "-fx-font-size: 15px;"
                        + "-fx-font-weight: bold;"
        );

        produtosLink.setStyle(
                "-fx-font-size: 13px;"
                        + "-fx-cursor: hand;"
        );

        VBox consultaProdutos = new VBox(8);

        consultaProdutos.setPadding(new Insets(15));

        consultaProdutos.setPrefWidth(300);

        consultaProdutos.getChildren().addAll(
                produtosTitulo,
                produtosLink
        );

        consultaProdutos.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 10;"
                        + "-fx-border-color: #e0e0e0;"
                        + "-fx-border-radius: 10;"
                        + "-fx-border-width: 1;"
        );

        // ------------------------------------------------------------
        // CONSULTA 4 - ÚLTIMOS 12 MESES
        // ------------------------------------------------------------

        Label periodoTitulo = new Label("📅  Últimos 12 meses");

        periodoTitulo.setStyle(
                "-fx-font-size: 15px;"
                        + "-fx-font-weight: bold;"
        );

        periodoLink.setStyle(
                "-fx-font-size: 13px;"
                        + "-fx-cursor: hand;"
        );

        VBox consultaPeriodo = new VBox(8);

        consultaPeriodo.setPadding(new Insets(15));

        consultaPeriodo.setPrefWidth(300);

        consultaPeriodo.getChildren().addAll(
                periodoTitulo,
                periodoLink
        );

        consultaPeriodo.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 10;"
                        + "-fx-border-color: #e0e0e0;"
                        + "-fx-border-radius: 10;"
                        + "-fx-border-width: 1;"
        );

        // ============================================================
        // GRID DAS CONSULTAS
        // ============================================================

        GridPane consultasGrid = new GridPane();

        consultasGrid.setAlignment(Pos.CENTER);

        consultasGrid.setHgap(20);

        consultasGrid.setVgap(15);

        consultasGrid.add(consultaCompras, 0, 0);
        consultasGrid.add(consultaVendas, 1, 0);

        consultasGrid.add(consultaProdutos, 0, 1);
        consultasGrid.add(consultaPeriodo, 1, 1);

        // ============================================================
        // PAINEL DE CONSULTAS
        // ============================================================

        VBox painelConsultas = new VBox(15);

        painelConsultas.setAlignment(Pos.CENTER);

        painelConsultas.setPadding(
                new Insets(20)
        );

        painelConsultas.getChildren().addAll(
                tituloConsultas,
                consultasGrid
        );

        Card cardConsultas = new Card(painelConsultas);

        cardConsultas.setWidthPercentage(0.75);

        cardConsultas.setMaxContentWidth(850);

        // ============================================================
        // PAINEL PRINCIPAL
        // ============================================================

        VBox painel = new VBox(20);

        painel.setAlignment(Pos.TOP_CENTER);

        painel.setPadding(new Insets(10));

        painel.getChildren().addAll(
                faixaIndicadores,
                cardConsultas
        );

        setContent(painel);

        new DashboardController(this);
    }

    //==================================================
    // GETTERS - CONSULTAS RÁPIDAS
    //==================================================

    public Label getComprasLink() {

        return comprasLink;
    }

    public MetricCard getClientesCard() {
        return clientesCard;
    }

    public MetricCard getComprasCard() {
        return comprasCard;
    }

    public MetricCard getVendasCard() {
        return vendasCard;
    }


    public Label getVendasLink() {

        return vendasLink;
    }


    public Label getProdutosLink() {

        return produtosLink;
    }


    public Label getPeriodoLink() {

        return periodoLink;
    }

    public MetricCard getProdutosCard() {
        return produtosCard;
    }

}