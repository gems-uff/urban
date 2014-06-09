# Use this file to easily define all of your cron jobs.
#
# It's helpful, but not entirely necessary to understand cron before proceeding.
# http://en.wikipedia.org/wiki/Cron

# Example:
#
# set :output, "/path/to/my/cron_log.log"
#
# every 2.hours do
#   command "/usr/bin/some_great_command"
#   runner "MyModel.some_method"
#   rake "some:great:rake:task"
# end
#
# every 4.days do
#   runner "AnotherModel.prune_old_records"
# end

set :environment, "development"
set :output, "tmp/bla.log"

#every 5.minutes do
#  runner "Webservice.atualizar_dados_onibus_rj"
#  end
every 1.minute do
  runner "Webservice.download_json_linhas"
end



# Learn more: http://github.com/javan/whenever
