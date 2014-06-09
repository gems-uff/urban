class CreateLinhas < ActiveRecord::Migration
  def up
    create_table :linhas do |t|
      t.string :linha, :unique => true
      t.timestamps
    end
    add_index :linhas, [:linha], :unique => true
  end

  def down
    drop_table :linhas
  end
end
