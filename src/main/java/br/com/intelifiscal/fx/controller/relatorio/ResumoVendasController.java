package br.com.intelifiscal.fx.controller.relatorio;

import br.com.intelifiscal.dto.relatorio.ClienteVendaDTO;
import br.com.intelifiscal.dto.relatorio.ResumoVendasDTO;
import br.com.intelifiscal.fx.navigation.NavigationManager;
import br.com.intelifiscal.fx.navigation.ScreenType;
import br.com.intelifiscal.fx.view.relatorio.ResumoVendasView;
import br.com.intelifiscal.service.relatorio.ResumoVendasService;
import br.com.intelifiscal.dto.venda.ResumoVendaDTO;
import br.com.intelifiscal.dto.relatorio.DetalhamentoVendaDTO;
import br.com.intelifiscal.service.relatorio.exportacao.ExcelRelatorioService;
import br.com.intelifiscal.service.relatorio.exportacao.PdfRelatorioService;
import br.com.intelifiscal.util.Mensagem;
import javafx.stage.Window;

import javafx.stage.FileChooser;

import java.io.File;

import java.time.LocalDate;
import java.util.List;

public class ResumoVendasController {

    private final ResumoVendasView view;
    private final ResumoVendasService service;

    private final ExcelRelatorioService excelService =
            new ExcelRelatorioService();

    private final PdfRelatorioService pdfService =
            new PdfRelatorioService();

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

        view.getBtExcel().setOnAction(
                e -> exportarExcel()
        );

        view.getCbPeriodo().setOnAction(
                e -> ajustarPeriodo()
        );

        view.getMiRelatorioSintetico().setOnAction(
                e -> gerarRelatorioSintetico()
        );

        view.getMiRelatorioAnalitico().setOnAction(
                e -> gerarRelatorioAnalitico()
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
    // RELATÓRIO SINTÉTICO DE VENDAS
    //==================================================

    private void gerarRelatorioSintetico() {

        ClienteVendaDTO cliente =
                view.getTabelaClientes()
                        .getSelectionModel()
                        .getSelectedItem();

        if (cliente == null) {

            Mensagem.aviso(
                    "Selecione um cliente para gerar o relatório."
            );

            return;
        }

        LocalDate dataInicio =
                view.getDtInicio().getValue();

        LocalDate dataFim =
                view.getDtFim().getValue();

        if (dataFim == null) {

            Mensagem.aviso(
                    "Informe a data final do período."
            );

            return;
        }

        List<DetalhamentoVendaDTO> dados =
                service.consultarDetalhamentoPorCliente(
                        dataInicio,
                        dataFim,
                        cliente.getCnpj()
                );

        if (dados == null || dados.isEmpty()) {

            Mensagem.aviso(
                    "Não existem vendas para o cliente selecionado no período."
            );

            return;
        }

        FileChooser chooser =
                new FileChooser();

        chooser.setTitle(
                "Salvar Relatório Sintético de Vendas"
        );

        chooser.setInitialFileName(
                "Relatorio_Sintetico_Vendas_"
                        + cliente.getCliente().replace("/", "_")
                        + ".pdf"
        );

        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Arquivo PDF (*.pdf)",
                        "*.pdf"
                )
        );

        Window window =
                view.getScene().getWindow();

        File arquivo =
                chooser.showSaveDialog(window);

        if (arquivo == null) {
            return;
        }

        //==================================================
        // PERÍODO DO RELATÓRIO
        //==================================================

        String periodoRelatorio =
                obterPeriodoRelatorio(
                        dataInicio,
                        dataFim
                );

        pdfService.gerarRelatorioSinteticoVendas(
                dados,
                arquivo.toPath(),
                periodoRelatorio
        );

