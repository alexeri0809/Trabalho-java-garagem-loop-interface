package com.example.loop;

import com.example.fabrica.VeiculoFactory;
import com.example.veiculo.TipoVeiculo;
import com.example.veiculo.Veiculo;

import java.util.List;
import java.util.Map;

/**
 * Garagem de rotação com 5 vagas em círculo. Os veículos escolhidos ocupam as primeiras
 * vagas e as restantes ficam Vazias (há sempre pelo menos 1 Vazio).
 * Em cada passo o veículo da frente do Vazio ocupa o Vazio e o Vazio avança.
 * No fim de cada volta, o veículo mais antigo sai e entra um veículo novo do mesmo tipo,
 * com matrícula nova.
 * A GaragemLoop só conhece a ListaCircular, nunca os nós.
 */
public class GaragemLoop {

    /** Número fixo de vagas do loop. */
    public static final int VAGAS = 5;

    private static final String TEXTO_VAZIO = "Vazio";

    private final ListaCircular vagas = new ListaCircular();
    private final Map<TipoVeiculo, VeiculoFactory> fabricas;
    private int percursoTotal;

    /**
     * Monta o loop com os veículos escolhidos, pela ordem dada, seguidos dos Vazios.
     *
     * @param veiculos os veículos a rodar, do mais antigo ao mais recente (1 a {@code VAGAS - 1})
     * @param fabricas as fábricas usadas para criar o veículo novo no fim de cada volta
     * @throws IllegalArgumentException se não houver veículos ou se não sobrar pelo menos 1 vaga Vazia
     */
    public GaragemLoop(List<Veiculo> veiculos, Map<TipoVeiculo, VeiculoFactory> fabricas) {
        if (veiculos.isEmpty()) {
            throw new IllegalArgumentException("O loop precisa de pelo menos 1 veículo.");
        }
        if (veiculos.size() > VAGAS - 1) {
            throw new IllegalArgumentException(
                    "O loop aceita no máximo " + (VAGAS - 1) + " veículos (tem de haver sempre 1 Vazio).");
        }
        this.fabricas = fabricas;
        for (Veiculo veiculo : veiculos) {
            vagas.adicionarVeiculo(veiculo);
        }
        for (int i = veiculos.size(); i < VAGAS; i++) {
            vagas.adicionarVazio();
        }
    }

    /**
     * Dá um passo da rotação: o Vazio avança e um veículo recua uma vaga. Se isso completar
     * uma volta, o veículo mais antigo sai e entra um veículo novo do mesmo tipo com matrícula nova.
     *
     * @return o estado depois do passo e, no fim de uma volta, os veículos que saíram e entraram
     */
    public Passo avancar() {
        int voltasAntes = percursoTotal / VAGAS;
        percursoTotal += vagas.moverVazio();
        boolean volta = percursoTotal / VAGAS > voltasAntes;
        Veiculo saiu = null;
        Veiculo entrou = null;
        if (volta) {
            saiu = vagas.maisAntigo();
            entrou = criarSubstituto(saiu);
            vagas.substituirMaisAntigo(entrou);
        }
        return new Passo(estado(), volta, saiu, entrou);
    }

    /** Cria um veículo novo do mesmo tipo, marca e modelo, mas com matrícula nova. */
    private Veiculo criarSubstituto(Veiculo antigo) {
        return fabricas.get(antigo.getTipo()).criar(antigo.getMarca(), antigo.getModelo());
    }

    /**
     * @return um texto como "[Carro DH-17-MP] [Mota NH-68-CL] [Vazio] [Vazio] [Barco 6º-CO-7-15-21]"
     */
    public String estado() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < vagas.tamanho(); i++) {
            Veiculo veiculo = vagas.obter(i);
            if (i > 0) {
                sb.append(' ');
            }
            sb.append('[');
            if (veiculo == null) {
                sb.append(TEXTO_VAZIO);
            } else {
                sb.append(veiculo.getTipo().getNome()).append(' ').append(veiculo.getMatricula());
            }
            sb.append(']');
        }
        return sb.toString();
    }

    /** O resultado de um passo da rotação. */
    public static final class Passo {
        public final String estado;
        public final boolean voltaCompleta;
        /** Veículo que saiu no fim da volta, ou null se a volta não acabou. */
        public final Veiculo veiculoQueSaiu;
        /** Veículo que entrou no fim da volta, ou null se a volta não acabou. */
        public final Veiculo veiculoQueEntrou;

        public Passo(String estado, boolean voltaCompleta, Veiculo veiculoQueSaiu, Veiculo veiculoQueEntrou) {
            this.estado = estado;
            this.voltaCompleta = voltaCompleta;
            this.veiculoQueSaiu = veiculoQueSaiu;
            this.veiculoQueEntrou = veiculoQueEntrou;
        }
    }
}
