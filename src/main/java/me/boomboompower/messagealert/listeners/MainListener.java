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

package me.boomboompower.messagealert.listeners;

import me.boomboompower.messagealert.MessageAlerter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MainListener {

    @SubscribeEvent
    public void onChatRecieve(ClientChatReceivedEvent event) {
        if (event.message.getFormattedText() == null) return;

        String message = event.message.getFormattedText();
        if (message.startsWith(EnumChatFormatting.LIGHT_PURPLE + "From") || (MessageAlerter.getInstance().getConfig().useTo() && message.startsWith(EnumChatFormatting.LIGHT_PURPLE + "To"))) {
            playAlert();
        }
    }

    private void playAlert() {
        Minecraft.getMinecraft().thePlayer.playSound(MessageAlerter.getInstance().getConfig().getAlertSound(), MessageAlerter.getInstance().getConfig().getVolume(), MessageAlerter.getInstance().getConfig().getPitch());
    }
}
