package br.com.intelifiscal.repository;

import br.com.intelifiscal.database.connection.DatabaseConnection;
import br.com.intelifiscal.entity.NFe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NFeRepository {

    public void salvar(NFe nfe) {

        String sql = """
        INSERT INTO tblNFe
        (
            chave,
            modelo,
            numero,
            serie,
            tipo,
            data_emissao,
            cnpj_emitente,
            emitente,
            cnpj_destinatario,
            destinatario,
            valor_total,
            situacao,
            data_importacao
        )
        VALUES
        (
            ?,?,?,?,?,?,?,?,?,?,?,?,?
        )
        """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setString(1, nfe.getChave());

            ps.setString(2, nfe.getModelo());

            ps.setString(3, nfe.getNumero());

            ps.setString(4, nfe.getSerie());

            ps.setString(5, nfe.getTipo());


            ps.setString(
                    6,
                    nfe.getDataEmissao() == null
                            ? null
                            : nfe.getDataEmissao().toString()
            );

            ps.setString(7, nfe.getCnpjEmitente());

            ps.setString(8, nfe.getEmitente());

            ps.setString(9, nfe.getCnpjDestinatario());

            ps.setString(10, nfe.getDestinatario());

            ps.setBigDecimal(11, nfe.getValorTotal());

            ps.setString(12, nfe.getSituacao());

            ps.setString(
                    13,
                    nfe.getDataImportacao() == null
                            ? null
                            : nfe.getDataImportacao().toString()
            );

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao salvar NF-e.",
                    e
            );

        }

    }

    public boolean existePorChave(String chave) {

        String sql = """
            SELECT 1
            FROM tblNFe
            WHERE chave = ?
            LIMIT 1
            """;

        try (

                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        connection.prepareStatement(sql)

        ) {

            ps.setString(1, chave);

            return ps.executeQuery().next();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao verificar chave da NF-e.",
                    e
            );

        }

    }

}