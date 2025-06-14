package eventos;

import tads.Lista;

/**
 * Classe responsável por gerenciar a fila de eventos de uma simulação.
 * <p>
 * A classe mantém os eventos agendados em ordem cronológica,
 * permitindo que sejam processados na sequência correta.
 */
public class GerenciadorAgenda {

    // Lista que armazena eventos da simulação em ordem cronológica
    private static Lista<Evento> eventos = new Lista<>();
    // Armazena o tempo do último evento processado
    private static int tempoUltimoEvento = 0;
    // Referência ao último evento processado
    private static Evento ultimoEvento = null;

    /**
     * Adiciona um novo evento à agenda, mantendo a ordem cronológica de execução.
     * <p>
     * Eventos com o menor tempo serão executados primeiro.
     * A comparação é realizada com base no tempo do evento, utilizando
     * um comparador para garantir a ordenação correta na lista.
     *
     * @param evento O evento a ser agendado.
     * @throws IllegalArgumentException Se o evento passado for {@code null}.
     */
    public static void adicionarEvento(Evento evento) {
        if (evento == null) {
            throw new IllegalArgumentException("Evento não pode ser nulo");
        }

        eventos.adicionarOrdenado(evento, (e1, e2) -> {
            if (e1.getTempo() < e2.getTempo()) return -1;
            if (e1.getTempo() > e2.getTempo()) return 1;
            return 0;
        });
    }

    /**
     * Remove um evento específico da agenda.
     *
     * @param evento O evento a ser removido.
     * @return {@code true} se o evento foi encontrado e removido;
     *         {@code false} caso contrário.
     */
    public static boolean removerEvento(Evento evento) {
        return eventos.removerProcurado(evento);
    }

    /**
     * Processa todos os eventos presentes na agenda na ordem cronológica existente.
     * <p>
     * Os eventos são executados sequencialmente e removidos da lista conforme são processados.
     * Durante a execução, o tempo do último evento processado e uma referência ao evento
     * são armazenados.
     */
    public static void processarEventos() {
        while (temEventos()) {
            Evento evento = eventos.removerHead(); // Remove o próximo evento
            tempoUltimoEvento = evento.getTempo(); // Atualiza o tempo para o tempo do evento
            ultimoEvento = evento; // Guarda o evento processado
            evento.executar(); // Executa a lógica do evento
        }
    }

    /**
     * Obtém o tempo do último evento processado.
     *
     * @return O tempo do último evento processado.
     */
    public static int getTempoUltimoEvento() {
        return tempoUltimoEvento;
    }

    /**
     * Obtém o último evento que foi processado.
     *
     * @return O último evento processado, ou {@code null} se nenhum evento foi processado ainda.
     */
    public static Evento getUltimoEvento() {
        return ultimoEvento;
    }

    /**
     * Verifica se ainda existem eventos pendentes na agenda.
     *
     * @return {@code true} se houver eventos a serem processados;
     *         {@code false} caso contrário.
     */
    public static boolean temEventos() {
        return !eventos.estaVazia();
    }
}