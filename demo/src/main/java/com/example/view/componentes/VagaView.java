package com.example.view.componentes;

import com.example.veiculo.Veiculo;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Uma vaga do loop: mostra um veículo (cor conforme o tipo) ou "Vazio".
 */
public class VagaView extends StackPane {

    public static final double LARGURA = 140;
    public static final double ALTURA = 76;

    // Cores de cada tipo de veículo
    private static final String COR_CARRO = "#1565c0";  // azul
    private static final String COR_BARCO = "#00838f";  // azul-esverdeado
    private static final String COR_MOTA = "#ef6c00";   // laranja

    private static final String ESTILO_VAZIO = "-fx-background-color: #eceff1; -fx-background-radius: 12;"
            + "-fx-border-color: #90a4ae; -fx-border-width: 2; -fx-border-style: dashed; -fx-border-radius: 12;";

    private final Label linhaTipo = new Label();
    private final Label linhaMatricula = new Label();
    private final Label linhaModelo = new Label();

    public VagaView() {
        setPrefSize(LARGURA, ALTURA);
        setMinSize(LARGURA, ALTURA);
        setMaxSize(LARGURA, ALTURA);

        linhaModelo.setMaxWidth(LARGURA - 12);
        VBox texto = new VBox(2, linhaTipo, linhaMatricula, linhaModelo);
        texto.setAlignment(Pos.CENTER);
        getChildren().add(texto);

        mostrar(null);
    }

    /** Mostra o veículo nesta vaga; com null mostra a vaga Vazio. */
    public void mostrar(Veiculo veiculo) {
        if (veiculo == null) {
            setStyle(ESTILO_VAZIO);
            linhaTipo.setText("Vazio");
            linhaTipo.setStyle("-fx-text-fill: #78909c; -fx-font-size: 16px; -fx-font-weight: bold;");
            linhaMatricula.setText("");
            linhaModelo.setText("");
            return;
        }
        setStyle("-fx-background-color: " + corDe(veiculo) + "; -fx-background-radius: 12;");
        linhaTipo.setText(veiculo.getTipo().getNome().toUpperCase());
        linhaTipo.setStyle("-fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold;");
        linhaMatricula.setText(veiculo.getMatricula());
        linhaMatricula.setStyle("-fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");
        linhaModelo.setText(veiculo.getMarca() + " " + veiculo.getModelo());
        linhaModelo.setStyle("-fx-text-fill: white; -fx-font-size: 11px;");
    }

    private static String corDe(Veiculo veiculo) {
        return switch (veiculo.getTipo()) {
            case CARRO -> COR_CARRO;
            case BARCO -> COR_BARCO;
            case MOTA -> COR_MOTA;
        };
    }
}
