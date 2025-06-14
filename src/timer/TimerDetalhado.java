package timer;

/**
 * Classe que armazena detalhes sobre os tempos associados a uma operação no simulador.
 * <p>
 * Esta classe é utilizada para encapsular os diferentes componentes de tempo envolvidos em uma operação
 * de caminhão, como tempo de coleta, tempo de deslocamento e o tempo adicional devido à carga.
 * O tempo total da operação é calculado automaticamente a partir destes valores.
 */
public class TimerDetalhado {

    /**
     * Tempo gasto na coleta de materiais, em unidades de tempo (minutos).
     */
    public final int tempoColeta;

    /**
     * Tempo gasto no deslocamento do caminhão, em unidades de tempo (minutos).
     * Este valor já considera ajustes por horários de pico.
     */
    public final int tempoDeslocamento;

    /**
     * Tempo adicional aplicado devido ao peso ou carga transportada pelo caminhão,
     * em unidades de tempo (minutos). Geralmente aplicado quando o caminhão está carregado.
     */
    public final int tempoExtraCarregado;

    /**
     * Tempo total do processo (soma de tempo de coleta, deslocamento e tempo extra),
     * em unidades de tempo (minutos).
     */
    public final int tempoTotal;

    /**
     * Construtor da classe TimerDetalhado.
     * <p>
     * Inicializa os tempos de coleta, deslocamento e tempo extra, e calcula automaticamente o tempo total.
     *
     * @param tempoColeta         O tempo gasto na coleta de materiais (em minutos).
     * @param tempoDeslocamento   O tempo gasto no deslocamento (em minutos).
     * @param tempoExtraCarregado O tempo adicional devido à carga transportada (em minutos).
     * @throws IllegalArgumentException se qualquer um dos parâmetros de tempo for negativo.
     */
    public TimerDetalhado(int tempoColeta, int tempoDeslocamento, int tempoExtraCarregado) {
        if (tempoColeta < 0 || tempoDeslocamento < 0 || tempoExtraCarregado < 0) {
            throw new IllegalArgumentException("Nenhum componente de tempo pode ser negativo.");
        }
        this.tempoColeta = tempoColeta;
        this.tempoDeslocamento = tempoDeslocamento;
        this.tempoExtraCarregado = tempoExtraCarregado;
        this.tempoTotal = tempoColeta + tempoDeslocamento + tempoExtraCarregado;
    }

    /**
     * Retorna uma representação em string dos tempos detalhados, formatando-os para melhor leitura.
     *
     * @return Uma string contendo os tempos de coleta, deslocamento, extra e total.
     */
    @Override
    public String toString() {
        return String.format("Coleta: %dmin, Deslocamento: %dmin, Extra: %dmin, Total: %dmin",
                tempoColeta, tempoDeslocamento, tempoExtraCarregado, tempoTotal);
    }
}