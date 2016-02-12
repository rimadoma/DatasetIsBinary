package org.bonej.ops.datasetIsBinary;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.stream.Stream;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.axis.Axes;
import net.imagej.axis.AxisType;
import net.imglib2.img.basictypeaccess.array.ArrayDataAccess;
import net.imglib2.img.basictypeaccess.array.ByteArray;
import net.imglib2.img.planar.PlanarImg;
import net.imglib2.img.planar.PlanarImgFactory;
import net.imglib2.type.logic.BitType;

import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.util.Fraction;
import org.bonej.testUtil.DatasetCreator;
import org.bonej.testUtil.DatasetCreator.DatasetType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit tests for the DatasetIsBinary Op
 *
 * @author Richard Domander
 */
public class TestIterableIntervalIsBinary {
    private static final ImageJ ij = new ImageJ();
    private static Dataset dataset = null;
    private static final DatasetCreator datasetCreator = new DatasetCreator();

    @BeforeClass
    public static void oneTimeSetup() {
        datasetCreator.setContext(ij.getContext());
    }

    @After
    public void tearDown() {
        dataset = null;
    }

    @AfterClass
    public static void oneTimeTearDown() {
        ij.context().dispose();
    }

    @Test
    public void testEmptyDatasetFails() throws AssertionError {
        final long[] dims = {0, 0};
        PlanarImg planarImg = new PlanarImg(dims, new Fraction());

        final boolean result = (boolean) ij.op().run(IterableIntervalIsBinary.class, planarImg);
        assertFalse("Empty interval is not binary", result);
    }

    @Test
    public void testIntervalWithOneValuePasses() throws AssertionError {
        final int minValue = 1;
        final int maxValue = 1;
        dataset = datasetCreator.createDataset(DatasetType.BIT).get();
        DatasetCreator.fillWithRandomWholeNumbers(dataset, minValue, maxValue);

        final boolean result = (boolean) ij.op().run(IterableIntervalIsBinary.class, dataset);

        assertTrue("An interval with one distinct value is binary", result);
    }

    @Test
    public void testIntervalWithTwoValuesPasses() throws AssertionError {
        final int minValue = 0;
        final int maxValue = 1;
        dataset = datasetCreator.createDataset(DatasetType.BIT).get();
        DatasetCreator.fillWithRandomWholeNumbers(dataset, minValue, maxValue);

        final boolean result = (boolean) ij.op().run(IterableIntervalIsBinary.class, dataset);

        assertTrue("A Dataset with two distinct values is binary", result);
    }

    @Test
    public void testIntervalWithMoreThanTwoValuesFails() throws AssertionError {
        PlanarImg planarImg = new PlanarImgFactory().create(new long[]{3}, new ByteType());
        planarImg.setPlane(0, new ByteArray(new byte[]{0, 1, 2}));
        final boolean result = (boolean) ij.op().run(IterableIntervalIsBinary.class, planarImg);
        assertFalse("An interval with more than two distinct values is not binary", result);
    }
}
