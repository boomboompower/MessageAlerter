/*
 *     Copyright (C) 2017 boomboompower
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.boomboompower.messagealert;

import me.boomboompower.messagealert.commands.MainCommand;
import me.boomboompower.messagealert.config.ConfigLoader;
import me.boomboompower.messagealert.listeners.MainListener;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MessageAlerter.MOD_ID, version = MessageAlerter.VERSION, acceptedMinecraftVersions = "*")
public class MessageAlerter {

    public static final String MOD_ID = "messagealerter";
    public static final String VERSION = "1.0-SNAPSHOT";

    private static MessageAlerter instance;

    private ConfigLoader loader;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModMetadata data = event.getModMetadata();
        data.description = EnumChatFormatting.AQUA + "Play a sound when someone messages you";
        data.authorList.add("boomboompower");

        loader = new ConfigLoader(event.getSuggestedConfigurationFile());

        instance = this;
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        loader.load();

        Minecraft.getMinecraft().addScheduledTask(() -> {
            MinecraftForge.EVENT_BUS.register(new MainListener());
            ClientCommandHandler.instance.registerCommand(new MainCommand());
        });
    }

    public ConfigLoader getConfig() {
        return this.loader;
    }

    public static MessageAlerter getInstance() {
        return instance;
    }
}
