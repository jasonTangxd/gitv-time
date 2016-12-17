package cn.gitv.bi.viscosity.cdhmr.bean;

/**
 * Created by Kang on 2016/11/19.
 */
public class Album_ZB implements Comparable<Album_ZB> {
    private String album;
    private String album_id;
    private double zb;
    private int time_length;

    public Album_ZB(String album, String album_id, double zb, int time_length) {
        this.zb = zb;
        this.album_id = album_id;
        this.album = album;
        this.time_length = time_length;
    }

    public int getTime_length() {
        return time_length;
    }

    public String getAlbum_id() {
        return album_id;
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
            if (this.album_id != null) {
                return this.album_id.equals(obj1.getAlbum_id()) && this.time_length == obj1.getTime_length() && this.zb == obj1.getZb();
            } else {
                return this.album.equals(obj1.getAlbum()) && this.time_length == obj1.getTime_length() && this.zb == obj1.getZb();
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
