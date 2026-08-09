package br.com.intelifiscal.repository.produto;

import br.com.intelifiscal.database.connection.DatabaseConnection;
import br.com.intelifiscal.dto.produto.ProdutoDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {

    public void salvar(ProdutoDTO produto) {

        String sql = """
                INSERT INTO tblProduto (
                    codigo_produto,
                    codigo_barras,
                    descricao,
                    ncm,
                    cest,
                    unidade,
                    data_cadastro,
                    ativo
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, produto.getCodigoProduto());
            ps.setString(2, produto.getCodigoBarras());
            ps.setString(3, produto.getDescricao());
            ps.setString(4, produto.getNcm());
            ps.setString(5, produto.getCest());
            ps.setString(6, produto.getUnidade());

            ps.setString(
                    7,
                    produto.getDataCadastro() == null
                            ? LocalDateTime.now().toString()
                            : produto.getDataCadastro().toString()
            );

            ps.setInt(
                    8,
                    produto.isAtivo() ? 1 : 0
            );

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao salvar produto.",
                    e
            );
        }
    }


    public void atualizar(ProdutoDTO produto) {

        String sql = """
                UPDATE tblProduto
                SET
                    codigo_produto = ?,
                    codigo_barras = ?,
                    descricao = ?,
                    ncm = ?,
                    cest = ?,
                    unidade = ?,
                    ativo = ?
                WHERE id = ?
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, produto.getCodigoProduto());
            ps.setString(2, produto.getCodigoBarras());
            ps.setString(3, produto.getDescricao());
            ps.setString(4, produto.getNcm());
            ps.setString(5, produto.getCest());
            ps.setString(6, produto.getUnidade());

            ps.setInt(
                    7,
                    produto.isAtivo() ? 1 : 0
            );

            ps.setInt(
                    8,
                    produto.getId()
            );

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao atualizar produto.",
                    e
            );
        }
    }


    public void excluir(int id) {

        String sql = """
                DELETE FROM tblProduto
                WHERE id = ?
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao excluir produto.",
                    e
            );
        }
    }


    public ProdutoDTO buscarPorCodigoProduto(
            String codigoProduto) {

        String sql = """
                SELECT
                    id,
                    codigo_produto,
                    codigo_barras,
                    descricao,
                    ncm,
                    cest,
                    unidade,
                    data_cadastro,
                    ativo
                FROM tblProduto
                WHERE codigo_produto = ?
                LIMIT 1
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, codigoProduto);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapear(rs);
                }
            }

            return null;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao buscar produto pelo código.",
                    e
            );
        }
    }


    public ProdutoDTO buscarPorCodigoBarras(
            String codigoBarras) {

        String sql = """
                SELECT
                    id,
                    codigo_produto,
                    codigo_barras,
                    descricao,
                    ncm,
                    cest,
                    unidade,
                    data_cadastro,
                    ativo
                FROM tblProduto
                WHERE codigo_barras = ?
                LIMIT 1
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, codigoBarras);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapear(rs);
                }
            }

            return null;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao buscar produto pelo código de barras.",
                    e
            );
        }
    }


    public List<ProdutoDTO> listarTodos() {

        String sql = """
                SELECT
                    id,
                    codigo_produto,
                    codigo_barras,
                    descricao,
                    ncm,
                    cest,
                    unidade,
                    data_cadastro,
                    ativo
                FROM tblProduto
                ORDER BY descricao
                """;

        List<ProdutoDTO> produtos =
                new ArrayList<>();

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                produtos.add(mapear(rs));
            }

            return produtos;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao listar produtos.",
                    e
            );
        }
    }


    private ProdutoDTO mapear(ResultSet rs)
            throws SQLException {

        ProdutoDTO produto =
                new ProdutoDTO();

        produto.setId(
                rs.getInt("id")
        );

        produto.setCodigoProduto(
                rs.getString("codigo_produto")
        );

        produto.setCodigoBarras(
                rs.getString("codigo_barras")
        );

        produto.setDescricao(
                rs.getString("descricao")
        );

        produto.setNcm(
                rs.getString("ncm")
        );

        produto.setCest(
                rs.getString("cest")
        );

        produto.setUnidade(
                rs.getString("unidade")
        );

        String dataCadastro =
                rs.getString("data_cadastro");

        if (dataCadastro != null
                && !dataCadastro.isBlank()) {

            produto.setDataCadastro(
                    LocalDateTime.parse(dataCadastro)
            );
        }

        produto.setAtivo(
                rs.getInt("ativo") == 1
        );

        return produto;
    }
}