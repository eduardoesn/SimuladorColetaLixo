package timer;

import configsimulador.ConfiguracoesDoSimulador;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Classe utilitária para manipulação e cálculo de tempo na simulação de coleta de lixo.
 * Fornece métodos para formatação de horários e durações, e cálculo de tempos de viagem
 * considerando fatores como horários de pico.
 */
public class Timer {

    /**
     * Construtor privado para evitar instanciação.
     * Esta é uma classe utilitária e todos os seus métodos são estáticos.
     */
    private Timer() {
        // Previne instanciação
    }

    /**
     * Converte o tempo decorrido desde 07:00 da manhã (início da simulação)
     * em um horário no formato "HH:mm".
     *
     * @param minutosDecorridos Minutos desde o início da simulação (07:00).
     * @return Horário formatado como "HH:mm" (ex: "07:30", "14:15").
     * @throws IllegalArgumentException se {@code minutosDecorridos} for negativo.
     */
    public static String formatarHorarioSimulado(int minutosDecorridos) {
        if (minutosDecorridos < 0) {
            throw new IllegalArgumentException("Minutos decorridos não podem ser negativos.");
        }
        int horaInicialSimulacao = 7; // Simulação começa às 07:00
        int hora = horaInicialSimulacao + (minutosDecorridos / 60);
        int minuto = minutosDecorridos % 60;
        return String.format("%02d:%02d", hora, minuto); // Formata com zero à esquerda se necessário
    }

    /**
     * Formata uma duração total em minutos para uma string mais legível.
     * O formato pode ser "Xh Ymin" (se houver horas) ou apenas "Zmin" (se for apenas minutos).
     *
     * @param duracaoMinutos Duração total em minutos.
     * @return String representando a duração formatada (ex: "1h 30min", "45min").
     * @throws IllegalArgumentException se {@code duracaoMinutos} for negativo.
     */
    public static String formatarDuracao(int duracaoMinutos) {
        if (duracaoMinutos < 0) {
            throw new IllegalArgumentException("Duração em minutos não pode ser negativa.");
        }
        int horas = duracaoMinutos / 60;
        int minutos = duracaoMinutos % 60;

        if (horas > 0) {
            return String.format("%dh %02dmin", horas, minutos);
        } else {
            return String.format("%dmin", minutos);
        }
    }

    /**
     * Calcula a duração real de uma viagem (em minutos), ajustada pelos horários de pico.
     * O tempo de viagem é incrementado minuto a minuto, e a cada minuto, verifica-se
     * se o período atual está em horário de pico para aplicar o multiplicador de tempo correspondente.
     *
     * @param tempoAtual  Tempo atual da simulação (em minutos desde 07:00), no início da viagem.
     * @param duracaoBase Duração base da viagem (sem ajuste de pico), em minutos.
     * @return A duração ajustada da viagem em minutos, considerando os multiplicadores de tempo de pico.
     * @throws IllegalArgumentException se {@code tempoAtual} ou {@code duracaoBase} forem negativos.
     */
    public static int calcularTempoRealDeViagem(int tempoAtual, int duracaoBase) {
        if (tempoAtual < 0 || duracaoBase < 0) {
            throw new IllegalArgumentException("Parâmetros de tempo não podem ser negativos.");
        }

        int tempoRestanteParaSimular = duracaoBase;
        int tempoSimuladoNoLoop = tempoAtual;
        int tempoRealTotal = 0; // Acumula o tempo real da viagem

        // Itera por cada "minuto base" da viagem, ajustando-o se for horário de pico
        while (tempoRestanteParaSimular > 0) {
            // Calcula a hora do dia para o minuto simulado atual
            int horaSimuladaDoDia = 7 + (tempoSimuladoNoLoop / 60);

            // Determina o multiplicador com base no horário de pico
            double multiplicador = ConfiguracoesDoSimulador.isHorarioDePico(horaSimuladaDoDia)
                    ? ConfiguracoesDoSimulador.MULTIPLICADOR_TEMPO_PICO
                    : ConfiguracoesDoSimulador.MULTIPLICADOR_TEMPO_FORA_PICO;

            // Adiciona o tempo real ajustado (arredondado) para este minuto
            tempoRealTotal += (int) Math.round(multiplicador);

            tempoSimuladoNoLoop++; // Avança um minuto no tempo simulado
            tempoRestanteParaSimular--; // Decrementa um minuto da duração base
        }

        return tempoRealTotal;
    }

    /**
     * Calcula um {@link TimerDetalhado} que contém o tempo total de uma operação de caminhão,
     * considerando tempo de coleta, tempo de deslocamento e tempo extra por estar carregado.
     * Ajusta os tempos com base em horários de pico.
     *
     * @param tempoAtual     Tempo atual da simulação (em minutos desde 07:00), no início da operação.
     * @param cargaToneladas Quantidade de carga envolvida na operação (em toneladas).
     * @param carregado      {@code true} se o caminhão está realizando um deslocamento carregado (ex: para estação/aterro),
     * {@code false} se está em coleta ou vazio. Isso afeta o tempo extra.
     * @return Uma instância de {@link TimerDetalhado} com os tempos discriminados (coleta, deslocamento, extra, total).
     * @throws IllegalArgumentException se {@code tempoAtual} ou {@code cargaToneladas} forem negativos.
     */
    public static TimerDetalhado calcularTimerDetalhado(int tempoAtual, int cargaToneladas, boolean carregado) {
        if (tempoAtual < 0 || cargaToneladas < 0) {
            throw new IllegalArgumentException("Parâmetros de tempo e carga não podem ser negativos.");
        }

        // Determina se o tempo atual está dentro do horário de pico
        int horaAtualDoDia = 7 + (tempoAtual / 60);
        boolean emPico = ConfiguracoesDoSimulador.isHorarioDePico(horaAtualDoDia);

        // Define os limites de tempo base de viagem (sem multiplicador) com base no horário de pico/fora de pico
        int tempoMinBase = emPico ? ConfiguracoesDoSimulador.VIAGEM_MIN_PICO : ConfiguracoesDoSimulador.VIAGEM_MIN_FORA_PICO;
        int tempoMaxBase = emPico ? ConfiguracoesDoSimulador.VIAGEM_MAX_PICO : ConfiguracoesDoSimulador.VIAGEM_MAX_FORA_PICO;

        // Gera um tempo base de deslocamento aleatório dentro do intervalo definido
        int tempoBaseDeslocamento = ThreadLocalRandom.current().nextInt(tempoMinBase, tempoMaxBase + 1);

        // Calcula o tempo de deslocamento real, ajustado pelos horários de pico
        int tempoDeslocamentoReal = calcularTempoRealDeViagem(tempoAtual, tempoBaseDeslocamento);

        // Calcula o tempo de coleta com base na carga e no tempo por tonelada
        int tempoColeta = cargaToneladas * ConfiguracoesDoSimulador.TEMPO_COLETA_TONELADA;

        // Calcula o tempo extra se o caminhão estiver carregado (ex: indo para a estação)
        // O tempo extra é de 50% do tempo de deslocamento real se carregado, senão 0.
        int tempoExtraCarregado = carregado ? (int) (tempoDeslocamentoReal * 0.5) : 0;

        return new TimerDetalhado(tempoColeta, tempoDeslocamentoReal, tempoExtraCarregado);
    }
}