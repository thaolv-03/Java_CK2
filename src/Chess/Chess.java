package Chess;

import javax.swing.*;

public class Chess{

    public static void main(String[] args) {
        JFrame frame = new JFrame("Chess");
        frame.setVisible(true);
        ChessPanel chessPanel = new ChessPanel();
        frame.add(chessPanel);
        frame.setLocation(1000, 80);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
