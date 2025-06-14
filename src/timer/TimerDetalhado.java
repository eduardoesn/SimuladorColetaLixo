package timer;

/**
 * Classe que armazena detalhes sobre os tempos associados a uma operação.
 * <p>
 * Esta classe é utilizada para representar os diferentes tempos envolvidos,
 * como tempo de coleta, tempo de deslocamento e o tempo adicional devido à carga.
 * O tempo total é calculado automaticamente com base nestes valores.
 */
public class TimerDetalhado {

    /**
     * Tempo gasto na coleta de materiais, em unidades de tempo.
     */
    public final int tempoColeta;

    /**
     * Tempo gasto no deslocamento, em unidades de tempo.
     */
    public final int tempoDeslocamento;

    /**
     * Tempo adicional devido ao peso ou carga transportada, em unidades de tempo.
     */
    public final int tempoExtraCarregado;

    /**
     * Tempo total do processo (soma de tempo de coleta, deslocamento e tempo extra), em unidades de tempo.
     */
    public final int tempoTotal;

    /**
     * Construtor da classe TimerDetalhado.
     * <p>
     * Inicializa os tempos de coleta, deslocamento e tempo extra, e calcula automaticamente o tempo total.
     *
     * @param tempoColeta         O tempo gasto na coleta de materiais.
     * @param tempoDeslocamento   O tempo gasto no deslocamento.
     * @param tempoExtraCarregado O tempo adicional devido à carga transportada.
     */
    public TimerDetalhado(int tempoColeta, int tempoDeslocamento, int tempoExtraCarregado) {
        this.tempoColeta = tempoColeta;
        this.tempoDeslocamento = tempoDeslocamento;
        this.tempoExtraCarregado = tempoExtraCarregado;
        this.tempoTotal = tempoColeta + tempoDeslocamento + tempoExtraCarregado;
    }
}