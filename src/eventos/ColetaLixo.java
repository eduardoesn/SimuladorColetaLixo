package eventos;

import caminhoes.CaminhaoPequeno;
import configsimulador.ConfiguracoesDoSimulador;
import timer.Timer;
import zonas.Zonas;
import timer.TimerDetalhado;

/**
 * Representa um evento de coleta de lixo em uma zona específica da cidade.
 * Este evento descreve a ação de um caminhão pequeno coletando lixo de uma zona
 * até que o caminhão atinja sua capacidade máxima ou que a zona fique sem lixo.
 * <p>
 * A lógica de execução do evento determina a próxima ação:
 * <ul>
 * <li>Se a zona estiver limpa (ou ficar limpa), o caminhão registra uma viagem e
 * pode tentar ir para a próxima zona em sua rota ou seguir para a estação de transferência.</li>
 * <li>Se o caminhão atingir sua capacidade máxima, ele é agendado para ir para a estação de transferência.</li>
 * <li>Caso contrário, o caminhão continua coletando na mesma zona ou, no futuro, poderia
 * mudar para outra zona se sua rota envolver múltiplas coletas pequenas.</li>
 * </ul>
 */
public class ColetaLixo extends Evento {

    /**
     * O caminhão pequeno responsável por realizar a coleta neste evento.
     */
    private CaminhaoPequeno caminhao;

    /**
     * A zona atual onde a coleta de lixo será realizada.
     */
    private Zonas zonaAtual;

    /**
     * Construtor do evento de coleta de lixo.
     *
     * @param tempo     Tempo simulado (em minutos) no qual o evento será executado.
     * @param caminhao  O {@link CaminhaoPequeno} designado para realizar a coleta.
     * @param zonaAtual A {@link Zonas} onde ocorrerá a coleta de lixo.
     * @throws IllegalArgumentException se o tempo for negativo, o caminhão ou a zona forem nulos.
     */
    public ColetaLixo(int tempo, CaminhaoPequeno caminhao, Zonas zonaAtual) {
        super(tempo);
        if (caminhao == null) {
            throw new IllegalArgumentException("Caminhão não pode ser nulo para um evento de coleta.");
        }
        if (zonaAtual == null) {
            throw new IllegalArgumentException("Zona não pode ser nula para um evento de coleta.");
        }
        this.caminhao = caminhao;
        this.zonaAtual = zonaAtual;
    }

    /**
     * Retorna uma descrição textual do evento de coleta, incluindo o ID do caminhão,
     * o nome da zona e o horário simulado em que o evento ocorre.
     *
     * @return Uma representação em string do evento.
     */
    @Override
    public String toString() {
        return String.format("EventoColeta | Caminhão %s | Zona %s | Horário: %s",
                caminhao.getId(),
                zonaAtual.getNome(),
                Timer.formatarHorarioSimulado(getTempo()));
    }

    // ===================================================================
    // MÉTODO ADICIONADO PARA CORRIGIR O ERRO
    // ===================================================================
    public caminhoes.CaminhaoPequeno getCaminhao() {
        return this.caminhao;
    }

    public Zonas getZona() {
        return zonaAtual;
    }
    // ===================================================================


