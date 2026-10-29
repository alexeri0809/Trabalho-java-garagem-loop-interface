package com.example.garagem;

import com.example.veiculo.Veiculo;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Padrão ITERATOR: percorre todos os veículos da garagem, um a um,
 * passando por cada grupo pela ordem Carro -> Barco -> Mota,
 * sem expor a estrutura interna da Garagem.
 */
public class IteradorGaragem implements Iterator<Veiculo> {
    private final Iterator<GrupoVeiculos> grupos;
    private Iterator<Veiculo> atual;

    public IteradorGaragem(Collection<GrupoVeiculos> grupos) {
        this.grupos = grupos.iterator();
    }

    @Override
    public boolean hasNext() {
        while ((atual == null || !atual.hasNext()) && grupos.hasNext()) {
            atual = grupos.next().iterator();
        }
        return atual != null && atual.hasNext();
    }

    @Override
    public Veiculo next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return atual.next();
    }
}
