package zonas;

import configsimulador.ConfiguracoesDoSimulador;
import zonas.Zonas;

/**
 * Classe utilitária que fornece métodos para criar instâncias parametrizadas de diferentes zonas.
 * <p>
 * Cada zona é configurada com seus respectivos nomes, limites mínimos e máximos de lixo,
 * baseados nos valores especificados na configuração do simulador.
 */
public class ZonasParametradas {

    /**
     * Cria e retorna uma instância da zona "Sul".
     * <p>
     * Os limites de lixo mínimo e máximo são obtidos da classe {@link ConfiguracoesDoSimulador}.
     *
     * @return Uma nova instância da classe {@link Zonas} representando a zona "Sul".
     */
    public static Zonas zonaSul() {
        return new Zonas("Sul", ConfiguracoesDoSimulador.LIXO_MIN_ZONA_SUL, ConfiguracoesDoSimulador.LIXO_MAX_ZONA_SUL);
    }

    /**
     * Cria e retorna uma instância da zona "Norte".
     * <p>
     * Os limites de lixo mínimo e máximo são obtidos da classe {@link ConfiguracoesDoSimulador}.
     *
     * @return Uma nova instância da classe {@link Zonas} representando a zona "Norte".
     */
    public static Zonas zonaNorte() {
        return new Zonas("Norte", ConfiguracoesDoSimulador.LIXO_MIN_ZONA_NORTE, ConfiguracoesDoSimulador.LIXO_MAX_ZONA_NORTE);
    }

    /**
     * Cria e retorna uma instância da zona "Centro".
     * <p>
     * Os limites de lixo mínimo e máximo são obtidos da classe {@link ConfiguracoesDoSimulador}.
     *
     * @return Uma nova instância da classe {@link Zonas} representando a zona "Centro".
     */
    public static Zonas zonaCentro() {
        return new Zonas("Centro", ConfiguracoesDoSimulador.LIXO_MIN_ZONA_CENTRO, ConfiguracoesDoSimulador.LIXO_MAX_ZONA_CENTRO);
    }

    /**
     * Cria e retorna uma instância da zona "Leste".
     * <p>
     * Os limites de lixo mínimo e máximo são obtidos da classe {@link ConfiguracoesDoSimulador}.
     *
     * @return Uma nova instância da classe {@link Zonas} representando a zona "Leste".
     */
    public static Zonas zonaLeste() {
        return new Zonas("Leste", ConfiguracoesDoSimulador.LIXO_MIN_ZONA_LESTE, ConfiguracoesDoSimulador.LIXO_MAX_ZONA_LESTE);
    }

    /**
     * Cria e retorna uma instância da zona "Sudeste".
     * <p>
     * Os limites de lixo mínimo e máximo são obtidos da classe {@link ConfiguracoesDoSimulador}.
     *
     * @return Uma nova instância da classe {@link Zonas} representando a zona "Sudeste".
     */
    public static Zonas zonaSudeste() {
        return new Zonas("Sudeste", ConfiguracoesDoSimulador.LIXO_MIN_ZONA_SUDESTE, ConfiguracoesDoSimulador.LIXO_MAX_ZONA_SUDESTE);
    }
}