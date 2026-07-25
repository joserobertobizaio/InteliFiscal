package br.com.intelifiscal.fx.components.sidebar;

import br.com.intelifiscal.fx.navigation.ScreenType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Componente reutilizável que representa um item do menu lateral.
 */
public class SidebarItem extends HBox {

    private final ScreenType screenType;
    private final Node icon;
    private final Label label;

    public SidebarItem(ScreenType screenType, Node icon, String text) {

        this.screenType = screenType;
        this.icon = icon;
        this.label = new Label(text);

        initialize();
    }

    private void initialize() {

        setAlignment(Pos.CENTER_LEFT);
        setSpacing(12);
        setPadding(new Insets(12, 16, 12, 16));

        getStyleClass().add("sidebar-item");
        label.getStyleClass().add("sidebar-item-text");

        getChildren().addAll(icon, label);
    }

    public ScreenType getScreenType() {
        return screenType;
    }

    public Node getIcon() {
        return icon;
    }

    public Label getLabel() {
        return label;
    }
}