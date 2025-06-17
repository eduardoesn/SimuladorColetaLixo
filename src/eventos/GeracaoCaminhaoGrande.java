package eventos;

import caminhoes.CaminhaoGrande;
import estacoes.EstacaoDeTransferencia;
import timer.Timer;

/**
 * Representa um evento que lida com a lógica de criação de um caminhão grande em uma estação de transferência.
 * Este evento é tipicamente agendado quando um caminhão pequeno chega a uma estação e não há um
 * caminhão grande disponível, ou o atual está cheio. Ele garante que, após um tempo de espera,
 * um novo caminhão grande seja providenciado se necessário.
 */
public class GeracaoCaminhaoGrande extends Evento {

    /**
     * A estação de transferência onde a geração do caminhão grande será avaliada.
     */
    private EstacaoDeTransferencia estacao;

    /**
     * Construtor para o evento de geração de caminhão grande.
     *
     * @param tempo   O tempo de simulação em que o evento ocorrerá.
     * @param estacao A estação de transferência associada a este evento.
     * @throws IllegalArgumentException se a estação for nula.
     */
    public GeracaoCaminhaoGrande(int tempo, EstacaoDeTransferencia estacao) {
        super(tempo);
        if (estacao == null) {
            throw new IllegalArgumentException("A estação para geração de caminhão grande não pode ser nula.");
        }
        this.estacao = estacao;
    }

    /**
     * Retorna a estação de transferência associada a este evento.
     *
     * @return A instância da {@link EstacaoDeTransferencia}.
     */
    public EstacaoDeTransferencia getEstacao() {
        return this.estacao;
    }

    /**
     * Retorna uma representação em string do evento, incluindo o nome da estação e o horário agendado.
     *
     * @return Uma string formatada descrevendo o evento.
     */
    @Override
    public String toString() {
        return String.format("EventoGeracaoCaminhaoGrande | Estação %s | Horário: %s",
                estacao.getNomeEstacao(),
                Timer.formatarHorarioSimulado(getTempo()));
    }

    /**
     * Executa a lógica do evento de geração de caminhão grande.
     * <p>
     * A lógica verifica várias condições:
     * <ul>
     * <li>Se a fila de caminhões pequenos estiver vazia, a geração é cancelada.</li>
     * <li>Se o caminhão grande atual tiver carga e sua tolerância de espera for atingida, ele é despachado.</li>
     * <li>Se o caminhão grande atual estiver vazio, ele continua aguardando.</li>
     * <li>Se nenhuma das condições acima for atendida (indicando que o tempo máximo de espera foi atingido),
     * um novo caminhão grande é gerado para a estação.</li>
     * </ul>
     */
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