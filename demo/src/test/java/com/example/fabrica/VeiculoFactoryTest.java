package com.example.fabrica;

import com.example.veiculo.Barco;
import com.example.veiculo.Carro;
import com.example.veiculo.Mota;
import com.example.veiculo.TipoVeiculo;
import com.example.veiculo.Veiculo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VeiculoFactoryTest {

    @Test
    void carroFactoryCriaCarro() {
        Veiculo veiculo = new CarroFactory().criar("Toyota", "Corolla");
        assertTrue(veiculo instanceof Carro);
        assertEquals(TipoVeiculo.CARRO, veiculo.getTipo());
        assertEquals("Toyota", veiculo.getMarca());
        assertEquals("Corolla", veiculo.getModelo());
    }

    @Test
    void barcoFactoryCriaBarco() {
        Veiculo veiculo = new BarcoFactory().criar("Quicksilver", "Activ");
        assertTrue(veiculo instanceof Barco);
        assertEquals(TipoVeiculo.BARCO, veiculo.getTipo());
    }

    @Test
    void motaFactoryCriaMota() {
        Veiculo veiculo = new MotaFactory().criar("Yamaha", "MT-07");
        assertTrue(veiculo instanceof Mota);
        assertEquals(TipoVeiculo.MOTA, veiculo.getTipo());
    }
}
