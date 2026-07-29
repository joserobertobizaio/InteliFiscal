package br.com.intelifiscal.fx.components.common;

import br.com.intelifiscal.fx.components.common.icons.AppIcon;
import br.com.intelifiscal.fx.components.common.icons.IconType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class CrudButtonBar extends HBox {

    private final Button btNovo =
            new Button("Novo", new AppIcon(IconType.NOVO));

    private final Button btSalvar =
            new Button("Salvar", new AppIcon(IconType.SALVAR));

    private final Button btExcluir =
            new Button("Excluir", new AppIcon(IconType.EXCLUIR));

    private final Button btFechar =
            new Button("Fechar", new AppIcon(IconType.FECHAR));

    public CrudButtonBar() {

        setSpacing(12);

        setAlignment(Pos.CENTER_RIGHT);

        setPadding(new Insets(15, 0, 0, 0));

        configurarBotao(btNovo, "crud-novo");

        configurarBotao(btSalvar, "crud-salvar");

        configurarBotao(btExcluir, "crud-excluir");

        configurarBotao(btFechar, "crud-fechar");

        getChildren().addAll(

                btNovo,

                btSalvar,

                btExcluir,

                btFechar
        );
    }

    private void configurarBotao(Button botao, String css) {

        botao.setPrefWidth(120);

        botao.setPrefHeight(38);

        botao.setGraphicTextGap(8);

        botao.getStyleClass().add(css);
    }

    public Button getBtNovo() {
        return btNovo;
    }

    public Button getBtSalvar() {
        return btSalvar;
    }

    public Button getBtExcluir() {
        return btExcluir;
    }

    public Button getBtFechar() {
        return btFechar;
    }

}