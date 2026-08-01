package br.com.intelifiscal.fx.controller.importacao;

import br.com.intelifiscal.fx.view.importacao.ImportacaoXMLView;
import br.com.intelifiscal.util.XmlUtil;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import br.com.intelifiscal.dto.xml.XmlNFeDTO;
import br.com.intelifiscal.service.xml.XmlNFeReader;

import java.io.File;
import java.util.List;

public class ImportacaoXMLController {

    private final ImportacaoXMLView view;

    private final XmlNFeReader reader = new XmlNFeReader();

    public ImportacaoXMLController(ImportacaoXMLView view) {

        this.view = view;

        configurarEventos();
    }

    private void configurarEventos() {

        view.getButtonBar()
                .getBtSelecionarXml()
                .setOnAction(e -> selecionarXml());

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

        // Adiciona os XMLs selecionados
        for (File arquivo : arquivos) {

            XmlNFeDTO dto = reader.ler(arquivo);

            var item = new br.com.intelifiscal.fx.view.importacao.model.ImportacaoXmlItem();

            item.arquivoProperty().set(dto.getArquivo());

            item.tipoProperty().set("Compra"); // provisório

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

            item.tipoProperty().set("Compra");

            item.situacaoProperty().set("Lido");

            item.situacaoProperty().set("Lido");

            view.getTabela().getItems().add(item);

        }

        view.getTxtLog().appendText(
                "Selecionados "
                        + arquivos.size()
                        + " arquivo(s).\n"
        );

        System.out.println("Itens na tabela: " + view.getTabela().getItems().size());

    }

}