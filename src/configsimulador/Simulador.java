package configsimulador;

import caminhoes.CaminhaoPequeno;
import estacoes.EstacaoDeTransferencia;
import eventos.DistribuirRota;
import eventos.GerenciadorAgenda;
import tads.Lista;
import timer.Timer;
import zonas.DistanciaZonas;
import zonas.Zonas;
import zonas.ZonasParametradas;

/**
 * Classe principal responsável por gerenciar a simulação da coleta de lixo.
 * Foi modificada para ser inicializada com parâmetros dinâmicos vindos de uma UI.
 */
public class Simulador {

    private boolean rodando = false;

    /**
     * Inicia a execução da simulação de coleta de lixo com base nos parâmetros fornecidos.
     *
     * @param params Objeto contendo todas as configurações da simulação (nº de caminhões, viagens, etc.).
     */
    public void iniciar(ParametrosSimulacao params) {
        System.out.println("=================== S I M U L A D O R ==================");
        System.out.println("Iniciando simulação de coleta de lixo em Teresina com parâmetros da UI.");

        // Limpa a agenda de eventos de simulações anteriores para garantir um início limpo
        GerenciadorAgenda.reset();

        // 1. Inicializa as estações de transferência
        EstacaoDeTransferencia estA = new EstacaoDeTransferencia("Estação A");
        EstacaoDeTransferencia estB = new EstacaoDeTransferencia("Estação B");

        // 2. Configura a associação entre estações de transferência e zonas
        DistanciaZonas.configurar(estA, estB);

        // 3. Inicializa as zonas participantes da simulação
        Lista<Zonas> zonas = inicializarZonas();

        // 4. Gera lixo diário inicial para cada zona
        for (int i = 0; i < zonas.getTamanho(); i++) {
            zonas.getValor(i).gerarLixoDiario();
        }

        // 5. Distribui os caminhões para as rotas com base nos parâmetros da UI
        DistribuirRota.distribuir(zonas, params);

        // 6. Processa todos os eventos agendados
        this.rodando = true;
        GerenciadorAgenda.processarEventos();
        this.rodando = false;

        // 7. Recupera o tempo final e exibe os resultados
        int tempoFinal = GerenciadorAgenda.getTempoUltimoEvento();
        System.out.println("===========================================================");
        System.out.println("Simulação finalizada com sucesso!");
        System.out.println("Tempo total de simulação: " + Timer.formatarDuracao(tempoFinal)
                + " (encerra às " + Timer.formatarHorarioSimulado(tempoFinal) + ")");
        System.out.println("\n[LIXO RESTANTE NAS ZONAS]");

        for (int i = 0; i < zonas.getTamanho(); i++) {
            Zonas zona = zonas.getValor(i);
            System.out.println("• " + zona.getNome() + ": " + zona.getLixoAcumulado() + "t");
        }
        System.out.println("===========================================================");
        System.out.println("Último evento processado: " + GerenciadorAgenda.getUltimoEvento());
    }

    /**
     * Inicializa as zonas de coleta parametrizadas.
     *
     * @return Uma {@link Lista} contendo as instâncias das zonas configuradas.
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
     * Finaliza a simulação.
     */
    public void encerrar() {
        System.out.println("Simulação encerrada manualmente.");
        this.rodando = false;
    }
}