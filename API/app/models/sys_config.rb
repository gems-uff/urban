#encoding: utf-8
class SysConfig < ActiveRecord::Base

  validates :positions_hm_range_1, :positions_hm_range_2, :positions_hm_range_3, :positions_hm_range_4,
            :speed_hm_diff_range_1, :speed_hm_diff_range_2, :speed_hm_diff_range_3, :speed_hm_diff_range_4,
            :speed_hm_query_range_1, :speed_hm_query_range_2, :speed_hm_query_range_3,
            :bounding_box_gap, :search_radius, presence: true

  def self.config
    SysConfig.first
  end

  def positions_hm
    attributes.slice("positions_hm_range_1", "positions_hm_range_2", "positions_hm_range_3", "positions_hm_range_4")
  end

  def speed_hm_diff
    attributes.slice("speed_hm_diff_range_1", "speed_hm_diff_range_2", "speed_hm_diff_range_3", "speed_hm_diff_range_4")
  end

  def speed_hm_query
    attributes.slice("speed_hm_query_range_1", "speed_hm_query_range_2", "speed_hm_query_range_3")
  end
end
