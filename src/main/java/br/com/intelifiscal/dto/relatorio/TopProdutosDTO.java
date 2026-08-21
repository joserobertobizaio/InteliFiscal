package br.com.intelifiscal.dto.relatorio;

import java.math.BigDecimal;

public class TopProdutosDTO {

    private Integer posicao;
    private String codigoProduto;
    private String descricao;
    private BigDecimal quantidadeVendida;
    private BigDecimal valorVendido;

    public TopProdutosDTO() {
    }

    public Integer getPosicao() {
        return posicao;
    }

    public void setPosicao(Integer posicao) {
        this.posicao = posicao;
    }

    public String getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getQuantidadeVendida() {
        return quantidadeVendida;
    }

    public void setQuantidadeVendida(BigDecimal quantidadeVendida) {
        this.quantidadeVendida = quantidadeVendida;
    }

    public BigDecimal getValorVendido() {
        return valorVendido;
    }

    public void setValorVendido(BigDecimal valorVendido) {
        this.valorVendido = valorVendido;
    }
}