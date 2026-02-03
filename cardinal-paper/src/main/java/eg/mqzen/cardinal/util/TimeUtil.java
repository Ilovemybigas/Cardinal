package eg.mqzen.cardinal.util;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a time-formatting utility which is used to format time into
 * a human-readable format.
 */
public class TimeUtil {
    private final static Pattern DURATION_PATTERN = Pattern.compile("([0-9]*\\.?[0-9]+)\\s*([a-z]+)");
    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy 'at' hh:mm:ss", Locale.ENGLISH);

    /**
     * Format a time into a human-readable format.
     *
     * @param time  Time to format
     * @param small Whether to use a small format such as 1s, 1d, etc.
     * @return Formatted time
     * @see #format(Duration, boolean)
     * @deprecated Deprecated in favor of a more stable time-keeping data type.
     */
    @Deprecated
    public static String format(long time, boolean small) {
        Preconditions.checkArgument(time >= 0, "Time cannot be smaller than 0.");

        int toSec = (int) (time / 1000) % 60;
        int toMin = (int) ((time / (1000 * 60)) % 60);
        int toHour = (int) ((time / (1000 * 60 * 60)) % 24);
        int toDays = (int) (time / (1000 * 60 * 60 * 24));

        String day = (small ? "d" : (toDays == 1 ? "day" : "days"));
        String hour = (small ? "h" : (toHour == 1 ? "hour" : "hours"));
        String minute = (small ? "m" : (toMin == 1 ? "minute" : "minutes"));
        String second = (small ? "s" : (toSec == 1 ? "second" : "seconds"));

        String dayFormat = (toDays != 0 ? toDays + (small ? "" : " ") + day + " " : "");
        String hourFormat = (toHour != 0 ? toHour + (small ? "" : " ") + hour + " " : "");
        String minuteFormat = (toMin != 0 ? toMin + (small ? "" : " ") + minute + " " : "");
        String secondFormat = (toSec != 0 ? toSec + (small ? "" : " ") + second : "");

        TimeFormatter formatter = new TimeFormatter(dayFormat, hourFormat, minuteFormat, secondFormat);
        return formatter.toString();
    }

    /**
     * Format a duration into a human-readable format.
     *
     * @param duration The duration to format
     * @param small    Whether to use a small format such as 1s, 1d, etc.
     * @return The formatted duration as a string
     * @throws NullPointerException If `duration` is null
     */
    public static String format(@NotNull Duration duration, boolean small) {
        Preconditions.checkNotNull(duration, "`duration` cannot be null!");

        long toDays = duration.toDays();
        long toHours = duration.toHours() % 24;
        long toMinutes = duration.toMinutes() % 60;
        long toSeconds = duration.getSeconds() % 60;

        String day = (small ? "d" : (toDays == 1 ? "day" : "days"));
        String hour = (small ? "h" : (toHours == 1 ? "hour" : "hours"));
        String minute = (small ? "m" : (toMinutes == 1 ? "minute" : "minutes"));
        String second = (small ? "s" : (toSeconds == 1 ? "second" : "seconds"));

        String dayFormat = (toDays != 0 ? toDays + (small ? "" : " ") + day + " " : "");
        String hourFormat = (toHours != 0 ? toHours + (small ? "" : " ") + hour + " " : "");
        String minuteFormat = (toMinutes != 0 ? toMinutes + (small ? "" : " ") + minute + " " : "");
        String secondFormat = (toSeconds != 0 ? toSeconds + (small ? "" : " ") + second : "");

        TimeFormatter formatter = new TimeFormatter(dayFormat, hourFormat, minuteFormat, secondFormat);
        return formatter.toString();
    }

    /**
     * Format a time into a human-readable format.
     *
     * @param time Time to format
     * @return Formatted time
     * @see #format(Duration)
     * @deprecated Deprecated in favor of a more stable time-keeping data type.
     */
    @Deprecated
    public static String format(long time) {
        return format(time, false);
    }

