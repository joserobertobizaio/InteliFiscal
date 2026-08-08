package br.com.intelifiscal.repository.nfeitem;

import br.com.intelifiscal.database.connection.DatabaseConnection;
import br.com.intelifiscal.dto.nfeitem.NFeItemDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NFeItemRepository {

    public void salvar(NFeItemDTO item) {

        String sql = """
            INSERT INTO tblNFeItem (
                id_nfe,
                numero_item,
                codigo_produto,
                codigo_barras,
                descricao,
                ncm,
                cest,
                cfop,
                unidade,
                quantidade,
                valor_unitario,
                valor_total,
                desconto,
                frete,
                seguro,
                outras_despesas,
                valor_icms,
                valor_ipi,
                valor_pis,
                valor_cofins,
                data_importacao
            )
            VALUES (
                ?,?,?,?,?,?,?,?,?,?,
                ?,?,?,?,?,?,?,?,?,?,
                ?
            )
            """;

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, item.getIdNfe());

            ps.setInt(2, item.getNumeroItem());

            ps.setString(3, item.getCodigoProduto());

            ps.setString(4, item.getCodigoBarras());

            ps.setString(5, item.getDescricao());

            ps.setString(6, item.getNcm());

            ps.setString(7, item.getCest());

            ps.setString(8, item.getCfop());

            ps.setString(9, item.getUnidade());

            ps.setDouble(10, valor(item.getQuantidade()));

            ps.setDouble(11, valor(item.getValorUnitario()));

            ps.setDouble(12, valor(item.getValorTotal()));

            ps.setDouble(13, valor(item.getDesconto()));

            ps.setDouble(14, valor(item.getFrete()));

            ps.setDouble(15, valor(item.getSeguro()));

            ps.setDouble(16, valor(item.getOutrasDespesas()));

            ps.setDouble(17, valor(item.getValorIcms()));

            ps.setDouble(18, valor(item.getValorIpi()));

            ps.setDouble(19, valor(item.getValorPis()));

            ps.setDouble(20, valor(item.getValorCofins()));

            ps.setString(
                    21,
                    item.getDataImportacao() == null
                            ? null
                            : item.getDataImportacao().toString()
            );

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao salvar item da NF-e.",
                    e
            );

        }
    }

    private double valor(Double valor) {

        return valor == null ? 0.0 : valor;

    }

}