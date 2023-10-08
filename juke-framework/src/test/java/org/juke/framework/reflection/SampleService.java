package org.juke.framework.reflection;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SampleService implements ISampleService {

	@Override
	public HashMap<String, BigDecimal> getMyDataMap(BigDecimal[] values, String keyNames) {
		// TODO Auto-generated method stub
		HashMap <String, BigDecimal> map = new HashMap<String, BigDecimal>();
		int i=0;
		for (BigDecimal value : values) {
			map.put(keyNames+"."+(i++), value);
			
		}
		return map;
	}

	@Override
	public BigDecimal[] getMyDataArrayReverse(Map<String, BigDecimal> values) {
		// TODO Auto-generated method stub
		return values.values().toArray(new BigDecimal[values.size()]);
	}

	@Override
	public Set<BigDecimal> getMyDataSet(BigDecimal[] values){
		// TODO Auto-generated method stub
		Set set= new HashSet<BigDecimal>();
		set.addAll(Arrays.asList(values));
		return set;
	}

	@Override
	public BigDecimal[] getMyDataArrayReverse2(Set<BigDecimal> values) {
		// TODO Auto-generated method stub
		return values.toArray(new BigDecimal[values.size()]);
	}

	@Override
	public List<HashMap<String, BigDecimal>> getMyDataMapAsList(BigDecimal[] values, String keyNames) {
		// TODO Auto-generated method stub
		List list= new ArrayList<HashMap<String,BigDecimal>>();

		int i=0;
		for (BigDecimal value : values) {
			list.add(getMyDataMap(values,keyNames));
			
			
		}
		return list;
	}
	@Override
	public List<HashMap<String, BigDecimal>> getMyDataMapAsList(List<BigDecimal> values, String keyNames) {
		// TODO Auto-generated method stub
		return getMyDataMapAsList(values.toArray(new BigDecimal[values.size()]), keyNames);
	}
	@Override
	public Double[] fromSimpleDoubleArray(double[] d1) {
		// TODO Auto-generated method stub
		Double objNums[] = new Double[d1.length];
		for (int i=0; i < d1.length; i++) {
			objNums[i]=d1[i];
		}
		return objNums;
	}

	@Override
	public double[] toSimpleDoubleArray(Double[] d1) {
		// TODO Auto-generated method stub
		double objNums[] = new double[d1.length];
		for (int i=0; i < d1.length; i++) {
			objNums[i]=d1[i];
		}
		return objNums;
	}





}
