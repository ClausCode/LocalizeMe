package com.clauscode.localization;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "localization")
public class Localization {
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

    public String getIdentifier() {
        return identifier;
    }

    public String getLanguage() {
        return language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Localization that = (Localization) o;
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
