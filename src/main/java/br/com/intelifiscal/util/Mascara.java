package br.com.intelifiscal.util;

import javafx.scene.control.TextField;

public final class Mascara {

    private Mascara() {
    }

    /**
     * Máscara para CNPJ alfanumérico.
     * Formato:
     *
     * XX.XXX.XXX/XXXX-XX
     *
     * Aceita letras e números.
     * Converte automaticamente letras para maiúsculas.
     */
    public static void aplicarCnpjAlfanumerico(TextField campo) {

        campo.textProperty().addListener((obs, antigo, novo) -> {

            if (novo == null) {
                return;
            }

            // Mantém apenas letras e números
            String valor = novo
                    .toUpperCase()
                    .replaceAll("[^A-Z0-9]", "");

            // Máximo de 14 caracteres
            if (valor.length() > 14) {
                valor = valor.substring(0, 14);
            }

            StringBuilder mascara = new StringBuilder();

            for (int i = 0; i < valor.length(); i++) {

                if (i == 2 || i == 5) {
                    mascara.append('.');
                }

                if (i == 8) {
                    mascara.append('/');
                }

                if (i == 12) {
                    mascara.append('-');
                }

                mascara.append(valor.charAt(i));
            }

            String textoFormatado = mascara.toString();

            if (!textoFormatado.equals(novo)) {

                campo.setText(textoFormatado);
                campo.positionCaret(textoFormatado.length());

            }

        });

    }

    /**
     * Máscara para CEP
     * Formato:
     *
     * 00000-000
     */
    public static void aplicarCep(TextField campo) {

        campo.textProperty().addListener((obs, antigo, novo) -> {

            if (novo == null) {
                return;
            }

            String valor = novo.replaceAll("\\D", "");

            if (valor.length() > 8) {
                valor = valor.substring(0, 8);
            }

            StringBuilder mascara = new StringBuilder();

            for (int i = 0; i < valor.length(); i++) {

                if (i == 5) {
                    mascara.append('-');
                }

                mascara.append(valor.charAt(i));
            }

            String textoFormatado = mascara.toString();

            if (!textoFormatado.equals(novo)) {

                campo.setText(textoFormatado);
                campo.positionCaret(textoFormatado.length());

            }

        });

    }

    /**
     * Formata um CNPJ numérico ou alfanumérico.
     *
     * Entrada:
     * 12345678000190
     *
     * Saída:
     * 12.345.678/0001-90
     *
     * Entrada:
     * 1E3D34DFRE4445
     *
     * Saída:
     * 1E.3D3.4DF/RE44-45
     */
    public static String formatarCnpj(String cnpj) {

        if (cnpj == null || cnpj.isBlank()) {
            return "";
        }

        String valor = cnpj
                .toUpperCase()
                .replaceAll("[^A-Z0-9]", "");

        if (valor.length() > 14) {
            valor = valor.substring(0, 14);
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < valor.length(); i++) {

            if (i == 2 || i == 5) {
                sb.append('.');
            }

            if (i == 8) {
                sb.append('/');
            }

            if (i == 12) {
                sb.append('-');
            }

            sb.append(valor.charAt(i));
        }

        return sb.toString();
    }

}