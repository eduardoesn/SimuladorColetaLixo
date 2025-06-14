package caminhoes;

import static configsimulador.ConfiguracoesDoSimulador.CAPACIDADE_CAMINHAO_GRANDE;

/**
 * Representa um caminhão de grande porte utilizado para o transporte de lixo
 * até o aterro sanitário. Este tipo de caminhão é utilizado nas estações de
 * transferência para consolidar a carga de diversos caminhões pequenos.
 *
 * Possui controle de carga, identificação, e regras para partida ao aterro.
 */
public class CaminhaoGrande {

    /**
     * Identificador único do caminhão.
     * É atribuído automaticamente no momento da criação de cada nova instância.
     */
    private int id;

    /**
     * Contador estático para atribuição sequencial de IDs aos caminhões.
     */
    private static int contadorIds = 1;

    /**
     * Capacidade máxima de carga do caminhão em quilogramas.
     * Por padrão, é definida como 20.000 kg (20 toneladas), conforme configuração do simulador.
     */
    private final int capacidadeMaxima = CAPACIDADE_CAMINHAO_GRANDE;

    /**
     * Quantidade atual de carga no caminhão (em kg).
     * Inicialmente definido como zero.
     */
    private int cargaAtual;

    /**
     * Indica se o caminhão está carregado.
     * Pode ser utilizado para controle de estado dentro da estação de transferência.
     */
    private boolean estaCarregado;

    /**
     * Construtor padrão da classe.
     * Inicializa o caminhão com carga igual a zero e atribui um ID único.
     * O estado inicial é de "carregado", assumindo que ele está pronto para novas operações.
     */
    public CaminhaoGrande() {
        this.id = contadorIds++;
        this.cargaAtual = 0;
        estaCarregado = true;
    }

    /**
     * Verifica se o caminhão atingiu sua capacidade máxima e está pronto para
     * partir para o aterro sanitário.
     *
     * @return true se a carga atual for igual ou superior à capacidade máxima.
     */
    public boolean prontoParaPartida() {
        return cargaAtual >= capacidadeMaxima;
    }

    /**
     * Adiciona uma quantidade de lixo à carga atual do caminhão.
     * Se a soma ultrapassar a capacidade máxima, a carga é ajustada ao limite.
     *
     * @param quantidade Quantidade de lixo a ser adicionada (em kg).
     */
    public void adicionarCarga(int quantidade) {
        if (cargaAtual + quantidade > capacidadeMaxima) {
            cargaAtual = capacidadeMaxima;
        } else {
            cargaAtual += quantidade;
        }
    }

    /**
     * Simula o descarregamento completo do caminhão no aterro sanitário.
     * Após o descarregamento, a carga é zerada e o estado "carregado" é definido como falso.
     * Também imprime uma mensagem indicando a quantidade transportada.
     */
    public void descarregar() {
        System.out.println("Caminhão grande partiu para o aterro com " + cargaAtual + "kg.");
        cargaAtual = 0;
        estaCarregado = false;

        // Lógica adicional pode ser implementada aqui futuramente.
        /*
         * - Tolerância de espera
         * - Despacho automático após determinado tempo
         * - Aguardando carregamento completo
         */
    }

    /**
     * Simula o comportamento de espera do caminhão na estação de transferência,
     * aguardando ser carregado.
     *
     * @return Sempre retorna true (pode ser adaptado para refletir o estado real).
     */
    public boolean aguardandoCarregamento() {
        return true;
    }

    /**
     * Retorna o identificador único do caminhão.
     *
     * @return ID do caminhão.
     */
    public int getId() {
        return id;
    }

    /**
     * Retorna a capacidade máxima de carga do caminhão.
     *
     * @return Capacidade máxima em kg.
     */
    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    /**
     * Retorna a carga atual presente no caminhão.
     *
     * @return Carga atual em kg.
     */
    public int getCargaAtual() {
        return cargaAtual;
    }

    /**
     * Verifica se o caminhão está atualmente carregado.
     *
     * @return true se está carregado, false se está descarregado.
     */
    public boolean getEstaCarregado() {
        return estaCarregado;
    }
}
