import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

class GameMenu extends Stage{
    private Field field;

    GameMenu() {
        Connection.setGameMenu(this);
        setOnCloseRequest(t -> {
            Connection.close();
        });
        createField();
    }

    private VBox layout() {
        VBox layout = new VBox();
        layout.getChildren().add(field);
        return layout;
    }

    private void createField() {
        field = new Field();
        field.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY)
                field.draw(e.getX(), e.getY());
            else if (e.getButton() == MouseButton.SECONDARY)
                field.delete(e.getX(), e.getY());
            field.repaint();
        });
        field.setOnMouseDragged(e -> {
            if (e.getButton() == MouseButton.PRIMARY)
                field.draw(e.getX(), e.getY());
            else if (e.getButton() == MouseButton.SECONDARY)
                field.delete(e.getX(), e.getY());
            field.repaint();
        });
        Connection.setField(field);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Connection.requestData();
    }

    void showWindow() {
        Platform.runLater(() -> {
            Scene scene = new Scene(layout());
            setScene(scene);
            setResizable(false);
            sizeToScene();
            show();
        });
    }
}
