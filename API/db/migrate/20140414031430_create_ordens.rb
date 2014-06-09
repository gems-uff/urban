class CreateOrdens < ActiveRecord::Migration
  def up
    create_table :ordens do |t|
      t.string :ordem, :unique => true
      t.timestamps
    end
    add_index :ordens, [:ordem], :unique => true
  end

  def down
    drop_table :ordens
  end
end
