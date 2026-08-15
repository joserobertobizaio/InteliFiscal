package br.com.intelifiscal.dto.periodo;

import java.math.BigDecimal;

public class ResumoPeriodoDTO {

    private String operacao;

    private int notas;

    private int itens;

    private double quantidade;

    private BigDecimal valorTotal;

    private BigDecimal ticketMedio;


    //==================================================
    // CONSTRUTOR
    //==================================================

    public ResumoPeriodoDTO() {
    }


    //==================================================
    // GETTERS E SETTERS
    //==================================================

    public String getOperacao() {

        return operacao;
    }

    public void setOperacao(String operacao) {

        this.operacao = operacao;
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


    public BigDecimal getTicketMedio() {

        return ticketMedio;
    }

    public void setTicketMedio(BigDecimal ticketMedio) {

        this.ticketMedio = ticketMedio;
    }
}