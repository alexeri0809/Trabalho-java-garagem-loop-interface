package com.example.fabrica;

import com.example.veiculo.Barco;
import com.example.veiculo.Veiculo;

/**
 * Fábrica concreta (padrão Factory Method) que cria instâncias de {@link Barco}.
 */
public class BarcoFactory extends VeiculoFactory {

    /**
     * {@inheritDoc}
     *
     * @return um novo {@link Barco}
     */
    @Override
    protected Veiculo criarVeiculo(String marca, String modelo) {
        return new Barco(marca, modelo);
    }
}
