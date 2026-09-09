package br.com.intelifiscal.repository;

import br.com.intelifiscal.database.connection.DatabaseConnection;
import br.com.intelifiscal.entity.NFeDuplicata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NFeDuplicataRepository {

    public void salvar(NFeDuplicata duplicata) {

        String sql = """
            INSERT INTO tblNFeDuplicata
            (
                id_nfe,
                numero_duplicata,
                data_vencimento,
                valor
            )
            VALUES
            (
                ?,?,?,?
            )
            """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setLong(
                    1,
                    duplicata.getIdNfe()
            );

            ps.setString(
                    2,
                    duplicata.getNumeroDuplicata()
            );

            ps.setString(
                    3,
                    duplicata.getDataVencimento() == null
                            ? null
                            : duplicata.getDataVencimento().toString()
            );

            ps.setBigDecimal(
                    4,
                    duplicata.getValor()
            );

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao salvar duplicata da NF-e.",
                    e
            );

        }

    }

    public List<NFeDuplicata> listarPorNfe(Long idNfe) {

        String sql = """
            SELECT
                id,
                id_nfe,
                numero_duplicata,
                data_vencimento,
                valor

            FROM tblNFeDuplicata

            WHERE id_nfe = ?

            ORDER BY
                numero_duplicata
            """;

        List<NFeDuplicata> duplicatas =
                new ArrayList<>();

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setLong(1, idNfe);

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                while (rs.next()) {

                    NFeDuplicata duplicata =
                            new NFeDuplicata();

                    duplicata.setId(
                            rs.getLong("id")
                    );

                    duplicata.setIdNfe(
                            rs.getLong("id_nfe")
                    );

                    duplicata.setNumeroDuplicata(
                            rs.getString(
                                    "numero_duplicata"
                            )
                    );

                    String dataVencimento =
                            rs.getString(
                                    "data_vencimento"
                            );

                    if (dataVencimento != null
                            && !dataVencimento.isBlank()) {

                        duplicata.setDataVencimento(
                                java.time.LocalDate.parse(
                                        dataVencimento
                                )
                        );
                    }

                    duplicata.setValor(
                            rs.getBigDecimal("valor")
                    );

                    duplicatas.add(duplicata);
                }

            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao buscar duplicatas da NF-e.",
                    e
            );

        }

        return duplicatas;
    }

    public boolean existe(
            Long idNfe,
            String numeroDuplicata
    ) {

        String sql = """
        SELECT 1
        FROM tblNFeDuplicata
        WHERE id_nfe = ?
          AND numero_duplicata = ?
        LIMIT 1
        """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setLong(1, idNfe);
            ps.setString(2, numeroDuplicata);

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                return rs.next();
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao verificar existência da duplicata da NF-e.",
                    e
            );
        }
    }

}