        Mensagem.aviso(
                "O Relatório Sintético de Vendas foi gerado com sucesso!"
        );
    }


        //==================================================
        // RELATÓRIO ANALÍTICO DE VENDAS
        //==================================================

    private void gerarRelatorioAnalitico() {

        ClienteVendaDTO cliente =
                view.getTabelaClientes()
                        .getSelectionModel()
                        .getSelectedItem();

        if (cliente == null) {

            Mensagem.aviso(
                    "Selecione um cliente para gerar o relatório."
            );

            return;
        }

        LocalDate dataInicio =
                view.getDtInicio().getValue();

        LocalDate dataFim =
                view.getDtFim().getValue();

        if (dataFim == null) {

            Mensagem.aviso(
                    "Informe a data final do período."
            );

            return;
        }

        //==================================================
        // CONSULTAR DADOS
        //==================================================

        List<DetalhamentoVendaDTO> dados =
                service.consultarDetalhamentoPorCliente(
                        dataInicio,
                        dataFim,
                        cliente.getCnpj()
                );

        if (dados == null || dados.isEmpty()) {

            Mensagem.aviso(
                    "Não existem vendas para o cliente selecionado no período."
            );

            return;
        }

        //==================================================
        // ESCOLHER ARQUIVO
        //==================================================

        FileChooser chooser =
                new FileChooser();

        chooser.setTitle(
                "Salvar Relatório Analítico de Vendas"
        );

        chooser.setInitialFileName(
                "Relatorio_Analitico_Vendas_"
                        + cliente.getCliente()
                        .replace("/", "_")
                        + ".pdf"
        );

        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Arquivo PDF (*.pdf)",
                        "*.pdf"
                )
        );

        Window window =
                view.getScene().getWindow();

        File arquivo =
                chooser.showSaveDialog(window);

        if (arquivo == null) {
            return;
        }

        //==================================================
        // PERÍODO DO RELATÓRIO
        //==================================================

        String periodoRelatorio =
                obterPeriodoRelatorio(
                        dataInicio,
                        dataFim
                );

        //==================================================
        // GERAR PDF
        //==================================================

        try {

            pdfService.gerarRelatorioAnaliticoVendas(
                    dados,
                    arquivo.toPath(),
                    periodoRelatorio
            );

            Mensagem.sucesso(
                    "O Relatório Analítico de Vendas foi gerado com sucesso."
            );

        } catch (Exception e) {

            e.printStackTrace();

            Mensagem.erro(
                    "Não foi possível gerar o relatório analítico de vendas.\n\n"
                            + e.getMessage()
            );
        }
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
    // EXPORTAR EXCEL
    //==================================================

    private void exportarExcel() {

        LocalDate dataInicio =
                view.getDtInicio().getValue();

        LocalDate dataFim =
                view.getDtFim().getValue();


        //==================================================
        // CONSULTAR DADOS
        //==================================================

        List<ResumoVendaDTO> dados =
                service.consultarVendasParaExportacao(
                        dataInicio,
                        dataFim
                );


        if (dados == null || dados.isEmpty()) {

            Mensagem.aviso(
                    "Não existem vendas no período selecionado."
            );

            return;
        }


        //==================================================
        // ESCOLHER ARQUIVO
        //==================================================

        FileChooser fileChooser =
                new FileChooser();

        fileChooser.setTitle(
                "Salvar Resumo de Vendas"
        );

        fileChooser.setInitialFileName(
                "Resumo_Vendas.xlsx"
        );

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Arquivo Excel (*.xlsx)",
                        "*.xlsx"
                )
        );


        File arquivo =
                fileChooser.showSaveDialog(
                        view.getScene().getWindow()
                );


        // Usuário cancelou
        if (arquivo == null) {
            return;
        }


        //==================================================
        // GARANTIR EXTENSÃO
        //==================================================

        String caminho =
                arquivo.getAbsolutePath();

        if (!caminho
                .toLowerCase()
                .endsWith(".xlsx")) {

            caminho += ".xlsx";
        }


        //==================================================
        // GERAR EXCEL
        //==================================================

        try {

            excelService.gerarResumoVendas(
                    dados,
                    caminho
            );

            Mensagem.sucesso(
                    "Resumo de Vendas exportado com sucesso."
            );

        } catch (Exception e) {

            e.printStackTrace();

            Mensagem.erro(
                    "Não foi possível gerar o relatório de vendas.\n\n"
                            + e.getMessage()
            );
        }
    }

    //==================================================
    // PERÍODO DO RELATÓRIO
    //==================================================

    private String obterPeriodoRelatorio(
            LocalDate dataInicio,
            LocalDate dataFim
    ) {

        String periodo =
                view.getCbPeriodo().getValue();

        //==================================================
        // DESDE O INÍCIO
        //==================================================

        if ("Desde o início".equals(periodo)) {

            return "DESDE O INÍCIO";
        }

        //==================================================
        // PERÍODO COM DATAS
        //==================================================

        if (dataInicio != null &&
                dataFim != null) {

            java.time.format.DateTimeFormatter formato =
                    java.time.format.DateTimeFormatter.ofPattern(
                            "dd/MM/yyyy"
                    );

            return "DE "
                    + dataInicio.format(formato)
                    + " ATÉ "
                    + dataFim.format(formato);
        }

        //==================================================
        // SEGURANÇA
        //==================================================

        return "";
    }
}