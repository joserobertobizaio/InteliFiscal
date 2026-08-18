package br.com.intelifiscal.fx.controller.relatorio;

import br.com.intelifiscal.dto.relatorio.ClienteVendaDTO;
import br.com.intelifiscal.dto.relatorio.ResumoVendasDTO;
import br.com.intelifiscal.fx.view.relatorio.ResumoVendasView;
import br.com.intelifiscal.service.relatorio.ResumoVendasService;

import java.time.LocalDate;
import java.util.List;

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

        LocalDate hoje =
                LocalDate.now();

        LocalDate inicio =
                hoje.minusMonths(12);

        view.getDtInicio().setValue(inicio);

        view.getDtFim().setValue(hoje);

        carregarResumo(
                inicio,
                hoje
        );

        carregarClientes(
                inicio,
                hoje
        );
    }


    //==================================================
    // CARREGAR RESUMO
    //==================================================

    private void carregarResumo(
            LocalDate dataInicio,
            LocalDate dataFim
    ) {

        ResumoVendasDTO dto =
                service.consultarResumo(
                        dataInicio,
                        dataFim
                );

        view.atualizarResumo(dto);
    }


    //==================================================
    // CARREGAR CLIENTES
    //==================================================

    private void carregarClientes(
            LocalDate dataInicio,
            LocalDate dataFim
    ) {

        List<ClienteVendaDTO> lista =
                service.consultarPorCliente(
                        dataInicio,
                        dataFim
                );

        view.atualizarClientes(lista);
    }


    //==================================================
    // EVENTOS
    //==================================================

    private void configurarEventos() {

        view.getBtFechar().setOnAction(
                e -> fechar()
        );

        view.getBtConsultar().setOnAction(
                e -> consultarPorPeriodo()
        );

        view.getCbPeriodo().setOnAction(
                e -> ajustarPeriodo()
        );
    }


    //==================================================
    // AJUSTAR PERÍODO
    //==================================================

    private void ajustarPeriodo() {

        String periodo =
                view.getCbPeriodo().getValue();

        if (periodo == null) {
            return;
        }

        LocalDate hoje =
                LocalDate.now();


        switch (periodo) {

            case "Últimos 30 dias":

                view.getDtInicio().setValue(
                        hoje.minusDays(30)
                );

                view.getDtFim().setValue(
                        hoje
                );

                break;


            case "Últimos 3 meses":

                view.getDtInicio().setValue(
                        hoje.minusMonths(3)
                );

                view.getDtFim().setValue(
                        hoje
                );

                break;


            case "Últimos 6 meses":

                view.getDtInicio().setValue(
                        hoje.minusMonths(6)
                );

                view.getDtFim().setValue(
                        hoje
                );

                break;


            case "Últimos 12 meses":

                view.getDtInicio().setValue(
                        hoje.minusMonths(12)
                );

                view.getDtFim().setValue(
                        hoje
                );

                break;


            case "Últimos 24 meses":

                view.getDtInicio().setValue(
                        hoje.minusMonths(24)
                );

                view.getDtFim().setValue(
                        hoje
                );

                break;


            case "Desde o início":

                view.getDtInicio().setValue(
                        null
                );

                view.getDtFim().setValue(
                        hoje
                );

                break;


            case "Período personalizado":

                // O usuário escolhe as datas manualmente.

                break;
        }

        //==================================================
        // ATUALIZA APARÊNCIA DOS CONTROLES
        //==================================================

        view.atualizarControlesPeriodo();



        //==================================================
        // CONSULTA AUTOMÁTICA
        //==================================================

        if (!"Período personalizado".equals(periodo)) {

            consultarPorPeriodo();
        }
    }


    //==================================================
    // CONSULTAR POR PERÍODO
    //==================================================

    private void consultarPorPeriodo() {

        LocalDate dataInicio =
                view.getDtInicio().getValue();

        LocalDate dataFim =
                view.getDtFim().getValue();


        //==================================================
        // VALIDAÇÃO
        //==================================================

        if (dataInicio == null &&
                dataFim == null) {

            return;
        }


        if (dataInicio != null &&
                dataFim != null &&
                dataInicio.isAfter(dataFim)) {

            return;
        }


        //==================================================
        // CONSULTAR RESUMO
        //==================================================

        carregarResumo(
                dataInicio,
                dataFim
        );


        //==================================================
        // CONSULTAR CLIENTES
        //==================================================

        carregarClientes(
                dataInicio,
                dataFim
        );
    }


    //==================================================
    // FECHAR
    //==================================================

    private void fechar() {

        view.setVisible(false);
    }
}