package br.com.intelifiscal.util;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

import java.text.NumberFormat;
import java.util.Locale;

public final class FormatadorNumero {

    private static final Locale LOCALE_BR =
            new Locale("pt", "BR");


    private FormatadorNumero() {
    }


    //==================================================
    // FORMATAÇÃO BRASILEIRA - VALORES
    //==================================================

    public static String formatar(Number valor) {

        if (valor == null) {
            return "";
        }

        NumberFormat formatter =
                NumberFormat.getNumberInstance(
                        LOCALE_BR
                );

        formatter.setGroupingUsed(true);
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);

        return formatter.format(valor);
    }


    //==================================================
    // FORMATAÇÃO - QUANTIDADE
    //==================================================

    public static String formatarQuantidade(
            Number valor
    ) {

        if (valor == null) {
            return "";
        }

        NumberFormat formatter =
                NumberFormat.getNumberInstance(
                        LOCALE_BR
                );

        formatter.setGroupingUsed(true);
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(0);

        return formatter.format(valor);
    }


    //==================================================
    // FORMATAÇÃO - VALOR UNITÁRIO
    //==================================================

    public static String formatarValorUnitario(
            Number valor
    ) {

        if (valor == null) {
            return "";
        }

        NumberFormat formatter =
                NumberFormat.getNumberInstance(
                        LOCALE_BR
                );

        formatter.setGroupingUsed(true);
        formatter.setMinimumFractionDigits(4);
        formatter.setMaximumFractionDigits(4);

        return formatter.format(valor);
    }


    //==================================================
    // CELL FACTORY - VALORES
    //==================================================

    public static <S> void aplicar(
            TableColumn<S, Number> coluna
    ) {

        coluna.setCellFactory(
                tc -> new TableCell<>() {

                    @Override
                    protected void updateItem(
                            Number item,
                            boolean empty
                    ) {

                        super.updateItem(
                                item,
                                empty
                        );

                        if (empty || item == null) {

                            setText(null);

                        } else {

                            setText(
                                    FormatadorNumero.formatar(
                                            item
                                    )
                            );
                        }
                    }
                }
        );
    }


    //==================================================
    // CELL FACTORY - QUANTIDADE
    //==================================================

    public static <S> void aplicarQuantidade(
            TableColumn<S, Number> coluna
    ) {

        coluna.setCellFactory(
                tc -> new TableCell<>() {

                    @Override
                    protected void updateItem(
                            Number item,
                            boolean empty
                    ) {

                        super.updateItem(
                                item,
                                empty
                        );

                        if (empty || item == null) {

                            setText(null);

                        } else {

                            setText(
                                    FormatadorNumero.formatarQuantidade(
                                            item
                                    )
                            );
                        }
                    }
                }
        );
    }


    //==================================================
    // CELL FACTORY - VALOR UNITÁRIO
    //==================================================

    public static <S> void aplicarValorUnitario(
            TableColumn<S, Number> coluna
    ) {

        coluna.setCellFactory(
                tc -> new TableCell<>() {

                    @Override
                    protected void updateItem(
                            Number item,
                            boolean empty
                    ) {

                        super.updateItem(
                                item,
                                empty
                        );

                        if (empty || item == null) {

                            setText(null);

                        } else {

                            setText(
                                    FormatadorNumero.formatarValorUnitario(
                                            item
                                    )
                            );
                        }
                    }
                }
        );
    }
}