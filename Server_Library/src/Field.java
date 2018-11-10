import ch.schmarcel.MessageData.MessageData;
import ch.schmarcel.Connection.Sendable;
import ch.schmarcel.Server.ClientList;

class Field implements Sendable{
    private ClientList clientList;
    public static int pWidth = 30, pHeight = 30;
    private int[][] field = new int[pWidth][pHeight];

    Field(ClientList clientList) {
        this.clientList = clientList;
    }

    void draw(int x, int y, int player) {
        if (x >= pWidth) x = pWidth - 1;
        if (y >= pHeight) y = pHeight - 1;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        field[x][y] = player;
        clientList.writeToAll(new MessageData("draw").set("x", x).set("y", y).set("id", player));
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < pHeight; i++) {
            for (int k = 0; k < pWidth; k++) {
                b.append(field[k][i]).append(",");
            }
            b.replace(b.length()-1, b.length(), ";");
        }
        b.delete(b.length()-1, b.length());
        return b.toString();
    }

    @Override
    public MessageData toMessageData() {
        MessageData data = new MessageData("field");
        data.set("width", pWidth);
        data.set("height", pHeight);
        data.set("field", toString());
        return data;
    }

    void clean(int id) {
        for (int y = 0; y < pHeight; y++) {
            for (int x = 0; x < pWidth; x++) {
                if (field[x][y] == id)
                    cleanField(x, y);
            }
        }
    }

    void cleanAll() {
        for (int y = 0; y < pHeight; y++) {
            for (int x = 0; x < pWidth; x++) {
                cleanField(x, y);
            }
        }
    }

    void cleanField(int x, int y) {
        draw(x, y, 0);
    }

    void cleanRow(int y) {
        for (int x = 0; x < pWidth; x++) {
            cleanField(x, y);
        }
    }

    void cleanColumn(int x) {
        for (int y = 0; y < pWidth; y++) {
            cleanField(x, y);
        }
    }
}
