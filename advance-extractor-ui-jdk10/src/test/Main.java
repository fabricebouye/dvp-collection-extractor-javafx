package test;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Application.
 */
public final class Main extends Application {

    private TextFlow messageFlow;

    /**
     * Création de l'éditeur.
     *
     * @param personTableView La table.
     * @return Une instance de {@code Pane}, jamais {@code null}.
     */
    private Pane createEditor(final TableView<Person> personTableView) {
        final var persons = personTableView.getItems();
        // Nom.
        final var nameField = new TextField();
        GridPane.setConstraints(nameField, 1, 0);
        GridPane.setHgrow(nameField, Priority.ALWAYS);
        final var nameLabel = new Label("Nom"); // NOI18N.
        nameLabel.setLabelFor(nameField);
        GridPane.setConstraints(nameLabel, 0, 0);
        // Prénom.
        final var surnameField = new TextField();
        GridPane.setConstraints(surnameField, 1, 1);
        GridPane.setHgrow(surnameField, Priority.ALWAYS);
        final var surnameLabel = new Label("Prénom"); // NOI18N.
        surnameLabel.setLabelFor(surnameField);
        GridPane.setConstraints(surnameLabel, 0, 1);
        // Binding booléens.
        final var hasNoName = nameField.textProperty().isEmpty();
        final var hasNoSurame = surnameField.textProperty().isEmpty();
        final var selectionIsNull = personTableView.getSelectionModel().selectedItemProperty().isNull();
        // Ajout d'une personne.
        final var addButton = new Button("Ajouter"); // NOI18N.
        addButton.disableProperty().bind(hasNoName.or(hasNoSurame));
        addButton.setOnAction(event -> {
            final var name = nameField.getText().trim();
            final var surname = surnameField.getText().trim();
            final var person = new Person(name, surname);
            persons.add(person);
            personTableView.getSelectionModel().select(person);
        });
        // Mise à jour d'une personne.
        final var updateButton = new Button("Modifier"); // NOI18N.
        updateButton.disableProperty().bind(selectionIsNull.or(hasNoName.or(hasNoSurame)));
        updateButton.setOnAction(event -> {
            final var name = nameField.getText().trim();
            final var surname = surnameField.getText().trim();
            final var person = personTableView.getSelectionModel().getSelectedItem();
            person.setName(name);
            person.setSurname(surname);
        });
        // Suppression d'une personne.
        final var removeButton = new Button("Supprimer"); // NOI18N.
        removeButton.disableProperty().bind(selectionIsNull);
        removeButton.setOnAction(event -> {
            final int index = personTableView.getSelectionModel().getSelectedIndex();
            persons.remove(index);
        });
        // Regroupement des boutons.
        final var buttonHBox = new HBox();
        buttonHBox.setSpacing(6);
        buttonHBox.setAlignment(Pos.CENTER_RIGHT);
        buttonHBox.getChildren().setAll(addButton, updateButton, removeButton);
        GridPane.setConstraints(buttonHBox, 0, 3, 2, 1);
        // Mettre à jour l'éditeur lors d'une sélection dans la table.
        personTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Retrait de l'ancien élément.
            Optional.ofNullable(oldValue)
                    .ifPresent(value -> {
                        nameField.setText(null);
                        surnameField.setText(null);
                    });
            // Mise en place du nouvel élément.
            Optional.ofNullable(newValue)
                    .ifPresent(value -> {
                        nameField.setText(value.getName());
                        surnameField.setText(value.getSurname());
                    });
        });
        // Mise en place de la grille.
        final var personEditor = new GridPane();
        personEditor.setHgap(6);
        personEditor.setVgap(6);
        // Configuration des colonnes de la grille.
        personEditor.getColumnConstraints().setAll(
                IntStream.range(0, 1)
                        .mapToObj(column -> new ColumnConstraints())
                        .toArray(ColumnConstraints[]::new));
        // Configuration des lignes de la grille.
        personEditor.getRowConstraints().setAll(
                IntStream.range(0, 2)
                        .mapToObj(row -> new RowConstraints())
                        .toArray(RowConstraints[]::new));
        personEditor.getChildren().setAll(
                nameLabel, nameField,
                surnameLabel, surnameField,
                buttonHBox);
        return personEditor;
    }

    @Override
    public void start(final Stage primaryStage) {
        // Mise en place du modèle.
        // Si ObservableList normale alors la ListView ne se met pas a jour lors d'une modification.
//        final ObservableList<Person> persons = FXCollections.observableArrayList();
        final ObservableList<Person> persons = FXCollections.observableArrayList(person -> new ObservableValue[]{person.nameProperty(), person.surnameProperty()});
        // Peuplement de la liste (ex: récupérer les valeurs depuis une BD).
        persons.setAll(
                new Person("Dupond", "Valérie"), // NOI18N.
                new Person("Higgins", "Clark"), // NOI18N.
                new Person("Pantou", "Maurice"), // NOI18N.
                new Person("Parmentier", "Yvette")); // NOI18N.
        // Colonne nom.
        final var nameColumn = new TableColumn<Person, String>("Nom"); // NOI18N.
        nameColumn.setCellValueFactory(feature -> feature.getValue().nameProperty());
        // Ou :
//        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name")); // NOI18N.
        // Colonne prénom.
        final var surnameColumn = new TableColumn<Person, String>("Prénom"); // NOI18N.
        surnameColumn.setCellValueFactory(feature -> feature.getValue().surnameProperty());
        // Ou :
//        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname")); // NOI18N.
        // Table.
        final var personTableView = new TableView<Person>();
        personTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        personTableView.getColumns().addAll(nameColumn, surnameColumn);
        personTableView.setItems(persons);
        VBox.setVgrow(personTableView, Priority.ALWAYS);
        // Éditeur
        final var personEditor = createEditor(personTableView);
        VBox.setVgrow(personEditor, Priority.NEVER);
        // Panneau de gauche.
        final var leftVBox = new VBox();
        leftVBox.setSpacing(6);
        leftVBox.setPadding(new Insets(6));
        leftVBox.getChildren().setAll(personTableView, personEditor);
        // Liste.
        final var personListView = new ListView<Person>();
        personListView.setItems(persons);
        // Zone de message.
        messageFlow = new TextFlow();
        persons.addListener(monitorListChange);
        AnchorPane.setTopAnchor(messageFlow, 0d);
        AnchorPane.setLeftAnchor(messageFlow, 0d);
        AnchorPane.setRightAnchor(messageFlow, 0d);
        final var messageAnchor = new AnchorPane(messageFlow);
        final var messageScroll = new ScrollPane(messageAnchor);
        // UI finale.
        final var root = new SplitPane(leftVBox, personListView, messageScroll);
        final var scene = new Scene(root, 600, 600);
        primaryStage.setTitle("Gestion des utilisateurs"); // NOI18N.
        primaryStage.setScene(scene);
        primaryStage.show();
        Platform.runLater(() -> root.setDividerPositions(0.33d, 0.66d));
//        ScenicView.show(scene);
    }

    public static void main(final String[] args) {
        launch(args);
    }

    private int added = 0;
    private int removed = 0;
    private int updated = 0;

    /**
     * Réagit lorsque le contenu de la liste change.
     */
    private final ListChangeListener<Person> monitorListChange = change -> {
        while (change.next()) {
            int oldAdded = added;
            int oldRemoved = removed;
            int oldUpdated = updated;
            if (change.wasPermutated()) {
            } else if (change.wasReplaced()) {
            } else if (change.wasAdded()) {
                System.out.println("Was added.");
                added++;
            } else if (change.wasRemoved()) {
                System.out.println("Was removed.");
                removed++;
            } else if (change.wasUpdated()) {
                System.out.println("Was updated.");
                updated++;
            }
            // Mise à jour du message.
            updateMessage(oldAdded, oldRemoved, oldUpdated, added, removed, updated);
        }
    };

    /**
     * Mise à jour du message.
     *
     * @param oldAdded   Ancienne valeur ajout.
     * @param oldRemoved Ancienne valeur retrait.
     * @param oldUpdated Ancienne valeur modification.
     * @param newAdded   Nouvelle valeur ajout.
     * @param newRemoved Nouvelle valeur retrait.
     * @param newUpdated Nouvelle valeur modification.
     */
    private void updateMessage(final int oldAdded, final int oldRemoved, final int oldUpdated, final int newAdded, final int newRemoved, final int newUpdated) {
//        System.out.printf("Updating message: %d %d %d%n", newAdded, newRemoved, newUpdated);
        final var bits = messageFlow.getChildren();
        messageFlow.getChildren().clear();
        if (newAdded > 0 || newRemoved > 0 || newUpdated > 0) {
            bits.add(new Text("La liste a été modifiée :")); // NOI18N.
            if (newAdded > 0) {
                final var text = new Text("\n\tajout(s) : " + newAdded); // NOI18N.
                if (oldAdded != newAdded) {
                    text.setFill(Color.RED);
                }
                bits.add(text);
            }
            if (newRemoved > 0) {
                final var text = new Text("\n\tretrait(s) : " + newRemoved); // NOI18N.
                if (oldRemoved != newRemoved) {
                    text.setFill(Color.RED);
                }
                bits.add(text);
            }
            if (newUpdated > 0) {
                final var text = new Text("\n\tmodification(s) : " + newUpdated); // NOI18N.
                if (oldUpdated != newUpdated) {
                    text.setFill(Color.RED);
                }
                bits.add(text);
            }
            // 2017-04-24 JDK 8/9 Bug ?
            // Parfois, lors d'une modification (pas de soucis avec ajout ou retrait), le rafraichissement s'arrete si on ne fini pas par un Text supplementaire.
            // Les appels de methode et la construction du TextFlow sont pourtant corrects.
            // Mais l'ecran affiche toujours les anciennes chaines.
            bits.add(new Text("\n")); // NOI18N.
        }
    }
}
