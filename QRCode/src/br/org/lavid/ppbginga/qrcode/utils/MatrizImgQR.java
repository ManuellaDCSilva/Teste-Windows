package br.org.lavid.ppbginga.qrcode.utils;

import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import javax.imageio.ImageIO;

public class MatrizImgQR {
    //private int versao=0; //define a versao do qr code
    private static int largura=0, altura=0;

    public static void GerarImagem(BinaryVector vector, int versao, int eccLevel) throws IOException {
         largura = 21 + ((versao-1)*4);
         altura=largura;
         JFrame frm = new JFrame("Teste Imagem");
         JPanel pan = new JPanel();
         JLabel lbl = new JLabel( criarImagem(trabalhaMatriz(vector, versao, eccLevel)) );
         pan.add( lbl );
         frm.getContentPane().add( pan );
         frm.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
         frm.pack();
         frm.show();
     }

    private static int UseMaskPatt(int x, int y, int i){
         int a=0;
         switch(i){
             case 0:
                 if((y+x)%2==0)
                     a=1;
                 break;
             case 1:
                 if(y%2==0)
                     a=1;
                 break;
             case 2:
                 if(x%3==0)
                     a=1;
                 break;
             case 3:
                 if((y+x)%3==0)
                     a=1;
                 break;
             case 4:
                 if(((y/2)+(x/3))%2==0)
                     a=1;
                 break;
             case 5:
                 if(((y*x)%2)+((y*x%3))==0)
                     a=1;
                 break;
             case 6:
                 if((((y*x)%2)+((y*x%3)))%2==0)
                     a=1;
                 break;
             case 7:
                 if((((y+x)%2)+((y*x%3)))%2==0)
                     a=1;
                 break;
         }
         return a;
     }
    
    private static int[] getListaAdjustmentPattern(int versao){
        int[][] tabela = {{0}, {6,18}, {6,22}, {6,26}, {6,30}, {6,34}, {6,22,38}, {6,24,42}, {6,26,46}, {6,28,50}, {6,30,54},
            {6,32,58}, {6,34,62}, {6,26,46,66}, {6,26,48,70}, {6,26,50,74}, {6,30,54,78}, {6,30,56,82}, {6,30,58,86}, {6,34,62,90},
            {6,28,50,72,94}, {6,26,50,74,98}, {6,30,54,78,102}, {6,28,54,80,106}, {6,32,58,84,110}, {6,30,58,86,114}, {6,34,62,90,118},
            {6,26,50,74,98,122}, {6,30,54,78,102,126}, {6,26,52,78,104,130}, {6,30,56,82,108,134}, {6,34,60,86,112,138}, {6,30,58,86,114,142},
            {6,34,62,90,118,146}, {6,30,54,78,102,126,150}, {6,24,50,76,102,128,154}, {6,28,54,80,106,132,158}, {6,32,58,84,110,136,162},
            {6,26,54,82,110,138,166}, {6,30,58,86,114,142,170}};
        return tabela[versao-1];
    }

