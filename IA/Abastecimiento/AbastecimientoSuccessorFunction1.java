package Abastecimiento;

// IMPORTS.
import java.util.*;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

public class AbastecimientoSuccessorFunction1 implements SuccessorFunction{
	
	public boolean assignacionsContains (@SuppressWarnings("exports") ArrayList<ArrayList<Peticion>> assig, Pair <Integer, Integer> p) {
		for (ArrayList <Peticion> a : assig) {
			for (Peticion pet : a) {
				if (p.equals(pet.get())) return true;
			}
		}
		return false;
	}
	
    public List getSuccessors (Object state) {
    	ArrayList <Successor> ret = new ArrayList<>();
    	AbastecimientoState as = (AbastecimientoState) state;
    	
    	int ncen = as.centrosDistribucion.size();
    	int ngas = as.gasolineras.size();
    	
    	for (int i = 0; i < ncen; i++) {
    		
    		// asigna peticiones no asignadas
    		for (int j = 0; j < ngas; j++) {
    			for (int k = 0; k < as.gasolineras.get(j).getPeticiones().size(); k++) {
    				Pair <Integer, Integer> p = new Pair <Integer, Integer>(j, k);
    				if (!assignacionsContains (as.getAsignaciones(), p)) {
	    				AbastecimientoState newState = new AbastecimientoState (as);
	    				if (newState.asignaPeticion(i, new Pair <Integer, Integer>(j, k))) {
	    					StringBuffer s = new StringBuffer ();
		        			s.append("add petition gas station: " + j + " petition " + k + " to truck " + i);
		        			ret.add(new Successor (s.toString(), newState));
	    				}
    				}
    			}
    		}
    		
    		// mover paquetes dentro del camion
    		int m = as.getAsignaciones().get(i).size();
    		for (int j = 0; j < m; j++) {
    			for (int k = j+1; k < m; k++) {
    				AbastecimientoState newState = new AbastecimientoState (as);
    				if (newState.intercambioOrden (j, k, i)) {
	    				StringBuffer s = new StringBuffer ();
	    				s.append("swap petition order, truck " + i + " petition " + j + " changed with petition " + k);
	    				ret.add(new Successor (s.toString(), newState));
    				}
    			}
    		}
    		
    		//mover paquetes con los que no estan assignados
    		for (int j = 0; j < ngas; j++) {
    			for (int k = 0; k < as.gasolineras.get(j).getPeticiones().size(); k++) {
    				Pair <Integer, Integer> p = new Pair <Integer, Integer>(j, k);
    				if (!assignacionsContains (as.getAsignaciones(), p)) {
    					for (int l = 0; l < m; l++) {
	    					AbastecimientoState newState = new AbastecimientoState (as);
	    					if (newState.cambioPeticionNoAsig (l, i, p)) {
		        				StringBuffer s = new StringBuffer ();
		        				s.append("swap petition order, truck " + i + " petition (" + j + "," + k + ")" + 
		        							" changed with petition pos " + l);
		        				ret.add(new Successor (s.toString(), newState));
	    					}
    					}
    				}
    			}
    		}
    		
    		//mover paquetes con los que ya estan assignados
    		for (int j = i + 1; j < ncen; j++) {
    			for (int k = 0; k < as.getAsignaciones().get(i).size(); k++) {
    				for (int l = 0; l < as.getAsignaciones().get(j).size(); l++) {
    					AbastecimientoState newState = new AbastecimientoState (as);
    					if (newState.intercambiaPeticiones (k, l, i, j)) {
	        				StringBuffer s = new StringBuffer ();
	        				s.append("swap petition, truck " + i + " petition " + k + " with petition in truck " + j + " petition " + l);
	        				ret.add(new Successor (s.toString(), newState));
    					}
	    			}
    			}
    		}
    		
    		// cambia peticiones
    		for (int j = i + 1; j < ncen; j++) {
    			for (int k = 0; k < as.getAsignaciones().get(j).size(); k++) {
    				AbastecimientoState newState = new AbastecimientoState (as);
    				if (newState.cambiaPeticion(k, j, i)) {
    					StringBuffer s = new StringBuffer ();
        				s.append("swap petition " + k + " from truck " + j + " to truck " + i);
        				ret.add(new Successor (s.toString(), newState));
    				}
    			}
    		}
    	}
    	
        return ret;
    }
}
