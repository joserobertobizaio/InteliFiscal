package br.com.intelifiscal.service.relatorio.exportacao;

import br.com.intelifiscal.dto.periodo.ResumoMensalDTO;
import br.com.intelifiscal.dto.periodo.ResumoPeriodoDTO;
import br.com.intelifiscal.dto.relatorio.DetalhamentoCompraDTO;
import br.com.intelifiscal.entity.NFeDuplicata;
import br.com.intelifiscal.repository.NFeDuplicataRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import br.com.intelifiscal.dto.relatorio.DetalhamentoVendaDTO;

import java.awt.Color;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.nio.file.Path;
import java.util.ArrayList;
import java.text.NumberFormat;
import java.util.Locale;

public class PdfRelatorioService {

    private final NFeDuplicataRepository nfeDuplicataRepository;

    public PdfRelatorioService() {
        this.nfeDuplicataRepository = new NFeDuplicataRepository();
    }

    //==================================================
    // GERAR RELATÓRIO
    //==================================================

    public void gerarResumoPeriodo(
            List<ResumoPeriodoDTO> dadosPeriodo,
            List<ResumoMensalDTO> dadosMensais,
            Path caminhoArquivo
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
                PDDocument documento =
                        new PDDocument()
        ) {

            PDPage pagina =
                    new PDPage(
                            PDRectangle.A4
                    );

            pagina.setRotation(90);

            documento.addPage(
                    pagina
            );

            try (
                    PDPageContentStream stream =
                            new PDPageContentStream(
                                    documento,
                                    pagina
                            )
            ) {

                float largura =
                        pagina.getMediaBox()
                                .getHeight();

                float altura =
                        pagina.getMediaBox()
                                .getWidth();

                float margem = 40;

                //==================================================
                // TÍTULO
                //==================================================

                escreverTexto(
                        stream,
                        "INTELIFISCAL",
                        margem,
                        altura - 45,
                        20,
                        true
                );

                //==================================================
                // SUBTÍTULO
                //==================================================

                escreverTexto(
                        stream,
                        "RELATÓRIO GERENCIAL — ÚLTIMOS 12 MESES",
                        margem,
                        altura - 70,
                        14,
                        true
                );

                //==================================================
                // PERÍODO
                //==================================================

                String periodo =
                        obterPeriodo(
                                dadosMensais
                        );

                escreverTexto(
                        stream,
                        "Período analisado: " + periodo,
                        margem,
                        altura - 92,
                        10,
                        false
                );

                //==================================================
                // RESUMO EXECUTIVO
                //==================================================

                escreverTexto(
                        stream,
                        "RESUMO EXECUTIVO",
                        margem,
                        altura - 130,
                        12,
                        true
                );

                BigDecimal totalCompras =
                        BigDecimal.ZERO;

                BigDecimal totalVendas =
                        BigDecimal.ZERO;

                for (
                        ResumoPeriodoDTO dto
                        : dadosPeriodo
                ) {

                    if (dto == null) {
                        continue;
                    }

                    BigDecimal valor =
                            dto.getValorTotal();

                    if (valor == null) {
                        valor = BigDecimal.ZERO;
                    }

                    if (
                            "COMPRA".equalsIgnoreCase(
                                    dto.getOperacao()
                            )
                    ) {

                        totalCompras =
                                totalCompras.add(
                                        valor
                                );

                    } else if (
                            "VENDA".equalsIgnoreCase(
                                    dto.getOperacao()
                            )
                    ) {

                        totalVendas =
                                totalVendas.add(
                                        valor
                                );
                    }
                }

                BigDecimal diferenca =
                        totalVendas.subtract(
                                totalCompras
                        );

                //==================================================
                // INDICADORES
                //==================================================

                float yIndicadores =
                        altura - 165;

                escreverTexto(
                        stream,
                        "Total de Compras",
                        margem,
                        yIndicadores,
                        10,
                        true
                );

                escreverTexto(
                        stream,
                        formatarMoeda(
                                totalCompras
                        ),
                        margem,
                        yIndicadores - 18,
                        13,
                        false
                );

                escreverTexto(
                        stream,
                        "Total de Vendas",
                        230,
                        yIndicadores,
                        10,
                        true
                );

                escreverTexto(
                        stream,
                        formatarMoeda(
                                totalVendas
                        ),
                        230,
                        yIndicadores - 18,
                        13,
                        false
                );

                escreverTexto(
                        stream,
                        "Diferença",
                        420,
                        yIndicadores,
                        10,
                        true
                );

                escreverTexto(
                        stream,
                        formatarMoeda(
                                diferenca
                        ),
                        420,
                        yIndicadores - 18,
                        13,
                        false
                );

                //==================================================
                // RELAÇÃO VENDAS / COMPRAS
                //==================================================

                String relacao =
                        "0,00x";

                if (
                        totalCompras.compareTo(
                                BigDecimal.ZERO
                        ) > 0
                ) {

                    relacao =
                            String.format(
                                    Locale.US,
                                    "%.2fx",
                                    totalVendas
                                            .divide(
                                                    totalCompras,
                                                    2,
                                                    java.math.RoundingMode.HALF_UP
                                            )
                            ).replace(
                                    ".",
                                    ","
                            );
                }

                escreverTexto(
                        stream,
                        "Relação Vendas / Compras: "
                                + relacao,
                        margem,
                        yIndicadores - 48,
                        10,
                        true
                );

                //==================================================
                // EVOLUÇÃO MENSAL
                //==================================================

                escreverTexto(
                        stream,
                        "EVOLUÇÃO MENSAL",
                        margem,
                        altura - 250,
                        12,
                        true
                );

                float yTabela =
                        altura - 275;

                // Cabeçalho
                escreverTexto(
                        stream,
                        "MÊS",
                        margem,
                        yTabela,
                        9,
                        true
                );

                escreverTexto(
                        stream,
                        "COMPRAS",
                        180,
                        yTabela,
                        9,
                        true
                );

                escreverTexto(
                        stream,
                        "VENDAS",
                        320,
                        yTabela,
                        9,
                        true
                );

                escreverTexto(
                        stream,
                        "DIFERENÇA",
                        460,
                        yTabela,
                        9,
                        true
                );

                float linhaY =
                        yTabela - 20;

                //==================================================
                // AGRUPAR COMPRAS E VENDAS POR MÊS
                //==================================================

                Map<String, BigDecimal> comprasPorMes =
                        new LinkedHashMap<>();

                Map<String, BigDecimal> vendasPorMes =
                        new LinkedHashMap<>();

                for (
                        ResumoMensalDTO dto
                        : dadosMensais
                ) {

                    if (dto == null) {
                        continue;
                    }

                    String mes =
                            dto.getMes();

                    if (mes == null) {
                        continue;
                    }

                    BigDecimal valor =
                            valorDecimal(
                                    dto.getValorTotal()
                            );

                    if (
                            "COMPRA".equalsIgnoreCase(
                                    dto.getOperacao()
                            )
                    ) {

                        comprasPorMes.merge(
                                mes,
                                valor,
                                BigDecimal::add
                        );

                    } else if (
                            "VENDA".equalsIgnoreCase(
                                    dto.getOperacao()
                            )
                    ) {

                        vendasPorMes.merge(
                                mes,
                                valor,
                                BigDecimal::add
                        );
                    }
                }


            //==================================================
            // LISTA DOS MESES
            //==================================================

                Map<String, BigDecimal> meses =
                        new LinkedHashMap<>();

                for (
                        ResumoMensalDTO dto
                        : dadosMensais
                ) {

                    if (dto == null) {
                        continue;
                    }

                    if (dto.getMes() != null) {

                        meses.putIfAbsent(
                                dto.getMes(),
                                BigDecimal.ZERO
                        );
                    }
                }


            //==================================================
            // ESCREVER TABELA
            //==================================================

                for (
                        String mes
                        : meses.keySet()
                ) {

                    BigDecimal compras =
                            comprasPorMes.getOrDefault(
                                    mes,
                                    BigDecimal.ZERO
                            );

                    BigDecimal vendas =
                            vendasPorMes.getOrDefault(
                                    mes,
                                    BigDecimal.ZERO
                            );

                    BigDecimal diferencaMensal =
                            vendas.subtract(
                                    compras
                            );


                    escreverTexto(
                            stream,
                            mes,
                            margem,
                            linhaY,
                            8,
                            false
                    );


                    escreverTexto(
                            stream,
                            formatarMoeda(
                                    compras
                            ),
                            180,
                            linhaY,
                            8,
                            false
                    );


                    escreverTexto(
                            stream,
                            formatarMoeda(
                                    vendas
                            ),
                            320,
                            linhaY,
                            8,
                            false
                    );


                    escreverTexto(
                            stream,
                            formatarMoeda(
                                    diferencaMensal
                            ),
                            460,
                            linhaY,
                            8,
                            false
                    );


                    linhaY -= 17;
                }

                //==================================================
                // RODAPÉ
                //==================================================

                escreverTexto(
                        stream,
                        "InteliFiscal — Relatório gerado pelo sistema",
                        margem,
                        25,
                        8,
                        false
                );
            }

            //==================================================
            // SEGUNDA PÁGINA - GRÁFICO
            //==================================================

            desenharGraficoComprasVendas(
                    documento,
                    dadosMensais
            );


            documento.save(
                    caminhoArquivo.toFile()
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Erro ao gerar o relatório PDF.",
                    e
            );
        }
    }

    //==================================================
    // PERÍODO
    //==================================================

    private String obterPeriodo(
            List<ResumoMensalDTO> dadosMensais
    ) {

        if (
                dadosMensais == null
                        || dadosMensais.isEmpty()
        ) {

            return "Não informado";
        }

        String primeiro =
                valorTexto(
                        dadosMensais
                                .get(0)
                                .getMes()
                );

        String ultimo =
                valorTexto(
                        dadosMensais
                                .get(
                                        dadosMensais.size() - 1
                                )
                                .getMes()
                );

        return primeiro
                + " a "
                + ultimo;
    }

    //==================================================
    // VALOR DECIMAL
    //==================================================

    private BigDecimal valorDecimal(
            BigDecimal valor
    ) {

        return valor == null
                ? BigDecimal.ZERO
                : valor;
    }

    //==================================================
    // MOEDA
    //==================================================

    private String formatarMoeda(
            BigDecimal valor
    ) {

        NumberFormat formato =
                NumberFormat.getCurrencyInstance(
                        new Locale(
                                "pt",
                                "BR"
                        )
                );

        return formato.format(
                valorDecimal(valor)
        );
    }

    //==================================================
    // TEXTO
    //==================================================

    private String valorTexto(
            String valor
    ) {

        return valor == null
                ? ""
                : valor;
    }

    //==================================================
    // ESCREVER TEXTO
    //==================================================

    private void escreverTexto(
            PDPageContentStream stream,
            String texto,
            float x,
            float y,
            float tamanho,
            boolean negrito
    ) throws IOException {

        PDType1Font fonte =
                new PDType1Font(
                        negrito
                                ? Standard14Fonts.FontName.HELVETICA_BOLD
                                : Standard14Fonts.FontName.HELVETICA
                );

        stream.beginText();

        stream.setFont(
                fonte,
                tamanho
        );

        stream.newLineAtOffset(
                x,
                y
        );

        stream.showText(
                texto
        );

        stream.endText();
    }

    //==================================================
    // GRÁFICO COMPRAS X VENDAS
    //==================================================

    private void desenharGraficoComprasVendas(
            PDDocument documento,
            List<ResumoMensalDTO> dadosMensais
    ) throws IOException {

        //==================================================
        // CRIAR PÁGINA A4 - PAISAGEM
        //==================================================

        PDPage paginaGrafico =
                new PDPage(
                        new PDRectangle(
                                PDRectangle.A4.getHeight(),
                                PDRectangle.A4.getWidth()
                        )
                );

        documento.addPage(paginaGrafico);


        try (
                PDPageContentStream stream =
                        new PDPageContentStream(
                                documento,
                                paginaGrafico
                        )
        ) {

            //==================================================
            // DIMENSÕES
            //==================================================

            float largura =
                    paginaGrafico.getMediaBox().getWidth();

            float altura =
                    paginaGrafico.getMediaBox().getHeight();

            float margem = 40;


            //==================================================
            // TÍTULO
            //==================================================

            escreverTexto(
                    stream,
                    "INTELIFISCAL",
                    margem,
                    altura - 45,
                    20,
                    true
            );

            escreverTexto(
                    stream,
                    "COMPRAS X VENDAS POR MÊS",
                    margem,
                    altura - 70,
                    14,
                    true
            );

            escreverTexto(
                    stream,
                    "Evolução dos valores movimentados nos últimos 12 meses.",
                    margem,
                    altura - 92,
                    10,
                    false
            );


            //==================================================
            // AGRUPAR DADOS
            //==================================================

            Map<String, BigDecimal> comprasPorMes =
                    new LinkedHashMap<>();

            Map<String, BigDecimal> vendasPorMes =
                    new LinkedHashMap<>();


            for (ResumoMensalDTO dto : dadosMensais) {

                if (dto == null) {
                    continue;
                }

                String mes = dto.getMes();

                if (mes == null) {
                    continue;
                }

                BigDecimal valor =
                        valorDecimal(
                                dto.getValorTotal()
                        );


                if ("COMPRA".equalsIgnoreCase(
                        dto.getOperacao()
                )) {

                    comprasPorMes.merge(
                            mes,
                            valor,
                            BigDecimal::add
                    );

                } else if ("VENDA".equalsIgnoreCase(
                        dto.getOperacao()
                )) {

                    vendasPorMes.merge(
                            mes,
                            valor,
                            BigDecimal::add
                    );
                }
            }


            //==================================================
            // LISTA DOS MESES
            //==================================================

            Map<String, Boolean> meses =
                    new LinkedHashMap<>();


            for (ResumoMensalDTO dto : dadosMensais) {

                if (dto == null) {
                    continue;
                }

                if (dto.getMes() != null) {

                    meses.putIfAbsent(
                            dto.getMes(),
                            true
                    );
                }
            }


            //==================================================
            // SEM DADOS
            //==================================================

            if (meses.isEmpty()) {

                escreverTexto(
                        stream,
                        "Não existem dados mensais para apresentar.",
                        margem,
                        altura - 150,
                        11,
                        false
                );

                return;
            }


            //==================================================
            // MAIOR VALOR
            //==================================================

            BigDecimal maiorValor =
                    BigDecimal.ZERO;


            for (String mes : meses.keySet()) {

                BigDecimal compras =
                        comprasPorMes.getOrDefault(
                                mes,
                                BigDecimal.ZERO
                        );

                BigDecimal vendas =
                        vendasPorMes.getOrDefault(
                                mes,
                                BigDecimal.ZERO
                        );


                if (compras.compareTo(maiorValor) > 0) {
                    maiorValor = compras;
                }

                if (vendas.compareTo(maiorValor) > 0) {
                    maiorValor = vendas;
                }
            }


            //==================================================
            // ÁREA DO GRÁFICO
            //==================================================

            float eixoX = 70;
            float eixoY = 145;

            float graficoLargura = 680;
            float graficoAltura = 300;


            //==================================================
            // ESCALA
            //==================================================

            float maiorValorFloat =
                    maiorValor.floatValue();


            if (maiorValorFloat <= 0) {
                maiorValorFloat = 1;
            }


            float passoY =
                    graficoAltura / 5;


            //==================================================
            // GRADE HORIZONTAL
            //==================================================

            stream.setStrokingColor(
                    new Color(
                            210,
                            210,
                            210
                    )
            );


            for (int i = 0; i <= 5; i++) {

                float y =
                        eixoY
                                + (passoY * i);


                stream.moveTo(
                        eixoX,
                        y
                );

                stream.lineTo(
                        eixoX + graficoLargura,
                        y
                );

                stream.stroke();


                BigDecimal valorEscala =
                        BigDecimal.valueOf(
                                        maiorValorFloat
                                )
                                .multiply(
                                        BigDecimal.valueOf(i)
                                )
                                .divide(
                                        BigDecimal.valueOf(5),
                                        2,
                                        java.math.RoundingMode.HALF_UP
                                );


                escreverTexto(
                        stream,
                        formatarMoedaCompacta(
                                valorEscala
                        ),
                        8,
                        y - 3,
                        7,
                        false
                );
            }


            //==================================================
            // VOLTAR PARA PRETO
            //==================================================

            stream.setStrokingColor(
                    Color.BLACK
            );

            stream.setNonStrokingColor(
                    Color.BLACK
            );


            //==================================================
            // EIXO VERTICAL
            //==================================================

            stream.moveTo(
                    eixoX,
                    eixoY
            );

            stream.lineTo(
                    eixoX,
                    eixoY + graficoAltura
            );

            stream.stroke();


            //==================================================
            // EIXO HORIZONTAL
            //==================================================

            stream.moveTo(
                    eixoX,
                    eixoY
            );

            stream.lineTo(
                    eixoX + graficoLargura,
                    eixoY
            );

            stream.stroke();


            //==================================================
            // BARRAS
            //==================================================

            int quantidadeMeses =
                    meses.size();

            float larguraGrupo =
                    graficoLargura
                            / quantidadeMeses;

            float larguraBarra =
                    Math.min(
                            22,
                            larguraGrupo * 0.28f
                    );


            int indice = 0;


            for (String mes : meses.keySet()) {

                BigDecimal compras =
                        comprasPorMes.getOrDefault(
                                mes,
                                BigDecimal.ZERO
                        );

                BigDecimal vendas =
                        vendasPorMes.getOrDefault(
                                mes,
                                BigDecimal.ZERO
                        );


                float alturaCompra =
                        compras.floatValue()
                                / maiorValorFloat
                                * graficoAltura;

                float alturaVenda =
                        vendas.floatValue()
                                / maiorValorFloat
                                * graficoAltura;


                float centroGrupo =
                        eixoX
                                + (indice * larguraGrupo)
                                + (larguraGrupo / 2);


                float xCompra =
                        centroGrupo
                                - larguraBarra
                                - 3;


                float xVenda =
                        centroGrupo
                                + 3;


                //==================================================
                // COR DAS COMPRAS - VERMELHO
                //==================================================

                stream.setNonStrokingColor(
                        new Color(
                                231,
                                76,
                                60
                        )
                );


                stream.addRect(
                        xCompra,
                        eixoY,
                        larguraBarra,
                        alturaCompra
                );

                stream.fill();


                //==================================================
                // COR DAS VENDAS - LARANJA
                //==================================================

                stream.setNonStrokingColor(
                        new Color(
                                243,
                                156,
                                18
                        )
                );


                stream.addRect(
                        xVenda,
                        eixoY,
                        larguraBarra,
                        alturaVenda
                );

                stream.fill();


                //==================================================
                // VOLTAR PARA PRETO
                //==================================================

                stream.setNonStrokingColor(
                        Color.BLACK
                );


                //==================================================
                // MÊS
                //==================================================

                escreverTexto(
                        stream,
                        formatarMes(mes),
                        centroGrupo - 17,
                        eixoY - 18,
                        7,
                        false
                );


                indice++;
            }


            //==================================================
            // LEGENDA
            //==================================================

            float legendaY =
                    eixoY - 55;


            //==================================================
            // LEGENDA - COMPRAS
            //==================================================

            stream.setNonStrokingColor(
                    new Color(
                            231,
                            76,
                            60
                    )
            );


            stream.addRect(
                    280,
                    legendaY,
                    10,
                    10
            );

            stream.fill();


            stream.setNonStrokingColor(
                    Color.BLACK
            );


            escreverTexto(
                    stream,
                    "Compras",
                    295,
                    legendaY + 1,
                    9,
                    false
            );


            //==================================================
            // LEGENDA - VENDAS
            //==================================================

            stream.setNonStrokingColor(
                    new Color(
                            243,
                            156,
                            18
                    )
            );


            stream.addRect(
                    380,
                    legendaY,
                    10,
                    10
            );

            stream.fill();


            stream.setNonStrokingColor(
                    Color.BLACK
            );


            escreverTexto(
                    stream,
                    "Vendas",
                    395,
                    legendaY + 1,
                    9,
                    false
            );


            //==================================================
            // RODAPÉ
            //==================================================

            escreverTexto(
                    stream,
                    "InteliFiscal — Relatório gerado pelo sistema",
                    margem,
                    25,
                    8,
                    false
            );
        }
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
                    mes.substring(
                            0,
                            4
                    );

            String numeroMes =
                    mes.substring(
                            5,
                            7
                    );

            return numeroMes
                    + "/"
                    + ano;
        }

        return mes;
    }


    //==================================================
    // MOEDA COMPACTA
    //==================================================

    private String formatarMoedaCompacta(
            BigDecimal valor
    ) {

        if (valor == null) {
            valor = BigDecimal.ZERO;
        }


        double numero =
                valor.doubleValue();


        if (numero >= 1_000_000) {

            return String.format(
                    Locale.US,
                    "%.1f mi",
                    numero / 1_000_000
            ).replace(
                    ".",
                    ","
            );
        }


        if (numero >= 1_000) {

            return String.format(
                    Locale.US,
                    "%.0f mil",
                    numero / 1_000
            );
        }


        return formatarMoeda(
                valor
        );
    }

    //==================================================
    // RELATÓRIO SINTÉTICO DE VENDAS POR CLIENTE
    //==================================================

    public void gerarRelatorioSinteticoVendas(
            List<DetalhamentoVendaDTO> dados,
            Path caminhoArquivo,
            String periodoRelatorio
    ) {

        if (dados == null) {
            throw new IllegalArgumentException(
                    "Os dados do relatório não podem ser nulos."
            );
        }

        if (caminhoArquivo == null) {
            throw new IllegalArgumentException(
                    "O caminho do arquivo não pode ser nulo."
            );
        }

        if (dados.isEmpty()) {
            throw new IllegalArgumentException(
                    "Não existem vendas para gerar o relatório."
            );
        }

        try (
                PDDocument documento =
                        new PDDocument()
        ) {

            //==================================================
            // AGRUPAR NOTAS
            //==================================================

            Map<String, DetalhamentoVendaDTO> notas =
                    new LinkedHashMap<>();

            for (DetalhamentoVendaDTO dto : dados) {

                if (dto == null) {
                    continue;
                }

                String numeroNota =
                        dto.getNumeroNota();

                if (numeroNota == null ||
                        numeroNota.isBlank()) {

                    continue;
                }

                notas.putIfAbsent(
                        numeroNota,
                        dto
                );
            }

            if (notas.isEmpty()) {
                throw new IllegalArgumentException(
                        "Não existem notas válidas para gerar o relatório."
                );
            }

            //==================================================
            // LISTA FINAL DE NOTAS
            //==================================================

            List<DetalhamentoVendaDTO> listaNotas =
                    new ArrayList<>(notas.values());

            //==================================================
            // TOTAL DAS VENDAS
            //==================================================

            BigDecimal total =
                    BigDecimal.ZERO;

            for (DetalhamentoVendaDTO dto : listaNotas) {

                BigDecimal valor =
                        valorDecimal(
                                dto.getValorTotalNF()
                        );

                total =
                        total.add(valor);
            }

            //==================================================
            // PAGINAÇÃO
            //==================================================

            /*
             * Quantidade máxima de notas por página.
             *
             * Mantemos uma margem de segurança para que
             * o resumo e o rodapé nunca sejam sobrepostos.
             */
            final int NOTAS_POR_PAGINA = 30;

            int totalPaginas =
                    (int) Math.ceil(
                            (double) listaNotas.size()
                                    / NOTAS_POR_PAGINA
                    );

            //==================================================
            // DADOS DO CLIENTE
            //==================================================

            DetalhamentoVendaDTO primeiro =
                    listaNotas.get(0);

            //==================================================
            // TICKET MÉDIO
            //==================================================

            BigDecimal ticketMedio =
                    BigDecimal.ZERO;

            if (!listaNotas.isEmpty()) {

                ticketMedio =
                        total.divide(
                                BigDecimal.valueOf(
                                        listaNotas.size()
                                ),
                                2,
                                java.math.RoundingMode.HALF_UP
                        );
            }

            //==================================================
            // GERAR CADA PÁGINA
            //==================================================

            for (int numeroPagina = 1;
                 numeroPagina <= totalPaginas;
                 numeroPagina++) {

                //==================================================
                // PÁGINA A4 - RETRATO
                //==================================================

                PDPage pagina =
                        new PDPage(
                                PDRectangle.A4
                        );

                documento.addPage(pagina);

                try (
                        PDPageContentStream stream =
                                new PDPageContentStream(
                                        documento,
                                        pagina
                                )
                ) {

                    float largura =
                            pagina.getMediaBox().getWidth();

                    float altura =
                            pagina.getMediaBox().getHeight();

                    float margem = 40;

                    //==================================================
                    // CABEÇALHO
                    //==================================================

                    escreverTexto(
                            stream,
                            "INTELIFISCAL",
                            margem,
                            altura - 40,
                            18,
                            true
                    );

                    //==================================================
                    // DATA DE EMISSÃO
                    //==================================================

                    String dataEmissao =
                            java.time.LocalDate.now()
                                    .format(
                                            java.time.format.DateTimeFormatter
                                                    .ofPattern("dd/MM/yyyy")
                                    );

                    escreverTexto(
                            stream,
                            "Data de emissão: " + dataEmissao,
                            largura - 170,
                            altura - 40,
                            8,
                            false
                    );

                    //==================================================
                    // TÍTULO
                    //==================================================

                    String tituloRelatorio =
                            "RELATÓRIO SINTÉTICO DE VENDAS";

                    if (periodoRelatorio != null &&
                            !periodoRelatorio.isBlank()) {

                        tituloRelatorio +=
                                " - " + periodoRelatorio;
                    }

                    escreverTexto(
                            stream,
                            tituloRelatorio,
                            margem,
                            altura - 62,
                            13,
                            true
                    );

                    //==================================================
                    // DADOS DO CLIENTE
                    //==================================================

                    escreverTexto(
                            stream,
                            "Cliente: "
                                    + valorTexto(
                                    primeiro.getCliente()
                            ),
                            margem,
                            altura - 95,
                            9,
                            true
                    );

                    escreverTexto(
                            stream,
                            "CNPJ: "
                                    + valorTexto(
                                    primeiro.getCnpj()
                            ),
                            margem,
                            altura - 112,
                            9,
                            false
                    );

                    escreverTexto(
                            stream,
                            "Município: "
                                    + valorTexto(
                                    primeiro.getMunicipioCliente()
                            ),
                            margem,
                            altura - 129,
                            9,
                            false
                    );

                    escreverTexto(
                            stream,
                            "UF: "
                                    + valorTexto(
                                    primeiro.getUfCliente()
                            ),
                            300,
                            altura - 129,
                            9,
                            false
                    );

                    //==================================================
                    // CABEÇALHO DA TABELA
                    //==================================================

                    float yCabecalho =
                            altura - 165;

                    escreverTexto(
                            stream,
                            "NF",
                            margem,
                            yCabecalho,
                            8,
                            true
                    );

                    escreverTexto(
                            stream,
                            "DATA",
                            120,
                            yCabecalho,
                            8,
                            true
                    );

                    escreverTexto(
                            stream,
                            "VALOR DA NOTA",
                            250,
                            yCabecalho,
                            8,
                            true
                    );

                    stream.moveTo(
                            margem,
                            yCabecalho - 6
                    );

                    stream.lineTo(
                            largura - margem,
                            yCabecalho - 6
                    );

                    stream.stroke();

                    //==================================================
                    // DETERMINAR INTERVALO DAS NOTAS
                    //==================================================

                    int indiceInicial =
                            (numeroPagina - 1)
                                    * NOTAS_POR_PAGINA;

                    int indiceFinal =
                            Math.min(
                                    indiceInicial
                                            + NOTAS_POR_PAGINA,
                                    listaNotas.size()
                            );

                    //==================================================
                    // ESCREVER NOTAS
                    //==================================================

                    float linhaY =
                            yCabecalho - 22;

                    for (int i = indiceInicial;
                         i < indiceFinal;
                         i++) {

                        DetalhamentoVendaDTO dto =
                                listaNotas.get(i);

                        BigDecimal valor =
                                valorDecimal(
                                        dto.getValorTotalNF()
                                );

                        escreverTexto(
                                stream,
                                valorTexto(
                                        dto.getNumeroNota()
                                ),
                                margem,
                                linhaY,
                                8,
                                false
                        );

                        //==================================================
                        // DATA
                        //==================================================

                        String dataNota = "";

                        if (dto.getDataVenda() != null) {

                            dataNota =
                                    dto.getDataVenda()
                                            .format(
                                                    java.time.format.DateTimeFormatter
                                                            .ofPattern("dd/MM/yyyy")
                                            );
                        }

                        escreverTexto(
                                stream,
                                dataNota,
                                120,
                                linhaY,
                                8,
                                false
                        );

                        //==================================================
                        // VALOR
                        //==================================================

                        escreverTexto(
                                stream,
                                formatarMoeda(valor),
                                250,
                                linhaY,
                                8,
                                false
                        );

                        linhaY -= 17;
                    }

                    //==================================================
                    // RESUMO
                    //==================================================

                    /*
                     * O resumo somente aparece na última página.
                     * Assim ele nunca fica no meio da listagem.
                     */
                    if (numeroPagina == totalPaginas) {

                        linhaY -= 12;

                        escreverTexto(
                                stream,
                                "Quantidade de NFs: "
                                        + listaNotas.size(),
                                margem,
                                linhaY,
                                9,
                                true
                        );

                        escreverTexto(
                                stream,
                                "Total das vendas: "
                                        + formatarMoeda(total),
                                margem,
                                linhaY - 20,
                                9,
                                true
                        );

                        escreverTexto(
                                stream,
                                "Ticket médio: "
                                        + formatarMoeda(ticketMedio),
                                margem,
                                linhaY - 40,
                                9,
                                true
                        );
                    }

                    //==================================================
                    // RODAPÉ
                    //==================================================

                    escreverTexto(
                            stream,
                            "InteliFiscal — Relatório gerado pelo sistema",
                            margem,
                            25,
                            8,
                            false
                    );

                    escreverTexto(
                            stream,
                            "Página "
                                    + numeroPagina
                                    + " de "
                                    + totalPaginas,
                            largura - 85,
                            25,
                            8,
                            false
                    );
                }
            }

            //==================================================
            // SALVAR
            //==================================================

            documento.save(
                    caminhoArquivo.toFile()
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Erro ao gerar o relatório sintético de vendas em PDF.",
                    e
            );
        }
    }

    //==================================================
    // RELATÓRIO ANALÍTICO DE VENDAS POR CLIENTE
    //==================================================

    public void gerarRelatorioAnaliticoVendas(
            List<DetalhamentoVendaDTO> dados,
            Path caminhoArquivo,
            String periodoRelatorio
    ) {

        if (dados == null) {
            throw new IllegalArgumentException(
                    "Os dados do relatório não podem ser nulos."
            );
        }

        if (caminhoArquivo == null) {
            throw new IllegalArgumentException(
                    "O caminho do arquivo não pode ser nulo."
            );
        }

        if (dados.isEmpty()) {
            throw new IllegalArgumentException(
                    "Não existem vendas para gerar o relatório."
            );
        }

        try (
                PDDocument documento =
                        new PDDocument()
        ) {

            float margem = 30;

            //==================================================
            // AGRUPAR ITENS POR NF
            //==================================================

            Map<Long, List<DetalhamentoVendaDTO>> notas =
                    new LinkedHashMap<>();

            for (DetalhamentoVendaDTO dto : dados) {

                if (dto == null || dto.getIdNfe() == null) {
                    continue;
                }

                notas.computeIfAbsent(
                        dto.getIdNfe(),
                        chave -> new ArrayList<>()
                ).add(dto);
            }

            if (notas.isEmpty()) {
                throw new IllegalArgumentException(
                        "Não foi possível identificar as Notas Fiscais."
                );
            }

            //==================================================
            // TOTAL DO PERÍODO
            //==================================================

            BigDecimal totalPeriodo =
                    BigDecimal.ZERO;

            for (
                    List<DetalhamentoVendaDTO> itensNF
                    : notas.values()
            ) {

                if (!itensNF.isEmpty()) {

                    BigDecimal valorNF =
                            valorDecimal(
                                    itensNF.get(0).getValorTotalNF()
                            );

                    totalPeriodo =
                            totalPeriodo.add(valorNF);
                }
            }

            //==================================================
            // PRIMEIRA PÁGINA
            //==================================================

            PDPage pagina =
                    new PDPage(PDRectangle.A4);

            documento.addPage(pagina);

            float largura =
                    pagina.getMediaBox().getWidth();

            float altura =
                    pagina.getMediaBox().getHeight();

            float y =
                    altura - 40;

            PDPageContentStream stream =
                    new PDPageContentStream(
                            documento,
                            pagina
                    );

            try {

                //==================================================
                // CABEÇALHO
                //==================================================

                escreverTexto(
                        stream,
                        "INTELIFISCAL",
                        margem,
                        y,
                        18,
                        true
                );

                escreverTexto(
                        stream,
                        "Data de emissão: "
                                + java.time.LocalDate.now()
                                .format(
                                        java.time.format.DateTimeFormatter
                                                .ofPattern("dd/MM/yyyy")
                                ),
                        largura - 170,
                        y,
                        8,
                        false
                );

                y -= 22;

                //==================================================
                // TÍTULO
                //==================================================

                String titulo =
                        "RELATÓRIO ANALÍTICO DE VENDAS";

                if (
                        periodoRelatorio != null
                                && !periodoRelatorio.isBlank()
                ) {

                    titulo +=
                            " - " + periodoRelatorio;
                }

                escreverTexto(
                        stream,
                        titulo,
                        margem,
                        y,
                        13,
                        true
                );

                y -= 32;

                //==================================================
                // CLIENTE
                //==================================================

                DetalhamentoVendaDTO primeiro =
                        dados.get(0);

                escreverTexto(
                        stream,
                        "Cliente: "
                                + valorTexto(
                                primeiro.getCliente()
                        ),
                        margem,
                        y,
                        9,
                        true
                );

                y -= 16;

                escreverTexto(
                        stream,
                        "CNPJ: "
                                + valorTexto(
                                primeiro.getCnpj()
                        ),
                        margem,
                        y,
                        9,
                        false
                );

                escreverTexto(
                        stream,
                        "Município: "
                                + valorTexto(
                                primeiro.getMunicipioCliente()
                        ),
                        280,
                        y,
                        9,
                        false
                );

                escreverTexto(
                        stream,
                        "UF: "
                                + valorTexto(
                                primeiro.getUfCliente()
                        ),
                        470,
                        y,
                        9,
                        false
                );

                y -= 18;

                //==================================================
                // TOTAL DO PERÍODO
                //==================================================

                escreverTexto(
                        stream,
                        "Total de vendas no período: "
                                + formatarMoeda(totalPeriodo),
                        margem,
                        y,
                        10,
                        true
                );

                y -= 28;

                //==================================================
                // NOTAS FISCAIS
                //==================================================

                for (
                        List<DetalhamentoVendaDTO> itensNF
                        : notas.values()
                ) {

                    //==================================================
                    // NOVA PÁGINA
                    //==================================================

                    if (y < 130) {

                        stream.close();

                        pagina =
                                new PDPage(
                                        PDRectangle.A4
                                );

                        documento.addPage(pagina);

                        largura =
                                pagina.getMediaBox()
                                        .getWidth();

                        altura =
                                pagina.getMediaBox()
                                        .getHeight();

                        y =
                                altura - 40;

                        stream =
                                new PDPageContentStream(
                                        documento,
                                        pagina
                                );
                    }

                    DetalhamentoVendaDTO nf =
                            itensNF.get(0);

                    //==================================================
                    // CABEÇALHO DA NF
                    //==================================================

                    escreverTexto(
                            stream,
                            "Nº NF: "
                                    + valorTexto(
                                    nf.getNumeroNota()
                            ),
                            margem,
                            y,
                            9,
                            true
                    );

                    String dataNF = "";

                    if (nf.getDataVenda() != null) {

                        dataNF =
                                nf.getDataVenda()
                                        .format(
                                                java.time.format
                                                        .DateTimeFormatter
                                                        .ofPattern(
                                                                "dd/MM/yyyy"
                                                        )
                                        );
                    }

                    escreverTexto(
                            stream,
                            "Data Emissão: "
                                    + dataNF,
                            160,
                            y,
                            9,
                            false
                    );

                    escreverTexto(
                            stream,
                            "Valor Total NF: "
                                    + formatarMoeda(
                                    nf.getValorTotalNF()
                            ),
                            310,
                            y,
                            9,
                            false
                    );

                    //==================================================
                    // DUPLICATAS
                    //==================================================

                    List<NFeDuplicata> duplicatas =
                            nfeDuplicataRepository
                                    .listarPorNfe(
                                            nf.getIdNfe()
                                    );

                    String parcelas =
                            duplicatas.isEmpty()
                                    ? "Não informado"
                                    : String.format(
                                    "%02d",
                                    duplicatas.size()
                            );

                    escreverTexto(
                            stream,
                            "Nº Parcelas: "
                                    + parcelas,
                            470,
                            y,
                            9,
                            false
                    );

                    y -= 18;

                    if (!duplicatas.isEmpty()) {

                        escreverTexto(
                                stream,
                                "Duplicatas:",
                                margem,
                                y,
                                8,
                                true
                        );

                        y -= 15;

                        for (
                                NFeDuplicata duplicata
                                : duplicatas
                        ) {

                            String vencimento = "";

                            if (
                                    duplicata
                                            .getDataVencimento()
                                            != null
                            ) {

                                vencimento =
                                        duplicata
                                                .getDataVencimento()
                                                .format(
                                                        java.time.format
                                                                .DateTimeFormatter
                                                                .ofPattern(
                                                                        "dd/MM/yyyy"
                                                                )
                                                );
                            }

                            escreverTexto(
                                    stream,
                                    valorTexto(
                                            duplicata
                                                    .getNumeroDuplicata()
                                    )
                                            + " - "
                                            + formatarMoeda(
                                            duplicata.getValor()
                                    )
                                            + " - "
                                            + vencimento,
                                    margem + 10,
                                    y,
                                    8,
                                    false
                            );

                            y -= 14;
                        }
                    }

                    //==================================================
                    // CABEÇALHO DOS ITENS
                    //==================================================

                    y -= 6;

                    escreverTexto(
                            stream,
                            "Nº Item",
                            margem,
                            y,
                            8,
                            true
                    );

                    escreverTexto(
                            stream,
                            "Descrição do item",
                            75,
                            y,
                            8,
                            true
                    );

                    escreverTexto(
                            stream,
                            "Qtde.",
                            300,
                            y,
                            8,
                            true
                    );

                    escreverTexto(
                            stream,
                            "UN.",
                            345,
                            y,
                            8,
                            true
                    );

                    escreverTexto(
                            stream,
                            "Valor Unit.",
                            390,
                            y,
                            8,
                            true
                    );

                    escreverTexto(
                            stream,
                            "Total Item",
                            490,
                            y,
                            8,
                            true
                    );

                    y -= 14;

                    //==================================================
                    // ITENS
                    //==================================================

                    for (
                            DetalhamentoVendaDTO item
                            : itensNF
                    ) {

                        if (y < 80) {

                            stream.close();

                            pagina =
                                    new PDPage(
                                            PDRectangle.A4
                                    );

                            documento.addPage(pagina);

                            largura =
                                    pagina.getMediaBox()
                                            .getWidth();

                            altura =
                                    pagina.getMediaBox()
                                            .getHeight();

                            y =
                                    altura - 40;

                            stream =
                                    new PDPageContentStream(
                                            documento,
                                            pagina
                                    );
                        }

                        String numeroItem =
                                item.getNumeroItem() == null
                                        ? ""
                                        : String.valueOf(
                                        item.getNumeroItem()
                                );

                        escreverTexto(
                                stream,
                                numeroItem,
                                margem,
                                y,
                                7,
                                false
                        );

                        escreverTexto(
                                stream,
                                limitarTexto(
                                        valorTexto(
                                                item.getProduto()
                                        ),
                                        34
                                ),
                                75,
                                y,
                                7,
                                false
                        );

                        escreverTexto(
                                stream,
                                String.format(
                                        Locale.US,
                                        "%.3f",
                                        item.getQuantidade()
                                ).replace(
                                        ".",
                                        ","
                                ),
                                300,
                                y,
                                7,
                                false
                        );

                        escreverTexto(
                                stream,
                                limitarTexto(
                                        valorTexto(
                                                item.getUnidade()
                                        ),
                                        5
                                ),
                                345,
                                y,
                                7,
                                false
                        );

                        escreverTexto(
                                stream,
                                formatarMoeda(
                                        item.getValorUnitario()
                                ),
                                390,
                                y,
                                7,
                                false
                        );

                        escreverTexto(
                                stream,
                                formatarMoeda(
                                        item.getValorTotal()
                                ),
                                490,
                                y,
                                7,
                                false
                        );

                        y -= 13;
                    }

                    y -= 8;

                    //==================================================
                    // SEPARADOR DA NF
                    //==================================================

                    stream.moveTo(
                            margem,
                            y
                    );

                    stream.lineTo(
                            largura - margem,
                            y
                    );

                    stream.stroke();

                    y -= 18;
                }

                //==================================================
                // RODAPÉ
                //==================================================


            } finally {

                stream.close();
            }

            //==================================================
            // RODAPÉ EM TODAS AS PÁGINAS
            //==================================================

            int totalPaginas =
                    documento.getNumberOfPages();

            for (int i = 0; i < totalPaginas; i++) {

                PDPage paginaRodape =
                        documento.getPage(i);

                try (
                        PDPageContentStream rodape =
                                new PDPageContentStream(
                                        documento,
                                        paginaRodape,
                                        PDPageContentStream.AppendMode.APPEND,
                                        true,
                                        true
                                )
                ) {

                    float larguraRodape =
                            paginaRodape.getMediaBox()
                                    .getWidth();

                    escreverTexto(
                            rodape,
                            "InteliFiscal — Relatório gerado pelo sistema",
                            30,
                            25,
                            8,
                            false
                    );

                    escreverTexto(
                            rodape,
                            "Página "
                                    + (i + 1)
                                    + " de "
                                    + totalPaginas,
                            larguraRodape - 85,
                            25,
                            8,
                            false
                    );
                }
            }

            documento.save(
                    caminhoArquivo.toFile()
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Erro ao gerar o relatório analítico de vendas em PDF.",
                    e
            );
        }
    }

    //==================================================
    // RELATÓRIO ANALÍTICO DE COMPRAS POR FORNECEDOR
    //==================================================

    public void gerarRelatorioAnalitico(
            List<DetalhamentoCompraDTO> dados,
            Path caminhoArquivo,
            String periodoRelatorio
    ) {

        if (dados == null) {
            throw new IllegalArgumentException(
                    "Os dados do relatório não podem ser nulos."
            );
        }

        if (caminhoArquivo == null) {
            throw new IllegalArgumentException(
                    "O caminho do arquivo não pode ser nulo."
            );
        }

        if (dados.isEmpty()) {
            throw new IllegalArgumentException(
                    "Não existem compras para gerar o relatório."
            );
        }

        try (PDDocument documento = new PDDocument()) {

            float margem = 30;

            //==================================================
            // AGRUPAR ITENS POR NF
            //==================================================

            Map<Long, List<DetalhamentoCompraDTO>> notas =
                    new LinkedHashMap<>();

            for (DetalhamentoCompraDTO dto : dados) {

                if (dto == null || dto.getIdNfe() == null) {
                    continue;
                }

                notas.computeIfAbsent(
                        dto.getIdNfe(),
                        chave -> new java.util.ArrayList<>()
                ).add(dto);
            }

            if (notas.isEmpty()) {
                throw new IllegalArgumentException(
                        "Não foi possível identificar as Notas Fiscais."
                );
            }

            //==================================================
            // TOTAL DO PERÍODO
            //==================================================

            BigDecimal totalPeriodo =
                    BigDecimal.ZERO;

            for (List<DetalhamentoCompraDTO> itensNF : notas.values()) {

                if (!itensNF.isEmpty()) {

                    BigDecimal valorNF =
                            itensNF.get(0).getValorTotalNF();

                    if (valorNF != null) {
                        totalPeriodo =
                                totalPeriodo.add(valorNF);
                    }
                }
            }

            //==================================================
            // PRIMEIRA PÁGINA
            //==================================================

            PDPage pagina =
                    new PDPage(PDRectangle.A4);

            documento.addPage(pagina);

            float largura =
                    pagina.getMediaBox().getWidth();

            float altura =
                    pagina.getMediaBox().getHeight();

            float y =
                    altura - 40;

            PDPageContentStream stream =
                    new PDPageContentStream(
                            documento,
                            pagina
                    );

            try {

                //==================================================
                // CABEÇALHO
                //==================================================

                escreverTexto(
                        stream,
                        "INTELIFISCAL",
                        margem,
                        y,
                        18,
                        true
                );

                escreverTexto(
                        stream,
                        "Data de emissão: "
                                + java.time.LocalDate.now()
                                .format(
                                        java.time.format.DateTimeFormatter.ofPattern(
                                                "dd/MM/yyyy"
                                        )
                                ),
                        largura - 170,
                        y,
                        8,
                        false
                );

                y -= 22;

                //==================================================
                // TÍTULO
                //==================================================

                String titulo =
                        "RELATÓRIO ANALÍTICO DE COMPRAS";

                if (periodoRelatorio != null &&
                        !periodoRelatorio.isBlank()) {

                    titulo +=
                            " - " + periodoRelatorio;
                }

                escreverTexto(
                        stream,
                        titulo,
                        margem,
                        y,
                        13,
                        true
                );

                y -= 32;

                //==================================================
                // FORNECEDOR
                //==================================================

                DetalhamentoCompraDTO primeiro =
                        dados.get(0);

                escreverTexto(
                        stream,
                        "Fornecedor: "
                                + valorTexto(
                                primeiro.getFornecedor()
                        ),
                        margem,
                        y,
                        9,
                        true
                );

                y -= 16;

                escreverTexto(
                        stream,
                        "CNPJ: "
                                + valorTexto(
                                primeiro.getCnpj()
                        ),
                        margem,
                        y,
                        9,
                        false
                );

                escreverTexto(
                        stream,
                        "Município: "
                                + valorTexto(
                                primeiro.getMunicipioFornecedor()
                        ),
                        280,
                        y,
                        9,
                        false
                );

                escreverTexto(
                        stream,
                        "UF: "
                                + valorTexto(
                                primeiro.getUfFornecedor()
                        ),
                        470,
                        y,
                        9,
                        false
                );

                y -= 18;

                //==================================================
                // TOTAL DO PERÍODO
                //==================================================

                escreverTexto(
                        stream,
                        "Total de compras no período: "
                                + formatarMoeda(totalPeriodo),
                        margem,
                        y,
                        10,
                        true
                );

                y -= 28;

                //==================================================
                // NOTAS FISCAIS
                //==================================================

                for (List<DetalhamentoCompraDTO> itensNF
                        : notas.values()) {

                    //==================================================
                    // NOVA PÁGINA
                    //==================================================

                    if (y < 130) {

                        stream.close();

                        pagina =
                                new PDPage(PDRectangle.A4);

                        documento.addPage(pagina);

                        largura =
                                pagina.getMediaBox().getWidth();

                        altura =
                                pagina.getMediaBox().getHeight();

                        y =
                                altura - 40;

                        stream =
                                new PDPageContentStream(
                                        documento,
                                        pagina
                                );
                    }

                    DetalhamentoCompraDTO nf =
                            itensNF.get(0);

                    //==================================================
                    // CABEÇALHO DA NF
                    //==================================================

                    escreverTexto(
                            stream,
                            "Nº NF: "
                                    + valorTexto(
                                    nf.getNumeroNota()
                            ),
                            margem,
                            y,
                            9,
                            true
                    );

                    String dataNF = "";

                    if (nf.getDataCompra() != null) {

                        dataNF =
                                nf.getDataCompra()
                                        .format(
                                                java.time.format.DateTimeFormatter.ofPattern(
                                                        "dd/MM/yyyy"
                                                )
                                        );
                    }

                    escreverTexto(
                            stream,
                            "Data Emissão: " + dataNF,
                            160,
                            y,
                            9,
                            false
                    );

                    escreverTexto(
                            stream,
                            "Valor Total NF: "
                                    + formatarMoeda(
                                    nf.getValorTotalNF()
                            ),
                            310,
                            y,
                            9,
                            false
                    );

                    //==================================================
                    // DUPLICATAS
                    //==================================================

                    List<NFeDuplicata> duplicatas =
                            nfeDuplicataRepository.listarPorNfe(
                                    nf.getIdNfe()
                            );

                    String parcelas =
                            duplicatas.isEmpty()
                                    ? "Não informado"
                                    : String.format(
                                    "%02d",
                                    duplicatas.size()
                            );

                    escreverTexto(
                            stream,
                            "Nº Parcelas: " + parcelas,
                            470,
                            y,
                            9,
                            false
                    );

                    y -= 18;

                    if (!duplicatas.isEmpty()) {

                        escreverTexto(
                                stream,
                                "Duplicatas:",
                                margem,
                                y,
                                8,
                                true
                        );

                        y -= 15;

                        for (
                                NFeDuplicata duplicata
                                : duplicatas
                        ) {

                            String vencimento = "";

                            if (duplicata.getDataVencimento() != null) {

                                vencimento =
                                        duplicata.getDataVencimento()
                                                .format(
                                                        java.time.format.DateTimeFormatter.ofPattern(
                                                                "dd/MM/yyyy"
                                                        )
                                                );
                            }

                            escreverTexto(
                                    stream,
                                    valorTexto(
                                            duplicata.getNumeroDuplicata()
                                    )
                                            + " - "
                                            + formatarMoeda(
                                            duplicata.getValor()
                                    )
                                            + " - "
                                            + vencimento,
                                    margem + 10,
                                    y,
                                    8,
                                    false
                            );

                            y -= 14;
                        }
                    }

                    //==================================================
                    // CABEÇALHO DOS ITENS
                    //==================================================

                    y -= 6;

                    escreverTexto(
                            stream,
                            "Nº Item",
                            margem,
                            y,
                            8,
                            true
                    );

                    escreverTexto(
                            stream,
                            "Descrição do item",
                            75,
                            y,
                            8,
                            true
                    );

                    escreverTexto(
                            stream,
                            "Qtde.",
                            300,
                            y,
                            8,
                            true
                    );

                    escreverTexto(
                            stream,
                            "UN.",
                            345,
                            y,
                            8,
                            true
                    );

                    escreverTexto(
                            stream,
                            "Valor Unit.",
                            390,
                            y,
                            8,
                            true
                    );

                    escreverTexto(
                            stream,
                            "Total Item",
                            490,
                            y,
                            8,
                            true
                    );

                    y -= 14;

                    //==================================================
                    // ITENS
                    //==================================================

                    for (DetalhamentoCompraDTO item : itensNF) {

                        if (y < 80) {

                            stream.close();

                            pagina =
                                    new PDPage(PDRectangle.A4);

                            documento.addPage(pagina);

                            largura =
                                    pagina.getMediaBox().getWidth();

                            altura =
                                    pagina.getMediaBox().getHeight();

                            y =
                                    altura - 40;

                            stream =
                                    new PDPageContentStream(
                                            documento,
                                            pagina
                                    );
                        }

                        String numeroItem =
                                item.getNumeroItem() == null
                                        ? ""
                                        : String.valueOf(
                                        item.getNumeroItem()
                                );

                        escreverTexto(
                                stream,
                                numeroItem,
                                margem,
                                y,
                                7,
                                false
                        );

                        escreverTexto(
                                stream,
                                limitarTexto(
                                        valorTexto(
                                                item.getProduto()
                                        ),
                                        34
                                ),
                                75,
                                y,
                                7,
                                false
                        );

                        escreverTexto(
                                stream,
                                String.format(
                                        Locale.US,
                                        "%.3f",
                                        item.getQuantidade()
                                ).replace(
                                        ".",
                                        ","
                                ),
                                300,
                                y,
                                7,
                                false
                        );

                        escreverTexto(
                                stream,
                                limitarTexto(
                                        valorTexto(
                                                item.getUnidade()
                                        ),
                                        5
                                ),
                                345,
                                y,
                                7,
                                false
                        );

                        escreverTexto(
                                stream,
                                formatarMoeda(
                                        item.getValorUnitario()
                                ),
                                390,
                                y,
                                7,
                                false
                        );

                        escreverTexto(
                                stream,
                                formatarMoeda(
                                        item.getValorTotal()
                                ),
                                490,
                                y,
                                7,
                                false
                        );

                        y -= 13;
                    }

                    //==================================================
                    // SEPARADOR DA NOTA FISCAL
                    //==================================================

                    y -= 8;

                    stream.moveTo(
                            margem,
                            y
                    );

                    stream.lineTo(
                            largura - margem,
                            y
                    );

                    stream.stroke();

                    y -= 18;
                }

                //==================================================
                // FECHAR STREAM ATUAL
                //==================================================

                stream.close();

                stream = null;

            } finally {

                if (stream != null) {
                    stream.close();
                }
            }

            //==================================================
            // RODAPÉ EM TODAS AS PÁGINAS
            //==================================================

            int totalPaginas =
                    documento.getNumberOfPages();

            for (int i = 0; i < totalPaginas; i++) {

                PDPage paginaRodape =
                        documento.getPage(i);

                try (
                        PDPageContentStream rodape =
                                new PDPageContentStream(
                                        documento,
                                        paginaRodape,
                                        PDPageContentStream.AppendMode.APPEND,
                                        true,
                                        true
                                )
                ) {

                    escreverRodapeAnalitico(
                            rodape,
                            paginaRodape.getMediaBox().getWidth(),
                            i + 1,
                            totalPaginas
                    );
                }
            }

            documento.save(
                    caminhoArquivo.toFile()
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Erro ao gerar o relatório analítico em PDF.",
                    e
            );
        }
    }

    //==================================================
    // RELATÓRIO SINTÉTICO DE COMPRAS POR FORNECEDOR
    //==================================================

    public void gerarRelatorioSintetico(
            List<br.com.intelifiscal.dto.relatorio.DetalhamentoCompraDTO> dados,
            Path caminhoArquivo,
            String periodoRelatorio
    ) {

        if (dados == null) {
            throw new IllegalArgumentException(
                    "Os dados do relatório não podem ser nulos."
            );
        }

        if (caminhoArquivo == null) {
            throw new IllegalArgumentException(
                    "O caminho do arquivo não pode ser nulo."
            );
        }

        if (dados.isEmpty()) {
            throw new IllegalArgumentException(
                    "Não existem compras para gerar o relatório."
            );
        }

        try (
                PDDocument documento =
                        new PDDocument()
        ) {

            //==================================================
            // PÁGINA A4 - RETRATO
            //==================================================

            PDPage pagina =
                    new PDPage(
                            PDRectangle.A4
                    );

            documento.addPage(pagina);

            try (
                    PDPageContentStream stream =
                            new PDPageContentStream(
                                    documento,
                                    pagina
                            )
            ) {

                float largura =
                        pagina.getMediaBox().getWidth();

                float altura =
                        pagina.getMediaBox().getHeight();

                float margem = 40;

                //==================================================
                // CABEÇALHO
                //==================================================

                escreverTexto(
                        stream,
                        "INTELIFISCAL",
                        margem,
                        altura - 40,
                        18,
                        true
                );

                //==================================================
                // TÍTULO DO RELATÓRIO + PERÍODO
                //==================================================

                String tituloRelatorio =
                        "RELATÓRIO SINTÉTICO DE COMPRAS";

                if (periodoRelatorio != null &&
                        !periodoRelatorio.isBlank()) {

                    tituloRelatorio +=
                            " - "
                                    + periodoRelatorio;
                }

                escreverTexto(
                        stream,
                        tituloRelatorio,
                        margem,
                        altura - 62,
                        13,
                        true
                );

                // Data de emissão do relatório
                String dataEmissao =
                        java.time.LocalDate.now()
                                .format(
                                        java.time.format.DateTimeFormatter.ofPattern(
                                                "dd/MM/yyyy"
                                        )
                                );

                escreverTexto(
                        stream,
                        "Data de emissão: " + dataEmissao,
                        largura - 170,
                        altura - 40,
                        8,
                        false
                );

                //==================================================
                // DADOS DO FORNECEDOR
                //==================================================

                escreverTexto(
                        stream,
                        "Fornecedor: "
                                + valorTexto(
                                dados.get(0).getFornecedor()
                        ),
                        margem,
                        altura - 95,
                        9,
                        true
                );

                escreverTexto(
                        stream,
                        "CNPJ: "
                                + valorTexto(
                                dados.get(0).getCnpj()
                        ),
                        margem,
                        altura - 112,
                        9,
                        false
                );

                escreverTexto(
                        stream,
                        "Município: "
                                + valorTexto(
                                dados.get(0).getMunicipioFornecedor()
                        ),
                        margem,
                        altura - 129,
                        9,
                        false
                );

                escreverTexto(
                        stream,
                        "UF: "
                                + valorTexto(
                                dados.get(0).getUfFornecedor()
                        ),
                        300,
                        altura - 129,
                        9,
                        false
                );

                //==================================================
                // TÍTULO DA TABELA
                //==================================================

                float yCabecalho =
                        altura - 165;

                escreverTexto(
                        stream,
                        "NF",
                        margem,
                        yCabecalho,
                        8,
                        true
                );

                escreverTexto(
                        stream,
                        "DATA",
                        120,
                        yCabecalho,
                        8,
                        true
                );

                escreverTexto(
                        stream,
                        "VALOR DA NOTA",
                        250,
                        yCabecalho,
                        8,
                        true
                );

                // Linha separadora
                stream.moveTo(
                        margem,
                        yCabecalho - 6
                );

                stream.lineTo(
                        largura - margem,
                        yCabecalho - 6
                );

                stream.stroke();

                //==================================================
                // AGRUPAR NOTAS
                //==================================================

                float linhaY =
                        yCabecalho - 22;

                Map<String, br.com.intelifiscal.dto.relatorio.DetalhamentoCompraDTO>
                        notas =
                        new LinkedHashMap<>();

                for (
                        br.com.intelifiscal.dto.relatorio.DetalhamentoCompraDTO dto
                        : dados
                ) {

                    if (dto == null) {
                        continue;
                    }

                    String numeroNota =
                            dto.getNumeroNota();

                    if (
                            numeroNota == null
                                    || numeroNota.isBlank()
                    ) {
                        continue;
                    }

                    notas.putIfAbsent(
                            numeroNota,
                            dto
                    );
                }

                //==================================================
                // ESCREVER NOTAS
                //==================================================

                BigDecimal total =
                        BigDecimal.ZERO;

                for (
                        br.com.intelifiscal.dto.relatorio.DetalhamentoCompraDTO dto
                        : notas.values()
                ) {

                    BigDecimal valor =
                            valorDecimal(
                                    dto.getValorTotalNF()
                            );

                    total =
                            total.add(valor);

                    escreverTexto(
                            stream,
                            valorTexto(
                                    dto.getNumeroNota()
                            ),
                            margem,
                            linhaY,
                            8,
                            false
                    );

                    // Data no formato brasileiro
                    String dataNota = "";

                    if (dto.getDataCompra() != null) {

                        dataNota =
                                dto.getDataCompra()
                                        .format(
                                                java.time.format.DateTimeFormatter.ofPattern(
                                                        "dd/MM/yyyy"
                                                )
                                        );
                    }

                    escreverTexto(
                            stream,
                            dataNota,
                            120,
                            linhaY,
                            8,
                            false
                    );

                    escreverTexto(
                            stream,
                            formatarMoeda(valor),
                            250,
                            linhaY,
                            8,
                            false
                    );

                    linhaY -= 17;

                    // Evitar escrever sobre o rodapé
                    if (linhaY < 100) {
                        break;
                    }
                }

                //==================================================
                // RESUMO
                //==================================================

                linhaY -= 12;

                escreverTexto(
                        stream,
                        "Quantidade de NFs: "
                                + notas.size(),
                        margem,
                        linhaY,
                        9,
                        true
                );

                escreverTexto(
                        stream,
                        "Total das compras: "
                                + formatarMoeda(total),
                        margem,
                        linhaY - 20,
                        9,
                        true
                );

                BigDecimal ticketMedio =
                        BigDecimal.ZERO;

                if (!notas.isEmpty()) {

                    ticketMedio =
                            total.divide(
                                    BigDecimal.valueOf(
                                            notas.size()
                                    ),
                                    2,
                                    java.math.RoundingMode.HALF_UP
                            );
                }

                escreverTexto(
                        stream,
                        "Ticket médio: "
                                + formatarMoeda(ticketMedio),
                        margem,
                        linhaY - 40,
                        9,
                        true
                );

                //==================================================
                // RODAPÉ
                //==================================================

                escreverTexto(
                        stream,
                        "InteliFiscal — Relatório gerado pelo sistema",
                        margem,
                        25,
                        8,
                        false
                );

                escreverTexto(
                        stream,
                        "Página 1 de 1",
                        largura - 85,
                        25,
                        8,
                        false
                );
            }

            documento.save(
                    caminhoArquivo.toFile()
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Erro ao gerar o relatório sintético em PDF.",
                    e
            );
        }
    }

        //==================================================
        // RODAPÉ DO RELATÓRIO ANALÍTICO
        //==================================================

    private void escreverRodapeAnalitico(
            PDPageContentStream stream,
            float largura,
            int numeroPagina,
            int totalPaginas
    ) throws IOException {

        escreverTexto(
                stream,
                "InteliFiscal — Relatório gerado pelo sistema",
                30,
                25,
                8,
                false
        );

        escreverTexto(
                stream,
                "Página "
                        + numeroPagina
                        + " de "
                        + totalPaginas,
                largura - 85,
                25,
                8,
                false
        );
    }


            //==================================================
            // LIMITAR TEXTO
            //==================================================

    private String limitarTexto(
            String texto,
            int tamanhoMaximo
    ) {

        if (texto == null) {
            return "";
        }

        if (texto.length() <= tamanhoMaximo) {
            return texto;
        }

        return texto.substring(
                0,
                tamanhoMaximo - 3
        ) + "...";
    }

}