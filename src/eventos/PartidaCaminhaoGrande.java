package eventos;

import caminhoes.CaminhaoGrande;
import estacoes.EstacaoDeTransferencia;
import timer.Timer;

/**
 * Evento que representa a partida de um caminhão grande de uma estação
 * de transferência em direção ao aterro sanitário.
 */
public class PartidaCaminhaoGrande extends Evento {

    /**
     * O caminhão grande que está partindo.
     */
    private final CaminhaoGrande caminhaoGrande;
    /**
     * A estação de transferência de onde o caminhão está partindo.
     */
    private final EstacaoDeTransferencia estacaoOrigem;

    /**
     * Construtor do evento de partida do caminhão grande.
     *
     * @param tempo          O tempo de simulação em que a partida ocorre.
     * @param caminhaoGrande O caminhão grande que está partindo.
     * @param estacaoOrigem  A estação de onde o caminhão parte.
     */
    public PartidaCaminhaoGrande(int tempo, CaminhaoGrande caminhaoGrande, EstacaoDeTransferencia estacaoOrigem) {
        super(tempo);
        this.caminhaoGrande = caminhaoGrande;
        this.estacaoOrigem = estacaoOrigem;
    }

    /**
     * Retorna o caminhão grande associado a este evento.
     *
     * @return A instância do {@link CaminhaoGrande}.
     */
    public CaminhaoGrande getCaminhaoGrande() {
        return caminhaoGrande;
    }

    /**
     * Retorna a estação de origem de onde o caminhão partiu.
     *
     * @return A instância da {@link EstacaoDeTransferencia}.
     */
    public EstacaoDeTransferencia getEstacaoOrigem() {
        return estacaoOrigem;
    }

    /**
     * Executa a ação do evento, que consiste principalmente em registrar
     * a partida no console. A lógica de descarregamento da carga já foi
     * simulada anteriormente, e a animação visual é tratada na classe MainFX.
     */
    @Override
    public void executar() {
        // A lógica principal é a visual, tratada na MainFX.
        // O descarregamento da carga já foi simulado no momento da criação do evento.
        System.out.println("Caminhão grande " + caminhaoGrande.getId() + " partiu da " + estacaoOrigem.getNomeEstacao() + " para o aterro.");
    }

    /**
     * Retorna uma representação em string do evento, detalhando o caminhão,
     * a estação de origem e o horário da partida.
     *
     * @return Uma string formatada descrevendo o evento.
     */
    @Override
    public String toString() {
        return String.format("EventoPartidaCaminhaoGrande | Caminhão Grande %d | Da Estação %s | Horário: %s",
                caminhaoGrande.getId(),
                estacaoOrigem.getNomeEstacao(),
                Timer.formatarHorarioSimulado(getTempo()));
    }
}