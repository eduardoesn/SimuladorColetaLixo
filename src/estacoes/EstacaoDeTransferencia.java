package estacoes;

import caminhoes.CaminhaoPequeno;
import caminhoes.CaminhaoGrande;
import configsimulador.ConfiguracoesDoSimulador;
import configsimulador.Simulador;
import eventos.ColetaLixo;
import eventos.GeracaoCaminhaoGrande;
import eventos.GerenciadorAgenda;
import tads.Fila;
import timer.Timer;

public class EstacaoDeTransferencia {

    private String nomeEstacao;
    private Fila<CaminhaoPequeno> filaCaminhoesPequeos;
    private CaminhaoGrande caminhaoGrandeReceber;

    public EstacaoDeTransferencia(String nomeEstacao) {
        this.nomeEstacao = nomeEstacao;
        this.filaCaminhoesPequeos = new Fila<>();
        this.caminhaoGrandeReceber = new CaminhaoGrande();
    }

    public String getNomeEstacao() {
        return nomeEstacao;
    }

    public Fila<CaminhaoPequeno> getFilaCaminhoesPequeos() {
        return filaCaminhoesPequeos;
    }

    public CaminhaoGrande getCaminhaoGrande() {
        return caminhaoGrandeReceber;
    }

    public void despacharCaminhaoGrande(int tempoAtual) {
        if (this.caminhaoGrandeReceber != null) {
            System.out.println("  • Despachando caminhão grande " + this.caminhaoGrandeReceber.getId() + " para o aterro.");
            GerenciadorAgenda.adicionarEvento(new eventos.PartidaCaminhaoGrande(tempoAtual, this.caminhaoGrandeReceber, this));
            this.caminhaoGrandeReceber.descarregar();
            this.caminhaoGrandeReceber = null;
            if (!filaCaminhoesPequeos.estaVazia()) {
                gerarNovoCaminhaoGrande(tempoAtual + 1);
            }
        }
    }

    public void receberCaminhaoPequeno(CaminhaoPequeno caminhao, int tempoAtual) {
        System.out.println("== ESTAÇÃO ==");
        System.out.printf("[%s]%n", Timer.formatarHorarioSimulado(tempoAtual));
        System.out.printf("[%s | Caminhão %s]%n", nomeEstacao, caminhao.getId());
        System.out.println("  → Chegada confirmada.");

        if (caminhaoGrandeReceber == null || caminhaoGrandeReceber.prontoParaPartida()) {
            caminhao.setTempoEntradaFila(tempoAtual);
            filaCaminhoesPequeos.enqueue(caminhao);
            System.out.printf("  • Fila de espera de caminhões pequenos aumentou. Tamanho: %d%n", filaCaminhoesPequeos.getTamanho());

            if (caminhao.getEventoAgendado() == null) {
                int tempoLimite = tempoAtual + ConfiguracoesDoSimulador.TEMPO_MAX_ESPERA_PEQUENO;
                GeracaoCaminhaoGrande eventoGeracao = new GeracaoCaminhaoGrande(tempoLimite, this);
                GerenciadorAgenda.adicionarEvento(eventoGeracao);
                caminhao.setEventoAgendado(eventoGeracao);
                System.out.printf("  • Evento para gerar caminhão grande agendado para %s%n",
                        Timer.formatarHorarioSimulado(tempoLimite));
            }
        } else {
            if (caminhao.getEventoAgendado() != null) {
                GerenciadorAgenda.removerEvento(caminhao.getEventoAgendado());
                caminhao.setEventoAgendado(null);
                System.out.println("  • Evento anterior para geração de caminhão grande cancelado.");
            }

            int cargaDescarregada = caminhao.getCargaAtual();
            int tempoDescarga = cargaDescarregada * ConfiguracoesDoSimulador.TEMPO_DESCARGA_TONELADA;

            caminhaoGrandeReceber.adicionarCarga(cargaDescarregada);
            caminhao.descarregarCarga();

            System.out.printf("  • Caminhão pequeno %s descarregou: %dt. Carga do Caminhão Grande %d: %d/%d t%n",
                    caminhao.getId(), cargaDescarregada, caminhaoGrandeReceber.getId(),
                    caminhaoGrandeReceber.getCargaAtual(), caminhaoGrandeReceber.getCapacidadeMaxima());
            System.out.printf("  • Horário previsto para fim da descarga: %s. Tempo de Descarga: %s%n",
                    Timer.formatarHorarioSimulado(tempoAtual + tempoDescarga), Timer.formatarDuracao(tempoDescarga));

            // LÓGICA REATORADA: Chama o método auxiliar
            agendarProximaViagem(caminhao, tempoAtual + tempoDescarga);

            if (caminhaoGrandeReceber.prontoParaPartida()) {
                despacharCaminhaoGrande(tempoAtual + tempoDescarga);
            }
        }
        System.out.println();
    }

