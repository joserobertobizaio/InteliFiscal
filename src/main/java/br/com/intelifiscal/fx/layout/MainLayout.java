package br.com.intelifiscal.fx.layout;

import br.com.intelifiscal.fx.components.sidebar.SideBar;
import br.com.intelifiscal.fx.components.topbar.TopBar;
import javafx.scene.layout.BorderPane;

/**
 * Layout principal da aplicação.
 *
 * Responsável por organizar:
 * - TopBar
 * - SideBar
 * - ContentPane
 * - StatusBar
 */
public class MainLayout extends BorderPane {

    public MainLayout() {
        initialize();
    }

    private void initialize() {

        setTop(new TopBar());

        setLeft(new SideBar());

        // Próximos passos:
        // setCenter(new ContentPane());
        // setBottom(new StatusBar());
    }
}