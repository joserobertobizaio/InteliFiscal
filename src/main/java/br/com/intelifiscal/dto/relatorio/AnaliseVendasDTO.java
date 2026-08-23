package br.com.intelifiscal.dto.relatorio;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AnaliseVendasDTO {

    private String cnpjCliente;
    private String cliente;

    private int notas;

    private LocalDate dataUltimaVenda;

    private double quantidade;

    private BigDecimal valorTotal;
    private BigDecimal participacao;


    //==================================================
    // CONSTRUTOR
    //==================================================

    public AnaliseVendasDTO() {
    }


    //==================================================
    // CNPJ CLIENTE
    //==================================================

    public String getCnpjCliente() {
        return cnpjCliente;
    }

    public void setCnpjCliente(String cnpjCliente) {
        this.cnpjCliente = cnpjCliente;
    }


    //==================================================
    // CLIENTE
    //==================================================

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
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
    // DATA DA ÚLTIMA VENDA
    //==================================================

    public LocalDate getDataUltimaVenda() {
        return dataUltimaVenda;
    }

    public void setDataUltimaVenda(LocalDate dataUltimaVenda) {
        this.dataUltimaVenda = dataUltimaVenda;
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