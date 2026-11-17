package com.example.controller;

import com.example.facade.GaragemFacade;
import com.example.loop.PosicaoRoda;
import com.example.veiculo.Veiculo;
import com.example.view.CenaGaragemView;
import com.example.view.componentes.ListaVeiculosView;

import java.util.List;

/**
 * CONTROLLER da janela 1. Liga a View (CenaGaragemView) ao Model (GaragemFacade):
 * ouve os botões, valida o que o utilizador escreveu, pede ao Model para fazer o trabalho
 * e manda a View mostrar o resultado. Não desenha nada e não tem regras da garagem.
 */
public class GaragemController {

    private final GaragemFacade modelo;
    private final CenaGaragemView vista;
    private final Navegacao navegacao;

    public GaragemController(GaragemFacade modelo, CenaGaragemView vista, Navegacao navegacao) {
        this.modelo = modelo;
        this.vista = vista;
        this.navegacao = navegacao;

        vista.getFormulario().setAoAdicionar(this::adicionar);
        vista.getFormulario().aoMudarRoda(roda -> atualizar());
        vista.getBotaoRemover().setOnAction(e -> remover());
        vista.getBotaoIniciarLoop().setOnAction(e -> iniciarLoop());
        atualizar();
    }

    /** Volta a desenhar as listas e liga ou desliga os botões conforme o estado do Model. */
    public void atualizar() {
        for (PosicaoRoda roda : PosicaoRoda.values()) {
            vista.getLista(roda).mostrar(modelo.getVeiculos(roda), modelo.getCapacidade());
        }
        vista.getFormulario().setAdicionarAtivo(modelo.getLugaresLivres(vista.getFormulario().getRoda()) > 0);
        vista.getBotaoIniciarLoop().setDisable(modelo.getTotalVeiculos() == 0);
    }

    private void adicionar() {
        String marca = vista.getFormulario().getMarca();
        String modeloTexto = vista.getFormulario().getModelo();
        PosicaoRoda roda = vista.getFormulario().getRoda();
        if (marca.isEmpty() || modeloTexto.isEmpty()) {
            vista.getFormulario().mostrarMensagem("Preenche a marca e o modelo.", true);
            return;
        }
        if (!modelo.adicionarVeiculo(roda, vista.getFormulario().getTipo(), marca, modeloTexto)) {
            vista.getFormulario().mostrarMensagem(roda.getNome() + " está cheia (" + modelo.getCapacidade() + "/"
                    + modelo.getCapacidade() + "). Remove um veículo dessa roda ou escolhe outra roda.", true);
            return;
        }
        Veiculo ultimo = ultimoVeiculo(roda);
        vista.getFormulario().mostrarMensagem("Estacionado: " + ultimo.getTipo().getNome() + " "
                + ultimo.getMatricula() + " (" + roda.getNome() + ")", false);
        vista.getFormulario().limpar();
        atualizar();
    }

    private void remover() {
        ListaVeiculosView listaAtiva = vista.getListaAtiva();
        Veiculo escolhido = listaAtiva.getSelecionado();
        if (escolhido == null) {
            vista.getFormulario().mostrarMensagem("Escolhe primeiro um veículo na lista.", true);
            return;
        }
        modelo.removerVeiculo(escolhido);
        vista.getFormulario().mostrarMensagem("Removido: " + escolhido.getMatricula(), false);
        atualizar();
    }

    private void iniciarLoop() {
        if (!modelo.iniciarLoop()) {
            vista.getFormulario().mostrarMensagem("Adiciona pelo menos 1 veículo antes de iniciar o loop.", true);
            return;
        }
        navegacao.irParaLoop();
    }

    /** O veículo acabado de adicionar é o mais recente da roda, ou seja, o último pela ordem de chegada. */
    private Veiculo ultimoVeiculo(PosicaoRoda roda) {
        List<Veiculo> veiculos = modelo.getVeiculos(roda);
        return veiculos.get(veiculos.size() - 1);
    }
}
