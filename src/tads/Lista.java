package tads;

import java.util.Comparator;

/**
 * Classe que representa uma lista duplamente ligada genérica.
 * <p>
 * Esta lista suporta as operações básicas de manipulação, como adicionar elementos, remover,
 * acessar elementos em posições específicas e imprimir os dados na ordem direta e reversa.
 *
 * @param <T> O tipo de elemento armazenado na lista.
 */
public class Lista<T> {
    // Nó inicial da lista (primeiro nó).
    private NoLista<T> head;
    // Nó final da lista (último nó).
    private NoLista<T> tail;
    // Tamanho atual da lista (número de elementos).
    private int tamanho;

    /**
     * Construtor da classe que inicializa a lista como vazia.
     */
    public Lista() {
        head = null;
        tail = null;
        tamanho = 0;
    }

    /**
     * Adiciona um elemento na posição especificada na lista.
     *
     * @param pos   A posição onde o elemento será inserido.
     *              Deve estar no intervalo [0, tamanho].
     * @param valor O valor do elemento a ser inserido.
     * @return {@code true} se o elemento foi inserido com sucesso.
     * @throws IndexOutOfBoundsException Se a posição for inválida.
     */
    public boolean adicionar(int pos, T valor) {
        if (pos < 0 || pos > tamanho) {
            throw new IndexOutOfBoundsException("Posição inválida: " + pos);
        }

        NoLista<T> novo = new NoLista<>(valor);

        if (pos == 0) { // Inserção no início
            if (head == null) {
                head = novo;
                tail = novo;
            } else {
                novo.setProx(head);
                head.setAnt(novo);
                head = novo;
            }
        } else if (pos == tamanho) { // Inserção no final
            tail.setProx(novo);
            novo.setAnt(tail);
            tail = novo;
        } else { // Inserção no meio
            NoLista<T> atual = head;
            for (int i = 0; i < pos - 1; i++) {
                atual = atual.getProx();
            }
            novo.setProx(atual.getProx());
            atual.setProx(novo);
            novo.getProx().setAnt(novo);
            novo.setAnt(atual);
        }
        tamanho++;
        return true;
    }

    /**
     * Insere um elemento na posição correta de acordo com a ordem definida por um comparador.
     *
     * @param elemento    O elemento a ser inserido.
     * @param comparador  Um comparador que define a ordem dos elementos na lista.
     * @throws IllegalArgumentException Se o elemento ou o comparador forem nulos.
     */
    public void adicionarOrdenado(T elemento, Comparator<T> comparador) {
        if (elemento == null || comparador == null) {
            throw new IllegalArgumentException("Elemento e comparador não podem ser nulos");
        }

        if (head == null || comparador.compare(elemento, head.getValor()) <= 0) {
            adicionar(0, elemento); // Inserção no início.
            return;
        }

        if (comparador.compare(elemento, tail.getValor()) >= 0) {
            adicionar(tamanho, elemento); // Inserção no final.
            return;
        }

        NoLista<T> atual = head;
        int pos = 0;
        while (atual != null && comparador.compare(elemento, atual.getValor()) > 0) {
            atual = atual.getProx();
            pos++;
        }

        adicionar(pos, elemento);
    }

    /**
     * Remove o nó que está no início da lista (head).
     *
     * @return O valor do nó removido ou {@code null} se a lista estiver vazia.
     */
    public T removerHead() {
        if (head == null) {
            return null;
        }
        T valor = head.getValor();
        head = head.getProx();
        if (head != null) {
            head.setAnt(null);
        } else {
            tail = null;
        }
        tamanho--;
        return valor;
    }

