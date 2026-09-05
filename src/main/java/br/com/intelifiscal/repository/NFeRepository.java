package br.com.intelifiscal.repository;

import br.com.intelifiscal.database.connection.DatabaseConnection;
import br.com.intelifiscal.entity.NFe;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class NFeRepository {

    public Integer salvar(NFe nfe) {

        String sql = """
        INSERT INTO tblNFe
        (
            chave,
            modelo,
            numero,
            serie,
            tipo,
            data_emissao,
            cnpj_emitente,
            emitente,
            municipio_emitente,
            cnpj_destinatario,
            destinatario,
            municipio_destinatario,
            valor_total,
            situacao,
            data_importacao
        )
        VALUES
        (
            ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?
        )
        """;

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )

        ) {

            ps.setString(1, nfe.getChave());

            ps.setString(2, nfe.getModelo());

            ps.setString(3, nfe.getNumero());

            ps.setString(4, nfe.getSerie());

            ps.setString(5, nfe.getTipo());


            ps.setString(
                    6,
                    nfe.getDataEmissao() == null
                            ? null
                            : nfe.getDataEmissao().toString()
            );

            ps.setString(7, nfe.getCnpjEmitente());

            ps.setString(8, nfe.getEmitente());

            ps.setString(9, nfe.getMunicipioEmitente());

            ps.setString(10, nfe.getCnpjDestinatario());

            ps.setString(11, nfe.getDestinatario());

            ps.setString(12, nfe.getMunicipioDestinatario());

            ps.setBigDecimal(13, nfe.getValorTotal());

            ps.setString(14, nfe.getSituacao());

            ps.setString(
                    15,
                    nfe.getDataImportacao() == null
                            ? null
                            : nfe.getDataImportacao().toString()
            );

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }

            }

            throw new RuntimeException(
                    "Não foi possível recuperar o ID da NF-e."
            );

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao salvar NF-e.",
                    e
            );

        }

    }

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

                PreparedStatement ps =
                        connection.prepareStatement(sql)

        ) {

            ps.setString(1, chave);

            return ps.executeQuery().next();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao verificar chave da NF-e.",
                    e
            );

        }

    }

    // ============================================================
    // BUSCAR NF-e PARA GERAÇÃO DO PDF
    // ============================================================

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
                cnpj_destinatario,
                destinatario,
                municipio_destinatario,
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

                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setString(1, numero);
            ps.setString(2, serie);
            ps.setString(3, emitente);

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (rs.next()) {

                    NFe nfe =
                            new NFe();

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
                                LocalDate.parse(
                                        dataEmissao.substring(
                                                0,
                                                10
                                        )
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

                    nfe.setCnpjDestinatario(
                            rs.getString("cnpj_destinatario")
                    );

                    nfe.setDestinatario(
                            rs.getString("destinatario")
                    );

                    nfe.setMunicipioDestinatario(
                            rs.getString("municipio_destinatario")
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
                                LocalDateTime.parse(
                                        dataImportacao
                                )
                        );
                    }

                    return nfe;
                }

            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao buscar NF-e para geração do PDF.",
                    e
            );

        }

        return null;
    }

}