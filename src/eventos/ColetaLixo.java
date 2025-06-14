package eventos;

import caminhoes.CaminhaoPequeno;
import configsimulador.ConfiguracoesDoSimulador;
import timer.Timer;
import zonas.Zonas;
import timer.TimerDetalhado;

/**
 * Representa um evento de coleta de lixo em uma zona específica da cidade.
 * Esse evento é executado por um caminhão pequeno que coleta lixo até atingir sua capacidade
 * ou até que não haja mais lixo disponível na zona.
 * <p>
 * Se o caminhão ainda puder realizar viagens após uma coleta, um novo evento de coleta é agendado.
 * Caso contrário, o próximo evento será a transferência do lixo para uma estação.
 */
public class ColetaLixo extends Evento {

    /**
     * Caminhão pequeno responsável por realizar a coleta.
     */
    private CaminhaoPequeno caminhao;

    /**
     * Zona onde a coleta de lixo será realizada.
     */
    private Zonas zonaAtual;

    /**
     * Construtor do evento de coleta de lixo.
     *
     * @param tempo     Tempo simulado (em minutos) no qual o evento será executado.
     * @param caminhao  Caminhão pequeno designado para a coleta.
     * @param zonaAtual Zona onde ocorrerá a coleta de lixo.
     */
    public ColetaLixo(int tempo, CaminhaoPequeno caminhao, Zonas zonaAtual) {
        super(tempo);
        this.caminhao = caminhao;
        this.zonaAtual = zonaAtual;
    }

    /**
     * Retorna uma descrição textual do evento, incluindo caminhão, zona e horário.
     *
     * @return Representação em string do evento.
     */
    @Override
    public String toString() {
        return String.format("EventoColeta | Caminhão %s | Zona %s | Horário: %s",
                caminhao.getId(),
                zonaAtual.getNome(),
                Timer.formatarHorarioSimulado(getTempo()));
    }

    /**
     * Executa o evento de coleta de lixo. O caminhão coleta lixo da zona até atingir
     * sua capacidade ou até a zona não ter mais lixo.
     * Dependendo da situação, agenda uma nova coleta ou a transferência para a estação.
     */
    @Override
    public void executar() {
        // Verifica se ainda há lixo na zona
        int qtdZona = zonaAtual.getLixoAcumulado();
        if (qtdZona == 0) {
            System.out.println("  • Zona está limpa. Nenhuma coleta realizada.");

            caminhao.registrarViagem();

            if (caminhao.podeViajarNovamente()) {
                // Agenda nova tentativa após tempo de espera
                int tempoDeEspera = 30;
                GerenciadorAgenda.adicionarEvento(new ColetaLixo(tempo + tempoDeEspera, caminhao, caminhao.getDestinoZona()));
            } else {
                // Vai direto para estação de transferência
                GerenciadorAgenda.adicionarEvento(new TransferenciaParaEstacao(tempo, caminhao, caminhao.getDestinoZona()));
            }
            return;
        }

        boolean coletou = false;
        int totalColetado = 0;

        // Loop de coleta enquanto houver capacidade e lixo na zona
        while (caminhao.podeViajarNovamente() &&
                caminhao.getCargaAtual() < caminhao.getCapacidadeMaxima() &&
                zonaAtual.getLixoAcumulado() > 0) {

            int qtdDisponivelZona = zonaAtual.getLixoAcumulado();
            int espacoRestante = caminhao.getCapacidadeMaxima() - caminhao.getCargaAtual();
            int qtdReal = Math.min(qtdDisponivelZona, espacoRestante);

            // Exibe informações da coleta
            String horarioAtual = Timer.formatarHorarioSimulado(tempo);
            System.out.println("== C O L E T A ==");
            System.out.printf("[%s] \n", horarioAtual);
            System.out.printf("[COLETA] Caminhão %s → Zona %s | %s Viagens %n", caminhao.getId(), zonaAtual.getNome(), caminhao.getViagensRestantes());

            coletou = caminhao.coletarCarga(qtdReal);
            if (coletou) {
                zonaAtual.coletarLixo(qtdReal);
                totalColetado += qtdReal;
                System.out.printf("  • Coletou: %dt    Carga: %d/%d%n",
                        qtdReal, caminhao.getCargaAtual(), caminhao.getCapacidadeMaxima());
            } else {
                System.out.println("  • Carga máxima atingida.");
                break;
            }
        }

        // Define o que acontece após a coleta
        if (caminhao.podeViajarNovamente() && coletou) {
            int tempoAtual = tempo;

            // Calcula o tempo detalhado com base na quantidade coletada
            TimerDetalhado tempoDetalhado = Timer.calcularTimerDetalhado(tempoAtual, totalColetado, false);

            System.out.printf("  • Tempo de coleta: %s%n", Timer.formatarDuracao(tempoDetalhado.tempoColeta));
            System.out.printf("  • Tempo de trajeto: %s%n", Timer.formatarDuracao(tempoDetalhado.tempoDeslocamento));
            if (tempoDetalhado.tempoExtraCarregado > 0)
                System.out.printf("  • Carga cheia: +%s%n", Timer.formatarDuracao(tempoDetalhado.tempoExtraCarregado));

            System.out.printf("  • Horário: %s    Tempo total: %s%n",
                    Timer.formatarHorarioSimulado(tempoAtual + tempoDetalhado.tempoTotal),
                    Timer.formatarDuracao(tempoDetalhado.tempoTotal)
            );
            System.out.println();

            // Agenda próxima coleta
            GerenciadorAgenda.adicionarEvento(new ColetaLixo(tempoAtual + tempoDetalhado.tempoTotal, caminhao, zonaAtual));
        } else {
            // Finaliza as coletas e envia caminhão para a estação
            GerenciadorAgenda.adicionarEvento(new TransferenciaParaEstacao(tempo, caminhao, zonaAtual));
        }
    }
}
