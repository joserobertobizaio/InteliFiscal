package br.com.intelifiscal.repository;

import br.com.intelifiscal.database.connection.DatabaseConnection;
import br.com.intelifiscal.entity.NFe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NFeRepository {

    //==================================================
    // SALVAR
    //==================================================

    public Integer salvar(NFe nfe) {

        String sql = """
                INSERT INTO tblNFe (
                    chave,
                    modelo,
                    numero,
                    serie,
                    tipo,
                    data_emissao,
                    cnpj_emitente,
                    emitente,
                    municipio_emitente,
                    uf_emitente,
                    cnpj_destinatario,
                    destinatario,
                    municipio_destinatario,
                    uf_destinatario,
                    valor_total,
                    situacao,
                    data_importacao
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql,
                                PreparedStatement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setString(1, nfe.getChave());
            statement.setString(2, nfe.getModelo());
            statement.setString(3, nfe.getNumero());
            statement.setString(4, nfe.getSerie());
            statement.setString(5, nfe.getTipo());

            if (nfe.getDataEmissao() != null) {

                statement.setString(
                        6,
                        nfe.getDataEmissao().toString()
                );

            } else {

                statement.setNull(
                        6,
                        java.sql.Types.VARCHAR
                );
            }

            statement.setString(
                    7,
                    nfe.getCnpjEmitente()
            );

            statement.setString(
                    8,
                    nfe.getEmitente()
            );

            statement.setString(
                    9,
                    nfe.getMunicipioEmitente()
            );

            statement.setString(
                    10,
                    nfe.getUfEmitente()
            );

            statement.setString(
                    11,
                    nfe.getCnpjDestinatario()
            );

            statement.setString(
                    12,
                    nfe.getDestinatario()
            );

            statement.setString(
                    13,
                    nfe.getMunicipioDestinatario()
            );

            statement.setString(
                    14,
                    nfe.getUfDestinatario()
            );

            if (nfe.getValorTotal() != null) {

                statement.setBigDecimal(
                        15,
                        nfe.getValorTotal()
                );

            } else {

                statement.setNull(
                        15,
                        java.sql.Types.NUMERIC
                );
            }

            statement.setString(
                    16,
                    nfe.getSituacao()
            );

            if (nfe.getDataImportacao() != null) {

                statement.setString(
                        17,
                        nfe.getDataImportacao().toString()
                );

            } else {

                statement.setNull(
                        17,
                        java.sql.Types.VARCHAR
                );
            }

            statement.executeUpdate();

            try (
                    ResultSet keys =
                            statement.getGeneratedKeys()
            ) {

                if (keys.next()) {

                    return keys.getInt(1);
                }
            }

            throw new SQLException(
                    "Não foi possível obter o ID da NF-e inserida."
            );

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao salvar NF-e.",
                    e
            );
        }
    }


    //==================================================
    // EXISTE POR CHAVE
    //==================================================

    public boolean existePorChave(String chave) {

        String sql = """
                SELECT 1
                FROM tblNFe
                WHERE chave = ?
                LIMIT 1
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    chave
            );

            try (
                    ResultSet rs =
                            statement.executeQuery()
            ) {

                return rs.next();
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao verificar existência da NF-e.",
                    e
            );
        }
    }


    //==================================================
    // BUSCAR ID POR CHAVE
    //==================================================

    public Long buscarIdPorChave(String chave) {

        String sql = """
                SELECT id
                FROM tblNFe
                WHERE chave = ?
                LIMIT 1
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    chave
            );

            try (
                    ResultSet rs =
                            statement.executeQuery()
            ) {

                if (rs.next()) {

                    return rs.getLong("id");
                }

                return null;
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao buscar ID da NF-e.",
                    e
            );
        }
    }


    //==================================================
    // REGISTRAR CANCELAMENTO
    //==================================================

    public void registrarCancelamento(
            String chave,
            String dataCancelamento,
            String protocoloCancelamento,
            String motivoCancelamento) {

        String sql = """
                UPDATE tblNFe
                SET
                    situacao = ?,
                    data_cancelamento = ?,
                    protocolo_cancelamento = ?,
                    motivo_cancelamento = ?
                WHERE chave = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    "CANCELADA"
            );

            statement.setString(
                    2,
                    dataCancelamento
            );

            statement.setString(
                    3,
                    protocoloCancelamento
            );

            statement.setString(
                    4,
                    motivoCancelamento
            );

            statement.setString(
                    5,
                    chave
            );

            int linhasAfetadas =
                    statement.executeUpdate();

            if (linhasAfetadas == 0) {

                throw new RuntimeException(
                        "NF-e não encontrada para registrar cancelamento."
                );
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao registrar cancelamento da NF-e.",
                    e
            );
        }
    }


    //==================================================
    // BUSCAR POR NÚMERO / SÉRIE / EMITENTE
    //==================================================

    public NFe buscarPorNumeroSerieEmitente(
            String numero,
            String serie,
            String emitente) {

        String sql = """
                SELECT
                    id,
                    chave,
                    modelo,
                    numero,
                    serie,
                    tipo,
                    data_emissao,
                    cnpj_emitente,
                    emitente,
                    municipio_emitente,
                    uf_emitente,
                    cnpj_destinatario,
                    destinatario,
                    municipio_destinatario,
                    uf_destinatario,
                    valor_total,
                    situacao,
                    data_importacao
                FROM tblNFe
                WHERE numero = ?
                  AND serie = ?
                  AND emitente = ?
                LIMIT 1
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    numero
            );

            statement.setString(
                    2,
                    serie
            );

            statement.setString(
                    3,
                    emitente
            );

            try (
                    ResultSet rs =
                            statement.executeQuery()
            ) {

                if (!rs.next()) {

                    return null;
                }

                return mapearNfe(rs);
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao buscar NF-e.",
                    e
            );
        }
    }


    //==================================================
    // PESQUISAR
    //==================================================

    public List<NFe> pesquisar(String texto) {

        String sql = """
                SELECT
                    id,
                    chave,
                    modelo,
                    numero,
                    serie,
                    tipo,
                    data_emissao,
                    cnpj_emitente,
                    emitente,
                    municipio_emitente,
                    uf_emitente,
                    cnpj_destinatario,
                    destinatario,
                    municipio_destinatario,
                    uf_destinatario,
                    valor_total,
                    situacao,
                    data_importacao
                FROM tblNFe
                WHERE
                    ? IS NULL
                    OR TRIM(?) = ''
                    OR numero LIKE ?
                    OR chave LIKE ?
                    OR emitente LIKE ?
                    OR destinatario LIKE ?
                    OR cnpj_emitente LIKE ?
                    OR cnpj_destinatario LIKE ?
                ORDER BY
                    data_emissao DESC,
                    id DESC
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            String filtro =
                    texto == null
                            ? null
                            : texto.trim();

            String like =
                    filtro == null || filtro.isBlank()
                            ? "%"
                            : "%" + filtro + "%";

            statement.setString(
                    1,
                    filtro
            );

            statement.setString(
                    2,
                    filtro
            );

            statement.setString(
                    3,
                    like
            );

            statement.setString(
                    4,
                    like
            );

            statement.setString(
                    5,
                    like
            );

            statement.setString(
                    6,
                    like
            );

            statement.setString(
                    7,
                    like
            );

            statement.setString(
                    8,
                    like
            );

            List<NFe> resultado =
                    new ArrayList<>();

            try (
                    ResultSet rs =
                            statement.executeQuery()
            ) {

                while (rs.next()) {

                    resultado.add(
                            mapearNfe(rs)
                    );
                }
            }

            return resultado;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao pesquisar NF-e.",
                    e
            );
        }
    }


    //==================================================
    // ATUALIZAR TIPO
    //==================================================

    public void atualizarTipo(
            Long id,
            String tipo) {

        if (id == null) {

            throw new IllegalArgumentException(
                    "ID da NF-e não pode ser nulo."
            );
        }

        if (tipo == null || tipo.isBlank()) {

            throw new IllegalArgumentException(
                    "Tipo da NF-e não pode ser vazio."
            );
        }

        String sql = """
                UPDATE tblNFe
                SET tipo = ?
                WHERE id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    tipo
            );

            statement.setLong(
                    2,
                    id
            );

            int linhasAfetadas =
                    statement.executeUpdate();

            if (linhasAfetadas == 0) {

                throw new RuntimeException(
                        "NF-e não encontrada para atualização."
                );
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao atualizar tipo da NF-e.",
                    e
            );
        }
    }


    //==================================================
    // EXCLUIR NF-e
    //==================================================

    public void excluir(Long id) {

        if (id == null) {

            throw new IllegalArgumentException(
                    "ID da NF-e não pode ser nulo."
            );
        }

        String sqlBuscarChave = """
                SELECT chave
                FROM tblNFe
                WHERE id = ?
                LIMIT 1
                """;

        String sqlExcluirItens = """
                DELETE FROM tblNFeItem
                WHERE id_nfe = ?
                """;

        String sqlExcluirDuplicatas = """
                DELETE FROM tblNFeDuplicata
                WHERE id_nfe = ?
                """;

        String sqlExcluirEventos = """
                DELETE FROM tblNFeEvento
                WHERE id_nfe = ?
                   OR (
                        chave_nfe = ?
                        AND id_nfe IS NULL
                   )
                """;

        String sqlExcluirNfe = """
                DELETE FROM tblNFe
                WHERE id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection()
        ) {

            boolean autoCommitOriginal =
                    connection.getAutoCommit();

            try {

                connection.setAutoCommit(false);

                String chave;

                //==========================================
                // LOCALIZAR CHAVE
                //==========================================

                try (
                        PreparedStatement statement =
                                connection.prepareStatement(
                                        sqlBuscarChave
                                )
                ) {

                    statement.setLong(
                            1,
                            id
                    );

                    try (
                            ResultSet rs =
                                    statement.executeQuery()
                    ) {

                        if (!rs.next()) {

                            throw new RuntimeException(
                                    "NF-e não encontrada para exclusão."
                            );
                        }

                        chave =
                                rs.getString("chave");
                    }
                }


                //==========================================
                // EXCLUIR ITENS
                //==========================================

                try (
                        PreparedStatement statement =
                                connection.prepareStatement(
                                        sqlExcluirItens
                                )
                ) {

                    statement.setLong(
                            1,
                            id
                    );

                    statement.executeUpdate();
                }


                //==========================================
                // EXCLUIR DUPLICATAS
                //==========================================

                try (
                        PreparedStatement statement =
                                connection.prepareStatement(
                                        sqlExcluirDuplicatas
                                )
                ) {

                    statement.setLong(
                            1,
                            id
                    );

                    statement.executeUpdate();
                }


                //==========================================
                // EXCLUIR EVENTOS
                //==========================================

                try (
                        PreparedStatement statement =
                                connection.prepareStatement(
                                        sqlExcluirEventos
                                )
                ) {

                    statement.setLong(
                            1,
                            id
                    );

                    statement.setString(
                            2,
                            chave
                    );

                    statement.executeUpdate();
                }


                //==========================================
                // EXCLUIR NF-e
                //==========================================

                try (
                        PreparedStatement statement =
                                connection.prepareStatement(
                                        sqlExcluirNfe
                                )
                ) {

                    statement.setLong(
                            1,
                            id
                    );

                    int linhasAfetadas =
                            statement.executeUpdate();

                    if (linhasAfetadas == 0) {

                        throw new RuntimeException(
                                "NF-e não encontrada para exclusão."
                        );
                    }
                }


                //==========================================
                // CONFIRMAR TRANSAÇÃO
                //==========================================

                connection.commit();

            } catch (Exception e) {

                try {

                    connection.rollback();

                } catch (SQLException rollbackException) {

                    e.addSuppressed(
                            rollbackException
                    );
                }

                throw e;

            } finally {

                connection.setAutoCommit(
                        autoCommitOriginal
                );
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao excluir NF-e.",
                    e
            );

        } catch (RuntimeException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro ao excluir NF-e.",
                    e
            );
        }
    }


    //==================================================
    // MAPEAR NF-e
    //==================================================

    private NFe mapearNfe(ResultSet rs)
            throws SQLException {

        NFe nfe = new NFe();

        nfe.setId(
                rs.getLong("id")
        );

        nfe.setChave(
                rs.getString("chave")
        );

        nfe.setModelo(
                rs.getString("modelo")
        );

        nfe.setNumero(
                rs.getString("numero")
        );

        nfe.setSerie(
                rs.getString("serie")
        );

        nfe.setTipo(
                rs.getString("tipo")
        );

        String dataEmissao =
                rs.getString("data_emissao");

        if (dataEmissao != null
                && !dataEmissao.isBlank()) {

            nfe.setDataEmissao(
                    java.time.LocalDate.parse(
                            dataEmissao
                    )
            );
        }

        nfe.setCnpjEmitente(
                rs.getString("cnpj_emitente")
        );

        nfe.setEmitente(
                rs.getString("emitente")
        );

        nfe.setMunicipioEmitente(
                rs.getString("municipio_emitente")
        );

        nfe.setUfEmitente(
                rs.getString("uf_emitente")
        );

        nfe.setCnpjDestinatario(
                rs.getString("cnpj_destinatario")
        );

        nfe.setDestinatario(
                rs.getString("destinatario")
        );

        nfe.setMunicipioDestinatario(
                rs.getString("municipio_destinatario")
        );

        nfe.setUfDestinatario(
                rs.getString("uf_destinatario")
        );

        nfe.setValorTotal(
                rs.getBigDecimal("valor_total")
        );

        nfe.setSituacao(
                rs.getString("situacao")
        );

        String dataImportacao =
                rs.getString("data_importacao");

        if (dataImportacao != null
                && !dataImportacao.isBlank()) {

            nfe.setDataImportacao(
                    java.time.LocalDateTime.parse(
                            dataImportacao
                    )
            );
        }

        return nfe;
    }

}