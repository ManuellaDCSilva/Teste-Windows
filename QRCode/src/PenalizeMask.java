
public class PenalizeMask {
    private static int matriz[][];
    private static int limite;
    private static int penalty;
    private static String aux;

    private static final int PT1 = 3;
    private static final int PT2 = 40;

    public static int TestMask(int mat[][], int lim){
        matriz = mat;
        limite = lim;
        penalty = 0;
        aux = "";
        int k=1;
        while(k<5){
            PenaltyRule(k);
            k++;
        }

        return penalty;
    }

    public static void PenaltyRule(int aj){
        switch(aj){
            case 1:
                int pen1 = 0;
                int last = 3;
                int aj1=5;
                //horizontal
                for(int x=0, y=0; x<limite && y<limite; x++){
                    aj1=matriz[x][y];
//                                    System.out.println("{["+aj1+"]}; x: "+x+"; y: "+y);
                    if(last==3){
                            last=aj1;
                            pen1=1;
                    }else {
                            if(aj1==last){
                                last=aj1;
                                pen1++;
                            }else {
                                if(pen1>=5){
                                    penalty+=PT1+(pen1-5);
//                                    System.out.println(penalty+"; x: "+x+"; y: "+y);
                                }
                                pen1=1;
                                last=aj1;
                            }
                        }
                    if((x==(limite-1)) && (y<limite)){
                        y++;
                        x=-1;
                        if(pen1>=5){
                                    penalty+=PT1+(pen1-5);
//                                    System.out.println(penalty+"; x: "+x+"; y: "+y);
                                }
                        pen1=0;
                        last=3;
                    }
                }

                //vertical
                pen1 = 0;
                last = 3;
                for(int x=0, y=0; x<limite && y<limite; y++){
                    aj1=matriz[x][y];
//                                    System.out.println("{["+aj1+"]}; x: "+x+"; y: "+y);
                    if(last==3){
                            last=aj1;
                            pen1=1;
                    }else {
                            if(aj1==last){
                                last=aj1;
                                pen1++;
                            }else {
                                if(pen1>=5){
                                    penalty+=PT1+(pen1-5);
//                                    System.out.println(penalty+"; x: "+x+"; y: "+y);
                                }
                                pen1=1;
                                last=aj1;
                            }
                        }
                    if((y==(limite-1)) && (x<limite)){
                        x++;
                        y=-1;
                        if(pen1>=5){
                                    penalty+=PT1+(pen1-5);
//                                    System.out.println(penalty+"; x: "+x+"; y: "+y);
                                }
                        pen1=0;
                        last=3;
                    }
                }
                break;
            case 2:
                int pen2 = 0;
                for(int x=0, y=0; x<(limite-1) && y<(limite-1); x++){
                    if((matriz[x][y]==matriz[x+1][y])&&(matriz[x+1][y]==matriz[x][y+1])&&(matriz[x][y+1]==matriz[x+1][y+1])){
                            pen2++;

                    }
                    if((x==(limite-2)) && (y<(limite-1))){
                        y++;
                        x=-1;
                    }
                }
                penalty+=(PT1*pen2);
                break;
            case 3:
                int pen3 = 0;
//                //horizontal
//                for(int x=0, y=0; x<limite && y<limite; x++){
//                    if((x+13)<limite && matriz[x][y]==0&&matriz[x+1][y]==0&&matriz[x+2][y]==0&&matriz[x+3][y]==0&&matriz[x+4][y]==1
//                            &&matriz[x+5][y]==0&&matriz[x+6][y]==1&&matriz[x+7][y]==1&&matriz[x+8][y]==1&&matriz[x+9][y]==0
//                            &&matriz[x+10][y]==1&&matriz[x+11][y]==0&&matriz[x+12][y]==0&&matriz[x+13][y]==0&&matriz[x+14][y]==0)
//                        pen3++;
//                    else if((((x-1)>=0 && matriz[x-1][y]!= 1)|x==0) && matriz[x][y] == 1 && matriz[x+1][y] == 0 && matriz[x+2][y] == 1 && matriz[x+3][y] == 1 && matriz[x+4][y] == 1
//                            && matriz[x+5][y] == 0 && matriz[x+6][y] == 1 && matriz[x+7][y] == 0 && matriz[x+8][y] == 0 && matriz[x+9][y]==0
//                            &&matriz[x+14][y]==0)
//                        pen3++;
//                    else if((x+11)<limite && matriz[x][y] == 0 && matriz[x + 1][y] == 0 && matriz[x + 2][y] == 0 && matriz[x + 3][y] == 0 && matriz[x + 4][y] == 1
//                            && matriz[x+5][y] == 0 && matriz[x+6][y] == 1 && matriz[x+7][y] == 1 && matriz[x+8][y] == 1 && matriz[x+9][y] == 0
//                            && matriz[x+10][y] == 1 && matriz[x+11][y] != 0)
//                        pen3++;
//                    if((x==(limite-11)) && (y<limite)){
//                        y++;
//                        x=0;
//                    }
//                }
//                //vertical
//                for(int x=0, y=0; x<limite && y<limite; y++){
//                    if((y+13)<limite && matriz[x][y]==0&&matriz[x][y+1]==0&&matriz[x][y+2]==0&&matriz[x][y+3]==0&&matriz[x][y+4]==1
//                            &&matriz[x][y+5]==0&&matriz[x][y+6]==1&&matriz[x][y+7]==1&&matriz[x][y+8]==1&&matriz[x][y+9]==0
//                            &&matriz[x][y+10]==1&&matriz[x][y+11]==0&&matriz[x][y+12]==0&&matriz[x][y+13]==0&&matriz[x][y]==0)
//                        pen3++;
//                    else if((((y-1)>=0 && matriz[x][y-1] != 1)|y==0) && matriz[x][y] == 1 && matriz[x][y+1] == 0 && matriz[x][y+2] == 1 && matriz[x][y+3] == 1 && matriz[x][y+4] == 1
//                            && matriz[x][y+5] == 0 && matriz[x][y+6] == 1 && matriz[x][y+7] == 0 && matriz[x][y+8] == 0 && matriz[x][y+9]==0
//                            &&matriz[x][y+10]==0)
//                        pen3++;
//                    else if((y+11)<limite && matriz[x][y] == 0 && matriz[x][y+1] == 0 && matriz[x][y+2] == 0 && matriz[x][y+3] == 0 && matriz[x][y+4] == 1
//                            && matriz[x][y+5] == 0 && matriz[x][y+6] == 1 && matriz[x][y+7] == 1 && matriz[x][y+8] == 1 && matriz[x][y+9] == 0
//                            && matriz[x][y+10] == 1 && matriz[x][y+11] != 0)
//                        pen3++;
//                    if((y==(limite-11)) && (x<limite)){
//                        x++;
//                        y=0;
//                    }
//                }
                //The part bellow was got by the ZXing Open Source Qr Code encoder and adapted //
                for (int x = 0; x < limite; x++) {
                    for (int y = 0; y < limite; y++) {
                        if (x + 6 < limite
                                && matriz[x][y] == 1
                                && matriz[x + 1][y] == 0
                                && matriz[x + 2][y] == 1
                                && matriz[x + 3][y] == 1
                                && matriz[x + 4][y] == 1
                                && matriz[x + 5][y] == 0
                                && matriz[x + 6][y] == 1
                                && ((x + 10 < limite
                                && matriz[x + 7][y] == 0
                                && matriz[x + 8][y] == 0
                                && matriz[x + 9][y] == 0
                                && matriz[x + 10][y] == 0)
                                || (x - 4 >= 0
                                && matriz[x - 1][y] == 0
                                && matriz[x - 2][y] == 0
                                && matriz[x - 3][y] == 0
                                && matriz[x - 4][y] == 0))) {
                            pen3++;
                        }
                        if (y + 6 < limite
                                && matriz[y][x] == 1
                                && matriz[x][y + 1] == 0
                                && matriz[x][y + 2] == 1
                                && matriz[x][y + 3] == 1
                                && matriz[x][y + 4] == 1
                                && matriz[x][y + 5] == 0
                                && matriz[x][y + 6] == 1
                                && ((y + 10 < limite
                                && matriz[x][y + 7] == 0
                                && matriz[x][y + 8] == 0
                                && matriz[x][y + 9] == 0
                                && matriz[x][y + 10] == 0)
                                || (y - 4 >= 0
                                && matriz[x][y - 1] == 0
                                && matriz[x][y - 2] == 0
                                && matriz[x][y - 3] == 0
                                && matriz[x][y - 4] == 0))) {
                            pen3++;
                        }
                    }
                }
                //End of ZXing code//
                penalty += (PT2 * pen3);
                break;
            case 4:
                int pen4 = 0;
                int nB=0, nW=0;
                for (int x = 0; x < limite; x++) {
                    for (int y = 0; y < limite; y++) {
                        if(matriz[x][y]==0)
                            nW++;
                        else
                            nB++;
                    }
                }
                pen4=(((int) Math.abs((((float) nB/(nB+nW))*100)-50))/5)*10;
                penalty+=pen4;
                break;
        }
    }
}