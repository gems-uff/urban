class Api::V1::StatisticsController < ApplicationController

  respond_to :json, :xml

  def index

    @statistics = Statistic.new({})
    @data = []
    @data << ['Valid Data', @statistics.bus_positions]
    @statistics.disposals.each { |s| @data << [s.disposal_reason, s.count] }

  end

  def details

  end

end
