package com.example.view;

import com.example.loop.PosicaoRoda;
import com.example.view.componentes.BotaoVisual;
import com.example.view.componentes.FormularioVeiculo;
import com.example.view.componentes.ListaVeiculosView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.EnumMap;
import java.util.Map;

/**
 * CENA 1 (janela 1): escolher os veículos de cada roda.
 * É o "tronco" desta janela: junta o formulário, os três separadores (uma lista por roda) e os botões,
 * que são objetos separados. Não conhece o Model: só mostra o que o Controller lhe manda mostrar.
 */
public class CenaGaragemView extends BorderPane {

    // ======================== TAMANHO E FUNDO DA JANELA: altera aqui ========================
    private static final double LARGURA_JANELA = 980;
    private static final double ALTURA_JANELA = 660;
    private static final String COR_FUNDO = "#eceff1";
    // =========================================================================================

    private final FormularioVeiculo formulario = new FormularioVeiculo();
    private final TabPane separadores = new TabPane();
    private final Map<PosicaoRoda, ListaVeiculosView> listas = new EnumMap<>(PosicaoRoda.class);
    private final BotaoVisual botaoRemover = new BotaoVisual("Remover selecionado", BotaoVisual.Estilo.PERIGO);
    private final BotaoVisual botaoIniciarLoop = new BotaoVisual("Iniciar loop  ▶", BotaoVisual.Estilo.PRINCIPAL);
    private final Scene cena;

    public CenaGaragemView() {
        setPadding(new Insets(20));
        setStyle("-fx-background-color: " + COR_FUNDO + ";");

        Label titulo = new Label("Garagem");
        titulo.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #263238;");
        Label subtitulo = new Label("Escolhe a roda e os veículos de cada uma, e depois põe as três a rodar no loop.");
        subtitulo.setStyle("-fx-text-fill: #607d8b;");
        VBox cabecalho = new VBox(2, titulo, subtitulo);
        cabecalho.setPadding(new Insets(0, 0, 16, 0));

        // Um separador por roda, pela ordem esquerda, central, direita
        separadores.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        for (PosicaoRoda posicao : PosicaoRoda.values()) {
            ListaVeiculosView lista = new ListaVeiculosView(posicao.getNome());
            listas.put(posicao, lista);
            separadores.getTabs().add(new Tab(posicao.getNome(), lista));
        }

        // O separador aberto e a roda escolhida no formulário andam sempre juntos
        separadores.getSelectionModel().select(PosicaoRoda.CENTRAL.ordinal());
        separadores.getSelectionModel().selectedIndexProperty().addListener(
                (observado, antigo, novo) -> formulario.setRoda(PosicaoRoda.values()[novo.intValue()]));
        formulario.aoMudarRoda(roda -> separadores.getSelectionModel().select(roda.ordinal()));

        HBox barraBotoes = new HBox(10, botaoRemover, botaoIniciarLoop);
        barraBotoes.setAlignment(Pos.CENTER_RIGHT);
        barraBotoes.setPadding(new Insets(12, 0, 0, 0));

        VBox centro = new VBox(separadores, barraBotoes);
        VBox.setVgrow(separadores, Priority.ALWAYS);
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

    /** @return a lista da roda indicada */
    public ListaVeiculosView getLista(PosicaoRoda roda) {
        return listas.get(roda);
    }

    /** @return a lista do separador que está aberto */
    public ListaVeiculosView getListaAtiva() {
        return listas.get(PosicaoRoda.values()[separadores.getSelectionModel().getSelectedIndex()]);
    }

    public BotaoVisual getBotaoRemover() {
        return botaoRemover;
    }

    public BotaoVisual getBotaoIniciarLoop() {
        return botaoIniciarLoop;
    }
}
