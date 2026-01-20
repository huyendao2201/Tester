package org;

public class HinhChuNhat {
    private final Diem trenTrai;
    private final Diem duoiPhai;

    public HinhChuNhat(Diem trenTrai, Diem duoiPhai) {
        if (trenTrai == null || duoiPhai == null) {
            throw new IllegalArgumentException("Invalid Data");
        }
        // Điểm trên trái: x nhỏ hơn, y lớn hơn
        // Điểm dưới phải: x lớn hơn, y nhỏ hơn
        if (trenTrai.getX() > duoiPhai.getX() || trenTrai.getY() < duoiPhai.getY()) {
            throw new IllegalArgumentException("Invalid Data");
        }

        this.trenTrai = trenTrai;
        this.duoiPhai = duoiPhai;
    }

    public Diem getTrenTrai() { return trenTrai; }
    public Diem getDuoiPhai() { return duoiPhai; }

    public double dienTich() {
        double width = duoiPhai.getX() - trenTrai.getX();
        double height = trenTrai.getY() - duoiPhai.getY();
        return width * height;
    }

    // Giao nhau
    public boolean giaoNhau(HinhChuNhat other) {
        if (other == null) return false;

        double leftA = this.trenTrai.getX();
        double rightA = this.duoiPhai.getX();
        double topA = this.trenTrai.getY();
        double bottomA = this.duoiPhai.getY();

        double leftB = other.trenTrai.getX();
        double rightB = other.duoiPhai.getX();
        double topB = other.trenTrai.getY();
        double bottomB = other.duoiPhai.getY();

        // Không giao nhau nếu 1 cái nằm hoàn toàn bên trái/phải/trên/dưới cái kia
        if (rightA <= leftB) return false;   // A ở bên trái B (hoặc chạm)
        if (rightB <= leftA) return false;   // B ở bên trái A (hoặc chạm)
        if (bottomA >= topB) return false;   // A ở dưới B (hoặc chạm) (trục y tăng lên phía trên)
        if (bottomB >= topA) return false;   // B ở dưới A (hoặc chạm)

        return true;
    }
}
