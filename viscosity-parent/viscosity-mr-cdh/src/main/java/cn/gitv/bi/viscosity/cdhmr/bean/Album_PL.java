package cn.gitv.bi.viscosity.cdhmr.bean;

public class Album_PL implements Comparable<Album_PL> {
    private String album;
    private String album_id;
    private int play_length;
    private int time_length;

    public Album_PL(String album, String album_id, int play_length, int time_length) {
        this.album_id = album_id;
        this.album = album;
        this.play_length = play_length;
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

    public int getPlay_length() {
        return play_length;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Album_PL) {
            Album_PL obj1 = (Album_PL) obj;
            if (this.album_id != null) {
                return this.album_id.equals(obj1.getAlbum_id()) && this.play_length == obj1.getPlay_length() && this.time_length == obj1.getTime_length();
            } else {
                return this.album.equals(obj1.getAlbum()) && this.play_length == obj1.getPlay_length() && this.time_length == obj1.getTime_length();
            }
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Album_PL o) {
        int x = this.getPlay_length();
        int y = o.getPlay_length();
        return (x > y) ? -1 : ((x == y) ? 0 : 1);
    }
}
