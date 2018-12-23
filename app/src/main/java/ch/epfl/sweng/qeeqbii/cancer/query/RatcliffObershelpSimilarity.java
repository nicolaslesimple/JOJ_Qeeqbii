package ch.epfl.sweng.qeeqbii.cancer.query;

import java.util.Locale;


/**
 * Created by adrien on 19.11.17.
 */

public class RatcliffObershelpSimilarity {

    public RatcliffObershelpSimilarity() {}

    private static float tcnt;

    private void findSubstr(String s1, int s1len, String s2, int s2len, Struct ss) {
        int size = 1;
        ss.setO2(-1);
        for (int i = 0; i < (s1len - size); i++) {
            for (int j = 0; j < (s2len - size); j++) {
                int test_size = size;

                while (true) {
                    if ((test_size <= (s1len - i)) && (test_size <= (s2len - j))) {
                        if (s1.regionMatches(i, s2, j, test_size)) {
                            if (test_size > size || ss.getO2() < 0) {
                                ss.setO1(i);
                                ss.setO2(j);
                                size = test_size;
                            }
                            test_size++;
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
        }

        if (ss.getO2() < 0) {
            ss.setLen(0);
        } else {
            ss.setLen(size);
        }
    }

    private void rsimil(String s1, int s1len, String s2, int s2len) {
        Struct ss = new Struct();

        if (s1len == 0 || s2len == 0) return;

        findSubstr(s1, s1len, s2, s2len, ss);

        if (ss.getLen() > 0) {
            int delta1, delta2;
            tcnt += ss.getLen() << 1;
            rsimil(s1, ss.getO1(), s2, ss.getO2());

            delta1 = ss.getO1() + ss.getLen();
            delta2 = ss.getO2() + ss.getLen();

            if (delta1 < s1len && delta2 < s2len) {
                rsimil(s1.substring(delta1, s1len), s1len - delta1, s2.substring(delta2, s2len), s2len - delta2);
            }
        }
    }

    public float ratcliff(String s1, String s2) {
        int s1len, s2len;
        float tlen;

        if (s1 == null || s2 == null) {
            return 0;
        } else if (s1.equals(s2)) {
            return 1;
        }

        s1 = s1.toLowerCase(Locale.getDefault());
        s2 = s2.toLowerCase(Locale.getDefault());

        s1len = s1.length();
        s2len = s2.length();

        tcnt = 0;
        tlen = s1len + s2len;

        rsimil(s1, s1len, s2, s2len);

        return tcnt / tlen;
    }

    private class Struct {
        Struct() {}
        int o1, o2, len;

        int getLen() {
            return len;
        }

        void setLen(int len) {
            this.len = len;
        }

        int getO1() {
            return o1;
        }

        void setO1(int o1) {
            this.o1 = o1;
        }

        int getO2() {
            return o2;
        }

        void setO2(int o2) {
            this.o2 = o2;
        }
    }
}