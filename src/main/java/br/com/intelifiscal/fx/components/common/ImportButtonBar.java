package br.com.intelifiscal.fx.components.common;

import br.com.intelifiscal.fx.components.common.icons.AppIcon;
import br.com.intelifiscal.fx.components.common.icons.IconType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ImportButtonBar extends HBox {

    private final Button btSelecionarXml =
            new Button(
                    "Selecionar XML",
                    new AppIcon(IconType.IMPORTACAO_XML)
            );

    private final Button btSelecionarPasta =
            new Button(
                    "Selecionar Pasta",
                    new AppIcon(IconType.IMPORTACAO_XML)
            );

    private final Button btImportar =
            new Button(
                    "Importar",
                    new AppIcon(IconType.SALVAR)
            );

    private final Button btRemover =
            new Button(
                    "Remover",
                    new AppIcon(IconType.EXCLUIR)
            );

    public ImportButtonBar() {

        setSpacing(12);

        setAlignment(Pos.CENTER_LEFT);

        setPadding(new Insets(0));

        configurar(btSelecionarXml, "crud-novo");
        configurar(btSelecionarPasta, "crud-novo");
        configurar(btImportar, "crud-salvar");
        configurar(btRemover, "crud-excluir");

        getChildren().addAll(
                btSelecionarXml,
                btSelecionarPasta,
                btImportar,
                btRemover
        );
    }

    private void configurar(Button botao, String css) {

        botao.setPrefWidth(170);
        botao.setPrefHeight(38);
        botao.setGraphicTextGap(8);

        botao.getStyleClass().add(css);
    }

    public Button getBtSelecionarXml() {
        return btSelecionarXml;
    }

    public Button getBtSelecionarPasta() {
        return btSelecionarPasta;
    }

    public Button getBtImportar() {
        return btImportar;
    }

    public Button getBtRemover() {
        return btRemover;
    }
}