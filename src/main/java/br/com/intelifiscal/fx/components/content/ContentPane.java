package br.com.intelifiscal.fx.components.content;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * Área central da aplicação.
 *
 * Todo o conteúdo das telas será exibido aqui.
 */
public class ContentPane extends StackPane {

    public ContentPane() {
        initialize();
    }

    public void show(Node node) {
        getChildren().setAll(node);
    }

    private void initialize() {
        getStyleClass().add("content-pane");
    }
}