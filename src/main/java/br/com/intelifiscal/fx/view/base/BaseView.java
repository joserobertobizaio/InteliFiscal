package br.com.intelifiscal.fx.view.base;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class BaseView extends BorderPane {

    private final VBox header = new VBox(5);

    private final Label lblTitulo = new Label();

    private final Label lblSubtitulo = new Label();

    public BaseView(String titulo,
                    String subtitulo) {

        initialize();

        lblTitulo.setText(titulo);
        lblSubtitulo.setText(subtitulo);
    }

    private void initialize() {

        lblTitulo.getStyleClass().add("view-title");

        lblSubtitulo.getStyleClass().add("view-subtitle");

        header.getChildren().addAll(
                lblTitulo,
                lblSubtitulo
        );

        header.setPadding(new Insets(20));

        setTop(header);
    }

    protected void setContent(Node node) {

        setCenter(node);
    }

}