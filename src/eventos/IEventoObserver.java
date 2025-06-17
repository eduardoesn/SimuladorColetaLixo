package eventos;

/**
 * Interface funcional para o padrão Observer.
 * <p>
 * Classes que implementam esta interface podem se registrar no {@link GerenciadorAgenda}
 * para serem notificadas sempre que um {@link Evento} for processado.
 * A anotação {@code @FunctionalInterface} garante que esta interface tenha apenas um método abstrato,
 * permitindo que seja utilizada com expressões lambda.
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