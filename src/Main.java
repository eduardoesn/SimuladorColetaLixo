/**
 * Ponto de entrada principal para a aplicação do Simulador de Coleta de Lixo.
 * Esta classe contém o método {@code main} que inicia a execução da simulação.
 */
public class Main {
    /**
     * O método principal que inicia a execução do simulador.
     * Cria uma instância do {@link Simulador} e chama seu método {@code iniciar()} para começar o processo.
     *
     * @param args Argumentos de linha de comando (não utilizados nesta aplicação).
     */
    public static void main(String[] args) {
        Simulador simulador = new Simulador();
        simulador.iniciar();
    }
}