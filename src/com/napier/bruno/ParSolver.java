package com.napier.bruno;

/**
 * -- The Parser/Solver class --
 * Here is where the magic will be processed.
 * Information will be sent and receive by Parsers and interact with the solution.
 * The solvers are located in the bottom of this class.
 * @author Bruno Pascaretta Guerra
 */
public class ParSolver {

    /**
     * Declaring parsing variables
     */
    private int[][] field = new int[9][9];
    private int[][] usrBoard = new int[9][9];
    /**
     * Declaring solving variables
     */
    private boolean[][] validateRow = new boolean[9][9];
    private boolean[][] validateCol = new boolean[9][9];
    private boolean[][][] validateCell = new boolean[3][3][9];
    private boolean[][] solvedCell = new boolean[3][3];
    private boolean[] solvedRow = new boolean[9];
    private boolean[] solvedCol = new boolean[9];
    /**
     * Parsing values to the board
     * @param values
     */
    protected ParSolver(String values){
        field = parseString(values);
    }

    /**
     * Board organising.
     * @param string
     * @return
     */
    private int[][] parseString(String string) {
        int counter = -1;
        String replacedString = string.replaceAll("\\W", "");
        for (int i = 0; i < this.field.length; i++) {
            for (int j = 0; j < this.field[i].length; j++) {
                counter++;
                this.field[i][j] = replacedString.charAt(counter) - '0';
            }
        }
        return this.field;
    }

    /**
     * Get board values
     * @return
     */
    protected int[][] getBoard() {
        return this.field;
    }

    /**
     * Organising columns and rows indicators -- Parser toString
     * @return
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("    A B C   D E F   G H I\n");
        for (int i = 0; i < field.length; i++) {
            if (i == 0 || i == 3 || i == 6) {
                result.append("  " + repeat("-", 25) + "\n");
            }
            for (int j = 0; j < field[i].length; j++) {
                if (j == 3 || j == 6) {
                    result.append("| " + field[i][j] + " ");
                }
                else if (j == 0) {
                    result.append((i + 1) + " | " + field[i][j] + " ");
                }
                else if (j == field[i].length - 1) {
                    result.append(field[i][j] + " |\n");
                }
                else {
                    result.append(field[i][j] + " ");
                }
            }
        }
        //Replace the 0's for "-" (Hifens), making easier to the user to identify
        result.append("  " + repeat("-", 25));
        String finalResult = result.toString().replaceAll("0", "-");
        return finalResult;
    }

    /**
     * Parsing strings to output with blockValues
     * @return
     */

