package br.com.intelifiscal.fx.components.dashboard;

import br.com.intelifiscal.fx.components.common.Card;
import br.com.intelifiscal.fx.components.common.icons.AppIcon;
import br.com.intelifiscal.fx.components.common.icons.IconType;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MetricCard extends StackPane {

    private final Label lblTitulo = new Label();

    private final Label lblValor = new Label();

    public MetricCard(IconType iconType,
                      String titulo,
                      String valor) {

        AppIcon icon = new AppIcon(iconType);

        icon.setIconSize(26);

        lblTitulo.setText(titulo);

        lblValor.setText(valor);

        lblTitulo.getStyleClass().add("metric-title");

        lblValor.getStyleClass().add("metric-value");

        VBox box = new VBox(12);

        box.setAlignment(Pos.CENTER);

        box.getChildren().addAll(
                icon,
                lblTitulo,
                lblValor
        );

        Card card = new Card(box);

        card.setPrefWidth(200);

        card.setMinWidth(200);

        card.setMaxWidth(200);

        card.setPrefHeight(130);

        getChildren().add(card);
    }

    public void setValue(String valor) {

        lblValor.setText(valor);

    }

}