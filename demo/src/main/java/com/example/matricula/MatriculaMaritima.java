package com.example.matricula;

/** Estratégia para Barco. Formato: 6º-CO-7-15-21 */
public class MatriculaMaritima extends Matricula {

    @Override
    public String gerar() {
        int distrito = random.nextInt(9) + 1;      // 1 a 9
        int letraChamada = random.nextInt(9) + 1;  // 1 a 9
        return distrito + "º-" + duasLetras() + "-" + letraChamada
                + "-" + doisDigitos() + "-" + doisDigitos();
    }
}
