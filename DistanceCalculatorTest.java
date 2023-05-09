import org.junit.Assert;
import org.junit.Test;

public class DistanceCalculatorTest {

    @Test
    public void testDistance() {
        DistanceCalculator d = new DistanceCalculator();
        double answer = d.distance(40.6943, 34.1141, -73.9249, -118.4068, 0.0, 0.0);

        Assert.assertEquals(2457, Math.round(answer));
    }

}
