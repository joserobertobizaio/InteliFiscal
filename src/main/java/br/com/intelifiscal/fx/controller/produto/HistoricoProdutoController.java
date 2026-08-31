package br.com.intelifiscal.fx.controller.produto;

import br.com.intelifiscal.dto.produto.ProdutoHistoricoDTO;
import br.com.intelifiscal.fx.navigation.NavigationManager;
import br.com.intelifiscal.repository.nfeitem.NFeItemRepository;
import br.com.intelifiscal.fx.view.produto.HistoricoProdutoView;

import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.util.List;

public class HistoricoProdutoController {

    private final HistoricoProdutoView view;

    private final NFeItemRepository repository =
            new NFeItemRepository();


    // ============================================================
    // CONSTRUTOR
    // ============================================================

    public HistoricoProdutoController(
            HistoricoProdutoView view) {

        this.view = view;

        configurarEventos();
    }


    // ============================================================
    // EVENTOS
    // ============================================================

    private void configurarEventos() {

        // --------------------------------------------------------
        // PESQUISAR
        // --------------------------------------------------------

        view.getBtPesquisar()
                .setOnAction(
                        event -> pesquisar()
                );


        // --------------------------------------------------------
        // LIMPAR
        // --------------------------------------------------------

        view.getBtLimpar()
                .setOnAction(
                        event -> limpar()
                );


        // --------------------------------------------------------
        // FECHAR
        // --------------------------------------------------------

        view.getBtFechar()
                .setOnAction(
                        event -> fechar()
                );


        // --------------------------------------------------------
        // ENTER NA PESQUISA
        // --------------------------------------------------------

        view.getTxtPesquisa()
                .setOnAction(
                        event -> pesquisar()
                );
    }


    // ============================================================
    // PESQUISAR
    // ============================================================

    private void pesquisar() {

        // --------------------------------------------------------
        // PEGA O TEXTO DA CAIXA QUE REALMENTE ESTÁ NA TELA
        // --------------------------------------------------------

        String pesquisa =
                view.getTxtPesquisa()
                        .getText()
                        .trim();


        // --------------------------------------------------------
        // VALIDA PESQUISA
        // --------------------------------------------------------

        if (pesquisa.isBlank()) {

            mostrarAviso(
                    "Informe o código ou a descrição do produto."
            );

            view.getTxtPesquisa()
                    .requestFocus();

            return;
        }


        LocalDate inicio = null;
        LocalDate fim = null;


        // ========================================================
        // PERÍODO
        // ========================================================

        String periodo =
                view.getCbPeriodo()
                        .getValue();


        /*
         * Por enquanto vamos tratar:
         *
         * Últimos 12 meses
         * Desde o início
         *
         * Os demais períodos serão ajustados na próxima etapa.
         */


        if ("Últimos 12 meses".equals(periodo)) {

            fim = LocalDate.now();

            inicio = fim.minusMonths(12);

        } else if ("Últimos 6 meses".equals(periodo)) {

            fim = LocalDate.now();

            inicio = fim.minusMonths(6);

        } else if ("Últimos 3 meses".equals(periodo)) {

            fim = LocalDate.now();

            inicio = fim.minusMonths(3);

        } else if ("Últimos 30 dias".equals(periodo)) {

            fim = LocalDate.now();

            inicio = fim.minusDays(30);

        } else if ("Últimos 24 meses".equals(periodo)) {

            fim = LocalDate.now();

            inicio = fim.minusMonths(24);

        } else {

            // Desde o início / período personalizado
            inicio = null;
            fim = null;
        }


        // ========================================================
        // CONSULTA
        // ========================================================

        try {

            List<ProdutoHistoricoDTO> historico;


            if (inicio == null || fim == null) {

                historico =
                        repository.listarHistoricoPorPesquisa(
                                pesquisa
                        );

            } else {

                historico =
                        repository.listarHistoricoPorPesquisa(
                                pesquisa,
                                inicio,
                                fim
                        );
            }


            // ====================================================
            // PREENCHE TABELA
            // ====================================================

            view.getDados()
                    .setAll(historico);


            // ====================================================
            // DESCRIÇÃO
            // ====================================================

            preencherDescricao(
                    historico
            );


            // ====================================================
            // NENHUM RESULTADO
            // ====================================================

            if (historico.isEmpty()) {

                mostrarAviso(
                        "Nenhum histórico encontrado para o produto informado."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            mostrarErro(
                    "Erro ao consultar o histórico do produto.",
                    e.getMessage()
            );
        }
    }


    // ============================================================
    // PREENCHE DESCRIÇÃO
    // ============================================================

    private void preencherDescricao(
            List<ProdutoHistoricoDTO> historico) {

        if (historico == null
                || historico.isEmpty()) {

            view.getTxtDescricao()
                    .clear();

            return;
        }


        for (ProdutoHistoricoDTO item : historico) {

            if (item.getDescricao() != null
                    && !item.getDescricao().isBlank()) {

                view.getTxtDescricao()
                        .setText(
                                item.getDescricao()
                        );

                return;
            }
        }


        view.getTxtDescricao()
                .clear();
    }


    // ============================================================
    // LIMPAR
    // ============================================================

    private void limpar() {

        // Campo visível da pesquisa
        view.getTxtPesquisa()
                .clear();


        // Campo antigo
        view.getTxtCodigoProduto()
                .clear();


        // Descrição
        view.getTxtDescricao()
                .clear();


        // Datas antigas
        view.getDtInicio()
                .setValue(null);

        view.getDtFim()
                .setValue(null);


        // Tabela
        view.getDados()
                .clear();


        // Limpa seleções
        view.limparSelecoes();


        // Volta para o período padrão
        view.getCbPeriodo()
                .setValue("Últimos 12 meses");


        // Foco
        view.getTxtPesquisa()
                .requestFocus();
    }


    // ============================================================
    // FECHAR
    // ============================================================

    private void fechar() {

        NavigationManager.show(
                br.com.intelifiscal.fx.navigation.ScreenType.PRODUTOS
        );
    }


    // ============================================================
    // ALERTA
    // ============================================================

    private void mostrarAviso(
            String mensagem) {

        Alert alerta =
                new Alert(
                        Alert.AlertType.WARNING
                );

        alerta.setTitle(
                "Histórico do Produto"
        );

        alerta.setHeaderText(null);

        alerta.setContentText(
                mensagem
        );

        alerta.showAndWait();
    }


    // ============================================================
    // ERRO
    // ============================================================

    private void mostrarErro(
            String titulo,
            String mensagem) {

        Alert alerta =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alerta.setTitle(
                "Histórico do Produto"
        );

        alerta.setHeaderText(
                titulo
        );

        alerta.setContentText(
                mensagem == null
                        ? "Erro desconhecido."
                        : mensagem
        );

        alerta.showAndWait();
    }
}