package br.com.intelifiscal.fx.controller.relatorio;

import br.com.intelifiscal.dto.relatorio.TopProdutosDTO;
import br.com.intelifiscal.fx.view.relatorio.TopProdutosView;
import br.com.intelifiscal.service.relatorio.TopProdutosService;

import java.util.List;

public class TopProdutosController {

    private final TopProdutosView view;
    private final TopProdutosService service;

    public TopProdutosController(TopProdutosView view) {

        this.view = view;
        this.service = new TopProdutosService();

        inicializar();
    }

    private void inicializar() {

        configurarEventos();

        carregarDados();
    }

    private void configurarEventos() {

        view.getBtConsultar().setOnAction(
                e -> carregarDados()
        );
    }

    private void carregarDados() {

        try {

            int limite =
                    view.getCbLimite().getValue();

            List<TopProdutosDTO> lista =
                    service.listarTopProdutos(limite);

            view.exibirDados(lista);

        } catch (Exception e) {

            view.exibirErro(
                    e.getMessage()
            );
        }
    }
}