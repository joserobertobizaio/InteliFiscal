package br.com.intelifiscal.service.relatorio.exportacao;

import br.com.intelifiscal.dto.periodo.ResumoMensalDTO;
import br.com.intelifiscal.dto.periodo.ResumoPeriodoDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.awt.Color;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.Locale;

public class PdfRelatorioService {

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
}