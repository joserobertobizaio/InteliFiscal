package br.com.intelifiscal.dto.produto;

import java.time.LocalDateTime;

public class ProdutoHistoricoDTO {

    private String tipo;

    private String numeroNfe;

    private String serie;

    private LocalDateTime dataEmissao;

    private String emitente;

    private String destinatario;

    private Double quantidade;

    private Double valorUnitario;

    private Double valorTotal;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNumeroNfe() {
        return numeroNfe;
    }

    public void setNumeroNfe(String numeroNfe) {
        this.numeroNfe = numeroNfe;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public LocalDateTime getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDateTime dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public String getEmitente() {
        return emitente;
    }

    public void setEmitente(String emitente) {
        this.emitente = emitente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public Double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(Double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }
}