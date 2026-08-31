package br.com.intelifiscal.repository.produto;

import br.com.intelifiscal.database.connection.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoGrupoRepository {

    /**
     * Cria um novo grupo de produtos.
     *
     * @return id do grupo criado
     */
    public int criarGrupo() {

        String sql = """
                INSERT INTO tblProdutoGrupo
                    (data_criacao)
                VALUES
                    (CURRENT_TIMESTAMP)
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            throw new RuntimeException(
                    "Não foi possível obter o ID do grupo criado."
            );

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao criar grupo de produtos.",
                    e
            );
        }
    }


    /**
     * Adiciona um produto a um grupo.
     */
    public void adicionarProdutoAoGrupo(
            int idGrupo,
            int idProduto) {

        String sql = """
                INSERT INTO tblProdutoGrupoItem
                    (id_grupo, id_produto)
                VALUES
                    (?, ?)
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idGrupo);
            ps.setInt(2, idProduto);

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao adicionar produto ao grupo.",
                    e
            );
        }
    }


    /**
     * Retorna o ID do grupo ao qual o produto pertence.
     *
     * @return id do grupo ou null caso não exista vínculo
     */
    public Integer buscarGrupoDoProduto(
            int idProduto) {

        String sql = """
                SELECT id_grupo
                FROM tblProdutoGrupoItem
                WHERE id_produto = ?
                LIMIT 1
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idProduto);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("id_grupo");
                }
            }

            return null;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao buscar grupo do produto.",
                    e
            );
        }
    }


    /**
     * Retorna todos os IDs dos produtos
     * pertencentes ao mesmo grupo.
     */
    public List<Integer> listarProdutosDoGrupo(
            int idGrupo) {

        String sql = """
                SELECT id_produto
                FROM tblProdutoGrupoItem
                WHERE id_grupo = ?
                ORDER BY id_produto
                """;

        List<Integer> produtos = new ArrayList<>();

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idGrupo);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    produtos.add(
                            rs.getInt("id_produto")
                    );
                }
            }

            return produtos;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao listar produtos do grupo.",
                    e
            );
        }
    }


    /**
     * Remove um produto de um grupo.
     */
    public void removerProdutoDoGrupo(
            int idGrupo,
            int idProduto) {

        String sql = """
                DELETE FROM tblProdutoGrupoItem
                WHERE id_grupo = ?
                  AND id_produto = ?
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idGrupo);
            ps.setInt(2, idProduto);

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao remover produto do grupo.",
                    e
            );
        }
    }


    /**
     * Remove todos os produtos de um grupo.
     *
     * O grupo em si continua existindo.
     */
    public void removerTodosProdutosDoGrupo(
            int idGrupo) {

        String sql = """
                DELETE FROM tblProdutoGrupoItem
                WHERE id_grupo = ?
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idGrupo);

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao remover produtos do grupo.",
                    e
            );
        }
    }


    /**
     * Exclui o grupo.
     *
     * Graças ao ON DELETE CASCADE,
     * os vínculos da tblProdutoGrupoItem
     * também serão excluídos.
     */
    public void excluirGrupo(
            int idGrupo) {

        String sql = """
                DELETE FROM tblProdutoGrupo
                WHERE id = ?
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idGrupo);

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao excluir grupo de produtos.",
                    e
            );
        }
    }
}