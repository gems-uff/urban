class CreateBusPositions < ActiveRecord::Migration
  def up
    create_table :bus_positions do |t|
      t.datetime :time, :null => false
      t.integer :bus_id, :references => Bus
      t.integer :line_id, :references => Line
      t.integer :loaded_file_id, :references => LoadedFile
      #t.point :position, :geographic => true
      t.float :speed, :null => false
      t.timestamps
    end
    add_index :bus_positions, [:time, :bus_id], :unique => true
    #add_index :bus_positions, :position
    add_index :bus_positions, :line_id
    add_index :bus_positions, :bus_id
    add_index :bus_positions, :time
  end

  def down
    drop_table :bus_positions
  end
end
