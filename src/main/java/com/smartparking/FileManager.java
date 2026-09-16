package com.smartparking;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all file I/O for the system: persisting tickets between runs
 * and appending to a simple activity log. Keeping this in its own class
 * means the rest of the app never touches java.io / java.nio directly.
 */
public class FileManager {

    private final Path ticketsFile;
    private final Path logFile;
    private static final DateTimeFormatter LOG_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public FileManager(String dataDirectory) {
        Path dir = Paths.get(dataDirectory);
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            System.err.println("Could not create data directory: " + e.getMessage());
        }
        this.ticketsFile = dir.resolve("tickets.txt");
        this.logFile = dir.resolve("activity.log");
    }

    /** Loads all previously saved tickets (active + closed) at startup. */
    public List<Ticket> loadTickets() {
        List<Ticket> tickets = new ArrayList<>();
        if (!Files.exists(ticketsFile)) {
            return tickets;
        }
        try (BufferedReader reader = Files.newBufferedReader(ticketsFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        tickets.add(Ticket.fromFileRecord(line));
                    } catch (Exception parseError) {
                        System.err.println("Skipping corrupted ticket record: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading tickets file: " + e.getMessage());
        }
        return tickets;
    }

    /** Rewrites the entire tickets file from the current in-memory list. */
    public void saveAllTickets(List<Ticket> tickets) {
        try (BufferedWriter writer = Files.newBufferedWriter(ticketsFile,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Ticket t : tickets) {
                writer.write(t.toFileRecord());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving tickets file: " + e.getMessage());
        }
    }

    /** Appends one line to the activity log (used for monitoring/audit trail). */
    public void log(String message) {
        String entry = "[" + LocalDateTime.now().format(LOG_FORMAT) + "] " + message;
        try (BufferedWriter writer = Files.newBufferedWriter(logFile,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(entry);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing log: " + e.getMessage());
        }
    }
}
