package eventos;

import caminhoes.CaminhaoPequeno;
import configsimulador.ConfiguracoesDoSimulador;
import configsimulador.ParametrosSimulacao;
import tads.Lista;
import zonas.Zonas;

/**
 * Classe responsável por distribuir as rotas entre os caminhões,
 * agora configurada para aceitar uma frota mista de caminhões.
 */
public class DistribuirRota {

    private DistribuirRota() {
        // Previne instanciação
    }

    /**
     * Distribui zonas para os caminhões com base nos parâmetros da simulação.
     *
     * @param zonas  Uma {@link Lista} de {@link Zonas} da cidade.
     * @param params Objeto com os parâmetros de configuração da UI.
     * @return Uma {@link Lista} de {@link CaminhaoPequeno} configurados.
     */
    public static Lista<CaminhaoPequeno> distribuir(Lista<Zonas> zonas, ParametrosSimulacao params) {
        if (zonas == null || zonas.estaVazia()) {
            throw new IllegalArgumentException("A lista de zonas não pode ser nula ou vazia.");
        }

        Lista<CaminhaoPequeno> caminhoes = new Lista<>();
        System.out.println("\n[DISTRIBUIÇÃO DE ROTAS]");

        int caminhaoIndex = 0;

        // Distribui caminhões de 2 toneladas
        for (int i = 0; i < params.getNumCaminhoes2t(); i++) {
            criarEAgendarCaminhao(caminhoes, zonas, caminhaoIndex++, "C2-" + (i + 1),
                    ConfiguracoesDoSimulador.CAPACIDADE_CAMINHAO_2T, params.getViagensCaminhoes2t());
        }

        // Distribui caminhões de 4 toneladas
        for (int i = 0; i < params.getNumCaminhoes4t(); i++) {
            criarEAgendarCaminhao(caminhoes, zonas, caminhaoIndex++, "C4-" + (i + 1),
                    ConfiguracoesDoSimulador.CAPACIDADE_CAMINHAO_4T, params.getViagensCaminhoes4t());
        }

        // Distribui caminhões de 8 toneladas
        for (int i = 0; i < params.getNumCaminhoes8t(); i++) {
            criarEAgendarCaminhao(caminhoes, zonas, caminhaoIndex++, "C8-" + (i + 1),
                    ConfiguracoesDoSimulador.CAPACIDADE_CAMINHAO_8T, params.getViagensCaminhoes8t());
        }

        // Distribui caminhões de 10 toneladas
        for (int i = 0; i < params.getNumCaminhoes10t(); i++) {
            criarEAgendarCaminhao(caminhoes, zonas, caminhaoIndex++, "C10-" + (i + 1),
                    ConfiguracoesDoSimulador.CAPACIDADE_CAMINHAO_10T, params.getViagensCaminhoes10t());
        }


        System.out.println("Distribuição de rotas concluída. Total de caminhões: " + caminhoes.getTamanho());
        return caminhoes;
    }

    /**
     * Método auxiliar para criar, configurar e agendar o primeiro evento de um caminhão.
     */
    private static void criarEAgendarCaminhao(Lista<CaminhaoPequeno> caminhoes, Lista<Zonas> zonas, int caminhaoIndex,
                                              String id, int capacidade, int viagens) {
        if (viagens <= 0) return;

        // Atribui uma zona inicial usando round-robin para distribuir o trabalho
        Zonas zonaInicial = zonas.getValor(caminhaoIndex % zonas.getTamanho());
        if (zonaInicial == null) {
            System.err.println("Erro: Não foi possível atribuir zona inicial para o caminhão " + id);
            return;
        }

        CaminhaoPequeno caminhao = new CaminhaoPequeno(id, capacidade, viagens, zonaInicial);
        caminhoes.adicionar(caminhoes.getTamanho(), caminhao);

        // Agenda o primeiro evento de coleta para este caminhão no tempo 0
        GerenciadorAgenda.adicionarEvento(new ColetaLixo(0, caminhao, zonaInicial));
        System.out.printf("  • Caminhão %s (Cap: %dt, Viagens: %d) atribuído à zona %s. Evento de coleta agendado.%n",
                id, capacidade, viagens, zonaInicial.getNome());
    }
}