import ch.schmarcel.MessageData.MessageData;
import ch.schmarcel.ConsoleUtil.CommandListener.Command;
import ch.schmarcel.ConsoleUtil.CommandListener.CommandListener;
import ch.schmarcel.Server.Client;
import ch.schmarcel.Server.ClientList;
import ch.schmarcel.Server.Listener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
    private ClientList clientList;
    private Listener listener;
    private Field field;

    Server(int port) throws IOException {
        clientList = new ClientList() {
            @Override
            public void writeToAllExceptSender(Client sender, MessageData data) {
                Client[] clients = this.getClientsAsArray();

                for(int i = 0; i < clients.length; ++i) {
                    Client c = clients[i];
                    if (sender.id != c.id && ((DrawClient)c).isReady) {
                        c.write(data);
                    }
                }
            }
        };
        field = new Field(clientList);
        clientList.setOnAfterClientAdd(client -> System.out.println("Client joined, was assigned id " + client.id));
        clientList.setOnAfterClientRemove(client -> System.out.println("Client " + client.id + " left"));
        clientList.setOnBeforeClientRemove(client -> {
            field.clean(client.id);
            clientList.writeToAll(field.toMessageData());
        });
        ServerSocket ss =  new ServerSocket(port);
        listener = new Listener(clientList, ss) {
            @Override
            public Client createClient(Socket socket, ClientList clientList) throws IOException {
                return new DrawClient(socket, clientList, field);
            }
        };
        listener.start();

        CommandListener cl = new CommandListener(System.in, '/');
        cl.addCommand("stop", new Command(new String[]{}, new String[]{"force"}, args -> {
            try {
                listener.stop();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (args.hasArgument("force"))
                clientList.forceKickAll();
            else
                clientList.kickAll();
        }));
        cl.addCommand("clear", new Command(new String[]{}, new String[]{"x", "y"}, args -> {
            if (args.hasArgument("x") && args.hasArgument("y"))
                field.cleanField(Integer.valueOf(args.getArgument("x")), Integer.valueOf(args.getArgument("y")));
            else if (args.hasArgument("x"))
                field.cleanColumn(Integer.valueOf(args.getArgument("x")));
            else if (args.hasArgument("y"))
                field.cleanRow(Integer.valueOf(args.getArgument("y")));
            else
                field.cleanAll();
        }));
        cl.addCommand("kick", new Command(new String[]{"id"}, new String[]{"force", "all"}, args -> {
            if (args.hasArgument("force"))
                clientList.forceKick(Integer.valueOf(args.getArgument("id")));
            else
                clientList.kick(Integer.valueOf(args.getArgument("id")));
        }));
        cl.addCommand("kickall", new Command(new String[]{}, new String[]{"force"}, args -> {
            if (args.hasArgument("force"))
                clientList.forceKickAll();
            else
                clientList.kickAll();
        }));
        cl.start();
    }
}
