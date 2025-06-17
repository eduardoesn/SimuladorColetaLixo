package eventos;

import configsimulador.Simulador;
import timer.Timer;

/**
 * Evento supervisor que verifica o estado da simulação periodicamente.
 * Se todos os caminhões terminaram suas viagens e ainda há lixo,
 * ele pode acionar a geração de novos caminhões.
 */
public class VerificarFimDeTurno extends Evento {

    private final Simulador simulador;

    public VerificarFimDeTurno(int tempo, Simulador simulador) {
        super(tempo);
        this.simulador = simulador;
    }

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

    private void agendarProximaVerificacao() {
        // Verifica novamente a cada 2 horas simuladas (120 minutos)
        GerenciadorAgenda.adicionarEvento(new VerificarFimDeTurno(getTempo() + 120, simulador));
    }

    @Override
    public String toString() {
        return String.format("EventoVerificarFimDeTurno | Horário: %s", Timer.formatarHorarioSimulado(getTempo()));
    }
}