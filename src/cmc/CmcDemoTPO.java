package cmc;
import java.awt.Color;

import javax.naming.ldap.SortControl;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;



/**
 * Obtiene la lista de los puntos marcados en la matriz (mapa)
 * Itera los mismos de la siguiente forma:
 * Obtiene los 2 primeros y expande los contiguos entre ambos.
 * Primero expande eje x, segundo expande el eje y.
 * Reitera la lista expandiendo el siguiente (siempre expandiendo de a pares)
 * El recorrido es secuencial (conforme al orden de marcado de los puntos en el mapa)
 * Invoca la m�todo dibujar en cada iteraci�n.
 * Al finalizar la iteraci�n expande los contiguos entre el �ltimo y el primero de la lista.
 * Vuelve a Invocar la m�todo dibujar para cerrar el ciclo.
 * No contempla las densidades definidas en la matriz (mapa)
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.SortedSet;
import java.util.TreeSet;
import graficos.Area;
import graficos.Punto;
import mapa.MapaInfo;

public class CmcDemoTPO {
	private MapaInfo mapa;
	private CmcImple cmc;
	boolean llegueADestino = false;
	double diag = 1.41;
	double recto = 1;
	
	public CmcDemoTPO(MapaInfo mapa, CmcImple cmc) {
		this.mapa = mapa;
		this.cmc = cmc;
		demoObtenerCamino();
	}
	
	@SuppressWarnings("null")
	private void demoObtenerCamino() {

		/* Se obtiene un mapa de las densidades */
		double[][] densidades = obtenerDensidades(mapa);
		/* Se crear matriz para puntos extandidos */
		PuntoCandidato[][] expandidos = crearMatrizDeExpandidos(mapa);
		/* Cola de Prioridad */
		SortedSet <PuntoCandidato> colaPrioridad = new TreeSet<PuntoCandidato>();
		
		
		// Buscamos los puntos de origen y destino
		Punto punto_inicio = null, punto_fin = null;
		punto_inicio = mapa.getPuntos().get(0);
		punto_fin = mapa.getPuntos().get(1);
		
		
		PuntoCandidato destino = new PuntoCandidato(0, punto_fin.x, punto_fin.y, null, punto_fin);
		expandidos[destino.x][destino.y] = destino;
		PuntoCandidato inicio = new PuntoCandidato(0, punto_inicio.x, punto_inicio.y, null, punto_fin);
		expandidos[inicio.x][inicio.y] = inicio;
		
		
		colaPrioridad.add(inicio);
		while(!llegueADestino && !colaPrioridad.isEmpty()){
			PuntoCandidato seleccionado = colaPrioridad.first();
			colaPrioridad.remove(seleccionado);
			seleccionado.setAbierto(false);
			expanderPunto(seleccionado, densidades, expandidos, colaPrioridad, mapa, punto_fin);
		}
		

		if(llegueADestino){
	
			List<Punto> listaPuntos = null;		
			listaPuntos = obtenerMejorCamino(expandidos, inicio, destino );
			
			if(!listaPuntos.isEmpty()){
				cmc.dibujarCamino(listaPuntos,Color.red);
				mapa.enviarMensaje(""
						+ "Camino minimo: " + listaPuntos.size() + " puntos. "
						+ "Peso Acumulado: " + Double.toString(destino.getCostoAcumulado()));
			}
		}else{
			mapa.enviarMensaje("No existe un camino posible.");
		}

		
	}
	

	private void expanderPunto(PuntoCandidato p, double[][] densidades, PuntoCandidato[][] expandidos, SortedSet<PuntoCandidato> colaPrioridad, MapaInfo mapa, Punto punto_fin) {
			
		PuntoCandidato diag_izq_arr = null, arr = null, diag_der_arr = null, izq = null, der = null, diag_izq_aba = null, aba = null, diag_aba_der = null;

		if ((p.x-1 >= 0) && (p.y-1 >= 0)){
			// Coordenadas expandido
			int x = p.x-1, y = p.y-1;
			// Se crea nuevo candidato 
			if (densidades[x][y] != mapa.MAX_DENSIDAD) {
				diag_izq_arr = new PuntoCandidato(diag * (densidades[x][y]+1), x,y, p, punto_fin);
				// Actualiza la matriz de expandidos con el nuevo PuntoCandidato
				// si es menor que uno existente o si no existe
				actualizarPuntoEnMatriz(diag_izq_arr, x, y, expandidos,colaPrioridad, punto_fin);
			}
		}
		
		if (p.y-1 >= 0){
			int x = p.x, y = p.y-1;
			if (densidades[x][y] != mapa.MAX_DENSIDAD) {
				arr = new PuntoCandidato(recto * (densidades[x][y]+1), x, y, p, punto_fin);
				actualizarPuntoEnMatriz(arr, x, y, expandidos, colaPrioridad, punto_fin);
			}
		}
		
		if ((p.x+1 <= mapa.LARGO-1) && (p.y-1 >= 0)){
			int x = p.x+1, y = p.y-1;
			if (densidades[x][y] != mapa.MAX_DENSIDAD) {
				diag_der_arr = new PuntoCandidato(diag * (densidades[x][y]+1), x, y, p, punto_fin);
				actualizarPuntoEnMatriz(diag_der_arr, x, y, expandidos, colaPrioridad, punto_fin);
			}
		}
		
		if (p.x-1  >= 0){
			int x = p.x-1, y = p.y;
			if (densidades[x][y] != mapa.MAX_DENSIDAD) {
				izq = new PuntoCandidato(recto * (densidades[x][y]+1), x, y, p, punto_fin);
				actualizarPuntoEnMatriz(izq, x, y, expandidos, colaPrioridad, punto_fin);
			}
		}
		
		if (p.x+1 <= mapa.LARGO-1){
			int x = p.x+1, y = p.y;
			if (densidades[x][y] != mapa.MAX_DENSIDAD) {
				der = new PuntoCandidato(recto * (densidades[x][y]+1), x, y, p, punto_fin);
				actualizarPuntoEnMatriz(der, x, y, expandidos, colaPrioridad, punto_fin);
			}
		}
		
		if ((p.x-1 >= 0) && (p.y+1 <= mapa.ALTO-1)){
			int x = p.x-1, y = p.y+1;
			if (densidades[x][y] != mapa.MAX_DENSIDAD) {
				diag_izq_aba = new PuntoCandidato(diag * (densidades[x][y]+1), x, y, p, punto_fin);
				actualizarPuntoEnMatriz(diag_izq_aba, x, y, expandidos, colaPrioridad, punto_fin);			
			}
		}
		
		if ((p.y+1 <= mapa.ALTO-1)){
			int x = p.x, y = p.y+1;
			if (densidades[x][y] != mapa.MAX_DENSIDAD) {
				aba = new PuntoCandidato(recto * (densidades[x][y]+1), x, y, p, punto_fin);
				actualizarPuntoEnMatriz(aba, x, y, expandidos, colaPrioridad, punto_fin);
			}
		}

		if ((p.x+1 <= mapa.LARGO-1) && (p.y+1 <= mapa.ALTO-1)){
			int x = p.x+1, y = p.y+1;
			if (densidades[x][y] != mapa.MAX_DENSIDAD) {
				diag_aba_der = new PuntoCandidato(diag * (densidades[x][y]+1), x, y, p, punto_fin);
				actualizarPuntoEnMatriz(diag_aba_der, x, y, expandidos, colaPrioridad, punto_fin);
			}
		}
	
	}
	
	private void actualizarPuntoEnMatriz(PuntoCandidato nuevoCandidato, int x, int y, PuntoCandidato[][] expandidos, SortedSet<PuntoCandidato> colaPrioridad, Punto punto_fin){
		// Se evalua si ya hay un candidato en la matriz de expandidos, sino hay uno se agrega y lo agrega en la cola prioridad
		if( x == punto_fin.x && y == punto_fin.y){
			llegueADestino = true;
			expandidos[x][y].setPredecesor(nuevoCandidato.getPredecesor());
			expandidos[x][y].setCostoAcumulado(nuevoCandidato.getCostoAcumulado());
			colaPrioridad.add(expandidos[x][y]);
		}else{
			if(expandidos[x][y] == null){
				expandidos[x][y] = nuevoCandidato;
				colaPrioridad.add(nuevoCandidato);
			}
			// Se evalua si el nuevo candidato tiene menor costo que el que esta en la matriz y se reemplaza en la cola prioridad y en la matriz
			else if(expandidos[x][y].isAbierto() && expandidos[x][y].getCostoAcumulado() > nuevoCandidato.getCostoAcumulado()){
				colaPrioridad.remove(expandidos[x][y]);
				expandidos[x][y] = nuevoCandidato;
				colaPrioridad.add(nuevoCandidato);
			}
		}
	}
	
	
	/**
	 * @param mapa
	 * @return
	 */
	private double[][] obtenerDensidades(MapaInfo mapa) {
		double[][] densidades = new double[mapa.LARGO][mapa.ALTO];
		for (int i = 0; i < mapa.LARGO; i++) {
			Arrays.fill(densidades[i], 0);
		}
		List<Area> areas = mapa.getAreas();
		for (Area a : areas) {
			int[] coordenadas = a.getCoordenadas();
			int x_ini = coordenadas[0];
			int y_ini = coordenadas[1];
			int x_fin = x_ini + coordenadas[2];
			int y_fin = y_ini + coordenadas[3];
			
			// System.out.println("X inicio " + x_ini);
			// System.out.println("Y inicio " + y_ini);
			// System.out.println("X fin " + x_fin);
			// System.out.println("Y fin " + y_fin);

			/* EL MAPA NO TOMA EL ULTIMO PUNTO MARCADO SINO (X-1,Y-1) */
			for (int x = x_ini; x < x_fin; x++) {
				for (int y = y_ini; y < y_fin; y++) {
					// System.out.println("Densidad "+x+","+y+": "+densidades[x][y]);
					// System.out.println("Mapa "+x+","+y+": "+mapa.getDensidad(x,y));
					if (mapa.getDensidad(x, y) > densidades[x][y]) {
						densidades[x][y] = mapa.getDensidad(x, y);
					}
				}
			}

		}
		return densidades;
	}
	
	private PuntoCandidato[][] crearMatrizDeExpandidos(MapaInfo mapa){
		PuntoCandidato[][] matrizExpandidos = new PuntoCandidato[mapa.LARGO][mapa.ALTO];
		for(int i = 0; i< mapa.LARGO; i++) {
			Arrays.fill(matrizExpandidos[i], null);
		}
		return matrizExpandidos;
	}
	
	private List<Punto> obtenerMejorCamino(PuntoCandidato [][] expandidos, PuntoCandidato inicio, PuntoCandidato destino ){
		
		PuntoCandidato actual = destino;
		
		List <Punto> camino = new ArrayList<Punto>();
		camino.add(new Punto(destino.x,destino.y));
		
		while(actual.getPredecesor()!=null){
			actual = actual.getPredecesor();
			camino.add(new Punto(actual.x,actual.y));
		}
		
		return camino;
	}
	
}


