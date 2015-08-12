#encoding: utf-8
class StatisticDetailDisposal

  attr_accessor :records

  def initialize(params)
    @records =
    @bus_positions = params[:bus_positions] || BusPosition.all.count
    @disposals = params[:disposals] || Disposal.select('disposal_reason, count(*)').group(:disposal_reason)
  end

end
