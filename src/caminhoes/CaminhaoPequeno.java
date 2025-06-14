package caminhoes;

import tads.Lista;
import eventos.GeracaoCaminhaoGrande;
import zonas.Zonas;

/**
 * Representa um caminhão pequeno utilizado para a coleta de lixo nas zonas da cidade.
 * Cada caminhão possui uma capacidade limitada, número máximo de viagens por dia,
 * e uma zona de destino definida.
 */
public class CaminhaoPequeno {

    /**
     * Identificador único do caminhão.
     */
    private String id;

    /**
     * Capacidade máxima de carga do caminhão, em toneladas.
     */
    private int capacidadeMaxima;

    /**
     * Quantidade atual de carga no caminhão, em toneladas.
     * Inicialmente zero.
     */
    private int cargaAtual;

    /**
     * Número de viagens restantes que o caminhão pode realizar no dia.
     * Este contador é decrementado a cada viagem.
     */
    private int viagensRestantes;

    /**
     * Zona de destino atual do caminhão para coleta de lixo.
     */
    private Zonas destinoZona;

    /**
     * Evento de geração de caminhão grande associado a este caminhão,
     * caso ele precise descarregar e a estação necessite de um caminhão grande.
     * Permite cancelar o evento se o caminhão grande se tornar disponível antes.
     */
    private GeracaoCaminhaoGrande eventoAgendado;

    /**
     * Construtor da classe CaminhaoPequeno.
     * Inicializa um novo caminhão com suas propriedades básicas.
     *
     * @param id               Identificador do caminhão (ex: "C1", "C2").
     * @param capacidadeMaxima Capacidade máxima de carga do caminhão em toneladas.
     * @param viagensRestantes Número inicial de viagens que o caminhão pode realizar por dia.
     * @param destinoZona      A zona inicial para a qual o caminhão está designado para coleta.
     */
    public CaminhaoPequeno(String id, int capacidadeMaxima, int viagensRestantes, Zonas destinoZona) {
        this.id = id;
        this.capacidadeMaxima = capacidadeMaxima;
        this.cargaAtual = 0; // Caminhão começa vazio
        this.viagensRestantes = viagensRestantes;
        this.destinoZona = destinoZona;
        this.eventoAgendado = null; // Nenhum evento de geração de caminhão grande agendado inicialmente
    }

    /**
     * Retorna a capacidade máxima de carga do caminhão.
     *
     * @return Capacidade máxima em toneladas.
     */
    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    /**
     * Retorna o número de viagens restantes disponíveis para o caminhão no dia.
     *
     * @return Número de viagens restantes.
     */
    public int getViagensRestantes() {
        return viagensRestantes;
    }

    /**
     * Retorna o identificador único do caminhão.
     *
     * @return O ID do caminhão.
     */
    public String getId() {
        return id;
    }

    /**
     * Retorna a quantidade atual de carga no caminhão.
     *
     * @return Carga atual em toneladas.
     */
    public int getCargaAtual() {
        return cargaAtual;
    }

    /**
     * Retorna a zona de destino atual para a coleta de lixo.
     *
     * @return A zona de destino.
     */
    public Zonas getDestinoZona() {
        return destinoZona;
    }

    /**
     * Retorna o evento de geração de caminhão grande que foi agendado para este caminhão pequeno,
     * caso ele esteja aguardando descarregamento em uma estação sem caminhão grande disponível.
     *
     * @return O evento agendado de transferência para caminhão grande, ou {@code null} se não houver.
     */
    public GeracaoCaminhaoGrande getEventoAgendado() {
        return eventoAgendado;
    }

    /**
     * Define o evento de geração de caminhão grande que será associado a este caminhão pequeno.
     * Isso é usado para permitir o cancelamento do evento se a situação mudar.
     *
     * @param eventoAgendado O evento a ser associado. Pode ser {@code null} para remover a associação.
     */
    public void setEventoAgendado(GeracaoCaminhaoGrande eventoAgendado) {
        this.eventoAgendado = eventoAgendado;
    }

    /**
     * Tenta adicionar uma quantidade de carga ao caminhão.
     * A carga só será adicionada se a quantidade total não exceder a capacidade máxima do caminhão.
     *
     * @param quantidade Quantidade de lixo a ser coletada (em toneladas).
     * @return {@code true} se a carga foi adicionada com sucesso, {@code false} caso a capacidade máxima seja excedida.
     */
    public boolean coletarCarga(int quantidade) {
        if (cargaAtual + quantidade <= capacidadeMaxima) {
            cargaAtual += quantidade;
            System.out.println("[CAMINHÃO " + id + "] Coletou " + quantidade + " toneladas.");
            return true;
        }
        System.out.println("[CAMINHÃO " + id + "] Carga máxima atingida. Não é possível coletar mais.");
        return false;
    }

    /**
     * Descarrega toda a carga do caminhão, resetando a {@code cargaAtual} para zero.
     * Simula o esvaziamento do caminhão em uma estação de transferência ou aterro.
     */
    public void descarregarCarga() {
        cargaAtual = 0;
        System.out.println("[CAMINHÃO " + id + "] Carga descarregada. Caminhão vazio.");
    }

    /**
     * Verifica se o caminhão ainda pode realizar mais viagens de coleta no dia.
     *
     * @return {@code true} se houver viagens restantes ({@code viagensRestantes > 0}), {@code false} caso contrário.
     */
    public boolean podeViajarNovamente() {
        return viagensRestantes > 0;
    }

    /**
     * Registra que uma viagem foi realizada, decrementando o contador de viagens restantes.
     * Se o número de viagens restantes for zero, o caminhão não poderá mais viajar para coleta.
     */
    public void registrarViagem() {
        if (viagensRestantes > 0) {
            viagensRestantes--;
            System.out.println("[CAMINHÃO " + id + "] Viagem registrada. " + viagensRestantes + " viagens restantes.");
        } else {
            System.out.println("[CAMINHÃO " + id + "] Limite de viagens diárias atingido.");
        }
    }
}