package br.com.intelifiscal.dto.periodo;

import java.math.BigDecimal;

public class ResumoMensalDTO {

    private String mes;
    private String operacao;
    private BigDecimal valorTotal;

    //==================================================
    // CONSTRUTOR
    //==================================================

    public ResumoMensalDTO() {
    }

    //==================================================
    // GETTERS E SETTERS
    //==================================================

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
}