    private static int[] getTypeInformationMask(int eccLevel, int nivelMask){
        int[][][] tabela = {{{1,1,1,0,1,1,1,1,1,0,0,0,1,0,0}, {1,1,1,0,0,1,0,1,1,1,1,0,0,1,1}, {1,1,1,1,1,0,1,1,0,1,0,1,0,1,0},
        {1,1,1,1,0,0,0,1,0,0,1,1,1,0,1}, {1,1,0,0,1,1,0,0,0,1,0,1,1,1,1}, {1,1,0,0,0,1,1,0,0,0,1,1,0,0,0}, {1,1,0,1,1,0,0,0,1,0,0,0,0,0,1},
        {1,1,0,1,0,0,1,0,1,1,1,0,1,1,0}}, /*fim do eccLevel L*/
        {{1,0,1,0,1,0,0,0,0,0,1,0,0,1,0}, {1,0,1,0,0,0,1,0,0,1,0,0,1,0,1}, {1,0,1,1,1,1,0,0,1,1,1,1,1,0,0},
        {1,0,1,1,0,1,1,0,1,0,0,1,0,1,1}, {1,0,0,0,1,0,1,1,1,1,1,1,0,0,1}, {1,0,0,0,0,0,0,1,1,0,0,1,1,1,0}, {1,0,0,1,1,1,1,1,0,0,1,0,1,1,1},
        {1,0,0,1,0,1,0,1,0,1,0,0,0,0,0}}, /*fim do eccLevel M*/
        {{0,1,1,0,1,0,1,0,1,0,1,1,1,1,1}, {0,1,1,0,0,0,0,0,1,1,0,1,0,0,0}, {0,1,1,1,1,1,1,0,0,1,1,0,0,0,1},
        {0,1,1,1,0,1,0,0,0,0,0,0,1,1,0}, {0,1,0,0,1,0,0,1,0,1,1,0,1,0,0}, {0,1,0,0,0,0,1,1,0,0,0,0,0,1,1}, {0,1,0,1,1,1,0,1,1,0,1,1,0,1,0},
        {0,1,0,1,0,1,1,1,1,1,0,1,1,0,1}}, /*fim do eccLevel Q*/
        {{0,0,1,0,1,1,0,1,0,0,0,1,0,0,1}, {0,0,1,0,0,1,1,1,0,1,1,1,1,1,0}, {0,0,1,1,1,0,0,1,1,1,0,0,1,1,1},
        {0,0,1,1,0,0,1,1,1,0,1,0,0,0,0}, {0,0,0,0,1,1,1,0,1,1,0,0,0,1,0}, {0,0,0,0,0,1,0,0,1,0,1,0,1,0,1}, {0,0,0,1,1,0,1,0,0,0,0,1,1,0,0},
        {0,0,0,1,0,0,0,0,0,1,1,1,0,1,1}} /*fim do eccLevel HQ*/};
        return tabela[eccLevel-1][nivelMask];
    }

