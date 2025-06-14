package tads;

/**
 * Representa um nó de uma lista duplamente encadeada genérica.
 * Cada nó armazena um valor e referências para o nó anterior e o próximo nó na sequência.
 *
 * @param <T> o tipo de valor armazenado no nó.
 */
public class NoLista<T> {
    /**
     * Valor armazenado no nó.
     */
    private T valor;

    /**
     * Referência para o próximo nó na lista.
     * É {@code null} se este for o último nó da lista.
     */
    private NoLista<T> prox;

    /**
     * Referência para o nó anterior na lista.
     * É {@code null} se este for o primeiro nó da lista.
     */
    private NoLista<T> ant;

    /**
     * Constrói um novo nó com o valor fornecido.
     * As referências para o próximo ({@code prox}) e o anterior ({@code ant}) são inicializadas como {@code null}.
     *
     * @param valor o valor a ser armazenado no nó. Não pode ser nulo.
     * @throws IllegalArgumentException se o valor fornecido for nulo.
     */
    public NoLista(T valor) {
        if (valor == null) {
            throw new IllegalArgumentException("O valor do nó não pode ser nulo.");
        }
        this.valor = valor;
        this.prox = null;
        this.ant = null;
    }

    /**
     * Retorna o valor armazenado no nó.
     *
     * @return o valor armazenado.
     */
    public T getValor() {
        return valor;
    }

    /**
     * Define o valor a ser armazenado no nó.
     *
     * @param valor o novo valor a ser armazenado.
     * @throws IllegalArgumentException se o valor fornecido for nulo.
     */
    public void setValor(T valor) {
        if (valor == null) {
            throw new IllegalArgumentException("O valor do nó não pode ser nulo.");
        }
        this.valor = valor;
    }

    /**
     * Retorna o próximo nó na lista.
     *
     * @return o {@link NoLista} que segue este nó, ou {@code null} se este for o último nó.
     */
    public NoLista<T> getProx() {
        return prox;
    }

    /**
     * Define o próximo nó na lista.
     *
     * @param prox o novo {@link NoLista} a ser definido como próximo. Pode ser {@code null}.
     */
    public void setProx(NoLista<T> prox) {
        this.prox = prox;
    }

    /**
     * Retorna o nó anterior na lista.
     *
     * @return o {@link NoLista} que precede este nó, ou {@code null} se este for o primeiro nó.
     */
    public NoLista<T> getAnt() {
        return ant;
    }

    /**
     * Define o nó anterior na lista.
     *
     * @param ant o novo {@link NoLista} a ser definido como anterior. Pode ser {@code null}.
     */
    public void setAnt(NoLista<T> ant) {
        this.ant = ant;
    }
}