package br.com.intelifiscal.dto.produto;

import java.time.LocalDateTime;

public class ProdutoVinculoDTO {

    private int id;

    private int idProduto;

    private String codigoProdutoNfe;

    private String descricaoProdutoNfe;

    private String cnpjEmitente;

    private LocalDateTime dataVinculo;

    private boolean ativo;


    // ============================================================
    // ID
    // ============================================================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    // ============================================================
    // ID PRODUTO
    // ============================================================

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }


    // ============================================================
    // CÓDIGO DO PRODUTO NA NF-e
    // ============================================================

    public String getCodigoProdutoNfe() {
        return codigoProdutoNfe;
    }

    public void setCodigoProdutoNfe(String codigoProdutoNfe) {
        this.codigoProdutoNfe = codigoProdutoNfe;
    }


    // ============================================================
    // DESCRIÇÃO DO PRODUTO NA NF-e
    // ============================================================

    public String getDescricaoProdutoNfe() {
        return descricaoProdutoNfe;
    }

    public void setDescricaoProdutoNfe(String descricaoProdutoNfe) {
        this.descricaoProdutoNfe = descricaoProdutoNfe;
    }


    // ============================================================
    // CNPJ DO EMITENTE
    // ============================================================

    public String getCnpjEmitente() {
        return cnpjEmitente;
    }

    public void setCnpjEmitente(String cnpjEmitente) {
        this.cnpjEmitente = cnpjEmitente;
    }


    // ============================================================
    // DATA DO VÍNCULO
    // ============================================================

    public LocalDateTime getDataVinculo() {
        return dataVinculo;
    }

    public void setDataVinculo(LocalDateTime dataVinculo) {
        this.dataVinculo = dataVinculo;
    }


    // ============================================================
    // ATIVO
    // ============================================================

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}