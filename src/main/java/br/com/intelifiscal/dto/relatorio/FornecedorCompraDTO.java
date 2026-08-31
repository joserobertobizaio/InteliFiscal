package br.com.intelifiscal.dto.relatorio;

import java.math.BigDecimal;

public class FornecedorCompraDTO {

    private String fornecedor;
    private int notas;
    private int itens;
    private double quantidade;
    private BigDecimal valorTotal;
    private String dataUltimaCompra;

    //==================================================
    // CONSTRUTOR
    //==================================================

    public FornecedorCompraDTO() {
    }

    //==================================================
    // GETTERS E SETTERS
    //==================================================

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public int getNotas() {
        return notas;
    }

    public void setNotas(int notas) {
        this.notas = notas;
    }

    public int getItens() {
        return itens;
    }

    public void setItens(int itens) {
        this.itens = itens;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getDataUltimaCompra() {
        return dataUltimaCompra;
    }

    public void setDataUltimaCompra(String dataUltimaCompra) {
        this.dataUltimaCompra = dataUltimaCompra;
    }
}