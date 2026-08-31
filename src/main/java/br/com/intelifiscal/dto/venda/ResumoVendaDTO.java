package br.com.intelifiscal.dto.venda;

import java.math.BigDecimal;

public class ResumoVendaDTO {

    private String nf;
    private String dataEmissao;
    private String destinatario;
    private String cnpjDestinatario;
    private BigDecimal valorTotalNF;
    private String codigoItem;
    private String descricaoItem;
    private String cfop;
    private BigDecimal quantidade;
    private BigDecimal valorUnitario;

    //==================================================
    // CONSTRUTOR
    //==================================================

    public ResumoVendaDTO() {
    }

    //==================================================
    // GETTERS E SETTERS
    //==================================================

    public String getNf() {
        return nf;
    }

    public void setNf(String nf) {
        this.nf = nf;
    }

    public String getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(String dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getCnpjDestinatario() {
        return cnpjDestinatario;
    }

    public void setCnpjDestinatario(String cnpjDestinatario) {
        this.cnpjDestinatario = cnpjDestinatario;
    }

    public BigDecimal getValorTotalNF() {
        return valorTotalNF;
    }

    public void setValorTotalNF(BigDecimal valorTotalNF) {
        this.valorTotalNF = valorTotalNF;
    }

    public String getCodigoItem() {
        return codigoItem;
    }

    public void setCodigoItem(String codigoItem) {
        this.codigoItem = codigoItem;
    }

    public String getDescricaoItem() {
        return descricaoItem;
    }

    public void setDescricaoItem(String descricaoItem) {
        this.descricaoItem = descricaoItem;
    }

    public String getCfop() {
        return cfop;
    }

    public void setCfop(String cfop) {
        this.cfop = cfop;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }
}