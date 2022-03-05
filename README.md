# Doughnut

 ## -- Closed --

Java FX application to generate patterns made using Spirograph on a computer

## Features

- Application is 1000x800 pixels in size and cannot be resized (atleast 1080p monitor is required)
- Uses a 4000x4000 pixels canvas to make drawings
- Status will be error when colors strings are invalid or the radii are invalid or the drawing is bigger than the canvas
- Undo reverts the last change in the drawing (does not undo color changes)
- Saves the canvas as output.png

 ## Used

Modular application with modules and containers

### Design perspective

- Module is a fully functional object with exposed methods for interactions
- Container is an object that holds and links all the modules and possibly small containers within it
- Modules cannot interact with each other directly and are only connected through link methods of its host container
- Module contains all the data and methods of a functional unit and a Container groups and connects the modules that work together for simplicity in design and ease in development and maintenance
- Modules are analogous to Integrated Circuits and Containers are analogous to PCBs

### Programming perspective

- A container is a package with all the modules as individual class files along with other class files
- The container package contains the "Container" class that acts as the container object and modules are created as private objects inside it
- Public link methods are present in the Container class for connecting modules and to the outside world
- App container is the super container holding all the modules and possibly small containers
- "Main" file is present outside the app container that contains the main method and starts the app container