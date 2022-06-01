package Chess;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.util.HashMap;
import java.util.Map;

public class ChessPanel extends JPanel {
    int border = 50;
    final int Screen_Width = 500 + 2*border;
    final int Screen_Height = 500 + 2*border;
    final int cellSide = 500/8;

    final int chessW = cellSide;
    final int chessH = cellSide*280/170;
    Map<Integer, BufferedImage> map = new HashMap<Integer, BufferedImage>();
    // r n b k q b n r
    // 2 3 4 6 5 4 3 2
    int[][] chessBoard = {
        {2,3,4,6,5,4,3,2},
        {1,1,1,1,1,1,1,1},
        {0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0},
        {11,11,11,11,11,11,11,11},
        {12,13,14,16,15,14,13,12},
    };

    public ChessPanel() {
        this.setPreferredSize(new Dimension(Screen_Width, Screen_Height));
        this.setBackground(Color.lightGray);

        map.put(1,getImageByPath("img-chess/pawn-black.png"));
        map.put(2,getImageByPath("img-chess/rook-black.png"));
        map.put(3,getImageByPath("img-chess/knight-black.png"));
        map.put(4,getImageByPath("img-chess/bishop-black.png"));
        map.put(5,getImageByPath("img-chess/queen-black.png"));
        map.put(6,getImageByPath("img-chess/king-black.png"));

        map.put(11,getImageByPath("img-chess/pawn-white.png"));
        map.put(12,getImageByPath("img-chess/rook-white.png"));
        map.put(13,getImageByPath("img-chess/knight-white.png"));
        map.put(14,getImageByPath("img-chess/bishop-white.png"));
        map.put(15,getImageByPath("img-chess/queen-white.png"));
        map.put(16,getImageByPath("img-chess/king-white.png"));

    }

    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);

        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.WHITE);
        g2.fillRect(border,border,cellSide*8,cellSide*8);

        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if ((i % 2 == 0 && j % 2 != 0) || (i % 2 != 0 && j % 2 == 0)){
                    g2.setColor(Color.darkGray);
                    g2.fillRect(i*cellSide+border, j*cellSide+border, cellSide, cellSide);
                }
            }
        }
        g2.setFont(new Font("Segoe UI", Font.BOLD,18));
        for (int i = 0; i < 8; i++){
            g2.drawString(((char)(i+65))+"",i*cellSide+cellSide/2+border - 5,border/2+5);
            g2.drawString(((char)(i+65))+"",i*cellSide+cellSide/2+border - 5,Screen_Height- border/2+5);
            g2.drawString(i+1+"",border/2 - 7,i*cellSide+cellSide/2+border + 5);
            g2.drawString(i+1+"", Screen_Width - border/2 - 7,i*cellSide+cellSide/2+border + 5);
        }
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                g2.drawImage(map.get(chessBoard[j][i]), border + i*cellSide,border + j*cellSide-(chessH-chessW),chessW,chessH,null);
            }
        }
    }

    public BufferedImage getImageByPath(String path){
        try {
            return ImageIO.read(getClass().getResourceAsStream(path));
        } catch (Exception e) {
            System.out.println("File error!");
        }
        return null;
    }
}
