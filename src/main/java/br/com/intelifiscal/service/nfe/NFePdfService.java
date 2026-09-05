package br.com.intelifiscal.service.nfe;

import br.com.intelifiscal.dto.nfeitem.NFeItemDTO;
import br.com.intelifiscal.entity.NFe;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class NFePdfService {

    private static final float MARGEM = 30;
    private static final float LARGURA_PAGINA =
            PDRectangle.A4.getWidth();

    private static final float ALTURA_PAGINA =
            PDRectangle.A4.getHeight();

    private static final float LARGURA_UTIL =
            LARGURA_PAGINA - (MARGEM * 2);

    private static final float TAMANHO_TITULO = 14;
    private static final float TAMANHO_NORMAL = 8;
    private static final float TAMANHO_PEQUENO = 7;

    private static final float ALTURA_LINHA = 14;

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");


    // ============================================================
    // GERA PDF DA NF-e
    // ============================================================

    public void gerar(
            NFe nfe,
            List<NFeItemDTO> itens,
            File arquivo) throws IOException {

        if (nfe == null) {

            throw new IllegalArgumentException(
                    "A NF-e não pode ser nula."
            );
        }

        if (itens == null) {

            throw new IllegalArgumentException(
                    "A lista de itens não pode ser nula."
            );
        }

        if (arquivo == null) {

            throw new IllegalArgumentException(
                    "O arquivo de destino não pode ser nulo."
            );
        }


        try (PDDocument documento =
                     new PDDocument()) {


            // ====================================================
            // PRIMEIRA PÁGINA
            // ====================================================

            PDPage pagina =
                    criarPagina(documento);

            PDPageContentStream stream =
                    new PDPageContentStream(
                            documento,
                            pagina
                    );


            float y =
                    ALTURA_PAGINA - MARGEM;


            // ----------------------------------------------------
            // CABEÇALHO DA NF-e
            // ----------------------------------------------------

            y = desenharCabecalho(
                    stream,
                    nfe,
                    y
            );


            // ----------------------------------------------------
            // EMITENTE / DESTINATÁRIO
            // ----------------------------------------------------

            y -= 10;

            y = desenharDadosParticipantes(
                    stream,
                    nfe,
                    y
            );


            // ----------------------------------------------------
            // TÍTULO DOS ITENS
            // ----------------------------------------------------

            y -= 12;

            y = desenharTituloItens(
                    stream,
                    y
            );


            // ====================================================
            // ITENS
            // ====================================================

            for (NFeItemDTO item : itens) {


                // ------------------------------------------------
                // VERIFICA ESPAÇO PARA O ITEM
                // ------------------------------------------------

                if (y < 120) {

                    // Fecha corretamente o stream da página atual
                    desenharRodape(
                            stream,
                            pagina
                    );

                    stream.close();


                    // --------------------------------------------
                    // NOVA PÁGINA
                    // --------------------------------------------

                    pagina =
                            criarPagina(documento);

                    stream =
                            new PDPageContentStream(
                                    documento,
                                    pagina
                            );

                    y =
                            ALTURA_PAGINA - MARGEM;


                    // --------------------------------------------
                    // CABEÇALHO DA NF-e NA NOVA PÁGINA
                    // --------------------------------------------

                    y = desenharCabecalho(
                            stream,
                            nfe,
                            y
                    );


                    y -= 10;


                    // --------------------------------------------
                    // EMITENTE / DESTINATÁRIO
                    // --------------------------------------------

                    y = desenharDadosParticipantes(
                            stream,
                            nfe,
                            y
                    );


                    y -= 12;


                    // --------------------------------------------
                    // CABEÇALHO DA TABELA DE ITENS
                    // --------------------------------------------

                    y = desenharTituloItens(
                            stream,
                            y
                    );
                }


                // ------------------------------------------------
                // DESENHA O ITEM
                // ------------------------------------------------

                y = desenharItem(
                        stream,
                        item,
                        y
                );
            }


            // ====================================================
            // TOTAL DA NF-e
            // ====================================================
            //
            // Precisamos garantir espaço suficiente para o total.
            // ====================================================

            if (y < 80) {

                desenharRodape(
                        stream,
                        pagina
                );

                stream.close();


                pagina =
                        criarPagina(documento);

                stream =
                        new PDPageContentStream(
                                documento,
                                pagina
                        );

                y =
                        ALTURA_PAGINA - MARGEM;


                y = desenharCabecalho(
                        stream,
                        nfe,
                        y
                );


                y -= 20;
            }


            // ----------------------------------------------------
            // TOTAL
            // ----------------------------------------------------

            y = desenharTotal(
                    stream,
                    nfe,
                    y
            );


            // ====================================================
            // RODAPÉ DA ÚLTIMA PÁGINA
            // ====================================================

            desenharRodape(
                    stream,
                    pagina
            );


            // ====================================================
            // FECHA O ÚLTIMO STREAM
            // ====================================================

            stream.close();


            // ====================================================
            // SALVA O DOCUMENTO
            // ====================================================

            documento.save(
                    arquivo
            );
        }
    }

    // ============================================================
    // CRIA PÁGINA
    // ============================================================

    private PDPage criarPagina(
            PDDocument documento) {

        PDPage pagina =
                new PDPage(
                        PDRectangle.A4
                );

        documento.addPage(pagina);

        return pagina;
    }


    // ============================================================
    // CABEÇALHO
    // ============================================================

    private float desenharCabecalho(
            PDPageContentStream stream,
            NFe nfe,
            float y) throws IOException {

        escrever(
                stream,
                "INTELIFISCAL - ESPELHO DA NF-e",
                MARGEM,
                y,
                TAMANHO_TITULO,
                true
        );

        y -= 20;


        escrever(
                stream,
                "NF-e Nº: "
                        + seguro(nfe.getNumero())
                        + "    Série: "
                        + seguro(nfe.getSerie()),
                MARGEM,
                y,
                TAMANHO_NORMAL,
                true
        );


        escrever(
                stream,
                "Modelo: "
                        + seguro(nfe.getModelo())
                        + "    Tipo: "
                        + seguro(nfe.getTipo()),
                300,
                y,
                TAMANHO_NORMAL,
                false
        );

        y -= 15;


        escrever(
                stream,
                "Chave: "
                        + seguro(nfe.getChave()),
                MARGEM,
                y,
                TAMANHO_PEQUENO,
                false
        );

        y -= 15;


        String data =
                nfe.getDataEmissao() == null
                        ? ""
                        : nfe.getDataEmissao()
                          .format(FORMATO_DATA);


        escrever(
                stream,
                "Data de emissão: "
                        + data,
                MARGEM,
                y,
                TAMANHO_NORMAL,
                false
        );


        escrever(
                stream,
                "Situação: "
                        + seguro(nfe.getSituacao()),
                250,
                y,
                TAMANHO_NORMAL,
                false
        );


        escrever(
                stream,
                "Valor total: R$ "
                        + moeda(nfe.getValorTotal()),
                400,
                y,
                TAMANHO_NORMAL,
                true
        );


        y -= 12;


        desenharLinha(
                stream,
                y
        );


        return y - 10;
    }


    // ============================================================
    // EMITENTE / DESTINATÁRIO
    // ============================================================

    private float desenharDadosParticipantes(
            PDPageContentStream stream,
            NFe nfe,
            float y) throws IOException {

        escrever(
                stream,
                "EMITENTE",
                MARGEM,
                y,
                TAMANHO_NORMAL,
                true
        );

        escrever(
                stream,
                "DESTINATÁRIO",
                310,
                y,
                TAMANHO_NORMAL,
                true
        );

        y -= 14;


        // --------------------------------------------------------
        // CNPJ
        // --------------------------------------------------------

        escrever(
                stream,
                "CNPJ: "
                        + seguro(nfe.getCnpjEmitente()),
                MARGEM,
                y,
                TAMANHO_PEQUENO,
                false
        );

        escrever(
                stream,
                "CNPJ: "
                        + seguro(nfe.getCnpjDestinatario()),
                310,
                y,
                TAMANHO_PEQUENO,
                false
        );

        y -= 13;


        // --------------------------------------------------------
        // NOME
        // --------------------------------------------------------

        escrever(
                stream,
                "Nome: "
                        + seguro(nfe.getEmitente()),
                MARGEM,
                y,
                TAMANHO_PEQUENO,
                false
        );

        escrever(
                stream,
                "Nome: "
                        + seguro(nfe.getDestinatario()),
                310,
                y,
                TAMANHO_PEQUENO,
                false
        );

        y -= 13;


        // --------------------------------------------------------
        // MUNICÍPIO
        // --------------------------------------------------------

        escrever(
                stream,
                "Município: "
                        + seguro(nfe.getMunicipioEmitente()),
                MARGEM,
                y,
                TAMANHO_PEQUENO,
                false
        );

        escrever(
                stream,
                "Município: "
                        + seguro(nfe.getMunicipioDestinatario()),
                310,
                y,
                TAMANHO_PEQUENO,
                false
        );

        y -= 12;


        desenharLinha(
                stream,
                y
        );


        return y - 10;
    }


    // ============================================================
    // TÍTULO DOS ITENS
    // ============================================================

    private float desenharTituloItens(
            PDPageContentStream stream,
            float y) throws IOException {

        escrever(
                stream,
                "ITENS DA NF-e",
                MARGEM,
                y,
                TAMANHO_NORMAL,
                true
        );

        y -= 15;


        escrever(
                stream,
                "Item",
                MARGEM,
                y,
                TAMANHO_PEQUENO,
                true
        );

        escrever(
                stream,
                "Código",
                55,
                y,
                TAMANHO_PEQUENO,
                true
        );

        escrever(
                stream,
                "Descrição",
                115,
                y,
                TAMANHO_PEQUENO,
                true
        );

        escrever(
                stream,
                "Un.",
                315,
                y,
                TAMANHO_PEQUENO,
                true
        );

        escrever(
                stream,
                "Qtd.",
                350,
                y,
                TAMANHO_PEQUENO,
                true
        );

        escrever(
                stream,
                "Vlr. Unit.",
                395,
                y,
                TAMANHO_PEQUENO,
                true
        );

        escrever(
                stream,
                "Vlr. Total",
                455,
                y,
                TAMANHO_PEQUENO,
                true
        );

        escrever(
                stream,
                "CFOP",
                520,
                y,
                TAMANHO_PEQUENO,
                true
        );


        y -= 5;

        desenharLinha(
                stream,
                y
        );


        return y - 10;
    }


    // ============================================================
    // ITEM
    // ============================================================

    private float desenharItem(
            PDPageContentStream stream,
            NFeItemDTO item,
            float y) throws IOException {

        escrever(
                stream,
                String.valueOf(
                        item.getNumeroItem() == null
                                ? ""
                                : item.getNumeroItem()
                ),
                MARGEM,
                y,
                TAMANHO_PEQUENO,
                false
        );


        String codigoProduto =
                seguro(item.getCodigoProduto());

        float tamanhoFonteCodigo = TAMANHO_PEQUENO;

        if (codigoProduto.length() > 14) {
            tamanhoFonteCodigo = 6;
        }

        if (codigoProduto.length() > 18) {
            tamanhoFonteCodigo = 5.5f;
        }

        if (codigoProduto.length() > 22) {
            tamanhoFonteCodigo = 5;
        }

        escrever(
                stream,
                codigoProduto,
                55,
                y,
                tamanhoFonteCodigo,
                false
        );


        escrever(
                stream,
                limitar(
                        seguro(item.getDescricao()),
                        42
                ),
                115,
                y,
                TAMANHO_PEQUENO,
                false
        );


        escrever(
                stream,
                limitar(
                        seguro(item.getUnidade()),
                        5
                ),
                315,
                y,
                TAMANHO_PEQUENO,
                false
        );


        escrever(
                stream,
                numero(item.getQuantidade()),
                350,
                y,
                TAMANHO_PEQUENO,
                false
        );


        escrever(
                stream,
                valorUnitario(item.getValorUnitario()),
                395,
                y,
                TAMANHO_PEQUENO,
                false
        );


        escrever(
                stream,
                moeda(item.getValorTotal()),
                455,
                y,
                TAMANHO_PEQUENO,
                false
        );


        escrever(
                stream,
                limitar(
                        seguro(item.getCfop()),
                        6
                ),
                520,
                y,
                TAMANHO_PEQUENO,
                false
        );


        y -= ALTURA_LINHA;


        // --------------------------------------------------------
        // DETALHES FISCAIS DO ITEM
        // --------------------------------------------------------

        escrever(
                stream,
                "NCM: "
                        + seguro(item.getNcm())
                        + "   CEST: "
                        + seguro(item.getCest())
                        + "   "
                        + "Barras: "
                        + seguro(item.getCodigoBarras()),
                55,
                y,
                6,
                false
        );


        y -= 10;


        escrever(
                stream,
                "Desc.: R$ "
                        + moeda(item.getDesconto())
                        + "   Frete: R$ "
                        + moeda(item.getFrete())
                        + "   Seguro: R$ "
                        + moeda(item.getSeguro())
                        + "   Outras: R$ "
                        + moeda(item.getOutrasDespesas()),
                55,
                y,
                6,
                false
        );


        y -= 10;


        escrever(
                stream,
                "ICMS: R$ "
                        + moeda(item.getValorIcms())
                        + "   IPI: R$ "
                        + moeda(item.getValorIpi())
                        + "   PIS: R$ "
                        + moeda(item.getValorPis())
                        + "   COFINS: R$ "
                        + moeda(item.getValorCofins()),
                55,
                y,
                6,
                false
        );


        y -= 13;


        return y;
    }


    // ============================================================
    // TOTAL
    // ============================================================

    private float desenharTotal(
            PDPageContentStream stream,
            NFe nfe,
            float y) throws IOException {

        desenharLinha(
                stream,
                y
        );

        y -= 20;


        escrever(
                stream,
                "TOTAL DA NF-e:",
                390,
                y,
                10,
                true
        );


        escrever(
                stream,
                "R$ "
                        + moeda(nfe.getValorTotal()),
                500,
                y,
                10,
                true
        );


        return y - 20;
    }


    // ============================================================
    // RODAPÉ
    // ============================================================

    private void desenharRodape(
            PDPageContentStream stream,
            PDPage pagina) throws IOException {

        escrever(
                stream,
                "Documento gerado pelo InteliFiscal.",
                MARGEM,
                25,
                6,
                false
        );
    }


    // ============================================================
    // ESCREVER TEXTO
    // ============================================================

    private void escrever(
            PDPageContentStream stream,
            String texto,
            float x,
            float y,
            float tamanho,
            boolean negrito) throws IOException {

        PDType1Font fonte =
                negrito
                        ? new PDType1Font(
                        Standard14Fonts.FontName.HELVETICA_BOLD
                )
                        : new PDType1Font(
                        Standard14Fonts.FontName.HELVETICA
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


    // ============================================================
    // LINHA
    // ============================================================

    private void desenharLinha(
            PDPageContentStream stream,
            float y) throws IOException {

        stream.moveTo(
                MARGEM,
                y
        );

        stream.lineTo(
                LARGURA_PAGINA - MARGEM,
                y
        );

        stream.stroke();
    }


    // ============================================================
    // TEXTO SEGURO
    // ============================================================

    private String seguro(
            String valor) {

        return valor == null
                ? ""
                : valor.trim();
    }


    // ============================================================
    // LIMITA TEXTO
    // ============================================================

    private String limitar(
            String texto,
            int tamanho) {

        if (texto == null) {
            return "";
        }

        if (texto.length() <= tamanho) {
            return texto;
        }

        return texto.substring(
                0,
                tamanho - 3
        ) + "...";
    }


    // ============================================================
    // NÚMERO
    // ============================================================

    private String numero(
            Double valor) {

        if (valor == null) {
            return "0";
        }

        return String.format(
                Locale.US,
                "%.3f",
                valor
        );
    }

    // ============================================================
// VALOR UNITÁRIO - 4 CASAS DECIMAIS
// ============================================================

    private String valorUnitario(
            Double valor) {

        if (valor == null) {
            return "0,0000";
        }

        return String.format(
                        Locale.US,
                        "%,.4f",
                        valor
                )
                .replace(",", "#")
                .replace(".", ",")
                .replace("#", ".");
    }

    // ============================================================
    // MOEDA
    // ============================================================

    private String moeda(
            Double valor) {

        if (valor == null) {
            return "0,00";
        }

        return String.format(
                        Locale.US,
                        "%,.2f",
                        valor
                )
                .replace(",", "#")
                .replace(".", ",")
                .replace("#", ".");
    }


    // ============================================================
    // MOEDA BIGDECIMAL
    // ============================================================

    private String moeda(
            java.math.BigDecimal valor) {

        if (valor == null) {
            return "0,00";
        }

        return String.format(
                        Locale.US,
                        "%,.2f",
                        valor.doubleValue()
                )
                .replace(",", "#")
                .replace(".", ",")
                .replace("#", ".");
    }
}