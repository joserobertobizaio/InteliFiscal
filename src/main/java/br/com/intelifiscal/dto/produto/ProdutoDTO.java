package br.com.intelifiscal.dto.produto;

import java.time.LocalDateTime;

public class ProdutoDTO {

    private int id;

    private String codigoProduto;

    private String codigoBarras;

    private String descricao;

    private String ncm;

    private String cest;

    private String unidade;

    private LocalDateTime dataCadastro;

    private boolean ativo;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }


    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }


    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


    public String getNcm() {
        return ncm;
    }

    public void setNcm(String ncm) {
        this.ncm = ncm;
    }


    public String getCest() {
        return cest;
    }

    public void setCest(String cest) {
        this.cest = cest;
    }


    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }


    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }


    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

}