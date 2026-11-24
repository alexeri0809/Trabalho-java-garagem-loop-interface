package com.example.view.componentes;

import com.example.veiculo.TipoVeiculo;
import com.example.veiculo.Veiculo;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Uma vaga do loop: mostra um veículo (cor e ícone conforme o tipo) ou "Vazio".
 * Quando o conteúdo muda, a vaga faz um pequeno fade para o movimento se notar melhor.
 */
public class VagaView extends StackPane {

    public static final double LARGURA = 120;
    public static final double ALTURA = 70;

    // ===================== CORES DOS VEÍCULOS: altera aqui (cor clara -> cor escura do degradê) =====================
    private static final String CARRO_CLARO = "#1e88e5", CARRO_ESCURO = "#0d47a1";   // azul
    private static final String BARCO_CLARO = "#00acc1", BARCO_ESCURO = "#006064";   // azul-esverdeado
    private static final String MOTA_CLARO = "#fb8c00", MOTA_ESCURO = "#e65100";     // laranja
    // ================================================================================================================

    private static final String SOMBRA = "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.28), 8, 0.1, 0, 3);";
    private static final String ESTILO_VAZIO = "-fx-background-color: rgba(255,255,255,0.55); -fx-background-radius: 12;"
            + "-fx-border-color: #90a4ae; -fx-border-width: 2; -fx-border-style: dashed; -fx-border-radius: 12;";

    private final ImageView foto = new ImageView();
    private final Label linhaTipo = new Label();
    private final Label linhaMatricula = new Label();
    private final Label linhaModelo = new Label();
    private final Rectangle aro = new Rectangle(LARGURA, ALTURA);
    private Veiculo ultimo;

    public VagaView() {
        setPrefSize(LARGURA, ALTURA);
        setMinSize(LARGURA, ALTURA);
        setMaxSize(LARGURA, ALTURA);

        linhaMatricula.setMaxWidth(LARGURA - 14);
        linhaModelo.setMaxWidth(LARGURA - 8);
        VBox texto = new VBox(1, foto, linhaTipo, linhaMatricula, linhaModelo);
        texto.setAlignment(Pos.CENTER);
        getChildren().add(texto);

        // Aro de destaque (invisível até haver um "flash")
        aro.setArcWidth(24);
        aro.setArcHeight(24);
        aro.setFill(Color.TRANSPARENT);
        aro.setStrokeWidth(4);
        aro.setOpacity(0);
        aro.setMouseTransparent(true);
        getChildren().add(aro);

        aplicar(null);
    }

    /** Mostra o veículo nesta vaga; com null mostra a vaga Vazio. */
    public void mostrar(Veiculo veiculo) {
        if (veiculo != ultimo) {
            aplicar(veiculo);
            FadeTransition fade = new FadeTransition(Duration.millis(260), this);
            fade.setFromValue(0.35);
            fade.setToValue(1.0);
            fade.play();
        }
    }

    /** Mostra o veículo já, sem fade (usado quando ele chega a deslizar). */
    public void mostrarSemAnimacao(Veiculo veiculo) {
        aplicar(veiculo);
    }

    /** @return o veículo que está agora nesta vaga (null = Vazio) */
    public Veiculo getVeiculo() {
        return ultimo;
    }

    /** Faz um aro de cor acender-se à volta da vaga e apagar-se devagar (1,2 s). */
    public void destacar(Color cor) {
        aro.setStroke(cor);
        FadeTransition fade = new FadeTransition(Duration.millis(1200), aro);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.play();
    }

    private void aplicar(Veiculo veiculo) {
        ultimo = veiculo;
        if (veiculo == null) {
            setStyle(ESTILO_VAZIO);
            foto.setImage(null);
            foto.setVisible(false);
            foto.setManaged(false);
            linhaTipo.setVisible(true);
            linhaTipo.setManaged(true);
            linhaTipo.setText("Vazio");
            linhaTipo.setStyle("-fx-text-fill: #78909c; -fx-font-size: 13px; -fx-font-weight: bold;");
            linhaMatricula.setText("");
            linhaMatricula.setStyle("");
            linhaModelo.setText("");
            return;
        }
        setStyle("-fx-background-color: linear-gradient(to bottom, " + corClara(veiculo.getTipo()) + ", "
                + corEscura(veiculo.getTipo()) + "); -fx-background-radius: 12; " + SOMBRA);
        // Com foto: mostra a foto em cima; sem foto: mostra o ícone e o tipo em texto
        javafx.scene.image.Image imagem = ImagensVeiculo.de(veiculo);
        foto.setImage(imagem);
        foto.setFitWidth(60);
        foto.setFitHeight(30);
        foto.setPreserveRatio(true);
        foto.setVisible(imagem != null);
        foto.setManaged(imagem != null);
        linhaTipo.setVisible(imagem == null);
        linhaTipo.setManaged(imagem == null);
        linhaTipo.setText(icone(veiculo.getTipo()) + " " + veiculo.getTipo().getNome().toUpperCase());
        linhaTipo.setStyle("-fx-text-fill: white; -fx-font-size: 10px; -fx-font-weight: bold;");
        linhaMatricula.setText(veiculo.getMatricula());
        // A matrícula aparece numa "chapa" branca
        linhaMatricula.setStyle("-fx-background-color: white; -fx-background-radius: 4; -fx-padding: 0 6 0 6;"
                + "-fx-text-fill: #263238; -fx-font-size: 11px; -fx-font-weight: bold;");
        linhaModelo.setText(veiculo.getMarca() + " " + veiculo.getModelo());
        linhaModelo.setStyle("-fx-text-fill: white; -fx-font-size: 9px;");
    }

    /** Ícone do tipo (se o teu sistema não mostrar os emojis, devolve "" em cada caso). */
    public static String icone(TipoVeiculo tipo) {
        return switch (tipo) {
            case CARRO -> "\uD83D\uDE97";
            case BARCO -> "\u26F5";
            case MOTA -> "\uD83C\uDFCD";
        };
    }

    /** Cor principal do tipo (usada também nas listas e na legenda). */
    public static String corClara(TipoVeiculo tipo) {
        return switch (tipo) {
            case CARRO -> CARRO_CLARO;
            case BARCO -> BARCO_CLARO;
            case MOTA -> MOTA_CLARO;
        };
    }

    public static String corEscura(TipoVeiculo tipo) {
        return switch (tipo) {
            case CARRO -> CARRO_ESCURO;
            case BARCO -> BARCO_ESCURO;
            case MOTA -> MOTA_ESCURO;
        };
    }
}
