package caminhoes;

import static configsimulador.ConfiguracoesDoSimulador.CAPACIDADE_CAMINHAO_GRANDE;

/**
 * Representa um caminhão de grande porte utilizado para o transporte de lixo
 * até o aterro sanitário. Este tipo de caminhão é utilizado nas estações de
 * transferência para consolidar a carga de diversos caminhões pequenos.
 * <p>
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
     * Garante que cada caminhão grande tenha um ID único.
     */
    private static int contadorIds = 1;

    /**
     * A capacidade máxima de carga que o caminhão pode transportar.
     */
    private final int capacidadeMaxima = CAPACIDADE_CAMINHAO_GRANDE;

    /**
     * Quantidade atual de carga no caminhão (em toneladas).
     * Inicialmente definido como zero.
     */
    private int cargaAtual;

    /**
     * Indica se o caminhão está carregado.
     * Pode ser utilizado para controle de estado dentro da estação de transferência.
     * Não é diretamente usado para controle de capacidade, mas para lógica de despacho.
     */
    private boolean estaCarregado;

    /**
     * Construtor padrão da classe CaminhaoGrande.
     * Inicializa o caminhão com carga zero e atribui um ID único.
     * O estado inicial é "carregado" ({@code true}), indicando que está disponível para carregar lixo.
     */
    public CaminhaoGrande() {
        this.id = contadorIds++;
        this.cargaAtual = 0;
        this.estaCarregado = true; // Inicia como "carregado" no sentido de estar pronto para receber carga
    }

    /**
     * Verifica se o caminhão atingiu sua capacidade máxima e está pronto para
     * partir para o aterro sanitário.
     *
     * @return {@code true} se a carga atual for igual ou superior à capacidade máxima, {@code false} caso contrário.
     */
    public boolean prontoParaPartida() {
        return cargaAtual >= capacidadeMaxima;
    }

    /**
     * Adiciona uma quantidade de lixo à carga atual do caminhão.
     * Se a soma ultrapassar a capacidade máxima, a carga é ajustada para o limite da capacidade.
     *
     * @param quantidade Quantidade de lixo a ser adicionada (em toneladas).
     */
    public void adicionarCarga(int quantidade) {
        this.cargaAtual = Math.min(this.cargaAtual + quantidade, capacidadeMaxima);
    }

    /**
     * Simula o descarregamento completo do caminhão no aterro sanitário.
     * Após o descarregamento, a carga é zerada e o estado {@code estaCarregado} é definido como {@code false}.
     */
    public void descarregar() {
        System.out.println("Caminhão grande " + id + " partiu para o aterro com " + cargaAtual + "t.");
        cargaAtual = 0;
        estaCarregado = false;
    }

    /**
     * Simula o comportamento de espera do caminhão na estação de transferência.
     * @return Sempre retorna {@code true}.
     */
    public boolean aguardandoCarregamento() {
        return true;
    }

    /**
     * Retorna o identificador único do caminhão.
     * @return O ID do caminhão.
     */
    public int getId() {
        return id;
    }

    /**
     * Retorna a capacidade máxima de carga do caminhão.
     * @return A capacidade máxima em toneladas.
     */
    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    /**
     * Retorna a carga atual presente no caminhão.
     * @return A carga atual em toneladas.
     */
    public int getCargaAtual() {
        return cargaAtual;
    }

    /**
     * Verifica se o caminhão está atualmente carregado com lixo.
     * @return {@code true} se está carregado com lixo, {@code false} se está vazio.
     */
    public boolean getEstaCarregado() {
        return estaCarregado;
    }

    /**
     * Retorna o número total de caminhões grandes instanciados.
     * @return O valor do contador de IDs.
     */
    public static int getContadorTotal() {
        return contadorIds - 1;
    }

    /**
     * Reseta o contador de IDs dos caminhões grandes para 1.
     * Útil para reiniciar o estado entre simulações.
     */
    public static void resetContador() {
        contadorIds = 1;
    }
}