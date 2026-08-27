package br.com.intelifiscal.service.relatorio.exportacao;

import br.com.intelifiscal.dto.periodo.ResumoMensalDTO;
import br.com.intelifiscal.dto.periodo.ResumoPeriodoDTO;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.util.CellRangeAddress;
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

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
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
}