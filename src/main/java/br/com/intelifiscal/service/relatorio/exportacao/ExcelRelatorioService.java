package br.com.intelifiscal.service.relatorio.exportacao;

import br.com.intelifiscal.dto.periodo.ResumoMensalDTO;
import br.com.intelifiscal.dto.periodo.ResumoPeriodoDTO;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

import br.com.intelifiscal.dto.relatorio.DetalhamentoCompraDTO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import br.com.intelifiscal.dto.relatorio.FornecedorCompraDTO;
import br.com.intelifiscal.dto.relatorio.ResumoComprasDTO;

import java.time.LocalDate;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import br.com.intelifiscal.dto.venda.ResumoVendaDTO;

import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.xddf.usermodel.chart.AxisCrosses;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.BarDirection;
import org.apache.poi.xddf.usermodel.chart.BarGrouping;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.XDDFBarChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryAxis;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFChartLegend;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFChart;


import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExcelRelatorioService {

    //==================================================
    // GERAR RELATÓRIO
    //==================================================

    public void gerarResumoPeriodo(
            List<ResumoPeriodoDTO> dadosPeriodo,
            List<ResumoMensalDTO> dadosMensais,
            String caminhoArquivo
    ) {

        if (dadosPeriodo == null) {
            throw new IllegalArgumentException(
                    "Os dados do período não podem ser nulos."
            );
        }

        if (dadosMensais == null) {
            throw new IllegalArgumentException(
                    "Os dados mensais não podem ser nulos."
            );
        }

        try (
                Workbook workbook = new XSSFWorkbook();
                FileOutputStream output =
                        new FileOutputStream(caminhoArquivo)
        ) {

            Sheet sheet =
                    workbook.createSheet(
                            "Resumo 12 Meses"
                    );

            //==================================================
            // ESTILOS
            //==================================================

            CellStyle estiloTitulo =
                    criarEstiloTitulo(workbook);

            CellStyle estiloSubtitulo =
                    criarEstiloSubtitulo(workbook);

            CellStyle estiloPeriodo =
                    criarEstiloPeriodo(workbook);

            CellStyle estiloSecao =
                    criarEstiloSecao(workbook);

            CellStyle estiloCabecalho =
                    criarEstiloCabecalho(workbook);

            CellStyle estiloTexto =
                    criarEstiloTexto(workbook);

            CellStyle estiloInteiro =
                    criarEstiloInteiro(workbook);

            CellStyle estiloQuantidade =
                    criarEstiloQuantidade(workbook);

            CellStyle estiloMoeda =
                    criarEstiloMoeda(workbook);

            CellStyle estiloDestaque =
                    criarEstiloDestaque(workbook);

            CellStyle estiloTotalMoeda =
                    criarEstiloTotalMoeda(workbook);

            //==================================================
            // TÍTULO
            //==================================================

            Row linha =
                    sheet.createRow(0);

            Cell titulo =
                    linha.createCell(0);

            titulo.setCellValue(
                    "INTELIFISCAL"
            );

            titulo.setCellStyle(
                    estiloTitulo
            );

            sheet.addMergedRegion(
                    new CellRangeAddress(
                            0,
                            0,
                            0,
                            3
                    )
            );

            //==================================================
            // NOME DO RELATÓRIO
            //==================================================

            linha =
                    sheet.createRow(1);

            Cell nomeRelatorio =
                    linha.createCell(0);

            nomeRelatorio.setCellValue(
                    "RELATÓRIO GERENCIAL — ÚLTIMOS 12 MESES"
            );

            nomeRelatorio.setCellStyle(
                    estiloSubtitulo
            );

            sheet.addMergedRegion(
                    new CellRangeAddress(
                            1,
                            1,
                            0,
                            3
                    )
            );

            //==================================================
            // DESCRIÇÃO
            //==================================================

            linha =
                    sheet.createRow(2);

            Cell descricao =
                    linha.createCell(0);

            descricao.setCellValue(
                    "Análise consolidada de compras e vendas"
            );

            descricao.setCellStyle(
                    estiloTexto
            );

            sheet.addMergedRegion(
                    new CellRangeAddress(
                            2,
                            2,
                            0,
                            3
                    )
            );

            //==================================================
            // PERÍODO
            //==================================================

            String periodo =
                    obterPeriodo(dadosMensais);

            linha =
                    sheet.createRow(3);

            Cell periodoCell =
                    linha.createCell(0);

            periodoCell.setCellValue(
                    "Período analisado: " + periodo
            );

            periodoCell.setCellStyle(
                    estiloPeriodo
            );

            sheet.addMergedRegion(
                    new CellRangeAddress(
                            3,
                            3,
                            0,
                            3
                    )
            );

            //==================================================
            // SEÇÃO RESUMO GERAL
            //==================================================

            linha =
                    sheet.createRow(5);

            Cell secaoResumo =
                    linha.createCell(0);

            secaoResumo.setCellValue(
                    "RESUMO GERAL"
            );

            secaoResumo.setCellStyle(
                    estiloSecao
            );

            sheet.addMergedRegion(
                    new CellRangeAddress(
                            5,
                            5,
                            0,
                            5
                    )
            );

            //==================================================
            // CABEÇALHO RESUMO
            //==================================================

            linha =
                    sheet.createRow(6);

            criarCabecalho(
                    linha,
                    estiloCabecalho,
                    "OPERAÇÃO",
                    "NOTAS",
                    "ITENS",
                    "QUANTIDADE",
                    "VALOR TOTAL",
                    "TICKET MÉDIO"
            );

            //==================================================
            // DADOS DO RESUMO
            //==================================================

            int linhaResumo = 7;

            BigDecimal totalCompras =
                    BigDecimal.ZERO;

            BigDecimal totalVendas =
                    BigDecimal.ZERO;

            for (ResumoPeriodoDTO dto : dadosPeriodo) {

                if (dto == null) {
                    continue;
                }

                linha =
                        sheet.createRow(
                                linhaResumo++
                        );

                Cell operacao =
                        linha.createCell(0);

                operacao.setCellValue(
                        valorTexto(dto.getOperacao())
                );

                operacao.setCellStyle(
                        estiloTexto
                );

                Cell notas =
                        linha.createCell(1);

                notas.setCellValue(
                        dto.getNotas()
                );

                notas.setCellStyle(
                        estiloInteiro
                );

                Cell itens =
                        linha.createCell(2);

                itens.setCellValue(
                        dto.getItens()
                );

                itens.setCellStyle(
                        estiloInteiro
                );

                Cell quantidade =
                        linha.createCell(3);

                quantidade.setCellValue(
                        dto.getQuantidade()
                );

                quantidade.setCellStyle(
                        estiloQuantidade
                );

                Cell valorTotal =
                        linha.createCell(4);

                BigDecimal valor =
                        valorBigDecimal(
                                dto.getValorTotal()
                        );

                valorTotal.setCellValue(
                        valor.doubleValue()
                );

                valorTotal.setCellStyle(
                        estiloMoeda
                );

                Cell ticket =
                        linha.createCell(5);

                BigDecimal ticketMedio =
                        valorBigDecimal(
                                dto.getTicketMedio()
                        );

                ticket.setCellValue(
                        ticketMedio.doubleValue()
                );

                ticket.setCellStyle(
                        estiloMoeda
                );

                if ("COMPRA".equalsIgnoreCase(
                        dto.getOperacao()
                )) {

                    totalCompras =
                            totalCompras.add(valor);

                } else if ("VENDA".equalsIgnoreCase(
                        dto.getOperacao()
                )) {

                    totalVendas =
                            totalVendas.add(valor);
                }
            }

            //==================================================
            // INDICADORES
            //==================================================

            int linhaIndicadores =
                    linhaResumo + 1;

            linha =
                    sheet.createRow(
                            linhaIndicadores
                    );

            Cell indicadorTitulo =
                    linha.createCell(0);

            indicadorTitulo.setCellValue(
                    "INDICADORES GERENCIAIS"
            );

            indicadorTitulo.setCellStyle(
                    estiloSecao
            );

            sheet.addMergedRegion(
                    new CellRangeAddress(
                            linhaIndicadores,
                            linhaIndicadores,
                            0,
                            2
                    )
            );

            linhaIndicadores++;

            adicionarIndicador(
                    sheet,
                    linhaIndicadores++,
                    "Total de Compras",
                    totalCompras,
                    estiloTexto,
                    estiloMoeda
            );

            adicionarIndicador(
                    sheet,
                    linhaIndicadores++,
                    "Total de Vendas",
                    totalVendas,
                    estiloTexto,
                    estiloMoeda
            );

            BigDecimal diferenca =
                    totalVendas.subtract(
                            totalCompras
                    );

            adicionarIndicador(
                    sheet,
                    linhaIndicadores++,
                    "Diferença Vendas - Compras",
                    diferenca,
                    estiloDestaque,
                    estiloDestaque
            );

            BigDecimal relacaoVendasCompras =
                    BigDecimal.ZERO;

            if (totalCompras.compareTo(BigDecimal.ZERO) > 0) {

                relacaoVendasCompras =
                        totalVendas.divide(
                                totalCompras,
                                2,
                                java.math.RoundingMode.HALF_UP
                        );
            }

            adicionarIndicador(
                    sheet,
                    linhaIndicadores++,
                    "Relação Vendas / Compras",
                    relacaoVendasCompras,
                    estiloTexto,
                    criarEstiloRelacao(workbook)
            );

            //==================================================
            // RESUMO MENSAL
            //==================================================

            int linhaMensal =
                    linhaIndicadores + 1;

            linha =
                    sheet.createRow(
                            linhaMensal
                    );

            Cell secaoMensal =
                    linha.createCell(0);

            secaoMensal.setCellValue(
                    "EVOLUÇÃO MENSAL"
            );

            secaoMensal.setCellStyle(
                    estiloSecao
            );

            sheet.addMergedRegion(
                    new CellRangeAddress(
                            linhaMensal,
                            linhaMensal,
                            0,
                            3
                    )
            );

            linhaMensal++;

            linha =
                    sheet.createRow(
                            linhaMensal++
                    );

            criarCabecalho(
                    linha,
                    estiloCabecalho,
                    "MÊS",
                    "COMPRAS",
                    "VENDAS",
                    "DIFERENÇA"
            );

            //==================================================
            // CONSOLIDAR DADOS MENSAIS
            //==================================================

            Map<String, ValoresMensais> mapaMensal =
                    consolidarDadosMensais(
                            dadosMensais
                    );

            BigDecimal somaComprasMensal =
                    BigDecimal.ZERO;

            BigDecimal somaVendasMensal =
                    BigDecimal.ZERO;

            for (
                    Map.Entry<String, ValoresMensais> entrada
                    : mapaMensal.entrySet()
            ) {

                String mes =
                        entrada.getKey();

                ValoresMensais valores =
                        entrada.getValue();

                linha =
                        sheet.createRow(
                                linhaMensal++
                        );

                Cell mesCell =
                        linha.createCell(0);

                mesCell.setCellValue(
                        formatarMes(mes)
                );

                mesCell.setCellStyle(
                        estiloTexto
                );

                Cell comprasCell =
                        linha.createCell(1);

                comprasCell.setCellValue(
                        valores.compras.doubleValue()
                );

                comprasCell.setCellStyle(
                        estiloMoeda
                );

                Cell vendasCell =
                        linha.createCell(2);

                vendasCell.setCellValue(
                        valores.vendas.doubleValue()
                );

                vendasCell.setCellStyle(
                        estiloMoeda
                );

                BigDecimal diferencaMensal =
                        valores.vendas.subtract(
                                valores.compras
                        );

                Cell diferencaCell =
                        linha.createCell(3);

                diferencaCell.setCellValue(
                        diferencaMensal.doubleValue()
                );

                diferencaCell.setCellStyle(
                        estiloMoeda
                );

                somaComprasMensal =
                        somaComprasMensal.add(
                                valores.compras
                        );

                somaVendasMensal =
                        somaVendasMensal.add(
                                valores.vendas
                        );
            }

            //==================================================
            // TOTAL MENSAL
            //==================================================

            linha =
                    sheet.createRow(
                            linhaMensal
                    );

            Cell totalTexto =
                    linha.createCell(0);

            totalTexto.setCellValue(
                    "TOTAL"
            );

            totalTexto.setCellStyle(
                    estiloCabecalho
            );

            Cell totalComprasCell =
                    linha.createCell(1);

            totalComprasCell.setCellValue(
                    somaComprasMensal.doubleValue()
            );

            totalComprasCell.setCellStyle(
                    estiloTotalMoeda
            );

            Cell totalVendasCell =
                    linha.createCell(2);

            totalVendasCell.setCellValue(
                    somaVendasMensal.doubleValue()
            );

            totalVendasCell.setCellStyle(
                    estiloTotalMoeda
            );

            Cell totalDiferencaCell =
                    linha.createCell(3);

            totalDiferencaCell.setCellValue(
                    somaVendasMensal
                            .subtract(
                                    somaComprasMensal
                            )
                            .doubleValue()
            );

            totalDiferencaCell.setCellStyle(
                    estiloTotalMoeda
            );

            //==================================================
            // FILTRO
            //==================================================

            sheet.setAutoFilter(
                    new CellRangeAddress(
                            6,
                            linhaResumo - 1,
                            0,
                            5
                    )
            );

            //==================================================
            // CONGELAR CABEÇALHO
            //==================================================

            sheet.createFreezePane(
                    0,
                    7
            );

            //==================================================
            // LARGURA DAS COLUNAS
            //==================================================

            sheet.setColumnWidth(
                    0,
                    24 * 256
            );

            sheet.setColumnWidth(
                    1,
                    15 * 256
            );

            sheet.setColumnWidth(
                    2,
                    15 * 256
            );

            sheet.setColumnWidth(
                    3,
                    18 * 256
            );

            sheet.setColumnWidth(
                    4,
                    20 * 256
            );

            sheet.setColumnWidth(
                    5,
                    20 * 256
            );

            //==================================================
            // GRÁFICO COMPRAS X VENDAS
            //==================================================

            criarGraficoComprasVendas(
                    sheet,
                    19,
                    30
            );

            //==================================================
            // CONFIGURAÇÃO DE IMPRESSÃO
            //==================================================

            PrintSetup printSetup =
                    sheet.getPrintSetup();

            printSetup.setLandscape(
                    true
            );

            printSetup.setFitWidth(
                    (short) 1
            );

            printSetup.setFitHeight(
                    (short) 0
            );

            sheet.setFitToPage(
                    true
            );

            sheet.setAutobreaks(
                    true
            );

            sheet.setPrintGridlines(false);

            sheet.setDisplayGridlines(false);

            sheet.setHorizontallyCenter(true);

            //==================================================
            // GRAVAR ARQUIVO
            //==================================================

            workbook.write(output);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Erro ao gerar o relatório Excel.",
                    e
            );
        }
    }

    //==================================================
