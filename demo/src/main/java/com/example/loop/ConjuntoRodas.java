package com.example.loop;

import com.example.fabrica.VeiculoFactory;
import com.example.veiculo.TipoVeiculo;
import com.example.veiculo.Veiculo;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * O "tronco" das três rodas: junta a roda da esquerda, a central e a da direita.
 * A sincronização mecânica está aqui: há um único {@link #avancar()} e é ele que faz as três
 * rodas darem um passo ao mesmo tempo, cada uma no seu sentido ({@link PosicaoRoda#getSentido()}).
 * Nenhuma roda anda sozinha.
 */
public class ConjuntoRodas {

    private final Map<PosicaoRoda, Roda> rodas = new EnumMap<>(PosicaoRoda.class);

    /**
     * @param veiculos os veículos de cada roda (uma roda em falta, ou com lista vazia, fica sem veículos)
     * @param fabricas as fábricas usadas para criar o veículo novo no fim de cada volta
     * @throws IllegalArgumentException se não houver nenhum veículo em nenhuma roda
     */
    public ConjuntoRodas(Map<PosicaoRoda, List<Veiculo>> veiculos, Map<TipoVeiculo, VeiculoFactory> fabricas) {
        boolean haVeiculos = false;
        for (PosicaoRoda posicao : PosicaoRoda.values()) {
            List<Veiculo> daRoda = veiculos.getOrDefault(posicao, List.of());
            haVeiculos |= !daRoda.isEmpty();
            rodas.put(posicao, new Roda(posicao, daRoda, fabricas));
        }
        if (!haVeiculos) {
            throw new IllegalArgumentException("O conjunto precisa de pelo menos 1 veículo.");
        }
    }

    /**
     * Faz as três rodas darem um passo, todas no mesmo instante.
     *
     * @return o resultado do passo de cada roda
     */
    public Map<PosicaoRoda, GaragemLoop.Passo> avancar() {
        Map<PosicaoRoda, GaragemLoop.Passo> passos = new EnumMap<>(PosicaoRoda.class);
        for (PosicaoRoda posicao : PosicaoRoda.values()) {
            passos.put(posicao, rodas.get(posicao).avancar());
        }
        return passos;
    }

    /**
     * Uma troca entre uma roda lateral e a central: o veículo da lateral passou para a vaga
     * do veículo do centro, e o do centro passou para a vaga do da lateral.
     */
    public record Troca(PosicaoRoda lateral, Veiculo daLateral, Veiculo doCentro) {
    }

    /**
     * Se as três rodas estiverem cheias, troca o veículo mais antigo da roda da esquerda com o mais
     * antigo da central e, a seguir, o mais antigo da direita com o mais antigo que ficou na central.
     * Cada veículo ocupa a vaga exata do que substitui. Não faz nada se alguma roda não estiver cheia.
     *
     * @return as trocas feitas (2), ou uma lista vazia se não houve troca
     */
    public List<Troca> trocarSeCheio() {
        for (PosicaoRoda posicao : PosicaoRoda.values()) {
            if (rodas.get(posicao).contarVeiculos() < GaragemLoop.VAGAS - 1) {
                return List.of();
            }
        }
        return List.of(trocarComCentral(PosicaoRoda.ESQUERDA), trocarComCentral(PosicaoRoda.DIREITA));
    }

    private Troca trocarComCentral(PosicaoRoda lateral) {
        Roda central = rodas.get(PosicaoRoda.CENTRAL);
        Roda roda = rodas.get(lateral);
        Veiculo daLateral = roda.maisAntigo();
        Veiculo doCentro = central.maisAntigo();
        roda.substituir(daLateral, doCentro);
        central.substituir(doCentro, daLateral);
        return new Troca(lateral, daLateral, doCentro);
    }

    public Roda getRoda(PosicaoRoda posicao) {
        return rodas.get(posicao);
    }

    /** @return as 5 vagas da roda pela ordem do círculo; null = Vazio */
    public List<Veiculo> getVagas(PosicaoRoda posicao) {
        return rodas.get(posicao).getVagas();
    }
}
