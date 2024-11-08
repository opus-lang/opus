package dev.opuslang.opus.api.plugin;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Version Identifier for Plugins.
 *
 * @param major non-negative integer
 * @param minor non-negative integer
 * @param patch non-negative integer
 */
public record PluginVersion(int major, int minor, int patch) implements Comparable<PluginVersion> {

    public PluginVersion{
        if(major < 0 || minor < 0 || patch < 0) throw new IllegalArgumentException("Invalid version: all values must be non-negative.");
    }

    /**
     * Returns an {@code PluginVersion} object holding the value
     * extracted from the specified {@code String}.
     *
     * @param version the string to be parsed.
     *                <p>Format: <code>major [ ‘.’ minor [ ‘.’ patch ]]</code></p>
     * @return
     */
    public static PluginVersion valueOf(String version){
        if(version == null) throw new IllegalArgumentException();
        version = version.trim();

        int major = 0;
        int minor = 0;
        int patch = 0;

        try{
            StringTokenizer tokenizer = new StringTokenizer(version, ".", true);
            major = Integer.parseInt(tokenizer.nextToken());

            if(tokenizer.hasMoreTokens()){
                tokenizer.nextToken(); // consume delimiter
                minor = Integer.parseInt(tokenizer.nextToken());
            }

            if(tokenizer.hasMoreTokens()){
                tokenizer.nextToken(); // consume delimiter
                patch = Integer.parseInt(tokenizer.nextToken());
            }

            if (tokenizer.hasMoreTokens()){
                throw new IllegalArgumentException();
            }
        }catch (NoSuchElementException | IllegalArgumentException e){
            throw new IllegalArgumentException("Invalid version string: '" + version + "'.");
        }
        return new PluginVersion(major, minor, patch);
    }


    @Override
    public int compareTo(PluginVersion other) {
        if(this == other) return 0;

        if(this.major() != other.major()) return this.major() - other.major();
        if(this.minor() != other.minor()) return this.minor() - other.minor();
        if(this.patch() != other.patch()) return this.patch() - other.patch();

        return 0;
    }
}
