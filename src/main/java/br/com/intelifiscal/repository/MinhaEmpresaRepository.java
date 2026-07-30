package br.com.intelifiscal.repository;

import br.com.intelifiscal.database.connection.DatabaseConnection;
import br.com.intelifiscal.entity.MinhaEmpresa;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Responsável pelo acesso aos dados da tabela tblMinhaEmpresa.
 */
public class MinhaEmpresaRepository {

    public boolean existeEmpresa() {

        final String sql = """
                SELECT COUNT(*)
                FROM tblMinhaEmpresa
                """;

        try (var connection = DatabaseConnection.getConnection();
             var statement = connection.prepareStatement(sql);
             var resultSet = statement.executeQuery()) {

            boolean existe = resultSet.next() && resultSet.getInt(1) > 0;

            System.out.println(">>> Existe empresa: " + existe);

            return existe;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao verificar existência da empresa.",
                    e
            );

        }

    }

    public Optional<MinhaEmpresa> buscar() {

        final String sql = """
            SELECT *
              FROM tblMinhaEmpresa
             ORDER BY id
             LIMIT 1
            """;

        try (var connection = br.com.intelifiscal.database.connection.DatabaseConnection.getConnection();
             var statement = connection.prepareStatement(sql);
             var resultSet = statement.executeQuery()) {

            if (!resultSet.next()) {
                return Optional.empty();
            }

            MinhaEmpresa empresa = new MinhaEmpresa();

            empresa.setId(resultSet.getLong("id"));
            empresa.setCnpj(resultSet.getString("cnpj"));
            empresa.setRazaoSocial(resultSet.getString("razao_social"));
            empresa.setNomeFantasia(resultSet.getString("nome_fantasia"));
            empresa.setInscricaoEstadual(resultSet.getString("inscricao_estadual"));
            empresa.setCep(resultSet.getString("cep"));
            empresa.setUf(resultSet.getString("uf"));
            empresa.setCidade(resultSet.getString("cidade"));
            empresa.setBairro(resultSet.getString("bairro"));
            empresa.setEndereco(resultSet.getString("endereco"));
            empresa.setNumero(resultSet.getString("numero"));
            empresa.setTelefone(resultSet.getString("telefone"));
            empresa.setEmail(resultSet.getString("email"));
            empresa.setAtivo(resultSet.getInt("ativo") == 1);

            empresa.setDataCadastro(
                    java.time.LocalDateTime.parse(
                            resultSet.getString("data_cadastro")
                    )
            );

            empresa.setDataAtualizacao(
                    java.time.LocalDateTime.parse(
                            resultSet.getString("data_atualizacao")
                    )
            );

            return Optional.of(empresa);

        } catch (java.sql.SQLException e) {

            throw new RuntimeException(
                    "Erro ao buscar empresa.",
                    e
            );

        }

    }

    public void salvar(MinhaEmpresa empresa) {

        System.out.println("====================================");
        System.out.println(">>> Repository.salvar()");
        System.out.println("CNPJ..........: " + empresa.getCnpj());
        System.out.println("Razão Social..: " + empresa.getRazaoSocial());

        final String sql = """
                INSERT INTO tblMinhaEmpresa (
                    cnpj,
                    razao_social,
                    nome_fantasia,
                    inscricao_estadual,
                    cep,
                    uf,
                    cidade,
                    bairro,
                    endereco,
                    numero,
                    telefone,
                    email,
                    ativo,
                    data_cadastro,
                    data_atualizacao
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (var connection = DatabaseConnection.getConnection();
             var statement = connection.prepareStatement(sql)) {

            statement.setString(1, empresa.getCnpj());
            statement.setString(2, empresa.getRazaoSocial());
            statement.setString(3, empresa.getNomeFantasia());
            statement.setString(4, empresa.getInscricaoEstadual());
            statement.setString(5, empresa.getCep());
            statement.setString(6, empresa.getUf());
            statement.setString(7, empresa.getCidade());
            statement.setString(8, empresa.getBairro());
            statement.setString(9, empresa.getEndereco());
            statement.setString(10, empresa.getNumero());
            statement.setString(11, empresa.getTelefone());
            statement.setString(12, empresa.getEmail());
            statement.setInt(13, empresa.isAtivo() ? 1 : 0);
            statement.setString(14, empresa.getDataCadastro().toString());
            statement.setString(15, empresa.getDataAtualizacao().toString());

            int linhas = statement.executeUpdate();

            System.out.println(">>> INSERT executado.");
            System.out.println(">>> Linhas afetadas: " + linhas);
            System.out.println("====================================");

        } catch (SQLException e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Erro ao salvar a empresa.",
                    e
            );

        }

    }

    public void atualizar(MinhaEmpresa empresa) {

        System.out.println("====================================");
        System.out.println("CNPJ..........: " + empresa.getCnpj());
        System.out.println("Razão Social..: " + empresa.getRazaoSocial());

        final String sql = """
                UPDATE tblMinhaEmpresa
                   SET cnpj = ?,
                       razao_social = ?,
                       nome_fantasia = ?,
                       inscricao_estadual = ?,
                       cep = ?,
                       uf = ?,
                       cidade = ?,
                       bairro = ?,
                       endereco = ?,
                       numero = ?,
                       telefone = ?,
                       email = ?,
                       ativo = ?,
                       data_atualizacao = ?
                 WHERE id = (
                        SELECT MIN(id)
                        FROM tblMinhaEmpresa
                 )
                """;

        try (var connection = DatabaseConnection.getConnection();
             var statement = connection.prepareStatement(sql)) {

            statement.setString(1, empresa.getCnpj());
            statement.setString(2, empresa.getRazaoSocial());
            statement.setString(3, empresa.getNomeFantasia());
            statement.setString(4, empresa.getInscricaoEstadual());
            statement.setString(5, empresa.getCep());
            statement.setString(6, empresa.getUf());
            statement.setString(7, empresa.getCidade());
            statement.setString(8, empresa.getBairro());
            statement.setString(9, empresa.getEndereco());
            statement.setString(10, empresa.getNumero());
            statement.setString(11, empresa.getTelefone());
            statement.setString(12, empresa.getEmail());
            statement.setInt(13, empresa.isAtivo() ? 1 : 0);
            statement.setString(14, empresa.getDataAtualizacao().toString());

            int linhas = statement.executeUpdate();

            System.out.println(">>> UPDATE executado.");
            System.out.println(">>> Linhas afetadas: " + linhas);
            System.out.println("====================================");

        } catch (SQLException e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Erro ao atualizar a empresa.",
                    e
            );

        }

    }

    public void excluir() {

        throw new UnsupportedOperationException(
                "Método ainda não implementado."
        );

    }

}