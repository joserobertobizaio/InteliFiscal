package br.com.intelifiscal.fx.controller;

import br.com.intelifiscal.fx.navigation.ScreenType;

/**
 * Controlador responsável pela navegação da SideBar.
 */
public class SidebarController {

    /**
     * Solicita a navegação para uma determinada tela.
     */
    public void navigate(ScreenType screenType) {

        System.out.println("Navegando para: " + screenType);

    }

}