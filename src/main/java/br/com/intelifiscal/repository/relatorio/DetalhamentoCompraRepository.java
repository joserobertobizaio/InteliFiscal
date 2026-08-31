package br.com.intelifiscal.repository.relatorio;

import br.com.intelifiscal.database.connection.DatabaseConnection;
import br.com.intelifiscal.dto.relatorio.DetalhamentoCompraDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DetalhamentoCompraRepository {

    /**
     * Lista o detalhamento das compras dentro de um período.
     *
     * Uma linha representa um item de uma nota fiscal.
     */
    public List<DetalhamentoCompraDTO> listar(
            LocalDate dataInicial,
            LocalDate dataFinal) {

        String sql = """
                SELECT
                    n.cnpj_emitente,
                    n.emitente,
                    n.numero,
                    n.data_emissao,
                    n.valor_total AS valor_total_nf,

                    i.descricao,
                    i.codigo_produto,
                    i.cfop,
                    i.quantidade,
                    i.valor_unitario,
                    i.valor_total

                FROM tblNFe n

                INNER JOIN tblNFeItem i
                    ON i.id_nfe = n.id

                WHERE n.tipo = 'Compra'

                  AND date(n.data_emissao)
                      BETWEEN date(?) AND date(?)

                ORDER BY
                    date(n.data_emissao) DESC,
                    n.numero DESC,
                    i.numero_item
                """;

        List<DetalhamentoCompraDTO> lista =
                new ArrayList<>();

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    dataInicial.toString()
            );

            ps.setString(
                    2,
                    dataFinal.toString()
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                while (rs.next()) {

                    DetalhamentoCompraDTO dto =
                            new DetalhamentoCompraDTO();

                    dto.setCnpj(
                            rs.getString("cnpj_emitente")
                    );

                    dto.setFornecedor(
                            rs.getString("emitente")
                    );

                    dto.setNumeroNota(
                            rs.getString("numero")
                    );

                    String data =
                            rs.getString("data_emissao");

                    if (data != null &&
                            !data.isBlank()) {

                        dto.setDataCompra(
                                LocalDate.parse(data)
                        );
                    }

                    // Valor total da NOTA FISCAL
                    dto.setValorTotalNF(
                            rs.getBigDecimal("valor_total_nf")
                    );

                    // Dados do item
                    dto.setProduto(
                            rs.getString("descricao")
                    );

                    dto.setCodigoProduto(
                            rs.getString("codigo_produto")
                    );

                    dto.setCfop(
                            rs.getString("cfop")
                    );

                    dto.setQuantidade(
                            rs.getDouble("quantidade")
                    );

                    dto.setValorUnitario(
                            rs.getBigDecimal("valor_unitario")
                    );

                    dto.setValorTotal(
                            rs.getBigDecimal("valor_total")
                    );

                    lista.add(dto);
                }
            }

            return lista;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao listar detalhamento das compras.",
                    e
            );
        }
    }
}