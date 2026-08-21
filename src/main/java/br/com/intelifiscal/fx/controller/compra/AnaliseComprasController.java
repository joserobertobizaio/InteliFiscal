package br.com.intelifiscal.fx.controller.compra;

import br.com.intelifiscal.dto.relatorio.AnaliseComprasDTO;
import br.com.intelifiscal.fx.navigation.NavigationManager;
import br.com.intelifiscal.fx.view.compra.AnaliseComprasView;
import br.com.intelifiscal.service.relatorio.AnaliseComprasService;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class AnaliseComprasController {

    private final AnaliseComprasView view;

    private final AnaliseComprasService service =
            new AnaliseComprasService();

    public AnaliseComprasController(AnaliseComprasView view) {

        this.view = view;

        inicializar();
    }

    //==================================================
    // INICIALIZAÇÃO
    //==================================================

    private void inicializar() {

        configurarPeriodo();

        configurarEventos();
    }


    //==================================================
    // PERÍODO
    //==================================================

    private void configurarPeriodo() {

        view.getCbPeriodo().setOnAction(
                event -> atualizarPeriodo()
        );

        atualizarPeriodo();
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


            case "Personalizado":

                // O usuário informa as datas manualmente.
                break;
        }
    }


    //==================================================
    // EVENTOS
    //==================================================

    private void configurarEventos() {

        view.getBtConsultar().setOnAction(
                event -> consultar()
        );

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


        if (dataInicio == null || dataFim == null) {

            view.getLblTotalComprado().setText("R$ 0,00");
            view.getLblNotas().setText("0");
            view.getLblFornecedores().setText("0");
            view.getLblProdutos().setText("0");
            view.getLblTicketMedio().setText("R$ 0,00");

            view.getTabela().getItems().clear();

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


        atualizarIndicadores(lista);
    }

    private void atualizarIndicadores(
            List<AnaliseComprasDTO> lista
    ) {

        BigDecimal totalComprado =
                BigDecimal.ZERO;

        int notas = 0;

        double quantidade = 0;


        for (AnaliseComprasDTO dto : lista) {

            if (dto.getValorTotal() != null) {

                totalComprado =
                        totalComprado.add(
                                dto.getValorTotal()
                        );
            }

            notas += dto.getNotas();

            quantidade += dto.getQuantidade();
        }


        int fornecedores =
                lista.size();


        int produtos = lista.size();


        BigDecimal ticketMedio =
                BigDecimal.ZERO;

        if (notas > 0) {

            ticketMedio =
                    totalComprado.divide(
                            BigDecimal.valueOf(notas),
                            2,
                            java.math.RoundingMode.HALF_UP
                    );
        }


        NumberFormat moeda =
                NumberFormat.getCurrencyInstance(
                        new Locale("pt", "BR")
                );


        view.getLblTotalComprado()
                .setText(
                        moeda.format(totalComprado)
                );


        view.getLblNotas()
                .setText(
                        String.valueOf(notas)
                );


        view.getLblFornecedores()
                .setText(
                        String.valueOf(fornecedores)
                );


        view.getLblProdutos()
                .setText(
                        String.valueOf(produtos)
                );


        view.getLblTicketMedio()
                .setText(
                        moeda.format(ticketMedio)
                );
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