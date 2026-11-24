package com.example.view.componentes;

import com.example.veiculo.Veiculo;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Procura a foto de cada veículo na pasta src/main/resources/imagens (carro1.png, barco2.png, mota3.png, ...).
 * Cada veículo usa sempre a mesma foto (escolhida pelo seu número, entre as que existirem), por isso não muda enquanto roda.
 * Se uma foto não existir, devolve null e a vaga mostra só o ícone e o texto.
 */
public final class ImagensVeiculo {

    // ===================== FOTOS: altera aqui =====================
    /** Pasta das fotos. Para cada tipo conta sozinho quantas há: carro1, carro2, carro3, carro4... */
    private static final String PASTA = "/imagens/";
    // ==============================================================

    private static final Map<String, Image> CACHE = new HashMap<>();

    private ImagensVeiculo() {
    }

    /** @return a foto deste veículo, ou null se o ficheiro não existir */
    public static Image de(Veiculo veiculo) {
        String tipo = veiculo.getTipo().name().toLowerCase(Locale.ROOT);
        int total = contarFotos(tipo);
        if (total == 0) {
            return null;
        }
        String nome = tipo + (veiculo.getId() % total + 1) + ".png";
        return CACHE.computeIfAbsent(nome, n -> {
            URL url = ImagensVeiculo.class.getResource(PASTA + n);
            return url == null ? null : new Image(url.toExternalForm());
        });
    }

    /** Conta as fotos seguidas que existem para o tipo (tipo1.png, tipo2.png, ...), parando na primeira que falte. */
    private static int contarFotos(String tipo) {
        int total = 0;
        while (ImagensVeiculo.class.getResource(PASTA + tipo + (total + 1) + ".png") != null) {
            total++;
        }
        return total;
    }

    /** @return uma ImageView já com o tamanho pedido, ou null se não houver foto */
    public static ImageView vista(Veiculo veiculo, double largura, double altura) {
        Image imagem = de(veiculo);
        if (imagem == null) {
            return null;
        }
        ImageView vista = new ImageView(imagem);
        vista.setFitWidth(largura);
        vista.setFitHeight(altura);
        vista.setPreserveRatio(true);
        return vista;
    }
}
