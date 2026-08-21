package br.com.intelifiscal.fx.controller.relatorio;

import br.com.intelifiscal.dto.relatorio.ClienteVendaDTO;
import br.com.intelifiscal.fx.view.relatorio.TopClientesView;
import br.com.intelifiscal.service.relatorio.TopClientesService;

import java.util.List;

public class TopClientesController {

    private final TopClientesView view;

    private final TopClientesService service;

    public TopClientesController(
            TopClientesView view
    ) {

        this.view = view;

        this.service =
                new TopClientesService();

        inicializar();

        configurarEventos();
    }

    //==================================================
    // INICIALIZAÇÃO
    //==================================================

    private void inicializar() {

        carregarClientes(
                view.getCbLimite().getValue()
        );
    }

    //==================================================
    // EVENTOS
    //==================================================

    private void configurarEventos() {

        view.getBtConsultar()
                .setOnAction(
                        e ->
                                carregarClientes(
                                        view.getCbLimite()
                                                .getValue()
                                )
                );
    }

    //==================================================
    // CARREGAR CLIENTES
    //==================================================

    private void carregarClientes(
            int limite
    ) {

        try {

            List<ClienteVendaDTO> lista =
                    service.listarTopClientes(
                            limite
                    );

            view.exibirDados(lista);

        } catch (Exception e) {

            view.exibirErro(
                    e.getMessage()
            );
        }
    }
}