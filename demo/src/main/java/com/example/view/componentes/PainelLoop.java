package com.example.view.componentes;

import com.example.veiculo.Veiculo;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import java.util.ArrayList;
import java.util.List;

/**
 * O "tronco" do loop: junta as vagas (VagaView) dispostas em círculo, mais o anel de fundo
 * e o contador de voltas ao centro. Quem o usa só precisa de lhe dar a lista de vagas.
 */
public class PainelLoop extends Pane {

    private static final double LARGURA = 560;
    private static final double ALTURA = 440;
    private static final double RAIO_X = 210;
    private static final double RAIO_Y = 155;

    private final List<VagaView> vagas = new ArrayList<>();
    private final Label contadorVoltas = new Label("Volta 0");

    public PainelLoop(int numeroVagas) {
        setPrefSize(LARGURA, ALTURA);
        setMinSize(LARGURA, ALTURA);
        setMaxSize(LARGURA, ALTURA);

        double centroX = LARGURA / 2;
        double centroY = ALTURA / 2;

        Ellipse anel = new Ellipse(centroX, centroY, RAIO_X, RAIO_Y);
        anel.setFill(Color.TRANSPARENT);
        anel.setStroke(Color.web("#cfd8dc"));
        anel.setStrokeWidth(4);
        getChildren().add(anel);

        // A vaga 0 fica em cima e as seguintes seguem no sentido dos ponteiros do relógio
        for (int i = 0; i < numeroVagas; i++) {
            double angulo = Math.toRadians(-90 + i * 360.0 / numeroVagas);
            VagaView vaga = new VagaView();
            vaga.setLayoutX(centroX + RAIO_X * Math.cos(angulo) - VagaView.LARGURA / 2);
            vaga.setLayoutY(centroY + RAIO_Y * Math.sin(angulo) - VagaView.ALTURA / 2);
            vagas.add(vaga);
            getChildren().add(vaga);
        }

        contadorVoltas.setPrefWidth(160);
        contadorVoltas.setAlignment(Pos.CENTER);
        contadorVoltas.setLayoutX(centroX - 80);
        contadorVoltas.setLayoutY(centroY - 16);
        contadorVoltas.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #455a64;");
        getChildren().add(contadorVoltas);
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
