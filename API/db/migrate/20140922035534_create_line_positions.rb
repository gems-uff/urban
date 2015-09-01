class CreateLinePositions < ActiveRecord::Migration
  def up
    create_table :line_positions do |t|
      t.integer :sequence_number, :limit => 2
      t.integer :line_id, :references => Line, :limit => 2
      t.string :description
      t.string :company
      t.integer :loaded_file_id, :references => LoadedFile
      t.point :position, :geographic => true
      t.integer :shape_id
      t.timestamps
    end
    ActiveRecord::Base.connection.execute('CREATE INDEX index_line_positions_on_position ON line_positions USING GIST (position)')
    add_index :line_positions, :line_id
  end

  def down
    drop_table :line_positions
  end
end
