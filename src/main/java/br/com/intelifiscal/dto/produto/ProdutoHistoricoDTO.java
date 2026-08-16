package br.com.intelifiscal.dto.produto;

import java.time.LocalDateTime;

public class ProdutoHistoricoDTO {

    private String codigoProduto;
    private String descricao;

    private String tipo;
    private String numeroNfe;
    private String serie;
    private LocalDateTime dataEmissao;
    private String emitente;
    private String destinatario;

    private Double quantidade;
    private String unidade;
    private Double valorUnitario;
    private Double valorTotal;


    // ============================================================
    // CÓDIGO DO PRODUTO
    // ============================================================

    public String getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }


    // ============================================================
    // DESCRIÇÃO
    // ============================================================

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


    // ============================================================
    // TIPO
    // ============================================================

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    // ============================================================
    // NÚMERO NF-e
    // ============================================================

    public String getNumeroNfe() {
        return numeroNfe;
    }

    public void setNumeroNfe(String numeroNfe) {
        this.numeroNfe = numeroNfe;
    }


    // ============================================================
    // SÉRIE
    // ============================================================

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }


    // ============================================================
    // DATA DE EMISSÃO
    // ============================================================

    public LocalDateTime getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDateTime dataEmissao) {
        this.dataEmissao = dataEmissao;
    }


    // ============================================================
    // EMITENTE
    // ============================================================

    public String getEmitente() {
        return emitente;
    }

    public void setEmitente(String emitente) {
        this.emitente = emitente;
    }


    // ============================================================
    // DESTINATÁRIO
    // ============================================================

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }


    // ============================================================
    // QUANTIDADE
    // ============================================================

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }


    // ============================================================
    // UNIDADE
    // ============================================================

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }


    // ============================================================
    // VALOR UNITÁRIO
    // ============================================================

    public Double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(Double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }


    // ============================================================
    // VALOR TOTAL
    // ============================================================

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }
}