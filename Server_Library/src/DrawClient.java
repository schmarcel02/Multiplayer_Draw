import ch.schmarcel.MessageData.MessageData;
import ch.schmarcel.Server.Client;
import ch.schmarcel.Server.ClientList;

import java.io.IOException;
import java.net.Socket;

public class DrawClient extends Client {
    private Field field;
    private double r, b, g;
    boolean isReady = false;

    DrawClient(Socket socket, ClientList clientList, Field field) throws IOException {
        super(socket, clientList);
        this.field = field;
        connection.start();
    }

    @Override
    public void setEvents() {
        connection.on("requestdata", data -> {
            connection.send(new MessageData("me").set("id", id));
            connection.send(field);
            Client[] clients = clientList.getClientsAsArray();
            for (Client client : clients) {
                DrawClient c = (DrawClient) client;
                if (c.isReady)
                    connection.send(new MessageData("player").set("id", c.id).set("r", c.r).set("g", c.g).set("b", c.b));
            }
        });
        connection.on("player", data -> {
            r = data.getDouble("r");
            g = data.getDouble("g");
            b = data.getDouble("b");
            clientList.writeToAllExceptSender(this, data.set("id", id));
            connection.send(new MessageData("confcol"));
            isReady = true;
        });
        connection.on("draw", data -> {
            field.draw(data.getInt("x"), data.getInt("y"), id);
        });
        connection.on("delete", data -> {
            field.cleanField(data.getInt("x"), data.getInt("y"));
        });
    }
}
