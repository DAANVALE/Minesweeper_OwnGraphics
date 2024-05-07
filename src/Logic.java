public class Logic {

    private int rows, cols;
    private float difficulty;

    int[][] returnable;

    public Logic(int[][] gridBlock, float difficulty){
        rows = gridBlock.length;
        cols = gridBlock[0].length;
        this.difficulty = difficulty;
        returnable = new int[rows][cols];
    }

    public int[][] generateValues(){
        setBombs();
        setNearBombs();
        return returnable;
    }

    public void setBombs(){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                if( Math.random() < difficulty){
                    returnable[i][j] = -1;
                }else{
                    returnable[i][j] = 0;
                }
            }
        }
    }

    public void setNearBombs(){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                if (returnable[i][j] != -1) {
                    continue; // Si no es una bomba, pasar a la siguiente iteración
                }

                // Iterar sobre las celdas vecinas (incluyendo diagonales)
                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        // Calcular las coordenadas de la celda vecina
                        int neighborX = i + x;
                        int neighborY = j + y;

                        // Verificar si la celda vecina está dentro de los límites del campo
                        if (neighborX >= 0 && neighborX < rows && neighborY >= 0 && neighborY < cols) {
                            // Verificar si la celda vecina es una bomba
                            if (returnable[neighborX][neighborY] != -1) {
                                returnable[neighborX][neighborY]++; // Si no es una bomba, incrementar el contador
                            }
                        }
                    }
                }
            }
        }
    }
}
