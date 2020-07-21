import java.util.Objects;

public class Pair implements Comparable<Pair>{
    int val1;
    int val2;

    public Pair(int val1, int val2) {
        this.val1 = val1;
        this.val2= val2;
    }

    public int[] get() {
        return new int[]{val1, val2};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return (val1 == pair.val1 && val2 == pair.val2)||
                (val1 == pair.val2 && val2 == pair.val1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(val1, val2);
    }

    @Override
    public String toString() {
        return val1 + "," + val2;
    }



    @Override
    public int compareTo(Pair o) {
        return val1+val2-o.val1-o.val2;
    }
}
