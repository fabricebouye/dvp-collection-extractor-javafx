package test;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public final class Person {

    public Person(final String name, final String surname) {
        setName(name);
        setSurname(surname);
    }

    private final StringProperty name = new SimpleStringProperty(this, "name"); // NOI18N.

    public final String getName() {
        return name.get();
    }

    public final void setName(final String value) {
        name.set(value);
    }

    public final StringProperty nameProperty() {
        return name;
    }

    private final StringProperty surname = new SimpleStringProperty(this, "surname"); // NOI18N.

    public final String getSurname() {
        return surname.get();
    }

    public final void setSurname(final String value) {
        surname.set(value);
    }

    public final StringProperty surnameProperty() {
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

