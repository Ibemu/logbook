package logbook.internal;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Version info
 *
 */
public final class Version implements Comparable<Version> {

    /** major */
    private final int major;

    /** minor */
    private final int minor;

    /** revision */
    private final int revision;

    /**
     * Version constructor
     *
     * @param major
     * @param minor
     * @param revision
     */
    public Version(int major, int minor, int revision) {
        this.major = major;
        this.minor = minor;
        this.revision = revision;
    }

    /**
     * Version constructor
     *
     * @param version
     */
    public Version(String version) {
        int major = 0, minor = 0, revision = 0;
        Iterator<String> ite = Arrays.asList(version.split("\\.")).iterator();
        if (ite.hasNext()) {
            major = Integer.parseInt(ite.next());
        }
        if (ite.hasNext()) {
            minor = Integer.parseInt(ite.next());
        }
        if (ite.hasNext()) {
            revision = Integer.parseInt(ite.next());
        }
        this.major = major;
        this.minor = minor;
        this.revision = revision;
    }

    /**
     * majorを取得します。
     * @return major
     */
    public int getMajor() {
        return this.major;
    }

    /**
     * minorを取得します。
     * @return minor
     */
    public int getMinor() {
        return this.minor;
    }

    /**
     * revisionを取得します。
     * @return revision
     */
    public int getRevision() {
        return this.revision;
    }

    @Override
    public String toString() {
        String version = this.major + "." + this.minor;
        if (this.revision > 0) {
            version += "." + this.revision;
        }
        return version;
    }

    public String toStringFull() {
        return this.major + "." + this.minor + "." + this.revision;
    }

    @Override
    public int compareTo(Version o) {
        if (this.major != o.major)
            return Integer.compare(this.major, o.major);
        if (this.minor != o.minor)
            return Integer.compare(this.minor, o.minor);
        if (this.revision != o.revision)
            return Integer.compare(this.revision, o.revision);
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Version) && (this.compareTo((Version) o) == 0);
    }

    @Override
    public int hashCode() {
        return (this.major << 16) | (this.minor << 8) | this.revision;
    }
}
