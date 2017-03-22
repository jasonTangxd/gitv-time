package cn.gitv.bi.viscosity.cdhmr.bean;

public class Album_PL implements Comparable<Album_PL> {
    private String album;
    private String albumId;
    private long playLength;
    private int timeLength;

    public Album_PL(String album, String albumId, long playLength, int timeLength) {
        this.albumId = albumId;
        this.album = album;
        this.playLength = playLength;
        this.timeLength = timeLength;
    }

    public int getTimeLength() {
        return timeLength;
    }

    public String getAlbumId() {
        return albumId;
    }

    public String getAlbum() {
        return album;
    }

    public long getPlayLength() {
        return playLength;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Album_PL) {
            Album_PL obj1 = (Album_PL) obj;
            if (this.albumId != null) {
                return this.albumId.equals(obj1.getAlbumId()) && this.playLength == obj1.getPlayLength() && this.timeLength == obj1.getTimeLength();
            } else {
                return this.album.equals(obj1.getAlbum()) && this.playLength == obj1.getPlayLength() && this.timeLength == obj1.getTimeLength();
            }
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Album_PL o) {
        long x = this.getPlayLength();
        long y = o.getPlayLength();
        return (x > y) ? -1 : ((x == y) ? 0 : 1);
    }
}
