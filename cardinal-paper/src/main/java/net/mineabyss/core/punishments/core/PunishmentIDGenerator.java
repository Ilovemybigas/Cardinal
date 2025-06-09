package net.mineabyss.core.punishments.core;

import java.util.concurrent.atomic.AtomicInteger;

public final class PunishmentIDGenerator {

    private final static AtomicInteger counter = new AtomicInteger(0);

    public static String generateNewID() {
        long timestamp = System.currentTimeMillis(); // Keep milliseconds
        int count = counter.getAndIncrement() & 0xFFFF;

        return String.format("%04X", (int)timestamp & 0xFFFF) +
                String.format("%04X", count);
    }

}
