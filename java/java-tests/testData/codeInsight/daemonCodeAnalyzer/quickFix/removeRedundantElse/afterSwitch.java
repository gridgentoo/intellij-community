// "Remove redundant 'else'" "true"
class T {
    void three(boolean b) {
        switch (3) {
            case 2:
                if (foo()) {
                    return;
                }
                return;
            case 3:
        }
    }
}