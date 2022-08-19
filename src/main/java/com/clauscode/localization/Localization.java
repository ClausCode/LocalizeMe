package com.clauscode.localization;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity(name = "localization")
@RequiredArgsConstructor
@Table(name = "localization")
public class LocalizeDatabaseObject {
    @Id
    @Column(name = "identifier")
    private String identifier;

    @Column(name = "language")
    private String language;

    @Column(name = "value")
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LocalizeDatabaseObject that = (LocalizeDatabaseObject) o;
        return identifier != null && Objects.equals(identifier, that.identifier);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + identifier.hashCode();
        hash = 31 * hash + language.hashCode();
        hash = 31 * hash + value.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        return String.format("LocalizeDatabaseObject [identifier = %s, language = %s, value = %s]", identifier, language, value);
    }
}
