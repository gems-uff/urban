#encoding: utf-8
class StatisticDetail

  attr_accessor :date, :count

  def initialize(params)
    @bus_positions = params[:bus_positions] || BusPosition.all.count
    @disposals = params[:disposals] || Disposal.select('disposal_reason, count(*)').group(:disposal_reason)
  end

end
