package greedy;

import estructura.Encreuades;
import estructura.PosicioInicial;

import java.util.ArrayList;
import java.util.List;

public class SolucióVoraç {
    private final Encreuades repte;
    private final char[][] solucio;

    public SolucióVoraç(Encreuades repte) {
        this.repte = repte;
        this.solucio = greedy();
    }

    public char[][] getSolucio() {
        return solucio;
    }

    private char[][] greedy() {
        char[][] copiaPuzzle = clonarPuzzle(repte.getPuzzle());
        List<PosicioInicial> espais = repte.getEspaisDisponibles();

        char[][] paraules = getItems(this.repte);
        boolean[] paraulesUsades = new boolean[paraules.length];

        for (PosicioInicial espai : espais) {
            char[] millorOpcio = null;
            int millorPuntuacio = -1;
            int indexMillorParaula = -1;

            for (int i = 0; i < paraules.length; i++) {
                if (paraulesUsades[i]) continue;

                char[] paraula = paraules[i];
                if (paraula.length == espai.getLength() && esPotColocar(copiaPuzzle, espai, paraula)) {
                    int puntuacioParaula = calcularPuntuacio(paraula);
                    if (puntuacioParaula > millorPuntuacio) {
                        millorOpcio = paraula;
                        millorPuntuacio = puntuacioParaula;
                        indexMillorParaula = i;
                    }
                }
            }

            if (millorOpcio != null) {
                insertarParaula(copiaPuzzle, espai, millorOpcio);
                paraulesUsades[indexMillorParaula] = true;
            }
        }

        return copiaPuzzle;
    }

    private boolean esPotColocar(char[][] puzzle, PosicioInicial espai, char[] paraula) {
        int fila = espai.getInitRow();
        int columna = espai.getInitCol();
        boolean esHoritzontal = espai.getDireccio() == 'H';

        for (int i = 0; i < paraula.length; i++) {
            char existent = esHoritzontal ? puzzle[fila][columna + i] : puzzle[fila + i][columna];
            if (existent != ' ' && existent != paraula[i]) {
                return false;
            }
        }
        return true;
    }

    private void insertarParaula(char[][] puzzle, PosicioInicial espai, char[] paraula) {
        int fila = espai.getInitRow();
        int columna = espai.getInitCol();
        boolean esHoritzontal = espai.getDireccio() == 'H';

        for (int i = 0; i < paraula.length; i++) {
            if (esHoritzontal) {
                puzzle[fila][columna + i] = paraula[i];
            } else {
                puzzle[fila + i][columna] = paraula[i];
            }
        }
    }

    private int calcularPuntuacio(char[] paraula) {
        int puntuacio = 0;
        for (char lletra : paraula) {
            puntuacio += lletra;
        }
        return puntuacio;
    }

    private char[][] clonarPuzzle(char[][] original) {
        char[][] copia = new char[original.length][];
        for (int i = 0; i < original.length; i++) {
            copia[i] = original[i].clone();
        }
        return copia;
    }

    public static char[][] getItems(Encreuades repte){
        int rows = repte.getItemsSize();
        char[][] itemsCopy = new char[rows][];


        for (int i = 0; i < rows; i++) {
            char[] row = repte.getItem(i);
            itemsCopy[i] = new char[row.length];
            System.arraycopy(row, 0, itemsCopy[i], 0, row.length); 
        }
        return itemsCopy;
    }

}
