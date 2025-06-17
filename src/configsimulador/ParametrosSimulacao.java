package configsimulador;

/**
 * Contêiner de dados para armazenar os parâmetros de configuração da simulação
 * fornecidos pela interface do usuário (GUI).
 * <p>
 * Isso permite passar de forma organizada todas as configurações necessárias
 * para inicializar o simulador.
 */
public class ParametrosSimulacao {
    private final int numCaminhoes2t;
    private final int viagensCaminhoes2t;
    private final int numCaminhoes4t;
    private final int viagensCaminhoes4t;
    private final int numCaminhoes8t;
    private final int viagensCaminhoes8t;
    private final int numCaminhoes10t;
    private final int viagensCaminhoes10t;
    private final int horasASimular;


    /**
     * Construtor para os parâmetros da simulação.
     *
     * @param numCaminhoes2t      Número de caminhões de 2 toneladas.
     * @param viagensCaminhoes2t  Número de viagens para os caminhões de 2t.
     * @param numCaminhoes4t      Número de caminhões de 4 toneladas.
     * @param viagensCaminhoes4t  Número de viagens para os caminhões de 4t.
     * @param numCaminhoes8t      Número de caminhões de 8 toneladas.
     * @param viagensCaminhoes8t  Número de viagens para os caminhões de 8t.
     * @param numCaminhoes10t     Número de caminhões de 10 toneladas.
     * @param viagensCaminhoes10t Número de viagens para os caminhões de 10t.
     * @param horasASimular       O número de horas que a simulação deve ser executada.
     */
    public ParametrosSimulacao(int numCaminhoes2t, int viagensCaminhoes2t, int numCaminhoes4t, int viagensCaminhoes4t, int numCaminhoes8t, int viagensCaminhoes8t, int numCaminhoes10t, int viagensCaminhoes10t, int horasASimular) {
        this.numCaminhoes2t = numCaminhoes2t;
        this.viagensCaminhoes2t = viagensCaminhoes2t;
        this.numCaminhoes4t = numCaminhoes4t;
        this.viagensCaminhoes4t = viagensCaminhoes4t;
        this.numCaminhoes8t = numCaminhoes8t;
        this.viagensCaminhoes8t = viagensCaminhoes8t;
        this.numCaminhoes10t = numCaminhoes10t;
        this.viagensCaminhoes10t = viagensCaminhoes10t;
        this.horasASimular = horasASimular;
    }

    // Getters

    /**
     * @return O número de caminhões de 2 toneladas.
     */
    public int getNumCaminhoes2t() { return numCaminhoes2t; }

    /**
     * @return O número de viagens para os caminhões de 2 toneladas.
     */
    public int getViagensCaminhoes2t() { return viagensCaminhoes2t; }

    /**
     * @return O número de caminhões de 4 toneladas.
     */
    public int getNumCaminhoes4t() { return numCaminhoes4t; }

    /**
     * @return O número de viagens para os caminhões de 4 toneladas.
     */
    public int getViagensCaminhoes4t() { return viagensCaminhoes4t; }

    /**
     * @return O número de caminhões de 8 toneladas.
     */
    public int getNumCaminhoes8t() { return numCaminhoes8t; }

    /**
     * @return O número de viagens para os caminhões de 8 toneladas.
     */
    public int getViagensCaminhoes8t() { return viagensCaminhoes8t; }

    /**
     * @return O número de caminhões de 10 toneladas.
     */
    public int getNumCaminhoes10t() { return numCaminhoes10t; }

    /**
     * @return O número de viagens para os caminhões de 10 toneladas.
     */
    public int getViagensCaminhoes10t() { return viagensCaminhoes10t; }

    /**
     * @return O número de horas a simular.
     */
    public int getHorasASimular() { return horasASimular; }
}