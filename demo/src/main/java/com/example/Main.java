package com.example;

import com.example.facade.GaragemFacade;
import com.example.loop.GaragemLoop;
import com.example.veiculo.TipoVeiculo;
import com.example.veiculo.Veiculo;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final long PAUSA_MS = 700;

    public static void main(String[] args) throws InterruptedException, IOException {
        GaragemFacade garagem = new GaragemFacade();
        Scanner scanner = new Scanner(System.in);

        boolean continuar = true;

        while (continuar) {
            System.out.println("\n--- Lugares livres: " + garagem.getLugaresLivres() + " ---");
            System.out.println("1 - Adicionar Carro");
            System.out.println("2 - Adicionar Barco");
            System.out.println("3 - Adicionar Mota");
            System.out.println("4 - Ver garagem");
            System.out.println("5 - Ver matrículas");
            System.out.println("6 - Iniciar loop");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> adicionarVeiculo(scanner, garagem, TipoVeiculo.CARRO);
                case "2" -> adicionarVeiculo(scanner, garagem, TipoVeiculo.BARCO);
                case "3" -> adicionarVeiculo(scanner, garagem, TipoVeiculo.MOTA);
                case "4" -> garagem.mostrarGaragem();
                case "5" -> garagem.mostrarMatriculas();
                case "6" -> executarLoop(scanner, garagem);
                case "0" -> continuar = false;
                default -> System.out.println("Opção inválida. Tenta novamente.");
            }
        }

        System.out.println("\nEstado final:");
        garagem.mostrarGaragem();
        scanner.close();
    }

    private static void adicionarVeiculo(Scanner scanner, GaragemFacade garagem, TipoVeiculo tipo) {
        if (garagem.getLugaresLivres() == 0) {
            System.out.println("A garagem está cheia! Não é possível adicionar mais veículos.");
            return;
        }

        System.out.print("Marca: ");
        String marca = scanner.nextLine();
        System.out.print("Modelo: ");
        String modelo = scanner.nextLine();

        garagem.adicionarVeiculo(tipo, marca, modelo);
    }

    /**
     * Faz rodar os veículos da garagem nas 5 vagas do loop. O Vazio roda sem parar;
     * a cada volta sai o veículo mais antigo e entra um novo do mesmo tipo.
     * Termina ao carregar em Enter (ou com Ctrl+C), voltando ao menu.
     */
    private static void executarLoop(Scanner scanner, GaragemFacade garagem)
            throws InterruptedException, IOException {
        if (!garagem.iniciarLoop()) {
            return;
        }

        System.out.println("\n=== Garagem em loop (" + GaragemLoop.VAGAS + " vagas) ===");
        System.out.println("Estado inicial:");
        System.out.println(garagem.estadoLoop());
        System.out.println();
        System.out.println("O Vazio roda sem parar. Carrega em Enter para parar e voltar ao menu (ou Ctrl+C para sair).");
        System.out.println();

        int voltas = 0;
        while (System.in.available() == 0) {
            Thread.sleep(PAUSA_MS);
            GaragemLoop.Passo passo = garagem.avancarLoop();
            if (passo.voltaCompleta) {
                voltas++;
                System.out.println("=== Volta " + voltas + " completa === Saiu: "
                        + descrever(passo.veiculoQueSaiu) + " | Entrou: " + descrever(passo.veiculoQueEntrou));
            }
            System.out.println(passo.estado);
        }
        scanner.nextLine(); // consome o Enter que parou o loop
        System.out.println("\nLoop parado.");
    }

    private static String descrever(Veiculo veiculo) {
        return veiculo.getTipo().getNome() + " " + veiculo.getMatricula();
    }
}
