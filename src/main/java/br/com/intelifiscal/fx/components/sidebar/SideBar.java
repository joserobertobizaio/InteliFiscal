package br.com.intelifiscal.fx.components.sidebar;

import br.com.intelifiscal.fx.navigation.ScreenType;
import br.com.intelifiscal.fx.components.common.icons.AppIcon;
import br.com.intelifiscal.fx.components.common.icons.IconType;
import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

/**
 * Menu lateral da aplicação.
 */
public class SideBar extends VBox {

    public SideBar() {
        initialize();
    }

    private void initialize() {

        getStyleClass().add("sidebar");

        setSpacing(14);
        setPadding(new Insets(20));

        setPrefWidth(225);
        setMinWidth(225);
        setMaxWidth(225);

        Label title = new Label("MENU PRINCIPAL");
        title.getStyleClass().add("sidebar-title");

        getChildren().add(title);

        createMenu();

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        getChildren().add(spacer);
    }

    private void createMenu() {

        getChildren().add(createItem(ScreenType.DASHBOARD, "Dashboard"));

        getChildren().add(createItem(ScreenType.ESTABELECIMENTO, "Estabelecimento"));

        getChildren().add(createItem(ScreenType.IMPORTACAO_XML, "Importação XML"));

        getChildren().add(createItem(ScreenType.COMPRAS, "Compras"));

        getChildren().add(createItem(ScreenType.VENDAS, "Vendas"));

        getChildren().add(createItem(ScreenType.PRODUTOS, "Produtos"));

        getChildren().add(createItem(ScreenType.RELATORIOS, "Relatórios"));

        getChildren().add(createItem(ScreenType.CONFIGURACOES, "Configurações"));
    }

    private SidebarItem createItem(ScreenType screenType, String text) {

        IconType iconType = switch (screenType) {

            case DASHBOARD -> IconType.DASHBOARD;

            case ESTABELECIMENTO -> IconType.ESTABELECIMENTO;

            case IMPORTACAO_XML -> IconType.IMPORTACAO_XML;

            case COMPRAS -> IconType.COMPRAS;

            case VENDAS -> IconType.VENDAS;

            case PRODUTOS -> IconType.PRODUTOS;

            case RELATORIOS -> IconType.RELATORIOS;

            case CONFIGURACOES -> IconType.CONFIGURACOES;
        };

        return new SidebarItem(
                screenType,
                new AppIcon(iconType),
                text
        );
    }

}