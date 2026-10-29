package com.example.veiculo;

public enum TipoVeiculo {
    // A ordem aqui define a ordem de listagem da garagem: Carro -> Barco -> Mota
    CARRO("Carro", "Carros"),
    BARCO("Barco", "Barcos"),
    MOTA("Mota", "Motas");

    private final String nome;
    private final String nomePlural;

    TipoVeiculo(String nome, String nomePlural) {
        this.nome = nome;
        this.nomePlural = nomePlural;
    }

    public String getNome() {
        return nome;
    }

    public String getNomePlural() {
        return nomePlural;
    }
}
