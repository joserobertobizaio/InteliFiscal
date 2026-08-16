package br.com.intelifiscal.dto.produto;

public class ProdutoHistoricoResumoDTO {

    private double quantidadeComprada;
    private double quantidadeVendida;

    private double valorTotalComprado;
    private double valorTotalVendido;

    private double menorPrecoCompra;
    private double maiorPrecoCompra;
    private double precoMedioCompra;

    private double menorPrecoVenda;
    private double maiorPrecoVenda;
    private double precoMedioVenda;


    public double getQuantidadeComprada() {
        return quantidadeComprada;
    }

    public void setQuantidadeComprada(double quantidadeComprada) {
        this.quantidadeComprada = quantidadeComprada;
    }


    public double getQuantidadeVendida() {
        return quantidadeVendida;
    }

    public void setQuantidadeVendida(double quantidadeVendida) {
        this.quantidadeVendida = quantidadeVendida;
    }


    public double getValorTotalComprado() {
        return valorTotalComprado;
    }

    public void setValorTotalComprado(double valorTotalComprado) {
        this.valorTotalComprado = valorTotalComprado;
    }


    public double getValorTotalVendido() {
        return valorTotalVendido;
    }

    public void setValorTotalVendido(double valorTotalVendido) {
        this.valorTotalVendido = valorTotalVendido;
    }


    public double getMenorPrecoCompra() {
        return menorPrecoCompra;
    }

    public void setMenorPrecoCompra(double menorPrecoCompra) {
        this.menorPrecoCompra = menorPrecoCompra;
    }


    public double getMaiorPrecoCompra() {
        return maiorPrecoCompra;
    }

    public void setMaiorPrecoCompra(double maiorPrecoCompra) {
        this.maiorPrecoCompra = maiorPrecoCompra;
    }


    public double getPrecoMedioCompra() {
        return precoMedioCompra;
    }

    public void setPrecoMedioCompra(double precoMedioCompra) {
        this.precoMedioCompra = precoMedioCompra;
    }


    public double getMenorPrecoVenda() {
        return menorPrecoVenda;
    }

    public void setMenorPrecoVenda(double menorPrecoVenda) {
        this.menorPrecoVenda = menorPrecoVenda;
    }


    public double getMaiorPrecoVenda() {
        return maiorPrecoVenda;
    }

    public void setMaiorPrecoVenda(double maiorPrecoVenda) {
        this.maiorPrecoVenda = maiorPrecoVenda;
    }


    public double getPrecoMedioVenda() {
        return precoMedioVenda;
    }

    public void setPrecoMedioVenda(double precoMedioVenda) {
        this.precoMedioVenda = precoMedioVenda;
    }
}