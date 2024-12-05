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

        char[][] paraules = getItems(); // Work directly with the 2D char array
        boolean[] paraulesUsades = new boolean[paraules.length]; // Use the length of the 2D array

        for (PosicioInicial espai : espais) {
            char[] millorOpcio = null;
            int millorPuntuacio = -1;
            int indexMillorParaula = -1;

            for (int i = 0; i < paraules.length; i++) { // Iterate through the 2D array
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

    private char[][] getItems(){
        int rows = repte.getItemsSize(); // Number of rows in the array
        char[][] itemsCopy = new char[rows][]; // Create a 2D array with the same number of rows

        // Copy each row from the original array using getItem().
        for (int i = 0; i < rows; i++) {
            char[] row = repte.getItem(i); // Get the original row
            itemsCopy[i] = new char[row.length]; // Create a new row of the same length
            System.arraycopy(row, 0, itemsCopy[i], 0, row.length); // Copy the row into the new array
        }
        return itemsCopy;
    }

}
