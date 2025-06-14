package estacoes;

import caminhoes.CaminhaoPequeno;
import caminhoes.CaminhaoGrande;
import configsimulador.ConfiguracoesDoSimulador;
import eventos.GeracaoCaminhaoGrande;
import eventos.GerenciadorAgenda;
import tads.Fila;
import timer.Timer;

/**
 * Classe que representa uma Estação de Transferência no sistema de simulação.
 * Responsável por receber caminhões pequenos, descarregar suas cargas em um caminhão grande
 * e gerenciar a fila de espera e eventos de geração de caminhão grande.
 */
public class EstacaoDeTransferencia {

    /** Nome identificador da estação de transferência. */
    public String nomeEstacao;

    /** Fila estática de caminhões pequenos aguardando descarregamento. */
    private static Fila<CaminhaoPequeno> filaCaminhoesPequeos = new Fila<>();

    /** Caminhão grande atualmente disponível para receber cargas na estação. */
    private CaminhaoGrande caminhaoGrandeReceber;

    /**
     * Construtor da estação de transferência.
     *
     * @param nomeEstacao Nome identificador da estação.
     */
    public EstacaoDeTransferencia(String nomeEstacao) {
        this.nomeEstacao = nomeEstacao;
        this.caminhaoGrandeReceber = new CaminhaoGrande();
    }

    /**
     * Retorna o nome da estação.
     *
     * @return Nome da estação.
     */
    public String getNomeEstacao() {
        return nomeEstacao;
    }

    /**
     * Retorna a fila de caminhões pequenos da estação.
     *
     * @return Fila de caminhões pequenos.
     */
    public static Fila<CaminhaoPequeno> getFilaCaminhoesPequeos() {
        return filaCaminhoesPequeos;
    }

    /**
     * Verifica se há caminhão grande disponível e ainda não pronto para partida.
     *
     * @return {@code true} se o caminhão grande estiver disponível e não pronto para partida.
     */
    public boolean getCaminhaoGrandeDisponivel() {
        return caminhaoGrandeReceber != null && !caminhaoGrandeReceber.prontoParaPartida();
    }

    /**
     * Método responsável por receber um caminhão pequeno na estação.
     * Caso não haja caminhão grande disponível ou este esteja cheio, o caminhão pequeno entra na fila.
     * Caso contrário, sua carga é descarregada diretamente no caminhão grande.
     *
     * @param caminhao Caminhão pequeno que chegou à estação.
     * @param tempoAtual Tempo atual da simulação.
     */
    public void receberCaminhaoPequeno(CaminhaoPequeno caminhao, int tempoAtual) {
        System.out.println("== ESTAÇÃO ==");
        System.out.printf("[%s]%n", Timer.formatarHorarioSimulado(tempoAtual));
        System.out.printf("[%s | Caminhão %s]%n", nomeEstacao, caminhao.getId());
        System.out.println("  → Chegada confirmada.");

        if (caminhaoGrandeReceber == null || caminhaoGrandeReceber.prontoParaPartida()) {
            filaCaminhoesPequeos.enqueue(caminhao);
            System.out.printf("  • Fila de espera aumentou. Tamanho: %d%n", filaCaminhoesPequeos.getTamanho());

            if (caminhao.getEventoAgendado() == null) {
                int tempoLimite = tempoAtual + 100; // 100 unidades de tempo (equivalente a 30 minutos)
                GeracaoCaminhaoGrande evento = new GeracaoCaminhaoGrande(tempoLimite, this);
                GerenciadorAgenda.adicionarEvento(evento);
                caminhao.setEventoAgendado(evento);
                System.out.printf("  • Evento para gerar caminhão grande agendado para %s%n", Timer.formatarDuracao(tempoLimite));
            }
        } else {
            if (caminhao.getEventoAgendado() != null) {
                GerenciadorAgenda.removerEvento(caminhao.getEventoAgendado());
                caminhao.setEventoAgendado(null);
                System.out.println("  • Evento anterior para geração de caminhão grande cancelado.");
            }

            int carga = caminhao.getCargaAtual();
            int tempoDescarga = carga * ConfiguracoesDoSimulador.TEMPO_DESCARGA_TONELADA;

            caminhaoGrandeReceber.adicionarCarga(carga);
            caminhao.descarregarCarga();

            System.out.printf("  • Descarregou: %dt    Carga: %d/%d%n", carga, caminhao.getCargaAtual(), caminhao.getCapacidadeMaxima());
            System.out.printf("  • Horário: %s     Tempo de Descarga: %s%n", Timer.formatarHorarioSimulado(tempoDescarga + tempoAtual), Timer.formatarDuracao(tempoDescarga));
            System.out.printf("  → Volta para atividadades %n", Timer.formatarHorarioSimulado(tempoDescarga + tempoAtual));

            if (caminhaoGrandeReceber.prontoParaPartida()) {
                System.out.println("  • Caminhão grande cheio!");
                System.out.println();
                System.out.printf("[COLETA] Caminhão Grande %s → Aterro%n", caminhaoGrandeReceber.getId());
                caminhaoGrandeReceber.descarregar();

                descarregarFilaEspera(tempoAtual);
            }
        }
    }

    /**
     * Descarrega caminhões pequenos da fila de espera no caminhão grande disponível.
     * Remove os eventos agendados dos caminhões descarregados.
     *
     * @param tempoAtual Tempo atual da simulação.
     */
    private void descarregarFilaEspera(int tempoAtual) {
        while (!filaCaminhoesPequeos.estaVazia() && !caminhaoGrandeReceber.prontoParaPartida()) {
            CaminhaoPequeno caminhaoFila = filaCaminhoesPequeos.poll();

            if (caminhaoFila.getEventoAgendado() != null) {
                GerenciadorAgenda.removerEvento(caminhaoFila.getEventoAgendado());
                caminhaoFila.setEventoAgendado(null);
            }

            int carga = caminhaoFila.getCargaAtual();
            caminhaoGrandeReceber.adicionarCarga(carga);
            System.out.printf("[ESTAÇÃO %s] Caminhão pequeno %s da fila descarregou %d toneladas.%n", nomeEstacao, caminhaoFila.getId(), carga);
        }
    }

    /**
     * Gera um novo caminhão grande para a estação e descarrega a fila de caminhões pequenos.
     *
     * @param tempoAtual Tempo atual da simulação.
     */
    public void gerarNovoCaminhaoGrande(int tempoAtual) {
        this.caminhaoGrandeReceber = new CaminhaoGrande();
        System.out.println("[ESTAÇÃO " + nomeEstacao + "] Novo caminhão grande criado.");
        descarregarFilaEspera(tempoAtual);
    }
}
