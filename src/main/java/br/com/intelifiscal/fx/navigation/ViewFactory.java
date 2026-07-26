package br.com.intelifiscal.fx.navigation;

import br.com.intelifiscal.fx.view.dashboard.DashboardView;
import javafx.scene.Node;

/**
 * Responsável por criar as Views da aplicação.
 */
public final class ViewFactory {

    private ViewFactory() {
    }

    public static Node create(ScreenType screenType) {

        return switch (screenType) {

            case DASHBOARD -> new DashboardView();

            default ->
                    throw new IllegalArgumentException(
                            "View não implementada para: " + screenType
                    );
        };
    }

}