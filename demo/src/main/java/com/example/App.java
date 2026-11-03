package com.example;

import com.example.controller.GaragemController;
import com.example.controller.LoopController;
import com.example.controller.Navegacao;
import com.example.facade.GaragemFacade;
import com.example.view.CenaGaragemView;
import com.example.view.CenaLoopView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Aplicação JavaFX. Aqui só se montam as peças do MVC e liga-se tudo:
 *
 * <pre>
 *   MODEL       GaragemFacade              (um só, partilhado pelas duas janelas)
 *   VIEWS       CenaGaragemView, CenaLoopView   (uma cena por janela)
 *   CONTROLLERS GaragemController, LoopController
 * </pre>
 *
 * Implementa {@link Navegacao} para os Controllers poderem pedir a troca de janela.
 */
public class App extends Application implements Navegacao {

    private Stage janela;
    private CenaGaragemView vistaGaragem;
    private CenaLoopView vistaLoop;
    private GaragemController controladorGaragem;
    private LoopController controladorLoop;

    @Override
    public void start(Stage stage) {
        janela = stage;

        GaragemFacade modelo = new GaragemFacade();
        vistaGaragem = new CenaGaragemView();
        vistaLoop = new CenaLoopView();
        controladorGaragem = new GaragemController(modelo, vistaGaragem, this);
        controladorLoop = new LoopController(modelo, vistaLoop, this);

        janela.setTitle("Garagem");
        janela.setScene(vistaGaragem.getCena());
        janela.show();
    }

    @Override
    public void irParaLoop() {
        janela.setScene(vistaLoop.getCena());
        controladorLoop.iniciar();
    }

    @Override
    public void irParaGaragem() {
        controladorGaragem.atualizar(); // o loop pode ter trocado veículos
        janela.setScene(vistaGaragem.getCena());
    }

    @Override
    public void stop() {
        controladorLoop.parar();
    }
}
