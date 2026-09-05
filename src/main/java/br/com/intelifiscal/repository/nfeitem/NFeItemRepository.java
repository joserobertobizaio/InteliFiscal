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
            n.data_emissao,
            n.emitente,
            i.codigo_produto,
            n.destinatario,
            i.descricao,
            i.unidade,
            i.quantidade,
            i.valor_unitario
            
        FROM tblNFeItem i

        INNER JOIN tblNFe n
            ON n.id = i.id_nfe

        WHERE (
                i.codigo_produto = ?
                OR (
                    ? = '0056'
                    AND i.codigo_produto = '102326'
                )
              )

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

            ps.setString(1, codigoProduto);

            ps.setString(2, codigoProduto);

            ps.setString(3, inicio.toString());

            ps.setString(4, fim.toString());

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

                    // ------------------------------------------------
                    // DESCRIÇÃO DO PRODUTO
                    // ------------------------------------------------

                    dto.setDescricao(
                            rs.getString("descricao")
                    );

                    dto.setUnidade(
                            rs.getString("unidade")
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

    public ProdutoHistoricoDTO buscarProdutoPorCodigoETipo(
            String codigoProduto,
            String tipo) {

        String sql = """
        SELECT
            i.codigo_produto,
            i.descricao,
            i.unidade,
            i.quantidade,
            i.valor_unitario,
            i.valor_total

        FROM tblNFeItem i

        INNER JOIN tblNFe n
            ON n.id = i.id_nfe

        WHERE i.codigo_produto = ?
          AND n.tipo = ?

        ORDER BY n.data_emissao DESC

        LIMIT 1
        """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, codigoProduto);
            ps.setString(2, tipo);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    ProdutoHistoricoDTO dto =
                            new ProdutoHistoricoDTO();

                    dto.setCodigoProduto(
                            rs.getString("codigo_produto")
                    );

                    dto.setDescricao(
                            rs.getString("descricao")
                    );

                    dto.setUnidade(
                            rs.getString("unidade")
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

                    return dto;
                }

            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao buscar produto para comparação.",
                    e
            );
        }

        return null;
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
            n.data_emissao,
            n.emitente,
            n.destinatario,
            i.descricao,
            i.unidade,
            i.quantidade,
            i.valor_unitario,
            i.valor_total

        FROM tblNFeItem i

        INNER JOIN tblNFe n
            ON n.id = i.id_nfe

        WHERE (
                i.codigo_produto = ?
                OR (
                    ? = '0056'
                    AND i.codigo_produto = '102326'
                )
              )

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

            ps.setString(1, codigoProduto);

            ps.setString(2, codigoProduto);

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

                    // ------------------------------------------------
                    // DESCRIÇÃO DO PRODUTO
                    // ------------------------------------------------

                    dto.setDescricao(
                            rs.getString("descricao")
                    );

                    dto.setUnidade(
                            rs.getString("unidade")
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

    // ============================================================
    // PESQUISA POR CÓDIGO OU DESCRIÇÃO — COM PERÍODO
    // ============================================================

    public List<ProdutoHistoricoDTO> pesquisarHistorico(
            String pesquisa,
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
            i.codigo_produto,
            i.descricao,
            i.unidade,
            i.quantidade,
            i.valor_unitario

        FROM tblNFeItem i

        INNER JOIN tblNFe n
            ON n.id = i.id_nfe

        WHERE (
                i.codigo_produto LIKE ?
                OR i.descricao LIKE ?
              )

          AND date(n.data_emissao)
              BETWEEN date(?) AND date(?)

        ORDER BY n.data_emissao DESC
        """;

        List<ProdutoHistoricoDTO> lista =
                new ArrayList<>();

        String filtro =
                "%" + pesquisa.trim() + "%";

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setString(1, filtro);
            ps.setString(2, filtro);
            ps.setString(3, inicio.toString());
            ps.setString(4, fim.toString());

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

                    dto.setCodigoProduto(
                            rs.getString("codigo_produto")
                    );

                    dto.setDescricao(
                            rs.getString("descricao")
                    );

                    dto.setEmitente(
                            rs.getString("emitente")
                    );

                    dto.setDestinatario(
                            rs.getString("destinatario")
                    );

                    dto.setUnidade(
                            rs.getString("unidade")
                    );

                    dto.setQuantidade(
                            rs.getDouble("quantidade")
                    );

                    dto.setValorUnitario(
                            rs.getDouble("valor_unitario")
                    );

                    lista.add(dto);
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao pesquisar histórico do produto.",
                    e
            );
        }

        return lista;
    }

    // ============================================================
// PESQUISA POR CÓDIGO OU DESCRIÇÃO — SEM PERÍODO
// ============================================================

    public List<ProdutoHistoricoDTO> pesquisarHistorico(
            String pesquisa) {

        String sql = """
        SELECT
            n.tipo,
            n.numero,
            n.serie,
            n.data_emissao,
            n.emitente,
            n.destinatario,
            i.codigo_produto,
            i.descricao,
            i.unidade,
            i.quantidade,
            i.valor_unitario,
            i.valor_total

        FROM tblNFeItem i

        INNER JOIN tblNFe n
            ON n.id = i.id_nfe

        WHERE (
                i.codigo_produto LIKE ?
                OR i.descricao LIKE ?
              )

        ORDER BY n.data_emissao DESC
        """;

        List<ProdutoHistoricoDTO> lista =
                new ArrayList<>();

        String filtro =
                "%" + pesquisa.trim() + "%";

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setString(1, filtro);
            ps.setString(2, filtro);

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

                    dto.setCodigoProduto(
                            rs.getString("codigo_produto")
                    );

                    dto.setDescricao(
                            rs.getString("descricao")
                    );

                    dto.setEmitente(
                            rs.getString("emitente")
                    );

                    dto.setDestinatario(
                            rs.getString("destinatario")
                    );

                    dto.setUnidade(
                            rs.getString("unidade")
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
                    "Erro ao pesquisar histórico do produto.",
                    e
            );
        }

        return lista;
    }

    // ============================================================
// HISTÓRICO POR PESQUISA
// Pesquisa por código ou descrição
// ============================================================

    public List<ProdutoHistoricoDTO> listarHistoricoPorPesquisa(
            String pesquisa) {

        String sql = """
        SELECT
            n.tipo,
            n.numero,
            n.serie,
            n.data_emissao,
            n.emitente,
            n.destinatario,
            i.codigo_produto,
            i.descricao,
            i.unidade,
            i.quantidade,
            i.valor_unitario,
            i.valor_total

        FROM tblNFeItem i

        INNER JOIN tblNFe n
            ON n.id = i.id_nfe

        WHERE (
                i.codigo_produto LIKE ?
                OR i.descricao LIKE ?
              )

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

            String filtro =
                    "%" + pesquisa + "%";

            ps.setString(1, filtro);
            ps.setString(2, filtro);

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

                    dto.setCodigoProduto(
                            rs.getString("codigo_produto")
                    );

                    dto.setDescricao(
                            rs.getString("descricao")
                    );

                    dto.setUnidade(
                            rs.getString("unidade")
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
                    "Erro ao consultar histórico por pesquisa.",
                    e
            );
        }

        return lista;
    }


