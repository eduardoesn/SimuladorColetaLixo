package tads;

/**
 * Classe que representa um nó em uma fila com suporte a prioridade.
 *
 * @param <T> o tipo de valor armazenado no nó
 */
public class NoFila<T> {

    /**
     * Valor armazenado no nó
     */
    T valor;

    /**
     * Prioridade associada ao valor (quanto maior, maior a prioridade)
     */
    int prioridade;

    /**
     * Referência para o próximo nó na fila
     */
    NoFila<T> prox;

    /**
     * Referência para o nó anterior na fila
     */
    NoFila<T> ant;

    /**
     * Construtor padrão que cria um nó com o valor fornecido e prioridade zero.
     *
     * @param valor o valor a ser armazenado no nó
     */
    public NoFila(T valor) {
        this.valor = valor;
        this.prioridade = 0;
        this.prox = null;
        this.ant = null;
    }

    /**
     * Construtor que cria um nó com o valor e a prioridade fornecidos.
     *
     * @param valor      o valor a ser armazenado no nó
     * @param prioridade a prioridade associada ao valor
     */
    public NoFila(T valor, int prioridade) {
        this.valor = valor;
        this.prioridade = prioridade;
        this.prox = null;
        this.ant = null;
    }

    /**
     * Retorna o valor armazenado neste nó.
     *
     * @return o valor armazenado
     */
    public T getValor() {
        return valor;
    }

    /**
     * Define o valor a ser armazenado neste nó.
     *
     * @param valor o novo valor a ser armazenado
     */
    public void setValor(T valor) {
        this.valor = valor;
    }

    /**
     * Retorna a prioridade associada a este nó.
     *
     * @return a prioridade do nó
     */
    public int getPrioridade() {
        return prioridade;
    }

    /**
     * Define a prioridade associada a este nó.
     *
     * @param prioridade a nova prioridade a ser atribuída
     */
    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    /**
     * Retorna o próximo nó na fila.
     *
     * @return o próximo nó
     */
    public NoFila<T> getProx() {
        return prox;
    }

    /**
     * Define o próximo nó na fila.
     *
     * @param prox o novo nó a ser definido como próximo
     */
    public void setProx(NoFila<T> prox) {
        this.prox = prox;
    }

    /**
     * Retorna o nó anterior na fila.
     *
     * @return o nó anterior
     */
    public NoFila<T> getAnt() {
        return ant;
    }

    /**
     * Define o nó anterior na fila.
     *
     * @param ant o novo nó a ser definido como anterior
     */
    public void setAnt(NoFila<T> ant) {
        this.ant = ant;
    }

}