    /**
     * Format a duration into a human-readable format.
     *
     * @param duration The duration to format
     * @return The formatted duration as a string
     * @throws NullPointerException If `duration` is null
     */
    public static String format(@NotNull Duration duration) {
        Preconditions.checkNotNull(duration, "`duration` cannot be null!");

        return format(duration, false);
    }

    /**
     * Format time from a string.
     *
     * @param input Input string
     * @return Formatted time
     * @deprecated Deprecated in favor of a more stable time-keeping data type.
     * @see #parse(String)
     */
    @Deprecated
    public static long fromString(@NotNull String input) {
        Preconditions.checkNotNull(input, "'input' cannot be null!");
        Preconditions.checkArgument(!input.isEmpty(), "'input' cannot be empty!");

        StringBuilder builder = new StringBuilder();
        int seconds = 0;
        int minutes = 0;
        int hours = 0;
        int days = 0;
        int weeks = 0;
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                builder.append(c);
            } else {
                switch (c) {
                    case 's' -> {
                        if (!builder.isEmpty()) {
                            seconds += Integer.parseInt(builder.toString());
                            builder.setLength(0);
                        }
                    }
                    case 'm' -> {
                        if (!builder.isEmpty()) {
                            minutes += Integer.parseInt(builder.toString());
                            builder.setLength(0);
                        }
                    }
                    case 'h' -> {
                        if (!builder.isEmpty()) {
                            hours += Integer.parseInt(builder.toString());
                            builder.setLength(0);
                        }
                    }
                    case 'd' -> {
                        if (!builder.isEmpty()) {
                            days += Integer.parseInt(builder.toString());
                            builder.setLength(0);
                        }
                    }
                    case 'w' -> {
                        if (!builder.isEmpty()) {
                            weeks += Integer.parseInt(builder.toString());
                            builder.setLength(0);
                        }
                    }
                    default -> throw new IllegalArgumentException("Not a valid duration format.");
                }
            }
        }

        try {
            long totalSeconds = 0;
            totalSeconds = Math.addExact(totalSeconds, seconds);
            totalSeconds = Math.addExact(totalSeconds, Math.multiplyExact(minutes, 60L));
            totalSeconds = Math.addExact(totalSeconds, Math.multiplyExact(hours, 60L * 60L));
            totalSeconds = Math.addExact(totalSeconds, Math.multiplyExact(days, 24L * 60L * 60L));
            totalSeconds = Math.addExact(totalSeconds, Math.multiplyExact(weeks, 7L * 24L * 60L * 60L));
            return Math.multiplyExact(totalSeconds, 1000L);
        } catch (ArithmeticException ex) {
            throw new IllegalArgumentException("Duration is too large and causes numeric overflow");
        }
    }


    /**
     * Parses a string representation of duration and returns the equivalent {@link Duration} object.
     *
     * @param input The input string representing duration.
     * @return The parsed {@link Duration} object.
     * @throws NullPointerException     if the input is null.
     * @throws IllegalArgumentException if the input is empty or not a valid duration format.
     */
    public static Duration parse(String input) {

        if (input == null) {
            throw new IllegalArgumentException("'input' cannot be null!");
        }
        if (input.isEmpty()) {
            throw new IllegalArgumentException("'input' cannot be empty!");
        }

        String normalizedInput = input.trim().toLowerCase();

        // Handle permanent duration
        if (normalizedInput.equals("permanent") || normalizedInput.equals("duration:permanent")) {
            return Duration.ZERO; // or Duration.ofSeconds(-1) if you prefer -1
        }

        Duration totalDuration = Duration.ZERO;

        // Regex pattern to match number (with optional decimal) + unit
        // Allows optional whitespace between number and unit
        Matcher matcher = DURATION_PATTERN.matcher(normalizedInput);

        StringBuilder consumedInput = new StringBuilder();
        boolean foundMatch = false;

        while (matcher.find()) {
            foundMatch = true;

            // Validate that the number is properly formatted (no multiple decimals)
            String numberStr = matcher.group(1);
            if (numberStr.chars().mapToObj(c -> (char) c).filter(c -> c == '.').count() > 1) {
                throw new IllegalArgumentException("Invalid number format: " + numberStr + " in input: " + input);
            }

            double value;
            try {
                value = Double.parseDouble(numberStr);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid number format: " + numberStr + " in input: " + input);
            }

            String unit = matcher.group(2);

            // Track what we've consumed
            consumedInput.append(matcher.group(0)).append(" ");

            Duration unitDuration = parseSingleUnit(value, unit);
            totalDuration = totalDuration.plus(unitDuration);
        }

        if (!foundMatch) {
            throw new IllegalArgumentException("Invalid duration format: " + input);
        }

        // Check if we consumed the entire input (allowing for whitespace differences)
        String consumedNormalized = consumedInput.toString().trim().replaceAll("\\s+", " ");
        String originalNormalized = normalizedInput.replaceAll("\\s+", " ");

        if (!consumedNormalized.equals(originalNormalized)) {
            throw new IllegalArgumentException("Invalid duration format - unparseable parts in: " + input);
        }

        return totalDuration;
    }

    private static Duration parseSingleUnit(double value, String unit) {
        return switch (unit) {
            case "s", "sec", "second", "seconds" -> Duration.ofMillis((long) (value * 1000));
            case "m", "min", "minute", "minutes" -> Duration.ofMillis((long) (value * 60 * 1000));
            case "h", "hr", "hour", "hours" -> Duration.ofMillis((long) (value * 60 * 60 * 1000));
            case "d", "day", "days" -> Duration.ofMillis((long) (value * 24 * 60 * 60 * 1000));
            case "w", "week", "weeks" -> Duration.ofMillis((long) (value * 7 * 24 * 60 * 60 * 1000));
            case "mo", "month", "months" -> Duration.ofMillis((long) (value * 30 * 24 * 60 * 60 * 1000)); // Approximate
            case "y", "year", "years" -> Duration.ofMillis((long) (value * 365 * 24 * 60 * 60 * 1000)); // Approximate
            default -> throw new IllegalArgumentException("Unknown time unit: " + unit);
        };
    }

    /**
     * Converts a {@link Duration} object into a human-readable string format like "1h30m".
     *
     * @param duration The Duration object to convert.
     * @return A string representing the human-readable format of the duration.
     * @throws NullPointerException if the duration is null.
     */
    @NotNull
    public static String parse(@NotNull Duration duration) {
        Preconditions.checkNotNull(duration, "'duration' cannot be null!");

        long days = duration.toDays();
        duration = duration.minusDays(days);

        long hours = duration.toHours();
        duration = duration.minusHours(hours);

        long minutes = duration.toMinutes();
        duration = duration.minusMinutes(minutes);

        long seconds = duration.getSeconds();

        StringBuilder builder = new StringBuilder();

        if (days > 0) {
            builder.append(days).append("d");
        }
        if (hours > 0) {
            builder.append(hours).append("h");
        }
        if (minutes > 0) {
            builder.append(minutes).append("m");
        }
        if (seconds > 0) {
            builder.append(seconds).append("s");
        }

        return builder.toString();
    }

    // Just fix your utility method once:
    public static String formatDate(@NotNull Instant instant) {
        return DATE_TIME_FORMATTER.format(instant.atZone(ZoneId.systemDefault()));
    }

    public static boolean isValidDurationFormat(@NotNull String input) {
        return !input.isEmpty() && DURATION_PATTERN.matcher(input).matches();
    }

    /**
     * The class to format time
     */
    private static class TimeFormatter {

        private final LinkedList<String> entries = new LinkedList<>();

        public TimeFormatter(final String... entries) {
            Preconditions.checkNotNull(entries, "'entries' cannot be null!");

            this.entries.addAll(Arrays.asList(entries));
        }


        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (String entry : entries) {
                if (entry.isBlank()) {
                    continue;
                }

                builder.append(entry);
            }

            String string = builder.toString();

            char last = string.charAt(string.length() - 1);
            if (last == ' ') {
                string = string.substring(0, string.length() - 1);
            }

            return string;
        }
    }
}