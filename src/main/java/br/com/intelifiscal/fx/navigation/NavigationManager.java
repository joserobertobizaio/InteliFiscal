package br.com.intelifiscal.fx.navigation;

import br.com.intelifiscal.fx.components.content.ContentPane;
import javafx.scene.Node;

/**
 * Gerencia a navegação entre as telas da aplicação.
 */
public final class NavigationManager {

    private static ContentPane contentPane;

    private NavigationManager() {
    }

    /**
     * Define a área onde as telas serão exibidas.
     */
    public static void initialize(ContentPane pane) {
        contentPane = pane;
    }

    /**
     * Exibe uma nova tela.
     */
    public static void show(Node node) {

        if (contentPane == null) {
            throw new IllegalStateException(
                    "NavigationManager não foi inicializado."
            );
        }

        contentPane.show(node);
    }

    /**
     * Exibe a View correspondente ao ScreenType informado.
     */
    public static void show(ScreenType screenType) {

        show(ViewFactory.create(screenType));

    }

}