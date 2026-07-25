package br.com.intelifiscal.fx.components.sidebar;

import br.com.intelifiscal.fx.navigation.ScreenType;
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

        setSpacing(10);
        setPadding(new Insets(20));

        Label title = new Label("MENU");
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

        Region iconPlaceholder = new Region();
        iconPlaceholder.setPrefSize(18, 18);
        iconPlaceholder.setMinSize(18, 18);
        iconPlaceholder.setMaxSize(18, 18);

        return new SidebarItem(screenType, iconPlaceholder, text);
    }

}