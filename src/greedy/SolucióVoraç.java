package greedy;

import estructura.Encreuades;
import estructura.PosicioInicial;

import java.util.List;

public class SolucióVoraç {
    /* TODO
     * cal definir els atributs necessaris
     */
    private final Encreuades repte;
    private char[][] solucio;

    public SolucióVoraç(Encreuades repte) {
        /* TODO
         * cal inicialitzar els atributs necessaris
         * i invocar al mètode greedy
         */
        this.repte = repte;

    }

    public char[][] getSolucio() {
        return null; // TODO
    }


    // mireu l'esquema utilitzat per la professora Lina
    // si voleu podeu modificar la capçalera d'aquest mètode
    // si voleu podeu utilitzar recursivitat
    //TODO
    private char[][] greedy() {
        char[][] encreuat = repte.getPuzzle();
        List<PosicioInicial> disponibilitat = repte.getEspaisDisponibles();
        for (PosicioInicial espai : repte.getEspaisDisponibles()) {
            for (int i = 0; i < repte.getItemsSize(); i++) {
                char[] item = repte.getItem(i);
                if (item.length == espai.getLength() && posarParaula(encreuat, espai, item)) {
                    insertItem(encreuat, espai, item);
                }
            }
        }
        return encreuat;
    }



    /* A aquesta classe
     * podeu definir els mètodes privats que vulgueu
     */

    private boolean posarParaula(char[][] encreuat, PosicioInicial inicial, char[] paraula) {
        int fila = inicial.getInitRow();
        int columna = inicial.getInitCol();
        int dFila = inicial.getDireccio() == 'V' ? 1 : 0;
        int dColumna = inicial.getDireccio() == 'H' ? 1 : 0;

        for (int i = 0; i < paraula.length; i++) {
            char actual = encreuat[fila + i * dFila][columna + i * dColumna];
            if (actual != ' ' && actual != paraula[i]) {
                return false;
            }
        }
        for (int i = 0; i < paraula.length; i++) {
            encreuat[fila + i * dFila][columna + i * dColumna] = paraula[i];
        }
        return true;
    }


    private void insertItem (char[][] puzzle, PosicioInicial espai, char[] item) {
        int row = espai.getInitRow();
        int col = espai.getInitCol();
        int dRow = espai.getDireccio() == 'V' ? 1 : 0;
        int dCol = espai.getDireccio() == 'H' ? 1 : 0;

        for (int i = 0; i < item.length; i++) {
            puzzle[row + i * dRow][col + i * dCol] = item[i];
        }
    }

}
