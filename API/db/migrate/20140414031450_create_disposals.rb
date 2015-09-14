class CreateDisposals < ActiveRecord::Migration
  def up
     create_table :disposals do |t|
       t.datetime :time, :null => false
       t.integer :bus_id, :references => Bus, :limit => 2
       t.integer :line_id, :references => Line, :limit => 2
       t.integer :loaded_file_id, :references => LoadedFile
       t.point :position, :geographic => true
       t.integer :last_postion_id, :references => BusPosition
       t.float :speed, :null => false
       t.integer :disposal_reason_id, :null => false, :limit => 2
       t.timestamps
     end
     ActiveRecord::Base.connection.execute('CREATE INDEX index_disposals_on_position ON disposals USING GIST (position)')
     add_index :disposals, :line_id
     add_index :disposals, :disposal_reason_id
     add_index :disposals, :bus_id
     add_index :disposals, :time
  end

  def down
     drop_table :disposals
  end
end
