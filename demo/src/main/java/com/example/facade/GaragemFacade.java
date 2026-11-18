package com.example.facade;

import com.example.fabrica.BarcoFactory;
import com.example.fabrica.CarroFactory;
import com.example.fabrica.MotaFactory;
import com.example.fabrica.VeiculoFactory;
import com.example.garagem.Garagem;
import com.example.loop.ConjuntoRodas;
import com.example.loop.GaragemLoop;
import com.example.loop.PosicaoRoda;
import com.example.veiculo.TipoVeiculo;
import com.example.veiculo.Veiculo;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Padrão FACADE e ponto de entrada do MODEL: é a única classe que os Controllers conhecem.
 * Esconde as fábricas, as garagens, as matrículas, o iterador e as rodas de rotação.
 * Não imprime nada nem conhece a interface gráfica: devolve dados e quem chama decide como os mostrar.
 *
 * <p>Há três rodas (esquerda, central, direita) e cada uma tem a sua própria {@link Garagem}
 * (até 4 veículos, uma vaga fica sempre Vazio). Os métodos sem o parâmetro {@link PosicaoRoda}
 * continuam a existir e falam com a roda central, que era a única antes.
 */
public class GaragemFacade {
    private final Map<PosicaoRoda, Garagem> garagens = new EnumMap<>(PosicaoRoda.class);
    private final Map<TipoVeiculo, VeiculoFactory> fabricas = new EnumMap<>(TipoVeiculo.class);
    private ConjuntoRodas rodas;
    private List<ConjuntoRodas.Troca> ultimasTrocas = List.of();

    public GaragemFacade() {
        fabricas.put(TipoVeiculo.CARRO, new CarroFactory());
        fabricas.put(TipoVeiculo.BARCO, new BarcoFactory());
        fabricas.put(TipoVeiculo.MOTA, new MotaFactory());
        for (PosicaoRoda posicao : PosicaoRoda.values()) {
            garagens.put(posicao, new Garagem());
        }
    }

    // ---------- Garagem ----------

    /** @return o número máximo de veículos de cada roda (uma das 5 vagas fica sempre Vazio) */
    public int getCapacidade() {
        return Garagem.CAPACIDADE_MAXIMA;
    }

    /** @return os lugares livres da roda central */
    public int getLugaresLivres() {
        return getLugaresLivres(PosicaoRoda.CENTRAL);
    }

    public int getLugaresLivres(PosicaoRoda roda) {
        return garagens.get(roda).getLugaresLivres();
    }

    /** @return o número de veículos nas três rodas */
    public int getTotalVeiculos() {
        int total = 0;
        for (Garagem garagem : garagens.values()) {
            total += garagem.getLugaresOcupados();
        }
        return total;
    }

    /**
     * Cria um veículo do tipo pedido (matrícula gerada automaticamente) e estaciona-o na roda central.
     *
     * @return false se a roda estiver cheia
     */
    public boolean adicionarVeiculo(TipoVeiculo tipo, String marca, String modelo) {
        return adicionarVeiculo(PosicaoRoda.CENTRAL, tipo, marca, modelo);
    }

    /**
     * Cria um veículo do tipo pedido (matrícula gerada automaticamente) e estaciona-o na roda indicada.
     *
     * @return false se essa roda estiver cheia
     */
    public boolean adicionarVeiculo(PosicaoRoda roda, TipoVeiculo tipo, String marca, String modelo) {
        Garagem garagem = garagens.get(roda);
        if (garagem.getLugaresLivres() == 0) {
            return false;
        }
        return garagem.adicionarVeiculo(fabricas.get(tipo).criar(marca, modelo));
    }

    /** @return true se o veículo estava numa das rodas e foi retirado */
    public boolean removerVeiculo(Veiculo veiculo) {
        for (Garagem garagem : garagens.values()) {
            if (garagem.removerVeiculo(veiculo)) {
                return true;
            }
        }
        return false;
    }

    /** @return os veículos da roda central pela ordem em que chegaram (do mais antigo ao mais recente) */
    public List<Veiculo> getVeiculos() {
        return getVeiculos(PosicaoRoda.CENTRAL);
    }

    /** @return os veículos da roda indicada pela ordem em que chegaram (do mais antigo ao mais recente) */
    public List<Veiculo> getVeiculos(PosicaoRoda roda) {
        return garagens.get(roda).getVeiculosPorOrdemDeChegada();
    }

    /** Mostra as garagens das três rodas no terminal (Composite). Útil para depuração. */
    public void mostrarGaragem() {
        for (PosicaoRoda posicao : PosicaoRoda.values()) {
            System.out.println(posicao.getNome() + ":");
            garagens.get(posicao).mostrar("  ");
        }
    }

