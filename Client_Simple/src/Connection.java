import ch.schmarcel.Connection.MessageConnection;
import ch.schmarcel.MessageData.MessageData;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.Socket;

class Connection {
    private static MessageConnection connection;

    private static LoginMenu loginMenu;
    private static GameMenu gameMenu;
    private static Field field;

    static void setLoginMenu(LoginMenu loginMenu) {
        Connection.loginMenu = loginMenu;
    }

    static void setGameMenu(GameMenu gameMenu) {
        Connection.gameMenu = gameMenu;
    }

    static void setField(Field field) {
        Connection.field = field;
    }

    static void start(String ip, int port) throws IOException {
        connection = new MessageConnection(new Socket(ip, port));
        connection.on("player", data -> {
            field.addColor(data.getInt("id"), new Color(data.getDouble("r"), data.getDouble("g"), data.getDouble("b"), 1));
        });
        connection.on("draw", data -> {
            field.setPixel(data.getInt("x"), data.getInt("y"), data.getInt("id"));
            field.repaint();
        });
        connection.on("field", data -> {
            field.setData(data);
            field.repaint();
            gameMenu.showWindow();
        });
        connection.on("me", data -> {
            field.setMyId(data.getInt("id"));
        });
        connection.on("confcol", data -> {
            loginMenu.proceed();
        });
        connection.start();
    }

    static void sendColor(Color color) {
        connection.send(new MessageData("player").set("r", color.getRed()).set("g", color.getGreen()).set("b", color.getBlue()));
    }

    static void sendDraw(int x, int y) {
        connection.send(new MessageData("draw").set("x", x).set("y", y));
    }

    static void sendDelete(int x, int y) {
        connection.send(new MessageData("delete").set("x", x).set("y", y));
    }

    static void requestData() {
        connection.send(new MessageData("requestdata"));
    }

    static void close() {
        connection.requestStop();
    }
}
