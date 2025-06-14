package zonas;

import java.util.Random;

/**
 * Representa uma zona da cidade que acumula lixo diariamente.
 */
public class Zonas {
    private String nome;
    private int lixoMin;
    private int lixoMax;
    private int lixoAcomulado;


    /**
     * Construtor da classe Zona.
     *
     * @param nome    Nome da zona.
     * @param lixoMin Valor mínimo de geração de lixo por dia.
     * @param lixoMax Valor máximo de geração de lixo por dia.
     */
    public Zonas(String nome, int lixoMin, int lixoMax) {
        this.nome = nome;
        this.lixoMin = lixoMin;
        this.lixoMax = lixoMax;
        this.lixoAcomulado = 0;
    }


    /**
     * Gera uma quantidade aleatória de lixo diário entre os limites mínimo e máximo.
     * Atualiza o valor de lixo acumulado.
     */
    public void gerarLixoDiario() {
        this.lixoAcomulado = new Random().nextInt(lixoMax - lixoMin + 1) + lixoMin;
        System.out.println("[Zona] " + nome + " gerou " + lixoAcomulado + " toneladas de lixo.");
    }

    /**
     * Coleta uma quantidade de lixo da zona.
     *
     * @param quantidade A quantidade de lixo a ser coletada.
     * @return A quantidade real coletada, que pode ser menor que a solicitada se não houver lixo suficiente.
     */
    public int coletarLixo(int quantidade) {
        int coletado = Math.min(quantidade, lixoAcomulado);
        lixoAcomulado -= coletado;
        return coletado;
    }

    /**
     * Verifica se ainda há lixo restante na zona.
     *
     * @return {@code true} se houver lixo acumulado, {@code false} caso contrário.
     */
    public boolean temLixoRestante() {
        return lixoAcomulado > 0;
    }

    /**
     * Retorna a quantidade atual de lixo acumulado na zona.
     *
     * @return A quantidade de lixo acumulado.
     */
    public int getLixoAcumulado() {
        return lixoAcomulado;
    }

    /**
     * Verifica se a zona está limpa (sem lixo acumulado).
     *
     * @return {@code true} se não houver lixo acumulado, {@code false} caso contrário.
     */
    public boolean estaLimpa() {
        return lixoAcomulado == 0;
    }

    /**
     * Verifica se a zona precisa de coleta com base em um limite mínimo.
     *
     * @param limiteMinimo O limite mínimo de lixo para considerar que a zona precisa de coleta.
     * @return {@code true} se o lixo acumulado for maior ou igual ao limite, {@code false} caso contrário.
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
}
