class CreateConfigs < ActiveRecord::Migration
  def up
    create_table :sys_configs do |t|
      t.float :positions_hm_range_1, default: -15
      t.float :positions_hm_range_2, default: -5
      t.float :positions_hm_range_3, default: 5
      t.float :positions_hm_range_4, default: 20

      t.float :speed_hm_diff_range_1, default: -20
      t.float :speed_hm_diff_range_2, default: -5
      t.float :speed_hm_diff_range_3, default: 5
      t.float :speed_hm_diff_range_4, default: 20

      t.float :speed_hm_query_range_1, default: 15
      t.float :speed_hm_query_range_2, default: 30
      t.float :speed_hm_query_range_3, default: 60

      t.float :bounding_box_gap, default: 0.001
      t.float :search_radius, default: 15

      t.timestamps
    end
    SysConfig.create!
  end

  def down
    drop_table :sys_configs
  end
end
