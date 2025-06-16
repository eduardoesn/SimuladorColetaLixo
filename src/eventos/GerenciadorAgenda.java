package eventos;

import tads.Lista;
import java.util.ArrayList;
import java.util.List;

/**
 * Gerencia a agenda de eventos, agora com capacidade de notificar observadores (como a UI).
 */
public class GerenciadorAgenda {

    private static Lista<Evento> eventos = new Lista<>();
    private static int tempoUltimoEvento = 0;
    private static Evento ultimoEvento = null;
    private static final List<IEventoObserver> observers = new ArrayList<>();

    private GerenciadorAgenda() {
        // Previne instanciação
    }

    /**
     * Adiciona um observador à lista. O observador será notificado sobre cada evento.
     * @param observer O observador a ser adicionado.
     */
    public static void adicionarObserver(IEventoObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Remove um observador da lista de notificações.
     * @param observer O observador a ser removido.
     */
    public static void removerObserver(IEventoObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifica todos os observadores registrados sobre um evento.
     * @param evento O evento que ocorreu.
     */
    private static void notificarObservers(Evento evento) {
        for (IEventoObserver observer : observers) {
            observer.onEvento(evento);
        }
    }

    /**
     * Adiciona um novo evento à agenda, mantendo a ordem cronológica.
     * @param evento O {@link Evento} a ser agendado.
     */
    public static void adicionarEvento(Evento evento) {
        if (evento == null) {
            throw new IllegalArgumentException("O evento a ser adicionado não pode ser nulo.");
        }
        eventos.adicionarOrdenado(evento, (e1, e2) -> Integer.compare(e1.getTempo(), e2.getTempo()));
    }

    /**
     * Remove um evento específico da agenda.
     * @param evento O {@link Evento} a ser removido.
     * @return {@code true} se o evento foi encontrado e removido.
     */
    public static boolean removerEvento(Evento evento) {
        if (evento == null) return false;
        return eventos.removerProcurado(evento);
    }

    /**
     * Processa todos os eventos da agenda em ordem. Após cada evento, notifica os observadores.
     */
    public static void processarEventos() {
        System.out.println("\n[PROCESSANDO EVENTOS]");
        while (temEventos()) {
            Evento eventoAtual = eventos.removerHead();
            if (eventoAtual != null) {
                tempoUltimoEvento = eventoAtual.getTempo();
                ultimoEvento = eventoAtual;

                System.out.println("--- Executando: " + eventoAtual.toString());
                eventoAtual.executar();
                notificarObservers(eventoAtual); // Notifica a UI após a execução do evento
            }
        }
        System.out.println("[PROCESSAMENTO DE EVENTOS CONCLUÍDO]");
    }

    /**
     * Reseta a agenda, limpando todos os eventos e zerando os contadores.
     * Essencial para permitir múltiplas execuções da simulação a partir da UI.
     */
    public static void reset() {
        eventos = new Lista<>();
        tempoUltimoEvento = 0;
        ultimoEvento = null;
        System.out.println("[AGENDA] A agenda de eventos foi resetada.");
    }

    public static int getTempoUltimoEvento() {
        return tempoUltimoEvento;
    }

    public static Evento getUltimoEvento() {
        return ultimoEvento;
    }

    public static boolean temEventos() {
        return !eventos.estaVazia();
    }
}