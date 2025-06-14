package configsimulador;

import caminhoes.CaminhaoGrande;
import caminhoes.CaminhaoPequeno;
import configsimulador.ConfiguracoesDoSimulador;
import estacoes.EstacaoDeTransferencia;
import eventos.DistribuirRota;
import eventos.GerenciadorAgenda;
import eventos.ColetaLixo;
import timer.Timer;
import zonas.DistanciaZonas;
import zonas.Zonas;
import zonas.ZonasParametradas;
import tads.Lista;

/**
 * Classe principal responsável por gerenciar a simulação da coleta de lixo em zonas específicas.
 * <p>
 * Esta classe inicializa e executa a lógica de simulação, gerencia eventos, associa zonas a estações
 * de transferência e calcula os resultados da coleta.
 */
public class Simulador {

    /** Array de caminhões grandes utilizados na simulação (não inicializados diretamente neste exemplo). */
    CaminhaoGrande[] caminhoesGrandes;

    /** Array de caminhões pequenos utilizados na simulação. */
    CaminhaoPequeno[] caminhaoPequenos;

    /** Array de estações de transferência definidas na simulação. */
    EstacaoDeTransferencia[] estacaoDeTransferencias;

    /** Zonas que participam da simulação. */
    Zonas[] zona;

    /** Tempo total da simulação em unidades de tempo. */
    float tempoSimulacao;

    /** Flag para verificar se a simulação está em andamento. */
    boolean rodando;

    /**
     * Construtor da classe Simulador que inicializa as variáveis principais.
     */
    public Simulador() {
        this.tempoSimulacao = tempoSimulacao;
        this.rodando = false;
    }

    /**
     * Inicia a execução da simulação.
     * <p>
     * Este método realiza a configuração inicial, gera lixo diário nas zonas, distribui rotas para os caminhões
     * e processa os eventos agendados.
     */
    public void iniciar() {
        System.out.println("=================== S I M U L A D O R ==================");
        System.out.println("Iniciando simulação de coleta de lixo em Teresina");

        // Inicializa as estações de transferência
        EstacaoDeTransferencia estA = new EstacaoDeTransferencia("Estação A");
        EstacaoDeTransferencia estB = new EstacaoDeTransferencia("Estação B");

        // Configura a associação entre estações de transferência e zonas
        DistanciaZonas.configurar(estA, estB);

        // Inicializa as zonas participantes da simulação
        Lista<Zonas> zonas = inicializarZonas();

        // Gera lixo diário para cada zona
        for (int i = 0; i < zonas.getTamanho(); i++) {
            zonas.getValor(i).gerarLixoDiario();
        }

        // Distribui os caminhões para as rotas definidas
        Lista<CaminhaoPequeno> caminhoes = DistribuirRota.distribuir(zonas, 10, 5);

        // Processa os eventos agendados
        GerenciadorAgenda.processarEventos();

        // Recupera o tempo final da simulação
        int tempoFinal = GerenciadorAgenda.getTempoUltimoEvento();

        // Finaliza a simulação e exibe os resultados
        System.out.println("===========================================================");
        System.out.println("Simulação finalizada com sucesso!");
        System.out.println("Tempo total: " + Timer.formatarDuracao(tempoFinal)
                + " (encerra às " + Timer.formatarHorarioSimulado(tempoFinal) + ")");
        System.out.println("[LIXO FINAL]");

        for (int i = 0; i < zonas.getTamanho(); i++) {
            Zonas zona = zonas.getValor(i);
            System.out.println("• " + zona.getNome() + ": " + zona.getLixoAcumulado() + "T");
        }
        System.out.println("===========================================================");

        System.out.println("Último evento processado: " + GerenciadorAgenda.getUltimoEvento());
    }

    /**
     * Inicializa as zonas de coleta parametrizadas com base nas configurações do simulador.
     *
     * @return Uma lista contendo as zonas configuradas.
     */
    public Lista<Zonas> inicializarZonas() {
        Lista<Zonas> zonas = new Lista<>();
        zonas.adicionar(0, ZonasParametradas.zonaSul());
        zonas.adicionar(1, ZonasParametradas.zonaSudeste());
        zonas.adicionar(2, ZonasParametradas.zonaCentro());
        zonas.adicionar(3, ZonasParametradas.zonaLeste());
        zonas.adicionar(4, ZonasParametradas.zonaNorte());
        return zonas;
    }

    /**
     * Pausa a execução da simulação.
     * <p>
     * Exibe uma mensagem indicando que a simulação foi pausada.
     */
    public void pausar() {
        System.out.println("Simulação pausada.");
    }

    /**
     * Retoma a execução da simulação após pausa.
     * <p>
     * Exibe uma mensagem indicando que a simulação foi retomada.
     */
    public void continuarSimulacao() {
        System.out.println("Simulação retomada.");
    }

    /**
     * Finaliza a simulação.
     * <p>
     * Este método pode ser expandido para anunciar explicitamente a finalização ou realizar ações de limpeza.
     */
    public void encerrar() {
        // Implementação para finalizar o simulador pode ser adicionada aqui.
    }
}