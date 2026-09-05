package br.com.intelifiscal.repository.produto;

import br.com.intelifiscal.database.connection.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProdutoVinculoRepository {

    // ============================================================
    // GARANTE A EXISTÊNCIA DA TABELA
    // ============================================================

    private void criarTabelaSeNecessario(
            Connection conn) throws SQLException {

        String sql = """
                CREATE TABLE IF NOT EXISTS tblProdutoVinculo (

                    id INTEGER PRIMARY KEY AUTOINCREMENT,

                    codigo_compra TEXT NOT NULL,

                    codigo_venda TEXT NOT NULL,

                    UNIQUE (
                        codigo_compra,
                        codigo_venda
                    )
                )
                """;

        try (PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.executeUpdate();
        }
    }


    // ============================================================
    // VINCULAR
    // ============================================================

    public void vincular(
            String codigoCompra,
            String codigoVenda) {

        String sql = """
                INSERT INTO tblProdutoVinculo (
                    codigo_compra,
                    codigo_venda
                )
                VALUES (?, ?)
                """;

        try (
                Connection conn =
                        DatabaseConnection.getConnection()
        ) {

            criarTabelaSeNecessario(conn);

            try (PreparedStatement ps =
                         conn.prepareStatement(sql)) {

                ps.setString(
                        1,
                        codigoCompra
                );

                ps.setString(
                        2,
                        codigoVenda
                );

                ps.executeUpdate();
            }

        } catch (SQLException e) {

            // ====================================================
            // JÁ EXISTE
            // ====================================================

            if (e.getMessage() != null
                    && e.getMessage()
                    .toLowerCase()
                    .contains("unique")) {

                throw new IllegalArgumentException(
                        "Os produtos já estão vinculados."
                );
            }

            throw new RuntimeException(
                    "Erro ao vincular os produtos.",
                    e
            );
        }
    }


    // ============================================================
    // VERIFICA VÍNCULO
    // ============================================================

    public boolean existeVinculo(
            String codigoCompra,
            String codigoVenda) {

        String sql = """
                SELECT 1
                FROM tblProdutoVinculo
                WHERE codigo_compra = ?
                  AND codigo_venda = ?
                LIMIT 1
                """;

        try (
                Connection conn =
                        DatabaseConnection.getConnection()
        ) {

            criarTabelaSeNecessario(conn);

            try (PreparedStatement ps =
                         conn.prepareStatement(sql)) {

                ps.setString(
                        1,
                        codigoCompra
                );

                ps.setString(
                        2,
                        codigoVenda
                );

                try (ResultSet rs =
                             ps.executeQuery()) {

                    return rs.next();
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao verificar vínculo dos produtos.",
                    e
            );
        }
    }


    // ============================================================
    // DESVINCULAR
    // ============================================================

    public void desvincular(
            String codigoCompra,
            String codigoVenda) {

        String sql = """
                DELETE FROM tblProdutoVinculo
                WHERE codigo_compra = ?
                  AND codigo_venda = ?
                """;

        try (
                Connection conn =
                        DatabaseConnection.getConnection()
        ) {

            criarTabelaSeNecessario(conn);

            try (PreparedStatement ps =
                         conn.prepareStatement(sql)) {

                ps.setString(
                        1,
                        codigoCompra
                );

                ps.setString(
                        2,
                        codigoVenda
                );

                ps.executeUpdate();
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao desvincular os produtos.",
                    e
            );
        }
    }
}