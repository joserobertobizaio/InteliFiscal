package br.com.intelifiscal.fx.controller.relatorio;

import br.com.intelifiscal.dto.relatorio.FornecedorCompraDTO;
import br.com.intelifiscal.dto.relatorio.ResumoComprasDTO;
import br.com.intelifiscal.fx.view.relatorio.ResumoComprasView;
import br.com.intelifiscal.service.relatorio.ResumoComprasService;

import java.time.LocalDate;
import java.util.List;

public class ResumoComprasController {

    private final ResumoComprasView view;
    private final ResumoComprasService service;


    public ResumoComprasController(
            ResumoComprasView view
    ) {

        this.view = view;

        this.service =
                new ResumoComprasService();

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

        carregarFornecedores(
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

        ResumoComprasDTO dto =
                service.consultarResumo(
                        dataInicio,
                        dataFim
                );

        view.atualizarResumo(dto);
    }


    //==================================================
    // CARREGAR FORNECEDORES
    //==================================================

    private void carregarFornecedores(
            LocalDate dataInicio,
            LocalDate dataFim
    ) {

        List<FornecedorCompraDTO> lista =
                service.consultarPorFornecedor(
                        dataInicio,
                        dataFim
                );

        view.atualizarFornecedores(lista);
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

                view.getDtInicio().setValue(null);

                view.getDtFim().setValue(hoje);

                break;


            case "Período personalizado":

                // Usuário escolherá as datas.
                break;
        }


        //==================================================
        // ATUALIZA OS CONTROLES VISUAIS
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
        // DESDE O INÍCIO
        //==================================================

        if (view.getCbPeriodo().getValue()
                .equals("Desde o início")) {

            carregarResumo(
                    null,
                    dataFim
            );

            carregarFornecedores(
                    null,
                    dataFim
            );

            return;
        }


        //==================================================
        // VALIDAÇÃO
        //==================================================

        if (dataInicio == null || dataFim == null) {

            return;
        }


        if (dataInicio.isAfter(dataFim)) {

            return;
        }


        //==================================================
        // CONSULTA
        //==================================================

        carregarResumo(
                dataInicio,
                dataFim
        );

        carregarFornecedores(
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