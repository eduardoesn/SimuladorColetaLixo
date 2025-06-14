package tads;

/**
 * Classe que representa um nó em uma fila com suporte a prioridade.
 * Cada nó armazena um valor, uma prioridade associada e referências para
 * o próximo e o nó anterior na estrutura da fila (se for duplamente encadeada).
 *
 * @param <T> o tipo de valor armazenado no nó.
 */
public class NoFila<T> {

    /**
     * Valor armazenado no nó.
     */
    T valor;

    /**
     * Prioridade associada ao valor. Quanto maior o valor, maior a prioridade.
     * (Atualmente não é utilizada para ordenação explícita na classe Fila,
     * que é FIFO simples, mas pode ser usada em uma Fila de Prioridade).
     */
    int prioridade;

    /**
     * Referência para o próximo nó na fila.
     */
    NoFila<T> prox;

    /**
     * Referência para o nó anterior na fila.
     * Permite a navegação bidirecional na estrutura.
     */
    NoFila<T> ant;

    /**
     * Construtor padrão que cria um nó com o valor fornecido e prioridade zero.
     * As referências {@code prox} e {@code ant} são inicializadas como {@code null}.
     *
     * @param valor o valor a ser armazenado no nó.
     */
    public NoFila(T valor) {
        this.valor = valor;
        this.prioridade = 0; // Prioridade padrão
        this.prox = null;
        this.ant = null;
    }

    /**
     * Construtor que cria um nó com o valor e a prioridade fornecidos.
     * As referências {@code prox} e {@code ant} são inicializadas como {@code null}.
     *
     * @param valor      o valor a ser armazenado no nó.
     * @param prioridade a prioridade associada ao valor (quanto maior, maior a prioridade).
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
     * @return o valor armazenado.
     */
    public T getValor() {
        return valor;
    }

    /**
     * Define o valor a ser armazenado neste nó.
     *
     * @param valor o novo valor a ser armazenado.
     */
    public void setValor(T valor) {
        this.valor = valor;
    }

    /**
     * Retorna a prioridade associada a este nó.
     *
     * @return a prioridade do nó.
     */
    public int getPrioridade() {
        return prioridade;
    }

    /**
     * Define a prioridade associada a este nó.
     *
     * @param prioridade a nova prioridade a ser atribuída.
     */
    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    /**
     * Retorna o próximo nó na fila.
     *
     * @return o {@link NoFila} que segue este nó, ou {@code null} se este for o último nó.
     */
    public NoFila<T> getProx() {
        return prox;
    }

    /**
     * Define o próximo nó na fila.
     *
     * @param prox o novo {@link NoFila} a ser definido como próximo.
     */
    public void setProx(NoFila<T> prox) {
        this.prox = prox;
    }

    /**
     * Retorna o nó anterior na fila.
     *
     * @return o {@link NoFila} que precede este nó, ou {@code null} se este for o primeiro nó.
     */
    public NoFila<T> getAnt() {
        return ant;
    }

    /**
     * Define o nó anterior na fila.
     *
     * @param ant o novo {@link NoFila} a ser definido como anterior.
     */
    public void setAnt(NoFila<T> ant) {
        this.ant = ant;
    }
}