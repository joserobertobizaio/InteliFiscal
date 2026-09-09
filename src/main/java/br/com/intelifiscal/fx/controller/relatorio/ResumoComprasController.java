package br.com.intelifiscal.fx.controller.relatorio;

import br.com.intelifiscal.dto.relatorio.FornecedorCompraDTO;
import br.com.intelifiscal.dto.relatorio.ResumoComprasDTO;
import br.com.intelifiscal.fx.navigation.NavigationManager;
import br.com.intelifiscal.fx.navigation.ScreenType;
import br.com.intelifiscal.fx.view.relatorio.ResumoComprasView;
import br.com.intelifiscal.service.relatorio.ResumoComprasService;
import br.com.intelifiscal.dto.relatorio.DetalhamentoCompraDTO;
import br.com.intelifiscal.service.relatorio.DetalhamentoCompraService;
import br.com.intelifiscal.service.relatorio.exportacao.ExcelRelatorioService;
import br.com.intelifiscal.service.relatorio.exportacao.PdfRelatorioService;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class ResumoComprasController {

    private final ResumoComprasView view;
    private final ResumoComprasService service;
    private final DetalhamentoCompraService detalhamentoService;


    private final ExcelRelatorioService excelService =
            new ExcelRelatorioService();

    private final PdfRelatorioService pdfService =
            new PdfRelatorioService();

    public ResumoComprasController(
            ResumoComprasView view
    ) {

        this.view = view;

        this.service =
                new ResumoComprasService();

        this.detalhamentoService =
                new DetalhamentoCompraService();

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

        view.getBtExcel().setOnAction(
                e -> exportarExcel()
        );


        //==================================================
        // MENU DE CONTEXTO
        //==================================================

        view.getMiRelatorioSintetico().setOnAction(
                e -> {

                    gerarRelatorioSintetico();
                }
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
        // CONSULTAR FORNECEDORES
        //==================================================

        carregarFornecedores(
                dataInicio,
                dataFim
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
    // EXPORTAR EXCEL
    //==================================================

    private void exportarExcel() {

        LocalDate dataInicio =
                view.getDtInicio().getValue();

        LocalDate dataFim =
                view.getDtFim().getValue();

        List<FornecedorCompraDTO> lista =
                service.consultarPorFornecedor(
                        dataInicio,
                        dataFim
                );

        List<DetalhamentoCompraDTO> detalhes =
                detalhamentoService.listarPorPeriodo(
                        dataInicio,
                        dataFim
                );

        FileChooser chooser =
                new FileChooser();

        chooser.setTitle(
                "Salvar Resumo de Compras"
        );

        chooser.setInitialFileName(
                "Resumo_Compras.xlsx"
        );

        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Arquivo Excel (*.xlsx)",
                        "*.xlsx"
                )
        );

        Window window =
                view.getScene().getWindow();

        File arquivo =
                chooser.showSaveDialog(window);

        if (arquivo == null) {
            return;
        }

        ResumoComprasDTO resumo =
                service.consultarResumo(
                        dataInicio,
                        dataFim
                );

        excelService.gerarResumoCompras(
                resumo,
                lista,
                detalhes,
                dataInicio,
                dataFim,
                arquivo.getAbsolutePath()
        );

        javafx.scene.control.Alert alerta =
                new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.INFORMATION
                );

        alerta.setTitle("Exportação concluída");
        alerta.setHeaderText(null);
        alerta.setContentText(
                "O relatório de compras foi exportado com sucesso!"
        );

        alerta.showAndWait();
    }

    //==================================================
    // RELATÓRIO SINTÉTICO
    //==================================================

    private void gerarRelatorioSintetico() {

        FornecedorCompraDTO fornecedor =
                view.getTabelaFornecedores()
                        .getSelectionModel()
                        .getSelectedItem();

        if (fornecedor == null) {
            return;
        }

        LocalDate dataInicio =
                view.getDtInicio().getValue();

        LocalDate dataFim =
                view.getDtFim().getValue();

        if (dataFim == null) {
            return;
        }

        List<DetalhamentoCompraDTO> detalhes =
                detalhamentoService.listarPorFornecedor(
                        dataInicio,
                        dataFim,
                        fornecedor.getCnpj()
                );

        if (detalhes.isEmpty()) {

            javafx.scene.control.Alert alerta =
                    new javafx.scene.control.Alert(
                            javafx.scene.control.Alert.AlertType.WARNING
                    );

            alerta.setTitle("Relatório Sintético");
            alerta.setHeaderText(null);
            alerta.setContentText(
                    "Nenhuma compra foi encontrada para o fornecedor selecionado."
            );

            alerta.showAndWait();

            return;
        }

        FileChooser chooser =
                new FileChooser();

        chooser.setTitle(
                "Salvar Relatório Sintético"
        );

        chooser.setInitialFileName(
                "Relatorio_Sintetico_"
                        + fornecedor.getFornecedor()
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

        pdfService.gerarRelatorioSintetico(
                detalhes,
                arquivo.toPath(),
                periodoRelatorio
        );

        javafx.scene.control.Alert alerta =
                new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.INFORMATION
                );

        alerta.setTitle(
                "Relatório gerado"
        );

        alerta.setHeaderText(null);

        alerta.setContentText(
                "O Relatório Sintético foi gerado com sucesso!"
        );

        alerta.showAndWait();
    }


    //==================================================
    // RELATÓRIO ANALÍTICO
    //==================================================

    private void gerarRelatorioAnalitico() {

        FornecedorCompraDTO fornecedor =
                view.getTabelaFornecedores()
                        .getSelectionModel()
                        .getSelectedItem();

        if (fornecedor == null) {

            javafx.scene.control.Alert alerta =
                    new javafx.scene.control.Alert(
                            javafx.scene.control.Alert.AlertType.WARNING
                    );

            alerta.setTitle("Relatório Analítico");
            alerta.setHeaderText(null);
            alerta.setContentText(
                    "Nenhum fornecedor está selecionado."
            );

            alerta.showAndWait();

            return;
        }

        LocalDate dataInicio =
                view.getDtInicio().getValue();

        LocalDate dataFim =
                view.getDtFim().getValue();

        if (dataFim == null) {
            return;
        }

        //==================================================
        // BUSCAR DETALHAMENTO
        //==================================================

        List<DetalhamentoCompraDTO> detalhes =
                detalhamentoService.listarPorFornecedor(
                        dataInicio,
                        dataFim,
                        fornecedor.getCnpj()
                );

        if (detalhes.isEmpty()) {

            javafx.scene.control.Alert alerta =
                    new javafx.scene.control.Alert(
                            javafx.scene.control.Alert.AlertType.WARNING
                    );

            alerta.setTitle("Relatório Analítico");
            alerta.setHeaderText(null);
            alerta.setContentText(
                    "Nenhuma compra foi encontrada para o fornecedor selecionado."
            );

            alerta.showAndWait();

            return;
        }

        //==================================================
        // ESCOLHER LOCAL DO ARQUIVO
        //==================================================

        FileChooser chooser =
                new FileChooser();

        chooser.setTitle(
                "Salvar Relatório Analítico"
        );

        chooser.setInitialFileName(
                "Relatorio_Analitico_"
                        + fornecedor.getFornecedor()
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

        pdfService.gerarRelatorioAnalitico(
                detalhes,
                arquivo.toPath(),
                periodoRelatorio
        );

        //==================================================
        // MENSAGEM DE SUCESSO
        //==================================================

        javafx.scene.control.Alert alerta =
                new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.INFORMATION
                );

        alerta.setTitle(
                "Relatório gerado"
        );

        alerta.setHeaderText(null);

        alerta.setContentText(
                "O Relatório Analítico foi gerado com sucesso!"
        );

        alerta.showAndWait();
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