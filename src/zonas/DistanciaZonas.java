package zonas;

import estacoes.EstacaoDeTransferencia;

/**
 * Classe responsável por determinar a estação de transferência associada a uma determinada zona.
 * <p>
 * Essa classe utiliza duas estações de transferência configuradas e decide qual delas está vinculada
 * a uma zona específica com base em seu nome.
 */
public class DistanciaZonas {

    /** Referência para a primeira estação de transferência associada. */
    private static EstacaoDeTransferencia estacaoA;

    /** Referência para a segunda estação de transferência associada. */
    private static EstacaoDeTransferencia estacaoB;

    /**
     * Configura as estações de transferência associadas.
     * <p>
     * Este método deve ser chamado antes de usar o método {@link #getEstacaoPara(Zonas)}.
     *
     * @param a A primeira estação de transferência.
     * @param b A segunda estação de transferência.
     */
    public static void configurar(EstacaoDeTransferencia a, EstacaoDeTransferencia b) {
        estacaoA = a;
        estacaoB = b;
    }

    /**
     * Retorna a estação de transferência correspondente a uma zona específica.
     * <p>
     * A associação entre zona e estação é baseada nos seguintes critérios:
     * <ul>
     *     <li>As zonas "norte" e "centro" retornam a estação definida como {@code estacaoA}.</li>
     *     <li>As zonas "sul", "sudeste" e "leste" retornam a estação definida como {@code estacaoB}.</li>
     * </ul>
     *
     * @param zona A zona para a qual se deseja obter a estação de transferência.
     * @return A estação de transferência associada à zona.
     * @throws IllegalArgumentException Se a zona não for reconhecida.
     */
    public static EstacaoDeTransferencia getEstacaoPara(Zonas zona) {
        String nome = zona.getNome().toLowerCase();
        if (nome.equals("norte") || nome.equals("centro")) {
            return estacaoA;
        }
        if (nome.equals("sul") || nome.equals("sudeste") || nome.equals("leste")) {
            return estacaoB;
        }
        throw new IllegalArgumentException("Zona desconhecida: " + zona.getNome());
    }
}