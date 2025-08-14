this is spring core task-2 gym CRM system.

initially main will load 5 elements of each type into in memory java maps,
but only if path is correct. currently path is set correctly for my computer, so
in order for it to work:
            
            1)whole correct path of each *_Data.txt* or any external file where initial information
            is loaded form, must be provided in *resources/application.properties* .
            correct setup for application properties should look like this : 
                        
                        trainer.data.file=*full path to your file*
                        trainee.data.file=*full path to your file*
                        training.data.file=*full path to your file*
            
            2)Formatting must be correct for each file, otherwise it will throw 
            parsing errors and fail.
