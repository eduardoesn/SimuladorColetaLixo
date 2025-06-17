package configsimulador;

import caminhoes.CaminhaoGrande;
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
 * Refatorada para apenas inicializar o estado da simulação e exibir relatórios.
 */
public class Simulador {

    private static long tempoTotalEspera = 0;
    private static int totalCaminhoesNaFila = 0;

    /**
     * Prepara a simulação para ser executada. Cria as zonas, estações, caminhões
     * e os eventos iniciais, mas não inicia o loop de processamento.
     * @param params Parâmetros da simulação vindos da UI.
     * @return A lista de zonas, para ser usada no relatório final.
     */
    public Lista<Zonas> inicializar(ParametrosSimulacao params) {
        System.out.println("=================== S I M U L A D O R ==================");
        System.out.println("Inicializando estado da simulação com parâmetros da UI.");

        // Limpa a agenda de eventos e reseta estatísticas de simulações anteriores
        GerenciadorAgenda.reset();
        resetEstatisticas();

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

        System.out.println("Estado inicial configurado. A simulação irá rodar por " + params.getHorasASimular() + " horas simuladas.");

        // Retorna a lista de zonas para que o relatório final possa ser gerado
        return zonas;
    }

    /**
     * Imprime o relatório final da simulação no console.
     * @param zonas A lista de zonas para exibir o lixo restante.
     */
    public void exibirRelatorioFinal(Lista<Zonas> zonas) {
        int tempoFinal = GerenciadorAgenda.getTempoUltimoEvento();

        System.out.println("\n[PROCESSAMENTO DE EVENTOS CONCLUÍDO]");
        System.out.println("===========================================================");
        System.out.println("Simulação finalizada!");
        System.out.println("Tempo total de simulação: " + Timer.formatarDuracao(tempoFinal)
                + " (encerra às " + Timer.formatarHorarioSimulado(tempoFinal) + ")");
        System.out.println("\n[LIXO RESTANTE NAS ZONAS]");

        for (int i = 0; i < zonas.getTamanho(); i++) {
            Zonas zona = zonas.getValor(i);
            System.out.println("• " + zona.getNome() + ": " + zona.getLixoAcumulado() + "t");
        }
        System.out.println("\n[ESTATÍSTICAS FINAIS]");
        System.out.println("• Total de caminhões grandes de 20t utilizados: " + CaminhaoGrande.getContadorTotal());

        if (totalCaminhoesNaFila > 0) {
            double tempoMedioEspera = (double) tempoTotalEspera / totalCaminhoesNaFila;
            System.out.println("• Tempo médio de espera na fila da estação: " + Timer.formatarDuracao((int) tempoMedioEspera));
        } else {
            System.out.println("• Nenhum caminhão precisou esperar na fila da estação.");
        }
        System.out.println("===========================================================");
        System.out.println("Último evento processado: " + GerenciadorAgenda.getUltimoEvento());
    }

    /**
     * Inicializa as zonas de coleta parametrizadas.
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
     * Registra o tempo de espera de um caminhão que saiu da fila.
     * @param tempoEspera O tempo em minutos que o caminhão esperou.
     */
    public static void registrarTempoEspera(int tempoEspera) {
        if (tempoEspera > 0) {
            tempoTotalEspera += tempoEspera;
            totalCaminhoesNaFila++;
        }
    }

    /**
     * Reseta as estatísticas para uma nova execução da simulação.
     */
    public static void resetEstatisticas() {
        tempoTotalEspera = 0;
        totalCaminhoesNaFila = 0;
        CaminhaoGrande.resetContador(); // Adicionado reset do contador de caminhões grandes
    }
}