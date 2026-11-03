package com.example.garagem;

import com.example.veiculo.ElementoGaragem;
import com.example.veiculo.TipoVeiculo;
import com.example.veiculo.Veiculo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Raiz do Composite (contém os grupos) e Iterable (permite usar for-each).
 * Capacidade total: 0 a 4 veículos, independentemente do tipo. O loop da garagem
 * tem 5 vagas e tem de ficar sempre com pelo menos 1 vaga Vazio.
 */
public class Garagem implements ElementoGaragem, Iterable<Veiculo> {
    /** 5 vagas do loop menos 1 que tem de ficar sempre Vazio. */
    public static final int CAPACIDADE_MAXIMA = 4;

    // EnumMap mantém a ordem do enum: Carro -> Barco -> Mota
    private final Map<TipoVeiculo, GrupoVeiculos> grupos = new EnumMap<>(TipoVeiculo.class);

    public Garagem() {
        for (TipoVeiculo tipo : TipoVeiculo.values()) {
            grupos.put(tipo, new GrupoVeiculos(tipo.getNomePlural()));
        }
    }

    public boolean adicionarVeiculo(Veiculo veiculo) {
        if (getLugaresLivres() == 0) {
            return false;
        }
        grupos.get(veiculo.getTipo()).adicionar(veiculo);
        return true;
    }

    public boolean removerVeiculo(Veiculo veiculo) {
        return grupos.get(veiculo.getTipo()).remover(veiculo);
    }

    public int getLugaresOcupados() {
        return contarVeiculos();
    }

    public int getLugaresLivres() {
        return CAPACIDADE_MAXIMA - contarVeiculos();
    }

    /**
     * @return os veículos da garagem pela ordem em que foram criados (do mais antigo ao mais recente),
     *         ao contrário do iterador, que agrupa por tipo
     */
    public List<Veiculo> getVeiculosPorOrdemDeChegada() {
        List<Veiculo> todos = new ArrayList<>();
        for (Veiculo veiculo : this) {
            todos.add(veiculo);
        }
        todos.sort(Comparator.comparingInt(Veiculo::getId));
        return todos;
    }

    @Override
    public int contarVeiculos() {
        int total = 0;
        for (GrupoVeiculos grupo : grupos.values()) {
            total += grupo.contarVeiculos();
        }
        return total;
    }

    @Override
    public void mostrar(String indentacao) {
        System.out.println(indentacao + "Garagem (" + contarVeiculos() + "/" + CAPACIDADE_MAXIMA + ")");
        for (GrupoVeiculos grupo : grupos.values()) {
            grupo.mostrar(indentacao + "  ");
        }
    }

    @Override
    public Iterator<Veiculo> iterator() {
        return new IteradorGaragem(grupos.values());
    }
}
