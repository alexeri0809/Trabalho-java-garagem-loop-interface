package com.example.loop;

import com.example.veiculo.Veiculo;

/**
 * Lista ligada circular de vagas. O último nó aponta de volta para o primeiro.
 * Uma vaga com conteúdo null representa um Vazio. Pode haver vários Vazios
 * (por exemplo, 3 veículos em 5 vagas deixam 2 Vazios).
 *
 * <p>A lista guarda um "cursor": o Vazio que anda à volta da lista. É esse Vazio que
 * {@link #moverVazio()} faz avançar.
 */
public class ListaCircular {

    private No inicio;
    private No fim;
    private No cursor;
    private int tamanho;

    /**
     * Acrescenta um veículo no fim da lista.
     *
     * @param veiculo o veículo a acrescentar (não pode ser null)
     * @throws IllegalArgumentException se o veículo for null
     */
    public void adicionarVeiculo(Veiculo veiculo) {
        if (veiculo == null) {
            throw new IllegalArgumentException("Use adicionarVazio() para acrescentar um Vazio.");
        }
        adicionar(veiculo);
    }

    /**
     * Acrescenta um Vazio no fim da lista. O primeiro Vazio acrescentado passa a ser o cursor.
     */
    public void adicionarVazio() {
        adicionar(null);
        if (cursor == null) {
            cursor = fim;
        }
    }

    private void adicionar(Veiculo veiculo) {
        No novo = new No(veiculo);
        if (inicio == null) {
            inicio = novo;
        } else {
            fim.setNext(novo);
        }
        fim = novo;
        fim.setNext(inicio);
        tamanho++;
    }

    /**
     * O Vazio avança até ao veículo seguinte e este recua para a vaga do Vazio.
     * Se à frente do Vazio houver outros Vazios, o cursor passa por cima deles sem mexer
     * em nenhum veículo, de modo que cada chamada move sempre um veículo.
     *
     * @return o número de vagas que o cursor percorreu (1 se o vizinho for logo um veículo)
     * @throws IllegalStateException se a lista não tiver Vazio ou não tiver veículos
     */
    public int moverVazio() {
        if (cursor == null) {
            throw new IllegalStateException("A lista não tem Vazio.");
        }
        if (contarVeiculos() == 0) {
            throw new IllegalStateException("A lista não tem veículos para mover.");
        }
        int percurso = 0;
        while (cursor.getNext().isVazio()) {
            cursor = cursor.getNext();
            percurso++;
        }
        No proximo = cursor.getNext();
        cursor.setConteudo(proximo.getConteudo());
        proximo.setConteudo(null);
        cursor = proximo;
        return percurso + 1;
    }

    /**
     * O veículo mais antigo (o de menor id) sai e o veículo novo fica na vaga dele.
     * Os Vazios não são afetados.
     *
     * @param novo o veículo que entra (não pode ser null)
     * @return o veículo que saiu, ou null se não houver nenhum veículo na lista
     * @throws IllegalArgumentException se o veículo novo for null
     */
    public Veiculo substituirMaisAntigo(Veiculo novo) {
        if (novo == null) {
            throw new IllegalArgumentException("O veículo que entra não pode ser null.");
        }
        No maisAntigo = encontrarMaisAntigo();
        if (maisAntigo == null) {
            return null;
        }
        Veiculo saiu = maisAntigo.getConteudo();
        maisAntigo.setConteudo(novo);
        return saiu;
    }

    /**
     * @return o veículo mais antigo (o de menor id), ou null se não houver nenhum veículo na lista
     */
    public Veiculo maisAntigo() {
        No no = encontrarMaisAntigo();
        return no == null ? null : no.getConteudo();
    }

    private No encontrarMaisAntigo() {
        if (inicio == null) {
            return null;
        }
        No maisAntigo = null;
        No atual = inicio;
        do {
            Veiculo veiculo = atual.getConteudo();
            if (veiculo != null && (maisAntigo == null || veiculo.getId() < maisAntigo.getConteudo().getId())) {
                maisAntigo = atual;
            }
            atual = atual.getNext();
        } while (atual != inicio);
        return maisAntigo;
    }

    /** @return true se alguma vaga estiver Vazia */
    public boolean temVazio() {
        return contarVazios() > 0;
    }

    /** @return o número de vagas Vazias */
    public int contarVazios() {
        return tamanho - contarVeiculos();
    }

    /** @return o número de vagas com um veículo */
    public int contarVeiculos() {
        int total = 0;
        if (inicio != null) {
            No atual = inicio;
            do {
                if (!atual.isVazio()) {
                    total++;
                }
                atual = atual.getNext();
            } while (atual != inicio);
        }
        return total;
    }

    /**
     * @return a posição do Vazio que anda à volta da lista (0 é o primeiro nó)
     * @throws IllegalStateException se a lista não tiver Vazio
     */
    public int posicaoDoVazio() {
        if (cursor == null) {
            throw new IllegalStateException("A lista não tem Vazio.");
        }
        No atual = inicio;
        int posicao = 0;
        while (atual != cursor) {
            atual = atual.getNext();
            posicao++;
        }
        return posicao;
    }

    /**
     * @param posicao a posição (0 é o primeiro nó)
     * @return o veículo dessa posição, ou null se for um Vazio
     * @throws IndexOutOfBoundsException se a posição não existir
     */
    public Veiculo obter(int posicao) {
        if (posicao < 0 || posicao >= tamanho) {
            throw new IndexOutOfBoundsException("Posição inválida: " + posicao);
        }
        No atual = inicio;
        for (int i = 0; i < posicao; i++) {
            atual = atual.getNext();
        }
        return atual.getConteudo();
    }

    /** @return o número de vagas da lista */
    public int tamanho() {
        return tamanho;
    }
}
