package br.com.intelifiscal.repository;

import br.com.intelifiscal.database.connection.DatabaseConnection;
import br.com.intelifiscal.model.NFeEvento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsável pela persistência dos eventos das NF-e.
 *
 * @author José Roberto Bizaio
 */
public class NFeEventoRepository {

    // ============================================================
    // SALVAR EVENTO
    // ============================================================

    public Integer salvar(NFeEvento evento) {

        String sql = """
            INSERT INTO tblNFeEvento
            (
                id_nfe,
                chave_nfe,
                tipo_evento,
                sequencia,
                data_evento,
                protocolo,
                motivo,
                status,
                descricao,
                data_registro
            )
            VALUES
            (
                ?,?,?,?,?,?,?,?,?,?
            )
            """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        connection.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            if (evento.getIdNfe() == null) {

                ps.setNull(
                        1,
                        java.sql.Types.INTEGER
                );

            } else {

                ps.setLong(
                        1,
                        evento.getIdNfe()
                );
            }

            ps.setString(
                    2,
                    evento.getChaveNfe()
            );

            ps.setString(
                    3,
                    evento.getTipoEvento()
            );

            if (evento.getSequencia() == null) {

                ps.setNull(
                        4,
                        java.sql.Types.INTEGER
                );

            } else {

                ps.setInt(
                        4,
                        evento.getSequencia()
                );
            }

            ps.setString(
                    5,
                    evento.getDataEvento()
            );

            ps.setString(
                    6,
                    evento.getProtocolo()
            );

            ps.setString(
                    7,
                    evento.getMotivo()
            );

            ps.setString(
                    8,
                    evento.getStatus()
            );

            ps.setString(
                    9,
                    evento.getDescricao()
            );

            ps.setString(
                    10,
                    evento.getDataRegistro()
            );

            ps.executeUpdate();

            try (ResultSet rs =
                         ps.getGeneratedKeys()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            throw new RuntimeException(
                    "Não foi possível recuperar o ID do evento da NF-e."
            );

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao salvar evento da NF-e.",
                    e
            );
        }
    }

    // ============================================================
    // VERIFICAR SE EVENTO JÁ EXISTE PELO ID DA NF-e
    // ============================================================

    public boolean existeEvento(
            Long idNfe,
            String tipoEvento,
            Integer sequencia) {

        String sql = """
            SELECT 1
            FROM tblNFeEvento
            WHERE id_nfe = ?
              AND tipo_evento = ?
              AND (
                    sequencia = ?
                    OR (
                        sequencia IS NULL
                        AND ? IS NULL
                    )
              )
            LIMIT 1
            """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setLong(
                    1,
                    idNfe
            );

            ps.setString(
                    2,
                    tipoEvento
            );

            if (sequencia == null) {

                ps.setNull(
                        3,
                        java.sql.Types.INTEGER
                );

                ps.setNull(
                        4,
                        java.sql.Types.INTEGER
                );

            } else {

                ps.setInt(
                        3,
                        sequencia
                );

                ps.setInt(
                        4,
                        sequencia
                );
            }

            try (ResultSet rs =
                         ps.executeQuery()) {

                return rs.next();
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao verificar evento da NF-e.",
                    e
            );
        }
    }

    // ============================================================
    // VERIFICAR EVENTO PELA CHAVE DA NF-e
    // ============================================================

    public boolean existeEvento(
            String chaveNfe,
            String tipoEvento,
            Integer sequencia) {

        String sql = """
            SELECT 1
            FROM tblNFeEvento
            WHERE chave_nfe = ?
              AND tipo_evento = ?
              AND (
                    sequencia = ?
                    OR (
                        sequencia IS NULL
                        AND ? IS NULL
                    )
              )
            LIMIT 1
            """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    chaveNfe
            );

            ps.setString(
                    2,
                    tipoEvento
            );

            if (sequencia == null) {

                ps.setNull(
                        3,
                        java.sql.Types.INTEGER
                );

                ps.setNull(
                        4,
                        java.sql.Types.INTEGER
                );

            } else {

                ps.setInt(
                        3,
                        sequencia
                );

                ps.setInt(
                        4,
                        sequencia
                );
            }

            try (ResultSet rs =
                         ps.executeQuery()) {

                return rs.next();
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao verificar evento da NF-e pela chave.",
                    e
            );
        }
    }

    // ============================================================
    // BUSCAR EVENTOS PENDENTES
    // ============================================================
    //
    // Localiza eventos que chegaram antes da NF-e original.
    //
    // Esses eventos possuem:
    //
    // id_nfe = NULL
    //
    // e são identificados pela chave da NF-e.
    //
    // ============================================================

    public List<NFeEvento> buscarEventosPendentes(
            String chaveNfe) {

        String sql = """
            SELECT
                id,
                id_nfe,
                chave_nfe,
                tipo_evento,
                sequencia,
                data_evento,
                protocolo,
                motivo,
                status,
                descricao,
                data_registro
            FROM tblNFeEvento
            WHERE chave_nfe = ?
              AND id_nfe IS NULL
            ORDER BY id
            """;

        List<NFeEvento> eventos =
                new ArrayList<>();

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    chaveNfe
            );

            try (ResultSet rs =
                         ps.executeQuery()) {

                while (rs.next()) {

                    NFeEvento evento =
                            new NFeEvento();

                    evento.setId(
                            rs.getLong("id")
                    );

                    long idNfe =
                            rs.getLong("id_nfe");

                    if (!rs.wasNull()) {
                        evento.setIdNfe(idNfe);
                    }

                    evento.setChaveNfe(
                            rs.getString("chave_nfe")
                    );

                    evento.setTipoEvento(
                            rs.getString("tipo_evento")
                    );

                    int sequencia =
                            rs.getInt("sequencia");

                    if (!rs.wasNull()) {
                        evento.setSequencia(sequencia);
                    }

                    evento.setDataEvento(
                            rs.getString("data_evento")
                    );

                    evento.setProtocolo(
                            rs.getString("protocolo")
                    );

                    evento.setMotivo(
                            rs.getString("motivo")
                    );

                    evento.setStatus(
                            rs.getString("status")
                    );

                    evento.setDescricao(
                            rs.getString("descricao")
                    );

                    evento.setDataRegistro(
                            rs.getString("data_registro")
                    );

                    eventos.add(evento);
                }
            }

            return eventos;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao buscar eventos pendentes da NF-e.",
                    e
            );
        }
    }

    // ============================================================
    // VINCULAR EVENTO À NF-e
    // ============================================================

    public void vincularEvento(
            Long idEvento,
            Long idNfe) {

        String sql = """
            UPDATE tblNFeEvento
            SET id_nfe = ?
            WHERE id = ?
              AND id_nfe IS NULL
            """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setLong(
                    1,
                    idNfe
            );

            ps.setLong(
                    2,
                    idEvento
            );

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao vincular evento à NF-e.",
                    e
            );
        }
    }
}