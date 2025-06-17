package configsimulador;

public final class ConfiguracoesDoSimulador {

    // ==================== CAPACIDADE DOS CAMINHÕES ====================
    public static final int CAPACIDADE_CAMINHAO_2T = 2;
    public static final int CAPACIDADE_CAMINHAO_4T = 4;
    public static final int CAPACIDADE_CAMINHAO_8T = 8;
    public static final int CAPACIDADE_CAMINHAO_10T = 10;
    public static final int CAPACIDADE_CAMINHAO_GRANDE = 20;

    // ==================== TEMPOS DE OPERAÇÃO ====================
    public static final int TEMPO_DESCARGA_TONELADA = 5;
    public static final int TEMPO_COLETA_TONELADA = 10;
    public static final int TEMPO_MAX_ESPERA_PEQUENO = 15;
    public static final int TOLERANCIA_ESPERA_GRANDE = 20;
    public static final int TEMPO_VIAGEM_ATERRO = 60; // Ex: 60 minutos

    // ==================== CONFIGURAÇÕES DE VIAGEM ====================
    public static final int VIAGEM_MIN_PICO = 30;
    public static final int VIAGEM_MAX_PICO = 60;
    public static final int VIAGEM_MIN_FORA_PICO = 20;
    public static final int VIAGEM_MAX_FORA_PICO = 40;
    public static final int MAX_VIAGENS_DIARIAS_PEQUENO = 3;

    // ==================== GERAÇÃO DE LIXO POR ZONA ====================
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
    public static final double MULTIPLICADOR_TEMPO_PICO = 1.5;
    public static final double MULTIPLICADOR_TEMPO_FORA_PICO = 1.0;

    // ==================== HORÁRIOS OPERACIONAIS ====================
    public static final int HORA_INICIO_PICO_MANHA = 7;
    public static final int HORA_FIM_PICO_MANHA = 9;
    public static final int HORA_INICIO_PICO_TARDE = 17;
    public static final int HORA_FIM_PICO_TARDE = 19;
    public static final int HORA_INICIO_ALMOCO = 12;
    public static final int HORA_FIM_ALMOCO = 14;

    public static boolean isHorarioDePico(int hora) {
        return (hora >= HORA_INICIO_PICO_MANHA && hora < HORA_FIM_PICO_MANHA)
                || (hora >= HORA_INICIO_PICO_TARDE && hora < HORA_FIM_PICO_TARDE);
    }

    private ConfiguracoesDoSimulador() {
        // Previne instanciação
    }
}