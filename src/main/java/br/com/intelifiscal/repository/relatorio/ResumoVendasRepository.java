package br.com.intelifiscal.repository.relatorio;

import br.com.intelifiscal.database.connection.DatabaseConnection;
import br.com.intelifiscal.dto.relatorio.ResumoVendasDTO;
import br.com.intelifiscal.dto.relatorio.ClienteVendaDTO;
import br.com.intelifiscal.dto.venda.ResumoVendaDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ResumoVendasRepository {

    //==================================================
    // RESUMO GERAL DE VENDAS
    //==================================================

    public ResumoVendasDTO consultarResumo() {

        return consultarResumo(null, null);
    }


    //==================================================
    // RESUMO POR PERÍODO
    //==================================================

    public ResumoVendasDTO consultarResumo(
            LocalDate dataInicio,
            LocalDate dataFim
    ) {

        StringBuilder sql = new StringBuilder("""
            SELECT
                COUNT(DISTINCT n.id) AS notas,

                COUNT(i.id) AS itens,

                ROUND(
                    COALESCE(SUM(i.quantidade), 0),
                    3
                ) AS quantidade,

                ROUND(
                    COALESCE(SUM(i.valor_total), 0),
                    2
                ) AS valor_total,

                ROUND(
                    COALESCE(SUM(i.valor_total), 0)
                    /
                    NULLIF(COUNT(DISTINCT n.id), 0),
                    2
                ) AS ticket_medio

            FROM tblNFe n

            CROSS JOIN tblMinhaEmpresa e

            LEFT JOIN tblNFeItem i
                ON i.id_nfe = n.id

            WHERE n.cnpj_emitente = e.cnpj

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
            """);


        //==================================================
        // FILTRO DE DATA
        //==================================================

        if (dataInicio != null && dataFim != null) {

            sql.append("""
                
                AND date(n.data_emissao)
                    BETWEEN date(?) AND date(?)
                """);
        }


        //==================================================
        // EXECUÇÃO
        //==================================================

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql.toString())
        ) {

            //==================================================
            // PARÂMETROS
            //==================================================

            if (dataInicio != null && dataFim != null) {

                ps.setString(
                        1,
                        dataInicio.toString()
                );

                ps.setString(
                        2,
                        dataFim.toString()
                );
            }


            //==================================================
            // RESULTADO
            //==================================================

            try (ResultSet rs = ps.executeQuery()) {

                ResumoVendasDTO dto =
                        new ResumoVendasDTO();

                if (rs.next()) {

                    dto.setNotas(
                            rs.getInt("notas")
                    );

                    dto.setItens(
                            rs.getInt("itens")
                    );


                    dto.setQuantidade(
                            rs.getDouble("quantidade")
                    );

                    dto.setValorTotal(
                            rs.getBigDecimal("valor_total")
                    );

                    dto.setTicketMedio(
                            rs.getBigDecimal("ticket_medio")
                    );
                }

                return dto;
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao consultar resumo de vendas.",
                    e
            );
        }
    }

    //==================================================
    // VENDAS POR CLIENTE
    //==================================================

    public List<ClienteVendaDTO> consultarPorCliente() {

        return consultarPorCliente(null, null);
    }


    //==================================================
    // VENDAS POR CLIENTE - COM PERÍODO
    //==================================================

    public List<ClienteVendaDTO> consultarPorCliente(
            LocalDate dataInicio,
            LocalDate dataFim
    ) {

        StringBuilder sql = new StringBuilder("""
        SELECT
            n.cnpj_destinatario AS cnpj,

            MAX(n.destinatario) AS cliente,

            COUNT(DISTINCT n.id) AS notas,
        
            COUNT(i.id) AS itens,
       
            MAX(date(n.data_emissao)) AS data_ultima_venda,
       
            MAX(date(n.data_emissao)) AS data_ultima_venda,
      
            ROUND(
      
                COALESCE(SUM(i.quantidade), 0),
                3
            ) AS quantidade,

            ROUND(
                COALESCE(SUM(i.valor_total), 0),
                2
            ) AS valor_total

        FROM tblNFe n

        CROSS JOIN tblMinhaEmpresa e

        LEFT JOIN tblNFeItem i
            ON i.id_nfe = n.id

        WHERE n.cnpj_emitente = e.cnpj

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
        """);


        //==================================================
        // FILTRO DE PERÍODO
        //==================================================

        if (dataInicio != null && dataFim != null) {

            sql.append("""
            
            AND date(n.data_emissao)
                BETWEEN date(?) AND date(?)
            """);
        }


        //==================================================
        // AGRUPAMENTO
        //==================================================

        sql.append("""
        
        GROUP BY n.cnpj_destinatario

        ORDER BY valor_total DESC
        """);


        List<ClienteVendaDTO> lista =
                new ArrayList<>();


        //==================================================
        // EXECUÇÃO
        //==================================================

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql.toString())
        ) {

            if (dataInicio != null && dataFim != null) {

                ps.setString(
                        1,
                        dataInicio.toString()
                );

                ps.setString(
                        2,
                        dataFim.toString()
                );
            }


            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    ClienteVendaDTO dto =
                            new ClienteVendaDTO();


                    dto.setCliente(
                            rs.getString("cliente")
                    );


                    dto.setCnpj(
                            rs.getString("cnpj")
                    );


                    dto.setNotas(
                            rs.getInt("notas")
                    );


                    dto.setItens(
                            rs.getInt("itens")
                    );

                    String dataUltimaVenda =
                            rs.getString("data_ultima_venda");

                    if (dataUltimaVenda != null
                            && !dataUltimaVenda.isBlank()) {

                        dto.setDataUltimaVenda(
                                LocalDate.parse(dataUltimaVenda)
                        );
                    }


                    dto.setQuantidade(
                            rs.getDouble("quantidade")
                    );


                    dto.setValorTotal(
                            rs.getBigDecimal("valor_total")
                    );


                    lista.add(dto);
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao consultar vendas por cliente.",
                    e
            );
        }

        return lista;
    }

    //==================================================
    // VENDAS PARA EXPORTAÇÃO
    //==================================================

    public List<ResumoVendaDTO> consultarVendasParaExportacao(
            LocalDate dataInicio,
            LocalDate dataFim
    ) {

        StringBuilder sql = new StringBuilder("""
        SELECT
            n.numero AS nf,
            n.data_emissao,
            n.destinatario,
            n.cnpj_destinatario,

            CASE
                WHEN ROW_NUMBER() OVER (
                    PARTITION BY n.id
                    ORDER BY i.numero_item
                ) = 1
                THEN n.valor_total
                ELSE NULL
            END AS valor_total_nf,

            i.codigo_produto AS codigo_item,
            i.descricao AS descricao_item,
            i.cfop,
            i.quantidade,
            i.valor_unitario

        FROM tblNFe n

        CROSS JOIN tblMinhaEmpresa e

        INNER JOIN tblNFeItem i
            ON i.id_nfe = n.id

        WHERE n.cnpj_emitente = e.cnpj

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
        """);


        //==================================================
        // FILTRO DE DATA
        //==================================================

        if (dataInicio != null && dataFim != null) {

            sql.append("""
            
            AND date(n.data_emissao)
                BETWEEN date(?) AND date(?)
            """);
        }


        //==================================================
        // ORDENAÇÃO
        //==================================================

        sql.append("""
        
        ORDER BY
            date(n.data_emissao),
            CAST(n.numero AS INTEGER),
            i.numero_item
        """);


        List<ResumoVendaDTO> lista =
                new ArrayList<>();


        //==================================================
        // EXECUÇÃO
        //==================================================

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(
                                sql.toString()
                        )
        ) {

            //==================================================
            // PARÂMETROS
            //==================================================

            if (dataInicio != null && dataFim != null) {

                ps.setString(
                        1,
                        dataInicio.toString()
                );

                ps.setString(
                        2,
                        dataFim.toString()
                );
            }


            //==================================================
            // RESULTADO
            //==================================================

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                while (rs.next()) {

                    ResumoVendaDTO dto =
                            new ResumoVendaDTO();


                    dto.setNf(
                            rs.getString("nf")
                    );


                    dto.setDataEmissao(
                            rs.getString("data_emissao")
                    );


                    dto.setDestinatario(
                            rs.getString("destinatario")
                    );


                    dto.setCnpjDestinatario(
                            rs.getString("cnpj_destinatario")
                    );


                    dto.setValorTotalNF(
                            rs.getBigDecimal(
                                    "valor_total_nf"
                            )
                    );


                    dto.setCodigoItem(
                            rs.getString("codigo_item")
                    );


                    dto.setDescricaoItem(
                            rs.getString("descricao_item")
                    );


                    dto.setCfop(
                            rs.getString("cfop")
                    );


                    dto.setQuantidade(
                            rs.getBigDecimal(
                                    "quantidade"
                            )
                    );


                    dto.setValorUnitario(
                            rs.getBigDecimal(
                                    "valor_unitario"
                            )
                    );


                    lista.add(dto);
                }
            }


        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao consultar vendas para exportação.",
                    e
            );
        }


        return lista;
    }

}