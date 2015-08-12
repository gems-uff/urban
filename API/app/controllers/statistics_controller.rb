class StatisticsController < ApplicationController

  def index

    @statistics = Statistic.new({})
    @data = []
    @data << ['Valid Data', @statistics.bus_positions]
    @statistics.disposals.each{|s| @data << [s.disposal_reason, s.count]}

  end

end
