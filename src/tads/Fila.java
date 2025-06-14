package tads;

/**
 * Classe que representa uma fila genérica baseada em nós duplamente ligados.
 * <p>
 * A fila funciona no modelo FIFO (First In, First Out), ou seja, elementos inseridos
 * primeiro serão removidos primeiro.
 * <p>
 * Esta classe é genérica e pode armazenar qualquer tipo de dado especificado como <T>.
 *
 * @param <T> O tipo de elemento armazenado na fila.
 */
public class Fila<T> {
    private NoFila<T> head; // Referência para o início da fila.
    private NoFila<T> tail; // Referência para o final da fila.
    private int tamanho;    // Número de elementos atualmente na fila.

    /**
     * Construtor da fila.
     * Inicializa uma fila vazia, sem elementos.
     */
    public Fila() {
        this.head = null;
        this.tail = null;
        this.tamanho = 0;
    }

    /**
     * Adiciona um novo elemento ao início da fila.
     * Este método não preserva a ordem FIFO no momento da inserção.
     *
     * @param valor O valor a ser inserido na fila.
     * @return Retorna {@code true} se o elemento foi inserido com sucesso.
     */
    public boolean enqueue(T valor) {
        NoFila<T> novo = new NoFila<>(valor);
        if (tamanho == 0) {
            this.head = novo;
            this.tail = novo;
        } else {
            novo.setProx(this.head);
            this.head = novo;
        }
        tamanho++;
        return true;
    }

    /**
     * Remove o último elemento da fila seguindo o modelo FIFO.
     * <p>
     * Caso seja removido, o elemento retirado é desconectado da estrutura.
     *
     * @return Retorna o nó removido ou {@code null} se a fila estiver vazia.
     */
    public NoFila<T> dequeue() {
        if (tamanho == 0) {
            System.out.println("Não há elementos na fila");
            return null;
        }

        NoFila<T> elemento;

        if (this.head == this.tail) { // Caso exista um único elemento na fila
            elemento = this.head;
            this.head = null;
            this.tail = null;
        } else { // Caso existam múltiplos elementos
            NoFila<T> atual = this.head;
            while (atual.getProx() != this.tail) {
                atual = atual.getProx();
            }

            elemento = atual.getProx();
            this.tail = atual;
            atual.setProx(null);
        }

        tamanho--;
        return elemento;
    }

    /**
     * Remove e retorna o primeiro elemento da fila seguindo o modelo FIFO.
     * <p>
     * Este método é mais rápido que {@link #dequeue()} porque mantém a ordem
     * natural da fila e não percorre todos os elementos.
     *
     * @return O valor do elemento removido ou {@code null} se a fila estiver vazia.
     */
    public T poll() {
        if (head == null) {
            return null;
        }
        T valor = head.valor;
        head = head.prox;
        if (head == null) { // Caso a fila fique totalmente vazia
            tail = null;
        }
        tamanho--;
        return valor;
    }

    /**
     * Imprime os elementos da fila na ordem de inserção, do início ao fim.
     * <p>
     * A impressão segue o formato "element1 -> element2 -> ... -> elementN".
     * Se a fila estiver vazia, não imprime nada.
     */
    public void imprimir() {
        NoFila<T> atual = this.head;
        for (int i = this.tamanho; i > 0; i--) {
            if (i == 1) {
                System.out.print(atual.getValor());
            } else {
                System.out.print(atual.getValor() + " -> ");
                atual = atual.getProx();
            }
        }
        System.out.println();
    }

    /**
     * Retorna o tamanho atual da fila, ou seja, o número de elementos nela.
     *
     * @return O número de elementos atualmente na fila.
     */
    public int getTamanho() {
        return tamanho;
    }

    /**
     * Verifica se a fila está vazia.
     *
     * @return {@code true} se a fila não contiver elementos, {@code false} caso contrário.
     */
    public boolean estaVazia() {
        return tamanho == 0;
    }
}