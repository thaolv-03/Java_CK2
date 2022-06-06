package Chess;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

public class ChessPanel extends JPanel implements ActionListener {
    Timer timer = new Timer(1000 / 60, this);

    Graphics2D grp2;

    int border = 50;

    final int Screen_Width = 700 + 2 * border;
    final int Screen_Height = 700 + 2 * border;
    final int cellSide = 700 / 8;
    int xCurrent = -1;
    int yCurrent = -1;

    final int chessW = cellSide;
    final int chessH = cellSide * 360 / 250;
    Map<Integer, BufferedImage> map = new HashMap<Integer, BufferedImage>();
    // r n b k q b n r
    // 2 3 4 6 5 4 3 2
    int[][] chessBoard = {
            { 02, 00, 00, 00, 00, 00, 03, 02 },
            { 01, 01, 01, 01, 06, 01, 01, 00 },
            { 03, 00, 04, 00, 01, 11, 00, 00 },
            { 01, 16, 00, 13, 00, 01, 00, 15 },
            { 01, 01, 00, 00, 15, 00, 16, 00 },
            { 00, 00, 14, 00, 00, 00, 00, 00 },
            { 00, 13, 00, 11, 11, 11, 11, 00 },
            { 12, 00, 00, 00, 16, 14, 13, 12 },
    };

