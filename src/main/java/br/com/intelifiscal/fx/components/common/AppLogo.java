package br.com.intelifiscal.fx.components.common;

import br.com.intelifiscal.constants.ApplicationConstants;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 * Logotipo da aplicação.
 */
public class AppLogo extends HBox {

    public AppLogo() {

        setSpacing(10);

        setAlignment(Pos.CENTER_LEFT);

        StackPane caixaLogo = new StackPane();

        caixaLogo.getStyleClass().add("app-logo-box");

        caixaLogo.setPrefSize(34, 34);

        Label abreviacao = new Label(
                ApplicationConstants.APPLICATION_ABBREVIATION
        );

        abreviacao.getStyleClass().add("app-logo-text");

        caixaLogo.getChildren().add(abreviacao);

        Label titulo = new Label(
                ApplicationConstants.APPLICATION_NAME
        );

        titulo.getStyleClass().add("app-title");

        getChildren().addAll(
                caixaLogo,
                titulo
        );

    }

}