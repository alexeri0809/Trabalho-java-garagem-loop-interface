package com.example.matricula;

/**
 * Padrão STRATEGY: cada tipo de veículo usa uma estratégia diferente
 * para gerar a sua matrícula.
 */
public interface EstrategiaMatricula {
    String gerar();
}
