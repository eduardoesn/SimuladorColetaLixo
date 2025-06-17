package eventos;

import caminhoes.CaminhaoPequeno;
import estacoes.EstacaoDeTransferencia;
import timer.Timer;
import timer.TimerDetalhado;
import zonas.Zonas;
import zonas.DistanciaZonas;

/**
 * Representa um evento de um caminhão pequeno se deslocando de uma zona de coleta
 * para uma estação de transferência. Este evento calcula o tempo de viagem
 * e agenda o evento de chegada correspondente.
 */
public class TransferenciaParaEstacao extends Evento {

    /**
     * O caminhão pequeno que está realizando a viagem.
     */
    private CaminhaoPequeno caminhaoPequeno;
    /**
     * A zona de onde o caminhão está partindo.
     */
    private Zonas zonaInicial;
    /**
     * A duração total da viagem até a estação, em minutos.
     */
    private int duracaoViagem;

    /**
     * Construtor para o evento de transferência para a estação.
     *
     * @param tempo           O tempo de simulação em que a viagem se inicia.
     * @param caminhaoPequeno O caminhão que está se transferindo.
     * @param zonaInicial     A zona de origem da viagem.
     */
    public TransferenciaParaEstacao(int tempo, CaminhaoPequeno caminhaoPequeno, Zonas zonaInicial) {
        super(tempo);
        this.caminhaoPequeno = caminhaoPequeno;
        this.zonaInicial = zonaInicial;
        this.duracaoViagem = 0; // Inicializada em zero, será calculada na execução.
    }

    /**
     * Retorna o caminhão pequeno associado a este evento.
     *
     * @return A instância do {@link CaminhaoPequeno}.
     */
    public CaminhaoPequeno getCaminhao() {
        return this.caminhaoPequeno;
    }

    /**
     * Retorna a duração calculada da viagem para a estação.
     *
     * @return A duração da viagem em minutos.
     */
    public int getDuracaoViagem() {
        return duracaoViagem;
    }

    /**
     * Retorna a zona inicial de onde o caminhão partiu.
     *
     * @return A zona de origem.
     */
    public Zonas getZona() {
        return this.zonaInicial;
    }


    /**
     * Retorna uma representação em string do evento, detalhando o caminhão,
     * a zona de origem e o horário de partida.
     *
     * @return Uma string formatada que descreve o evento.
     */
    @Override
    public String toString() {
        return String.format("EventoTransferencia | Caminhão %s | Zona %s | Horário: %s",
                caminhaoPequeno.getId(),
                zonaInicial.getNome(),
                Timer.formatarHorarioSimulado(getTempo()));
    }

    /**
     * Executa a lógica do evento de transferência.
     * <p>
     * Este método determina a estação de destino com base na zona de origem,
     * calcula o tempo total de viagem (incluindo tempo de deslocamento e tempo extra por carga),
     * imprime os detalhes da viagem no console e, o mais importante, agenda um novo
     * evento {@link EstacaoTransferencia} para simular a chegada do caminhão no destino.
     */
    @Override
    public void executar() {

        EstacaoDeTransferencia estacaoDestino = DistanciaZonas.getEstacaoPara(zonaInicial);
        int tempoAtual = getTempo();
        int cargaAtual = caminhaoPequeno.getCargaAtual();
        TimerDetalhado timerDetalhado = Timer.calcularTimerDetalhado(tempoAtual, cargaAtual, true);

        this.duracaoViagem = timerDetalhado.tempoTotal;

        System.out.println("== TRANSFERÊNCIA ==");
        System.out.printf("[%s] \n", Timer.formatarHorarioSimulado(tempoAtual));
        System.out.printf("Caminhão %s → Estação %s%n", caminhaoPequeno.getId(), estacaoDestino.getNomeEstacao());
        System.out.printf("  • Tempo de trajeto: %s%n", Timer.formatarDuracao(timerDetalhado.tempoDeslocamento));
        if (timerDetalhado.tempoExtraCarregado > 0) {
            System.out.printf("  • Tempo extra por carga: +%s%n", Timer.formatarDuracao(timerDetalhado.tempoExtraCarregado));
        }
        System.out.printf("  • Tempo total da viagem: %s%n", Timer.formatarDuracao(this.duracaoViagem));
        System.out.printf("  • Horário previsto de chegada: %s%n", Timer.formatarHorarioSimulado(tempoAtual + this.duracaoViagem));
        System.out.println();

        GerenciadorAgenda.adicionarEvento(
                new EstacaoTransferencia((tempoAtual + this.duracaoViagem), estacaoDestino, caminhaoPequeno));
    }
}