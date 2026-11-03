package com.example.view;

import com.example.view.componentes.BotaoVisual;
import com.example.view.componentes.PainelLoop;
import com.example.veiculo.Veiculo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * CENA 2 (janela 2): a garagem a rodar em loop.
 * É o "tronco" desta janela: junta o painel circular, a mensagem de fim de volta e os botões.
 * Não conhece o Model: só mostra o que o Controller lhe manda mostrar.
 */
public class CenaLoopView extends BorderPane {

    // ======================== TAMANHO E FUNDO DA JANELA: altera aqui ========================
    private static final double LARGURA_JANELA = 940;
    private static final double ALTURA_JANELA = 640;
    private static final String COR_FUNDO = "#eceff1";
    // =========================================================================================

    private static final int NUMERO_DE_VAGAS = 5;

    private final PainelLoop painel = new PainelLoop(NUMERO_DE_VAGAS);
    private final Label mensagem = new Label();
    private final BotaoVisual botaoPausa = new BotaoVisual("Pausar", BotaoVisual.Estilo.SECUNDARIO);
    private final BotaoVisual botaoVoltar = new BotaoVisual("◀  Voltar à garagem", BotaoVisual.Estilo.PRINCIPAL);
    private final Scene cena;

    public CenaLoopView() {
        setPadding(new Insets(20));
        setStyle("-fx-background-color: " + COR_FUNDO + ";");

        Label titulo = new Label("Garagem em loop");
        titulo.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #263238;");
        Label subtitulo = new Label("O Vazio roda sem parar. No fim de cada volta sai o veículo mais antigo "
                + "e entra um novo do mesmo tipo.");
        subtitulo.setStyle("-fx-text-fill: #607d8b;");
        VBox cabecalho = new VBox(2, titulo, subtitulo);

        mensagem.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #455a64;");
        mensagem.setMinHeight(24);

        HBox barraBotoes = new HBox(10, botaoPausa, botaoVoltar);
        barraBotoes.setAlignment(Pos.CENTER);

        VBox rodape = new VBox(12, mensagem, barraBotoes);
        rodape.setAlignment(Pos.CENTER);

        setTop(cabecalho);
        setCenter(painel);
        setBottom(rodape);
        BorderPane.setAlignment(painel, Pos.CENTER);

        cena = new Scene(this, LARGURA_JANELA, ALTURA_JANELA);
    }

    public Scene getCena() {
        return cena;
    }

    public BotaoVisual getBotaoPausa() {
        return botaoPausa;
    }

    public BotaoVisual getBotaoVoltar() {
        return botaoVoltar;
    }

    /** Mostra as vagas do loop pela ordem do círculo (null = Vazio). */
    public void mostrarVagas(List<Veiculo> vagas) {
        painel.mostrar(vagas);
    }

    public void mostrarVoltas(int voltas) {
        painel.mostrarVoltas(voltas);
    }

    public void mostrarMensagem(String texto) {
        mensagem.setText(texto);
    }

    /** Muda o texto do botão entre "Pausar" e "Retomar". */
    public void setEmPausa(boolean emPausa) {
        botaoPausa.setText(emPausa ? "Retomar" : "Pausar");
    }
}