    /** Mostra as matrículas das três rodas no terminal (Iterator). Útil para depuração. */
    public void mostrarMatriculas() {
        for (PosicaoRoda posicao : PosicaoRoda.values()) {
            for (Veiculo veiculo : garagens.get(posicao)) {
                System.out.println(" - " + veiculo.getMatricula() + " (" + veiculo.getTipo().getNome()
                        + ", " + posicao.getNome() + ")");
            }
        }
    }

    // ---------- Rodas ----------

    /**
     * Monta as três rodas (5 vagas cada) com os veículos que estão agora na garagem de cada uma.
     * Os veículos ocupam as primeiras vagas pela ordem em que chegaram e as restantes ficam Vazias.
     * Uma roda sem veículos fica parada.
     *
     * @return false se não houver nenhum veículo em nenhuma roda (não há nada para rodar)
     */
    public boolean iniciarLoop() {
        if (getTotalVeiculos() == 0) {
            return false;
        }
        Map<PosicaoRoda, List<Veiculo>> veiculos = new EnumMap<>(PosicaoRoda.class);
        for (PosicaoRoda posicao : PosicaoRoda.values()) {
            veiculos.put(posicao, getVeiculos(posicao));
        }
        rodas = new ConjuntoRodas(veiculos, fabricas);
        return true;
    }

    /** @return as 5 vagas da roda central pela ordem do círculo; null = Vazio */
    public List<Veiculo> vagasLoop() {
        return vagasLoop(PosicaoRoda.CENTRAL);
    }

    /** @return as 5 vagas da roda indicada pela ordem do círculo; null = Vazio */
    public List<Veiculo> vagasLoop(PosicaoRoda roda) {
        verificarLoopIniciado();
        return rodas.getVagas(roda);
    }

    /** @return o estado atual das 5 vagas da roda central em texto, por exemplo "[Carro AA-12-BB] [Vazio] ..." */
    public String estadoLoop() {
        verificarLoopIniciado();
        return rodas.getRoda(PosicaoRoda.CENTRAL).estado();
    }

    /**
     * Dá um passo no conjunto: as três rodas andam ao mesmo tempo. Em cada roda que acabe uma volta,
     * o veículo que saiu é retirado da garagem dessa roda e o que entrou é acrescentado, para a garagem
     * refletir sempre o que está na roda.
     *
     * @return o resultado do passo de cada roda
     */
    public Map<PosicaoRoda, GaragemLoop.Passo> avancarRodas() {
        verificarLoopIniciado();
        Map<PosicaoRoda, GaragemLoop.Passo> passos = rodas.avancar();
        for (Map.Entry<PosicaoRoda, GaragemLoop.Passo> entrada : passos.entrySet()) {
            GaragemLoop.Passo passo = entrada.getValue();
            if (passo.voltaCompleta) {
                Garagem garagem = garagens.get(entrada.getKey());
                garagem.removerVeiculo(passo.veiculoQueSaiu);
                garagem.adicionarVeiculo(passo.veiculoQueEntrou);
            }
        }
        // Com as três rodas cheias, no fim de volta da central há troca entre as rodas
        ultimasTrocas = List.of();
        if (passos.get(PosicaoRoda.CENTRAL).voltaCompleta) {
            ultimasTrocas = rodas.trocarSeCheio();
            Garagem centro = garagens.get(PosicaoRoda.CENTRAL);
            for (ConjuntoRodas.Troca troca : ultimasTrocas) {
                Garagem lateral = garagens.get(troca.lateral());
                lateral.removerVeiculo(troca.daLateral());
                centro.removerVeiculo(troca.doCentro());
                lateral.adicionarVeiculo(troca.doCentro());
                centro.adicionarVeiculo(troca.daLateral());
            }
        }
        return passos;
    }

    /** @return as trocas entre rodas feitas no último passo (lista vazia se não houve nenhuma) */
    public List<ConjuntoRodas.Troca> getUltimasTrocas() {
        return ultimasTrocas;
    }

    /**
     * Dá um passo no conjunto (como {@link #avancarRodas()}) e devolve só o resultado da roda central,
     * que era o comportamento de quando só havia uma roda.
     */
    public GaragemLoop.Passo avancarLoop() {
        return avancarRodas().get(PosicaoRoda.CENTRAL);
    }

    private void verificarLoopIniciado() {
        if (rodas == null) {
            throw new IllegalStateException("O loop ainda não foi iniciado. Chama iniciarLoop() primeiro.");
        }
    }
}