    private static int[] getVersionInformation(int versao){
        int[][] tabela = {{0,0,1,0,1,0,0,1,0,0,1,1,1,1,1,0,0,0}, {0,0,0,1,1,1,1,0,1,1,0,1,0,0,0,1,0,0}, {1,0,0,1,1,0,0,1,0,1,0,1,1,0,0,1,0,0},
            {0,1,1,0,0,1,0,1,1,0,0,1,0,1,0,1,0,0}, {0,1,1,0,1,1,1,1,1,1,0,1,1,1,0,1,0,0}, {0,0,1,0,0,0,1,1,0,1,1,1,0,0,1,1,0,0}, {1,1,1,0,0,0,1,0,0,0,0,1,1,0,1,1,0,0},
            {0,1,0,1,1,0,0,0,0,0,1,1,0,1,1,1,0,0}, {0,0,0,1,0,1,0,0,1,0,0,1,1,1,1,1,0,0}, {0,0,0,1,1,1,1,0,1,1,0,1,0,0,0,0,1,0}, {0,1,0,1,1,1,0,1,0,0,0,1,1,0,0,0,1,0},
            {1,1,1,0,1,0,0,0,0,1,0,1,0,1,0,0,1,0}, {0,0,1,0,0,1,1,0,0,1,0,1,1,1,0,0,1,0}, {0,1,1,0,0,1,0,1,1,0,0,1,0,0,1,0,1,0}, {0,1,1,0,0,0,0,0,1,0,1,1,1,0,1,0,1,0},
            {1,0,0,1,0,0,1,1,0,0,0,1,0,1,1,0,1,0}, {0,0,0,1,1,0,1,1,1,1,1,1,1,1,1,0,1,0}, {0,0,1,0,0,0,1,1,0,1,1,1,0,0,0,1,1,0}, {0,0,0,1,0,0,0,0,1,1,1,1,1,0,0,1,1,0},
            {1,1,0,1,0,1,0,1,1,1,1,1,0,1,0,1,1,0}, {0,0,0,0,0,1,1,1,0,0,0,1,1,1,0,1,1,0}, {0,1,0,1,1,0,0,0,0,0,1,1,0,0,1,1,1,0}, {0,0,1,1,1,1,1,1,0,0,1,1,1,0,1,1,1,0},
            {1,0,1,0,1,1,1,0,1,0,1,1,0,1,1,1,1,0}, {0,0,0,0,0,0,1,0,1,0,0,1,1,1,1,1,1,0}, {1,0,1,0,1,0,1,1,1,0,0,1,0,0,0,0,0,1}, {0,0,0,0,0,1,1,1,1,0,1,1,1,0,0,0,0,1},
            {0,1,0,1,1,1,0,1,0,0,0,1,0,1,0,0,0,1}, {0,1,1,1,1,1,0,0,1,1,1,1,1,1,0,0,0,1}, {1,1,0,1,0,0,0,0,1,1,0,1,0,0,1,0,0,1}, {0,0,1,1,1,0,1,0,0,0,0,1,1,0,1,0,0,1},
            {0,0,1,0,0,1,1,0,0,1,0,1,0,1,1,0,0,1}, {0,1,0,0,0,0,0,1,0,1,0,1,1,1,1,0,0,1}, {1,0,0,1,0,1,1,0,0,0,1,1,0,0,0,1,0,1}};
        return tabela[versao-7];
    }
    public static int[][] trabalhaMatriz(BinaryVector vector, int versao, int eccLevel){
         //método indireto de produção de pixels = matriz articulada temporária representativa da imagem
         //array de bits
         int[][] a = new int[largura][altura];

         //instanciação elemento a elemento dos arrays
         int m=0, n=0;
         for(m=0; m<largura; m++)
             for(n=0; n<altura; n++)
                 a[m][n]=3; //valor de iniciação = indica disponibilidade de uso

         //Desenha os Detectores de Posição da Máscara
         int aX=3, aY=3;

         //formando os quadrados de dentro pra fora
         //quadrado superior esquerdo
         a[aX][aY]=1;
         a[aX-1][aY-1]=1; a[aX][aY-1]=1;a[aX+1][aY-1]=1;a[aX+1][aY]=1;a[aX+1][aY+1]=1;a[aX][aY+1]=1;a[aX-1][aY+1]=1;a[aX-1][aY]=1;
         a[aX-2][aY-2]=0;a[aX-1][aY-2]=0;a[aX][aY-2]=0;a[aX+1][aY-2]=0;a[aX+2][aY-2]=0;a[aX+2][aY-1]=0;a[aX+2][aY]=0;a[aX+2][aY+1]=0;a[aX+2][aY+2]=0;a[aX+1][aY+2]=0;a[aX][aY+2]=0;a[aX-1][aY+2]=0;a[aX-2][aY+2]=0;a[aX-2][aY+1]=0;a[aX-2][aY]=0;a[aX-2][aY-1]=0;
         a[aX-3][aY-3]=1;a[aX-2][aY-3]=1;a[aX-1][aY-3]=1;a[aX][aY-3]=1;a[aX+1][aY-3]=1;a[aX+2][aY-3]=1;a[aX+3][aY-3]=1;a[aX+3][aY-2]=1;a[aX+3][aY-1]=1;a[aX+3][aY]=1;a[aX+3][aY+1]=1;a[aX+3][aY+2]=1;a[aX+3][aY+3]=1;a[aX+2][aY+3]=1;a[aX+1][aY+3]=1;a[aX][aY+3]=1;a[aX-1][aY+3]=1;a[aX-2][aY+3]=1;a[aX-3][aY+3]=1;a[aX-3][aY+2]=1;a[aX-3][aY+1]=1;a[aX-3][aY]=1;a[aX-3][aY-1]=1;a[aX-3][aY-2]=1;
         for(int g=aX-3; g<8; g++)
             a[g][aY+4]=0;
         for(int f=aY+4; f>=0; f--)
             a[aX+4][f]=0;

         //quadrado inferior esquerdo
         aX=3; aY=altura-4;
         a[aX][aY]=1;
         a[aX-1][aY-1]=1; a[aX][aY-1]=1;a[aX+1][aY-1]=1;a[aX+1][aY]=1;a[aX+1][aY+1]=1;a[aX][aY+1]=1;a[aX-1][aY+1]=1;a[aX-1][aY]=1;
         a[aX-2][aY-2]=0;a[aX-1][aY-2]=0;a[aX][aY-2]=0;a[aX+1][aY-2]=0;a[aX+2][aY-2]=0;a[aX+2][aY-1]=0;a[aX+2][aY]=0;a[aX+2][aY+1]=0;a[aX+2][aY+2]=0;a[aX+1][aY+2]=0;a[aX][aY+2]=0;a[aX-1][aY+2]=0;a[aX-2][aY+2]=0;a[aX-2][aY+1]=0;a[aX-2][aY]=0;a[aX-2][aY-1]=0;
         a[aX-3][aY-3]=1;a[aX-2][aY-3]=1;a[aX-1][aY-3]=1;a[aX][aY-3]=1;a[aX+1][aY-3]=1;a[aX+2][aY-3]=1;a[aX+3][aY-3]=1;a[aX+3][aY-2]=1;a[aX+3][aY-1]=1;a[aX+3][aY]=1;a[aX+3][aY+1]=1;a[aX+3][aY+2]=1;a[aX+3][aY+3]=1;a[aX+2][aY+3]=1;a[aX+1][aY+3]=1;a[aX][aY+3]=1;a[aX-1][aY+3]=1;a[aX-2][aY+3]=1;a[aX-3][aY+3]=1;a[aX-3][aY+2]=1;a[aX-3][aY+1]=1;a[aX-3][aY]=1;a[aX-3][aY-1]=1;a[aX-3][aY-2]=1;
         for(int g=aX-3; g<8; g++)
             a[g][aY-4]=0;
         for(int f=aY-4; f<altura; f++)
             a[aX+4][f]=0;

         //quadrado superior direito
         aX=largura-4; aY=3;
         a[aX][aY]=1;
         a[aX-1][aY-1]=1; a[aX][aY-1]=1;a[aX+1][aY-1]=1;a[aX+1][aY]=1;a[aX+1][aY+1]=1;a[aX][aY+1]=1;a[aX-1][aY+1]=1;a[aX-1][aY]=1;
         a[aX-2][aY-2]=0;a[aX-1][aY-2]=0;a[aX][aY-2]=0;a[aX+1][aY-2]=0;a[aX+2][aY-2]=0;a[aX+2][aY-1]=0;a[aX+2][aY]=0;a[aX+2][aY+1]=0;a[aX+2][aY+2]=0;a[aX+1][aY+2]=0;a[aX][aY+2]=0;a[aX-1][aY+2]=0;a[aX-2][aY+2]=0;a[aX-2][aY+1]=0;a[aX-2][aY]=0;a[aX-2][aY-1]=0;
         a[aX-3][aY-3]=1;a[aX-2][aY-3]=1;a[aX-1][aY-3]=1;a[aX][aY-3]=1;a[aX+1][aY-3]=1;a[aX+2][aY-3]=1;a[aX+3][aY-3]=1;a[aX+3][aY-2]=1;a[aX+3][aY-1]=1;a[aX+3][aY]=1;a[aX+3][aY+1]=1;a[aX+3][aY+2]=1;a[aX+3][aY+3]=1;a[aX+2][aY+3]=1;a[aX+1][aY+3]=1;a[aX][aY+3]=1;a[aX-1][aY+3]=1;a[aX-2][aY+3]=1;a[aX-3][aY+3]=1;a[aX-3][aY+2]=1;a[aX-3][aY+1]=1;a[aX-3][aY]=1;a[aX-3][aY-1]=1;a[aX-3][aY-2]=1;
         for(int g=aX-3; g<largura; g++)
             a[g][aY+4]=0;
         for(int f=aY-3; f<8; f++)
             a[aX-4][f]=0;

         //Adjustment Pattern = ajuste padronizado
         int auxilia0[]=getListaAdjustmentPattern(versao);
         if(auxilia0[0]!=0){
            for(int hi=0; hi<auxilia0.length; hi++){
                aX=auxilia0[hi];
                for(int hj=0; hj<auxilia0.length; hj++){
                    aY=auxilia0[hj];
                    if(a[aX][aY]==3){
                        a[aX][aY]=1;
                        a[aX-1][aY-1]=0;a[aX][aY-1]=0;a[aX+1][aY-1]=0;a[aX+1][aY]=0;a[aX+1][aY+1]=0;a[aX][aY+1]=0;a[aX-1][aY+1]=0;a[aX-1][aY]=0;
                        a[aX-2][aY-2]=1;a[aX-1][aY-2]=1;a[aX][aY-2]=1;a[aX+1][aY-2]=1;a[aX+2][aY-2]=1;a[aX+2][aY-1]=1;a[aX+2][aY]=1;a[aX+2][aY+1]=1;a[aX+2][aY+2]=1;a[aX+1][aY+2]=1;a[aX][aY+2]=1;a[aX-1][aY+2]=1;a[aX-2][aY+2]=1;a[aX-2][aY+1]=1;a[aX-2][aY]=1;a[aX-2][aY-1]=1;
                    }
                }
            }
          }
         
         //Caminho dos Timing Patterns
         // *Horizontal
         int x=0, y=0;
         for(x=3+5, y=6; x<largura-8; x++){
             if(x%2==0){
                 a[x][y]=1;//infoTimingH[x-8];}
             } else {
                 a[x][y]=0;}
         }
         // *Vertical
         for(x=3+3, y=3+5; y<largura-8; y++){
             if(y%2==0){
                 a[x][y]=1;//infoTimingV[x-8];}
             } else {
                 a[x][y]=0;}
         }

         //Desenha o Black pixel
         a[8][altura-8]=1;


            int z=0;
         //Type Version Information = Tipo de Informação
            int[] typeInformationMask = getTypeInformationMask(eccLevel, 0);

            // *bit 0 à 7
            x=0; y=0;
            for(x=largura-1, y=8, z=14; x>largura-9; x--, z--)
                a[x][y]=typeInformationMask[z];
            for(x=8, y=0, z=14; y<9; y++)
                if(a[x][y]==3){
                    a[x][y]=typeInformationMask[z];
                    z--;
                }
            // *bit 8 à 14
            for(x=7, y=8, z=6; x>0; x--)
                if(a[x][y]==3){
                    a[x][y]=typeInformationMask[z];
                    z--;
                }
            for(x=8, y=altura-7, z=6; y<altura; y++, z--)
                a[x][y]=typeInformationMask[z];

         //Version Information
         if(versao>=7){
            int[] versionInformation = getVersionInformation(versao);
            // *Vertical
            x=0; y=0; z=0;
            for(y=0, x=largura-11, z=0; y<7 && z<18; y++)
                for(x=largura-11; x<largura-8; x++, z++)
                        a[x][y]=versionInformation[z];

            // *Horizontal
            x=0; y=0; z=0;
            for(x=0, y=altura-11, z=0; x<7 && z<18; x++)
                for(y=altura-11; y<largura-8; y++, z++)
                        a[x][y]=versionInformation[z];
         }

         //Inserção do Vetor Data de Binário
         int loop=largura-1;
         int aI=0;
         x=largura-1; y=altura-1;
         int teste=0;
         int itera=0;
         do{
                for(y=largura-1, x=loop; y>=0 && (x==loop || x==loop-1) && loop>=0; y--){
                    x=loop;
                    if(UseMaskPatt(x,y,0)==1 && a[x][y]==3) {
                        if(vector.getBitWise(itera)==1)
                            a[x][y]=0;
                        else
                            a[x][y]=1;
                        itera++;
                        aI=itera;
                    } else if(UseMaskPatt(x,y,0)==0 && a[x][y]==3){
                        a[x][y]=vector.getBitWise(itera);
                        itera++;
                        aI=itera;
                    }
                    if(loop>0){
                    x=loop-1;
                    if(UseMaskPatt(x,y,0)==1 && a[x][y]==3) {
                        if(vector.getBitWise(itera)==1)
                            a[x][y]=0;
                        else
                            a[x][y]=1;
                        itera++;
                        aI=itera;
                    } else if(UseMaskPatt(x,y,0)==0 && a[x][y]==3){
                        a[x][y]=vector.getBitWise(itera);
                        itera++;
                        aI=itera;
                    }
                    }
                }
                if(aI!=0)
                    loop=loop-2;
                 for(y=0, x=loop; y<=largura-1 && (x==loop || x==loop-1) && loop>=0; y++){
                    x=loop;
                    if(UseMaskPatt(x,y,0)==1 && a[x][y]==3) {
                        if(vector.getBitWise(itera)==1)
                            a[x][y]=0;
                        else
                            a[x][y]=1;
                        itera++;
                        aI=itera;
                    } else if(UseMaskPatt(x,y,0)==0 && a[x][y]==3){
                        a[x][y]=vector.getBitWise(itera);
                        itera++;
                        aI=itera;
                    }
                    if(loop>0){
                    x=loop-1;
                    if(UseMaskPatt(x,y,0)==1 && a[x][y]==3) {
                        if(vector.getBitWise(itera)==1)
                            a[x][y]=0;
                        else
                            a[x][y]=1;
                        itera++;
                        aI=itera;
                    } else if(UseMaskPatt(x,y,0)==0 && a[x][y]==3){
                        a[x][y]=vector.getBitWise(itera);
                        itera++;
                        aI=itera;
                    }
                     }
                }
                if(aI!=0)
                    loop=loop-2;
                aI=0;
         }while(itera<vector.getNumberOfBits()-1);

         System.err.println("[1] Matriz de inteiros como representação do QR Code gerada com sucesso!");
         System.out.println("Último valor do itera: "+itera);

         return a;
     }

