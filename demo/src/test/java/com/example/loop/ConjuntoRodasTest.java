package com.example.loop;

import com.example.fabrica.BarcoFactory;
import com.example.fabrica.CarroFactory;
import com.example.fabrica.MotaFactory;
import com.example.fabrica.VeiculoFactory;
import com.example.veiculo.Barco;
import com.example.veiculo.Carro;
import com.example.veiculo.Mota;
import com.example.veiculo.TipoVeiculo;
import com.example.veiculo.Veiculo;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConjuntoRodasTest {

    private static Map<TipoVeiculo, VeiculoFactory> fabricas() {
        Map<TipoVeiculo, VeiculoFactory> fabricas = new EnumMap<>(TipoVeiculo.class);
        fabricas.put(TipoVeiculo.CARRO, new CarroFactory());
        fabricas.put(TipoVeiculo.BARCO, new BarcoFactory());
        fabricas.put(TipoVeiculo.MOTA, new MotaFactory());
        return fabricas;
    }

    private static Map<PosicaoRoda, List<Veiculo>> comVeiculosEmTodas() {
        Map<PosicaoRoda, List<Veiculo>> veiculos = new EnumMap<>(PosicaoRoda.class);
        veiculos.put(PosicaoRoda.ESQUERDA, List.of(new Carro("A", "A"), new Mota("B", "B")));
        veiculos.put(PosicaoRoda.CENTRAL, List.of(new Carro("C", "C"), new Barco("D", "D"), new Mota("E", "E")));
        veiculos.put(PosicaoRoda.DIREITA, List.of(new Barco("F", "F")));
        return veiculos;
    }

    private static long vazios(List<Veiculo> vagas) {
        return vagas.stream().filter(v -> v == null).count();
    }

    @Test
    void esquerdaECentralTemOVazioNoSentidoHorarioEADireitaNoAntiHorario() {
        assertEquals(Sentido.HORARIO, PosicaoRoda.ESQUERDA.getSentido());
        assertEquals(Sentido.HORARIO, PosicaoRoda.CENTRAL.getSentido());
        assertEquals(Sentido.ANTI_HORARIO, PosicaoRoda.DIREITA.getSentido());
    }

    @Test
    void asRodasSaoEsquerdaCentralDireitaPorEssaOrdem() {
        assertEquals(List.of(PosicaoRoda.ESQUERDA, PosicaoRoda.CENTRAL, PosicaoRoda.DIREITA),
                List.of(PosicaoRoda.values()));
    }

    @Test
    void naoArrancaSemNenhumVeiculo() {
        assertThrows(IllegalArgumentException.class, () -> new ConjuntoRodas(new EnumMap<>(PosicaoRoda.class), fabricas()));
    }

    @Test
    void cadaRodaFicaComOsSeusVeiculosEOsRestantesVazios() {
        ConjuntoRodas conjunto = new ConjuntoRodas(comVeiculosEmTodas(), fabricas());

        assertEquals(5, conjunto.getVagas(PosicaoRoda.ESQUERDA).size());
        assertEquals(3, vazios(conjunto.getVagas(PosicaoRoda.ESQUERDA)));
        assertEquals(2, vazios(conjunto.getVagas(PosicaoRoda.CENTRAL)));
        assertEquals(4, vazios(conjunto.getVagas(PosicaoRoda.DIREITA)));
    }

    @Test
    void umaRodaSemVeiculosFicaParadaComCincoVazios() {
        Map<PosicaoRoda, List<Veiculo>> veiculos = new EnumMap<>(PosicaoRoda.class);
        veiculos.put(PosicaoRoda.CENTRAL, List.of(new Carro("A", "A"), new Mota("B", "B")));
        ConjuntoRodas conjunto = new ConjuntoRodas(veiculos, fabricas());

        assertFalse(conjunto.getRoda(PosicaoRoda.ESQUERDA).temVeiculos());
        for (int passo = 0; passo < 30; passo++) {
            Map<PosicaoRoda, GaragemLoop.Passo> passos = conjunto.avancar();
            assertFalse(passos.get(PosicaoRoda.ESQUERDA).voltaCompleta);
            assertFalse(passos.get(PosicaoRoda.DIREITA).voltaCompleta);
            assertEquals(5, vazios(conjunto.getVagas(PosicaoRoda.ESQUERDA)));
            assertEquals(5, vazios(conjunto.getVagas(PosicaoRoda.DIREITA)));
        }
    }

    @Test
    void asTresRodasDaoUmPassoAoMesmoTempoEMexemSempreUmVeiculoCada() {
        ConjuntoRodas conjunto = new ConjuntoRodas(comVeiculosEmTodas(), fabricas());

        Map<PosicaoRoda, String> anterior = new EnumMap<>(PosicaoRoda.class);
        for (PosicaoRoda posicao : PosicaoRoda.values()) {
            anterior.put(posicao, conjunto.getRoda(posicao).estado());
        }
        for (int passo = 0; passo < 40; passo++) {
            Map<PosicaoRoda, GaragemLoop.Passo> passos = conjunto.avancar();
            assertEquals(3, passos.size());
            for (PosicaoRoda posicao : PosicaoRoda.values()) {
                String atual = passos.get(posicao).estado;
                assertNotEquals(anterior.get(posicao), atual, posicao + " ficou parada no passo " + passo);
                assertEquals(atual, conjunto.getRoda(posicao).estado());
                anterior.put(posicao, atual);
            }
        }
    }

    @Test
    void cadaRodaMantemOsSeusVeiculosECompoeSeIgualDepoisDeVariasVoltas() {
        ConjuntoRodas conjunto = new ConjuntoRodas(comVeiculosEmTodas(), fabricas());

        for (int passo = 0; passo < 200; passo++) {
            conjunto.avancar();
        }

        assertEquals(3, vazios(conjunto.getVagas(PosicaoRoda.ESQUERDA)));
        assertEquals(2, vazios(conjunto.getVagas(PosicaoRoda.CENTRAL)));
        assertEquals(4, vazios(conjunto.getVagas(PosicaoRoda.DIREITA)));
        // a roda da direita tinha 1 barco: continua a ter 1 barco, nenhum veículo passou para outra roda
        assertEquals(1, conjunto.getVagas(PosicaoRoda.DIREITA).stream()
                .filter(v -> v != null && v.getTipo() == TipoVeiculo.BARCO).count());
    }

    @Test
    void ofimDeVoltaTrocaOMaisAntigoDaPropriaRodaPorUmDoMesmoTipo() {
        Veiculo carro = new Carro("Toyota", "Corolla");
        Veiculo mota = new Mota("Yamaha", "MT-07");
        Map<PosicaoRoda, List<Veiculo>> veiculos = new EnumMap<>(PosicaoRoda.class);
        veiculos.put(PosicaoRoda.DIREITA, List.of(carro, mota));
        ConjuntoRodas conjunto = new ConjuntoRodas(veiculos, fabricas());

        GaragemLoop.Passo passo;
        do {
            passo = conjunto.avancar().get(PosicaoRoda.DIREITA);
        } while (!passo.voltaCompleta);

        assertEquals(carro, passo.veiculoQueSaiu);
        assertEquals(TipoVeiculo.CARRO, passo.veiculoQueEntrou.getTipo());
        assertNotEquals(carro.getMatricula(), passo.veiculoQueEntrou.getMatricula());
        assertTrue(conjunto.getVagas(PosicaoRoda.DIREITA).contains(passo.veiculoQueEntrou));
    }
}
