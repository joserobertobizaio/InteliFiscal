package br.com.intelifiscal.fx.navigation;

import br.com.intelifiscal.fx.view.dashboard.DashboardView;
import br.com.intelifiscal.fx.view.estabelecimento.EstabelecimentoView;
import br.com.intelifiscal.fx.controller.estabelecimento.EstabelecimentoController;
import br.com.intelifiscal.fx.controller.produto.ProdutoController;
import br.com.intelifiscal.fx.view.produto.ProdutoView;

import br.com.intelifiscal.fx.view.compra.AnaliseComprasView;
import br.com.intelifiscal.fx.controller.compra.AnaliseComprasController;
import br.com.intelifiscal.fx.view.relatorio.TopClientesView;
import br.com.intelifiscal.fx.controller.relatorio.TopClientesController;

import br.com.intelifiscal.fx.view.relatorio.TopProdutosView;
import br.com.intelifiscal.fx.controller.relatorio.TopProdutosController;
import br.com.intelifiscal.fx.controller.venda.AnaliseVendasController;
import br.com.intelifiscal.fx.view.relatorio.ResumoVendasView;
import br.com.intelifiscal.fx.controller.relatorio.ResumoVendasController;
import br.com.intelifiscal.fx.controller.dashboard.DashboardController;
import br.com.intelifiscal.fx.view.relatorio.RelatoriosView;
import br.com.intelifiscal.fx.controller.relatorio.RelatoriosController;

import br.com.intelifiscal.fx.view.produto.CompararCompraVendaView;
import br.com.intelifiscal.fx.controller.produto.CompararCompraVendaController;

import br.com.intelifiscal.fx.view.relatorio.ResumoComprasView;
import br.com.intelifiscal.fx.controller.relatorio.ResumoComprasController;

import br.com.intelifiscal.fx.view.periodo.ResumoPeriodoView;
import br.com.intelifiscal.fx.controller.periodo.ResumoPeriodoController;

import br.com.intelifiscal.fx.view.compra.CompraView;
import br.com.intelifiscal.fx.controller.compra.CompraController;
import br.com.intelifiscal.fx.view.venda.AnaliseVendasView;
import br.com.intelifiscal.fx.view.venda.VendaView;
import br.com.intelifiscal.fx.controller.venda.VendaController;

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

            case DASHBOARD -> {
                DashboardView view = new DashboardView();
                new DashboardController(view);
                yield view;
            }

            case TOP_CLIENTES -> {

                TopClientesView view =
                        new TopClientesView();

                new TopClientesController(view);

                yield view;
            }

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

            case PRODUTOS -> {

                ProdutoView view =
                        new ProdutoView();

                new ProdutoController(view);

                yield view;
            }

            case COMPRAS -> {

                CompraView view =
                        new CompraView();

                new CompraController(view);

                yield view;
            }

            case VENDAS -> {

                VendaView view =
                        new VendaView();

                new VendaController(view);

                yield view;
            }

            case COMPARAR_COMPRA_VENDA -> {

                CompararCompraVendaView view =
                        new CompararCompraVendaView();

                new CompararCompraVendaController(view);

                yield view;
            }


            case RESUMO_PERIODO -> {

                ResumoPeriodoView view =
                        new ResumoPeriodoView();

                new ResumoPeriodoController(view);

                yield view;
            }

            case RESUMO_COMPRAS -> {

                ResumoComprasView view =
                        new ResumoComprasView();

                new ResumoComprasController(view);

                yield view;
            }

            case ANALISE_COMPRAS -> {

                AnaliseComprasView view =
                        new AnaliseComprasView();

                new AnaliseComprasController(view);

                yield view;
            }

            case ANALISE_VENDAS -> {
                AnaliseVendasView view =
                        new AnaliseVendasView();

                new AnaliseVendasController(view);

                yield view;
            }

            case RESUMO_VENDAS -> {

                ResumoVendasView view =
                        new ResumoVendasView();

                new ResumoVendasController(view);

                yield view;
            }


            case TOP_PRODUTOS -> {

                TopProdutosView view =
                        new TopProdutosView();

                new TopProdutosController(view);

                yield view;
            }

            case RELATORIOS -> {

                RelatoriosView view =
                        new RelatoriosView();

                new RelatoriosController(view);

                yield view;
            }

            default ->
                    throw new IllegalArgumentException(
                            "View não implementada para: " + screenType
                    );
        };
    }

}