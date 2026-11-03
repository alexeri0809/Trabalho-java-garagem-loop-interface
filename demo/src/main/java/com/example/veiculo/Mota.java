package com.example.veiculo;

import com.example.matricula.MatriculaTerrestre;

public class Mota extends Veiculo {
    public Mota(String marca, String modelo) {
        super(marca, modelo, new MatriculaTerrestre());
    }

    @Override
    public TipoVeiculo getTipo() {
        return TipoVeiculo.MOTA;
    }
}
