package br.com.intelifiscal.service.produto;

import java.util.Locale;

public class ConversaoUnidadeService {

    // ============================================================
    // CONVERTE QUANTIDADE PARA A UNIDADE BASE
    // ============================================================

    /**
     * Converte a quantidade informada para a unidade base.
     *
     * Exemplos:
     *
     * MIL -> PC
     * 80 MIL = 80.000 PC
     *
     * CX -> PC
     * depende da quantidade de unidades por caixa,
     * por isso não fazemos essa conversão automaticamente.
     *
     * Para unidades que não possuem conversão conhecida,
     * a quantidade original é mantida.
     */
    public double converterQuantidade(
            double quantidade,
            String unidadeOrigem,
            String unidadeDestino) {

        String origem =
                normalizar(unidadeOrigem);

        String destino =
                normalizar(unidadeDestino);


        // --------------------------------------------------------
        // MESMA UNIDADE
        // --------------------------------------------------------

        if (origem.equals(destino)) {

            return quantidade;
        }


        // --------------------------------------------------------
        // MILHEIRO -> PEÇA
        // --------------------------------------------------------

        if (ehMilheiro(origem)
                && ehPeca(destino)) {

            return quantidade * 1000.0;
        }


        // --------------------------------------------------------
        // PEÇA -> MILHEIRO
        // --------------------------------------------------------

        if (ehPeca(origem)
                && ehMilheiro(destino)) {

            return quantidade / 1000.0;
        }


        // --------------------------------------------------------
        // NÃO EXISTE CONVERSÃO AUTOMÁTICA
        // --------------------------------------------------------

        return quantidade;
    }


    // ============================================================
    // CONVERTE VALOR UNITÁRIO
    // ============================================================

    /**
     * Ajusta o valor unitário para a unidade de destino.
     *
     * Exemplo:
     *
     * R$ 69,50 / MIL
     *
     * para PEÇA:
     *
     * R$ 69,50 / 1000
     *
     * = R$ 0,0695 / PC
     */
    public double converterValorUnitario(
            double valorUnitario,
            String unidadeOrigem,
            String unidadeDestino) {

        String origem =
                normalizar(unidadeOrigem);

        String destino =
                normalizar(unidadeDestino);


        // --------------------------------------------------------
        // MESMA UNIDADE
        // --------------------------------------------------------

        if (origem.equals(destino)) {

            return valorUnitario;
        }


        // --------------------------------------------------------
        // MILHEIRO -> PEÇA
        // --------------------------------------------------------

        if (ehMilheiro(origem)
                && ehPeca(destino)) {

            return valorUnitario / 1000.0;
        }


        // --------------------------------------------------------
        // PEÇA -> MILHEIRO
        // --------------------------------------------------------

        if (ehPeca(origem)
                && ehMilheiro(destino)) {

            return valorUnitario * 1000.0;
        }


        // --------------------------------------------------------
        // NÃO EXISTE CONVERSÃO AUTOMÁTICA
        // --------------------------------------------------------

        return valorUnitario;
    }


    // ============================================================
    // CONVERTE VALOR TOTAL
    // ============================================================

    /**
     * O valor total da nota não precisa ser convertido.
     *
     * Se temos:
     *
     * 80 MIL x R$ 69,50 = R$ 5.560,00
     *
     * O valor total continua sendo R$ 5.560,00.
     */
    public double manterValorTotal(
            double valorTotal) {

        return valorTotal;
    }


    // ============================================================
    // VERIFICA MILHEIRO
    // ============================================================

    private boolean ehMilheiro(
            String unidade) {

        return "MIL".equals(unidade)
                || "M".equals(unidade)
                || "MILHEIRO".equals(unidade);
    }


    // ============================================================
    // VERIFICA PEÇA
    // ============================================================

    private boolean ehPeca(
            String unidade) {

        return "PC".equals(unidade)
                || "PÇ".equals(unidade)
                || "PECA".equals(unidade)
                || "PEÇ".equals(unidade)
                || "UN".equals(unidade)
                || "UND".equals(unidade)
                || "UNID".equals(unidade);
    }


    // ============================================================
    // NORMALIZA UNIDADE
    // ============================================================

    private String normalizar(
            String unidade) {

        if (unidade == null) {

            return "";
        }

        return unidade
                .trim()
                .toUpperCase(Locale.ROOT);
    }
}