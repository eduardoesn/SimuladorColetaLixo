package configsimulador;


import zonas.Zonas;

/**
 * Classe contendo todos os parâmetros de configuração para a simulação.
 * <p>
 * Esta classe centraliza todas as constantes utilizadas no sistema de simulação,
 * organizadas em categorias lógicas para fácil manutenção e ajuste.
 * <p>
 * Padrão de implementação: Singleton (todos os membros são estáticos e finais)
 */

public class ConfiguracoesDoSimulador {
    // ==================== CAPACIDADE DOS CAMINHÕES ====================
    /**
     * Capacidades em toneladas para caminhões pequenos
     */
    public static final int CAPACIDADE_CAMINHAO_2T = 2;
    public static final int CAPACIDADE_CAMINHAO_4T = 4;
    public static final int CAPACIDADE_CAMINHAO_8T = 8;
    public static final int CAPACIDADE_CAMINHAO_10T = 10;

    /**
     * Capacidade em toneladas para caminhão grande
     */
    public static final int CAPACIDADE_CAMINHAO_GRANDE = 20;

    // ==================== TEMPOS DE OPERAÇÃO ====================
    /**
     * Tempo necessário para descarregar 1 tonelada de lixo (em minutos)
     */
    public static final int TEMPO_DESCARGA_TONELADA = 5;

    /**
     * Tempo necessário para coletar 1 tonelada de lixo (em minutos)
     */
    public static final int TEMPO_COLETA_TONELADA = 10;

    /**
     * Tempo máximo de espera de um caminhão pequeno na estação (em minutos)
     */
    public static final int TEMPO_MAX_ESPERA_PEQUENO = 15;

    /**
     * Tolerância máxima de espera de um caminhão grande na estação (em minutos)
     */
    public static final int TOLERANCIA_ESPERA_GRANDE = 20;

    // ==================== CONFIGURAÇÕES DE VIAGEM ====================
    /**
     * Tempo mínimo de viagem em horário de pico (em minutos)
     */
    public static final int VIAGEM_MIN_PICO = 30;

    /**
     * Tempo máximo de viagem em horário de pico (em minutos)
     */
    public static final int VIAGEM_MAX_PICO = 60;

    /**
     * Tempo mínimo de viagem fora de pico (em minutos)
     */
    public static final int VIAGEM_MIN_FORA_PICO = 20;

    /**
     * Tempo máximo de viagem fora de pico (em minutos)
     */
    public static final int VIAGEM_MAX_FORA_PICO = 40;

    /**
     * Número máximo de viagens que um caminhão pequeno pode realizar por dia
     */
    public static final int MAX_VIAGENS_DIARIAS_PEQUENO = 3;

    // ==================== GERAÇÃO DE LIXO POR ZONA ====================
    /**
     * Faixas de geração de lixo por zona (em toneladas/dia)
     */
    public static final int LIXO_MIN_ZONA_SUL = 20;
    public static final int LIXO_MAX_ZONA_SUL = 40;
    public static final int LIXO_MIN_ZONA_NORTE = 15;
    public static final int LIXO_MAX_ZONA_NORTE = 30;
    public static final int LIXO_MIN_ZONA_CENTRO = 10;
    public static final int LIXO_MAX_ZONA_CENTRO = 20;
    public static final int LIXO_MIN_ZONA_LESTE = 15;
    public static final int LIXO_MAX_ZONA_LESTE = 25;
    public static final int LIXO_MIN_ZONA_SUDESTE = 18;
    public static final int LIXO_MAX_ZONA_SUDESTE = 35;

    // ==================== MULTIPLICADORES DE TEMPO ====================
    /**
     * Multiplicador de tempo para viagens durante o horário de pico
     */
    public static final double MULTIPLICADOR_TEMPO_PICO = 1.5;

    /**
     * Multiplicador de tempo para viagens fora do horário de pico
     */
    public static final double MULTIPLICADOR_TEMPO_FORA_PICO = 1.0;

    // ==================== HORÁRIOS OPERACIONAIS ====================
    /**
     * Início e fim do horário de pico da manhã (formato 24h)
     */
    public static final int HORA_INICIO_PICO_MANHA = 7;
    public static final int HORA_FIM_PICO_MANHA = 9;

    /**
     * Início e fim do horário de pico da tarde (formato 24h)
     */
    public static final int HORA_INICIO_PICO_TARDE = 17;
    public static final int HORA_FIM_PICO_TARDE = 19;

    /**
     * Intervalo de horário reservado para almoço
     */
    public static final int HORA_INICIO_ALMOCO = 12;
    public static final int HORA_FIM_ALMOCO = 14;

    // ==================== PARÂMETROS DA SIMULAÇÃO ====================
    /**
     * Duração total da simulação em minutos (24 horas)
     */
    public static final int DURACAO_TOTAL_SIMULACAO = 1440;

    /**
     * Número de estações de transferência disponíveis
     */
    public static final int NUM_TOTAL_ESTACOES = 2;

    /**
     * Verifica se uma hora específica está dentro do horário de pico.
     *
     * @param hora Hora do dia (formato 24h)
     * @return true se estiver no horário de pico, false caso contrário
     */
    public static boolean isHorarioDePico(int hora) {
        return (hora >= HORA_INICIO_PICO_MANHA && hora < HORA_FIM_PICO_MANHA)
                || (hora >= HORA_INICIO_PICO_TARDE && hora < HORA_FIM_PICO_TARDE);
    }

    /**
     * Construtor privado para evitar instanciação.
     * <p>
     * Esta classe é um utilitário com apenas membros estáticos.
     */
    private ConfiguracoesDoSimulador() {
        // Previne instanciação
    }
}
