package net.mineabyss.core.punishments.core;

import net.mineabyss.cardinal.api.punishments.Punishable;
import net.mineabyss.cardinal.api.punishments.Punishment;
import net.mineabyss.cardinal.api.punishments.PunishmentID;
import net.mineabyss.cardinal.api.punishments.PunishmentIssuer;
import net.mineabyss.cardinal.api.punishments.PunishmentType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class StandardPunishment<T> implements Punishment<T> {

    private final @NotNull PunishmentID id;
    private final @NotNull PunishmentType type;
    private final @NotNull Punishable<T> target;
    private final @NotNull PunishmentIssuer issuer;
    private final @Nullable String reason;

    private final Instant issuedAt;
    private final Duration duration;
    private final Instant expiresAt;

    private final List<String> notes = new ArrayList<>();

    private RevocationInfo revocationInfo;

    StandardPunishment(
            @NotNull PunishmentID id,
            @NotNull PunishmentType type,
            @NotNull Punishable<T> target,
            @NotNull PunishmentIssuer issuer,
            @Nullable String reason,
            @NotNull Instant issuedAt,
            @NotNull Duration duration
    ) {
        this.id = id;
        this.type = type;
        this.target = target;
        this.issuer = issuer;
        this.reason = reason;

        this.issuedAt = issuedAt;
        this.duration = duration;
        this.expiresAt = issuedAt.plus(duration);
    }

    StandardPunishment(
            @NotNull PunishmentType type,
            @NotNull Punishable<T> target,
            @NotNull PunishmentIssuer issuer,
            @Nullable String reason,
            @NotNull Instant issuedAt,
            @NotNull Duration duration
    ) {
        this(new StandardPunishmentID(), type, target, issuer, reason, issuedAt, duration);
    }


    StandardPunishment(
            @NotNull PunishmentID id,
            @NotNull PunishmentType type,
            @NotNull Punishable<T> target,
            @NotNull PunishmentIssuer issuer,
            @Nullable String reason
    ) {
        this(id, type, target, issuer, reason, Instant.now(), Duration.ZERO);
    }

    StandardPunishment(
            @NotNull PunishmentType type,
            @NotNull Punishable<T> target,
            @NotNull PunishmentIssuer issuer,
            @Nullable String reason
    ) {
        this(type, target, issuer, reason, Instant.now(), Duration.ZERO);
    }

    StandardPunishment(
            @NotNull PunishmentID id,
            @NotNull PunishmentType type,
            @NotNull Punishable<T> target,
            @NotNull PunishmentIssuer issuer
    ) {
        this(id, type, target, issuer, null);
    }

    StandardPunishment(
            @NotNull PunishmentType type,
            @NotNull Punishable<T> target,
            @NotNull PunishmentIssuer issuer
    ) {
        this(type, target, issuer, null);
    }

    /**
     * Returns the unique identifier for this punishment.
     *
     * @return the unique identifier
     */
    @Override
    public @NotNull PunishmentID getId() {
        return id;
    }

    /**
     * Returns the type of this punishment.
     *
     * @return the type of the punishment
     */
    @Override
    public @NotNull PunishmentType getType() {
        return type;
    }

    /**
     * Returns the target of this punishment.
     * <p>
     * The target is an entity that can be punished, such as a player or an IP address.
     * </p>
     *
     * @return the target entity that is being punished
     */
    @Override
    public @NotNull Punishable<T> getTarget() {
        return target;
    }

    /**
     * Returns the {@link PunishmentIssuer} who issued this punishment.
     * <p>
     * The issuer is the entity that applied the punishment, such as a player or the console.
     * </p>
     *
     * @return the issuer of the punishment
     */
    @Override
    public @NotNull PunishmentIssuer getIssuer() {
        return issuer;
    }

    /**
     * Returns the reason for this punishment.
     * <p>
     * The reason is an optional string that explains why the punishment was applied.
     * </p>
     *
     * @return an Optional containing the reason, or empty if no reason was provided
     */
    @Override
    public @NotNull Optional<String> getReason() {
        return Optional.ofNullable(reason);
    }

    /**
     * Returns the time when this punishment was issued.
     * <p>
     * This is represented as {@link Instant}, which provides a precise point in time.
     * </p>
     *
     * @return the time when the punishment was issued
     */
    @Override
    public @NotNull Instant getIssuedAt() {
        return issuedAt;
    }

    /**
     * Returns the duration of this punishment.
     * <p>
     * The duration is represented as a {@link Duration}, which indicates how long the punishment lasts.
     * </p>
     *
     * @return the duration of the punishment
     */
    @Override
    public @NotNull Duration getDuration() {
        return duration;
    }

    /**
     * Returns the time when this punishment expires.
     * <p>
     * This is represented as {@link Instant}, which provides a precise point in time.
     * </p>
     *
     * @return the expiration time of the punishment
     */
    @NotNull @Override
    public Instant getExpiresAt() {
        return expiresAt;
    }

    /**
     * Retrieves all notes associated with the punished player or entity.
     *
     * @return a list of strings representing the punishment-related notes.
     */
    @NotNull
    @Override
    public List<String> getNotes() {
        return notes;
    }

    /**
     * Adds a new note to the punished player or entity's record.
     * Notes are typically used to store contextual or historical information
     * about punishments or player behavior that may not warrant a formal punishment.
     *
     * @param note the note to add; should not be null or empty.
     */
    @Override
    public void addNote(@NotNull String note) {
        this.notes.add(note);
    }

    /**
     * Clears all existing notes from the punished player or entity's record.
     * This action is irreversible and should be used with caution.
     */
    @Override
    public void clearNotes() {
        this.notes.clear();
    }

    /**
     * Checks if this punishment has been revoked.
     *
     * @return true if revoked, false otherwise
     */
    @Override
    public boolean isRevoked() {
        return revocationInfo != null;
    }

    /**
     * Gets the revocation information if this punishment was revoked.
     *
     * @return the revocation info, or empty if not revoked
     */
    @NotNull
    @Override
    public Optional<RevocationInfo> getRevocationInfo() {
        return Optional.ofNullable(revocationInfo);
    }

    /**
     * Revokes this punishment, and sets its {@link RevocationInfo}
     *
     * @param revocationInfo the info to set regarding the revocation.
     */
    @Override
    public void revoke(@NotNull RevocationInfo revocationInfo) {
        this.revocationInfo = revocationInfo;
    }

}
