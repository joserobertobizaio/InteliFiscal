package br.com.intelifiscal.fx.components.common.icons;

import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Componente responsável por exibir os ícones da aplicação.
 *
 * Toda a aplicação utiliza AppIcon.
 * Somente esta classe conhece o Ikonli.
 */
public class AppIcon extends FontIcon {

    public AppIcon(IconType iconType) {

        super(resolve(iconType));

        setIconSize(18);
    }

    private static FontAwesomeSolid resolve(IconType iconType) {

        return switch (iconType) {

            case DASHBOARD -> FontAwesomeSolid.HOUSE_USER;

            case ESTABELECIMENTO -> FontAwesomeSolid.BUILDING;

            case IMPORTACAO_XML -> FontAwesomeSolid.FILE_IMPORT;

            case COMPRAS -> FontAwesomeSolid.SHOPPING_CART;

            case VENDAS -> FontAwesomeSolid.MONEY_BILL;

            case COMPARAR_COMPRA_VENDA -> FontAwesomeSolid.EXCHANGE_ALT;

            case PRODUTOS -> FontAwesomeSolid.BOX_OPEN;

            case RELATORIOS -> FontAwesomeSolid.CHART_BAR;

            case CONFIGURACOES -> FontAwesomeSolid.COG;

            case NOVO -> FontAwesomeSolid.PLUS;

            case EDITAR -> FontAwesomeSolid.PENCIL_ALT;

            case SALVAR -> FontAwesomeSolid.SAVE;

            case EXCLUIR -> FontAwesomeSolid.TRASH;

            case FECHAR -> FontAwesomeSolid.TIMES;

        };
    }
}