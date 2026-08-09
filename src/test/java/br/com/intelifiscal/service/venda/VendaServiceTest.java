package br.com.intelifiscal.service.venda;

import br.com.intelifiscal.dto.venda.VendaItemDTO;
import br.com.intelifiscal.repository.venda.VendaRepository;

import java.util.List;

public class VendaServiceTest {

    public static void main(String[] args) {

        VendaService service =
                new VendaService();

        Integer idNfe = 644;

        List<VendaItemDTO> itens =
                service.listarItensPorNfe(idNfe);

        System.out.println();
        System.out.println("======================================");
        System.out.println("       TESTE DE ITENS DA VENDA");
        System.out.println("======================================");

        System.out.println(
                "ID da NF: " + idNfe
        );

        System.out.println(
                "Quantidade de itens: "
                        + itens.size()
        );

        for (VendaItemDTO item : itens) {

            System.out.println("--------------------------------------");

            System.out.println(
                    "Item.............: "
                            + item.getNumeroItem()
            );

            System.out.println(
                    "Código produto...: "
                            + item.getCodigoProduto()
            );

            System.out.println(
                    "Código barras....: "
                            + item.getCodigoBarras()
            );

            System.out.println(
                    "Descrição........: "
                            + item.getDescricao()
            );

            System.out.println(
                    "Unidade..........: "
                            + item.getUnidade()
            );

            System.out.println(
                    "Quantidade.......: "
                            + item.getQuantidade()
            );

            System.out.println(
                    "Valor unitário...: "
                            + item.getValorUnitario()
            );

            System.out.println(
                    "Valor total......: "
                            + item.getValorTotal()
            );

            System.out.println(
                    "Desconto.........: "
                            + item.getDesconto()
            );
        }

        System.out.println("--------------------------------------");
        System.out.println("Fim do teste.");
        System.out.println("======================================");
    }
}