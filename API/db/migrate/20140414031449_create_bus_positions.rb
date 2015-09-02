class CreateBusPositions < ActiveRecord::Migration
  def up
    create_table :bus_positions do |t|
      t.datetime :time, :null => false
      t.integer :bus_id, :references => Bus, :limit => 2
      t.integer :line_id, :references => Line, :limit => 2
      t.integer :loaded_file_id, :references => LoadedFile
      t.point :position, :geographic => true
      t.float :speed, :null => false
      t.timestamps
    end
    change_column :bus_positions, :id, :integer, :limit => 8
    add_index :bus_positions, [:time, :bus_id], :unique => true
    ActiveRecord::Base.connection.execute('CREATE INDEX index_bus_positions_on_position ON bus_positions USING GIST (position)')
    add_index :bus_positions, :line_id
    add_index :bus_positions, :bus_id
    add_index :bus_positions, :time
  end

  def down
    drop_table :bus_positions
  end
end
