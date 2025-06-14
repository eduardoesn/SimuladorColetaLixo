package eventos;

import tads.Lista;

/**
 * Classe responsável por gerenciar a agenda de eventos de uma simulação.
 * <p>
 * O {@code GerenciadorAgenda} mantém todos os eventos agendados em ordem cronológica
 * (do mais antigo para o mais recente), permitindo que sejam processados
 * na sequência correta de tempo.
 * <p>
 * É implementado como uma classe utilitária estática para fornecer um ponto de acesso
 * global e único para o gerenciamento da agenda de eventos.
 */
public class GerenciadorAgenda {

    /**
     * Lista que armazena os eventos da simulação em ordem cronológica.
     * Utiliza uma {@link Lista} duplamente encadeada para permitir inserções ordenadas eficientes.
     */
    private static Lista<Evento> eventos = new Lista<>();

    /**
     * Armazena o tempo (em minutos) do último evento processado.
     * Útil para rastrear o progresso da simulação e calcular a duração total.
     */
    private static int tempoUltimoEvento = 0;

    /**
     * Referência ao último evento que foi processado.
     * Pode ser útil para depuração ou para fornecer informações sobre o ponto final da simulação.
     */
    private static Evento ultimoEvento = null;

    /**
     * Construtor privado para evitar a instanciação.
     * Esta é uma classe utilitária e seus métodos são estáticos.
     */
    private GerenciadorAgenda() {
        // Previne instanciação
    }

    /**
     * Adiciona um novo evento à agenda, mantendo a ordem cronológica de execução.
     * <p>
     * O evento é inserido na posição correta na lista com base em seu tempo,
     * garantindo que eventos com o menor tempo sejam executados primeiro.
     * A ordenação é realizada utilizando um {@link java.util.Comparator} que compara os tempos dos eventos.
     *
     * @param evento O {@link Evento} a ser agendado.
     * @throws IllegalArgumentException Se o evento passado for {@code null}.
     */
    public static void adicionarEvento(Evento evento) {
        if (evento == null) {
            throw new IllegalArgumentException("O evento a ser adicionado não pode ser nulo.");
        }
        // Adiciona o evento de forma ordenada na lista, usando o tempo do evento como critério de ordenação.
        eventos.adicionarOrdenado(evento, (e1, e2) -> Integer.compare(e1.getTempo(), e2.getTempo()));
        System.out.println("[AGENDA] Evento adicionado: " + evento.getClass().getSimpleName() + " @ " + evento.getTempo());
    }

    /**
     * Remove um evento específico da agenda.
     * Útil para cancelar eventos que não são mais relevantes (ex: um caminhão que estava na fila
     * de espera por um caminhão grande, mas que agora conseguiu descarregar).
     *
     * @param evento O {@link Evento} a ser removido.
     * @return {@code true} se o evento foi encontrado e removido com sucesso;
     * {@code false} caso contrário (se o evento não estava na agenda ou era nulo).
     */
    public static boolean removerEvento(Evento evento) {
        if (evento == null) {
            return false; // Não é possível remover um evento nulo.
        }
        boolean removido = eventos.removerProcurado(evento);
        if (removido) {
            System.out.println("[AGENDA] Evento removido: " + evento.getClass().getSimpleName() + " @ " + evento.getTempo());
        }
        return removido;
    }

    /**
     * Processa todos os eventos presentes na agenda na ordem cronológica.
     * <p>
     * Os eventos são removidos do início da lista e seus métodos {@code executar()} são chamados.
     * Durante a execução, o {@code tempoUltimoEvento} e {@code ultimoEvento} são atualizados
     * para refletir o evento que está sendo processado. A simulação continua enquanto
     * houver eventos na agenda.
     */
    public static void processarEventos() {
        System.out.println("\n[PROCESSANDO EVENTOS]");
        // Continua processando enquanto houver eventos na lista
        while (temEventos()) {
            Evento eventoAtual = eventos.removerHead(); // Remove o próximo evento a ser processado (o mais antigo)
            if (eventoAtual != null) {
                tempoUltimoEvento = eventoAtual.getTempo(); // Atualiza o tempo global da simulação
                ultimoEvento = eventoAtual; // Armazena uma referência ao evento que está sendo processado

                System.out.println("--- Executando: " + eventoAtual.toString()); // Log do evento sendo executado
                eventoAtual.executar(); // Executa a lógica específica do evento
            }
        }
        System.out.println("[PROCESSAMENTO DE EVENTOS CONCLUÍDO]");
    }

    /**
     * Obtém o tempo (em minutos) do último evento que foi processado na simulação.
     *
     * @return O tempo do último evento processado. Retorna 0 se nenhum evento foi processado ainda.
     */
    public static int getTempoUltimoEvento() {
        return tempoUltimoEvento;
    }

    /**
     * Obtém o último evento que foi processado pelo gerenciador.
     *
     * @return O último {@link Evento} processado, ou {@code null} se nenhum evento foi processado ainda.
     */
    public static Evento getUltimoEvento() {
        return ultimoEvento;
    }

    /**
     * Verifica se ainda existem eventos pendentes na agenda para serem processados.
     *
     * @return {@code true} se houver um ou mais eventos a serem processados;
     * {@code false} caso contrário (se a agenda estiver vazia).
     */
    public static boolean temEventos() {
        return !eventos.estaVazia();
    }
}