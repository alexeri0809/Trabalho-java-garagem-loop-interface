package com.example.view.componentes;

import javafx.scene.Cursor;
import javafx.scene.control.Button;

/**
 * Botão da aplicação. Todos os botões usam esta classe, por isso as REGRAS DOS BOTÕES
 * (cores, cantos, letra) estão todas aqui, no bloco a seguir. Muda uma vez e muda em todo o lado.
 */
public class BotaoVisual extends Button {

    // ===================== REGRAS DOS BOTÕES: altera aqui =====================
    private static final String COR_PRINCIPAL = "#2e7d32";   // botão principal (verde)
    private static final String COR_SECUNDARIA = "#546e7a";  // botão secundário (cinza-azulado)
    private static final String COR_PERIGO = "#c62828";      // botão que remove/apaga (vermelho)
    private static final String COR_TEXTO = "#ffffff";       // cor da letra
    private static final int RAIO_CANTOS = 8;                // quanto mais alto, mais redondo
    private static final int TAMANHO_LETRA = 14;
    // ==========================================================================

    /** Os três aspetos possíveis de um botão. */
    public enum Estilo {
        PRINCIPAL, SECUNDARIO, PERIGO
    }

    public BotaoVisual(String texto, Estilo estilo) {
        super(texto);
        String base = "-fx-background-color: " + corDe(estilo) + ";"
                + "-fx-text-fill: " + COR_TEXTO + ";"
                + "-fx-font-size: " + TAMANHO_LETRA + "px;"
                + "-fx-font-weight: bold;"
                + "-fx-background-radius: " + RAIO_CANTOS + ";"
                + "-fx-padding: 9 20 9 20;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.30), 6, 0.1, 0, 2);";
        setStyle(base);
        setCursor(Cursor.HAND);
        // Rato por cima: mais claro. Rato carregado: o botão "afunda" um pouco.
        setOnMouseEntered(e -> setStyle(base + "-fx-opacity: 0.88;"));
        setOnMouseExited(e -> {
            setStyle(base);
            setTranslateY(0);
        });
        setOnMousePressed(e -> setTranslateY(1));
        setOnMouseReleased(e -> setTranslateY(0));
    }

    private static String corDe(Estilo estilo) {
        return switch (estilo) {
            case PRINCIPAL -> COR_PRINCIPAL;
            case SECUNDARIO -> COR_SECUNDARIA;
            case PERIGO -> COR_PERIGO;
        };
    }
}
