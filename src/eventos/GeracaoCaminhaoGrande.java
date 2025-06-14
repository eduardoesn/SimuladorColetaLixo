package eventos;

import estacoes.EstacaoDeTransferencia;

/**
 * Evento responsável por gerar um novo caminhão grande em uma estação de transferência,
 * caso o tempo máximo de espera tenha sido excedido e não haja caminhões grandes disponíveis.
 * <p>
 * Esse evento garante que a operação da estação não seja interrompida por falta de capacidade
 * para transportar o lixo ao aterro sanitário.
 */
public class GeracaoCaminhaoGrande extends Evento {

    /**
     * Estação de transferência onde será avaliada a necessidade de gerar um novo caminhão grande.
     */
    private EstacaoDeTransferencia estacao;

    /**
     * Construtor do evento de geração de caminhão grande.
     *
     * @param tempo    Tempo simulado (em minutos) em que o evento será executado.
     * @param estacao  Estação de transferência onde o caminhão será gerado, se necessário.
     */
    public GeracaoCaminhaoGrande(int tempo, EstacaoDeTransferencia estacao) {
        super(tempo);
        this.estacao = estacao;
    }

    /**
     * Executa o evento. Verifica se há caminhão grande disponível.
     * Caso não haja, um novo caminhão é criado para atender a demanda da estação.
     */
    @Override
    public void executar() {
        // Se já houver caminhão grande disponível, não há necessidade de gerar outro
        if (estacao.getCaminhaoGrandeDisponivel()) return;

        // Caso contrário, cria um novo caminhão grande
        System.out.println("[GERAÇÃO] Tempo máximo de espera atingido. Criando caminhão grande.");
        estacao.gerarNovoCaminhaoGrande(tempo);
    }
}
