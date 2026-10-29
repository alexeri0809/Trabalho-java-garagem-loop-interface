package com.example.garagem;

import com.example.veiculo.ElementoGaragem;
import com.example.veiculo.Veiculo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/** Composto do Composite: agrupa veículos do mesmo tipo (Carros, Barcos ou Motas). */
public class GrupoVeiculos implements ElementoGaragem, Iterable<Veiculo> {
    private final String nome;
    private final List<Veiculo> veiculos = new ArrayList<>();

    public GrupoVeiculos(String nome) {
        this.nome = nome;
    }

    public void adicionar(Veiculo veiculo) {
        veiculos.add(veiculo);
    }

    public boolean remover(Veiculo veiculo) {
        return veiculos.remove(veiculo);
    }

    @Override
    public int contarVeiculos() {
        int total = 0;
        for (Veiculo v : veiculos) {
            total += v.contarVeiculos();
        }
        return total;
    }

    @Override
    public void mostrar(String indentacao) {
        System.out.println(indentacao + nome + " (" + contarVeiculos() + ")");
        for (Veiculo v : veiculos) {
            v.mostrar(indentacao + "  ");
        }
    }

    @Override
    public Iterator<Veiculo> iterator() {
        return Collections.unmodifiableList(veiculos).iterator();
    }
}
