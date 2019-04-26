package test;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.stream.IntStream;

public final class Main {

    private static ObservableList<Person> initializeList() {
        // Création de la liste.
        final var persons = FXCollections.<Person>observableArrayList(person -> new ObservableValue[]{person.nameProperty(), person.surnameProperty()});
        persons.setAll(
                new Person("Dupond", "Valérie"), // NOI18N.
                new Person("Higgins", "Clark"), // NOI18N.
                new Person("Pantou", "Maurice"), // NOI18N.
                new Person("Parmentier", "Yvette")); // NOI18N.
        // Écouteur.
        persons.addListener((ListChangeListener.Change<? extends Person> change) -> {
            // Rappel : plusieurs modifications peuvent être agrégées dans un seul événement.
            while (change.next()) {
                var changeLabel = "?"; // NOI18N.
                if (change.wasReplaced()) {
                    changeLabel = "replaced"; // NOI18N.
                } else if (change.wasAdded()) {
                    changeLabel = "added"; // NOI18N.
                } else if (change.wasRemoved()) {
                    changeLabel = "removed"; // NOI18N.
                } else if (change.wasPermutated()) {
                    changeLabel = "permutated"; // NOI18N.
                } else if (change.wasUpdated()) {
                    changeLabel = "updated"; // NOI18N.
                }
                final var pattern = String.format("Element %s was %s%n", "%d", changeLabel); // NOI18N.
                if (change.wasAdded() || change.wasReplaced() || change.wasUpdated()) {
                    // Parcours exclusif.
                    IntStream.range(change.getFrom(), change.getTo())
                            .forEach(index -> System.out.printf(pattern, index));
                } else {
                    // Parcours inclusif.
                    IntStream.rangeClosed(change.getFrom(), change.getTo())
                            .forEach(index -> System.out.printf(pattern, index));
                }
            }
        });
        return persons;
    }

    public static void main(String... args) {
        final var persons = initializeList();
        // Opérations.
        System.out.println(persons);
        // Retrait.
        System.out.println("== Remove ==");
        persons.remove(0);
        System.out.println(persons);
        // Ajout.
        System.out.println("== Add ==");
        persons.add(0, new Person("Dupond", "Valérie")); // NOI18N.
        System.out.println(persons);
        // Remplacement.
        System.out.println("== Replace ==");

        persons.set(0, new Person("Langworth", "Cathy")); // NOI18N.
        System.out.println(persons);
        // Tri -> permutation.
        System.out.println("== Permute ==");
        persons.sort((p1, p2) -> {
            int result = p1.getName().compareTo(p2.getName());
            if (result == 0) {
                result = p1.getSurname().compareTo(p2.getSurname());
            }
            return result;
        });
        System.out.println(persons);
        // Mise à jour.
        System.out.println("== Update ==");
        persons.get(0).setName("Smith"); // NOI18N.
        persons.get(0).setSurname("Anny"); // NOI18N.
        System.out.println(persons);
    }
}
