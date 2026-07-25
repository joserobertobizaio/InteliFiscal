package br.com.intelifiscal.fx.components.topbar;

import br.com.intelifiscal.constants.LayoutConstants;
import br.com.intelifiscal.fx.components.common.AppLogo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import br.com.intelifiscal.constants.ApplicationConstants;

/**
 * Barra superior da aplicação.
 */
public class TopBar extends BorderPane {

    public TopBar() {

        setPrefHeight(LayoutConstants.TOPBAR_HEIGHT);

        setMinHeight(LayoutConstants.TOPBAR_HEIGHT);

        setMaxHeight(LayoutConstants.TOPBAR_HEIGHT);

        setPadding(new Insets(
                0,
                LayoutConstants.DEFAULT_PADDING,
                0,
                LayoutConstants.DEFAULT_PADDING
        ));

        getStyleClass().add("top-bar");

        AppLogo logo = new AppLogo();

        Label versao = new Label(ApplicationConstants.APPLICATION_VERSION);

        versao.getStyleClass().add("top-bar-version");

        setLeft(logo);

        setRight(versao);

        BorderPane.setAlignment(logo, Pos.CENTER_LEFT);

        BorderPane.setAlignment(versao, Pos.CENTER_RIGHT);

    }

}