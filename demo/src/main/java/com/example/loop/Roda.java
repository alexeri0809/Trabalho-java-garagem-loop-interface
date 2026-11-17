package com.example.loop;

import com.example.fabrica.VeiculoFactory;
import com.example.veiculo.TipoVeiculo;
import com.example.veiculo.Veiculo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Uma roda do conjunto: 5 vagas em círculo com os seus veículos.
 * Por dentro usa o {@link GaragemLoop}, que continua exatamente igual. Uma roda sem veículos
 * é permitida (fica parada, com as 5 vagas Vazio), coisa que o GaragemLoop sozinho não aceita.
 */
public class Roda {

    private final PosicaoRoda posicao;
    private final GaragemLoop loop; // null se a roda não tiver veículos

    /**
     * @param posicao  qual das três rodas é
     * @param veiculos os veículos desta roda, do mais antigo ao mais recente (0 a {@code VAGAS - 1})
     * @param fabricas as fábricas usadas para criar o veículo novo no fim de cada volta
     * @throws IllegalArgumentException se houver mais veículos do que {@code VAGAS - 1}
     */
    public Roda(PosicaoRoda posicao, List<Veiculo> veiculos, Map<TipoVeiculo, VeiculoFactory> fabricas) {
        this.posicao = posicao;
        this.loop = veiculos.isEmpty() ? null : new GaragemLoop(veiculos, fabricas);
    }

    public PosicaoRoda getPosicao() {
        return posicao;
    }

    /** @return true se a roda tiver pelo menos 1 veículo (e por isso rode) */
    public boolean temVeiculos() {
        return loop != null;
    }

    /**
     * Dá um passo. Numa roda sem veículos nada se mexe e o passo devolvido nunca marca fim de volta.
     */
    public GaragemLoop.Passo avancar() {
        if (loop == null) {
            return new GaragemLoop.Passo(estado(), false, null, null);
        }
        return loop.avancar();
    }

    /** @return as 5 vagas pela ordem do círculo; null = Vazio (cópia, pode ser alterada à vontade) */
    public List<Veiculo> getVagas() {
        if (loop == null) {
            return new ArrayList<>(Collections.<Veiculo>nCopies(GaragemLoop.VAGAS, null));
        }
        return loop.getVagas();
    }

    /** @return as 5 vagas em texto, por exemplo "[Carro AA-12-BB] [Vazio] ..." */
    public String estado() {
        if (loop == null) {
            return String.join(" ", Collections.nCopies(GaragemLoop.VAGAS, "[Vazio]"));
        }
        return loop.estado();
    }
}
