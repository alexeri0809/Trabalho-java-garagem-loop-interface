package com.example.veiculo;

/**
 * Padrão COMPOSITE (componente): tanto um Veiculo (folha) como um
 * GrupoVeiculos ou a Garagem (compostos) são tratados da mesma forma.
 */
public interface ElementoGaragem {
    int contarVeiculos();

    void mostrar(String indentacao);
}
