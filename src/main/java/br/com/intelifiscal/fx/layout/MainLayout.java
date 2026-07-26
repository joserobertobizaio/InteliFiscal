package br.com.intelifiscal.fx.layout;

import br.com.intelifiscal.fx.components.content.ContentPane;
import br.com.intelifiscal.fx.components.sidebar.SideBar;
import br.com.intelifiscal.fx.components.topbar.TopBar;
import br.com.intelifiscal.fx.navigation.NavigationManager;
import br.com.intelifiscal.fx.navigation.ScreenType;
import br.com.intelifiscal.fx.view.dashboard.DashboardView;
import javafx.scene.layout.BorderPane;

public class MainLayout extends BorderPane {

    private final ContentPane contentPane = new ContentPane();

    private final SideBar sideBar = new SideBar();

    public MainLayout() {
        initialize();
    }

    private void initialize() {

        setTop(new TopBar());

        setLeft(sideBar);

        setCenter(contentPane);

        NavigationManager.initialize(contentPane);

        sideBar.setOnScreenSelected(NavigationManager::show);

        NavigationManager.show(ScreenType.DASHBOARD);

        // Próximo passo:
        // setBottom(new StatusBar());
    }
}