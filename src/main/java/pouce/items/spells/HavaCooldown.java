package pouce.items.spells;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HavaCooldown {
    private static final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    public static boolean isOnCooldown(Player player, String spellId) {
        Map<String, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (playerCooldowns == null) {
            return false;
        }

        Long cooldownEnd = playerCooldowns.get(spellId);
        if (cooldownEnd == null) {
            return false;
        }

        return System.currentTimeMillis() < cooldownEnd;
    }

    public static long getCooldownRemaining(Player player, String spellId) {
        Map<String, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (playerCooldowns == null) {
            return 0;
        }

        Long cooldownEnd = playerCooldowns.get(spellId);
        if (cooldownEnd == null) {
            return 0;
        }

        long timeRemaining = cooldownEnd - System.currentTimeMillis();
        return timeRemaining > 0 ? timeRemaining : 0;
    }

    public static void setCooldown(Player player, String spellId, long cooldownInMillis) {
        cooldowns.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>()).put(spellId, System.currentTimeMillis() + cooldownInMillis);
    }
}
