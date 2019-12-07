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
        System.exit(new Main().run());
    }

    private int run() {
        YearlyRateSequence sequence = new YearlyRateSequence(SMOOTHING_INTERVAL, AVERAGE_RATE);
        double pureAmount = AMOUNT;
        double amountWithCharge = AMOUNT;

        for(int y = 0; y<YEARS; y++) {
            double rate = sequence.next();

            pureAmount *= rate;
            amountWithCharge *= rate;
            amountWithCharge *= YearlyRateSequence.convertRate(-CHARGE);
        }
        System.out.printf("%.2f %.2f", pureAmount, amountWithCharge);

        return 0;
    }

    private static class YearlyRateSequence {
        private List<Double> interval;
        private double threshold;
        private Random rand;

        YearlyRateSequence(int smoothingInterval, int rate) {
            this.rand = new Random();
            this.interval = new ArrayList<>();
            this.threshold = 1.0;
            double convertedRate = convertRate(rate);
            for (int i = 0; i < smoothingInterval; i++) {
                this.interval.add(convertedRate);
                this.threshold *= convertedRate;
            }
        }

        private double next() {
            double current = nextRate();
            interval.remove(0);
            interval.add(current);
            return current;
        }

        private double nextRate() {
            int currentRate;
            if(up()) {
                currentRate = rand.nextInt(16)+AVERAGE_RATE;
            }
            else {
                currentRate = AVERAGE_RATE - rand.nextInt(16);
            }
            System.out.println(currentRate);
            return convertRate(currentRate);
        }

        private boolean up() {
            double summaryRate = summaryRate();
            if(summaryRate>threshold) {
                return (rand.nextInt(100)>70);
            }
            else {
                return (rand.nextInt(100)<70);
            }
        }

        private double summaryRate() {
            return interval.stream().reduce(1.0, (a, b) -> a * b);
        }

        private static double convertRate(int rate) {
            assert rate >= -100 && rate <= 100;
            return (rate / 100.0) + 1.0;
        }
    }

}
