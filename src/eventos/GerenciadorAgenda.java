package eventos;

import tads.Lista;
import java.util.ArrayList;
import java.util.List;

/**
 * Gerencia a agenda de eventos da simulação. Esta é uma classe utilitária estática
 * que mantém uma lista cronologicamente ordenada de eventos a serem executados.
 * Também implementa o padrão Observer para notificar outras partes do sistema
 * sobre eventos processados. O loop de processamento de eventos foi movido
 * para a classe MainFX para melhor controle da interface e da simulação.
 */
public class GerenciadorAgenda {

    /** A lista ordenada de eventos a serem processados. */
    private static Lista<Evento> eventos = new Lista<>();
    /** O tempo de simulação do último evento que foi processado. */
    private static int tempoUltimoEvento = 0;
    /** A referência para o último evento que foi processado. */
    private static Evento ultimoEvento = null;
    /** A lista de observadores a serem notificados quando um evento é processado. */
    private static final List<IEventoObserver> observers = new ArrayList<>();

    /**
     * Construtor privado para impedir a instanciação da classe.
     */
    private GerenciadorAgenda() {
        // Previne instanciação
    }

    /**
     * Adiciona um observador à lista. O observador será notificado sempre que um evento for processado.
     *
     * @param observer O observador a ser adicionado.
     */
    public static void adicionarObserver(IEventoObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Remove um observador da lista de notificações.
     *
     * @param observer O observador a ser removido.
     */
    public static void removerObserver(IEventoObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifica todos os observadores registrados sobre um evento que acabou de ser processado.
     *
     * @param evento O evento que será passado para os observadores.
     */
    public static void notificarObservers(Evento evento) {
        for (IEventoObserver observer : observers) {
            observer.onEvento(evento);
        }
    }

    /**
     * Retorna e remove o próximo evento da agenda (o evento com o menor tempo).
     * Atualiza o estado do último evento processado.
     *
     * @return O próximo evento, ou null se a agenda estiver vazia.
     */
    public static Evento proximoEvento() {
        if (estaVazia()) {
            return null;
        }
        Evento proximo = eventos.removerHead();
        if (proximo != null) {
            ultimoEvento = proximo;
            tempoUltimoEvento = proximo.getTempo();
        }
        return proximo;
    }

    /**
     * Adiciona um novo evento à agenda, mantendo a ordem cronológica baseada no tempo do evento.
     *
     * @param evento O evento a ser agendado.
     * @throws IllegalArgumentException se o evento for nulo.
     */
    public static void adicionarEvento(Evento evento) {
        if (evento == null) {
            throw new IllegalArgumentException("O evento a ser adicionado não pode ser nulo.");
        }
        eventos.adicionarOrdenado(evento, (e1, e2) -> Integer.compare(e1.getTempo(), e2.getTempo()));
    }

    /**
     * Remove um evento específico da agenda.
     * Útil para cancelar eventos agendados, como timeouts.
     *
     * @param evento O evento a ser removido.
     * @return {@code true} se o evento foi encontrado e removido, {@code false} caso contrário.
     */
    public static boolean removerEvento(Evento evento) {
        if (evento == null) return false;
        return eventos.removerProcurado(evento);
    }


    /**
     * Reseta a agenda, limpando todos os eventos pendentes e reiniciando
     * as variáveis de estado. Usado para iniciar uma nova simulação.
     */
    public static void reset() {
        eventos = new Lista<>();
        tempoUltimoEvento = 0;
        ultimoEvento = null;
        System.out.println("[AGENDA] A agenda de eventos foi resetada.");
    }

    /**
     * Retorna o tempo de simulação em que o último evento foi processado.
     *
     * @return O tempo do último evento.
     */
    public static int getTempoUltimoEvento() {
        return tempoUltimoEvento;
    }

    /**
     * Retorna a instância do último evento que foi processado.
     *
     * @return O último objeto {@link Evento} processado.
     */
    public static Evento getUltimoEvento() {
        return ultimoEvento;
    }

    /**
     * Verifica se a agenda de eventos está vazia.
     *
     * @return {@code true} se não houver eventos na agenda, {@code false} caso contrário.
     */
    public static boolean estaVazia() {
        return eventos.estaVazia();
    }

    /**
     * Retorna o próximo evento da agenda sem removê-lo (operação "peek").
     *
     * @return O próximo evento a ser processado, ou {@code null} se a agenda estiver vazia.
     */
    public static Evento espiarProximoEvento() {
        if (estaVazia()) return null;
        return eventos.espiarPrimeiro().getValor();
    }
}