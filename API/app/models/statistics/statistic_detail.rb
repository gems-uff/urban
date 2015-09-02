#encoding: utf-8
class StatisticDetail

  attr_accessor :records

  TYPE_VALID_DATA = 0
  TYPE_DISPOSAL = 1

  TYPES = {
      TYPE_VALID_DATA => StatisticDetailValidData,
      TYPE_DISPOSAL => StatisticDetailValidData,
      nil => StatisticDetailNull,
      '' => StatisticDetailNull
  }

  def initialize(params)
    @records = TYPE[params[:type]].new(params)
  end
end
