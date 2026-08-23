package br.com.intelifiscal.fx.controller.venda;

import br.com.intelifiscal.dto.relatorio.AnaliseVendasDTO;
import br.com.intelifiscal.fx.navigation.NavigationManager;
import br.com.intelifiscal.fx.navigation.ScreenType;
import br.com.intelifiscal.fx.view.venda.AnaliseVendasView;
import br.com.intelifiscal.service.relatorio.AnaliseVendasService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class AnaliseVendasController {

    private final AnaliseVendasView view;

    private final AnaliseVendasService service =
            new AnaliseVendasService();

    private final ObservableList<AnaliseVendasDTO> dados =
            FXCollections.observableArrayList();


    //==================================================
    // CONSTRUTOR
    //==================================================

    public AnaliseVendasController(
            AnaliseVendasView view
    ) {

        this.view = view;

        inicializar();
    }


    //==================================================
    // INICIALIZAÇÃO
    //==================================================

    private void inicializar() {

        configurarPeriodo();

        configurarEventos();

        view.getTabela()
                .setItems(dados);
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

                // Usuário informa as datas manualmente.
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

            limparDados();

            return;
        }


        if (dataInicio.isAfter(dataFim)) {

            limparDados();

            return;
        }


        List<AnaliseVendasDTO> lista =
                service.consultar(
                        dataInicio,
                        dataFim,
                        null,
                        null
                );


        dados.setAll(lista);

        atualizarIndicadores(lista);
    }


    //==================================================
    // LIMPAR DADOS
    //==================================================

    private void limparDados() {

        dados.clear();

        view.getLblTotalVendido()
                .setText("R$ 0,00");

        view.getLblNotas()
                .setText("0");

        view.getLblClientes()
                .setText("0");

        view.getLblProdutos()
                .setText("0");

        view.getLblTicketMedio()
                .setText("R$ 0,00");
    }


    //==================================================
    // INDICADORES
    //==================================================

    private void atualizarIndicadores(
            List<AnaliseVendasDTO> lista
    ) {

        BigDecimal totalVendido =
                BigDecimal.ZERO;

        int notas = 0;

        for (AnaliseVendasDTO dto : lista) {

            if (dto.getValorTotal() != null) {

                totalVendido =
                        totalVendido.add(
                                dto.getValorTotal()
                        );
            }

            notas += dto.getNotas();
        }


        //==================================================
        // CLIENTES
        //==================================================

        int clientes =
                lista.size();


        //==================================================
        // PRODUTOS
        //==================================================

        int produtos =
                lista.size();


        //==================================================
        // TICKET MÉDIO
        //==================================================

        BigDecimal ticketMedio =
                BigDecimal.ZERO;

        if (notas > 0) {

            ticketMedio =
                    totalVendido.divide(
                            BigDecimal.valueOf(notas),
                            2,
                            java.math.RoundingMode.HALF_UP
                    );
        }


        //==================================================
        // FORMATAÇÃO
        //==================================================

        NumberFormat moeda =
                NumberFormat.getCurrencyInstance(
                        new Locale("pt", "BR")
                );


        //==================================================
        // ATUALIZA CARDS
        //==================================================

        view.getLblTotalVendido()
                .setText(
                        moeda.format(totalVendido)
                );


        view.getLblNotas()
                .setText(
                        String.valueOf(notas)
                );


        view.getLblClientes()
                .setText(
                        String.valueOf(clientes)
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
                ScreenType.DASHBOARD
        );
    }


    //==================================================
    // GET VIEW
    //==================================================

    public AnaliseVendasView getView() {

        return view;
    }
}