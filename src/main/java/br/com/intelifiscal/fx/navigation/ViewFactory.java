package br.com.intelifiscal.fx.navigation;

import br.com.intelifiscal.fx.view.dashboard.DashboardView;
import br.com.intelifiscal.fx.view.estabelecimento.EstabelecimentoView;
import br.com.intelifiscal.fx.controller.estabelecimento.EstabelecimentoController;
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

            case ESTABELECIMENTO -> {

                EstabelecimentoView view = new EstabelecimentoView();

                new EstabelecimentoController(view);

                yield view;

            }

                default ->
                    throw new IllegalArgumentException(
                            "View não implementada para: " + screenType
                    );
        };
    }

}