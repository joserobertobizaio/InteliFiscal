package br.com.intelifiscal.dto.relatorio;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AnaliseComprasDTO {

    private String cnpjFornecedor;
    private String fornecedor;

    private int notas;

    private LocalDate dataUltimaCompra;

    private double quantidade;

    private BigDecimal valorTotal;
    private BigDecimal participacao;


    //==================================================
    // CONSTRUTOR
    //==================================================

    public AnaliseComprasDTO() {
    }


    //==================================================
    // CNPJ FORNECEDOR
    //==================================================

    public String getCnpjFornecedor() {
        return cnpjFornecedor;
    }

    public void setCnpjFornecedor(String cnpjFornecedor) {
        this.cnpjFornecedor = cnpjFornecedor;
    }


    //==================================================
    // FORNECEDOR
    //==================================================

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }


    //==================================================
    // NOTAS
    //==================================================

    public int getNotas() {
        return notas;
    }

    public void setNotas(int notas) {
        this.notas = notas;
    }


    //==================================================
    // DATA DA ÚLTIMA COMPRA
    //==================================================

    public LocalDate getDataUltimaCompra() {
        return dataUltimaCompra;
    }

    public void setDataUltimaCompra(LocalDate dataUltimaCompra) {
        this.dataUltimaCompra = dataUltimaCompra;
    }


    //==================================================
    // QUANTIDADE
    //==================================================

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }


    //==================================================
    // VALOR TOTAL
    //==================================================

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }


    //==================================================
    // PARTICIPAÇÃO
    //==================================================

    public BigDecimal getParticipacao() {
        return participacao;
    }

    public void setParticipacao(BigDecimal participacao) {
        this.participacao = participacao;
    }
}