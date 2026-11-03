package com.example.controller;

import com.example.facade.GaragemFacade;
import com.example.loop.GaragemLoop;
import com.example.veiculo.Veiculo;
import com.example.view.CenaLoopView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * CONTROLLER da janela 2. Dá um passo no loop (Model) a cada 0,7 segundos e manda a View
 * desenhar o resultado. O relógio é uma Timeline do JavaFX, que corre na thread da interface,
 * por isso a janela nunca fica bloqueada.
 */
public class LoopController {

    // ======================== VELOCIDADE DO LOOP: altera aqui (milissegundos por passo) ========================
    private static final double PAUSA_MS = 700;
    // ===========================================================================================================

    private final GaragemFacade modelo;
    private final CenaLoopView vista;
    private final Navegacao navegacao;
    private final Timeline relogio;
    private int voltas;

    public LoopController(GaragemFacade modelo, CenaLoopView vista, Navegacao navegacao) {
        this.modelo = modelo;
        this.vista = vista;
        this.navegacao = navegacao;

        relogio = new Timeline(new KeyFrame(Duration.millis(PAUSA_MS), e -> passo()));
        relogio.setCycleCount(Animation.INDEFINITE);

        vista.getBotaoPausa().setOnAction(e -> alternarPausa());
        vista.getBotaoVoltar().setOnAction(e -> {
            parar();
            navegacao.irParaGaragem();
        });
    }

    /** Arranca o loop a partir do estado atual do Model (o Model já tem de ter o loop iniciado). */
    public void iniciar() {
        voltas = 0;
        vista.mostrarVoltas(voltas);
        vista.mostrarMensagem("");
        vista.setEmPausa(false);
        vista.mostrarVagas(modelo.vagasLoop());
        relogio.playFromStart();
    }

    /** Para o relógio. Chama-se ao sair desta janela. */
    public void parar() {
        relogio.stop();
    }

    private void alternarPausa() {
        if (relogio.getStatus() == Animation.Status.RUNNING) {
            relogio.pause();
            vista.setEmPausa(true);
        } else {
            relogio.play();
            vista.setEmPausa(false);
        }
    }

    private void passo() {
        GaragemLoop.Passo passo = modelo.avancarLoop();
        vista.mostrarVagas(modelo.vagasLoop());
        if (passo.voltaCompleta) {
            voltas++;
            vista.mostrarVoltas(voltas);
            vista.mostrarMensagem("Volta " + voltas + " completa   |   Saiu: " + descrever(passo.veiculoQueSaiu)
                    + "   |   Entrou: " + descrever(passo.veiculoQueEntrou));
        }
    }

    private static String descrever(Veiculo veiculo) {
        return veiculo.getTipo().getNome() + " " + veiculo.getMatricula();
    }
}
