package eventos;

import caminhoes.CaminhaoGrande;
import estacoes.EstacaoDeTransferencia;
import timer.Timer;

public class GeracaoCaminhaoGrande extends Evento {

    private EstacaoDeTransferencia estacao;

    public GeracaoCaminhaoGrande(int tempo, EstacaoDeTransferencia estacao) {
        super(tempo);
        if (estacao == null) {
            throw new IllegalArgumentException("A estação para geração de caminhão grande não pode ser nula.");
        }
        this.estacao = estacao;
    }

    public EstacaoDeTransferencia getEstacao() {
        return this.estacao;
    }

    @Override
    public String toString() {
        return String.format("EventoGeracaoCaminhaoGrande | Estação %s | Horário: %s",
                estacao.getNomeEstacao(),
                Timer.formatarHorarioSimulado(getTempo()));
    }

    @Override
    public void executar() {
        System.out.println("== GERAÇÃO DE CAMINHÃO GRANDE ==");
        System.out.printf("[%s] [Estação %s]%n", Timer.formatarHorarioSimulado(getTempo()), estacao.getNomeEstacao());

        if (estacao.getFilaCaminhoesPequeos().estaVazia()) {
            System.out.println("  • Fila de espera está vazia. Geração de caminhão grande cancelada.");
            return;
        }

        CaminhaoGrande caminhaoGrandeAtual = estacao.getCaminhaoGrande();

        if (caminhaoGrandeAtual != null && caminhaoGrandeAtual.getCargaAtual() > 0) {
            System.out.println("  • Tolerância de espera do caminhão grande " + caminhaoGrandeAtual.getId() + " atingida. Partindo para o aterro.");
            estacao.despacharCaminhaoGrande(getTempo());
            return;
        }

        if (caminhaoGrandeAtual != null && caminhaoGrandeAtual.getCargaAtual() == 0) {
            System.out.println("  • Caminhão grande " + caminhaoGrandeAtual.getId() + " está vazio e continua aguardando.");
            return;
        }

        System.out.println("  • Tempo máximo de espera atingido para caminhão na fila. Gerando novo caminhão grande para a estação.");
        estacao.gerarNovoCaminhaoGrande(getTempo());
        System.out.println();
    }
}