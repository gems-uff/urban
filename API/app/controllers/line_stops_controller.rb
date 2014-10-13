class LineStopsController < ApplicationController

  def index
    @lines = Line.select(:line_number).joins(:line_stops).group(:line_number).map(&:line_number)
  end

end
