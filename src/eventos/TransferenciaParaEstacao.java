package eventos;

import caminhoes.CaminhaoPequeno;
import estacoes.EstacaoDeTransferencia;
import timer.Timer;
import timer.TimerDetalhado;
import zonas.Zonas;
import zonas.DistanciaZonas;

/**
 * Evento responsável por representar a chegada de um caminhão a uma estação de transferência.
 * <p>
 * Esse evento lida com a transferência de carga de um caminhão pequeno para a estação, incluindo:
 * <ul>
 *     <li>Exibição de informações detalhadas sobre a operação.</li>
 *     <li>Calculo do tempo total da viagem considerando condições como carga transportada e horários de pico.</li>
 *     <li>Registro da chegada do caminhão na estação de transferência.</li>
 * </ul>
 */
public class TransferenciaParaEstacao extends Evento {

    /**
     * Caminhão pequeno que será transferido para a estação.
     */
    private CaminhaoPequeno caminhaoPequeno;

    /**
     * Zona inicial de onde o caminhão parte.
     */
    private Zonas zonaInicial;

    /**
     * Construtor do evento de transferência para uma estação de transferência.
     *
     * @param tempo           O tempo simulado em que o evento ocorre.
     * @param caminhaoPequeno O caminhão pequeno responsável pela transferência.
     * @param zonaInicial     A zona inicial de onde o caminhão inicia a viagem.
     */
    public TransferenciaParaEstacao(int tempo, CaminhaoPequeno caminhaoPequeno, Zonas zonaInicial) {
        super(tempo);
        this.caminhaoPequeno = caminhaoPequeno;
        this.zonaInicial = zonaInicial;
    }

    /**
     * Cria uma string representando o evento de transferência.
     * <p>
     * A string contém informações como:
     * <ul>
     *     <li>O identificador do caminhão.</li>
     *     <li>A zona inicial de onde o caminhão parte.</li>
     *     <li>O horário simulado do evento.</li>
     * </ul>
     *
     * @return Uma representação textual do evento de transferência.
     */
    @Override
    public String toString() {
        return String.format("EventoTransferencia | Caminhão %s | Zona %s | Horário: %s",
                caminhaoPequeno.getId(),
                zonaInicial.getNome(),
                Timer.formatarHorarioSimulado(getTempo()));
    }

    /**
     * Executa o evento de transferência do caminhão para a estação de transferência.
     * <p>
     * Durante a execução, realiza as seguintes ações:
     * <ul>
     *     <li>Obtém a estação de transferência de destino baseada na zona inicial.</li>
     *     <li>Calcula o tempo total de deslocamento considerando condições como carga e horários de pico.</li>
     *     <li>Exibe informações detalhadas sobre o percurso, como tempo previsto e durações.</li>
     *     <li>Agenda um novo evento representando a chegada do caminhão na estação.</li>
     * </ul>
     */
    @Override
    public void executar() {

        // Obtém a estação de transferência de acordo com a zona do caminhão.
        EstacaoDeTransferencia estacaoDestino = DistanciaZonas.getEstacaoPara(zonaInicial);

        // Obtém o tempo atual em que o evento está sendo executado.
        int tempoAtual = getTempo();

        // Obtém a carga atual transportada pelo caminhão.
        int cargaAtual = caminhaoPequeno.getCargaAtual();

        // Calcula o tempo total para o deslocamento até a estação, considerando carga e horários.
        TimerDetalhado timerDetalhado = Timer.calcularTimerDetalhado(tempoAtual, cargaAtual, true);

        // Exibe informações detalhadas do evento no console para fins de log/debug.
        System.out.println("== TRANSFERÊNCIA ==");
        System.out.printf("[%s] \n", Timer.formatarHorarioSimulado(tempoAtual));
        System.out.printf("Caminhão %s → Estação %s%n", caminhaoPequeno.getId(), estacaoDestino.getNomeEstacao());
        System.out.printf("  • Tempo de trajeto: %s%n", Timer.formatarDuracao(timerDetalhado.tempoDeslocamento));
        if (timerDetalhado.tempoExtraCarregado > 0) {
            System.out.printf("  • Tempo extra por carga: +%s%n", Timer.formatarDuracao(timerDetalhado.tempoExtraCarregado));
        }
        System.out.printf("  • Tempo total da viagem: %s%n", Timer.formatarDuracao(timerDetalhado.tempoTotal));
        System.out.printf("  • Horário previsto de chegada: %s%n", Timer.formatarHorarioSimulado(tempoAtual + timerDetalhado.tempoTotal));
        System.out.println();

        // Agenda um novo evento indicando a chegada do caminhão na estação de transferência
        GerenciadorAgenda.adicionarEvento(
                new EstacaoTransferencia((tempoAtual + timerDetalhado.tempoTotal), estacaoDestino, caminhaoPequeno));
    }
}