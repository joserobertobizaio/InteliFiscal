package br.com.intelifiscal.fx.controller.relatorio;

import br.com.intelifiscal.dto.relatorio.ResumoVendasDTO;
import br.com.intelifiscal.fx.view.relatorio.ResumoVendasView;
import br.com.intelifiscal.service.relatorio.ResumoVendasService;

public class ResumoVendasController {

    private final ResumoVendasView view;
    private final ResumoVendasService service;


    public ResumoVendasController(
            ResumoVendasView view
    ) {

        this.view = view;

        this.service =
                new ResumoVendasService();

        inicializar();

        configurarEventos();
    }


    //==================================================
    // INICIALIZAÇÃO
    //==================================================

    private void inicializar() {

        carregarResumo();
    }


    //==================================================
    // CARREGAR RESUMO
    //==================================================

    private void carregarResumo() {

        ResumoVendasDTO dto =
                service.consultarResumo();

        view.atualizarResumo(dto);
    }


    //==================================================
    // EVENTOS
    //==================================================

    private void configurarEventos() {

        view.getBtFechar().setOnAction(
                e -> fechar()
        );
    }


    //==================================================
    // FECHAR
    //==================================================

    private void fechar() {

        view.setVisible(false);
    }
}