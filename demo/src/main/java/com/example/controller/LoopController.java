package com.example.controller;

import com.example.facade.GaragemFacade;
import com.example.loop.ConjuntoRodas;
import com.example.loop.GaragemLoop;
import com.example.loop.PosicaoRoda;
import com.example.veiculo.Veiculo;
import com.example.view.CenaLoopView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * CONTROLLER da janela 2. Dá um passo no conjunto das três rodas (Model) a cada 0,7 segundos e manda
 * a View desenhar o resultado. Há um só relógio para as três rodas, e é isso que as mantém sincronizadas.
 * O relógio é uma Timeline do JavaFX, que corre na thread da interface, por isso a janela nunca fica bloqueada.
 */
public class LoopController {

    // ======================== VELOCIDADE DO LOOP: altera aqui (milissegundos por passo) ========================
    private static final double PAUSA_MS = 700;
    // ===========================================================================================================

    private final GaragemFacade modelo;
    private final CenaLoopView vista;
    private final Navegacao navegacao;
    private final Timeline relogio;
    private final Map<PosicaoRoda, Integer> voltas = new EnumMap<>(PosicaoRoda.class);

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
        vista.mostrarMensagem("");
        vista.setEmPausa(false);
        for (PosicaoRoda roda : PosicaoRoda.values()) {
            voltas.put(roda, 0);
            vista.mostrarVoltas(roda, 0);
            vista.mostrarVagas(roda, modelo.vagasLoop(roda));
        }
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
        Map<PosicaoRoda, GaragemLoop.Passo> passos = modelo.avancarRodas();
        List<String> fimsDeVolta = new ArrayList<>();
        for (PosicaoRoda roda : PosicaoRoda.values()) {
            GaragemLoop.Passo passo = passos.get(roda);
            vista.mostrarVagas(roda, modelo.vagasLoop(roda));
            if (passo.voltaCompleta) {
                int numero = voltas.get(roda) + 1;
                voltas.put(roda, numero);
                vista.mostrarVoltas(roda, numero);
                fimsDeVolta.add(roda.getNome() + ", volta " + numero + "   |   Saiu: "
                        + descrever(passo.veiculoQueSaiu) + "   |   Entrou: " + descrever(passo.veiculoQueEntrou));
            }
        }
        for (ConjuntoRodas.Troca troca : modelo.getUltimasTrocas()) {
            fimsDeVolta.add("Troca: " + troca.lateral().getNome() + " ↔ Roda central   |   "
                    + descrever(troca.daLateral()) + " ↔ " + descrever(troca.doCentro()));
        }
        if (!fimsDeVolta.isEmpty()) {
            vista.mostrarMensagem(String.join("\n", fimsDeVolta));
        }
    }

    private static String descrever(Veiculo veiculo) {
        return veiculo.getTipo().getNome() + " " + veiculo.getMatricula();
    }
}
