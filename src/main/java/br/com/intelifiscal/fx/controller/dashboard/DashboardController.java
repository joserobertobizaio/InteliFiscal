package br.com.intelifiscal.fx.controller.dashboard;

import javafx.scene.input.MouseEvent;
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
        // CARD - MAIORES CLIENTES
        //==================================================

        view.getClientesCard()
                .setOnMouseClicked(
                        event ->
                                NavigationManager.show(
                                        ScreenType.TOP_CLIENTES
                                )
                );

        //==================================================
        // CARD - COMPRAS
        //==================================================

        view.getComprasCard()
                .setOnMouseClicked(
                        event ->
                                NavigationManager.show(
                                        ScreenType.ANALISE_COMPRAS
                                )
                );


        //==================================================
        // CARD - VENDAS
        //==================================================

        view.getVendasCard()
                .setOnMouseClicked(
                        event ->
                                NavigationManager.show(
                                        ScreenType.RESUMO_VENDAS
                                )
                );


        //==================================================
        // RESUMO DE COMPRAS
        //==================================================

        view.getComprasLink()
                .setOnMouseClicked(
                        event ->
                                NavigationManager.show(
                                        ScreenType.RESUMO_COMPRAS
                                )
                );

        //==================================================
        // RESUMO DE VENDAS
        //==================================================

        view.getVendasLink()
                .setOnMouseClicked(
                        event ->
                                NavigationManager.show(
                                        ScreenType.RESUMO_VENDAS
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

        //==================================================
        // CARD - PRODUTOS MAIS VENDIDOS
        //==================================================

        view.getProdutosCard()
                .addEventFilter(
                        MouseEvent.MOUSE_CLICKED,
                        event ->
                                NavigationManager.show(
                                        ScreenType.TOP_PRODUTOS
                                )
                );


    }

}