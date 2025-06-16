package eventos;

/**
 * Interface funcional para o padrão Observer.
 * <p>
 * Classes que implementam esta interface podem se registrar no {@link GerenciadorAgenda}
 * para serem notificadas sempre que um {@link Evento} for processado.
 */
@FunctionalInterface
public interface IEventoObserver {
    /**
     * Método chamado pelo {@link GerenciadorAgenda} quando um evento é processado.
     *
     * @param evento O evento que acabou de ser executado pela simulação.
     */
    void onEvento(Evento evento);
}