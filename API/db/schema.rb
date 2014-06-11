# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended to check this file into your version control system.

ActiveRecord::Schema.define(:version => 20140611163549) do

  create_table "coletas", :force => true do |t|
    t.datetime "data_hora_inicio", :null => false
    t.datetime "data_hora_fim"
    t.integer  "status"
    t.string   "erros"
    t.string   "filename"
    t.datetime "created_at",       :null => false
    t.datetime "updated_at",       :null => false
  end

  create_table "dados_rj", :force => true do |t|
    t.datetime "data_hora",  :null => false
    t.integer  "ordem_id"
    t.integer  "linha_id"
    t.integer  "coleta_id"
    t.float    "latitude",   :null => false
    t.float    "longitude",  :null => false
    t.float    "velocidade", :null => false
    t.datetime "created_at", :null => false
    t.datetime "updated_at", :null => false
  end

  create_table "linhas", :force => true do |t|
    t.string   "linha"
    t.datetime "created_at", :null => false
    t.datetime "updated_at", :null => false
  end

  create_table "ordens", :force => true do |t|
    t.string   "ordem"
    t.datetime "created_at", :null => false
    t.datetime "updated_at", :null => false
  end

  create_table "descartes", :force => true do |t|
    t.datetime "data_hora",  :null => false
    t.integer  "ordem_id"
    t.integer  "linha_id"
    t.integer  "coleta_id"
    t.float    "latitude",   :null => false
    t.float    "longitude",  :null => false
    t.float    "velocidade", :null => false
    t.string   "motivo",     :null => false
    t.datetime "created_at", :null => false
    t.datetime "updated_at", :null => false
    t.index ["coleta_id"], :name => "fk__descartes_coleta_id"
    t.index ["linha_id"], :name => "fk__descartes_linha_id"
    t.index ["ordem_id"], :name => "fk__descartes_ordem_id"
    t.foreign_key ["coleta_id"], "coletas", ["id"], :on_update => :restrict, :on_delete => :restrict, :name => "fk_descartes_coleta_id"
    t.foreign_key ["linha_id"], "linhas", ["id"], :on_update => :restrict, :on_delete => :restrict, :name => "fk_descartes_linha_id"
    t.foreign_key ["ordem_id"], "ordens", ["id"], :on_update => :restrict, :on_delete => :restrict, :name => "fk_descartes_ordem_id"
  end

end
