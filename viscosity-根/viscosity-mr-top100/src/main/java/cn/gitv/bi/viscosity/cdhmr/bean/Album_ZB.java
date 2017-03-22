package cn.gitv.bi.viscosity.cdhmr.bean;

/**
 * Created by Kang on 2016/11/19.
 */
public class Album_ZB implements Comparable<Album_ZB> {
    private String album;
    private String albumId;
    private double zb;
    private int timeLength;

    public Album_ZB(String album, String albumId, double zb, int timeLength) {
        this.zb = zb;
        this.albumId = albumId;
        this.album = album;
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

    public double getZb() {
        return zb;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Album_ZB) {
            Album_ZB obj1 = (Album_ZB) obj;
            if (this.albumId != null) {
                return this.albumId.equals(obj1.getAlbumId()) && this.timeLength == obj1.getTimeLength() && this.zb == obj1.getZb();
            } else {
                return this.album.equals(obj1.getAlbum()) && this.timeLength == obj1.getTimeLength() && this.zb == obj1.getZb();
            }
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Album_ZB o) {
        double x = this.getZb();
        double y = o.getZb();
        return (x > y) ? -1 : ((x == y) ? 0 : 1);
    }
}
