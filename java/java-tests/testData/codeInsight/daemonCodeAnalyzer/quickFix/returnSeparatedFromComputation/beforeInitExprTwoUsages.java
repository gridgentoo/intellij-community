// "Move 'return' closer to computation of the value of 'n'" "true"
import java.util.*;

class T {
    List foo(boolean b) {
        List n = new ArrayList();
        if (b)
            n = new ArrayList(1);
        <caret>return n;
    }
}