package tads;

/**
 * Representa um nó de uma lista duplamente encadeada genérica.
 *
 * @param <T> o tipo de valor armazenado no nó
 */
public class NoLista<T> {
    /**
     * Valor armazenado no nó
     */
    private T valor;

    /**
     * Referência para o próximo nó na lista
     */
    private NoLista<T> prox;

    /**
     * Referência para o nó anterior na lista
     */
    private NoLista<T> ant;

    /**
     * Constrói um novo nó com o valor fornecido.
     * As referências para o próximo e o anterior são inicializadas como null.
     *
     * @param valor o valor a ser armazenado no nó
     */
    public NoLista(T valor) {
        this.valor = valor;
        this.prox = null;
        this.ant = null;
    }

    /**
     * Retorna o valor armazenado no nó.
     *
     * @return o valor armazenado
     */
    public T getValor() {
        return valor;
    }

    /**
     * Retorna o próximo nó na lista.
     *
     * @return o próximo nó
     */
    public NoLista<T> getProx() {
        return prox;
    }

    /**
     * Define o próximo nó na lista.
     *
     * @param prox o novo nó a ser definido como próximo
     */
    public void setProx(NoLista<T> prox) {
        this.prox = prox;
    }

    /**
     * Retorna o nó anterior na lista.
     *
     * @return o nó anterior
     */
    public NoLista<T> getAnt() {
        return ant;
    }

    /**
     * Define o nó anterior na lista.
     *
     * @param ant o novo nó a ser definido como anterior
     */
    public void setAnt(NoLista<T> ant) {
        this.ant = ant;
    }
}
