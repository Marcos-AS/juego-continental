package rmimvc.src.observer;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Esta clase, entre otras, permiten aplicar el patrón Observer
 * entre el modelo que reside en el servidor y el controlador que posee cada cliente.
 * El modelo remoto debe extender de esta clase para poder notificar a los controladores de los cambios
 * mediante el método notificarObservadores().
 * Cada controlador debe suscribirse con el método agregarObservador() para recibir las actualizaciones del modelo remoto.
 *
 */
public abstract class ObservableRemoto implements Remote, IObservableRemoto {
	private ArrayList<IObservadorRemoto> observadores;
	
	public ObservableRemoto() {
		this.observadores = new ArrayList<>();
	}

	@Override
	public void agregarObservador(IObservadorRemoto o) throws RemoteException {
		this.observadores.add(o);
	}

	@Override
	public void removerObservador(IObservadorRemoto o) throws RemoteException {
		this.observadores.remove(o);
	}

	@Override
	public void notificarObservadores(Object obj) throws RemoteException {
		for (IObservadorRemoto o: this.observadores)
			o.actualizar(this, obj);
	}

	@Override
	public void notificarObservadores() throws RemoteException {
		this.notificarObservadores(null);
	}

	@Override
	public void notificarObservadores(int[] jugadoresQuePuedenRobarConCastigo) throws RemoteException {
		for (int i = 0; i < observadores.size()-1; i++) {
			observadores.get(jugadoresQuePuedenRobarConCastigo[i]).actualizar(this, jugadoresQuePuedenRobarConCastigo);
		}
	}

}