// ============================================================
// HISTÓRICO POR PESQUISA + PERÍODO
// Pesquisa por código ou descrição
// ============================================================

    public List<ProdutoHistoricoDTO> listarHistoricoPorPesquisa(
            String pesquisa,
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
            i.codigo_produto,
            i.descricao,
            i.unidade,
            i.quantidade,
            i.valor_unitario,
            i.valor_total

        FROM tblNFeItem i

        INNER JOIN tblNFe n
            ON n.id = i.id_nfe

        WHERE (
                i.codigo_produto LIKE ?
                OR i.descricao LIKE ?
              )

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

            String filtro =
                    "%" + pesquisa + "%";

            ps.setString(1, filtro);

            ps.setString(2, filtro);

            ps.setString(3, inicio.toString());

            ps.setString(4, fim.toString());

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

                    dto.setCodigoProduto(
                            rs.getString("codigo_produto")
                    );

                    dto.setDescricao(
                            rs.getString("descricao")
                    );

                    dto.setUnidade(
                            rs.getString("unidade")
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
                    "Erro ao consultar histórico por pesquisa e período.",
                    e
            );
        }

        return lista;
    }

    // ============================================================
    // LISTA TODOS OS ITENS DE UMA NF-e
    // Usado para geração do PDF completo da NF-e
    // ============================================================

    public List<NFeItemDTO> listarPorIdNfe(Integer idNfe) {

        String sql = """
        SELECT
            id,
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

        FROM tblNFeItem

        WHERE id_nfe = ?

        ORDER BY numero_item
        """;

        List<NFeItemDTO> lista = new ArrayList<>();

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idNfe);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    NFeItemDTO item = new NFeItemDTO();

                    item.setId(
                            rs.getInt("id")
                    );

                    item.setIdNfe(
                            rs.getInt("id_nfe")
                    );

                    item.setNumeroItem(
                            rs.getInt("numero_item")
                    );

                    item.setCodigoProduto(
                            rs.getString("codigo_produto")
                    );

                    item.setCodigoBarras(
                            rs.getString("codigo_barras")
                    );

                    item.setDescricao(
                            rs.getString("descricao")
                    );

                    item.setNcm(
                            rs.getString("ncm")
                    );

                    item.setCest(
                            rs.getString("cest")
                    );

                    item.setCfop(
                            rs.getString("cfop")
                    );

                    item.setUnidade(
                            rs.getString("unidade")
                    );

                    item.setQuantidade(
                            rs.getDouble("quantidade")
                    );

                    item.setValorUnitario(
                            rs.getDouble("valor_unitario")
                    );

                    item.setValorTotal(
                            rs.getDouble("valor_total")
                    );

                    item.setDesconto(
                            rs.getDouble("desconto")
                    );

                    item.setFrete(
                            rs.getDouble("frete")
                    );

                    item.setSeguro(
                            rs.getDouble("seguro")
                    );

                    item.setOutrasDespesas(
                            rs.getDouble("outras_despesas")
                    );

                    item.setValorIcms(
                            rs.getDouble("valor_icms")
                    );

                    item.setValorIpi(
                            rs.getDouble("valor_ipi")
                    );

                    item.setValorPis(
                            rs.getDouble("valor_pis")
                    );

                    item.setValorCofins(
                            rs.getDouble("valor_cofins")
                    );

                    String dataImportacao =
                            rs.getString("data_importacao");

                    if (dataImportacao != null
                            && !dataImportacao.isBlank()) {

                        item.setDataImportacao(
                                LocalDateTime.parse(dataImportacao)
                        );
                    }

                    lista.add(item);
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao listar itens da NF-e.",
                    e
            );
        }

        return lista;
    }

}