    private void descarregarFilaEspera(int tempoAtual) {
        System.out.printf("[%s] [ESTAÇÃO %s] Tentando descarregar fila de espera...%n",
                Timer.formatarHorarioSimulado(tempoAtual), nomeEstacao);

        while (!filaCaminhoesPequeos.estaVazia() && caminhaoGrandeReceber != null && !caminhaoGrandeReceber.prontoParaPartida()) {
            CaminhaoPequeno caminhaoFila = filaCaminhoesPequeos.poll();
            int tempoEspera = tempoAtual - caminhaoFila.getTempoEntradaFila();
            Simulador.registrarTempoEspera(tempoEspera);

            if (caminhaoFila.getEventoAgendado() != null) {
                GerenciadorAgenda.removerEvento(caminhaoFila.getEventoAgendado());
                caminhaoFila.setEventoAgendado(null);
            }

            int carga = caminhaoFila.getCargaAtual();
            caminhaoGrandeReceber.adicionarCarga(carga);

            System.out.printf("[ESTAÇÃO %s] Caminhão pequeno %s da fila (esperou %s) descarregou %d toneladas. Carga do Grande: %d/%d t%n",
                    nomeEstacao, caminhaoFila.getId(), Timer.formatarDuracao(tempoEspera), carga,
                    caminhaoGrandeReceber.getCargaAtual(), caminhaoGrandeReceber.getCapacidadeMaxima());

            caminhaoFila.descarregarCarga();

            // LÓGICA REATORADA: Chama o método auxiliar
            agendarProximaViagem(caminhaoFila, tempoAtual);
        }

        if (filaCaminhoesPequeos.estaVazia()) {
            System.out.println("  • Fila de espera de caminhões pequenos vazia.");
        } else {
            System.out.println("  • Caminhão grande ficou cheio ou indisponível. " + filaCaminhoesPequeos.getTamanho() + " caminhões ainda na fila.");
        }
    }

    public void gerarNovoCaminhaoGrande(int tempoAtual) {
        this.caminhaoGrandeReceber = new CaminhaoGrande();
        System.out.println("[ESTAÇÃO " + nomeEstacao + "] Novo caminhão grande " + caminhaoGrandeReceber.getId() + " gerado.");
        descarregarFilaEspera(tempoAtual);
    }

    /**
     * MÉTODO ADICIONADO: Centraliza a lógica para enviar um caminhão de volta à sua rota.
     * Verifica se o caminhão ainda tem viagens, registra a viagem e agenda a próxima coleta.
     * @param caminhao O caminhão que terminou o descarregamento.
     * @param tempoDeSaida O tempo de simulação em que o caminhão fica disponível para a próxima tarefa.
     */
    private void agendarProximaViagem(CaminhaoPequeno caminhao, int tempoDeSaida) {
        if (caminhao.podeViajarNovamente()) {
            caminhao.registrarViagem(); // Gasta uma das viagens diárias
            int tempoDeVolta = ConfiguracoesDoSimulador.VIAGEM_MIN_FORA_PICO; // Simula o tempo de volta para a zona
            System.out.printf("  → Caminhão %s volta para atividades. Agendando próxima coleta na zona %s.%n", caminhao.getId(), caminhao.getDestinoZona().getNome());
            GerenciadorAgenda.adicionarEvento(new ColetaLixo(tempoDeSaida + tempoDeVolta, caminhao, caminhao.getDestinoZona()));
        } else {
            System.out.printf("  → Caminhão %s finalizou todas as suas viagens diárias.%n", caminhao.getId());
        }
    }
}