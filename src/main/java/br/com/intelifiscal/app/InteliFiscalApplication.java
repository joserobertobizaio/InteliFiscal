package br.com.intelifiscal.app;

import br.com.intelifiscal.database.initializer.DatabaseInitializer;
import br.com.intelifiscal.fx.layout.MainWindow;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Classe principal da aplicação InteliFiscal.
 */
public class InteliFiscalApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // Inicializa a infraestrutura do banco de dados
        DatabaseInitializer.initialize();

        // Exibe a janela principal
        MainWindow mainWindow = new MainWindow();
        mainWindow.show(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }

}