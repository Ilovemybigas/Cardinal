package eg.mqzen.cardinal.punishments.gui;

import eg.mqzen.cardinal.Cardinal;
import eg.mqzen.cardinal.api.punishments.Punishment;
import eg.mqzen.cardinal.util.TimeUtil;
import io.github.mqzen.menus.base.pagination.PageComponent;
import io.github.mqzen.menus.base.pagination.PageView;
import io.github.mqzen.menus.misc.itembuilder.LegacyItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class PunishmentPageComponent implements PageComponent {

    private final Punishment<?> punishment;

    public PunishmentPageComponent(Punishment<?> punishment) {
        this.punishment = punishment;
    }

    /**
     * Creates an ItemStack representation of this punishment for display in the GUI.
     * The item uses a paper material with the punishment ID as the display name
     * and detailed information about the punishment in the lore.
     *
     * @return an ItemStack representing this punishment component
     */
    @Override
    public ItemStack toItem() {
        List<String> lore = new ArrayList<>();
        lore.add("&7----------------");
        lore.add("");
        lore.add("&7Type: &f" + punishment.getType().name());
        lore.add("&7Target: &f" + punishment.getTarget().getTargetName());
        lore.add("&7Issuer: &f" + punishment.getIssuer().getName());
        lore.add("");

        // Add reason if present
        if (punishment.getReason().isPresent()) {
            var cfg = Cardinal.getInstance().getConfigYaml();
            if(cfg == null) {
                throw new IllegalStateException();
            }
            String defReason = Cardinal.getInstance().getConfigYaml().getString("default-reason");
            lore.add("&7Reason: &f" + punishment.getReason().orElse(defReason));
            lore.add("");
        }

        // Add duration and expiration info
        if (punishment.isPermanent()) {
            lore.add("&7Duration: &cPermanent");
        } else {
            lore.add("&7Duration: &f" + TimeUtil.format(punishment.getDuration()));

            Instant expiresAt = punishment.getExpiresAt();
            if(expiresAt == null) {
                lore.add("&7Expires: &cNever");
            } else {
                lore.add("&7Expires: &f" + TimeUtil.formatDate(expiresAt));
            }
        }

        lore.add("&7Issued: &f" + TimeUtil.formatDate(punishment.getIssuedAt()));
        lore.add("");

        // Add status
        if (punishment.isRevoked()) {
            lore.add("&7Status: &aRevoked");
            punishment.getRevocationInfo().ifPresent(info -> {
                lore.add("&7Revoked by: &f" + info.getRevoker().getName());
                lore.add("&7Revoke reason: &f" + info.getReason());
            });
        } else if (punishment.hasExpired()) {
            lore.add("&7Status: &eExpired");
        } else {
            lore.add("&7Status: &cActive");
        }

        // Add notes if present
        if (!punishment.getNotes().isEmpty()) {
            lore.add("");
            lore.add("&7Notes:");
            for (String note : punishment.getNotes()) {
                lore.add("&f- " + note);
            }
        }

        lore.add("");
        lore.add("&7----------------");

        return LegacyItemBuilder.legacy(Material.PAPER)
                .setDisplay("&6#" + punishment.getId().getRepresentation())
                .setLore(lore)
                .build();
    }

    @Override
    public void onClick(PageView pageView, InventoryClickEvent event) {
        event.setCancelled(true);
    }
}
