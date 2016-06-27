package methods;

import java.lang.Double;
import java.util.Arrays;
import com.google.common.primitives.Doubles;

public class StatisticalNLPTools {
	
	public StatisticalNLPTools()
	{
		
	}
	
	public double prob(double[] list, double x)
	{
		double rep = 0.0; 
		for (int i = 0 ; i < list.length ; i++)
			if (list[i]==x)
				rep++;
		/*if(rep>1)
		{
			for(int j = index+1 ; j < list.length; j++)
			{
				if (list[j] == x)
				{
					list[j] = Double.MAX_VALUE;
				}
			}
		}*/
		return rep / list.length;
	}
	
	public double[] removeDuplicates(double[] inp)
	{
		int cnt = 0; 
		for (int i = 0 ; i < inp.length; i++)
		{
			for (int j = 0 ; j < inp.length; j++)
			{
				if (inp[i] == inp[j] & i != j)
				{					
					inp[j] = Double.MAX_VALUE; 
					if (inp[j] != Double.MAX_VALUE) cnt++;
				}
			}
		}
		double[] res = new double[inp.length-cnt];
		for (int i = 0 , j = 0; i < inp.length; i++)
		{
			if (inp[i] != Double.MAX_VALUE)
			{					
				res[j] = inp[i]; j++;
			}
		}
		return res;
	}
	
	public double expectation(double[] inp)
	{
		double[] prob = new double[inp.length];
		
		//probability
		for (int i = 0 ; i < prob.length; i++){
			prob[i] = this.prob(inp, inp[i]);
			//System.out.println(prob[i]);
		}
		
		double[] inp_noDuplicate = removeDuplicates(inp);
		
		//Expectation
		double exp = 0; 
		for (int i = 0 ; i < inp_noDuplicate.length; i++)
		{
			for(int j = 0 ; j < inp.length; j++)
			{
				if(inp_noDuplicate[i] == inp[j])
				{
					exp += inp_noDuplicate[i] * prob[j];
				}
			}
		}
		
		return exp;
	}
	
	public double information(double[] inp, double y)
	{
		
		//number of occurrences
		double count = 0;
	    for (int i = 0; i<inp.length; i++)
	    	if (inp[i] == y)
	    		count = count + 1;
	    
	    //Probability
		double probibility = count / inp.length;
		
		//Information
		return Math.log(1/probibility)/Math.log(2);		
	}
	
	public double entropy(double[] inp)
	{
		double exp = expectation(inp);
		double entropy = 0;
		
		for(int i = 0 ; i < inp.length ; i++)
			entropy += exp * information(inp, inp[i]);
		
		return entropy;
	}
	
	public double[] rankData(double[] list)
	{
		double[] sorted = new double[list.length];
		for (int i = 0; i < list.length; i++) sorted[i] = list[i];
		Arrays.sort(sorted);
		
		double[] ranks = list;
		for(int i = 0 ; i < list.length; i++)
			ranks[i] = Doubles.indexOf(sorted, list[i])+1;
		return ranks;
	}

	public double distanceBetweenRanks(double[] list1, double[] list2)
	{
		double diff = 0 ;
		list1 = rankData(list1);
		list2 = rankData(list2);
		for(int i = 0 ; i < list1.length; i++)	diff += Math.pow(Math.abs(list1[i]-list2[i]),2);	
		return diff;
	}
	
	public double spearmanCoeff(double[] list1, double[] list2)
	{
		/*	Interpretation
		 * .00 to .19 very weak
		 * .20 to .39 weak
		 * .40 to .59 moderate
		 * .60 to .79 strong
		 * .80 to 1.0 very strong
		 */
		int n = list1.length; n = n * n * n - n;
		double d = distanceBetweenRanks(list1, list2);	
		double spearmanCoeff = 1 - (6 * d / n);
		return spearmanCoeff; 
	} 

	public double[] simpleLinearRegression(double[] list1, double[] list2)
	{
		double[] thetas = new double[2];
		int n = list1.length;
		double sum1 = 0 , sum2 = 0 , sum12 = 0 , sum11=0;
		for (int i = 0 ; i < n; i++)
		{
			sum1 += list1[i]; 
			sum2 += list2[i];
			sum12 += list1[i] * list2[i];
			sum11 += list1[i] * list1[i];
		}
		thetas[1] = (sum12 - (sum1 * sum2 / n)) / (sum11 - (sum1 * sum1 / n));
		thetas[0] = (sum2 / n) - (thetas[1] * sum1 / n);
		return thetas;
	}

	public double predict(double[] features, double[] scores, double a1)
	{
		double[] thetas = null;
		if(Math.abs(spearmanCoeff(features, scores)) >= 0.6)
		{
			thetas = simpleLinearRegression(features, scores);
			return (thetas[0] + thetas[1] * a1);
		}
		return Double.MAX_VALUE;
	}

	public double vectorMagnitude(double[] vector)
	{
		double magnitude = 0;
		for(double d: vector)
			magnitude += d*d;
		return Math.sqrt(magnitude);
	}
	
	public double dotProduct(double[] v1, double[] v2)
	{
		if(v1.length != v2.length) //Error: not defined
			return Double.MAX_VALUE;
		
		double prod = 0;
		for(int i = 0 ; i < v1.length ; i++)
			prod += v1[i] * v2[i]; 
		return prod;
	}
	
	public double cosine_similarity(double[] v1, double[] v2)
	{
		if(v1.length != v2.length) //Error: not defined
			return Double.MAX_VALUE;
		
		return dotProduct(v1, v2) / vectorMagnitude(v1) * vectorMagnitude(v2);
	}
}
