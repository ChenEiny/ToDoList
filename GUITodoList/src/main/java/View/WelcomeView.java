package View;

import ClientSetup.Client;
import Controller.ReportViewController;
import Controller.WelcomeViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeView extends Application {
    private static Stage primaryStage;
    private static Scene welcomeScene;
    private static Scene reportScene;

    private static Client client; // Static client instance

    @Override
    public void start(Stage primaryStage) throws Exception {
        WelcomeView.primaryStage = primaryStage;

        FXMLLoader welcomeLoader = new FXMLLoader(getClass().getResource("/WelcomeView.fxml"));
        Parent welcomeRoot = welcomeLoader.load();
        WelcomeViewController welcomeController = welcomeLoader.getController();
        welcomeScene = new Scene(welcomeRoot);

        FXMLLoader reportLoader = new FXMLLoader(getClass().getResource("/ReportView.fxml"));
        Parent reportRoot = reportLoader.load();
        ReportViewController reportController = reportLoader.getController();
        reportScene = new Scene(reportRoot);

        primaryStage.setTitle("Task Management");
        primaryStage.setScene(welcomeScene);
        primaryStage.show();

        // Initialize and connect to the server
        initializeClient();
        if (client != null) {
            welcomeController.setClient(client);
            reportController.setClient(client);
        }
    }

    private void initializeClient() {
        try {
            client = new Client("localhost", 12345); // Update the server address and port if needed
            client.connect();
            System.out.println("Client connected to server.");
        } catch (IOException e) {
            System.err.println("Failed to connect to server: " + e.getMessage());
        }
    }

    public static Client getClient() {
        return client;
    }

    public static void switchToReportView() {
        primaryStage.setScene(reportScene);
    }

    public static void switchToWelcomeView() {
        primaryStage.setScene(welcomeScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
