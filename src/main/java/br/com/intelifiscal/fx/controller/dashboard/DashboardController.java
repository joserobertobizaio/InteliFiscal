package br.com.intelifiscal.fx.controller.dashboard;

import br.com.intelifiscal.fx.navigation.NavigationManager;
import br.com.intelifiscal.fx.navigation.ScreenType;
import br.com.intelifiscal.fx.view.dashboard.DashboardView;

public class DashboardController {

    private final DashboardView view;


    public DashboardController(
            DashboardView view
    ) {

        this.view = view;

        configurarEventos();
    }


    //==================================================
    // CONFIGURAÇÃO DOS EVENTOS
    //==================================================

    private void configurarEventos() {

        //==================================================
        // RESUMO DE COMPRAS
        //==================================================

        view.getComprasLink()
                .setOnMouseClicked(
                        event ->
                                NavigationManager.show(
                                        ScreenType.COMPRAS
                                )
                );


        //==================================================
        // RESUMO DE VENDAS
        //==================================================

        view.getVendasLink()
                .setOnMouseClicked(
                        event ->
                                NavigationManager.show(
                                        ScreenType.VENDAS
                                )
                );


        //==================================================
        // HISTÓRICO DE PRODUTO
        //==================================================

        view.getProdutosLink()
                .setOnMouseClicked(
                        event ->
                                NavigationManager.show(
                                        ScreenType.PRODUTOS
                                )
                );

        //==================================================
        // ÚLTIMOS 12 MESES
        //==================================================

        view.getPeriodoLink()
                .setOnMouseClicked(
                        event ->
                                NavigationManager.show(
                                        ScreenType.RESUMO_PERIODO
                                )
                );

    }

}