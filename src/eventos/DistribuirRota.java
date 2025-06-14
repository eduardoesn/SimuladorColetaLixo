package eventos;

import tads.Lista;
import caminhoes.CaminhaoPequeno;
import eventos.GerenciadorAgenda;
import eventos.ColetaLixo;
import zonas.Zonas;

/**
 * Classe responsável por distribuir as rotas entre os caminhões pequenos
 * com base nas zonas disponíveis e no número de viagens que cada caminhão pode realizar.
 * <p>
 * A distribuição visa garantir que todas as zonas sejam cobertas de forma balanceada,
 * utilizando o conceito de round-robin com base no índice dos caminhões e das zonas.
 */
public class DistribuirRota {

    /**
     * Distribui zonas para os caminhões pequenos, criando um plano de coleta para cada um
     * e agendando o primeiro evento de coleta para iniciar a simulação.
     *
     * @param zonas                Lista de zonas da cidade que geram lixo.
     * @param quantidadeCaminhoes  Quantidade de caminhões pequenos disponíveis.
     * @param viagensPorCaminhao   Número de viagens que cada caminhão pode realizar por dia.
     * @return Lista de caminhões configurados com suas rotas e prontos para iniciar a coleta.
     */
    public static Lista<CaminhaoPequeno> distribuir(Lista<Zonas> zonas, int quantidadeCaminhoes, int viagensPorCaminhao) {
        Lista<CaminhaoPequeno> caminhoes = new Lista<>();
        int quantidadeZonas = zonas.getTamanho();

        // Para cada caminhão, criamos uma rota de zonas (uma por viagem)
        for (int i = 0; i < quantidadeCaminhoes; i++) {
            Lista<Zonas> rotaCaminhao = new Lista<>();

            // Distribui zonas em estilo round-robin para cada viagem do caminhão
            for (int j = 0; j < viagensPorCaminhao; j++) {
                Zonas zonaAlvo = zonas.getValor((i + j) % quantidadeZonas); // distribuição circular
                rotaCaminhao.adicionar(j, zonaAlvo);
            }

            // Geração de identificador e configuração do caminhão
            String id = "C" + (i + 1);
            int capacidade = 8; // Capacidade em toneladas (parametrizável futuramente)

            // Criação do caminhão: remove o primeiro elemento da rota para definir zona inicial
            CaminhaoPequeno caminhao = new CaminhaoPequeno(id, capacidade, viagensPorCaminhao, rotaCaminhao.removerHead());

            // Adiciona caminhão à lista principal
            caminhoes.adicionar(i, caminhao);

            // Agenda o primeiro evento de coleta na zona inicial
            GerenciadorAgenda.adicionarEvento(new ColetaLixo(0, caminhao, caminhao.getDestinoZona()));
        }

        // Retorna a lista com todos os caminhões configurados
        return caminhoes;
    }
}
