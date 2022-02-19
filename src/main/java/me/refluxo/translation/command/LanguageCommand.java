package me.refluxo.translation.command;

import me.refluxo.moduleloader.module.ModuleCommand;
import me.refluxo.moduleloader.module.ModuleListener;
import me.refluxo.moduleloader.module.ModuleCommandExecutor;
import me.refluxo.moduleloader.util.inventory.ItemUtil;
import me.refluxo.moduleloader.util.inventory.PlayerHead;
import me.refluxo.translation.util.Lang;
import me.refluxo.translation.util.TranslationUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@ModuleListener
@ModuleCommand(command = "language", description = "A command to change the player language", aliases = {"lang", "langs"}, permissions = {}, tabCompleterIsEnabled = true, usage = "/language <LANGUAGE>")
public class LanguageCommand extends ModuleCommandExecutor implements Listener {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        var translator = new TranslationUtil();
        if (args.length != 1) {
            Inventory inventory = Bukkit.createInventory(null, InventoryType.DROPPER, Component.text("§e§l" + translator.getTranslation((Player) sender, "translation.module.gui.command.language.title", "Übersetzungen")).asComponent());
            inventory.setItem(0, new ItemUtil("§e§lDeutsch", PlayerHead.getItemStackWithTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWU3ODk5YjQ4MDY4NTg2OTdlMjgzZjA4NGQ5MTczZmU0ODc4ODY0NTM3NzQ2MjZiMjRiZDhjZmVjYzc3YjNmIn19fQ=="), "", "§bWechsle die Sprache auf §e§lDeutsch", "").buildItem());
            inventory.setItem(1, new ItemUtil("§c§lEnglish", PlayerHead.getItemStackWithTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmNiYzMyY2IyNGQ1N2ZjZGMwMzFlODUxMjM1ZGEyZGFhZDNlMTkxNGI4NzA0M2JkMDEyNjMzZTZmMzJjNyJ9fX0="), "", "§bSwitch the language to §c§lEnglish", "").buildItem());
            inventory.setItem(2, new ItemUtil("§f§lFrancés", PlayerHead.getItemStackWithTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTEyNjlhMDY3ZWUzN2U2MzYzNWNhMWU3MjNiNjc2ZjEzOWRjMmRiZGRmZjk2YmJmZWY5OWQ4YjM1Yzk5NmJjIn19fQ=="), "", "§bCambiar el idioma a §f§lFrancés", "").buildItem());
            inventory.setItem(3, new ItemUtil("§b§lNederlands", PlayerHead.getItemStackWithTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzIzY2YyMTBlZGVhMzk2ZjJmNWRmYmNlZDY5ODQ4NDM0ZjkzNDA0ZWVmZWFiZjU0YjIzYzA3M2IwOTBhZGYifX19"), "", "§bVerander de taal in §b§lNederlands", "").buildItem());
            inventory.setItem(4, new ItemUtil("§6§lEspañol", PlayerHead.getItemStackWithTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJiZDQ1MjE5ODMzMDllMGFkNzZjMWVlMjk4NzQyODc5NTdlYzNkOTZmOGQ4ODkzMjRkYThjODg3ZTQ4NWVhOCJ9fX0="), "", "§bCambiar el idioma a §6§lEspañol", "").buildItem());
            ((Player) sender).openInventory(inventory);
        } else {
            sender.sendMessage("§b§lTranslations §8» §7" + translator.getTranslation((Player) sender, "translation.module.gui.command.language.changed", "Du hast deine Sprache auf %l_a_n_g% gewechselt.").replaceAll("%l_a_n_g%", Lang.valueOf(args[0]).name()));
        }
        return false;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        var translator = new TranslationUtil();
        if(event.getView().getTitle().equalsIgnoreCase("§e§l" + translator.getTranslation((Player) event.getWhoClicked(), "translation.module.gui.command.language.title", "Übersetzungen"))) {
            if(event.getCurrentItem() != null) {
                event.setCancelled(true);
                if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§e§lDeutsch")) {
                    translator.updateLanguage((Player) event.getWhoClicked(), Lang.DE);
                    event.getWhoClicked().sendMessage("§b§lTranslations §8» §7" + translator.getTranslation((Player) event.getWhoClicked(), "translation.module.gui.command.language.changed", "Du hast deine Sprache auf %p gewechselt.", Lang.DE.name()));
                    event.getWhoClicked().closeInventory();
                } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§c§lEnglish")) {
                    translator.updateLanguage((Player) event.getWhoClicked(), Lang.EN);
                    event.getWhoClicked().sendMessage("§b§lTranslations §8» §7" + translator.getTranslation((Player) event.getWhoClicked(), "translation.module.gui.command.language.changed", "Du hast deine Sprache auf %p gewechselt.", Lang.EN.name()));
                    event.getWhoClicked().closeInventory();
                } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§f§lFrancés")) {
                    translator.updateLanguage((Player) event.getWhoClicked(), Lang.FR);
                    event.getWhoClicked().sendMessage("§b§lTranslations §8» §7" + translator.getTranslation((Player) event.getWhoClicked(), "translation.module.gui.command.language.changed", "Du hast deine Sprache auf %p gewechselt.", Lang.FR.name()));
                    event.getWhoClicked().closeInventory();
                } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§b§lNederlands")) {
                    translator.updateLanguage((Player) event.getWhoClicked(), Lang.NL);
                    event.getWhoClicked().sendMessage("§b§lTranslations §8» §7" + translator.getTranslation((Player) event.getWhoClicked(), "translation.module.gui.command.language.changed", "Du hast deine Sprache auf %p gewechselt.", Lang.NL.name()));
                    event.getWhoClicked().closeInventory();
                } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6§lEspañol")) {
                    translator.updateLanguage((Player) event.getWhoClicked(), Lang.ES);
                    event.getWhoClicked().sendMessage("§b§lTranslations §8» §7" + translator.getTranslation((Player) event.getWhoClicked(), "translation.module.gui.command.language.changed", "Du hast deine Sprache auf %p gewechselt.", Lang.ES.name()));
                    event.getWhoClicked().closeInventory();
                }
            }
        }
    }

    @Override
    public List<String> getTabCompletions(String[] args) {
        List<String> completions = new ArrayList<>();
        if(args.length == 0) {
            for(Lang l : Lang.values()) {
                completions.add(l.name());
            }
        }
        return completions;
    }
}
