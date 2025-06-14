package estacoes;

import caminhoes.CaminhaoPequeno;
import caminhoes.CaminhaoGrande;
import configsimulador.ConfiguracoesDoSimulador;
import eventos.GeracaoCaminhaoGrande;
import eventos.GerenciadorAgenda;
import tads.Fila;
import timer.Timer;

/**
 * Classe que representa uma Estação de Transferência no sistema de simulação de coleta de lixo.
 * <p>
 * Uma Estação de Transferência é responsável por:
 * <ul>
 * <li>Receber caminhões pequenos que chegam com lixo das zonas.</li>
 * <li>Descarregar as cargas dos caminhões pequenos em um caminhão grande.</li>
 * <li>Gerenciar uma fila de espera para caminhões pequenos, caso o caminhão grande não esteja disponível.</li>
 * <li>Acionar a geração de um novo caminhão grande se o tempo de espera exceder um limite.</li>
 * <li>Despachar o caminhão grande para o aterro sanitário quando ele atinge sua capacidade.</li>
 * </ul>
 */
public class EstacaoDeTransferencia {

    /** Nome identificador da estação de transferência (ex: "Estação A", "Estação B"). */
    private String nomeEstacao;

    /**
     * Fila estática de caminhões pequenos aguardando para descarregar na estação.
     * É estática para simular uma fila única compartilhada entre todas as estações (se aplicável,
     * mas no contexto atual, cada estação tem sua própria instância de EstacaoDeTransferencia e,
     * portanto, uma fila logicamente separada, a menos que o design seja global).
     * O ideal seria que a fila não fosse estática se cada estação tem a sua.
     * MUDANÇA PROPOSTA: Tornar a fila não estática para que cada instância de EstaçãoDeTransferencia
     * tenha sua própria fila de caminhões pequenos.
     */
    private Fila<CaminhaoPequeno> filaCaminhoesPequeos;

    /**
     * Caminhão grande atualmente disponível para receber cargas na estação.
     * Quando este caminhão fica cheio ou é despachado, um novo pode ser gerado.
     */
    private CaminhaoGrande caminhaoGrandeReceber;

    /**
     * Construtor da estação de transferência.
     * Inicializa a estação com um nome e cria um novo caminhão grande para receber lixo.
     *
     * @param nomeEstacao Nome identificador da estação.
     */
    public EstacaoDeTransferencia(String nomeEstacao) {
        this.nomeEstacao = nomeEstacao;
        this.filaCaminhoesPequeos = new Fila<>(); // Instancia a fila para esta estação
        this.caminhaoGrandeReceber = new CaminhaoGrande(); // Um novo caminhão grande é gerado ao iniciar a estação
    }

    /**
     * Retorna o nome da estação de transferência.
     *
     * @return O nome da estação.
     */
    public String getNomeEstacao() {
        return nomeEstacao;
    }

    /**
     * Retorna a fila de caminhões pequenos aguardando para descarregar nesta estação.
     *
     * @return A {@link Fila} de caminhões pequenos.
     */
    public Fila<CaminhaoPequeno> getFilaCaminhoesPequeos() {
        return filaCaminhoesPequeos;
    }

    /**
     * Verifica se há um caminhão grande disponível na estação para receber carga
     * e se ele ainda não está pronto para partida (ou seja, não está cheio).
     *
     * @return {@code true} se o caminhão grande estiver presente e ainda puder receber carga,
     * {@code false} caso contrário (se for nulo, pronto para partida, ou em trânsito).
     */
    public boolean getCaminhaoGrandeDisponivel() {
        return caminhaoGrandeReceber != null && !caminhaoGrandeReceber.prontoParaPartida();
    }

