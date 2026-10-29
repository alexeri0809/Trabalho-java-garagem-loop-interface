package com.example.facade;

import com.example.loop.GaragemLoop;
import com.example.veiculo.TipoVeiculo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GaragemFacadeTest {

    @Test
    void facadeRespeitaLimiteDeQuatroLugares() {
        GaragemFacade garagem = new GaragemFacade();

        for (int i = 0; i < 4; i++) {
            assertTrue(garagem.adicionarVeiculo(TipoVeiculo.CARRO, "Marca", "Modelo"));
        }
        assertFalse(garagem.adicionarVeiculo(TipoVeiculo.MOTA, "Extra", "Extra"));
        assertEquals(0, garagem.getLugaresLivres());
    }

    @Test
    void loopNaoArrancaComAGaragemVazia() {
        GaragemFacade garagem = new GaragemFacade();
        assertFalse(garagem.iniciarLoop());
    }

    @Test
    void naoSePodeAvancarOLoopSemOIniciar() {
        GaragemFacade garagem = new GaragemFacade();
        assertThrows(IllegalStateException.class, garagem::avancarLoop);
        assertThrows(IllegalStateException.class, garagem::estadoLoop);
    }

    @Test
    void loopMostraOsVeiculosEscolhidosEOsRestantesVagasVazias() {
        GaragemFacade garagem = new GaragemFacade();
        garagem.adicionarVeiculo(TipoVeiculo.CARRO, "Toyota", "Corolla");
        garagem.adicionarVeiculo(TipoVeiculo.CARRO, "Seat", "Ibiza");
        garagem.adicionarVeiculo(TipoVeiculo.MOTA, "Yamaha", "MT-07");
        assertTrue(garagem.iniciarLoop());

        String estado = garagem.estadoLoop();
        assertEquals(2, contar(estado, "[Carro "));
        assertEquals(1, contar(estado, "[Mota "));
        assertEquals(2, contar(estado, "[Vazio]"));
    }

    @Test
    void aGaragemAcompanhaAsTrocasDeFimDeVolta() {
        GaragemFacade garagem = new GaragemFacade();
        garagem.adicionarVeiculo(TipoVeiculo.CARRO, "Toyota", "Corolla");
        garagem.adicionarVeiculo(TipoVeiculo.BARCO, "Quicksilver", "Activ");
        garagem.iniciarLoop();

        GaragemLoop.Passo passo;
        do {
            passo = garagem.avancarLoop();
        } while (!passo.voltaCompleta);

        // Saiu 1 veículo e entrou 1 do mesmo tipo: a garagem continua com 2 veículos
        assertEquals(2, 4 - garagem.getLugaresLivres());
        assertEquals(passo.veiculoQueSaiu.getTipo(), passo.veiculoQueEntrou.getTipo());
    }

    private static int contar(String texto, String procurado) {
        int total = 0;
        int posicao = texto.indexOf(procurado);
        while (posicao >= 0) {
            total++;
            posicao = texto.indexOf(procurado, posicao + procurado.length());
        }
        return total;
    }
}
