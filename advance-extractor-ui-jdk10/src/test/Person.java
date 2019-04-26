package test;

import java.util.Objects;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;

/**
 * Cette classe modélise une personne.
 */
public final class Person {

    /**
     * Crée une nouvelle instance.
     * @param name Nom de famille.
     * @param surname Prénom.
     * @throws NullPointerException Si {@code name} ou {@code surname} est {@code null}.
     * @throws IllegalArgumentException Si {@code name} ou {@code surname} est vide. 
     */
    public Person(final String name, final String surname) throws NullPointerException, IllegalArgumentException {
        setName(name);
        setSurname(surname);
    }

    /**
     * Propriété nom.
     */
    private final ReadOnlyStringWrapper name = new ReadOnlyStringWrapper(this, "name"); // NOI18N.

    public final String getName() {
        return name.get();
    }

    public final void setName(final String value) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(value);
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty."); // NOI18N.
        }
        name.set(value.trim());
    }

    public final ReadOnlyStringProperty nameProperty() {
        return name;
    }

    /**
     * Propriété prénom.
     */
    private final ReadOnlyStringWrapper surname = new ReadOnlyStringWrapper(this, "surname"); // NOI18N.

    public final String getSurname() {
        return surname.get();
    }

    public final void setSurname(final String value) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(value);
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException("Surname cannot be empty."); // NOI18N.
        }
        surname.set(value.trim());
    }

    public final ReadOnlyStringProperty surnameProperty() {
        return surname;
    }

    @Override
    public String toString() {
        String name = getName();
        name = (name == null) ? "" : name.trim(); // NOI18N.
        String surname = getSurname();
        surname = (surname == null) ? "" : surname.trim(); // NOI18N.
        final StringBuilder builder = new StringBuilder();
        if (!name.isEmpty()) {
            builder.append(name.trim());
        }
        if (!name.isEmpty() && !surname.isEmpty()) {
            builder.append(" ");
        }
        if (!surname.isEmpty()) {
            builder.append(surname.trim());
        }
        return builder.toString();
    }
}
