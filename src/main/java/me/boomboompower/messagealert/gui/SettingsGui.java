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

package me.boomboompower.messagealert.gui;

import me.boomboompower.messagealert.MessageAlerter;

import me.boomboompower.messagealert.gui.utils.ModernButton;
import me.boomboompower.messagealert.gui.utils.ModernTextBox;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;

public class SettingsGui extends GuiScreen {

    private String message = "";

    private ModernTextBox textField;

    private ModernButton sound;
    private ModernButton volume;
    private ModernButton pitch;

    public SettingsGui() {
        this("");
    }

    public SettingsGui(String message) {
        this.message = message;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        this.textField = new ModernTextBox(0, this.width / 2 - 100, this.height / 2 - 24, 200, 20);

        this.buttonList.add(this.sound = new ModernButton(1, this.width / 2 - 100, this.height / 2 + 26, 200, 20, "Set Sound"));
        this.buttonList.add(this.volume = new ModernButton(2, this.width / 2 - 100, this.height / 2 + 50, 200, 20, "Set Volume"));
        this.buttonList.add(this.pitch = new ModernButton(3, this.width / 2 - 100, this.height / 2 + 74, 200, 20, "Set Pitch"));
        this.buttonList.add(new ModernButton(4, this.width / 2 - 100, this.height / 2 + 98, 200, 20, "Trigger on send: " + a()));

        this.textField.setMaxStringLength(16);
        this.textField.setText(message);

        sound.enabled = false;
        volume.enabled = false;
        pitch.enabled = false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        textField.setEnabled(true);
        textField.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);

        a("Created by boomboompower");
        a("Sound Settings", this.width / 2, this.height / 2 + 8, Color.WHITE.getRGB());

        if (textField.getText().isEmpty()) {
            sound.enabled = false;
            volume.enabled = false;
            pitch.enabled = false;
            return;
        }

        sound.enabled = true;

        try {
            float dummyVar = Float.valueOf(textField.getText());

            volume.enabled = true;
            pitch.enabled = true;
        } catch (NumberFormatException ex) {
            volume.enabled = false;
            pitch.enabled = false;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1:
                MessageAlerter.getInstance().getConfig().setAlertSound(textField.getText());
                sendChatMessage(String.format("Alert sound was set to %s", a(EnumChatFormatting.GOLD, MessageAlerter.getInstance().getConfig().getAlertSound())));
                break;
            case 2:
                MessageAlerter.getInstance().getConfig().setVolume(Float.valueOf(textField.getText()));
                sendChatMessage(String.format("Volume was set to %s", a(EnumChatFormatting.GOLD, MessageAlerter.getInstance().getConfig().getVolume())));
                break;
            case 3:
                MessageAlerter.getInstance().getConfig().setPitch(Float.valueOf(textField.getText()));
                sendChatMessage(String.format("Pitch was set to %s", a(EnumChatFormatting.GOLD, MessageAlerter.getInstance().getConfig().getPitch())));
                break;
            case 4:
                MessageAlerter.getInstance().getConfig().setUseTo(!MessageAlerter.getInstance().getConfig().useTo());
                button.displayString = "Trigger on send: " + a();
                sendChatMessage(String.format("%s triggering when sending messages", (MessageAlerter.getInstance().getConfig().useTo() ? a(EnumChatFormatting.GREEN, "Now") : a(EnumChatFormatting.RED, "No longer"))));
                break;
        }
        mc.displayGuiScreen(null);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        MessageAlerter.getInstance().getConfig().save();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void keyTyped(char c, int key) {
        if (key == 1) {
            mc.displayGuiScreen(null);
        } else {
            textField.textboxKeyTyped(c, key);
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int btn) {
        try {
            super.mouseClicked(x, y, btn);
            textField.mouseClicked(x, y, btn);
        } catch (Exception ex) {}
    }

    @Override
    public void sendChatMessage(String msg) {
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GRAY + msg));
    }

    public void display() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        MinecraftForge.EVENT_BUS.unregister(this);
        Minecraft.getMinecraft().displayGuiScreen(this);
    }

    private String a() {
        return MessageAlerter.getInstance().getConfig().useTo() ? a(EnumChatFormatting.GREEN, "Yes") : a(EnumChatFormatting.RED, "No");
    }

    private void a(String a) {
        a(a, this.width / 2, this.height / 2 - 48, Color.WHITE.getRGB());
        drawHorizontalLine(this.width / 2 - mc.fontRendererObj.getStringWidth(a) / 2 - 5, this.width / 2 + mc.fontRendererObj.getStringWidth(a) / 2 + 5, this.height / 2 - 38, Color.WHITE.getRGB());
    }

    private String a(EnumChatFormatting a, Object b) {
        return a + b.toString() + EnumChatFormatting.GRAY;
    }

    private void a(String message, int x, int y, int color) {
        fontRendererObj.drawString(message, (float) (x - fontRendererObj.getStringWidth(message) / 2), (float) y, color, false);
    }
}
