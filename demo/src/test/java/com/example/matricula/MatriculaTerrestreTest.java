package com.example.matricula;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MatriculaTerrestreTest {

    @Test
    void gerarDevolveFormatoCorreto() {
        // Formato esperado: FS-13-HD (2 letras - 2 dígitos - 2 letras)
        String matricula = new MatriculaTerrestre().gerar();
        assertTrue(matricula.matches("[A-Z]{2}-\\d{2}-[A-Z]{2}"),
                "Formato inválido: " + matricula);
    }
}
