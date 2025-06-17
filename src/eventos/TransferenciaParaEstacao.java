package eventos;

import caminhoes.CaminhaoPequeno;
import estacoes.EstacaoDeTransferencia;
import timer.Timer;
import timer.TimerDetalhado;
import zonas.Zonas;
import zonas.DistanciaZonas;

public class TransferenciaParaEstacao extends Evento {

    private CaminhaoPequeno caminhaoPequeno;
    private Zonas zonaInicial;
    private int duracaoViagem;

    public TransferenciaParaEstacao(int tempo, CaminhaoPequeno caminhaoPequeno, Zonas zonaInicial) {
        super(tempo);
        this.caminhaoPequeno = caminhaoPequeno;
        this.zonaInicial = zonaInicial;
        this.duracaoViagem = 0;
    }

    public CaminhaoPequeno getCaminhao() {
        return this.caminhaoPequeno;
    }

    public int getDuracaoViagem() {
        return duracaoViagem;
    }

    /**
     * Retorna a zona inicial de onde o caminhão partiu.
     * @return A zona de origem.
     */
    public Zonas getZona() {
        return this.zonaInicial;
    }


    @Override
    public String toString() {
        return String.format("EventoTransferencia | Caminhão %s | Zona %s | Horário: %s",
                caminhaoPequeno.getId(),
                zonaInicial.getNome(),
                Timer.formatarHorarioSimulado(getTempo()));
    }

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