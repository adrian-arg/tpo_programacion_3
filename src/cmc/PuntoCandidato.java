package cmc;

import java.util.*; 

import graficos.Punto;

public class PuntoCandidato implements Comparable<PuntoCandidato>{
	
	/**
	 * setAbierto
	 * @param abierto utiliza false para dejar el Punto cerrado y true como abierto
	 */
	
	private double costoDistancia;
	private double costoPeso;
	private double costoAcumulado;
	public int x, y;
	private boolean abierto; //Abierto == true , Cerrado == false
//	private Punto punto;
	private Punto destino;
	private PuntoCandidato predecesor;
	private double multiploPeso = 1;
	
	public PuntoCandidato(double costoPeso, int x, int y, 
			PuntoCandidato predecesor, Punto destino) {

		/* PuntoCandidato */
		this.x = x;
		this.y = y;
//		this.punto = new Punto(x,y);
		/* PuntoCandidato */
		this.predecesor = predecesor;
		this.abierto = true;
		
		this.costoDistancia = calcularDistanciaADestino(destino);
		this.costoPeso = costoPeso;
		this.costoAcumulado = calcularCostoAcumulado();

	}
	

	private double calcularDistanciaADestino(Punto destino) {
		return (Math.abs(this.x - destino.getX()) + Math.abs(this.y - destino.getY())) * multiploPeso; 
	}
	
	public double calcularCostoAcumulado() {
		if(this.predecesor!=null){
//			return this.costoDistancia + this.costoPeso;
			return this.predecesor.getCostoAcumulado() + this.costoDistancia + this.costoPeso;
		}
		else{
			return this.costoDistancia + this.costoPeso;
		}
	}
	
	public boolean isAbierto() {
		return abierto;
	}
 
	public PuntoCandidato getPredecesor() {
		return predecesor;
	}
	
	public double getCostoDistancia() {
		return costoDistancia;
	}

	public double getCostoAcumulado() {
		return costoAcumulado;
	}

	public void setCostoAcumulado(double costoAcumulado) {
		this.costoAcumulado = costoAcumulado;
	}

	public void setAbierto(boolean abierto) {
		this.abierto = abierto;
	}
	
	public void setEjes(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setDestino(Punto d) {
		this.destino = d;
	}

	public void setPredecesor(PuntoCandidato p) {
		this.predecesor = p;
	}
	
//	@Override
//	public int compareTo(PuntoCandidato otroPunto) {
////		double diferencia = this.costoAcumulado - ((PuntoCandidato) otroPunto).getCostoAcumulado();
////		return (int)diferencia;
//		
//		if(this.costoAcumulado > otroPunto.getCostoAcumulado()) {
//            return 1;
//        } else if (this.costoAcumulado < otroPunto.getCostoAcumulado()) {
//            return -1;
//        } else {
//            return 0;
//        }
//	}
	
	@Override
	public int compareTo(PuntoCandidato otroPunto) {
	  {
	    if (this.costoAcumulado < otroPunto.getCostoAcumulado()) {
	      return -1;
	    }
	    if (this.costoAcumulado > otroPunto.getCostoAcumulado()) {
	      return 1;
	    }
	    if (this.costoDistancia < otroPunto.getCostoDistancia()) {
	      return 1;
	    }
	    if (this.costoDistancia > otroPunto.getCostoDistancia()) {
	      return -1;
	    }
	    //	    return this.toString().compareTo(otroPunto.toString());
	   	return this.getCoordenadaString().compareTo(otroPunto.getCoordenadaString());
	  }
	}
	
	
	  public String getCoordenadaString()
	  {
	    return String.valueOf("("+this.x) + ";" + String.valueOf(this.y+")");
	  }
	
	
	
//	public PuntoCandidato mejorPuntoCandidato(PuntoCandidato PuntoOrigen, List<Object> listaPuntosUsados) {
//		
//		PuntoCandidato a = null, b = null, c = null, d = null, e = null, f = null, g = null, h = null;
//		
//		a.setEjes(PuntoOrigen.x-1 , PuntoOrigen.y-1); a.setDestino(PuntoOrigen.destino);
//		b.setEjes(PuntoOrigen.x   , PuntoOrigen.y-1); b.setDestino(PuntoOrigen.destino);
//		c.setEjes(PuntoOrigen.x+1 , PuntoOrigen.y-1); c.setDestino(PuntoOrigen.destino);
//		d.setEjes(PuntoOrigen.x-1 , PuntoOrigen.y );  d.setDestino(PuntoOrigen.destino);
//		e.setEjes(PuntoOrigen.x+1 , PuntoOrigen.y);   e.setDestino(PuntoOrigen.destino);
//		f.setEjes(PuntoOrigen.x-1 , PuntoOrigen.y+1); f.setDestino(PuntoOrigen.destino);
//		g.setEjes(PuntoOrigen.x   , PuntoOrigen.y+1); g.setDestino(PuntoOrigen.destino);
//		h.setEjes(PuntoOrigen.x+1 , PuntoOrigen.y+1); h.setDestino(PuntoOrigen.destino);
//		
//		Queue<PuntoCandidato> cola = new PriorityQueue<PuntoCandidato>(); 
//		cola.add(a);	
//		cola.add(b); 
//		cola.add(c); 
//		cola.add(d); 
//		cola.add(e);
//		cola.add(f);
//		cola.add(g);
//		cola.add(h);
//		
//		while (!cola.isEmpty()) { 
//			//Busco si existe en la lista que pase de puntos si existe
//			//Si existe, lo descarto
//			//Sino, lo utilizo
//             cola.poll(); 
//        }  
//		 
//		return null;
//	}

}
