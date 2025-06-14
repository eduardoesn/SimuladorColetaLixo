package configsimulador;

/**
 * Classe contendo todos os parâmetros de configuração para a simulação de coleta de lixo.
 * <p>
 * Esta classe centraliza todas as constantes utilizadas no sistema de simulação,
 * organizadas em categorias lógicas para fácil manutenção e ajuste.
 * É implementada como uma classe utilitária com membros estáticos e finais para
 * evitar instanciação e garantir acesso global e imutável aos parâmetros.
 */
public final class ConfiguracoesDoSimulador { // Marcada como final para prevenir herança

    // ==================== CAPACIDADE DOS CAMINHÕES ====================
    /**
     * Capacidade em toneladas para caminhões pequenos: 2 toneladas.
     */
    public static final int CAPACIDADE_CAMINHAO_2T = 2;
    /**
     * Capacidade em toneladas para caminhões pequenos: 4 toneladas.
     */
    public static final int CAPACIDADE_CAMINHAO_4T = 4;
    /**
     * Capacidade em toneladas para caminhões pequenos: 8 toneladas.
     */
    public static final int CAPACIDADE_CAMINHAO_8T = 8;
    /**
     * Capacidade em toneladas para caminhões pequenos: 10 toneladas.
     */
    public static final int CAPACIDADE_CAMINHAO_10T = 10;

    /**
     * Capacidade em toneladas para o caminhão grande de transferência.
     * Define o volume máximo de lixo que um caminhão grande pode transportar para o aterro.
     */
    public static final int CAPACIDADE_CAMINHAO_GRANDE = 20;

    // ==================== TEMPOS DE OPERAÇÃO ====================
    /**
     * Tempo necessário para descarregar 1 tonelada de lixo (em minutos) em uma estação de transferência.
     */
    public static final int TEMPO_DESCARGA_TONELADA = 5;

    /**
     * Tempo necessário para coletar 1 tonelada de lixo (em minutos) em uma zona.
     */
    public static final int TEMPO_COLETA_TONELADA = 10;

    /**
     * Tempo máximo de espera de um caminhão pequeno na estação de transferência (em minutos)
     * antes de uma decisão ser tomada sobre seu descarregamento ou realocação.
     * (Atualmente não utilizada diretamente para esta lógica no código fornecido).
     */
    public static final int TEMPO_MAX_ESPERA_PEQUENO = 15;

    /**
     * Tolerância máxima de espera de um caminhão grande na estação (em minutos)
     * antes que uma ação (como a geração de um novo caminhão grande) seja considerada.
     */
    public static final int TOLERANCIA_ESPERA_GRANDE = 20;

    // ==================== CONFIGURAÇÕES DE VIAGEM ====================
    /**
     * Tempo mínimo de viagem em horário de pico (em minutos).
     */
    public static final int VIAGEM_MIN_PICO = 30;

    /**
     * Tempo máximo de viagem em horário de pico (em minutos).
     */
    public static final int VIAGEM_MAX_PICO = 60;

    /**
     * Tempo mínimo de viagem fora do horário de pico (em minutos).
     */
    public static final int VIAGEM_MIN_FORA_PICO = 20;

    /**
     * Tempo máximo de viagem fora do horário de pico (em minutos).
     */
    public static final int VIAGEM_MAX_FORA_PICO = 40;

    /**
     * Número máximo de viagens que um caminhão pequeno pode realizar por dia.
     * Determina a capacidade diária de trabalho de cada caminhão pequeno.
     */
    public static final int MAX_VIAGENS_DIARIAS_PEQUENO = 3;

