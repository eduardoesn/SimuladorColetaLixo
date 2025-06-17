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
     * A {@link EstacaoDeTransferencia} que irá receber o caminhão pequeno.
     */
    private EstacaoDeTransferencia estacao;

    /**
     * O {@link CaminhaoPequeno} que está chegando à estação de transferência.
     */
    private CaminhaoPequeno caminhao;

    /**
     * Construtor do evento de chegada de caminhão a uma estação de transferência.
     *
     * @param tempo    Tempo simulado (em minutos) em que o evento ocorrerá.
     * @param estacao  Instância da estação de transferência que receberá o caminhão.
     * @param caminhao O caminhão pequeno que será processado pela estação.
     * @throws IllegalArgumentException se o tempo for negativo, ou se a estação ou o caminhão forem nulos.
     */
    public EstacaoTransferencia(int tempo, EstacaoDeTransferencia estacao, CaminhaoPequeno caminhao) {
        super(tempo);
        if (estacao == null) {
            throw new IllegalArgumentException("A estação de transferência não pode ser nula.");
        }
        if (caminhao == null) {
            throw new IllegalArgumentException("O caminhão não pode ser nulo.");
        }
        this.estacao = estacao;
        this.caminhao = caminhao;
    }

    /**
     * Retorna o caminhão associado a este evento.
     * @return O caminhão pequeno.
     */
    public CaminhaoPequeno getCaminhao() {
        return this.caminhao;
    }

    /**
     * Retorna a estação associada a este evento.
     * @return A estação de transferência.
     */
    public EstacaoDeTransferencia getEstacao() {
        return estacao;
    }


    /**
     * Retorna uma representação textual do evento, contendo o ID do caminhão,
     * o nome da estação de destino e o horário simulado do evento formatado.
     *
     * @return Uma {@code String} com a descrição detalhada do evento.
     */
    @Override
    public String toString() {
        return String.format("EventoEstacaoTransferencia | Caminhão %s | Estação %s | Horário: %s",
                caminhao.getId(),
                estacao.getNomeEstacao(),
                Timer.formatarHorarioSimulado(getTempo()));
    }

    /**
     * Executa o evento de chegada do caminhão pequeno na estação de transferência.
     * <p>
     * Este método aciona a lógica da estação para receber o caminhão, que por sua vez
     * gerencia o descarregamento do lixo e a interação com o caminhão grande,
     * ou coloca o caminhão pequeno na fila de espera.
     */
    @Override
    public void executar() {
        estacao.receberCaminhaoPequeno(caminhao, getTempo());
    }
}