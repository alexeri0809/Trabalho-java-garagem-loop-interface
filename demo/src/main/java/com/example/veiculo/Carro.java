package com.example.veiculo;

import com.example.matricula.MatriculaTerrestre;

public class Carro extends Veiculo {
    public Carro(String marca, String modelo) {
        super(marca, modelo, new MatriculaTerrestre());
    }

    @Override
    public TipoVeiculo getTipo() {
        return TipoVeiculo.CARRO;
    }
}