    public ChessPanel() {
        this.setPreferredSize(new Dimension(Screen_Width, Screen_Height));
        this.setBackground(Color.lightGray);
        this.addMouseListener(new CustomMouseListener());
        this.timer.start();

        // BLACK chess piece
        map.put(1, getImageByPath("img-chess/pawn-black.png"));
        map.put(2, getImageByPath("img-chess/rook-black.png"));
        map.put(3, getImageByPath("img-chess/knight-black.png"));
        map.put(4, getImageByPath("img-chess/bishop-black.png"));
        map.put(5, getImageByPath("img-chess/queen-black.png"));
        map.put(6, getImageByPath("img-chess/king-black.png"));
        // WHITE chess piece
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

        this.grp2 = g2;

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
        g2.setColor(new Color(0, 153, 255));

        if (xCurrent >= 0 && yCurrent >= 0) {
            if (chessBoard[yCurrent][xCurrent] != 0) {
                g2.fillRect(xCurrent * cellSide + border, yCurrent * cellSide + border, cellSide, cellSide);
            }

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

    // Move chess pieces
    public void moveChessPieces(int x, int y) {
        chessBoard[y][x] = chessBoard[yCurrent][xCurrent];
        chessBoard[yCurrent][xCurrent] = 0;
        xCurrent = -1;
        yCurrent = -1;
    }

    // Next step of chess pieces
    public boolean[][] nextStep(int x, int y, Graphics2D g) {
        boolean[][] result = {
                { false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false },
        };

        g.setColor(new Color(0, 255, 0, 127));
        switch (chessBoard[y][x]) {

            // ---------------- PAWN ----------------
            case 11:
                // NEXT_STEP
                if (chessBoard[y - 1][x] == 0) { // if nextStep don't have any chess
                    g.fillRect(x * cellSide + border, (y - 1) * cellSide + border, cellSide, cellSide);
                    result[x][y - 1] = true;
                    if (y == 6 && chessBoard[y - 2][x] == 0) {
                        g.fillRect(x * cellSide + border, (y - 2) * cellSide + border, cellSide, cellSide);
                        result[x][y - 2] = true;
                    }
                }
                // NEXT_KILL
                g.setColor(new Color(230, 0, 0, 180));
                if (x - 1 >= 0) {
                    if (chessBoard[y - 1][x - 1] != 0 && chessBoard[y - 1][x - 1] < 7) {
                        g.fillRect((x - 1) * cellSide + border, (y - 1) * cellSide + border, cellSide, cellSide);
                        result[x - 1][y - 1] = true;
                    }
                }
                if (x + 1 < 8) {
                    if (chessBoard[y - 1][x + 1] != 0 && chessBoard[y - 1][x + 1] < 7) {
                        g.fillRect((x + 1) * cellSide + border, (y - 1) * cellSide + border, cellSide, cellSide);
                        result[x + 1][y - 1] = true;
                    }
                }

                break;

            case 1:
                // NEXT_STEP
                if (chessBoard[y + 1][x] == 0) {
                    g.fillRect(x * cellSide + border, (y + 1) * cellSide + border, cellSide, cellSide);
                    result[x][y + 1] = true;
                    if (y == 1 && chessBoard[y + 2][x] == 0) {
                        g.fillRect(x * cellSide + border, (y + 2) * cellSide + border, cellSide, cellSide);
                        result[x][y + 2] = true;
                    }
                }
                // NEXT_KILL
                g.setColor(new Color(230, 0, 0, 180));
                if (x - 1 >= 0) {
                    if (chessBoard[y + 1][x - 1] != 0 && chessBoard[y + 1][x - 1] > 7) {
                        g.fillRect((x - 1) * cellSide + border, (y + 1) * cellSide + border, cellSide, cellSide);
                        result[x - 1][y + 1] = true;
                    }
                }
                if (x + 1 < 8) {
                    if (chessBoard[y + 1][x + 1] != 0 && chessBoard[y + 1][x + 1] > 7) {
                        g.fillRect((x + 1) * cellSide + border, (y + 1) * cellSide + border, cellSide, cellSide);
                        result[x + 1][y + 1] = true;
                    }
                }
                break;

            // ---------------- ROOK ----------------
            case 12:
                // Move to left
                for (int i = x - 1; i >= 0; i--) {
                    if (chessBoard[y][i] == 0) {
                        g.fillRect(i * cellSide + border, y * cellSide + border, cellSide, cellSide);
                        result[i][y] = true;
                    } else if (chessBoard[y][i] < 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect(i * cellSide + border, y * cellSide + border, cellSide, cellSide);
                        result[i][y] = true;
                        break;
                    } else
                        // result[i][y] = true;
                        break;
                }

                // Move to right
                g.setColor(new Color(0, 255, 0, 127));
                for (int i = x + 1; i < 8; i++) {
                    if (chessBoard[y][i] == 0) {
                        g.fillRect(i * cellSide + border, y * cellSide + border, cellSide, cellSide);
                        result[i][y] = true;
                    } else if (chessBoard[y][i] < 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect(i * cellSide + border, y * cellSide + border, cellSide, cellSide);
                        result[i][y] = true;
                        break;
                    } else
                        break;
                }

                // Move to top
                g.setColor(new Color(0, 255, 0, 127));
                for (int i = y - 1; i >= 0; i--) {
                    if (chessBoard[i][x] == 0) {
                        g.fillRect(x * cellSide + border, i * cellSide + border, cellSide, cellSide);
                        result[x][i] = true;
                    } else if (chessBoard[i][x] < 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect(x * cellSide + border, i * cellSide + border, cellSide, cellSide);
                        result[x][i] = true;
                        break;
                    } else
                        break;
                }

                // Move to bottom
                g.setColor(new Color(0, 255, 0, 127));
                for (int i = y + 1; i < 8; i++) {
                    if (chessBoard[i][x] == 0) {
                        g.fillRect(x * cellSide + border, i * cellSide + border, cellSide, cellSide);
                        result[x][i] = true;
                    } else if (chessBoard[i][x] < 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect(x * cellSide + border, i * cellSide + border, cellSide, cellSide);
                        result[x][i] = true;
                        break;
                    } else
                        break;
                }

                break;
            case 2:
                // Move to left
                for (int i = x - 1; i >= 0; i--) {
                    if (chessBoard[y][i] == 0) {
                        g.fillRect(i * cellSide + border, y * cellSide + border, cellSide, cellSide);
                        result[i][y] = true;
                    } else if (chessBoard[y][i] > 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect(i * cellSide + border, y * cellSide + border, cellSide, cellSide);
                        result[i][y] = true;
                        break;
                    } else
                        break;
                }

                // Move to right
                g.setColor(new Color(0, 255, 0, 127));
                for (int i = x + 1; i < 8; i++) {
                    if (chessBoard[y][i] == 0) {
                        g.fillRect(i * cellSide + border, y * cellSide + border, cellSide, cellSide);
                        result[i][y] = true;
                    } else if (chessBoard[y][i] > 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect(i * cellSide + border, y * cellSide + border, cellSide, cellSide);
                        result[i][y] = true;
                        break;
                    } else
                        break;
                }

                // Move to top
                g.setColor(new Color(0, 255, 0, 127));
                for (int i = y - 1; i >= 0; i--) {
                    if (chessBoard[i][x] == 0) {
                        g.fillRect(x * cellSide + border, i * cellSide + border, cellSide, cellSide);
                        result[x][i] = true;
                    } else if (chessBoard[i][x] > 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect(x * cellSide + border, i * cellSide + border, cellSide, cellSide);
                        result[x][i] = true;
                        break;
                    } else
                        break;
                }

                // Move to bottom
                g.setColor(new Color(0, 255, 0, 127));
                for (int i = y + 1; i < 8; i++) {
                    if (chessBoard[i][x] == 0) {
                        g.fillRect(x * cellSide + border, i * cellSide + border, cellSide, cellSide);
                        result[x][i] = true;
                    } else if (chessBoard[i][x] > 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect(x * cellSide + border, i * cellSide + border, cellSide, cellSide);
                        result[x][i] = true;
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
                            g.fillRect((x - 1) * cellSide + border, (y - 2) * cellSide + border, cellSide, cellSide);
                            result[x - 1][y - 2] = true;
                        }
                    }
                    if (y + 2 < 8) {
                        if (chessBoard[y + 2][x - 1] == 0) {
                            g.fillRect((x - 1) * cellSide + border, (y + 2) * cellSide + border, cellSide, cellSide);
                            result[x - 1][y + 2] = true;
                        }
                    }
                }
                // Move to top & bottom ( right )
                if ((x + 1) < 8) {
                    if (y - 2 >= 0)
                        if (chessBoard[y - 2][x + 1] == 0) {
                            g.fillRect((x + 1) * cellSide + border, (y - 2) * cellSide + border, cellSide, cellSide);
                            result[x + 1][y - 2] = true;
                        }

                    if (y + 2 < 8)
                        if (chessBoard[y + 2][x + 1] == 0) {
                            g.fillRect((x + 1) * cellSide + border, (y + 2) * cellSide + border, cellSide, cellSide);
                            result[x + 1][y + 2] = true;
                        }
                }

                // ACROSS
                // Move to top & bottom ( left )
                if ((x - 2) >= 0) {
                    if ((y - 1) >= 0)
                        if (chessBoard[y - 1][x - 2] == 0) {
                            g.fillRect((x - 2) * cellSide + border, (y - 1) * cellSide + border, cellSide, cellSide);
                            result[x - 2][y - 1] = true;
                        }
                    if ((y + 1) < 8)
                        if (chessBoard[y + 1][x - 2] == 0) {
                            g.fillRect((x - 2) * cellSide + border, (y + 1) * cellSide + border, cellSide, cellSide);
                            result[x - 2][y + 1] = true;
                        }
                }
                // Move to top & bottom ( right )
                if ((x + 2) < 8) {
                    if ((y - 1) >= 0)
                        if (chessBoard[y - 1][x + 2] == 0) {
                            g.fillRect((x + 2) * cellSide + border, (y - 1) * cellSide + border, cellSide, cellSide);
                            result[x + 2][y - 1] = true;
                        }
                    if ((y + 1) < 8)
                        if (chessBoard[y + 1][x + 2] == 0) {
                            g.fillRect((x + 2) * cellSide + border, (y + 1) * cellSide + border, cellSide, cellSide);
                            result[x + 2][y + 1] = true;
                        }
                }

                // NEXT_KILL
                // ALONG
                g.setColor(new Color(230, 0, 0, 180));
                // Kill top & bottom ( left )
                if ((x - 1) >= 0) {
                    if (y - 2 >= 0)
                        if (chessBoard[y - 2][x - 1] != 0 && chessBoard[y - 2][x - 1] < 7) {
                            g.fillRect((x - 1) * cellSide + border, (y - 2) * cellSide + border, cellSide, cellSide);
                            result[x - 1][y - 2] = true;
                        }
                    if ((y + 2) < 8)
                        if (chessBoard[y + 2][x - 1] != 0 && chessBoard[y + 2][x - 1] < 7) {
                            g.fillRect((x - 1) * cellSide + border, (y + 2) * cellSide + border, cellSide, cellSide);
                            result[x - 1][y + 2] = true;
                        }
                }
                // Kill top & bottom ( right )
                if ((x + 1) < 8) {
                    if ((y - 2) >= 0)
                        if (chessBoard[y - 2][x + 1] != 0 && chessBoard[y - 2][x + 1] < 7) {
                            g.fillRect((x + 1) * cellSide + border, (y - 2) * cellSide + border, cellSide, cellSide);
                            result[x + 1][y - 2] = true;
                        }
                    if ((y + 2) < 8)
                        if (chessBoard[y + 2][x + 1] != 0 && chessBoard[y + 2][x + 1] < 7) {
                            g.fillRect((x + 1) * cellSide + border, (y + 2) * cellSide + border, cellSide, cellSide);
                            result[x + 1][y + 2] = true;
                        }
                }

                // ACROSS
                // Kill top & bottom ( left )
                if ((x - 2) >= 0) {
                    if ((y - 1) >= 0)
                        if (chessBoard[y - 1][x - 2] != 0 && chessBoard[y - 1][x - 2] < 7) {
                            g.fillRect((x - 2) * cellSide + border, (y - 1) * cellSide + border, cellSide, cellSide);
                            result[x - 2][y - 1] = true;
                        }
                    if ((y + 1) < 8)
                        if (chessBoard[y + 1][x - 2] != 0 && chessBoard[y + 1][x - 2] < 7) {
                            g.fillRect((x - 2) * cellSide + border, (y + 1) * cellSide + border, cellSide, cellSide);
                            result[x - 2][y + 1] = true;
                        }
                }
                // Kill top & bottom ( right )
                if ((x + 2) < 8) {
                    if ((y - 1) >= 0)
                        if (chessBoard[y - 1][x + 2] != 0 && chessBoard[y - 1][x + 2] < 7) {
                            g.fillRect((x + 2) * cellSide + border, (y - 1) * cellSide + border, cellSide, cellSide);
                            result[x + 2][y - 1] = true;
                        }
                    if ((y + 1) < 8)
                        if (chessBoard[y + 1][x + 2] != 0 && chessBoard[y + 1][x + 2] < 7) {
                            g.fillRect((x + 2) * cellSide + border, (y + 1) * cellSide + border, cellSide, cellSide);
                            result[x + 2][y + 1] = true;
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
                            g.fillRect((x - 1) * cellSide + border, (y - 2) * cellSide + border, cellSide, cellSide);
                            result[x - 1][y - 2] = true;
                        }
                    }
                    if (y + 2 < 8) {
                        if (chessBoard[y + 2][x - 1] == 0) {
                            g.fillRect((x - 1) * cellSide + border, (y + 2) * cellSide + border, cellSide, cellSide);
                            result[x - 1][y + 2] = true;
                        }
                    }
                }
                // Move to top & bottom ( right )
                if ((x + 1) < 8) {
                    if (y - 2 >= 0)
                        if (chessBoard[y - 2][x + 1] == 0) {
                            g.fillRect((x + 1) * cellSide + border, (y - 2) * cellSide + border, cellSide, cellSide);
                            result[x + 1][y - 2] = true;
                        }

                    if (y + 2 < 8)
                        if (chessBoard[y + 2][x + 1] == 0) {
                            g.fillRect((x + 1) * cellSide + border, (y + 2) * cellSide + border, cellSide, cellSide);
                            result[x + 1][y + 2] = true;
                        }
                }

                // ACROSS
                // Move to top & bottom ( left )
                if ((x - 2) >= 0) {
                    if ((y - 1) >= 0)
                        if (chessBoard[y - 1][x - 2] == 0) {
                            g.fillRect((x - 2) * cellSide + border, (y - 1) * cellSide + border, cellSide, cellSide);
                            result[x - 2][y - 1] = true;
                        }
                    if ((y + 1) < 8)
                        if (chessBoard[y + 1][x - 2] == 0) {
                            g.fillRect((x - 2) * cellSide + border, (y + 1) * cellSide + border, cellSide, cellSide);
                            result[x - 2][y + 1] = true;
                        }
                }
                // Move to top & bottom ( right )
                if ((x + 2) < 8) {
                    if ((y - 1) >= 0)
                        if (chessBoard[y - 1][x + 2] == 0) {
                            g.fillRect((x + 2) * cellSide + border, (y - 1) * cellSide + border, cellSide, cellSide);
                            result[x + 2][y - 1] = true;
                        }
                    if ((y + 1) < 8)
                        if (chessBoard[y + 1][x + 2] == 0) {
                            g.fillRect((x + 2) * cellSide + border, (y + 1) * cellSide + border, cellSide, cellSide);
                            result[x + 2][y + 1] = true;
                        }
                }

                // NEXT_KILL
                // ALONG
                g.setColor(new Color(230, 0, 0, 180));
                // Kill top & bottom ( left )
                if ((x - 1) >= 0) {
                    if (y - 2 >= 0)
                        if (chessBoard[y - 2][x - 1] != 0 && chessBoard[y - 2][x - 1] > 7) {
                            g.fillRect((x - 1) * cellSide + border, (y - 2) * cellSide + border, cellSide, cellSide);
                            result[x - 1][y - 2] = true;
                        }
                    if ((y + 2) < 8)
                        if (chessBoard[y + 2][x - 1] != 0 && chessBoard[y + 2][x - 1] > 7) {
                            g.fillRect((x - 1) * cellSide + border, (y + 2) * cellSide + border, cellSide, cellSide);
                            result[x - 1][y + 2] = true;
                        }
                }
                // Kill top & bottom ( right )
                if ((x + 1) < 8) {
                    if ((y - 2) >= 0)
                        if (chessBoard[y - 2][x + 1] != 0 && chessBoard[y - 2][x + 1] > 7) {
                            g.fillRect((x + 1) * cellSide + border, (y - 2) * cellSide + border, cellSide, cellSide);
                            result[x + 1][y - 2] = true;
                        }
                    if ((y + 2) < 8)
                        if (chessBoard[y + 2][x + 1] != 0 && chessBoard[y + 2][x + 1] > 7) {
                            g.fillRect((x + 1) * cellSide + border, (y + 2) * cellSide + border, cellSide, cellSide);
                            result[x + 1][y + 2] = true;
                        }
                }

                // ACROSS
                // Kill top & bottom ( left )
                if ((x - 2) >= 0) {
                    if ((y - 1) >= 0)
                        if (chessBoard[y - 1][x - 2] != 0 && chessBoard[y - 1][x - 2] > 7) {
                            g.fillRect((x - 2) * cellSide + border, (y - 1) * cellSide + border, cellSide, cellSide);
                            result[x - 2][y - 1] = true;
                        }
                    if ((y + 1) < 8)
                        if (chessBoard[y + 1][x - 2] != 0 && chessBoard[y + 1][x - 2] > 7) {
                            g.fillRect((x - 2) * cellSide + border, (y + 1) * cellSide + border, cellSide, cellSide);
                            result[x - 2][y + 1] = true;
                        }
                }
                // Kill top & bottom ( right )
                if ((x + 2) < 8) {
                    if ((y - 1) >= 0)
                        if (chessBoard[y - 1][x + 2] != 0 && chessBoard[y - 1][x + 2] > 7) {
                            g.fillRect((x + 2) * cellSide + border, (y - 1) * cellSide + border, cellSide, cellSide);
                            result[x + 2][y - 1] = true;
                        }
                    if ((y + 1) < 8)
                        if (chessBoard[y + 1][x + 2] != 0 && chessBoard[y + 1][x + 2] > 7) {
                            g.fillRect((x + 2) * cellSide + border, (y + 1) * cellSide + border, cellSide, cellSide);
                            result[x + 2][y + 1] = true;
                        }
                }

                break;

            // ---------------- BISHOP ----------------
            case 14:
                // Move to top left
                g.setColor(new Color(0, 255, 0, 127));
                for (int i = 1; ((x - i) >= 0) && ((y - i) >= 0) && ((x - i) < 8) && ((y - i) < 8); i++) {
                    if (chessBoard[y - i][x - i] == 0) {
                        g.fillRect((x - i) * cellSide + border, (y - i) * cellSide + border, cellSide, cellSide);
                        result[x - i][y - i] = true;
                    } else if (chessBoard[y - i][x - i] < 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect((x - i) * cellSide + border, (y - i) * cellSide + border, cellSide, cellSide);
                        result[x - i][y - i] = true;
                        break;
                    } else
                        break;
                }

                // Move to top right
                g.setColor(new Color(0, 255, 0, 127));
                for (int i = 1; ((x + i) >= 0) && ((y - i) >= 0) && ((x + i) < 8) && ((y - i) < 8); i++) {
                    if (chessBoard[y - i][x + i] == 0) {
                        g.fillRect((x + i) * cellSide + border, (y - i) * cellSide + border, cellSide, cellSide);
                        result[x + i][y - i] = true;
                    } else if (chessBoard[y - i][x + i] < 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect((x + i) * cellSide + border, (y - i) * cellSide + border, cellSide, cellSide);
                        result[x + i][y - i] = true;
                        break;
                    } else
                        break;
                }

                // Move to bottom right
                g.setColor(new Color(0, 255, 0, 127));
                for (int i = 1; ((x + i) >= 0) && ((y + i) >= 0) && ((x + i) < 8) && ((y + i) < 8); i++) {
                    if (chessBoard[y + i][x + i] == 0) {
                        g.fillRect((x + i) * cellSide + border, (y + i) * cellSide + border, cellSide, cellSide);
                        result[x + i][y + i] = true;
                    } else if (chessBoard[y + i][x + i] < 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect((x + i) * cellSide + border, (y + i) * cellSide + border, cellSide, cellSide);
                        result[x + i][y + i] = true;
                        break;
                    } else
                        break;
                }

                // Move to bottom left
                g.setColor(new Color(0, 255, 0, 127));
                for (int i = 1; ((x - i) >= 0) && ((y + i) >= 0) && ((x - i) < 8) && ((y + i) < 8); i++) {
                    if (chessBoard[y + i][x - i] == 0) {
                        g.fillRect((x - i) * cellSide + border, (y + i) * cellSide + border, cellSide, cellSide);
                        result[x - i][y + i] = true;
                    } else if (chessBoard[y + i][x - i] < 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect((x - i) * cellSide + border, (y + i) * cellSide + border, cellSide, cellSide);
                        result[x - i][y + i] = true;
                        break;
                    } else
                        break;
                }
                break;

            case 4:
                // Move to top left
                g.setColor(new Color(0, 255, 0, 127));
                for (int i = 1; ((x - i) >= 0) && ((y - i) >= 0) && ((x - i) < 8) && ((y - i) < 8); i++) {
                    if (chessBoard[y - i][x - i] == 0) {
                        g.fillRect((x - i) * cellSide + border, (y - i) * cellSide + border, cellSide, cellSide);
                        result[x - i][y - i] = true;
                    } else if (chessBoard[y - i][x - i] > 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect((x - i) * cellSide + border, (y - i) * cellSide + border, cellSide, cellSide);
                        result[x - i][y - i] = true;
                        break;
                    } else
                        break;
                }

                // Move to top right
                g.setColor(new Color(0, 255, 0, 127));
                for (int i = 1; ((x + i) >= 0) && ((y - i) >= 0) && ((x + i) < 8) && ((y - i) < 8); i++) {
                    if (chessBoard[y - i][x + i] == 0) {
                        g.fillRect((x + i) * cellSide + border, (y - i) * cellSide + border, cellSide, cellSide);
                        result[x + i][y - i] = true;
                    } else if (chessBoard[y - i][x + i] > 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect((x + i) * cellSide + border, (y - i) * cellSide + border, cellSide, cellSide);
                        result[x + i][y - i] = true;
                        break;
                    } else
                        break;
                }

                // Move to bottom right
                g.setColor(new Color(0, 255, 0, 127));
                for (int i = 1; ((x + i) >= 0) && ((y + i) >= 0) && ((x + i) < 8) && ((y + i) < 8); i++) {
                    if (chessBoard[y + i][x + i] == 0) {
                        g.fillRect((x + i) * cellSide + border, (y + i) * cellSide + border, cellSide, cellSide);
                        result[x + i][y + i] = true;
                    } else if (chessBoard[y + i][x + i] > 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect((x + i) * cellSide + border, (y + i) * cellSide + border, cellSide, cellSide);
                        result[x + i][y + i] = true;
                        break;
                    } else
                        break;
                }

                // Move to bottom left
                g.setColor(new Color(0, 255, 0, 127));
                for (int i = 1; ((x - i) >= 0) && ((y + i) >= 0) && ((x - i) < 8) && ((y + i) < 8); i++) {
                    if (chessBoard[y + i][x - i] == 0) {
                        g.fillRect((x - i) * cellSide + border, (y + i) * cellSide + border, cellSide, cellSide);
                        result[x - i][y + i] = true;
                    } else if (chessBoard[y + i][x - i] > 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect((x - i) * cellSide + border, (y + i) * cellSide + border, cellSide, cellSide);
                        result[x - i][y + i] = true;
                        break;
                    } else
                        break;
                }
                break;

            // ---------------- QUEEN ----------------
            case 15:

                for (int i = x - 1; i >= 0; i--) {
                    if (chessBoard[y][i] == 0) {
                        g.fillRect(i * cellSide + border, y * cellSide + border, cellSide, cellSide);
                        result[i][y] = true;
                    } else if (chessBoard[y][i] < 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect(i * cellSide + border, y * cellSide + border, cellSide, cellSide);
                        result[i][y] = true;
                        break;
                    } else
                        break;
                }

                // Move to right
                g.setColor(new Color(0, 255, 0, 127));
                for (int i = x + 1; i < 8; i++) {
                    if (chessBoard[y][i] == 0) {
                        g.fillRect(i * cellSide + border, y * cellSide + border, cellSide, cellSide);
                        result[i][y] = true;

                    } else if (chessBoard[y][i] < 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect(i * cellSide + border, y * cellSide + border, cellSide, cellSide);
                        result[i][y] = true;

                        break;
                    } else
                        break;
                }

                // Move to top
                g.setColor(new Color(0, 255, 0, 127));
                for (int i = y - 1; i >= 0; i--) {
                    if (chessBoard[i][x] == 0) {
                        g.fillRect(x * cellSide + border, i * cellSide + border, cellSide, cellSide);
                        result[x][i] = true;

                    } else if (chessBoard[i][x] < 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect(x * cellSide + border, i * cellSide + border, cellSide, cellSide);
                        result[x][i] = true;

                        break;
                    } else
                        break;
                }

                // Move to bottom
                g.setColor(new Color(0, 255, 0, 127));
                for (int i = y + 1; i < 8; i++) {
                    if (chessBoard[i][x] == 0) {
                        g.fillRect(x * cellSide + border, i * cellSide + border, cellSide, cellSide);
                        result[x][i] = true;

                    } else if (chessBoard[i][x] < 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect(x * cellSide + border, i * cellSide + border, cellSide, cellSide);
                        result[x][i] = true;

                        break;
                    } else
                        break;
                }

                // Move to top left
                g.setColor(new Color(0, 255, 0, 127));
                for (int i = 1; ((x - i) >= 0) && ((y - i) >= 0) && ((x - i) < 8) && ((y - i) < 8); i++) {
                    if (chessBoard[y - i][x - i] == 0) {
                        g.fillRect((x - i) * cellSide + border, (y - i) * cellSide + border, cellSide, cellSide);
                        result[x - i][y - i] = true;

                    } else if (chessBoard[y - i][x - i] < 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect((x - i) * cellSide + border, (y - i) * cellSide + border, cellSide, cellSide);
                        result[x - i][y - i] = true;
                        break;
                    } else
                        break;
                }

                // Move to top right
                g.setColor(new Color(0, 255, 0, 127));
                for (int i = 1; ((x + i) >= 0) && ((y - i) >= 0) && ((x + i) < 8) && ((y - i) < 8); i++) {
                    if (chessBoard[y - i][x + i] == 0) {
                        g.fillRect((x + i) * cellSide + border, (y - i) * cellSide + border, cellSide, cellSide);
                        result[x + i][y - i] = true;
                    } else if (chessBoard[y - i][x + i] < 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect((x + i) * cellSide + border, (y - i) * cellSide + border, cellSide, cellSide);
                        result[x + i][y - i] = true;
                        break;
                    } else
                        break;
                }

                // Move to bottom right
                g.setColor(new Color(0, 255, 0, 127));
                for (int i = 1; ((x + i) >= 0) && ((y + i) >= 0) && ((x + i) < 8) && ((y + i) < 8); i++) {
                    if (chessBoard[y + i][x + i] == 0) {
                        g.fillRect((x + i) * cellSide + border, (y + i) * cellSide + border, cellSide, cellSide);
                        result[x + i][y + i] = true;
                    } else if (chessBoard[y + i][x + i] < 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect((x + i) * cellSide + border, (y + i) * cellSide + border, cellSide, cellSide);
                        result[x + i][y + i] = true;
                        break;
                    } else
                        break;
                }

                // Move to bottom left
                g.setColor(new Color(0, 255, 0, 127));
                for (int i = 1; ((x - i) >= 0) && ((y + i) >= 0) && ((x - i) < 8) && ((y + i) < 8); i++) {
                    if (chessBoard[y + i][x - i] == 0) {
                        g.fillRect((x - i) * cellSide + border, (y + i) * cellSide + border, cellSide, cellSide);
                        result[x - i][y + i] = true;
                    } else if (chessBoard[y + i][x - i] < 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect((x - i) * cellSide + border, (y + i) * cellSide + border, cellSide, cellSide);
                        result[x - i][y + i] = true;
                        break;
                    } else
                        break;
                }

                break;

            case 5:

                // Move to left
                for (int i = x - 1; i >= 0; i--) {
                    if (chessBoard[y][i] == 0) {
                        g.fillRect(i * cellSide + border, y * cellSide + border, cellSide, cellSide);
                        result[i][y] = true;
                    } else if (chessBoard[y][i] > 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect(i * cellSide + border, y * cellSide + border, cellSide, cellSide);
                        result[i][y] = true;
                        break;
                    } else
                        break;
                }

                // Move to right
                g.setColor(new Color(0, 255, 0, 127));
                for (int i = x + 1; i < 8; i++) {
                    if (chessBoard[y][i] == 0) {
                        g.fillRect(i * cellSide + border, y * cellSide + border, cellSide, cellSide);
                        result[i][y] = true;
                    } else if (chessBoard[y][i] > 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect(i * cellSide + border, y * cellSide + border, cellSide, cellSide);
                        result[i][y] = true;
                        break;
                    } else
                        break;
                }

                // Move to top
                g.setColor(new Color(0, 255, 0, 127));
                for (int i = y - 1; i >= 0; i--) {
                    if (chessBoard[i][x] == 0) {
                        g.fillRect(x * cellSide + border, i * cellSide + border, cellSide, cellSide);
                        result[x][i] = true;
                    } else if (chessBoard[i][x] > 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect(x * cellSide + border, i * cellSide + border, cellSide, cellSide);
                        result[x][i] = true;
                        break;
                    } else
                        break;
                }

                // Move to bottom
                g.setColor(new Color(0, 255, 0, 127));
                for (int i = y + 1; i < 8; i++) {
                    if (chessBoard[i][x] == 0) {
                        g.fillRect(x * cellSide + border, i * cellSide + border, cellSide, cellSide);
                        result[x][i] = true;
                    } else if (chessBoard[i][x] > 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect(x * cellSide + border, i * cellSide + border, cellSide, cellSide);
                        result[x][i] = true;
                        break;
                    } else
                        break;
                }

                // Move to top left
                g.setColor(new Color(0, 255, 0, 127));
                for (int i = 1; ((x - i) >= 0) && ((y - i) >= 0) && ((x - i) < 8) && ((y - i) < 8); i++) {
                    if (chessBoard[y - i][x - i] == 0) {
                        g.fillRect((x - i) * cellSide + border, (y - i) * cellSide + border, cellSide, cellSide);
                        result[x - i][y - i] = true;
                    } else if (chessBoard[y - i][x - i] > 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect((x - i) * cellSide + border, (y - i) * cellSide + border, cellSide, cellSide);
                        result[x - i][y - i] = true;
                        break;
                    } else
                        break;
                }

                // Move to top right
                g.setColor(new Color(0, 255, 0, 127));
                for (int i = 1; ((x + i) >= 0) && ((y - i) >= 0) && ((x + i) < 8) && ((y - i) < 8); i++) {
                    if (chessBoard[y - i][x + i] == 0) {
                        g.fillRect((x + i) * cellSide + border, (y - i) * cellSide + border, cellSide, cellSide);
                        result[x + i][y - i] = true;
                    } else if (chessBoard[y - i][x + i] > 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect((x + i) * cellSide + border, (y - i) * cellSide + border, cellSide, cellSide);
                        result[x + i][y - i] = true;
                        break;
                    } else
                        break;
                }

                // Move to bottom right
                g.setColor(new Color(0, 255, 0, 127));
                for (int i = 1; ((x + i) >= 0) && ((y + i) >= 0) && ((x + i) < 8) && ((y + i) < 8); i++) {
                    if (chessBoard[y + i][x + i] == 0) {
                        g.fillRect((x + i) * cellSide + border, (y + i) * cellSide + border, cellSide, cellSide);
                        result[x + i][y + i] = true;
                    } else if (chessBoard[y + i][x + i] > 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect((x + i) * cellSide + border, (y + i) * cellSide + border, cellSide, cellSide);
                        result[x + i][y + i] = true;
                        break;
                    } else
                        break;
                }

                // Move to bottom left
                g.setColor(new Color(0, 255, 0, 127));
                for (int i = 1; ((x - i) >= 0) && ((y + i) >= 0) && ((x - i) < 8) && ((y + i) < 8); i++) {
                    if (chessBoard[y + i][x - i] == 0) {
                        g.fillRect((x - i) * cellSide + border, (y + i) * cellSide + border, cellSide, cellSide);
                        result[x - i][y + i] = true;
                    } else if (chessBoard[y + i][x - i] > 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect((x - i) * cellSide + border, (y + i) * cellSide + border, cellSide, cellSide);
                        result[x - i][y + i] = true;
                        break;
                    } else
                        break;
                }

                break;

            case 16:
                // NEXT_STEP
                if (y - 1 >= 0) {
                    if (chessBoard[y - 1][x] == 0) {
                        g.fillRect(x * cellSide + border, (y - 1) * cellSide + border, cellSide, cellSide);
                        result[x][y - 1] = true;
                    }
                }
                if (y + 1 < 8) {
                    if (chessBoard[y + 1][x] == 0) {
                        g.fillRect(x * cellSide + border, (y + 1) * cellSide + border, cellSide, cellSide);
                        result[x][y + 1] = true;
                    }
                }
                if (x - 1 >= 0) {
                    if (chessBoard[y][x - 1] == 0) {
                        g.fillRect((x - 1) * cellSide + border, y * cellSide + border, cellSide, cellSide);
                        result[x - 1][y] = true;
                    }
                }
                if (x + 1 < 8) {
                    if (chessBoard[y][x + 1] == 0) {
                        g.fillRect((x + 1) * cellSide + border, y * cellSide + border, cellSide, cellSide);
                        result[x + 1][y] = true;
                    }
                }

                if (x - 1 >= 0) {
                    if (y - 1 >= 0) {
                        if (chessBoard[y - 1][x - 1] == 0) {
                            g.fillRect((x - 1) * cellSide + border, (y - 1) * cellSide + border, cellSide, cellSide);
                            result[x - 1][y - 1] = true;
                        }
                    }
                    if (y + 1 < 8) {
                        if (chessBoard[y + 1][x - 1] == 0) {
                            g.fillRect((x - 1) * cellSide + border, (y + 1) * cellSide + border, cellSide, cellSide);
                            result[x - 1][y + 1] = true;
                        }
                    }
                }

                if (x + 1 < 8) {
                    if (y - 1 >= 0) {
                        if (chessBoard[y - 1][x + 1] == 0) {
                            g.fillRect((x + 1) * cellSide + border, (y - 1) * cellSide + border, cellSide, cellSide);
                            result[x + 1][y + 1] = true;
                        }
                    }
                    if (y + 1 < 8) {
                        if (chessBoard[y + 1][x + 1] == 0) {
                            g.fillRect((x + 1) * cellSide + border, (y + 1) * cellSide + border, cellSide, cellSide);
                            result[x + 1][y + 1] = true;
                        }
                    }
                }

                // NEXT_KILL

                if (y - 1 >= 0) {
                    if (chessBoard[y - 1][x] != 0 && chessBoard[y - 1][x] < 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect(x * cellSide + border, (y - 1) * cellSide + border, cellSide, cellSide);
                        result[x][y - 1] = true;
                    }
                }
                if (y + 1 < 8) {
                    if (chessBoard[y + 1][x] != 0 && chessBoard[y + 1][x] < 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect(x * cellSide + border, (y + 1) * cellSide + border, cellSide, cellSide);
                        result[x][y + 1] = true;
                    }
                }
                if (x - 1 >= 0) {
                    if (chessBoard[y][x - 1] != 0 && chessBoard[y][x - 1] < 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect((x - 1) * cellSide + border, y * cellSide + border, cellSide, cellSide);
                        result[x - 1][y] = true;
                    }
                }
                if (x + 1 < 8) {
                    if (chessBoard[y][x + 1] != 0 && chessBoard[y][x + 1] < 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect((x + 1) * cellSide + border, y * cellSide + border, cellSide, cellSide);
                        result[x + 1][y] = true;
                    }
                }

                if (x - 1 >= 0) {
                    if (y - 1 >= 0) {
                        if (chessBoard[y - 1][x - 1] != 0 && chessBoard[y - 1][x - 1] < 7) {
                            g.setColor(new Color(230, 0, 0, 180));
                            g.fillRect((x - 1) * cellSide + border, (y - 1) * cellSide + border, cellSide, cellSide);
                            result[x - 1][y - 1] = true;
                        }
                    }
                    if (y + 1 < 8) {
                        if (chessBoard[y + 1][x - 1] != 0 && chessBoard[y + 1][x - 1] < 7) {
                            g.setColor(new Color(230, 0, 0, 180));
                            g.fillRect((x - 1) * cellSide + border, (y + 1) * cellSide + border, cellSide, cellSide);
                            result[x - 1][y + 1] = true;
                        }
                    }
                }

                if (x + 1 < 8) {
                    if (y - 1 >= 0) {
                        if (chessBoard[y - 1][x + 1] != 0 && chessBoard[y - 1][x + 1] < 7) {
                            g.setColor(new Color(230, 0, 0, 180));
                            g.fillRect((x + 1) * cellSide + border, (y - 1) * cellSide + border, cellSide, cellSide);
                            result[x - 1][y - 1] = true;
                        }
                    }
                    if (y + 1 < 8) {
                        if (chessBoard[y + 1][x + 1] != 0 && chessBoard[y + 1][x + 1] < 7) {
                            g.setColor(new Color(230, 0, 0, 180));
                            g.fillRect((x + 1) * cellSide + border, (y + 1) * cellSide + border, cellSide, cellSide);
                            result[x + 1][y + 1] = true;
                        }
                    }
                }

                break;

            case 6:
                // NEXT_STEP
                if (y - 1 >= 0) {
                    if (chessBoard[y - 1][x] == 0) {
                        g.fillRect(x * cellSide + border, (y - 1) * cellSide + border, cellSide, cellSide);
                        result[x][y - 1] = true;
                    }
                }
                if (y + 1 < 8) {
                    if (chessBoard[y + 1][x] == 0) {
                        g.fillRect(x * cellSide + border, (y + 1) * cellSide + border, cellSide, cellSide);
                        result[x][y + 1] = true;
                    }
                }
                if (x - 1 >= 0) {
                    if (chessBoard[y][x - 1] == 0) {
                        g.fillRect((x - 1) * cellSide + border, y * cellSide + border, cellSide, cellSide);
                        result[x - 1][y] = true;
                    }
                }
                if (x + 1 < 8) {
                    if (chessBoard[y][x + 1] == 0) {
                        g.fillRect((x + 1) * cellSide + border, y * cellSide + border, cellSide, cellSide);
                        result[x + 1][y] = true;
                    }
                }

                if (x - 1 >= 0) {
                    if (y - 1 >= 0) {
                        if (chessBoard[y - 1][x - 1] == 0) {
                            g.fillRect((x - 1) * cellSide + border, (y - 1) * cellSide + border, cellSide, cellSide);
                            result[x - 1][y - 1] = true;
                        }
                    }
                    if (y + 1 < 8) {
                        if (chessBoard[y + 1][x - 1] == 0) {
                            g.fillRect((x - 1) * cellSide + border, (y + 1) * cellSide + border, cellSide, cellSide);
                            result[x - 1][y + 1] = true;
                        }
                    }
                }

                if (x + 1 < 8) {
                    if (y - 1 >= 0) {
                        if (chessBoard[y - 1][x + 1] == 0) {
                            g.fillRect((x + 1) * cellSide + border, (y - 1) * cellSide + border, cellSide, cellSide);
                            result[x + 1][y + 1] = true;
                        }
                    }
                    if (y + 1 < 8) {
                        if (chessBoard[y + 1][x + 1] == 0) {
                            g.fillRect((x + 1) * cellSide + border, (y + 1) * cellSide + border, cellSide, cellSide);
                            result[x + 1][y + 1] = true;
                        }
                    }
                }

                // NEXT_KILL
                if (y - 1 >= 0) {
                    if (chessBoard[y - 1][x] != 0 && chessBoard[y - 1][x] > 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect(x * cellSide + border, (y - 1) * cellSide + border, cellSide, cellSide);
                        result[x][y - 1] = true;
                    }
                }
                if (y + 1 < 8) {
                    if (chessBoard[y + 1][x] != 0 && chessBoard[y + 1][x] > 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect(x * cellSide + border, (y + 1) * cellSide + border, cellSide, cellSide);
                        result[x][y + 1] = true;
                    }
                }
                if (x - 1 >= 0) {
                    if (chessBoard[y][x - 1] != 0 && chessBoard[y][x - 1] > 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect((x - 1) * cellSide + border, y * cellSide + border, cellSide, cellSide);
                        result[x - 1][y] = true;
                    }
                }
                if (x + 1 < 8) {
                    if (chessBoard[y][x + 1] != 0 && chessBoard[y][x + 1] > 7) {
                        g.setColor(new Color(230, 0, 0, 180));
                        g.fillRect((x + 1) * cellSide + border, y * cellSide + border, cellSide, cellSide);
                        result[x + 1][y] = true;
                    }
                }

                if (x - 1 >= 0) {
                    if (y - 1 >= 0) {
                        if (chessBoard[y - 1][x - 1] != 0 && chessBoard[y - 1][x - 1] > 7) {
                            g.setColor(new Color(230, 0, 0, 180));
                            g.fillRect((x - 1) * cellSide + border, (y - 1) * cellSide + border, cellSide, cellSide);
                            result[x - 1][y - 1] = true;
                        }
                    }
                    if (y + 1 < 8) {
                        if (chessBoard[y + 1][x - 1] != 0 && chessBoard[y + 1][x - 1] > 7) {
                            g.setColor(new Color(230, 0, 0, 180));
                            g.fillRect((x - 1) * cellSide + border, (y + 1) * cellSide + border, cellSide, cellSide);
                            result[x - 1][y + 1] = true;
                        }
                    }
                }

                if (x + 1 < 8) {
                    if (y - 1 >= 0) {
                        if (chessBoard[y - 1][x + 1] != 0 && chessBoard[y - 1][x + 1] > 7) {
                            g.setColor(new Color(230, 0, 0, 180));
                            g.fillRect((x + 1) * cellSide + border, (y - 1) * cellSide + border, cellSide, cellSide);
                            result[x - 1][y - 1] = true;
                        }
                    }
                    if (y + 1 < 8) {
                        if (chessBoard[y + 1][x + 1] != 0 && chessBoard[y + 1][x + 1] > 7) {
                            g.setColor(new Color(230, 0, 0, 180));
                            g.fillRect((x + 1) * cellSide + border, (y + 1) * cellSide + border, cellSide, cellSide);
                            result[x + 1][y + 1] = true;
                        }
                    }
                }

                break;

            default:
                break;
        }
        return result;
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
                // System.out.println("-> none");
            } else if (xCurrent != -1 && yCurrent != -1 && chessBoard[yCurrent][xCurrent] != 0
                    && nextStep(xCurrent, yCurrent, (Graphics2D) getGraphics())[(e.getX() - border)
                            / cellSide][(e.getY() - border) / cellSide]) {
                moveChessPieces((e.getX() - border) / cellSide, (e.getY() - border) / cellSide);
            } else if (e.getX() >= border && e.getX() < border + cellSide * 8 && e.getY() >= border
                    && e.getY() < border + cellSide * 8) {
                xCurrent = (e.getX() - border) / cellSide;
                yCurrent = (e.getY() - border) / cellSide;
                // System.out.println("-> "+((char)(xCurrent+65)) + "" + (yCurrent+1));
            }
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