    /**
     * Método responsável por receber um caminhão pequeno que chegou à estação de transferência.
     * <p>
     * Lida com a lógica de descarregamento da carga do caminhão pequeno:
     * <ul>
     * <li>Se não houver caminhão grande disponível ou o atual estiver cheio, o caminhão pequeno
     * é adicionado a uma fila de espera. Um evento para gerar um novo caminhão grande
     * pode ser agendado se necessário.</li>
     * <li>Se um caminhão grande estiver disponível e puder receber carga, o caminhão pequeno
     * descarrega sua carga diretamente, e qualquer evento de geração de caminhão grande
     * anteriormente agendado para este caminhão pequeno é cancelado.</li>
     * <li>Se o caminhão grande ficar cheio após o descarregamento, ele é despachado para o aterro.</li>
     * </ul>
     *
     * @param caminhao   O {@link CaminhaoPequeno} que chegou à estação.
     * @param tempoAtual O tempo atual da simulação em minutos.
     */
    public void receberCaminhaoPequeno(CaminhaoPequeno caminhao, int tempoAtual) {
        System.out.println("== ESTAÇÃO ==");
        System.out.printf("[%s]%n", Timer.formatarHorarioSimulado(tempoAtual));
        System.out.printf("[%s | Caminhão %s]%n", nomeEstacao, caminhao.getId());
        System.out.println("  → Chegada confirmada.");

        // Verifica se há um caminhão grande disponível para receber carga
        if (caminhaoGrandeReceber == null || caminhaoGrandeReceber.prontoParaPartida()) {
            // Se não houver caminhão grande disponível ou o atual estiver cheio, o caminhão pequeno entra na fila.
            filaCaminhoesPequeos.enqueue(caminhao);
            System.out.printf("  • Fila de espera de caminhões pequenos aumentou. Tamanho: %d%n", filaCaminhoesPequeos.getTamanho());

            // Agenda um evento para gerar um caminhão grande se o caminhão pequeno atual não tiver um evento agendado.
            // Isso evita agendar múltiplos eventos para o mesmo caminhão pequeno.
            if (caminhao.getEventoAgendado() == null) {
                // O tempo limite para a geração de um caminhão grande.
                // Atualmente 100 unidades de tempo após o tempo atual.
                int tempoLimite = tempoAtual + 100;
                GeracaoCaminhaoGrande eventoGeracao = new GeracaoCaminhaoGrande(tempoLimite, this);
                GerenciadorAgenda.adicionarEvento(eventoGeracao);
                caminhao.setEventoAgendado(eventoGeracao); // Associa o evento ao caminhão pequeno
                System.out.printf("  • Evento para gerar caminhão grande agendado para %s%n",
                        Timer.formatarHorarioSimulado(tempoLimite));
            }
        } else {
            // Se um caminhão grande estiver disponível e puder receber carga:
            // Cancela qualquer evento de geração de caminhão grande anteriormente agendado para este caminhão pequeno.
            if (caminhao.getEventoAgendado() != null) {
                GerenciadorAgenda.removerEvento(caminhao.getEventoAgendado());
                caminhao.setEventoAgendado(null);
                System.out.println("  • Evento anterior para geração de caminhão grande cancelado.");
            }

            // Realiza o descarregamento da carga do caminhão pequeno para o caminhão grande.
            int cargaDescarregada = caminhao.getCargaAtual();
            // Calcula o tempo necessário para descarregar a carga com base na quantidade.
            int tempoDescarga = cargaDescarregada * ConfiguracoesDoSimulador.TEMPO_DESCARGA_TONELADA;

            caminhaoGrandeReceber.adicionarCarga(cargaDescarregada); // Adiciona a carga ao caminhão grande
            caminhao.descarregarCarga(); // Esvazia o caminhão pequeno

            System.out.printf("  • Caminhão pequeno %s descarregou: %dt. Carga do Caminhão Grande %d: %d/%d t%n",
                    caminhao.getId(), cargaDescarregada, caminhaoGrandeReceber.getId(),
                    caminhaoGrandeReceber.getCargaAtual(), caminhaoGrandeReceber.getCapacidadeMaxima());
            System.out.printf("  • Horário previsto para fim da descarga: %s. Tempo de Descarga: %s%n",
                    Timer.formatarHorarioSimulado(tempoDescarga + tempoAtual), Timer.formatarDuracao(tempoDescarga));

            // Marca o caminhão pequeno como pronto para sua próxima atividade
            // (retornar para coleta ou aguardar próxima rota, dependendo da lógica do simulador).
            // A mensagem abaixo sugere um retorno, mas a lógica de re-agendamento ocorre em ColetaLixo.
            System.out.printf("  → Caminhão %s volta para atividades.%n", caminhao.getId());

            // Verifica se o caminhão grande ficou cheio após o descarregamento.
            if (caminhaoGrandeReceber.prontoParaPartida()) {
                System.out.println("  • Caminhão grande " + caminhaoGrandeReceber.getId() + " cheio! Pronto para o aterro.");
                caminhaoGrandeReceber.descarregar(); // Despacha o caminhão grande para o aterro

                // Tenta descarregar caminhões que estavam na fila de espera, já que o caminhão grande foi despachado.
                descarregarFilaEspera(tempoAtual);
            }
        }
        System.out.println(); // Linha em branco para melhor legibilidade
    }