    /**
     * Remove o elemento na posição especificada.
     *
     * @param pos A posição do elemento a ser removido.
     * @return {@code true} se a remoção foi bem-sucedida.
     * @throws IndexOutOfBoundsException Se a posição for inválida.
     */
    public boolean remover(int pos) {
        if (pos < 0 || pos >= tamanho || head == null) {
            throw new IndexOutOfBoundsException("Posição inválida: " + pos);
        }

        if (tamanho == 1) { // Único elemento na lista.
            head = null;
            tail = null;
        } else if (pos == 0) { // Remover o primeiro elemento.
            head = head.getProx();
            head.setAnt(null);
        } else if (pos == tamanho - 1) { // Remover o último elemento.
            tail = tail.getAnt();
            tail.setProx(null);
        } else { // Remover elemento no meio da lista.
            NoLista<T> atual = getNo(pos);
            atual.getAnt().setProx(atual.getProx());
            atual.getProx().setAnt(atual.getAnt());
        }
        tamanho--;
        return true;
    }

    /**
     * Remove o nó que contém o elemento especificado.
     *
     * @param elemento O elemento a ser removido.
     * @return {@code true} se o elemento foi encontrado e removido; {@code false} caso contrário.
     */
    public boolean removerProcurado(T elemento) {
        if (elemento == null || head == null) return false;

        NoLista<T> atual = head;

        while (atual != null) {
            if (atual.getValor().equals(elemento)) {
                if (atual == head) { // Elemento é o head.
                    head = atual.getProx();
                    if (head != null) head.setAnt(null);
                } else if (atual == tail) { // Elemento é o tail.
                    tail = atual.getAnt();
                    if (tail != null) tail.setProx(null);
                } else { // Elemento no meio da lista.
                    atual.getAnt().setProx(atual.getProx());
                    atual.getProx().setAnt(atual.getAnt());
                }

                tamanho--;
                return true;
            }

            atual = atual.getProx();
        }

        return false; // Elemento não encontrado.
    }

    /**
     * Retorna o nó correspondente à posição especificada.
     *
     * @param pos A posição do nó na lista.
     * @return O nó na posição ou {@code null} se a posição for inválida.
     */
    private NoLista<T> getNo(int pos) {
        if (pos < 0 || pos >= tamanho) {
            return null;
        }

        NoLista<T> atual;
        if (pos < tamanho / 2) { // Percorre do início até a posição.
            atual = head;
            for (int i = 0; i < pos; i++) {
                atual = atual.getProx();
            }
        } else { // Percorre do final até a posição.
            atual = tail;
            for (int i = tamanho - 1; i > pos; i--) {
                atual = atual.getAnt();
            }
        }
        return atual;
    }

    /**
     * Retorna a referência para o nó inicial da lista (head), sem removê-lo.
     *
     * @return O primeiro nó da lista ou {@code null} se a lista estiver vazia.
     */
    public NoLista<T> espiarPrimeiro() {
        return head;
    }

    /**
     * Retorna o tamanho da lista.
     *
     * @return O número total de elementos na lista.
     */
    public int getTamanho() {
        return tamanho;
    }

    /**
     * Verifica se a lista está vazia.
     *
     * @return {@code true} se a lista estiver vazia, {@code false} caso contrário.
     */
    public boolean estaVazia() {
        return tamanho == 0;
    }

    /**
     * Imprime os elementos da lista na ordem direta.
     */
    public void imprimir() {
        NoLista<T> atual = head;
        while (atual != null) {
            System.out.print(atual.getValor() + " ");
            atual = atual.getProx();
        }
        System.out.println("-> NULL");
    }

    /**
     * Imprime os elementos da lista na ordem inversa.
     */
    public void imprimirReverso() {
        NoLista<T> atual = tail;
        while (atual != null) {
            System.out.print(atual.getValor() + " ");
            atual = atual.getAnt();
        }
        System.out.println("-> NULL");
    }

    /**
     * Retorna o valor armazenado no nó de uma posição específica.
     *
     * @param pos A posição do elemento desejado.
     * @return O valor do elemento na posição ou {@code null} se a posição for inválida.
     */
    public T getValor(int pos) {
        NoLista<T> no = getNo(pos); // Obtém o nó da posição.
        return no != null ? no.getValor() : null;
    }
}