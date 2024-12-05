package greedy;

import estructura.Encreuades;
import estructura.PosicioInicial;

import java.util.List;

public class SolucioVorac {
    private final Encreuades crucigrama;
    private final char[][] solucio;

    public SolucioVorac(Encreuades crucigrama) {
        this.crucigrama = crucigrama;
        this.solucio = generarSolucio();
    }

    public char[][] obtenirSolucio() {
        return solucio;
    }

    private char[][] generarSolucio() {
        char[][] copiaPuzzle = clonarPuzzle(crucigrama.getPuzzle());
        List<PosicioInicial> espais = crucigrama.getEspaisDisponibles();
        List<char[]> paraules = crucigrama.getItem();
        boolean[] paraulesUsades = new boolean[paraules.size()];

        for (PosicioInicial espai : espais) {
            char[] millorOpcio = null;
            int millorPuntuacio = -1;
            int indexMillorParaula = -1;

            for (int i = 0; i < paraules.size(); i++) {
                if (paraulesUsades[i]) continue;

                char[] paraula = paraules.get(i);
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
}
