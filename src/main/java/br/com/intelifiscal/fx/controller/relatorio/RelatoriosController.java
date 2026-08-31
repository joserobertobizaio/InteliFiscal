package br.com.intelifiscal.fx.controller.relatorio;

import br.com.intelifiscal.fx.navigation.NavigationManager;
import br.com.intelifiscal.fx.navigation.ScreenType;
import br.com.intelifiscal.fx.view.relatorio.RelatoriosView;

public class RelatoriosController {

    private final RelatoriosView view;

    public RelatoriosController(RelatoriosView view) {

        this.view = view;

        inicializar();
    }

    private void inicializar() {

        configurarEventos();
    }

    private void configurarEventos() {

        view.getBtResumoPeriodo().setOnAction(
                e -> NavigationManager.show(
                        ScreenType.RESUMO_PERIODO
                )
        );

        view.getBtResumoCompras().setOnAction(
                e -> NavigationManager.show(
                        ScreenType.RESUMO_COMPRAS
                )
        );

        view.getBtResumoVendas().setOnAction(
                e -> NavigationManager.show(
                        ScreenType.RESUMO_VENDAS
                )
        );

        view.getBtHistoricoProduto().setOnAction(
                e -> NavigationManager.show(
                        ScreenType.HISTORICO_PRODUTO
                )
        );
    }
}