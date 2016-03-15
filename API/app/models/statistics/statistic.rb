#encoding: utf-8
class Statistic

  attr_accessor :bus_positions, :disposals

  def initialize(params)
    @bus_positions = params[:bus_positions] || BusPosition.all.count
    @disposals = params[:disposals] || Disposal.select('disposal_reason_id, count(*)').group(:disposal_reason_id)
  end

end
