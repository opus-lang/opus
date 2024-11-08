package dev.opuslang.opus.api.plugin;

/**
 * Range of Version Identifiers for Plugins.
 *
 * @param lowerInclusive
 * @param lowerBound
 * @param upperBound
 * @param upperInclusive
 */
public record PluginVersionRange(boolean lowerInclusive, PluginVersion lowerBound, PluginVersion upperBound, boolean upperInclusive) {

    public PluginVersionRange{
        if (lowerBound.compareTo(upperBound) >= 0) throw new IllegalArgumentException("Lower bound must be smaller than the upper bound.");
    }

    /**
     * Returns an {@code PluginVersionRange} object holding the value
     * extracted from the specified {@code String}.
     *
     * @param range the string to be parsed.
     *                <p>Format: <code>(‘[’|‘(’) version‘,’version (‘)’|‘]’)</code></p>
     * @return
     */
    public static PluginVersionRange valueOf(String range){
        if(range == null) throw new IllegalArgumentException();
        range = range.trim();

        if((range.startsWith("[") || range.startsWith("(")) && (range.endsWith("]") || range.endsWith(")"))){
            boolean lowerInclusive = range.startsWith("[");
            boolean upperInclusive = range.endsWith("]");
            if(!lowerInclusive && !range.startsWith("(")) throw new IllegalArgumentException();
            if(!upperInclusive && !range.endsWith(")")) throw new IllegalArgumentException();

            String[] bounds = range.substring(1, range.length() - 1).split(",");
            if (bounds.length != 2) throw new IllegalArgumentException("Range must contain exactly one comma");

            PluginVersion lowerBound = PluginVersion.valueOf(bounds[0]);
            PluginVersion upperBound = PluginVersion.valueOf(bounds[1]);

            return new PluginVersionRange(lowerInclusive, lowerBound, upperBound, upperInclusive);
        } else {
            PluginVersion lowerBound = PluginVersion.valueOf(range);
            return new PluginVersionRange(true, lowerBound, new PluginVersion(lowerBound.major(), lowerBound.minor(), lowerBound.patch()+1), false);
        }
    }

    public boolean contains(PluginVersion version){
        int compareToLower = version.compareTo(lowerBound);
        int compareToUpper = version.compareTo(upperBound);

        boolean aboveLower = this.lowerInclusive ? compareToLower >= 0 : compareToLower > 0;
        boolean belowUpper = this.upperInclusive ? compareToUpper <= 0 : compareToUpper < 0;

        return aboveLower && belowUpper;
    }

}
