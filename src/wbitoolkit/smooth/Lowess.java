/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wbitoolkit.smooth;

/**
 *
 * @author wb
 */
import java.util.Arrays;

public class Lowess {
    private double bandwidth=0.2;
    private int robustnessIters = 2;
    private double accuracy = 1e-12;
    private double[] sy;

    public Lowess(){}

    public Lowess(double bandwidth, int robustnessIters){
        this.bandwidth=bandwidth;
        this.robustnessIters=robustnessIters;
    }
    public Lowess(double bandwidth, int robustnessIters, double accuracy){
        this.bandwidth=bandwidth;
        this.robustnessIters=robustnessIters;
        this.accuracy=accuracy;
    }

    public final void smooth(final double[] xval, final double[] yval) {
        final double[] unitWeights = new double[xval.length];
        Arrays.fill(unitWeights, 1.0);

        sy=smooth(xval, yval, unitWeights);
    }

    public final double[] smooth(final double[] xval, final double[] yval,
                                 final double[] weights)  {


        final int n = xval.length;


        if (n == 1) {
            return new double[]{yval[0]};
        }

        if (n == 2) {
            return new double[]{yval[0], yval[1]};
        }

        int bandwidthInPoints = (int)Math.round(getBandwidth() * n);

        final double[] res = new double[n];

        final double[] residuals = new double[n];
        final double[] sortedResiduals = new double[n];

        final double[] robustnessWeights = new double[n];

        // Do an initial fit and 'robustnessIters' robustness iterations.
        // This is equivalent to doing 'robustnessIters+1' robustness iterations
        // starting with all robustness weights set to 1.
        Arrays.fill(robustnessWeights, 1);

        for (int iter = 0; iter <= getRobustnessIters(); ++iter) {
            final int[] bandwidthInterval = {0, bandwidthInPoints - 1};
            // At each x, compute a local weighted linear regression
            for (int i = 0; i < n; ++i) {
                final double x = xval[i];

                // Find out the interval of source points on which
                // a regression is to be made.
                if (i > 0) {
                    updateBandwidthInterval(xval, weights, i, bandwidthInterval);
                }

                final int ileft = bandwidthInterval[0];
                final int iright = bandwidthInterval[1];

                // Compute the point of the bandwidth interval that is
                // farthest from x
                final int edge;
                if (xval[i] - xval[ileft] > xval[iright] - xval[i]) {
                    edge = ileft;
                } else {
                    edge = iright;
                }


                // Compute a least-squares linear fit weighted by
                // the product of robustness weights and the tricube
                // weight function.
                // See http://en.wikipedia.org/wiki/Linear_regression
                // (section "Univariate linear case")
                // and http://en.wikipedia.org/wiki/Weighted_least_squares
                // (section "Weighted least squares")
                double sumWeights = 0;
                double sumX = 0;
                double sumXSquared = 0;
                double sumY = 0;
                double sumXY = 0;
                //scale
                double denom = Math.abs(1.0 / (xval[edge] - x));
                for (int k = ileft; k <= iright; ++k) {
                    final double xk   = xval[k];
                    final double yk   = yval[k];
                    final double dist = (k < i) ? x - xk : xk - x;
                    final double w    = tricube(dist * denom) * robustnessWeights[k] * weights[k];
                    final double xkw  = xk * w;
                    sumWeights += w;
                    sumX += xkw;
                    sumXSquared += xk * xkw;
                    sumY += yk * w;
                    sumXY += yk * xkw;
                }

                final double meanX = sumX / sumWeights;
                final double meanY = sumY / sumWeights;
                final double meanXY = sumXY / sumWeights;
                final double meanXSquared = sumXSquared / sumWeights;

                final double beta;
                if (Math.sqrt(Math.abs(meanXSquared - meanX * meanX)) < getAccuracy()) {
                    beta = 0;
                } else {
                    beta = (meanXY - meanX * meanY) / (meanXSquared - meanX * meanX);      
                }

                final double alpha = meanY - beta * meanX;

                res[i] = beta * x + alpha;
                residuals[i] = Math.abs(yval[i] - res[i]);

            }

            // No need to recompute the robustness weights at the last
            // iteration, they won't be needed anymore
            if (iter == getRobustnessIters()) {
                break;
            }

            // Recompute the robustness weights.

            // Find the median residual.
            // An arraycopy and a sort are completely tractable here,
            // because the preceding loop is a lot more expensive
            System.arraycopy(residuals, 0, sortedResiduals, 0, n);
            Arrays.sort(sortedResiduals);
            final double medianResidual = sortedResiduals[n / 2];

            if (Math.abs(medianResidual) < getAccuracy()) {
                break;
            }

            for (int i = 0; i < n; ++i) {
                final double arg = residuals[i] / (6 * medianResidual);
                if (arg >= 1) {
                    robustnessWeights[i] = 0;
                } else {
                    final double w = 1 - arg * arg;
                    robustnessWeights[i] = w * w;
                }
            }
        }

        return res;
    }

     private static void updateBandwidthInterval(final double[] xval, final double[] weights,
                                                final int i,
                                                final int[] bandwidthInterval) {
        final int left = bandwidthInterval[0];
        final int right = bandwidthInterval[1];

        // The right edge should be adjusted if the next point to the right
        // is closer to xval[i] than the leftmost point of the current interval
        int nextRight = nextNonzero(weights, right);
        if (nextRight < xval.length && xval[nextRight] - xval[i] < xval[i] - xval[left]) {
            int nextLeft = nextNonzero(weights, bandwidthInterval[0]);
            bandwidthInterval[0] = nextLeft;
            bandwidthInterval[1] = nextRight;
        }
    }

     private static int nextNonzero(final double[] weights, final int i) {
        int j = i + 1;
        while(j < weights.length && weights[j] == 0) {
            ++j;
        }
        return j;
    }

     private static double tricube(final double x) {
        final double absX = Math.abs(x);
        if (absX >= 1.0) {
            return 0.0;
        }
        final double tmp = 1 - absX * absX * absX;
        return tmp * tmp * tmp;
    }

     /**
      * @return smoothed y array
      */
     public double[] getSy(){
         return this.sy;
     }

    /**
     * @return the bandwidth
     */
    public double getBandwidth() {
        return bandwidth;
    }

    /**
     * @param bandwidth the bandwidth to set
     */
    public void setBandwidth(double bandwidth) {
        this.bandwidth = bandwidth;
    }

    /**
     * @return the robustnessIters
     */
    public int getRobustnessIters() {
        return robustnessIters;
    }

    /**
     * @param robustnessIters the robustnessIters to set
     */
    public void setRobustnessIters(int robustnessIters) {
        this.robustnessIters = robustnessIters;
    }

    /**
     * @return the accuracy
     */
    public double getAccuracy() {
        return accuracy;
    }

    /**
     * @param accuracy the accuracy to set
     */
    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }
}