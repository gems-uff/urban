class CreateDescartes < ActiveRecord::Migration
  def up
    create_table :descartes do |t|
      t.datetime :data_hora, :null => false
      t.integer :ordem_id, :references => Ordem
      t.integer :linha_id, :references => Linha
      t.integer :coleta_id, :references => Coleta
      t.float :latitude, :null => false
      t.float :longitude, :null => false
      t.float :velocidade, :null => false
      t.string :motivo, :null => false
      t.timestamps
    end
  end

  def down
    drop_table :descartes
  end
end
