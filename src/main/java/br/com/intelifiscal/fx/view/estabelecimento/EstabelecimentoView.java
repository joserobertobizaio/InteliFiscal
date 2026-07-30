package br.com.intelifiscal.fx.view.estabelecimento;

import br.com.intelifiscal.fx.components.common.Card;
import br.com.intelifiscal.fx.components.common.CrudButtonBar;
import br.com.intelifiscal.fx.components.common.SectionTitle;
import br.com.intelifiscal.fx.components.common.icons.AppIcon;
import br.com.intelifiscal.fx.components.common.icons.IconType;
import br.com.intelifiscal.fx.view.base.BaseView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class EstabelecimentoView extends BaseView {

    private final TextField txtCnpj = new TextField();
    private final TextField txtInscricaoEstadual = new TextField();
    private final TextField txtRazaoSocial = new TextField();
    private final TextField txtNomeFantasia = new TextField();
    private final TextField txtCep = new TextField();
    private final TextField txtUf = new TextField();
    private final TextField txtCidade = new TextField();
    private final TextField txtBairro = new TextField();
    private final TextField txtEndereco = new TextField();
    private final TextField txtNumero = new TextField();
    private final TextField txtTelefone = new TextField();
    private final TextField txtEmail = new TextField();

    private final ComboBox<String> cbEmpresa = new ComboBox<>();

    private final CrudButtonBar crudButtonBar = new CrudButtonBar();

    private final Button btNovaEmpresa =
            new Button(
                    "Nova Empresa",
                    new AppIcon(IconType.NOVO)
            );

    public EstabelecimentoView() {

        super(
                "Estabelecimento",
                "Cadastro da empresa emissora das Notas Fiscais"
        );

        btNovaEmpresa.getStyleClass().add("btn-nova-empresa");

        btNovaEmpresa.setGraphicTextGap(8);

        btNovaEmpresa.setPrefHeight(36);

        btNovaEmpresa.setPrefWidth(145);

        initialize();
    }

    private void initialize() {

        //---------------------------------------------
        // BARRA SUPERIOR
        //---------------------------------------------

        Label lblEmpresa = new Label("Empresa");

        cbEmpresa.setPrefWidth(340);

        cbEmpresa.getItems().add("Matriz");

        cbEmpresa.getSelectionModel().selectFirst();

        HBox barraEmpresa = new HBox(12);

        barraEmpresa.setAlignment(Pos.CENTER_LEFT);

        HBox.setHgrow(cbEmpresa, Priority.NEVER);

        barraEmpresa.getChildren().addAll(
                lblEmpresa,
                cbEmpresa,
                btNovaEmpresa
        );

        //---------------------------------------------
        // GRID - DADOS DA EMPRESA
        //---------------------------------------------

        GridPane dadosEmpresa = new GridPane();

        dadosEmpresa.setHgap(15);
        dadosEmpresa.setVgap(15);

        dadosEmpresa.add(new Label("CNPJ"), 0, 0);
        dadosEmpresa.add(txtCnpj, 0, 1);

        dadosEmpresa.add(new Label("Inscrição Estadual"), 1, 0);
        dadosEmpresa.add(txtInscricaoEstadual, 1, 1);

        dadosEmpresa.add(new Label("Razão Social"), 0, 2);
        dadosEmpresa.add(txtRazaoSocial, 0, 3, 2, 1);

        dadosEmpresa.add(new Label("Nome Fantasia"), 0, 4);
        dadosEmpresa.add(txtNomeFantasia, 0, 5, 2, 1);

        //---------------------------------------------
        // GRID - ENDEREÇO
        //---------------------------------------------

        GridPane endereco = new GridPane();

        endereco.setHgap(15);
        endereco.setVgap(15);

        endereco.add(new Label("CEP"), 0, 0);
        endereco.add(txtCep, 0, 1);

        endereco.add(new Label("UF"), 1, 0);
        endereco.add(txtUf, 1, 1);

        endereco.add(new Label("Cidade"), 0, 2);
        endereco.add(txtCidade, 0, 3);

        endereco.add(new Label("Bairro"), 1, 2);
        endereco.add(txtBairro, 1, 3);

        endereco.add(new Label("Endereço"), 0, 4);
        endereco.add(txtEndereco, 0, 5);

        endereco.add(new Label("Número"), 1, 4);
        endereco.add(txtNumero, 1, 5);

        //---------------------------------------------
        // GRID - CONTATO
        //---------------------------------------------

        GridPane contato = new GridPane();

        contato.setHgap(15);
        contato.setVgap(15);

        contato.add(new Label("Telefone"), 0, 0);
        contato.add(txtTelefone, 0, 1);

        contato.add(new Label("E-mail"), 1, 0);
        contato.add(txtEmail, 1, 1);

        //---------------------------------------------
        // TAMANHO DOS CAMPOS
        //---------------------------------------------

        txtCnpj.setPrefWidth(280);
        txtInscricaoEstadual.setPrefWidth(280);

        txtRazaoSocial.setPrefWidth(580);
        txtNomeFantasia.setPrefWidth(580);

        txtCep.setPrefWidth(220);
        txtUf.setPrefWidth(120);

        txtCidade.setPrefWidth(580);
        txtBairro.setPrefWidth(160);

        txtEndereco.setPrefWidth(580);
        txtNumero.setPrefWidth(160);

        txtTelefone.setPrefWidth(220);
        txtEmail.setPrefWidth(420);

        //---------------------------------------------
        // SEÇÕES
        //---------------------------------------------

        SectionTitle secEmpresa =
                new SectionTitle("Dados da Empresa");

        SectionTitle secEndereco =
                new SectionTitle("Endereço");

        SectionTitle secContato =
                new SectionTitle("Contato");

        //---------------------------------------------
        // CONTEÚDO
        //---------------------------------------------

        VBox conteudo = new VBox(28);

        conteudo.setPadding(new Insets(20));

        conteudo.getChildren().addAll(

                barraEmpresa,

                secEmpresa,
                dadosEmpresa,

                secEndereco,
                endereco,

                secContato,
                contato,

                crudButtonBar
        );

        //---------------------------------------------
        // CARD
        //---------------------------------------------

        Card card = new Card(conteudo);

        card.setWidthPercentage(0.92);

        card.setMaxContentWidth(1100);

        //---------------------------------------------
        // PAINEL
        //---------------------------------------------

        VBox painel = new VBox(card);

        painel.setPadding(new Insets(10));

        //---------------------------------------------
        // SCROLL
        //---------------------------------------------

        ScrollPane scrollPane = new ScrollPane(painel);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setPannable(true);

        setContent(scrollPane);
    }

    public CrudButtonBar getCrudButtonBar() {
        return crudButtonBar;
    }

    public TextField getTxtCnpj() {
        return txtCnpj;
    }

    public TextField getTxtInscricaoEstadual() {
        return txtInscricaoEstadual;
    }

    public TextField getTxtRazaoSocial() {
        return txtRazaoSocial;
    }

    public TextField getTxtNomeFantasia() {
        return txtNomeFantasia;
    }

    public TextField getTxtCep() {
        return txtCep;
    }

    public TextField getTxtUf() {
        return txtUf;
    }

    public TextField getTxtCidade() {
        return txtCidade;
    }

    public TextField getTxtBairro() {
        return txtBairro;
    }

    public TextField getTxtEndereco() {
        return txtEndereco;
    }

    public TextField getTxtNumero() {
        return txtNumero;
    }

    public TextField getTxtTelefone() {
        return txtTelefone;
    }

    public TextField getTxtEmail() {
        return txtEmail;
    }

    public ComboBox<String> getCbEmpresa() {
        return cbEmpresa;
    }

    public Button getBtNovaEmpresa() {
        return btNovaEmpresa;
    }
}