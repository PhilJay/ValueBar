
package com.philjay.valuebar.colors;

/**
 * Interface for providing custom colors for the ValueBar.
 * 
 * @author Philipp Jahoda
 */
public interface BarColorFormatter {

    /**
     * Use this method to return whatever color you like the ValueBar to have.
     * You can also make use of the current value the bar has.
     * 
     * @param value
     * @param maxVal the maximum value the bar can display
     * @param minVal the minimum value the bar can display
     * @return
     */
    public int getColor(float value, float maxVal, float minVal);
}
