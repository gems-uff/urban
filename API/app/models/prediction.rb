#encoding: utf-8
class Prediction

    #Média
    def self.AVG(sample)
      total = 0;
      n = sample.size

      if(n<1)
        return 0
      end

      sample.each do |s|
        total += s
      end

      return total/n
    end

    #Desvio Padrão raiz(variancia(amostra))
    def self.standard_deviation(sample)
      variance = self.variance(sample)
      return variance**(0.5)
    end

    #Variancia 1/(n-1) * somatorio(xi - media)
    def self.variance(sample)
      total = 0;
      n = sample.size
      if(n<2)
        return 0
      end

      avg = self.AVG(sample)

      sample.each do |s|
        total += (s - avg) ** 2
      end

      return total/(n-1)
    end

    #Intervalo de Confiança
    def self.confidence_interval(sample, confidence)
      n = sample.size
      if (n < 2)
        return 0
      end

      #Bicaudal
      if(confidence == 99)
        t_student = {1 => 63.657, 2 => 9.925, 3 => 5.841, 4 => 4.604,
                      5 => 4.032, 6 => 3.707, 7 => 3.499, 8 => 3.355,
                      9 => 3.25, 10 => 3.169, 11 => 3.106, 12 => 3.055,
                      13 => 3.012, 14 => 2.997, 15 => 2.947, 16 => 2.921,
                      17 => 2.898, 18 => 2.878, 19 => 2.861, 20 => 2.845,
                      21 => 2.831, 22 => 2.819, 23 => 2.807, 24 => 2.797,
                      25 => 2.787, 26 => 2.779, 27 => 2.771, 28 => 2.763,
                      29 => 2.756, 30 => 2.75, 40 => 2.704, 50 => 2.678,
                      60 => 2.660, 80 => 2.639, 100 => 2.626, 120 => 2.617}
      elsif(confidence == 95)
        t_student = {1 => 12.71, 2 => 4.303, 3 => 3.182, 4 => 2.776,
                      5 => 2.571, 6 => 2.447, 7 => 2.365, 8 => 2.306,
                      9 => 2.262, 10 => 2.228, 11 => 2.201, 12 => 2.179,
                      13 => 2.16, 14 => 2.145, 15 => 2.131, 16 => 2.12,
                      17 => 2.11, 18 => 2.101, 19 => 2.093, 20 => 2.086,
                      21 => 2.08, 22 => 2.074, 23 => 2.069, 24 => 2.064,
                      25 => 2.06, 26 => 2.056, 27 => 2.052, 28 => 2.048,
                      29 => 2.045, 30 => 2.042, 40 => 2.021, 50 => 2.009,
                      60 => 2, 80 => 1.99, 100 => 1.984, 120 => 1.98}
      elsif(confidence == 90)
        t_student = {1 => 6.314, 2 => 2.92, 3 => 2.353, 4 => 2.132,
                      5 => 2.015, 6 => 1.943, 7 => 1.895, 8 => 1.86,
                      9 => 1.833, 10 => 1.812, 11 => 1.796, 12 => 1.782,
                      13 => 1.771, 14 => 1.761, 15 => 1.753, 16 => 1.746,
                      17 => 1.74, 18 => 1.734, 19 => 1.729, 20 => 1.725,
                      21 => 1.721, 22 => 1.717, 23 => 1.714, 24 => 1.711,
                      25 => 1.708, 26 => 1.706, 27 => 1.703, 28 => 1.701,
                      29 => 1.699, 30 => 1.697, 40 => 1.684, 50 => 1.676,
                      60 => 1.671, 80 => 1.664, 100 => 1.66, 120 => 1.658}
      else(confidence == 75)
        t_student = {1 => 2.414, 2 => 1.604, 3 => 1.423, 4 => 1.344,
                      5 => 1.301, 6 => 1.273, 7 => 1.254, 8 => 1.24,
                      9 => 1.23, 10 => 1.221, 11 => 1.215, 12 => 1.209,
                      13 => 1.204, 14 => 1.200, 15 => 1.197, 16 => 1.194,
                      17 => 1.191, 18 => 1.189, 19 => 1.187, 20 => 1.185,
                      21 => 1.183, 22 => 1.182, 23 => 1.180, 24 => 1.179,
                      25 => 1.178, 26 => 1.177, 27 => 1.176, 28 => 1.175,
                      29 => 1.173, 30 => 1.173, 40 => 1.167, 60 => 1.162,
                      120 => 1.156}
      end
      avg = self.AVG(sample)
      standard_deviation = self.standard_deviation(sample)

      i = n
      while(t_student[i-1]==nil)
        i = i-1
      end

      aux = t_student[n-1] * standard_deviation / Math.sqrt(n)

      interval = {:li => avg - aux,
                  :ls => avg + aux}
      p "=== Log === Confidence Interval:"
      p "=== Log === t student #{t_student[n-1]}"
      p "=== Log === AVG #{avg}"
      p "=== Log === DP #{standard_deviation}"
      p "=== Log === li #{interval[:li]}"
      p "=== Log === ls #{interval[:ls]}"

      return interval
    end

    def self.mediana (sample)
    	sample = sample.sort
      n = sample.size

      if (n%2 == 1)
    		median = sample[n/2]
    	else
    		median = (sample[n/2]+sample[n/2+1] )/2
      end
    	return median
    end

    def self.Q1 (sample)
    	sample = sample.sort
      n = sample.size

      if n%4 == 0
    		median = (sample[n/4]+sample[n/4-1] )/2
    	elsif n%4 == 1
    		median = (sample[n/4]+sample[n/4-1] )/2
    	elsif n%4 == 2
    		median = (sample[n/4])
    	elsif n%4 == 3
    		median = (sample[n/4])
      end
    	return median
    end

    def self.Q3 (sample)
    	sample = sample.sort
      n = sample.size

    	if n%4 == 0
    		median = (sample[n - n/4 -1] + sample[n - n/4])/2
    	elsif n%4 == 1
    		median = (sample[n - n/4 -1] + sample[n - n/4])/2
    	elsif n%4 == 2
    		median = (sample[n - n/4 -1])
    	elsif n%4 == 3
    		median = (sample[n - n/4 -1])
      end
      return median
    end

    def self.remove_outliers(sample)
      if(sample.size <= 2)
        return sample
      end

      q1 = self.Q1(sample)
      q3 = self.Q3(sample)

      irq = q3 - q1

      li = q1 - 1.5*irq
      ls = q3 + 1.5*irq

      sample = sample.sort
      p "=== Log === sample before outliers #{sample}"

      sample.delete_if {|s| s < li }
      sample.delete_if {|s| s > ls }

      p "=== Log === sample after outliers #{sample}"

      return sample
    end

  end
