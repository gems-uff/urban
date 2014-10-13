class CreateLines < ActiveRecord::Migration
  def up
    create_table :lines do |t|
      t.string :line_number, :unique => true
      t.timestamps
    end
    add_index :lines, [:line_number], :unique => true
  end

  def down
    drop_table :lines
  end
end
