package laustrup.bandwichpersistence.models.users;

import laustrup.bandwichpersistence.models.dtos.users.LoginDTO;
import laustrup.bandwichpersistence.utilities.console.Printer;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.InputMismatchException;

/**
 * Is use for logging in a user.
 * Can check validations of email and password.
 */
@ToString
public class Login {

    /**
     * The title used for a user.
     * Can also be as an email, since it's possible to log in with email.
     */
    @Getter
    private String _username;

    /**
     * This makes sure, that the user is allowed to log in.
     * The length must be 7, also must contain a special character and number.
     */
    @Getter
    private String _password;

    /**
     * Specifies the time this entity was created.
     */
    @Getter
    private LocalDateTime _timestamp;

    public Login(LoginDTO login) {
        this(login.getUsername(), login.getPassword());
    }
    public Login(String username, String password) {
        _username = username;
        _password = password;
        _timestamp = LocalDateTime.now();
    }

    /**
     * Insures the password has the length of 7 and contains a special character and number.
     * Must be used before processing the Login.
     * @return True in case the previous conditions mentioned are met, otherwise False.
     */
    public boolean passwordIsValid() {
        boolean containsSpecialCharacter = false, containsNumber = false;

        if (_password.length() >= 7) {
            for (int i = 0; i < _password.length(); i++) {
                String current = String.valueOf(_password.charAt(i));

                if (!current.matches("[^a-zA-Z0-9 ]"))
                    containsSpecialCharacter = true;

                try {
                    Integer.parseInt(current);
                    containsNumber = true;
                } catch (Exception ignored) {}

                if (containsSpecialCharacter && containsNumber)
                    return true;
            }
        }
        return containsSpecialCharacter && containsNumber;
    }

    /**
     * Uses the emailIsValid() method to check, if the username of this Login class is an email.
     * @return True in case the username has a single @ with content before and the other part
     * can be split with a dot, that also have content on both ends.
     */
    public boolean usernameIsEmailKind() { return emailIsValid(_username); }

    /**
     * Checks if a String is an email or not.
     * @param email Is the String of which the method will check of being an email.
     * @return True in case the username has a single @ with content before and the other part
     * can be split with a dot, that also have content on both ends.
     */
    public boolean emailIsValid(String email) {
        if (email.contains("@")) {
            String[] sections = email.split("@");
            if (sections.length == 2 && !sections[0].isEmpty()) {
                try {
                    sections = separateEmailSection(sections);
                    Printer.get_instance().print(sections);
                    return true;
                } catch (InputMismatchException e) {
                    Printer.get_instance().print("Email is missing an ending...",e);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Separates the last part of the email, that is being validated for being an email.
     * @param sections The sections of the email that is being validated.
     * @return A String array, that is of all three sections parts of email.
     * @throws InputMismatchException Will be thrown, if the email can't be split into 3 parts.
     */
    private String[] separateEmailSection(String[] sections) throws InputMismatchException {
        String[] splittedSection = sections[1].split(".");
        if (splittedSection.length == 2 && !splittedSection[0].isEmpty() && !splittedSection[1].isEmpty()) {
            String[] storage = new String[3];

            storage[0] = sections[0];
            storage[1] = splittedSection[0];
            storage[2] = splittedSection[1];

            return storage;
        }
        throw new InputMismatchException();
    }
}
