tasksController = function() { 
	
	function errorLogger(errorCode, errorMessage) {
		console.log(errorCode +':'+ errorMessage);
	}
	
	var taskPage;
	var initialised = false;

    /**
	 * makes json call to server to get task list.
	 * currently just testing this and writing return value out to console
	 * 111917kl
     */
	function retrieveTasksServer() {
        $.ajax("task", {
            "type": "get",
			dataType: "json"
        }).done(displayTasksServer.bind()); // need reference to the tasksController object
    }
	
	function retrieveTeamsServer() {
        $.ajax("team", {
            "type": "get",
			dataType: "json"
        }).done(displayTeamsServer.bind()); // need reference to the tasksController object
    }
	
    /**
	 * 111917kl
	 * callback for retrieveTasksServer
     * @param data
     */
    function displayTasksServer(data) { //this needs to be bound to the tasksController -- used bind in retrieveTasksServer 111917kl
    	console.log('tasks: ' + data);
        tasksController.loadServerTasks(data);
    }
    
    function displayTeamsServer(data) { //this needs to be bound to the tasksController -- used bind in retrieveTasksServer 111917kl
    	console.log('teams: ' + data);
        tasksController.loadServerTeams(data);
    }
	
	function taskCountChanged() {
		var count = $(taskPage).find( '#tblTasks tbody tr').length;
		$('footer').find('#taskCount').text(count);
	}
	
	function clearTask() {
		$(taskPage).find('form').fromObject({});
	}
	
	function renderTable() {
		$.each($(taskPage).find('#tblTasks tbody tr'), function(idx, row) {
			var due = Date.parse($(row).find('[datetime]').text());
			if (due.compareTo(Date.today()) < 0) {
				$(row).addClass("overdue");
			} else if (due.compareTo((2).days().fromNow()) <= 0) {
				$(row).addClass("warning");
			}
		});
	}
	
	return { 
		init : function(page, callback) { 
			if (initialised) {
				callback()
			} else {
				taskPage = page;
				storageEngine.init(function() {
					storageEngine.initObjectStore('task', function() {
						callback();
					}, errorLogger) 
				}, errorLogger);	 				
				$(taskPage).find('[required="required"]').prev('label').append( '<span>*</span>').children( 'span').addClass('required');
				$(taskPage).find('tbody tr:even').addClass('even');
				
				$(taskPage).find('#btnAddTask').click(function(evt) {
					evt.preventDefault();
					$(taskPage).find('#taskCreation').removeClass('not');
					clearTask();
				});
				
				$(taskPage).find('#btnAddTeam').click(function(evt) {
					evt.preventDefault();
					$(taskPage).find('#teamCreation').removeClass('not');
					clearTask();
				});

				// Sort task by priority or due date by clicking on table row header
                $(taskPage).find('#tblTasks thead > tr > th').click(function (evt) {
                    let sortBy = evt.target.id;
                    let sortFunction = sortMap[sortBy];
                    if (sortFunction != null) {
                        storageEngine.findAll('task', function(tasks) {
                            tasks.sort(sortFunction);
                            $(taskPage).find('#tblTasks tbody').empty();
                            $('#taskRow').tmpl(tasks).appendTo($(taskPage).find('#tblTasks tbody'));
                        }, errorLogger);
					}
                });
                
                var sortMap = {
                        'header-priority' : function (task1, task2) {
                            return task1.priority > task2.priority;
                        },
                        'header-due' : function (task1, task2) {
                            return Date.parse(task1.due).compareTo(Date.parse(task2.due));
                        },
                        'header-user' : function (task1, task2) {
                            return task1.userId < task2.userId;
                        }
                };

                /**	 * 11/19/17kl        */
                $(taskPage).find('#btnRetrieveTasks').click(function(evt) {
                    evt.preventDefault();
                    console.log('making ajax call - retrieve tasks');
                    retrieveTasksServer();
                });
                
                $(taskPage).find('#btnRetrieveTeams').click(function(evt) {
                    evt.preventDefault();
                    console.log('making ajax call - retrieve teams');
                    retrieveTeamsServer();
                });
				
				$(taskPage).find('#tblTasks tbody').on('click', 'tr', function(evt) {
					$(evt.target).closest('td').siblings().andSelf().toggleClass('rowHighlight');
				});	
				
				$(taskPage).find('#tblTasks tbody').on('click', '.deleteRow', 
					function(evt) { 		
						// delete from database
						$.ajax("task/delete", {
				        	contentType : "application/json",
				            "type": "get",
				            data: {"taskId": $(evt.target).data().taskId},
							dataType: "json"
				        }).done(displayTasksServer.bind()); 
						
						// delete from cache
						storageEngine.delete('task', $(evt.target).data().taskId, 
							function() {
								$(evt.target).parents('tr').remove(); 
								taskCountChanged();
							}, errorLogger);
					}
				);
				
				$(taskPage).find('#tblTeams tbody').on('click', '.deleteRow', 
						function(evt) { 		
							// delete from database
							$.ajax("team/delete", {
					        	contentType : "application/json",
					            "type": "post",
					            data: JSON.stringify({"teamId": $(this).attr('data-team-id')}),
								dataType: "json"
					        }).done(displayTeamsServer.bind()); 
						}
					);
				
				$(taskPage).find('#tblTasks tbody').on('click', '.editRow', 
					function(evt) { 
						$(taskPage).find('#taskCreation').removeClass('not');
						console.log($(evt.target).data().taskId);
						storageEngine.findById('task', $(evt.target).data().taskId, function(task) {
							console.log(task);
							$(taskPage).find('form').fromObject(task);
						}, errorLogger);
					}
				);
				
				$(taskPage).find('#tblTeams tbody').on('click', '.addRow', 
						function(evt) {
							$('#memberForm').find('#teamId').val($(this).attr('data-team-id'));
							$('#memberAdd').removeClass('not');
						}
					);
				
				$(taskPage).find('#btnAddMember').click(function(evt) {
					evt.preventDefault();
					if ($(taskPage).find('#memberForm').valid()) {
						evt.preventDefault();
						var teamId = $('#memberForm').find('#teamId').val();
						
						// save in database						
				        $.ajax("team/add", {
				        	contentType : "application/json",
				            "type": "post",
				            data: JSON.stringify({'teamId': teamId, 'username': $('#member').val()}),
							dataType: "json"
				        }).done(displayTeamsServer.bind());
						
				        $(taskPage).find('#memberAdd').addClass('not');
					}
				});
				
				$(taskPage).find('#clearTask').click(function(evt) {
					evt.preventDefault();
					clearTask();
				});
				
				$(taskPage).find('#tblTasks tbody').on('click', '.completeRow', function(evt) { 					
					storageEngine.findById('task', $(evt.target).data().taskId, function(task) {
						task.complete = true;
						evt.preventDefault();
						
						// update in cache
						storageEngine.save('task', task, function() {
							tasksController.loadTasks();
						},errorLogger);
						
						// save in database
						$.ajax("task/complete", {
				        	contentType : "application/json",
				            "type": "post",
				            data: JSON.stringify(task),
							dataType: "json"
				        }).done(displayTasksServer.bind()); 
					}, errorLogger);
				});
				
				$(taskPage).find('#saveTask').click(function(evt) {
					evt.preventDefault();
					if ($(taskPage).find('#taskForm').valid()) {
						var task = $(taskPage).find('#taskForm').toObject();
						evt.preventDefault();
						
						// save in database						
				        $.ajax("task/save", {
				        	contentType : "application/json",
				            "type": "post",
				            data: JSON.stringify(task),
							dataType: "json"
				        }).done(displayTasksServer.bind()); // need reference to the tasksController object
						
				        $(taskPage).find('#taskCreation').addClass('not');
					}
				});
				
				$(taskPage).find('#btnFilterTask').click(function(evt) {
					evt.preventDefault();
					if ($(taskPage).find('#taskForm').valid()) {
						var username = $('#filter-task').val();
						evt.preventDefault();
						
						// save in database						
				        $.ajax("task/filter", {
				        	contentType : "application/json",
				            "type": "post",
				            data: JSON.stringify({'username': username}),
							dataType: "json"
				        }).done(displayTasksServer.bind()); // need reference to the tasksController object
					}
				});
				
				$(taskPage).find('#saveTeam').click(function(evt) {
					evt.preventDefault();
					if ($(taskPage).find('#teamForm').valid()) {
						var teamName = $(taskPage).find('#teamName').val();
						evt.preventDefault();
						
						// save in database						
				        $.ajax("team/save", {
				        	contentType : "application/json",
				            "type": "post",
				            data: JSON.stringify({'teamName': teamName}),
							dataType: "json"
				        }).done(displayTeamsServer.bind()); 
						
				        $(taskPage).find('#teamCreation').addClass('not');
					}
				});
				
				initialised = true;
			}
		},
        /**
		 * 111917kl
		 * modification of the loadTasks method to load tasks retrieved from the server
         */
		loadServerTasks: function(tasks) {
			console.log('display tasks server');
			storageEngine.initializedObjectStores = {}; // clean cache
            $(taskPage).find('#tblTasks tbody').empty();
            $.each(tasks, function (index, task) {
                if (!task.complete) {
                    task.complete = false;
                }
                $('#taskRow').tmpl(task).appendTo($(taskPage).find('#tblTasks tbody'));
                taskCountChanged();
                console.log('about to render table with server tasks');
                
                // Re-initialize cache
                storageEngine.save('task', task, function(){}, errorLogger); 
                
                renderTable();
            });
		},
		
		loadServerTeams: function(teams) {
			console.log('display teams server');
            $(taskPage).find('#tblTeams tbody').empty();
            $.each(teams, function (index, team) {
                $('#teamRow').tmpl(team).appendTo($(taskPage).find('#tblTeams tbody'));
            });
		},
		
		
		loadTasks : function() {
			console.log('load tasks cache');
			storageEngine.initializedObjectStores = {}; // clean cache
			$(taskPage).find('#tblTasks tbody').empty();
			storageEngine.findAll('task', function(tasks) {
				tasks.sort(function(o1, o2) {
					return Date.parse(o1.due).compareTo(Date.parse(o2.due));
				});
				$.each(tasks, function(index, task) {
					if (!task.complete) {
						task.complete = false;
					}
					$('#taskRow').tmpl(task).appendTo($(taskPage).find('#tblTasks tbody'));
					taskCountChanged();
					renderTable();
				});
			}, errorLogger);
		} 
	} 
}();
