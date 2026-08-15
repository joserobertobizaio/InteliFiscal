package br.com.intelifiscal.fx.controller.importacao;

import br.com.intelifiscal.service.xml.XmlNFeItemReader;
import br.com.intelifiscal.service.nfeitem.NFeItemService;
import br.com.intelifiscal.dto.nfeitem.NFeItemDTO;
import br.com.intelifiscal.fx.view.importacao.ImportacaoXMLView;
import br.com.intelifiscal.util.XmlUtil;
import javafx.concurrent.Task;

import br.com.intelifiscal.dto.produto.ProdutoDTO;
import br.com.intelifiscal.service.produto.ProdutoService;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import br.com.intelifiscal.dto.xml.XmlNFeDTO;
import br.com.intelifiscal.service.xml.XmlNFeReader;
import br.com.intelifiscal.service.MinhaEmpresaService;
import javafx.stage.DirectoryChooser;
import java.util.Arrays;
import java.util.Comparator;

import javafx.scene.control.ButtonType;
import java.util.Optional;

import br.com.intelifiscal.fx.navigation.NavigationManager;
import br.com.intelifiscal.fx.navigation.ScreenType;
import javafx.scene.control.Alert;

import br.com.intelifiscal.entity.NFe;
import br.com.intelifiscal.service.NFeService;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.io.File;
import java.util.List;

public class ImportacaoXMLController {

    private final ImportacaoXMLView view;

    private final XmlNFeReader reader = new XmlNFeReader();

    private final XmlNFeItemReader itemReader =
            new XmlNFeItemReader();

    private final MinhaEmpresaService minhaEmpresaService =
            new MinhaEmpresaService();

    private final NFeService nfeService =
            new NFeService();

    private final NFeItemService nfeItemService =
            new NFeItemService();

    private final ProdutoService produtoService =
            new ProdutoService();

    private final List<XmlNFeDTO> xmls =
            new ArrayList<>();

    public ImportacaoXMLController(ImportacaoXMLView view) {

        this.view = view;

        configurarEventos();

        atualizarEstadoBotoes();
    }

    private void configurarEventos() {

        view.getButtonBar()
                .getBtSelecionarXml()
                .setOnAction(e -> selecionarXml());

        view.getButtonBar()
                .getBtRemover()
                .setOnAction(e -> remover());

        view.getButtonBar()
                .getBtImportar()
                .setOnAction(e -> importar());

        view.getButtonBar()
                .getBtSelecionarPasta()
                .setOnAction(e -> selecionarPasta());
    }


    private void atualizarEstadoBotoes() {

        boolean existemXmls = !xmls.isEmpty();

        view.getButtonBar()
                .getBtImportar()
                .setDisable(!existemXmls);

        view.getButtonBar()
                .getBtRemover()
                .setDisable(!existemXmls);
    }

    private void resetarProgressBar() {

        view.getProgressBar()
                .progressProperty()
                .unbind();

        view.getProgressBar()
                .setProgress(0);
    }

    private void selecionarXml() {

        if (minhaEmpresaService.buscarTodas().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);

            alert.setTitle("Importação XML");

            alert.setHeaderText("Nenhum estabelecimento cadastrado.");

            alert.setContentText(
                    "Acesse -> Estabelecimento, e cadastre pelo menos um estabelecimento antes de importar XMLs."
            );

            alert.showAndWait();

            return;
        }


        FileChooser chooser = new FileChooser();

        chooser.setTitle("Selecionar Arquivos XML");

        chooser.getExtensionFilters().add(

                new FileChooser.ExtensionFilter(
                        "Arquivos XML",
                        "*.xml"
                )

        );

        Window window = view.getScene().getWindow();

        List<File> arquivos =
                chooser.showOpenMultipleDialog(window);

        if (arquivos == null || arquivos.isEmpty()) {
            return;
        }

        // Limpa a tabela
        view.getTabela().getItems().clear();

        xmls.clear();

        resetarProgressBar();

        view.getTxtLog().clear();

        // Adiciona os XMLs selecionados