    /**
     * Executa a lógica do evento de coleta de lixo.
     * O caminhão tenta coletar lixo da zona até que sua capacidade seja atingida
     * ou não haja mais lixo na zona. Com base no resultado da coleta e na
     * disponibilidade do caminhão, um novo evento é agendado (próxima coleta ou transferência).
     */
    @Override
    public void executar() {
        System.out.println("== C O L E T A ==");
        String horarioAtual = Timer.formatarHorarioSimulado(getTempo());
        System.out.printf("[%s] %n", horarioAtual);
        System.out.printf("[COLETA] Caminhão %s → Zona %s | Viagens restantes: %d%n",
                caminhao.getId(), zonaAtual.getNome(), caminhao.getViagensRestantes());

        // Verifica se a zona tem lixo disponível para coleta
        if (zonaAtual.getLixoAcumulado() == 0) {
            System.out.println("  • Zona está limpa. Nenhuma coleta realizada.");
            caminhao.registrarViagem(); // Mesmo que não tenha coletado, a visita conta como viagem.

            // Se o caminhão ainda pode fazer mais viagens, ele tenta ir para a próxima zona
            if (caminhao.podeViajarNovamente()) {
                // Assume um tempo de espera ou deslocamento mínimo para a próxima tentativa
                // ou deslocamento para a próxima zona na rota.
                int tempoDeEsperaOuDeslocamento = ConfiguracoesDoSimulador.VIAGEM_MIN_FORA_PICO; // Exemplo
                System.out.printf("  • Caminhão %s procurando próxima zona ou aguardando. Tempo de espera: %s%n",
                        caminhao.getId(), Timer.formatarDuracao(tempoDeEsperaOuDeslocamento));
                GerenciadorAgenda.adicionarEvento(
                        new ColetaLixo(getTempo() + tempoDeEsperaOuDeslocamento, caminhao, caminhao.getDestinoZona()));
            } else {
                // Se não pode mais viajar para coleta, o caminhão vai para a estação de transferência
                System.out.printf("  • Caminhão %s não pode mais coletar. Indo para estação de transferência.%n", caminhao.getId());
                GerenciadorAgenda.adicionarEvento(
                        new TransferenciaParaEstacao(getTempo(), caminhao, zonaAtual));
            }
            return; // Encerra a execução deste evento de coleta
        }

        boolean coletouNestaIteracao = false;
        int totalColetadoNestaOperacao = 0; // Total coletado antes de decidir a próxima ação

        // Loop para coletar lixo até a capacidade do caminhão ou o esgotamento da zona
        while (caminhao.getCargaAtual() < caminhao.getCapacidadeMaxima() &&
                zonaAtual.getLixoAcumulado() > 0) {

            int qtdDisponivelZona = zonaAtual.getLixoAcumulado();
            int espacoRestanteCaminhao = caminhao.getCapacidadeMaxima() - caminhao.getCargaAtual();
            // A quantidade real a ser coletada é o mínimo entre o disponível e o espaço restante
            int qtdParaColetar = Math.min(qtdDisponivelZona, espacoRestanteCaminhao);

            // Tenta coletar a carga
            boolean sucessoColeta = caminhao.coletarCarga(qtdParaColetar);

            if (sucessoColeta) {
                zonaAtual.coletarLixo(qtdParaColetar); // Remove o lixo da zona
                totalColetadoNestaOperacao += qtdParaColetar;
                coletouNestaIteracao = true;
                System.out.printf("  • Coletou: %dt    Carga atual: %d/%d t%n",
                        qtdParaColetar, caminhao.getCargaAtual(), caminhao.getCapacidadeMaxima());
            } else {
                // Se a coleta falhou (ex: capacidade máxima atingida), sai do loop de coleta
                System.out.println("  • Caminhão " + caminhao.getId() + " atingiu sua carga máxima.");
                break;
            }
        }

        // Determina a próxima ação após a tentativa de coleta
        if (coletouNestaIteracao) { // Se algo foi coletado nesta operação
            // Calcula os tempos detalhados para o movimento do caminhão
            TimerDetalhado tempoDetalhado = Timer.calcularTimerDetalhado(getTempo(), totalColetadoNestaOperacao, false); // 'false' pois está em coleta

            System.out.printf("  • Tempo gasto na coleta: %s%n", Timer.formatarDuracao(tempoDetalhado.tempoColeta));
            System.out.printf("  • Tempo de trajeto para coleta: %s%n", Timer.formatarDuracao(tempoDetalhado.tempoDeslocamento));
            if (tempoDetalhado.tempoExtraCarregado > 0) {
                System.out.printf("  • Tempo extra por carga cheia (se aplicável): +%s%n", Timer.formatarDuracao(tempoDetalhado.tempoExtraCarregado));
            }
            System.out.printf("  • Próximo Horário de Ação: %s    Tempo total da operação: %s%n",
                    Timer.formatarHorarioSimulado(getTempo() + tempoDetalhado.tempoTotal),
                    Timer.formatarDuracao(tempoDetalhado.tempoTotal));
            System.out.println();

            // Se o caminhão ainda pode viajar e não está cheio ou a zona ainda tem lixo, agenda próxima coleta.
            // Esta lógica pode precisar de refinamento se o caminhão muda de zona.
            if (caminhao.podeViajarNovamente() && !zonaAtual.estaLimpa() && caminhao.getCargaAtual() < caminhao.getCapacidadeMaxima()) {
                // Continua coletando na mesma zona ou vai para a próxima da rota
                GerenciadorAgenda.adicionarEvento(
                        new ColetaLixo(getTempo() + tempoDetalhado.tempoTotal, caminhao, zonaAtual)); // Mantém na mesma zona por enquanto
            } else {
                // Caso contrário (caminhão cheio, zona limpa, ou limite de viagens), vai para a estação.
                System.out.printf("  • Caminhão %s completou a coleta ou está cheio. Enviando para estação de transferência.%n", caminhao.getId());
                GerenciadorAgenda.adicionarEvento(
                        new TransferenciaParaEstacao(getTempo() + tempoDetalhado.tempoTotal, caminhao, zonaAtual));
            }
        } else {
            // Se nenhuma carga foi coletada e a zona estava vazia (tratado no início do método),
            // ou se o caminhão já estava cheio.
            // A decisão para onde o caminhão vai já foi tomada no início do método para zonas vazias.
            // Se o caminhão já estava cheio, ele deve ir para a estação.
            if (caminhao.getCargaAtual() >= caminhao.getCapacidadeMaxima()) {
                System.out.printf("  • Caminhão %s já estava cheio ou atingiu capacidade máxima. Enviando para estação de transferência.%n", caminhao.getId());
                GerenciadorAgenda.adicionarEvento(
                        new TransferenciaParaEstacao(getTempo(), caminhao, zonaAtual));
            }
            // Se não coletou e não estava cheio, significa que não havia lixo suficiente ou outra condição impediu.
            // Neste caso, a lógica acima para "zona limpa" já deve ter direcionado o caminhão.
        }
    }
}