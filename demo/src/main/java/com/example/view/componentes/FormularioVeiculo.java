package com.example.view.componentes;

import com.example.veiculo.TipoVeiculo;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

/**
 * Painel para escolher o tipo de veículo e escrever a marca e o modelo.
 * A matrícula não se escreve: é gerada automaticamente pelo Model.
 * Junta vários objetos mais pequenos (ComboBox, dois CampoRotulado, botão e mensagem).
 */
public class FormularioVeiculo extends VBox {

    private static final String COR_ERRO = "#c62828";
    private static final String COR_SUCESSO = "#2e7d32";

    private final ComboBox<TipoVeiculo> tipo = new ComboBox<>();
    private final CampoRotulado marca = new CampoRotulado("Marca", "ex.: Toyota");
    private final CampoRotulado modelo = new CampoRotulado("Modelo", "ex.: Corolla");
    private final BotaoVisual botaoAdicionar = new BotaoVisual("Adicionar à garagem", BotaoVisual.Estilo.PRINCIPAL);
    private final Label mensagem = new Label();

    public FormularioVeiculo() {
        super(14);
        setPadding(new Insets(20));
        setPrefWidth(300);
        setStyle("-fx-background-color: white; -fx-background-radius: 12;");

        Label titulo = new Label("Novo veículo");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #263238;");

        tipo.getItems().addAll(TipoVeiculo.values());
        tipo.getSelectionModel().selectFirst();
        tipo.setMaxWidth(Double.MAX_VALUE);
        tipo.setConverter(new StringConverter<TipoVeiculo>() {
            @Override
            public String toString(TipoVeiculo valor) {
                return valor == null ? "" : valor.getNome();
            }

            @Override
            public TipoVeiculo fromString(String texto) {
                return null; // a ComboBox não é editável
            }
        });
        Label tituloTipo = new Label("Tipo de veículo");
        tituloTipo.setStyle("-fx-font-weight: bold; -fx-text-fill: #37474f;");
        VBox blocoTipo = new VBox(4, tituloTipo, tipo);

        Label nota = new Label("A matrícula é gerada automaticamente.");
        nota.setWrapText(true);
        nota.setStyle("-fx-text-fill: #78909c; -fx-font-size: 12px;");

        botaoAdicionar.setMaxWidth(Double.MAX_VALUE);
        mensagem.setWrapText(true);

        getChildren().addAll(titulo, blocoTipo, marca, modelo, nota, botaoAdicionar, mensagem);
    }

    public TipoVeiculo getTipo() {
        return tipo.getValue();
    }

    public String getMarca() {
        return marca.getTexto();
    }

    public String getModelo() {
        return modelo.getTexto();
    }

    /** Limpa a marca e o modelo e põe o cursor outra vez na marca. */
    public void limpar() {
        marca.limpar();
        modelo.limpar();
        marca.focar();
    }

    /** Mostra uma mensagem por baixo do botão: a vermelho se for erro, a verde se não for. */
    public void mostrarMensagem(String texto, boolean erro) {
        mensagem.setText(texto);
        mensagem.setStyle("-fx-text-fill: " + (erro ? COR_ERRO : COR_SUCESSO) + "; -fx-font-weight: bold;");
    }

    public void setAdicionarAtivo(boolean ativo) {
        botaoAdicionar.setDisable(!ativo);
    }

    /** O que fazer ao carregar no botão "Adicionar" ou em Enter num dos campos de texto. */
    public void setAoAdicionar(Runnable acao) {
        botaoAdicionar.setOnAction(e -> acao.run());
        marca.setAoCarregarEnter(acao);
        modelo.setAoCarregarEnter(acao);
    }
}
