package com.example.matricula;

/** Estratégia para Carro e Mota. Formato: FS-13-HD */
public class MatriculaTerrestre extends Matricula {

    @Override
    public String gerar() {
        return duasLetras() + "-" + doisDigitos() + "-" + duasLetras();
    }
}
