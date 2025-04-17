/*
 * Copyright (C) 2025  Tete
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.trs.bobbuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.gradle.api.Project;
import org.gradle.language.jvm.tasks.ProcessResources;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

import java.util.Arrays;
import java.util.Random;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import org.apache.commons.lang3.SystemUtils;

import org.jetbrains.annotations.NotNull;

public final class ReleaseUtils
{
    private static final String[] RELEASE_PREFIXES = {
            "Slow", "Quick", "Bright", "Dark", "Fast", "Blue", "Star", "Sun", "Wind",
            "Thunder", "Soft", "Hard", "High", "Low", "Night", "Day", "Cloud",
            "Fire", "Frost", "Sweet", "Earth", "Sky", "Golden", "Silver", "Shadow",
            "Swift", "Bold", "Iron", "Steel", "Mystic", "Electric", "Storm", "Rain",
            "Snow", "Crimson", "Emerald", "Ruby", "Amber", "Lunar", "Solar",
            "Cosmic", "Ocean", "Wave", "Echo", "Silent", "Ancient", "Frozen",
            "Burning", "Clever", "Gentle", "Wild", "Free", "Majestic", "Brightest",
            "Darkest", "Fierce", "Shining", "Blazing", "Soaring", "Falling",
            "Glowing", "Hidden", "Distant", "Endless", "Brave", "Calm", "Goldenrod",
            "Velvet", "Starlit", "Stormy", "Dew", "Drift", "Feather", "Gale",
            "Ironclad", "Pebble", "Radiant", "Shimmer", "Silentwood", "Spire",
            "Stoneheart", "Verdant", "Zephyr", "Hollow", "Whispering", "Boldest",
            "Ethereal", "Frosty", "Horizon", "Infinity", "Jagged", "Kindred",
            "Lucid", "Meadow", "Noble", "Primal", "Quiet", "Restless", "Serene",
            "Tenacious", "Umbra", "Valiant", "Wistful", "Zenith"
    };

    private static final String[] RELEASE_SUFFIXES = {
            "Bulbasaur", "Ivysaur", "Venusaur", "Charmander", "Charmeleon", "Charizard",
            "Squirtle", "Wartortle", "Blastoise", "Caterpie", "Metapod", "Butterfree",
            "Weedle", "Kakuna", "Beedrill", "Pidgey", "Pidgeotto", "Pidgeot",
            "Rattata", "Raticate", "Spearow", "Fearow", "Ekans", "Arbok",
            "Pikachu", "Raichu", "Sandshrew", "Sandslash", "Nidoran'F", "Nidorina",
            "Nidoqueen", "Nidoran'M", "Nidorino", "Nidoking", "Clefairy", "Clefable",
            "Vulpix", "Ninetales", "Jigglypuff", "Wigglytuff", "Zubat", "Golbat",
            "Oddish", "Gloom", "Vileplume", "Paras", "Parasect", "Venonat",
            "Venomoth", "Diglett", "Dugtrio", "Meowth", "Persian", "Psyduck",
            "Golduck", "Mankey", "Primeape", "Growlithe", "Arcanine", "Poliwag",
            "Poliwhirl", "Poliwrath", "Abra", "Kadabra", "Alakazam", "Machop",
            "Machoke", "Machamp", "Bellsprout", "Weepinbell", "Victreebel", "Tentacool",
            "Tentacruel", "Geodude", "Graveler", "Golem", "Ponyta", "Rapidash",
            "Slowpoke", "Slowbro", "Magnemite", "Magneton", "Farfetch'd", "Doduo",
            "Dodrio", "Seel", "Dewgong", "Grimer", "Muk", "Shellder",
            "Cloyster", "Gastly", "Haunter", "Gengar", "Onix", "Drowzee",
            "Hypno", "Krabby", "Kingler", "Voltorb", "Electrode", "Exeggcute"
    };

    private static byte[] mkVendorBytes()
    {
        return ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(mkVendorId()).array();
    }

    @Deprecated
    public static void setUpBuildEnvironment(@NotNull Project project)
    {

    }

    private static String getRandomName(final long seed)
    {
        Random random = new Random(seed);
        String prefix = RELEASE_PREFIXES[random.nextInt(RELEASE_PREFIXES.length-1)];
        String suffix = RELEASE_SUFFIXES[random.nextInt(RELEASE_SUFFIXES.length-1)];
        return (prefix + '-' + suffix).toLowerCase();
    }

    private static long mkVendorId()
    {
        long un = Math.abs(SystemUtils.getUserName().hashCode());
        long rn = Math.abs(SystemUtils.getHostName().hashCode());
        return (un + rn) + (rn/2);
    }

    public static void mkJsonMark(@NotNull ProcessResources processResources) throws IOException
    {
        final var sep = File.separatorChar;
        final var META_INF_PATH = new File(processResources.getDestinationDir().getPath() + sep + "META-INF");
        final var BUILD_STAMP_JSON_PATH = new File(META_INF_PATH.getPath() + sep + processResources.getProject().getName().toLowerCase() + ".BuildStamp.json");
        final var VENDOR_STAMP_PATH = new File(META_INF_PATH.getPath() + sep + processResources.getProject().getName().toLowerCase() + ".VendorStamp.bin");
        final var AUX_STAMP_PATH = new File(META_INF_PATH.getPath() + sep + processResources.getProject().getName().toLowerCase() + ".AuxStamp.bin");

        final String ver = (String)processResources.getProject().getVersion();
        final String date = String.valueOf(LocalDate.now().getMonthValue()) + '/' + LocalDate.now().getDayOfMonth() + '/' + LocalDate.now().getYear();
        final String time = String.valueOf(LocalTime.now().getHour()) + ':' + LocalTime.now().getHour() + ':' + LocalTime.now().getSecond();
        final int fbid = Math.abs((ver + date + time).hashCode());
        long vid = mkVendorId();

        final BuildStamp stamp = new BuildStamp(
                ver,
                date,
                time,
                getRandomName(vid),
                UUID.randomUUID(),
                fbid,
                vid
        );

        if (!META_INF_PATH.exists())
        {
            META_INF_PATH.mkdir();
        }

        if (!BUILD_STAMP_JSON_PATH.exists())
        {
            BUILD_STAMP_JSON_PATH.createNewFile();

            try (FileWriter s = new FileWriter(BUILD_STAMP_JSON_PATH))
            {
                //s.write(Qlang.builder().AddTagLibrary(instructions).build().parse(BUILD_STAMP_JSON).outputString());
                s.write(prettyPrintWithIndent(new Gson().toJson(stamp), 4));
            }
            catch (Throwable e)
            {
                throw new RuntimeException(e);
            }
        }

        if (!VENDOR_STAMP_PATH.exists())
        {
            VENDOR_STAMP_PATH.createNewFile();

            try (FileOutputStream s = new FileOutputStream(VENDOR_STAMP_PATH))
            {
                s.write("VID".getBytes(StandardCharsets.US_ASCII));
                s.write(mkVendorBytes());
                s.write("TRS".getBytes(StandardCharsets.US_ASCII));
            }
            catch (Throwable e)
            {
                throw new RuntimeException(e);
            }
        }


        if (!AUX_STAMP_PATH.exists())
        {
            AUX_STAMP_PATH.createNewFile();
            int stapHash = 0;

            try (FileInputStream s = new FileInputStream(BUILD_STAMP_JSON_PATH))
            {
                stapHash = Arrays.hashCode(s.readAllBytes());
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }

            try (FileOutputStream s = new FileOutputStream(AUX_STAMP_PATH))
            {
                byte[] auxBuff = ByteBuffer.allocate(Integer.BYTES)
                        .order(ByteOrder.LITTLE_ENDIAN)
                        .putInt(stapHash)
                        .array();
                s.write(auxBuff);
                s.write("TRS".getBytes(StandardCharsets.US_ASCII));
            }
            catch (Throwable e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    private static String prettyPrintWithIndent(String json, int indentSize)
    {
        JsonElement jsonElement = JsonParser.parseString(json);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Step 1: Convert JSON to a pretty-printed version with default indentation
        String prettyJson = gson.toJson(jsonElement);

        // Step 2: Define the custom indentation string
        String indent = " ".repeat(indentSize);

        // Step 3: Use StringBuilder to construct the final string with custom indentation
        StringBuilder indentedJson = new StringBuilder();
        int currentIndentLevel = 0;

        // Split the JSON into lines and apply custom indentation per line
        for (String line : prettyJson.split("\n"))
        {
            String trimmedLine = line.trim();

            // Decrease indent level for closing braces/brackets
            if (trimmedLine.startsWith("}") || trimmedLine.startsWith("]"))
            {
                currentIndentLevel--;
            }

            // Apply current indentation and add line
            indentedJson.append(indent.repeat(currentIndentLevel)).append(trimmedLine).append("\n");

            // Increase indent level after opening braces/brackets
            if (trimmedLine.endsWith("{") || trimmedLine.endsWith("["))
            {
                currentIndentLevel++;
            }
        }

        return indentedJson.toString().trim();
    }
}
