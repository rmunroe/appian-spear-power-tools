package org.appiansc.plugins.spt.functions.num;

import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.AppianType;
import com.appiancorp.suiteapi.type.TypedValue;

import java.util.Random;

@SptNumCategory
public class SPT_Num_RandInRange {
    private static final Random random = new Random();


    @Function
    public TypedValue spt_num_randinrange(
            @Parameter TypedValue min,
            @Parameter TypedValue max,
            @Parameter(required = false) Long count,
            @Parameter(required = false) Long places
    ) throws Exception {
        if (!((min.getValue() instanceof Long) || (min.getValue() instanceof Double)))
            throw new Exception("Only Integer or Decimal values allowed for min");
        if (!((max.getValue() instanceof Long) || (max.getValue() instanceof Double)))
            throw new Exception("Only Integer or Decimal values allowed for max");
        if (!min.getValue().getClass().equals(max.getValue().getClass()))
            throw new Exception("min and max must be the same numeric type");

        if (count == null || count == 1)
            return getSingle(min, max, places);
        if (count == 0)
            return null;
        else
            return getMany(min, max, count, places);
    }

    private TypedValue getSingle(TypedValue min, TypedValue max, Long places) {
        TypedValue returnValue = new TypedValue();

        if (min.getValue() instanceof Long) {
            returnValue.setInstanceType((long) AppianType.INTEGER);
            returnValue.setValue(
                    random.longs((long) min.getValue(), (long) max.getValue())
                            .findFirst()
                            .orElse(0)
            );
        } else {
            returnValue.setInstanceType((long) AppianType.DOUBLE);
            returnValue.setValue(
                    random.doubles((double) min.getValue(), (double) max.getValue())
                            .findFirst()
                            .orElse(0)
            );
        }

        // Truncate to number of places
        if (min.getInstanceType() == AppianType.DOUBLE && places != null) {
            double factor = Math.pow(10.0, places);
            returnValue.setValue(((double) ((int) ((double) returnValue.getValue() * factor))) / factor);
        }

        return returnValue;
    }

    private TypedValue getMany(TypedValue min, TypedValue max, Long count, Long places) {
        TypedValue returnValue = new TypedValue();

        if (min.getValue() instanceof Long) {
            returnValue.setInstanceType((long) AppianType.LIST_OF_INTEGER);
            returnValue.setValue(
                    random.longs((long) min.getValue(), (long) max.getValue())
                            .limit(count)
                            .boxed()
                            .toArray(Long[]::new)
            );
        } else {
            returnValue.setInstanceType((long) AppianType.LIST_OF_DOUBLE);
            returnValue.setValue(
                    random.doubles((double) min.getValue(), (double) max.getValue())
                            .limit(count)
                            .boxed()
                            .toArray(Double[]::new)
            );
        }

        // Truncate to number of places
        if (min.getInstanceType() == AppianType.DOUBLE && places != null) {
            double factor = Math.pow(10.0, places);
            Double[] arr = (Double[]) returnValue.getValue();
            for (int i = 0; i < arr.length; i++) {
                arr[i] = ((double) ((int) (arr[i] * factor))) / factor;
            }
            returnValue.setValue(arr);
        }

        return returnValue;
    }
}
