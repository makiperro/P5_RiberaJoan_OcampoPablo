package backtracking;

import estructura.Encreuades;
import estructura.PosicioInicial;
import greedy.SolucióVoraç;

import java.util.ArrayList;

public class SolucioBacktracking {

    /* TODO
     * cal definir els atributs necessaris
     */
    private int valorParaula;
    private int valorTotalParaules;
    private final Encreuades repte;
    private Encreuades solucioProvisional;
    private Encreuades solucioMillor;
    private boolean [] indexMarcat ;


    public SolucioBacktracking(Encreuades repte) {
        this.repte = repte;
    }

    public char[][] getMillorSolucio() {
        //TODO
        return solucioMillor.getPuzzle();
    }

    public Runnable start(boolean optim) {
        /* TODO
         * cal inicialitzar els atributs necessaris
         */
        valorParaula = 0;
        valorTotalParaules = 0;
		indexMarcat = new boolean[repte.getItemsSize()];
		for (int i = 0; i < repte.getItemsSize(); i++) {
			indexMarcat[i] = false;
		}


        if (!optim) {
            if (!this.backUnaSolucio(0))
                throw new RuntimeException("solució no trobada");
            guardarMillorSolucio();
        } else
            this.backMillorSolucio(0);
        return null;
    }

    /* esquema recursiu que troba una solució
     * utilitzem una variable booleana (que retornem)
     * per aturar el recorregut quan haguem trobat una solució
     */
    private boolean backUnaSolucio(int indexUbicacio) {
        boolean trobada = false;
        // iterem sobre els possibles elements
        for (int indexItem = 0; indexItem < this.repte.getItemsSize() && !trobada; indexItem++) {
            //mirem si l'element es pot posar a la ubicació actual
            if (acceptable(indexUbicacio, indexItem)) {
                //posem l'element a la solució actual
                anotarASolucio(indexUbicacio, indexItem);

                if (esSolucio(indexUbicacio)) { // és solució si totes les ubicacions estan plenes
                    return true;
                } else
                    trobada = this.backUnaSolucio(indexUbicacio + 1); //inserim la següent paraula
                if (!trobada)
                    // esborrem la paraula actual, per després posar-la a una altra ubicació
                    desanotarDeSolucio(indexUbicacio, indexItem);
            }
        }
        return trobada;
    }

    /* TODO
     * Esquema recursiu que busca totes les solucions
     * no cal utilitzar una variable booleana per aturar perquè busquem totes les solucions
     * cal guardar una COPIA de la millor solució a una variable
     */
    private void backMillorSolucio(int indexUbicacio) {
       int valor = calcularFuncioObjectiu(solucioProvisional.getPuzzle());

        for(int i = 0; i < this.calendari.length; ++i) {
            for(int j = 0; j < this.calendari[i].length; ++j) {
                if (this.acceptable(i,j)) {
                    int nAfectatsTmp = this.anotarValor(profunditat, i, j);
                    if (profunditat == this.ass.length - 1) {
                        if (this.nAfectats < this.nAfectatsMillor) {
                            this.guardarResultat();
                        }
                    } else if (this.nAfectats < this.nAfectatsMillor) {
                        this.backMillorSolucio(profunditat + 1);
                    }

                    this.desanotarValor(i, j, nAfectatsTmp);
                }
            }
        }
    }

    private boolean acceptable(int indexUbicacio, int indexItem) {
        //TODO
        if (repte.getEspaisDisponibles().get(indexUbicacio).getLength() == repte.getItem(indexItem).length) {
            return true;
        }
        return false;
    }

    private void anotarASolucio(int indexUbicacio, int indexItem) {//me ha dado una embolia cerebral pensando en esto, aqui va la explicación no fiable
        char[][] newPuzzle = repte.getPuzzle(); // duplicamos este puzle, ya que vamos a crear otro objeto encreuades y tenemos que crear los objetos que pasaremos por parametros en el constructor
        ArrayList espaiDisponible = (ArrayList) repte.getEspaisDisponibles(); // que nos den los espacios disponibles, realmente esto era para poder raelizar un casteo para que me dejara poner el metodo .get() sobre esta lista, y de esta manera poder acceder a la posicionInicial disponible
        PosicioInicial aux = (PosicioInicial) espaiDisponible.get(indexUbicacio); // guardo esa posicion inicial en un auxiliar para poder acceder a sus getters

		if(!indexMarcat[indexItem]) {


        for (int i = 0; i < repte.getItem(indexItem).length; i++) { //tenemos que apuntar caracter a caracter en el array 2D puzzle en las posiciones correspondientes
            if (aux.getDireccio() == 'h') {
                newPuzzle[aux.getInitCol() + i][aux.getInitRow()] = repte.getItem(indexItem)[i];
            }
            newPuzzle[aux.getInitCol()][aux.getInitRow() + i] = repte.getItem(indexItem)[i];
        }
        //eliminamos el item del array = tenemos que copiar este array en otro y añadirle todo el contenido que tiene excepto el que hemos anotado, podemos tambien añadir el indice de este en la lista de  indexMarcat, de esta forma, antes de añadir un resultado, tenemos que ver si el indice de este se encuentra o no en la lista, si se encuentra, no lo podemos anotar, si no se encuentra, lo anotamos.
        //de hecho, yo marcaría, por algo tenemos ese atributo...
        indexMarcat[indexItem]=true;//<--marcamos REVISAR MAS TARDE
        //añadimos?? no se man, he hecho un atributo provisional, antes de meterlo a repte
        //faak, los items son privados rip
        solucioProvisional = new Encreuades(newPuzzle, SolucióVoraç.getItems(repte));
		}

    }

    private void desanotarDeSolucio(int indexUbicacio, int indexItem) {
        //TODO
        ArrayList espaiDisponible = (ArrayList) repte.getEspaisDisponibles();
        PosicioInicial aux = (PosicioInicial) espaiDisponible.get(indexUbicacio);
		char [] itemToBeRemoved = repte.getItem(indexItem);
        char[][] newPuzzle = solucioProvisional.getPuzzle();
        for (int i = 0; i < newPuzzle.length; i++) {
            for (int j = 0; j < newPuzzle[i].length; j++) {
                if (aux.getInitCol() == j && aux.getInitRow() == i) {

                    for (int k = 0; k < repte.getItem(indexItem).length; k++) {
                        if (aux.getDireccio() == 'h') {
							if(newPuzzle[i][j + k] == itemToBeRemoved[k]) {
								newPuzzle[i][j + k] = ' ';
							}
                        }
						if(newPuzzle[i+k][j] == itemToBeRemoved[k]) {
							newPuzzle[i + k][j] = ' ';
						}
                    }
                }
            }
        }
		solucioProvisional = new Encreuades( newPuzzle, SolucióVoraç.getItems(repte));
    }


    private boolean esSolucio(int index) {
         //TODO

		if(solucioProvisional.getEspaisDisponibles() == null ||solucioProvisional.getEspaisDisponibles().size() == 0)
			return true;
		return false;
    }

    private int calcularFuncioObjectiu(char[][] matriu) {
		int valor=0;
        for( int i = 0; i < matriu.length; i++) {
			for( int j = 0; j < matriu[i].length; j++) {
				valor += matriu[i][j];
			}
		}
		return valor;
    }

    private void guardarMillorSolucio() {
        // TODO - cal guardar un clone

    }

    public String toString() {
        String resultat = "";
        //TODO
        return resultat;
    }


}
