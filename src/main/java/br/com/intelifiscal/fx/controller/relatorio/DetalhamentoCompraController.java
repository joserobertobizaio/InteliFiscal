package br.com.intelifiscal.fx.controller.relatorio;

import br.com.intelifiscal.dto.relatorio.DetalhamentoCompraDTO;
import br.com.intelifiscal.fx.view.relatorio.DetalhamentoCompraView;
import br.com.intelifiscal.service.relatorio.DetalhamentoCompraService;

import javafx.scene.control.Alert;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class DetalhamentoCompraController {

    private final DetalhamentoCompraView view;
    private final DetalhamentoCompraService service;

    public DetalhamentoCompraController(
            DetalhamentoCompraView view) {

        this.view = view;
        this.service =
                new DetalhamentoCompraService();

        configurarEventos();
    }

    /*
     * ============================================================
     * EVENTOS
     * ============================================================
     */

    private void configurarEventos() {

        view.getBtPesquisar().setOnAction(
                event -> pesquisar()
        );

        view.getBtLimpar().setOnAction(
                event -> limpar()
        );
    }

    /*
     * ============================================================
     * PESQUISAR
     * ============================================================
     */

    private void pesquisar() {

        LocalDate inicio =
                view.getDtInicial().getValue();

        LocalDate fim =
                view.getDtFinal().getValue();

        if (inicio == null) {

            mostrarAviso(
                    "Informe a data inicial."
            );

            return;
        }

        if (fim == null) {

            mostrarAviso(
                    "Informe a data final."
            );

            return;
        }

        try {

            List<DetalhamentoCompraDTO> lista =
                    service.listarPorPeriodo(
                            inicio,
                            fim
                    );

            view.getDados().getItems().setAll(lista);

            atualizarTotal(lista);

        } catch (IllegalArgumentException e) {
            mostrarAviso(
                    e.getMessage()
            );

        } catch (Exception e) {

            mostrarErro(
                    "Erro ao consultar o detalhamento das compras.",
                    e
            );
        }
    }

    /*
     * ============================================================
     * LIMPAR
     * ============================================================
     */

    private void limpar() {

        view.getDtInicial().setValue(null);
        view.getDtFinal().setValue(null);

        view.getDados().getItems().clear();

        view.getLbTotal().setText(
                "Total: R$ 0,00"
        );
    }

    /*
     * ============================================================
     * TOTAL
     * ============================================================
     */

    private void atualizarTotal(
            List<DetalhamentoCompraDTO> lista) {

        BigDecimal total =
                BigDecimal.ZERO;

        for (DetalhamentoCompraDTO dto : lista) {

            if (dto.getValorTotal() != null) {

                total =
                        total.add(
                                dto.getValorTotal()
                        );
            }
        }

        view.getLbTotal().setText(
                "Total: R$ " +
                        String.format(
                                java.util.Locale.forLanguageTag("pt-BR"),
                                "%,.2f",
                                total
                        )
        );
    }

    /*
     * ============================================================
     * AVISO
     * ============================================================
     */

    private void mostrarAviso(String mensagem) {

        Alert alert =
                new Alert(
                        Alert.AlertType.WARNING
                );

        alert.setTitle("Atenção");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);

        alert.showAndWait();
    }

    /*
     * ============================================================
     * ERRO
     * ============================================================
     */

    private void mostrarErro(
            String mensagem,
            Exception e) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(
                mensagem +
                        "\n\n" +
                        e.getMessage()
        );

        alert.showAndWait();
    }

    /*
     * ============================================================
     * GETTERS
     * ============================================================
     */

    public DetalhamentoCompraView getView() {
        return view;
    }
}