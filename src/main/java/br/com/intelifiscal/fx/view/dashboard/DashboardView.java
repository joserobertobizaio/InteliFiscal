package br.com.intelifiscal.fx.view.dashboard;

import br.com.intelifiscal.fx.view.base.BaseView;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class DashboardView extends BaseView {

    public DashboardView() {

        super(
                "Dashboard",
                "Bem-vindo ao InteliFiscal"
        );

        initialize();
    }

    private void initialize() {

        Label lbl = new Label("Dashboard em construção...");

        lbl.getStyleClass().add("dashboard-message");

        StackPane painel = new StackPane(lbl);

        painel.setAlignment(Pos.CENTER);

        setContent(painel);
    }
}