package net.justapie.majutsu.new_gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class UIManager extends Application {

    // Object states are declared here. If you move it, you are gay.

    // Login states.
    private Stage loginStage;
    private Component loginScene;
    protected LoginConsole loginConsole;

    // Dashboard states.
    private Stage dashboardStage;
    private Component dashboardScene;
    protected DashboardConsole dashboardConsole;

    // So, start function is just an initialization, login scene should not belong here. I should make a new class for login phase.
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Initialization.
        {
            // Basic states:
            dashboardScene = new Component();
            loginScene = new Component();

            // Login stage and scene.
            loginStage = primaryStage;
            loginConsole = new LoginConsole(loginScene);
            loginConsole.initializeLoginGUI(() -> {
                loginStage.close();
                triggerDashboard();
            });

            // Login configuration.
            {
                loginStage.setTitle("Welcome to Majutsu!"); // Thang dat ten cho chuong trinh wibu vl
                loginStage.setMinWidth(854);
                loginStage.setMinHeight(480);
            }

            // Dashboard stage and scene.
            dashboardStage = new Stage();
            dashboardConsole = new DashboardConsole(dashboardScene);
            dashboardConsole.initializeDashboardGUI(() -> {
                dashboardStage.close();
                triggerLogin();
            }, () -> {
                dashboardStage.close();
            });

            // Dashboard configuration.
            {
                dashboardStage.setTitle("Majutsu - Library Management System!"); // Thang dat ten cho chuong trinh wibu vl
                dashboardStage.setMinWidth(1280);
                dashboardStage.setMinHeight(720);
            }

            // Start new programme.
            triggerLogin();
        }
    }

    private void triggerLogin() {
        loginConsole.show(loginStage);
    }

    private void triggerDashboard() {
        dashboardConsole.setUsername(loginConsole.getUsername());
        dashboardConsole.show(dashboardStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}