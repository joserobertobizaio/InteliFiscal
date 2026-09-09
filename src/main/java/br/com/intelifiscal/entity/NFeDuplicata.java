package br.com.intelifiscal.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class NFeDuplicata {

    private Long id;

    private Long idNfe;

    private String numeroDuplicata;

    private LocalDate dataVencimento;

    private BigDecimal valor;

    public NFeDuplicata() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdNfe() {
        return idNfe;
    }

    public void setIdNfe(Long idNfe) {
        this.idNfe = idNfe;
    }

    public String getNumeroDuplicata() {
        return numeroDuplicata;
    }

    public void setNumeroDuplicata(String numeroDuplicata) {
        this.numeroDuplicata = numeroDuplicata;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

}