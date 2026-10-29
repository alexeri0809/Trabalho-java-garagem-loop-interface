package com.example.fabrica;

import com.example.veiculo.Carro;
import com.example.veiculo.Veiculo;

public class CarroFactory extends VeiculoFactory {
    @Override
    protected Veiculo criarVeiculo(String marca, String modelo) {
        return new Carro(marca, modelo);
    }
}