        for (File arquivo : arquivos) {

            try {

                XmlNFeDTO dto = reader.ler(arquivo);

                xmls.add(dto);

                var item = new br.com.intelifiscal.fx.view.importacao.model.ImportacaoXmlItem();

                item.arquivoProperty().set(dto.getArquivo());

                item.numeroNotaProperty().set(dto.getNumero());

                item.serieProperty().set(dto.getSerie());

                item.emitenteProperty().set(dto.getRazaoSocialEmitente());

                item.destinatarioProperty().set(dto.getRazaoSocialDestinatario());

                item.emissaoProperty().set(
                        XmlUtil.formatarData(dto.getDataEmissao())
                );

                item.valorProperty().set(
                        XmlUtil.formatarValor(dto.getValorTotal())
                );

                String tipo;

                if (minhaEmpresaService.ehMinhaEmpresa(dto.getCnpjEmitente())) {

                    tipo = "Venda";

                } else {

                    tipo = "Compra";

                }

                item.tipoProperty().set(tipo);

                item.situacaoProperty().set("Lido");

                view.getTabela().getItems().add(item);

            } catch (Exception ex) {

                view.getTxtLog().appendText(
                        "XML ignorado: "
                                + arquivo.getName()
                                + "\nMotivo: "
                                + ex.getMessage()
                                + "\n\n"
                );

                System.err.println(
                        "Erro ao ler XML: "
                                + arquivo.getName()
                );

                ex.printStackTrace();

            }

        }

        view.getTxtLog().appendText(
                "\nXMLs válidos carregados: "
                        + xmls.size()
                        + "\n"
        );

        atualizarEstadoBotoes();

