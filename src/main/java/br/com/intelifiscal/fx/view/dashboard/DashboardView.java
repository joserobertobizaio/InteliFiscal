package br.com.intelifiscal.fx.view.dashboard;

import br.com.intelifiscal.fx.view.base.BaseView;
import br.com.intelifiscal.fx.components.common.Card;
import br.com.intelifiscal.fx.components.dashboard.MetricCard;
import br.com.intelifiscal.fx.components.common.icons.IconType;
import javafx.geometry.HPos;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class DashboardView extends BaseView {

    public DashboardView() {

        super(
                "Dashboard",
                "Bem-vindo ao InteliFiscal"
        );

        initialize();
    }

    private void initialize() {

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

        GridPane indicadores = new GridPane();

        indicadores.setAlignment(Pos.CENTER);

        indicadores.setHgap(30);
        indicadores.setVgap(20);

        MetricCard xmlCard =
                new MetricCard(
                        IconType.IMPORTACAO_XML,
                        "XML",
                        "0"
                );

        MetricCard comprasCard =
                new MetricCard(
                        IconType.COMPRAS,
                        "Compras",
                        "R$ 0,00"
                );

        MetricCard vendasCard =
                new MetricCard(
                        IconType.VENDAS,
                        "Vendas",
                        "R$ 0,00"
                );

        MetricCard produtosCard =
                new MetricCard(
                        IconType.PRODUTOS,
                        "Produtos",
                        "0"
                );

        indicadores.add(xmlCard, 0, 0);

        indicadores.add(comprasCard, 1, 0);

        indicadores.add(vendasCard, 2, 0);

        indicadores.add(produtosCard, 3, 0);

        GridPane.setHalignment(xmlCard, HPos.CENTER);
        GridPane.setHalignment(comprasCard, HPos.CENTER);
        GridPane.setHalignment(vendasCard, HPos.CENTER);
        GridPane.setHalignment(produtosCard, HPos.CENTER);

        StackPane faixaIndicadores = new StackPane(indicadores);

        faixaIndicadores.setAlignment(Pos.CENTER);

        faixaIndicadores.setPadding(new Insets(10, 0, 20, 0));

        Card card = new Card(conteudo);

        card.setWidthPercentage(0.55);

        card.setMaxContentWidth(850);

        VBox painel = new VBox(30);

        painel.setAlignment(Pos.TOP_CENTER);

        painel.setPadding(new Insets(10));

        painel.getChildren().addAll(
                faixaIndicadores,
                card
        );

        setContent(painel);

    }
}