    // ==================== GERAÇÃO DE LIXO POR ZONA ====================
    /**
     * Limite mínimo de geração de lixo diário para a zona "Sul" (em toneladas/dia).
     */
    public static final int LIXO_MIN_ZONA_SUL = 20;
    /**
     * Limite máximo de geração de lixo diário para a zona "Sul" (em toneladas/dia).
     */
    public static final int LIXO_MAX_ZONA_SUL = 40;
    /**
     * Limite mínimo de geração de lixo diário para a zona "Norte" (em toneladas/dia).
     */
    public static final int LIXO_MIN_ZONA_NORTE = 15;
    /**
     * Limite máximo de geração de lixo diário para a zona "Norte" (em toneladas/dia).
     */
    public static final int LIXO_MAX_ZONA_NORTE = 30;
    /**
     * Limite mínimo de geração de lixo diário para a zona "Centro" (em toneladas/dia).
     */
    public static final int LIXO_MIN_ZONA_CENTRO = 10;
    /**
     * Limite máximo de geração de lixo diário para a zona "Centro" (em toneladas/dia).
     */
    public static final int LIXO_MAX_ZONA_CENTRO = 20;
    /**
     * Limite mínimo de geração de lixo diário para a zona "Leste" (em toneladas/dia).
     */
    public static final int LIXO_MIN_ZONA_LESTE = 15;
    /**
     * Limite máximo de geração de lixo diário para a zona "Leste" (em toneladas/dia).
     */
    public static final int LIXO_MAX_ZONA_LESTE = 25;
    /**
     * Limite mínimo de geração de lixo diário para a zona "Sudeste" (em toneladas/dia).
     */
    public static final int LIXO_MIN_ZONA_SUDESTE = 18;
    /**
     * Limite máximo de geração de lixo diário para a zona "Sudeste" (em toneladas/dia).
     */
    public static final int LIXO_MAX_ZONA_SUDESTE = 35;

    // ==================== MULTIPLICADORES DE TEMPO ====================
    /**
     * Multiplicador de tempo aplicado a viagens durante o horário de pico.
     * Um valor de 1.5 significa que o tempo de viagem é 50% maior no pico.
     */
    public static final double MULTIPLICADOR_TEMPO_PICO = 1.5;

    /**
     * Multiplicador de tempo aplicado a viagens fora do horário de pico.
     * Um valor de 1.0 significa que o tempo de viagem não sofre alteração.
     */
    public static final double MULTIPLICADOR_TEMPO_FORA_PICO = 1.0;

    // ==================== HORÁRIOS OPERACIONAIS ====================
    /**
     * Hora de início do período de pico da manhã (formato 24h).
     */
    public static final int HORA_INICIO_PICO_MANHA = 7;
    /**
     * Hora de fim do período de pico da manhã (formato 24h).
     */
    public static final int HORA_FIM_PICO_MANHA = 9;

    /**
     * Hora de início do período de pico da tarde (formato 24h).
     */
    public static final int HORA_INICIO_PICO_TARDE = 17;
    /**
     * Hora de fim do período de pico da tarde (formato 24h).
     */
    public static final int HORA_FIM_PICO_TARDE = 19;

    /**
     * Hora de início do intervalo de horário reservado para almoço.
     * (Atualmente não utilizada diretamente para controle de operações no código fornecido).
     */
    public static final int HORA_INICIO_ALMOCO = 12;
    /**
     * Hora de fim do intervalo de horário reservado para almoço.
     * (Atualmente não utilizada diretamente para controle de operações no código fornecido).
     */
    public static final int HORA_FIM_ALMOCO = 14;

    // ==================== PARÂMETROS DA SIMULAÇÃO ====================
    /**
     * Duração total da simulação em minutos.
     * (Atualmente não utilizada para definir o fim da simulação, que é controlado por eventos).
     */
    public static final int DURACAO_TOTAL_SIMULACAO = 1440; // 24 horas * 60 minutos

    /**
     * Número total de estações de transferência disponíveis na simulação.
     */
    public static final int NUM_TOTAL_ESTACOES = 2;

    /**
     * Verifica se uma hora específica (formato 24h) está dentro de um dos horários de pico definidos.
     *
     * @param hora Hora do dia a ser verificada (ex: 7 para 07:00, 18 para 18:00).
     * @return {@code true} se a hora estiver dentro do horário de pico (manhã ou tarde), {@code false} caso contrário.
     */
    public static boolean isHorarioDePico(int hora) {
        return (hora >= HORA_INICIO_PICO_MANHA && hora < HORA_FIM_PICO_MANHA)
                || (hora >= HORA_INICIO_PICO_TARDE && hora < HORA_FIM_PICO_TARDE);
    }

    /**
     * Construtor privado para evitar a instanciação desta classe.
     * {@code ConfiguracoesDoSimulador} é uma classe utilitária e não deve ser instanciada.
     */
    private ConfiguracoesDoSimulador() {
        // Previne instanciação
    }
}