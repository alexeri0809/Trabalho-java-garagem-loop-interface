package com.example.loop;

import com.example.veiculo.Barco;
import com.example.veiculo.Carro;
import com.example.veiculo.Mota;
import com.example.veiculo.Veiculo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ListaCircularTest {

    @Test
    void naoAceitaVeiculoNull() {
        assertThrows(IllegalArgumentException.class, () -> new ListaCircular().adicionarVeiculo(null));
    }

    @Test
    void aceitaVariosVazios() {
        ListaCircular lista = new ListaCircular();
        lista.adicionarVeiculo(new Carro("A", "A"));
        lista.adicionarVazio();
        lista.adicionarVazio();

        assertEquals(3, lista.tamanho());
        assertEquals(2, lista.contarVazios());
        assertEquals(1, lista.contarVeiculos());
        assertTrue(lista.temVazio());
    }

    @Test
    void moverVazioFazOVeiculoSeguinteRecuarUmaVaga() {
        ListaCircular lista = new ListaCircular();
        Veiculo carro = new Carro("A", "A");
        Veiculo mota = new Mota("B", "B");
        lista.adicionarVeiculo(carro);
        lista.adicionarVazio();
        lista.adicionarVeiculo(mota);

        int percurso = lista.moverVazio();

        assertEquals(1, percurso);
        assertSame(carro, lista.obter(0));
        assertSame(mota, lista.obter(1));
        assertNull(lista.obter(2));
        assertEquals(2, lista.posicaoDoVazio());
    }

    @Test
    void moverVazioSaltaPorCimaDeOutrosVaziosEMoveSempreUmVeiculo() {
        ListaCircular lista = new ListaCircular();
        Veiculo carro = new Carro("A", "A");
        lista.adicionarVazio();
        lista.adicionarVazio();
        lista.adicionarVazio();
        lista.adicionarVeiculo(carro);

        int percurso = lista.moverVazio();

        // O cursor passou pelos Vazios das posições 1 e 2 e depois trocou com o carro da posição 3
        assertEquals(3, percurso);
        assertSame(carro, lista.obter(2));
        assertNull(lista.obter(3));
        assertEquals(3, lista.posicaoDoVazio());
    }

    @Test
    void moverVazioSemVazioOuSemVeiculosDaErro() {
        ListaCircular semVazio = new ListaCircular();
        semVazio.adicionarVeiculo(new Carro("A", "A"));
        assertThrows(IllegalStateException.class, semVazio::moverVazio);

        ListaCircular semVeiculos = new ListaCircular();
        semVeiculos.adicionarVazio();
        semVeiculos.adicionarVazio();
        assertThrows(IllegalStateException.class, semVeiculos::moverVazio);
    }

    @Test
    void substituirMaisAntigoTrocaOVeiculoDeMenorIdEDeixaOsVaziosComoEstavam() {
        ListaCircular lista = new ListaCircular();
        Veiculo antigo = new Carro("A", "A");   // criado primeiro: id menor
        Veiculo recente = new Barco("B", "B");
        Veiculo entra = new Mota("C", "C");
        lista.adicionarVeiculo(recente);
        lista.adicionarVazio();
        lista.adicionarVeiculo(antigo);         // o mais antigo está numa posição posterior
        lista.adicionarVazio();

        Veiculo saiu = lista.substituirMaisAntigo(entra);

        assertSame(antigo, saiu);
        assertSame(recente, lista.obter(0));
        assertSame(entra, lista.obter(2));
        assertEquals(2, lista.contarVazios());
    }

    @Test
    void substituirMaisAntigoNumaListaSemVeiculosDevolveNull() {
        ListaCircular lista = new ListaCircular();
        lista.adicionarVazio();
        assertNull(lista.substituirMaisAntigo(new Carro("A", "A")));
        assertNull(lista.maisAntigo());
    }

    @Test
    void substituirMaisAntigoNaoAceitaNull() {
        assertThrows(IllegalArgumentException.class, () -> new ListaCircular().substituirMaisAntigo(null));
    }

    @Test
    void obterForaDaListaDaErro() {
        ListaCircular lista = new ListaCircular();
        lista.adicionarVazio();
        assertThrows(IndexOutOfBoundsException.class, () -> lista.obter(1));
        assertThrows(IndexOutOfBoundsException.class, () -> lista.obter(-1));
    }
}
