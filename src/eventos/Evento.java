package eventos;

/**
 * Classe abstrata que representa um evento genérico no simulador.
 * <p>
 * Esta classe serve como base para todos os tipos de eventos no sistema de simulação,
 * fornecendo a estrutura básica para ordenação e execução de eventos.
 * <p>
 * Implementa {@code Comparable<Evento>} para permitir a ordenação cronológica dos eventos
 * na agenda de simulação.
 *
 * @see Comparable
 */
public abstract class Evento implements Comparable<Evento> {
    /** O tempo em minutos quando o evento deve ocorrer na simulação */
    protected int tempo;

    /**
     * Constrói um novo Evento com o tempo especificado.
     *
     * @param tempo O tempo em minutos quando o evento ocorrerá na simulação
     * @throws IllegalArgumentException se o tempo for negativo
     */
    public Evento(int tempo) {
        if (tempo < 0) {
            throw new IllegalArgumentException("Tempo não pode ser negativo");
        }
        this.tempo = tempo;
    }

    /**
     * Retorna o tempo agendado para este evento.
     *
     * @return O tempo em minutos quando este evento ocorrerá
     */
    public int getTempo() {
        return tempo;
    }

    /**
     * Método abstrato que contém a lógica específica de execução do evento.
     * <p>
     * Deve ser implementado pelas subclasses para definir o comportamento
     * específico de cada tipo de evento.
     */
    public abstract void executar();

    /**
     * Compara este evento com outro evento baseado no tempo de ocorrência.
     * <p>
     * Permite a ordenação cronológica dos eventos na agenda de simulação.
     *
     * @param outro O outro evento a ser comparado
     * @return Um valor negativo, zero ou positivo se este evento for anterior,
     *         simultâneo ou posterior ao evento comparado, respectivamente
     * @throws NullPointerException se o outro evento for null
     */
    @Override
    public int compareTo(Evento outro) {
        if (outro == null) {
            throw new NullPointerException("Evento para comparação não pode ser null");
        }
        return Integer.compare(this.tempo, outro.tempo);
    }
}
