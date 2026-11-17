package com.example.loop;

/**
 * Sentido em que o Vazio anda à volta de uma roda, visto no ecrã.
 * Quando o Vazio anda num sentido, os veículos recuam um lugar no sentido contrário.
 */
public enum Sentido {
    HORARIO("horário"),
    ANTI_HORARIO("anti-horário");

    private final String nome;

    Sentido(String nome) {
        this.nome = nome;
    }

    /** @return o nome para mostrar ao utilizador (por exemplo "anti-horário") */
    public String getNome() {
        return nome;
    }
}
