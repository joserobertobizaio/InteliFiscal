package br.com.intelifiscal.repository.nfeitem;

import br.com.intelifiscal.database.connection.DatabaseConnection;
import br.com.intelifiscal.dto.nfeitem.NFeItemDTO;
import br.com.intelifiscal.dto.produto.ProdutoHistoricoDTO;
import java.time.LocalDate;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public List<ProdutoHistoricoDTO> listarHistoricoPorCodigoProduto(
            String codigoProduto,
            LocalDate inicio,
            LocalDate fim) {

        String sql = """
        SELECT
            n.tipo,
            n.numero,
            n.serie,
            n.data_emissao,
            n.emitente,
            n.destinatario,
            i.quantidade,
            i.valor_unitario,
            i.valor_total

        FROM tblNFeItem i

        INNER JOIN tblNFe n
            ON n.id = i.id_nfe

        WHERE i.codigo_produto = ?

          AND date(n.data_emissao)
              BETWEEN date(?) AND date(?)

        ORDER BY n.data_emissao DESC
        """;

        List<ProdutoHistoricoDTO> lista =
                new ArrayList<>();

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    codigoProduto
            );

            ps.setString(
                    2,
                    inicio.toString()
            );

            ps.setString(
                    3,
                    fim.toString()
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                while (rs.next()) {

                    ProdutoHistoricoDTO dto =
                            new ProdutoHistoricoDTO();

                    dto.setTipo(
                            rs.getString("tipo")
                    );

                    dto.setNumeroNfe(
                            rs.getString("numero")
                    );

                    dto.setSerie(
                            rs.getString("serie")
                    );

                    String data =
                            rs.getString("data_emissao");

                    if (data != null
                            && !data.isBlank()) {

                        if (data.length() == 10) {

                            dto.setDataEmissao(
                                    LocalDate.parse(data)
                                            .atStartOfDay()
                            );

                        } else {

                            dto.setDataEmissao(
                                    LocalDateTime.parse(data)
                            );
                        }
                    }

                    dto.setEmitente(
                            rs.getString("emitente")
                    );

                    dto.setDestinatario(
                            rs.getString("destinatario")
                    );

                    dto.setQuantidade(
                            rs.getDouble("quantidade")
                    );

                    dto.setValorUnitario(
                            rs.getDouble("valor_unitario")
                    );

                    dto.setValorTotal(
                            rs.getDouble("valor_total")
                    );

                    lista.add(dto);
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao consultar histórico do produto.",
                    e
            );
        }

        return lista;
    }

    private double valor(Double valor) {

        return valor == null ? 0.0 : valor;

    }

    public List<ProdutoHistoricoDTO> listarHistoricoPorCodigoProduto(
            String codigoProduto) {

        String sql = """
            SELECT
                n.tipo,
                n.numero,
                n.serie,
                n.data_emissao,
                n.emitente,
                n.destinatario,
                i.quantidade,
                i.valor_unitario,
                i.valor_total

            FROM tblNFeItem i

            INNER JOIN tblNFe n
                ON n.id = i.id_nfe

            WHERE i.codigo_produto = ?

            ORDER BY n.data_emissao DESC
            """;

        List<ProdutoHistoricoDTO> lista = new ArrayList<>();

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, codigoProduto);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    ProdutoHistoricoDTO dto =
                            new ProdutoHistoricoDTO();

                    dto.setTipo(
                            rs.getString("tipo")
                    );

                    dto.setNumeroNfe(
                            rs.getString("numero")
                    );

                    dto.setSerie(
                            rs.getString("serie")
                    );

                    String data =
                            rs.getString("data_emissao");

                    if (data != null && !data.isBlank()) {

                        if (data.length() == 10) {

                            dto.setDataEmissao(
                                    LocalDate.parse(data).atStartOfDay()
                            );

                        } else {

                            dto.setDataEmissao(
                                    LocalDateTime.parse(data)
                            );
                        }
                    }

                    dto.setEmitente(
                            rs.getString("emitente")
                    );

                    dto.setDestinatario(
                            rs.getString("destinatario")
                    );

                    dto.setQuantidade(
                            rs.getDouble("quantidade")
                    );

                    dto.setValorUnitario(
                            rs.getDouble("valor_unitario")
                    );

                    dto.setValorTotal(
                            rs.getDouble("valor_total")
                    );

                    lista.add(dto);
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao consultar histórico do produto.",
                    e
            );
        }

        return lista;
    }

}