package eventos;

import configsimulador.Simulador;
import timer.Timer;

/**
 * Evento supervisor que verifica o estado da simulação periodicamente.
 * Se todos os caminhões terminaram suas viagens e ainda há lixo,
 * ele pode acionar a geração de novos caminhões de reforço.
 */
public class VerificarFimDeTurno extends Evento {

    /**
     * Referência à instância principal do simulador para consulta do estado do sistema.
     */
    private final Simulador simulador;

    /**
     * Construtor do evento supervisor de fim de turno.
     *
     * @param tempo     O tempo de simulação em que a verificação ocorrerá.
     * @param simulador A instância do simulador a ser verificada.
     */
    public VerificarFimDeTurno(int tempo, Simulador simulador) {
        super(tempo);
        this.simulador = simulador;
    }

    /**
     * Executa a lógica de verificação do supervisor.
     * <p>
     * O método primeiro verifica se há caminhões ainda trabalhando.
     * Se não houver, ele verifica se ainda há lixo nas zonas.
     * Caso haja lixo, ele solicita um caminhão de reforço para a zona mais suja.
     * Se não houver mais lixo, a operação do dia é considerada encerrada.
     * Em todos os casos, exceto no encerramento, uma nova verificação é agendada.
     */
    @Override
    public void executar() {
        System.out.printf("== SUPERVISOR ==%n[%s] Verificando estado da simulação...%n", Timer.formatarHorarioSimulado(getTempo()));

        boolean algumCaminhaoTrabalhando = simulador.verificarCaminhoesAtivos();

        // Se ainda há caminhões com viagens a fazer, não faz nada e agenda a próxima verificação.
        if (algumCaminhaoTrabalhando) {
            System.out.println("  • Ainda há caminhões em atividade. Próxima verificação agendada.");
            agendarProximaVerificacao();
            return;
        }

        System.out.println("  • Todos os caminhões finalizaram suas rotas.");

        // Se não há caminhões trabalhando, verifica se há lixo restante.
        zonas.Zonas zonaMaisSucia = simulador.getZonaComMaisLixo();

        if (zonaMaisSucia != null) {
            System.out.printf("  • Lixo encontrado na Zona %s (%d T). Solicitando caminhão de reforço!%n",
                    zonaMaisSucia.getNome(), zonaMaisSucia.getLixoAcumulado());

            // Pede ao simulador para adicionar um caminhão extra
            simulador.adicionarCaminhaoExtra(getTempo(), zonaMaisSucia);
        } else {
            System.out.println("  • Não há mais lixo nas zonas. Operação do dia encerrada.");
            // Não agenda a próxima verificação, permitindo que a simulação termine.
            return;
        }

        // Agenda a próxima verificação para continuar monitorando.
        agendarProximaVerificacao();
    }

    /**
     * Agenda a próxima execução deste evento supervisor para um momento futuro.
     * A verificação ocorre a cada 2 horas simuladas (120 minutos).
     */
    private void agendarProximaVerificacao() {
        // Verifica novamente a cada 2 horas simuladas (120 minutos)
        GerenciadorAgenda.adicionarEvento(new VerificarFimDeTurno(getTempo() + 120, simulador));
    }

    /**
     * Retorna uma representação em string do evento, incluindo seu tipo e horário agendado.
     *
     * @return Uma string formatada descrevendo o evento.
     */
    @Override
    public String toString() {
        return String.format("EventoVerificarFimDeTurno | Horário: %s", Timer.formatarHorarioSimulado(getTempo()));
    }
}