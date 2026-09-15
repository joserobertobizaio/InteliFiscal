package br.com.intelifiscal.service;

import br.com.intelifiscal.model.NFeEvento;
import br.com.intelifiscal.repository.NFeEventoRepository;

import java.util.List;

/**
 * Service responsável pelas regras de negócio dos eventos das NF-e.
 *
 * @author José Roberto Bizaio
 */
public class NFeEventoService {

    private final NFeEventoRepository repository =
            new NFeEventoRepository();

    // ============================================================
    // SALVAR EVENTO
    // ============================================================

    public Integer salvar(NFeEvento evento) {

        if (evento == null) {
            throw new IllegalArgumentException(
                    "Evento da NF-e não informado."
            );
        }

        if (evento.getChaveNfe() == null
                || evento.getChaveNfe().isBlank()) {

            throw new IllegalArgumentException(
                    "Chave da NF-e não informada."
            );
        }

        if (evento.getTipoEvento() == null
                || evento.getTipoEvento().isBlank()) {

            throw new IllegalArgumentException(
                    "Tipo do evento não informado."
            );
        }

        // --------------------------------------------------------
        // EVENTO JÁ REGISTRADO
        // --------------------------------------------------------

        if (repository.existeEvento(
                evento.getChaveNfe(),
                evento.getTipoEvento(),
                evento.getSequencia()
        )) {

            return null;
        }

        // --------------------------------------------------------
        // SALVA NOVO EVENTO
        // --------------------------------------------------------

        return repository.salvar(evento);
    }

    // ============================================================
    // VERIFICAR EVENTO PELA CHAVE DA NF-e
    // ============================================================

    public boolean existeEvento(
            String chaveNfe,
            String tipoEvento,
            Integer sequencia) {

        return repository.existeEvento(
                chaveNfe,
                tipoEvento,
                sequencia
        );
    }

    // ============================================================
    // VERIFICAR EVENTO PELO ID DA NF-e
    // ============================================================
    //
    // Mantido para compatibilidade com eventuais chamadas
    // existentes no sistema.
    // ============================================================

    public boolean existeEvento(
            Long idNfe,
            String tipoEvento,
            Integer sequencia) {

        return repository.existeEvento(
                idNfe,
                tipoEvento,
                sequencia
        );
    }

    // ============================================================
    // BUSCAR EVENTOS PENDENTES
    // ============================================================
    //
    // Retorna os eventos que foram importados antes da NF-e
    // original e ainda não possuem id_nfe.
    //
    // ============================================================

    public List<NFeEvento> buscarEventosPendentes(
            String chaveNfe) {

        if (chaveNfe == null
                || chaveNfe.isBlank()) {

            throw new IllegalArgumentException(
                    "Chave da NF-e não informada."
            );
        }

        return repository.buscarEventosPendentes(
                chaveNfe
        );
    }

    // ============================================================
    // VINCULAR EVENTO À NF-e
    // ============================================================

    public void vincularEvento(
            Long idEvento,
            Long idNfe) {

        if (idEvento == null) {

            throw new IllegalArgumentException(
                    "ID do evento não informado."
            );
        }

        if (idNfe == null) {

            throw new IllegalArgumentException(
                    "ID da NF-e não informado."
            );
        }

        repository.vincularEvento(
                idEvento,
                idNfe
        );
    }
}