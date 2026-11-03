package com.example.controller;

/**
 * Como os Controllers pedem para mudar de janela (cena) sem se conhecerem uns aos outros.
 * Quem a implementa (a classe App) é que sabe qual é a janela de cada destino.
 */
public interface Navegacao {

    /** Passa para a janela do loop (a garagem já tem de ter pelo menos 1 veículo). */
    void irParaLoop();

    /** Volta à janela onde se escolhem os veículos. */
    void irParaGaragem();
}
