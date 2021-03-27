package chess;

public enum Color {

    BLACK("Black"), WHITE("White");

    private String color;

    Color(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
