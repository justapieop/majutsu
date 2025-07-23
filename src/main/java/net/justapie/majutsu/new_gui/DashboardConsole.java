package net.justapie.majutsu.new_gui;

import atlantafx.base.theme.PrimerDark;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.*;
import net.justapie.majutsu.db.repository.user.UserRepository;
import net.justapie.majutsu.db.repository.user.UserRepositoryFactory;
import net.justapie.majutsu.utils.CryptoUtils;
import java.util.ArrayList;

public class DashboardConsole {

    private Component associatedComponent;
    private Runnable terminateProgram;
    TitledPane usernameSection;

    private VBox availableDocSection;
    private VBox unavailableDocSection;
    private VBox expiredDocSection;

    private VBox screen;

    private String currentUsername;

    private ArrayList<ArrayList<String>> booksStatus = new ArrayList<>();
    private ArrayList<ArrayList<String>> papersStatus = new ArrayList<>();

    private void fetchData() {
    }

    protected DashboardConsole(Component component) {
        this.associatedComponent = component;
        fetchData();
    }

    protected void initializeAvailableDocSections() {
        availableDocSection = new VBox(10);
        {
            availableDocSection.setPadding(new Insets(15));

            availableDocSection.getChildren().add(createButton("AVAILABLE"));

//                        TitledPane first = createTitledPane("Học kỳ 1 năm 2024-2025 🔒", data1);
//                        TitledPane second = createTitledPane("Học kỳ 2 năm 2024-2025 🔒", data2);

//                        mainScreen.getChildren().addAll();
        }
    }

    protected void initializeUnavailableDocSections() {
        unavailableDocSection = new VBox(10);
        {
            unavailableDocSection.setPadding(new Insets(15));

            unavailableDocSection.getChildren().add(createButton("UNAVAILABLE"));
        }
    }

    protected void initializeExpiredDocSections() {
        expiredDocSection = new VBox(10);
        {
            expiredDocSection.setPadding(new Insets(15));

            expiredDocSection.getChildren().add(createButton("EXPIRED"));

        }
    }

    protected void initializeDashboardGUI(Runnable internalAPI, Runnable terminateProgram) {
        // Set terminating method.
        this.terminateProgram = terminateProgram;

        // Set internal API.
        associatedComponent.setInternalAPI(internalAPI);

        // Logo data.
        Image logoImage = new Image("https://www.lawdistrict.com/static/fc96213acf942f65110f1e02ad43d588/job-application-sample.png");
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(256);
        logoView.setPreserveRatio(true);

        // Scene setup.
        initializeAvailableDocSections();
        initializeUnavailableDocSections();
        initializeExpiredDocSections();

        BorderPane root = new BorderPane();
        {
            // Left pane.
            VBox leftPane = new VBox();
            {
                // Configuration.
                leftPane.setPadding(new Insets(0, 0, 0, 0));
                leftPane.setStyle("""
                    -fx-background-color: #1E2A38;
                    -fx-border-color: gray;
                    -fx-border-width: 1.5px;
                """);
                leftPane.setPrefWidth(256);
                leftPane.setSpacing(0);

                // Logo.
                leftPane.getChildren().add(logoView);

                // Sidebar.
                VBox sidebar = new VBox(10);
                {
                    // Sidebar configuration.
                    sidebar.setPadding(new Insets(20));
                    sidebar.setPrefWidth(200);
                    sidebar.setStyle("-fx-background-color: #1E2A38;");

                    // Buttons for sidebar.
                    {
                        // Button 1.
                        Button availableDocButton = createButton("Available documents");
                        availableDocButton.setOnAction(e -> {
                            screen.getChildren().set(1, availableDocSection);
                        });

                        // Button 2.
                        Button unavailableDocButton = createButton("Unavailable documents");
                        unavailableDocButton.setOnAction(e -> {
                            screen.getChildren().set(1, unavailableDocSection);
                        });

                        // Button 3.
                        Button expiredDocButton = createButton("Expired documents");
                        expiredDocButton.setOnAction(e -> {
                            screen.getChildren().set(1, expiredDocSection);
                        });

                        sidebar.getChildren().addAll(
                            availableDocButton,
                            unavailableDocButton,
                            expiredDocButton
                        );
                    }
                }
                leftPane.getChildren().add(sidebar);

            }
            root.setLeft(leftPane);

            // Center pane.
            StackPane centerPane = new StackPane();
            {
                centerPane.setPadding(new Insets(0, 0, 0, 0));
                centerPane.setStyle("""
                    -fx-background-color: white;
                    -fx-border-color: gray;
                    -fx-border-width: 1.5px;
                """);

                screen = new VBox();
                {
                    // Screen configuration.
                    screen.setSpacing(0);
                    screen.setPadding(new Insets(0, 0, 0, 0));

                    // Header bar.
                    HBox headerBar = new HBox();
                    {
                        // Header bar configuration.
                        headerBar.setPrefHeight(64);
                        headerBar.setAlignment(Pos.CENTER_LEFT);
                        headerBar.setPadding(new Insets(5, 10, 10, 5));
                        headerBar.setStyle("""
                            -fx-background-color: #1E2A38;
                            -fx-border-color: gray;
                            -fx-border-width: 1.5px;
                        """);

                        // Label.
                        Label labelLMS = new Label("Majutsu - Library Management System.");
                        labelLMS.setPadding(new Insets(5, 8, 5, 8));
                        labelLMS.setStyle("""
                            -fx-text-fill: green;
                            -fx-font-size: 18px;
                            -fx-font-style: italic;
                            -fx-font-weight: bold;
                            -fx-alignment: CENTER;
                            -fx-border-color: transparent;
                        """);

                        // Add label.
                        headerBar.getChildren().add(labelLMS);
                    }
                    screen.getChildren().add(headerBar);

                    // Add default main screen.
                    screen.getChildren().add(availableDocSection);
                }
                centerPane.getChildren().add(screen);

                // Username and settings.
                Button logoutButton = createButton("Logout.");
                {
                    logoutButton.setOnAction(e -> {
                        usernameSection.setExpanded(false);
                        associatedComponent.getManagerI().run();
                    });
                }
                Button exitButton = createButton("Exit.");
                {
                    exitButton.setOnAction(e -> {
                        terminateProgram.run();
                    });
                }
                usernameSection = createDropdown("Welcome, " + currentUsername + "!", logoutButton, exitButton);
                centerPane.setAlignment(usernameSection, Pos.TOP_RIGHT);
                centerPane.setMargin(usernameSection, new Insets(14, 8, 0, 0));
                usernameSection.setMaxWidth(256);

                centerPane.getChildren().add(usernameSection);
            }
            root.setCenter(centerPane);
        }

        // Finishing procedure.
        associatedComponent.setScene(new Scene(root, 1280, 720));
    }

