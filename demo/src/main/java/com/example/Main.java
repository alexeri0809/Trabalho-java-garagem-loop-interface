package com.example;

import javafx.application.Application;

/**
 * Ponto de entrada do programa: abre a janela JavaFX (classe {@link App}).
 * Fica separado da App porque o JavaFX, ao arrancar a partir de um jar ou de uma classe
 * que já é uma Application, exige módulos; assim funciona em todos os casos.
 */
public class Main {
    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
}
