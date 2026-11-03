package com.example.view.componentes;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Um campo de texto com o seu título por cima (por exemplo "Marca").
 * É um objeto separado que o formulário reutiliza para a marca e para o modelo.
 */
public class CampoRotulado extends VBox {

    private final TextField campo = new TextField();

    public CampoRotulado(String rotulo, String dica) {
        super(4);
        Label titulo = new Label(rotulo);
        titulo.setStyle("-fx-font-weight: bold; -fx-text-fill: #37474f;");
        campo.setPromptText(dica);
        getChildren().addAll(titulo, campo);
    }

    /** @return o texto escrito, sem espaços no início e no fim */
    public String getTexto() {
        return campo.getText() == null ? "" : campo.getText().trim();
    }

    public void limpar() {
        campo.clear();
    }

    public void focar() {
        campo.requestFocus();
    }

    /** O que fazer quando o utilizador carrega em Enter dentro do campo. */
    public void setAoCarregarEnter(Runnable acao) {
        campo.setOnAction(e -> acao.run());
    }
}
