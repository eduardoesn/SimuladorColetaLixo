package eventos;

/**
 * Classe abstrata que representa um evento genérico no simulador.
 * <p>
 * Esta classe serve como base para todos os tipos de eventos no sistema de simulação,
 * fornecendo a estrutura básica para ordenação e execução de eventos. Cada evento
 * possui um tempo agendado para sua ocorrência na simulação.
 * <p>
 * Implementa {@code Comparable<Evento>} para permitir a ordenação cronológica dos eventos
 * na agenda de simulação, onde eventos com menor tempo são considerados "menores" e
 * devem ser processados primeiro.
 *
 * @see Comparable
 */
public abstract class Evento implements Comparable<Evento> {
    /**
     * O tempo em minutos quando o evento deve ocorrer na simulação,
     * contado a partir do início da simulação (07:00).
     */
    protected int tempo;

    /**
     * Constrói um novo Evento com o tempo especificado para sua ocorrência.
     *
     * @param tempo O tempo em minutos, a partir do início da simulação, quando este evento ocorrerá.
     * @throws IllegalArgumentException se o tempo fornecido for negativo.
     */
    public Evento(int tempo) {
        if (tempo < 0) {
            throw new IllegalArgumentException("O tempo do evento não pode ser negativo.");
        }
        this.tempo = tempo;
    }

    /**
     * Retorna o tempo agendado para este evento.
     *
     * @return O tempo em minutos quando este evento ocorrerá na simulação.
     */
    public int getTempo() {
        return tempo;
    }

    /**
     * Método abstrato que contém a lógica específica de execução do evento.
     * <p>
     * Deve ser implementado por cada subclasse de {@code Evento} para definir
     * o comportamento particular que ocorre quando o evento é processado.
     * Por exemplo, um evento de coleta de lixo implementaria a lógica de um caminhão
     * coletando lixo de uma zona.
     */
    public abstract void executar();

    /**
     * Compara este evento com outro evento baseado em seus tempos de ocorrência.
     * <p>
     * Este método é crucial para a ordenação dos eventos na agenda de simulação,
     * garantindo que eles sejam processados em ordem cronológica.
     *
     * @param outro O outro evento a ser comparado com este.
     * @return Um valor negativo se este evento ocorrer antes do {@code outro} evento;
     * zero se ambos ocorrerem no mesmo tempo;
     * e um valor positivo se este evento ocorrer depois do {@code outro} evento.
     * @throws NullPointerException se o {@code outro} evento for {@code null}.
     */
    @Override
    public int compareTo(Evento outro) {
        if (outro == null) {
            throw new NullPointerException("Evento para comparação não pode ser nulo.");
        }
        return Integer.compare(this.tempo, outro.tempo);
    }

    /**
     * Retorna uma representação em string do evento.
     * Subclasses devem sobrescrever este método para fornecer uma descrição mais detalhada
     * e específica do evento (ex: "EventoColeta | Caminhão C1 | Zona Sul | Horário: 08:00").
     *
     * @return Uma representação textual genérica do evento.
     */
    @Override
    public String toString() {
        return "Evento @ Tempo: " + tempo;
    }
}