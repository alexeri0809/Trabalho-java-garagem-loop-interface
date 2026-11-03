package com.example.veiculo;

import com.example.matricula.MatriculaMaritima;

public class Barco extends Veiculo {
    public Barco(String marca, String modelo) {
        super(marca, modelo, new MatriculaMaritima());
    }

    @Override
    public TipoVeiculo getTipo() {
        return TipoVeiculo.BARCO;
    }
}
