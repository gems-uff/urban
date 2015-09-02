class CreateLineStops < ActiveRecord::Migration
  def up
    create_table :line_stops do |t|
      t.integer :sequence_number, :limit => 2
      t.integer :line_id, :references => Line, :limit => 2
      t.integer :loaded_file_id, :references => LoadedFile
      t.string :description
      t.string :company
      t.point :position, :geographic => true

      t.timestamps

    end
    ActiveRecord::Base.connection.execute('CREATE INDEX index_line_stops_on_position ON line_stops USING GIST (position)')
    add_index :line_stops, :line_id
  end

  def down
    drop_table :line_stops
  end
end
