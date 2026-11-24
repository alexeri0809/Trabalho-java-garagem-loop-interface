package com.example.view.componentes;

import com.example.loop.PosicaoRoda;
import com.example.loop.Sentido;
import com.example.veiculo.Veiculo;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.effect.DropShadow;

import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * Uma roda desenhada: o anel, as suas vagas (VagaView) em círculo, o contador de voltas ao centro
 * e o nome com o sentido por baixo. O sentido vem do Model ({@link PosicaoRoda#getSentido()}):
 * numa roda de sentido anti-horário as vagas são dispostas ao contrário, e assim o Vazio, que
 * avança de uma vaga para a seguinte, anda no sentido anti-horário no ecrã.
 */
public class RodaView extends Pane {

    public static final double LARGURA = 340;
    public static final double ALTURA = 340;
    public static final double CENTRO_X = LARGURA / 2;
    public static final double CENTRO_Y = 160;
    public static final double RAIO_X = 118;
    public static final double RAIO_Y = 118;

    private final List<VagaView> vagas = new ArrayList<>();
    private final Label contadorVoltas = new Label("Volta 0");

    public RodaView(PosicaoRoda posicao, int numeroVagas) {
        setPrefSize(LARGURA, ALTURA);
        setMinSize(LARGURA, ALTURA);
        setMaxSize(LARGURA, ALTURA);

        Ellipse anel = new Ellipse(CENTRO_X, CENTRO_Y, RAIO_X, RAIO_Y);
        anel.setFill(Color.web("#ffffff", 0.45));
        anel.setStroke(Color.web("#90a4ae"));
        anel.setStrokeWidth(5);
        anel.setEffect(new DropShadow(10, 0, 3, Color.web("#000000", 0.2)));
        getChildren().add(anel);

        // A vaga 0 fica em cima. Em sentido horário as seguintes vão para a direita; em anti-horário, para a esquerda.
        double sinal = posicao.getSentido() == Sentido.HORARIO ? 1 : -1;
        for (int i = 0; i < numeroVagas; i++) {
            double angulo = Math.toRadians(-90 + sinal * i * 360.0 / numeroVagas);
            VagaView vaga = new VagaView();
            vaga.setLayoutX(CENTRO_X + RAIO_X * Math.cos(angulo) - VagaView.LARGURA / 2);
            vaga.setLayoutY(CENTRO_Y + RAIO_Y * Math.sin(angulo) - VagaView.ALTURA / 2);
            vagas.add(vaga);
            getChildren().add(vaga);
        }

        contadorVoltas.setPrefWidth(90);
        contadorVoltas.setAlignment(Pos.CENTER);
        contadorVoltas.setLayoutX(CENTRO_X - 45);
        contadorVoltas.setLayoutY(CENTRO_Y - 4);
        contadorVoltas.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: white;"
                + "-fx-background-color: #37474f; -fx-background-radius: 14; -fx-padding: 4 0 4 0;");
        getChildren().add(contadorVoltas);

        // Seta grande no centro a indicar o sentido do Vazio
        Label seta0 = new Label(posicao.getSentido() == Sentido.HORARIO ? "\u21BB" : "\u21BA");
        seta0.setPrefWidth(60);
        seta0.setAlignment(Pos.CENTER);
        seta0.setLayoutX(CENTRO_X - 30);
        seta0.setLayoutY(CENTRO_Y - 52);
        seta0.setStyle("-fx-font-size: 38px; -fx-text-fill: #78909c;");
        getChildren().add(seta0);

        // ↻ = seta no sentido horário, ↺ = seta no sentido anti-horário
        String seta = posicao.getSentido() == Sentido.HORARIO ? "↻" : "↺";
        Label nome = new Label(posicao.getNome());
        nome.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1c313a;");
        Label sentido = new Label(seta + "  Vazio no sentido " + posicao.getSentido().getNome());
        sentido.setStyle("-fx-font-size: 12px; -fx-text-fill: #607d8b;");
        VBox legenda = new VBox(2, nome, sentido);
        legenda.setAlignment(Pos.CENTER);
        legenda.setPrefWidth(LARGURA);
        legenda.setLayoutY(ALTURA - 38);
        getChildren().add(legenda);
    }

    /** Mostra o estado das vagas pela ordem do círculo (null = Vazio). */
    public void mostrar(List<Veiculo> estado) {
        List<Veiculo> antes = new ArrayList<>();
        for (VagaView vaga : vagas) {
            antes.add(vaga.getVeiculo());
        }
        for (int j = 0; j < vagas.size() && j < estado.size(); j++) {
            Veiculo novo = estado.get(j);
            int i = indiceDe(antes, novo);
            if (novo != null && i >= 0 && i != j) {
                // O veículo já estava na roda mas noutra vaga: desliza de lá até cá
                vagas.get(j).mostrarSemAnimacao(novo);
                vagas.get(j).setOpacity(0);
                deslizar(novo, vagas.get(i), vagas.get(j));
            } else {
                vagas.get(j).mostrar(novo);
            }
        }
    }

    private static int indiceDe(List<Veiculo> lista, Veiculo veiculo) {
        for (int i = 0; i < lista.size(); i++) {
            if (veiculo != null && lista.get(i) == veiculo) {
                return i;
            }
        }
        return -1;
    }

    /** Anima uma cópia do veículo da vaga de origem até à de destino (0,45 s, menos que um passo do loop). */
    private void deslizar(Veiculo veiculo, VagaView de, VagaView para) {
        VagaView fantasma = new VagaView();
        fantasma.mostrarSemAnimacao(veiculo);
        fantasma.setLayoutX(de.getLayoutX());
        fantasma.setLayoutY(de.getLayoutY());
        fantasma.setMouseTransparent(true);
        getChildren().add(fantasma);
        TranslateTransition viagem = new TranslateTransition(Duration.millis(450), fantasma);
        viagem.setToX(para.getLayoutX() - de.getLayoutX());
        viagem.setToY(para.getLayoutY() - de.getLayoutY());
        viagem.setInterpolator(Interpolator.EASE_BOTH);
        viagem.setOnFinished(e -> {
            getChildren().remove(fantasma);
            para.setOpacity(1);
        });
        viagem.play();
    }

    /** Faz brilhar a vaga onde está este veículo (se estiver nesta roda). */
    public void destacar(Veiculo veiculo, Color cor) {
        for (VagaView vaga : vagas) {
            if (vaga.getVeiculo() == veiculo) {
                vaga.destacar(cor);
            }
        }
    }

    public void mostrarVoltas(int voltas) {
        contadorVoltas.setText("Volta " + voltas);
        if (voltas > 0) {
            ScaleTransition pulso = new ScaleTransition(Duration.millis(350), contadorVoltas);
            pulso.setFromX(1.5);
            pulso.setFromY(1.5);
            pulso.setToX(1.0);
            pulso.setToY(1.0);
            pulso.play();
        }
    }
}
