package com.example.matricula;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MatriculaMaritimaTest {

    @Test
    void gerarDevolveFormatoCorreto() {
        // Formato esperado: 6º-CO-7-15-21
        String matricula = new MatriculaMaritima().gerar();
        assertTrue(matricula.matches("[1-9]º-[A-Z]{2}-[1-9]-\\d{2}-\\d{2}"),
                "Formato inválido: " + matricula);
    }
}
