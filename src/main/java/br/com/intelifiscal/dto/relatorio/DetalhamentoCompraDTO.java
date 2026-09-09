package br.com.intelifiscal.dto.relatorio;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DetalhamentoCompraDTO {

    private String cnpj;
    private Long idNfe;
    private String fornecedor;
    private String numeroNota;
    private LocalDate dataCompra;
    private String produto;
    private String codigoProduto;
    private Integer numeroItem;
    private String unidade;
    private double quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;

    private BigDecimal valorTotalNF;
    private String cfop;
    private String municipioFornecedor;
    private String ufFornecedor;

    public DetalhamentoCompraDTO() {
    }

    public BigDecimal getValorTotalNF() {

        return valorTotalNF;
    }

    public void setValorTotalNF(BigDecimal valorTotalNF) {

        this.valorTotalNF = valorTotalNF;
    }

    public String getCfop() {

        return cfop;
    }

    public void setCfop(String cfop) {

        this.cfop = cfop;
    }

    public String getCnpj() {

        return cnpj;
    }

    public void setCnpj(String cnpj) {

        this.cnpj = cnpj;
    }

    public String getFornecedor() {

        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {

        this.fornecedor = fornecedor;
    }

    public String getNumeroNota() {

        return numeroNota;
    }

    public void setNumeroNota(String numeroNota) {

        this.numeroNota = numeroNota;
    }

    public LocalDate getDataCompra() {

        return dataCompra;
    }

    public void setDataCompra(LocalDate dataCompra) {

        this.dataCompra = dataCompra;
    }

    public String getProduto() {

        return produto;
    }

    public void setProduto(String produto) {

        this.produto = produto;
    }

    public String getCodigoProduto() {

        return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {

        this.codigoProduto = codigoProduto;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {

        this.quantidade = quantidade;
    }

    public BigDecimal getValorUnitario() {

        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {

        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getValorTotal() {

        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {

        this.valorTotal = valorTotal;
    }

    public String getMunicipioFornecedor() {

        return municipioFornecedor;
    }

    public void setMunicipioFornecedor(String municipioFornecedor) {

        this.municipioFornecedor = municipioFornecedor;
    }

    public String getUfFornecedor() {

        return ufFornecedor;
    }

    public void setUfFornecedor(String ufFornecedor) {

        this.ufFornecedor = ufFornecedor;
    }

    public Integer getNumeroItem() {
        return numeroItem;
    }

    public void setNumeroItem(Integer numeroItem) {
        this.numeroItem = numeroItem;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public Long getIdNfe() {
        return idNfe;
    }

    public void setIdNfe(Long idNfe) {
        this.idNfe = idNfe;
    }

}