import ch.schmarcel.MessageData.MessageData;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.HashMap;

class Field extends Canvas{
    static int pSize;
    private int pWidth, pHeight;
    private int absWidth, absHeight;
    private int[][] field;
    private HashMap<Integer, Color> colors = new HashMap<>();
    private int myId;

    Field() {
        colors.put(0, Color.WHITE);
    }

    void setData(MessageData data) {
        pWidth = data.getInt("width");
        pHeight = data.getInt("height");
        absWidth = pWidth*pSize;
        absHeight = pHeight*pSize;
        setWidth(absWidth);
        setHeight(absHeight);
        field = new int[pWidth][pHeight];
        String fieldData = data.getString("field");
        String[] rows = fieldData.split(";");
        for (int y = 0; y < pHeight; y++) {
            String[] fields = rows[y].split(",");
            for (int x = 0; x < pWidth; x++) {
                field[x][y] = Integer.valueOf(fields[x]);
            }
        }
    }

    void addColor(int id, Color color) {
        colors.put(id, color);
    }

    void setMyId(int myId) {
        this.myId = myId;
    }

    void draw(double x, double y) {
        int px = getPixelX(x);
        int py = getPixelY(y);
        if (field[px][py] != myId) {
            Connection.sendDraw(px, py);
            setPixel(px, py, myId);
        }
    }

    void delete(double x, double y) {
        int px = getPixelX(x);
        int py = getPixelY(y);
        if (field[px][py] != 0)
            Connection.sendDelete(px,py);
    }

    private int getPixelX(double x) {
        int px = (int) Math.floor(x/Field.pSize);
        if (px >= pWidth) return pWidth - 1;
        if (px < 0) return 0;
        return px;
    }

    private int getPixelY(double y) {
        int py = (int) Math.floor(y/Field.pSize);
        if (py >= pHeight) return pHeight - 1;
        if (py < 0) return 0;
        return py;
    }

    void setPixel(int x, int y, int id) {
        field[x][y] = id;
    }

    void repaint() {
        Platform.runLater(() -> {
            GraphicsContext g = getGraphicsContext2D();
            g.clearRect(0, 0, getWidth(), getHeight());
            for (int y = 0; y < pHeight; y++) {
                for (int x = 0; x < pWidth; x++) {
                    g.setFill(colors.get(field[x][y]));
                    g.fillRect(x*pSize, y*pSize, pSize, pSize);
                }
            }
            g.setFill(Color.BLACK);
            for (int x = 0; x <= pWidth; x++) {
                g.strokeLine(x*pSize, 0, x*pSize, absHeight);
            }
            for (int y = 0; y <= pHeight; y++) {
                g.strokeLine(0, y*pSize, absWidth, y*pSize);
            }
        });
    }
}