    protected void show(Stage stage) {
        associatedComponent.show(stage);
    }

    protected void setUsername(String username) {
        currentUsername = username;
    }



    // Better button.
    private Button createButton(String text) {
        Button button = new Button(text);

        button.setStyle("""
            -fx-background-color: #1E2A38;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-alignment: CENTER_LEFT;
        """);

        button.setOnMouseEntered(e -> button.setStyle("""
            -fx-background-color: #2F3B4C;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-alignment: CENTER_LEFT;
        """));

        button.setOnMouseExited(e -> button.setStyle("""
            -fx-background-color: #1E2A38;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-alignment: CENTER_LEFT;
        """));

        return button;
    }

    private HBox createCourseRow(String code, String name, int credits, boolean compulsory, String status) {
        HBox row = new HBox(10);
        row.setPadding(new Insets(5));
        row.setStyle("-fx-border-color: #ccc; -fx-border-width: 0 0 1 0;");

        Label codeLabel = new Label(code);
        codeLabel.setPrefWidth(80);

        Label nameLabel = new Label(name);
        nameLabel.setPrefWidth(250);

        Label creditsLabel = new Label(String.valueOf(credits));
        creditsLabel.setPrefWidth(50);

        Label compulsoryLabel = new Label(compulsory ? "✔" : "");
        compulsoryLabel.setPrefWidth(50);

        Label statusLabel = new Label(status);
        if (status.equalsIgnoreCase("Hoàn thành"))
            statusLabel.setStyle("-fx-text-fill: green;");
        else if (status.equalsIgnoreCase("Đang học"))
            statusLabel.setStyle("-fx-text-fill: purple;");
        statusLabel.setPrefWidth(100);

        row.getChildren().addAll(codeLabel, nameLabel, creditsLabel, compulsoryLabel, statusLabel);
        return row;
    }

//    private TitledPane createTitledPane(String title, String[][] data) {
//        VBox content = new VBox();
//        content.setPadding(new Insets(10));
//        content.setSpacing(5);
//
//        for (String[] row : data) {
//            String code = row[0];
//            String name = row[1];
//            int credits = Integer.parseInt(row[2]);
//            boolean compulsory = Boolean.parseBoolean(row[3]);
//            String status = row[4];
//
//            content.getChildren().add(createCourseRow(code, name, credits, compulsory, status));
//        }
//
//        TitledPane pane = new TitledPane(title, content);
//        pane.setExpanded(true);
//
//        return pane;
//    }

    // Dropdown pane.
    private TitledPane createDropdown(String title, Button... subButtons) {
        VBox content = new VBox(5, subButtons);

        content.setStyle("""
            -fx-background-color: #1E2A38;
            -fx-border-color: gray;
            -fx-border-width: 1.5px;
        """);

        TitledPane pane = new TitledPane(title, content);
        pane.setExpanded(false);
        pane.setCollapsible(true);
        pane.setStyle("""
            -fx-background-color: #1E2A38;
            -fx-border-color: #1E2A38;
            -fx-border-width: 1.5px;
            -fx-text-fill: white;
            -fx-font-size: 14px;
        """);

        pane.getStylesheets().add(
                "data:text/css," +
                        ".titled-pane > .title {" +
                        " -fx-background-color: #1E2A38;" +
                        " -fx-border-color: transparent;" +
                        " -fx-border-width: 2;" +
                        " -fx-border-radius: 5;" +
                        " -fx-background-radius: 5;" +
                "}"
        );

        return pane;
    }

    private ScrollPane createScrollPane(Button... subButtons) {
        VBox content = new VBox(subButtons);

        content.setStyle("""
            -fx-background-color: white;
            -fx-border-color: gray;
            -fx-border-width: 1.5px;
        """);

        return new ScrollPane(content);
    }
}

