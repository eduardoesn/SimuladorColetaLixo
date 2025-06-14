package timer;

import configsimulador.ConfiguracoesDoSimulador;
import java.util.concurrent.ThreadLocalRandom;

public class Timer {
    /**
     * Converte o tempo decorrido desde 07:00 em um horário no formato "HH:mm".
     *
     * @param minutosDecorridos Minutos desde o início da simulação (07:00)
     * @return Horário formatado como "HH:mm"
     * @throws IllegalArgumentException se minutosDecorridos for negativo
     */
    public static String formatarHorarioSimulado(int minutosDecorridos) {
        if (minutosDecorridos < 0) {
            throw new IllegalArgumentException("Minutos não podem ser negativos");
        }
        int hora = 7 + (minutosDecorridos / 60);
        int minuto = minutosDecorridos % 60;
        return String.format("%02d:%02d", hora, minuto);
    }

    /**
     * Formata uma duração em minutos para uma string no formato "Xh Ymin" ou apenas "Zmin".
     *
     * @param duracaoMinutos Duração total em minutos
     * @return String representando a duração formatada
     */
    public static String formatarDuracao(int duracaoMinutos) {
        int horas = duracaoMinutos / 60;
        int minutos = duracaoMinutos % 60;
        if (horas > 0) {
            return String.format("%dh %02dmin", horas, minutos);
        } else {
            return String.format("%dmin", minutos);
        }
    }

    /**
     * Calcula a duração real de uma viagem ajustada pelos horários de pico.
     *
     * @param tempoAtual  Tempo atual da simulação (em minutos desde 07:00)
     * @param duracaoBase Duração base da viagem (sem ajuste de pico)
     * @return Duração ajustada em minutos
     * @throws IllegalArgumentException se algum dos parâmetros for negativo
     */
    public static int calcularTempoRealDeViagem(int tempoAtual, int duracaoBase) {
        if (tempoAtual < 0 || duracaoBase < 0) {
            throw new IllegalArgumentException("Parâmetros de tempo não podem ser negativos");
        }

        int tempoRestante = duracaoBase;
        int tempoSimulado = tempoAtual;
        int tempoFinal = 0;

        while (tempoRestante > 0) {
            int horaSimulada = 7 + (tempoSimulado / 60);

            double multiplicador = ConfiguracoesDoSimulador.isHorarioDePico(horaSimulada)
                    ? ConfiguracoesDoSimulador.MULTIPLICADOR_TEMPO_PICO
                    : ConfiguracoesDoSimulador.MULTIPLICADOR_TEMPO_FORA_PICO;

            tempoFinal += (int) Math.round(multiplicador);
            tempoSimulado++;
            tempoRestante--;
        }

        return tempoFinal;
    }

    /**
     * Calcula o tempo total de deslocamento e operação de um caminhão (coleta e transporte),
     * considerando carga transportada, horário e se o caminhão está carregado.
     *
     * @param tempoAtual     Tempo atual da simulação (em minutos desde 07:00)
     * @param cargaToneladas Quantidade de carga em toneladas
     * @param carregado      true se o caminhão está indo para a estação, false se está coletando
     * @return Tempo total da operação em minutos
     */
    public static TimerDetalhado calcularTimerDetalhado(int tempoAtual, int cargaToneladas, boolean carregado) {
        // Verifica se está em horário de pico
        boolean emPico = ConfiguracoesDoSimulador.isHorarioDePico(tempoAtual);
        int tempoMin = emPico ? ConfiguracoesDoSimulador.VIAGEM_MIN_PICO : ConfiguracoesDoSimulador.VIAGEM_MIN_FORA_PICO;
        int tempoMax = emPico ? ConfiguracoesDoSimulador.VIAGEM_MAX_PICO : ConfiguracoesDoSimulador.VIAGEM_MAX_FORA_PICO;

        // Tempo base de deslocamento (aleatório entre intervalo)
        int tempoBase = ThreadLocalRandom.current().nextInt(tempoMin, tempoMax + 1);

        // Tempo ajustado com multiplicador de pico
        int tempoDeslocamento = calcularTempoRealDeViagem(tempoAtual, tempoBase);

        // Tempo de coleta com base na carga
        int tempoColeta = cargaToneladas * ConfiguracoesDoSimulador.TEMPO_COLETA_TONELADA;

        // Se estiver carregado, aplica tempo extra (30% mais lento)
        int tempoExtra = carregado ? (int) (tempoDeslocamento * 0.5) : 0;

        return new TimerDetalhado(tempoColeta,tempoDeslocamento,tempoExtra);
    }
}
