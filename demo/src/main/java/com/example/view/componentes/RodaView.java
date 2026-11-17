package com.example.view.componentes;

import com.example.loop.PosicaoRoda;
import com.example.loop.Sentido;
import com.example.veiculo.Veiculo;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import java.util.ArrayList;
import java.util.List;

/**
 * Uma roda desenhada: o anel, as suas vagas (VagaView) em círculo, o contador de voltas ao centro
 * e o nome com o sentido por baixo. O sentido vem do Model ({@link PosicaoRoda#getSentido()}):
 * numa roda de sentido anti-horário as vagas são dispostas ao contrário, e assim o Vazio, que
 * avança de uma vaga para a seguinte, anda no sentido anti-horário no ecrã.
 */
public class RodaView extends Pane {

    public static final double LARGURA = 310;
    public static final double ALTURA = 300;
    public static final double CENTRO_X = LARGURA / 2;
    public static final double CENTRO_Y = 140;
    public static final double RAIO_X = 104;
    public static final double RAIO_Y = 110;

    private final List<VagaView> vagas = new ArrayList<>();
    private final Label contadorVoltas = new Label("Volta 0");

    public RodaView(PosicaoRoda posicao, int numeroVagas) {
        setPrefSize(LARGURA, ALTURA);
        setMinSize(LARGURA, ALTURA);
        setMaxSize(LARGURA, ALTURA);

        Ellipse anel = new Ellipse(CENTRO_X, CENTRO_Y, RAIO_X, RAIO_Y);
        anel.setFill(Color.TRANSPARENT);
        anel.setStroke(Color.web("#cfd8dc"));
        anel.setStrokeWidth(4);
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
        contadorVoltas.setLayoutY(CENTRO_Y - 14);
        contadorVoltas.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #455a64;");
        getChildren().add(contadorVoltas);

        // ↻ = seta no sentido horário, ↺ = seta no sentido anti-horário
        String seta = posicao.getSentido() == Sentido.HORARIO ? "↻" : "↺";
        Label nome = new Label(posicao.getNome());
        nome.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #263238;");
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
        for (int i = 0; i < vagas.size() && i < estado.size(); i++) {
            vagas.get(i).mostrar(estado.get(i));
        }
    }

    public void mostrarVoltas(int voltas) {
        contadorVoltas.setText("Volta " + voltas);
    }
}
