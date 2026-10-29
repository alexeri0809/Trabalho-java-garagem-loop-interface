package com.example.loop;

import com.example.veiculo.Veiculo;

/**
 * Um nó da lista ligada circular. Cada nó representa uma vaga do loop.
 */
public class No {

    private Veiculo conteudo;
    private No next;

    /**
     * @param conteudo o veículo da vaga, ou null se a vaga estiver vazia
     */
    public No(Veiculo conteudo) {
        this.conteudo = conteudo;
    }

    /** @return o veículo desta vaga, ou null se for um Vazio */
    public Veiculo getConteudo() {
        return conteudo;
    }

    /** @param conteudo o novo conteúdo da vaga (null para o Vazio) */
    public void setConteudo(Veiculo conteudo) {
        this.conteudo = conteudo;
    }

    /** @return o próximo nó da lista */
    public No getNext() {
        return next;
    }

    /** @param next o próximo nó da lista */
    public void setNext(No next) {
        this.next = next;
    }

    /** @return true se a vaga estiver vazia */
    public boolean isVazio() {
        return conteudo == null;
    }
}
