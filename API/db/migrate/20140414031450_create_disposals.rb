class CreateDisposals < ActiveRecord::Migration
  def up
    create_table :disposals do |t|
      t.datetime :time, :null => false
      t.integer :bus_id, :references => Bus
      t.integer :line_id, :references => Line
      t.integer :loaded_file_id, :references => LoadedFile
      t.point :position, :geographic => true
      t.integer :last_postion_id, :references => BusPosition
      t.float :speed, :null => false
      t.string :disposal_reason, :null => false
      t.timestamps
    end
    add_index :disposals, :position
    add_index :disposals, :line_id
    add_index :disposals, :bus_id
    add_index :disposals, :time
  end

  def down
    drop_table :disposals
  end
end
