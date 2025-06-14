package eventos;

import tads.Lista;
import caminhoes.CaminhaoPequeno;
import eventos.GerenciadorAgenda;
import eventos.ColetaLixo;
import zonas.Zonas;

/**
 * Classe responsável por distribuir as rotas entre os caminhões pequenos,
 * com base nas zonas disponíveis e no número de viagens que cada caminhão pode realizar.
 * <p>
 * A distribuição visa garantir que todas as zonas sejam cobertas de forma balanceada,
 * utilizando um padrão de round-robin para atribuir zonas aos caminhões para cada viagem.
 * Isso garante que a carga de trabalho seja distribuída entre os recursos.
 */
public class DistribuirRota {

    /**
     * Construtor privado para evitar a instanciação.
     * Esta é uma classe utilitária e seus métodos são estáticos.
     */
    private DistribuirRota() {
        // Previne instanciação
    }

    /**
     * Distribui zonas para os caminhões pequenos, criando um plano de coleta para cada um
     * e agendando o primeiro evento de coleta para iniciar a simulação.
     * <p>
     * Este método configura os caminhões com sua capacidade e número de viagens,
     * e atribui zonas a eles em um esquema round-robin. Para cada caminhão,
     * o primeiro evento de coleta é imediatamente agendado no tempo 0.
     *
     * @param zonas                Uma {@link Lista} de {@link Zonas} da cidade que geram lixo.
     * @param quantidadeCaminhoes  O número total de caminhões pequenos disponíveis para distribuição.
     * @param viagensPorCaminhao   O número máximo de viagens que cada caminhão pequeno pode realizar por dia.
     * @return Uma {@link Lista} de {@link CaminhaoPequeno} configurados com suas rotas e prontos para iniciar a coleta.
     * @throws IllegalArgumentException se a lista de zonas for nula ou vazia,
     * ou se a quantidade de caminhões ou viagens for inválida.
     */
    public static Lista<CaminhaoPequeno> distribuir(Lista<Zonas> zonas, int quantidadeCaminhoes, int viagensPorCaminhao) {
        if (zonas == null || zonas.estaVazia()) {
            throw new IllegalArgumentException("A lista de zonas não pode ser nula ou vazia.");
        }
        if (quantidadeCaminhoes <= 0) {
            throw new IllegalArgumentException("A quantidade de caminhões deve ser maior que zero.");
        }
        if (viagensPorCaminhao <= 0) {
            throw new IllegalArgumentException("O número de viagens por caminhão deve ser maior que zero.");
        }

        Lista<CaminhaoPequeno> caminhoes = new Lista<>();
        int quantidadeZonas = zonas.getTamanho();

        System.out.println("\n[DISTRIBUIÇÃO DE ROTAS]");

        // Para cada caminhão, criamos uma rota de zonas (uma por viagem)
        for (int i = 0; i < quantidadeCaminhoes; i++) {
            // A rota de um caminhão pequeno é um conjunto de zonas que ele visitará
            Lista<Zonas> rotaCaminhao = new Lista<>();

            // Distribui zonas em estilo round-robin para cada viagem do caminhão
            // Isso garante que as zonas sejam visitadas por diferentes caminhões ao longo do dia/viagens.
            for (int j = 0; j < viagensPorCaminhao; j++) {
                // Calcula o índice da zona usando o operador módulo para distribuição circular
                Zonas zonaAlvo = zonas.getValor((i + j) % quantidadeZonas);
                if (zonaAlvo != null) { // Garante que a zona foi encontrada
                    rotaCaminhao.adicionar(j, zonaAlvo);
                } else {
                    System.err.println("Aviso: Zona não encontrada no índice " + ((i + j) % quantidadeZonas) + ". Pulando.");
                }
            }

            // Geração de identificador para o caminhão (ex: "C1", "C2", etc.)
            String idCaminhao = "C" + (i + 1);
            // Capacidade do caminhão em toneladas. Pode ser parametrizável via ConfiguracoesDoSimulador.
            int capacidadeCaminhao = 8; // Exemplo: ConfiguracoesDoSimulador.CAPACIDADE_CAMINHAO_8T;

            // Criação da instância do caminhão. A primeira zona da rota é definida como seu destino inicial.
            // A zona é removida da rotaCaminhao para indicar que é o "destino atual" do caminhão.
            Zonas primeiraZonaRota = rotaCaminhao.removerHead();
            if (primeiraZonaRota == null) {
                System.err.println("Erro: Não foi possível atribuir zona inicial para o caminhão " + idCaminhao + ". Pulando este caminhão.");
                continue; // Pula para o próximo caminhão se não houver zona inicial
            }

            CaminhaoPequeno caminhao = new CaminhaoPequeno(idCaminhao, capacidadeCaminhao, viagensPorCaminhao, primeiraZonaRota);

            // Adiciona o caminhão recém-configurado à lista principal de caminhões.
            caminhoes.adicionar(i, caminhao);

            // Agenda o primeiro evento de coleta para este caminhão na sua zona inicial.
            // O evento é agendado para o tempo 0, indicando o início da simulação de coleta.
            GerenciadorAgenda.adicionarEvento(new ColetaLixo(0, caminhao, caminhao.getDestinoZona()));
            System.out.printf("  • Caminhão %s atribuído à zona %s. Primeiro evento de coleta agendado.%n",
                    idCaminhao, caminhao.getDestinoZona().getNome());
        }

        // Retorna a lista com todos os caminhões configurados e seus eventos iniciais agendados.
        System.out.println("Distribuição de rotas concluída. Total de caminhões: " + caminhoes.getTamanho());
        return caminhoes;
    }
}