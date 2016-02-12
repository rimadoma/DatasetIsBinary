package org.bonej.testUtil;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Iterator;
import java.util.Optional;
import java.util.Random;

import net.imagej.Dataset;
import net.imagej.DatasetService;
import net.imagej.axis.Axes;
import net.imagej.axis.AxisType;
import net.imglib2.Cursor;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.*;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;

import org.scijava.AbstractContextual;
import org.scijava.plugin.Parameter;

import javax.annotation.Nullable;

/**
 * A utility class that can be used to automatically create different types of Datasets.
 * Handy for, e.g. testing.
 *
 * Call setContext before creating Datasets.
 *
 * @author  Richard Domander
 */
public final class DatasetCreator extends AbstractContextual {
    private static final long DEFAULT_WIDTH = 10;
    private static final long DEFAULT_HEIGHT = 10;
    private static final long DEFAULT_DEPTH = 10;
    private static final long[] DEFAULT_DIMS = {DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_DEPTH};
    private static final AxisType[] DEFAULT_AXES = {Axes.X, Axes.Y, Axes.Z};

    @Parameter
    private DatasetService datasetService = null;

    /**
     * Creates an empty Dataset of the given type
     * @see DatasetCreator#createDataset(DatasetType, AxisType[], long[])
     */
    public Optional<Dataset> createEmptyDataset(DatasetType type) {
        return createDataset(type, new AxisType[]{Axes.X, Axes.Y}, new long[]{0, 0});
    }

    /**
     * Creates a Dataset of the given type with the default dimensions (X = 10, Y = 10, Z = 10)
     * @see DatasetCreator#createDataset(DatasetType, AxisType[], long[])
     */
    public Optional<Dataset> createDataset(DatasetType type) {
        return createDataset(type, DEFAULT_AXES, DEFAULT_DIMS);
    }

    /**
     * Creates a Dataset with the given type, axes and dimensions
     *
     * @throws              NullPointerException if there's no DatasetService
     * @throws              NullPointerException if any of the parameters is null
     * @param type          The type of the Dataset - see Dataset#DatasetType
     * @param axesTypes     The types of the dimensions in the Dataset
     * @param dimensions    The sizes of the dimensions in the Dataset
     * @return An Optional with a new Dataset available if type is recognized
     */
    public Optional<Dataset> createDataset(DatasetType type, AxisType[] axesTypes, long[] dimensions)
            throws NullPointerException {
        checkNotNull(datasetService, "No datasetService available - did you call setContext?");
        checkNotNull(type, "Can't create a dataset: type is null");
        checkNotNull(axesTypes, "Can't create a dataset: axesTypes is null");
        checkNotNull(dimensions, "Can't create a dataset: dimensions is null");

        Dataset dataset;

        switch (type) {
            case BIT:
                dataset = datasetService.create(new BitType(), dimensions, "Dataset", axesTypes);
                break;
            case BYTE:
                dataset = datasetService.create(new ByteType(), dimensions, "Dataset", axesTypes);
                break;
            case DOUBLE:
                dataset = datasetService.create(new DoubleType(), dimensions, "Dataset", axesTypes);
                break;
            case FLOAT:
                dataset = datasetService.create(new FloatType(), dimensions, "Dataset", axesTypes);
                break;
            case INT:
                dataset = datasetService.create(new IntType(), dimensions, "Dataset", axesTypes);
                break;
            case LONG:
                dataset = datasetService.create(new LongType(), dimensions, "Dataset", axesTypes);
                break;
            case SHORT:
                dataset = datasetService.create(new ShortType(), dimensions, "Dataset", axesTypes);
                break;
            case UNSIGNED_128_BIT:
                dataset = datasetService.create(new Unsigned128BitType(), dimensions, "Dataset", axesTypes);
                break;
            case UNSIGNED_12_BIT:
                dataset = datasetService.create(new Unsigned12BitType(), dimensions, "Dataset", axesTypes);
                break;
            case UNSIGNED_2_BIT:
                dataset = datasetService.create(new Unsigned2BitType(), dimensions, "Dataset", axesTypes);
                break;
            case UNSIGNED_4_BIT:
                dataset = datasetService.create(new Unsigned4BitType(), dimensions, "Dataset", axesTypes);
                break;
            case UNSIGNED_BYTE:
                dataset = datasetService.create(new UnsignedByteType(), dimensions, "Dataset", axesTypes);
                break;
            case UNSIGNED_SHORT:
                dataset = datasetService.create(new UnsignedShortType(), dimensions, "Dataset", axesTypes);
                break;
            case UNSIGNED_INT:
                dataset = datasetService.create(new UnsignedIntType(), dimensions, "Dataset", axesTypes);
                break;
            case UNSIGNED_LONG:
                dataset = datasetService.create(new UnsignedLongType(), dimensions, "Dataset", axesTypes);
                break;
            case UNSIGNED_VARIABLE_BIT_LENGTH:
                dataset = datasetService.create(new UnsignedVariableBitLengthType(64), dimensions, "Dataset",
                        axesTypes);
                break;
            default:
                dataset = null;
                break;
        }

        return Optional.ofNullable(dataset);
    }

    /**
     * Fills the elements in the given Dataset with random whole numbers.
     *
     * @implNote        Min and max values are clamped to prevent under - and overflow.
     *                  E.g. If maxValue == 1000 and dataset type == UnsignedByteType, then maxValue = 255
     * @param minValue  Minimum value of the random numbers (inclusive)
     * @param maxValue  Maximum value of the random numbers (inclusive)
     */
    public static void fillWithRandomWholeNumbers(@Nullable final Dataset dataset, long minValue, long maxValue) {
        if (dataset == null) {
            return;
        }

        final Cursor<RealType<?>> cursor = dataset.cursor();
        if (!cursor.hasNext()) {
            return;
        }

        cursor.fwd();
        final RealType<?> element = cursor.next();
        final long typeMin = (long) element.getMinValue();
        final long typeMax = (long) element.getMaxValue();
        minValue = clamp(minValue, typeMin, typeMax);
        maxValue = clamp(maxValue, typeMin, typeMax);
        cursor.reset();

        long exclusiveMax = maxValue + 1;

        final Iterator<Long> randomIterator =
                new Random(System.currentTimeMillis()).longs(minValue, exclusiveMax).iterator();

        cursor.forEachRemaining(c -> c.setReal(randomIterator.next()));
    }

    private static long clamp(long value, long min, long max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    public enum DatasetType {
        BIT,
        BYTE,
        DOUBLE,
        FLOAT,
        INT,
        LONG,
        SHORT,
        UNSIGNED_128_BIT,
        UNSIGNED_12_BIT,
        UNSIGNED_2_BIT,
        UNSIGNED_4_BIT,
        UNSIGNED_BYTE,
        UNSIGNED_SHORT,
        UNSIGNED_INT,
        UNSIGNED_LONG,
        UNSIGNED_VARIABLE_BIT_LENGTH
    }
}
