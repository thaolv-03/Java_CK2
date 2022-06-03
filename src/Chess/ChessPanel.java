package Chess;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.HashMap;
import java.util.Map;

public class ChessPanel extends JPanel implements ActionListener {
    Timer timer = new Timer(1000 / 60, this);

    int border = 50;

    final int Screen_Width = 700 + 2 * border;
    final int Screen_Height = 700 + 2 * border;
    final int cellSide = 700 / 8;
    int ovalMove = 45;
    int MoveSide = (cellSide-ovalMove)/2;
    int rectKill = 75;
    int KillSide = (cellSide-rectKill)/2;
    int xCurrent = -1;
    int yCurrent = -1;

    final int chessW = cellSide;
    final int chessH = cellSide * 360 / 250;
    Map<Integer, BufferedImage> map = new HashMap<Integer, BufferedImage>();
    // r n b k q b n r
    // 2 3 4 6 5 4 3 2
    int[][] chessBoard = {
            { 02, 00, 00, 05, 00, 04, 03, 02 },
            { 01, 01, 01, 01, 06, 01, 01, 00 },
            { 03, 00, 04, 00, 01, 11, 00, 00 },
            { 01, 16, 00, 13, 00, 01, 00, 00 },
            { 01, 01, 00, 00, 15, 00, 16, 00 },
            { 00, 00, 14, 00, 00, 00, 00, 00 },
            { 00, 13, 00, 11, 11, 11, 11, 00 },
            { 12, 00, 00, 00, 16, 14, 13, 12 },
    };
    // Get average runtime of successful runs in seconds
    public ChessPanel() {
        this.setPreferredSize(new Dimension(Screen_Width, Screen_Height));
//        this.setBackground(Color.lightGray);
//        this.setBackground(new Color(255, 204, 0));
        this.setBackground(new Color(0, 153, 255));
        this.addMouseListener(new CustomMouseListener());
        this.timer.start();

        // BLACK
        map.put(1, getImageByPath("img-chess/pawn-black.png"));
        map.put(2, getImageByPath("img-chess/rook-black.png"));
        map.put(3, getImageByPath("img-chess/knight-black.png"));
        map.put(4, getImageByPath("img-chess/bishop-black.png"));
        map.put(5, getImageByPath("img-chess/queen-black.png"));
        map.put(6, getImageByPath("img-chess/king-black.png"));
        // WHITE
        map.put(11, getImageByPath("img-chess/pawn-white.png"));
        map.put(12, getImageByPath("img-chess/rook-white.png"));
        map.put(13, getImageByPath("img-chess/knight-white.png"));
        map.put(14, getImageByPath("img-chess/bishop-white.png"));
        map.put(15, getImageByPath("img-chess/queen-white.png"));
        map.put(16, getImageByPath("img-chess/king-white.png"));
    }

    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);

        Graphics2D g2 = (Graphics2D) g;

        // set color cellWhite
        g2.setColor(Color.WHITE);
        g2.fillRect(border, border, cellSide * 8, cellSide * 8);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i % 2 == 0 && j % 2 != 0) || (i % 2 != 0 && j % 2 == 0)) {
                    // set color cellBlack
                    g2.setColor(Color.darkGray);
                    g2.fillRect(i * cellSide + border, j * cellSide + border, cellSide, cellSide);
                }
            }
        }
        // set color current Cell
