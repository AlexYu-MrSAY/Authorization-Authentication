package com.company.syugai.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDate;
import java.util.Objects;

@DatabaseTable(tableName = "Users")
public class User implements Model{
    @DatabaseField(id = true)
    private int id;
    @DatabaseField(columnName = "first_name", canBeNull = false)
    private String first_name;
    @DatabaseField(columnName = "last_name", canBeNull = false)
    private String last_name;
    @DatabaseField(columnName = "login", canBeNull = false)
    private String login;
    @DatabaseField(columnName = "password", canBeNull = false)
    private String password;
    @DatabaseField(columnName = "birthday", canBeNull = false, dataType = DataType.SERIALIZABLE)
    private LocalDate birthday;
    @DatabaseField(columnName = "role", canBeNull = false)
    private UserRole role;

    public User(int id, String first_name, String last_name, String login, String password, LocalDate birthday, UserRole role) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.login = login;
        this.password = password;
        this.birthday = birthday;
        this.role = role;
    }

    public User() {
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public static final String FIELD_ID = "id";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    public static final String BIRTHDAY = "birthday";
    public static final String ROLE = "role";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(first_name, user.first_name) && Objects.equals(last_name, user.last_name) && Objects.equals(login, user.login) && Objects.equals(password, user.password) && Objects.equals(birthday, user.birthday) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, first_name, last_name, login, password, birthday, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", login='" + login + '\'' +
                ", birthday=" + birthday +'\''+
                ", role=" + role + '\'' +
                ", password=" + password +
                '}';
    }
}