// GERAR RESUMO DE COMPRAS
//==================================================

    public void gerarResumoCompras(

            ResumoComprasDTO resumo,

            List<FornecedorCompraDTO> fornecedores,

            List<DetalhamentoCompraDTO> detalhes,

            LocalDate dataInicio,

            LocalDate dataFim,

            String caminhoArquivo

    ) {

        if (resumo == null) {

            throw new IllegalArgumentException(
                    "O resumo de compras não pode ser nulo."
            );
        }

        if (fornecedores == null) {

            throw new IllegalArgumentException(
                    "A lista de fornecedores não pode ser nula."
            );
        }

        if (detalhes == null) {

            throw new IllegalArgumentException(
                    "A lista de detalhes não pode ser nula."
            );
        }

        if (caminhoArquivo == null ||
                caminhoArquivo.isBlank()) {

            throw new IllegalArgumentException(
                    "O caminho do arquivo não pode ser vazio."
            );
        }


        try (

                Workbook workbook =
                        new XSSFWorkbook();

                FileOutputStream output =
                        new FileOutputStream(
                                caminhoArquivo
                        )

        ) {


            //==================================================
            // ESTILOS
            //==================================================

            CellStyle estiloTitulo =
                    criarEstiloTitulo(workbook);

            CellStyle estiloSubtitulo =
                    criarEstiloSubtitulo(workbook);

            CellStyle estiloPeriodo =
                    criarEstiloPeriodo(workbook);

            CellStyle estiloSecao =
                    criarEstiloSecao(workbook);

            CellStyle estiloCabecalho =
                    criarEstiloCabecalho(workbook);

            CellStyle estiloTexto =
                    criarEstiloTexto(workbook);

            CellStyle estiloInteiro =
                    criarEstiloInteiro(workbook);

            CellStyle estiloQuantidade =
                    criarEstiloQuantidade(workbook);

            CellStyle estiloMoeda =
                    criarEstiloMoeda(workbook);


            //==================================================
            // ABA 1 - RESUMO DE COMPRAS
            //==================================================

            Sheet sheet =
                    workbook.createSheet(
                            "Resumo de Compras"
                    );


            //==================================================
            // TÍTULO
            //==================================================

            Row linha =
                    sheet.createRow(0);

            Cell titulo =
                    linha.createCell(0);

            titulo.setCellValue(
                    "INTELIFISCAL"
            );

            titulo.setCellStyle(
                    estiloTitulo
            );

            sheet.addMergedRegion(
                    new CellRangeAddress(
                            0,
                            0,
                            0,
                            4
                    )
            );


            //==================================================
            // NOME DO RELATÓRIO
            //==================================================

            linha =
                    sheet.createRow(1);

            Cell nomeRelatorio =
                    linha.createCell(0);

            nomeRelatorio.setCellValue(
                    "RESUMO DE COMPRAS"
            );

            nomeRelatorio.setCellStyle(
                    estiloSubtitulo
            );

            sheet.addMergedRegion(
                    new CellRangeAddress(
                            1,
                            1,
                            0,
                            4
                    )
            );


            //==================================================
            // PERÍODO
            //==================================================

            linha =
                    sheet.createRow(2);

            Cell periodo =
                    linha.createCell(0);

            String textoPeriodo;

            if (dataInicio != null &&
                    dataFim != null) {

                textoPeriodo =
                        "Período analisado: "
                                + dataInicio
                                + " até "
                                + dataFim;

            } else {

                textoPeriodo =
                        "Período analisado: Desde o início";
            }

            periodo.setCellValue(
                    textoPeriodo
            );

            periodo.setCellStyle(
                    estiloPeriodo
            );

            sheet.addMergedRegion(
                    new CellRangeAddress(
                            2,
                            2,
                            0,
                            4
                    )
            );


            //==================================================
            // RESUMO GERAL
            //==================================================

            linha =
                    sheet.createRow(4);

            Cell secao =
                    linha.createCell(0);

            secao.setCellValue(
                    "RESUMO GERAL"
            );

            secao.setCellStyle(
                    estiloSecao
            );

            sheet.addMergedRegion(
                    new CellRangeAddress(
                            4,
                            4,
                            0,
                            4
                    )
            );


            //==================================================
            // CABEÇALHO RESUMO
            //==================================================

            linha =
                    sheet.createRow(5);

            criarCabecalho(

                    linha,

                    estiloCabecalho,

                    "NOTAS",
                    "ITENS",
                    "QUANTIDADE",
                    "VALOR TOTAL",
                    "TICKET MÉDIO"
            );


            //==================================================
            // DADOS RESUMO
            //==================================================

            linha =
                    sheet.createRow(6);


            Cell notas =
                    linha.createCell(0);

            notas.setCellValue(
                    resumo.getNotas()
            );

            notas.setCellStyle(
                    estiloInteiro
            );


            Cell itens =
                    linha.createCell(1);

            itens.setCellValue(
                    resumo.getItens()
            );

            itens.setCellStyle(
                    estiloInteiro
            );


            Cell quantidade =
                    linha.createCell(2);

            quantidade.setCellValue(
                    resumo.getQuantidade()
            );

            quantidade.setCellStyle(
                    estiloQuantidade
            );


            Cell valorTotal =
                    linha.createCell(3);

            BigDecimal valor =
                    valorBigDecimal(
                            resumo.getValorTotal()
                    );

            valorTotal.setCellValue(
                    valor.doubleValue()
            );

            valorTotal.setCellStyle(
                    estiloMoeda
            );


            Cell ticket =
                    linha.createCell(4);

            BigDecimal ticketMedio =
                    valorBigDecimal(
                            resumo.getTicketMedio()
                    );

            ticket.setCellValue(
                    ticketMedio.doubleValue()
            );

            ticket.setCellStyle(
                    estiloMoeda
            );


            //==================================================
            // COMPRAS POR FORNECEDOR
            //==================================================

            linha =
                    sheet.createRow(8);

            Cell secaoFornecedores =
                    linha.createCell(0);

            secaoFornecedores.setCellValue(
                    "COMPRAS POR FORNECEDOR"
            );

            secaoFornecedores.setCellStyle(
                    estiloSecao
            );

            sheet.addMergedRegion(
                    new CellRangeAddress(
                            8,
                            8,
                            0,
                            4
                    )
            );


            //==================================================
            // CABEÇALHO FORNECEDORES
            //==================================================

            linha =
                    sheet.createRow(9);

            criarCabecalho(

                    linha,

                    estiloCabecalho,

                    "FORNECEDOR",
                    "NOTAS",
                    "DATA DA ÚLTIMA COMPRA",
                    "QUANTIDADE",
                    "VALOR TOTAL"
            );


            //==================================================
            // FORNECEDORES
            //==================================================

            int linhaFornecedor = 10;


            for (
                    FornecedorCompraDTO fornecedor
                    : fornecedores
            ) {

                if (fornecedor == null) {
                    continue;
                }


                linha =
                        sheet.createRow(
                                linhaFornecedor++
                        );


                //==================================================
                // FORNECEDOR
                //==================================================

                Cell nome =
                        linha.createCell(0);

                nome.setCellValue(
                        valorTexto(
                                fornecedor.getFornecedor()
                        )
                );

                nome.setCellStyle(
                        estiloTexto
                );


                //==================================================
                // NOTAS
                //==================================================

                Cell notasFornecedor =
                        linha.createCell(1);

                notasFornecedor.setCellValue(
                        fornecedor.getNotas()
                );

                notasFornecedor.setCellStyle(
                        estiloInteiro
                );


                //==================================================
                // DATA ÚLTIMA COMPRA
                //==================================================

                Cell dataUltimaCompra =
                        linha.createCell(2);


                String dataTexto =
                        fornecedor.getDataUltimaCompra();


                if (dataTexto != null &&
                        !dataTexto.isBlank()) {

                    try {

                        LocalDate data;

                        if (
                                dataTexto.matches(
                                        "\\d{4}-\\d{2}-\\d{2}"
                                )
                        ) {

                            data =
                                    LocalDate.parse(
                                            dataTexto
                                    );

                        } else {

                            data =
                                    LocalDate.parse(
                                            dataTexto,
                                            java.time.format
                                                    .DateTimeFormatter
                                                    .ofPattern(
                                                            "dd/MM/yyyy"
                                                    )
                                    );
                        }


                        dataUltimaCompra.setCellValue(
                                data
                        );


                        CellStyle estiloData =
                                workbook.createCellStyle();

                        estiloData.cloneStyleFrom(
                                estiloTexto
                        );

                        estiloData.setDataFormat(
                                workbook.createDataFormat()
                                        .getFormat(
                                                "dd/MM/yyyy"
                                        )
                        );

                        dataUltimaCompra.setCellStyle(
                                estiloData
                        );

                    } catch (Exception e) {

                        dataUltimaCompra.setCellValue(
                                dataTexto
                        );

                        dataUltimaCompra.setCellStyle(
                                estiloTexto
                        );
                    }

                } else {

                    dataUltimaCompra.setCellValue(
                            ""
                    );

                    dataUltimaCompra.setCellStyle(
                            estiloTexto
                    );
                }


                //==================================================
                // QUANTIDADE
                //==================================================

                Cell quantidadeFornecedor =
                        linha.createCell(3);

                quantidadeFornecedor.setCellValue(
                        fornecedor.getQuantidade()
                );

                quantidadeFornecedor.setCellStyle(
                        estiloQuantidade
                );


                //==================================================
                // VALOR TOTAL
                //==================================================

                Cell valorFornecedor =
                        linha.createCell(4);

                BigDecimal valorFornecedorBigDecimal =
                        valorBigDecimal(
                                fornecedor.getValorTotal()
                        );

                valorFornecedor.setCellValue(
                        valorFornecedorBigDecimal
                                .doubleValue()
                );

                valorFornecedor.setCellStyle(
                        estiloMoeda
                );
            }


            //==================================================
            // FILTRO
            //==================================================

            if (linhaFornecedor > 10) {

                sheet.setAutoFilter(
                        new CellRangeAddress(
                                9,
                                linhaFornecedor - 1,
                                0,
                                4
                        )
                );
            }


            //==================================================
            // CONGELAR
            //==================================================

            sheet.createFreezePane(
                    0,
                    10
            );


            //==================================================
            // LARGURAS
            //==================================================

            sheet.setColumnWidth(
                    0,
                    42 * 256
            );

            sheet.setColumnWidth(
                    1,
                    12 * 256
            );

            sheet.setColumnWidth(
                    2,
                    22 * 256
            );

            sheet.setColumnWidth(
                    3,
                    18 * 256
            );

            sheet.setColumnWidth(
                    4,
                    20 * 256
            );


            //==================================================
            // IMPRESSÃO
            //==================================================

            PrintSetup printSetup =
                    sheet.getPrintSetup();

            printSetup.setLandscape(
                    true
            );

            printSetup.setFitWidth(
                    (short) 1
            );

            printSetup.setFitHeight(
                    (short) 0
            );

            sheet.setFitToPage(
                    true
            );

            sheet.setPrintGridlines(
                    false
            );

            sheet.setDisplayGridlines(
                    false
            );


            //==================================================
            // ABA 2 - DETALHAMENTO
            //==================================================

            Sheet detalhe =
                    workbook.createSheet(
                            "Detalhamento de Compras"
                    );


            //==================================================
            // ESTILOS DO DETALHAMENTO
            //==================================================

            CellStyle estiloCabecalhoDetalhe =
                    criarEstiloCabecalho(workbook);

            CellStyle estiloDataDetalhe =
                    workbook.createCellStyle();

            estiloDataDetalhe.cloneStyleFrom(
                    estiloTexto
            );

            estiloDataDetalhe.setDataFormat(
                    workbook.createDataFormat()
                            .getFormat(
                                    "dd/MM/yyyy"
                            )
            );


            CellStyle estiloQuantidadeDetalhe =
                    criarEstiloQuantidade(workbook);

            CellStyle estiloMoedaDetalhe =
                    criarEstiloMoeda(workbook);


            //==================================================
            // CABEÇALHO DETALHAMENTO
            //==================================================

            Row cabecalho =
                    detalhe.createRow(0);

            criarCabecalho(

                    cabecalho,

                    estiloCabecalhoDetalhe,

                    "CNPJ",
                    "FORNECEDOR",
                    "NF",
                    "DATA DA COMPRA",
                    "PRODUTO",
                    "CÓDIGO",
                    "QUANTIDADE",
                    "VALOR UNITÁRIO",
                    "VALOR TOTAL"
            );


            //==================================================
            // DADOS DETALHAMENTO
            //==================================================

            int linhaDetalhe = 1;


            for (
                    DetalhamentoCompraDTO dto
                    : detalhes
            ) {

                if (dto == null) {
                    continue;
                }


                Row row =
                        detalhe.createRow(
                                linhaDetalhe++
                        );


                //==================================================
                // CNPJ
                //==================================================

                row.createCell(0)
                        .setCellValue(
                                valorTexto(
                                        dto.getCnpj()
                                )
                        );


                //==================================================
                // FORNECEDOR
                //==================================================

                row.createCell(1)
                        .setCellValue(
                                valorTexto(
                                        dto.getFornecedor()
                                )
                        );


                //==================================================
                // NF
                //==================================================

                row.createCell(2)
                        .setCellValue(
                                valorTexto(
                                        dto.getNumeroNota()
                                )
                        );


                //==================================================
                // DATA
                //==================================================

                Cell cellData =
                        row.createCell(3);

                if (dto.getDataCompra() != null) {

                    cellData.setCellValue(
                            dto.getDataCompra()
                    );

                    cellData.setCellStyle(
                            estiloDataDetalhe
                    );

                } else {

                    cellData.setCellValue(
                            ""
                    );

                    cellData.setCellStyle(
                            estiloTexto
                    );
                }


                //==================================================
                // PRODUTO
                //==================================================

                row.createCell(4)
                        .setCellValue(
                                valorTexto(
                                        dto.getProduto()
                                )
                        );


                //==================================================
                // CÓDIGO
                //==================================================

                row.createCell(5)
                        .setCellValue(
                                valorTexto(
                                        dto.getCodigoProduto()
                                )
                        );


                //==================================================
                // QUANTIDADE
                //==================================================

                Cell cellQuantidade =
                        row.createCell(6);

                cellQuantidade.setCellValue(
                        dto.getQuantidade()
                );

                cellQuantidade.setCellStyle(
                        estiloQuantidadeDetalhe
                );

                //==================================================
                // VALOR UNITÁRIO
                //==================================================

                Cell cellValorUnitario =
                        row.createCell(7);

                if (dto.getValorUnitario() != null) {

                    cellValorUnitario.setCellValue(
                            dto.getValorUnitario()
                                    .doubleValue()
                    );
                }

                cellValorUnitario.setCellStyle(
                        estiloMoedaDetalhe
                );


                //==================================================
                // VALOR TOTAL
                //==================================================

                Cell cellValorTotal =
                        row.createCell(8);

                if (dto.getValorTotal() != null) {

                    cellValorTotal.setCellValue(
                            dto.getValorTotal()
                                    .doubleValue()
                    );
                }

                cellValorTotal.setCellStyle(
                        estiloMoedaDetalhe
                );
            }


            //==================================================
            // FILTRO DETALHAMENTO
            //==================================================

            if (linhaDetalhe > 1) {

                detalhe.setAutoFilter(
                        new CellRangeAddress(
                                0,
                                linhaDetalhe - 1,
                                0,
                                8
                        )
                );
            }


            //==================================================
            // CONGELAR CABEÇALHO
            //==================================================

            detalhe.createFreezePane(
                    0,
                    1
            );


            //==================================================
            // LARGURAS
            //==================================================

            detalhe.setColumnWidth(
                    0,
                    20 * 256
            );

            detalhe.setColumnWidth(
                    1,
                    40 * 256
            );

            detalhe.setColumnWidth(
                    2,
                    12 * 256
            );

            detalhe.setColumnWidth(
                    3,
                    18 * 256
            );

            detalhe.setColumnWidth(
                    4,
                    40 * 256
            );

            detalhe.setColumnWidth(
                    5,
                    18 * 256
            );

            detalhe.setColumnWidth(
                    6,
                    15 * 256
            );

            detalhe.setColumnWidth(
                    7,
                    18 * 256
            );

            detalhe.setColumnWidth(
                    8,
                    18 * 256
            );


            //==================================================
            // IMPRESSÃO DETALHAMENTO
            //==================================================

            PrintSetup printSetupDetalhe =
                    detalhe.getPrintSetup();

            printSetupDetalhe.setLandscape(
                    true
            );

            printSetupDetalhe.setFitWidth(
                    (short) 1
            );

            printSetupDetalhe.setFitHeight(
                    (short) 0
            );

            detalhe.setFitToPage(
                    true
            );

            detalhe.setPrintGridlines(
                    false
            );

            detalhe.setDisplayGridlines(
                    false
            );


            //==================================================
            // GRAVAR ARQUIVO
            //==================================================

            workbook.write(
                    output
            );


        } catch (IOException e) {

            throw new RuntimeException(
                    "Erro ao gerar o Excel do resumo de compras.",
                    e
            );
        }
    }

    //==================================================
    // CONSOLIDAR DADOS MENSAIS
    //==================================================

    private Map<String, ValoresMensais>
    consolidarDadosMensais(
            List<ResumoMensalDTO> dados
    ) {

        Map<String, ValoresMensais> mapa =
                new LinkedHashMap<>();

        for (ResumoMensalDTO dto : dados) {

            if (dto == null) {
                continue;
            }

            String mes =
                    dto.getMes();

            if (mes == null) {
                continue;
            }

            ValoresMensais valores =
                    mapa.computeIfAbsent(
                            mes,
                            chave -> new ValoresMensais()
                    );

            BigDecimal valor =
                    valorBigDecimal(
                            dto.getValorTotal()
                    );

            if ("COMPRA".equalsIgnoreCase(
                    dto.getOperacao()
            )) {

                valores.compras =
                        valores.compras.add(
                                valor
                        );

            } else if ("VENDA".equalsIgnoreCase(
                    dto.getOperacao()
            )) {

                valores.vendas =
                        valores.vendas.add(
                                valor
                        );
            }
        }

        return mapa;
    }


    //==================================================
    // PERÍODO
    //==================================================

    private String obterPeriodo(
            List<ResumoMensalDTO> dados
    ) {

        if (dados == null || dados.isEmpty()) {
            return "Não disponível";
        }

        String primeiro = null;
        String ultimo = null;

        for (ResumoMensalDTO dto : dados) {

            if (dto == null ||
                    dto.getMes() == null) {
                continue;
            }

            if (primeiro == null) {
                primeiro = dto.getMes();
            }

            ultimo = dto.getMes();
        }

        if (primeiro == null) {
            return "Não disponível";
        }

        return formatarMes(primeiro)
                + " a "
                + formatarMes(ultimo);
    }


    //==================================================
    // FORMATAR MÊS
    //==================================================

    private String formatarMes(
            String mes
    ) {

        if (mes == null) {
            return "";
        }

        if (mes.length() == 7) {

            String ano =
                    mes.substring(0, 4);

            String numeroMes =
                    mes.substring(5, 7);

            return numeroMes
                    + "/"
                    + ano;
        }

        return mes;
    }


    //==================================================
    // ADICIONAR INDICADOR
    //==================================================

    private void adicionarIndicador(
            Sheet sheet,
            int numeroLinha,
            String descricao,
            BigDecimal valor,
            CellStyle estiloDescricao,
            CellStyle estiloValor
    ) {

        Row linha =
                sheet.createRow(
                        numeroLinha
                );

        Cell descricaoCell =
                linha.createCell(0);

        descricaoCell.setCellValue(
                descricao
        );

        descricaoCell.setCellStyle(
                estiloDescricao
        );

        Cell valorCell =
                linha.createCell(1);

        valorCell.setCellValue(
                valor.doubleValue()
        );

        valorCell.setCellStyle(
                estiloValor
        );
    }


    //==================================================
    // CABEÇALHO
    //==================================================

    private void criarCabecalho(
            Row linha,
            CellStyle estilo,
            String... valores
    ) {

        for (int i = 0; i < valores.length; i++) {

            Cell cell =
                    linha.createCell(i);

            cell.setCellValue(
                    valores[i]
            );

            cell.setCellStyle(
                    estilo
            );
        }
    }


    //==================================================
    // BIG DECIMAL
    //==================================================

    private BigDecimal valorBigDecimal(
            BigDecimal valor
    ) {

        if (valor == null) {
            return BigDecimal.ZERO;
        }

        return valor;
    }


    //==================================================
    // TEXTO
    //==================================================

    private String valorTexto(
            String valor
    ) {

        if (valor == null) {
            return "";
        }

        return valor;
    }


    //==================================================
    // ESTILO TÍTULO
    //==================================================

    private CellStyle criarEstiloTitulo(
            Workbook workbook
    ) {

        CellStyle estilo =
                workbook.createCellStyle();

        Font fonte =
                workbook.createFont();

        fonte.setBold(true);
        fonte.setFontHeightInPoints(
                (short) 18
        );

        fonte.setColor(
                IndexedColors.WHITE.getIndex()
        );

        estilo.setFont(fonte);

        estilo.setAlignment(
                HorizontalAlignment.CENTER
        );

        estilo.setVerticalAlignment(
                VerticalAlignment.CENTER
        );

        estilo.setFillForegroundColor(
                IndexedColors.DARK_BLUE.getIndex()
        );

        estilo.setFillPattern(
                FillPatternType.SOLID_FOREGROUND
        );

        estilo.setBorderBottom(
                BorderStyle.THICK
        );

        return estilo;
    }


    //==================================================
    // ESTILO SUBTÍTULO
    //==================================================

    private CellStyle criarEstiloSubtitulo(
            Workbook workbook
    ) {

        CellStyle estilo =
                workbook.createCellStyle();

        Font fonte =
                workbook.createFont();

        fonte.setBold(true);
        fonte.setFontHeightInPoints(
                (short) 13
        );

        fonte.setColor(
                IndexedColors.DARK_BLUE.getIndex()
        );

        estilo.setFont(fonte);

        estilo.setAlignment(
                HorizontalAlignment.LEFT
        );

        estilo.setVerticalAlignment(
                VerticalAlignment.CENTER
        );

        return estilo;
    }


    //==================================================
    // ESTILO PERÍODO
    //==================================================

    private CellStyle criarEstiloPeriodo(
            Workbook workbook
    ) {

        CellStyle estilo =
                workbook.createCellStyle();

        Font fonte =
                workbook.createFont();

        fonte.setItalic(true);

        estilo.setFont(fonte);

        return estilo;
    }


    //==================================================
    // ESTILO SEÇÃO
    //==================================================

    private CellStyle criarEstiloSecao(
            Workbook workbook
    ) {

        CellStyle estilo =
                workbook.createCellStyle();

        Font fonte =
                workbook.createFont();

        fonte.setBold(true);
        fonte.setFontHeightInPoints(
                (short) 12
        );

        fonte.setColor(
                IndexedColors.WHITE.getIndex()
        );

        estilo.setFont(fonte);

        estilo.setAlignment(
                HorizontalAlignment.LEFT
        );

        estilo.setVerticalAlignment(
                VerticalAlignment.CENTER
        );

        estilo.setFillForegroundColor(
                IndexedColors.BLUE.getIndex()
        );

        estilo.setFillPattern(
                FillPatternType.SOLID_FOREGROUND
        );

        estilo.setBorderTop(
                BorderStyle.THIN
        );

        estilo.setBorderBottom(
                BorderStyle.THIN
        );

        return estilo;
    }

    //==================================================
    // ESTILO CABEÇALHO
    //==================================================

    private CellStyle criarEstiloCabecalho(
            Workbook workbook
    ) {

        CellStyle estilo =
                workbook.createCellStyle();

        Font fonte =
                workbook.createFont();

        fonte.setBold(true);

        fonte.setColor(
                IndexedColors.WHITE.getIndex()
        );

        estilo.setFont(fonte);

        estilo.setAlignment(
                HorizontalAlignment.CENTER
        );

        estilo.setVerticalAlignment(
                VerticalAlignment.CENTER
        );

        estilo.setFillForegroundColor(
                IndexedColors.DARK_BLUE.getIndex()
        );

        estilo.setFillPattern(
                FillPatternType.SOLID_FOREGROUND
        );

        estilo.setBorderTop(
                BorderStyle.THIN
        );

        estilo.setBorderBottom(
                BorderStyle.THIN
        );

        estilo.setBorderLeft(
                BorderStyle.THIN
        );

        estilo.setBorderRight(
                BorderStyle.THIN
        );

        return estilo;
    }


    //==================================================
    // ESTILO TEXTO
    //==================================================

    private CellStyle criarEstiloTexto(
            Workbook workbook
    ) {

        CellStyle estilo =
                workbook.createCellStyle();

        estilo.setAlignment(
                HorizontalAlignment.LEFT
        );

        estilo.setVerticalAlignment(
                VerticalAlignment.CENTER
        );

        return estilo;
    }


    //==================================================
    // ESTILO INTEIRO
    //==================================================

    private CellStyle criarEstiloInteiro(
            Workbook workbook
    ) {

        CellStyle estilo =
                workbook.createCellStyle();

        estilo.setDataFormat(
                workbook.createDataFormat()
                        .getFormat(
                                "#,##0"
                        )
        );

        estilo.setAlignment(
                HorizontalAlignment.RIGHT
        );

        return estilo;
    }


    //==================================================
    // ESTILO QUANTIDADE
    //==================================================

    private CellStyle criarEstiloQuantidade(
            Workbook workbook
    ) {

        CellStyle estilo =
                workbook.createCellStyle();

        estilo.setDataFormat(
                workbook.createDataFormat()
                        .getFormat(
                                "#,##0.###"
                        )
        );

        estilo.setAlignment(
                HorizontalAlignment.RIGHT
        );

        return estilo;
    }


    //==================================================
    // ESTILO MOEDA
    //==================================================

    private CellStyle criarEstiloMoeda(
            Workbook workbook
    ) {

        CellStyle estilo =
                workbook.createCellStyle();

        estilo.setDataFormat(
                workbook.createDataFormat()
                        .getFormat(
                                "\"R$\" #,##0.00"
                        )
        );

        estilo.setAlignment(
                HorizontalAlignment.RIGHT
        );

        return estilo;
    }


    //==================================================
    // ESTILO DESTAQUE
    //==================================================

    private CellStyle criarEstiloDestaque(
            Workbook workbook
    ) {

        CellStyle estilo =
                workbook.createCellStyle();

        Font fonte =
                workbook.createFont();

        fonte.setBold(true);

        fonte.setColor(
                IndexedColors.DARK_GREEN.getIndex()
        );

        estilo.setFont(fonte);

        estilo.setDataFormat(
                workbook.createDataFormat()
                        .getFormat(
                                "\"R$\" #,##0.00"
                        )
        );

        estilo.setAlignment(
                HorizontalAlignment.RIGHT
        );

        estilo.setVerticalAlignment(
                VerticalAlignment.CENTER
        );

        estilo.setFillForegroundColor(
                IndexedColors.LIGHT_GREEN.getIndex()
        );

        estilo.setFillPattern(
                FillPatternType.SOLID_FOREGROUND
        );

        estilo.setBorderTop(
                BorderStyle.THIN
        );

        estilo.setBorderBottom(
                BorderStyle.THIN
        );

        estilo.setBorderLeft(
                BorderStyle.THIN
        );

        estilo.setBorderRight(
                BorderStyle.THIN
        );

        return estilo;
    }

    //==================================================
    // ESTILO RELAÇÃO
    //==================================================

    private CellStyle criarEstiloRelacao(
            Workbook workbook
    ) {

        CellStyle estilo =
                workbook.createCellStyle();

        Font fonte =
                workbook.createFont();

        fonte.setBold(true);

        estilo.setFont(fonte);

        estilo.setDataFormat(
                workbook.createDataFormat()
                        .getFormat(
                                "0.00\"x\""
                        )
        );

        estilo.setAlignment(
                HorizontalAlignment.RIGHT
        );

        estilo.setVerticalAlignment(
                VerticalAlignment.CENTER
        );

        estilo.setFillForegroundColor(
                IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex()
        );

        estilo.setFillPattern(
                FillPatternType.SOLID_FOREGROUND
        );

        estilo.setBorderTop(
                BorderStyle.THIN
        );

        estilo.setBorderBottom(
                BorderStyle.THIN
        );

        estilo.setBorderLeft(
                BorderStyle.THIN
        );

        estilo.setBorderRight(
                BorderStyle.THIN
        );

        return estilo;
    }

    //==================================================
    // ESTILO TOTAL MOEDA
    //==================================================

    private CellStyle criarEstiloTotalMoeda(
            Workbook workbook
    ) {

        CellStyle estilo =
                workbook.createCellStyle();

        Font fonte =
                workbook.createFont();

        fonte.setBold(true);

        fonte.setColor(
                IndexedColors.WHITE.getIndex()
        );

        estilo.setFont(fonte);

        estilo.setDataFormat(
                workbook.createDataFormat()
                        .getFormat(
                                "\"R$\" #,##0.00"
                        )
        );

        estilo.setAlignment(
                HorizontalAlignment.RIGHT
        );

        estilo.setVerticalAlignment(
                VerticalAlignment.CENTER
        );

        estilo.setFillForegroundColor(
                IndexedColors.DARK_BLUE.getIndex()
        );

        estilo.setFillPattern(
                FillPatternType.SOLID_FOREGROUND
        );

        estilo.setBorderTop(
                BorderStyle.THIN
        );

        estilo.setBorderBottom(
                BorderStyle.THIN
        );

        estilo.setBorderLeft(
                BorderStyle.THIN
        );

        estilo.setBorderRight(
                BorderStyle.THIN
        );

        return estilo;
    }

    //==================================================
    // CRIAR GRÁFICO COMPRAS X VENDAS
    //==================================================

    private void criarGraficoComprasVendas(
            Sheet sheet,
            int primeiraLinha,
            int ultimaLinha
    ) {

        XSSFDrawing drawing =
                (XSSFDrawing) sheet.createDrawingPatriarch();

        ClientAnchor anchor =
                drawing.createAnchor(
                        0,
                        0,
                        0,
                        0,
                        4,
                        1,
                        15,
                        20
                );

        XSSFChart chart =
                drawing.createChart(anchor);

        chart.setTitleText(
                "Compras x Vendas por mês"
        );

        chart.setTitleOverlay(false);

        XDDFChartLegend legend =
                chart.getOrAddLegend();

        legend.setPosition(
                LegendPosition.BOTTOM
        );

        //==================================================
        // EIXO X
        //==================================================

        XDDFCategoryAxis eixoX =
                chart.createCategoryAxis(
                        AxisPosition.BOTTOM
                );

        eixoX.setTitle("Mês");

        //==================================================
        // EIXO Y
        //==================================================

        XDDFValueAxis eixoY =
                chart.createValueAxis(
                        AxisPosition.LEFT
                );

        eixoY.setTitle("Valor (R$)");

        eixoY.setCrosses(
                AxisCrosses.AUTO_ZERO
        );

        //==================================================
        // DADOS
        //==================================================

        XDDFDataSource<String> categorias =
                XDDFDataSourcesFactory.fromStringCellRange(
                        (XSSFSheet) sheet,
                        new CellRangeAddress(
                                primeiraLinha,
                                ultimaLinha,
                                0,
                                0
                        )
                );

        XDDFNumericalDataSource<Double> compras =
                XDDFDataSourcesFactory.fromNumericCellRange(
                        (XSSFSheet) sheet,
                        new CellRangeAddress(
                                primeiraLinha,
                                ultimaLinha,
                                1,
                                1
                        )
                );

        XDDFNumericalDataSource<Double> vendas =
                XDDFDataSourcesFactory.fromNumericCellRange(
                        (XSSFSheet) sheet,
                        new CellRangeAddress(
                                primeiraLinha,
                                ultimaLinha,
                                2,
                                2
                        )
                );

        //==================================================
        // GRÁFICO DE COLUNAS
        //==================================================

        XDDFBarChartData data =
                (XDDFBarChartData) chart.createData(
                        ChartTypes.BAR,
                        eixoX,
                        eixoY
                );

        data.setBarDirection(
                BarDirection.COL
        );

        data.setBarGrouping(
                BarGrouping.CLUSTERED
        );

        //==================================================
        // COMPRAS
        //==================================================

        XDDFChartData.Series serieCompras =
                data.addSeries(
                        categorias,
                        compras
                );

        serieCompras.setTitle(
                "Compras",
                null
        );

        //==================================================
        // VENDAS
        //==================================================

        XDDFChartData.Series serieVendas =
                data.addSeries(
                        categorias,
                        vendas
                );

        serieVendas.setTitle(
                "Vendas",
                null
        );

        //==================================================
        // GERAR GRÁFICO
        //==================================================

        chart.plot(data);
    }


    //==================================================
    // CLASSE AUXILIAR
    //==================================================

    private static class ValoresMensais {

        private BigDecimal compras =
                BigDecimal.ZERO;

        private BigDecimal vendas =
                BigDecimal.ZERO;
    }

    //==================================================
    // EXPORTAR RESUMO DE VENDAS
    //==================================================

    public void gerarResumoVendas(
            List<ResumoVendaDTO> dados,
            String caminhoArquivo
    ) {

        if (dados == null) {

            throw new IllegalArgumentException(
                    "Os dados de vendas não podem ser nulos."
            );
        }

        if (caminhoArquivo == null ||
                caminhoArquivo.isBlank()) {

            throw new IllegalArgumentException(
                    "O caminho do arquivo não pode ser vazio."
            );
        }


        try (
                Workbook workbook = new XSSFWorkbook();
                FileOutputStream output =
                        new FileOutputStream(caminhoArquivo)
        ) {


            //==================================================
            // ABA 1 - RESUMO DE VENDAS
            //==================================================

            Sheet sheet =
                    workbook.createSheet(
                            "Resumo de Vendas"
                    );


            //==================================================
            // ESTILOS
            //==================================================

            CellStyle estiloTitulo =
                    criarEstiloTitulo(workbook);

            CellStyle estiloSubtitulo =
                    criarEstiloSubtitulo(workbook);

            CellStyle estiloPeriodo =
                    criarEstiloPeriodo(workbook);

            CellStyle estiloSecao =
                    criarEstiloSecao(workbook);

            CellStyle estiloCabecalho =
                    criarEstiloCabecalho(workbook);

            CellStyle estiloTexto =
                    criarEstiloTexto(workbook);

            CellStyle estiloInteiro =
                    criarEstiloInteiro(workbook);

            CellStyle estiloQuantidade =
                    criarEstiloQuantidade(workbook);

            CellStyle estiloMoeda =
                    criarEstiloMoeda(workbook);


            //==================================================
            // ESTILO DATA
            //==================================================

            CellStyle estiloData =
                    workbook.createCellStyle();

            estiloData.cloneStyleFrom(
                    estiloTexto
            );

            estiloData.setDataFormat(
                    workbook.createDataFormat()
                            .getFormat("dd/MM/yyyy")
            );


            //==================================================
            // CONSOLIDAÇÃO DAS VENDAS
            //
            // O ValorTotalNF é contado somente uma vez
            // para cada número de NF.
            //==================================================

            Map<String, BigDecimal> valoresPorNF =
                    new LinkedHashMap<>();


            Map<String, ClienteResumo> clientes =
                    new LinkedHashMap<>();


            int totalItens = 0;

            BigDecimal totalQuantidade =
                    BigDecimal.ZERO;


            LocalDate menorData = null;
            LocalDate maiorData = null;


            for (ResumoVendaDTO dto : dados) {

                if (dto == null) {
                    continue;
                }


                //==================================================
                // ITENS
                //==================================================

                totalItens++;


                //==================================================
                // QUANTIDADE
                //==================================================

                if (dto.getQuantidade() != null) {

                    totalQuantidade =
                            totalQuantidade.add(
                                    dto.getQuantidade()
                            );
                }


                //==================================================
                // DATA
                //==================================================

                LocalDate dataVenda = null;

                String dataTexto =
                        dto.getDataEmissao();


                if (dataTexto != null &&
                        !dataTexto.isBlank()) {

                    try {

                        if (dataTexto.length() >= 10 &&
                                dataTexto.charAt(4) == '-' &&
                                dataTexto.charAt(7) == '-') {

                            dataVenda =
                                    LocalDate.parse(
                                            dataTexto.substring(
                                                    0,
                                                    10
                                            )
                                    );

                        } else {

                            dataVenda =
                                    LocalDate.parse(
                                            dataTexto,
                                            java.time.format.DateTimeFormatter
                                                    .ofPattern(
                                                            "dd/MM/yyyy"
                                                    )
                                    );
                        }

                    } catch (Exception ignored) {
                        // Mantém a exportação mesmo se
                        // alguma data estiver inválida.
                    }
                }


                if (dataVenda != null) {

                    if (menorData == null ||
                            dataVenda.isBefore(menorData)) {

                        menorData = dataVenda;
                    }

                    if (maiorData == null ||
                            dataVenda.isAfter(maiorData)) {

                        maiorData = dataVenda;
                    }
                }


                //==================================================
                // VALOR DA NF
                //
                // Cada NF entra somente uma vez.
                //==================================================

                String nf =
                        dto.getNf();

                if (nf == null ||
                        nf.isBlank()) {

                    nf = "SEM_NF_" + totalItens;
                }


                if (!valoresPorNF.containsKey(nf)) {

                    valoresPorNF.put(
                            nf,
                            valorBigDecimal(
                                    dto.getValorTotalNF()
                            )
                    );
                }


                //==================================================
                // AGRUPAMENTO POR CLIENTE
                //==================================================

                String cliente =
                        valorTexto(
                                dto.getDestinatario()
                        );


                ClienteResumo resumoCliente =
                        clientes.get(cliente);


                if (resumoCliente == null) {

                    resumoCliente =
                            new ClienteResumo();

                    resumoCliente.cliente =
                            cliente;

                    resumoCliente.cnpj =
                            valorTexto(
                                    dto.getCnpjDestinatario()
                            );

                    clientes.put(
                            cliente,
                            resumoCliente
                    );
                }


                resumoCliente.itens++;


                if (dto.getQuantidade() != null) {

                    resumoCliente.quantidade =
                            resumoCliente.quantidade.add(
                                    dto.getQuantidade()
                            );
                }


                //==================================================
                // NF DO CLIENTE
                //
                // O valor da NF também entra somente uma vez
                // para cada cliente.
                //==================================================

                if (!resumoCliente.notas.contains(nf)) {

                    resumoCliente.notas.add(nf);

                    resumoCliente.valorTotal =
                            resumoCliente.valorTotal.add(
                                    valorBigDecimal(
                                            dto.getValorTotalNF()
                                    )
                            );
                }


                //==================================================
                // ÚLTIMA VENDA DO CLIENTE
                //==================================================

                if (dataVenda != null) {

                    if (resumoCliente.dataUltimaVenda == null ||
                            dataVenda.isAfter(
                                    resumoCliente.dataUltimaVenda
                            )) {

                        resumoCliente.dataUltimaVenda =
                                dataVenda;
                    }
                }
            }


            //==================================================
            // RESUMO GERAL
            //==================================================

            int totalNotas =
                    valoresPorNF.size();


            BigDecimal valorTotal =
                    BigDecimal.ZERO;


            for (BigDecimal valorNF :
                    valoresPorNF.values()) {

                valorTotal =
                        valorTotal.add(
                                valorBigDecimal(valorNF)
                        );
            }


            BigDecimal ticketMedio =
                    BigDecimal.ZERO;


            if (totalNotas > 0) {

                ticketMedio =
                        valorTotal.divide(
                                BigDecimal.valueOf(
                                        totalNotas
                                ),
                                2,
                                java.math.RoundingMode.HALF_UP
                        );
            }


            //==================================================
            // TÍTULO
            //==================================================

            Row linha =
                    sheet.createRow(0);

            Cell titulo =
                    linha.createCell(0);

            titulo.setCellValue(
                    "INTELIFISCAL"
            );

            titulo.setCellStyle(
                    estiloTitulo
            );

            sheet.addMergedRegion(
                    new CellRangeAddress(
                            0,
                            0,
                            0,
                            4
                    )
            );


            //==================================================
            // NOME DO RELATÓRIO
            //==================================================

            linha =
                    sheet.createRow(1);

            Cell nomeRelatorio =
                    linha.createCell(0);

            nomeRelatorio.setCellValue(
                    "RESUMO DE VENDAS"
            );

            nomeRelatorio.setCellStyle(
                    estiloSubtitulo
            );

            sheet.addMergedRegion(
                    new CellRangeAddress(
                            1,
                            1,
                            0,
                            4
                    )
            );


            //==================================================
            // PERÍODO
            //==================================================

            linha =
                    sheet.createRow(2);

            Cell periodo =
                    linha.createCell(0);


            String textoPeriodo;


            if (menorData != null &&
                    maiorData != null) {

                textoPeriodo =
                        "Período analisado: "
                                + menorData
                                .format(
                                        java.time.format.DateTimeFormatter
                                                .ofPattern(
                                                        "dd/MM/yyyy"
                                                )
                                )
                                + " até "
                                + maiorData
                                .format(
                                        java.time.format.DateTimeFormatter
                                                .ofPattern(
                                                        "dd/MM/yyyy"
                                                )
                                );

            } else {

                textoPeriodo =
                        "Período analisado: Não disponível";
            }


            periodo.setCellValue(
                    textoPeriodo
            );

            periodo.setCellStyle(
                    estiloPeriodo
            );

            sheet.addMergedRegion(
                    new CellRangeAddress(
                            2,
                            2,
                            0,
                            4
                    )
            );


            //==================================================
            // RESUMO GERAL
            //==================================================

            linha =
                    sheet.createRow(4);

            Cell secao =
                    linha.createCell(0);

            secao.setCellValue(
                    "RESUMO GERAL"
            );

            secao.setCellStyle(
                    estiloSecao
            );

            sheet.addMergedRegion(
                    new CellRangeAddress(
                            4,
                            4,
                            0,
                            4
                    )
            );


            //==================================================
            // CABEÇALHO DO RESUMO
            //==================================================

            linha =
                    sheet.createRow(5);

            criarCabecalho(
                    linha,
                    estiloCabecalho,
                    "NOTAS",
                    "ITENS",
                    "QUANTIDADE",
                    "VALOR TOTAL",
                    "TICKET MÉDIO"
            );


            //==================================================
            // DADOS DO RESUMO
            //==================================================

            linha =
                    sheet.createRow(6);


            Cell notas =
                    linha.createCell(0);

            notas.setCellValue(
                    totalNotas
            );

            notas.setCellStyle(
                    estiloInteiro
            );


            Cell itens =
                    linha.createCell(1);

            itens.setCellValue(
                    totalItens
            );

            itens.setCellStyle(
                    estiloInteiro
            );


            Cell quantidade =
                    linha.createCell(2);

            quantidade.setCellValue(
                    totalQuantidade.doubleValue()
            );

            quantidade.setCellStyle(
                    estiloQuantidade
            );


            Cell valorTotalCell =
                    linha.createCell(3);

            valorTotalCell.setCellValue(
                    valorTotal.doubleValue()
            );

            valorTotalCell.setCellStyle(
                    estiloMoeda
            );


            Cell ticket =
                    linha.createCell(4);

            ticket.setCellValue(
                    ticketMedio.doubleValue()
            );

            ticket.setCellStyle(
                    estiloMoeda
            );


            //==================================================
            // VENDAS POR CLIENTE
            //==================================================

            linha =
                    sheet.createRow(8);

            Cell secaoClientes =
                    linha.createCell(0);

            secaoClientes.setCellValue(
                    "VENDAS POR CLIENTE"
            );

            secaoClientes.setCellStyle(
                    estiloSecao
            );

            sheet.addMergedRegion(
                    new CellRangeAddress(
                            8,
                            8,
                            0,
                            4
                    )
            );


            //==================================================
            // CABEÇALHO CLIENTES
            //==================================================

            linha =
                    sheet.createRow(9);

            criarCabecalho(
                    linha,
                    estiloCabecalho,
                    "CLIENTE",
                    "NOTAS",
                    "DATA DA ÚLTIMA VENDA",
                    "QUANTIDADE",
                    "VALOR TOTAL"
            );


            //==================================================
            // DADOS DOS CLIENTES
            //==================================================

            int linhaCliente = 10;


            for (ClienteResumo clienteResumo :
                    clientes.values()) {

                linha =
                        sheet.createRow(
                                linhaCliente++
                        );


                //==================================================
                // CLIENTE
                //==================================================

                Cell nomeCliente =
                        linha.createCell(0);

                nomeCliente.setCellValue(
                        clienteResumo.cliente
                );

                nomeCliente.setCellStyle(
                        estiloTexto
                );


                //==================================================
                // NOTAS
                //==================================================

                Cell notasCliente =
                        linha.createCell(1);

                notasCliente.setCellValue(
                        clienteResumo.notas.size()
                );

                notasCliente.setCellStyle(
                        estiloInteiro
                );


                //==================================================
                // DATA DA ÚLTIMA VENDA
                //==================================================

                Cell dataUltimaVenda =
                        linha.createCell(2);


                if (clienteResumo.dataUltimaVenda != null) {

                    dataUltimaVenda.setCellValue(
                            clienteResumo.dataUltimaVenda
                    );

                    dataUltimaVenda.setCellStyle(
                            estiloData
                    );

                } else {

                    dataUltimaVenda.setCellValue(
                            ""
                    );

                    dataUltimaVenda.setCellStyle(
                            estiloTexto
                    );
                }


                //==================================================
                // QUANTIDADE
                //==================================================

                Cell quantidadeCliente =
                        linha.createCell(3);

                quantidadeCliente.setCellValue(
                        clienteResumo.quantidade.doubleValue()
                );

                quantidadeCliente.setCellStyle(
                        estiloQuantidade
                );


                //==================================================
                // VALOR TOTAL
                //==================================================

                Cell valorCliente =
                        linha.createCell(4);

                valorCliente.setCellValue(
                        clienteResumo.valorTotal.doubleValue()
                );

                valorCliente.setCellStyle(
                        estiloMoeda
                );
            }


            //==================================================
            // FILTRO
            //==================================================

            if (linhaCliente > 10) {

                sheet.setAutoFilter(
                        new CellRangeAddress(
                                9,
                                linhaCliente - 1,
                                0,
                                4
                        )
                );
            }


            //==================================================
            // CONGELAR
            //==================================================

            sheet.createFreezePane(
                    0,
                    10
            );


            //==================================================
            // LARGURA DAS COLUNAS
            //==================================================

            sheet.setColumnWidth(
                    0,
                    42 * 256
            );

            sheet.setColumnWidth(
                    1,
                    12 * 256
            );

            sheet.setColumnWidth(
                    2,
                    22 * 256
            );

            sheet.setColumnWidth(
                    3,
                    18 * 256
            );

            sheet.setColumnWidth(
                    4,
                    20 * 256
            );


            //==================================================
            // CONFIGURAÇÃO DE IMPRESSÃO
            //==================================================

            PrintSetup printSetup =
                    sheet.getPrintSetup();

            printSetup.setLandscape(
                    true
            );

            printSetup.setFitWidth(
                    (short) 1
            );

            printSetup.setFitHeight(
                    (short) 0
            );

            sheet.setFitToPage(
                    true
            );

            sheet.setAutobreaks(
                    true
            );

            sheet.setPrintGridlines(
                    false
            );

            sheet.setDisplayGridlines(
                    false
            );

            sheet.setHorizontallyCenter(
                    true
            );


            //==================================================
            // ABA 2 - DETALHAMENTO DE VENDAS
            //==================================================

            Sheet detalhe =
                    workbook.createSheet(
                            "Detalhamento de Vendas"
                    );


            //==================================================
            // ESTILOS DO DETALHAMENTO
            //==================================================

            CellStyle estiloCabecalhoDetalhe =
                    criarEstiloCabecalho(workbook);

            CellStyle estiloDataDetalhe =
                    workbook.createCellStyle();

            estiloDataDetalhe.setDataFormat(
                    workbook.createDataFormat()
                            .getFormat("dd/MM/yyyy")
            );


            CellStyle estiloMoedaDetalhe =
                    criarEstiloMoeda(workbook);


            CellStyle estiloQuantidadeDetalhe =
                    criarEstiloQuantidade(workbook);


            //==================================================
            // CABEÇALHO
            //==================================================

            Row cabecalho =
                    detalhe.createRow(0);


            criarCabecalho(
                    cabecalho,
                    estiloCabecalhoDetalhe,
                    "NF",
                    "DATA DA EMISSÃO",
                    "DESTINATÁRIO",
                    "CNPJ DESTINATÁRIO",
                    "VALOR TOTAL NF",
                    "CÓDIGO ITEM",
                    "DESCRIÇÃO ITEM",
                    "CFOP",
                    "QUANTIDADE",
                    "VALOR UNITÁRIO"
            );


            //==================================================
            // DADOS
            //==================================================

            int linhaDetalhe = 1;


            for (ResumoVendaDTO dto :
                    dados) {

                if (dto == null) {
                    continue;
                }


                Row row =
                        detalhe.createRow(
                                linhaDetalhe++
                        );


                //==================================================
                // NF
                //==================================================

                row.createCell(0)
                        .setCellValue(
                                valorTexto(
                                        dto.getNf()
                                )
                        );


                //==================================================
                // DATA
                //==================================================

                Cell cellData =
                        row.createCell(1);


                LocalDate data =
                        null;


                if (dto.getDataEmissao() != null &&
                        !dto.getDataEmissao().isBlank()) {

                    try {

                        String texto =
                                dto.getDataEmissao();

                        if (texto.length() >= 10 &&
                                texto.charAt(4) == '-' &&
                                texto.charAt(7) == '-') {

                            data =
                                    LocalDate.parse(
                                            texto.substring(
                                                    0,
                                                    10
                                            )
                                    );

                        } else {

                            data =
                                    LocalDate.parse(
                                            texto,
                                            java.time.format.DateTimeFormatter
                                                    .ofPattern(
                                                            "dd/MM/yyyy"
                                                    )
                                    );
                        }

                    } catch (Exception ignored) {
                    }
                }


                if (data != null) {

                    cellData.setCellValue(
                            data
                    );

                    cellData.setCellStyle(
                            estiloDataDetalhe
                    );

                } else {

                    cellData.setCellValue(
                            valorTexto(
                                    dto.getDataEmissao()
                            )
                    );
                }


                //==================================================
                // DESTINATÁRIO
                //==================================================

                row.createCell(2)
                        .setCellValue(
                                valorTexto(
                                        dto.getDestinatario()
                                )
                        );


                //==================================================
                // CNPJ
                //==================================================

                row.createCell(3)
                        .setCellValue(
                                valorTexto(
                                        dto.getCnpjDestinatario()
                                )
                        );


                //==================================================
                // VALOR TOTAL NF
                //==================================================

                Cell cellValorNF =
                        row.createCell(4);


                cellValorNF.setCellValue(
                        valorBigDecimal(
                                dto.getValorTotalNF()
                        ).doubleValue()
                );

                cellValorNF.setCellStyle(
                        estiloMoedaDetalhe
                );


                //==================================================
                // CÓDIGO ITEM
                //==================================================

                row.createCell(5)
                        .setCellValue(
                                valorTexto(
                                        dto.getCodigoItem()
                                )
                        );


                //==================================================
                // DESCRIÇÃO
                //==================================================

                row.createCell(6)
                        .setCellValue(
                                valorTexto(
                                        dto.getDescricaoItem()
                                )
                        );


                //==================================================
                // CFOP
                //==================================================

                row.createCell(7)
                        .setCellValue(
                                valorTexto(
                                        dto.getCfop()
                                )
                        );


                //==================================================
                // QUANTIDADE
                //==================================================

                Cell cellQuantidade =
                        row.createCell(8);


                if (dto.getQuantidade() != null) {

                    cellQuantidade.setCellValue(
                            dto.getQuantidade()
                                    .doubleValue()
                    );
                }


                cellQuantidade.setCellStyle(
                        estiloQuantidadeDetalhe
                );


                //==================================================
                // VALOR UNITÁRIO
                //==================================================

                Cell cellValorUnitario =
                        row.createCell(9);


                if (dto.getValorUnitario() != null) {

                    cellValorUnitario.setCellValue(
                            dto.getValorUnitario()
                                    .doubleValue()
                    );
                }


                cellValorUnitario.setCellStyle(
                        estiloMoedaDetalhe
                );
            }


            //==================================================
            // FILTRO DETALHAMENTO
            //==================================================

            if (!dados.isEmpty()) {

                detalhe.setAutoFilter(
                        new CellRangeAddress(
                                0,
                                linhaDetalhe - 1,
                                0,
                                9
                        )
                );
            }


            //==================================================
            // CONGELAR CABEÇALHO
            //==================================================

            detalhe.createFreezePane(
                    0,
                    1
            );


            //==================================================
            // LARGURAS
            //==================================================

            detalhe.setColumnWidth(
                    0,
                    12 * 256
            );

            detalhe.setColumnWidth(
                    1,
                    18 * 256
            );

            detalhe.setColumnWidth(
                    2,
                    35 * 256
            );

            detalhe.setColumnWidth(
                    3,
                    20 * 256
            );

            detalhe.setColumnWidth(
                    4,
                    18 * 256
            );

            detalhe.setColumnWidth(
                    5,
                    18 * 256
            );

            detalhe.setColumnWidth(
                    6,
                    40 * 256
            );

            detalhe.setColumnWidth(
                    7,
                    12 * 256
            );

            detalhe.setColumnWidth(
                    8,
                    15 * 256
            );

            detalhe.setColumnWidth(
                    9,
                    18 * 256
            );


            //==================================================
            // IMPRESSÃO DETALHAMENTO
            //==================================================

            PrintSetup printSetupDetalhe =
                    detalhe.getPrintSetup();

            printSetupDetalhe.setLandscape(
                    true
            );

            printSetupDetalhe.setFitWidth(
                    (short) 1
            );

            printSetupDetalhe.setFitHeight(
                    (short) 0
            );

            detalhe.setFitToPage(
                    true
            );

            detalhe.setPrintGridlines(
                    false
            );

            detalhe.setDisplayGridlines(
                    false
            );


            //==================================================
            // GRAVAR
            //==================================================

            workbook.write(output);


        } catch (IOException e) {

            throw new RuntimeException(
                    "Erro ao gerar o relatório de vendas.",
                    e
            );
        }
    }


    //==================================================
    // CLASSE AUXILIAR DO RESUMO DE CLIENTES
    //==================================================

    private static class ClienteResumo {

        private String cliente = "";

        private String cnpj = "";

        private int itens = 0;

        private final java.util.Set<String> notas =
                new java.util.LinkedHashSet<>();

        private LocalDate dataUltimaVenda;

        private BigDecimal quantidade =
                BigDecimal.ZERO;

        private BigDecimal valorTotal =
                BigDecimal.ZERO;
    }
}