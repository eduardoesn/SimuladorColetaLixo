package eventos;

import caminhoes.CaminhaoGrande;
import estacoes.EstacaoDeTransferencia;
import timer.Timer;

/**
 * Evento que representa a partida de um caminhão grande de uma estação
 * em direção ao aterro sanitário.
 */
public class PartidaCaminhaoGrande extends Evento {

    private final CaminhaoGrande caminhaoGrande;
    private final EstacaoDeTransferencia estacaoOrigem;

    public PartidaCaminhaoGrande(int tempo, CaminhaoGrande caminhaoGrande, EstacaoDeTransferencia estacaoOrigem) {
        super(tempo);
        this.caminhaoGrande = caminhaoGrande;
        this.estacaoOrigem = estacaoOrigem;
    }

    public CaminhaoGrande getCaminhaoGrande() {
        return caminhaoGrande;
    }

    public EstacaoDeTransferencia getEstacaoOrigem() {
        return estacaoOrigem;
    }

    @Override
    public void executar() {
        // A lógica principal é a visual, tratada na MainFX.
        // O descarregamento da carga já foi simulado no momento da criação do evento.
        System.out.println("Caminhão grande " + caminhaoGrande.getId() + " partiu da " + estacaoOrigem.getNomeEstacao() + " para o aterro.");
    }

    @Override
    public String toString() {
        return String.format("EventoPartidaCaminhaoGrande | Caminhão Grande %d | Da Estação %s | Horário: %s",
                caminhaoGrande.getId(),
                estacaoOrigem.getNomeEstacao(),
                Timer.formatarHorarioSimulado(getTempo()));
    }
}