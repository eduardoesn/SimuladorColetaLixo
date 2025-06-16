import configsimulador.ParametrosSimulacao;
import configsimulador.Simulador;

/**
 * Ponto de entrada principal para a execução da versão CONSOLE do simulador.
 * NOTA: A aplicação principal agora é a classe MainFX, que fornece uma interface gráfica.
 * Esta classe foi mantida para fins de teste e execução sem GUI.
 */
public class Main {
    /**
     * O método principal que inicia a execução do simulador via console.
     * Cria uma instância do {@link Simulador} e chama seu método {@code iniciar()}
     * com um conjunto de parâmetros padrão.
     *
     * @param args Argumentos de linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        Simulador simulador = new Simulador();

        // Como o método iniciar() agora exige parâmetros, criamos um conjunto padrão
        // para a execução via console. Estes valores podem ser alterados para testes.
        ParametrosSimulacao parametrosPadrao = new ParametrosSimulacao(
                2, // número de caminhões de 2t
                3, // viagens por caminhão de 2t
                1, // número de caminhões de 4t
                3, // viagens por caminhão de 4t
                1, // número de caminhões de 8t
                3  // viagens por caminhão de 8t
        );

        // Inicia a simulação com os parâmetros padrão
        simulador.iniciar(parametrosPadrao);
    }
}