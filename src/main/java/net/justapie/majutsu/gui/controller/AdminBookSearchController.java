package net.justapie.majutsu.gui.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import net.justapie.majutsu.db.repository.book.BookRepositoryFactory;
import net.justapie.majutsu.gbook.GBookClient;
import net.justapie.majutsu.gbook.model.Volume;
import net.justapie.majutsu.gui.SceneType;
import net.justapie.majutsu.gui.component.BookSelect;
import net.justapie.majutsu.gui.component.SelectedBook;
import net.justapie.majutsu.gui.model.DisplayableBook;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminBookSearchController extends BaseController {
    @FXML
    private TextField adminBookSearchQuery;

    private final HashMap<DisplayableBook, Integer> selected = new HashMap<>();
    @FXML
    private Label selectedPrompt;
    @FXML
    private Button confirmButton;
    @FXML
    private Label prompt;
    @FXML
    private Button adminSearchButton;
    @FXML
    private VBox bookListVBox;
    @FXML
    private VBox selectedListVBox;

    @FXML
    private void onAdminBookSearch() {
        String query = this.adminBookSearchQuery.getText();

        if (query.isBlank()) {
            this.prompt.setText("Please enter a query");
            return;
        }

        query = URLEncoder.encode(query, StandardCharsets.UTF_8);

        this.adminSearchButton.setDisable(true);
        this.prompt.setText("Please wait");

        String finalQuery = query;
        Thread thread = new Thread(() -> {
            List<Volume> volumes = GBookClient.getInstance().searchVolume(finalQuery).get().getItems();
            this.adminSearchButton.setDisable(false);

            Platform.runLater(
                    () -> this.prompt.setText(String.format("%d results found", volumes.size()))
            );

            for (final Volume vol : volumes) {
                DisplayableBook displayable = DisplayableBook.fromVolume(vol);

                Platform.runLater(() -> this.bookListVBox.getChildren().add(
                        new BookSelect(displayable).getMaster()
                ));

                if (this.selected.containsKey(displayable)) {
                    displayable.getCheckBox().setSelected(true);
                }

                displayable.getCheckBox().setOnAction(event -> {
                    boolean isSelected = displayable.getCheckBox().isSelected();

                    if (isSelected) {
                        this.selected.put(displayable, 1);
                    }

                    if (!isSelected) {
                        this.selected.remove(displayable);
                    }

                    this.selectedListVBox.getChildren().clear();
                    for (final Map.Entry<DisplayableBook, Integer> entry : this.selected.entrySet()) {
                        SelectedBook selectedBook = new SelectedBook(
                                entry.getKey().getVolumeInfo().getImageLinks().getThumbnail()
                        );

                        this.selectedListVBox.getChildren().addFirst(
                                selectedBook.getMaster()
                        );

                        selectedBook.getButton().setOnAction(e -> {
                            this.selected.remove(entry.getKey());
                            this.selectedListVBox.getChildren().remove(
                                    selectedBook.getMaster()
                            );
                            entry.getKey().getCheckBox().setSelected(false);

                            this.refresh();
                        });
                    }

                    this.refresh();
                });
            }
        });

        thread.start();
    }

    @FXML
    private void onConfirm() {
        List<DisplayableBook> displayableBooks = this.selected.keySet().stream().toList();
        BookRepositoryFactory.getInstance().create().addBook(displayableBooks);

        new AdminSplashController().process();
    }

    private void refresh() {
        this.selectedPrompt.setText(String.format("%d selected", this.selected.size()));
        this.confirmButton.setDisable(this.selected.isEmpty());
    }

    @FXML
    private void onAdminBookSearchBack() {
        this.switchToScene(SceneType.ADMIN);
    }
}
