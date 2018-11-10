import ch.schmarcel.ConsoleUtil.ParameterCompiler.ParameterCompiler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenu extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ParameterCompiler pars = new ParameterCompiler(getParameters().getRaw(), '/');
        String ip = pars.getString("ip", "schmarcel.internet-box.ch");
        int port = pars.getInt("p", 27007);
        Field.pSize = pars.getInt("s", 30);
        try {
            System.out.println("IP: " + ip + " Port: " + port);
            Connection.start(ip, port);
            new LoginMenu();
        } catch (IOException e) {
            e.printStackTrace();
            Platform.exit();
        }
    }
}
