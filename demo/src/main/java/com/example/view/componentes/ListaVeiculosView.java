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
 * Lista dos veículos de uma roda, com o nome da roda e o contador "3/4" no título.
 */
public class ListaVeiculosView extends VBox {

    private final String nome;
    private final Label titulo = new Label();
    private final ListView<Veiculo> lista = new ListView<>();

    /** @param nome o nome mostrado no título, por exemplo "Roda da esquerda" */
    public ListaVeiculosView(String nome) {
        super(10);
        this.nome = nome;
        setPadding(new Insets(20));
        setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0.1, 0, 3);");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #263238;");

        lista.setPlaceholder(new Label("Ainda não há veículos nesta roda. Adiciona o primeiro à esquerda."));
        lista.setCellFactory(l -> new ListCell<Veiculo>() {
            @Override
            protected void updateItem(Veiculo veiculo, boolean vazio) {
                super.updateItem(veiculo, vazio);
                if (vazio || veiculo == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setStyle("-fx-border-color: transparent transparent transparent "
                            + VagaView.corClara(veiculo.getTipo()) + "; -fx-border-width: 0 0 0 5;"
                            + "-fx-padding: 8 10 8 10; -fx-font-size: 13px;");
                    setText(VagaView.icone(veiculo.getTipo()) + "  " + veiculo.getTipo().getNome() + "   " + veiculo.getMatricula()
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
        titulo.setText(nome + " (" + veiculos.size() + "/" + capacidade + ")");
    }

    /** @return o veículo escolhido na lista, ou null se não houver nenhum escolhido */
    public Veiculo getSelecionado() {
        return lista.getSelectionModel().getSelectedItem();
    }
}
