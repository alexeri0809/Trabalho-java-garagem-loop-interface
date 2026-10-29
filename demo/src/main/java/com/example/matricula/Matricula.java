package com.example.matricula;

import java.util.Random;

/**
 * Base comum das estratégias de matrícula (ferramentas para gerar letras e números).
 */
public abstract class Matricula implements EstrategiaMatricula {
    protected static final String LETRAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    protected static final Random random = new Random();

    protected String duasLetras() {
        char c1 = LETRAS.charAt(random.nextInt(LETRAS.length()));
        char c2 = LETRAS.charAt(random.nextInt(LETRAS.length()));
        return "" + c1 + c2;
    }

    protected String doisDigitos() {
        return String.format("%02d", random.nextInt(100)); // 00 a 99
    }
}
