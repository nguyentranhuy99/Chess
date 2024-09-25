package piece;

import button.NextMove;
import main.GamePanel;
import main.MouseHandle;
import main.Panel;
import pair.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public abstract class ChessMan {
    GamePanel panel;
    BufferedImage image;
    String name;
    public MouseHandle mouseHandle;
    public int x, y, i, j;
    public int value;
    public boolean button;
    public boolean alive;
    public ArrayList<Pair<Integer, Integer>> moves = new ArrayList<>();
    public ArrayList<NextMove> nextMoves = new ArrayList<>();


    public ChessMan(GamePanel panel, int x, int y) {
        this.panel = panel;
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (panel.Board[i][j] == this.value){
                    this.i = i;
                }
            }
        }
        this.x = x;
        this.y = y;
        this.j = (this.x / panel.tileSize) - 4;
        this.i = (this.y / panel.tileSize) - 2;
        setValue();
        panel.Board[i][j] = panel.turn * value;
        this.mouseHandle = new MouseHandle(x, y, panel.tileSize, panel.tileSize);
        panel.addMouseListener(mouseHandle);
        panel.addMouseMotionListener(mouseHandle);
        setImageName();
        getImage();
    }

    public abstract void setImageName();
    public abstract void setValue();

    public void getImage(){
        try {
            image = ImageIO.read((getClass().getResourceAsStream("/image/" + name + ".png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buttonUpdate() {
        if (mouseHandle.click) {
            button = true;
            for (Pair<Integer, Integer> move : moves){
                nextMoves.add(new NextMove(this.panel, move.first, move.second, panel.tileSize, panel.tileSize));
            }
            mouseHandle.click = false;
        }
    }

    public abstract void functionUpdate();

    public void update(){
        buttonUpdate();
        functionUpdate();
        for (NextMove move : nextMoves){
            move.update();
            if (move.button){
                panel.Board[i][j] = 0;
                this.x = move.x;
                this.y = move.y;
                this.j = (this.x / panel.tileSize) - 4;
                this.i = (this.y / panel.tileSize) - 2;
                panel.Board[i][j] = panel.turn * value;
                this.mouseHandle = new MouseHandle(this.x, this.y, panel.tileSize, panel.tileSize);
                this.panel.addMouseListener(mouseHandle);
                this.panel.addMouseMotionListener(mouseHandle);
                this.button = false;
                nextMoves = new ArrayList<>();
                moves = new ArrayList<>();
            }
        }
    }

    public void draw(Graphics2D g2D) {
        g2D.drawImage(image, x, y, panel.tileSize, panel.tileSize, null);
        for (NextMove move : nextMoves){
            move.draw(g2D);
        }
    }
}
