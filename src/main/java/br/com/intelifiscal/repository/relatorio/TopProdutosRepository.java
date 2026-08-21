package br.com.intelifiscal.repository.relatorio;

import br.com.intelifiscal.dto.relatorio.TopProdutosDTO;
import br.com.intelifiscal.database.connection.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TopProdutosRepository {

    /**
     * Retorna os produtos mais vendidos nos últimos 12 meses.
     *
     * O ranking inicial é feito pela quantidade vendida,
     * em ordem decrescente.
     *
     * @param limite quantidade de produtos desejada
     *              (5, 10, 20 ou 50)
     */
    public List<TopProdutosDTO> listarTopProdutos(int limite) {

        String sql = """
    SELECT
        i.codigo_produto,
        i.descricao,
        SUM(i.quantidade) AS quantidade_vendida,
        SUM(i.valor_total) AS valor_vendido

    FROM tblNFeItem i

    INNER JOIN tblNFe n
        ON n.id = i.id_nfe

    WHERE n.tipo = 'Venda'

      AND i.cfop IN (
          '5101',
          '5102',
          '5401',
          '5405',
          '6101',
          '6102',
          '6107',
          '6108',
          '6401',
          '6404'
      )

      AND date(n.data_emissao)
          BETWEEN date(?) AND date(?)

    GROUP BY
        i.codigo_produto,
        i.descricao

    ORDER BY
        quantidade_vendida DESC

    LIMIT ?
    """;

        LocalDate dataFim = LocalDate.now();
        LocalDate dataInicio = dataFim.minusMonths(12);

        List<TopProdutosDTO> lista = new ArrayList<>();

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setString(1, dataInicio.toString());
            ps.setString(2, dataFim.toString());
            ps.setInt(3, limite);

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

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao consultar os produtos mais vendidos.",
                    e
            );
        }

        return lista;
    }
}