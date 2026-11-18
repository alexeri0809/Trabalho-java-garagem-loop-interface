package com.example.view.componentes;

import com.example.loop.PosicaoRoda;
import com.example.veiculo.Veiculo;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * O "tronco" das três rodas: junta as três RodaView lado a lado (esquerda, central, direita)
 * e desenha as duas ligações entre elas, para se ver que formam um só conjunto.
 * Quem o usa só precisa de lhe dizer que vagas mostrar em cada roda.
 */
public class PainelLoop extends Pane {

    private static final double ESPACO_ENTRE_RODAS = 56;
    /** A ligação fica um pouco abaixo do meio da roda, onde não há nenhuma vaga. */
    private static final double DESCIDA_DA_LIGACAO = 25;

    private final Map<PosicaoRoda, RodaView> rodas = new EnumMap<>(PosicaoRoda.class);
    private final List<RotateTransition> engrenagens = new ArrayList<>();

    public PainelLoop(int numeroVagas) {
        double largura = 3 * RodaView.LARGURA + 2 * ESPACO_ENTRE_RODAS;
        setPrefSize(largura, RodaView.ALTURA);
        setMinSize(largura, RodaView.ALTURA);
        setMaxSize(largura, RodaView.ALTURA);

        double x = 0;
        for (PosicaoRoda posicao : PosicaoRoda.values()) {
            RodaView roda = new RodaView(posicao, numeroVagas);
            roda.setLayoutX(x);
            rodas.put(posicao, roda);
            getChildren().add(roda);
            x += RodaView.LARGURA + ESPACO_ENTRE_RODAS;
        }

        // Ligação entre cada roda e a seguinte: do anel de uma ao anel da outra, com um eixo no meio
        double y = RodaView.CENTRO_Y + DESCIDA_DA_LIGACAO;
        double meiaLargura = RodaView.RAIO_X * Math.sqrt(1 - Math.pow(DESCIDA_DA_LIGACAO / RodaView.RAIO_Y, 2));
        for (int i = 0; i < 2; i++) {
            double inicioDaRoda = i * (RodaView.LARGURA + ESPACO_ENTRE_RODAS);
            double deX = inicioDaRoda + RodaView.CENTRO_X + meiaLargura;
            double ateX = inicioDaRoda + RodaView.LARGURA + ESPACO_ENTRE_RODAS + RodaView.CENTRO_X - meiaLargura;

            Line ligacao = new Line(deX, y, ateX, y);
            ligacao.setStroke(Color.web("#90a4ae"));
            ligacao.setStrokeWidth(6);

            Circle eixo = new Circle(16);
            eixo.setFill(Color.web("#546e7a"));
            eixo.setStroke(Color.WHITE);
            eixo.setStrokeWidth(3);
            // Engrenagem que roda; as duas rodam em sentidos contrários, como engrenagens ligadas
            Label engrenagem = new Label("\u2699");
            engrenagem.setStyle("-fx-font-size: 22px; -fx-text-fill: white;");
            StackPane cubo = new StackPane(eixo, engrenagem);
            cubo.setLayoutX((deX + ateX) / 2 - 16);
            cubo.setLayoutY(y - 16);
            RotateTransition giro = new RotateTransition(Duration.seconds(5), engrenagem);
            giro.setByAngle(i == 0 ? 360 : -360);
            giro.setCycleCount(Animation.INDEFINITE);
            giro.setInterpolator(Interpolator.LINEAR);
            engrenagens.add(giro);

            getChildren().addAll(ligacao, cubo);
        }
    }

    /** Liga ou desliga o giro das engrenagens (acompanha o Pausar/Retomar). */
    public void setAGirar(boolean agirar) {
        for (RotateTransition giro : engrenagens) {
            if (agirar) {
                giro.play();
            } else {
                giro.pause();
            }
        }
    }

    /** Faz brilhar o veículo na roda indicada. */
    public void destacar(PosicaoRoda roda, Veiculo veiculo, Color cor) {
        rodas.get(roda).destacar(veiculo, cor);
    }

    /** Mostra o estado das vagas de uma roda pela ordem do círculo (null = Vazio). */
    public void mostrar(PosicaoRoda roda, List<Veiculo> estado) {
        rodas.get(roda).mostrar(estado);
    }

    public void mostrarVoltas(PosicaoRoda roda, int voltas) {
        rodas.get(roda).mostrarVoltas(voltas);
    }
}
