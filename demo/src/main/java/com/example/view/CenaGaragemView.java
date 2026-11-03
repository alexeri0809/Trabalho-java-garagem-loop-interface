package com.example.view;

import com.example.view.componentes.BotaoVisual;
import com.example.view.componentes.FormularioVeiculo;
import com.example.view.componentes.ListaVeiculosView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * CENA 1 (janela 1): escolher os veículos da garagem.
 * É o "tronco" desta janela: junta o formulário, a lista e os botões, que são objetos separados.
 * Não conhece o Model: só mostra o que o Controller lhe manda mostrar.
 */
public class CenaGaragemView extends BorderPane {

    // ======================== TAMANHO E FUNDO DA JANELA: altera aqui ========================
    private static final double LARGURA_JANELA = 940;
    private static final double ALTURA_JANELA = 580;
    private static final String COR_FUNDO = "#eceff1";
    // =========================================================================================

    private final FormularioVeiculo formulario = new FormularioVeiculo();
    private final ListaVeiculosView lista = new ListaVeiculosView();
    private final BotaoVisual botaoRemover = new BotaoVisual("Remover selecionado", BotaoVisual.Estilo.PERIGO);
    private final BotaoVisual botaoIniciarLoop = new BotaoVisual("Iniciar loop  ▶", BotaoVisual.Estilo.PRINCIPAL);
    private final Scene cena;

    public CenaGaragemView() {
        setPadding(new Insets(20));
        setStyle("-fx-background-color: " + COR_FUNDO + ";");

        Label titulo = new Label("Garagem");
        titulo.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #263238;");
        Label subtitulo = new Label("Escolhe os veículos e depois põe-nos a rodar no loop de 5 vagas.");
        subtitulo.setStyle("-fx-text-fill: #607d8b;");
        VBox cabecalho = new VBox(2, titulo, subtitulo);
        cabecalho.setPadding(new Insets(0, 0, 16, 0));

        HBox barraBotoes = new HBox(10, botaoRemover, botaoIniciarLoop);
        barraBotoes.setAlignment(Pos.CENTER_RIGHT);
        barraBotoes.setPadding(new Insets(12, 0, 0, 0));

        VBox centro = new VBox(lista, barraBotoes);
        VBox.setVgrow(lista, Priority.ALWAYS);
        BorderPane.setMargin(centro, new Insets(0, 0, 0, 20));

        setTop(cabecalho);
        setLeft(formulario);
        setCenter(centro);

        cena = new Scene(this, LARGURA_JANELA, ALTURA_JANELA);
    }

    public Scene getCena() {
        return cena;
    }

    public FormularioVeiculo getFormulario() {
        return formulario;
    }

    public ListaVeiculosView getLista() {
        return lista;
    }

    public BotaoVisual getBotaoRemover() {
        return botaoRemover;
    }

    public BotaoVisual getBotaoIniciarLoop() {
        return botaoIniciarLoop;
    }
}
