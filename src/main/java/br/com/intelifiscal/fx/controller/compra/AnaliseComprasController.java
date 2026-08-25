package br.com.intelifiscal.fx.controller.compra;

import br.com.intelifiscal.dto.relatorio.AnaliseComprasDTO;
import br.com.intelifiscal.fx.navigation.NavigationManager;
import br.com.intelifiscal.fx.view.compra.AnaliseComprasView;
import br.com.intelifiscal.service.relatorio.AnaliseComprasService;

import java.time.LocalDate;
import java.util.List;

public class AnaliseComprasController {

    private final AnaliseComprasView view;

    private final AnaliseComprasService service =
            new AnaliseComprasService();


    public AnaliseComprasController(
            AnaliseComprasView view
    ) {

        this.view = view;

        inicializar();
    }


    //==================================================
    // INICIALIZAÇÃO
    //==================================================

    private void inicializar() {

        configurarPeriodo();

        configurarDatas();

        configurarEventos();

        atualizarPeriodo();
    }


    //==================================================
    // PERÍODO
    //==================================================

    private void configurarPeriodo() {

        view.getCbPeriodo().setOnAction(
                event -> atualizarPeriodo()
        );
    }


    private void atualizarPeriodo() {

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

                bloquearCalendarios();

                consultar();

                break;


            case "Últimos 3 meses":

                view.getDtInicio().setValue(
                        hoje.minusMonths(3)
                );

                view.getDtFim().setValue(
                        hoje
                );

                bloquearCalendarios();

                consultar();

                break;


            case "Últimos 6 meses":

                view.getDtInicio().setValue(
                        hoje.minusMonths(6)
                );

                view.getDtFim().setValue(
                        hoje
                );

                bloquearCalendarios();

                consultar();

                break;


            case "Últimos 12 meses":

                view.getDtInicio().setValue(
                        hoje.minusMonths(12)
                );

                view.getDtFim().setValue(
                        hoje
                );

                bloquearCalendarios();

                consultar();

                break;


            case "Personalizado":

                habilitarCalendarios();

                // Se já existirem duas datas,
                // consulta automaticamente.
                if (view.getDtInicio().getValue() != null
                        && view.getDtFim().getValue() != null) {

                    consultar();
                }

                break;
        }
    }


    //==================================================
    // CALENDÁRIOS
    //==================================================

    private void bloquearCalendarios() {

        view.getDtInicio().setDisable(true);

        view.getDtFim().setDisable(true);
    }


    private void habilitarCalendarios() {

        view.getDtInicio().setDisable(false);

        view.getDtFim().setDisable(false);
    }


    //==================================================
    // DATAS PERSONALIZADAS
    //==================================================

    private void configurarDatas() {

        view.getDtInicio()
                .valueProperty()
                .addListener(
                        (obs, antiga, nova) ->
                                consultarSePersonalizado()
                );


        view.getDtFim()
                .valueProperty()
                .addListener(
                        (obs, antiga, nova) ->
                                consultarSePersonalizado()
                );
    }


    private void consultarSePersonalizado() {

        String periodo =
                view.getCbPeriodo().getValue();

        if (!"Personalizado".equals(periodo)) {
            return;
        }

        if (view.getDtInicio().getValue() == null
                || view.getDtFim().getValue() == null) {

            return;
        }

        consultar();
    }


    //==================================================
    // EVENTOS
    //==================================================

    private void configurarEventos() {

        view.getBtFechar().setOnAction(
                event -> fechar()
        );
    }


    //==================================================
    // CONSULTAR
    //==================================================

    private void consultar() {

        LocalDate dataInicio =
                view.getDtInicio().getValue();

        LocalDate dataFim =
                view.getDtFim().getValue();


        if (dataInicio == null
                || dataFim == null) {

            view.getTabela()
                    .getItems()
                    .clear();

            return;
        }


        if (dataInicio.isAfter(dataFim)) {

            return;
        }


        List<AnaliseComprasDTO> lista =
                service.consultar(
                        dataInicio,
                        dataFim,
                        null,
                        null
                );


        view.getTabela()
                .getItems()
                .setAll(lista);
    }


    //==================================================
    // FECHAR
    //==================================================

    private void fechar() {

        NavigationManager.show(
                br.com.intelifiscal.fx.navigation.ScreenType.DASHBOARD
        );
    }
}