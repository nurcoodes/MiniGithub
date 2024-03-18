// Nur Ahmed
// CSE 123 AO
// P1: Mini-Git
// 5.3.2023

import java.util.*;
// This class represents a Repository which can store history of commits
public class Repository {
    private String name;
    private Commit head;

    // Constructor for repository that takes in the Repository's name
    // Throws IllegalArgumentException if name is empty or null
    public Repository(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.head = null;
    }

    // Returns String of the head id and returns null if head is null
    public String getRepoHead() {
        if (head == null) {
            return null;
        }
        return head.id;
    }

    // Returns String representation of the repo name and id of head. If head is null, it
    // returns the name and a message that there is no commits
    public String toString() {
        if (head == null) {
            return name + " - No commits";
        }
        return name + " - Current head: " + head;
    }

    // Takes in a String targetId and checks if the Repository contains a Commit with the given
    // ID. Returns true if it contains it and false if it's not found
    public boolean contains(String targetId) {
        Commit current = head;
        while (current != null) {
            if (current.id.equals(targetId)) {
                return true;
            }
            current = current.past;
        }
        return false;
    }

    // Displays a string representation of the most recent commits by n amount of times within
    // the Repo's history from most recent to oldest. Returns empty string if head is null
    // If n is greater than the number of commits, it displays all commits
    // Throws IllegalArgumentException if n is less or equal to 0
    public String getHistory(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        } 
        if (head == null) {
            return "";
        }
        String message = "";
        int count = 0;
        Commit current = head;
        while (current != null && count < n) {
            message += current.toString() + "\n";
            current = current.past;
            count++;
        }
        return message.trim();
    }

    // Creates a new commit with given message and sets it as the new head of the Repository
    // Returns new commit id as String
    public String commit(String message) {
        Commit newCommit = new Commit(message, head);
        head = newCommit;
        return newCommit.id;
    }

    // Resets the repository to a previous state by moving backwards
    // by n commits. If n is greater than the number of commits in the repository,
    // the head will be set to null.
    // Throws IllegalArgumentException if n is less or equal to 0
    public void reset(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        Commit current = head;
        while (current != null && n > 0) {
            current = current.past;
            n--;
        }
        head = current;
    }

    // Removes the Commit that matches the given ID from the Repository
    // Returns true if action was successful and false if not
    public boolean drop(String targetId) {
        Commit current = head;
        Commit prev = null;
        while (current != null) {
            if (current.id.equals(targetId)) {
                if (prev == null) {
                    head = current.past;
                } else {
                    prev.past = current.past;
                }
                return true;
            }
            prev = current;
            current = current.past;
        }
        return false;
    }

    // Creates new commit combining the commit with the given ID and the one before it
    // replacing both of them with the new one while preserving the rest of the history
    // Returns true if squash occured and false if not
    public boolean squash(String targetId) {
        Commit current = head;
        Commit prev = null;

        while (current != null && current.past != null) {
            if (current.id.equals(targetId)) {
                String newMessage = "SQUASHED: " + current.message + "/" + current.past.message;
                Commit newCommit = new Commit(newMessage, current.past.past);

                if (prev == null) {
                    head = newCommit;
                } else {
                    prev.past = newCommit;
                }

                return true;
            }

            prev = current;
            current = current.past;
        }
        return false;
    }

    // This class represents a single commit in a repository. Each commit contains a unique 
    // identifier, a message describing the changes made in the commit, and a reference to
    // the previous commit in the repository.
    public static class Commit {
        public final String id;
        public final String message;
        public Commit past;

        // Constructs new commit with given message and reference to previous commit
        public Commit(String message, Commit past) {
            this.id = getNewId();
            this.message = message;
            this.past = past;
        }

        // Constructs new commit with given message and a null reference
        public Commit(String message) {
            this(message, null);
        }

        // Returns string representation of the commit with the id followed by the message
        public String toString() {
            return id + ": " + message;
        }

        private static String getNewId() {
            return UUID.randomUUID().toString();
        }
    }
}