package br.com.intelifiscal.fx.navigation;

import br.com.intelifiscal.fx.view.dashboard.DashboardView;
import br.com.intelifiscal.fx.view.estabelecimento.EstabelecimentoView;
import br.com.intelifiscal.fx.controller.estabelecimento.EstabelecimentoController;
import javafx.scene.Node;
import br.com.intelifiscal.fx.view.importacao.ImportacaoXMLView;
import br.com.intelifiscal.fx.controller.importacao.ImportacaoXMLController;

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

            case IMPORTACAO_XML -> {

                ImportacaoXMLView view = new ImportacaoXMLView();

                new ImportacaoXMLController(view);

                yield view;

            }

            default ->
                    throw new IllegalArgumentException(
                            "View não implementada para: " + screenType
                    );
        };
    }

}