import ch.schmarcel.ConsoleUtil.ParameterCompiler.ParameterCompiler;

import java.io.IOException;

public class Server_Main {
    public static void main(String[] args) {
        try {
            ParameterCompiler pars = new ParameterCompiler(args, '/');
            int port = pars.getInt("p", 27007);
            Field.pWidth = pars.getInt("w", 25);
            Field.pHeight = pars.getInt("h", 20);
            new Server(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
