# Flux
Android app that serves as a planner with Canvas integration. 
Using: 
- MVVM for the architecture 
- Volley for API calls 
- SQLite for the database
- Room for offline/local storage (abstraction layer over SQLite database)
- LiveData (lifecycle-aware data holder class to reactively display data to the user based on changes to the local database)
- Material Design guidelines followed


Contributors:
- Angelo Soliveras: [Mystoganzi](https://github.com/Mystoganzi)
- Cristian Martinez: [Haruhiism](https://github.com/Haruhiism)
- Claudio Osorio: [realstylishguy](https://github.com/realstylishguy)
- Jerry Laplante: [meruemV2](https://github.com/meruemV2)
- Nicholas Tarallico: [Nickt1596](https://github.com/Nickt1596)


## Dashboard
- [x] Display the user's day at glance
- [x] Addition of a new custom task via floating action button (bottom right corner)
- [x] Tapping a day on the calendar that has an indicator displays a recycler view with all the tasks/assignments due that day
- [x] A day is composed of both custom user created tasks, and assignments directly pulled from Canvas via API calls using Volley.
- [x] Data displayed on single recycler view, using two different lists (logic selects the item to display on the adapter based on the class type)

<img src="https://i.ibb.co/VxfkJwk/dashboard-display-day-at-glance.png" width="400" />

## Create New Task (via FAB)
- [x] Give the custom task a name, and description 
- [x] Add a new, select and existing, delete or edit: categories under which the task will be sorted by
- [x] Preserve the the content the user passed in via the UI (used ViewModel on the fragment)

<img src="https://i.ibb.co/S0cpndK/add-task-complete.png" width="400" />

- [x] Select an alert frequency for the task (to be reminded of the upcoming task 15 minutes, 1 hour before, 1 day before, etc)
<img src="https://i.ibb.co/JFWPdS1/add-task-alert-pick.png" width="400" />

- [x] Select date & time the task will be due on via Material date & time pickers
<img src="https://i.ibb.co/x7ZYnh1/add-task-date-pick.png" width="400" /> 
<img src="https://i.ibb.co/gDZ6Jhj/add-task-date-time-pick.png" width="400" />

- [x] Notifications for both user created tasks & Canvas assignments trigger based on the time the device is on:
<img src="https://i.ibb.co/grk5v7P/notificaiton-due.png" width="400" />
<img src="https://i.ibb.co/0trvm0K/notification-assignment-due.png" width="400" />

## Study
- [x] Implement Pomodoro style timer
- [x] Works as a study tool based on Pomodoro technique, 25 minutes focused on a task, 5 minute rest
<img src="https://i.ibb.co/xs4XXQ6/study-pomodoro-1.png" width="400" />
<img src="https://i.ibb.co/tLHX5Hy/study-pomodoro-2.png" width="400" />
<img src="https://i.ibb.co/KGbBW3S/study-pomodoro-3.png" width="400" />
<img src="https://i.ibb.co/2yVdfTr/study-pomodoro-4.png" width="400" />



## Settings
- [x] Implement dark mode funcitonality
- [x] User can put in their Canvas token to use the Canvas funcitonality of the app 
- [x] Choice for Canvas synch frequency 
<img src="https://i.ibb.co/4M1bJNd/settings-token-accepted-success-toast.png" width="400" />
<img src="https://i.ibb.co/kHcJhGg/settings-dark-mode-switch.png" width="400" />
<img src="https://i.ibb.co/9y92XXj/Screenshot-1637263904.png" width="400" />