        System.out.println("Itens na tabela: " + view.getTabela().getItems().size());

    }

    private void selecionarPasta() {

        if (minhaEmpresaService.buscarTodas().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);

            alert.setTitle("Importação XML");

            alert.setHeaderText("Nenhum estabelecimento cadastrado.");

            alert.setContentText(
                    "Acesse -> Estabelecimento, e cadastre o estabelecimento " +
                            "que tenha o mesmo CNPJ das notas de vendas antes de importar os XMLs."
            );

            alert.showAndWait();

            return;
        }

        DirectoryChooser chooser = new DirectoryChooser();

        chooser.setTitle("Selecionar Pasta com XMLs");

        Window window = view.getScene().getWindow();

        File pasta = chooser.showDialog(window);

        if (pasta == null) {
            return;
        }

        File[] arquivos = pasta.listFiles((dir, nome) ->
                nome.toLowerCase().endsWith(".xml"));

        if (arquivos == null || arquivos.length == 0) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Selecionar Pasta");

            alert.setHeaderText(null);

            alert.setContentText(
                    "Nenhum arquivo XML encontrado na pasta selecionada."
            );

            alert.showAndWait();

            return;
        }

        Arrays.sort(arquivos, Comparator.comparing(File::getName));

        view.getTabela().getItems().clear();

        xmls.clear();

        view.getProgressBar().setProgress(0);

        view.getTxtLog().clear();

        for (File arquivo : arquivos) {

            try {

                XmlNFeDTO dto = reader.ler(arquivo);

                xmls.add(dto);

                var item = new br.com.intelifiscal.fx.view.importacao.model.ImportacaoXmlItem();

                item.arquivoProperty().set(dto.getArquivo());

                item.numeroNotaProperty().set(dto.getNumero());

                item.serieProperty().set(dto.getSerie());

                item.emitenteProperty().set(dto.getRazaoSocialEmitente());

                item.destinatarioProperty().set(dto.getRazaoSocialDestinatario());

                item.emissaoProperty().set(
                        XmlUtil.formatarData(dto.getDataEmissao())
                );

                item.valorProperty().set(
                        XmlUtil.formatarValor(dto.getValorTotal())
                );

                String tipo = minhaEmpresaService.ehMinhaEmpresa(dto.getCnpjEmitente())
                        ? "Venda"
                        : "Compra";

                item.tipoProperty().set(tipo);

                item.situacaoProperty().set("Lido");

                view.getTabela().getItems().add(item);

            } catch (Exception ex) {

                view.getTxtLog().appendText(
                        "XML ignorado: "
                                + arquivo.getName()
                                + "\nMotivo: "
                                + ex.getMessage()
                                + "\n\n"
                );
            }
        }

        view.getTxtLog().appendText(
                "Pasta selecionada:\n"
                        + pasta.getAbsolutePath()
                        + "\n\n"
        );

        view.getTxtLog().appendText(
                "XMLs válidos carregados: "
                        + xmls.size()
                        + "\n"
        );

        atualizarEstadoBotoes();

        System.out.println("Itens na tabela: " + view.getTabela().getItems().size());
    }

    private void remover() {

        if (xmls.isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Remover");

            alert.setHeaderText(null);

            alert.setContentText(
                    "Não existem arquivos carregados."
            );

            alert.showAndWait();

            return;

        }

        view.getTabela().getItems().clear();

        xmls.clear();

        view.getTxtLog().clear();

        resetarProgressBar();

        view.getResumo().limpar();

        view.getTxtLog().appendText(
                "Lista de arquivos removida.\n"
        );

        atualizarEstadoBotoes();

    }

    private void importar() {

        if (xmls.isEmpty()) {

            view.getTxtLog().appendText(
                    "Nenhum XML selecionado.\n"
            );

            return;
        }

        // ==================================================
        // DESABILITA OS BOTÕES DURANTE A IMPORTAÇÃO
        // ==================================================

        view.getButtonBar()
                .getBtSelecionarXml()
                .setDisable(true);

        view.getButtonBar()
                .getBtSelecionarPasta()
                .setDisable(true);

        view.getButtonBar()
                .getBtImportar()
                .setDisable(true);

        view.getButtonBar()
                .getBtRemover()
                .setDisable(true);

        view.getProgressBar().setProgress(0);

        view.getTxtLog().appendText(
                "Iniciando importação...\n\n"
        );

        // ==================================================
        // CONTADORES
        // ==================================================

        final int total = xmls.size();

        final int[] importadas = {0};

        final int[] ignoradas = {0};

        // ==================================================
        // TASK DE IMPORTAÇÃO
        // ==================================================

        Task<Void> task = new Task<>() {

            @Override
            protected Void call() {

                int processadas = 0;

                for (XmlNFeDTO dto : xmls) {

                    try {

                        // ==============================
                        // VALIDAÇÃO DO XML
                        // ==============================

                        if (dto == null) {

                            ignoradas[0]++;

                            updateMessage(
                                    "XML ignorado: objeto nulo.\n"
                            );

                            continue;
                        }

                        if (dto.getChave() == null
                                || dto.getChave().isBlank()) {

                            ignoradas[0]++;

                            updateMessage(
                                    "XML ignorado: chave inexistente.\n"
                            );

                            continue;
                        }

                        if (dto.getNumero() == null) {

                            ignoradas[0]++;

                            updateMessage(
                                    "XML ignorado: número inexistente.\n"
                            );

                            continue;
                        }

                        // ==============================
                        // NF JÁ EXISTE
                        // ==============================

                        if (nfeService.existe(dto.getChave())) {

                            ignoradas[0]++;

                            updateMessage(
                                    "NF "
                                            + dto.getNumero()
                                            + " já existe.\n"
                            );

                            continue;
                        }

                        // ==============================
                        // MONTA A NFE
                        // ==============================

                        NFe nfe = new NFe();

                        nfe.setChave(dto.getChave());

                        nfe.setNumero(dto.getNumero());

                        nfe.setSerie(dto.getSerie());

                        nfe.setModelo("55");

                        nfe.setDataEmissao(
                                dto.getDataEmissao()
                        );

                        nfe.setCnpjEmitente(
                                dto.getCnpjEmitente()
                        );

                        nfe.setEmitente(
                                dto.getRazaoSocialEmitente()
                        );

                        nfe.setCnpjDestinatario(
                                dto.getCnpjDestinatario()
                        );

                        nfe.setDestinatario(
                                dto.getRazaoSocialDestinatario()
                        );

                        nfe.setValorTotal(
                                dto.getValorTotal()
                        );

                        // ==============================
                        // COMPRA OU VENDA
                        // ==============================

                        if (minhaEmpresaService
                                .ehMinhaEmpresa(
                                        dto.getCnpjEmitente()
                                )) {

                            nfe.setTipo("Venda");

                        } else {

                            nfe.setTipo("Compra");
                        }

                        nfe.setSituacao("Importado");

                        nfe.setDataImportacao(
                                LocalDateTime.now()
                        );

                        // ==============================
                        // SALVA A NFE
                        // ==============================

                        System.out.println(
                                "-----------------------------------"
                        );

                        System.out.println(
                                "Arquivo : "
                                        + dto.getArquivo()
                        );

                        System.out.println(
                                "Chave   : "
                                        + dto.getChave()
                        );

                        System.out.println(
                                "Número  : "
                                        + dto.getNumero()
                        );

                        Integer idNFe =
                                nfeService.salvar(nfe);

                        // ==============================
                        // LÊ OS ITENS DO XML
                        // ==============================

                        List<NFeItemDTO> itens =
                                itemReader.ler(
                                        dto.getArquivoXml()
                                );

                        // ==============================
                        // SALVA OS ITENS E PRODUTOS
                        // ==============================

                        for (NFeItemDTO item : itens) {

                            item.setIdNfe(idNFe);

                            item.setDataImportacao(
                                    LocalDateTime.now()
                            );

                            // --------------------------
                            // Salva item da NF-e
                            // --------------------------

                            nfeItemService.salvar(item);

                            // --------------------------
                            // Cadastro do produto
                            // --------------------------

                            ProdutoDTO produto =
                                    new ProdutoDTO();

                            produto.setCodigoProduto(
                                    item.getCodigoProduto()
                            );

                            produto.setCodigoBarras(
                                    item.getCodigoBarras()
                            );

                            produto.setDescricao(
                                    item.getDescricao()
                            );

                            produto.setNcm(
                                    item.getNcm()
                            );

                            produto.setCest(
                                    item.getCest()
                            );

                            produto.setUnidade(
                                    item.getUnidade()
                            );

                            produto.setDataCadastro(
                                    LocalDateTime.now()
                            );

                            produto.setAtivo(true);

                            produtoService
                                    .salvarSeNaoExistir(
                                            produto
                                    );
                        }

                        // ==============================
                        // LOG DO CONSOLE
                        // ==============================

                        System.out.println();

                        System.out.println(
                                "===================================="
                        );

                        System.out.println(
                                "NF " + dto.getNumero()
                        );

                        System.out.println(
                                "Quantidade de itens: "
                                        + itens.size()
                        );

                        for (NFeItemDTO item : itens) {

                            System.out.println(
                                    "------------------------------------"
                            );

                            System.out.println(
                                    "Item.............: "
                                            + item.getNumeroItem()
                            );

                            System.out.println(
                                    "Código...........: "
                                            + item.getCodigoProduto()
                            );

                            System.out.println(
                                    "Descrição........: "
                                            + item.getDescricao()
                            );

                            System.out.println(
                                    "NCM..............: "
                                            + item.getNcm()
                            );

                            System.out.println(
                                    "CFOP.............: "
                                            + item.getCfop()
                            );

                            System.out.println(
                                    "Unidade..........: "
                                            + item.getUnidade()
                            );
                        }

                        System.out.println(
                                "===================================="
                        );

                        System.out.println();

                        // ==============================
                        // IMPORTAÇÃO CONCLUÍDA
                        // ==============================

                        importadas[0]++;

                        updateMessage(
                                "NF "
                                        + dto.getNumero()
                                        + " importada.\n"
                        );

                    } catch (Exception ex) {

                        ignoradas[0]++;

                        String arquivo =
                                dto != null
                                        ? dto.getArquivo()
                                        : "(XML desconhecido)";

                        updateMessage(
                                "Erro ao importar XML: "
                                        + arquivo
                                        + "\n"
                                        + ex.getMessage()
                                        + "\n"
                        );

                        ex.printStackTrace();
                    }

                    // ==============================
                    // ATUALIZA PROGRESSO
                    // ==============================

                    processadas++;

                    updateProgress(
                            processadas,
                            total
                    );

                    updateMessage(
                            "Processando XML "
                                    + processadas
                                    + " de "
                                    + total
                                    + "...\n"
                    );
                }

                return null;
            }
        };

        // ==================================================
        // LIGA A BARRA DE PROGRESSO À TASK
        // ==================================================

        view.getProgressBar()
                .progressProperty()
                .unbind();

        view.getProgressBar()
                .progressProperty()
                .bind(task.progressProperty());

        // ==================================================
        // ATUALIZA O LOG NA THREAD DO JAVAFX
        // ==================================================

        task.messageProperty().addListener(
                (obs, antigo, novo) -> {

                    if (novo != null && !novo.isEmpty()) {

                        view.getTxtLog().appendText(
                                novo
                        );
                    }
                }
        );

        // ==================================================
        // QUANDO A IMPORTAÇÃO COMEÇAR
        // ==================================================

        task.setOnRunning(event -> {

            view.getTxtLog().appendText(
                    "Processamento em segundo plano iniciado.\n\n"
            );
        });

        // ==================================================
        // QUANDO TERMINAR COM SUCESSO
        // ==================================================

        task.setOnSucceeded(event -> {

            view.getTxtLog().appendText(
                    "\nImportação concluída.\n"
                            + "Importadas: "
                            + importadas[0]
                            + "\n"
                            + "Ignoradas: "
                            + ignoradas[0]
                            + "\n"
            );

            // ------------------------------
            // Reabilita os botões
            // ------------------------------

            view.getButtonBar()
                    .getBtSelecionarXml()
                    .setDisable(false);

            view.getButtonBar()
                    .getBtSelecionarPasta()
                    .setDisable(false);

            view.getButtonBar()
                    .getBtImportar()
                    .setDisable(false);

            view.getButtonBar()
                    .getBtRemover()
                    .setDisable(false);

            // ==================================================
            // MENSAGEM FINAL DA IMPORTAÇÃO
            // ==================================================

            ButtonType btFechar =
                    new ButtonType("Fechar");

            ButtonType btNovoLote =
                    new ButtonType("Novo Lote");

            ButtonType btDashboard =
                    new ButtonType("Dashboard");

            Alert alert =
                    new Alert(
                            Alert.AlertType.INFORMATION
                    );

            alert.setTitle(
                    "Importação Concluída"
            );

            alert.setHeaderText(
                    "Importação realizada com sucesso!"
            );

            alert.setContentText(
                    "Notas importadas: "
                            + importadas[0]
                            + "\nNotas ignoradas: "
                            + ignoradas[0]
                            + "\n\nO que deseja fazer?"
            );

            alert.getButtonTypes().setAll(
                    btFechar,
                    btNovoLote,
                    btDashboard
            );

            Optional<ButtonType> resultado =
                    alert.showAndWait();

            if (resultado.isPresent()) {

                if (resultado.get() == btDashboard) {

                    NavigationManager.show(
                            ScreenType.DASHBOARD
                    );

                } else if (resultado.get() == btNovoLote) {

                    // ==========================================
                    // PREPARA PARA NOVA IMPORTAÇÃO
                    // ==========================================

                    view.getTabela()
                            .getItems()
                            .clear();

                    view.getTxtLog()
                            .clear();

                    xmls.clear();

                    resetarProgressBar();

                    // Estado inicial da tela
                    view.getButtonBar()
                            .getBtImportar()
                            .setDisable(true);

                    view.getButtonBar()
                            .getBtRemover()
                            .setDisable(true);

                } else if (resultado.get() == btFechar) {

                    resetarProgressBar();

                    // ==========================================
                    // APENAS FECHA A MENSAGEM
                    // ==========================================
                    //
                    // Não limpa tabela.
                    // Não limpa log.
                    // Não zera progresso.
                    // Não limpa XMLs.
                    //
                }
            }

        });

        // ==================================================
        // SE DER ERRO NA TASK
        // ==================================================

        task.setOnFailed(event -> {

            view.getButtonBar()
                    .getBtSelecionarXml()
                    .setDisable(false);

            view.getButtonBar()
                    .getBtSelecionarPasta()
                    .setDisable(false);

            view.getButtonBar()
                    .getBtImportar()
                    .setDisable(false);

            view.getButtonBar()
                    .getBtRemover()
                    .setDisable(false);

            Throwable erro =
                    task.getException();

            view.getTxtLog().appendText(
                    "\nERRO DURANTE A IMPORTAÇÃO:\n"
                            + (erro != null
                            ? erro.getMessage()
                            : "Erro desconhecido.")
                            + "\n"
            );

            erro.printStackTrace();
        });

        // ==================================================
        // INICIA A TASK EM SEGUNDO PLANO
        // ==================================================

        Thread thread =
                new Thread(task);

        thread.setDaemon(true);

        thread.start();
    }

}