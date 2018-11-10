import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

class LoginMenu extends Stage{
    private ColorPicker colorPicker;

    LoginMenu() {
        Connection.setLoginMenu(this);
        Scene scene = new Scene(layout());
        setScene(scene);
        show();
    }

    private VBox layout() {
        VBox layout = new VBox();
        layout.setPrefSize(600, 400);
        layout.setAlignment(Pos.CENTER);
        layout.setFillWidth(false);
        layout.getChildren().addAll(colorPicker(), btnLogin());
        return layout;
    }

    private ColorPicker colorPicker() {
        colorPicker = new ColorPicker();
        colorPicker.setPrefWidth(200);
        colorPicker.setPromptText("Color");
        return colorPicker;
    }

    private Button btnLogin() {
        Button btnLogin = new Button("Login");
        btnLogin.setPrefWidth(200);
        btnLogin.setOnAction(event -> {
            Connection.sendColor(colorPicker.getValue());
        });
        return btnLogin;
    }

    void proceed() {
        Platform.runLater(() -> {
            new GameMenu();
            close();
        });
    }
}
