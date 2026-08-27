package br.com.intelifiscal.fx.controller.periodo;

import br.com.intelifiscal.dto.periodo.ResumoMensalDTO;
import br.com.intelifiscal.dto.periodo.ResumoPeriodoDTO;
import br.com.intelifiscal.fx.view.periodo.ResumoPeriodoView;
import br.com.intelifiscal.service.periodo.ResumoPeriodoService;
import br.com.intelifiscal.service.relatorio.exportacao.ExcelRelatorioService;
import br.com.intelifiscal.service.relatorio.exportacao.PdfRelatorioService;
import br.com.intelifiscal.util.Mensagem;
import javafx.stage.FileChooser;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.util.List;

public class ResumoPeriodoController {

    private final ResumoPeriodoView view;

    private final ResumoPeriodoService service =
            new ResumoPeriodoService();

    private final ExcelRelatorioService excelService =
            new ExcelRelatorioService();

    private final PdfRelatorioService pdfService =
            new PdfRelatorioService();


    //==================================================
    // DADOS DO RELATÓRIO
    //==================================================

    private List<ResumoPeriodoDTO> dadosPeriodo;

    private List<ResumoMensalDTO> dadosMensais;


    //==================================================
    // CONSTRUTOR
    //==================================================

    public ResumoPeriodoController(
            ResumoPeriodoView view
    ) {

        this.view = view;

        inicializar();
    }


    //==================================================
    // INICIALIZAÇÃO
    //==================================================

    private void inicializar() {

        carregarDados();

        configurarEventos();
    }


    //==================================================
    // CARREGAR DADOS
    //==================================================

    private void carregarDados() {

        dadosPeriodo =
                service.consultarUltimos12Meses();

        dadosMensais =
                service.consultarResumoMensal();

        view.atualizar(dadosPeriodo);

        view.atualizarGrafico(dadosMensais);
    }


    //==================================================
    // EVENTOS
    //==================================================

    private void configurarEventos() {

        view.getBtExcel().setOnAction(
                e -> exportarExcel()
        );

        view.getBtPdf().setOnAction(
                e -> exportarPdf()
        );

    }

    //==================================================
    // EXPORTAR EXCEL
    //==================================================

    private void exportarExcel() {

        if (dadosPeriodo == null || dadosMensais == null) {

            Mensagem.aviso(
                    "Não existem dados disponíveis para gerar o relatório."
            );

            return;
        }

        FileChooser fileChooser =
                new FileChooser();

        fileChooser.setTitle(
                "Salvar Relatório Excel"
        );

        fileChooser.setInitialFileName(
                "Relatorio_Resumo_12_Meses.xlsx"
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

        // Garante a extensão .xlsx
        String caminho =
                arquivo.getAbsolutePath();

        if (!caminho.toLowerCase().endsWith(".xlsx")) {

            caminho += ".xlsx";
        }

        try {

            excelService.gerarResumoPeriodo(
                    dadosPeriodo,
                    dadosMensais,
                    caminho
            );

            Mensagem.sucesso(
                    "Relatório Excel gerado com sucesso."
            );

        } catch (Exception e) {

            e.printStackTrace();

            Mensagem.erro(
                    "Não foi possível gerar o relatório Excel.\n\n"
                            + e.getMessage()
            );
        }
    }

    //==================================================
    // EXPORTAR PDF
    //==================================================

    private void exportarPdf() {

        if (dadosPeriodo == null || dadosMensais == null) {

            Mensagem.aviso(
                    "Não existem dados disponíveis para gerar o relatório."
            );

            return;
        }


        FileChooser fileChooser =
                new FileChooser();


        fileChooser.setTitle(
                "Salvar Relatório PDF"
        );


        fileChooser.setInitialFileName(
                "Relatorio_Resumo_12_Meses.pdf"
        );


        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Arquivo PDF (*.pdf)",
                        "*.pdf"
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
        // GARANTE A EXTENSÃO .PDF
        //==================================================

        String caminho =
                arquivo.getAbsolutePath();


        if (
                !caminho
                        .toLowerCase()
                        .endsWith(".pdf")
        ) {

            caminho += ".pdf";
        }


        try {

            pdfService.gerarResumoPeriodo(
                    dadosPeriodo,
                    dadosMensais,
                    new File(caminho).toPath()
            );


            Mensagem.sucesso(
                    "Relatório PDF gerado com sucesso."
            );


        } catch (Exception e) {

            e.printStackTrace();


            Mensagem.erro(
                    "Não foi possível gerar o relatório PDF.\n\n"
                            + e.getMessage()
            );
        }
    }


    //==================================================
    // GETTERS DOS DADOS
    //==================================================

    public List<ResumoPeriodoDTO> getDadosPeriodo() {

        return dadosPeriodo;
    }


    public List<ResumoMensalDTO> getDadosMensais() {

        return dadosMensais;
    }
}