//        g2.setColor(Color.cyan);
        g2.setColor(new Color(255, 204, 0));
        if (xCurrent >= 0 && yCurrent >= 0) {
            g2.fillRect(xCurrent * cellSide + border, yCurrent * cellSide + border, cellSide, cellSide);
            nextStep(xCurrent, yCurrent, g2);
        }
        // set letters (A -> H) and numbers (1 -> 8)
        g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
        g2.setColor(Color.black);
        for (int i = 0; i < 8; i++) {
            g2.drawString(((char) (i + 65)) + "", i * cellSide + cellSide / 2 + border - 5, border / 2 + 5);
            g2.drawString(((char) (i + 65)) + "", i * cellSide + cellSide / 2 + border - 5,
                    Screen_Height - border / 2 + 5);
            g2.drawString(i + 1 + "", border / 2 - 7, i * cellSide + cellSide / 2 + border + 5);
            g2.drawString(i + 1 + "", Screen_Width - border / 2 - 7, i * cellSide + cellSide / 2 + border + 5);
        }
        // Set chess pieces to the chessboard
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                g2.drawImage(map.get(chessBoard[j][i]), border + i * cellSide,
                        border + j * cellSide - (chessH - chessW), chessW, chessH, null);
            }
        }
    }

    //
    public void nextStep(int x, int y, Graphics2D g) {
        g.setColor(Color.green);
        switch (chessBoard[y][x]) {

            // ---------------- PAWN ----------------
            case 11:
                // NEXT_STEP
                if (chessBoard[y - 1][x] == 0) { // if nextStep don't have any chess
                    g.fillOval(x * cellSide + border + MoveSide, (y - 1) * cellSide + border + MoveSide, ovalMove, ovalMove);

                    if (y == 6 && chessBoard[y - 2][x] == 0) {
                        g.fillOval(x * cellSide + border + MoveSide, (y - 2) * cellSide + border + MoveSide, ovalMove, ovalMove);
                    }
                }
                // NEXT_KILL
                g.setColor(Color.red);
                if (x - 1 >= 0) {
                    if (chessBoard[y - 1][x - 1] != 0 && chessBoard[y - 1][x - 1] < 7) {
                        g.fillRect((x - 1) * cellSide + border + KillSide, (y - 1) * cellSide + border + KillSide, rectKill, rectKill);
                    }
                }
                if (x + 1 < 8) {
                    if (chessBoard[y - 1][x + 1] != 0 && chessBoard[y - 1][x + 1] < 7) {
                        g.fillRect((x + 1) * cellSide + border + KillSide, (y - 1) * cellSide + border + KillSide, rectKill, rectKill);
                    }
                }

                break;
            case 1:
                // NEXT_STEP
                if (chessBoard[y + 1][x] == 0) {
                    g.fillOval(x * cellSide + border + MoveSide, (y + 1) * cellSide + border + MoveSide, ovalMove, ovalMove);
                    if (y == 1 && chessBoard[y + 2][x] == 0) {
                        g.fillOval(x * cellSide + border + MoveSide, (y + 2) * cellSide + border + MoveSide, ovalMove, ovalMove);
                    }
                }
                // NEXT_KILL
                g.setColor(Color.red);
                if (x - 1 >= 0) {
                    if (chessBoard[y + 1][x - 1] != 0 && chessBoard[y + 1][x - 1] > 7) {
                        g.fillRect((x - 1) * cellSide + border + KillSide, (y + 1) * cellSide + border + KillSide, rectKill, rectKill);
                    }
                }
                if (x + 1 < 8) {
                    if (chessBoard[y + 1][x + 1] != 0 && chessBoard[y + 1][x + 1] > 7) {
                        g.fillRect((x + 1) * cellSide + border + KillSide, (y + 1) * cellSide + border + KillSide, rectKill, rectKill);
                    }
                }
                break;


            // ---------------- ROOK ----------------
            case 12:
                // Move to left
                for (int i = x - 1; i >= 0; i--) {
                    if (chessBoard[y][i] == 0) {
                        g.fillOval(i * cellSide + border + MoveSide, y * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[y][i] < 7) {
                        g.setColor(Color.red);
                        g.fillRect(i * cellSide + border + KillSide, y * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                // Move to right
                g.setColor(Color.green);
                for (int i = x + 1; i < 8; i++) {
                    if (chessBoard[y][i] == 0) {
                        g.fillOval(i * cellSide + border + MoveSide, y * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[y][i] < 7) {
                        g.setColor(Color.red);
                        g.fillRect(i * cellSide + border + KillSide, y * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                // Move to top
                g.setColor(Color.green);
                for (int i = y - 1; i >= 0; i--) {
                    if (chessBoard[i][x] == 0) {
                        g.fillOval(x * cellSide + border + MoveSide, i * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[i][x] < 7) {
                        g.setColor(Color.red);
                        g.fillRect(x * cellSide + border + KillSide, i * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                // Move to bottom
                g.setColor(Color.green);
                for (int i = y + 1; i < 8; i++) {
                    if (chessBoard[i][x] == 0) {
                        g.fillOval(x * cellSide + border + MoveSide, i * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[i][x] < 7) {
                        g.setColor(Color.red);
                        g.fillRect(x * cellSide + border + KillSide, i * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                break;
            case 2:
                // Move to left
                for (int i = x - 1; i >= 0; i--) {
                    if (chessBoard[y][i] == 0) {
                        g.fillOval(i * cellSide + border + MoveSide, y * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[y][i] > 7) {
                        g.setColor(Color.red);
                        g.fillRect(i * cellSide + border + KillSide, y * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                // Move to right
                g.setColor(Color.green);
                for (int i = x + 1; i < 8; i++) {
                    if (chessBoard[y][i] == 0) {
                        g.fillOval(i * cellSide + border + MoveSide, y * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[y][i] > 7) {
                        g.setColor(Color.red);
                        g.fillRect(i * cellSide + border + KillSide, y * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                // Move to top
                g.setColor(Color.green);
                for (int i = y - 1; i >= 0; i--) {
                    if (chessBoard[i][x] == 0) {
                        g.fillOval(x * cellSide + border + MoveSide, i * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[i][x] > 7) {
                        g.setColor(Color.red);
                        g.fillRect(x * cellSide + border + KillSide, i * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                // Move to bottom
                g.setColor(Color.green);
                for (int i = y + 1; i < 8; i++) {
                    if (chessBoard[i][x] == 0) {
                        g.fillOval(x * cellSide + border + MoveSide, i * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[i][x] > 7) {
                        g.setColor(Color.red);
                        g.fillRect(x * cellSide + border + KillSide, i * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                break;

            // ---------------- KNIGHT ----------------

            // ---------- KNIGHT-WHITE ----------
            case 13:
                // NEXT_STEP

                // ALONG
                // Move to top & bottom ( left )
                if ((x - 1) >= 0) {
                    if (y - 2 >= 0) {
                        if (chessBoard[y - 2][x - 1] == 0) {
                            g.fillOval((x - 1) * cellSide + border + MoveSide, (y - 2) * cellSide + border + MoveSide, ovalMove, ovalMove);
                        }
                    }
                    if (y + 2 < 8) {
                        if (chessBoard[y + 2][x - 1] == 0) {
                            g.fillOval((x - 1) * cellSide + border + MoveSide, (y + 2) * cellSide + border + MoveSide, ovalMove, ovalMove);
                        }
                    }
                }
                // Move to top & bottom ( right )
                if ((x + 1) < 8) {
                    if (y - 2 >= 0)
                        if (chessBoard[y - 2][x + 1] == 0) {
                            g.fillOval((x + 1) * cellSide + border + MoveSide, (y - 2) * cellSide + border + MoveSide, ovalMove, ovalMove);
                        }

                    if (y + 2 < 8)
                        if (chessBoard[y + 2][x + 1] == 0) {
                            g.fillOval((x + 1) * cellSide + border + MoveSide, (y + 2) * cellSide + border + MoveSide, ovalMove, ovalMove);
                        }
                }

                // ACROSS
                // Move to top & bottom ( left )
                if ((x - 2) >= 0) {
                    if ((y - 1) >= 0)
                        if (chessBoard[y - 1][x - 2] == 0) {
                            g.fillOval((x - 2) * cellSide + border + MoveSide, (y - 1) * cellSide + border + MoveSide, ovalMove, ovalMove);
                        }
                    if ((y + 1) < 8)
                        if (chessBoard[y + 1][x - 2] == 0) {
                            g.fillOval((x - 2) * cellSide + border + MoveSide, (y + 1) * cellSide + border + MoveSide, ovalMove, ovalMove);
                        }
                }
                // Move to top & bottom ( right )
                if ((x + 2) < 8) {
                    if ((y - 1) >= 0)
                        if (chessBoard[y - 1][x + 2] == 0) {
                            g.fillOval((x + 2) * cellSide + border + MoveSide, (y - 1) * cellSide + border + MoveSide, ovalMove, ovalMove);
                        }
                    if ((y + 1) < 8)
                        if (chessBoard[y + 1][x + 2] == 0) {
                            g.fillOval((x + 2) * cellSide + border + MoveSide, (y + 1) * cellSide + border + MoveSide, ovalMove, ovalMove);
                        }
                }

                // NEXT_KILL
                // ALONG
                g.setColor(Color.red);
                // Kill top & bottom ( left )
                if ((x - 1) >= 0) {
                    if (y - 2 >= 0)
                        if (chessBoard[y - 2][x - 1] != 0 && chessBoard[y - 2][x - 1] < 7) {
                            g.fillRect((x - 1) * cellSide + border + KillSide, (y - 2) * cellSide + border + KillSide, rectKill, rectKill);
                        }
                    if ((y + 2) < 8)
                        if (chessBoard[y + 2][x - 1] != 0 && chessBoard[y + 2][x - 1] < 7) {
                            g.fillRect((x - 1) * cellSide + border + KillSide, (y + 2) * cellSide + border + KillSide, rectKill, rectKill);
                        }
                }
                // Kill top & bottom ( right )
                if ((x + 1) < 8) {
                    if ((y - 2) >= 0)
                        if (chessBoard[y - 2][x + 1] != 0 && chessBoard[y - 2][x + 1] < 7) {
                            g.fillRect((x + 1) * cellSide + border + KillSide, (y - 2) * cellSide + border + KillSide, rectKill, rectKill);
                        }
                    if ((y + 2) < 8)
                        if (chessBoard[y + 2][x + 1] != 0 && chessBoard[y + 2][x + 1] < 7) {
                            g.fillRect((x + 1) * cellSide + border + KillSide, (y + 2) * cellSide + border + KillSide, rectKill, rectKill);
                        }
                }

                // ACROSS
                // Kill top & bottom ( left )
                if ((x - 2) >= 0) {
                    if ((y - 1) >= 0)
                        if (chessBoard[y - 1][x - 2] != 0 && chessBoard[y - 1][x - 2] < 7) {
                            g.fillRect((x - 2) * cellSide + border + KillSide, (y - 1) * cellSide + border + KillSide, rectKill, rectKill);
                        }
                    if ((y + 1) < 8)
                        if (chessBoard[y + 1][x - 2] != 0 && chessBoard[y + 1][x - 2] < 7) {
                            g.fillRect((x - 2) * cellSide + border + KillSide, (y + 1) * cellSide + border + KillSide, rectKill, rectKill);
                        }
                }
                // Kill top & bottom ( right )
                if ((x + 2) < 8) {
                    if((y - 1) >= 0)
                        if (chessBoard[y - 1][x + 2] != 0 && chessBoard[y - 1][x + 2] < 7) {
                            g.fillRect((x + 2) * cellSide + border + KillSide, (y - 1) * cellSide + border + KillSide, rectKill, rectKill);
                        }
                    if ((y + 1) < 8)
                        if (chessBoard[y + 1][x + 2] != 0 && chessBoard[y + 1][x + 2] < 7) {
                            g.fillRect((x + 2) * cellSide + border + KillSide, (y + 1) * cellSide + border + KillSide, rectKill, rectKill);
                        }
                }

                break;

            // ---------- KNIGHT-BLACK ----------
            case 3:
                // NEXT_STEP

                // ALONG
                // Move to top & bottom ( left )
                if ((x - 1) >= 0) {
                    if (y - 2 >= 0) {
                        if (chessBoard[y - 2][x - 1] == 0) {
                            g.fillOval((x - 1) * cellSide + border + MoveSide, (y - 2) * cellSide + border + MoveSide, ovalMove, ovalMove);
                        }
                    }
                    if (y + 2 < 8) {
                        if (chessBoard[y + 2][x - 1] == 0) {
                            g.fillOval((x - 1) * cellSide + border + MoveSide, (y + 2) * cellSide + border + MoveSide, ovalMove, ovalMove);
                        }
                    }
                }
                // Move to top & bottom ( right )
                if ((x + 1) < 8) {
                    if (y - 2 >= 0)
                        if (chessBoard[y - 2][x + 1] == 0) {
                            g.fillOval((x + 1) * cellSide + border + MoveSide, (y - 2) * cellSide + border + MoveSide, ovalMove, ovalMove);
                        }

                    if (y + 2 < 8)
                        if (chessBoard[y + 2][x + 1] == 0) {
                            g.fillOval((x + 1) * cellSide + border + MoveSide, (y + 2) * cellSide + border + MoveSide, ovalMove, ovalMove);
                        }
                }

                // ACROSS
                // Move to top & bottom ( left )
                if ((x - 2) >= 0) {
                    if ((y - 1) >= 0)
                        if (chessBoard[y - 1][x - 2] == 0) {
                            g.fillOval((x - 2) * cellSide + border + MoveSide, (y - 1) * cellSide + border + MoveSide, ovalMove, ovalMove);
                        }
                    if ((y + 1) < 8)
                        if (chessBoard[y + 1][x - 2] == 0) {
                            g.fillOval((x - 2) * cellSide + border + MoveSide, (y + 1) * cellSide + border + MoveSide, ovalMove, ovalMove);
                        }
                }
                // Move to top & bottom ( right )
                if ((x + 2) < 8) {
                    if ((y - 1) >= 0)
                        if (chessBoard[y - 1][x + 2] == 0) {
                            g.fillOval((x + 2) * cellSide + border + MoveSide, (y - 1) * cellSide + border + MoveSide, ovalMove, ovalMove);
                        }
                    if ((y + 1) < 8)
                        if (chessBoard[y + 1][x + 2] == 0) {
                            g.fillOval((x + 2) * cellSide + border + MoveSide, (y + 1) * cellSide + border + MoveSide, ovalMove, ovalMove);
                        }
                }

                // NEXT_KILL
                // ALONG
                g.setColor(Color.red);
                // Kill top & bottom ( left )
                if ((x - 1) >= 0) {
                    if (y - 2 >= 0)
                        if (chessBoard[y - 2][x - 1] != 0 && chessBoard[y - 2][x - 1] > 7) {
                            g.fillRect((x - 1) * cellSide + border + KillSide, (y - 2) * cellSide + border + KillSide, rectKill, rectKill);
                        }
                    if ((y + 2) < 8)
                        if (chessBoard[y + 2][x - 1] != 0 && chessBoard[y + 2][x - 1] > 7) {
                            g.fillRect((x - 1) * cellSide + border + KillSide, (y + 2) * cellSide + border + KillSide, rectKill, rectKill);
                        }
                }
                // Kill top & bottom ( right )
                if ((x + 1) < 8) {
                    if ((y - 2) >= 0)
                        if (chessBoard[y - 2][x + 1] != 0 && chessBoard[y - 2][x + 1] > 7) {
                            g.fillRect((x + 1) * cellSide + border + KillSide, (y - 2) * cellSide + border + KillSide, rectKill, rectKill);
                        }
                    if ((y + 2) < 8)
                        if (chessBoard[y + 2][x + 1] != 0 && chessBoard[y + 2][x + 1] > 7) {
                            g.fillRect((x + 1) * cellSide + border + KillSide, (y + 2) * cellSide + border + KillSide, rectKill, rectKill);
                        }
                }

                // ACROSS
                // Kill top & bottom ( left )
                if ((x - 2) >= 0) {
                    if ((y - 1) >= 0)
                        if (chessBoard[y - 1][x - 2] != 0 && chessBoard[y - 1][x - 2] > 7) {
                            g.fillRect((x - 2) * cellSide + border + KillSide, (y - 1) * cellSide + border + KillSide, rectKill, rectKill);
                        }
                    if ((y + 1) < 8)
                        if (chessBoard[y + 1][x - 2] != 0 && chessBoard[y + 1][x - 2] > 7) {
                            g.fillRect((x - 2) * cellSide + border + KillSide, (y + 1) * cellSide + border + KillSide, rectKill, rectKill);
                        }
                }
                // Kill top & bottom ( right )
                if ((x + 2) < 8) {
                    if((y - 1) >= 0)
                        if (chessBoard[y - 1][x + 2] != 0 && chessBoard[y - 1][x + 2] > 7) {
                            g.fillRect((x + 2) * cellSide + border + KillSide, (y - 1) * cellSide + border + KillSide, rectKill, rectKill);
                        }
                    if ((y + 1) < 8)
                        if (chessBoard[y + 1][x + 2] != 0 && chessBoard[y + 1][x + 2] > 7) {
                            g.fillRect((x + 2) * cellSide + border + KillSide, (y + 1) * cellSide + border + KillSide, rectKill, rectKill);
                        }
                }

                break;


            // ---------------- BISHOP ----------------
            case 14:
                // Move to top left
                g.setColor(Color.green);
                for (int i = 1; ((x - i) >= 0) && ((y - i) >= 0) && ((x - i) < 8) && ((y - i) < 8); i++) {
                    if (chessBoard[y - i][x - i] == 0) {
                        g.fillOval((x - i) * cellSide + border + MoveSide, (y - i) * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[y - i][x - i] < 7) {
                        g.setColor(Color.red);
                        g.fillRect((x - i) * cellSide + border + KillSide, (y - i) * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                // Move to top right
                g.setColor(Color.green);
                for (int i = 1; ((x + i) >= 0) && ((y - i) >= 0) && ((x + i) < 8) && ((y - i) < 8); i++) {
                    if (chessBoard[y - i][x + i] == 0) {
                        g.fillOval((x + i) * cellSide + border + MoveSide, (y - i) * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[y - i][x + i] < 7) {
                        g.setColor(Color.red);
                        g.fillRect((x + i) * cellSide + border + KillSide, (y - i) * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                // Move to bottom right
                g.setColor(Color.green);
                for (int i = 1; ((x + i) >= 0) && ((y + i) >= 0) && ((x + i) < 8) && ((y + i) < 8); i++) {
                    if (chessBoard[y + i][x + i] == 0) {
                        g.fillOval((x + i) * cellSide + border + MoveSide, (y + i) * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[y + i][x + i] < 7) {
                        g.setColor(Color.red);
                        g.fillRect((x + i) * cellSide + border + KillSide, (y + i) * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                // Move to bottom left
                g.setColor(Color.green);
                for (int i = 1; ((x - i) >= 0) && ((y + i) >= 0) && ((x - i) < 8) && ((y + i) < 8); i++) {
                    if (chessBoard[y + i][x - i] == 0) {
                        g.fillOval((x - i) * cellSide + border + MoveSide, (y + i) * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[y + i][x - i] < 7) {
                        g.setColor(Color.red);
                        g.fillRect((x - i) * cellSide + border + KillSide, (y + i) * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }
                break;

            case 4:
                // Move to top left
                g.setColor(Color.green);
                for (int i = 1; ((x - i) >= 0) && ((y - i) >= 0) && ((x - i) < 8) && ((y - i) < 8); i++) {
                    if (chessBoard[y - i][x - i] == 0) {
                        g.fillOval((x - i) * cellSide + border + MoveSide, (y - i) * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[y - i][x - i] > 7) {
                        g.setColor(Color.red);
                        g.fillRect((x - i) * cellSide + border + KillSide, (y - i) * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                // Move to top right
                g.setColor(Color.green);
                for (int i = 1; ((x + i) >= 0) && ((y - i) >= 0) && ((x + i) < 8) && ((y - i) < 8); i++) {
                    if (chessBoard[y - i][x + i] == 0) {
                        g.fillOval((x + i) * cellSide + border + MoveSide, (y - i) * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[y - i][x + i] > 7) {
                        g.setColor(Color.red);
                        g.fillRect((x + i) * cellSide + border + KillSide, (y - i) * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                // Move to bottom right
                g.setColor(Color.green);
                for (int i = 1; ((x + i) >= 0) && ((y + i) >= 0) && ((x + i) < 8) && ((y + i) < 8); i++) {
                    if (chessBoard[y + i][x + i] == 0) {
                        g.fillOval((x + i) * cellSide + border + MoveSide, (y + i) * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[y + i][x + i] > 7) {
                        g.setColor(Color.red);
                        g.fillRect((x + i) * cellSide + border + KillSide, (y + i) * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                // Move to bottom left
                g.setColor(Color.green);
                for (int i = 1; ((x - i) >= 0) && ((y + i) >= 0) && ((x - i) < 8) && ((y + i) < 8); i++) {
                    if (chessBoard[y + i][x - i] == 0) {
                        g.fillOval((x - i) * cellSide + border + MoveSide, (y + i) * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[y + i][x - i] > 7) {
                        g.setColor(Color.red);
                        g.fillRect((x - i) * cellSide + border + KillSide, (y + i) * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }
                break;

            // ---------------- QUEEN ----------------
            case 15:

                // Move to left
                for (int i = x - 1; i >= 0; i--) {
                    if (chessBoard[y][i] == 0) {
                        g.fillOval(i * cellSide + border + MoveSide, y * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[y][i] < 7) {
                        g.setColor(Color.red);
                        g.fillRect(i * cellSide + border + KillSide, y * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                // Move to right
                g.setColor(Color.green);
                for (int i = x + 1; i < 8; i++) {
                    if (chessBoard[y][i] == 0) {
                        g.fillOval(i * cellSide + border + MoveSide, y * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[y][i] < 7) {
                        g.setColor(Color.red);
                        g.fillRect(i * cellSide + border + KillSide, y * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                // Move to top
                g.setColor(Color.green);
                for (int i = y - 1; i >= 0; i--) {
                    if (chessBoard[i][x] == 0) {
                        g.fillOval(x * cellSide + border + MoveSide, i * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[i][x] < 7) {
                        g.setColor(Color.red);
                        g.fillRect(x * cellSide + border + KillSide, i * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                // Move to bottom
                g.setColor(Color.green);
                for (int i = y + 1; i < 8; i++) {
                    if (chessBoard[i][x] == 0) {
                        g.fillOval(x * cellSide + border + MoveSide, i * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[i][x] < 7) {
                        g.setColor(Color.red);
                        g.fillRect(x * cellSide + border + KillSide, i * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                // Move to top left
                g.setColor(Color.green);
                for (int i = 1; ((x - i) >= 0) && ((y - i) >= 0) && ((x - i) < 8) && ((y - i) < 8); i++) {
                    if (chessBoard[y - i][x - i] == 0) {
                        g.fillOval((x - i) * cellSide + border + MoveSide, (y - i) * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[y - i][x - i] < 7) {
                        g.setColor(Color.red);
                        g.fillRect((x - i) * cellSide + border + KillSide, (y - i) * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                // Move to top right
                g.setColor(Color.green);
                for (int i = 1; ((x + i) >= 0) && ((y - i) >= 0) && ((x + i) < 8) && ((y - i) < 8); i++) {
                    if (chessBoard[y - i][x + i] == 0) {
                        g.fillOval((x + i) * cellSide + border + MoveSide, (y - i) * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[y - i][x + i] < 7) {
                        g.setColor(Color.red);
                        g.fillRect((x + i) * cellSide + border + KillSide, (y - i) * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                // Move to bottom right
                g.setColor(Color.green);
                for (int i = 1; ((x + i) >= 0) && ((y + i) >= 0) && ((x + i) < 8) && ((y + i) < 8); i++) {
                    if (chessBoard[y + i][x + i] == 0) {
                        g.fillOval((x + i) * cellSide + border + MoveSide, (y + i) * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[y + i][x + i] < 7) {
                        g.setColor(Color.red);
                        g.fillRect((x + i) * cellSide + border + KillSide, (y + i) * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                // Move to bottom left
                g.setColor(Color.green);
                for (int i = 1; ((x - i) >= 0) && ((y + i) >= 0) && ((x - i) < 8) && ((y + i) < 8); i++) {
                    if (chessBoard[y + i][x - i] == 0) {
                        g.fillOval((x - i) * cellSide + border + MoveSide, (y + i) * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[y + i][x - i] < 7) {
                        g.setColor(Color.red);
                        g.fillRect((x - i) * cellSide + border + KillSide, (y + i) * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                break;

            case 5:

                // Move to left
                for (int i = x - 1; i >= 0; i--) {
                    if (chessBoard[y][i] == 0) {
                        g.fillOval(i * cellSide + border + MoveSide, y * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[y][i] > 7) {
                        g.setColor(Color.red);
                        g.fillRect(i * cellSide + border + KillSide, y * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                // Move to right
                g.setColor(Color.green);
                for (int i = x + 1; i < 8; i++) {
                    if (chessBoard[y][i] == 0) {
                        g.fillOval(i * cellSide + border + MoveSide, y * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[y][i] > 7) {
                        g.setColor(Color.red);
                        g.fillRect(i * cellSide + border + KillSide, y * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                // Move to top
                g.setColor(Color.green);
                for (int i = y - 1; i >= 0; i--) {
                    if (chessBoard[i][x] == 0) {
                        g.fillOval(x * cellSide + border + MoveSide, i * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[i][x] > 7) {
                        g.setColor(Color.red);
                        g.fillRect(x * cellSide + border + KillSide, i * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                // Move to bottom
                g.setColor(Color.green);
                for (int i = y + 1; i < 8; i++) {
                    if (chessBoard[i][x] == 0) {
                        g.fillOval(x * cellSide + border + MoveSide, i * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[i][x] > 7) {
                        g.setColor(Color.red);
                        g.fillRect(x * cellSide + border + KillSide, i * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                // Move to top left
                g.setColor(Color.green);
                for (int i = 1; ((x - i) >= 0) && ((y - i) >= 0) && ((x - i) < 8) && ((y - i) < 8); i++) {
                    if (chessBoard[y - i][x - i] == 0) {
                        g.fillOval((x - i) * cellSide + border + MoveSide, (y - i) * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[y - i][x - i] > 7) {
                        g.setColor(Color.red);
                        g.fillRect((x - i) * cellSide + border + KillSide, (y - i) * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                // Move to top right
                g.setColor(Color.green);
                for (int i = 1; ((x + i) >= 0) && ((y - i) >= 0) && ((x + i) < 8) && ((y - i) < 8); i++) {
                    if (chessBoard[y - i][x + i] == 0) {
                        g.fillOval((x + i) * cellSide + border + MoveSide, (y - i) * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[y - i][x + i] > 7) {
                        g.setColor(Color.red);
                        g.fillRect((x + i) * cellSide + border + KillSide, (y - i) * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                // Move to bottom right
                g.setColor(Color.green);
                for (int i = 1; ((x + i) >= 0) && ((y + i) >= 0) && ((x + i) < 8) && ((y + i) < 8); i++) {
                    if (chessBoard[y + i][x + i] == 0) {
                        g.fillOval((x + i) * cellSide + border + MoveSide, (y + i) * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[y + i][x + i] > 7) {
                        g.setColor(Color.red);
                        g.fillRect((x + i) * cellSide + border + KillSide, (y + i) * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                // Move to bottom left
                g.setColor(Color.green);
                for (int i = 1; ((x - i) >= 0) && ((y + i) >= 0) && ((x - i) < 8) && ((y + i) < 8); i++) {
                    if (chessBoard[y + i][x - i] == 0) {
                        g.fillOval((x - i) * cellSide + border + MoveSide, (y + i) * cellSide + border + MoveSide, ovalMove, ovalMove);
                    } else if (chessBoard[y + i][x - i] > 7) {
                        g.setColor(Color.red);
                        g.fillRect((x - i) * cellSide + border + KillSide, (y + i) * cellSide + border + KillSide, rectKill, rectKill);
                        break;
                    } else
                        break;
                }

                break;

            case 16:
                // NEXT_STEP
                if (y-1>=0) {
                    if (chessBoard[y-1][x] == 0 ) {
                        g.fillOval(x * cellSide + border + MoveSide, (y - 1) * cellSide + border + MoveSide, ovalMove, ovalMove);
                    }
                }
                if (y+1<8) {
                    if (chessBoard[y+1][x] == 0 ) {
                        g.fillOval(x * cellSide + border + MoveSide, (y + 1) * cellSide + border + MoveSide, ovalMove, ovalMove);
                    }
                }
                if (x-1>=0) {
                    if (chessBoard[y][x-1] == 0 ) {
                        g.fillOval((x-1) * cellSide + border + MoveSide, y * cellSide + border + MoveSide, ovalMove, ovalMove);
                    }
                }
                if (x+1<8) {
                    if (chessBoard[y][x+1] == 0 ) {
                        g.fillOval((x+1) * cellSide + border + MoveSide, y * cellSide + border + MoveSide, ovalMove, ovalMove);
                    }
                }


                if (x-1>=0){
                    if (y-1>=0) {
                        if (chessBoard[y-1][x-1] == 0) {
                            g.fillOval((x-1) * cellSide + border + MoveSide, (y-1) * cellSide + border + MoveSide, ovalMove, ovalMove);
                        }
                    }
                    if (y+1<8) {
                        if (chessBoard[y+1][x-1] == 0) {
                            g.fillOval((x-1) * cellSide + border + MoveSide, (y+1) * cellSide + border + MoveSide, ovalMove, ovalMove);
                        }
                    }
                }

                if (x+1<8){
                    if (y-1>=0) {
                        if (chessBoard[y-1][x+1] == 0) {
                            g.fillOval((x+1) * cellSide + border + MoveSide, (y-1) * cellSide + border + MoveSide, ovalMove, ovalMove);
                        }
                    }
                    if (y+1<8) {
                        if (chessBoard[y+1][x+1] == 0) {
                            g.fillOval((x+1) * cellSide + border + MoveSide, (y+1) * cellSide + border + MoveSide, ovalMove, ovalMove);
                        }
                    }
                }

                // NEXT_KILL

                if (y-1>=0) {
                    if (chessBoard[y-1][x] != 0 && chessBoard[y-1][x] < 7) {
                        g.setColor(Color.red);
                        g.fillRect(x * cellSide + border + KillSide, (y - 1) * cellSide + border + KillSide, rectKill, rectKill);
                    }
                }
                if (y+1<8) {
                    if (chessBoard[y+1][x] != 0 && chessBoard[y+1][x] < 7 ) {
                        g.setColor(Color.red);
                        g.fillRect(x * cellSide + border + KillSide, (y + 1) * cellSide + border + KillSide, rectKill, rectKill);
                    }
                }
                if (x-1>=0) {
                    if (chessBoard[y][x-1] != 0 && chessBoard[y][x-1] < 7 ) {
                        g.setColor(Color.red);
                        g.fillRect((x - 1) * cellSide + border + KillSide, y * cellSide + border + KillSide, rectKill, rectKill);
                    }
                }
                if (x+1<8) {
                    if (chessBoard[y][x+1] != 0 && chessBoard[y][x+1] < 7 ) {
                        g.setColor(Color.red);
                        g.fillRect((x + 1) * cellSide + border + KillSide, y * cellSide + border + KillSide, rectKill, rectKill);
                    }
                }

                if (x-1>=0){
                    if (y-1>=0) {
                        if (chessBoard[y-1][x-1] != 0 && chessBoard[y-1][x-1] < 7) {
                            g.setColor(Color.red);
                            g.fillRect((x - 1) * cellSide + border + KillSide, (y - 1) * cellSide + border + KillSide, rectKill, rectKill);
                        }
                    }
                    if (y+1<8) {
                        if (chessBoard[y+1][x-1] != 0 && chessBoard[y+1][x-1] < 7) {
                            g.setColor(Color.red);
                            g.fillRect((x - 1) * cellSide + border + KillSide, (y + 1) * cellSide + border + KillSide, rectKill, rectKill);
                        }
                    }
                }

                if (x+1<8){
                    if (y-1>=0) {
                        if (chessBoard[y-1][x+1] != 0 && chessBoard[y-1][x+1] < 7) {
                            g.setColor(Color.red);
                            g.fillRect((x + 1) * cellSide + border + KillSide, (y - 1) * cellSide + border + KillSide, rectKill, rectKill);
                        }
                    }
                    if (y+1<8) {
                        if (chessBoard[y+1][x+1] != 0 && chessBoard[y+1][x+1] < 7) {
                            g.setColor(Color.red);
                            g.fillRect((x + 1) * cellSide + border + KillSide, (y + 1) * cellSide + border + KillSide, rectKill, rectKill);
                        }
                    }
                }

                break;

            case 6:
                // NEXT_STEP
                if (y-1>=0) {
                    if (chessBoard[y-1][x] == 0 ) {
                        g.fillOval(x * cellSide + border + MoveSide, (y - 1) * cellSide + border + MoveSide, ovalMove, ovalMove);
                    }
                }
                if (y+1<8) {
                    if (chessBoard[y+1][x] == 0 ) {
                        g.fillOval(x * cellSide + border + MoveSide, (y + 1) * cellSide + border + MoveSide, ovalMove, ovalMove);
                    }
                }
                if (x-1>=0) {
                    if (chessBoard[y][x-1] == 0 ) {
                        g.fillOval((x-1) * cellSide + border + MoveSide, y * cellSide + border + MoveSide, ovalMove, ovalMove);
                    }
                }
                if (x+1<8) {
                    if (chessBoard[y][x+1] == 0 ) {
                        g.fillOval((x+1) * cellSide + border + MoveSide, y * cellSide + border + MoveSide, ovalMove, ovalMove);
                    }
                }

                if (x-1>=0){
                    if (y-1>=0) {
                        if (chessBoard[y-1][x-1] == 0) {
                            g.fillOval((x-1) * cellSide + border + MoveSide, (y-1) * cellSide + border + MoveSide, ovalMove, ovalMove);
                        }
                    }
                    if (y+1<8) {
                        if (chessBoard[y+1][x-1] == 0) {
                            g.fillOval((x-1) * cellSide + border + MoveSide, (y+1) * cellSide + border + MoveSide, ovalMove, ovalMove);
                        }
                    }
                }
                if (x+1<8){
                    if (y-1>=0) {
                        if (chessBoard[y-1][x+1] == 0) {
                            g.fillOval((x+1) * cellSide + border + MoveSide, (y-1) * cellSide + border + MoveSide, ovalMove, ovalMove);
                        }
                    }
                    if (y+1<8) {
                        if (chessBoard[y+1][x+1] == 0) {
                            g.fillOval((x+1) * cellSide + border + MoveSide, (y+1) * cellSide + border + MoveSide, ovalMove, ovalMove);
                        }
                    }
                }

                // NEXT_KILL
                if (y-1>=0) {
                    if (chessBoard[y-1][x] != 0 && chessBoard[y-1][x] > 7) {
                        g.setColor(Color.red);
                        g.fillRect(x * cellSide + border + KillSide, (y - 1) * cellSide + border + KillSide, rectKill, rectKill);
                    }
                }
                if (y+1<8) {
                    if (chessBoard[y+1][x] != 0 && chessBoard[y+1][x] > 7 ) {
                        g.setColor(Color.red);
                        g.fillRect(x * cellSide + border + KillSide, (y + 1) * cellSide + border + KillSide, rectKill, rectKill);
                    }
                }
                if (x-1>=0) {
                    if (chessBoard[y][x-1] != 0 && chessBoard[y][x-1] > 7 ) {
                        g.setColor(Color.red);
                        g.fillRect((x - 1) * cellSide + border + KillSide, y * cellSide + border + KillSide, rectKill, rectKill);
                    }
                }
                if (x+1<8) {
                    if (chessBoard[y][x+1] != 0 && chessBoard[y][x+1] > 7 ) {
                        g.setColor(Color.red);
                        g.fillRect((x + 1) * cellSide + border + KillSide, y * cellSide + border + KillSide, rectKill, rectKill);
                    }
                }

                if (x-1>=0){
                    if (y-1>=0) {
                        if (chessBoard[y-1][x-1] != 0 && chessBoard[y-1][x-1] > 7) {
                            g.setColor(Color.red);
                            g.fillRect((x - 1) * cellSide + border + KillSide, (y - 1) * cellSide + border + KillSide, rectKill, rectKill);
                        }
                    }
                    if (y+1<8) {
                        if (chessBoard[y+1][x-1] != 0 && chessBoard[y+1][x-1] > 7) {
                            g.setColor(Color.red);
                            g.fillRect((x - 1) * cellSide + border + KillSide, (y + 1) * cellSide + border + KillSide, rectKill, rectKill);
                        }
                    }
                }

                if (x+1<8){
                    if (y-1>=0) {
                        if (chessBoard[y-1][x+1] != 0 && chessBoard[y-1][x+1] > 7) {
                            g.setColor(Color.red);
                            g.fillRect((x + 1) * cellSide + border + KillSide, (y - 1) * cellSide + border + KillSide, rectKill, rectKill);
                        }
                    }
                    if (y+1<8) {
                        if (chessBoard[y+1][x+1] != 0 && chessBoard[y+1][x+1] > 7) {
                            g.setColor(Color.red);
                            g.fillRect((x + 1) * cellSide + border + KillSide, (y + 1) * cellSide + border + KillSide, rectKill, rectKill);
                        }
                    }
                }

                break;

            default:
                break;
        }
    }

    public BufferedImage getImageByPath(String path) {
        try {
            return ImageIO.read(getClass().getResourceAsStream(path));
        } catch (Exception e) {
            System.out.println("File error!");
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    class CustomMouseListener implements MouseListener {
        public void mouseClicked(MouseEvent e) {

            if (xCurrent == (e.getX() - border) / cellSide && yCurrent == (e.getY() - border) / cellSide) {
                xCurrent = -1;
                yCurrent = -1;
            } else if (e.getX() >= border && e.getX() < border + cellSide * 8 && e.getY() >= border
                    && e.getY() < border + cellSide * 8) {
                xCurrent = (e.getX() - border) / cellSide;
                yCurrent = (e.getY() - border) / cellSide;
            }

            System.out.println((xCurrent) + " " + (yCurrent));
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}
