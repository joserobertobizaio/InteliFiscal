package br.com.intelifiscal.fx.components.common;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Componente reutilizável para exibição de conteúdo em formato de Card.
 */
public class Card extends StackPane {

    /**
     * Largura padrão do Card em relação ao container.
     */
    private static final double DEFAULT_WIDTH_PERCENTAGE = 0.60;

    /**
     * Largura máxima padrão.
     */
    private static final double DEFAULT_MAX_WIDTH = 900;

    private final DoubleProperty widthPercentage =
            new SimpleDoubleProperty(DEFAULT_WIDTH_PERCENTAGE);

    private final DoubleProperty maxContentWidth =
            new SimpleDoubleProperty(DEFAULT_MAX_WIDTH);

    public Card() {
        initialize();
    }

    public Card(Node content) {

        initialize();

        setContent(content);
    }

    private void initialize() {

        getStyleClass().add("card");

        setPadding(new Insets(30));

        setMaxWidth(Region.USE_PREF_SIZE);

        parentProperty().addListener((obs, oldParent, newParent) -> {

            if (newParent instanceof Region region) {

                prefWidthProperty().bind(
                        Bindings.createDoubleBinding(
                                () -> Math.min(
                                        region.getWidth() * widthPercentage.get(),
                                        maxContentWidth.get()
                                ),
                                region.widthProperty(),
                                widthPercentage,
                                maxContentWidth
                        )
                );

            }

        });

    }

    /**
     * Define o conteúdo do Card.
     */
    public void setContent(Node content) {

        getChildren().setAll(content);

    }

    /**
     * Define a largura percentual.
     */
    public void setWidthPercentage(double percentage) {

        widthPercentage.set(percentage);

    }
    /**
     * Define a largura máxima.
     */

    public void setMaxContentWidth(double width) {

        maxContentWidth.set(width);

    }

}