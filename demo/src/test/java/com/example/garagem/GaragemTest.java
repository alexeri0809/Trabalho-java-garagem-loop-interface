package com.example.garagem;

import com.example.loop.GaragemLoop;
import com.example.veiculo.Barco;
import com.example.veiculo.Carro;
import com.example.veiculo.Mota;
import com.example.veiculo.Veiculo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GaragemTest {

    @Test
    void comecaVaziaComQuatroLugaresLivres() {
        Garagem garagem = new Garagem();
        assertEquals(0, garagem.getLugaresOcupados());
        assertEquals(4, garagem.getLugaresLivres());
    }

    @Test
    void naoDeixaPassarDeQuatroVeiculos() {
        Garagem garagem = new Garagem();
        for (int i = 0; i < 4; i++) {
            assertTrue(garagem.adicionarVeiculo(new Carro("Marca" + i, "Modelo" + i)));
        }
        // O 5º veículo deve ser recusado: o loop tem de ficar sempre com 1 Vazio
        assertFalse(garagem.adicionarVeiculo(new Carro("Extra", "Extra")));
        assertEquals(4, garagem.getLugaresOcupados());
        assertEquals(0, garagem.getLugaresLivres());
    }

    @Test
    void capacidadeDeixaSempreUmaVagaVazioNoLoop() {
        assertEquals(GaragemLoop.VAGAS - 1, Garagem.CAPACIDADE_MAXIMA);
    }

    @Test
    void aceitaQualquerCombinacaoDeTipos() {
        Garagem garagem = new Garagem();
        garagem.adicionarVeiculo(new Carro("Toyota", "Corolla"));
        garagem.adicionarVeiculo(new Barco("Quicksilver", "Activ"));
        garagem.adicionarVeiculo(new Mota("Yamaha", "MT-07"));

        assertEquals(3, garagem.getLugaresOcupados());
    }

    @Test
    void listaVeiculosPelaOrdemDeChegadaEmVezDeAgruparPorTipo() {
        Garagem garagem = new Garagem();
        Veiculo mota = new Mota("Yamaha", "MT-07");
        Veiculo carro = new Carro("Toyota", "Corolla");
        Veiculo barco = new Barco("Quicksilver", "Activ");
        garagem.adicionarVeiculo(mota);
        garagem.adicionarVeiculo(carro);
        garagem.adicionarVeiculo(barco);

        assertEquals(List.of(mota, carro, barco), garagem.getVeiculosPorOrdemDeChegada());
    }
}
