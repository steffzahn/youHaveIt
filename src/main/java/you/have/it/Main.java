package you.have.it;

import java.util.Arrays;

public class Main {

    public  static  void main(String[] args) {
        System.exit(new Main().run(args));
    }

    private int run(String[] args) {
        System.out.println(Arrays.asList(args));
        return 0;
    }

}