    public String output() {
        StringBuilder result = new StringBuilder();
        int blockValue;
        result.append("    A B C   D E F   G H I\n");
        for (int i = 0; i < field.length; i++) {
            if (i == 0 || i == 3 || i == 6) {
                result.append("  " + repeat("-", 25) + "\n");
            }
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] > 0) {
                    blockValue = field[i][j];
                }
                else {
                    if (usrBoard[i][j] > 0) {
                        blockValue = usrBoard[i][j];
                    }
                    else {
                        blockValue = 0;
                    }
                }
                if (j == 3 || j == 6) {
                    result.append("| " + blockValue + " ");
                }
                else if (j == 0) {
                    result.append((i + 1) + " | " + blockValue + " ");
                }
                else if (j == field[i].length - 1) {
                    result.append(blockValue + " |\n");
                }
                else {
                    result.append(blockValue + " ");
                }
            }
        }
        result.append("  " + repeat("-", 25));
        String finalResult = result.toString().replaceAll("0", "-");
        return finalResult;
    }

    /**
     * Builder repeat function.
     * @param s
     * @param n
     * @return
     */

    private String repeat(String s, int n) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            builder.append(s);
        }
        return builder.toString();
    }

    /**
     * Checks if number is present in row.
     * @param value
     * @param row
     * @return
     */
    public boolean presentInRow(int value, int row) {
        int[] rowArray = this.field[row];
        for (int i = 0; i < rowArray.length; i++) {
            if (value == rowArray[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if number is present in collumn.
     * @param value
     * @param col
     * @return
     */

    public boolean presentInCol(int value, int col) {
        int[] columnArray = new int[9];
        for (int i = 0; i < 9; i++) {
            columnArray[i] = this.field[i][col];
        }
        for (int i = 0; i < 9; i++) {
            if (value == columnArray[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if number is present in cell.
     * @param value
     * @param col
     * @param row
     * @return
     */

    public boolean presentInCell(int value, int col, int row) {
        int[] cellArray = new int[9];
        int celCol = col / 3;
        int celRow = row / 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cellArray[i * 3 + j] = this.field[celRow * 3 + i][celCol * 3 + j];
            }
        }
        for (int i = 0; i < cellArray.length; i++) {
            if (value == cellArray[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get board function.
     * @return
     */

    public String getBoardAsString() {
        int counter = 0;
        String result = "";
        for (int i = 0; i < this.field.length; i++) {
            for (int j = 0; j < this.field[i].length; j++) {
                counter++;
                result += field[i][j];
                if (counter % 3 == 0 && counter != 81) {
                    result += " ";
                }
            }
        }
        return result;
    }

    /**
     * Adds the number selected by user.
     * @param col
     * @param row
     * @param value
     * @return
     */
    public boolean add(int col, int row, int value) {
        if (this.field[row][col] == 0 && value != 0) {
            if (!presentInCol(value, col) && !presentInRow(value, row) && !presentInCell(value, col, row)) {
                this.usrBoard[row][col] = value;
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the number selected by user.
     * @param col
     * @param row
     * @return
     */

    public int remove(int col, int row) {
        int result;
        if (this.field[row][col] != 0) {
            return 0;
        }
        result = this.usrBoard[row][col];
        this.usrBoard[row][col] = 0;
        return result;
    }

    /**
     * Checks if the user won.
     * @return
     */

    public boolean isSolved() {
        int counter = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (this.field[i][j] != 0) {
                    counter++;
                    this.usrBoard[i][j] = this.field[i][j];
                }
                else {
                    if (this.usrBoard[i][j] != 0) {
                        counter++;
                    }
                }
            }
        }
        if (counter != 81) {
            return false;
        }
        for (int i = 0; i < 9; i++) {
            int[] array = new int[9];
            int value;
            for (int j = 0; j < 9; j++) {
                value = this.usrBoard[i][j];
                array[value - 1] = 1;
            }
            for (int j = 0; j < 9; j++) {
                if (array[j] == 0) {
                    return false;
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            int[] array = new int[9];
            int value;
            for (int j = 0; j < 9; j++) {
                value = this.usrBoard[j][i];
                array[value - 1] = 1;
            }
            for (int j = 0; j < 9; j++) {
                if (array[j] == 0) {
                    return false;
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int[] array = new int[9];
                int value;
                for (int k = 0; k < 9; k++) {
                    value = this.usrBoard[i * 3 + k / 3][j * 3 + k % 3];
                    array[value - 1] = 1;
                }
                for (int k = 0; k < 9; k++) {
                    if (array[k] == 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    /**
     * Solvers
     */

    /**
     * Analyze if number is possible in Row
     */
    private void analyzeRow() {
        int number;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                number = this.field[row][col];
                if (number > 0 && number <= 9) {
                    this.validateRow[row][number - 1] = true;
                }
            }
        }
    }

    /**
     * Analyze if number is possible in Collumn
     */

    private void analyzeCol() {
        int number;
        for (int col = 0; col < 9; col++) {
            for (int row = 0; row < 9; row++) {
                number = this.field[row][col];
                if (number > 0 && number <= 9) {
                    this.validateCol[col][number - 1] = true;
                }
            }
        }
    }

    /**
     * Analyze if number is possible in Cell
     */

    private void analyzeCell() {
        int number;
        for (int cellRow = 0; cellRow < 3; cellRow++) {
            for (int cellCollumn = 0; cellCollumn < 3; cellCollumn++) {
                for (int i = 0; i < 9; i++) {
                    number = this.field[cellRow * 3 + i / 3][cellCollumn * 3 + i % 3];
                    if (number > 0 && number <= 9) {
                        this.validateCell[cellRow][cellCollumn][number - 1] = true;
                    }
                }
            }
        }
    }

    /**
     * Updates to analyze the next number.
     * @param number
     * @param row
     * @param col
     */

    private void updateAnalyzed(int number, int row, int col) {
        if (number >= 1 && number <= 9) {
            this.validateRow[row][number - 1] = true;
            this.validateCol[col][number - 1] = true;
            this.validateCell[row / 3][col / 3][number - 1] = true;
        }
    }

    /**
     * Find possible values
     * @param arr
     * @return
     */

    private int findPossibleValues(boolean[] arr) {
        int counter = 0;
        int setValues = 0;
        for (int value = 1; value <= 9; value++) {
            if (!arr[value - 1]) {
                counter++;
                setValues = value;
            }
        }
        if (counter == 1) {
            return setValues;
        }
        else {
            return -1;
        }
    }

    /**
     * Find undone block.
     * @param matrix
     * @param doneBlocks
     * @param value
     * @return
     */

    private int findBlocks(boolean[][] matrix, boolean[] doneBlocks, int value) {
        int counter = 0;
        int setBlock = 0;
        for (int i = 0; i < 9; i++) {
            if (!doneBlocks[i] && !matrix[i][value - 1]) {
                counter++;
                setBlock = i;
            }
        }
        if (counter == 1) {
            return setBlock;
        }
        else {
            return -1;
        }
    }

    /**
     * Find possible values in the cell.
     * @param cellRow
     * @param cellCol
     * @return
     */
    private int findOnlyInCell(int cellRow, int cellCol) {
        boolean isUpdate = false;
        int solvedCounter = 0;
        int row;
        int col;
        int i;
        int value;
        int setValue;
        int setBlock;
        boolean[][] tmp = new boolean[9][9];
        boolean[] doneBlocks = new boolean[9];
        // Find the value for a certain block...
        for (i = 0; i < 9; i++) {
            row = cellRow * 3 + i / 3;
            col = cellCol * 3 + i % 3;
            if (this.field[row][col] == 0) {
                for (value = 1; value <= 9; value++) {
                    tmp[i][value - 1] = validateRow[row][value - 1] || validateCol[col][value - 1] || validateCell[cellRow][cellCol][value - 1];
                }
                setValue = findPossibleValues(tmp[i]);
                if (setValue != -1) {
                    this.field[row][col] = setValue;
                    updateAnalyzed(setValue, row, col);
                    isUpdate = true;
                }
            }
            else {
                solvedCounter++;
                doneBlocks[i] = true;
            }
        }
        // Find the block for a certain value...
        if (solvedCounter < 9) {
            for (value = 1; value <= 9; value++) {
                setBlock = findBlocks(tmp, doneBlocks, value);
                if (setBlock != -1) {
                    row = cellRow * 3 + setBlock / 3;
                    col = cellCol * 3 + setBlock % 3;
                    this.field[row][col] = value;
                    updateAnalyzed(value, row, col);
                    isUpdate = true;
                }
            }
        }
        if (solvedCounter == 9) {
            return 0;
        }
        else {
            if (isUpdate) {
                return 1;
            }
            else {
                return -1;
            }
        }
    }

    /**
     * Find possible values in Row.
     * @param row
     * @return
     */

    private int findOnlyInRow(int row) {
        boolean isUpdate = false;
        int solvedCounter = 0;
        int col;
        int value;
        int setValue;
        int setBlock;
        boolean[][] tmp = new boolean[9][9];
        boolean[] doneBlocks = new boolean[9];
        // Find the value for a certain block...
        for (col = 0; col < 9; col++) {
            if (this.field[row][col] == 0) {
                for (value = 1; value <= 9; value++) {
                    tmp[col][value - 1] = validateRow[row][value - 1] || validateCol[col][value - 1] || validateCell[row / 3][col / 3][value - 1];
                }
                setValue = findPossibleValues(tmp[col]);
                if (setValue != -1) {
                    this.field[row][col] = setValue;
                    updateAnalyzed(setValue, row, col);
                    isUpdate = true;
                }
            }
            else {
                solvedCounter++;
                doneBlocks[col] = true;
            }
        }
        // Find the block for a certain value...
        if (solvedCounter < 9) {
            for (value = 1; value <= 9; value++) {
                setBlock = findBlocks(tmp, doneBlocks, value);
                if (setBlock != -1) {
                    this.field[row][setBlock] = value;
                    updateAnalyzed(value, row, setBlock);
                    isUpdate = true;
                }
            }
        }
        if (solvedCounter == 9) {
            return 0;
        }
        else {
            if (isUpdate) {
                return 1;
            }
            else {
                return -1;
            }
        }
    }

    /**
     * Find possible values in collumn.
     * @param col
     * @return
     */

    private int findOnlyInCol(int col) {
        boolean isUpdate = false;
        int solvedCounter = 0;
        int row;
        int value;
        int setValue;
        int setBlock;
        boolean[][] tmp = new boolean[9][9];
        boolean[] doneBlocks = new boolean[9];
        // Find the value for a certain block...
        for (row = 0; row < 9; row++) {
            if (this.field[row][col] == 0) {
                for (value = 1; value <= 9; value++) {
                    tmp[row][value - 1] = validateRow[row][value - 1] || validateCol[col][value - 1] || validateCell[row / 3][col / 3][value - 1];
                }
                setValue = findPossibleValues(tmp[row]);
                if (setValue != -1) {
                    this.field[row][col] = setValue;
                    updateAnalyzed(setValue, row, col);
                    isUpdate = true;
                }
            }
            else {
                solvedCounter++;
                doneBlocks[row] = true;
            }
        }
        // Find the block for a certain value...
        if (solvedCounter < 9) {
            for (value = 1; value <= 9; value++) {
                setBlock = findBlocks(tmp, doneBlocks, value);
                if (setBlock != -1) {
                    this.field[setBlock][col] = value;
                    updateAnalyzed(value, setBlock, col);
                    isUpdate = true;
                }
            }
        }
        if (solvedCounter == 9) {
            return 0;
        }
        else {
            if (isUpdate) {
                return 1;
            }
            else {
                return -1;
            }
        }
    }

    /**
     * Main solving function. It will be called if the user decides to give up.
     */

    public void solve() {
        analyzeRow();
        analyzeCol();
        analyzeCell();
        int result;
        int solvedCount;
        int unProcCell;
        int unProcRow;
        int unProcCol;
        boolean finish = false;
        while (!finish) {
            solvedCount = 0;
            unProcCell = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (!this.solvedCell[i][j]) {
                        result = findOnlyInCell(i, j);
                        if (result == 0) {
                            this.solvedCell[i][j] = true;
                            solvedCount++;
                        }
                        else {
                            if (result == -1) {
                                unProcCell++;
                            }
                        }
                    }
                    else {
                        solvedCount++;
                        unProcCell++;
                    }
                }
            }
            if (solvedCount == 9) {
                finish = true;
            }

            solvedCount = 0;
            unProcRow = 0;
            for (int i = 0; i < 9; i++) {
                if (!this.solvedRow[i]) {
                    result = findOnlyInRow(i);
                    if (result == 0) {
                        this.solvedRow[i] = true;
                        solvedCount++;
                    }
                    else {
                        if (result == -1) {
                            unProcRow++;
                        }
                    }
                }
                else {
                    solvedCount++;
                    unProcRow++;
                }
            }
            if (solvedCount == 9) {
                finish = true;
            }

            solvedCount = 0;
            unProcCol = 0;
            for (int i = 0; i < 9; i++) {
                if (!this.solvedCol[i]) {
                    result = findOnlyInCol(i);
                    if (result == 0) {
                        this.solvedCol[i] = true;
                        solvedCount++;
                    }
                    else {
                        if (result == -1) {
                            unProcCol++;
                        }
                    }
                }
                else {
                    solvedCount++;
                    unProcCol++;
                }
            }
            if (solvedCount == 9) {
                finish = true;
            }
            if (unProcCol == 9 && unProcCell == 9 && unProcRow == 9) {
                finish = true;
            }
        }

    }

}
