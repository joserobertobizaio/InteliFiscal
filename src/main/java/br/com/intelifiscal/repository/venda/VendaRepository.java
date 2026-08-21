package br.com.intelifiscal.repository.venda;

import br.com.intelifiscal.database.connection.DatabaseConnection;
import br.com.intelifiscal.dto.venda.VendaDTO;
import br.com.intelifiscal.dto.venda.VendaItemDTO;
import br.com.intelifiscal.dto.relatorio.TopProdutosDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VendaRepository {

    public List<VendaDTO> listarTodas() {

        String sql = """
            SELECT
                id,
                chave,
                numero,
                serie,
                data_emissao,
                cnpj_destinatario,
                destinatario,
                valor_total,
                situacao

            FROM tblNFe

            WHERE tipo = 'Venda'

            ORDER BY data_emissao DESC, id DESC
            """;

        List<VendaDTO> lista = new ArrayList<>();

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                VendaDTO dto =
                        new VendaDTO();

                dto.setId(
                        rs.getInt("id")
                );

                dto.setChave(
                        rs.getString("chave")
                );

                dto.setNumero(
                        rs.getString("numero")
                );

                dto.setSerie(
                        rs.getString("serie")
                );

                String data =
                        rs.getString("data_emissao");

                if (data != null && !data.isBlank()) {

                    dto.setDataEmissao(
                            java.time.LocalDate.parse(data)
                    );
                }

                dto.setCnpjDestinatario(
                        rs.getString("cnpj_destinatario")
                );

                dto.setDestinatario(
                        rs.getString("destinatario")
                );

                dto.setValorTotal(
                        rs.getBigDecimal("valor_total")
                );

                dto.setSituacao(
                        rs.getString("situacao")
                );

                lista.add(dto);
            }

            return lista;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao listar vendas.",
                    e
            );
        }
    }

    public List<VendaItemDTO> listarItensPorNfe(
            Integer idNfe) {

        String sql = """
        SELECT
            id_nfe,
            numero_item,
            codigo_produto,
            codigo_barras,
            descricao,
            unidade,
            quantidade,
            valor_unitario,
            valor_total,
            desconto

        FROM tblNFeItem

        WHERE id_nfe = ?

        ORDER BY numero_item
        """;

        List<VendaItemDTO> lista =
                new ArrayList<>();

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idNfe);

            try (ResultSet rs =
                         ps.executeQuery()) {

                while (rs.next()) {

                    VendaItemDTO dto =
                            new VendaItemDTO();

                    dto.setIdNfe(
                            rs.getInt("id_nfe")
                    );

                    dto.setNumeroItem(
                            rs.getInt("numero_item")
                    );

                    dto.setCodigoProduto(
                            rs.getString("codigo_produto")
                    );

                    dto.setCodigoBarras(
                            rs.getString("codigo_barras")
                    );

                    dto.setDescricao(
                            rs.getString("descricao")
                    );

                    dto.setUnidade(
                            rs.getString("unidade")
                    );

                    dto.setQuantidade(
                            rs.getBigDecimal("quantidade")
                    );

                    dto.setValorUnitario(
                            rs.getBigDecimal("valor_unitario")
                    );

                    dto.setValorTotal(
                            rs.getBigDecimal("valor_total")
                    );

                    dto.setDesconto(
                            rs.getBigDecimal("desconto")
                    );

                    lista.add(dto);
                }
            }

            return lista;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao listar itens da venda.",
                    e
            );
        }
    }

    public List<TopProdutosDTO> listarTopProdutos(int limite) {

        String sql = """
        SELECT
            i.codigo_produto,
            i.descricao,
            SUM(i.quantidade) AS quantidade_vendida,
            SUM(i.valor_total) AS valor_vendido

        FROM tblNFe n

        INNER JOIN tblNFeItem i
            ON i.id_nfe = n.id

        WHERE n.tipo = 'Venda'
          AND date(n.data_emissao) >= date('now', '-12 months')

        GROUP BY
            i.codigo_produto,
            i.descricao

        ORDER BY
            quantidade_vendida DESC

        LIMIT ?
        """;

        List<TopProdutosDTO> lista = new ArrayList<>();

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(1, limite);

            try (ResultSet rs = ps.executeQuery()) {

                int posicao = 1;

                while (rs.next()) {

                    TopProdutosDTO dto =
                            new TopProdutosDTO();

                    dto.setPosicao(posicao++);

                    dto.setCodigoProduto(
                            rs.getString("codigo_produto")
                    );

                    dto.setDescricao(
                            rs.getString("descricao")
                    );

                    dto.setQuantidadeVendida(
                            rs.getBigDecimal("quantidade_vendida")
                    );

                    dto.setValorVendido(
                            rs.getBigDecimal("valor_vendido")
                    );

                    lista.add(dto);
                }
            }

            return lista;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao listar os produtos mais vendidos.",
                    e
            );
        }
    }

}