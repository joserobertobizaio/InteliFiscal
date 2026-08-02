package br.com.intelifiscal.fx.controller.importacao;

import br.com.intelifiscal.fx.view.importacao.ImportacaoXMLView;
import br.com.intelifiscal.util.XmlUtil;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import br.com.intelifiscal.dto.xml.XmlNFeDTO;
import br.com.intelifiscal.service.xml.XmlNFeReader;
import br.com.intelifiscal.service.MinhaEmpresaService;

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

    private final MinhaEmpresaService minhaEmpresaService =
            new MinhaEmpresaService();

    private final NFeService nfeService =
            new NFeService();

    private final List<XmlNFeDTO> xmls =
            new ArrayList<>();

    public ImportacaoXMLController(ImportacaoXMLView view) {

        this.view = view;

        configurarEventos();
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
    }

    private void selecionarXml() {

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

        view.getProgressBar().setProgress(0);

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

        view.getProgressBar().setProgress(0);

        view.getResumo().limpar();

        view.getTxtLog().appendText(
                "Lista de arquivos removida.\n"
        );

    }

    private void importar() {

        if (xmls.isEmpty()) {

            view.getTxtLog().appendText(
                    "Nenhum XML selecionado.\n"
            );

            return;
        }

        view.getProgressBar().setProgress(0);

        view.getTxtLog().appendText(
                "Iniciando importação...\n"
        );

        int importadas = 0;
        int ignoradas = 0;
        int total = xmls.size();

        for (XmlNFeDTO dto : xmls) {

            if (nfeService.existe(dto.getChave())) {

                ignoradas++;

                view.getProgressBar().setProgress(
                        (double) (importadas + ignoradas) / total
                );

                view.getTxtLog().appendText(
                        "NF " + dto.getNumero()
                                + " já existe.\n"
                );

                continue;
            }

            NFe nfe = new NFe();

            nfe.setChave(dto.getChave());
            nfe.setNumero(dto.getNumero());
            nfe.setSerie(dto.getSerie());
            nfe.setModelo("55");
            nfe.setDataEmissao(dto.getDataEmissao());
            nfe.setCnpjEmitente(dto.getCnpjEmitente());
            nfe.setEmitente(dto.getRazaoSocialEmitente());
            nfe.setCnpjDestinatario(dto.getCnpjDestinatario());
            nfe.setDestinatario(dto.getRazaoSocialDestinatario());
            nfe.setValorTotal(dto.getValorTotal());

            if (minhaEmpresaService.ehMinhaEmpresa(dto.getCnpjEmitente())) {
                nfe.setTipo("Venda");
            } else {
                nfe.setTipo("Compra");
            }

            nfe.setSituacao("Importado");
            nfe.setDataImportacao(LocalDateTime.now());

            System.out.println("Chave: " + dto.getChave());
            System.out.println("Número: " + dto.getNumero());

            nfeService.salvar(nfe);

            importadas++;

            view.getProgressBar().setProgress(
                    (double) (importadas + ignoradas) / total
            );

            view.getTxtLog().appendText(
                    "NF " + dto.getNumero()
                            + " importada.\n"
            );
        }

        view.getProgressBar().setProgress(1.0);

        view.getTxtLog().appendText(
                "\nImportação concluída.\n" +
                        "Importadas: " + importadas + "\n" +
                        "Ignoradas: " + ignoradas + "\n"
        );

        ButtonType btNovoLote = new ButtonType("Novo Lote");
        ButtonType btDashboard = new ButtonType("Dashboard");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Importação Concluída");
        alert.setHeaderText("Importação realizada com sucesso!");

        alert.setContentText(
                "Notas importadas: " + importadas +
                        "\nNotas ignoradas: " + ignoradas +
                        "\n\nO que deseja fazer?"
        );

        alert.getButtonTypes().setAll(
                btNovoLote,
                btDashboard
        );

        Optional<ButtonType> resultado = alert.showAndWait();

        if (resultado.isPresent()) {

            if (resultado.get() == btDashboard) {

                NavigationManager.show(ScreenType.DASHBOARD);

            } else {

                view.getTabela().getItems().clear();
                view.getTxtLog().clear();
                view.getProgressBar().setProgress(0);
                xmls.clear();

            }
        }
    }

}