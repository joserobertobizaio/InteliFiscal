package br.com.intelifiscal.fx.components.common;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;

public class SectionTitle extends VBox {

    private final Label title;

    private final Separator separator;

    public SectionTitle(String text) {

        title = new Label(text);

        separator = new Separator();

        initialize();
    }

    private void initialize() {

        setSpacing(3);

        setPadding(new Insets(6, 0, 0, 0));

        title.getStyleClass().add("section-title");

        getChildren().addAll(
                title,
                separator
        );
    }
}