    private static ImageIcon criarImagem(int matrizAux[][]) throws IOException {
         BufferedImage buffer = new BufferedImage( largura, altura, BufferedImage.TYPE_INT_RGB );
         Graphics g = buffer.createGraphics();
         g.setColor( Color.WHITE );
         g.fillRect( 0, 0, largura, altura );
         g.setColor( Color.BLACK );

         int[][] a = new int[largura][altura];
         a = matrizAux;

         int x=0, y=0;

         for(x=0; x<largura; x++){
             for(y=0; y<altura; y++){
                 if(a[x][y]==1/* && b[x][y]==1*/)
                     g.drawLine(x, y, x, y);
             }
         }

         //Redimensiona o QR Code, pois estava trabalhando cada pixel como 1 bit
         BufferedImage aux = new BufferedImage(4*largura, 4*altura, buffer.getType());//cria um buffer auxiliar com o tamanho desejado
         Graphics2D k = aux.createGraphics();//pega a classe graphics do aux para edicao
         AffineTransform at = AffineTransform.getScaleInstance((double) 4*largura / buffer.getWidth(), (double) 4*altura / buffer.getHeight());//cria a transformacao
         k.drawRenderedImage(buffer, at);

         ImageIO.write(aux, "png", new File("qr.png") );
         System.err.println("[2] Imagem QR Code criada com sucesso a partir da matriz de inteiros!");

         return new ImageIcon( aux );
     }
 }