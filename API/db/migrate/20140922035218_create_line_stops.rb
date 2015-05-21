class CreateLineStops < ActiveRecord::Migration
  def up
    create_table :line_stops do |t|
      t.integer :sequence_number
      t.integer :line_id, :references => Line
      t.integer :loaded_file_id, :references => LoadedFile
      t.string :description
      t.string :company
      #t.point :position, :geographic => true

      t.timestamps

    end
    #add_index :line_stops, :position
    add_index :line_stops, :line_id
  end

  def down
    drop_table :line_stops
  end
end
