import java.util.Hashtable;
import java.util.ArrayList;
import java.math.RoundingMode;
import java.math.BigDecimal;
import java.math.BigInteger;

public class Cryptography {

    // EXTENDED EUCLIDEAN

    public static BigInteger[] extendedEuclideanAlgorithm(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) {
            return new BigInteger[] { a, BigInteger.ONE, BigInteger.ZERO };
        }

        BigInteger u = BigInteger.ONE;
        BigInteger g = a;
        BigInteger x = BigInteger.ZERO;
        BigInteger y = b;

        while (!y.equals(BigInteger.ZERO)) {
            BigInteger t = g.mod(y);
            BigInteger q = g.subtract(t).divide(y);
            BigInteger s = u.subtract(q.multiply(x));

            u = x;
            g = y;
            x = s;
            y = t;
        }

        BigInteger v = (g.subtract(a.multiply(u))).divide(b);
        while (u.compareTo(BigInteger.ZERO) == -1) {
            u = u.add(b.divide(g));
            v = v.subtract(a.divide(g));
        }

        return new BigInteger[] { g, u, v };
    }

    static String formatExtendedEuclideanResults(BigInteger[] result, BigInteger a, BigInteger b) {
        return (
            result[1] + "·" + a +  " + " +
            result[2] + "·" + b +  " = " +
            result[0]
        );
    }

    static void runExtendedEuclidean() {
        BigInteger a = new BigInteger("2024");
        BigInteger b = new BigInteger("748");

        System.out.println("Example from book:");
        System.out.println(formatExtendedEuclideanResults(
            extendedEuclideanAlgorithm(a, b), a, b)
        );

        System.out.println("\n1.12 Exercise 'c':");

        System.out.print("  i)   ");
        a = new BigInteger("527");
        b = new BigInteger("1258");
        System.out.println(formatExtendedEuclideanResults(
            extendedEuclideanAlgorithm(a, b), a, b)
        );

        System.out.print("  ii)  ");
        a = new BigInteger("228");
        b = new BigInteger("1056");
        System.out.println(formatExtendedEuclideanResults(
            extendedEuclideanAlgorithm(a, b), a, b)
        );

        System.out.print("  iii) ");
        a = new BigInteger("163961");
        b = new BigInteger("167181");
        System.out.println(formatExtendedEuclideanResults(
            extendedEuclideanAlgorithm(a, b), a, b)
        );

        System.out.print("  iv)  ");
        a = new BigInteger("3892394");
        b = new BigInteger("239847");
        System.out.println(formatExtendedEuclideanResults(
            extendedEuclideanAlgorithm(a, b), a, b)
        );
    }

    // SHANK'S ALGORITHM

    // https://stackoverflow.com/questions/4407839/how-can-i-find-the-square-root-of-a-java-biginteger
    static BigInteger bigIntSqRootCeil(BigInteger x)
            throws IllegalArgumentException {
        if (x.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("Negative argument.");
        }

        if (x == BigInteger.ZERO || x == BigInteger.ONE) {
            return x;
        }

        BigInteger two = BigInteger.valueOf(2L);
        BigInteger y;
        for (
            y = x.divide(two);
            y.compareTo(x.divide(y)) > 0;
            y = ((x.divide(y)).add(y)).divide(two)
        );

        if (x.compareTo(y.multiply(y)) == 0) {
            return y;
        } else {
            return y.add(BigInteger.ONE);
        }
    }

    static BigInteger shanksAlgorithm(BigInteger g, BigInteger h, BigInteger N) {
        BigInteger n = bigIntSqRootCeil(N);

        Hashtable<BigInteger, BigInteger> table =
            new Hashtable<BigInteger, BigInteger>();

        for (
            BigInteger i = BigInteger.ZERO, gPower = BigInteger.ONE;
            i.compareTo(n) == -1;
            i = i.add(BigInteger.ONE),
            gPower = gPower.multiply(g).mod(N)
        ) {
            table.put(gPower, i);
        }

        BigInteger u = g.modPow(n,N).modInverse(N);
        BigInteger value;
        for (
            BigInteger i = BigInteger.ZERO, key = h;
            i.compareTo(n) == -1;
            i = i.add(BigInteger.ONE),
            key = key.multiply(u).mod(N)
        ) {
            value = table.get(key);
            if (value != null) {
                return i.multiply(n).add(value);
            }
        }

        throw new Error("No solution");
    }

    static void runShanksAlgorithm() {
        BigInteger g = new BigInteger("9704");
        BigInteger h = new BigInteger("13896");
        BigInteger N = new BigInteger("17389");

        System.out.println("Example from book:");
        System.out.println("9704^x ≅ 1389 (mod 17389)");
        System.out.println("x = " + shanksAlgorithm(g, h, N));

        String indent = "  ";
        System.out.println("\n2.17:");

        g = new BigInteger("11");
        h = new BigInteger("21");
        N = new BigInteger("71");
        System.out.println(indent + "a)");
        System.out.println(indent + indent + "11^x ≅ 21 (mod 71)");
        System.out.println(indent + indent + "x = " + shanksAlgorithm(g, h, N));

        g = new BigInteger("156");
        h = new BigInteger("116");
        N = new BigInteger("593");
        System.out.println(indent + "b)");
        System.out.println(indent + indent + "156^x ≅ 116 (mod 593)");
        System.out.println(indent + indent + "x = " + shanksAlgorithm(g, h, N));

        g = new BigInteger("650");
        h = new BigInteger("2213");
        N = new BigInteger("3571");
        System.out.println(indent + "b)");
        System.out.println(indent + indent + "650^x ≅ 2213 (mod 3571)");
        System.out.println(indent + indent + "x = " + shanksAlgorithm(g, h, N));
    }

    // RUNNER

    public static void main(String[] args) {
        // runExtendedEuclidean();
        runShanksAlgorithm();
    }
}
