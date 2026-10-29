package com.example.veiculo;

import com.example.matricula.EstrategiaMatricula;

import java.util.concurrent.atomic.AtomicInteger;

/** Folha do Composite: um veículo individual. */
public abstract class Veiculo implements ElementoGaragem {
    /** Número de ordem de chegada: cada veículo criado recebe o número seguinte (1, 2, 3, ...). */
    private static final AtomicInteger SEQUENCIA = new AtomicInteger();

    protected final int id;
    protected final String matricula;
    protected final String marca;
    protected final String modelo;

    public Veiculo(String marca, String modelo, EstrategiaMatricula estrategiaMatricula) {
        this.id = SEQUENCIA.incrementAndGet();
        this.matricula = estrategiaMatricula.gerar(); // Strategy
        this.marca = marca;
        this.modelo = modelo;
    }

    public abstract TipoVeiculo getTipo();

    /** @return o número de ordem de chegada (o veículo mais antigo tem o menor id) */
    public int getId() {
        return id;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    @Override
    public int contarVeiculos() {
        return 1;
    }

    @Override
    public void mostrar(String indentacao) {
        System.out.println(indentacao + "- " + this);
    }

    @Override
    public String toString() {
        return getTipo().getNome() + " [" + matricula + " - " + marca + " " + modelo + "]";
    }
}
