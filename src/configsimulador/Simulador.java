package configsimulador;

import caminhoes.CaminhaoGrande;
import caminhoes.CaminhaoPequeno;
import estacoes.EstacaoDeTransferencia;
import eventos.ColetaLixo;
import eventos.DistribuirRota;
import eventos.GerenciadorAgenda;
import eventos.VerificarFimDeTurno;
import tads.Lista;
import timer.Timer;
import zonas.DistanciaZonas;
import zonas.Zonas;
import zonas.ZonasParametradas;

/**
 * Classe principal que orquestra a simulação de coleta de lixo.
 * É responsável por inicializar todos os componentes do sistema,
 * como zonas, caminhões e estações, e por controlar o fluxo geral da simulação,
 * incluindo a geração de relatórios e estatísticas.
 */
public class Simulador {

    // As listas agora são variáveis de instância para serem acessadas pelo supervisor
    private Lista<Zonas> zonas;
    private Lista<CaminhaoPequeno> caminhoes;

    private static long tempoTotalEspera = 0;
    private static int totalCaminhoesNaFila = 0;
    private static int contadorCaminhoesExtra = 0;

    /**
     * Inicializa o estado da simulação com base nos parâmetros fornecidos.
     * Configura as zonas, estações de transferência, caminhões e agenda os eventos iniciais.
     *
     * @param params Os parâmetros de simulação, geralmente fornecidos pela interface do usuário.
     * @return A lista de zonas inicializadas para que a GUI possa observá-las.
     */
    public Lista<Zonas> inicializar(ParametrosSimulacao params) {
        System.out.println("=================== S I M U L A D O R ==================");
        System.out.println("Inicializando estado da simulação com parâmetros da UI.");

        GerenciadorAgenda.reset();
        resetEstatisticas();

        EstacaoDeTransferencia estA = new EstacaoDeTransferencia("Estação A");
        EstacaoDeTransferencia estB = new EstacaoDeTransferencia("Estação B");
        DistanciaZonas.configurar(estA, estB);

        this.zonas = inicializarZonas();
        for (int i = 0; i < this.zonas.getTamanho(); i++) {
            this.zonas.getValor(i).gerarLixoDiario();
        }

        // O método distribuir agora retorna a lista de caminhões criados
        this.caminhoes = DistribuirRota.distribuir(this.zonas, params);

        // Agenda o primeiro evento de verificação do supervisor para o final de um turno de 8h
        GerenciadorAgenda.adicionarEvento(new VerificarFimDeTurno(480, this));

        System.out.println("Estado inicial configurado. A simulação irá rodar por " + params.getHorasASimular() + " horas simuladas.");
        return this.zonas;
    }

    /**
     * Adiciona um caminhão extra à simulação para lidar com o lixo restante.
     * @param tempoAtual O tempo atual para agendar a coleta.
     * @param zonaAlvo A zona com mais lixo que precisa de coleta.
     */
    public void adicionarCaminhaoExtra(int tempoAtual, Zonas zonaAlvo) {
        contadorCaminhoesExtra++;
        String id = "EXTRA-" + contadorCaminhoesExtra;
        // Cria um caminhão padrão de 4T com apenas 1 viagem
        CaminhaoPequeno caminhaoExtra = new CaminhaoPequeno(id, 4, 1, zonaAlvo);
        this.caminhoes.adicionar(this.caminhoes.getTamanho(), caminhaoExtra);

        // Agenda a coleta para este caminhão
        GerenciadorAgenda.adicionarEvento(new ColetaLixo(tempoAtual, caminhaoExtra, zonaAlvo));
        System.out.printf("  • Caminhão de reforço %s (Cap: 4t) enviado para a zona %s.%n", id, zonaAlvo.getNome());
    }

    /**
     * Verifica se algum caminhão na frota ainda tem viagens restantes.
     * @return true se pelo menos um caminhão está ativo, false caso contrário.
     */
    public boolean verificarCaminhoesAtivos() {
        for (int i = 0; i < caminhoes.getTamanho(); i++) {
            if (caminhoes.getValor(i).getViagensRestantes() > 0) {
                return true; // Encontrou um caminhão ativo
            }
        }
        return false; // Nenhum caminhão ativo
    }

    /**
     * Encontra a zona que atualmente tem a maior quantidade de lixo acumulado.
     * @return A instância da zona com mais lixo, ou null se todas estiverem limpas.
     */
    public Zonas getZonaComMaisLixo() {
        Zonas zonaMaisSucia = null;
        int maxLixo = 0;
        for (int i = 0; i < zonas.getTamanho(); i++) {
            Zonas zonaAtual = zonas.getValor(i);
            if (zonaAtual.getLixoAcumulado() > maxLixo) {
                maxLixo = zonaAtual.getLixoAcumulado();
                zonaMaisSucia = zonaAtual;
            }
        }
        return zonaMaisSucia;
    }

    /**
     * Exibe um relatório final no console com as estatísticas da simulação.
     * Inclui o tempo total, lixo restante por zona e métricas de desempenho.
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
        System.out.println("• Caminhões de reforço acionados: " + contadorCaminhoesExtra);
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
     * Cria e retorna a lista de zonas da cidade com suas configurações padrão.
     * @return Uma {@link Lista} de objetos {@link Zonas}.
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
     * Registra o tempo de espera de um caminhão na fila de uma estação
     * para cálculo de estatísticas.
     * @param tempoEspera O tempo (em minutos) que o caminhão esperou.
     */
    public static void registrarTempoEspera(int tempoEspera) {
        if (tempoEspera > 0) {
            tempoTotalEspera += tempoEspera;
            totalCaminhoesNaFila++;
        }
    }

    /**
     * Reseta todas as variáveis estáticas de estatísticas para o início de uma nova simulação.
     */
    public static void resetEstatisticas() {
        tempoTotalEspera = 0;
        totalCaminhoesNaFila = 0;
        contadorCaminhoesExtra = 0;
        CaminhaoGrande.resetContador();
    }
}