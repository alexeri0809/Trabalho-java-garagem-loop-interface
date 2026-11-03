package com.example.fabrica;

import com.example.veiculo.Mota;
import com.example.veiculo.Veiculo;

public class MotaFactory extends VeiculoFactory {
    @Override
    protected Veiculo criarVeiculo(String marca, String modelo) {
        return new Mota(marca, modelo);
    }
}
