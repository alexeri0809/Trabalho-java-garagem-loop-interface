package com.example.view.componentes;

import com.example.veiculo.Veiculo;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Lista dos veículos que estão na garagem, com o contador "3/4" no título.
 */
public class ListaVeiculosView extends VBox {

    private final Label titulo = new Label();
    private final ListView<Veiculo> lista = new ListView<>();

    public ListaVeiculosView() {
        super(10);
        setPadding(new Insets(20));
        setStyle("-fx-background-color: white; -fx-background-radius: 12;");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #263238;");

        lista.setPlaceholder(new Label("Ainda não há veículos. Adiciona o primeiro à esquerda."));
        lista.setCellFactory(l -> new ListCell<Veiculo>() {
            @Override
            protected void updateItem(Veiculo veiculo, boolean vazio) {
                super.updateItem(veiculo, vazio);
                if (vazio || veiculo == null) {
                    setText(null);
                } else {
                    setText(veiculo.getTipo().getNome() + "   " + veiculo.getMatricula()
                            + "   -   " + veiculo.getMarca() + " " + veiculo.getModelo());
                }
            }
        });
        VBox.setVgrow(lista, Priority.ALWAYS);

        getChildren().addAll(titulo, lista);
        mostrar(List.of(), 0);
    }

    /** Mostra estes veículos e atualiza o contador (por exemplo "2/4"). */
    public void mostrar(List<Veiculo> veiculos, int capacidade) {
        lista.getItems().setAll(veiculos);
        titulo.setText("Veículos na garagem (" + veiculos.size() + "/" + capacidade + ")");
    }

    /** @return o veículo escolhido na lista, ou null se não houver nenhum escolhido */
    public Veiculo getSelecionado() {
        return lista.getSelectionModel().getSelectedItem();
    }
}
