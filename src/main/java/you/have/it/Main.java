package you.have.it;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static final int AMOUNT = 100000;
    public static final int YEARS = 25;
    public static final int AVERAGE_RATE = 6;
    public static final int SMOOTHING_INTERVAL = 7;
    public static final int CHARGE = 2;

    public static void main(String[] args) {
        System.exit(new Main().run(args));
    }

    private int run(String[] args) {
        YearlyRateSequence sequence = new YearlyRateSequence(SMOOTHING_INTERVAL, AVERAGE_RATE);
        double pureAmount = AMOUNT;
        double amountWithCharge = AMOUNT;
        Random rand = new Random();

        for(int y = 0; y<YEARS; y++) {
            int currentRate = rand.nextInt(32)-16+AVERAGE_RATE;
            double rate = sequence.add(currentRate);

            pureAmount *= rate;
            amountWithCharge *= rate;
            amountWithCharge *= YearlyRateSequence.convertRate(-CHARGE);
        }
        System.out.printf("%.2f %.2f", pureAmount, amountWithCharge);

        return 0;
    }

    private static class YearlyRateSequence {
        private int smoothingInterval;
        private int rate;
        private List<Double> interval;

        YearlyRateSequence(int smoothingInterval, int rate) {
            this.smoothingInterval = smoothingInterval;
            this.rate = rate;
            this.interval = new ArrayList<>();
            for (int i = 0; i < smoothingInterval; i++) {
                this.interval.add(convertRate(rate));
            }
        }

        private double add(int rate) {
            double current = convertRate(rate);
            interval.remove(0);
            interval.add(current);
            return current;
        }

        private static double convertRate(int rate) {
            assert rate >= -100 && rate <= 100;
            return (rate / 100.0) + 1.0;
        }
    }

}