    /**
     * Descarrega caminhões pequenos da fila de espera no caminhão grande disponível.
     * Este método é chamado quando um novo caminhão grande é gerado ou um caminhão grande existente é despachado.
     * Remove os eventos de geração de caminhão grande agendados para os caminhões que são descarregados.
     *
     * @param tempoAtual O tempo atual da simulação em minutos.
     */
    private void descarregarFilaEspera(int tempoAtual) {
        System.out.printf("[%s] [ESTAÇÃO %s] Tentando descarregar fila de espera...%n",
                Timer.formatarHorarioSimulado(tempoAtual), nomeEstacao);

        // Continua descarregando enquanto houver caminhões na fila e o caminhão grande não estiver cheio.
        while (!filaCaminhoesPequeos.estaVazia() && !caminhaoGrandeReceber.prontoParaPartida()) {
            CaminhaoPequeno caminhaoFila = filaCaminhoesPequeos.poll(); // Remove o primeiro caminhão da fila (FIFO)

            // Cancela o evento de geração de caminhão grande que poderia ter sido agendado para este caminhão.
            if (caminhaoFila.getEventoAgendado() != null) {
                GerenciadorAgenda.removerEvento(caminhaoFila.getEventoAgendado());
                caminhaoFila.setEventoAgendado(null); // Remove a associação para evitar referências inválidas
            }

            int carga = caminhaoFila.getCargaAtual();
            caminhaoGrandeReceber.adicionarCarga(carga); // Adiciona a carga do caminhão pequeno ao caminhão grande

            System.out.printf("[ESTAÇÃO %s] Caminhão pequeno %s da fila descarregou %d toneladas. Carga do Grande: %d/%d t%n",
                    nomeEstacao, caminhaoFila.getId(), carga,
                    caminhaoGrandeReceber.getCargaAtual(), caminhaoGrandeReceber.getCapacidadeMaxima());

            caminhaoFila.descarregarCarga(); // Esvazia o caminhão pequeno da fila.
        }
        if (filaCaminhoesPequeos.estaVazia()) {
            System.out.println("  • Fila de espera de caminhões pequenos vazia.");
        } else {
            System.out.println("  • Caminhão grande ficou cheio. " + filaCaminhoesPequeos.getTamanho() + " caminhões ainda na fila.");
        }
    }

    /**
     * Gera um novo caminhão grande para a estação e tenta descarregar a fila de caminhões pequenos.
     * Este método é acionado por um evento {@link GeracaoCaminhaoGrande} quando o tempo de espera é excedido.
     *
     * @param tempoAtual O tempo atual da simulação em minutos.
     */
    public void gerarNovoCaminhaoGrande(int tempoAtual) {
        this.caminhaoGrandeReceber = new CaminhaoGrande(); // Cria uma nova instância de caminhão grande
        System.out.println("[ESTAÇÃO " + nomeEstacao + "] Novo caminhão grande " + caminhaoGrandeReceber.getId() + " gerado.");
        // Após gerar um novo caminhão grande, tenta esvaziar a fila de espera.
        descarregarFilaEspera(tempoAtual);
    }
}