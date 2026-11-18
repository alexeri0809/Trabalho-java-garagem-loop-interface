package com.example.view;

import com.example.loop.PosicaoRoda;
import com.example.veiculo.TipoVeiculo;
import com.example.veiculo.Veiculo;
import com.example.view.componentes.BotaoVisual;
import com.example.view.componentes.PainelLoop;
import com.example.view.componentes.VagaView;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.util.List;

/**
 * CENA 2 (janela 2): as três rodas a rodar em loop.
 * É o "tronco" desta janela: junta o painel das três rodas, a mensagem de fim de volta e os botões.
 * Não conhece o Model: só mostra o que o Controller lhe manda mostrar.
 */
public class CenaLoopView extends BorderPane {

    // ======================== TAMANHO E FUNDO DA JANELA: altera aqui ========================
    private static final double LARGURA_JANELA = 1140;
    private static final double ALTURA_JANELA = 660;
    private static final String COR_FUNDO = "linear-gradient(to bottom right, #eceff1, #b0bec5)";
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
        titulo.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #1c313a;");
        Label subtitulo = new Label("As três rodas andam ao mesmo tempo. No fim de cada volta, em cada roda, "
                + "sai o veículo mais antigo e entra um novo do mesmo tipo.");
        subtitulo.setStyle("-fx-text-fill: #607d8b;");
        HBox legenda = new HBox(8);
        legenda.setPadding(new Insets(8, 0, 0, 0));
        for (TipoVeiculo tipo : TipoVeiculo.values()) {
            Label chip = new Label(VagaView.icone(tipo) + " " + tipo.getNome());
            chip.setStyle("-fx-background-color: linear-gradient(to bottom, " + VagaView.corClara(tipo) + ", "
                    + VagaView.corEscura(tipo) + "); -fx-background-radius: 12; -fx-text-fill: white;"
                    + "-fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 3 12 3 12;");
            legenda.getChildren().add(chip);
        }
        Label vazio = new Label("Vazio");
        vazio.setStyle("-fx-border-color: #78909c; -fx-border-style: dashed; -fx-border-radius: 12;"
                + "-fx-text-fill: #546e7a; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 2 12 2 12;");
        legenda.getChildren().add(vazio);
        VBox cabecalho = new VBox(2, titulo, subtitulo, legenda);

        // A mensagem de fim de volta aparece num cartão branco (invisível enquanto não há mensagem)
        mensagem.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #37474f;"
                + "-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 8 18 8 18;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0.1, 0, 2);");
        mensagem.setOpacity(0);
        mensagem.setMaxWidth(980);
        mensagem.setMinHeight(52);
        mensagem.setWrapText(true);
        mensagem.setAlignment(Pos.CENTER);
        mensagem.setTextAlignment(TextAlignment.CENTER);

        HBox barraBotoes = new HBox(10, botaoPausa, botaoVoltar);
        barraBotoes.setAlignment(Pos.CENTER);

        VBox rodape = new VBox(12, mensagem, barraBotoes);
        rodape.setAlignment(Pos.CENTER);

        setTop(cabecalho);
        // As rodas ajustam-se ao tamanho da janela (nunca ficam maiores que 1,5x nem desaparecem)
        StackPane zona = new StackPane(painel);
        zona.setMinSize(0, 0);
        var escala = Bindings.createDoubleBinding(
                () -> Math.max(0.3, Math.min(1.5, Math.min(zona.getWidth() / painel.getPrefWidth(),
                        zona.getHeight() / painel.getPrefHeight()))),
                zona.widthProperty(), zona.heightProperty());
        painel.scaleXProperty().bind(escala);
        painel.scaleYProperty().bind(escala);
        setCenter(zona);
        setBottom(rodape);

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

    /** Mostra as vagas de uma roda pela ordem do círculo (null = Vazio). */
    public void mostrarVagas(PosicaoRoda roda, List<Veiculo> vagas) {
        painel.mostrar(roda, vagas);
    }

    public void mostrarVoltas(PosicaoRoda roda, int voltas) {
        painel.mostrarVoltas(roda, voltas);
    }

    /** Faz brilhar um veículo: verde se acabou de entrar, dourado se acabou de ser trocado entre rodas. */
    public void destacar(PosicaoRoda roda, Veiculo veiculo, boolean troca) {
        painel.destacar(roda, veiculo, Color.web(troca ? "#ffb300" : "#43a047"));
    }

    public void mostrarMensagem(String texto) {
        mensagem.setText(texto);
        mensagem.setOpacity(texto == null || texto.isEmpty() ? 0 : 1);
    }

    /** Muda o texto do botão entre "Pausar" e "Retomar". */
    public void setEmPausa(boolean emPausa) {
        botaoPausa.setText(emPausa ? "Retomar" : "Pausar");
        painel.setAGirar(!emPausa);
    }
}
