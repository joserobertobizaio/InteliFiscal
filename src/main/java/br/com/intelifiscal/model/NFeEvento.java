package br.com.intelifiscal.model;

/**
 * Representa um evento vinculado a uma NF-e.
 *
 * Armazena eventos como cancelamento e outros eventos
 * que poderão ser suportados futuramente pelo sistema.
 *
 * @author José Roberto Bizaio
 */
public class NFeEvento {

    private Long id;
    private Long idNfe;
    private String chaveNfe;
    private String tipoEvento;
    private Integer sequencia;
    private String dataEvento;
    private String protocolo;
    private String motivo;
    private String status;
    private String descricao;
    private String dataRegistro;

    public NFeEvento() {
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

    public String getChaveNfe() {
        return chaveNfe;
    }

    public void setChaveNfe(String chaveNfe) {
        this.chaveNfe = chaveNfe;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public Integer getSequencia() {
        return sequencia;
    }

    public void setSequencia(Integer sequencia) {
        this.sequencia = sequencia;
    }

    public String getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(String dataEvento) {
        this.dataEvento = dataEvento;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(String dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
}