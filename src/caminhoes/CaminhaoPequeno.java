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
     * Identificador do caminhão.
     */
    private String id;

    /**
     * Capacidade máxima de carga do caminhão, em toneladas.
     */
    private int capacidadeMaxima;

    /**
     * Quantidade atual de carga no caminhão, em toneladas.
     */
    private int cargaAtual;

    /**
     * Número de viagens restantes que o caminhão pode realizar no dia.
     */
    private int viagensRestantes;

    /**
     * Zona de destino atual do caminhão.
     */
    private Zonas destinoZona;

    /**
     * Evento de geração de caminhão grande associado, caso este caminhão precise descarregar.
     */
    private GeracaoCaminhaoGrande eventoAgendado;

    /**
     * Construtor da classe CaminhaoPequeno.
     *
     * @param id               Identificador do caminhão.
     * @param capacidadeMaxima Capacidade máxima de carga do caminhão (em toneladas).
     * @param viagensRestantes Número de viagens que o caminhão pode realizar por dia.
     * @param destinoZona      Zona de destino inicial para a coleta.
     */
    public CaminhaoPequeno(String id, int capacidadeMaxima, int viagensRestantes, Zonas destinoZona) {
        this.id = id;
        this.capacidadeMaxima = capacidadeMaxima;
        this.cargaAtual = 0;
        this.viagensRestantes = viagensRestantes;
        this.destinoZona = destinoZona;
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
     * Retorna o número de viagens restantes disponíveis no dia.
     *
     * @return Número de viagens restantes.
     */
    public int getViagensRestantes() {
        return viagensRestantes;
    }

    /**
     * Retorna o identificador do caminhão.
     *
     * @return ID do caminhão.
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
     * Retorna a zona de destino atual do caminhão.
     *
     * @return Zona de destino.
     */
    public Zonas getDestinoZona() {
        return destinoZona;
    }

    /**
     * Retorna o evento de geração de caminhão grande associado.
     *
     * @return Evento agendado de transferência para caminhão grande.
     */
    public GeracaoCaminhaoGrande getEventoAgendado() {
        return eventoAgendado;
    }

    /**
     * Define o evento de geração de caminhão grande que será associado a este caminhão pequeno.
     *
     * @param eventoAgendado Evento a ser associado.
     */
    public void setEventoAgendado(GeracaoCaminhaoGrande eventoAgendado) {
        this.eventoAgendado = eventoAgendado;
    }

    /**
     * Tenta adicionar uma quantidade de carga ao caminhão.
     * Caso ultrapasse a capacidade máxima, a carga não será adicionada.
     *
     * @param quantidade Quantidade a ser coletada (em toneladas).
     * @return true se a carga foi adicionada com sucesso, false caso exceda a capacidade.
     */
    public boolean coletarCarga(int quantidade) {
        if (cargaAtual + quantidade <= capacidadeMaxima) {
            cargaAtual += quantidade;
            System.out.println("[CAMINHÃO " + id + "] Coletou " + quantidade + " toneladas");
            return true;
        }
        System.out.println("[CAMINHÃO " + id + "] Carga máxima atingida.");
        return false;
    }

    /**
     * Descarrega toda a carga do caminhão, resetando para zero.
     */
    public void descarregarCarga() {
        cargaAtual = 0;
    }

    /**
     * Verifica se o caminhão ainda pode realizar mais viagens no dia.
     *
     * @return true se houver viagens restantes, false caso contrário.
     */
    public boolean podeViajarNovamente() {
        return viagensRestantes > 0;
    }

    /**
     * Registra que uma viagem foi realizada, decrementando o contador de viagens restantes.
     */
    public void registrarViagem() {
        if (viagensRestantes > 0) {
            viagensRestantes--;
            System.out.println("[CAMINHÃO " + id + "] " + viagensRestantes + " viagens restantes.");
        }
    }
}
