package com.example.fabrica;

import com.example.veiculo.Veiculo;

/**
 * Padrão FACTORY METHOD (criador): as subclasses decidem que tipo de
 * veículo é criado através do método criarVeiculo().
 */
public abstract class VeiculoFactory {

    public Veiculo criar(String marca, String modelo) {
        return criarVeiculo(marca.trim(), modelo.trim());
    }

    protected abstract Veiculo criarVeiculo(String marca, String modelo);
}
