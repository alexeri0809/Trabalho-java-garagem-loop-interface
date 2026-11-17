package com.example.loop;

/**
 * As três rodas do conjunto, da esquerda para a direita, e o sentido em que o Vazio roda em cada uma.
 * É aqui que se definem as regras do sentido: para trocar o de uma roda, muda-se só a linha dela.
 */
public enum PosicaoRoda {

    // ======================== SENTIDO DE CADA RODA: altera aqui ========================
    ESQUERDA("Roda da esquerda", Sentido.HORARIO),
    CENTRAL("Roda central", Sentido.HORARIO),
    DIREITA("Roda da direita", Sentido.ANTI_HORARIO);
    // ===================================================================================

    private final String nome;
    private final Sentido sentido;

    PosicaoRoda(String nome, Sentido sentido) {
        this.nome = nome;
        this.sentido = sentido;
    }

    public String getNome() {
        return nome;
    }

    /** @return o sentido em que o Vazio anda nesta roda */
    public Sentido getSentido() {
        return sentido;
    }
}
