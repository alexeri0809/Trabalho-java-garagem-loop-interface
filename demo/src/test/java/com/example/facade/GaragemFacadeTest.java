package com.example.facade;

import com.example.loop.GaragemLoop;
import com.example.loop.PosicaoRoda;
import com.example.veiculo.TipoVeiculo;
import org.junit.jupiter.api.Test;

import java.util.Map;

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

    @Test
    void adicionarVeiculoNaoImprimeNadaEGuardaMarcaEModelo() {
        GaragemFacade garagem = new GaragemFacade();
        assertTrue(garagem.adicionarVeiculo(TipoVeiculo.BARCO, "  Quicksilver ", " Activ "));

        assertEquals(1, garagem.getVeiculos().size());
        assertEquals("Quicksilver", garagem.getVeiculos().get(0).getMarca());
        assertEquals("Activ", garagem.getVeiculos().get(0).getModelo());
    }

    @Test
    void removerVeiculoLibertaUmLugar() {
        GaragemFacade garagem = new GaragemFacade();
        garagem.adicionarVeiculo(TipoVeiculo.CARRO, "Toyota", "Corolla");
        garagem.adicionarVeiculo(TipoVeiculo.MOTA, "Yamaha", "MT-07");

        assertTrue(garagem.removerVeiculo(garagem.getVeiculos().get(0)));

        assertEquals(1, garagem.getVeiculos().size());
        assertEquals(garagem.getCapacidade() - 1, garagem.getLugaresLivres());
        assertEquals(TipoVeiculo.MOTA, garagem.getVeiculos().get(0).getTipo());
    }

    @Test
    void vagasLoopTemSempreCincoPosicoesEOsVaziosSaoNull() {
        GaragemFacade garagem = new GaragemFacade();
        garagem.adicionarVeiculo(TipoVeiculo.CARRO, "Toyota", "Corolla");
        garagem.adicionarVeiculo(TipoVeiculo.MOTA, "Yamaha", "MT-07");
        garagem.iniciarLoop();

        for (int passo = 0; passo < 20; passo++) {
            var vagas = garagem.vagasLoop();
            assertEquals(5, vagas.size());
            assertEquals(3L, vagas.stream().filter(v -> v == null).count());
            garagem.avancarLoop();
        }
    }

    // ---------- três rodas ----------

    @Test
    void cadaRodaTemOsSeusQuatroLugaresIndependentes() {
        GaragemFacade garagem = new GaragemFacade();

        for (int i = 0; i < 4; i++) {
            assertTrue(garagem.adicionarVeiculo(PosicaoRoda.ESQUERDA, TipoVeiculo.CARRO, "M", "X"));
        }
        assertFalse(garagem.adicionarVeiculo(PosicaoRoda.ESQUERDA, TipoVeiculo.CARRO, "M", "X"));
        assertEquals(0, garagem.getLugaresLivres(PosicaoRoda.ESQUERDA));

        // as outras rodas não são afetadas
        assertEquals(4, garagem.getLugaresLivres(PosicaoRoda.CENTRAL));
        assertEquals(4, garagem.getLugaresLivres(PosicaoRoda.DIREITA));
        assertTrue(garagem.adicionarVeiculo(PosicaoRoda.DIREITA, TipoVeiculo.BARCO, "M", "X"));
        assertEquals(5, garagem.getTotalVeiculos());
    }

    @Test
    void osMetodosAntigosFalamComARodaCentral() {
        GaragemFacade garagem = new GaragemFacade();
        assertTrue(garagem.adicionarVeiculo(TipoVeiculo.MOTA, "Yamaha", "MT-07"));

        assertEquals(1, garagem.getVeiculos(PosicaoRoda.CENTRAL).size());
        assertEquals(1, garagem.getVeiculos().size());
        assertEquals(0, garagem.getVeiculos(PosicaoRoda.ESQUERDA).size());
        assertEquals(0, garagem.getVeiculos(PosicaoRoda.DIREITA).size());
    }

    @Test
    void removerVeiculoEncontraOVeiculoEmQualquerRoda() {
        GaragemFacade garagem = new GaragemFacade();
        garagem.adicionarVeiculo(PosicaoRoda.DIREITA, TipoVeiculo.BARCO, "Quicksilver", "Activ");

        assertTrue(garagem.removerVeiculo(garagem.getVeiculos(PosicaoRoda.DIREITA).get(0)));

        assertEquals(0, garagem.getTotalVeiculos());
    }

    @Test
    void oLoopArrancaSoComVeiculosNumaRodaLateralEACentralFicaVazia() {
        GaragemFacade garagem = new GaragemFacade();
        garagem.adicionarVeiculo(PosicaoRoda.ESQUERDA, TipoVeiculo.CARRO, "Toyota", "Corolla");
        assertTrue(garagem.iniciarLoop());

        assertEquals(5, garagem.vagasLoop(PosicaoRoda.CENTRAL).stream().filter(v -> v == null).count());
        assertEquals(4, garagem.vagasLoop(PosicaoRoda.ESQUERDA).stream().filter(v -> v == null).count());
        // avancarLoop() devolve a roda central, que está parada: nunca marca volta
        assertFalse(garagem.avancarLoop().voltaCompleta);
    }

    @Test
    void avancarRodasMexeAsTresRodasEADaGaragemDeCadaRodaAcompanhaAsTrocas() {
        GaragemFacade garagem = new GaragemFacade();
        garagem.adicionarVeiculo(PosicaoRoda.ESQUERDA, TipoVeiculo.CARRO, "A", "A");
        garagem.adicionarVeiculo(PosicaoRoda.CENTRAL, TipoVeiculo.MOTA, "B", "B");
        garagem.adicionarVeiculo(PosicaoRoda.DIREITA, TipoVeiculo.BARCO, "C", "C");
        garagem.iniciarLoop();
        var antigosDaDireita = garagem.getVeiculos(PosicaoRoda.DIREITA);

        Map<PosicaoRoda, GaragemLoop.Passo> passos;
        do {
            passos = garagem.avancarRodas();
        } while (!passos.get(PosicaoRoda.DIREITA).voltaCompleta);

        assertEquals(3, passos.size());
        // cada roda continua com 1 veículo, do mesmo tipo, e o da direita foi trocado por um novo
        assertEquals(1, garagem.getVeiculos(PosicaoRoda.DIREITA).size());
        assertEquals(TipoVeiculo.BARCO, garagem.getVeiculos(PosicaoRoda.DIREITA).get(0).getTipo());
        assertFalse(garagem.getVeiculos(PosicaoRoda.DIREITA).contains(antigosDaDireita.get(0)));
        assertEquals(1, garagem.getVeiculos(PosicaoRoda.ESQUERDA).size());
        assertEquals(1, garagem.getVeiculos(PosicaoRoda.CENTRAL).size());
        assertEquals(3, garagem.getTotalVeiculos());
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
