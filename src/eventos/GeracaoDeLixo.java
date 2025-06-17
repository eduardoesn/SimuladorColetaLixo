package eventos;

import zonas.Zonas;
import timer.Timer; // Importe a classe Timer para formatar o horário, se necessário em logs.

/**
 * Evento responsável por simular a geração diária de lixo em uma determinada zona.
 * Este evento é executado periodicamente (ou uma única vez no início da simulação, dependendo da configuração)
 * para acumular lixo na zona ao longo do tempo simulado, representando o ciclo de produção de resíduos.
 */
public class GeracaoDeLixo extends Evento {

    /** A {@link Zonas} onde o lixo será gerado. */
    private Zonas zona;

    /**
     * Construtor do evento de geração de lixo.
     *
     * @param tempo Tempo simulado (em minutos) em que a geração de lixo ocorrerá.
     * @param zona  A {@link Zonas} específica onde o lixo será acumulado.
     * @throws IllegalArgumentException se o tempo for negativo ou a zona for nula.
     */
    public GeracaoDeLixo(int tempo, Zonas zona) {
        super(tempo);
        if (zona == null) {
            throw new IllegalArgumentException("A zona para geração de lixo não pode ser nula.");
        }
        this.zona = zona;
    }

    /**
     * Retorna uma representação textual do evento de geração de lixo,
     * incluindo o nome da zona e o horário simulado do evento.
     *
     * @return Uma {@code String} formatada com os detalhes do evento.
     */
    @Override
    public String toString() {
        return String.format("EventoGeracaoDeLixo | Zona %s | Horário: %s",
                zona.getNome(),
                Timer.formatarHorarioSimulado(getTempo()));
    }

    /**
     * Executa o evento de geração de lixo.
     * <p>
     * Quando este evento é processado:
     * <ul>
     * <li>Uma quantidade aleatória de lixo é gerada e adicionada ao total acumulado na zona.</li>
     * <li>Uma mensagem no console é exibida indicando a quantidade total de lixo na zona após a geração.</li>
     * </ul>
     */
    @Override
    public void executar() {
        System.out.println("== GERAÇÃO DE LIXO ==");
        System.out.printf("[%s] %n", Timer.formatarHorarioSimulado(getTempo()));
        zona.gerarLixoDiario(); // Chama o método da zona para gerar e acumular lixo
        System.out.println(); // Linha em branco para melhor legibilidade
    }

    public Zonas getZona() {
        return zona;
    }
}