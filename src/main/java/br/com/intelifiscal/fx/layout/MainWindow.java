package br.com.intelifiscal.fx.layout;

import br.com.intelifiscal.constants.WindowConstants;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Janela principal da aplicação.
 */
public class MainWindow extends BorderPane {

    public MainWindow() {

        Label titulo = new Label(WindowConstants.APPLICATION_TITLE);

        titulo.setStyle("""
                -fx-font-size: 24px;
                -fx-font-weight: bold;
                """);

        setCenter(titulo);

        BorderPane.setAlignment(titulo, Pos.CENTER);

    }

    /**
     * Exibe a janela principal.
     */
    public void show(Stage stage) {

        Rectangle2D areaUtil = Screen.getPrimary().getVisualBounds();

        Scene scene = new Scene(this);

        stage.setScene(scene);

        stage.setTitle(WindowConstants.APPLICATION_TITLE);

        double largura = areaUtil.getWidth()
                * WindowConstants.INITIAL_WIDTH_PERCENT;

        double altura = areaUtil.getHeight()
                * WindowConstants.INITIAL_HEIGHT_PERCENT;

        stage.setWidth(
                Math.max(WindowConstants.MIN_WIDTH, largura)
        );

        stage.setHeight(
                Math.max(WindowConstants.MIN_HEIGHT, altura)
        );

        stage.setMinWidth(WindowConstants.MIN_WIDTH);

        stage.setMinHeight(WindowConstants.MIN_HEIGHT);

        stage.centerOnScreen();

        stage.setResizable(true);

        stage.show();

    }

}