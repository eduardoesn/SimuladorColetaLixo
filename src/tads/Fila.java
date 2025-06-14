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
     * Adiciona um novo elemento ao final da fila (FIFO).
     *
     * @param valor O valor a ser inserido na fila.
     */
    public void enqueue(T valor) {
        NoFila<T> novoNo = new NoFila<>(valor);
        if (estaVazia()) {
            this.head = novoNo;
        } else {
            this.tail.setProx(novoNo);
            novoNo.setAnt(this.tail); // Ajusta a referência 'ant' para o novo nó
        }
        this.tail = novoNo; // O novo nó sempre será o novo tail
        tamanho++;
    }

    /**
     * Remove e retorna o primeiro elemento da fila seguindo o modelo FIFO.
     *
     * @return O valor do elemento removido ou {@code null} se a fila estiver vazia.
     */
    public T poll() {
        if (estaVazia()) {
            return null;
        }
        T valor = head.getValor();
        head = head.getProx();
        if (head == null) { // Se a fila ficou vazia após a remoção
            tail = null;
        } else {
            head.setAnt(null); // Remove a referência para o nó anterior (o que foi removido)
        }
        tamanho--;
        return valor;
    }

    /**
     * Retorna o primeiro elemento da fila sem removê-lo.
     *
     * @return O valor do primeiro elemento da fila ou {@code null} se a fila estiver vazia.
     */
    public T peek() {
        if (estaVazia()) {
            return null;
        }
        return head.getValor();
    }

    /**
     * Imprime os elementos da fila na ordem de inserção, do início ao fim.
     * <p>
     * A impressão segue o formato "element1 -> element2 -> ... -> elementN".
     * Se a fila estiver vazia, imprime "Fila vazia".
     */
    public void imprimir() {
        if (estaVazia()) {
            System.out.println("Fila vazia.");
            return;
        }
        NoFila<T> atual = this.head;
        while (atual != null) {
            System.out.print(atual.getValor());
            if (atual.getProx() != null) {
                System.out.print(" -> ");
            }
            atual = atual.getProx();
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