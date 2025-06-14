package configsimulador;

import caminhoes.CaminhaoGrande;
import caminhoes.CaminhaoPequeno;
import estacoes.EstacaoDeTransferencia;
import eventos.DistribuirRota;
import eventos.GerenciadorAgenda;
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

    // Nota: Os arrays abaixo não são diretamente inicializados ou utilizados em sua forma de array no código atual.
    // As entidades são gerenciadas através de Listas e instâncias individuais.

    /**
     * Array de caminhões grandes utilizados na simulação.
     * (Atualmente não inicializado diretamente aqui, os caminhões grandes são criados sob demanda em estações).
     */
    CaminhaoGrande[] caminhoesGrandes;

    /**
     * Array de caminhões pequenos utilizados na simulação.
     * (Atualmente gerenciado via {@link Lista} no método {@code iniciar()}).
     */
    CaminhaoPequeno[] caminhaoPequenos;

    /**
     * Array de estações de transferência definidas na simulação.
     * (Atualmente gerenciado via instâncias diretas no método {@code iniciar()}).
     */
    EstacaoDeTransferencia[] estacaoDeTransferencias;

    /**
     * Array de zonas que participam da simulação.
     * (Atualmente gerenciado via {@link Lista} no método {@code inicializarZonas()}).
     */
    Zonas[] zona;

    /**
     * Tempo total da simulação em unidades de tempo (minutos).
     * (Atualmente não é o critério de parada principal, a simulação termina quando não há mais eventos).
     */
    float tempoSimulacao; // O tipo float pode não ser ideal para representar minutos inteiros

    /**
     * Flag para verificar se a simulação está em andamento.
     * (Atualmente não utilizada para controlar o loop principal do simulador, que é orientado a eventos).
     */
    boolean rodando;

    /**
     * Construtor da classe Simulador.
     * Inicializa as variáveis de controle da simulação.
     * O tempo de simulação e o status "rodando" são propriedades que poderiam ser usadas para
     * um controle de simulação baseado em tempo fixo, mas a implementação atual é baseada em eventos.
     */
    public Simulador() {
        // O valor de 'tempoSimulacao' precisa ser definido com um valor significativo se for usado como limite.
        // Por exemplo, ConfiguracoesDoSimulador.DURACAO_TOTAL_SIMULACAO;
        this.tempoSimulacao = 0; // Inicializa para um valor padrão.
        this.rodando = false; // Inicia a simulação como não rodando até que 'iniciar()' seja chamado.
    }

    /**
     * Inicia a execução da simulação de coleta de lixo.
     * <p>
     * Este método realiza os passos de configuração inicial, como:
     * <ul>
     * <li>Inicialização das estações de transferência.</li>
     * <li>Configuração da associação entre zonas e estações.</li>
     * <li>Geração inicial de lixo em todas as zonas.</li>
     * <li>Distribuição de rotas para os caminhões pequenos.</li>
     * <li>Processamento de todos os eventos agendados até o fim da simulação.</li>
     * </ul>
     * Ao final, exibe um resumo dos resultados da simulação.
     */
    public void iniciar() {
        System.out.println("=================== S I M U L A D O R ==================");
        System.out.println("Iniciando simulação de coleta de lixo em Teresina");

        // 1. Inicializa as estações de transferência
        // Estas estações são pontos onde os caminhões pequenos descarregam e o lixo é transferido para caminhões grandes.
        EstacaoDeTransferencia estA = new EstacaoDeTransferencia("Estação A");
        EstacaoDeTransferencia estB = new EstacaoDeTransferencia("Estação B");

        // 2. Configura a associação entre estações de transferência e zonas
        // A classe DistanciaZonas é utilizada para determinar qual estação de transferência é responsável por qual zona.
        DistanciaZonas.configurar(estA, estB);

        // 3. Inicializa as zonas participantes da simulação
        // As zonas são criadas com parâmetros de geração de lixo definidos em ConfiguracoesDoSimulador.
        Lista<Zonas> zonas = inicializarZonas();

        // 4. Gera lixo diário inicial para cada zona
        // Simula o acúmulo de lixo nas zonas antes do início das operações de coleta.
        for (int i = 0; i < zonas.getTamanho(); i++) {
            zonas.getValor(i).gerarLixoDiario();
        }

        // 5. Distribui os caminhões para as rotas definidas
        // Cria e configura os caminhões pequenos, atribuindo-lhes zonas específicas para coleta.
        // O número de caminhões e viagens por caminhão são parâmetros da simulação.
        Lista<CaminhaoPequeno> caminhoes = DistribuirRota.distribuir(
                zonas,
                ConfiguracoesDoSimulador.MAX_VIAGENS_DIARIAS_PEQUENO, // Exemplo de uso de constante
                ConfiguracoesDoSimulador.MAX_VIAGENS_DIARIAS_PEQUENO // Usando a mesma constante para viagens, pode ser diferente
        );

        // Define a simulação como rodando. Embora 'rodando' não controle o loop diretamente aqui,
        // pode ser usado para um controle de estado externo ou para futuras expansões.
        this.rodando = true;

        // 6. Processa todos os eventos agendados
        // O coração da simulação, onde os eventos (coleta, transferência, geração de lixo, etc.) são executados
        // em sua ordem cronológica até que não haja mais eventos na agenda.
        GerenciadorAgenda.processarEventos();

        // Define a simulação como não rodando após o processamento de todos os eventos.
        this.rodando = false;

        // 7. Recupera o tempo final da simulação
        // O tempo total decorrido é o tempo do último evento processado.
        int tempoFinal = GerenciadorAgenda.getTempoUltimoEvento();

        // 8. Finaliza a simulação e exibe os resultados
        System.out.println("===========================================================");
        System.out.println("Simulação finalizada com sucesso!");
        System.out.println("Tempo total de simulação: " + Timer.formatarDuracao(tempoFinal)
                + " (encerra às " + Timer.formatarHorarioSimulado(tempoFinal) + ")");
        System.out.println("\n[LIXO RESTANTE NAS ZONAS]");

        // Exibe a quantidade final de lixo acumulado em cada zona.
        for (int i = 0; i < zonas.getTamanho(); i++) {
            Zonas zona = zonas.getValor(i);
            System.out.println("• " + zona.getNome() + ": " + zona.getLixoAcumulado() + "t");
        }
        System.out.println("===========================================================");

        // Informação sobre o último evento processado.
        System.out.println("Último evento processado: " + GerenciadorAgenda.getUltimoEvento());
    }

    /**
     * Inicializa as zonas de coleta parametrizadas com base nas configurações do simulador.
     * As zonas são adicionadas à lista em uma ordem específica.
     *
     * @return Uma {@link Lista} contendo as instâncias das zonas configuradas.
     */
    public Lista<Zonas> inicializarZonas() {
        Lista<Zonas> zonas = new Lista<>();
        // As zonas são instanciadas usando métodos estáticos de ZonasParametradas,
        // que recuperam os limites de lixo de ConfiguracoesDoSimulador.
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
     * Atualmente, apenas exibe uma mensagem no console. Para uma pausa real,
     * seria necessário implementar um mecanismo de suspensão do processamento de eventos.
     */
    public void pausar() {
        System.out.println("Simulação pausada.");
        this.rodando = false;
    }

    /**
     * Retoma a execução da simulação após uma pausa.
     * <p>
     * Atualmente, apenas exibe uma mensagem no console. Para uma retomada real,
     * seria necessário continuar o processamento de eventos.
     */
    public void continuarSimulacao() {
        System.out.println("Simulação retomada.");
        this.rodando = true;
    }

    /**
     * Finaliza a simulação.
     * <p>
     * Este método pode ser expandido para realizar ações de limpeza,
     * salvar resultados ou gerar relatórios finais.
     */
    public void encerrar() {
        System.out.println("Simulação encerrada manualmente.");
        this.rodando = false;
        // Lógica de finalização, como geração de relatórios ou salvamento de estado.
    }
}