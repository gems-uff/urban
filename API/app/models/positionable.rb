#encoding: utf-8
module Positionable
  def latitude
    position_field.y
  end

  def longitude
    position_field.x
  end

  def position_field
    self.position
  end
end
