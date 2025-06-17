package eventos;

import tads.Lista;
import java.util.ArrayList;
import java.util.List;

/**
 * Gerencia a agenda de eventos. O loop de processamento de eventos foi movido
 * para a classe MainFX para melhor controle da interface e da simulação.
 */
public class GerenciadorAgenda {

    private static Lista<Evento> eventos = new Lista<>();
    private static int tempoUltimoEvento = 0;
    private static Evento ultimoEvento = null;
    private static final List<IEventoObserver> observers = new ArrayList<>();

    private GerenciadorAgenda() {
        // Previne instanciação
    }

    public static void adicionarObserver(IEventoObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public static void removerObserver(IEventoObserver observer) {
        observers.remove(observer);
    }

    public static void notificarObservers(Evento evento) {
        for (IEventoObserver observer : observers) {
            observer.onEvento(evento);
        }
    }

    /**
     * Retorna e remove o próximo evento da agenda.
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
     * Adiciona um novo evento à agenda, mantendo a ordem cronológica.
     * @param evento O evento a ser agendado.
     */
    public static void adicionarEvento(Evento evento) {
        if (evento == null) {
            throw new IllegalArgumentException("O evento a ser adicionado não pode ser nulo.");
        }
        eventos.adicionarOrdenado(evento, (e1, e2) -> Integer.compare(e1.getTempo(), e2.getTempo()));
    }

    /**
     * MÉTODO ADICIONADO DE VOLTA PARA CORRIGIR O ERRO.
     * Remove um evento específico da agenda.
     * @param evento O evento a ser removido.
     * @return true se o evento foi encontrado e removido.
     */
    public static boolean removerEvento(Evento evento) {
        if (evento == null) return false;
        return eventos.removerProcurado(evento);
    }


    /**
     * Reseta a agenda de eventos e contadores.
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

    public static boolean estaVazia() {
        return eventos.estaVazia();
    }

    public static Evento espiarProximoEvento() {
        if (estaVazia()) return null;
        return eventos.espiarPrimeiro().getValor();
    }
}