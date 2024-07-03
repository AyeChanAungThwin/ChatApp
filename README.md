# ChatApp (JFX)
## Abstract
> This is the test purpose JFX project that can directly runs as "Java Application" on Maven project.
> So, you need to set configuration if you wanna run it as JFX.

## How it looks like
<img src="images/chat-app.gif" alt="Person with 4 attributes, ER Diagram">

## About
- [X] Sockets in Java (Server/Client)
- [X] Multi-threading in Java (Standalone/ThreadPool)
- [X] Project Lombok (For boilerplate codes.)
- [X] Jackson (To convert object to json and vice versa.)
- [X] Encrypt/Decrypt String (Using Apache Commons Crypto library.)
- [X] Chain of responsibility (Refactored with COR design pattern.)

## Concerning your CPU Performance
```
	public ServerController() {
		new Thread(() -> {
			try {
				server = new Server(7777); 
				Thread.sleep(300); 
				//Codes
			} catch (Exception e) {
				//Codes
			}
		}).start();
	}
```
- Both ServerController and ClientController class, there is a sleep ```Thread``` because this thread ALWAYS runs faster than rendering UI through _Server.fxml_ (i.e., JFX had to read through the file.) and so, initialization of ```status``` field will not completed yet and thus, it will still be null. So, we need to wait for it to finish its initialization.
- So, according to your CPU's performance, ```NullPointerException``` can be prone. If it happens try adjusting the sleep time to 700 to 1000 ms.

## Electronics Engineer-cum-J2EE Backend Developer ##
-  Created by - Aye Chan Aung Thwin