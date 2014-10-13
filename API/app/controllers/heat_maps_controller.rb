class HeatMapsController < ApplicationController

  def position
    @lines = Line.select(:line_number).joins(:line_positions).group(:line_number).order(:line_number).map(&:line_number)
  end

  def speed
    @lines = Line.select(:line_number).joins(:line_positions).group(:line_number).order(:line_number).map(&:line_number)
  end

end
