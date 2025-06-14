package zonas;

import estacoes.EstacaoDeTransferencia;

/**
 * Classe utilitária responsável por determinar a estação de transferência associada a uma determinada zona.
 * <p>
 * Essa classe mapeia zonas específicas a estações de transferência predefinidas (Estação A e Estação B),
 * simulando a proximidade ou designação de rotas.
 */
public class DistanciaZonas {

    /** Referência para a primeira estação de transferência associada. */
    private static EstacaoDeTransferencia estacaoA;

    /** Referência para a segunda estação de transferência associada. */
    private static EstacaoDeTransferencia estacaoB;

    /**
     * Construtor privado para evitar a instanciação.
     * Esta é uma classe utilitária e seus métodos são estáticos.
     */
    private DistanciaZonas() {
        // Previne instanciação
    }

    /**
     * Configura as estações de transferência que serão utilizadas para mapear as zonas.
     * <p>
     * Este método **deve ser chamado** antes de qualquer tentativa de usar
     * o método {@link #getEstacaoPara(Zonas)}, para garantir que as estações estejam definidas.
     *
     * @param a A primeira estação de transferência a ser configurada.
     * @param b A segunda estação de transferência a ser configurada.
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
     * <li>As zonas "Norte" e "Centro" retornam a estação definida como {@code estacaoA}.</li>
     * <li>As zonas "Sul", "Sudeste" e "Leste" retornam a estação definida como {@code estacaoB}.</li>
     * </ul>
     * O nome da zona é convertido para minúsculas para garantir que a comparação não seja sensível a maiúsculas/minúsculas.
     *
     * @param zona A {@link Zonas} para a qual se deseja obter a estação de transferência.
     * @return A {@link EstacaoDeTransferencia} associada à zona.
     * @throws IllegalArgumentException Se a zona fornecida não for reconhecida ou não tiver uma estação associada.
     * @throws IllegalStateException Se as estações A ou B não tiverem sido configuradas previamente com {@link #configurar(EstacaoDeTransferencia, EstacaoDeTransferencia)}.
     */
    public static EstacaoDeTransferencia getEstacaoPara(Zonas zona) {
        if (estacaoA == null || estacaoB == null) {
            throw new IllegalStateException("As estações de transferência não foram configuradas. Chame DistanciaZonas.configurar() primeiro.");
        }

        String nome = zona.getNome().toLowerCase(); // Converte para minúsculas para comparação flexível

        if (nome.equals("norte") || nome.equals("centro")) {
            return estacaoA;
        }
        if (nome.equals("sul") || nome.equals("sudeste") || nome.equals("leste")) {
            return estacaoB;
        }
        // Lança exceção se a zona não corresponder a nenhuma regra definida
        throw new IllegalArgumentException("Zona desconhecida: " + zona.getNome() + ". Não há estação de transferência associada.");
    }
}