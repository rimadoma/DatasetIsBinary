package org.bonej.ops.datasetIsBinary;

import net.imagej.ops.Op;
import net.imagej.ops.OpEnvironment;
import net.imglib2.IterableInterval;
import net.imglib2.type.BooleanType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * An Op which checks whether the given IterableInterval is binary.
 * A binary interval contains only two distinct values.
 *
 * @author Richard Domander
 */
@Plugin(type = Op.class, name = "iterableIntervalIsBinary")
public class IterableIntervalIsBinary implements Op {

    @Parameter(type = ItemIO.INPUT)
    private IterableInterval interval = null;

    /**
     * If true, then the interval contains only one or two distinct values
     */
    @Parameter(type = ItemIO.OUTPUT)
    private boolean isBinary = false;

    @Override
    public OpEnvironment ops() {
        return null;
    }

    @Override
    public void setEnvironment(OpEnvironment ops) {

    }

    @Override
    public void run() {
        isBinary = isBinaryType();
    }

    //region -- Helper methods --
    private boolean isBinaryType() {
        if (interval.size() == 0) {
            return false;
        }

        Object element = interval.firstElement();
        final Class datasetTypeClass = element.getClass();
        return BooleanType.class.isAssignableFrom(datasetTypeClass);
    }
    //endregion
}
