package eventos;

import zonas.Zonas;

/**
 * Evento responsável por simular a geração diária de lixo em uma determinada zona.
 * Esse evento é executado para acumular lixo na zona ao longo do tempo simulado.
 */
public class GeracaoDeLixo extends Evento {

    /** Zona onde o lixo será gerado. */
    private Zonas zona;

    /**
     * Construtor do evento de geração de lixo.
     *
     * @param tempo Tempo simulado em que a geração de lixo ocorrerá.
     * @param zona  Zona específica onde o lixo será acumulado.
     */
    public GeracaoDeLixo(int tempo, Zonas zona) {
        super(tempo);
        this.zona = zona;
    }

    /**
     * Executa o evento de geração de lixo:
     * <ul>
     *     <li>Acumula a quantidade diária de lixo na zona especificada.</li>
     *     <li>Exibe no console a quantidade atual de lixo acumulado.</li>
     * </ul>
     */
    @Override
    public void executar() {
        zona.gerarLixoDiario();
        System.out.println("[Geração] Zona " + zona.getNome() + " com lixo: " + zona.getLixoAcumulado() + "t");
    }
}
