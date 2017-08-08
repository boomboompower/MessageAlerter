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

package me.boomboompower.messagealert.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigLoader {

    // SETTINGS - START
    private boolean useTo = false;

    private String alertSound = "note.pling";
    private float volume = 1F;
    private float pitch = 0.5F;

    // SETTINGS - END

    private File configFile;
    private JsonObject configJson;

    public ConfigLoader(File configFile) {
        this.configFile = configFile;
    }

    public void load() {
        if (exists()) {
            try {
                FileReader fileReader = new FileReader(configFile);
                BufferedReader reader = new BufferedReader(fileReader);
                StringBuilder builder = new StringBuilder();

                String current;
                while ((current = reader.readLine()) != null) {
                    builder.append(current);
                }
                configJson = new JsonParser().parse(builder.toString()).getAsJsonObject();
            } catch (Exception ex) {
                log("Could not read config properly, may be corrupt.");
                save();
            }
            alertSound = (configJson.has("alertSound") ? configJson.get("alertSound").getAsString() : "note.pling");
            useTo = (configJson.has("useTo") && configJson.get("useTo").getAsBoolean());
            volume = (configJson.has("volume") ? configJson.get("volume").getAsInt() : 10);
            pitch = (configJson.has("pitch") ? configJson.get("pitch").getAsInt() : 0);
        } else {
            save();
        }
    }

    public void save() {
        configJson = new JsonObject();
        try {
            configFile.createNewFile();
            FileWriter writer = new FileWriter(configFile);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            configJson.addProperty("alertSound", alertSound);
            configJson.addProperty("useTo", useTo);
            configJson.addProperty("volume", volume);
            configJson.addProperty("pitch", pitch);

            bufferedWriter.write(configJson.toString());
            bufferedWriter.close();
            writer.close();
        } catch (Exception ex) {
            log("An error occured while saving the config");
            log("Report this to boomboompower!");
        }
    }

    public boolean exists() {
        return Files.exists(Paths.get(configFile.getPath()));
    }

    public File getConfigFile() {
        return this.configFile;
    }

    protected void log(String message, Object... replace) {
        System.out.println(String.format("[%s] " + message, replace));
    }

    // SETTINGS - START

    public String getAlertSound() {
        return this.alertSound;
    }

    public void setAlertSound(String soundIn) {
        this.alertSound = soundIn;
    }

    public boolean useTo() {
        return this.useTo;
    }

    public void setUseTo(boolean useTo) {
        this.useTo = useTo;
    }

    public float getVolume() {
        return this.volume;
    }

    public void setVolume(float volumeIn) {
        this.volume = volumeIn;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitchIn) {
        this.pitch = pitchIn;
    }

    // SETTINGS - END
}
