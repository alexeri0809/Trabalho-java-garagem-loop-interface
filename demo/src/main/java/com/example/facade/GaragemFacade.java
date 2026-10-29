package com.example.facade;

import com.example.fabrica.BarcoFactory;
import com.example.fabrica.CarroFactory;
import com.example.fabrica.MotaFactory;
import com.example.fabrica.VeiculoFactory;
import com.example.garagem.Garagem;
import com.example.loop.GaragemLoop;
import com.example.veiculo.TipoVeiculo;
import com.example.veiculo.Veiculo;

import java.util.EnumMap;
import java.util.Map;

/**
 * Padrão FACADE: interface simples para o Main. Esconde as fábricas,
 * a garagem, as matrículas, o iterador e o loop de rotação.
 */
public class GaragemFacade {
    private final Garagem garagem = new Garagem();
    private final Map<TipoVeiculo, VeiculoFactory> fabricas = new EnumMap<>(TipoVeiculo.class);
    private GaragemLoop loop;

    public GaragemFacade() {
        fabricas.put(TipoVeiculo.CARRO, new CarroFactory());
        fabricas.put(TipoVeiculo.BARCO, new BarcoFactory());
        fabricas.put(TipoVeiculo.MOTA, new MotaFactory());
    }

    public int getLugaresLivres() {
        return garagem.getLugaresLivres();
    }

    public boolean adicionarVeiculo(TipoVeiculo tipo, String marca, String modelo) {
        if (garagem.getLugaresLivres() == 0) {
            System.out.println("A garagem está cheia! Não é possível adicionar mais veículos.");
            return false;
        }
        Veiculo veiculo = fabricas.get(tipo).criar(marca, modelo);
        garagem.adicionarVeiculo(veiculo);
        System.out.println("Estacionado: " + veiculo);
        return true;
    }

    public void mostrarGaragem() {
        garagem.mostrar("");
    }

    public void mostrarMatriculas() {
        if (garagem.getLugaresOcupados() == 0) {
            System.out.println("A garagem está vazia.");
            return;
        }
        System.out.println("--- Matrículas (ordem: Carro -> Barco -> Mota) ---");
        for (Veiculo veiculo : garagem) { // Iterator
            System.out.println(" - " + veiculo.getMatricula() + " (" + veiculo.getTipo().getNome() + ")");
        }
    }

    /**
     * Monta o loop (5 vagas) com os veículos que estão agora na garagem. Os veículos ocupam
     * as primeiras vagas pela ordem em que foram criados e as restantes ficam Vazias.
     *
     * @return false se a garagem estiver vazia (não há nada para rodar), true caso contrário
     */
    public boolean iniciarLoop() {
        if (garagem.getLugaresOcupados() == 0) {
            System.out.println("A garagem está vazia. Adiciona pelo menos 1 veículo antes de iniciar o loop.");
            return false;
        }
        loop = new GaragemLoop(garagem.getVeiculosPorOrdemDeChegada(), fabricas);
        return true;
    }

    /** @return o estado atual das 5 vagas do loop, por exemplo "[Carro AA-12-BB] [Vazio] ..." */
    public String estadoLoop() {
        verificarLoopIniciado();
        return loop.estado();
    }

    /**
     * Dá um passo no loop. No fim de uma volta, o veículo que saiu é retirado da garagem
     * e o que entrou é acrescentado, para a garagem refletir sempre o que está no loop.
     *
     * @return o resultado do passo
     */
    public GaragemLoop.Passo avancarLoop() {
        verificarLoopIniciado();
        GaragemLoop.Passo passo = loop.avancar();
        if (passo.voltaCompleta) {
            garagem.removerVeiculo(passo.veiculoQueSaiu);
            garagem.adicionarVeiculo(passo.veiculoQueEntrou);
        }
        return passo;
    }

    private void verificarLoopIniciado() {
        if (loop == null) {
            throw new IllegalStateException("O loop ainda não foi iniciado. Chama iniciarLoop() primeiro.");
        }
    }
}
