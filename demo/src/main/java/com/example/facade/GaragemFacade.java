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
import java.util.List;
import java.util.Map;

/**
 * Padrão FACADE e ponto de entrada do MODEL: é a única classe que os Controllers conhecem.
 * Esconde as fábricas, a garagem, as matrículas, o iterador e o loop de rotação.
 * Não imprime nada nem conhece a interface gráfica: devolve dados e quem chama decide como os mostrar.
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

    // ---------- Garagem ----------

    /** @return o número máximo de veículos (uma das 5 vagas do loop fica sempre Vazio) */
    public int getCapacidade() {
        return Garagem.CAPACIDADE_MAXIMA;
    }

    public int getLugaresLivres() {
        return garagem.getLugaresLivres();
    }

    /**
     * Cria um veículo do tipo pedido (matrícula gerada automaticamente) e estaciona-o.
     *
     * @return false se a garagem estiver cheia
     */
    public boolean adicionarVeiculo(TipoVeiculo tipo, String marca, String modelo) {
        if (garagem.getLugaresLivres() == 0) {
            return false;
        }
        return garagem.adicionarVeiculo(fabricas.get(tipo).criar(marca, modelo));
    }

    /** @return true se o veículo estava na garagem e foi retirado */
    public boolean removerVeiculo(Veiculo veiculo) {
        return garagem.removerVeiculo(veiculo);
    }

    /** @return os veículos pela ordem em que chegaram (do mais antigo ao mais recente) */
    public List<Veiculo> getVeiculos() {
        return garagem.getVeiculosPorOrdemDeChegada();
    }

    /** Mostra a garagem no terminal (Composite). Útil para depuração. */
    public void mostrarGaragem() {
        garagem.mostrar("");
    }

    /** Mostra as matrículas no terminal (Iterator). Útil para depuração. */
    public void mostrarMatriculas() {
        for (Veiculo veiculo : garagem) {
            System.out.println(" - " + veiculo.getMatricula() + " (" + veiculo.getTipo().getNome() + ")");
        }
    }

    // ---------- Loop ----------

    /**
     * Monta o loop (5 vagas) com os veículos que estão agora na garagem. Os veículos ocupam
     * as primeiras vagas pela ordem em que chegaram e as restantes ficam Vazias.
     *
     * @return false se a garagem estiver vazia (não há nada para rodar), true caso contrário
     */
    public boolean iniciarLoop() {
        if (garagem.getLugaresOcupados() == 0) {
            return false;
        }
        loop = new GaragemLoop(garagem.getVeiculosPorOrdemDeChegada(), fabricas);
        return true;
    }

    /** @return as 5 vagas do loop pela ordem do círculo; null = Vazio */
    public List<Veiculo> vagasLoop() {
        verificarLoopIniciado();
        return loop.getVagas();
    }

    /** @return o estado atual das 5 vagas do loop em texto, por exemplo "[Carro AA-12-BB] [Vazio] ..." */
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
