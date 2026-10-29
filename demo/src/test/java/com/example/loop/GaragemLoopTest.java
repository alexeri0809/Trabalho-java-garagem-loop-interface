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

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GaragemLoopTest {

    private static Map<TipoVeiculo, VeiculoFactory> fabricas() {
        Map<TipoVeiculo, VeiculoFactory> fabricas = new EnumMap<>(TipoVeiculo.class);
        fabricas.put(TipoVeiculo.CARRO, new CarroFactory());
        fabricas.put(TipoVeiculo.BARCO, new BarcoFactory());
        fabricas.put(TipoVeiculo.MOTA, new MotaFactory());
        return fabricas;
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

    @Test
    void doisCarrosEUmaMotaFicamComDoisVazios() {
        GaragemLoop loop = new GaragemLoop(
                List.of(new Carro("A", "A"), new Carro("B", "B"), new Mota("C", "C")), fabricas());

        String estado = loop.estado();

        assertEquals(2, contar(estado, "[Carro "));
        assertEquals(1, contar(estado, "[Mota "));
        assertEquals(2, contar(estado, "[Vazio]"));
    }

    @Test
    void naoAceitaZeroVeiculos() {
        assertThrows(IllegalArgumentException.class, () -> new GaragemLoop(List.of(), fabricas()));
    }

    @Test
    void naoAceitaCincoVeiculosPoisTemDeHaverSempreUmVazio() {
        List<Veiculo> cinco = List.of(new Carro("A", "A"), new Carro("B", "B"), new Barco("C", "C"),
                new Mota("D", "D"), new Mota("E", "E"));
        assertThrows(IllegalArgumentException.class, () -> new GaragemLoop(cinco, fabricas()));
    }

    @Test
    void comQuatroVeiculosUmaVoltaAcabaAoQuintoPasso() {
        GaragemLoop loop = new GaragemLoop(
                List.of(new Carro("A", "A"), new Barco("B", "B"), new Mota("C", "C"), new Carro("D", "D")),
                fabricas());

        for (int passo = 1; passo <= 4; passo++) {
            assertFalse(loop.avancar().voltaCompleta, "Não devia acabar a volta no passo " + passo);
        }
        assertTrue(loop.avancar().voltaCompleta);
    }

    @Test
    void haSempreExatamenteCincoVagasEPeloMenosUmVazioEmQualquerCombinacao() {
        for (int quantidade = 1; quantidade <= 4; quantidade++) {
            List<Veiculo> veiculos = new ArrayList<>();
            for (int i = 0; i < quantidade; i++) {
                veiculos.add(i % 2 == 0 ? new Carro("M" + i, "X") : new Barco("M" + i, "X"));
            }
            GaragemLoop loop = new GaragemLoop(veiculos, fabricas());

            for (int passo = 0; passo < 60; passo++) {
                String estado = loop.avancar().estado;
                int vazios = contar(estado, "[Vazio]");
                int ocupadas = contar(estado, "[Carro ") + contar(estado, "[Barco ") + contar(estado, "[Mota ");
                assertEquals(GaragemLoop.VAGAS, vazios + ocupadas);
                assertEquals(quantidade, ocupadas);
                assertTrue(vazios >= 1);
            }
        }
    }

    @Test
    void nenhumPassoFicaIgualAoAnteriorPoisSempreSeMoveUmVeiculo() {
        GaragemLoop loop = new GaragemLoop(List.of(new Carro("A", "A"), new Mota("B", "B")), fabricas());

        String anterior = loop.estado();
        for (int passo = 0; passo < 30; passo++) {
            String atual = loop.avancar().estado;
            assertNotEquals(anterior, atual);
            anterior = atual;
        }
    }

    @Test
    void noFimDaVoltaSaiOMaisAntigoEEntraUmDoMesmoTipoComMatriculaNova() {
        Veiculo carro = new Carro("Toyota", "Corolla");   // o mais antigo
        Veiculo barco = new Barco("Quicksilver", "Activ");
        Veiculo mota = new Mota("Yamaha", "MT-07");
        GaragemLoop loop = new GaragemLoop(List.of(carro, barco, mota), fabricas());

        GaragemLoop.Passo passo;
        do {
            passo = loop.avancar();
        } while (!passo.voltaCompleta);

        assertEquals(carro, passo.veiculoQueSaiu);
        assertEquals(TipoVeiculo.CARRO, passo.veiculoQueEntrou.getTipo());
        assertEquals("Toyota", passo.veiculoQueEntrou.getMarca());
        assertEquals("Corolla", passo.veiculoQueEntrou.getModelo());
        assertNotEquals(carro.getMatricula(), passo.veiculoQueEntrou.getMatricula());
        assertTrue(passo.estado.contains(passo.veiculoQueEntrou.getMatricula()));
        assertFalse(passo.estado.contains(carro.getMatricula()));
    }

    @Test
    void aComposicaoDeTiposMantemSeAoLongoDasVoltas() {
        GaragemLoop loop = new GaragemLoop(
                List.of(new Carro("A", "A"), new Carro("B", "B"), new Mota("C", "C")), fabricas());

        int voltas = 0;
        GaragemLoop.Passo passo = null;
        while (voltas < 12) {
            passo = loop.avancar();
            if (passo.voltaCompleta) {
                voltas++;
            }
        }

        assertEquals(2, contar(passo.estado, "[Carro "));
        assertEquals(1, contar(passo.estado, "[Mota "));
        assertEquals(2, contar(passo.estado, "[Vazio]"));
    }

    @Test
    void asMatriculasQueEntramNaoSeRepetemAoLongoDeVariasVoltas() {
        GaragemLoop loop = new GaragemLoop(
                List.of(new Carro("A", "A"), new Carro("B", "B"), new Carro("C", "C"), new Carro("D", "D")),
                fabricas());

        Set<String> vistas = new HashSet<>();
        int voltas = 0;
        while (voltas < 10) {
            GaragemLoop.Passo passo = loop.avancar();
            if (passo.voltaCompleta) {
                voltas++;
                assertTrue(vistas.add(passo.veiculoQueEntrou.getMatricula()),
                        "Matrícula repetida: " + passo.veiculoQueEntrou.getMatricula());
            }
        }
    }

    @Test
    void comUmSoVeiculoOLoopTambemFunciona() {
        Veiculo unico = new Barco("Quicksilver", "Activ");
        GaragemLoop loop = new GaragemLoop(List.of(unico), fabricas());

        GaragemLoop.Passo passo;
        int passos = 0;
        do {
            passo = loop.avancar();
            passos++;
        } while (!passo.voltaCompleta && passos < 20);

        assertTrue(passo.voltaCompleta);
        assertEquals(unico, passo.veiculoQueSaiu);
        assertEquals(TipoVeiculo.BARCO, passo.veiculoQueEntrou.getTipo());
        assertEquals(1, contar(passo.estado, "[Barco "));
        assertEquals(4, contar(passo.estado, "[Vazio]"));
    }
}
