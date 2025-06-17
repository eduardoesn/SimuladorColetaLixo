package zonas;

import java.util.Random;

/**
 * Representa uma zona da cidade que acumula lixo diariamente.
 * Cada zona possui um nome, limites mínimos e máximos para a geração diária de lixo,
 * e um registro da quantidade de lixo acumulado.
 */
public class Zonas {
    private String nome;
    private int lixoMin;
    private int lixoMax;
    private int lixoAcomulado;

    /**
     * Construtor da classe Zona.
     * Inicializa uma nova zona com seu nome e os limites de geração de lixo.
     * O lixo acumulado inicial é zero.
     *
     * @param nome    Nome da zona (ex: "Sul", "Norte", "Centro").
     * @param lixoMin Valor mínimo de geração de lixo por dia para esta zona (em toneladas).
     * @param lixoMax Valor máximo de geração de lixo por dia para esta zona (em toneladas).
     */
    public Zonas(String nome, int lixoMin, int lixoMax) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da zona não pode ser nulo ou vazio.");
        }
        if (lixoMin < 0 || lixoMax < lixoMin) {
            throw new IllegalArgumentException("Limites de lixo inválidos: lixoMin não pode ser negativo e lixoMax deve ser maior ou igual a lixoMin.");
        }
        this.nome = nome;
        this.lixoMin = lixoMin;
        this.lixoMax = lixoMax;
        this.lixoAcomulado = 0; // Inicialmente, a zona não tem lixo acumulado
    }

    /**
     * Gera uma quantidade aleatória de lixo diário para a zona, entre os limites mínimo e máximo configurados.
     * Esta quantidade é então adicionada ao valor de lixo acumulado da zona.
     */
    public void gerarLixoDiario() {
        // Gera um número aleatório de lixo entre lixoMin e lixoMax (inclusive)
        // O método nextInt(bound) retorna um valor entre 0 (inclusive) e bound (exclusive).
        // Para incluir lixoMax, a faixa é (lixoMax - lixoMin + 1).
        int lixoGerado = new Random().nextInt(lixoMax - lixoMin + 1) + lixoMin;
        this.lixoAcomulado += lixoGerado; // Adiciona o lixo gerado ao acumulado
        System.out.println("[Zona] " + nome + " gerou " + lixoGerado + " toneladas de lixo. Total acumulado: " + lixoAcomulado + "t.");
    }

    /**
     * Coleta uma quantidade especificada de lixo da zona.
     * A quantidade real coletada será o mínimo entre a quantidade solicitada
     * e o lixo atualmente acumulado na zona. O lixo acumulado é então reduzido.
     *
     * @param quantidade A quantidade de lixo (em toneladas) a ser coletada.
     * @return A quantidade real de lixo coletada. Pode ser menor que a quantidade solicitada
     * se não houver lixo suficiente na zona.
     */
    public int coletarLixo(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("A quantidade a ser coletada não pode ser negativa.");
        }
        int coletado = Math.min(quantidade, lixoAcomulado); // Garante que não se colete mais lixo do que o disponível
        lixoAcomulado -= coletado; // Reduz o lixo acumulado
        System.out.println("[Zona] " + nome + " coletou " + coletado + " toneladas de lixo. Lixo restante: " + lixoAcomulado + "t.");
        return coletado;
    }

    /**
     * Verifica se ainda há lixo acumulado na zona.
     *
     * @return {@code true} se o lixo acumulado for maior que zero, {@code false} caso contrário.
     */
    public boolean temLixoRestante() {
        return lixoAcomulado > 0;
    }

    /**
     * Retorna a quantidade atual de lixo acumulado na zona.
     *
     * @return A quantidade de lixo acumulado em toneladas.
     */
    public int getLixoAcumulado() {
        return lixoAcomulado;
    }

    /**
     * Verifica se a zona está limpa (ou seja, sem lixo acumulado).
     *
     * @return {@code true} se o lixo acumulado for zero, {@code false} caso contrário.
     */
    public boolean estaLimpa() {
        return lixoAcomulado == 0;
    }

    /**
     * Verifica se a zona precisa de coleta com base em um limite mínimo de lixo.
     *
     * @param limiteMinimo O limite mínimo de lixo (em toneladas) para considerar que a zona precisa de coleta.
     * @return {@code true} se o lixo acumulado for maior ou igual ao limite mínimo, {@code false} caso contrário.
     */
    public boolean precisaDeColeta(int limiteMinimo) {
        return lixoAcomulado >= limiteMinimo;
    }

    /**
     * Retorna o nome da zona.
     *
     * @return O nome da zona.
     */
    public String getNome() {
        return nome;
    }

    public int getLixoMax() {
        return lixoMax;
    }
}