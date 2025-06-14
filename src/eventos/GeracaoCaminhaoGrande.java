package eventos;

import estacoes.EstacaoDeTransferencia;
import timer.Timer;

/**
 * Evento responsável por gerar um novo caminhão grande em uma estação de transferência.
 * Este evento é agendado quando um caminhão pequeno precisa descarregar na estação,
 * mas não há um caminhão grande disponível ou o caminhão grande existente está cheio
 * e o tempo máximo de espera foi excedido.
 * <p>
 * Garante que a operação da estação não seja interrompida por falta de capacidade
 * para transportar o lixo ao aterro sanitário.
 */
public class GeracaoCaminhaoGrande extends Evento {

    /**
     * A {@link EstacaoDeTransferencia} onde será avaliada a necessidade de gerar um novo caminhão grande.
     */
    private EstacaoDeTransferencia estacao;

    /**
     * Construtor do evento de geração de caminhão grande.
     *
     * @param tempo    Tempo simulado (em minutos) em que o evento será executado.
     * @param estacao  A {@link EstacaoDeTransferencia} onde o caminhão grande será gerado, se necessário.
     * @throws IllegalArgumentException se o tempo for negativo ou a estação for nula.
     */
    public GeracaoCaminhaoGrande(int tempo, EstacaoDeTransferencia estacao) {
        super(tempo);
        if (estacao == null) {
            throw new IllegalArgumentException("A estação para geração de caminhão grande não pode ser nula.");
        }
        this.estacao = estacao;
    }

    /**
     * Retorna uma representação textual do evento de geração de caminhão grande,
     * incluindo o nome da estação e o horário simulado.
     *
     * @return Uma {@code String} formatada com os detalhes do evento.
     */
    @Override
    public String toString() {
        return String.format("EventoGeracaoCaminhaoGrande | Estação %s | Horário: %s",
                estacao.getNomeEstacao(),
                Timer.formatarHorarioSimulado(getTempo()));
    }

    /**
     * Executa o evento de geração de caminhão grande.
     * <p>
     * Verifica se já existe um caminhão grande disponível na estação que ainda não está pronto para partida.
     * Se não houver, um novo caminhão grande é criado e associado à estação, e a fila de espera de caminhões
     * pequenos é tentada ser descarregada.
     */
    @Override
    public void executar() {
        System.out.println("== GERAÇÃO DE CAMINHÃO GRANDE ==");
        System.out.printf("[%s] [Estação %s]%n", Timer.formatarHorarioSimulado(getTempo()), estacao.getNomeEstacao());

        // Se já houver um caminhão grande disponível e ele não estiver pronto para partida (ou seja, ainda pode carregar),
        // não há necessidade de gerar outro neste momento.
        if (estacao.getCaminhaoGrandeDisponivel()) {
            System.out.println("  • Caminhão grande já disponível na estação. Geração cancelada.");
            return;
        }

        // Caso contrário, um novo caminhão grande é gerado para atender à demanda da estação.
        System.out.println("  • Tempo máximo de espera atingido. Gerando novo caminhão grande para a estação.");
        estacao.gerarNovoCaminhaoGrande(getTempo());
        System.out.println(); // Linha em branco para melhor legibilidade
    }
}