package br.com.intelifiscal.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public final class Mensagem {

    private Mensagem() {
        // Impede instanciação
    }

    public static void sucesso(String mensagem) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Sucesso");
        alert.setHeaderText("Operação concluída.");
        alert.setContentText("✓ " + mensagem);

        alert.showAndWait();
    }

    public static void aviso(String mensagem) {

        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle("Atenção");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);

        alert.showAndWait();
    }

    public static void erro(String mensagem) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Erro");
        alert.setHeaderText("Ocorreu um problema.");
        alert.setContentText(mensagem);

        alert.showAndWait();
    }

    public static boolean confirmar(String mensagem) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Confirmação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);

        return alert.showAndWait()
                .filter(ButtonType.OK::equals)
                .isPresent();
    }

}