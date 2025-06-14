package eventos;

import caminhoes.CaminhaoPequeno;
import estacoes.EstacaoDeTransferencia;
import timer.Timer;

/**
 * Representa um evento de chegada de um caminhão pequeno a uma estação de transferência.
 * Este evento é responsável por acionar o processo de descarregamento de lixo
 * do caminhão pequeno para o sistema da estação de transferência.
 */
public class EstacaoTransferencia extends Evento {

    /**
     * Estação de transferência que irá receber o caminhão pequeno.
     */
    private EstacaoDeTransferencia estacao;

    /**
     * Caminhão pequeno que está chegando à estação de transferência.
     */
    private CaminhaoPequeno caminhao;

    /**
     * Construtor do evento de estação de transferência.
     *
     * @param tempo    Tempo simulado (em minutos) em que o evento ocorrerá.
     * @param estacao  Instância da estação de transferência que receberá o caminhão.
     * @param caminhao Caminhão pequeno que será processado pela estação.
     */
    public EstacaoTransferencia(int tempo, EstacaoDeTransferencia estacao, CaminhaoPequeno caminhao) {
        super(tempo);
        this.estacao = estacao;
        this.caminhao = caminhao;
    }

    /**
     * Retorna uma representação textual do evento, contendo o ID do caminhão,
     * o nome da estação e o horário do evento formatado.
     *
     * @return String com a descrição do evento.
     */
    @Override
    public String toString() {
        return String.format("EventoEstacaoTransferencia | Caminhão %s | Estação %s | Horário: %s",
                caminhao.getId(),
                estacao.getNomeEstacao(),
                Timer.formatarHorarioSimulado(tempo));
    }

    /**
     * Executa o evento, acionando a lógica da estação de transferência para
     * processar o caminhão pequeno que está chegando.
     */
    @Override
    public void executar() {
        estacao.receberCaminhaoPequeno(caminhao, tempo);
    }
}
